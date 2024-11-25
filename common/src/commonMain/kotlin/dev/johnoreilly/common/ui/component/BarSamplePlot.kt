package dev.johnoreilly.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.PlayerPastHistory
import dev.johnoreilly.common.ui.theme.primaryEplContainer
import io.github.koalaplot.core.ChartLayout
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

internal val padding = 8.dp
internal const val barWidth = 0.8f

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun BarSamplePlot(
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
                    modifier = Modifier
                        .rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
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

data class TickPositionState(
    val verticalAxis: TickPosition,
    val horizontalAxis: TickPosition
)

private fun barChartEntries(playerHistory: List<PlayerPastHistory>) = playerHistory.map { player ->
    DefaultBarChartEntry(
        xValue = player.seasonName.takeLast(2),
        yMin = 0f,
        yMax = player.totalPoints.toFloat(),
    )
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