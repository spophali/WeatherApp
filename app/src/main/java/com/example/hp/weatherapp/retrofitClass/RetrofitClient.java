package com.example.hp.weatherapp.retrofitClass;

import com.example.hp.weatherapp.Utils;
import com.example.hp.weatherapp.retrofitClass.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is a singleton class which provides Retrofit object for performing API calls
 */
public class RetrofitClient {

    private static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

}
