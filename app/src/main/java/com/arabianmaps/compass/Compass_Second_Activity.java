package com.arabianmaps.compass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arabianmaps.compass.Compass_Adsdk.AdAdmob;
import com.arabianmaps.compass.Compass_Adsdk.Ads_First_splash_Activity;
import com.arabianmaps.compass.Compass_Adsdk.Compass_Ads_Start_Activity;


public class Compass_Second_Activity extends AppCompatActivity {
    Context context;
    TextView img;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
//
//        img = findViewById(R.id.img);
//        final Handler handler = new Handler();
//        AdAdmob adAdmob = new AdAdmob( this);
//        adAdmob.FullscreenAd(this);
//        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                        Intent shareIntent = new Intent(Compass_Second_Activity.this, Compass_FragmentScreen.class);
//                        startActivity(shareIntent);
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent shareIntent = new Intent(Compass_Second_Activity.this, Compass_FragmentScreen.class);
                startActivity(shareIntent);
                finish();

            }
        }, 1000);


    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
