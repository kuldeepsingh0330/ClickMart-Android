package com.ransankul.clickmart.activities;

import static com.ransankul.clickmart.util.Constants.CATEGORIES_IMAGE_URL;
import static com.ransankul.clickmart.util.Constants.PRODUCTS_IMAGE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.adapter.CategoryAdapter;
import com.ransankul.clickmart.adapter.ProductAdapter;
import com.ransankul.clickmart.databinding.ActivityMainBinding;
import com.ransankul.clickmart.model.Category;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.navigationview.bringToFront();

        binding.navigationButton.setOnClickListener(view -> {
            if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(enabled) binding.navigationButton.setVisibility(View.GONE);
                else binding.navigationButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

         List<String> sugg = binding.searchBar.getLastSuggestions();
         binding.searchBar.setLastSuggestions(sugg);
         binding.searchBar.setMaxSuggestionCount(5);

        initCategories();
        initProducts();
        initSlider();

        binding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch(id){
                    case R.id.wishlist_item:
                        Intent intent = new Intent(MainActivity.this,WishlistActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.cart_item:
                        Intent inten = new Intent(MainActivity.this,CartActivity.class);
                        startActivity(inten);
                        break;
                    case R.id.address_item:
                        Intent inte = new Intent(MainActivity.this,AllAddressActivity.class);
                        startActivity(inte);
                        break;
                    case R.id.orderhistory_item:
                        Intent in = new Intent(MainActivity.this,OrderHistoryActivity.class);
                        startActivity(in);
                        break;
                    case R.id.logout_item:
                        logOutUser(MainActivity.this);
                        break;
                }
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void logOutUser(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Log Out");
        builder.setMessage("Are you sure to LOG OUT")
                .setPositiveButton("LOG OUT", (dialog, which) -> {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.KEY_STRING_VALUE, "");
                    editor.apply();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    Toast.makeText(this, "Log Out Succesfully", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("CANCEL", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void initSlider() {
        getRecentOffers();
    }

    void initCategories() {
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_CATEGORIES_URL;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i =0; i< jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Category category = new Category(
                                object.getString("name"),
                                CATEGORIES_IMAGE_URL + object.getString("icon"),
                                object.getString("color").toLowerCase(),
                                object.getString("brief"),
                                object.getInt("id")
                        );
                        categories.add(category);

                        categoryAdapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                }, error -> {

        });

        queue.add(request);
    }

    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try{
                JSONArray jsonArray = new JSONArray(response);
                for(int i =0; i<jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Product  product = new Product(object.getString("name"),
                            PRODUCTS_IMAGE_URL+object.getInt("productId"),
                            object.getDouble("price"),
                            object.getDouble("discount"),
                            object.getInt("productId"));
                    products.add(product);

                    productAdapter.notifyDataSetChanged();
                }
            }catch (JSONException e){

                e.printStackTrace();
            }
        }, error -> {
        });

        queue.add(request);
    }

    void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_OFFERS_URL;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for(int i =0; i < array.length(); i++) {
                    JSONObject object =  array.getJSONObject(i);
                    binding.carousel.addData(
                            new CarouselItem(
                                    Constants.OFFER_IMAGE_URL + object.getString("imageName"),
                                    object.getString("title")
                            )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    void initProducts() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

}