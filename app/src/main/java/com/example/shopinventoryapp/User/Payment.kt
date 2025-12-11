package com.example.shopinventoryapp.User

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.BuyerDetails
import com.example.shopinventoryapp.Users
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Payment(
    uid: String?,
    viewModel: AppViewModel,
    NavigateToPayment: () -> Unit,
    onBackClick: () -> Unit
) {


    val uid =uid?: FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(uid) {
        uid?.let { viewModel.displayBuyerDetails(it) }
        uid?.let { viewModel.getUsers(it) }
    }

    val details by viewModel.buyerDetails.collectAsState(initial = emptyList())
    val users by viewModel.user.collectAsState(initial = Users())

    // totals
    val grandTotal = details.sumOf { it.totalprice }
    /* val pendingTotal = details.filter { it.paid.not() }.sumOf { it.totalprice }
     val paidTotal = details.filter { it.paid }.sumOf { it.totalprice }*/

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = colorResource(id = com.example.shopinventoryapp.R.color.teal_700),
                ),
                title = {
                    Column {
                        Text("Buyer Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            text = "Welcome, ${users?.displayName?.ifBlank { "User" }}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        // Outer column respects scaffold padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // --- SUMMARY BAR (always visible) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Purchased", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("${details.size} item(s)", fontSize = 12.sp, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "Grand Total: Rs ${grandTotal}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text("Pending: ", fontSize = 12.sp, color = Color.Gray)
                            /* Text("Rs ${"%.2f".format(pendingTotal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                             Spacer(modifier = Modifier.width(8.dp))
                             Text("Paid: ", fontSize = 12.sp, color = Color.Gray)
                             Text("Rs ${"%.2f".format(paidTotal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))*/
                        }
                    }
                }
            }

            // --- Empty state or list ---
            if (details.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No purchases found", fontSize = 18.sp)
                }
                return@Column
            }

            // --- List of items (scrollable) ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(details) { item ->
                    ItemCard3(item) // uses the ItemCard3 below
                }
            }
        }
    }
}

@Composable
fun ItemCard3(details: BuyerDetails) {

    val statusIsPaid = details.status == true
    val statusText = if (statusIsPaid) "PAID" else "PENDING"
    val statusColor = if (statusIsPaid) Color(0xFF4CAF50) else Color(0xFFFFA726)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp, max = 150.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            // status badge top-right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        statusColor.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = details.itemName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Qty: ${details.requestedQuantity}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    /*     Text(
                             text = details.date ?: "",
                             fontSize = 12.sp,
                             color = Color.Gray
                         )*/
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Rs:${details.totalprice}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}