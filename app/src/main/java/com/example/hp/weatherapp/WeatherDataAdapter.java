package com.example.hp.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.weatherapp.model.WeatherAdapterData;

import java.util.ArrayList;

public class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataAdapter.WeatherDataVH> {

    private ArrayList<WeatherAdapterData> weatherAdapterDataList;

    public void setAdapterData(ArrayList<WeatherAdapterData> adapterData){
        this.weatherAdapterDataList = adapterData;
        Log.d("Adpater", "setAdapterData: "+weatherAdapterDataList.size());
    }
    @NonNull
    @Override
    public WeatherDataVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item, viewGroup, false);
        return new WeatherDataVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDataVH weatherDataVH, int i) {
        final WeatherAdapterData weatherAdapterData = weatherAdapterDataList.get(i);
        weatherDataVH.getDate().setText(weatherAdapterData.getDate());
        weatherDataVH.getTemp().setText(weatherAdapterData.getMinTamp()+ "/"+weatherAdapterData.getMaxTemp());
        weatherDataVH.getSkyDesp().setText(weatherAdapterData.getSkyStatus());
        String status = weatherAdapterData.getSkyStatus();
        if(status.contains("clouds")){
            weatherDataVH.getSkyImage().setImageResource(R.drawable.cloud);
        }else if(status.contains("clear")){
            weatherDataVH.getSkyImage().setImageResource(R.drawable.sunny);
        }else{
            weatherDataVH.getSkyImage().setImageResource(R.drawable.rain);
        }
    }

    @Override
    public int getItemCount() {
        return weatherAdapterDataList.size();
    }

    public class WeatherDataVH extends RecyclerView.ViewHolder {

        private TextView date;

        public TextView getDate() {
            return date;
        }

        public TextView getSkyDesp() {
            return skyDesp;
        }

        public TextView getTemp() {
            return temp;
        }

        public ImageView getSkyImage() {
            return skyImage;
        }

        private TextView skyDesp;
        private TextView temp;
        private ImageView skyImage;
        public WeatherDataVH(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            skyDesp = (TextView) itemView.findViewById(R.id.sky_descp);
            temp = (TextView) itemView.findViewById(R.id.temp_txt);
            skyImage = (ImageView) itemView.findViewById(R.id.sky_image);
        }
    }
}
