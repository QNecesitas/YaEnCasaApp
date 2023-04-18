package com.example.yaencasa.Data

data class ModelOrder(
    val id: Int,
    val price: Double,
    val products: String,
    val celnumber: String,
    val actDate: String,
    val location: String,
    val address: String,
    val name: String,
    val zone: String,
    val state: String,
    val descr: String
)