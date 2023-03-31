package com.example.yaencasa.Data

import com.example.yaencasa.Network.IRetrofitProducts
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.http.Body

class RemoteProductsDataSource(
    private val retrofitProduct:IRetrofitProducts,
    private val ioDispatcher: CoroutineDispatcher
    ){

    suspend fun updateVisibilityProduct(token: String,idProduct: Long,state: Int) : Boolean=
        withContext(ioDispatcher){
            retrofitProduct.updateVisibilityProduct(token,idProduct,state)=="Success"
        }

    suspend fun addProduct(token: String,product: ModelProduct) : Boolean=
        withContext(ioDispatcher) {
            retrofitProduct.addProduct(token,product)=="Success"
        }

    suspend fun removeProduct(token: String,idProduct: Long) : Boolean=
        withContext(ioDispatcher){
            retrofitProduct.removeProduct(token,idProduct)=="Success"
        }

    suspend fun updateProduct(token: String,product: ModelProduct) : Boolean=
        withContext(ioDispatcher) {
            retrofitProduct.updateProduct(token,product)=="Success"
        }

    suspend fun fetchProducts(token: String,id_category:Int):ArrayList<ModelProduct> =
        withContext(ioDispatcher){
            retrofitProduct.fetchProducts(token,id_category)
        }

}