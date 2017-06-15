package com.nykidxxx.thforecast.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.nykidxxx.thforecast.R;
import com.nykidxxx.thforecast.adapters.HourAdapter;
import com.nykidxxx.thforecast.weather.Hour;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends AppCompatActivity {

    private Hour[] mHours;

    @BindView(R.id.hourlyActivityLayout) RelativeLayout mHourlyActivityLayout;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        boolean sunIsUp = intent.getBooleanExtra(MainActivity.SUN_IS_UP, true);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        if(!sunIsUp) {
            mHourlyActivityLayout.setBackground(getResources().getDrawable(R.drawable.bg_gradient_night));
        } else {
            mHourlyActivityLayout.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
        }

    }
}

























