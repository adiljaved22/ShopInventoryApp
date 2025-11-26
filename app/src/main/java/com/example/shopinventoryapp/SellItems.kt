package com.example.shopinventoryapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellItems(NavigateToSellItem: () -> Unit,viewModel: AppViewModel = viewModel(),onBack: () -> Unit) {
    var BuyerName by remember { mutableStateOf("") }
    var ItemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Sell Items",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = BuyerName,
                onValueChange = { BuyerName = it },
                label = { Text("Buyer Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ItemName,
                onValueChange = { ItemName = it },
                label = { Text("Item Name") },

                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
               viewModel.addBuyerDetails(
                    BuyerDetails(
                        buyerName = BuyerName,
                        itemName = ItemName,
                        quantity = quantity.toInt()
                    ))
                onBack()
            }) {
                Text("Sell")
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text("Thanks For Shopping")
            }
        }

    }

}
