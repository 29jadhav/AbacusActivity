package com.vivek.abacusactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.vivek.abacusactivity.domain.repository.AuthRepository
import com.vivek.abacusactivity.navigation.AppNavigation
import com.vivek.abacusactivity.ui.theme.AbacusActivityTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbacusActivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val currentUser by authRepository.currentUser.collectAsState(initial = Unit)
                    if (currentUser != Unit) {
                        AppNavigation(modifier = Modifier.padding(innerPadding),isUserLoggedIn = currentUser != null)
                    }
                }
            }
        }
    }
}