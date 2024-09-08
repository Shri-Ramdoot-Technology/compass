package com.arabianmaps.compass.Compass_Adsdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.arabianmaps.compass.Compass_Second_Activity;
import com.arabianmaps.compass.R;

public class Compass_Ads_Exit_Activity extends Activity {
    Button freeEmi_Calculater_cancelbtn, freeEmi_Calculater_exitbtn;
    Dialog freeEmi_Calculater_goBackDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_ads_exit_activity);
        freeEmi_Calculater_cancelbtn = findViewById(R.id.cancelbtn);
        freeEmi_Calculater_exitbtn = findViewById(R.id.exitbtn);
        com.arabianmaps.compass.Compass_Adsdk.AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.FullscreenAd(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);

        freeEmi_Calculater_cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Compass_Ads_Exit_Activity.this, Compass_Second_Activity.class);
                startActivity(intent);
            }
        });

        freeEmi_Calculater_exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratedialog();
            }
        });
    }

    public void ratedialog() {
        freeEmi_Calculater_goBackDialog = new Dialog(Compass_Ads_Exit_Activity.this);
        freeEmi_Calculater_goBackDialog.requestWindowFeature(1);
        freeEmi_Calculater_goBackDialog.setContentView(R.layout.a_ads_dialog_rate);
        freeEmi_Calculater_goBackDialog.setCancelable(false);
        freeEmi_Calculater_goBackDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        (freeEmi_Calculater_goBackDialog.findViewById(R.id.nothanks)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                freeEmi_Calculater_goBackDialog.dismiss();
                finishAffinity();
            }
        });
        ((Button) freeEmi_Calculater_goBackDialog.findViewById(R.id.ratebtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));

                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        freeEmi_Calculater_goBackDialog.show();
    }

    @Override
    public void onBackPressed() {
        ratedialog();
    }
}