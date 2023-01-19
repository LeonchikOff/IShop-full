package net.ishop.services;

import net.framework.annotations.jdbc.Transactional;
import net.ishop.entities.Category;
import net.ishop.entities.Producer;
import net.ishop.entities.Product;
import net.ishop.models.forms.SearchForm;

import java.util.List;

public interface ProductService {

    List<Category> getAllCategoriesList();
    List<Producer> getAllProducersList();

    List<Product> getAllProductsList(int numberOfPage, int limit);
    List<Product> getByCategoryProductsList(String categoryUrl, int numberOfPage, int limit);
    List<Product> getBySearchFormProductsList(SearchForm searchForm, int numberOfPage, int limit);

    int getCountAllProducts();
    int getCountByCategoryProducts(String categoryUrl);
    int getCountBySearchFormProducts(SearchForm searchForm);
}
