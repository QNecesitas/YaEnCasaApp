package com.example.yaencasa.Data.Network

import com.example.yaencasa.Auxiliary.Constants
import com.example.yaencasa.Data.ModelCategory
import com.example.yaencasa.Data.ModelOrder
import com.example.yaencasa.Data.ModelProduct
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.ArrayList

class RetrofitCategorialmpl :IRetrofitCategories {

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PHP_FILES)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productApi: IRetrofitCategories = retrofit.create(IRetrofitCategories::class.java)

    override fun fetchCategories(): Call<ArrayList<ModelCategory>> {
        return productApi.fetchCategories()
    }

}