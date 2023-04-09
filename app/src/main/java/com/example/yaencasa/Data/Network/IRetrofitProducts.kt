package com.example.yaencasa.Data.Network

import com.example.yaencasa.Data.ModelProduct
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface IRetrofitProducts {



    @FormUrlEncoded
    @POST("UpdateVisibility.php")
    fun updateVisibilityProduct(
        @Field("token") token: String,
        @Field("idProduct") idProduct: Long,
        @Field("state") stateProduct: Int
    ) : Call<String>



    @FormUrlEncoded
    @POST("UpdateProduct.php")
    fun updateProduct(
        @Field("token") token: String,
        @Field("file") file: String,
        @Field("idProduct") id: Long,
        @Field("name") name: String,
        @Field("price") price: Double,
        @Field("desc") desc: String,
    ) : Call<String>



    @FormUrlEncoded
    @POST("DeleteProduct.php")
    fun removeProduct(
        @Field("token") token: String,
        @Field("idProduct") idProduct: Long
    ) : Call<String>



    @FormUrlEncoded
    @POST("AddProduct.php")
    fun addProduct(
        @Field("token") token: String,
        @Field("file") file: String,
        @Field("idCategory")idCategory: Int,
        @Field("idProduct") id: Long,
        @Field("name") name: String,
        @Field("price") price: Double,
        @Field("desc") desc: String
    ) : Call<String>



    @GET("FetchProducts.php")
    fun fetchProducts(
        @Query("token") token: String,
        @Query("idCategory") idCategory: Int
    ):Call<ArrayList<ModelProduct>>


}