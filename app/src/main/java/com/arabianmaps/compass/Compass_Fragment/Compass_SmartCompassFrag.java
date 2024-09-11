package com.arabianmaps.compass.Compass_Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.kobakei.ratethisapp.RateThisApp;
import com.arabianmaps.compass.R;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import com.arabianmaps.compass.Compass_Compass_customCompass.Compass_A;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Compass_SmartCompassFrag extends Fragment {
    private static String TAG = "SmartCompass";
    static int cnt;
    static double flat;
    static double flng;
    static double lat;
    static double lng;
    public static TextView magneticHolder;
    String FinalLocationResult;
    //    TextView addressHolder;
    List<Address> addressList;
    private float azimuth;
    private float azimuthFix;
    private Compass_A compass;
    ImageView compassImg, quibilaImg;
    private float currentAzimuth, pastRotate = 0;
    TextView degreeDisplay;
    Geocoder geocoder;
    TextView latHolder;
    private CompassListener listener;
    TextView lngHolder;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    SensorManager magneticSensor;
    SensorManager sensorManager;
    TextView speedTxt, altitudeTxt, distanceToMTxt;
    /* renamed from: v */ View f146v;
    float degreeStart = 0.0f;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];

    /* renamed from: R1 */
    private float[] f145R1 = new float[9];

    /* renamed from: I */
    private float[] f144I = new float[9];
    boolean isFirstInstall = true;
    final float alpha = 0.97f;
    Location meccaLocation = new Location("mecca");


    LocationCallback locationCallback = new LocationCallback() { // from class: com.digitalcompass.liveweather.Fragment.SmartCompassFrag.3
        @Override // com.google.android.gms.location.LocationCallback
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location lastLocation = locationResult.getLastLocation();
            Compass_SmartCompassFrag.lat = lastLocation.getLatitude();
            Compass_SmartCompassFrag.this.latHolder.setText(String.valueOf(Compass_SmartCompassFrag.lat));
            Compass_SmartCompassFrag.lng = lastLocation.getLongitude();
            Compass_SmartCompassFrag.this.lngHolder.setText(String.valueOf(Compass_SmartCompassFrag.lng));

        }
    };

    /* loaded from: classes2.dex */
    public interface CompassListener {
        void onNewAzimuth(float f);
    }

    public void updateLocationAndLat(double d, double d2, String str) {
        if (this.isFirstInstall) {
            flat = d;
            flng = d2;
            this.FinalLocationResult = str;
//            this.addressHolder = (TextView) getActivity().findViewById(R.id.addressHolder);
            this.latHolder = (TextView) getActivity().findViewById(R.id.latHolder);
            this.lngHolder = (TextView) getActivity().findViewById(R.id.lngHolder);
//            this.addressHolder.setText(this.FinalLocationResult);
            this.latHolder.setText(doubToDMS(flat, true));
            this.lngHolder.setText(doubToDMS(flng, false));
            String str2 = TAG;
            Log.e(str2, "LAT VALUE " + d + " lng value " + d2 + " : LOCATION RESULT " + this.FinalLocationResult);


        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.f146v = layoutInflater.inflate(R.layout.d_layout_of_smart_fragment_layout, viewGroup, false);
        Log.d("sachin", "On create View ");
        setupCompass();
//        RateThisApp.init(new RateThisApp.Config(5, 10));
//        RateThisApp.onCreate(this.f146v.getContext());
//        RateThisApp.showRateDialogIfNeeded(this.f146v.getContext());
        meccaLocation.setLatitude(21.4225);  // Latitude of Mecca
        meccaLocation.setLongitude(39.8262); // Longitude of Mecca

        this.latHolder = (TextView) this.f146v.findViewById(R.id.latHolder);
        this.lngHolder = (TextView) this.f146v.findViewById(R.id.lngHolder);
        this.altitudeTxt = (TextView) this.f146v.findViewById(R.id.altitudeTxt);
        this.distanceToMTxt = (TextView) this.f146v.findViewById(R.id.distanceToMTxt);
        this.speedTxt = (TextView) this.f146v.findViewById(R.id.speedTxt);
        this.lngHolder = (TextView) this.f146v.findViewById(R.id.lngHolder);
        this.sensorManager = (SensorManager) getContext().getSystemService("sensor");
        this.magneticSensor = (SensorManager) getContext().getSystemService("sensor");
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        this.geocoder = new Geocoder(getContext(), Locale.getDefault());
        this.degreeDisplay = (TextView) this.f146v.findViewById(R.id.degreeDisplay);

        this.compassImg = this.f146v.findViewById(R.id.compassImg);
        this.quibilaImg = this.f146v.findViewById(R.id.quibilaImg);

//        LinearLayout linearLayout2 = this.f146v.findViewById(R.id.layAddressHolder);

        ImageView imageView = (ImageView) this.f146v.findViewById(R.id.shareImg);

        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.SmartCompassFrag.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Sharing location ");
                intent.putExtra("android.intent.extra.TEXT", Compass_SmartCompassFrag.this.FinalLocationResult + " https://www.google.com/maps/@" + Compass_SmartCompassFrag.flat + "," + Compass_SmartCompassFrag.flng);
                Compass_SmartCompassFrag.this.startActivity(Intent.createChooser(intent, "Share Location"));
            }
        });
//        this.addressHolder = (TextView) this.f146v.findViewById(R.id.addressHolder);
//        this.addressHolder.setText(this.FinalLocationResult);
        this.latHolder.setText(doubToDMS(flat, true));
        this.lngHolder.setText(doubToDMS(flng, false));
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ");
        progressDialog.setProgressStyle(0);
        progressDialog.setCancelable(false);
        ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.setMessage("Please Wait ");
        progressDialog2.setProgressStyle(0);
        progressDialog2.setCancelable(false);
        this.latHolder = this.f146v.findViewById(R.id.latHolder);
        this.lngHolder = this.f146v.findViewById(R.id.lngHolder);
        Log.d("sachin", "On create View ");
        getLastKnowLocation();
//        Compass_C1105Ui.setHeightWidth(this.f146v.getContext(), this.latHolder, 216, 216);
//        Compass_C1105Ui.setHeightWidth(this.f146v.getContext(), this.lngHolder, 216, 216);
//        Compass_C1105Ui.setHeightWidth(this.f146v.getContext(), this.addressHolder, 937, 79);
//        Compass_C1105Ui.setHeightWidth(getContext(), imageView, 106, 106);
//        Compass_C1105Ui.setMarginTop(getContext(), linearLayout2, 40);
//        Compass_C1105Ui.setMarginBottom(getContext(), linearLayout2, 20);
//        Compass_C1105Ui.setHeightWidth(getContext(), linearLayout2, 1076, 104);
//        Compass_C1105Ui.setHeightWidth(getContext(), this.f146v.findViewById(R.id.ic_location), ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 50);
//        Compass_C1105Ui.setHeightWidth(getContext(), this.compassImg, 1080, 1080);
//        Compass_C1105Ui.setHeightWidth(getContext(), this.f146v.findViewById(R.id.img_stable_cmt), 1080, 1080);
//        Compass_C1105Ui.setMarginBottom(this.f146v.getContext(), (LinearLayout) this.f146v.findViewById(R.id.layout_Title), 87);
//        Compass_C1105Ui.setMarginTop(getContext(), this.f146v.findViewById(R.id.linear), 20);
//        Compass_C1105Ui.setHeightWidth(getContext(), this.f146v.findViewById(R.id.Laycompass), 1080, 1080);
        return this.f146v;
    }

    public String getAddressList(double d, double d2) throws IOException {
        this.addressList = this.geocoder.getFromLocation(d, d2, 1);
        List<Address> list = this.addressList;
        if (list != null) {
//            this.addressHolder.setText(list.get(0).getAddressLine(0));
            String str = TAG;
            Log.e(str, "Address is :" + this.addressList.get(0).getAddressLine(0));
            return this.addressList.get(0).getAddressLine(0);
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        googleLocationListener();
    }

    private Location locationForRotate;

    @SuppressLint("MissingPermission")
    private void googleLocationListener() {
        Log.d("sachin", "googleLocationListener");
        FusedLocationProviderClient localFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Configure LocationRequest
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 5 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Define the callback to handle the location updates
        LocationCallback localLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location localLocation : locationResult.getLocations()) {

                    locationForRotate = localLocation;
                    // Do something with the location data
                    double latitude = localLocation.getLatitude();
                    double longitude = localLocation.getLongitude();
                    // Log the location or send to server
                    System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);

                    altitudeTxt.setText(String.format("%.0f", Math.abs(localLocation.getAltitude())));
                    speedTxt.setText(String.format("%.0f", localLocation.getSpeed()));

                    float distanceInMeters = localLocation.distanceTo(meccaLocation);

                    float distanceInKilometers = distanceInMeters / 1000;
                    distanceToMTxt.setText(String.format("%.2f", distanceInKilometers));
                    Log.d("sachinGooLCAltitude", String.valueOf(localLocation.getAltitude()));
                    Log.d("sachin", localLocation.toString());
                    Log.d("sachinGooLCSpeed", String.valueOf(localLocation.getSpeed()));
                    rotateQuibila();
                }
            }
        };

        localFusedLocationProviderClient.requestLocationUpdates(locationRequest, localLocationCallback, Looper.getMainLooper());

    }

    public boolean checkNetwork() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        return connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED;
    }

    @SuppressLint({"SetTextI18n", "MissingPermission"})
    public void getLastKnowLocation() {
        Log.d("sachin", "getLastKnowLocation()");
        if (checkPermission()) {
            Log.d("sachin", "Permission Granted()");
            if (isLocationEnable()) {
                Log.d("sachin", "LocationEnabled calling fused set Client");
                this.mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() { // from class: com.digitalcompass.liveweather.Fragment.SmartCompassFrag.2
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    @SuppressLint({"SetTextI18n"})
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d("sachin", "Location getted");
                        Location result = task.getResult();
                        if (result != null) {
                            Compass_SmartCompassFrag.this.latHolder.setText(Compass_SmartCompassFrag.this.doubToDMS(result.getLatitude(), true));
                            Compass_SmartCompassFrag.this.lngHolder.setText(Compass_SmartCompassFrag.this.doubToDMS(result.getLongitude(), true));
                            try {
                                String addressList = Compass_SmartCompassFrag.this.getAddressList(result.getLatitude(), result.getLongitude());
                                if (addressList != null) {
//                                    Compass_SmartCompassFrag.this.addressHolder.setText(addressList);
//                                    Compass_SmartCompassFrag.this.addressHolder.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//                                    Compass_SmartCompassFrag.this.addressHolder.setMarqueeRepeatLimit(-1);
//                                    Compass_SmartCompassFrag.this.addressHolder.setFocusable(true);
//                                    Compass_SmartCompassFrag.this.addressHolder.setSelected(true);
//                                    Compass_SmartCompassFrag.this.addressHolder.setFocusableInTouchMode(true);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Compass_SmartCompassFrag.lat = result.getLatitude();
                            Compass_SmartCompassFrag.lng = result.getLongitude();
                            Compass_SmartCompassFrag.this.latHolder.setText(Compass_SmartCompassFrag.this.doubToDMS(Compass_SmartCompassFrag.lat, true));
                            Compass_SmartCompassFrag.this.lngHolder.setText(Compass_SmartCompassFrag.this.doubToDMS(Compass_SmartCompassFrag.lng, false));
                            return;
                        }
                        Log.d("sachin", "requestNewLocation");
                        Compass_SmartCompassFrag.this.requestNewLocation();
                    }
                });
                return;
            }
            Log.e(TAG, "not Enable Location");
            this.latHolder.setText("Wait......");
            this.lngHolder.setText("Wait......");
            return;
        }
        Log.e(TAG, "not Enable Permission");
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

    @SuppressLint("MissingPermission")
    public void requestNewLocation() {
        Log.d("sachin", "requestNewLocation()");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        locationRequest.setInterval(5L);
        locationRequest.setFastestInterval(0L);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.f146v.getContext());
        checkPermission();
        Log.d("sachin", "called");
        this.mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    private void setupCompass() {
        this.compass = new Compass_A(this.f146v.getContext());
        this.compass.setListener(new Compass_A.CompassListener() { // from class: com.digitalcompass.liveweather.Fragment.SmartCompassFrag.4
            @Override // com.digitalcompass.liveweather.customCompass.Compass.CompassListener
            public void onNewAzimuth(float f) {
                Compass_SmartCompassFrag.this.adjustArrow(f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"SetTextI18n"})
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
        this.degreeDisplay.setText(firstDirection + "° " + "\n" + secondDirection);
        rotateQuibila();
//        this.degreeDisplay.setText(str2 + "° " + str);
        rotateAnimation.setDuration(500L);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        this.compassImg.startAnimation(rotateAnimation);
    }

    //    private void rotateQuibila() {
//        if (locationForRotate != null) {
//            float bearingToPos = locationForRotate.bearingTo(meccaLocation); // Bearing in degrees
//            float adjustedDirection = (bearingToPos - this.currentAzimuth + 360) % 360;
//
//            // Create and configure the rotation animation for the quibila
//            RotateAnimation rotateAnimation = new RotateAnimation(-this.pastRotate, -adjustedDirection,
//                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//            this.pastRotate = adjustedDirection;
//            rotateAnimation.setDuration(500L);
//            rotateAnimation.setRepeatCount(0);
//            rotateAnimation.setFillAfter(true);
//            this.quibilaImg.startAnimation(rotateAnimation);
//        }
//    }
    private void rotateQuibila() {
        if (locationForRotate != null) {
            // Calculate bearing from current location to Mecca
            float bearingToMecca = locationForRotate.bearingTo(meccaLocation);

            float adjustedDirection = (bearingToMecca - this.currentAzimuth + 360) % 360;

            RotateAnimation rotateAnimation = new RotateAnimation(this.pastRotate, -adjustedDirection, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            this.pastRotate = adjustedDirection;
            rotateAnimation.setDuration(500L);
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setFillAfter(true);
            this.quibilaImg.startAnimation(rotateAnimation);
        }
    }

    public boolean isLocationEnable() {
        this.locationManager = (LocationManager) requireContext().getSystemService("location");
        return this.locationManager.isProviderEnabled("gps") || this.locationManager.isProviderEnabled("network");
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 1);
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(requireContext(), "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public void onProviderEnabled(@NonNull String str) {
        Log.e("Service", str + " is enable");
    }

    public void onProviderDisabled(@NonNull String str) {
        Log.e("Service", str + " is disable");
    }

    public void onLocationChanged(@NonNull Location location) {
        Log.e("location", "Lat " + location.getLatitude() + " Lng " + location.getLongitude());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.compass.start();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.compass.stop();
    }
}
