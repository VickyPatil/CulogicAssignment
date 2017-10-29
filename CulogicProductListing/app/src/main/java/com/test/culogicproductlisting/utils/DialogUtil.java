package com.test.culogicproductlisting.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.test.culogicproductlisting.R;

/**
 * Created by Vikas on 12/2/16.
 */
public class DialogUtil {

    private static ProgressDialog _pd;
    private static ProgressDialog progressDialog;

    public static ProgressDialog startProgressDialog(Context context, String message) {
        if (_pd == null) {
            _pd = ProgressDialog.show(context, null, null);
            _pd.setContentView(R.layout.layout_progress_dialog);
            _pd.setMessage(message);
            _pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            _pd.setCancelable(false);
            _pd.show();
        }
        return _pd;
    }

    public static void stopProgressDialog() {
        if (_pd != null)
            _pd.dismiss();
        _pd = null;
    }

    public static void showToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static void showLog(Context context, String tagName, String msg) {
        Log.d(tagName, msg);
    }

}
