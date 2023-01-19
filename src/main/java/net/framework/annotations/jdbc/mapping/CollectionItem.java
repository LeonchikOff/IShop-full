package net.framework.annotations.jdbc.mapping;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CollectionItem {
    Class<?> parameterizedClass();
}
