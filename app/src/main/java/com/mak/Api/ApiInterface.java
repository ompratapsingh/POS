package com.mak.Api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mak.pos.Model.POJO.MenuCategory;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.TableInfo;
import com.mak.pos.Model.POJO.UserModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers("content-type: application/json")
    @POST("user/login")
    Call<UserModel> onLoginAttempt(@Body JsonObject userBody);

    @Headers("content-type: application/json")
    @GET("user/table/Super")
    Call<ArrayList<TableInfo>> onGetTableInfo();


    @Headers("content-type: application/json")
    @GET("menu/categories")
    Call<ArrayList<MenuCategory>> onGetAllCategory();


    @Headers("content-type: application/json")
    @POST("menu/generateBill")
    Call<Cart> onGenrateBill(@Body JsonObject object);


    @Headers("content-type: application/json")
    @POST("menu/generateBill")
    Call<JsonObject> onGenrateBillTest(@Body JsonObject object);


    @Headers("content-type: application/json")
    @POST("menu/paymentSettlement")
    Call<ResponseBody> onPaymentSettlement(@Body JsonObject object);
/*
    @Headers("content-type: application/json")
    @GET("menu/menu/000007")
    Call<ArrayList<MenuItemInfo>> onGetAllCategoryItem();*/

    @Headers("content-type: application/json")
    @GET("menu/{id}/items")
    Call<ArrayList<MenuItemInfo>> onGetAllCategoryItem(@Path("id") String id);



    @Headers("content-type: application/json")
    @GET("user/{id}/salesman")
    Call<JsonObject> onGetSalesMenList(@Path("id") String id);

    @Headers("content-type: application/json")
    @GET("user/logout/{id}")
    Call<ResponseBody> onUserLogOut(@Path("id") String id);

    @Headers("content-type: application/json")
    @GET("user/getReport")
    Call<JsonObject> oGetReport();


    @Headers("content-type: application/json")
    @GET("menu/{id}/cart")
    Call<Cart> onGetCart(@Path("id") String id);

    @Headers("content-type: application/json")
    @POST("menu/savecart")
    Call<ResponseBody> onSaveCart(@Body JsonObject cart);



    @PATCH("user/table/{id}/status/{status}")
    Call<ResponseBody> onTableStatusUpdate(@Path ("id") String id, @Path ("status") String status);

}
