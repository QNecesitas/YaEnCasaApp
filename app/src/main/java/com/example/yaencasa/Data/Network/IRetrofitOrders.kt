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
    @POST("CleanMyOrders.php")
    fun cleanMyOrders(
        @Field("token") token: String,
        @Field("idClient") idClient: Long
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
        @Field("products") products: String,
        @Field("celnumber") celnumber: String,
        @Field("location") location: String,
        @Field("address") address: String,
        @Field("name") name: String,
        @Field("zone") zone: String,
        @Field("idClient") idClient: Long
    ) : Call<Int>



    @GET("FetchOrders.php")
    fun fetchOrders(@Query("token")token: String):Call<ArrayList<ModelOrder>>

    @GET("FetchMyOrders.php")
    fun fetchMyOrders(
        @Query("token")token: String,
        @Query("idClient")id :Long
    ):Call<ArrayList<ModelOrder>>


}