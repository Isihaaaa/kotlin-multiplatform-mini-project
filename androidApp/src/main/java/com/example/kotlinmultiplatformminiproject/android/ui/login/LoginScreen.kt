package com.example.kotlinmultiplatformminiproject.android.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinmultiplatformminiproject.Greeting
import com.example.kotlinmultiplatformminiproject.android.GreetingView
import com.example.kotlinmultiplatformminiproject.android.MyApplicationTheme
import com.example.kotlinmultiplatformminiproject.android.ui.components.AppLoading

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (viewModel.showLoading.collectAsStateWithLifecycle().value) {
        AppLoading()
    }

    LoginScreenContent(
        navigateToEventList = { viewModel.login(navController) },
        email = uiState!!.email,
        password = uiState.password,
        onEmailChanged = { viewModel.onEmailChanged(it) },
        isEmailError = uiState.isEmailError,
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        isPasswordError = uiState.isPasswordError
    )

}

@Composable
fun LoginScreenContent(
    navigateToEventList: () -> Unit,
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    isEmailError: Boolean,
    onPasswordChanged: (String) -> Unit,
    isPasswordError: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                ),
                value = email,
                onValueChange = { onEmailChanged(it) },
                label = { Text("Email") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                isError = isEmailError

            )

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                ),
                value = password,
                onValueChange = { onPasswordChanged(it) },
                label = { Text("Password") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null
                    )
                },
                isError = isPasswordError
            )

            Button(
                onClick = { navigateToEventList() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Login")
            }
        }

        GreetingView(text = Greeting().greet(), modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreenContent(
            navigateToEventList = { },
            email = "",
            password = "",
            onEmailChanged = { },
            isEmailError = false,
            onPasswordChanged = { },
            isPasswordError = false
        )
    }
}