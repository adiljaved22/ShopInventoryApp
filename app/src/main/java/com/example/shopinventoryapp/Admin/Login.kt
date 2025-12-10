package com.example.shopinventoryapp.Admin


import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.SessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun Login(navcontroller: NavController, NavigateToDashBoard1: () -> Unit,viewModel: AppViewModel) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    LaunchedEffect(Unit) {
        if (sessionManager.isLoggedIn()) {
            Log.e("loginscreen", "${sessionManager.isLoggedIn()}")
            navcontroller.navigate("DashBoard1") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }

        }
    }


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontWeight = FontWeight.Bold)


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = emailError.ifEmpty { "Email" },
                    color = if (emailError.isNotEmpty()) Red else Unspecified
                )
            },

            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = passwordError.ifEmpty { "Password" },
                    color = if (passwordError.isNotEmpty()) Red else Unspecified
                )


            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "") },
            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None

                } else {
                    PasswordVisualTransformation('*')
                },
            trailingIcon = {
                val visibilityIcon =
                    if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = visibilityIcon, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            emailError = when {
                email.isBlank() -> "Email is required"
                !isValidEmail(email) -> "Invalid Email"
                else -> ""
            }
            passwordError = when {
                password.isBlank() -> "Password is required"
                password.length < 6 -> "Password must be at least 6 characters"
                else -> ""
            }
            if (emailError.isNotEmpty() || passwordError.isNotEmpty()) {
                return@Button
            }
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                            ?: return@addOnCompleteListener

                        viewModel.AdminLogin(uid, email,"Admin")
                        navcontroller.navigate("DashBoard1") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                        sessionManager.saveLogin()
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("LOGIN", "Login Failed: ${task.exception?.message}")
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }) {
            Text("Login")
        }


    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}