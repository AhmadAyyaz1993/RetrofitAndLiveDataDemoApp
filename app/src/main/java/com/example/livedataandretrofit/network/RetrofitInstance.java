package com.example.livedataandretrofit.network;

import com.example.livedataandretrofit.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.livedataandretrofit.BuildConfig.BASE_URL;

public class RetrofitInstance {
    private static Retrofit retrofit = null;

    public static RestApiService getApiService(String url) {
        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit.create(RestApiService.class);

    }
}
