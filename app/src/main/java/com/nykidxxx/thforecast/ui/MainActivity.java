package com.nykidxxx.thforecast.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nykidxxx.thforecast.R;
import com.nykidxxx.thforecast.weather.Current;
import com.nykidxxx.thforecast.weather.Day;
import com.nykidxxx.thforecast.weather.Forecast;
import com.nykidxxx.thforecast.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String SUN_IS_UP = "SUN_IS_UP";
    public boolean sunIsUp;
    public boolean newInstance = false;

    private Forecast mForecast;
    private Current mCurrent;
    final double latitude = 40.606648;
    final double longitude = -73.978133;

    @BindView(R.id.mainActivityLayout) RelativeLayout mMainActivityLayout;
    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.sunValue) TextView mSunValue;
    @BindView(R.id.sunLabelButton) Button mSunLabel;
    @BindView(R.id.pwhValue) TextView mPWHValue;
    @BindView(R.id.pwhLabelButton) Button mPWHLabel;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.imageViewIcon) ImageView mImageViewIcon;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.minTempLabel) TextView mMinTempLabel;
    @BindView(R.id.maxTempLabel) TextView mMaxTempLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        newInstance = true;

        mProgressBar.setVisibility(View.INVISIBLE);

        getForecast(latitude, longitude);

        Log.d(TAG, "Main ID code is running!");
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "56c3cbe4662b381256af30ccba733b8f";

        String forecastURL = "https://api.darksky.net/forecast/" +
                                apiKey + "/" + latitude + "," + longitude;

        if(isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        }
                        else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, R.string.network_unavailable_message,
                                Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private Forecast parseForecastDetails(String jsonData) throws  JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            days[i] = day;
        }

        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[25];

        for (int i = 0; i < 25; i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        //"throws JSONException" moves the responsibility for handling the exception.
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        Log.i(TAG, "From JSON: " + currently);

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);
        current.setWindSpeed(currently.getDouble("windSpeed"));
        Log.d(TAG, current.getFormattedTime());

        JSONObject todayFromDaily = forecast.getJSONObject("daily")
                                            .getJSONArray("data")
                                            .getJSONObject(0);
        current.setSunsetTime(todayFromDaily.getLong("sunsetTime"));
        current.setSunriseTime(todayFromDaily.getLong("sunriseTime"));
        current.setMinTemp(todayFromDaily.getDouble("temperatureMin"));
        current.setMaxTemp(todayFromDaily.getDouble("temperatureMax"));
        Log.d(TAG, current.getSunriseTime()+"");
        Log.d(TAG, current.getTime()+"");
        Log.d(TAG, current.getSunsetTime()+"");

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                                        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected())
            isAvailable = true;

        return isAvailable;
    }

    private  void alertUserAboutError(){
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");

    }


    private void updateDisplay() {
        mCurrent = mForecast.getCurrent();

        mTemperatureLabel.setText(mCurrent.getTemperature()+"");
        mTimeLabel.setText("At " + mCurrent.getFormattedTime() + " it is");
        mSummaryLabel.setText(mCurrent.getSummary());
        mMinTempLabel.setText(mCurrent.getMinTemp()+"");
        mMaxTempLabel.setText(mCurrent.getMaxTemp()+"");

        Drawable drawable = getResources().getDrawable(mCurrent.getIconId());
        mImageViewIcon.setImageDrawable(drawable);

        Log.d(TAG, "Is Sun up? :"+mCurrent.isSunUp());

        if(!mCurrent.isSunUp()) {
            sunIsUp = false;
            mMainActivityLayout.setBackground(getResources().getDrawable(R.drawable.bg_gradient_night));
        } else {
            sunIsUp = true;
            mMainActivityLayout.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
        }

        if (newInstance){
            mSunLabel.setText("Sunrise");
            mSunValue.setText(mCurrent.getFormattedSunriseTime()+"");
            mPWHLabel.setText("Rain/Snow");
            mPWHValue.setText(mCurrent.getPrecipChance() + "%");
            newInstance = false;
        }
    }

    @OnClick (R.id.buttonDaily)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        intent.putExtra(SUN_IS_UP, sunIsUp);
        startActivity(intent);
    }

    @OnClick (R.id.buttonHourly)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        intent.putExtra(SUN_IS_UP, sunIsUp);
        startActivity(intent);
    }

    @OnClick (R.id.temperatureLabel)
    public void tempLabelClicked(View view){
        getForecast(latitude, longitude);
    }

    @OnClick (R.id.sunLabelButton)
    public void sunLabelButtonClicked(View view){
        if(mSunLabel.getText() == "Sunrise") {
            mSunLabel.setText("Sunset");
            mSunValue.setText(mCurrent.getFormattedSunsetTime()+"");
        }
        else {
            mSunLabel.setText("Sunrise");
            mSunValue.setText(mCurrent.getFormattedSunriseTime()+"");
        }
    }

    @OnClick (R.id.pwhLabelButton)
    public void pwhLabelButtonClicked(View view){
        if(mPWHLabel.getText() == "Rain/Snow") {
            mPWHLabel.setText("Wind Speed");
            mPWHValue.setText(mCurrent.getWindSpeed() +"");
        }
        else if (mPWHLabel.getText() == "Wind Speed"){
            mPWHLabel.setText("Humidity");
            mPWHValue.setText(mCurrent.getHumidity()+"");
        } else {
            mPWHLabel.setText("Rain/Snow");
            mPWHValue.setText(mCurrent.getPrecipChance() + "%");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getForecast(latitude, longitude);
    }
}
