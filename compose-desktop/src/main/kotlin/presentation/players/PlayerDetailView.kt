package presentation.players

import LocalKoin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import dev.johnoreilly.common.ui.PlayerDetailsViewShared

@Composable
fun PlayerDetailsView(playerId: Int, popBackStack: () -> Unit) {
    val repository = LocalKoin.current.get<FantasyPremierLeagueRepository>()
    val player by produceState<Player?>(initialValue = null) {
        value = repository.getPlayer(playerId)
    }
    player?.let { player ->
        var playerHistory by remember { mutableStateOf(emptyList<PlayerPastHistory>()) }
        LaunchedEffect(player) {
            playerHistory = repository.getPlayerHistoryData(player.id)
        }

        Scaffold {
            Column(Modifier.padding(it)) {
                PlayerDetailsViewShared(player, playerHistory)
            }
            IconButton(onClick = { popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
}
