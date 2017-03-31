package com.nykidxxx.thforecast.adapters;
// Created on 3/29/2017

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nykidxxx.thforecast.R;
import com.nykidxxx.thforecast.weather.Day;

public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter (Context context, Day[] days){
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int i) {
        return 0; //Will not be used
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            //brand new view
            view = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
            holder.temperatureLabel = (TextView) view.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) view.findViewById(R.id.dayLabel);
            holder.imageViewCircle = (ImageView) view.findViewById(R.id.imageViewCircle);

            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }

        Day day = mDays[position];

        holder.imageViewIcon.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax()+"");
        if(position == 0) {
            holder.dayLabel.setText("Today");
        }
        else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        holder.imageViewCircle.setImageResource(R.mipmap.bg_temperature);

        return view;
    }

    private static class ViewHolder {
        ImageView imageViewIcon;
        TextView temperatureLabel;
        TextView dayLabel;
        ImageView imageViewCircle;
    }
}







