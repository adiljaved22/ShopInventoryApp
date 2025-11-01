package com.example.shopinventoryapp

import android.content.ClipData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Items>>(emptyList())
    val items: StateFlow<List<Items>> = _items
    fun addItems(item: Items) {
        val db = FirebaseFirestore.getInstance()
        val id = db.collection("items").document()
        val itemId = item.copy(firestoreId = id.id)
        id.set(itemId)
            .addOnSuccessListener { println("Item Added") }
            .addOnFailureListener { e -> println("Item Failed,$e") }


    }

    fun displayItems() {
        val db = FirebaseFirestore.getInstance()
        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                val itemsList = mutableListOf<Items>()
                for (document in result) {
                    val item = document.toObject(Items::class.java)
                    itemsList.add(item)
                }
                _items.value = itemsList
                println("Added Successfully")
            }
            .addOnFailureListener { exception ->
                println("Error h bhai: $exception")
            }

    }

}