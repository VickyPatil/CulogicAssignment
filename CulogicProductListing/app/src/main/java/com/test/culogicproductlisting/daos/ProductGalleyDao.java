package com.test.culogicproductlisting.daos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.test.culogicproductlisting.utils.Constants;

/**
 * Created by vikas on 28-10-2017.
 */
@DatabaseTable(tableName = "ProductGalleyDao")
public class ProductGalleyDao {
    @DatabaseField(columnName = Constants.PRODUCT_GALLERY_ID,generatedId = true)
    private int productGalleryId;
    @DatabaseField(columnName = Constants.PRODUCT_ID,foreign = true)
    private ProductDao productDao;
    @DatabaseField(columnName = Constants.PRODUCT_IMAGE_URL )
    private String productImageUrl;

    public int getProductGalleryId() {
        return productGalleryId;
    }

    public void setProductGalleryId(int productGalleryId) {
        this.productGalleryId = productGalleryId;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
