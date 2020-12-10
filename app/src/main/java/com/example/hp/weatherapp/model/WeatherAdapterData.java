package com.example.hp.weatherapp.model;

public class WeatherAdapterData {
    String date;
    String skyStatus;
    String minTamp;

    public WeatherAdapterData(String date, String skyStatus, String minTamp, String maxTemp) {
        this.date = date;
        this.skyStatus = skyStatus;
        this.minTamp = minTamp;
        this.maxTemp = maxTemp;
    }

    @Override
    public String toString() {
        return "WeatherAdapterData{" +
                "date='" + date + '\'' +
                ", skyStatus='" + skyStatus + '\'' +
                ", minTamp='" + minTamp + '\'' +
                ", maxTemp='" + maxTemp + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSkyStatus() {
        return skyStatus;
    }

    public void setSkyStatus(String skyStatus) {
        this.skyStatus = skyStatus;
    }

    public String getMinTamp() {
        return minTamp;
    }

    public void setMinTamp(String minTamp) {
        this.minTamp = minTamp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    String maxTemp;
}
