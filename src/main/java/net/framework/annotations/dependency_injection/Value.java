package net.framework.annotations.dependency_injection;

import java.lang.annotation.*;

//Initializing the field class by reading the parameter value from the properties file
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    String value();
}
