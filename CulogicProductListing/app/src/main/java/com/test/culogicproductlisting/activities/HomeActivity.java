package com.test.culogicproductlisting.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.culogicproductlisting.R;
import com.test.culogicproductlisting.adapters.ViewPagerHomeAdapter;
import com.test.culogicproductlisting.fragments.CartFragment;
import com.test.culogicproductlisting.fragments.ProductListingFragment;
import com.test.culogicproductlisting.interfaces.AddToCartCallBAck;
import com.test.culogicproductlisting.models.Product;
import com.test.culogicproductlisting.utils.BundleKeyConstant;
import com.test.culogicproductlisting.utils.DialogUtil;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, AddToCartCallBAck {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerHomeAdapter viewPagerProductPortfolioAdapter;

    private ArrayList<Product> cartProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = setChildLayout(R.layout.activity_home);
        if (savedInstanceState != null) {
            cartProductList = (ArrayList<Product>) savedInstanceState.getSerializable(BundleKeyConstant.PRODUCT_LIST);
        } else
            cartProductList = new ArrayList<>();

        initUIComponents(view,savedInstanceState);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }


    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initUIComponents(View view,Bundle savedInstance) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPagerHome);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        setListeners();
        setupViewPager(viewPager,savedInstance);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setListeners() {
        tabLayout.addOnTabSelectedListener(this);
    }


    private void setupViewPager(final ViewPager viewPager, Bundle savedInstance) {
        viewPager.setOffscreenPageLimit(1);
        if(savedInstance == null){
            cartProductList = new ArrayList<>();
        }else {
            cartProductList = (ArrayList<Product>) savedInstance.getSerializable(BundleKeyConstant.PRODUCT_LIST);
        }
        viewPagerProductPortfolioAdapter = new ViewPagerHomeAdapter(this.getSupportFragmentManager());
        viewPagerProductPortfolioAdapter.addFrag(ProductListingFragment.newInstance(), "");
        viewPagerProductPortfolioAdapter.addFrag(CartFragment.newInstance(cartProductList), "");
        viewPager.setAdapter(viewPagerProductPortfolioAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                // DialogUtils.startProgressDialog(ProductPortfolioActivity.this,"");
                if (position == 0) {
                    setToolbarTitle(getString(R.string.product_list_tab_title));
                } else {
                    setToolbarTitle(getString(R.string.cart_list_tab_title));

                    Fragment fragment = viewPagerProductPortfolioAdapter.getFragmentForPosition(viewPager,position);
                    if(fragment instanceof CartFragment) {
                        CartFragment cartFragment = (CartFragment) fragment;
                        if (cartFragment != null) {
                            cartFragment.onCartRefresh(cartProductList);
                        }
                    }

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        setToolbarTitle(getString(R.string.product_list_tab_title));
    }


    private void setupTabIcons() {
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.GRAY);
        drawable.setSize(2, 2);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);
        TextView tabProductList = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabProductList.setText(getString(R.string.label_products));

        tabProductList.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_list_black, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabProductList);

        TextView tabCart = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabCart.setText(getString(R.string.cart_list_tab_title));

        tabCart.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_shopping_cart_black, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabCart);

    }


    @Override
    public void addProductToCart(Product product) {
        if (cartProductList.contains(product)) {
            int position = cartProductList.indexOf(product);
            int prevQuantity = Integer.parseInt(cartProductList.get(position).getProductQuantity());
            cartProductList.remove(position);
            int finalQuantity = prevQuantity + 1;
            product.setProductQuantity(String.valueOf(finalQuantity));
            cartProductList.add(position,product);

            DialogUtil.showToast(getApplicationContext(), "Added to cart (quantity : "+finalQuantity+")");
        } else {
            product.setProductQuantity("1");
            cartProductList.add(product);
            DialogUtil.showToast(getApplicationContext(), "Added to cart");
        }
    }

    @Override
    public void onCartRefresh(ArrayList<Product> productArrayList) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putSerializable(BundleKeyConstant.PRODUCT_LIST, cartProductList);

        }catch (Exception e){

        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}