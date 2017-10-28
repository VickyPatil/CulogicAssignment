package com.test.culogicproductlisting.interfaces;

import com.test.culogicproductlisting.networkcommunication.WebServiceRequest;
import com.test.culogicproductlisting.utils.ExceptionType;

/**
 * Created by Vikas.
 */

public interface WebServiceCallBack
{

    void onResponse(WebServiceRequest requestType, String response, ExceptionType responseStatus);

}
