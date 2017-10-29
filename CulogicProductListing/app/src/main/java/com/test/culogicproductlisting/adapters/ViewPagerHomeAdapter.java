package com.test.culogicproductlisting.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 28-10-2017.
 */

public class ViewPagerHomeAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private FragmentManager fragmentManager;


    public ViewPagerHomeAdapter(FragmentManager manager) {
        super(manager);
        this.fragmentManager = manager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public static String makeFragmentName(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }

    public
    @Nullable
    Fragment getFragmentForPosition(ViewPager viewPager, int position) {
        String tag = makeFragmentName(viewPager.getId(), getItemId(position));
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        return fragment;
    }
}
