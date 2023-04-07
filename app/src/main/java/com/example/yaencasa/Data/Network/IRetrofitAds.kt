package com.example.yaencasa.Data.Network

import com.example.yaencasa.Data.ModelAd
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface IRetrofitAds {

    @GET("FetchAds.php")
    fun fetchAds():Call<ArrayList<ModelAd>>


}