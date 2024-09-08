package com.arabianmaps.compass.Compass_Services;

import android.content.Context;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arabianmaps.compass.Compass_volley.Compass_MySingleton;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class Compass_weatherService {
    private Context context;

    public Compass_weatherService(Context context) {
        this.context = context;
    }

    public void ExchangeRate(String str) {
        Compass_MySingleton.getInstance(this.context).addToRequestQueue(new JsonObjectRequest(0, "https://v6.exchangerate-api.com/v6/adbebca6cefc5151eeea0cab/latest/" + str, null, new Response.Listener<JSONObject>() { // from class: com.digitalcompass.liveweather.Services.weatherService.1
            @Override // com.android.volley.Response.Listener
            public void onResponse(JSONObject jSONObject) {
            }
        }, new Response.ErrorListener() { // from class: com.digitalcompass.liveweather.Services.weatherService.2
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
            }
        }));
    }
}
