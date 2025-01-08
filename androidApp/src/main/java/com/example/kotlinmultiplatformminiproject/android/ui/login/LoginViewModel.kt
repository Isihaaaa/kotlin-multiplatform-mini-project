package com.example.kotlinmultiplatformminiproject.android.ui.login

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.kotlinmultiplatformminiproject.android.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(), DefaultLifecycleObserver {
    val uiState: StateFlow<UIState?>
    private val businessData: MutableStateFlow<BusinessData?> = MutableStateFlow(null)

    protected val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    //// STATE HANDLING

    init {
        uiState = businessData.map {
            try {
                it?.toUIState()
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error: ${e.message}")

                null // do not update the UI state
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

        viewModelScope.launch {
            initBusinessData()
        }
    }

    private fun initBusinessData() {
        try {
            businessData.value = createBusinessData()
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error: ${e.message}")
        }
    }

    private fun createBusinessData(): BusinessData {

        return BusinessData(
            email = "",
            isEmailError = false,
            password = "",
            isPasswordError = false
        )
    }

    //// EVENT HANDLING

    fun onEmailChanged(email: String) {
        val emailRegex =
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        val isEmailError = !email.matches(emailRegex.toRegex())

        businessData.update {
            it!!.copy(
                email = email,
                isEmailError = isEmailError
            )
        }
    }

    fun onPasswordChanged(password: String) {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}"
        val isPasswordError = !passwordRegex.toRegex().matches(password)

        businessData.update {
            it!!.copy(
                password = password,
                isPasswordError = isPasswordError
            )
        }
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            try {
                _showLoading.value = true

                delay(1500)

                navController.navigate(Route.EVENT_LIST)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error: ${e.message}")
            } finally {
                _showLoading.value = false
            }
        }
    }

    //// OBJECT HOLDERS

    data class BusinessData(
        val email: String,
        val isEmailError: Boolean,
        val password: String,
        val isPasswordError: Boolean
    ) {
        fun toUIState() = UIState(
            email = email,
            isEmailError = isEmailError,
            password = password,
            isPasswordError = isPasswordError
        )
    }

    data class UIState(
        val email: String,
        val isEmailError: Boolean,
        val password: String,
        val isPasswordError: Boolean
    )
}