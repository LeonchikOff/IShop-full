package net.framework.annotations.jdbc.mapping;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String nameOfTable();
    String nameOfFieldWithId()  default "id";
    String generationNextIdExpresion() default "";
}
