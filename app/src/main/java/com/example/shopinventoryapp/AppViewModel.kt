package com.example.shopinventoryapp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Items>>(emptyList())
    val items: StateFlow<List<Items>> = _items
    private val _buyerDetails = MutableStateFlow<List<BuyerDetails>>(emptyList())
    val buyerDetails: StateFlow<List<BuyerDetails>> = _buyerDetails

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message
    init {
        displayItems()
        displayBuyerDetails()
    }

    fun addItems(item: Items) {
        val db = FirebaseFirestore.getInstance()
        val id = db.collection("items").document()
        val itemId = item.copy(firestoreId = id.id)
        id.set(itemId)
            .addOnSuccessListener { println("Item Added") }
            .addOnFailureListener { e -> println("Item Failed,$e") }


    }

    /*fun addBuyerDetails(details: BuyerDetails) {
        val db = FirebaseFirestore.getInstance()
        val id = db.collection("buyerDetails").document()
        val buyerId = details.copy(firestoreId = id.id)
        id.set(buyerId)
            .addOnSuccessListener { println("Buyer Details Added") }
            .addOnFailureListener { e -> println("Buyer Details Failed,$e") }
    }*/

    fun displayItems() {
        val db = FirebaseFirestore.getInstance()
        db.collection("items").addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                val itemsList = snapshot.toObjects(Items::class.java)
                _items.value = itemsList
            }
        }
    }

    fun displayBuyerDetails() {
        val db = FirebaseFirestore.getInstance()
        db.collection("buyerDetails").addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                val List = snapshot.toObjects(BuyerDetails::class.java)
                _buyerDetails.value = List
            }
        }
    }

    fun deleteItem(firestoreId: String) {
        val db = FirebaseFirestore.getInstance()
        val del = db.collection("items").document(firestoreId)
        del.delete()
            .addOnSuccessListener { println("Item Deleted") }
            .addOnFailureListener { e -> println("Error deleting item: $e") }
    }


    fun updateItem(item: Items) {
        val db = FirebaseFirestore.getInstance()
        db.collection("items").document(item.firestoreId)
            .set(item)
            .addOnSuccessListener { println("Item Updated") }
            .addOnFailureListener { e -> println("Error updating item: $e") }
    }

    fun sellItem(buyerDetails: BuyerDetails, item: Items) {
        viewModelScope.launch {
            if (item.currentStock < buyerDetails.requestedQuantity) {
                _message.value = "Out of Stock"
                return@launch
            }
            val updatedItem = item.copy(
                currentStock = item.currentStock - buyerDetails.requestedQuantity,
                profit = item.profit + (item.salesPrice - item.unitPrice) * buyerDetails.requestedQuantity
            )
            val db = FirebaseFirestore.getInstance()
            db.collection("items").document(item.firestoreId).set(updatedItem)
            val buyerDocRef = db.collection("buyerDetails").document()
            val newBuyer = buyerDetails.copy(
                firestoreId = buyerDocRef.id,
                itemName = item.name,
                totalSales = item.salesPrice * buyerDetails.requestedQuantity,
                profit = (item.salesPrice - item.unitPrice) * buyerDetails.requestedQuantity
            )
            buyerDocRef.set(newBuyer)
            _message.value = "Item Sold Successfully"
        }
    }


}