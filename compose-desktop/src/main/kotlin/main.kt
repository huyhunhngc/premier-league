import androidx.compose.material3.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.johnoreilly.common.di.initKoin
import dev.johnoreilly.common.ui.theme.PremierLeagueTypography
import org.koin.core.Koin
import presentation.main.MainGraph

@Suppress("CompositionLocalAllowlist")
val LocalKoin = compositionLocalOf<Koin> {
    error("No LocalRepository provided")
}

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1000.dp, 800.dp))
    val koin = initKoin(enableNetworkLogs = true).koin

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Fantasy Premier League"
    ) {
        MaterialTheme(
            typography = PremierLeagueTypography()
        ) {
            CompositionLocalProvider(
                LocalKoin provides koin
            ) {
                MainGraph()
            }
        }
    }
}
