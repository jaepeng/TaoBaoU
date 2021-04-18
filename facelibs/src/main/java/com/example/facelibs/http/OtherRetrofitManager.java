package com.example.facelibs.http;

import com.example.baselib.AppConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtherRetrofitManager {
    private static final OtherRetrofitManager ourInstance = new OtherRetrofitManager();
    private final Retrofit mRetrofit;

    public static OtherRetrofitManager getInstance() {
        return ourInstance;
    }

    private OtherRetrofitManager() {

        //创建retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.BASE_OTHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public Api getApiService(){
        return mRetrofit.create(Api.class);
    }
}
