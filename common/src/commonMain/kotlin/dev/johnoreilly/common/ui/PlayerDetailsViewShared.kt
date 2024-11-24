package dev.johnoreilly.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.component.BarSamplePlot
import dev.johnoreilly.common.ui.component.PlayerHeader
import dev.johnoreilly.common.ui.component.PlayerStatView
import dev.johnoreilly.common.ui.component.TickPositionState
import dev.johnoreilly.common.ui.ext.extractTeamCode
import dev.johnoreilly.common.ui.theme.PremierLeagueTypography
import fantasypremierleague.common.generated.resources.Res
import fantasypremierleague.common.generated.resources.background_header
import fantasypremierleague.common.generated.resources.background_header_mobile
import fantasypremierleague.common.generated.resources.team
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.BarChartEntry
import io.github.koalaplot.core.bar.DefaultBarChartEntry
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xychart.CategoryAxisModel
import io.github.koalaplot.core.xychart.LinearAxisModel
import io.github.koalaplot.core.xychart.TickPosition
import io.github.koalaplot.core.xychart.XYChart
import io.github.koalaplot.core.xychart.rememberAxisStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import dev.johnoreilly.common.ui.theme.primaryEpl
import dev.johnoreilly.common.ui.theme.primaryEplContainer

@Composable
fun PlayerDetailsViewShared(player: Player, playerHistory: List<PlayerPastHistory>) {
    val tickPositionState by remember {
        mutableStateOf(
            TickPositionState(
                TickPosition.Outside,
                TickPosition.Outside
            )
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerHeader(player)
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
