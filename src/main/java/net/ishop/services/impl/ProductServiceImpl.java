package net.ishop.services.impl;

import net.ishop.entities.Category;
import net.ishop.entities.Producer;
import net.ishop.entities.Product;
import net.ishop.exceptions.InternalServerErrorException;
import net.ishop.jdbc.JDBCUtils;
import net.ishop.jdbc.ResultSetHandler;
import net.ishop.jdbc.ResultSetHandlerFactory;
import net.ishop.jdbc.SearchQuery;
import net.ishop.models.forms.SearchForm;
import net.ishop.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ProductServiceImpl implements ProductService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static final ResultSetHandler<List<Product>> productsResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);
    private static final ResultSetHandler<List<Category>> categoriesResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.CATEGORY_RESULT_SET_HANDLER);
    private static final ResultSetHandler<List<Producer>> producersResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCER_RESULT_SET_HANDLER);
    private static final ResultSetHandler<Integer> countProductsResultSetHandler =
            ResultSetHandlerFactory.getCountResultSetHandler();

    private final DataSource dataSource;

    ProductServiceImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> getAllCategoriesList() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM category ORDER BY category.name";
            return JDBCUtils.getDataOnSelect(connection, sql, categoriesResultSetHandler);
        } catch (SQLException checkedSqlException) {
            // I'll catch it in the web container - in the error filter
            throw new InternalServerErrorException("Can't execute sql query: " + checkedSqlException.getMessage(), checkedSqlException);
        }
    }

    @Override
    public List<Producer> getAllProducersList() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM producer ORDER BY producer.name";
            return JDBCUtils.getDataOnSelect(connection, sql, producersResultSetHandler);
        } catch (SQLException checkedSqlException) {
            // I'll catch it in the web container - in the error filter
            throw new InternalServerErrorException("Can't execute sql query: " + checkedSqlException.getMessage(), checkedSqlException);
        }
    }

    @Override
    public List<Product> getAllProductsList(int numberOfPage, int limit) {
        try (Connection connection = dataSource.getConnection()) {
            //сдвиг по базе данных
            int offSet = (numberOfPage - 1) * limit;
            String sql = "SELECT p.*, pr.name AS producer, ctr.name AS category " +
                    "FROM product p, producer pr, category ctr " +
                    "WHERE p.id_category = ctr.id AND  p.id_producer = pr.id " +
                    "LIMIT ? OFFSET ?";
            return JDBCUtils.getDataOnSelect(connection, sql, productsResultSetHandler, limit, offSet);
        } catch (SQLException checkedSqlException) {
            // I'll catch it in the web container - in the error filter
            throw new InternalServerErrorException("Can't execute sql query: " + checkedSqlException.getMessage(), checkedSqlException);
        }
    }

    @Override
    public List<Product> getByCategoryProductsList(String categoryUrl, int numberOfPage, int limit) {
        try (Connection connection = dataSource.getConnection()) {
            int offSet = (numberOfPage - 1) * limit;
            String sgl = "SELECT p.*, ctr.name AS category, pr.name AS producer " +
                    "FROM product p, category ctr, producer pr " +
                    "WHERE ctr.url = ? " +
                    "AND p.id_producer = pr.id " +
                    "AND p.id_category = ctr.id " +
                    "ORDER BY p.id " +
                    "LIMIT ? OFFSET ? ";
            return JDBCUtils.getDataOnSelect(connection, sgl, productsResultSetHandler, categoryUrl, limit, offSet);
        } catch (SQLException checkedSqlException) {
            // I'll catch it in the web container - in the error filter
            throw new InternalServerErrorException("Can't execute sql query: " + checkedSqlException.getMessage(), checkedSqlException);
        }
    }

    @Override
    public List<Product> getBySearchFormProductsList(SearchForm searchForm, int numberOfPage, int limit) {
        try (Connection connection = dataSource.getConnection()) {
            int offSet = (numberOfPage - 1) * limit;
            String selectFields = "p.*, ctr.name AS category, pr.name AS producer";
            SearchQuery searchQuery = buildSearchQuery(selectFields, searchForm);
            searchQuery.getStringBuilderSQL().append(" ORDER BY p.id LIMIT ? OFFSET ?");
            searchQuery.getParams().add(limit);
            searchQuery.getParams().add(offSet);
            LOGGER.debug("search query={} with params={}", searchQuery.getStringBuilderSQL(), searchQuery.getParams());
            return JDBCUtils.getDataOnSelect(
                    connection,
                    searchQuery.getStringBuilderSQL().toString(),
                    productsResultSetHandler,
                    searchQuery.getParams().toArray());
        } catch (SQLException checkedSqlException) {
            // I'll catch it in the web container - in the error filter
            throw new InternalServerErrorException("Can't execute sql query: " + checkedSqlException.getMessage(), checkedSqlException);
        }
    }

    protected SearchQuery buildSearchQuery(String selectFields, SearchForm searchForm) {
        List<Object> params = new ArrayList<>();
        StringBuilder stringBuilderSQL = new StringBuilder();
        stringBuilderSQL
                .append("SELECT")
                .append(" ").append(selectFields)
                .append(" ").append("FROM product p, category ctr, producer pr")
                .append(" ").append("WHERE p.id_category = ctr.id AND p.id_producer = pr.id")
                .append(" ").append("AND (p.name ILIKE ? OR description ILIKE ?)");
        String searchFormQueryByNameAndDesc = "%" + searchForm.getQuery() + "%";
        params.add(searchFormQueryByNameAndDesc);
        params.add(searchFormQueryByNameAndDesc);
        JDBCUtils.populateSqlAndParams(stringBuilderSQL, params, searchForm.getIdsOfCategories(), "ctr.id = ?");
        JDBCUtils.populateSqlAndParams(stringBuilderSQL, params, searchForm.getIdsOfProducers(), "pr.id = ?");
        return new SearchQuery(stringBuilderSQL, params);
    }

    @Override
    public int getCountAllProducts() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT count(*) FROM product";
            return JDBCUtils.getDataOnSelect(connection, sql, countProductsResultSetHandler);
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute sql query: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public int getCountByCategoryProducts(String categoryUrl) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT count(p.*) FROM product p, category ctr WHERE p.id_category = ctr.id AND ctr.url=?";
            return JDBCUtils.getDataOnSelect(connection, sql, countProductsResultSetHandler, categoryUrl);
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute sql query: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public int getCountBySearchFormProducts(SearchForm searchForm) {
        try (Connection connection = dataSource.getConnection()) {
            SearchQuery searchQuery = buildSearchQuery("count(*)", searchForm);
            LOGGER.debug("search query={} with params={}", searchQuery.getStringBuilderSQL(), searchQuery.getParams());
            return JDBCUtils.getDataOnSelect(
                    connection,
                    searchQuery.getStringBuilderSQL().toString(),
                    countProductsResultSetHandler,
                    searchQuery.getParams().toArray()
            );
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute sql query: " + sqlException.getMessage(), sqlException);
        }
    }
}
