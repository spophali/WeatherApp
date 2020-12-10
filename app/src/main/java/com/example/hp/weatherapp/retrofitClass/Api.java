package com.example.hp.weatherapp.retrofitClass;

import com.example.hp.weatherapp.model.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This class contains the API preparation logic
 */
public interface Api {
    @GET("/data/2.5/forecast")
    Call<WeatherModel> getListOfData(@Query("q") String q, @Query("appid") String appid);
}
