package com.test.culogicproductlisting.interfaces;

import com.androidapp.culogicassignment.models.Product;

import java.util.ArrayList;

/**
 * Created by vikas on 28-10-2017.
 */

public interface AddToCartCallBAck {
    void addProductToCart(Product product);
    void onCartRefresh(ArrayList<Product> productArrayList);
}
