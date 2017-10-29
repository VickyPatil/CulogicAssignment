package com.test.culogicproductlisting.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.culogicproductlisting.R;
import com.test.culogicproductlisting.activities.HomeActivity;
import com.test.culogicproductlisting.adapters.RecyclerViewProductListAdapter;
import com.test.culogicproductlisting.customviews.GridSpacingItemDecoration;
import com.test.culogicproductlisting.customviews.PaginationScrollListener;
import com.test.culogicproductlisting.daos.ProductDao;
import com.test.culogicproductlisting.daos.ProductGalleyDao;
import com.test.culogicproductlisting.database.DatabaseManager;
import com.test.culogicproductlisting.interfaces.WebServiceCallBack;
import com.test.culogicproductlisting.managers.ProductManager;
import com.test.culogicproductlisting.models.Product;
import com.test.culogicproductlisting.networkcommunication.WebServiceRequest;
import com.test.culogicproductlisting.utils.DialogUtil;
import com.test.culogicproductlisting.utils.ExceptionType;
import com.test.culogicproductlisting.utils.JSONKeyConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.test.culogicproductlisting.utils.BundleKeyConstant.PRODUCT_LIST;


/**
 * Created by vikas on 28-10-2017.
 */

public class ProductListingFragment extends Fragment implements WebServiceCallBack {
    private static final String TAG = ProductListingFragment.class.getSimpleName();
    private static int GRID_SPAN;

    private RecyclerViewProductListAdapter recyclerProductPortfolioAdapter;
    private RecyclerView rvProductPortfolio;
    private ArrayList<Product> allProductList;
    private TextView tvNoData;
    private GridLayoutManager gridLayoutManager;

    public static ProductListingFragment newInstance() {
        ProductListingFragment productPortfolioFragment = new ProductListingFragment();
        Bundle args = new Bundle();
        productPortfolioFragment.setArguments(args);
        return productPortfolioFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_listing, container, false);
        initUIComponents(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (recyclerProductPortfolioAdapter == null) {
            recyclerProductPortfolioAdapter = new RecyclerViewProductListAdapter((HomeActivity) getActivity(), allProductList);
        }
        rvProductPortfolio.setAdapter(recyclerProductPortfolioAdapter);
        addScrollListener(gridLayoutManager);
        /*if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if(allProductList.isEmpty()) {
            DialogUtil.startProgressDialog(getActivity(), "");
            ProductManager.getInstance(getActivity()).getProductListFromServer(getActivity(), this, String.valueOf(0));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putSerializable(PRODUCT_LIST, allProductList);
    }

    @Override
    public void onResponse(WebServiceRequest requestType, String response, ExceptionType responseStatus) {
        DialogUtil.stopProgressDialog();
        ArrayList<Product> productArrayList = null;
        if (response != null && responseStatus.equals(ExceptionType.CONNECTED)) {
            try {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonProductArray = jsonObject.getJSONArray(JSONKeyConstant.KEY_PRODUCTS);
                if (jsonProductArray != null && jsonProductArray.length() > 0) {
                    productArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonProductArray.length(); i++) {
                        JSONObject jObj = jsonProductArray.getJSONObject(i);
                        Product product = gson
                                .fromJson(jObj.toString(), Product.class);
                        // addProductsIntoDb(product);
                        productArrayList.add(product);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(responseStatus.equals(ExceptionType.NETWORK_FAILURE)){
            DialogUtil.showToast(getActivity().getApplicationContext(),"Please check network connection");
        }else {
            DialogUtil.showToast(getActivity().getApplicationContext(),"Something went wrong..Please try again");
        }
        addAllProductsToList(productArrayList, 0);

    }

    private void addProductsIntoDb(Product product) {
        ProductDao productDao = new ProductDao();
        productDao.setProductName(product.getProductName());
        productDao.setPrice(product.getPrice());
        productDao.setVendorName(product.getVendorName());
        productDao.setVendorAddress(product.getVendorAddress());
        productDao.setPhoneNumber(product.getPhoneNumber());
        for(String url:product.getProductGallery()){
            ProductGalleyDao productGalleyDao = new ProductGalleyDao();
            productGalleyDao.setProductImageUrl(url);
        }
        boolean status = DatabaseManager.getInstance(this.getActivity()).insertProduct(productDao);
    }

    private void restoreData(Bundle savedInstanceState) {
        allProductList.addAll((ArrayList<Product>) savedInstanceState.getSerializable(PRODUCT_LIST));
        addAllProductsToList(allProductList, 0);
    }


    private void initUIComponents(View view) {
        rvProductPortfolio = (RecyclerView) view.findViewById(R.id.rvProductPortfolio);
        allProductList = new ArrayList<>();
        tvNoData = (TextView) view.findViewById(R.id.tvNoData);
        Configuration configuration = getActivity().getResources().getConfiguration();
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (configuration.smallestScreenWidthDp >= 600) {
                GRID_SPAN = 3;
            } else {
                GRID_SPAN = 2;
            }
            //code for portrait mode
        } else {
            //code for landscape mode
            if (configuration.smallestScreenWidthDp >= 600) {
                GRID_SPAN = 5;
            } else {
                GRID_SPAN = 3;
            }
        }




        gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN);
        rvProductPortfolio.setLayoutManager(gridLayoutManager);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(GRID_SPAN, 10, true);
        rvProductPortfolio.addItemDecoration(gridSpacingItemDecoration);
    }

    public void addAllProductsToList(ArrayList<Product> productList, long offset) {
        //  ArrayList<ProductDao> productList = getProductListFromDb(offset);

        if (productList != null && productList.size() > 0) {
            allProductList.addAll(productList);
            recyclerProductPortfolioAdapter.setProductArrayList(allProductList);

            rvProductPortfolio.post(new Runnable() {
                public void run() {
                    recyclerProductPortfolioAdapter.notifyDataSetChanged();
                }
            });
        }

        if (allProductList.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            rvProductPortfolio.setVisibility(View.VISIBLE);
        } else {
            rvProductPortfolio.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<ProductDao> getProductListFromDb(long offset) {
        return ProductManager.getInstance(getActivity()).getProductListFromDatabase(offset);
    }


    private void addScrollListener(final GridLayoutManager gridLayoutManager) {
        PaginationScrollListener paginationScrollListener = new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
              //  ProductManager.getInstance(getActivity())
                   //     .getProductListFromServer(getActivity(),ProductListingFragment.this,String.valueOf(allProductList.size()));
             //   addAllProductsToList(allProductList.size());
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        };
        // Adds the scroll listener to RecyclerView
        rvProductPortfolio.addOnScrollListener(paginationScrollListener);
    }


}