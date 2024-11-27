@file:OptIn(ExperimentalMaterial3Api::class)

package dev.johnoreilly.fantasypremierleague.presentation.players.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.LifecycleStartEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.component.AutoResizedText
import dev.johnoreilly.common.ui.features.player.PlayerDetailsViewShared
import dev.johnoreilly.common.ui.theme.primaryEplContainer
import dev.johnoreilly.common.viewmodel.PlayerDetailsViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailsScreen(
    padding: PaddingValues = PaddingValues(),
    windowSizeClass: WindowSizeClass,
    playerId: Int,
    popBackStack: () -> Unit
) {
    val viewModel = koinInject<PlayerDetailsViewModel>()
    val systemUiController = rememberSystemUiController()
    LifecycleStartEffect(Unit) {
        systemUiController.statusBarDarkContentEnabled = false
        onStopOrDispose {
            systemUiController.statusBarDarkContentEnabled = true
        }
    }

    val uiState by produceState<PlayerDetailsUiState>(PlayerDetailsUiState.Loading, viewModel) {
        val player = viewModel.getPlayer(playerId)
        val playerHistory = viewModel.getPlayerHistory(playerId)
        value = PlayerDetailsUiState.Data(player, playerHistory)
    }

    when (val state = uiState) {
        is PlayerDetailsUiState.Data -> {
            Scaffold(
                modifier = Modifier.padding(padding),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            AutoResizedText(text = state.player.name, maxLines = 1)
                        },
                        navigationIcon = {
                            IconButton(onClick = { popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = primaryEplContainer,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                }
            ) {
                Box(Modifier.padding(top = it.calculateTopPadding())) {
                    PlayerDetailsViewShared(windowSizeClass, state.player, state.playerHistory)
                }
            }
        }

        PlayerDetailsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

sealed class PlayerDetailsUiState {
    data class Data(
        val player: Player,
        val playerHistory: List<PlayerPastHistory> = emptyList()
    ) : PlayerDetailsUiState()

    data object Loading : PlayerDetailsUiState()
}


