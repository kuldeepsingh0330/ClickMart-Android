package com.ransankul.clickmart.util;

public class Constants {
    public static final String API_BASE_URL = "https://tutorials.mianasad.com/ecommerce";
    public static final String GET_CATEGORIES_URL = API_BASE_URL + "/services/listCategory";
    public static final String GET_PRODUCTS_URL = API_BASE_URL + "/services/listProduct";
    public static final String GET_OFFERS_URL = API_BASE_URL + "/services/listFeaturedNews";
    public static final String GET_PRODUCT_DETAILS_URL = API_BASE_URL + "/services/getProductDetails?id=";
    public static final String POST_ORDER_URL = API_BASE_URL + "/services/submitProductOrder";
    public static final String  PAYMENT_URL = API_BASE_URL + "/services/paymentPage?code=";

    public static final String NEWS_IMAGE_URL = API_BASE_URL + "/uploads/news/";
    public static final String CATEGORIES_IMAGE_URL = API_BASE_URL + "/uploads/category/";
    public static final String PRODUCTS_IMAGE_URL = API_BASE_URL + "/uploads/product/";
}
