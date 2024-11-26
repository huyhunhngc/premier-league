package dev.johnoreilly.common.ui.features.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.features.player.component.BarSamplePlot
import dev.johnoreilly.common.ui.features.player.component.PlayerHeader
import dev.johnoreilly.common.ui.features.player.component.PlayerStatView
import dev.johnoreilly.common.ui.features.player.component.TickPositionState
import fantasypremierleague.common.generated.resources.Res
import fantasypremierleague.common.generated.resources.team
import io.github.koalaplot.core.xychart.TickPosition
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayerDetailsViewShared(
    windowSizeClass: WindowSizeClass,
    player: Player,
    playerHistory: List<PlayerPastHistory>
) {
    val tickPositionState by remember {
        mutableStateOf(
            TickPositionState(TickPosition.Outside, TickPosition.Outside)
        )
    }

    LazyColumn {
        item {
            PlayerHeader(windowSizeClass, player)
        }
        item {
            PlayerStatView(stringResource(Res.string.team), player.team)
            PlayerStatView("CurrentPrice", player.currentPrice.toString())
            PlayerStatView("Points", player.points.toString())
            PlayerStatView("Goals Scored", player.goalsScored.toString())
            PlayerStatView("Assists", player.assists.toString())
        }
        item {
            Spacer(modifier = Modifier.size(8.dp))
        }
        item {
            if (playerHistory.isNotEmpty() && playerHistory.maxOf { it.totalPoints } > 0) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    BarSamplePlot(playerHistory, tickPositionState, "Points by Season")
                }
            }
        }
    }
}
