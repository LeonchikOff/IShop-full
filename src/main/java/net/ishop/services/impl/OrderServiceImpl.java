package net.ishop.services.impl;

import net.framework.annotations.jdbc.Transactional;
import net.ishop.entities.Account;
import net.ishop.entities.Order;
import net.ishop.entities.OrderItem;
import net.ishop.entities.Product;
import net.ishop.exceptions.AccessDeniedException;
import net.ishop.exceptions.InternalServerErrorException;
import net.ishop.exceptions.ResourceNotFoundException;
import net.ishop.jdbc.repository.AccountRepository;
import net.ishop.jdbc.repository.OrderItemRepository;
import net.ishop.jdbc.repository.OrderRepository;
import net.ishop.jdbc.repository.ProductRepository;
import net.ishop.models.ShoppingCart;
import net.ishop.models.forms.ProductForm;
import net.ishop.models.social.CurrentAccount;
import net.ishop.models.social.SocialAccount;
import net.ishop.services.OrderService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final String rootDir;

    private final String smtpHost;
    private final String smtpPort;
    private final String smtpUserName;
    private final String smtpPassword;
    private final String host;
    private final String fromAddress;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AccountRepository accountRepository;

    public OrderServiceImpl(ServiceManager serviceManager) {
        this.rootDir = serviceManager.getApplicationProperty("app.avatar.root.dir");
        this.smtpHost = serviceManager.getApplicationProperty("email.smtp.server");
        this.smtpPort = serviceManager.getApplicationProperty("email.smtp.port");
        this.smtpUserName = serviceManager.getApplicationProperty("email.smtp.username");
        this.smtpPassword = serviceManager.getApplicationProperty("email.smtp.password");
        this.host = serviceManager.getApplicationProperty("app.host");
        this.fromAddress = serviceManager.getApplicationProperty("email.smtp.fromAddress");

        productRepository = serviceManager.getProductRepository();
        orderRepository = serviceManager.getOrderRepository();
        orderItemRepository = serviceManager.getOrderItemRepository();
        accountRepository = serviceManager.getAccountRepository();
    }

    @Override
    @Transactional(readOnly = false)
    public CurrentAccount authenticateViaDB(SocialAccount socialAccount) {
        try {
            Account accountByEmail = accountRepository.findByEmail(socialAccount.getEmail());
            if (accountByEmail == null) {
                String uniqFileName = UUID.randomUUID().toString() + ".jpg";
                Path pathFileToSave = Paths.get(rootDir + "/" + uniqFileName);
                downloadAvatar(socialAccount.getAvatarUrl(), pathFileToSave);
                String avatarUrl = "/media/avatars/" + uniqFileName;
                accountByEmail = new Account(socialAccount.getName(), socialAccount.getEmail(), avatarUrl);
                accountRepository.createAccount(accountByEmail);
            }
            return accountByEmail;
        } catch (IOException ioException) {
            throw new InternalServerErrorException("Can't process avatar link ", ioException);
        }
    }

    protected void downloadAvatar(String avatarUrl, Path pathFileToSave) throws IOException {
        try (InputStream inputStream = new URL(avatarUrl).openStream()) {
            Files.copy(inputStream, pathFileToSave);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public long makeOrderAndGetId(ShoppingCart shoppingCart, CurrentAccount currentAccount) {
        validateShoppingCart(shoppingCart);
        Order order = new Order(new Timestamp(System.currentTimeMillis()), currentAccount.getId());
        orderRepository.createOrder(order);
        shoppingCart.getShoppingCartItems().forEach(shoppingCartItem -> {
            orderItemRepository.createOrderItem(
                    new OrderItem(order.getId(), shoppingCartItem.getProduct(), shoppingCartItem.getQuantityOfUniqueProduct()));
        });
        sendEmail(currentAccount.getEmail(), order);
        return order.getId();
    }

    private void validateShoppingCart(ShoppingCart shoppingCart) {
        if (shoppingCart == null || shoppingCart.getShoppingCartItems().isEmpty())
            throw new InternalServerErrorException("Shopping cart is empty");
    }

    @Override
    @Transactional
    public void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
        Product productById = productRepository.findProductById(productForm.getIdProduct());
        if (productById == null)
            throw new InternalServerErrorException("Product not found by id: " + productForm.getIdProduct());
        shoppingCart.addProduct(productById, productForm.getCount());
    }


    @Override
    public void removeProductFromShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
        shoppingCart.removeProduct(productForm.getIdProduct(), productForm.getCount());
    }


    @Override
    @Transactional
    public Order findOrderById(long orderId, CurrentAccount currentAccount) {
        Order orderById = orderRepository.findOrderById(orderId);
        if (orderById == null) throw new ResourceNotFoundException("Order not found by id: " + orderId);
        if (!orderById.getIdAccount().equals(currentAccount.getId()))
            throw new AccessDeniedException("Account with id: " + currentAccount.getId() + " is not owner for order with id: " + orderId);
        orderById.setOrderItemsList(orderItemRepository.findOrderItemsByOrderId(orderId));
        return orderById;
    }

    @Override
    @Transactional
    public List<Order> getListOrdersForCurrentAccount(CurrentAccount currentAccount, int numberOfPage, int limit) {
        int offSet = (numberOfPage - 1) * limit;
        return orderRepository.findOrdersByAccountId(currentAccount.getId(), limit, offSet);
    }

    @Override
    @Transactional
    public int getCountOfOrdersForCurrentAccount(CurrentAccount currentAccount) {
        return orderRepository.getCountOrdersByAccountId(currentAccount.getId());
    }


    public String serializeShoppingCart(ShoppingCart shoppingCart) {
        StringBuilder stringBuilder = new StringBuilder();
        shoppingCart.getShoppingCartItems().forEach(shoppingCartItem -> {
            stringBuilder
                    .append(shoppingCartItem.getProduct().getId()).append("-")
                    .append(shoppingCartItem.getQuantityOfUniqueProduct()).append("|");
        });
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @Transactional
    public ShoppingCart deserializeShoppingCart(String str) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String[] shoppingCartItems = str.split("[|]");
        for (String shoppingCartItemStr : shoppingCartItems) {
            try {
                String[] dataItem = shoppingCartItemStr.split("[-]");
                int idProduct = Integer.parseInt(dataItem[0]);
                int count = Integer.parseInt(dataItem[1]);
                this.addProductToShoppingCart(new ProductForm(idProduct, count), shoppingCart);
            } catch (RuntimeException runtimeException) {
                LOGGER.error("Can't add product to Shopping Cart during deserialization: item= " + shoppingCartItemStr, runtimeException);
            }
        }
        return shoppingCart.getShoppingCartItems().isEmpty() ? null : shoppingCart;
    }

    private void sendEmail(String emailAddress, Order order) {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setCharset("UTF-8");
            email.setHostName(smtpHost);
            email.setSSLOnConnect(true);
            email.setSslSmtpPort(smtpPort);
            email.setFrom(fromAddress);
            email.setAuthentication(smtpUserName, smtpPassword);
            email.setSubject("New order");
            email.setMsg(host + "/order?id=" + order.getId());
            email.addTo(emailAddress);
            email.send();
        } catch (EmailException e) {
            LOGGER.error("Error during send email: " + e.getMessage(), e);
        }
    }
}
