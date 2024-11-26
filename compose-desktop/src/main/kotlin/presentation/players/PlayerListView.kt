package presentation.players

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.ui.theme.gradientGreenYellow

@Composable
fun PlayerListView(
    isExpanded: Boolean,
    playerList: List<Player>,
    selectedPlayer: Player?,
    onPlayerSelected: (player: Player) -> Unit
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
                    PlayerView(
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                if (player == selectedPlayer && isExpanded) {
                                    Brush.linearGradient(gradientGreenYellow)
                                } else {
                                    Brush.linearGradient(listOf(Color.LightGray, Color.White))
                                }
                            ),
                        player = player,
                        onPlayerSelected = onPlayerSelected
                    )
                }
            )
        }
    }
}

@Composable
fun PlayerView(
    modifier: Modifier,
    player: Player,
    onPlayerSelected: (player: Player) -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onPlayerSelected(player) })
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
            Text(
                player.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                player.team,
                style = MaterialTheme.typography.labelMedium,
                color = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            player.points.toString(),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )
    }
}
