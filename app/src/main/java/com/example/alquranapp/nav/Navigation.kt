package com.example.alquranapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alquranapp.screens.DetailJuzScreen
import com.example.alquranapp.screens.DetailSurahScreen
import com.example.alquranapp.screens.ListSurahScreen
import com.example.alquranapp.screens.SearchScreen

@Composable
fun QuranNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "list") {
        composable("list") {
            ListSurahScreen(navController)
        }
        composable("detail/{surahNumber}") { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getString("surahNumber")?.toIntOrNull() ?: 1
            DetailSurahScreen(surahNumber)
        }
        composable("juzDetail/{juzNumber}") { backStackEntry ->
            val juzNumber = backStackEntry.arguments?.getString("juzNumber")?.toIntOrNull() ?: 1
            DetailJuzScreen(juzNumber = juzNumber)
        }
        composable("search") {
            SearchScreen()
        }
    }
}
