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
import dev.johnoreilly.common.ui.ext.extractTeamCode
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
        var isWide by remember { mutableStateOf(false) }

        BoxWithConstraints {
            isWide = maxWidth > 500.dp
            val colorHeader = player.extractTeamCode()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(colorHeader.second))
                    .paint(
                        painterResource(
                            if (isWide) Res.drawable.background_header
                            else Res.drawable.background_header_mobile
                        ),
                        colorFilter = ColorFilter.tint(Color(colorHeader.first).copy(alpha = 0.4f)),
                        contentScale = ContentScale.FillBounds
                    )
            ) {
                val imageSize = if (isWide) 180.dp else 150.dp
                val alignment = if (isWide) Alignment.BottomStart else Alignment.BottomEnd
                val playerName = player.name.split(" ")
                val firstname = playerName.firstOrNull().orEmpty()
                val lastname = playerName.lastOrNull().orEmpty()
                Image(
                    painter = rememberImagePainter(player.photoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(imageSize)
                        .align(alignment),
                    contentScale = ContentScale.Fit,
                )
                Image(
                    painter = rememberImagePainter(player.teamPhotoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(60.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp)
                        .align(if (isWide) Alignment.BottomEnd else Alignment.BottomStart),
                    contentScale = ContentScale.Fit,
                )
                Column(
                    modifier = Modifier.align(
                        if (isWide) Alignment.TopCenter else Alignment.TopStart
                    ).let {
                        if (isWide) {
                            it.padding(top = 16.dp)
                        } else {
                            it.padding(start = 16.dp, top = 42.dp)
                        }
                    }
                ) {
                    Text(
                        text = firstname,
                        color = Color(colorHeader.third),
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontSize = if (isWide) 36.sp else 18.sp)
                    )
                    Text(
                        text = lastname,
                        fontWeight = FontWeight.Bold,
                        color = Color(colorHeader.third),
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontSize = if (isWide) 40.sp else 20.sp)
                    )
                }
            }
        }


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


@Composable
fun PlayerStatView(statName: String, statValue: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = statName,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    text = statValue,
                    color = primaryEpl,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}



private fun barChartEntries(playerHistory: List<PlayerPastHistory>): List<BarChartEntry<String, Float>> {
    val list = mutableListOf<BarChartEntry<String, Float>>()

    playerHistory.forEach { player ->
        list.add(
            DefaultBarChartEntry(
                xValue = player.seasonName.takeLast(2),
                yMin = 0f,
                yMax = player.totalPoints.toFloat(),
            )
        )
    }
    return list
}


@Composable
fun ChartTitle(title: String) {
    Column {
        Text(
            title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AxisTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier
    )
}

@Composable
fun AxisLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier
    )
}

internal val padding = 8.dp
internal val barWidth = 0.8f

@Composable
fun HoverSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.LightGray,
        modifier = modifier.padding(padding)
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}


private data class TickPositionState(
    val verticalAxis: TickPosition,
    val horizontalAxis: TickPosition
)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun BarSamplePlot(
    playerHistory: List<PlayerPastHistory>,
    tickPositionState: TickPositionState,
    title: String
) {
    val barChartEntries = remember(playerHistory) { mutableStateOf(barChartEntries(playerHistory)) }

    ChartLayout(
        modifier = Modifier.padding(8.dp),
        title = { ChartTitle(title) }
    ) {

        XYChart(
            xAxisModel = CategoryAxisModel(playerHistory.map {
                it.seasonName.takeLast(2)
            }),
            yAxisModel = LinearAxisModel(
                0f..playerHistory.maxOf { it.totalPoints }.toFloat(),
                minimumMajorTickIncrement = 1f,
                minorTickCount = 0
            ),
            xAxisStyle = rememberAxisStyle(
                tickPosition = tickPositionState.horizontalAxis,
                color = Color.LightGray
            ),
            xAxisLabels = {
                AxisLabel(it, Modifier.padding(top = 2.dp))
            },
            xAxisTitle = { AxisTitle("Season") },
            yAxisStyle = rememberAxisStyle(tickPosition = tickPositionState.verticalAxis),
            yAxisLabels = {
                AxisLabel(it.toString(1), Modifier.absolutePadding(right = 2.dp))
            },
            yAxisTitle = {
                AxisTitle(
                    "Points",
                    modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        .padding(bottom = padding)
                )
            },
            verticalMajorGridLineStyle = null
        ) {
            VerticalBarChart(
                series = listOf(barChartEntries.value),
                bar = { _, _, value ->
                    DefaultVerticalBar(
                        brush = SolidColor(primaryEplContainer),
                        modifier = Modifier.fillMaxWidth(barWidth),
                    ) {
                        HoverSurface { Text(value.yMax.toString()) }
                    }
                }
            )
        }
    }
}

