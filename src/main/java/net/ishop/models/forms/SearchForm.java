package net.ishop.models.forms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchForm {
    private String query;
    private List<Integer> idsOfCategories;
    private List<Integer> idsOfProducers;

    public SearchForm(String query, String[] arrayOfIdsOfCategories, String[] arrayOfIdsOfProducers) {
        this.query = query;
        this.idsOfCategories = convert(arrayOfIdsOfCategories);
        this.idsOfProducers = convert(arrayOfIdsOfProducers);
    }

    private List<Integer> convert(String[] args) {
        if (args == null) {
            return Collections.emptyList();
        } else {
            List<Integer> integerList = new ArrayList<>(args.length);
            for (String arg : args) {
                integerList.add(Integer.parseInt(arg));
            }
            return integerList;
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getIdsOfCategories() {
        return idsOfCategories;
    }

    public void setIdsOfCategories(List<Integer> idsOfCategories) {
        this.idsOfCategories = idsOfCategories;
    }

    public List<Integer> getIdsOfProducers() {
        return idsOfProducers;
    }

    public void setIdsOfProducers(List<Integer> idsOfProducers) {
        this.idsOfProducers = idsOfProducers;
    }

    public boolean isCategoriesNotEmpty() {
        return !this.idsOfCategories.isEmpty();
    }

    public boolean isProducersNotEmpty() {
        return !this.idsOfProducers.isEmpty();
    }
}
