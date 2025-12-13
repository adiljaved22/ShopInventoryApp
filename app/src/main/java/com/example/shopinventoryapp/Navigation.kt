package com.example.shopinventoryapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shopinventoryapp.Admin.AddItem
import com.example.shopinventoryapp.Admin.DashBoard1
import com.example.shopinventoryapp.Admin.Login
import com.example.shopinventoryapp.Admin.UsersList
import com.example.shopinventoryapp.Admin.ViewItems

import com.example.shopinventoryapp.User.DashBoard2
import com.example.shopinventoryapp.User.Payment
import com.example.shopinventoryapp.User.UserLogin
import com.example.shopinventoryapp.User.UserSignUp
import com.example.shopinventoryapp.User.ViewItemsForBuyers

@Composable
fun Navigation(sessionManager: SessionManager) {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "AdminAurUser") {


        composable("AdminAurUser") {
            AdminAurUser(
                NavigateToLogin = {
                    navController.navigate("Login") {
                        popUpTo("AdminAurUser")
                        launchSingleTop = true
                    }
                }, NavigateToDashBoard2 = {
                    navController.navigate("DashBoard2") {
                        popUpTo("AdminAurUser") { inclusive = true }
                        launchSingleTop = true
                    }
                }, navController
            )
        }
        composable("Login") {
            Login(
                navcontroller = navController, viewModel = viewModel(),
               /* NavigateToDashBoard1 = {

*//*
                    sessionManager.saveLogin()*//*

                    navController.navigate("DashBoard1") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }*/)
        }
        composable("UserLogin") {
            UserLogin(
                navcontroller = navController, NavigateToDashBoard2 = {
                    sessionManager.saveLogin()
                    navController.navigate("DashBoard2") {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
        }
        composable("UserSignUp") {
            UserSignUp(
                navController = navController,
                viewModel = viewModel(),
                NavigateToUserLogin = { navController.navigate("UserLogin") })

        }

        composable("DashBoard1") {
            DashBoard1(Logout = {
                sessionManager.logout()

                navController.navigate("AdminAurUser") {
                    popUpTo(0) { inclusive = true }
                }
            }, NavigateToAddItem = {
                navController.navigate("AddItem") {
                    launchSingleTop = true
                }
            }, NavigateToBuyerDetails = {
                navController.navigate("BuyerDetails") {
                    launchSingleTop = true
                }
            }, NavigateToViewItem = {
                navController.navigate("ViewItems") {
                    launchSingleTop = true
                }
            }, NavigateToUsers = {
                navController.navigate("UsersList") {
                    launchSingleTop = true
                }
            })
        }
        composable("UsersList") {
            UsersList(
                viewModel = viewModel(), NavigateToUsers = {
                    navController.navigate("UsersList") {
                        launchSingleTop = true
                    }
                }, onBackClick = { navController.popBackStack() },
                navController
            )
        }

        composable("DashBoard2") {

            DashBoard2(
                Logout2 = {
                    sessionManager.logout()
                    navController.navigate("AdminAurUser") {
                        popUpTo(0) { inclusive = true }

                    }
                }, NavigateToViewItem = {
                    navController.navigate("ViewItemsForBuyers") {
                        launchSingleTop = true
                    }
                }, NavigateToSellItem = {
                    navController.navigate("SellItems") {
                        launchSingleTop = true
                    }

                }, NavigateToPayment = {
                    navController.navigate("Payment") {
                        launchSingleTop = true
                    }
                }, navController = navController
            )
        }

        composable("AddItem") {
            AddItem(
                onBackClick = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                NavigateToAddItem = {
                    navController.navigate("AddItem") {
                        launchSingleTop = true
                    }
                })
        }


        composable("ViewItems") {
            ViewItems(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToViewItem = {
                    navController.navigate("ViewItems") {
                        launchSingleTop = true
                    }
                })
        }
        composable("Payment") {
            Payment(
                uuid = null,
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToPayment = {/*
                    navController.navigate("Payment") {
                        launchSingleTop = true
                    }*/

                }

            )
        }
        composable(
            "Payment/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid")
            Payment(
                uuid = uid,
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(),
                NavigateToPayment = {
                    navController.navigate("Payment") {
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
                })
        }
    }
}