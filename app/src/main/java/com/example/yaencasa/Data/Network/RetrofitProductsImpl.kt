package com.example.yaencasa.Data.Network

import com.example.yaencasa.Auxiliary.Constants
import com.example.yaencasa.Data.ModelProduct
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProductsImpl : IRetrofitProducts {

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PHP_FILES)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productApi: IRetrofitProducts = retrofit.create(IRetrofitProducts::class.java)

    override fun updateVisibilityProduct(token: String, idProduct: Long, stateProduct: Int): Call<String> {
        return productApi.updateVisibilityProduct(token, idProduct, stateProduct);
    }

    override fun updateProduct(token: String, file:String, id: Long, name: String, price: Double, desc: String): Call<String> {
        return productApi.updateProduct(token, file, id, name, price,desc)
    }

    override fun removeProduct(token: String, idProduct: Long): Call<String> {
        return productApi.removeProduct(token,idProduct)
    }

    override fun addProduct(token: String, file: String,idCategory: Int, id: Long, name: String, price: Double, desc: String): Call<String> {
        return productApi.addProduct(token,file,idCategory, id,name,price,desc)
    }

    override fun fetchProducts(token: String, idCategory: Int): Call<ArrayList<ModelProduct>> {
        return productApi.fetchProducts(token,idCategory)
    }


}