package com.example.yaencasa.Network

import com.example.yaencasa.Data.ModelProduct
import retrofit2.Call
import retrofit2.http.*

interface IRetrofitProducts {

    @FormUrlEncoded
    @POST("UpdateVisibilityProduct.php")
    fun updateVisibilityProduct(
        @Field("token") token: String,
        @Field("idProduct") idProduct: Long,
        @Field("state") stateProduct: Int
    ) : String

    @FormUrlEncoded
    @POST("UpdateProduct.php")
    fun updateProduct(
        @Field("token") token: String,
        @Body product: ModelProduct
    ) : String

    @FormUrlEncoded
    @POST("RemoveProduct.php")
    fun removeProduct(
        @Field("token") token: String,
        @Field("idProduct") idProduct: Long
    ) : String

    @FormUrlEncoded
    @POST("AddProduct.php")
    fun addProduct(
        @Field("token") token: String,
        @Body product: ModelProduct
    ) : String

    @GET("FetchProducts.php")
    fun fetchProducts(
        @Field("token") token: String,
        @Query("idCategory") idCategory: Int
    ):ArrayList<ModelProduct>


}