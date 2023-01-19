package net.framework.utils;

import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Transient;
import net.framework.exception.FrameworkSystemException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {
    public static List<Field> getAccessibleFieldsOfEntity(Class<?> entityClass) {
        List<Field> fields = new ArrayList<>();
        while (entityClass != Object.class) {
            Field[] entityClassDeclaredFields = entityClass.getDeclaredFields();
            for (Field entityClassDeclaredField : entityClassDeclaredFields) {
                if(shouldBeFieldIncluded(entityClassDeclaredField)) {
                    entityClassDeclaredField.setAccessible(true);
                    fields.add(entityClassDeclaredField);
                }
            }
            entityClass = entityClass.getSuperclass();
        }
        return fields;
    }

    private static boolean shouldBeFieldIncluded(Field field) {
        int modifiers = field.getModifiers();
        return (modifiers & (Modifier.STATIC | Modifier.FINAL)) == 0 && (field.getAnnotation(Transient.class) == null);
    }

    public static Field findFieldByName(Class<?> fieldClass, List<Field> fieldList, String fieldName) {
        for (Field field : fieldList) {
            if(field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new FrameworkSystemException("Can't find field " + fieldName + " in the class " + fieldClass);
    }

    public static String getColumnNameForField(Field field) {
        Column annotation = field.getAnnotation(Column.class);
        return annotation == null ? field.getName() : annotation.columnName();
    }

    public static Method findMethod(Method method, Class<?> classInstance) {
        Method[] declaredMethods = classInstance.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.getName().equals(method.getName()) &&
                    Arrays.equals(declaredMethod.getParameterTypes(), method.getParameterTypes())) {
                return declaredMethod;
            }
        }
        throw new FrameworkSystemException("Can't find method " + method + " in the classInstance " + classInstance);
    }

    public static <T extends Annotation> T findConfigAnnotation(Method method, Class<T> desiredAnnotationClass) {
        T annotation = method.getAnnotation(desiredAnnotationClass);
        if(annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(desiredAnnotationClass);
        }
        return annotation;
    }
}
