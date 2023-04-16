package com.example.yaencasa.Data.Network

import com.example.yaencasa.Auxiliary.Constants
import com.example.yaencasa.Data.ModelCategory
import com.example.yaencasa.Data.ModelZone
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class RetrofitZoneslmpl :IRetrofitZones{

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PHP_FILES)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productApi: IRetrofitZones = retrofit.create(IRetrofitZones::class.java)

    override fun fetchZones(): Call<ArrayList<ModelZone>> {
        return productApi.fetchZones()
    }


    override fun addZone(token: String,idZone: Long,name: String,price: Double): Call<String> {
        return productApi.addZone(token,idZone,name,price)
    }


    override fun removeZone(token: String, idZone: Long): Call<String> {
        return productApi.removeZone(token, idZone);
    }

}