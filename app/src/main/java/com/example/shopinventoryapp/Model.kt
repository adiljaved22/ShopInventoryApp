package com.example.shopinventoryapp


data class Items(
    val id: Int = 0,
    val firestoreId: String = "",
    val date: String = "",
    val name: String = "",
    val unitPrice: Double = 0.0,
    val quantity: Int = 0
)