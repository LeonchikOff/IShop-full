package net.ishop.jdbc.repository;

import net.framework.annotations.jdbc.Select;
import net.framework.annotations.jdbc.mapping.CollectionItem;
import net.ishop.entities.Category;

import java.util.List;

public interface CategoryRepository {
    String QUERY_ALL_CATEGORIES_LIST = "SELECT * FROM category ORDER BY category.name";

    @Select(sqlQuery = QUERY_ALL_CATEGORIES_LIST)
    @CollectionItem(parameterizedClass = Category.class)
    List<Category> getAllCategoriesList();
}
