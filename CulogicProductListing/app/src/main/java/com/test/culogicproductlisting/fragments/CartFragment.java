package com.test.culogicproductlisting.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.culogicproductlisting.R;
import com.test.culogicproductlisting.adapters.RecyclerViewCartAdapter;
import com.test.culogicproductlisting.customviews.PaginationScrollListener;
import com.test.culogicproductlisting.daos.ProductDao;
import com.test.culogicproductlisting.database.DatabaseManager;
import com.test.culogicproductlisting.interfaces.AddToCartCallBAck;
import com.test.culogicproductlisting.models.Product;
import com.test.culogicproductlisting.utils.BundleKeyConstant;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.test.culogicproductlisting.utils.BundleKeyConstant.PRODUCT_LIST;

/**
 * Created by vikas on 28-10-2017.
 */

public class CartFragment extends Fragment implements AddToCartCallBAck {
    private static final String TAG = ProductListingFragment.class.getSimpleName();

    private static final int REQUEST_PERMISSION_PHONE_CALL = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;


    private RecyclerViewCartAdapter recyclerViewCartAdapter;
    private RecyclerView rvProductPortfolio;
    private ArrayList<Product> cartProductList;
    private TextView tvNoData, tvTotalPrice;
    private LinearLayoutManager linearLayoutManager;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private String vendorPhoneNumber;

    public static CartFragment newInstance(ArrayList<Product> productArrayList) {
        CartFragment cartFragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable(BundleKeyConstant.PRODUCT_LIST, productArrayList);
        cartFragment.setArguments(args);
        return cartFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        cartProductList = new ArrayList<>();
      //  cartProductList = (ArrayList<Product>) getArguments().getSerializable(PRODUCT_LIST);
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
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (recyclerViewCartAdapter == null) {
            recyclerViewCartAdapter = new RecyclerViewCartAdapter(this, cartProductList);
        }
        rvProductPortfolio.setAdapter(recyclerViewCartAdapter);
        addScrollListener(linearLayoutManager);
        if (savedInstanceState != null) {
            //restoreData(savedInstanceState);
        } else {
            addAllProductsToList(0);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.label_permission_alert_title));
                    builder.setMessage(getString(R.string.label_permission_alert_message));
                    builder.setPositiveButton(getString(R.string.label_permission_alert_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_PHONE_CALL);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.label_permission_alert_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.label_unable_to_get_permission), Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void addProductToCart(Product product) {
    }

    @Override
    public void onCartRefresh(ArrayList<Product> productArrayList) {
       if(isAdded()) {
            if (productArrayList != null && productArrayList.size() > 0) {
                setCartListVisibility(true);

                recyclerViewCartAdapter.setProductArrayList(productArrayList);
                recyclerViewCartAdapter.notifyDataSetChanged();
                updateTotalPrice(productArrayList);
            } else {
                setCartListVisibility(false);
            }
       }
    }


    private void proceedAfterPermission() {
        callPhoneIntent(vendorPhoneNumber);
    }

    public void requestPhoneCallPermission(String phoneNumber) {
        vendorPhoneNumber = phoneNumber;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.label_permission_alert_title));
                    builder.setMessage(getString(R.string.label_permission_alert_message));
                    builder.setPositiveButton(getString(R.string.label_permission_alert_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_PHONE_CALL);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.label_permission_alert_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(Manifest.permission.CALL_PHONE, false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.label_permission_alert_title));
                    builder.setMessage(getString(R.string.label_permission_alert_message));
                    builder.setPositiveButton(getString(R.string.label_permission_alert_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.label_unable_to_get_permission), Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.label_permission_alert_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_PHONE_CALL);
                }

                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.CALL_PHONE, true);
                editor.commit();


            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        } else {
            callPhoneIntent(phoneNumber);
        }
    }

    private void callPhoneIntent(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            getActivity().startActivity(intent);
        }
    }


    private void restoreData(Bundle savedInstanceState) {
        cartProductList.addAll((ArrayList<Product>) savedInstanceState.getSerializable(PRODUCT_LIST));
        addAllProductsToList(0);
    }


    private void initUIComponents(View view) {
        rvProductPortfolio = (RecyclerView) view.findViewById(R.id.rvProductPortfolio);
        cartProductList = new ArrayList<>();
        rvProductPortfolio.setHasFixedSize(true);
        tvNoData = (TextView) view.findViewById(R.id.tvNoData);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProductPortfolio.setLayoutManager(linearLayoutManager);
        tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);
    }

    public void addAllProductsToList(int offset) {
        //  ArrayList<ProductDao> productList = getAllProductList(offset);

        if (cartProductList.size() > 0) {
            // cartProductList.addAll(cartProductList);
            recyclerViewCartAdapter.setProductArrayList(cartProductList);

            rvProductPortfolio.post(new Runnable() {
                public void run() {
                    recyclerViewCartAdapter.notifyDataSetChanged();
                }
            });
        }


    }

    private ArrayList<ProductDao> getAllProductList(int offset) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(getActivity().getApplicationContext());
        return databaseManager.getAllProductListByOffset(offset);
    }


    private void addScrollListener(final LinearLayoutManager linearLayoutManager) {
        PaginationScrollListener paginationScrollListener = new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                //  addAllProductsToList(allProductList.size());
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

    public void updateTotalPrice(ArrayList<Product> productArrayList) {
        if (productArrayList.size() > 0) {
            setCartListVisibility(true);
            double totalPrice = 0;
            for (Product product : productArrayList) {

                double productPrice = (Double.parseDouble(product.getProductQuantity()) * product.getPrice());
                totalPrice += productPrice;
            }
            tvTotalPrice.setText("Total Price : " + String.valueOf(totalPrice));
        } else {
            setCartListVisibility(false);
        }

    }

    private void setCartListVisibility(boolean isDataAvailable) {
        if(isAdded()) {
            if (isDataAvailable) {
                tvNoData.setVisibility(View.GONE);
                rvProductPortfolio.setVisibility(View.VISIBLE);
                tvTotalPrice.setVisibility(View.VISIBLE);
            } else {
                tvTotalPrice.setVisibility(View.GONE);
                rvProductPortfolio.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }


}