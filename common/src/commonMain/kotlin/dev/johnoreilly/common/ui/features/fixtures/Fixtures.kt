package dev.johnoreilly.common.ui.features.fixtures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.GameFixture

@Composable
fun Fixtures(
    fixtureItems: List<GameFixture>,
    columnNum: Int,
    onFixtureSelected: (fixtureId: Int) -> Unit,
    isLoading: Boolean
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
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