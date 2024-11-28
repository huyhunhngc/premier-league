@file:OptIn(ExperimentalMaterial3Api::class)

package dev.johnoreilly.fantasypremierleague.presentation.fixtures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.ui.features.fixtures.FixtureView
import dev.johnoreilly.common.ui.features.fixtures.GameweekChange
import dev.johnoreilly.common.ui.features.fixtures.GameweekSelector
import dev.johnoreilly.common.viewmodel.FixturesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FixturesScreen(
    padding: PaddingValues = PaddingValues(),
    onFixtureSelected: (fixtureId: Int) -> Unit,
) {
    val fixturesViewModel = koinViewModel<FixturesViewModel>()

    val fixturesState = fixturesViewModel.gameWeekFixtures.collectAsStateWithLifecycle()
    val currentGameweek: State<Int> =
        fixturesViewModel.currentGameweek.collectAsStateWithLifecycle()
    val selectedGameweek = remember { mutableIntStateOf(currentGameweek.value) }
    val isLoading = fixturesState.value[currentGameweek.value] == null
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    GameweekSelector(
                        selectedGameweek = selectedGameweek.intValue,
                        onGameweekChanged = { gameweekChange ->
                            if (gameweekChange is GameweekChange.PastGameweek) {
                                selectedGameweek.intValue -= 1
                            } else {
                                selectedGameweek.intValue += 1
                            }
                        }
                    )
                }
            )
        }
    ) {
        Box(Modifier.padding(top = it.calculateTopPadding(), start = 16.dp, end = 16.dp)) {
            LazyColumn {
                val fixtureItems: List<GameFixture> = if (isLoading) placeholderFixtureList
                else fixturesState.value[selectedGameweek.intValue] ?: emptyList()
                items(
                    items = fixtureItems,
                    itemContent = { fixture ->
                        FixtureView(
                            fixture = fixture,
                            onFixtureSelected = onFixtureSelected,
                            isDataLoading = isLoading
                        )
                    }
                )
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private val placeholderKickoffTime = kotlinx.datetime.LocalDateTime(2022, 9, 5, 13, 30, 0)
private val placeholderFixtureList = List(10) {
    GameFixture(
        id = 1,
        localKickoffTime = placeholderKickoffTime,
        homeTeam = "Liverpool",
        awayTeam = "Manchester United",
        homeTeamPhotoUrl = "",
        awayTeamPhotoUrl = "",
        homeTeamScore = null,
        awayTeamScore = null,
        event = 0
    )
}