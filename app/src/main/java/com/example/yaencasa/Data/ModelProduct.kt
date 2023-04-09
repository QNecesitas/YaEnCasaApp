package com.example.yaencasa.Data

data class ModelProduct(
    var idProduct:Long,
    var name: String,
    var price: Double,
    var state:Boolean,
    var desc: String,
    var idCategory: Int,
    var statePhoto: Boolean
    ): IModel_Content