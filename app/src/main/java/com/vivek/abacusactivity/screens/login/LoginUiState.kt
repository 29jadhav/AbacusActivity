package com.vivek.abacusactivity.screens.login

// Represents the state of the Login UI
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignInSuccessful: Boolean = false
)