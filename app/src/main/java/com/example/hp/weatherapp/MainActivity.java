package com.example.hp.weatherapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.weatherapp.model.WeatherAdapterData;
import com.example.hp.weatherapp.model.WeatherModel;
import com.example.hp.weatherapp.viewModel.WeatherViewModel;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements ErrorResponseListener, View.OnClickListener{

    private static final String TAG = "MainActivity";
    private WeatherViewModel weatherViewModel;
    private WeatherDataAdapter weatherDataAdapter;
    private RecyclerView weatherRecyclerView;
    private RecyclerView.LayoutManager linearLayMgr;
    private TextView currTemp;
    private TextView cityName;
    private LinearLayout errRetryLay;
    private RelativeLayout weatherDataLay;
    private TextView retryLoadTxt;
    private Double currentTempResp;
    private String cityNameResp;

    /**
     * This is used to notify the UI components on change in WeatherModel data
     */
    private Observer<WeatherModel> mutableLiveDataObserver = new Observer<WeatherModel>() {
        @Override
        public void onChanged(@Nullable WeatherModel weatherModel) {

            cityNameResp = weatherModel.getCity().getName();
            currentTempResp = weatherModel.getList().get(0).getMain().getTemp() - 273;

            currTemp.setText(Utils.df.format(currentTempResp));
            cityName.setText(cityNameResp);

            errRetryLay.setVisibility(View.GONE);
            weatherDataLay.setVisibility(View.VISIBLE);
//            for (WeatherAdapterData data : weatherAdapterDataList){
//                Log.d(TAG, "onChanged: "+data.toString());
//            }
        }
    };

    /**
     * This is used to observe changes in WeatherAdapterData list & notify the adapter
     */
    private Observer<ArrayList<WeatherAdapterData>> arrayListObserver = new Observer<ArrayList<WeatherAdapterData>>() {
        @Override
        public void onChanged(@Nullable ArrayList<WeatherAdapterData> weatherAdapterData) {
            Log.d(TAG, "onChanged: size :: "+weatherAdapterData.size());
            weatherDataAdapter.setAdapterData(weatherAdapterData);
            weatherDataAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViewsById();

        retryLoadTxt.setOnClickListener(this);

        adapterInitialization();

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
    }


    private void findAllViewsById(){
        weatherRecyclerView = findViewById(R.id.recyclerView);
        currTemp = findViewById(R.id.currentTemp);
        cityName = findViewById(R.id.cityName);

        errRetryLay = findViewById(R.id.loadingHideableLayout);
        weatherDataLay = findViewById(R.id.weatherHideableLay);
        retryLoadTxt = findViewById(R.id.loadingRetryTxt);
    }

    private void adapterInitialization(){
        weatherRecyclerView.setHasFixedSize(true);
        linearLayMgr = new LinearLayoutManager(this);
        weatherRecyclerView.setLayoutManager(linearLayMgr);
        weatherDataAdapter = new WeatherDataAdapter();
        weatherRecyclerView.setAdapter(weatherDataAdapter);
        weatherRecyclerView.setAlpha(0.8f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        weatherViewModel.getObservableWeatherData().observe(this, mutableLiveDataObserver);
        weatherViewModel.getObservableWeatherAdapterData().observe(this,arrayListObserver);
        weatherViewModel.addResponseListener(this);
        weatherViewModel.getWeatherData();

    }

    @Override
    protected void onStop() {
        super.onStop();
        weatherViewModel.removeResponseListener(this);
        weatherViewModel.getObservableWeatherData().removeObserver(mutableLiveDataObserver);
        weatherViewModel.getObservableWeatherAdapterData().removeObserver(arrayListObserver);
    }


    @Override
    public void onFail(String failMsg) {
        Log.d(TAG, "onFail: "+failMsg);
        weatherDataLay.setVisibility(View.GONE);
        errRetryLay.setVisibility(View.VISIBLE);
        retryLoadTxt.setText("Click to retry");
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();

        switch (id){
            case R.id.loadingRetryTxt:
                retryLoadTxt.setText("Loading...");
                weatherViewModel.getWeatherData();
                break;
        }
    }
}
