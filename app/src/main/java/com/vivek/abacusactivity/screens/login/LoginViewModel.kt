package com.vivek.abacusactivity.screens.login

import android.app.Application
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.domain.repository.AuthRepository
import com.vivek.abacusactivity.domain.repository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val credentialManager = CredentialManager.create(application)

    fun beginSignIn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(application.getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = application,
                )
                handleSignInResult(result.credential)
            } catch (e: GetCredentialException) {
                _uiState.update { it.copy(isLoading = false) }

                if (e is GetCredentialCancellationException || e is GetCredentialInterruptedException) {
                    Log.i("LoginViewModel", "Sign-in flow was cancelled by the user.", e)
                } else {
                    Log.e("LoginViewModel", "GetCredentialException", e)
                    // USE STRING RESOURCE
                    _uiState.update { it.copy(error = application.getString(R.string.error_sign_in_failed)) }
                }
            }
        }
    }

    private suspend fun handleSignInResult(credential: androidx.credentials.Credential) {
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                signInWithGoogle(googleIdToken)
            } catch (e: GoogleIdTokenParsingException) {
                // USE STRING RESOURCE
                _uiState.update { it.copy(isLoading = false, error = application.getString(R.string.error_token_parse_failed)) }
                Log.e("LoginViewModel", "Google ID Token parsing failed", e)
            }
        } else {
            // USE STRING RESOURCE
            _uiState.update { it.copy(isLoading = false, error = application.getString(R.string.error_unexpected_credential)) }
            Log.e("LoginViewModel", "Unexpected credential type: ${credential::class.java.name}")
        }
    }

    private suspend fun signInWithGoogle(idToken: String) {
        when (val result = authRepository.signInWithGoogle(idToken)) {
            is AuthResult.Success -> {
                _uiState.update { it.copy(isLoading = false, isSignInSuccessful = true) }
            }
            is AuthResult.Error -> {
                // Here, the error message comes from the repository, which is fine as it's likely a developer-facing message from Firebase.
                // If this message were also for users, we'd map the error type to a string resource here.
                _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
            is AuthResult.Loading -> { /* Handled */ }
        }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}
