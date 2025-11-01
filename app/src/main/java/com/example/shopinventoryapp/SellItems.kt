package com.example.shopinventoryapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SellItems(NavigateToSellItem: () -> Unit) {
    var BuyerName by remember { mutableStateOf("") }
    var ItemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = BuyerName,
            onValueChange = { BuyerName = it },
            label = { Text("Buyer Name") })

        OutlinedTextField(
            value = ItemName,
            onValueChange = { ItemName = it },
            label = { Text("Item Name") })
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") }
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") }
        )
    }
}