package presentation.players

import LocalKoin
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.ui.features.player.PlayerDetailsViewShared
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import presentation.fixtures.localFantasyPremierLeagueRepository
import androidx.compose.material.TextField as ComposeTextField

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun PlayersSummaryView(
    padding: PaddingValues,
    windowSizeClass: WindowSizeClass,
    repository: FantasyPremierLeagueRepository = localFantasyPremierLeagueRepository(),
    onPlayerSelected: (player: Player) -> Unit,
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    var selectedPlayer by rememberSaveable { mutableStateOf<Player?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val playerHistory by produceState(emptyList(), selectedPlayer) {
        value = selectedPlayer?.id?.let { repository.getPlayerHistoryData(it) }.orEmpty()
    }
    val playerList by snapshotFlow { searchQuery }.debounce(250).flatMapLatest { query ->
        repository.getPlayers().mapLatest { playerList ->
            playerList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.team.contains(query, ignoreCase = true)
            }.sortedByDescending { it.points }
        }
    }.collectAsState(emptyList())

    Row(Modifier.fillMaxSize().padding(padding)) {
        Box(
            modifier = Modifier.fillMaxWidth(if (isExpanded) 0.3f else 1f),
            contentAlignment = Alignment.Center
        ) {
            Column {
                PlayerSearchView(
                    searchQuery,
                    onValueChange = { searchQuery = it },
                    onValueClear = { searchQuery = "" }
                )
                PlayerListView(isExpanded, playerList, selectedPlayer) {
                    selectedPlayer = it
                    if (!isExpanded) {
                        onPlayerSelected(it)
                    }
                }
            }
        }
        if (isExpanded) {
            if (selectedPlayer == null) selectedPlayer = playerList.firstOrNull()
            selectedPlayer?.let { player ->
                PlayerDetailsViewShared(windowSizeClass, player, playerHistory)
            }
        }
    }
}

@Composable
fun PlayerSearchView(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    onValueClear: () -> Unit
) {
    ComposeTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(36.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        value = searchQuery,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable {
                        onValueClear()
                    },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
    )
}
