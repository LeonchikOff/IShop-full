package net.ishop.jdbc.repository;

import net.framework.annotations.jdbc.Select;
import net.framework.annotations.jdbc.mapping.CollectionItem;
import net.ishop.entities.Product;
import net.ishop.jdbc.repository.sql_builders.CountProductsSearchFormSqlBuilder;
import net.ishop.jdbc.repository.sql_builders.ListProductsSearchFormSqlBuilder;
import net.ishop.models.forms.SearchForm;

import java.util.List;

public interface ProductRepository {

    String sqlQuery1 = "SELECT p.*, ctr.name AS category, pr.name AS producer " +
            "FROM product p, category ctr, producer pr " +
            "WHERE p.id_category = ctr.id AND p.id_producer = pr.id " +
            "LIMIT ? OFFSET ? ";

    String sqlQuery2 = "SELECT p.*," +
            "       ctr.name AS category_name, " +
            "       pr.name AS producer_name " +
            "FROM product p, category ctr, producer pr " +
            "WHERE p.id_category = ctr.id " +
            "  and p.id_producer = pr.id " +
            "LIMIT ? OFFSET ?";

    @Select(sqlQuery = "SELECT p.*, ctr.name AS category, pr.name AS producer " +
            "FROM product p, category ctr, producer pr " +
            "WHERE p.id_category = ctr.id AND p.id_producer = pr.id AND p.id= ? ")
    Product findProductById(int productId);

    @Select(sqlQuery = sqlQuery2)
    @CollectionItem(parameterizedClass = Product.class)
    List<Product> getAllProductsList(int limit, int offSet);

    @Select(sqlQuery = "SELECT p.*, ctr.name AS category, pr.name AS producer " +
            "FROM product p, category ctr, producer pr " +
            "WHERE p.id_category = ctr.id AND p.id_producer = pr.id AND ctr.url = ? " +
            "ORDER BY p.id " +
            "LIMIT ? OFFSET ? ")
    @CollectionItem(parameterizedClass = Product.class)
    List<Product> getByCategoryProductsList(String categoryUrl, int limit, int offSet);

    @Select(sqlQuery = "", sqlBuilderQueryClass = ListProductsSearchFormSqlBuilder.class)
    @CollectionItem(parameterizedClass = Product.class)
    List<Product> getBySearchFormProductsList(SearchForm searchForm, int limit, int offSet);

    @Select(sqlQuery = "SELECT count(*) FROM product")
    int getCountAllProducts();

    @Select(sqlQuery = "SELECT count(p.*) FROM product p, category ctr WHERE p.id_category = ctr.id AND ctr.url = ? ")
    int getCountByCategoryProducts(String categoryUrl);

    @Select(sqlQuery = "", sqlBuilderQueryClass = CountProductsSearchFormSqlBuilder.class)
    @CollectionItem(parameterizedClass = Product.class)
    int getCountBySearchFormProducts(SearchForm searchForm);
}
