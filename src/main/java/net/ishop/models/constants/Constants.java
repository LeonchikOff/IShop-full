package net.ishop.models.constants;

public class Constants {
    public static final int MAX_PRODUCTS_PER_HTML_PAGE = 12;
    public static final int COUNT_ORDERS_PER_PAGE = 5 ;

    public static final int MAX_QUANTITY_OF_UNIQUE_PRODUCT_PER_ONE_SHOPPING_CART = 10;
    public static final int MAX_COUNT_PRODUCTS_PER_SHOPPING_CART = 20;

    public static final String CATEGORY_LIST_ATTR_NAME = "CATEGORY_LIST";
    public static final String PRODUCER_LIST_ATTR_NAME = "PRODUCER_LIST";

    public static final String CURRENT_REQUEST_URL = "CURRENT_REQUEST_URL";
    public static final String CURRENT_ACCOUNT = "CURRENT_ACCOUNT";
    public static final String CURRENT_SHOPPING_CART = "CURRENT_SHOPPING_CART";

    public static final String SUCCESS_REDIRECT_URL_AFTER_SIGN_IN = "SUCCESS_REDIRECT_URL_AFTER_SIGN_IN";



    public enum CookieForShoppingCart {
        SHOPPING_CART_COOKIE("itemShoppingCartCookie", 60*60*24*365);
        private final String name;
        private final int timeOfLife;

        CookieForShoppingCart(String name, int timeOfLife) {
            this.name = name;
            this.timeOfLife = timeOfLife;
        }

        public String getName() {
            return name;
        }

        public int getTimeOfLife() {
            return timeOfLife;
        }
    }
}
