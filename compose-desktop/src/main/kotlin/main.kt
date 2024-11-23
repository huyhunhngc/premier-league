import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.johnoreilly.common.di.initKoin
import org.koin.core.Koin
import presentation.main.MainGraph

@Suppress("CompositionLocalAllowlist")
val LocalKoin = compositionLocalOf<Koin> {
    error("No LocalRepository provided")
}

val lightThemeColors = lightColors(
    primary = Color(0xFFDD0D3C),
    primaryVariant = Color(0xFFC20029),
    secondary = Color.White,
    error = Color(0xFFD00036)
)

fun main() = application {
    val windowState = rememberWindowState()
    val koin = initKoin(enableNetworkLogs = true).koin

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Fantasy Premier League"
    ) {
        MaterialTheme(
            colors = lightThemeColors
        ) {
            CompositionLocalProvider(
                LocalKoin provides koin
            ) {
                MainGraph()
            }
        }
    }
}

