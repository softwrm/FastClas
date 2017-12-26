package com.versatile.fastclas.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by uday kiran on 07-04-2017.
 */

public interface IParseListener {
    void ErrorResponse(VolleyError volleyError, int requestCode);

    void SuccessResponse(String response, int requestCode);
}
