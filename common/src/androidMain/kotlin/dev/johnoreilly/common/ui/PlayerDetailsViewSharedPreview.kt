package dev.johnoreilly.common.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.features.player.PlayerDetailsViewShared

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
fun PlayerDetailsViewSharedPreview() {
    val player = Player(
        1, "Mo Salah", "Liverpool",
        "", 98, 10.0, 14, 1, ""
    )
    val playerPastHistory1 = PlayerPastHistory("2021", 50)
    val playerPastHistory2 = PlayerPastHistory("2022", 75)
    val playerPastHistory3 = PlayerPastHistory("2023", 60)

    val playerHistory = listOf(playerPastHistory1, playerPastHistory2, playerPastHistory3)
    PlayerDetailsViewShared(calculateWindowSizeClass(), player, playerHistory)
}