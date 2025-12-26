package com.example.shopinventoryapp.User


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.BuyerDetails
import com.example.shopinventoryapp.Items
import com.example.shopinventoryapp.QuantitySelector
import com.example.shopinventoryapp.R
import com.example.shopinventoryapp.SessionManager
import com.example.shopinventoryapp.Users
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.compose


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoard2(
    Logout2: () -> Unit,
    navController: NavController,
    NavigateToPayment: () -> Unit,
    NavigateToViewItem: () -> Unit,
    NavigateToSellItem: () -> Unit,
    viewModel: AppViewModel = viewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    val users by viewModel.user.collectAsState(initial = Users())
    val items by viewModel.items.collectAsState(initial = emptyList())
    val context = LocalContext.current
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val sessionManager = SessionManager(context)
    LaunchedEffect(currentUser) {
        currentUser?.let { viewModel.getUsers(it) }
        viewModel.displayItems()
    }
    val cart = remember {
        mutableStateMapOf<String, Int>()
    }


    val grandTotal = items.sumOf { item ->
        val qty = cart[item.firestoreId] ?: 0
        item.salesPrice * qty
    }
    val filtered = if (searchQuery.isBlank()) {
        items
    } else {
        items.filter { it.name.contains(searchQuery,ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search...") },
                            singleLine = true
                        )
                    } else {
                        Text("DashBoard", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSearching = !isSearching
                        if (!isSearching) {
                            searchQuery = ""
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(onClick = {
                        Logout2()
                        sessionManager.logout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Welcome, ${users?.displayName}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Card(
                    modifier = Modifier
                        .height(70.dp)
                        .width(120.dp)
                        .clickable { NavigateToPayment() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Rounded.Payments,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            "Payment",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = "Available Items",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))
            if (filtered.isEmpty()) {
                Text(
                    text = "No users found",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { item ->
                        ItemCard1(
                            item = item,
                            quantity = cart[item.firestoreId] ?: 0,
                            onQuantityChange = { newQty ->
                                if (newQty == 0) {
                                    cart.remove(item.firestoreId)
                                } else {
                                    cart[item.firestoreId] = newQty
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
            ) {
                Box(
                    modifier = Modifier.padding(15.dp),
                    contentAlignment = Alignment.CenterStart

                ) {
                    Text(
                        text = "Grand Total: Rs $grandTotal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(
                            onClick = {
                                if (cart.isEmpty()) {
                                    Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT)
                                        .show()
                                } else {


                                    cart.forEach { (itemId, qty) ->
                                        val item = items.firstOrNull { it.firestoreId == itemId }
                                        if (item != null && qty > 0) {
                                            viewModel.sellItem(
                                                buyerDetails = BuyerDetails(
                                                    requestedQuantity = qty,
                                                    totalprice = item.salesPrice * qty
                                                ),
                                                item = item
                                            )
                                        }
                                    }

                                    Toast.makeText(context, "Order Placed", Toast.LENGTH_LONG)
                                        .show()
                                    cart.clear()
                                }
                            },
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                        )

                        {
                            Text("Buy")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard1(
    item: Items,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    val itemTotal = item.salesPrice * quantity

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rs ${item.salesPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Stock: ${item.currentStock}",
                    fontSize = 14.sp
                )
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

                QuantitySelector(
                    initialQuantity = quantity,
                    onQuantityChanged = { onQuantityChange(it) },
                    minQuantity = 0,
                    maxQuantity = item.currentStock
                )
            }

            Divider()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Total: Rs $itemTotal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

