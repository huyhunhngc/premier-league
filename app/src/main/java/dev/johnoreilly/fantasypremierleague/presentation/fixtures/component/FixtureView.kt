package dev.johnoreilly.fantasypremierleague.presentation.fixtures.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.ui.features.fixtures.FixtureView

@Preview
@Composable
fun PreviewFixtureView() {
    val placeholderKickoffTime = kotlinx.datetime.LocalDateTime(2022, 9, 5, 13, 30, 0)
    Column(modifier = Modifier.height(200.dp)) {
        FixtureView(
            fixture = GameFixture(
                id = 1,
                localKickoffTime = placeholderKickoffTime,
                homeTeam = "Liverpool",
                "Spurs",
                "",
                "",
                3,
                0,
                5
            ),
            onFixtureSelected = {},
            isDataLoading = false
        )
    }
}