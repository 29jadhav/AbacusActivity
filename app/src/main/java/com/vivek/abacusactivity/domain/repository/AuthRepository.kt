package com.vivek.abacusactivity.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

// A simple wrapper for results
sealed class AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}

interface AuthRepository {
    /**
     * Returns a Flow that emits the current FirebaseUser or null if not logged in.
     * This allows the UI to reactively update when the auth state changes.
     */
    val currentUser: Flow<FirebaseUser?>

    /**
     * Attempts to sign in the user with a Google ID token.
     * @param idToken The token received from Google One Tap sign-in.
     * @return AuthResult indicating success or failure.
     */
    suspend fun signInWithGoogle(idToken: String): AuthResult

    /**
     * Signs out the current user.
     */
    suspend fun signOut()
}