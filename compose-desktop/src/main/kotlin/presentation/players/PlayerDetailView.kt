package presentation.players

import LocalKoin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.features.player.PlayerDetailsViewShared

@Composable
fun PlayerDetailsView(
    windowSizeClass: WindowSizeClass,
    playerId: Int,
    popBackStack: () -> Unit
) {
    val repository = LocalKoin.current.get<FantasyPremierLeagueRepository>()
    val playerState by produceState<Player?>(initialValue = null) {
        value = repository.getPlayer(playerId)
    }
    playerState?.let { player ->
        var playerHistory by remember { mutableStateOf(emptyList<PlayerPastHistory>()) }
        LaunchedEffect(player) {
            playerHistory = repository.getPlayerHistoryData(player.id)
        }

        Scaffold { padding ->
            Column(Modifier.padding(padding)) {
                PlayerDetailsViewShared(windowSizeClass, player, playerHistory)
            }
            IconButton(onClick = { popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
}
