package com.example.yaencasa.Data

import kotlinx.coroutines.withContext

    class ProductRepository(val remoteDataSource: RemoteProductsDataSource){

        suspend fun updateVisibilityProduct(idProduct: Long,state: Int) : Boolean {
           return remoteDataSource.updateVisibilityProduct(idProduct, state)
        }

        suspend fun addProduct(product: ModelProduct) : Boolean{
            return remoteDataSource.addProduct(product)
        }


        suspend fun removeProduct(idProduct: Long) : Boolean {
            return remoteDataSource.removeProduct(idProduct)
        }

        suspend fun updateProduct(product: ModelProduct) : Boolean{
            return remoteDataSource.updateProduct(product)
        }

        suspend fun fetchProducts(id_category:Int):ArrayList<ModelProduct>{
            return remoteDataSource.fetchProducts(id_category)
        }

    }