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
import com.example.shopinventoryapp.User.BuyerDetails
import com.example.shopinventoryapp.User.DashBoard2
import com.example.shopinventoryapp.User.UserLogin
import com.example.shopinventoryapp.User.ViewItemsForBuyers

@Composable
fun Navigation(sessionManager: SessionManager) {

    val navController = rememberNavController()
    /*   val startDestination = if (sessionManager.isLoggedIn()) {
           "DashBoard1"
       } else {
           "AdminAurUser"
       }*/

    NavHost(navController = navController, startDestination = "AdminAurUser") {


        composable("AdminAurUser") {
            AdminAurUser(
                NavigateToLogin = {
                    navController.navigate("Login") {
                        popUpTo("AdminAurUser")
                        launchSingleTop = true
                    }
                },
                NavigateToDashBoard2 = {
                    navController.navigate("DashBoard2") {
                        popUpTo("AdminAurUser") { inclusive = true }
                        launchSingleTop = true
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
        composable("UserLogin") {
            UserLogin(
                navcontroller = navController,
                NavigateToDashBoard2 = {
                    navController.navigate("DashBoard2") {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop =true
                    }
                })
        }
      /*  composable("UserSignUp"){
            UserSignUp(navController = navController, NavigateToLogin = {
                navController.navigate("UserLogin"){
                    popUpTo(0)
                    launchSingleTop = true
                }
            })
        }*/

        composable("DashBoard1") {
            DashBoard1(
                Logout = {
                    sessionManager.logout()

                    navController.navigate("AdminAurUser") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                NavigateToAddItem = {
                    navController.navigate("AddItem") {
                        launchSingleTop = true
                    }
                },
                NavigateToBuyerDetails = {
                    navController.navigate("BuyerDetails") {
                        launchSingleTop = true
                    }
                },
                NavigateToViewItem = {
                    navController.navigate("ViewItems") {
                        launchSingleTop = true
                    }
                }
            )
        }


        composable("DashBoard2") {
            DashBoard2(
                NavigateToViewItem = {
                    navController.navigate("ViewItemsForBuyers") {
                        launchSingleTop = true
                    }
                },
                NavigateToSellItem = {
                    navController.navigate("SellItems") {
                        launchSingleTop = true
                    }
                },
                navController = navController
            )
        }

        composable("AddItem") {
            AddItem(
                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToAddItem = { navController.navigate("AddItem") { launchSingleTop = true } }
            )
        }


        composable("ViewItems") {
            ViewItems(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToViewItem = {
                    navController.navigate("ViewItems") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("ViewItemsForBuyers") {
            ViewItemsForBuyers(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToBuyerViewItem = {
                    navController.navigate("ViewItemsForBuyers") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("SellItems") {
            BuyItems(
                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToSellItem = {
                    navController.navigate("SellItems") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("BuyerDetails") {
            BuyerDetails(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToBuyerDetails = {
                    navController.navigate("BuyerDetails") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}