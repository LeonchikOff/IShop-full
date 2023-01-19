package net.framework.annotations.jdbc.mapping;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JoinTable {
    String columnName();
    String nameOfFieldBeJoined() default "id";
}
