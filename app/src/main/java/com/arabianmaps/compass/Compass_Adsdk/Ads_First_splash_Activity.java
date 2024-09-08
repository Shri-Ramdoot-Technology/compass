package com.arabianmaps.compass.Compass_Adsdk;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.arabianmaps.compass.Compass_Second_Activity;
import com.arabianmaps.compass.R;


public class Ads_First_splash_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.a_ads_first_splash_activity);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(Ads_First_splash_Activity.this, Compass_Second_Activity.class);
                startActivity(mainIntent);

            }
        }, 3000);


    }
}