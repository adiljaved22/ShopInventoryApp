package com.example.shopinventoryapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AdminAurUser(NavigateToLogin: () -> Unit,NavigateToDashBoard2:()->Unit,navcontroller: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Admin or User", fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(35.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Column(
                modifier = Modifier
                    .size(130.dp)
                    .background(
                        Color(0xFFFF6347 ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable { navcontroller.navigate("Login") }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Add Items",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Admin",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }


            Column(
                modifier = Modifier
                    .size(130.dp)
                    .background(
                        Color(0xFF4682B4),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable { navcontroller.navigate("UserLogin") }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.VerifiedUser,
                    contentDescription = "View Items",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "User Panel",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}