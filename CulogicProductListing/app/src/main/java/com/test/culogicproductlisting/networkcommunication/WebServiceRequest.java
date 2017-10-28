package com.test.culogicproductlisting.networkcommunication;

/**
 * Created by vikas .
 */

public enum WebServiceRequest {

    PRODUCT_LIST(1);

    private int requestID;
    WebServiceRequest(int requestID ) {
        this.requestID = requestID;
    }

    public int getRequestID() {
        return requestID;
    }
}
