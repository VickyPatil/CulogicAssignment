package com.test.culogicproductlisting.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.test.culogicproductlisting.daos.ProductDao;
import com.test.culogicproductlisting.utils.Constants;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Vikas on 9/6/17.
 */

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getSimpleName();

    private static DatabaseManager databaseManager;
    private DatabaseHelper databaseHelper;
    Context context;

    private DatabaseManager(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
        this.context = context;
    }

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }

    public boolean insertProduct(ProductDao product) {
        Dao.CreateOrUpdateStatus createOrUpdateStatus = null;
        try {
            createOrUpdateStatus = databaseHelper.createOrUpdate(product);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return createOrUpdateStatus.isCreated();
    }


    public ArrayList<ProductDao> getAllProductListByOffset(long offset) {
        ArrayList<ProductDao> pestList = new ArrayList<>();
        try {
            QueryBuilder<ProductDao, Integer> pestMasterDaoQueryBuilder = databaseHelper
                    .getProductDao().queryBuilder();

            pestMasterDaoQueryBuilder.offset(offset).limit(offset);
            pestMasterDaoQueryBuilder.orderByRaw(Constants.PRODUCT_NAME + " COLLATE NOCASE");
            pestList = (ArrayList<ProductDao>) pestMasterDaoQueryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pestList;
    }
}

