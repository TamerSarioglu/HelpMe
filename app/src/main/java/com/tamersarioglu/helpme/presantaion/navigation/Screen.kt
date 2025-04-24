package com.tamersarioglu.helpme.presantaion.navigation

import android.net.Uri
import com.tamersarioglu.helpme.domain.model.Earthquake
import kotlinx.serialization.json.Json

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")

    object Detail : Screen(route = "detail/{earthquakeJson}") {
        fun createRoute(earthquake: Earthquake): String {
            val json = Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
            val earthquakeJson = Uri.encode( json.encodeToString(value = earthquake))
            return "detail/$earthquakeJson"
        }
    }
}