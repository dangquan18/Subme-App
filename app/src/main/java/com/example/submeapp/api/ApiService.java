package com.example.submeapp.api;

import com.example.submeapp.api.models.LoginRequest;
import com.example.submeapp.api.models.LoginResponse;
import com.example.submeapp.api.models.Notification;
import com.example.submeapp.api.models.NotificationResponse;
import com.example.submeapp.api.models.Plan;
import com.example.submeapp.api.models.ProfileResponse;
import com.example.submeapp.api.models.PurchaseRequest;
import com.example.submeapp.api.models.PurchaseResponse;
import com.example.submeapp.api.models.RegisterRequest;
import com.example.submeapp.api.models.RegisterResponse;
import com.example.submeapp.api.models.UpdateProfileRequest;
import com.example.submeapp.api.models.UserSubscription;
import com.example.submeapp.api.models.UserSubscriptionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("plans")
    Call<List<Plan>> getPlans();

    @GET("plans/{id}")
    Call<List<Plan>> getPlanDetail(@Path("id") int planId);

    @POST("subscription/purchase")
    Call<PurchaseResponse> purchaseSubscription(@Header("Authorization") String token, @Body PurchaseRequest request);

    @GET("subscription/user/{user_id}/active")
    Call<UserSubscriptionsResponse> getUserActiveSubscriptions(@Header("Authorization") String token, @Path("user_id") int userId);

    @GET("notification/user/{user_id}")
    Call<NotificationResponse> getUserNotifications(@Header("Authorization") String token, @Path("user_id") int userId);

    @GET("user/profile/{user_id}")
    Call<ProfileResponse> getUserProfile(@Header("Authorization") String token, @Path("user_id") int userId);

    @PUT("user/profile/{user_id}")
    Call<ProfileResponse> updateUserProfile(@Header("Authorization") String token, @Path("user_id") int userId, @Body UpdateProfileRequest request);
}

