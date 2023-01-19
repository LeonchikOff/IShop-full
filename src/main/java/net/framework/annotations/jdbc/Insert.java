package net.framework.annotations.jdbc;

import net.framework.handlers.DefaultImplResultSetHandlerForOneRow;
import net.framework.handlers.ResultSetHandler;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Insert {
    Class<? extends ResultSetHandler> resultSetHandlerClass() default DefaultImplResultSetHandlerForOneRow.class;
}
