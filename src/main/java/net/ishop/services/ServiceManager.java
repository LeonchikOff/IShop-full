package net.ishop.services;

import net.framework.DependencyInjectionManager;
import net.framework.DependencyInjectionManagerAdvanced;
import net.ishop.services.interfaces.OrderService;
import net.ishop.services.interfaces.ProductService;
import net.ishop.services.interfaces.SocialService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//Singleton
public class ServiceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);

    //create and get singleton ServiceManager
    public static ServiceManager getInstance(ServletContext servletContext) {
        ServiceManager serviceManagerInst = (ServiceManager) servletContext.getAttribute("SERVICE_MANAGER");
        if (serviceManagerInst == null) {
            serviceManagerInst = new ServiceManager(servletContext);
            servletContext.setAttribute("SERVICE_MANAGER", serviceManagerInst);

        }
        return serviceManagerInst;
    }

    private final Properties applicationProperties = new Properties();
    private final BasicDataSource dataSource;
    private final DependencyInjectionManager dependencyInjectionManager;

    private ServiceManager(ServletContext context) {
        loadApplicationProperties();
        dataSource = createDataSource();
        Map<Class<?>, Object> externalDependencies = new HashMap<>();
        externalDependencies.put(DataSource.class, dataSource);
        dependencyInjectionManager = new DependencyInjectionManagerAdvanced(applicationProperties, externalDependencies);
        dependencyInjectionManager.addingClassesAndCorrespondingInstancesToContainerFromPackage("net.ishop.jdbc.repository");
        dependencyInjectionManager.addingClassesAndCorrespondingInstancesToContainerFromPackage("net.ishop.services.impl");
        dependencyInjectionManager.injectionOfDependenciesInInstances();
    }

    public ProductService getProductService() {
        return dependencyInjectionManager.getInstance(ProductService.class);
    }

    public OrderService getOrderService() {
        return dependencyInjectionManager.getInstance(OrderService.class);
    }

    public SocialService getSocialService() {
        return dependencyInjectionManager.getInstance(SocialService.class);
    }

    private BasicDataSource createDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDefaultAutoCommit(false);
        basicDataSource.setRollbackOnReturn(true);
        basicDataSource.setDriverClassName(getApplicationProperty("db.driver"));
        basicDataSource.setUrl(getApplicationProperty("db.url"));
        basicDataSource.setUsername(getApplicationProperty("db.username"));
        basicDataSource.setPassword(getApplicationProperty("db.password"));
        basicDataSource.setInitialSize(Integer.parseInt(getApplicationProperty("db.pool.initSize")));
        basicDataSource.setMaxTotal(Integer.parseInt(getApplicationProperty("db.pool.maxSize")));
        return basicDataSource;
    }

    private void loadApplicationProperties() {
        try (InputStream resourceAsStream = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            applicationProperties.load(resourceAsStream);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public String getApplicationProperty(String key) {
        String propertyValue = applicationProperties.getProperty(key);
        if (propertyValue.startsWith("${sysEnv")) {
            String keySystem = propertyValue.replace("${sysEnv.", "").replace("}", "");
            System.out.println(keySystem);
            propertyValue = System.getProperty(keySystem);
            System.out.println(propertyValue);
        }
        return propertyValue;
    }


    public void close() {
        //method sort of close connection DB
        try {
            dataSource.close();
        } catch (SQLException sqlException) {
            LOGGER.error("Close data source failed: " + sqlException.getMessage(), sqlException);
        }

        dependencyInjectionManager.destroyInstances();
    }
}
