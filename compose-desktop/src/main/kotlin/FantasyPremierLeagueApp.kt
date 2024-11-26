import androidx.compose.material3.MaterialTheme
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
import dev.johnoreilly.common.ui.theme.PremierLeagueTypography
import presentation.main.mainGraph
import presentation.players.PlayerDetailsView
import presentation.players.PlayersSummaryView

@Composable
fun FantasyPremierLeagueApp(
    windowSize: WindowSizeClass,
    modifier: Modifier
) {
    MaterialTheme(
        typography = PremierLeagueTypography()
    ) {
        AppNavHost(
            windowSize = windowSize,
            navController = rememberNavController(),
            mainNestedNavController = rememberNavController()
        )
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
            composable(Screen.PlayerSummaryScreen.title) {
                PlayersSummaryView(
                    padding = padding,
                    windowSizeClass = windowSize
                ) { player ->
                    mainNestedNavController.navigate(Screen.PlayerDetailsScreen.title + "/${player.id}")
                }
            }
            composable(
                Screen.PlayerDetailsScreen.title + "/{playerId}",
                arguments = listOf(navArgument("playerId") { type = NavType.IntType })
            ) { navBackStackEntry ->
                val playerId: Int? = navBackStackEntry.arguments?.getInt("playerId")
                playerId?.let {
                    PlayerDetailsView(
                        padding = padding,
                        windowSizeClass = windowSize,
                        playerId = playerId,
                        popBackStack = { mainNestedNavController.popBackStack() },
                    )
                }
            }
            composable(Screen.FixtureListScreen.title) {

            }
        }
    }
}
