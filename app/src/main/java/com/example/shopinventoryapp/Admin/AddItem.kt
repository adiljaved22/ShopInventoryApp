package com.example.shopinventoryapp.Admin

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.Items
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem(
    onBack: () -> Unit,
    NavigateToAddItem: () -> Unit,
    viewModel: AppViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var itemName by remember { mutableStateOf("") }
    var purchaseAmount by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var salesPrice by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    val todayDate = remember {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        "$day/$month/$year"
    }


    LaunchedEffect(Unit) {
        date = todayDate
    }


    LaunchedEffect(purchaseAmount, quantity) {
        val purchase = purchaseAmount.toDoubleOrNull()
        val qty = quantity.toIntOrNull()

        unitPrice = if (purchase != null && qty != null && qty > 0) {
            (purchase / qty).toString()
        } else ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Item") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Pick Date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
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
                label = { Text("Unit Price") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = salesPrice,
                onValueChange = { salesPrice = it },
                label = { Text("Sales Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {

                    if (
                        itemName.isNotEmpty() &&
                        purchaseAmount.isNotEmpty() &&
                        quantity.isNotEmpty() &&
                        salesPrice.isNotEmpty()
                    ) {
                        val sales = salesPrice.toDoubleOrNull()
                        val unit = unitPrice.toDoubleOrNull()

                        if (sales == null || unit == null || sales <= unit) {
                            Toast.makeText(
                                context,
                                "Sales price must be greater than unit price",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        viewModel.addItems(
                            Items(
                                date = date,
                                name = itemName,
                                unitPrice = unit,
                                currentStock = quantity.toInt(),
                                salesPrice = sales,
                                purchasePrice = purchaseAmount.toDouble()
                            )
                        )
                        itemName = ""
                        purchaseAmount = ""
                        quantity = ""
                        unitPrice = ""
                        salesPrice = ""
                        focusManager.clearFocus()


                        Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.surface
                ),
            ) {
                Text("Save")
            }
        }
    }
}
