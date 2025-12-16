package com.example.shopinventoryapp

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Items>>(emptyList())
    val items: StateFlow<List<Items>> = _items

    private val _buyerDetails = MutableStateFlow<List<BuyerDetails>>(emptyList())
    val buyerDetails: StateFlow<List<BuyerDetails>> = _buyerDetails


    private val _adminTotalSales = MutableStateFlow(0.0)
    val adminTotalSales: StateFlow<Double> = _adminTotalSales

    private val _adminTotalProfit = MutableStateFlow(0.0)
    val adminTotalProfit: StateFlow<Double> = _adminTotalProfit
    private val _userPendingPayment = MutableStateFlow(0.0)
    val userPendingPayment: StateFlow<Double> = _userPendingPayment
    private val _userGrandTotal = MutableStateFlow(0.0)
    val userGrandTotal: StateFlow<Double> = _userGrandTotal

    private val _paidTotal = MutableStateFlow(0.0)
    val paidTotal: StateFlow<Double> = _paidTotal
    fun loadAdminBuyerDetails() {
        val db = FirebaseFirestore.getInstance()

        db.collection("buyerDetails")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.toObjects(BuyerDetails::class.java)
                    _buyerDetails.value = list


                    var totalSales = 0.0
                    var totalProfit = 0.0

                    list.forEach {
                        totalSales += it.singleItemSales
                        totalProfit += it.singleItemprofit
                    }

                    _adminTotalSales.value = totalSales
                    _adminTotalProfit.value = totalProfit
                }
            }
    }

    fun loadingPayment(uid: String?) {
        val db = FirebaseFirestore.getInstance()
        db.collection("buyerDetails").whereEqualTo("buyerUid",uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.toObjects(BuyerDetails::class.java)
                    _buyerDetails.value = list
                    var pendingPayment = 0.0
                    var paidPayment = 0.0
                    var grandTotal = 0.0
                    list.forEach { _ ->
                        grandTotal = list.sumOf { it.totalprice }
                        paidPayment = list.filter { it.status == true }.sumOf { it.totalprice }
                        pendingPayment = grandTotal - paidPayment
                    }

                    _userGrandTotal.value = grandTotal
                    _paidTotal.value = paidPayment
                    _userPendingPayment.value = pendingPayment
                }

            }
    }

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message
    private val _users = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?> = _users


    init {
        displayItems()

    }

    private val _payingItemId = MutableStateFlow<String?>(null)
    val payingItemId = _payingItemId.asStateFlow()

    fun payItem(firestoreId: String) {

        if (_payingItemId.value == firestoreId) return

        _payingItemId.value = firestoreId

        val db = Firebase.firestore
        val collectionName = "buyerDetails"
        val docRef = db.collection(collectionName).document(firestoreId)
        val adminUid = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

        val updates = hashMapOf<String, Any>(
            "status" to true,
            "paidAt" to FieldValue.serverTimestamp(),
            "paidBy" to adminUid
        )

        docRef.update(updates)
            .addOnSuccessListener {
                _payingItemId.value = null

            }
            .addOnFailureListener { e ->
                _payingItemId.value = null

            }
    }

    fun getUsers(uid: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid)
            .get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val displayName = document.getString("displayName") ?: ""
                    val email = document.getString("email") ?: ""
                    _users.value = Users(
                        displayName = displayName,
                        email = email
                    )
                }
            }.addOnFailureListener {
                println("User Failed")
            }
    }

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
        db.collection("items").addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                val itemsList = snapshot.toObjects(Items::class.java)
                _items.value = itemsList
            }
        }
    }


    fun displayBuyerDetails(uid: String? = null) {
        val db = FirebaseFirestore.getInstance()
        val base = db.collection("buyerDetails")
        val query = if (!uid.isNullOrBlank()) base.whereEqualTo("buyerUid", uid) else base
        query.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val list = snapshot.toObjects(BuyerDetails::class.java)
                _buyerDetails.value = list
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
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid ?: ""
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
                buyerUid = currentUser,
                itemName = item.name,
                status = false,
                createdAt = Timestamp.now(),
                singleItemSales = item.salesPrice * buyerDetails.requestedQuantity,
                singleItemprofit = (item.salesPrice - item.unitPrice) * buyerDetails.requestedQuantity
            )
            buyerDocRef.set(newBuyer)
            _message.value = "Item Sold Successfully"
        }
    }

    /*  fun AdminLogin(uid: String, email: String, role: String) {

          val db = FirebaseFirestore.getInstance()
          val admin = AdLogin(uid = uid, email = email, role = role)
          db.collection("Admin").document(uid).set(admin)
              .addOnSuccessListener {
                  println("Admin Added")
              }
              .addOnFailureListener { e -> println("Admin Failed,$e") }


      }*/

    fun UserSignUp(displayName: String, email: String, role: String, uid: String?) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")
        val users = SignUp(displayName, email, role, uid, createdAt = Timestamp.now())
        usersCollection.document(uid ?: return)
            .set(users).addOnSuccessListener {
                println("User Added")

            }.addOnFailureListener {
                println("User Failed")
            }


    }
}