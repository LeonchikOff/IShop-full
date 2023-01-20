package net.ishop.jdbc.sql_builders;

public class ListProductsSearchFormSqlBuilder extends AbstractSearchFormSqlBuilder {
    @Override
    protected String getSelectFields() {
        return " p.*, ctr.name AS category, pr.name AS producer ";
    }

    @Override
    protected String getLastSqlPart() {
        return " ORDER BY p.id LIMIT ? OFFSET ? ";
    }
}
