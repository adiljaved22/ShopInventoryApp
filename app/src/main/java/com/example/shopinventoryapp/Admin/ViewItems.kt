package com.example.shopinventoryapp.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.Items
import com.example.shopinventoryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewItems(
    NavigateToViewItem: () -> Unit,
    viewModel: AppViewModel = viewModel(),
    onBackClick: () -> Unit,
) {
    val list by viewModel.items.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.displayItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = colorResource(id = R.color.teal_700),
                ),
                title = { Text("Inventory Items") },

                actions = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )

                    }
                })
        }
    ) { padding ->
        if (list.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No items found", fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(list) { item ->
                    ItemCard(   item, viewModel)
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: Items, viewModel: AppViewModel) {
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Price: Rs ${item.salesPrice}")
                Text("Qty: ${item.currentStock}")
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Date: ${item.date}", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { showEditDialog = true }) {
                    Text("Edit")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                    onClick = {
                        viewModel.deleteItem(item.firestoreId)
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }

    if (showEditDialog) {
        EditItemDialog(item, onDismiss = { showEditDialog = false }) { updatedItem ->
            viewModel.updateItem(updatedItem)
            showEditDialog = false
        }
    }
}

@Composable
fun EditItemDialog(
    item: Items,
    onDismiss: () -> Unit,
    onSave: (Items) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var price by remember { mutableStateOf(item.unitPrice.toString()) }
    var quantity by remember { mutableStateOf(item.currentStock.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Edit Item", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") })
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Unit Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = {
                        val updatedItem = item.copy(
                            name = name,
                            unitPrice = price.toDoubleOrNull() ?: 0.0,
                            currentStock = quantity.toIntOrNull() ?: 0
                        )
                        onSave(updatedItem)
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}