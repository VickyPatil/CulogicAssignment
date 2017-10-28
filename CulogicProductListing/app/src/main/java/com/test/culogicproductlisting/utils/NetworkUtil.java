package com.test.culogicproductlisting.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Vikas.
 */

public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    //Check the network Availability
    public static boolean isNetworkAvailable(Context contexts) {
        ConnectivityManager connectivityManager = (ConnectivityManager) contexts
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
            return true;
        else
            return false;
    }
}
