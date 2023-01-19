package net.framework.annotations.jdbc;

import net.framework.handlers.DefaultImplResultSetHandlerForOneRow;
import net.framework.handlers.ResultSetHandler;
import net.framework.handlers.SQLBuilder;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Select {
    String sqlQuery();
    Class<? extends SQLBuilder> sqlBuilderQueryClass() default SQLBuilder.class;
    Class<? extends ResultSetHandler> resultSetHandlerClass() default DefaultImplResultSetHandlerForOneRow.class;
}
