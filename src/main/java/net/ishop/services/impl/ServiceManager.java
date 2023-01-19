package net.ishop.services.impl;

import net.framework.utils.jdbc.factory.JDBCRepositoryServiceFactory;
import net.framework.utils.jdbc.factory.JDBCTransactionalServiceFactory;
import net.ishop.jdbc.repository.AccountRepository;
import net.ishop.jdbc.repository.CategoryRepository;
import net.ishop.jdbc.repository.OrderItemRepository;
import net.ishop.jdbc.repository.OrderRepository;
import net.ishop.jdbc.repository.ProducerRepository;
import net.ishop.jdbc.repository.ProductRepository;
import net.ishop.services.OrderService;
import net.ishop.services.ProductService;
import net.ishop.services.SocialService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

//Singleton
public class ServiceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);

    //create and get singleton ServiceManager
    public static ServiceManager getInstance(ServletContext servletContext) {
        ServiceManager serviceManagerInst = (ServiceManager) servletContext.getAttribute("SERVICE_MANAGER");
        if (serviceManagerInst == null) {
            serviceManagerInst = new ServiceManager(servletContext);
            servletContext.setAttribute("SERVICE_MANAGER", serviceManagerInst);

        }
        return serviceManagerInst;
    }

    private final Properties applicationProperties = new Properties();
    private final BasicDataSource dataSource;

    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AccountRepository accountRepository;

    private final ProductService productService;
    private final OrderService orderService;
    private final SocialService socialService;


    private ServiceManager(ServletContext context) {
        loadApplicationProperties();
        dataSource = createDataSource();

        productRepository = JDBCRepositoryServiceFactory.createRepositoryService(ProductRepository.class);
        producerRepository = JDBCRepositoryServiceFactory.createRepositoryService(ProducerRepository.class);
        categoryRepository = JDBCRepositoryServiceFactory.createRepositoryService(CategoryRepository.class);
        orderRepository = JDBCRepositoryServiceFactory.createRepositoryService(OrderRepository.class);
        orderItemRepository = JDBCRepositoryServiceFactory.createRepositoryService(OrderItemRepository.class);
        accountRepository = JDBCRepositoryServiceFactory.createRepositoryService(AccountRepository.class);

        productService = (ProductService) JDBCTransactionalServiceFactory.createTransactionService(dataSource, new ProductServiceImpl(this));
        orderService = (OrderService) JDBCTransactionalServiceFactory.createTransactionService(dataSource, new OrderServiceImpl(this));
        socialService = new FacebookSocialServiceImpl(this);

    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }
    public ProducerRepository getProducerRepository() {
        return producerRepository;
    }
    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
    public OrderItemRepository getOrderItemRepository() {
        return orderItemRepository;
    }
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public ProductService getProductService() {
        return productService;
    }
    public OrderService getOrderService() {
        return orderService;
    }
    public SocialService getSocialService() {
        return socialService;
    }

    private BasicDataSource createDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDefaultAutoCommit(true);
        basicDataSource.setRollbackOnReturn(true);
        basicDataSource.setDriverClassName(getApplicationProperty("db.driver"));
        basicDataSource.setUrl(getApplicationProperty("db.url"));
        basicDataSource.setUsername(getApplicationProperty("db.username"));
        basicDataSource.setPassword(getApplicationProperty("db.password"));
        basicDataSource.setInitialSize(Integer.parseInt(getApplicationProperty("db.pool.initSize")));
        basicDataSource.setMaxTotal(Integer.parseInt(getApplicationProperty("db.pool.maxSize")));
        return basicDataSource;
    }

    private void loadApplicationProperties() {
            try (InputStream resourceAsStream = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")) {
                applicationProperties.load(resourceAsStream);
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
    }

    public String getApplicationProperty(String key) {
        String propertyValue = applicationProperties.getProperty(key);
        if(propertyValue.startsWith("${sysEnv")) {
            String keySystem = propertyValue.replace("${sysEnv.", "").replace("}", "");
            System.out.println(keySystem);
            propertyValue = System.getProperty(keySystem);
            System.out.println(propertyValue);
        }
        return propertyValue;
    }


    public void close() {
        //method sort of close connection DB
        try {
            dataSource.close();
        } catch (SQLException sqlException) {
            LOGGER.error("Close data source failed: " + sqlException.getMessage(), sqlException);
        }
    }
}
