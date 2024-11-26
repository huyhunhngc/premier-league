package dev.johnoreilly.fantasypremierleague.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.johnoreilly.common.ui.NavHostWithSharedAxisX
import dev.johnoreilly.common.ui.features.main.Screen
import dev.johnoreilly.common.ui.features.main.bottomNavigationItems
import dev.johnoreilly.common.ui.features.main.section.FantasyPremierLeagueBottomNavigation

fun NavGraphBuilder.mainGraph(
    windowSize: WindowSizeClass,
    mainNestedNavController: NavHostController,
    mainNestedGraph: NavGraphBuilder.(mainNestedNavController: NavController, PaddingValues) -> Unit,
) {
    composable(Screen.MainScreen.title) {
        MainScreen(
            windowSizeClass = windowSize,
            navController = mainNestedNavController,
            mainNestedGraph = mainNestedGraph
        )
    }
}

@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    mainNestedGraph: NavGraphBuilder.(mainNestedNavController: NavController, PaddingValues) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            FantasyPremierLeagueBottomNavigation(
                bottomNavigationItems = bottomNavigationItems,
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentRoute = currentRoute
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        NavHostWithSharedAxisX(navController, startDestination = Screen.PlayerListScreen.title) {
            mainNestedGraph(navController, PaddingValues(bottom = padding.calculateBottomPadding()))
        }
    }
}