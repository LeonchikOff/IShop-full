package net.ishop.jdbc.sql_builders;

import net.framework.handlers.SQLBuilder;
import net.framework.handlers.SearchQuery;
import net.ishop.models.forms.SearchForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSearchFormSqlBuilder implements SQLBuilder {


    @Override
    public SearchQuery buildSearchQuery(Object... argsForQuery) {
        SearchForm searchForm = (SearchForm) argsForQuery[0];
        List<Object> params = new ArrayList<>();
        StringBuilder stringBuilderSQL = new StringBuilder();
        stringBuilderSQL
                .append("SELECT")
                .append(" ").append(getSelectFields())
                .append(" ").append("FROM product p, category ctr, producer pr")
                .append(" ").append("WHERE p.id_category = ctr.id AND p.id_producer = pr.id")
                .append(" ").append("AND (p.name ILIKE ? OR p.description ILIKE ?)");
        String searchFormQueryByNameAndDesc = "%" + searchForm.getQuery() + "%";
        params.add(searchFormQueryByNameAndDesc);
        params.add(searchFormQueryByNameAndDesc);
        populateSqlAndParams(stringBuilderSQL, params, searchForm.getIdsOfCategories(), "ctr.id = ?");
        populateSqlAndParams(stringBuilderSQL, params, searchForm.getIdsOfProducers(), "pr.id = ?");
        stringBuilderSQL.append(getLastSqlPart());
        params.addAll(Arrays.asList(argsForQuery).subList(1, argsForQuery.length));
        return new SearchQuery(stringBuilderSQL, params);
    }

    protected abstract String getSelectFields();

    protected static void populateSqlAndParams(StringBuilder stringBuilderSQL, List<Object> params,
                                               List<Integer> list, String expression) {
        if (list != null && !list.isEmpty()) {
            stringBuilderSQL.append(" AND (");
            for (int i = 0; i < list.size(); i++) {
                stringBuilderSQL.append(expression);
                params.add(list.get(i));
                if (i != list.size() - 1) {
                    stringBuilderSQL.append(" OR ");
                }
            }
            stringBuilderSQL.append(" )");
        }
    }

    protected String getLastSqlPart() {
        return "";
    }
}
