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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.BuyerDetails
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
    viewModel: AppViewModel = viewModel(),
) {
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid


    LaunchedEffect(currentUser) {
        currentUser?.let { viewModel.getUsers(it) }
    }

    val users by viewModel.user.collectAsState(initial = Users())

    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = colorResource(id = R.color.teal_700),
                ),
                title = {
                    Text(
                        "Dashboard",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Logout2()
                        sessionManager.logout()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val displayName = users?.displayName

            Text(
                text = "Welcome: $displayName", style = TextStyle(
                    fontSize = 24.sp,

                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f, 2f),
                        blurRadius = 2f
                    )
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Inventory Management",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(35.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BuyItems()
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .size(130.dp)
                        .background(Color.Black, shape = RoundedCornerShape(25.dp))
                        .clickable { NavigateToViewItem() }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ViewList,
                        contentDescription = "View Items",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "View Items",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .size(130.dp)
                        .background(Color(0xFF00796B), shape = RoundedCornerShape(25.dp))
                        .clickable { NavigateToPayment() }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Payments,
                        contentDescription = "Bills",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Payment",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
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
                list.forEach { item ->
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
                qty <= itemToSell.currentStock -> Toast.makeText(
                    context,
                    "Thanks for buying",
                    Toast.LENGTH_SHORT
                ).show()


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