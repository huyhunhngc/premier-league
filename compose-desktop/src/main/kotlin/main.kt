import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.johnoreilly.common.di.initKoin
import org.koin.core.Koin
import java.awt.Dimension

@Suppress("CompositionLocalAllowlist")
val LocalKoin = compositionLocalOf<Koin> {
    error("No LocalRepository provided")
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1200.dp, 800.dp))
    val koin = initKoin(enableNetworkLogs = true).koin

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Fantasy Premier League"
    ) {
        window.minimumSize = Dimension(300, 800)
        val windowSize = calculateWindowSizeClass()
        CompositionLocalProvider(
            LocalKoin provides koin
        ) {
            FantasyPremierLeagueApp(
                windowSize,
                Modifier
            )
        }
    }
}
