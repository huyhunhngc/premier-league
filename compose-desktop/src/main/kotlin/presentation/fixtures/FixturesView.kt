package presentation.fixtures

import LocalKoin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.ui.features.fixtures.Fixtures
import dev.johnoreilly.common.ui.features.fixtures.GameweekChange
import dev.johnoreilly.common.ui.features.fixtures.GameweekSelector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
fun FixturesView(
    padding: PaddingValues = PaddingValues(),
    windowSize: WindowSizeClass,
    onFixtureSelected: (fixtureId: Int) -> Unit,
    fantasyPLRepository: FantasyPremierLeagueRepository = localFantasyPremierLeagueRepository()
) {
    val fixturesState by rememberUpdatedState(
        fantasyPLRepository.getFixtures().groupByEvent().collectAsState(emptyMap())
    )
    val currentGameweek = fantasyPLRepository.currentGameweek.collectAsState()

    var selectedGameweek by remember { mutableStateOf(currentGameweek.value) }
    val isLoading by remember {
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
            if (isLoading) emptyList() else fixturesState.value[selectedGameweek].orEmpty()
        }
    }
    Column(modifier = Modifier.padding(padding)) {
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

        Fixtures(
            fixtureItems = fixtureItems,
            columnNum = columnNum,
            onFixtureSelected = onFixtureSelected,
            isLoading = isLoading
        )
    }
}

fun Flow<List<GameFixture>>.groupByEvent() = map { it.groupBy { it.event } }

@Composable
fun localFantasyPremierLeagueRepository(): FantasyPremierLeagueRepository {
    return LocalKoin.current.get<FantasyPremierLeagueRepository>()
}