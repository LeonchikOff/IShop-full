package net.framework.handlers;

import java.util.List;

public class SearchQuery {
    private StringBuilder stringBuilderSQL;
    private List<Object> params;

    public SearchQuery(StringBuilder stringBuilderSQL, List<Object> params) {
        this.stringBuilderSQL = stringBuilderSQL;
        this.params = params;
    }

    public SearchQuery() {
    }

    public StringBuilder getStringBuilderSQL() {
        return stringBuilderSQL;
    }

    public void setStringBuilderSQL(StringBuilder stringBuilderSQL) {
        this.stringBuilderSQL = stringBuilderSQL;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
