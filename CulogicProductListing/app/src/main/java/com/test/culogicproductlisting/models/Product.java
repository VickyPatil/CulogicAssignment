package com.test.culogicproductlisting.models;

import com.google.gson.annotations.SerializedName;
import com.test.culogicproductlisting.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vikas on 26/10/17.
 */

public class Product implements Serializable{
    @SerializedName(Constants.PRODUCT_NAME)
    private String productName;
    @SerializedName(Constants.PRICE)
    private double price;
    @SerializedName(Constants.VENDOR_NAME)
    private String vendorName;
    @SerializedName(Constants.VENDOR_ADDRESS)
    private String vendorAddress;
    @SerializedName(Constants.PRODUCT_IMG)
    private String productImg;
    @SerializedName(Constants.PRODUCT_GALLERY)
    private ArrayList<String> productGallery;
    @SerializedName(Constants.PHONE_NUMBER)
    private String phoneNumber;
    transient
    private String productQuantity;

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public ArrayList<String> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(ArrayList<String> productGallery) {
        this.productGallery = productGallery;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean  equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Product product = (Product) object;
            if (this.productName.equals(product.getProductName())) {
                result = true;
            }
        }
        return result;
    }
}
