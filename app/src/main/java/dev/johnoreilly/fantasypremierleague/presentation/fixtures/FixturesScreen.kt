@file:OptIn(ExperimentalMaterial3Api::class)

package dev.johnoreilly.fantasypremierleague.presentation.fixtures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.ui.features.fixtures.Fixtures
import dev.johnoreilly.common.ui.features.fixtures.GameweekChange
import dev.johnoreilly.common.ui.features.fixtures.GameweekSelector
import dev.johnoreilly.common.viewmodel.FixturesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FixturesScreen(
    padding: PaddingValues = PaddingValues(),
    onFixtureSelected: (fixtureId: Int) -> Unit,
) {
    val fixturesViewModel = koinViewModel<FixturesViewModel>()
    val fixturesState = fixturesViewModel.gameWeekFixtures.collectAsStateWithLifecycle()
    val currentGameweek = fixturesViewModel.currentGameweek.collectAsStateWithLifecycle()
    val windowSize = calculateWindowSizeClass()

    var selectedGameweek by remember(currentGameweek) { mutableIntStateOf(currentGameweek.value) }
    val isLoading by remember(fixturesState) {
        derivedStateOf { fixturesState.value[currentGameweek.value] == null }
    }
    val columnNum = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }
    val fixtureItems by remember(fixturesState, selectedGameweek) {
        derivedStateOf {
            if (isLoading) placeholderFixtureList else fixturesState.value[selectedGameweek].orEmpty()
        }
    }
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    GameweekSelector(
                        selectedGameweek = selectedGameweek,
                        onGameweekChanged = { gameweekChange ->
                            if (gameweekChange is GameweekChange.PastGameweek) {
                                selectedGameweek -= 1
                            } else {
                                selectedGameweek += 1
                            }
                        }
                    )
                }
            )
        }
    ) {
        Box(Modifier.padding(top = it.calculateTopPadding())) {
            Fixtures(
                fixtureItems = fixtureItems,
                columnNum = columnNum,
                onFixtureSelected = onFixtureSelected,
                isLoading = isLoading
            )
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