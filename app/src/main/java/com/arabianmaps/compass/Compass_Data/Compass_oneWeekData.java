package com.arabianmaps.compass.Compass_Data;

/* loaded from: classes2.dex */
public class Compass_oneWeekData {
    private String date;
    private int humidity;
    private int maxTemp;
    private int minTemp;
    private int weatherIcon;

    public Compass_oneWeekData(String str, int i, int i2, int i3, int i4) {
        this.date = str;
        this.humidity = i;
        this.weatherIcon = i2;
        this.minTemp = i3;
        this.maxTemp = i4;
    }

    public String getDate() {
        return this.date;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public int getWeatherIcon() {
        return this.weatherIcon;
    }

    public int getMinTemp() {
        return this.minTemp;
    }

    public int getMaxTemp() {
        return this.maxTemp;
    }
}
