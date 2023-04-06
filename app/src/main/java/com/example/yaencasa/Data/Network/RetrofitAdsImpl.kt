package com.example.yaencasa.Data.Network

import com.example.yaencasa.Auxiliary.Constants
import com.example.yaencasa.Data.Model_Ad
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class RetrofitAdsImpl : IRetrofitAds {

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PHP_FILES_AD)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val adsApi: IRetrofitAds = retrofit.create(IRetrofitAds::class.java)
    override fun fetchAds(): Call<ArrayList<Model_Ad>> {
        return adsApi.fetchAds();
    }


}