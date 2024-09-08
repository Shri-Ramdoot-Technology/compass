package com.arabianmaps.compass.Compass_Data;

/* loaded from: classes2.dex */
public class Compass_weatherModel {
    private int temprature;
    private String time;
    private int weatherImage;

    public Compass_weatherModel(int i, int i2, String str) {
        this.weatherImage = i;
        this.temprature = i2;
        this.time = str;
    }

    public int getWeatherImage() {
        return this.weatherImage;
    }

    public int getTemprature() {
        return this.temprature;
    }

    public String getTime() {
        return this.time;
    }
}
