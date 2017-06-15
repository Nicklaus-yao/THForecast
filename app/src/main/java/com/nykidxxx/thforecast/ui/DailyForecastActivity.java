package com.nykidxxx.thforecast.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nykidxxx.thforecast.R;
import com.nykidxxx.thforecast.adapters.DayAdapter;
import com.nykidxxx.thforecast.weather.Day;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyForecastActivity extends AppCompatActivity {

    private Day[] mDays;

    @BindView(R.id.dailyActivityLayout) RelativeLayout mDailyActivityLayout;
    @BindView(android.R.id.list) ListView mListView;
    @BindView(android.R.id.empty) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        boolean sunIsUp = intent.getBooleanExtra(MainActivity.SUN_IS_UP, true);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String dayOfTheWeek = mDays[i].getDayOfTheWeek();
                String conditions = mDays[i].getSummary();
                String highTemp = mDays[i].getTemperatureMax()+"";
                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        highTemp,
                        conditions);
                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        if(!sunIsUp) {
            mDailyActivityLayout.setBackground(getResources().getDrawable(R.drawable.bg_gradient_night));
        } else {
            mDailyActivityLayout.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
        }



    }
}













