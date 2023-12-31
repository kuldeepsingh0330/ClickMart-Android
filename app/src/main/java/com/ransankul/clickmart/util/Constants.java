package com.ransankul.clickmart.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    public static final String SHARED_PREFS_NAME = "ransankulClickmart";
    public static final String KEY_STRING_VALUE = "JWTToken";
    public static final String RAZORPAY_KEY_ID = "set_your_key_id";
    public static final String API_BASE_URL = "http://000.000.000.000:8080";
    // url = http://add_here_your_IP:8080
    public static final String REGISTER_USER_URL = API_BASE_URL + "/register";
    public static final String GET_CATEGORIES_URL = API_BASE_URL + "/categories/";
    public static final String GET_PRODUCTS_URL = API_BASE_URL + "/product/all/";
    public static final String SEARCH_PRODUCTS_URL = API_BASE_URL + "/product/search/";
    public static final String GET_PRODUCT_BY_CATEGORY_ID_URL = API_BASE_URL + "/product/getProductsByCategory/";
    public static final String GET_OFFERS_URL = API_BASE_URL + "/recentoffer/";
    public static final String GET_PRODUCT_DETAILS_URL = API_BASE_URL + "/product/getProductById/";
    public static final String POST_CREATE_ORDER_URL = API_BASE_URL + "/payment/create-order";
    public static final String POST_UPDATE_ORDER_URL = API_BASE_URL + "/payment/verify-payment";

    public static final String OFFER_IMAGE_URL = API_BASE_URL + "/recentoffer/image/";
    public static final String CATEGORIES_IMAGE_URL = API_BASE_URL + "/categories/category_image/";
    public static final String PRODUCTS_IMAGE_URL = API_BASE_URL + "/product/image/";
    public static final String ADD_NEW_ADDRESS_URL = API_BASE_URL + "/address/";
    public static final String GET_ALL_ADDRESS_URL = API_BASE_URL + "/address/allAddress";
    public static final String UPDATE_ADDRESS_URL = API_BASE_URL + "/address/updateAddress";
    public static final String POST_ADD_TO_WISHLIST_URL = API_BASE_URL + "/product/wishlist/add";
    public static final String POST_REMOVE_TO_WISHLIST_URL = API_BASE_URL + "/product/wishlist/remove";
    public static final String POST_LOAD_ALL_TO_WISHLIST_URL = API_BASE_URL + "/product/wishlist/all/";
    public static final String POST_PRODUCT_EXIST_TO_WISHLIST_URL = API_BASE_URL + "/product/wishlist/isExist";
    public static final String DELETE_ADDRESS_URL = API_BASE_URL+"/address/";
    public static final String POST_LOAD_ALL_TRANSACTION_URL = API_BASE_URL+"/payment/";
    public static final String POST_IS_TOKEN_EXPIRED_URL = API_BASE_URL+"/auth/validate-token";
    public static final String VALIDATE_USER_URL = API_BASE_URL + "/auth/login";

    public static String getTokenValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_STRING_VALUE, "");
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
