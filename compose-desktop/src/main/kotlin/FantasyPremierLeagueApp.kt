import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import dev.johnoreilly.common.data.repository.AppPreferencesRepository
import dev.johnoreilly.common.model.AppTheme
import dev.johnoreilly.common.ui.NavHostWithSharedAxisX
import dev.johnoreilly.common.ui.features.main.Screen
import dev.johnoreilly.common.ui.navigation.PlayerDetailDestination
import dev.johnoreilly.common.ui.theme.PremierLeagueTypography
import presentation.fixtures.FixturesView
import presentation.main.mainGraph
import presentation.players.PlayerDetailsView
import presentation.players.PlayersSummaryView

@Composable
fun FantasyPremierLeagueApp(
    windowSize: WindowSizeClass,
    modifier: Modifier
) {
    val themePreferences by LocalKoin.current.get<AppPreferencesRepository>().getTheme()
        .collectAsState(AppTheme.SYSTEM)
    val isDarkTheme = when (themePreferences) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()

    }
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme(),
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
                    mainNestedNavController.navigate(PlayerDetailDestination(player.id))
                }
            }
            composable<PlayerDetailDestination> { navBackStackEntry ->
                val player: PlayerDetailDestination = navBackStackEntry.toRoute()
                PlayerDetailsView(
                    padding = padding,
                    windowSizeClass = windowSize,
                    playerId = player.playerId,
                    popBackStack = { mainNestedNavController.popBackStack() },
                )
            }
            composable(Screen.FixtureListScreen.title) {
                FixturesView(
                    padding = padding,
                    windowSize = windowSize,
                    onFixtureSelected = { fixtureId ->
                        //mainNestedNavController.navigate()
                    }
                )
            }
        }
    }
}
