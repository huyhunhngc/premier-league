package dev.johnoreilly.fantasypremierleague.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.johnoreilly.common.ui.NavHostWithSharedAxisX
import dev.johnoreilly.common.ui.features.main.NavigationItem
import dev.johnoreilly.common.ui.features.main.NavigationType
import dev.johnoreilly.common.ui.features.main.Screen
import dev.johnoreilly.common.ui.features.main.section.FantasyPremierLeagueBottomNavigation
import dev.johnoreilly.common.ui.features.main.section.FantasyPremierLeagueNavigationRail

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
    val navigationType: NavigationType =
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> NavigationType.BottomNavigation
            WindowWidthSizeClass.Medium -> NavigationType.NavigationRail
            WindowWidthSizeClass.Expanded -> NavigationType.NavigationRail
            else -> NavigationType.BottomNavigation
        }
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(navigationType == NavigationType.NavigationRail) {
            FantasyPremierLeagueNavigationRail(
                navigationItems = bottomNavigationItems,
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
        }
        Scaffold(
            bottomBar = {
                AnimatedVisibility(navigationType == NavigationType.BottomNavigation) {
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
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            NavHostWithSharedAxisX(navController, startDestination = Screen.PlayerListScreen.title) {
                mainNestedGraph(navController, PaddingValues(bottom = padding.calculateBottomPadding()))
            }
        }
    }
}

val bottomNavigationItems = listOf(
    NavigationItem(
        Screen.PlayerListScreen.title,
        Icons.Default.Person,
        "Player"
    ),
    NavigationItem(
        Screen.FixtureListScreen.title,
        Icons.Filled.DateRange,
        "Fixtures"
    ),
    NavigationItem(
        Screen.LeagueStandingsListScreen.title,
        Icons.Filled.Search,
        "League"
    )
)