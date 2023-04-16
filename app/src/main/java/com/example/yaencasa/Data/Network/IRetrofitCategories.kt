package com.example.yaencasa.Data.Network

import com.example.yaencasa.Data.ModelCategory
import com.example.yaencasa.Data.ModelOrder
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface IRetrofitCategories {


    @GET("FetchCategories.php")
    fun fetchCategories():Call<ArrayList<ModelCategory>>


}