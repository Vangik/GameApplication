package com.example.gameapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gameapplication.ui.components.GameFieldScreen
import com.example.gameapplication.ui.components.HomeScreen
import com.example.gameapplication.ui.components.ResultsScreen
import com.example.gameapplication.viewModel.FarmGameViewModel

@Composable
fun SetupNavHost(navController: NavHostController, viewModel: FarmGameViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.GameField.route + "?level={level}",
            arguments = listOf(
                navArgument("level") {
                    type = NavType.IntType
                    defaultValue = 1
                    nullable = false
                }
            )) {
            it.arguments?.getInt("level")?.let { level ->
                GameFieldScreen(
                    level = level,
                    viewModel,
                    navController
                )
            }
        }
        composable(route = Screen.Results.route +
                "?level={level}&score={score}&result={result}",
            arguments = listOf(
                navArgument("level") {
                    type = NavType.IntType
                    defaultValue = 1
                    nullable = false
                },
                navArgument("score") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                },
                navArgument("result") {
                    type = NavType.BoolType
                    defaultValue = false
                    nullable = false
                }
            )) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level")
            val score = backStackEntry.arguments?.getInt("score")
            val result = backStackEntry.arguments?.getBoolean("result")
            ResultsScreen(score, level, navController, result)
        }
    }
}
