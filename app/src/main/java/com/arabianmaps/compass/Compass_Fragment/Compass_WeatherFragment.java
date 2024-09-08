package com.arabianmaps.compass.Compass_Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.arabianmaps.compass.R;
import com.arabianmaps.compass.Compass_Data.Compass_oneWeekData;
import com.arabianmaps.compass.Compass_Data.Compass_weatherModel;
import com.arabianmaps.compass.Compass_ListViewAdapter.Compass_listViewAdapter;
import com.arabianmaps.compass.Compass_RecyleViewAdapter.Compass_RecycleAapter;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import com.arabianmaps.compass.Compass_volley.Compass_MySingleton;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class Compass_WeatherFragment extends Fragment {
    static String cityName = "surat";
    static int cnt = 0;
    static String date = null;
    static String description = null;
    static int hdt = 0;
    static double lat = 0.0d;
    static double lng = 0.0d;
    static String locationKey = "202441";
    static int pressure;
    static int sunrise;
    static int sunriseHour;
    static int sunriseMinute;
    static int sunset;
    static int sunsetHour;
    static int sunsetMinute;
    static int temperature;
    static int vis;
    static int wind_speed;
    Context context;
    boolean isWeatherGetCalled = false ;
    TextView currentTime;
    Geocoder geocoder;
    TextView humidity;
    ListView listView;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    Compass_RecycleAapter myAdapter;
    List<Address> myAddress;
    Compass_listViewAdapter mylistAdapter;
    TextView pressureShow;
    RecyclerView recyclerView;
    ImageView refreshPage;
    TextView sunSetHold;
    TextView sunriseHold;
    TextView tempShw;

    /* renamed from: v */
    View f147v;
    TextView visibility;
    TextView weatherDes;
    TextView weatherDes1;
    TextView weatherDes2;
    TextView windAns;
    private final String TAG = "WeatherFragment";
    ArrayList<Compass_weatherModel> weatherlist = new ArrayList<>();
    ArrayList<Compass_oneWeekData> day7Data = new ArrayList<>();
    int[] weatherState = {R.drawable.c_digital_sun, R.drawable.c_digital_mid};
    int[] weatherState12H = {R.drawable.c_digital_sun, R.drawable.c_digital_mid, R.drawable.c_digital_cloud, R.drawable.c_digital_fog, R.drawable.c_digital_rain, R.drawable.c_digital_cold, R.drawable.c_digital_hot, R.drawable.c_digital_wind_in_high, R.drawable.c_digital_night};
    LocationCallback locationCallback = new LocationCallback() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.3
        @Override // com.google.android.gms.location.LocationCallback
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();

            if (lastLocation==null){
                Compass_WeatherFragment.this.requestNewLocation();
                return;
            }
            if (lastLocation.getLatitude()>0 && lastLocation.getLongitude()>0){
                Compass_WeatherFragment.lat = lastLocation.getLatitude();
                Compass_WeatherFragment.lng = lastLocation.getLongitude();
                if (!isWeatherGetCalled){
                    getWeather();
                    get12hourWeatherData();
                }


            }else{
                Compass_WeatherFragment.this.requestNewLocation();
            }

        }
    };

    /* JADX WARN: Type inference failed for: r11v66, types: [com.digitalcompass.liveweather.Fragment.WeatherFragment$2] */
    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.f147v = layoutInflater.inflate(R.layout.d_layout_for_weather_fragment, viewGroup, false);
        Compass_C1105Ui.setHeightWidth(this.f147v.getContext(), (ConstraintLayout) this.f147v.findViewById(R.id.constraint2), 1031, 426);
        this.tempShw = (TextView) this.f147v.findViewById(R.id.tempShow);
        this.windAns = (TextView) this.f147v.findViewById(R.id.windAns);
        this.humidity = (TextView) this.f147v.findViewById(R.id.humidityAns);
        this.pressureShow = (TextView) this.f147v.findViewById(R.id.pressureAns);
        this.visibility = (TextView) this.f147v.findViewById(R.id.visibilityAns);
        this.sunriseHold = (TextView) this.f147v.findViewById(R.id.sunriseHolder);
        this.sunSetHold = (TextView) this.f147v.findViewById(R.id.sunsetHolder);
        this.currentTime = (TextView) this.f147v.findViewById(R.id.currentTime);
        Compass_C1105Ui.setMarginLeft(this.f147v.getContext(), this.currentTime, 59);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.currentTime, 39);
        TextView textView = (TextView) this.f147v.findViewById(R.id.hours48);
        Compass_C1105Ui.setMarginLeft(this.f147v.getContext(), textView, 59);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), textView, 39);
        this.weatherDes1 = (TextView) this.f147v.findViewById(R.id.weatherDes1);
        Compass_C1105Ui.setMarginLeft(this.f147v.getContext(), this.weatherDes1, 83);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.weatherDes1, 39);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), (TextView) this.f147v.findViewById(R.id.day7title), 20);
        ConstraintLayout constraintLayout = (ConstraintLayout) this.f147v.findViewById(R.id.constraint12Day);
        Compass_C1105Ui.setMarginBottom(this.f147v.getContext(), constraintLayout, 31);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), constraintLayout, 20);
        Compass_C1105Ui.setHeightWidth(this.f147v.getContext(), constraintLayout, 1031, 557);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.windAns, 10);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.humidity, 10);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.visibility, 10);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.pressureShow, 10);
        ConstraintLayout constraintLayout2 = (ConstraintLayout) this.f147v.findViewById(R.id.constraint1);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), constraintLayout2, 20);
        Compass_C1105Ui.setMarginRight(this.f147v.getContext(), constraintLayout2, 10);
        this.refreshPage = (ImageView) this.f147v.findViewById(R.id.refreshPage);
        Compass_C1105Ui.setHeightWidth(getContext(), this.refreshPage, 160, 160);
        Compass_C1105Ui.setMarginRight(getContext(), this.refreshPage, 20);
        ImageView imageView = (ImageView) this.f147v.findViewById(R.id.txt_celsius);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView, 89, 80);
        Compass_C1105Ui.setMarginsAsWidth(getContext(), imageView, 290, 70, 0, 100);
        Compass_C1105Ui.setMarginTop(getContext(), this.tempShw, 20);
        Compass_C1105Ui.setHeight(getContext(), this.tempShw, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        Compass_C1105Ui.setMarginRight(getContext(), this.tempShw, 80);
        Compass_C1105Ui.setMarginLeft(getContext(), this.sunriseHold, 40);
        Compass_C1105Ui.setMarginBottom(getContext(), this.sunriseHold, 20);
        Compass_C1105Ui.setMarginTop(getContext(), this.sunriseHold, 20);
        Compass_C1105Ui.setMarginBottom(getContext(), this.sunSetHold, 20);
        Compass_C1105Ui.setMarginTop(getContext(), this.sunSetHold, 20);
        ImageView imageView2 = (ImageView) this.f147v.findViewById(R.id.sunrise);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView2, 80, 55);
        Compass_C1105Ui.setMarginLeft(getContext(), imageView2, 40);
        Compass_C1105Ui.setMarginTop(getContext(), imageView2, 20);
        ImageView imageView3 = (ImageView) this.f147v.findViewById(R.id.wind);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView3, 80, 80);
        Compass_C1105Ui.setMarginTop(getContext(), imageView3, 40);
        ImageView imageView4 = (ImageView) this.f147v.findViewById(R.id.humidity);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView4, 80, 80);
        Compass_C1105Ui.setMarginLeft(getContext(), imageView4, 43);
        Compass_C1105Ui.setMarginTop(getContext(), imageView4, 40);
        ImageView imageView5 = (ImageView) this.f147v.findViewById(R.id.visibility);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView5, 80, 80);
        Compass_C1105Ui.setMarginLeft(getContext(), imageView5, 43);
        Compass_C1105Ui.setMarginTop(getContext(), imageView5, 40);
        ImageView imageView6 = (ImageView) this.f147v.findViewById(R.id.pressure);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView6, 80, 80);
        Compass_C1105Ui.setMarginLeft(getContext(), imageView6, 43);
        Compass_C1105Ui.setMarginTop(getContext(), imageView6, 40);
        ImageView imageView7 = (ImageView) this.f147v.findViewById(R.id.sunset);
        Compass_C1105Ui.setHeightWidth(getContext(), imageView7, 80, 55);
        Compass_C1105Ui.setMarginLeft(getContext(), imageView7, 40);
        Compass_C1105Ui.setMarginTop(getContext(), imageView7, 20);
        this.geocoder = new Geocoder(getContext(), Locale.getDefault());
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        this.refreshPage.setOnClickListener(new View.OnClickListener() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Toast.makeText(Compass_WeatherFragment.this.getContext(), "Has Refresh", 0).show();
                Compass_WeatherFragment.this.getWeather();
                Compass_WeatherFragment.this.get12hourWeatherData();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ");
        progressDialog.setProgressStyle(0);
        progressDialog.setCancelable(false);
        while (!checkNetwork()) {
            cnt += 1000;
            if (checkNetwork()) {
                break;
            }
            new CountDownTimer(cnt + 10000, 1000L) { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.2
                @Override // android.os.CountDownTimer
                public void onTick(long j) {
                    progressDialog.show();
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    progressDialog.dismiss();
                }
            }.start();
        }
        Calendar.getInstance();
        this.listView = (ListView) this.f147v.findViewById(R.id.listView);
        this.mylistAdapter = new Compass_listViewAdapter(this.day7Data, getContext());
        this.listView.setAdapter((ListAdapter) this.mylistAdapter);
        this.mylistAdapter.notifyDataSetChanged();
        this.recyclerView = (RecyclerView) this.f147v.findViewById(R.id.recycleView);
        Compass_C1105Ui.setHeight(this.f147v.getContext(), this.recyclerView, 328);
        Compass_C1105Ui.setMarginTop(this.f147v.getContext(), this.recyclerView, 20);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.myAdapter = new Compass_RecycleAapter(this.weatherlist, getContext());
        this.recyclerView.setAdapter(this.myAdapter);
        getLastLocation();


        return this.f147v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SachinResume","Onresume");

       getWeather();
       get12hourWeatherData();
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public boolean isLocationEnable() {
        this.locationManager = (LocationManager) getContext().getSystemService("location");
        return this.locationManager.isProviderEnabled("gps") || this.locationManager.isProviderEnabled("network");
    }

    public void requestNewLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        locationRequest.setInterval(5L);
        locationRequest.setFastestInterval(1L);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        checkPermission();
        this.mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    public void getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnable()) {
                this.mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.4
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    @SuppressLint({"SetTextI18n"})
                    public void onComplete(@NonNull Task<Location> task) {
                        Location result = task.getResult();
                        if (result == null) {
                            Compass_WeatherFragment.this.requestNewLocation();
                            return;
                        }
                        if (Compass_WeatherFragment.lat==0 || Compass_WeatherFragment.lng==0){
                            Compass_WeatherFragment.this.requestNewLocation();
                            return;
                        }
                        Compass_WeatherFragment.lat = result.getLatitude();
                        Compass_WeatherFragment.lng = result.getLongitude();
                        try {
                            List<Address> addresslist = Compass_WeatherFragment.this.getAddresslist(Compass_WeatherFragment.lat, Compass_WeatherFragment.lng);
                            if (addresslist != null) {
                                Compass_WeatherFragment.cityName = addresslist.get(0).getSubAdminArea();
                                Compass_WeatherFragment.this.getLocationKey(Compass_WeatherFragment.cityName);
                                Log.d("sachinCity",Compass_WeatherFragment.cityName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!isWeatherGetCalled){
                            getWeather();
                            get12hourWeatherData();
                        }

                    }
                });
                return;
            } else {
                Log.e("WeatherFragment", "not Enable Location");
                return;
            }
        }
        Log.e("WeatherFragment", "not Enable Permission");
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 1);
    }

    public List<Address> getAddresslist(double d, double d2) throws IOException {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(3);
        this.myAddress = this.geocoder.getFromLocation(Double.parseDouble(decimalFormat.format(d)), Double.parseDouble(decimalFormat.format(d2)), 1);
        List<Address> list = this.myAddress;
        if (list != null) {
            return list;
        }
        return null;
    }

    public boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        return connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED;
    }

    public void get12hourWeatherData() {
        this.weatherlist.clear();
        Compass_MySingleton.getInstance(requireContext()).addToRequestQueue(new JsonArrayRequest(0, "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/" + locationKey + "?apikey=7AXu9YVlHK99IdGUYTGeuMsrJufsRKz6", null, new Response.Listener<JSONArray>() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.5
            @Override // com.android.volley.Response.Listener
            public void onResponse(JSONArray jSONArray) {
                Log.d("sachinWeather",jSONArray.toString());
                for (int i = 0; i < jSONArray.length(); i++) {
                    try {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        JSONObject jSONObject2 = jSONObject.getJSONObject("Temperature");
                        String string = jSONObject.getString("IconPhrase");
                        Log.e("WeatherFragment", "12 hour Icon State " + string + " called time " + i);
                        int parseInt = Integer.parseInt(new StringTokenizer(Compass_WeatherFragment.this.UtcMiliToTime(jSONObject.getLong("EpochDateTime")), ":").nextToken());
                        StringBuilder sb = new StringBuilder();
                        sb.append(parseInt + 6);
                        sb.append(":00");
                        String sb2 = sb.toString();
                        if (sb2.equals("24:00")) {
                            sb2 = "00:00";
                        }
                        if (sb2.equals("25:00")) {
                            sb2 = "01:00";
                        }
                        if (sb2.equals("26:00")) {
                            sb2 = "02:00";
                        }
                        if (sb2.equals("27:00")) {
                            sb2 = "03:00";
                        }
                        if (sb2.equals("28:00")) {
                            sb2 = "04:00";
                        }
                        if (sb2.equals("29:00")) {
                            sb2 = "05:00";
                        }
                        if (!string.equals("Sunny") && !string.equals("Mostly sunny") && !string.equals("Partly sunny") && !string.equals("Intermittent clouds") && !string.equals("Hazy sunshine")) {
                            if (!string.equals("Mostly clear") && !string.equals("Clear")) {
                                if (!string.equals("Mostly cloudy") && !string.equals("Cloudy") && !string.equals("Dreary overcast")) {
                                    if (string.equals("Hazy moonlight")) {
                                        Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[8], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                                    } else if (string.equals("Fog")) {
                                        Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[3], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                                    } else {
                                        if (!string.equals("Showers") && !string.equals("Mostly cloudy w/ showers") && !string.equals("Partly sunny w/ showers") && !string.equals("T-Storms") && !string.equals("Mostly cloudy w/ t-Storms") && !string.equals("Partly sunny w/ t-Storms") && !string.equals("Rain") && !string.equals("Freezing rain") && !string.equals("Rain and snow") && !string.equals("Sleet")) {
                                            if (string.equals("Cold")) {
                                                Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[5], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                                            } else if (string.equals("Hot")) {
                                                Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[6], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                                            } else {
                                                Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[2], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                                            }
                                        }
                                        Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[4], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                                    }
                                }
                                Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[1], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                            }
                            Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[1], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                        }
                        Compass_WeatherFragment.this.weatherlist.add(new Compass_weatherModel(Compass_WeatherFragment.this.weatherState12H[0], Compass_WeatherFragment.this.FarToCelsius(jSONObject2.getInt("Value")), sb2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Compass_WeatherFragment.this.myAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.6
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("WeatherFragment", volleyError.getMessage() + " is Fragment");
            }
        }));
    }

    public int FarToCelsius(int i) {
        double d = i - 32;
        Double.isNaN(d);
        return (int) (d / 1.8d);
    }

    public void getLocationKey(String str) {
        Compass_MySingleton.getInstance(requireContext()).addToRequestQueue(new JsonArrayRequest(0, "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=kIiJta9RQjN2jAVyfG4hLj0SYiNMJPxc&q=" + str + "&language=en&details=false", null, new Response.Listener<JSONArray>() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.7
            @Override // com.android.volley.Response.Listener
            public void onResponse(JSONArray jSONArray) {
                Log.d("sachinWeatherLocationKey",jSONArray.toString());
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(0);
                    jSONObject.getString("Key");
                    Compass_WeatherFragment.locationKey = jSONObject.getString("Key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.8
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
            }
        }));
    }

    public String UtcMiliToTime(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date(j * 1000));
    }

    public void getWeather() {
        if (Compass_WeatherFragment.lat==0.0 || Compass_WeatherFragment.lng==0.0){
            isWeatherGetCalled =false ;
            requestNewLocation();
            return;
        }
        isWeatherGetCalled =true ;
        Log.d("sachinUrl","https://api.weatherbit.io/v2.0/forecast/daily?lat="+Compass_WeatherFragment.lat+"&lon="+Compass_WeatherFragment.lng+"&key=dc463fc9ea0149468a8a065b5acd39d9");
        Compass_MySingleton.getInstance(requireContext()).addToRequestQueue(new JsonObjectRequest(0, "https://api.weatherbit.io/v2.0/forecast/daily?lat="+Compass_WeatherFragment.lat+"&lon="+Compass_WeatherFragment.lng+"&key=dc463fc9ea0149468a8a065b5acd39d9", null, new Response.Listener<JSONObject>() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.9
            @Override // com.android.volley.Response.Listener
            @SuppressLint({"SetTextI18n"})
            public void onResponse(JSONObject jSONObject) {
                Log.d("sachinWeather",jSONObject.toString());
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    JSONObject jSONObject2 = jSONArray.getJSONObject(0);
                    JSONObject jSONObject3 = jSONArray.getJSONObject(1);
                    JSONObject jSONObject4 = jSONArray.getJSONObject(2);
                    JSONObject jSONObject5 = jSONArray.getJSONObject(3);
                    JSONObject jSONObject6 = jSONArray.getJSONObject(4);
                    JSONObject jSONObject7 = jSONArray.getJSONObject(5);
                    JSONObject jSONObject8 = jSONArray.getJSONObject(6);
                    JSONObject jSONObject9 = jSONArray.getJSONObject(7);
                    JSONObject jSONObject10 = jSONArray.getJSONObject(8);
                    JSONObject jSONObject11 = jSONArray.getJSONObject(9);
                    JSONObject jSONObject12 = jSONArray.getJSONObject(10);
                    JSONObject jSONObject13 = jSONArray.getJSONObject(11);
                    JSONObject jSONObject14 = jSONArray.getJSONObject(12);
                    JSONObject jSONObject15 = jSONArray.getJSONObject(13);
                    JSONObject jSONObject16 = jSONArray.getJSONObject(14);
                    JSONObject jSONObject17 = jSONArray.getJSONObject(15);
                    Compass_WeatherFragment.sunrise = jSONObject2.getInt("sunrise_ts");
                    long j = Compass_WeatherFragment.sunrise * 1000L;
                    Log.d("sachinMilli", String.valueOf(j));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.getDefault());
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    StringTokenizer stringTokenizer = new StringTokenizer(simpleDateFormat.format(new Date(j)), " ,:");
                    try {
                        Log.e("WeatherAct spilit", stringTokenizer.nextToken());
                        Log.e("WeatherAct spilit1", stringTokenizer.nextToken());
                        Log.e("WeatherAct spilit2", stringTokenizer.nextToken());
                        Log.e("WeatherAct spilit3", stringTokenizer.nextToken());
                        Compass_WeatherFragment.sunriseHour = Integer.parseInt(stringTokenizer.nextToken());
                        Compass_WeatherFragment.sunriseMinute = Integer.parseInt(stringTokenizer.nextToken());
                        Compass_WeatherFragment.sunriseHour += 5;
                        Compass_WeatherFragment.sunriseMinute += 30;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Compass_WeatherFragment.sunset = jSONObject2.getInt("sunset_ts");
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));
                    StringTokenizer stringTokenizer2 = new StringTokenizer(simpleDateFormat2.format(new Date(Compass_WeatherFragment.sunset * 1000)), " :");
                    Compass_WeatherFragment.sunsetHour = Integer.parseInt(stringTokenizer2.nextToken());
                    Compass_WeatherFragment.sunsetMinute = Integer.parseInt(stringTokenizer2.nextToken());
                    Compass_WeatherFragment.sunsetHour += 5;
                    Compass_WeatherFragment.sunsetMinute += 30;
                    Compass_WeatherFragment.pressure = jSONObject2.getInt("pres");
                    Compass_WeatherFragment.wind_speed = jSONObject2.getInt("wind_gust_spd");
                    Compass_WeatherFragment.temperature = jSONObject2.getInt("max_temp");
                    Compass_WeatherFragment.vis = jSONObject2.getInt("vis");
                    Compass_WeatherFragment.hdt = jSONObject2.getInt("rh");
                    Compass_WeatherFragment.date = jSONObject2.getString("valid_date");
                    Compass_WeatherFragment.description = jSONObject2.getJSONObject("weather").getString("description");
                    Log.e("WeatherFragment", "weather Description " + Compass_WeatherFragment.description);
                    if (Compass_WeatherFragment.description.equals("Clear sky")) {
                        Log.e("WeatherFragment", "called Condition is if ");
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject2.getString("valid_date"), jSONObject2.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject2.getInt("min_temp"), jSONObject2.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject3.getString("valid_date"), jSONObject3.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject3.getInt("min_temp"), jSONObject3.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject4.getString("valid_date"), jSONObject4.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject4.getInt("min_temp"), jSONObject4.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject5.getString("valid_date"), jSONObject5.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject5.getInt("min_temp"), jSONObject5.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject6.getString("valid_date"), jSONObject6.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject6.getInt("min_temp"), jSONObject6.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject7.getString("valid_date"), jSONObject7.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject7.getInt("min_temp"), jSONObject7.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject8.getString("valid_date"), jSONObject8.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject8.getInt("min_temp"), jSONObject8.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject9.getString("valid_date"), jSONObject9.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject9.getInt("min_temp"), jSONObject9.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject10.getString("valid_date"), jSONObject10.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject10.getInt("min_temp"), jSONObject10.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject11.getString("valid_date"), jSONObject11.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject11.getInt("min_temp"), jSONObject11.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject12.getString("valid_date"), jSONObject12.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject12.getInt("min_temp"), jSONObject12.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject13.getString("valid_date"), jSONObject13.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject13.getInt("min_temp"), jSONObject13.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject14.getString("valid_date"), jSONObject14.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject14.getInt("min_temp"), jSONObject14.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject15.getString("valid_date"), jSONObject15.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject15.getInt("min_temp"), jSONObject15.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject16.getString("valid_date"), jSONObject16.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject16.getInt("min_temp"), jSONObject16.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject17.getString("valid_date"), jSONObject17.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject17.getInt("min_temp"), jSONObject17.getInt("max_temp")));
                    } else {
                        if (!Compass_WeatherFragment.description.equals("Few clouds") && !Compass_WeatherFragment.description.equals("Scattered clouds") && !Compass_WeatherFragment.description.equals("Broken clouds")) {
                            Log.e("WeatherFragment", "called Condition is else ");
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject2.getString("valid_date"), jSONObject2.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject2.getInt("min_temp"), jSONObject2.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject3.getString("valid_date"), jSONObject3.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject3.getInt("min_temp"), jSONObject3.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject4.getString("valid_date"), jSONObject4.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject4.getInt("min_temp"), jSONObject4.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject5.getString("valid_date"), jSONObject5.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject5.getInt("min_temp"), jSONObject5.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject6.getString("valid_date"), jSONObject6.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject6.getInt("min_temp"), jSONObject6.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject7.getString("valid_date"), jSONObject7.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject7.getInt("min_temp"), jSONObject7.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject8.getString("valid_date"), jSONObject8.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject8.getInt("min_temp"), jSONObject8.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject9.getString("valid_date"), jSONObject9.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject9.getInt("min_temp"), jSONObject9.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject10.getString("valid_date"), jSONObject10.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject10.getInt("min_temp"), jSONObject10.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject11.getString("valid_date"), jSONObject11.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject11.getInt("min_temp"), jSONObject11.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject12.getString("valid_date"), jSONObject12.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject12.getInt("min_temp"), jSONObject12.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject13.getString("valid_date"), jSONObject13.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject13.getInt("min_temp"), jSONObject13.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject14.getString("valid_date"), jSONObject14.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject14.getInt("min_temp"), jSONObject14.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject15.getString("valid_date"), jSONObject15.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject15.getInt("min_temp"), jSONObject15.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject16.getString("valid_date"), jSONObject16.getInt("rh"), Compass_WeatherFragment.this.weatherState[0], jSONObject16.getInt("min_temp"), jSONObject16.getInt("max_temp")));
                            Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject17.getString("valid_date"), jSONObject17.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject17.getInt("min_temp"), jSONObject17.getInt("max_temp")));
                        }
                        Log.e("WeatherFragment", "called Condition is else if ");
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject2.getString("valid_date"), jSONObject2.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject2.getInt("min_temp"), jSONObject2.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject3.getString("valid_date"), jSONObject3.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject3.getInt("min_temp"), jSONObject3.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject4.getString("valid_date"), jSONObject4.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject4.getInt("min_temp"), jSONObject4.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject5.getString("valid_date"), jSONObject5.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject5.getInt("min_temp"), jSONObject5.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject6.getString("valid_date"), jSONObject6.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject6.getInt("min_temp"), jSONObject6.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject7.getString("valid_date"), jSONObject7.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject7.getInt("min_temp"), jSONObject7.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject8.getString("valid_date"), jSONObject8.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject8.getInt("min_temp"), jSONObject8.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject9.getString("valid_date"), jSONObject9.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject9.getInt("min_temp"), jSONObject9.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject10.getString("valid_date"), jSONObject10.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject10.getInt("min_temp"), jSONObject10.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject11.getString("valid_date"), jSONObject11.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject11.getInt("min_temp"), jSONObject11.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject12.getString("valid_date"), jSONObject12.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject12.getInt("min_temp"), jSONObject12.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject13.getString("valid_date"), jSONObject13.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject13.getInt("min_temp"), jSONObject13.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject14.getString("valid_date"), jSONObject14.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject14.getInt("min_temp"), jSONObject14.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject15.getString("valid_date"), jSONObject15.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject15.getInt("min_temp"), jSONObject15.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject16.getString("valid_date"), jSONObject16.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject16.getInt("min_temp"), jSONObject16.getInt("max_temp")));
                        Compass_WeatherFragment.this.day7Data.add(new Compass_oneWeekData(jSONObject17.getString("valid_date"), jSONObject17.getInt("rh"), Compass_WeatherFragment.this.weatherState[1], jSONObject17.getInt("min_temp"), jSONObject17.getInt("max_temp")));
                    }
                    Compass_WeatherFragment.this.mylistAdapter.notifyDataSetChanged();
                    Compass_WeatherFragment.this.tempShw.setText(Compass_WeatherFragment.temperature + "");
                    Compass_WeatherFragment.this.windAns.setText(Compass_WeatherFragment.wind_speed + " km/h");
                    Compass_WeatherFragment.this.humidity.setText(Compass_WeatherFragment.hdt + ".0%");
                    Compass_WeatherFragment.this.pressureShow.setText(Compass_WeatherFragment.pressure + " mb");
                    Compass_WeatherFragment.this.visibility.setText(Compass_WeatherFragment.vis + " km");
                    Compass_WeatherFragment.this.sunriseHold.setText(Compass_WeatherFragment.sunriseHour + ":" + Compass_WeatherFragment.sunriseMinute + " am");
                    Compass_WeatherFragment.this.sunSetHold.setText(Compass_WeatherFragment.sunsetHour + ":" + Compass_WeatherFragment.sunsetMinute + " pm");
                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
                    simpleDateFormat3.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Compass_WeatherFragment.this.currentTime.setText(simpleDateFormat3.format(new Date(j)));
                    Compass_WeatherFragment.this.weatherDes1.setText(String.valueOf(Compass_WeatherFragment.description));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // from class: com.digitalcompass.liveweather.Fragment.WeatherFragment.10
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
            }
        }));
    }
}
