/*
package com.example.shopinventoryapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "AdminAurUser") {
        */
/* composable("Signup") {
             SignUp(
                 navController = navController,
                 NavigateToLogin = {
                     navController.navigate("Login")
                 }
             )
         }*//*

        composable("AdminAurUser")
        {
            AdminAurUser(
                NavigateToLogin = { navController.navigate("Login") },
                NavigateToDashBoard2 = { navController.navigate("DashBoard2") }, navController
            )
        }
        composable("DashBoard2") {
            DashBoard2(
                NavigateToViewItem = { navController.navigate("ViewItems") },
                NavigateToSellItem = { navController.navigate("SellItems") },
                navController = navController
            )
        }
        composable("Login") {
            Login(
                navcontroller = navController,
                NavigateToDashBoard1 = {
                    navController.navigate("DashBoard1") {
                        popUpTo("Login") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("DashBoard1") {
            DashBoard1(
                Logout = {
                    navController.navigate("AdminAurUser") {
                        popUpTo("DashBoard1") { inclusive = true }
                        launchSingleTop = true

                    }
                },
                NavigateToAddItem = { navController.navigate("AddItem") },
                NavigateToBuyerDetails = { navController.navigate("BuyerDetails") }
            )
        }
        composable("AddItem") {
            AddItem(
                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToAddItem = { navController.navigate("AddItem") })
        }
        composable("ViewItems")
        {
            ViewItems(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToViewItem = { navController.navigate("ViewItems") }
            )
        }
        composable("SellItems")
        {
            BuyItems(


                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToSellItem = { navController.navigate("SellItems") }
            )
        }
        composable("BuyerDetails")
        {
            BuyerDetails(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToBuyerDetails = { navController.navigate("BuyerDetails") }
            )
        }
    }


}*/
package com.example.shopinventoryapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(sessionManager: SessionManager) {

    val navController = rememberNavController()
    val startDestination = if (sessionManager.isLoggedIn()) {
        "DashBoard1"
    } else {
        "AdminAurUser"
    }

    NavHost(navController = navController, startDestination = startDestination) {


        composable("AdminAurUser") {
            AdminAurUser(
                NavigateToLogin = {
                    navController.navigate("Login") {
                        popUpTo("AdminAurUser")
                    }
                },
                NavigateToDashBoard2 = {
                    navController.navigate("DashBoard2") {
                        popUpTo("AdminAurUser") { inclusive = true }
                    }
                },
                navController
            )
        }
        composable("Login") {
            Login(
                navcontroller = navController,
                NavigateToDashBoard1 = {

                    sessionManager.saveLogin()

                    navController.navigate("DashBoard1") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("DashBoard1") {
            DashBoard1(
                Logout = {
                    sessionManager.logout()

                    navController.navigate("AdminAurUser") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                NavigateToAddItem = { navController.navigate("AddItem") },
                NavigateToBuyerDetails = { navController.navigate("BuyerDetails") },
                NavigateToViewItem = {navController.navigate("ViewItems")}
            )
        }


        composable("DashBoard2") {
            DashBoard2(
                NavigateToViewItem = { navController.navigate("ViewItems") },
                NavigateToSellItem = { navController.navigate("SellItems") },
                navController = navController
            )
        }

        composable("AddItem") {
            AddItem(
                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToAddItem = { navController.navigate("AddItem") }
            )
        }


        composable("ViewItems") {
            ViewItems(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToViewItem = { navController.navigate("ViewItems") }
            )
        }

        composable("SellItems") {
            BuyItems(
                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToSellItem = { navController.navigate("SellItems") }
            )
        }

        composable("BuyerDetails") {
            BuyerDetails(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToBuyerDetails = { navController.navigate("BuyerDetails") }
            )
        }
    }
}