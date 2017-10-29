package com.test.culogicproductlisting.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.culogicproductlisting.R;
import com.test.culogicproductlisting.adapters.SlidingImageAdapter;

import java.util.ArrayList;

/**
 * Created by vikas on 28-10-2017.
 */

public class ProductGalleryDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = ProductGalleryDialog.class.getSimpleName();
    private Context context;
    private ImageView imageClose;
    private TextView textViewTitle;
    private String title;

    private ViewPager viewPager;
    private static int FIRST_PAGE_POSITION = 0;
    private static int LAST_PAGE_POSITION ;


    private ArrayList<String> imageArrayList;

    private ImageView ivNext, ivPrev;
    private int currentPagePosition;


    public ProductGalleryDialog(Context context, String title, ArrayList<String> productImageList) {
        super(context);
        this.context = context;
        this.title = title;
        imageArrayList = productImageList;
        LAST_PAGE_POSITION = imageArrayList.size();
        // Set the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_gallery_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        initView();
    }

    private void initView() {

        imageClose = (ImageView) findViewById(R.id.ivClose);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        viewPager = (ViewPager) findViewById(R.id.pager);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivPrev = (ImageView) findViewById(R.id.ivPrev);
        viewPager.setAdapter(new SlidingImageAdapter(context, imageArrayList));
        addPageChangeListener();
        ivNext.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        imageClose.setOnClickListener(this);
        viewPager.setCurrentItem(currentPagePosition);

        handlePageChange(currentPagePosition);


    }

    private void addPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPagePosition = position;
                handlePageChange(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivNext:
                if (currentPagePosition < LAST_PAGE_POSITION)
                    viewPager.setCurrentItem((currentPagePosition + 1));
                handlePageChange(currentPagePosition);

                break;
            case R.id.ivPrev:
                if (currentPagePosition > FIRST_PAGE_POSITION)
                    viewPager.setCurrentItem((currentPagePosition - 1));
                handlePageChange(currentPagePosition);

                break;
            case R.id.ivClose:
                dismiss();
                break;
        }
    }

    private void handlePageChange(int position) {
        if (imageArrayList.size() == 1) {
            setButtonVisibility(View.GONE);
        } else {

            if (position == 0) {
                ivPrev.setVisibility(View.GONE);
                ivNext.setVisibility(View.VISIBLE);
            } else if (position == (imageArrayList.size() - 1)) {
                ivNext.setVisibility(View.GONE);
                ivPrev.setVisibility(View.VISIBLE);
            } else {
                setButtonVisibility(View.VISIBLE);
            }
        }
    }

    public void setButtonVisibility(int visibility) {
        ivPrev.setVisibility(visibility);
        ivNext.setVisibility(visibility);
    }
}
