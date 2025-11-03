package com.example.submeapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    // Cho Android Emulator dùng 10.0.2.2
    // Cho thiết bị thật, thay bằng IP máy tính (vd: "http://192.168.1.100:3000/")
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    // Nếu dùng thiết bị thật và không chạy được, hãy thử:
    // 1. Tìm IP máy tính: ipconfig (Windows) hoặc ifconfig (Mac/Linux)
    // 2. Đổi BASE_URL thành "http://[IP_MÁY_TÍNH]:3000/"
    // Ví dụ: "http://192.168.1.5:3000/"

    private static Retrofit retrofit;
    private static ApiService apiService;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Logging interceptor để debug
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofitInstance().create(ApiService.class);
        }
        return apiService;
    }
}

