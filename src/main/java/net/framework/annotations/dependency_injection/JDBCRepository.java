package net.framework.annotations.dependency_injection;

import java.lang.annotation.*;

// Marker Annotation for the interface, which is a dynamic JDBC repository,
// for automatically creating an instance of class, implementing this interface using a proxy
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JDBCRepository {
}
