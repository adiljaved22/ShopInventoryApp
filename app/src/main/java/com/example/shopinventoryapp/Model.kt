package com.example.shopinventoryapp

import com.google.firebase.Timestamp


data class Items(
    val id: Int = 0,
    val firestoreId: String = "",
    val date: String = "",
    val name: String = "",
    val unitPrice: Double = 0.0,
    val currentStock: Int = 0,
    val salesPrice: Double = 0.0,
    val purchasePrice: Double = 0.0,
    val profit: Double = 0.0,
)

data class BuyerDetails(
    val firestoreId: String = "",
    val buyerUid: String = "",
    val itemName: String = "",
    val requestedQuantity: Int = 0,
    val totalprice: Double = 0.0,
    val SingleItemSales: Double = 0.0,
    val SingleItemprofit: Double = 0.0,
    val status: Boolean? = false,
    val createdAt: Timestamp = Timestamp.now()

)

data class SignUp(
    val displayName: String = "",
    val email: String = "",
    val role: String = "",
    val uid: String? = "",
    val createdAt: Timestamp = Timestamp.now()
)

data class Users(
    val uid: String="",
    val displayName: String = "",
    val email: String = "",
)

data class AdLogin(
    val email: String = "",
    val uid: String = "",
    val role: String = "",

    )