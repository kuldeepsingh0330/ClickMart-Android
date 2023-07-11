package com.ransankul.clickmart.activities;

import static com.ransankul.clickmart.util.Constants.CATEGORIES_IMAGE_URL;
import static com.ransankul.clickmart.util.Constants.PRODUCTS_IMAGE_URL;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

//        binding.searchBar.setOnClickListener(view -> {
//            binding.navigationButton.setVisibility(View.GONE);
//        });
//
//        if(!binding.searchBar.isSearchOpened())
//            binding.navigationButton.setVisibility(View.VISIBLE);

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                binding.navigationButton.setVisibility(View.GONE);
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

        initCategories();
        initProducts();
        initSlider();
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