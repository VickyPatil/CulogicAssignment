package com.test.culogicproductlisting.daos;

import com.androidapp.culogicassignment.utils.Constants;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by Vikas on 26/10/17.
 */
@DatabaseTable(tableName = "ProductDao")
public class ProductDao {
    @DatabaseField(columnName = Constants.PRODUCT_ID,generatedId = true)
    private int productId;
    @DatabaseField(columnName = Constants.PRODUCT_NAME)
    private String productName;
    @DatabaseField(columnName = Constants.PRICE)
    private double price;
    @DatabaseField(columnName = Constants.VENDOR_NAME)
    private String vendorName;
    @DatabaseField(columnName = Constants.VENDOR_ADDRESS)
    private String vendorAddress;
    @DatabaseField(columnName = Constants.PRODUCT_IMG)
    private String productImg;
    @DatabaseField(columnName = Constants.PRODUCT_GALLERY)
    private ArrayList<String> productGallery;
    @DatabaseField(columnName = Constants.PHONE_NUMBER)
    private String phoneNumber;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
}
