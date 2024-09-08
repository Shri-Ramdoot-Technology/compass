package com.arabianmaps.compass.Compass_Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.arabianmaps.compass.R;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Compass_geoLocationFragment extends Fragment {
    static int cnt;
    static String finalAddress;
    static double latValue;
    static double latestLat;
    static double latestLng;
    static double lngValue;
    static int zoomLevel;
    TextView MapPage;
    ClipboardManager clipboardManager;
    ImageView copy;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    TextView latHolder;
    TextView lngHolder;
    LocationManager locationManager;
    private GoogleMap mMap;
    MapView mapView;
    ImageView myLocation;
    ImageView share;
    TextView userLocation;

    /* renamed from: v */
    View f148v;
    ImageView zoomIn;
    ImageView zoomOut;
    boolean isFirstInstall = true;
    boolean isFliperUp = false;
    private String TAG = "geoLocationFragment";
    LocationCallback locationCallback = new LocationCallback() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.9
        @Override // com.google.android.gms.location.LocationCallback
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            Compass_geoLocationFragment.latValue = lastLocation.getLatitude();
            Compass_geoLocationFragment.lngValue = lastLocation.getLongitude();
        }
    };

    public void updateLocationAndLat(double d, double d2, String str) {
        if (this.isFirstInstall) {
            String str2 = this.TAG;
            Log.e(str2, "location result " + str + " lat and lng " + d + " : " + d2);
            this.isFirstInstall = false;
            latValue = d;
            lngValue = d2;
            finalAddress = str;
            latestLat = d;
            latestLng = d2;
            this.userLocation = (TextView) getActivity().findViewById(R.id.userLocation);
            this.latHolder = (TextView) getActivity().findViewById(R.id.infoLat);
            this.lngHolder = (TextView) getActivity().findViewById(R.id.infoLng);
            this.userLocation.setText(finalAddress);
            this.latHolder.setText(doubToDMS(latValue, true));
            this.lngHolder.setText(doubToDMS(lngValue, false));
        }
    }

    @SuppressLint("WrongViewCast")
    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.f148v = layoutInflater.inflate(R.layout.d_layout_for_geolocation_fragment, viewGroup, false);
        getActivity().getWindow().setFlags(1024, 1024);
        Compass_C1105Ui.setHeight(getContext(), (LinearLayout) this.f148v.findViewById(R.id.locationLayout), 105);
        this.mapView = (MapView) this.f148v.findViewById(R.id.map1);
        this.mapView.onCreate(bundle);
        this.mapView.onResume();
        this.mapView.getMapAsync(new OnMapReadyCallback() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.1
            @Override // com.google.android.gms.maps.OnMapReadyCallback
            public void onMapReady(GoogleMap googleMap) {
                Compass_geoLocationFragment.this.mMap = googleMap;
                Compass_geoLocationFragment.zoomLevel = (int) Compass_geoLocationFragment.this.mMap.getCameraPosition().zoom;
                Compass_geoLocationFragment.this.getLastKnowLocation();
            }
        });
        Compass_C1105Ui.setMarginLeft(this.f148v.getContext(), (TextView) this.f148v.findViewById(R.id.latShower), 105);
        Compass_C1105Ui.setMarginRight(this.f148v.getContext(), (TextView) this.f148v.findViewById(R.id.longShower), 105);
        Compass_C1105Ui.setMarginTop(this.f148v.getContext(), (LinearLayout) this.f148v.findViewById(R.id.titleLayout), 40);
        final ConstraintLayout constraintLayout = (ConstraintLayout) this.f148v.findViewById(R.id.constraint2);
        Compass_C1105Ui.setHeight(this.f148v.getContext(), constraintLayout, 397);
        this.geocoder = new Geocoder(getContext(), Locale.getDefault());
        this.copy = (ImageView) this.f148v.findViewById(R.id.copyButton);
        this.share = (ImageView) this.f148v.findViewById(R.id.share);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), this.share, 106, 106);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), this.copy, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 50);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), (ImageView) this.f148v.findViewById(R.id.ic_map_location), ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 50);
        this.zoomIn = (ImageView) this.f148v.findViewById(R.id.ZoomIn);
        this.zoomOut = (ImageView) this.f148v.findViewById(R.id.ZoomOut);
        this.myLocation = (ImageView) this.f148v.findViewById(R.id.yourLocation);
        final ImageView imageView = (ImageView) this.f148v.findViewById(R.id.fliperValue);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), imageView, 153, 100);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), this.myLocation, 95, 95);
        Compass_C1105Ui.setMarginsAsWidth(this.f148v.getContext(), this.myLocation, 0, 0, 59, 29);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), this.zoomOut, 95, 95);
        Compass_C1105Ui.setMarginsAsWidth(this.f148v.getContext(), this.zoomOut, 0, 0, 59, 249);
        Compass_C1105Ui.setHeightWidth(this.f148v.getContext(), this.zoomIn, 95, 95);
        Compass_C1105Ui.setMarginsAsWidth(this.f148v.getContext(), this.zoomIn, 0, 0, 59, 29);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.2
            @Override // android.view.View.OnClickListener
            @SuppressLint({"SetTextI18n"})
            public void onClick(View view) {
                if (!Compass_geoLocationFragment.this.isFliperUp) {
                    constraintLayout.setVisibility(0);
                    imageView.setImageResource(R.drawable.c_digital_up);
                    Compass_geoLocationFragment.this.isFliperUp = true;
                } else if (Compass_geoLocationFragment.this.isFliperUp) {
                    imageView.setImageResource(R.drawable.c_digital_down);
                    constraintLayout.setVisibility(8);
                    Compass_geoLocationFragment.this.isFliperUp = false;
                } else {
                    Log.e("No operation", "for fliper ");
                }
            }
        });
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please wait ");
        progressDialog.setProgressStyle(0);
        this.myLocation.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_geoLocationFragment.this.getLastKnowLocation();
                CameraUpdate newLatLngZoom = CameraUpdateFactory.newLatLngZoom(new LatLng(Compass_geoLocationFragment.latValue, Compass_geoLocationFragment.lngValue), 15.0f);
                Compass_geoLocationFragment.zoomLevel = 15;
                Compass_geoLocationFragment.this.mMap.moveCamera(newLatLngZoom);
            }
        });
        this.zoomIn.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_geoLocationFragment.zoomLevel += 5;
                if (Compass_geoLocationFragment.zoomLevel >= 15) {
                    Compass_geoLocationFragment.zoomLevel = (int) Compass_geoLocationFragment.this.mMap.getCameraPosition().zoom;
                    Log.e(Compass_geoLocationFragment.this.TAG, "zoom level" + Compass_geoLocationFragment.this.mMap.getCameraPosition().zoom);
                    return;
                }
                Compass_geoLocationFragment.this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Compass_geoLocationFragment.latValue, Compass_geoLocationFragment.lngValue), Compass_geoLocationFragment.zoomLevel));
                Compass_geoLocationFragment.this.mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        this.zoomOut.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_geoLocationFragment.zoomLevel -= 5;
                if (Compass_geoLocationFragment.zoomLevel <= 0) {
                    Compass_geoLocationFragment.zoomLevel = (int) Compass_geoLocationFragment.this.mMap.getCameraPosition().zoom;
                    Log.e(Compass_geoLocationFragment.this.TAG, "zoom level" + Compass_geoLocationFragment.this.mMap.getCameraPosition().zoom);
                    return;
                }
                Compass_geoLocationFragment.this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Compass_geoLocationFragment.latValue, Compass_geoLocationFragment.lngValue), Compass_geoLocationFragment.zoomLevel));
                Compass_geoLocationFragment.this.mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        this.clipboardManager = (ClipboardManager) getContext().getSystemService("clipboard");
        this.MapPage = (TextView) this.f148v.findViewById(R.id.mapPage);
        this.userLocation = (TextView) this.f148v.findViewById(R.id.userLocation);
        Compass_C1105Ui.setWidth(this.f148v.getContext(), this.userLocation, 500);
        this.locationManager = (LocationManager) getContext().getSystemService("location");
        this.latHolder = (TextView) this.f148v.findViewById(R.id.infoLat);
        this.lngHolder = (TextView) this.f148v.findViewById(R.id.infoLng);
        this.userLocation.setText(finalAddress);
        this.latHolder.setText(doubToDMS(latValue, true));
        this.lngHolder.setText(doubToDMS(lngValue, false));
        Compass_C1105Ui.setWidth(this.f148v.getContext(), this.latHolder, 255);
        this.copy.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Compass_geoLocationFragment.this.clipboardManager.setPrimaryClip(ClipData.newPlainText("Location", "latitude : " + Compass_geoLocationFragment.latValue + " longitude: " + Compass_geoLocationFragment.lngValue + " Adddress: " + Compass_geoLocationFragment.finalAddress));
                Toast.makeText(Compass_geoLocationFragment.this.getContext(), "successfully copied", 0).show();
            }
        });
        this.share.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "\t\t\t\t\t\t\t\tSharing location \n");
                intent.putExtra("android.intent.extra.TEXT", Compass_geoLocationFragment.finalAddress + "\n https://www.google.com/maps/@" + Compass_geoLocationFragment.latestLat + "," + Compass_geoLocationFragment.latestLng);
                Compass_geoLocationFragment.this.startActivity(Intent.createChooser(intent, "Share Location"));
            }
        });
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastKnowLocation();
        return this.f148v;
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

    public boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        return connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED;
    }

    public List<Address> getMyAddressList(double d, double d2) {
        try {
            return this.geocoder.getFromLocation(d, d2, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @SuppressLint("MissingPermission")
    public void getLastKnowLocation() {
        if (checkPermission()) {
            if (isLocationEnable()) {
                this.fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() { // from class: com.digitalcompass.liveweather.Fragment.geoLocationFragment.8
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    @SuppressLint({"SetTextI18n"})
                    public void onComplete(@NonNull Task<Location> task) {
                        Location result = task.getResult();
                        if (result == null) {
                            Compass_geoLocationFragment.this.requestNewLocation();
                            return;
                        }
                        Compass_geoLocationFragment.latValue = result.getLatitude();
                        Compass_geoLocationFragment.lngValue = result.getLongitude();
                        List<Address> myAddressList = Compass_geoLocationFragment.this.getMyAddressList(Compass_geoLocationFragment.latValue, Compass_geoLocationFragment.lngValue);
                        if (myAddressList != null) {
                            String featureName = myAddressList.get(0).getFeatureName();
                            String locality = myAddressList.get(0).getLocality();
                            String subAdminArea = myAddressList.get(0).getSubAdminArea();
                            String adminArea = myAddressList.get(0).getAdminArea();
                            Compass_geoLocationFragment.finalAddress = featureName + " , " + locality + " , " + subAdminArea + " , " + adminArea;
                            Compass_geoLocationFragment.this.userLocation.setText(Compass_geoLocationFragment.finalAddress);
                        }
                        LatLng latLng = new LatLng(Compass_geoLocationFragment.latValue, Compass_geoLocationFragment.lngValue);
                        Log.e("info lat and lng ", Compass_geoLocationFragment.latValue + " " + Compass_geoLocationFragment.lngValue);
                        Compass_geoLocationFragment.this.latHolder.setText(Compass_geoLocationFragment.this.doubToDMS(Compass_geoLocationFragment.latValue, true));
                        Compass_geoLocationFragment.this.lngHolder.setText(Compass_geoLocationFragment.this.doubToDMS(Compass_geoLocationFragment.lngValue, false));
                        Compass_geoLocationFragment.this.mMap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
                        Compass_geoLocationFragment.this.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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

    @SuppressLint("MissingPermission")
    public void requestNewLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        locationRequest.setInterval(5L);
        locationRequest.setFastestInterval(1L);
        checkPermission();
        this.fusedLocationProviderClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    public boolean isLocationEnable() {
        this.locationManager = (LocationManager) getContext().getSystemService("location");
        return this.locationManager.isProviderEnabled("gps") || this.locationManager.isProviderEnabled("network");
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 1);
    }

    public void onLocationChanged(@NonNull Location location) {
        Log.e("change location", location.getLatitude() + " " + location.getLongitude());
    }
}
