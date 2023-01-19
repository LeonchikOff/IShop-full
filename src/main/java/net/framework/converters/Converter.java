package net.framework.converters;

public interface Converter {
    <T> T convert(Class<T> entityClass, Object value);
}
