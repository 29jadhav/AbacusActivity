package com.vivek.abacusactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vivek.abacusactivity.screens.AbacusScreen
import com.vivek.abacusactivity.ui.theme.AbacusActivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbacusActivityTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    // Just call the main screen, which manages its own state via the ViewModel
                    AbacusScreen(modifier = Modifier.Companion.padding(innerPadding))
                }
            }
        }
    }
}