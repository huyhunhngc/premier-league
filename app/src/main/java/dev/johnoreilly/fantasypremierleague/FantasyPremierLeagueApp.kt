package dev.johnoreilly.fantasypremierleague

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.johnoreilly.common.ui.NavHostWithSharedAxisX
import dev.johnoreilly.common.ui.features.main.Screen
import dev.johnoreilly.fantasypremierleague.presentation.fixtures.details.FixtureDetailsScreen
import dev.johnoreilly.fantasypremierleague.presentation.fixtures.FixturesScreen
import dev.johnoreilly.fantasypremierleague.presentation.global.FantasyPremierLeagueTheme
import dev.johnoreilly.fantasypremierleague.presentation.leagues.LeagueListView
import dev.johnoreilly.fantasypremierleague.presentation.main.mainGraph
import dev.johnoreilly.fantasypremierleague.presentation.players.PlayersScreen
import dev.johnoreilly.fantasypremierleague.presentation.players.details.PlayerDetailsScreen
import dev.johnoreilly.fantasypremierleague.presentation.settings.SettingsView

@Composable
fun FantasyPremierLeagueApp(
    windowSize: WindowSizeClass,
    modifier: Modifier
) {
    FantasyPremierLeagueTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavHost(
                windowSize = windowSize,
                navController = rememberNavController(),
                mainNestedNavController = rememberNavController()
            )
        }
    }
}

@Composable
fun AppNavHost(
    windowSize: WindowSizeClass,
    navController: NavHostController,
    mainNestedNavController: NavHostController,
) {
    NavHostWithSharedAxisX(navController, startDestination = Screen.MainScreen.title) {
        mainGraph(windowSize, mainNestedNavController) { mainNestedNavController, padding ->
            composable(Screen.PlayerListScreen.title) {
                PlayersScreen(
                    padding = padding,
                    windowSizeClass = windowSize,
                    onPlayerSelected = { player ->
                        mainNestedNavController.navigate(Screen.PlayerDetailsScreen.title + "/${player.id}")
                    },
                    onShowSettings = {
                        mainNestedNavController.navigate(Screen.SettingsScreen.title)
                    }
                )
            }
            composable(
                Screen.PlayerDetailsScreen.title + "/{playerId}",
                arguments = listOf(navArgument("playerId") { type = NavType.IntType })
            ) { navBackStackEntry ->
                val playerId: Int? = navBackStackEntry.arguments?.getInt("playerId")
                playerId?.let {
                    PlayerDetailsScreen(
                        padding = padding,
                        windowSizeClass = windowSize,
                        playerId = playerId,
                        popBackStack = { mainNestedNavController.popBackStack() })
                }
            }
            composable(Screen.FixtureListScreen.title) {
                FixturesScreen(
                    padding = padding,
                    onFixtureSelected = { fixtureId ->
                        mainNestedNavController.navigate(Screen.FixtureDetailsScreen.title + "/${fixtureId}")
                    }
                )
            }
            composable(
                Screen.FixtureDetailsScreen.title + "/{fixtureId}",
                arguments = listOf(navArgument("fixtureId") { type = NavType.IntType })
            ) { navBackStackEntry ->
                val fixtureId: Int? = navBackStackEntry.arguments?.getInt("fixtureId")
                fixtureId?.let {
                    FixtureDetailsScreen(
                        padding = padding,
                        fixtureId = fixtureId,
                        popBackStack = { mainNestedNavController.popBackStack() })
                }
            }
            composable(Screen.LeagueStandingsListScreen.title) {
                LeagueListView()
            }
            composable(Screen.SettingsScreen.title) {
                SettingsView(popBackStack = { mainNestedNavController.popBackStack() })
            }
        }

    }
}