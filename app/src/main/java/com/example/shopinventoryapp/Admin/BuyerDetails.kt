/*
package com.example.shopinventoryapp.Admin


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.webauthn.Cbor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.BuyerDetails
import com.example.shopinventoryapp.R
import com.example.shopinventoryapp.Users
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerDetails(
    NavigateToBuyerDetails: () -> Unit,
    viewModel: AppViewModel = viewModel(),
    onBackClick: () -> Unit
) {

    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uid) {
        uid?.let { viewModel.displayBuyerDetails(uid) }
        uid?.let { viewModel.getUsers(it) }

    }
    val details by viewModel.buyerDetails.collectAsState(initial = emptyList())
    val users by viewModel.user.collectAsState(initial = Users())
    Text("Buyer Details")
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = colorResource(id = R.color.teal_700),
                ),
                title = { Text("Buyer Details") },

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
    ) { padding ->
        if (details.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No purchases found ", fontSize = 18.sp)
            }
        } else {
            Text("Welcome $users")
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(details) { details ->
                    ItemCard(details, viewModel)
                }
            }
        }
    }
}

@Composable
fun ItemCard(details: BuyerDetails, viewModel: AppViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(details.itemName, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Quantity: ${details.requestedQuantity}",

                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    "Rs:${details.totalprice.toString()}",

                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }


        }
    }
}*/
