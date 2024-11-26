package dev.johnoreilly.common.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.features.player.PlayerDetailsViewShared
import dev.johnoreilly.common.viewmodel.PlayerDetailsViewModel
import org.koin.compose.koinInject
import platform.UIKit.UIViewController

object SharedViewControllers {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    fun playerDetailsViewController(player: Player): UIViewController =
        ComposeUIViewController {
            val viewModel = koinInject<PlayerDetailsViewModel>()

            // TODO cleaner way of managing this?
            var playerHistory by remember { mutableStateOf(emptyList<PlayerPastHistory>()) }
            LaunchedEffect(player) {
                playerHistory = viewModel.getPlayerHistory(player.id)
            }
            val windowSizeClass = calculateWindowSizeClass()
            PlayerDetailsViewShared(windowSizeClass, player, playerHistory)
        }
}
