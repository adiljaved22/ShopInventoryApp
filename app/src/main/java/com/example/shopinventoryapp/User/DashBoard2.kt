package com.example.shopinventoryapp.User


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
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.SessionManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoard2(
    Logout2: () -> Unit,

    navController: NavController,
    NavigateToViewItem: () -> Unit,
    NavigateToSellItem: () -> Unit,
    NavigateToPayment: () -> Unit,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dashboard",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                },


                actions = {
                    IconButton(onClick = {
                        Logout2()
                        sessionManager.logout()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Back",
                            tint = Color.Black
                        )

                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Inventory Management",
                fontSize = 28.sp,
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
                            Color.Black,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable { NavigateToViewItem() }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ViewList,
                        contentDescription = "View Items",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "View Items",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }


                Spacer(modifier = Modifier.height(25.dp))



                Column(
                    modifier = Modifier
                        .size(130.dp)
                        .background(
                            Color.Blue,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable { NavigateToSellItem() }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Sell,
                        contentDescription = "Buy Items",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Buy Items",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column(
                modifier = Modifier
                    .size(130.dp)
                    .background(
                        Color(0xFF00796B),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable {navController.navigate("Payment") }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Payments,
                    contentDescription = "Bills",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Payment",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }

        }


    }
}

