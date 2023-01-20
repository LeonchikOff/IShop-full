package net.framework;

import net.framework.annotations.dependency_injection.Autowired;
import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.dependency_injection.JDBCRepository;
import net.framework.annotations.dependency_injection.Value;
import net.framework.annotations.jdbc.Transactional;
import net.framework.exception.FrameworkSystemException;
import net.framework.utils.ReflectionUtils;
import net.framework.utils.jdbc.factory.JDBCRepositoryServiceFactory;
import net.framework.utils.jdbc.factory.JDBCTransactionalServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.*;

@SuppressWarnings("unchecked")
public class DependencyInjectionManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(DependencyInjectionManager.class);

    private final Map<Class<?>, Object> containerClassAndInstances = new HashMap<>();
    private final Properties applicationProperties;
    private final Map<Class<?>, Object> externalDependencies;

    public DependencyInjectionManager(Properties applicationProperties, Map<Class<?>, Object> externalDependencies) {
        this.applicationProperties = applicationProperties;
        this.externalDependencies = externalDependencies;
    }

    public <T> T getInstance(Class<T> classObject) {
        T instance = (T) containerClassAndInstances.get(classObject);
        Objects.requireNonNull(instance, "Instance not found for class: " + classObject);
        return instance;
    }

    public void destroyInstances() {
        Collection<Object> instancesCollection = containerClassAndInstances.values();
        instancesCollection.forEach(this::destroyInstances);
        containerClassAndInstances.clear();
    }

    protected void destroyInstances(Object instance) {
//        This approach requires that the class be implemented from Autocloseable interface
//        if(instance instanceof AutoCloseable) {
//            try {
//                ((AutoCloseable) instance).close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        Method[] declaredMethods = instance.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("close") && declaredMethod.getParameterCount() == 0) {
                LOGGER.info("Invoke close method from class {}", instance.getClass().getSimpleName());
                try {
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Invoke close method failed: " + e.getMessage(), e);
                }
            }
        }
    }

    public void addingClassesAndCorrespondingInstancesToContainerFromPackage(String packagePath) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urlEnumerationResources = contextClassLoader.getResources(packagePath.replace(".", "/"));
            List<Class<?>> classes = new ArrayList<>();
            while (urlEnumerationResources.hasMoreElements()) {
                URL urlResource = urlEnumerationResources.nextElement();
                String urlResources = urlResource.getFile();
                File fileDir = new File(urlResources);
                for (File file : Objects.requireNonNull(fileDir.listFiles())) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".class")) {
                        Class<?> classByName = Class.forName(packagePath.concat(".").concat(fileName.replace(".class", "")));
                        classes.add(classByName);
                    }
                }
            }
            for (Class<?> aClass : classes) {
                Object instance = null;
                if (aClass.isInterface()) {
                    if (aClass.getAnnotation(JDBCRepository.class) != null) {
                        instance = JDBCRepositoryServiceFactory.createRepositoryService(aClass);
                    }
                } else {
                    if (aClass.getAnnotation(Component.class) != null) {
                        try {
                            instance = aClass.newInstance();
                            boolean isExistsTransactionAnnotation = false;
                            if (aClass.getAnnotation(Transactional.class) != null) {
                                isExistsTransactionAnnotation = true;
                            } else {
                                for (Method declaredMethod : aClass.getDeclaredMethods()) {
                                    if (declaredMethod.getAnnotation(Transactional.class) != null) {
                                        isExistsTransactionAnnotation = true;
                                        break;
                                    }
                                }
                            }
                            if (isExistsTransactionAnnotation) {
                                DataSource dataSource = (DataSource) this.externalDependencies.get(DataSource.class);
                                Objects.requireNonNull(dataSource, "DateSource instance not found");
                                instance = JDBCTransactionalServiceFactory.createTransactionService(dataSource, instance);
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new FrameworkSystemException("Can't instantiate class: " + aClass +
                                    "! Does it have default constructor without parameter?", e);
                        }
                    }
                }
                if (instance != null) {
                    Class<?>[] interfaceClasses = aClass.isInterface() ? new Class[]{aClass} : aClass.getInterfaces();
                    for (Class<?> interfaceClass : interfaceClasses) {
                        containerClassAndInstances.put(interfaceClass, instance);

                        LOGGER.info("Added {}.class = {}", interfaceClass.getSimpleName(),
                                Proxy.isProxyClass(instance.getClass()) ? instance.toString() : instance.getClass().getSimpleName());
                    }

                }
            }

        } catch (ClassNotFoundException | IllegalArgumentException | IOException e) {
            throw new FrameworkSystemException("Can't load instances from package:" + packagePath, e);
        }
    }

    public void injectionOfDependenciesInInstances() {
        try {
            Set<Map.Entry<Class<?>, Object>> entrySetInstances = containerClassAndInstances.entrySet();
            for (Map.Entry<Class<?>, Object> instancePair : entrySetInstances) {
                Object instance = instancePair.getValue();
                if (!Proxy.isProxyClass(instance.getClass())) {
                    injectionOfDependenciesInInstances(instance);
                } else {
                    List<Field> fieldsOfProxyEntity = ReflectionUtils.getAccessibleFieldsOfEntity(instance.getClass());
                    for (Field invocationHandlerField : fieldsOfProxyEntity) {
                        Object invocationHandler = invocationHandlerField.get(instance);
                        if (invocationHandler.getClass() == JDBCTransactionalServiceFactory.TransactionalServiceInvocationHandler.class) {
                            Object realService =
                                    ((JDBCTransactionalServiceFactory.TransactionalServiceInvocationHandler) invocationHandler).getRealService();
                            injectionOfDependenciesInInstances(realService);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new FrameworkSystemException("Can't inject dependencies: " + e.getMessage(), e);
        }
    }

    private void injectionOfDependenciesInInstances(Object realService) throws IllegalAccessException {
        List<Field> fieldsOfRealService = ReflectionUtils.getAccessibleFieldsOfEntity(realService.getClass());
        for (Field field : fieldsOfRealService) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation != null) {
                Object dependency = containerClassAndInstances.get(field.getType());
                if (dependency == null) {
                    throw new FrameworkSystemException("Can't inject dependency: field=" + field +
                            " from class=" + field.getType());
                }
                field.set(realService, dependency);
                LOGGER.info("Dependency {}.{} injected by instance {}", field.getDeclaringClass().getSimpleName(), field.getName(),
                        Proxy.isProxyClass(realService.getClass()) ? realService.toString() : realService.getClass().getSimpleName());
            }
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String keyForProperty = valueAnnotation.value();
                String propertyValue = applicationProperties.getProperty(keyForProperty);
                if (propertyValue == null) {
                    throw new FrameworkSystemException("Property with value: " + valueAnnotation.value() + " not found");
                }
                boolean isSystemProperty = propertyValue.startsWith("${sysEnv.");
                if (isSystemProperty) {
                    String keyForSystemProperty = propertyValue.replace("${sysEnv.", "").replace("}", "");
                    String systemPropertyValue = System.getProperty(keyForSystemProperty);
                    if (systemPropertyValue != null) {
                        propertyValue = systemPropertyValue;
                    } else {
                        throw new FrameworkSystemException("System property " + keyForSystemProperty + " not found");
                    }
                }
                field.set(realService, propertyValue);
                String loggerPropValue = propertyValue.startsWith("${sysEnv.") ? "${" + keyForProperty + "}" : propertyValue;
                LOGGER.info("Value {}.{} injected by property {}", field.getDeclaringClass().getSimpleName(),
                        field.getName(), loggerPropValue);

            }
        }
    }


}
