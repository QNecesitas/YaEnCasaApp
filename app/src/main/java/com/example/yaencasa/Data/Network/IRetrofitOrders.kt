package com.example.yaencasa.Data.Network

import com.example.yaencasa.Data.ModelOrder
import com.example.yaencasa.Data.ModelProduct
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface IRetrofitOrders {


    @FormUrlEncoded
    @POST("CleanOrders.php")
    fun cleanOrders(
        @Field("token") token: String
    ) : Call<String>


    @FormUrlEncoded
    @POST("UpdateOrder.php")
    fun updateOrder(
        @Field("token") token: String,
        @Field("id") id:Int,
        @Field("state") state: String,
        @Field("descr") descr: String
    ) : Call<String>



    @FormUrlEncoded
    @POST("AddOrder.php")
    fun addOrder(
        @Field("token") token: String,
        @Field("price") price: Double,
        @Field("products")products: String,
        @Field("celnumber") celnumber: String,
        @Field("location") location: String,
        @Field("address") address: String,
        @Field("name") name: String
    ) : Call<Int>



    @GET("FetchOrders.php")
    fun fetchOrders():Call<ArrayList<ModelOrder>>


}