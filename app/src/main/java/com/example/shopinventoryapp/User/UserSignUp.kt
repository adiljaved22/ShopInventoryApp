package com.example.shopinventoryapp.User

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shopinventoryapp.AppViewModel
import com.example.shopinventoryapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.text.ifEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignUp(
    viewModel: AppViewModel,
    navController: NavController,
    NavigateToUserLogin: () -> Unit
) {
    val context = LocalContext.current
    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var displayNameError by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { Text("Shop Inventory", fontWeight = FontWeight.Medium) },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )

                    }
                }
            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Create account",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign up to get started",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = displayName,
                onValueChange = { displayName = it },
                label = {
                    Text(
                        text = displayNameError.ifEmpty { "Username" },
                        color = if (displayNameError.isNotEmpty()) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
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
                        color = if (passwordError.isNotEmpty()) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
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
            Button(
                onClick = {
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
                    displayNameError = when {
                        displayName.isBlank() -> "Username is required"
                        else -> ""
                    }


                    if (emailError.isNotEmpty() || passwordError.isNotEmpty() || displayNameError.isNotEmpty()) {
                        return@Button
                    }
                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val currentUser = FirebaseAuth.getInstance().currentUser
                                val uid = currentUser?.uid


                                currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener {


                                        viewModel.UserSignUp(displayName, email, "user", uid)

                                        Toast.makeText(
                                            context,
                                            "Verification email sent. Please verify your email.",
                                            Toast.LENGTH_LONG
                                        ).show()


                                        FirebaseAuth.getInstance().signOut()

                                        NavigateToUserLogin()
                                    }
                                    ?.addOnFailureListener { e ->
                                        Toast.makeText(
                                            context,
                                            e.localizedMessage ?: "Verification email failed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                            } else {

                                Toast.makeText(
                                    context,
                                    task.exception?.localizedMessage ?: "Sign Up Failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }


                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)


            )

            {

                Text("Sign up")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row {
                    Text("Already have an account? ")
                    Text(
                        "Login",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { navController.navigate("UserLogin") })
                }
            }
        }


    }
}
