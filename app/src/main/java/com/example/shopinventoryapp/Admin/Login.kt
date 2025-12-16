package com.example.shopinventoryapp.Admin

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.R
import com.example.shopinventoryapp.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navcontroller: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    var isLoading by remember { mutableStateOf(false) }


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = colorResource(id = R.color.teal_700),
                ),
                title = { Text("Shop Inventory", fontWeight = FontWeight.Medium) },

                actions = {
                    IconButton(onClick = { navcontroller.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )

                    }
                }
            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign in to continue",
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

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
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = ""
                    )
                }
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
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = ""
                    )
                },
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(
                        '*'
                    ),
                trailingIcon = {
                    val visibilityIcon =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = visibilityIcon, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    emailError = when {
                        email.isBlank() -> "Email is required"
                        !isValidEmail(email) -> "Invalid Email"
                        else -> ""
                    }
                    passwordError = when {
                        password.isBlank() -> "Password is required"
                        password.length < 6 -> "Password must be at least 8 characters"
                        else -> ""
                    }

                    if (emailError.isNotEmpty() || passwordError.isNotEmpty()) return@Button

                    isLoading = true

                    Firebase.auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                    ?: return@addOnCompleteListener
                                val db = FirebaseFirestore.getInstance()

                                db.collection("users").document(uid).get()
                                    .addOnSuccessListener { userDoc ->
                                        if (userDoc.exists()) {
                                            val role = userDoc.getString("role") ?: "user"
                                            if (role == "Admin") {
                                                sessionManager.saveAdminLogin()
                                                navcontroller.navigate("DashBoard1") {
                                                    popUpTo(0)
                                                    launchSingleTop = true
                                                }
                                                Toast.makeText(
                                                    context,
                                                    "Admin Login Successful",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                FirebaseAuth.getInstance().signOut()
                                                Toast.makeText(
                                                    context,
                                                    "Not authorized as admin",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        } else {
                                            db.collection("Admin").document(uid).get()
                                                .addOnSuccessListener { adminDoc ->
                                                    if (adminDoc.exists()) {
                                                        sessionManager.saveAdminLogin()
                                                        navcontroller.navigate("DashBoard1") {
                                                            popUpTo(0)
                                                            launchSingleTop = true
                                                        }
                                                        Toast.makeText(
                                                            context,
                                                            "Admin Login Successful",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        FirebaseAuth.getInstance().signOut()
                                                        Toast.makeText(
                                                            context,
                                                            "Account not registered. Contact support.",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    FirebaseAuth.getInstance().signOut()
                                                    Toast.makeText(
                                                        context,
                                                        "Role check failed: ${e.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        FirebaseAuth.getInstance().signOut()
                                        Toast.makeText(
                                            context,
                                            "Role check failed: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                            } else {
                                Log.e("LOGIN", "Login Failed: ${task.exception?.message}")
                                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Login")
            }
        }


        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = Color.White,
                        strokeWidth = 5.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Please wait...", color = Color.White)
                }
            }
        }

    }

}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}