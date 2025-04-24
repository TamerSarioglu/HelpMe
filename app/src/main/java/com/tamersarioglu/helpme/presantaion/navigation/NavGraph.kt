package com.tamersarioglu.helpme.presantaion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tamersarioglu.helpme.domain.model.Earthquake
import com.tamersarioglu.helpme.presantaion.ui.detail.DetailScreen
import com.tamersarioglu.helpme.presantaion.ui.home.HomeScreen
import kotlinx.serialization.json.Json

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screen.Detail.route) { backStackEntry ->
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }

            val earthquakeJson = backStackEntry.arguments?.getString("earthquakeJson")
            earthquakeJson?.let { encodedJson ->
                val earthquake = json.decodeFromString<Earthquake>(encodedJson)
                DetailScreen(earthquake = earthquake, navController = navController)
            }
        }
    }
}