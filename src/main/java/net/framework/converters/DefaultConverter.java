package net.framework.converters;

import net.framework.exception.FrameworkSystemException;

@SuppressWarnings(value = "unchecked")
public class DefaultConverter implements Converter {
    @Override
    public <T> T convert(Class<T> targetEntityClass, Object initialValue) {
        if (initialValue == null) {
            return null;
        } else if (targetEntityClass == Object.class || targetEntityClass == initialValue.getClass()) {
            return (T) initialValue;
        } else if (targetEntityClass == String.class) {
            return (T) initialValue.toString();
        } else {
            throw new FrameworkSystemException("Can't convert class " + initialValue.getClass() + " to class " + targetEntityClass);
        }
    }
}
