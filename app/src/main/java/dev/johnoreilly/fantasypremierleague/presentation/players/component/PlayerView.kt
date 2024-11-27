package dev.johnoreilly.fantasypremierleague.presentation.players.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.placeholder.placeholder
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.fantasypremierleague.presentation.global.lowfidelitygray

@Composable
fun PlayerView(
    player: Player,
    onPlayerSelected: (player: Player) -> Unit,
    isDataLoading: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onPlayerSelected(player) }
    ) {
        AsyncImage(
            model = player.teamPhotoUrl,
            contentDescription = player.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(50.dp)
                .placeholder(visible = isDataLoading, lowfidelitygray)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                modifier = Modifier.placeholder(visible = isDataLoading, lowfidelitygray),
                text = player.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.size(1.dp))
            Text(
                modifier = Modifier.placeholder(visible = isDataLoading, lowfidelitygray),
                text = player.team,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Text(
            modifier = Modifier.placeholder(visible = isDataLoading, lowfidelitygray),
            text = player.points.toString(),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}