package com.ransankul.clickmart.util;

public class Constants {

    public static final String SHARED_PREFS_NAME = "ransankulClickmart";
    public static final String KEY_STRING_VALUE = "JWTToken";
    public static final String RAZORPAY_KEY_ID = "rzp_test_E09iJEUuEwhiqc";
    public static final String API_BASE_URL = "http://192.168.255.235:8080";

    public static final String REGISTER_USER_URL = API_BASE_URL + "/register";

    public  static final String VALIDATE_USER_URL = API_BASE_URL + "/auth/login";

    public static final String GET_CATEGORIES_URL = API_BASE_URL + "/categories/";
    public static final String GET_PRODUCTS_URL = API_BASE_URL + "/product/all";
    public static final String SEARCH_PRODUCTS_URL = API_BASE_URL + "/product/search";
    public static final String GET_PRODUCT_BY_CATEGORY_ID_URL = API_BASE_URL + "/product/getProductsByCategory/";
    public static final String GET_OFFERS_URL = API_BASE_URL + "/recentoffer/";
    public static final String GET_PRODUCT_DETAILS_URL = API_BASE_URL + "/product/getProductById/";
    public static final String POST_CREATE_ORDER_URL = API_BASE_URL + "/payment/create-order";
    public static final String  PAYMENT_URL = API_BASE_URL + "/services/paymentPage?code=";

    public static final String OFFER_IMAGE_URL = API_BASE_URL + "/recentoffer/image/";
    public static final String CATEGORIES_IMAGE_URL = API_BASE_URL + "/categories/category_image/";
    public static final String PRODUCTS_IMAGE_URL = API_BASE_URL + "/product/image/";
    public static final String ADD_NEW_ADDRESS_URL = API_BASE_URL + "/address/";
    public static final String GET_ALL_ADDRESS_URL = API_BASE_URL + "/address/allAddress/";
    public static final String UPDATE_ADDRESS_URL = API_BASE_URL + "/address/updateAddress";
}
