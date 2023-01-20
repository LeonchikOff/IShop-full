package net.framework.annotations.dependency_injection;

import java.lang.annotation.*;

//Injection class dependency by initializing fields of class
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
