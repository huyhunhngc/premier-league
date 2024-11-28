package dev.johnoreilly.fantasypremierleague

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.johnoreilly.common.ui.NavHostWithSharedAxisX
import dev.johnoreilly.common.ui.features.main.Screen
import dev.johnoreilly.common.ui.navigation.FixtureDetailDestination
import dev.johnoreilly.common.ui.navigation.PlayerDetailDestination
import dev.johnoreilly.fantasypremierleague.presentation.fixtures.FixturesScreen
import dev.johnoreilly.fantasypremierleague.presentation.fixtures.details.FixtureDetailsScreen
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
                        mainNestedNavController.navigate(PlayerDetailDestination(player.id))
                    },
                    onShowSettings = {
                        mainNestedNavController.navigate(Screen.SettingsScreen.title)
                    }
                )
            }
            composable<PlayerDetailDestination> { navBackStackEntry ->
                val player: PlayerDetailDestination = navBackStackEntry.toRoute()
                PlayerDetailsScreen(
                    padding = padding,
                    windowSizeClass = windowSize,
                    playerId = player.playerId,
                    popBackStack = { mainNestedNavController.popBackStack() })
            }
            composable(Screen.FixtureListScreen.title) {
                FixturesScreen(
                    padding = padding,
                    onFixtureSelected = { fixtureId ->
                        mainNestedNavController.navigate(FixtureDetailDestination(fixtureId))
                    }
                )
            }
            composable<FixtureDetailDestination> { navBackStackEntry ->
                val fixture: FixtureDetailDestination = navBackStackEntry.toRoute()
                FixtureDetailsScreen(
                    padding = padding,
                    fixtureId = fixture.fixtureId,
                    popBackStack = { mainNestedNavController.popBackStack() })

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