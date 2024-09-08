package com.arabianmaps.compass.Compass_ViewPageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.arabianmaps.compass.Compass_Fragment.Compass_MapFragment;
import com.arabianmaps.compass.Compass_Fragment.Compass_SmartCompassFrag;
import com.arabianmaps.compass.Compass_Fragment.Compass_WeatherFragment;
import com.arabianmaps.compass.Compass_Fragment.Compass_geoLocationFragment;

/* loaded from: classes2.dex */
public class Compass_MyAdapter extends FragmentPagerAdapter {
    Compass_geoLocationFragment geoLocationFragment;
    Compass_MapFragment mapFragment;
    Compass_SmartCompassFrag smartCompassFrag;
    Compass_WeatherFragment weatherFragment;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return 4;
    }

    public Compass_MyAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        this.smartCompassFrag = new Compass_SmartCompassFrag();
        this.mapFragment = new Compass_MapFragment();
        this.geoLocationFragment = new Compass_geoLocationFragment();
        this.weatherFragment = new Compass_WeatherFragment();
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return this.smartCompassFrag;
            case 1:
                return this.mapFragment;
            case 2:
                return this.weatherFragment;
            case 3:
                return this.geoLocationFragment;
            default:
                return this.smartCompassFrag;
        }
    }

    public Compass_SmartCompassFrag getSmartCompassFrag() {
        return this.smartCompassFrag;
    }

    public Compass_MapFragment getMapFragment() {
        return this.mapFragment;
    }

    public Compass_geoLocationFragment getGeoLocationFragment() {
        return this.geoLocationFragment;
    }

    public Compass_WeatherFragment getWeatherFragment() {
        return this.weatherFragment;
    }
}
