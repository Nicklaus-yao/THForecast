package com.nykidxxx.thforecast.weather;
// Created on 3/29/2017


import android.widget.Toast;

import com.nykidxxx.thforecast.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Integer.parseInt;

public class Current {
    //Taken from Currently JSON Object
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;
    private double mWindSpeed;

    //Taken from Daily JSON Object
    private long mSunsetTime;
    private long mSunriseTime;
    private double mMinTemp;
    private double mMaxTemp;

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public int getMinTemp() {
        return (int)Math.round(mMinTemp);
    }

    public void setMinTemp(double minTemp) {
        mMinTemp = minTemp;
    }

    public int getMaxTemp() {
        return (int)Math.round(mMaxTemp);
    }

    public void setMaxTemp(double maxTemp) {
        mMaxTemp = maxTemp;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId(){

        return Forecast.getIconId(mIcon);
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormattedTime(){
        //Get time
        Date dateTime = new Date(getTime() * 1000);
        //Dictate how we want it formatted
        SimpleDateFormat mFormat = new SimpleDateFormat("h:mm a");
        mFormat.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        //Apply our desired format to the current time
        String timeString = mFormat.format(dateTime);

        return timeString;
    }

    public boolean isSunUp(){
        boolean mIsSunUp;
        if(getSunriseTime() < getTime() && getTime() < getSunsetTime())
            mIsSunUp = true;
        else
            mIsSunUp = false;

        return mIsSunUp;
    }

    public int getTemperature() {
        return (int)Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance * 100;
        return (int)Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public long getSunsetTime() {
        return mSunsetTime;
    }

    public String getFormattedSunsetTime(){
        //Get time
        Date dateTime = new Date(getSunsetTime() * 1000);
        //Dictate how we want it formatted
        SimpleDateFormat mFormat = new SimpleDateFormat("h:mm a");
        mFormat.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        //Apply our desired format to the current time
        String timeString = mFormat.format(dateTime);

        return timeString;
    }

    public void setSunsetTime(long sunsetTime) {
        mSunsetTime = sunsetTime;
    }

    public long getSunriseTime() {
        return mSunriseTime;
    }

    public String getFormattedSunriseTime(){
        //Get time
        Date dateTime = new Date(getSunriseTime() * 1000);
        //Dictate how we want it formatted
        SimpleDateFormat mFormat = new SimpleDateFormat("h:mm a");
        mFormat.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        //Apply our desired format to the current time
        String timeString = mFormat.format(dateTime);

        return timeString;
    }

    public void setSunriseTime(long sunriseTime) {
        mSunriseTime = sunriseTime;
    }
}
