package com.vivek.abacusactivity.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlin.collections.forEach

sealed class AppRoutes(
    val baseRoute: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    // The final route string used by NavHost
    val route: String
        get() = if (navArguments.isEmpty()) {
            baseRoute
        } else {
            val builder = StringBuilder(baseRoute)
            navArguments.forEach { builder.append("/{${it.name}}") }
            builder.toString()
        }

    // Route for Start Screen (no arguments)
    data object Start : AppRoutes("start")

    data class Result(val gameId: Long = -1, val isFromHistory: Boolean = false) : AppRoutes(
        baseRoute = "result",
        navArguments = listOf(
            navArgument("gameId") { type = NavType.LongType },
            navArgument("isFromHistory") { type = NavType.BoolType }
        )
    ) {
        fun buildRoute() = "$baseRoute/$gameId/$isFromHistory"
    }

    // Route for Game Screen (with a duration argument)
    data class Game(val duration: Int = 0) : AppRoutes(
        baseRoute = "game",
        navArguments = listOf(navArgument("durationInSeconds") { type = NavType.IntType })
    ) {
        fun buildRoute() = "$baseRoute/$duration"

    }

    object History : AppRoutes("history")
}