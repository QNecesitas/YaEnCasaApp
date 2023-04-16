package com.example.yaencasa.Data.Network

import com.example.yaencasa.Data.ModelCategory
import com.example.yaencasa.Data.ModelZone
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.ArrayList

interface IRetrofitZones {

    @FormUrlEncoded
    @POST("RemoveZone.php")
    fun removeZone(
        @Field("token") token: String,
        @Field("idZone") idZone: Long
    ) : Call<String>


    @FormUrlEncoded
    @POST("AddZone.php")
    fun addZone(
        @Field("token") token: String,
        @Field("idZone") idZone: Long,
        @Field("name") name: String,
        @Field("price") price: Double
    ) : Call<String>

    @GET("FetchZones.php")
    fun fetchZones(): Call<ArrayList<ModelZone>>

}