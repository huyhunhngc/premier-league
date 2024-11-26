@file:OptIn(ExperimentalMaterial3Api::class)

package dev.johnoreilly.fantasypremierleague.presentation.fixtures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.placeholder
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.viewmodel.FixturesViewModel
import dev.johnoreilly.fantasypremierleague.presentation.fixtures.component.FixtureView
import dev.johnoreilly.fantasypremierleague.presentation.global.lowfidelitygray
import dev.johnoreilly.fantasypremierleague.presentation.global.maroon200
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
                        isDataLoading = isLoading,
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
        Box(Modifier.padding(top = it.calculateTopPadding())) {
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

@Composable
fun GameweekSelector(
    selectedGameweek: Int,
    onGameweekChanged: (gameweekChange: GameweekChange) -> Unit,
    isDataLoading: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .placeholder(visible = isDataLoading, lowfidelitygray),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (selectedGameweek > 1) {
            IconButton(
                modifier = Modifier
                    .width(30.dp),
                onClick = { onGameweekChanged(GameweekChange.PastGameweek) }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back arrow",
                    tint = maroon200
                )
            }
        } else {
            Spacer(modifier = Modifier.width(30.dp))
        }

        Text(
            text = "Gameweek $selectedGameweek",
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )

        if (selectedGameweek < 38) {
            IconButton(
                modifier = Modifier
                    .width(30.dp),
                onClick = { onGameweekChanged(GameweekChange.NextGameweek) }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Forward arrow",
                    tint = maroon200
                )
            }
        } else {
            Spacer(modifier = Modifier.width(30.dp))
        }
    }
}

sealed class GameweekChange {
    data object NextGameweek : GameweekChange()
    data object PastGameweek : GameweekChange()
}

private val placeholderKickoffTime = kotlinx.datetime.LocalDateTime(2022, 9, 5, 13, 30, 0)
private val placeholderFixtureList = listOf(
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
    ),
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
    ),
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
    ),
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
    ),
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
)