package presentation.main

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.johnoreilly.common.ui.Screen
import presentation.players.PlayerDetailsView
import presentation.players.PlayersSummaryView

@Composable
fun MainGraph(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.PlayerSummaryScreen.title) {
        composable(Screen.PlayerSummaryScreen.title) {
            PlayersSummaryView(windowSizeClass) { player ->
                navController.navigate(Screen.PlayerDetailsScreen.title + "/${player.id}")
            }
        }
        composable(
            Screen.PlayerDetailsScreen.title + "/{playerId}",
            arguments = listOf(navArgument("playerId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val playerId: Int? = navBackStackEntry.arguments?.getInt("playerId")
            playerId?.let {
                PlayerDetailsView(
                    windowSizeClass = windowSizeClass,
                    playerId = playerId,
                    popBackStack = { navController.popBackStack() },
                )
            }
        }
    }
}