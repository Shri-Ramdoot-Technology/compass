package com.arabianmaps.compass.Compass_Adsdk;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;

import com.arabianmaps.compass.R;
import com.arabianmaps.compass.Compass_Second_Activity;


public class Compass_Ads_Start_Activity extends AppCompatActivity {
    LinearLayout pp, share, rate;
    TextView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_ads_start_activity);

        com.arabianmaps.compass.Compass_Adsdk.AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.FullscreenAd(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);

        rate = findViewById(R.id.rate);
        share = findViewById(R.id.share);
        pp = findViewById(R.id.pp);
        start = findViewById(R.id.start);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent shareIntent = new Intent(Compass_Ads_Start_Activity.this, Compass_Second_Activity.class);
                        startActivity(shareIntent);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException e) {
                    Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {

                }
            }
        });


        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(getString(R.string.pp));
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent shareIntent = new Intent(Compass_Ads_Start_Activity.this, Compass_Ads_Exit_Activity.class);
        startActivity(shareIntent);

    }
}



