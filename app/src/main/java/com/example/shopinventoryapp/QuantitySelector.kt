package com.example.shopinventoryapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuantitySelector(
    initialQuantity: Int,
    onQuantityChanged: (Int) -> Unit,
    minQuantity: Int = 0,
    maxQuantity: Int = Int.MAX_VALUE
) {
    var quantity by remember { mutableStateOf(initialQuantity) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp) // ⬅ spacing kam
    ) {

        IconButton(
            onClick = {
                if (quantity > minQuantity) {
                    quantity--
                    onQuantityChanged(quantity)
                }
            },
            enabled = quantity > minQuantity,
            modifier = Modifier.size(35.dp) // ⬅ chhota button
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrement",
                modifier = Modifier.size(16.dp) // ⬅ chhota icon
            )
        }

        Text(
            text = "$quantity",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp // ⬅ chhota text
        )

        IconButton(
            onClick = {
                if (quantity < maxQuantity) {
                    quantity++
                    onQuantityChanged(quantity)
                }
            },
            enabled = quantity < maxQuantity,
            modifier = Modifier.size(35.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increment",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


