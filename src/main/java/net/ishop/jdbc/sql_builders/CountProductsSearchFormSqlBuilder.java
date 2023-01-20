package net.ishop.jdbc.sql_builders;

public class CountProductsSearchFormSqlBuilder extends AbstractSearchFormSqlBuilder {
    @Override
    protected String getSelectFields() {
        return "count(*)";
    }
}
