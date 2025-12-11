package com.example.shopinventoryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
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
    private val _users = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?> = _users

    init {
        displayItems()
        displayBuyerDetails()
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

    /*    fun displayUsers() {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val usersList = snapshot.toObjects(Users::class.java)
                    _users.value = usersList
                }
            }
        }*/
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
                SingleItemSales = item.salesPrice * buyerDetails.requestedQuantity,
                SingleItemprofit = (item.salesPrice - item.unitPrice) * buyerDetails.requestedQuantity
            )
            buyerDocRef.set(newBuyer)
            _message.value = "Item Sold Successfully"
        }
    }

    fun AdminLogin(uid: String, email: String, role: String) {

        val db = FirebaseFirestore.getInstance()
        val admin = AdLogin(uid = uid, email = email, role = role)
        db.collection("Admin").document(uid).set(admin)
            .addOnSuccessListener { println("Admin Added") }
            .addOnFailureListener { e -> println("Admin Failed,$e") }


    }

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


        /*   fun logic(uid: String, email: String,context: Context) {
           val db = FirebaseFirestore.getInstance()
           val usersCollection = db.collection("Users")
           usersCollection.whereEqualTo("role", "Admin").get()
               .addOnSuccessListener { snapshots ->
                   val role = if (snapshots.isEmpty) {
                       "Admin"
                   } else {
                       "User"
                   }
                   val userMap = mapOf("email" to email, "role" to role)
                   usersCollection.document(uid).set(userMap)
                       .addOnSuccessListener {
                           Toast.makeText(context, "Signup Successful as $role", Toast.LENGTH_SHORT)
                               .show()

                       }.addOnFailureListener {
                           Toast.makeText(context, "Signup Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                       }
               }
       }*/


    }
}