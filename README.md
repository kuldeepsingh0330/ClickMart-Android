# ClickMart Android Application

The ClickMart Android Application offers a convenient and user-friendly shopping experience, allowing users to explore, discover, and purchase a wide range of products right from their Android devices.

## Table of Contents

- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Activities](#activities)
- [How to Use](#how-to-use)
  - [Setting Up the Backend](#setting-up-the-backend)
  - [Configuring Razorpay and Localhost](#configuring-razorpay-and-localhost)
  - [Running the Application](#running-the-application)
  - [Exploring the App](#exploring-the-app)
- [Screenshots](#screenshots)
- [Video](#video)
- [Contributions](#contributions)

## Key Features

- **User Authentication and Registration**: Securely register or log in to your account using your credentials.
- **Browse Categories and Products**: Explore a variety of product categories and view detailed product information.
- **Search Functionality**: Easily search for products to quickly find what you need.
- **Offers and Recent Offers**: Discover exciting offers and discounts on various products.
- **Wishlist Management**: Save and manage your favorite products for future purchase.
- **Cart Management**: Add products to your cart, review, and proceed to checkout.
- **Secure Payments**: Make secure transactions using the integrated Razorpay gateway.
- **Address Management**: Add, update, and delete delivery addresses for seamless order processing.
- **Transaction History**: View a history of your previous transactions.

## Technologies Used

- Android Studio
- Java
- Retrofit (for API communication)
- Glide (for image loading)
- Razorpay (for payment processing)

## Activities

- CartActivity
- CategoryActivity
- CheckoutActivity
- LoginActivity
- MainActivity
- NoInternetActivity
- OrderHistoryActivity
- ProductDetailActivity
- RegisterActivity
- SearchActivity
- SplashActivity
- WishlistActivity

## How to Use

### Setting Up the Backend

1. To begin, set up the backend by following the instructions at [ClickMart-SpringBoot](https://github.com/kuldeepsingh0330/ClickMart-SpringBoot).

### Configuring Razorpay and Localhost

2. Open the `Constants.java` file in the Android application (`app/src/main/java/com/ransankul/clickmart/util/Constants.java`).
3. Replace the value of `RAZORPAY_KEY_ID` with your Razorpay API key to enable payment processing.
4. Modify the `API_BASE_URL` to your system's localhost IP (e.g., `http://192.168.0.100:8080`) to connect to the backend.

### Running the Application

5. Clone this repository and open it in Android Studio.
   ```sh
   https://github.com/kuldeepsingh0330/ClickMart-Android.git
7. Build and run the application on an emulator or a physical device.

### Exploring the App

7. After launching the app, log in using your credentials or create a new account.
8. Browse categories, search for products, and view detailed product information.
9. Add products to your wishlist or cart and proceed to checkout.
10. Make secure payments using the integrated Razorpay gateway.
11. Manage your delivery addresses and view your transaction history.

## Screenshots
<table>
  <tr>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/7569ce25-4ffd-4900-8a55-2352c53b30b8" alt="Image 2" width="200">
    </td>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/91406b10-b412-4f1d-ad4c-7f880366df84" alt="Image 1" width="200">
    </td>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/7770160c-d079-4fd0-8c5b-d024eac4e31f" alt="Image 3" width="200">
    </td>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/72bf4779-699a-4d84-961b-35131e87f50f" alt="Image 4" width="200">
    </td>
  </tr>

  <tr>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/36ecfb67-7e79-468c-9557-f0845146251a" alt="Image 1" width="200">
    </td>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/3c3bc675-ef01-4695-a6ef-c29a1941e12a" alt="Image 2" width="200">
    </td>
    <td style="border: 1px solid black;">
      <img src="https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/fc321e14-86ab-4f51-afba-dfea4da7c787" alt="Image 3" width="200">
    </td>

  </tr>
</table>

## Video

https://github.com/kuldeepsingh0330/ClickMart-Android/assets/95225751/315cf674-b97c-4ae5-980e-9eba0dcafac4



## Contributions

Contributions to the ClickMart project are welcome! If you find any issues or have suggestions for improvements, feel free to open an issue or submit a pull request.




