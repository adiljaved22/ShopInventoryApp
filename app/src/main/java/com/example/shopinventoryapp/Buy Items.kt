package com.example.shopinventoryapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyItems(
    NavigateToSellItem: () -> Unit,
    viewModel: AppViewModel = viewModel(),
    onBack: () -> Unit,
    onBackClick:()->Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.displayItems()
    }

    var BuyerName by remember { mutableStateOf("") }
    var ItemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var isExpended by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    val list by viewModel.items.collectAsState(initial = emptyList())
    var selectedItem by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Buy Items",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            },
                actions = {
                   IconButton(onClick = {onBackClick()}) {
                       Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)

                    }
                }
                )
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
                val qty = quantity.toIntOrNull()


                val salesprice = itemToSell?.salesPrice ?: 0.0
                val total = salesprice * (qty ?: 0)
                println("Total price of item: $total")

                when {
                    itemToSell == null -> errorMsg = "Please select an item"
                    qty == null || qty <= 0 -> errorMsg = "Enter a valid quantity"
                    qty > itemToSell.currentStock -> errorMsg = "Out of Stock"
                    else -> {

                        viewModel.sellItem(
                            buyerDetails = BuyerDetails(
                                buyerName = BuyerName,
                                itemName = ItemName,
                                requestedQuantity = qty,
                                total = total
                            ),
                            item = itemToSell,
                        )
                        onBack()
                    }
                }
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




