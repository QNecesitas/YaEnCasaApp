package com.example.yaencasa.Data.Network

import com.example.yaencasa.Auxiliary.Constants
import com.example.yaencasa.Data.ModelOrder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class RetrofitOrdersImpl :IRetrofitOrders{

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PHP_FILES)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productApi: IRetrofitOrders = retrofit.create(IRetrofitOrders::class.java)

    override fun cleanOrders(token: String): Call<String> {
        return productApi.cleanOrders(token);
    }

    override fun updateOrder(token: String, id: Int, state: String, descr: String): Call<String> {
        return productApi.updateOrder(token, id, state, descr);
    }

    override fun addOrder(
        token: String,
        price: Double,
        products: String,
        celnumber: String,
        location: String,
        address: String,
        name: String
    ): Call<Int> {
        return productApi.addOrder(token, price, products, celnumber,  location, address, name);
    }

    override fun fetchOrders(): Call<ArrayList<ModelOrder>> {
        return productApi.fetchOrders();
    }


}