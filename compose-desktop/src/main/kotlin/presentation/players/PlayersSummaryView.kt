package presentation.players

import LocalKoin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.PlayerDetailsViewShared
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun PlayersSummaryView(
    onPlayerSelected: (player: Player) -> Unit,
) {
    val repository = LocalKoin.current.get<FantasyPremierLeagueRepository>()
    var selectedPlayer by rememberSaveable { mutableStateOf<Player?>(null) }

    val searchQuery = MutableStateFlow("")
    val playerList by searchQuery.debounce(250).flatMapLatest { searchQueryValue ->
        repository.getPlayers().mapLatest { playerList ->
            playerList
                .filter { it.name.contains(searchQueryValue, ignoreCase = true) }
                .sortedByDescending { it.points }
        }
    }.collectAsState(emptyList())

    BoxWithConstraints {
        if (maxWidth.value > 700) {
            TwoColumnsLayout(playerList, selectedPlayer ?: playerList.firstOrNull()) {
                selectedPlayer = it
            }
        } else {
            PlayerListView(playerList, selectedPlayer) {
                selectedPlayer = it
                onPlayerSelected(it)
            }
        }
    }
}

@Composable
fun TwoColumnsLayout(
    playerList: List<Player>,
    selectedPlayer: Player?,
    repository: FantasyPremierLeagueRepository = LocalKoin.current.get(),
    playerSelected: (player: Player) -> Unit,
) {
    var playerHistory by remember { mutableStateOf(emptyList<PlayerPastHistory>()) }
    LaunchedEffect(selectedPlayer) {
        selectedPlayer?.let {
            playerHistory = repository.getPlayerHistoryData(it.id)
        }
    }
    Row(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth(0.3f), contentAlignment = Alignment.Center) {
            PlayerListView(playerList, selectedPlayer) {
                playerSelected(it)
            }
        }
        selectedPlayer?.let { player ->
            PlayerDetailsViewShared(player, playerHistory)
        }
    }
}
