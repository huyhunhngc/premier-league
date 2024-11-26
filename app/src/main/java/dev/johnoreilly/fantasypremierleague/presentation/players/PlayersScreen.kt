@file:OptIn(ExperimentalMaterial3Api::class)

package dev.johnoreilly.fantasypremierleague.presentation.players

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.placeholder
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.ui.features.player.component.PlayerHeader
import dev.johnoreilly.common.viewmodel.PlayerListUIState
import dev.johnoreilly.common.viewmodel.PlayerListViewModel
import dev.johnoreilly.fantasypremierleague.presentation.global.lowfidelitygray
import dev.johnoreilly.fantasypremierleague.presentation.players.component.InputTextField
import dev.johnoreilly.fantasypremierleague.presentation.players.component.PlayerView
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersScreen(
    padding: PaddingValues = PaddingValues(),
    windowSizeClass: WindowSizeClass,
    onPlayerSelected: (player: Player) -> Unit,
    onShowSettings: () -> Unit
) {
    val playerListViewModel = koinViewModel<PlayerListViewModel>()
    val playerListUIState = playerListViewModel.playerListUIState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val allPlayers = playerListViewModel.allPlayers.collectAsStateWithLifecycle()
    val playerSearchQuery = playerListViewModel.searchQuery.collectAsStateWithLifecycle()
    val isDataLoading by remember(allPlayers) { derivedStateOf { allPlayers.value.isEmpty() } }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(padding),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchView(
                        paddingValues = PaddingValues(),
                        isDataLoading = isDataLoading,
                        searchValue = playerSearchQuery.value,
                        onValueChange = playerListViewModel::onPlayerSearchQueryChange,
                        onClearQuery = {
                            playerListViewModel.onPlayerSearchQueryChange("")
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        onShowSettings()
                    }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) {
        Column {
            when (val uiState = playerListUIState.value) {
                is PlayerListUIState.Error -> {
                    Text("Error: ${uiState.message}", modifier = Modifier.padding(it))
                }

                PlayerListUIState.Loading -> {
                    LazyColumn {
                        items(
                            items = placeHolderPlayerList, itemContent = { player ->
                                PlayerView(player, onPlayerSelected, isDataLoading)
                            }
                        )
                    }
                }

                is PlayerListUIState.Success -> {
                    LazyColumn(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                        itemsIndexed(items = uiState.result, itemContent = { index, player ->
                            if (index == 0) {
                                Box(modifier = Modifier.clickable { onPlayerSelected(player) }) {
                                    PlayerHeader(windowSizeClass, player)
                                }
                            } else {
                                PlayerView(player, onPlayerSelected, isDataLoading)
                            }
                            HorizontalDivider()
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(
    paddingValues: PaddingValues,
    isDataLoading: Boolean,
    searchValue: String,
    onValueChange: (String) -> Unit,
    onClearQuery: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    InputTextField(
        singleLine = true,
        value = searchValue,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(32.dp))
            .placeholder(
                visible = isDataLoading,
                color = lowfidelitygray
            ),
        textStyle = MaterialTheme.typography.labelLarge,
        placeholder = {
            Text(text = "Search", style = MaterialTheme.typography.labelLarge)
        },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        onValueChange = onValueChange,
        trailingIcon = {
            if (searchValue.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onClearQuery()
                        },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

private val placeHolderPlayerList = listOf(
    Player(
        1, "Jordan Henderson", "Liverpool",
        "", 95, 10.0, 14, 1, ""
    ),
    Player(
        1, "Jordan Henderson", "Liverpool",
        "", 95, 10.0, 14, 1, ""
    ),
    Player(
        1, "Jordan Henderson", "Liverpool",
        "", 95, 10.0, 14, 1, ""
    ),
    Player(
        1, "Jordan Henderson", "Liverpool",
        "", 95, 10.0, 14, 1, ""
    ),
    Player(
        1, "Jordan Henderson", "Liverpool",
        "", 95, 10.0, 14, 1, ""
    ),
    Player(
        1, "Jordan Henderson", "Liverpool",
        "", 95, 10.0, 14, 1, ""
    )
)