package net.ricardochavezt.budgetbuddy.api;

import net.ricardochavezt.budgetbuddy.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    public static <T> T createClient(Class<T> apiClass) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(apiClass);
    }
}
