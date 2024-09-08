package com.arabianmaps.compass.Compass_volley;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/* loaded from: classes2.dex */
public class Compass_MySingleton {
    private static Context ctx;
    private static Compass_MySingleton instance;
    private RequestQueue requestQueue;

    private Compass_MySingleton(Context context) {
        ctx = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized Compass_MySingleton getInstance(Context context) {
        Compass_MySingleton mySingleton;
        synchronized (Compass_MySingleton.class) {
            if (instance == null) {
                instance = new Compass_MySingleton(context);
            }
            mySingleton = instance;
        }
        return mySingleton;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return this.requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
