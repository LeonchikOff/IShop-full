package net.ishop.services.impl;

import net.framework.annotations.dependency_injection.Autowired;
import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.jdbc.Transactional;
import net.ishop.entities.Category;
import net.ishop.entities.Producer;
import net.ishop.entities.Product;
import net.ishop.jdbc.repository.CategoryRepository;
import net.ishop.jdbc.repository.ProducerRepository;
import net.ishop.jdbc.repository.ProductRepository;
import net.ishop.models.forms.SearchForm;
import net.ishop.services.interfaces.ProductService;

import java.util.List;

@Component
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductServiceImpl() {
    }

    @Override
    public List<Category> getAllCategoriesList() {
        return categoryRepository.getAllCategoriesList();
    }

    @Override
    public List<Producer> getAllProducersList() {
        return producerRepository.getAllProducersList();
    }

    @Override
    public List<Product> getAllProductsList(int numberOfPage, int limit) {
        int offSet = (numberOfPage - 1) * limit;
        return productRepository.getAllProductsList(limit, offSet);
    }

    @Override
    public List<Product> getByCategoryProductsList(String categoryUrl, int numberOfPage, int limit) {
        int offSet = (numberOfPage - 1) * limit;
        return productRepository.getByCategoryProductsList(categoryUrl, limit, offSet);
    }

    @Override
    public List<Product> getBySearchFormProductsList(SearchForm searchForm, int numberOfPage, int limit) {
        int offSet = (numberOfPage - 1) * limit;
        return productRepository.getBySearchFormProductsList(searchForm, limit, offSet);
    }

    @Override
    public int getCountAllProducts() {
        return productRepository.getCountAllProducts();
    }

    @Override
    public int getCountByCategoryProducts(String categoryUrl) {
        return productRepository.getCountByCategoryProducts(categoryUrl);
    }

    @Override
    public int getCountBySearchFormProducts(SearchForm searchForm) {
        return productRepository.getCountBySearchFormProducts(searchForm);
    }
}
