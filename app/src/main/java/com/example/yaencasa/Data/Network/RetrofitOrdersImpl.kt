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

    override fun cleanMyOrders(token: String, idClient: Long): Call<String> {
        return productApi.cleanMyOrders(token, idClient);
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
        name: String,
        zone: String,
        idClient: Long
    ): Call<Int> {
        return productApi.addOrder(
            token,
            price,
            products,
            celnumber,
            location,
            address,
            name,
            zone,
            idClient
        );
    }

    override fun fetchOrders(token: String): Call<ArrayList<ModelOrder>> {
        return productApi.fetchOrders(token);
    }

    override fun fetchMyOrders(token: String, id: Long): Call<ArrayList<ModelOrder>> {
        return productApi.fetchMyOrders(token, id)
    }


}