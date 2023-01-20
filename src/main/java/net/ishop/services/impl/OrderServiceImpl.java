package net.ishop.services.impl;

import net.framework.TransactionalSynchronizationManager;
import net.framework.annotations.dependency_injection.Autowired;
import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.jdbc.Transactional;
import net.framework.utils.jdbc.factory.TransactionSynchronization;
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
import net.ishop.services.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    NotificationContentBuilderService notificationContentBuilderService;

    public OrderServiceImpl() {
    }

    @Override
    @Transactional(readOnly = false)
    public CurrentAccount authenticateViaDB(SocialAccount socialAccount) {
        Account accountByEmail = accountRepository.findByEmail(socialAccount.getEmail());
        if (accountByEmail == null) {
            String avatarUrl = avatarService.processGettingAvatarLink(socialAccount.getAvatarUrl());
            accountByEmail = new Account(socialAccount.getName(), socialAccount.getEmail(), avatarUrl);
            accountRepository.createAccount(accountByEmail);
        }
        return accountByEmail;
    }


    @Override
    @Transactional(readOnly = false)
    public long makeOrderAndGetId(ShoppingCart shoppingCart, final CurrentAccount currentAccount) {
        validateShoppingCart(shoppingCart);
        final Order order = new Order(new Timestamp(System.currentTimeMillis()), currentAccount.getId());
        orderRepository.createOrder(order);
        shoppingCart.getShoppingCartItems().forEach(shoppingCartItem -> {
            orderItemRepository.createOrderItem(
                    new OrderItem(order.getId(), shoppingCartItem.getProduct(), shoppingCartItem.getQuantityOfUniqueProduct()));
        });
        TransactionalSynchronizationManager.addTransactionSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                String notificationMessage = notificationContentBuilderService.buildNotificationMessageAboutCreatedNewOrder(order);
                notificationService.sendNotification(currentAccount.getEmail(), notificationMessage);
            }
        });
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
        return cookieService.createdShoppingCartCookie(shoppingCart.getShoppingCartItems());
    }

    @Transactional
    public ShoppingCart deserializeShoppingCart(String cookieValue) {
        ShoppingCart shoppingCart = new ShoppingCart();
        List<ProductForm> productFormList = cookieService.parseChoppingCartCookie(cookieValue);
        for (ProductForm productForm : productFormList) {
            try {
                this.addProductToShoppingCart(productForm, shoppingCart);
            } catch (RuntimeException runtimeException) {
                LOGGER.error("Can't add product to Shopping Cart during deserialization: item= " + productForm, runtimeException);
            }
        }
        return shoppingCart.getShoppingCartItems().isEmpty() ? null : shoppingCart;
    }
}
