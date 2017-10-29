package com.test.culogicproductlisting.models;

import com.google.gson.annotations.SerializedName;
import com.test.culogicproductlisting.utils.Constants;

/**
 * Created by vikas on 28-10-2017.
 */

public class ProductGallery {
    @SerializedName(Constants.PRODUCT_IMAGE_URL)
    private String productImageUrl;

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
