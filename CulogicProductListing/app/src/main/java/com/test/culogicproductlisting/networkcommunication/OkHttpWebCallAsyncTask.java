package com.test.culogicproductlisting.networkcommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.test.culogicproductlisting.interfaces.WebServiceCallBack;
import com.test.culogicproductlisting.utils.ExceptionType;
import com.test.culogicproductlisting.utils.NetworkUtil;

import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vikas .
 */

public class OkHttpWebCallAsyncTask extends AsyncTask<Void, String, String> {
    private static final String TAG = OkHttpWebCallAsyncTask.class.getSimpleName();
    private final Context context;
    private LinkedHashMap<String, String> params;
    private String URL;
    private WebServiceCallBack webServiceCallBack;
    private OkHttpClient httpClient = new OkHttpClient();
    private String responseString;
    private WebServiceRequest requestType;
    private ExceptionType responseStatus;



    public OkHttpWebCallAsyncTask(Context context,
                                  WebServiceCallBack webServiceCallBack,
                                  String URL,
                                  WebServiceRequest requestType,
                                  LinkedHashMap<String, String> params) {

        this.context = context;
        this.webServiceCallBack = webServiceCallBack;
        this.URL = URL;
        this.params = params;
        this.requestType = requestType;
    }

    @Override
    protected String doInBackground(Void... param) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            Request request;
            // Check If there is URL needs parameters and add it Request body
            if (params != null) {
                FormEncodingBuilder encodingBuilder = new FormEncodingBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodingBuilder.add(entry.getKey(), entry.getValue());
                    Log.e("key and value pair", " " + entry.getKey() + " " + entry.getValue());
                }

                RequestBody requestBody = encodingBuilder.build();
                request = new Request.Builder()
                        .url(URL)
                        .post(requestBody)
                        .build();

            } else {
                request = new Request.Builder()
                        .url(URL)
                        .build();
            }
            responseString = callAPI(request);
        } else {
            responseString = null;
            responseStatus = ExceptionType.NETWORK_FAILURE;
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String responseString) {
        super.onPostExecute(responseString);
        webServiceCallBack.onResponse(requestType, responseString, ExceptionType.CONNECTED);
    }

    private String callAPI(Request request) {
        try {
            httpClient.setConnectTimeout(120, TimeUnit.SECONDS); // connect timeout
            Response response = httpClient.newCall(request).execute();

            responseStatus = ExceptionType.CONNECTED;
            return response.body().string();
        } catch (SocketTimeoutException e) {
            responseStatus = ExceptionType.SERVER_ERROR;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            responseStatus = ExceptionType.SERVER_ERROR;
            return null;
        }
    }
}

