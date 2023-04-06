package com.example.yaencasa.Data

data class ModelProduct(
    var id:Long,
    var name: String,
    var price: Double,
    var desc: String,
    var state:Boolean
    ): IModel_Content
