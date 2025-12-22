package com.example.shopinventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shopinventoryapp.Internetobserve.InfoDialog
import com.example.shopinventoryapp.Internetobserve.observeInternet
import com.example.shopinventoryapp.ui.theme.ShopInventoryAppTheme
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)
        enableEdgeToEdge()

        setContent {
            MyAppTheme() {

                    val context = LocalContext.current

                    var isConnected by remember { mutableStateOf(true) }
                    var isAppReady by remember { mutableStateOf(false) }


                    splashScreen.setKeepOnScreenCondition {
                           !isAppReady
                    }


                    LaunchedEffect(Unit) {
                        observeInternet(context).collect { connected ->
                            isConnected = connected

                            if (connected) {
                                isAppReady = true
                            } else {
                                isAppReady = false
                            }
                        }
                    }


                    if (!isConnected) {
                        InfoDialog(
                            title = "Ah!!!\nNo Internet",
                            desc = "Please check your internet connection",
                            onDismiss = {}
                        )
                    }


                    if (isAppReady) {
                        val sessionManager = remember { SessionManager(context) }

                        val startDestination = when {
                            sessionManager.isLoggedIn() && sessionManager.getUserRole() == UserRole.ADMIN ->
                                "DashBoard1"

                            sessionManager.isLoggedIn() && sessionManager.getUserRole() == UserRole.USER ->
                                "DashBoard2"

                            else ->
                                "AdminAurUser"
                        }

                        Surface(color = MaterialTheme.colorScheme.background) {
                            Navigation(startDestination)
                        }
                    }
            }
        }
    }
}