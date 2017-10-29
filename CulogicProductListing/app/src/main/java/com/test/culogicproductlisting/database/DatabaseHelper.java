package com.test.culogicproductlisting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.test.culogicproductlisting.daos.ProductDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Vikas on 9/6/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "productList.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper databaseHelper;


    private Dao<ProductDao, Integer> productDao;


    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        openDatabase();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // Create Table with given table name with columnName
            TableUtils.createTable(connectionSource, ProductDao.class);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to create databases", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        getWritableDatabase();
    }


    public <T> List<T> getAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryForAll();
    }

    public <T> List<T> getAll(Class<T> clazz, long offset) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().offset(offset).limit(10l).query();
    }

    public <T> List<T> getAllOrdered(Class<T> clazz, String orderBy, boolean ascending) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().orderBy(orderBy, ascending).query();
    }

    public <T> List<T> getAllDistinct(Class<T> clazz, String columnName) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().distinct().selectColumns(columnName).query();
    }


    public <T> T getById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.queryForId(aId);
    }


    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
        Dao<T, ?> dao = (Dao<T, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }

    public <T> int deleteById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.deleteById(aId);
    }

    public <T> int deleteObjects(Class<T> clazz, Collection<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.delete(aObjList);
    }

    public <T> void deleteAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.deleteBuilder().delete();
    }


    public <T> List<T> getDataByQuery(String columnName, String columnValue, Class<T> clazz) {
        List<T> objList = new ArrayList<>();
        try {
            Dao<T, ?> dao = getDao(clazz);
            QueryBuilder qB = dao.queryBuilder();
           /* if (columnValue != -1)
            {*/
            Where wh = qB.where();
            wh.eq(columnName, columnValue);

            // }
            objList = dao.query(qB.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objList;
    }


    public <T> List<T> getDataByQuery(String columnName, String columnValue, Class<T> clazz, String searchColmnName, String searchText, boolean isSearchTextSelected) {
        List<T> objList = new ArrayList<>();
        try {
            Dao<T, ?> dao = getDao(clazz);
            QueryBuilder qB = dao.queryBuilder();
           /* if (columnValue != -1)
            {*/
            Where wh = qB.where();

            if (columnValue.isEmpty()) {
                if (isSearchTextSelected) {
                    wh.like(searchColmnName, searchText);
                } else {
                    wh.like(searchColmnName, "%" + searchText + "%");
                }
            } else {
                if (isSearchTextSelected) {
                    wh.eq(columnName, columnValue).and().like(searchColmnName, searchText);
                } else {
                    wh.eq(columnName, columnValue).and()
                            .like(searchColmnName, "%" + searchText + "%");
                }
            }

            // }
            objList = dao.query(qB.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objList;
    }


    public <T> List<T> getDataByQuery(String columnName, String columnValue, Class<T> clazz, long offset) {
        List<T> objList = new ArrayList<>();
        try {
            Dao<T, ?> dao = getDao(clazz);
            QueryBuilder qB = dao.queryBuilder();
           /* if (columnValue != -1)
            {*/
            Where wh = qB.where();
            wh.eq(columnName, columnValue);

            qB.offset(offset).limit(10l);
            // }
            objList = dao.query(qB.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objList;
    }


    public Dao<ProductDao, Integer> getProductDao() throws SQLException {
        if (productDao == null) {
            productDao = getDao(ProductDao.class);
        }
        return productDao;
    }

}
