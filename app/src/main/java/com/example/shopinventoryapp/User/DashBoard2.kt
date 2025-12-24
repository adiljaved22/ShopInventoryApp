package com.example.shopinventoryapp.User


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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    NavigateToViewItem: () -> Unit,
    NavigateToSellItem: () -> Unit,
    NavigateToPayment: () -> Unit,
    viewModel: AppViewModel = viewModel()
) {

    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    val users by viewModel.user.collectAsState(initial = Users())
    val items by viewModel.items.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    LaunchedEffect(currentUser) {
        currentUser?.let { viewModel.getUsers(it) }
        viewModel.displayItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
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


            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No items available", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items) { item ->
                        ItemCard1(item, viewModel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            // BuyItems()
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyItems(
    viewModel: AppViewModel = viewModel(),

    ) {
    LaunchedEffect(Unit) {
        viewModel.displayItems()

    }


    var ItemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var isExpended by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    val list by viewModel.items.collectAsState(initial = emptyList())
    var selectedItem by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier

            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpended,
            onExpandedChange = { isExpended = !isExpended }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedItem,
                onValueChange = {},
                label = { Text("Select Item") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended) }

            )

            ExposedDropdownMenu(
                expanded = isExpended,
                onDismissRequest = { isExpended = false }
            ) {
                list.filter { it.currentStock > 0 }.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            selectedItem = item.name
                            ItemName = item.name
                            isExpended = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()

        )

        if (errorMsg.isNotEmpty()) {
            Text(text = errorMsg, color = Color.Red)
        }

        Button(onClick = {
            val itemToSell = list.find { it.name == selectedItem }
            val qty = quantity.toIntOrNull() ?: 0
            val sale = itemToSell?.salesPrice ?: 0.0
            val totalprice = (sale * qty)
            quantity = ""
            selectedItem = ""



            when {
                itemToSell == null -> errorMsg = "Please select an item"
                qty <= 0 -> errorMsg = "Enter a valid quantity"
                qty > itemToSell.currentStock -> errorMsg = "Out of Stock"


                else -> {

                    viewModel.sellItem(
                        buyerDetails = BuyerDetails(

                            itemName = ItemName,
                            requestedQuantity = qty,
                            totalprice = totalprice
                        ),
                        item = itemToSell,
                    )

                }
            }
        }) {

            Text("Buy")
        }


    }
}

@Composable
fun ItemCard1(item: Items, viewModel: AppViewModel) {
    var currentItemQuantity by remember { mutableStateOf(0) }

    val totalPrice = item.salesPrice * currentItemQuantity





    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(8.dp),

        ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 20.sp,
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
                    fontSize = 14.sp,
                    /*color = Color.Gray*/
                )
            }

            Divider()


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                QuantitySelector(
                    initialQuantity = currentItemQuantity,
                    onQuantityChanged = { currentItemQuantity = it },
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
                    text = "Total: Rs $totalPrice",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
