package com.arabianmaps.compass.Compass_Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.digitalcompass.liveweather.R;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import com.arabianmaps.compass.Compass_Compass_customCompass.Compass_A;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.arabianmaps.compass.R;

/* loaded from: classes2.dex */
public class Compass_MapFragment extends Fragment implements LocationListener {
    static double lat;
    static double latValue;
    static double latestLat;
    static double latestLng;
    static int len;
    static double lng;
    static double lngValue;
    static String[] valueData;
    static int zoomLevel;
    ImageView GeolocationPage;
    TextView anotherLatShow;
    TextView anotherLngShow;
    private Compass_A compass;
    ImageView compassImg;
    ViewFlipper contentFliper;
    Context context;
    private float currentAzimuth;
    TextView directionView;
    Geocoder geocoder;
    TextView gmtHolder;
    TextView latshow;
    private Compass_SmartCompassFrag.CompassListener listener;
    TextView lngshow;
    ImageView location;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap map;
    TextView mapDegreeDisplay;
    MapView mapView;
    List<Address> myAddress;
    TextView mylocation;
    ProgressDialog networkDialog;
    ImageView pin;
    ProgressDialog progressDialog;
    ImageView search;
    SearchView searchView;
    SensorManager sensorManager;
    ImageView share;
    ImageView smsLocation;
    TextView timeDateDisplay;
    TextView timerShower;
    ImageView types;
    ViewFlipper upDownFliiper;

    /* renamed from: v */
    View f143v;
    ImageView weatherPage;
    ImageView zoomIn;
    ImageView zoomOut;
    float degreeStart = 0.0f;
    private String TAG = "MapFragment";
    String finalAddress = null;
    boolean isMapTypeOn = true;
    boolean isFliperUp = false;
    boolean isFirstInstall = true;
    boolean isOn = false;
    LocationCallback locationCallback = new LocationCallback() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.14
        @Override // com.google.android.gms.location.LocationCallback
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            Compass_MapFragment.lat = lastLocation.getLatitude();
            Compass_MapFragment.lng = lastLocation.getLongitude();
        }
    };

    /* loaded from: classes2.dex */
    public interface CompassListener {
        void onNewAzimuth(float f);
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public void updateLocationAndLat(double d, double d2, String str) {
        if (this.isFirstInstall) {
            String str2 = this.TAG;
            Log.e(str2, "location result " + str + " lat and lng " + d + " : " + d2);
            this.finalAddress = str;
            latValue = d;
            lngValue = d2;
            latestLat = d;
            latestLng = d2;
            this.mylocation = (TextView) getActivity().findViewById(R.id.myLocation);
            this.anotherLatShow = (TextView) getActivity().findViewById(R.id.anotherLatShow);
            this.anotherLngShow = (TextView) getActivity().findViewById(R.id.anotherLngShow);
            this.mylocation.setText(this.finalAddress);
            this.anotherLatShow.setText(doubToDMS(d, true));
            this.anotherLngShow.setText(doubToDMS(d2, false));
        }
    }

    /* JADX WARN: Type inference failed for: r14v53, types: [com.digitalcompass.liveweather.Fragment.MapFragment$2] */
    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.f143v = layoutInflater.inflate(R.layout.d_layout_for_smart_map, viewGroup, false);
        setupCompass();
        this.contentFliper = (ViewFlipper) this.f143v.findViewById(R.id.locationDetailsDisplay);
        Compass_C1105Ui.setHeight(this.f143v.getContext(), this.contentFliper, 420);
        this.latshow = (TextView) this.f143v.findViewById(R.id.latShow);
        this.lngshow = (TextView) this.f143v.findViewById(R.id.lngShow);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), (TextView) this.f143v.findViewById(R.id.headingValue), 25);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), this.latshow, 25);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), this.lngshow, 25);
        this.gmtHolder = (TextView) this.f143v.findViewById(R.id.gmtHolder);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), this.gmtHolder, 55);
        Compass_C1105Ui.setWidth(this.f143v.getContext(), this.gmtHolder, 350);
        this.mapDegreeDisplay = (TextView) this.f143v.findViewById(R.id.mapDegreeDisplay);
        this.compassImg = (ImageView) this.f143v.findViewById(R.id.compassImg1);
        this.pin = (ImageView) this.f143v.findViewById(R.id.pin);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), this.compassImg, 600, 600);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), this.pin, 600, 600);
        LinearLayout linearLayout = (LinearLayout) this.f143v.findViewById(R.id.locationLayout);
        Compass_C1105Ui.setHeight(this.f143v.getContext(), linearLayout, 104);
        Compass_C1105Ui.setMarginBottom(this.f143v.getContext(), linearLayout, 20);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), linearLayout, 20);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), (ImageView) this.f143v.findViewById(R.id.ic_map_location), ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 50);
        this.anotherLatShow = (TextView) this.f143v.findViewById(R.id.anotherLatShow);
        this.anotherLngShow = (TextView) this.f143v.findViewById(R.id.anotherLngShow);
        Compass_C1105Ui.setMarginTop(getContext(), this.anotherLatShow, 25);
        Compass_C1105Ui.setMarginTop(getContext(), this.anotherLngShow, 25);
        this.timeDateDisplay = (TextView) this.f143v.findViewById(R.id.timeDateDisplay);
        Compass_C1105Ui.setWidth(getContext(), this.timeDateDisplay, 350);
        Compass_C1105Ui.setMarginTop(getContext(), this.timeDateDisplay, 55);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMM yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.timeDateDisplay.setText(simpleDateFormat.format(new Date()));
        getActivity().getWindow().setFlags(1024, 1024);
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            String str = this.TAG;
            Log.e(str, "error " + e.getMessage());
        }
        this.mapView = (MapView) this.f143v.findViewById(R.id.map);
        this.mapView.onCreate(bundle);
        this.mapView.onResume();
        this.mapView.getMapAsync(new OnMapReadyCallback() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.1
            @Override // com.google.android.gms.maps.OnMapReadyCallback
            public void onMapReady(GoogleMap googleMap) {
                Compass_MapFragment.this.map = googleMap;
                Compass_MapFragment.zoomLevel = (int) Compass_MapFragment.this.map.getCameraPosition().zoom;
                String str2 = Compass_MapFragment.this.TAG;
                Log.e(str2, "zoom level" + Compass_MapFragment.this.map.getCameraPosition().zoom);
                Compass_MapFragment.this.getLastLocation();
                new LatLng(Compass_MapFragment.lat, Compass_MapFragment.lng);
                Compass_MapFragment.this.map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.1.1
                    static final /* synthetic */ boolean $assertionsDisabled = false;

                    @Override // com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
                    @SuppressLint({"SetTextI18n"})
                    public void onCameraIdle() {
                        Compass_MapFragment.latValue = Compass_MapFragment.this.map.getCameraPosition().target.latitude;
                        Compass_MapFragment.lngValue = Compass_MapFragment.this.map.getCameraPosition().target.longitude;
                        Compass_MapFragment.this.anotherLatShow.setText(Compass_MapFragment.this.doubToDMS(Compass_MapFragment.latValue, true));
                        Compass_MapFragment.this.anotherLngShow.setText(Compass_MapFragment.this.doubToDMS(Compass_MapFragment.lngValue, false));
                        try {
                            String addresslist = Compass_MapFragment.this.getAddresslist(Compass_MapFragment.latValue, Compass_MapFragment.lngValue);
                            if (addresslist != null) {
                                Compass_MapFragment.this.finalAddress = addresslist;
                                Compass_MapFragment.this.mylocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                Compass_MapFragment.this.mylocation.setMarqueeRepeatLimit(-1);
                                Compass_MapFragment.this.mylocation.setFocusable(true);
                                Compass_MapFragment.this.mylocation.setSelected(true);
                                Compass_MapFragment.this.mylocation.setFocusableInTouchMode(true);
                                Compass_MapFragment.this.mylocation.setText(Compass_MapFragment.this.finalAddress);
                                String[] StringDivder = Compass_MapFragment.this.StringDivder(String.valueOf(Compass_MapFragment.lat));
                                Log.e(Compass_MapFragment.this.TAG, String.valueOf(StringDivder.length));
                                String str3 = Compass_MapFragment.this.TAG;
                                Log.e(str3, String.valueOf(StringDivder[0] + " " + StringDivder[1] + " " + StringDivder[2]));
                            }
                        } catch (Exception e2) {
                            String str4 = Compass_MapFragment.this.TAG;
                            Log.e(str4, "Error" + e2.getMessage());
                        }
                    }
                });
            }
        });
        final ImageView imageView = (ImageView) this.f143v.findViewById(R.id.fliperValue);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView, 153, 100);
        this.networkDialog = new ProgressDialog(getContext());
        if (!checkNetwork()) {
            this.networkDialog.setCancelable(false);
            this.networkDialog.setProgressStyle(0);
            this.networkDialog.setMessage("Please Wait....");
            this.networkDialog.show();
        } else {
            this.networkDialog.dismiss();
        }
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Please Wait ");
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setCancelable(false);
        new CountDownTimer(5000L, 1000L) { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.2
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
                Compass_MapFragment.this.getLastLocation();
                Compass_MapFragment.this.progressDialog.show();
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                Compass_MapFragment.this.progressDialog.dismiss();
            }
        }.start();
        this.searchView = (SearchView) this.f143v.findViewById(R.id.search_view);
        this.searchView.setBackgroundColor(-1);
        this.searchView.requestFocus();
        this.searchView.setFocusable(true);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.3
            @Override // android.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str2) {
                Log.e("MapsActivity submitT", str2);
                try {
                    List<Address> list = Compass_MapFragment.this.getcustomAddress(str2);
                    Compass_MapFragment.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()), 10.0f));
                } catch (IOException e2) {
                    Log.e("MapsAct errorCutm", e2.getMessage());
                }
                return false;
            }

            @Override // android.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str2) {
                Log.e("MapsActivity Search", str2);
                return false;
            }
        });
        final ConstraintLayout constraintLayout = (ConstraintLayout) this.f143v.findViewById(R.id.LayMapType);
        Compass_C1105Ui.setHeight(this.f143v.getContext(), constraintLayout, 300);
        Compass_C1105Ui.setMarginRight(this.f143v.getContext(), constraintLayout, 37);
        this.zoomIn = (ImageView) this.f143v.findViewById(R.id.ZoomIn);
        Compass_C1105Ui.setHeightWidth(getContext(), this.zoomIn, 95, 95);
        Compass_C1105Ui.setMarginTop(getContext(), this.zoomIn, 29);
        this.zoomOut = (ImageView) this.f143v.findViewById(R.id.ZoomOut);
        Compass_C1105Ui.setHeightWidth(getContext(), this.zoomOut, 95, 95);
        Compass_C1105Ui.setMarginBottom(getContext(), this.zoomOut, 29);
        this.search = (ImageView) this.f143v.findViewById(R.id.searchMap);
        Compass_C1105Ui.setHeightWidth(getContext(), this.search, 95, 95);
        Compass_C1105Ui.setMarginBottom(getContext(), this.search, 29);
        this.types = (ImageView) this.f143v.findViewById(R.id.MapType);
        Compass_C1105Ui.setHeightWidth(getContext(), this.types, 95, 95);
        Compass_C1105Ui.setMarginBottom(getContext(), this.types, 29);
        this.location = (ImageView) this.f143v.findViewById(R.id.currentLocation);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), this.location, 71);
        Compass_C1105Ui.setHeightWidth(getContext(), this.location, 95, 95);
        Compass_C1105Ui.setMarginBottom(getContext(), this.location, 29);
        final ImageView imageView2 = (ImageView) this.f143v.findViewById(R.id.img_normal_map);
        final ImageView imageView3 = (ImageView) this.f143v.findViewById(R.id.img_hybrid_map);
        final ImageView imageView4 = (ImageView) this.f143v.findViewById(R.id.img_terrian_map);
        final ImageView imageView5 = (ImageView) this.f143v.findViewById(R.id.img_satelite_map);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), imageView2, 167, 59);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), imageView3, 167, 59);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), imageView3, 20);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), imageView4, 167, 59);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), imageView4, 20);
        Compass_C1105Ui.setHeightWidth(this.f143v.getContext(), imageView5, 167, 59);
        Compass_C1105Ui.setMarginTop(this.f143v.getContext(), imageView5, 20);
        this.types.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Compass_MapFragment.this.isMapTypeOn) {
                    constraintLayout.setVisibility(4);
                    Compass_MapFragment.this.isMapTypeOn = false;
                    return;
                }
                Compass_MapFragment.this.isMapTypeOn = true;
                constraintLayout.setVisibility(0);
                imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.4.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Compass_MapFragment.this.map.setMapType(1);
                    }
                });
                imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.4.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Compass_MapFragment.this.map.setMapType(4);
                    }
                });
                imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.4.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Compass_MapFragment.this.map.setMapType(3);
                    }
                });
                imageView5.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.4.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Compass_MapFragment.this.map.setMapType(2);
                    }
                });
            }
        });
        this.smsLocation = (ImageView) this.f143v.findViewById(R.id.smsLocation);
        Compass_C1105Ui.setHeightWidth(getContext(), this.smsLocation, 106, 106);
        Compass_C1105Ui.setMarginsAsWidth(getContext(), this.smsLocation, 40, 20, 0, 50);
        this.smsLocation.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.5
            @Override // android.view.View.OnClickListener
            @SuppressLint({"IntentReset"})
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "\t\t\t\t\t\t\t\tSharing location \n");
                intent.putExtra("android.intent.extra.TEXT", Compass_MapFragment.this.finalAddress + "\n https://www.google.com/maps/@" + Compass_MapFragment.latestLat + "," + Compass_MapFragment.latestLng);
                Compass_MapFragment.this.startActivity(Intent.createChooser(intent, "Share Location"));
            }
        });
        this.search.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Log.e("MapsAct Search", "clicked");
                if (!Compass_MapFragment.this.isOn) {
                    String str2 = Compass_MapFragment.this.TAG;
                    Log.e(str2, "Search View on/off " + Compass_MapFragment.this.isOn);
                    Compass_MapFragment.this.searchView.setVisibility(0);
                    Compass_MapFragment.this.searchView.requestFocus();
                    Compass_MapFragment.this.searchView.setFocusableInTouchMode(true);
                    Compass_MapFragment.this.searchView.setIconifiedByDefault(false);
                    ((InputMethodManager) Compass_MapFragment.this.getActivity().getSystemService("input_method")).showSoftInput(Compass_MapFragment.this.searchView, 0);
                    Compass_MapFragment.this.isOn = true;
                    return;
                }
                String str3 = Compass_MapFragment.this.TAG;
                Log.e(str3, "Search View on/off " + Compass_MapFragment.this.isOn);
                Compass_MapFragment.this.searchView.setVisibility(8);
                Compass_MapFragment.this.searchView.setFocusable(false);
                Compass_MapFragment.this.isOn = false;
            }
        });
        this.location.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_MapFragment.this.getLastLocation();
                CameraUpdate newLatLngZoom = CameraUpdateFactory.newLatLngZoom(new LatLng(Compass_MapFragment.lat, Compass_MapFragment.lng), 15.0f);
                Compass_MapFragment.zoomLevel = 15;
                Compass_MapFragment.this.map.moveCamera(newLatLngZoom);
            }
        });
        this.zoomIn.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_MapFragment.zoomLevel += 5;
                if (Compass_MapFragment.zoomLevel >= 15) {
                    Compass_MapFragment.zoomLevel = (int) Compass_MapFragment.this.map.getCameraPosition().zoom;
                    Log.e(Compass_MapFragment.this.TAG, "zoom level" + Compass_MapFragment.this.map.getCameraPosition().zoom);
                    return;
                }
                Compass_MapFragment.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Compass_MapFragment.lat, Compass_MapFragment.lng), Compass_MapFragment.zoomLevel));
                Log.e(Compass_MapFragment.this.TAG, "zoom level" + Compass_MapFragment.this.map.getCameraPosition().zoom);
                Compass_MapFragment.this.map.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        this.zoomOut.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_MapFragment.zoomLevel -= 5;
                if (Compass_MapFragment.zoomLevel <= 0) {
                    Compass_MapFragment.zoomLevel = (int) Compass_MapFragment.this.map.getCameraPosition().zoom;
                    Log.e(Compass_MapFragment.this.TAG, "zoom level" + Compass_MapFragment.this.map.getCameraPosition().zoom);
                    return;
                }
                Compass_MapFragment.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Compass_MapFragment.lat, Compass_MapFragment.lng), Compass_MapFragment.zoomLevel));
                Compass_MapFragment.this.map.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        this.timerShower = (TextView) this.f143v.findViewById(R.id.timeShower);
        this.directionView = (TextView) this.f143v.findViewById(R.id.directionView);
        Compass_C1105Ui.setMarginTop(getContext(), this.directionView, 25);
        this.mylocation = (TextView) this.f143v.findViewById(R.id.myLocation);
        Compass_C1105Ui.setPaddingLeft(getContext(), this.mylocation, 40);
        Compass_C1105Ui.setWidth(getContext(), this.mylocation, 871);
        Compass_C1105Ui.setWidth(this.f143v.getContext(), this.timerShower, 350);
        Compass_C1105Ui.setMarginTop(getContext(), this.timerShower, 55);
        this.sensorManager = (SensorManager) getActivity().getSystemService("sensor");
        this.geocoder = new Geocoder(getContext(), Locale.getDefault());
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.10
            @Override // android.view.View.OnClickListener
            @SuppressLint({"SetTextI18n"})
            public void onClick(View view) {
                if (!Compass_MapFragment.this.isFliperUp) {
                    Compass_MapFragment.this.contentFliper.setVisibility(0);
                    imageView.setImageResource(R.drawable.c_digital_up);
                    Compass_MapFragment.this.isFliperUp = true;
                } else if (Compass_MapFragment.this.isFliperUp) {
                    imageView.setImageResource(R.drawable.c_digital_down);
                    Compass_MapFragment.this.contentFliper.setVisibility(8);
                    Compass_MapFragment.this.isFliperUp = false;
                } else {
                    Log.e("No operation", "for fliper ");
                }
            }
        });
        new Thread(new GMTCountDown()).start();
        getGMTTime();
        new Thread(new countDownTimer()).start();
        this.mylocation.setText(this.finalAddress);
        this.anotherLatShow.setText(doubToDMS(lat, true));
        this.anotherLngShow.setText(doubToDMS(lng, false));
        getDate();
        getLastLocation();
        return this.f143v;
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
        return (z ? d < 0.0d ? "S" : "N" : d < 0.0d ? "W" : "E") + " " + i + "° " + i2 + "' " + i3;
    }

    public void getTime() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.11
            @Override // java.lang.Runnable
            @SuppressLint({"SetTextI18n"})
            public void run() {
                Date date = new Date();
                int hours = date.getHours();
                int minutes = date.getMinutes();
                int seconds = date.getSeconds();
                TextView textView = Compass_MapFragment.this.timerShower;
                textView.setText("Local : " + hours + ":" + minutes + ":" + seconds);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.compass.start();
        if (new Compass_A(getContext()).isExit) {
            getActivity().finish();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.compass.stop();
    }

    public void getDate() {
        Log.e("Date is ", new SimpleDateFormat("", Locale.getDefault()).format(Calendar.getInstance().getTime()));
    }

    public boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        return connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 1);
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public List<Address> getcustomAddress(String str) throws IOException {
        List<Address> fromLocationName = this.geocoder.getFromLocationName(str, 1);
        if (fromLocationName != null) {
            Log.e("MapsAct cust lat ", String.valueOf(fromLocationName.get(0).getLatitude()));
            Log.e("MapsAct cust lng ", String.valueOf(fromLocationName.get(0).getLongitude()));
            return fromLocationName;
        }
        return null;
    }

    public String getAddresslist(double d, double d2) throws IOException {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(3);
        double parseDouble = Double.parseDouble(decimalFormat.format(d));
        double parseDouble2 = Double.parseDouble(decimalFormat.format(d2));
        String str = this.TAG;
        Log.e(str, "Geocode values " + Geocoder.isPresent());
        this.myAddress = this.geocoder.getFromLocation(parseDouble, parseDouble2, 1);
        List<Address> list = this.myAddress;
        if (list != null) {
            String addressLine = list.get(0).getAddressLine(0);
            String str2 = this.TAG;
            Log.e(str2, "Address is " + this.myAddress.get(0).getAddressLine(0));
            return addressLine;
        }
        return null;
    }

    public void getGMTTime() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.12
            @Override // java.lang.Runnable
            @SuppressLint({"SetTextI18n"})
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String format = simpleDateFormat.format(new Date());
                TextView textView = Compass_MapFragment.this.gmtHolder;
                textView.setText("GMT " + format);
            }
        });
    }

    public String[] StringDivder(String str) {
        len = str.length();
        int i = len;
        valueData = new String[i];
        if (i == 0) {
            return null;
        }
        int i2 = 0;
        while (true) {
            int i3 = len;
            if (i2 < i3) {
                valueData[i2] = str.substring(i2, i3);
                i2++;
            } else {
                return valueData;
            }
        }
    }

    public void getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnable()) {
                this.mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.13
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    @SuppressLint({"SetTextI18n"})
                    public void onComplete(@NonNull Task<Location> task) {
                        Location result = task.getResult();
                        if (result == null) {
                            Compass_MapFragment.this.requestNewLocation();
                            return;
                        }
                        Compass_MapFragment.lat = result.getLatitude();
                        Compass_MapFragment.lng = result.getLongitude();
                        LatLng latLng = new LatLng(Compass_MapFragment.lat, Compass_MapFragment.lng);
                        Compass_MapFragment.this.latshow.setText(Compass_MapFragment.this.doubToDMS(Compass_MapFragment.lat, true));
                        Compass_MapFragment.this.lngshow.setText(Compass_MapFragment.this.doubToDMS(Compass_MapFragment.lng, false));
                        Compass_MapFragment.this.anotherLatShow.setText(Compass_MapFragment.this.doubToDMS(Compass_MapFragment.lat, true));
                        Compass_MapFragment.this.anotherLngShow.setText(Compass_MapFragment.this.doubToDMS(Compass_MapFragment.lng, false));
                        try {
                            String addresslist = Compass_MapFragment.this.getAddresslist(Compass_MapFragment.lat, Compass_MapFragment.lng);
                            if (addresslist != null) {
                                Compass_MapFragment.this.finalAddress = addresslist;
                                Compass_MapFragment.this.mylocation.setText(addresslist);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MarkerOptions title = new MarkerOptions().position(latLng).title("Current Location");
                        Compass_MapFragment mapFragment = Compass_MapFragment.this;
                        title.icon(mapFragment.bitmapDescriptorFromVector(mapFragment.f143v.getContext(), R.drawable.c_digital_pin));
                        Compass_MapFragment.this.map.addMarker(title);
                        Compass_MapFragment.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    }
                });
                return;
            } else {
                Log.e(this.TAG, "not Enable Location");
                return;
            }
        }
        Log.e(this.TAG, "not Enable Permission");
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int i) {
        Drawable drawable = ContextCompat.getDrawable(context.getApplicationContext(), i);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        drawable.draw(new Canvas(createBitmap));
        return BitmapDescriptorFactory.fromBitmap(createBitmap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        ProgressDialog progressDialog;
        ProgressDialog progressDialog2 = this.progressDialog;
        if ((progressDialog2 == null || !progressDialog2.isShowing()) && ((progressDialog = this.networkDialog) == null || !progressDialog.isShowing())) {
            return;
        }
        this.progressDialog.dismiss();
        this.networkDialog.dismiss();
    }

    public void requestNewLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        locationRequest.setInterval(5L);
        locationRequest.setFastestInterval(1L);
        checkPermission();
        this.mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    public boolean isLocationEnable() {
        this.locationManager = (LocationManager) getContext().getSystemService("location");
        return this.locationManager.isProviderEnabled("gps") || this.locationManager.isProviderEnabled("network");
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(@NonNull Location location) {
        Log.e("changed Location ", "lat " + location.getLatitude() + " lng " + location.getLongitude());
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(@NonNull String str) {
        Log.e("enable ", str + " is enable ");
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(@NonNull String str) {
        Log.e("disable ", str + " is disable ");
    }

    private void setupCompass() {
        this.compass = new Compass_A(this.f143v.getContext());
        this.compass.setListener(new Compass_A.CompassListener() { // from class: com.digitalcompass.liveweather.Fragment.MapFragment.15
            @Override // com.digitalcompass.liveweather.customCompass.Compass.CompassListener
            public void onNewAzimuth(float f) {
                Compass_MapFragment.this.adjustArrow(f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustArrow(float f) {
        String str;
        RotateAnimation rotateAnimation = new RotateAnimation(-this.currentAzimuth, -f, 1, 0.5f, 1, 0.5f);
        this.currentAzimuth = f;
        String str2 = ((int) this.currentAzimuth) + "";
        float f2 = this.currentAzimuth;
        if (f2 == 0.0f || f2 == 360.0f) {
            str = "N";
        } else if (f2 <= 0.0f || f2 >= 90.0f) {
            float f3 = this.currentAzimuth;
            if (f3 == 90.0f) {
                str = "E";
            } else if (f3 <= 90.0f || f3 >= 180.0f) {
                float f4 = this.currentAzimuth;
                if (f4 == 180.0f) {
                    str = "S";
                } else if (f4 <= 180.0f || f4 >= 270.0f) {
                    float f5 = this.currentAzimuth;
                    str = f5 == 270.0f ? "W" : (f5 <= 270.0f || f5 >= 360.0f) ? "Unknown" : "NW";
                } else {
                    str = "SW";
                }
            } else {
                str = "SE";
            }
        } else {
            str = "NE";
        }

        String firstDirection = str2;
        switch (str2) {
            case "N":
                firstDirection = "شمال";
                break;
            case "S":
                firstDirection = "جنوب";
                break;
            case "E":
                firstDirection = "شرق";
                break;
            case "W":
                firstDirection = "غرب";
                break;
            case "NW":
                firstDirection = "شمال غرب";
                break;
            case "NE":
                firstDirection = "شمال شرق";
                break;
            case "SW":
                firstDirection = "جنوب غرب";
                break;
            case "SE":
                firstDirection = "جنوب شرق";
                break;

        }

        String secondDirection = str;
        switch (str) {
            case "N":
                secondDirection = "شمال";
                break;
            case "S":
                secondDirection = "جنوب";
                break;
            case "E":
                secondDirection = "شرق";
                break;
            case "W":
                secondDirection = "غرب";
                break;
            case "NW":
                secondDirection = "شمال غرب";
                break;
            case "NE":
                secondDirection = "شمال شرق";
                break;
            case "SW":
                secondDirection = "جنوب غرب";
                break;
            case "SE":
                secondDirection = "جنوب شرق";
                break;

        }


        this.directionView.setText(firstDirection + "° " + secondDirection);
        rotateAnimation.setDuration(500L);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        this.compassImg.startAnimation(rotateAnimation);
    }

    /* loaded from: classes2.dex */
    class countDownTimer implements Runnable {
        countDownTimer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                Thread.currentThread();
                if (Thread.interrupted()) {
                    return;
                }
                try {
                    Compass_MapFragment.this.getTime();
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    class GMTCountDown implements Runnable {
        GMTCountDown() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                Thread.currentThread();
                if (Thread.interrupted()) {
                    return;
                }
                Compass_MapFragment.this.getGMTTime();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
