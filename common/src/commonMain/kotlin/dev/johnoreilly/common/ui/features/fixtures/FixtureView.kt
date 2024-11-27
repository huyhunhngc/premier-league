package dev.johnoreilly.common.ui.features.fixtures

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.johnoreilly.common.format
import dev.johnoreilly.common.model.GameFixture

@Composable
fun FixtureView(
    modifier: Modifier = Modifier,
    fixture: GameFixture,
    onFixtureSelected: (fixtureId: Int) -> Unit,
    isDataLoading: Boolean = false
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .clickable { onFixtureSelected(fixture.id) },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClubInFixtureView(
                    fixture.homeTeam,
                    fixture.homeTeamPhotoUrl
                )
                Text(
                    text = "${fixture.homeTeamScore ?: ""}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                HorizontalDivider(
                    modifier = Modifier
                        .heightIn(min = 20.dp, max = 30.dp)
                        .width(1.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = "${fixture.awayTeamScore ?: ""}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                ClubInFixtureView(
                    fixture.awayTeam,
                    fixture.awayTeamPhotoUrl
                )
            }
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = fixture.localKickoffTime.date.toString(),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )

            fixture.localKickoffTime.let { localKickoffTime ->
                val formattedTime = "%02d:%02d".format(localKickoffTime.hour, localKickoffTime.minute)
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = formattedTime,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ClubInFixtureView(
    teamName: String,
    teamPhotoUrl: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = teamPhotoUrl,
            contentDescription = teamName,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(60.dp)
        )
        Text(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 4.dp),
            text = teamName,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
