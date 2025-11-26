package com.example.shopinventoryapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "DashBoard") {
        composable("DashBoard") {
            DashBoard(
                NavigateToAddItem = { navController.navigate("AddItem") },
                NavigateToViewItem = { navController.navigate("ViewItems") },
                NavigateToSellItem = { navController.navigate("SellItems") },
                NavigateToBuyerDetails = { navController.navigate("BuyerDetails") }
            )
        }
        composable("AddItem") {
            AddItem(onBack = { navController.popBackStack() } ,NavigateToAddItem = { navController.navigate("AddItem") })
        }
        composable("ViewItems")
        {
            ViewItems(
               viewModel = viewModel(), NavigateToViewItem = { navController.navigate("ViewItems") }
            )
        }
        composable("SellItems")
        {
            SellItems(onBack = { navController.popBackStack() },
                NavigateToSellItem = { navController.navigate("SellItems") }
            )
        }
        composable("BuyerDetails")
        {
            BuyerDetails(
                NavigateToBuyerDetails = { navController.navigate("BuyerDetails") }
            )
        }
    }



    }

