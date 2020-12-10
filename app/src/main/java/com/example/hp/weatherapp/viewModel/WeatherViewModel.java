package com.example.hp.weatherapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Looper;
import android.util.Log;

import com.example.hp.weatherapp.ErrorResponseListener;
import com.example.hp.weatherapp.Utils;
import com.example.hp.weatherapp.model.List;
import com.example.hp.weatherapp.model.WeatherAdapterData;
import com.example.hp.weatherapp.model.WeatherModel;
import com.example.hp.weatherapp.retrofitClass.Api;
import com.example.hp.weatherapp.retrofitClass.RetrofitClient;

import java.util.ArrayList;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Weather Data fetching logic is present in this class
 */
public class WeatherViewModel extends ViewModel {
    private static final String TAG = "WeatherVM";
    private MutableLiveData<WeatherModel> weatherModelMutableLiveData;
    private Vector<ErrorResponseListener> errorResponseListeners;
    private MutableLiveData<ArrayList<WeatherAdapterData>> weatherAdapterDataMutableLiveData;

    /**
     * This method is used to observe change in WeatherModel data
     * @return
     */
    public LiveData<WeatherModel> getObservableWeatherData(){
        if(weatherModelMutableLiveData == null){
            errorResponseListeners = new Vector<>();
            weatherModelMutableLiveData = new MutableLiveData<>();
            weatherAdapterDataMutableLiveData = new MutableLiveData<>();
        }
        return weatherModelMutableLiveData;
    }

    /**
     * This method is used to observe WeatherAdapterData list
     * @return
     */
    public LiveData<ArrayList<WeatherAdapterData>> getObservableWeatherAdapterData(){
        return weatherAdapterDataMutableLiveData;
    }

    /**
     * This method is used to fire the weather API with default city as bengaluru & fetch the WeatherModel object
     * After fetching WeatherModel data successfully, 5 day prediction data is created for displaying in the adapter
     */
    public void getWeatherData(){
        final Call<WeatherModel> weatherModelCall = RetrofitClient.getInstance().create(Api.class).getListOfData(Utils.CITY, Utils.API_KEY);

        weatherModelCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                if(response != null && response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.body().toString());
                    createWeatherAdapterData(response.body());
                    if(Looper.getMainLooper().isCurrentThread()){
                        weatherModelMutableLiveData.setValue(response.body());
                    }else{
                        weatherModelMutableLiveData.postValue(response.body());
                    }
                } else{
                    for(ErrorResponseListener errorResponseListener : errorResponseListeners){
                        errorResponseListener.onFail(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                for(ErrorResponseListener errorResponseListener : errorResponseListeners){
                    errorResponseListener.onFail(t.getMessage());
                }
            }
        });
    }

    /**
     * This method is used to create the weather adapter data from main response.
     * As the response contains data for every 3hrs for 5 days, we need to extract single day data
     * @param weatherModel
     */
    private void createWeatherAdapterData(WeatherModel weatherModel){
        ArrayList<List> historicalData = (ArrayList<List>) weatherModel.getList();
        ArrayList<WeatherAdapterData> weatherAdapterDataArrayList = new ArrayList<>();
        String tempTime = "";
        for(int i = 0; i < historicalData.size(); i++){
            if(i == 0){
                String[] dateArr = historicalData.get(i).getDtTxt().split(" ");
                tempTime = dateArr[1].split(":")[0];
                String date = dateArr[0];
                String skyDescp = historicalData.get(0).getWeather().get(0).getDescription();
                Double minTemp = historicalData.get(0).getMain().getTempMin() - 273; //Kelvin -273
                Double maxTemp = historicalData.get(0).getMain().getTempMax() - 273;
                weatherAdapterDataArrayList.add(new WeatherAdapterData(date, skyDescp, Utils.df.format(minTemp), Utils.df.format(maxTemp)));
            }else{
                String[] dateArr = historicalData.get(i).getDtTxt().split(" ");
                String time = dateArr[1].split(":")[0];
                if(tempTime.equalsIgnoreCase(time)){
                    String date = dateArr[0];
                    String skyDescp = historicalData.get(i).getWeather().get(0).getDescription();
                    Double minTemp = historicalData.get(i).getMain().getTempMin() - 273; //Kelvin -273
                    Double maxTemp = historicalData.get(i).getMain().getTempMax() - 273;
                    weatherAdapterDataArrayList.add(new WeatherAdapterData(date, skyDescp, Utils.df.format(minTemp), Utils.df.format(maxTemp)));
                }
            }
        }

        weatherAdapterDataMutableLiveData.setValue(weatherAdapterDataArrayList);
    }

    /**
     * add error response
     * @param errorResponseListener
     */
    public void addResponseListener(ErrorResponseListener errorResponseListener){
        if(errorResponseListener != null && !errorResponseListeners.contains(errorResponseListener)){
            errorResponseListeners.add(errorResponseListener);
        }
    }

    /**
     * remove error response
     * @param errorResponseListener
     */
    public void removeResponseListener(ErrorResponseListener errorResponseListener){
        if(errorResponseListener != null && !errorResponseListeners.contains(errorResponseListener)){
            errorResponseListeners.remove(errorResponseListener);
        }
    }
}
