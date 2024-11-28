package presentation.main

import LocalKoin
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.johnoreilly.common.data.repository.AppPreferencesRepository
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.model.AppTheme
import dev.johnoreilly.common.ui.NavHostWithSharedAxisX
import dev.johnoreilly.common.ui.component.ToggleThemeIcon
import dev.johnoreilly.common.ui.features.main.NavigationItem
import dev.johnoreilly.common.ui.features.main.NavigationType
import dev.johnoreilly.common.ui.features.main.Screen
import dev.johnoreilly.common.ui.features.main.section.FantasyPremierLeagueBottomNavigation
import dev.johnoreilly.common.ui.features.main.section.FantasyPremierLeagueNavigationRail
import kotlinx.coroutines.launch

fun NavGraphBuilder.mainGraph(
    windowSize: WindowSizeClass,
    mainNestedNavController: NavHostController,
    mainNestedGraph: NavGraphBuilder.(mainNestedNavController: NavController, PaddingValues) -> Unit,
) {
    composable(Screen.MainScreen.title) {
        MainView(
            windowSizeClass = windowSize,
            navController = mainNestedNavController,
            mainNestedGraph = mainNestedGraph
        )
    }
}

@Composable
fun MainView(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    mainNestedGraph: NavGraphBuilder.(mainNestedNavController: NavController, PaddingValues) -> Unit
) {
    val appPreferencesRepository = LocalKoin.current.get<AppPreferencesRepository>()
    val scope = rememberCoroutineScope()
    val currentTheme by appPreferencesRepository.getTheme().collectAsState(initial = AppTheme.SYSTEM)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationType: NavigationType =
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> NavigationType.NavigationRail
            else -> NavigationType.BottomNavigation
        }
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(navigationType == NavigationType.NavigationRail) {
            FantasyPremierLeagueNavigationRail(
                navigationItems = navigationItems,
                onTabSelected = { route ->
                    navController.navigate(route) {
                        navController.graph.route?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentRoute = currentRoute
            ) {
                Spacer(modifier = Modifier.weight(1f))
                ToggleThemeIcon(
                    currentTheme = currentTheme,
                    isSystemInDarkTheme = isSystemInDarkTheme(),
                    toggleTheme = { scope.launch { appPreferencesRepository.saveTheme(it) } }
                )
            }
        }
        Scaffold(
            bottomBar = {
                AnimatedVisibility(navigationType == NavigationType.BottomNavigation) {
                    FantasyPremierLeagueBottomNavigation(
                        bottomNavigationItems = navigationItems,
                        onTabSelected = { route ->
                            navController.navigate(route) {
                                navController.graph.route?.let {
                                    popUpTo(it) {
                                        saveState = true
                                    }
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
            NavHostWithSharedAxisX(navController, startDestination = Screen.PlayerSummaryScreen.title) {
                mainNestedGraph(navController, PaddingValues(bottom = padding.calculateBottomPadding()))
            }
        }
    }
}

val navigationItems = listOf(
    NavigationItem(
        Screen.PlayerSummaryScreen.title,
        Icons.Default.Person,
        "Player"
    ),
    NavigationItem(
        Screen.FixtureListScreen.title,
        Icons.Filled.DateRange,
        "Fixtures"
    )
)