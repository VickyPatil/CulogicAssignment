package com.test.culogicproductlisting.managers;

import android.content.Context;

import com.test.culogicproductlisting.daos.ProductDao;
import com.test.culogicproductlisting.database.DatabaseManager;
import com.test.culogicproductlisting.interfaces.WebServiceCallBack;
import com.test.culogicproductlisting.networkcommunication.OkHttpWebCallAsyncTask;
import com.test.culogicproductlisting.networkcommunication.WebServiceConstants;
import com.test.culogicproductlisting.networkcommunication.WebServiceRequest;

import java.util.ArrayList;

/**
 * Created by Vikas on 28/10/17.
 */

public class ProductManager {
    private static final String TAG = ProductManager.class.getSimpleName();
    private static ProductManager productManager;
    Context context;

    private ProductManager(Context context) {
        this.context = context;
    }

    public static ProductManager getInstance(Context context) {
        if (productManager == null) {
            productManager = new ProductManager(context);
        }
        return productManager;
    }

    public void getProductListFromServer(Context context, WebServiceCallBack webServiceCallBack, String pageNo) {
        new OkHttpWebCallAsyncTask(context, webServiceCallBack, WebServiceConstants
                .PRODUCT_LIST_API, WebServiceRequest.PRODUCT_LIST, null)
                .execute();
    }

    public ArrayList<ProductDao> getProductListFromDatabase(long offset){
        return DatabaseManager.getInstance(context).getAllProductListByOffset(offset);
    }
}
