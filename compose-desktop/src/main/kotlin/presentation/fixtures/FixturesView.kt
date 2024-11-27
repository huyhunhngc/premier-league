package presentation.fixtures

import LocalKoin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.ui.features.fixtures.FixtureView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
fun FixturesView(
    padding: PaddingValues = PaddingValues(),
    windowSize: WindowSizeClass,
    onFixtureSelected: (fixtureId: Int) -> Unit
) {
    val repository = LocalKoin.current.get<FantasyPremierLeagueRepository>()
    val fixturesState by rememberUpdatedState(
        repository.getFixtures().groupByEvent().collectAsState(initial = emptyMap())
    )
    val currentGameweek = repository.currentGameweek.collectAsState()

    val selectedGameweek = remember { mutableIntStateOf(currentGameweek.value) }
    val isLoading = fixturesState.value[currentGameweek.value] == null

    val columnNum = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }
    Column(modifier = Modifier.padding(padding)) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp).background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
        ) {
            val fixtureItems: List<GameFixture> = if (isLoading) {
                emptyList()
            } else {
                fixturesState.value[selectedGameweek.intValue] ?: emptyList()
            }
            items(fixtureItems.chunked(columnNum)) { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    rowItems.forEach { fixture ->
                        FixtureView(
                            modifier = Modifier.weight(1f),
                            fixture = fixture,
                            onFixtureSelected = onFixtureSelected,
                            isDataLoading = isLoading
                        )
                    }
                    repeat(columnNum - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

fun Flow<List<GameFixture>>.groupByEvent() = map { it.groupBy { it.event } }