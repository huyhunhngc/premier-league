import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.johnoreilly.common.data.repository.AppPreferencesRepository
import dev.johnoreilly.common.di.initKoin
import dev.johnoreilly.common.model.AppTheme
import org.koin.core.Koin
import presentation.CustomWindow
import java.awt.Dimension

@Suppress("CompositionLocalAllowlist")
val LocalKoin = compositionLocalOf<Koin> {
    error("No LocalRepository provided")
}
private val koin = initKoin(enableNetworkLogs = true).koin

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1200.dp, 800.dp))

    val themePreferences by koin.get<AppPreferencesRepository>().getTheme()
        .collectAsState(AppTheme.SYSTEM)
    val isDarkTheme = when (themePreferences) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()

    }

    CustomWindow(
        onCloseRequest = ::exitApplication,
        state = windowState,
        defaultTitle = "Fantasy Premier League",
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        window.minimumSize = Dimension(300, 800)
        val windowSize = calculateWindowSizeClass()
        CompositionLocalProvider(
            LocalKoin provides koin
        ) {
            FantasyPremierLeagueApp(
                windowSize = windowSize,
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme(),
                modifier = Modifier
            )
        }
    }
}
