package com.arabianmaps.compass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
//import com.ayoubfletcher.consentsdk.ConsentSDK;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdLoader;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.VideoOptions;
//import com.google.android.gms.ads.formats.NativeAdOptions;
//import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.arabianmaps.compass.Compass_Adsdk.AdAdmob;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import com.arabianmaps.compass.Compass_ViewPageAdapter.Compass_MyAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Compass_FragmentScreen extends AppCompatActivity {
    static double lat;
    static double lng;
    //    public static UnifiedNativeAd unifiedNativeAd1;
    private FrameLayout adContainerView;
    //    private AdLoader adLoader;
//    AdView adView;
    List<Address> addressList;
    View bgView;
    LinearLayout bottomLayout;
    ImageView compassPage;
    View compassView;
    Context context;
    View geoView;
    Geocoder geocoder;
    ImageView geolocationPage;
    //    InterstitialAd interstitialAd;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    ImageView mapPage;
    View mapView;
    LinearLayout menuLayout;
    ConstraintLayout middleDivider;
    Compass_MyAdapter myAdapter;
    ViewPager viewPager;
    ImageView weatherPage;
    View weatherView;
    private final String TAG = "Fragment Screen";
    boolean isLocationOn = false;
    LocationCallback locationCallback = new LocationCallback() { // from class: com.digitalcompass.liveweather.FragmentScreen.6
        @Override // com.google.android.gms.location.LocationCallback
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            Compass_FragmentScreen.lat = lastLocation.getLatitude();
            Compass_FragmentScreen.lng = lastLocation.getLongitude();
            try {
                String addressList = Compass_FragmentScreen.this.getAddressList(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng);
                Compass_FragmentScreen.this.myAdapter.getSmartCompassFrag().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, addressList);
                Compass_FragmentScreen.this.myAdapter.getMapFragment().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, addressList);
                Compass_FragmentScreen.this.myAdapter.getGeoLocationFragment().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, addressList);
                Log.e("Fragment Screen", "LAT AND LNG " + Compass_FragmentScreen.lat + " : " + Compass_FragmentScreen.lng);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private ActivityResultLauncher<Intent> locationSettingsLauncher  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // Check if location is enabled
            if (isLocationEnable()) {
               recreate();
            }
            else {
                starter();
            }
        }
    }
        );

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.d_activity_fragment_screen);

        this.context = this;
        init();
        ReSize();
        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.FullscreenAd(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        this.myAdapter = new Compass_MyAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.myAdapter);
        this.viewPager.setOffscreenPageLimit(4);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        this.geocoder = new Geocoder(this, Locale.getDefault());
        getLastKnowLocation();
        this.compassPage.setImageResource(R.drawable.c_digital_compass_press);
        clickListener();

        starter();

    }

    private  void starter(){

        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() { // from class: com.digitalcompass.liveweather.FragmentScreen.9
                @Override // com.google.android.gms.tasks.OnCompleteListener
                @SuppressLint({"SetTextI18n"})
                public void onComplete(@NonNull Task<Location> task) {
                    Location result = task.getResult();
                    if (result != null) {
                        String str = null;
                        Compass_FragmentScreen.this.doubToDMS(result.getLatitude(), true);
                        Compass_FragmentScreen.this.doubToDMS(result.getLongitude(), true);
                        try {
                            str = Compass_FragmentScreen.this.getAddressList(result.getLatitude(), result.getLongitude());
                            if (str != null) {
                                Compass_FragmentScreen.this.myAdapter.getSmartCompassFrag().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str);
                                Compass_FragmentScreen.this.myAdapter.getMapFragment().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str);
                                Log.e("Fragment Screen", "location update ");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Compass_FragmentScreen.lat = result.getLatitude();
                        Compass_FragmentScreen.lng = result.getLongitude();
                        String str2 = str;
                        Compass_FragmentScreen.this.myAdapter.getSmartCompassFrag().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str2);
                        Compass_FragmentScreen.this.myAdapter.getMapFragment().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str2);
                        Compass_FragmentScreen.this.myAdapter.getGeoLocationFragment().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str2);
                        Compass_FragmentScreen.this.doubToDMS(Compass_FragmentScreen.lat, true);
                        Compass_FragmentScreen.this.doubToDMS(Compass_FragmentScreen.lng, false);
                        return;
                    }
                    Compass_FragmentScreen.this.requestNewLocation();
                }
            });
        }


    }

    private void clickListener() {
        this.compassPage.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.FragmentScreen.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_FragmentScreen.this.viewPager.setCurrentItem(0);
                Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_press);
                Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_unpress);
                Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_unpress);
                Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_unpress);
                Compass_FragmentScreen.this.compassView.setVisibility(0);
                Compass_FragmentScreen.this.mapView.setVisibility(4);
                Compass_FragmentScreen.this.weatherView.setVisibility(4);
                Compass_FragmentScreen.this.geoView.setVisibility(4);
            }
        });
        this.mapPage.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.FragmentScreen.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {


                Compass_FragmentScreen.this.viewPager.setCurrentItem(1);
                Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_unpress);
                Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_unpress);
                Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_press);
                Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_unpress);
                Compass_FragmentScreen.this.compassView.setVisibility(4);
                Compass_FragmentScreen.this.mapView.setVisibility(0);
                Compass_FragmentScreen.this.weatherView.setVisibility(4);
                Compass_FragmentScreen.this.geoView.setVisibility(4);


            }
        });
        this.weatherPage.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.FragmentScreen.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_FragmentScreen.this.viewPager.setCurrentItem(2);
                Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_unpress);
                Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_press);
                Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_unpress);
                Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_unpress);
                Compass_FragmentScreen.this.compassView.setVisibility(4);
                Compass_FragmentScreen.this.mapView.setVisibility(4);
                Compass_FragmentScreen.this.weatherView.setVisibility(0);
                Compass_FragmentScreen.this.geoView.setVisibility(4);
            }
        });
        this.geolocationPage.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.FragmentScreen.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {


                Compass_FragmentScreen.this.viewPager.setCurrentItem(3);
                Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_unpress);
                Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_unpress);
                Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_unpress);
                Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_press);
                Compass_FragmentScreen.this.compassView.setVisibility(4);
                Compass_FragmentScreen.this.mapView.setVisibility(4);
                Compass_FragmentScreen.this.weatherView.setVisibility(4);
                Compass_FragmentScreen.this.geoView.setVisibility(0);


            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.digitalcompass.liveweather.FragmentScreen.5
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                if (i == 0) {
                    Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_press);
                    Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_unpress);
                    Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_unpress);
                    Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_unpress);
                    Compass_FragmentScreen.this.compassView.setVisibility(0);
                    Compass_FragmentScreen.this.mapView.setVisibility(4);
                    Compass_FragmentScreen.this.weatherView.setVisibility(4);
                    Compass_FragmentScreen.this.geoView.setVisibility(4);
                } else if (i == 1) {
                    Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_unpress);
                    Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_unpress);
                    Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_press);
                    Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_unpress);
                    Compass_FragmentScreen.this.compassView.setVisibility(4);
                    Compass_FragmentScreen.this.mapView.setVisibility(0);
                    Compass_FragmentScreen.this.weatherView.setVisibility(4);
                    Compass_FragmentScreen.this.geoView.setVisibility(4);
                } else if (i == 2) {
                    Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_unpress);
                    Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_press);
                    Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_unpress);
                    Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_unpress);
                    Compass_FragmentScreen.this.compassView.setVisibility(4);
                    Compass_FragmentScreen.this.mapView.setVisibility(4);
                    Compass_FragmentScreen.this.weatherView.setVisibility(0);
                    Compass_FragmentScreen.this.geoView.setVisibility(4);
                } else if (i == 3) {
                    Compass_FragmentScreen.this.compassPage.setImageResource(R.drawable.c_digital_compass_unpress);
                    Compass_FragmentScreen.this.weatherPage.setImageResource(R.drawable.c_digital_weather_unpress);
                    Compass_FragmentScreen.this.mapPage.setImageResource(R.drawable.c_digital_map_unpress);
                    Compass_FragmentScreen.this.geolocationPage.setImageResource(R.drawable.c_digital_geo_press);
                    Compass_FragmentScreen.this.compassView.setVisibility(4);
                    Compass_FragmentScreen.this.mapView.setVisibility(4);
                    Compass_FragmentScreen.this.weatherView.setVisibility(4);
                    Compass_FragmentScreen.this.geoView.setVisibility(0);
                } else {
                    Log.e("Fragment Screen", "No Fragment and wrong choice ");
                }
            }
        });


    }

    private void ReSize() {
        Compass_C1105Ui.setMarginTop(this.context, this.middleDivider, 10);
        Compass_C1105Ui.setHeight(this.context, this.bgView, 4);
        Compass_C1105Ui.setWidth(this.context, this.compassView, 270);
        Compass_C1105Ui.setWidth(this.context, this.mapView, 270);
        Compass_C1105Ui.setWidth(this.context, this.weatherView, 270);
        Compass_C1105Ui.setWidth(this.context, this.geoView, 270);
        Compass_C1105Ui.setMarginTop(this.context, this.menuLayout, 20);
        Compass_C1105Ui.setMarginBottom(this.context, this.menuLayout, 41);
        Compass_C1105Ui.setHeight(this.context, this.bottomLayout, 263);
        Compass_C1105Ui.setHeightWidth(this.context, this.compassPage, 216, 216);
        Compass_C1105Ui.setHeightWidth(this.context, this.mapPage, 216, 216);
        Compass_C1105Ui.setHeightWidth(this.context, this.weatherPage, 216, 216);
        Compass_C1105Ui.setHeightWidth(this.context, this.geolocationPage, 216, 216);
    }

    private void init() {
        this.compassPage = (ImageView) findViewById(R.id.compassPage);
        this.mapPage = (ImageView) findViewById(R.id.mapPage);
        this.weatherPage = (ImageView) findViewById(R.id.weatherPage);
        this.geolocationPage = (ImageView) findViewById(R.id.geoLocationPage);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.viewPager.setOffscreenPageLimit(1);
        this.compassView = findViewById(R.id.compassView);
        this.mapView = findViewById(R.id.mapView);
        this.weatherView = findViewById(R.id.weatherView);
        this.geoView = findViewById(R.id.geoView);
        this.bgView = findViewById(R.id.bgView);
        this.menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
        this.bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        this.middleDivider = (ConstraintLayout) findViewById(R.id.middleDivider);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        Log.e("Fragment Screen", "on Start Activity ");
        requestNewLocation();
        getLastKnowLocation();
    }

    @Override
    // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            try {
                if (iArr[0] == 0 && iArr.length > 0) {
                    if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
                        requestPermission();
                        return;
                    }
                    Log.e("Fragment Screen", "getting location ");
                    getLastKnowLocation();
                    return;
                }
            } catch (Exception e) {
                Log.e("Fragment Screen", e.getMessage());
                return;
            }
        }
        requestPermission();
        Log.e("no Granted ", "permission  " + iArr[0]);
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        locationRequest.setInterval(10L);
        locationRequest.setFastestInterval(0L);
        checkPermission();
        this.mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    public String getAddressList(double d, double d2) throws IOException {
        this.addressList = this.geocoder.getFromLocation(d, d2, 1);
        if (this.addressList != null) {
            Log.e("Fragment Screen", "Address is :" + this.addressList.get(0).getAddressLine(0));
            return this.addressList.get(0).getAddressLine(0);
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    private void getLastKnowLocation() {
        Log.e("Fragment Screen", "check permission value " + checkPermission());
        if (checkPermission()) {
            Log.e("Fragment Screen", "check location value " + isLocationEnable());
            if (isLocationEnable()) {
                this.isLocationOn = true;
                this.mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() { // from class: com.digitalcompass.liveweather.FragmentScreen.7
                    @Override // com.google.android.gms.tasks.OnSuccessListener
                    public void onSuccess(Location location) {
                    }
                });
                this.mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() { // from class: com.digitalcompass.liveweather.FragmentScreen.8
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    @SuppressLint({"SetTextI18n"})
                    public void onComplete(@NonNull Task<Location> task) {
                        String str;
                        Location result = task.getResult();
                        if (result != null) {
                            String str2 = null;
                            Compass_FragmentScreen.this.doubToDMS(result.getLatitude(), true);
                            Compass_FragmentScreen.this.doubToDMS(result.getLongitude(), true);
                            try {
                                str2 = Compass_FragmentScreen.this.getAddressList(result.getLatitude(), result.getLongitude());
                                if (str2 != null) {
                                    Compass_FragmentScreen.this.myAdapter.getSmartCompassFrag().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str2);
                                    Compass_FragmentScreen.this.myAdapter.getMapFragment().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str2);
                                }
                                str = str2;
                            } catch (IOException e) {
                                e.printStackTrace();
                                str = str2;
                            }
                            Compass_FragmentScreen.lat = result.getLatitude();
                            Compass_FragmentScreen.lng = result.getLongitude();
                            Compass_FragmentScreen.this.myAdapter.getSmartCompassFrag().updateLocationAndLat(Compass_FragmentScreen.lat, Compass_FragmentScreen.lng, str);
                            Compass_FragmentScreen.this.doubToDMS(Compass_FragmentScreen.lat, true);
                            Compass_FragmentScreen.this.doubToDMS(Compass_FragmentScreen.lng, false);
                            return;
                        }
                        Compass_FragmentScreen.this.requestNewLocation();
                    }
                });
                Log.e("Fragment Screen", "location is disable");
                return;
            }
            if (this.isLocationOn) {
                getLastKnowLocation();
            } else {
//                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                locationSettingsLauncher.launch(intent);
            }
            Log.e("Fragment Screen", "location is enable");
            return;
        }
        requestPermission();
        Log.e("Fragment Screen", "Location denied  ");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String doubToDMS(double d, boolean z) {
        double abs = Math.abs(d);
        int i = (int) abs;
        double d2 = i;
        Double.isNaN(d2);
        double d3 = (abs - d2) * 60.0d;
        int i2 = (int) d3;
        double d4 = i2;
        Double.isNaN(d4);
        int i3 = (int) ((d3 - d4) * 60.0d);
        return (z ? d < 0.0d ? "S" : "N" : d < 0.0d ? "W" : "E") + "\" " + i + "° " + i2 + "' " + i3;
    }

    public boolean isLocationEnable() {
        this.locationManager = (LocationManager) getSystemService("location");
        return this.locationManager.isProviderEnabled("gps") || this.locationManager.isProviderEnabled("network");
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressLint("MissingPermission")
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 1);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
//        if (this.interstitialAd.isLoaded()) {
//            this.interstitialAd.setAdListener(new AdListener() { // from class: com.digitalcompass.liveweather.FragmentScreen.10
//                @Override // com.google.android.gms.ads.AdListener
//                public void onAdClosed() {
//                    Compass_FragmentScreen fragmentScreen = Compass_FragmentScreen.this;
//                    fragmentScreen.startActivity(new Intent(fragmentScreen, Compass_Second_Activity.class));
        super.onBackPressed();
        finish();
//                    FragmentScreen.this.loadInterstitial();
//                }
//            });
//            this.interstitialAd.show();
//            return;
//        }
//        startActivity(new Intent(this, SplashActivity.class));
    }


}
