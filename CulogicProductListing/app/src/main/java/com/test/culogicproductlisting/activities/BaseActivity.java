package com.test.culogicproductlisting.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.test.culogicproductlisting.R;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Toolbar toolbar;
    protected FrameLayout childContainer;
    private TextView tvToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initUIComponents();
        setSupportActionBar(toolbar);
    }

    private void initUIComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        childContainer = (FrameLayout) findViewById(R.id.base_container);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
    }

    /**
     * This method add the child activity layout into base container.
     * As Child Activity extends BaseActivity, we can not use setContentView() Directly.So,
     * We are adding child activity layout into BaseActivity using LayoutInflater.
     *
     * @param layoutId child activity layout
     * @return View Parent layout contains other views
     */
    protected View setChildLayout(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutId, null, false);
        childContainer.addView(contentView, 0);
        return contentView;
    }

    protected void setToolbarTitle(String toolbarTitle) {
        tvToolbarTitle.setText(toolbarTitle);
    }
}
