package presentation.players

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.ui.theme.gradientGreenYellow

@Composable
fun PlayerListView(
    playerList: List<Player>,
    selectedPlayer: Player?,
    playerSelected: (player: Player) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(3.dp)
            .background(color = Color.White)
            .clip(shape = RoundedCornerShape(3.dp))
    ) {
        LazyColumn {
            items(
                items = playerList,
                itemContent = { player ->
                    PlayerView(player, selectedPlayer, playerSelected)
                }
            )
        }
    }
}

@Composable
fun PlayerView(
    player: Player,
    selectedPlayer: Player?,
    playerSelected: (player: Player) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (player == selectedPlayer) Brush.linearGradient(
                    colors = gradientGreenYellow
                ) else Brush.linearGradient(
                    colors = listOf(Color.LightGray, Color.White)
                ),
            )
            .clickable(onClick = { playerSelected(player) })
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = rememberImagePainter(player.teamPhotoUrl)
        Image(
            painter,
            null,
            modifier = Modifier.size(42.dp),
            contentScale = ContentScale.Fit,
        )
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(player.name, style = MaterialTheme.typography.titleMedium)
            Row {
                Text(
                    player.team,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.DarkGray
                )
                Text(
                    " • ",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.DarkGray
                )
                Text(
                    player.points.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}
