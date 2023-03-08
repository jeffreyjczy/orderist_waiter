package com.example.orderistwaiter

data class Orders (
    val tableNo: Int = 0,
    val orders: ArrayList<Food> = ArrayList<Food>(),
    var id: String =""
)
