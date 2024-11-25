package dev.johnoreilly.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.component.BarSamplePlot
import dev.johnoreilly.common.ui.component.PlayerHeader
import dev.johnoreilly.common.ui.component.PlayerStatView
import dev.johnoreilly.common.ui.component.TickPositionState
import dev.johnoreilly.common.ui.theme.primaryEplContainer
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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerHeader(windowSizeClass, player)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryEplContainer)
                .padding(4.dp)
        ) {
            Text(
                text = "Stats",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        PlayerStatView(stringResource(Res.string.team), player.team)
        PlayerStatView("CurrentPrice", player.currentPrice.toString())
        PlayerStatView("Points", player.points.toString())
        PlayerStatView("Goals Scored", player.goalsScored.toString())
        PlayerStatView("Assists", player.assists.toString())

        Spacer(modifier = Modifier.size(8.dp))
        if (playerHistory.isNotEmpty()) {
            BarSamplePlot(playerHistory, tickPositionState, "Points by Season")
        }
    }
}
