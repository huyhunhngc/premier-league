package dev.johnoreilly.fantasypremierleague

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.johnoreilly.common.ui.features.main.bottomNavigationItems

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (VERSION.SDK_INT < VERSION_CODES.O) {
            enableEdgeToEdge()
        } else {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.Transparent.toArgb(),
                    darkScrim = Color.Transparent.toArgb(),
                ),
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.Transparent.toArgb(),
                    darkScrim = Color.Transparent.toArgb(),
                ),
            )

            if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val windowSize = calculateWindowSizeClass()
            FantasyPremierLeagueApp(
                windowSize,
                Modifier.fillMaxSize()
            )
        }
    }
}
