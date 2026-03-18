package com.KotlinCrossPlatform.cotacao
import io.ktor.client.HttpClient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.KotlinCrossPlatform.cotacao.data.CurrencyRemoteDataSource
import com.KotlinCrossPlatform.cotacao.data.createHttpClient
import com.KotlinCrossPlatform.cotacao.domain.repository.CurrencyRepository
import com.KotlinCrossPlatform.cotacao.presentation.HistoryViewModel
import com.KotlinCrossPlatform.cotacao.presentation.HomeViewModel
import com.KotlinCrossPlatform.cotacao.ui.history.HistoryScreen
import com.KotlinCrossPlatform.cotacao.ui.home.HomeScreen
import com.KotlinCrossPlatform.cotacao.ui.theme.AppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        
        // Manual DI for simplicity in this migration
        val repository = remember {
            CurrencyRepository(CurrencyRemoteDataSource(createHttpClient()))
        }

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                val homeViewModel = viewModel { HomeViewModel(repository) }
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToHistory = { pair ->
                        navController.navigate("history/$pair")
                    }
                )
            }
            composable("history/{pair}") { backStackEntry ->
                val pair = backStackEntry.arguments?.getString("pair") ?: ""
                val historyViewModel = viewModel { HistoryViewModel(repository) }
                HistoryScreen(
                    pair = pair,
                    viewModel = historyViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
