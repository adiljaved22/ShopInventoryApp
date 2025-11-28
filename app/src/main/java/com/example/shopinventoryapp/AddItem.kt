package com.example.shopinventoryapp

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem(
    onBack: () -> Unit,
    NavigateToAddItem: () -> Unit,
    viewModel: AppViewModel = viewModel(),
    onBackClick:()->Unit
) {
    var itemName by remember { mutableStateOf("") }
    var purchaseAmount by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var salesPrice by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    LaunchedEffect(purchaseAmount, quantity) {
        val purchaseAmount = purchaseAmount.toDoubleOrNull()
        val qty = quantity.toIntOrNull()

        if (purchaseAmount != null && qty != null && qty > 0) {
            val unit = purchaseAmount / qty
            unitPrice = unit.toString()
        } else {
            unitPrice = ""
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Item") },

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val datePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    date = "$dayOfMonth/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Date") },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Pick Date")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = purchaseAmount,
                onValueChange = { purchaseAmount = it },
                label = { Text("Purchase Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = unitPrice,
                onValueChange = {},
                label = { Text("Unit Price ") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = salesPrice,
                onValueChange = { salesPrice = it },
                label = { Text("Sales Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (itemName.isNotEmpty() && purchaseAmount.isNotEmpty() && quantity.isNotEmpty() && salesPrice.isNotEmpty() && date.isNotEmpty() && unitPrice.isNotEmpty()) {
                        viewModel.addItems(
                            Items(
                                date = date,
                                name = itemName,
                                unitPrice = unitPrice.toDoubleOrNull() ?: 0.0,
                                currentStock = quantity.toInt(),
                                salesPrice = salesPrice.toDoubleOrNull() ?: 0.0

                            )

                        )
                        onBack()
                        Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Save")
            }
        }
    }
}