package com.example.shopinventoryapp


data class Items(
    val id: Int = 0,
    val firestoreId: String = "",
    val date: String = "",
    val name: String = "",
    val unitPrice: Double = 0.0,
    val currentStock: Int = 0,
    val salesPrice: Double = 0.0,
    val purchasePrice: Double =0.0,

    val profit : Double =0.0,
)

data class BuyerDetails(
    val firestoreId: String = "",
    val buyerName: String = "",
    val itemName: String = "",
    val requestedQuantity: Int = 0,
    val totalprice: Double=0.0,
    val totalSales : Double = 0.0,
    val profit : Double =0.0,

    )

data class SignUp(
    val email: String = "",
    val password: String = "",
    val role: String = "",
)

data class Login(
    val email: String = "",
    val password: String = "",
)