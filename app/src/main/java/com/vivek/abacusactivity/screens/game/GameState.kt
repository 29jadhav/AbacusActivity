package com.vivek.abacusactivity.screens.game

enum class GameState {
    SETUP, // The initial screen where the user chooses the time
    ACTIVE, // The game is running
    PAUSED, // The game is paused
    FINISHED // The results screen
}