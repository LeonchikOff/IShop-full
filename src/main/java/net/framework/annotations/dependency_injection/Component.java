package net.framework.annotations.dependency_injection;

import java.lang.annotation.*;

//Automatic creation of a class instantiation in a container
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
}
