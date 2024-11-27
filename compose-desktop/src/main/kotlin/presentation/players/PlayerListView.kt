package presentation.players

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.ui.extension.gradientBackground

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun PlayerListView(
    isExpanded: Boolean,
    playerList: List<Player>,
    selectedPlayer: Player?,
    onPlayerSelected: (player: Player) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = Color.White)
            .clip(shape = RoundedCornerShape(3.dp))
    ) {
        val windowSize = calculateWindowSizeClass()
        val columnNum = when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> 2
            else -> 1
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(playerList.chunked(columnNum)) { players ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    players.forEach { player ->
                        PlayerView(
                            modifier = Modifier.weight(1f),
                            isSelected = selectedPlayer?.id == player.id && isExpanded,
                            player = player,
                            onPlayerSelected = onPlayerSelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerView(
    modifier: Modifier,
    isSelected: Boolean = false,
    player: Player,
    onPlayerSelected: (player: Player) -> Unit
) {
    Row(
        modifier = modifier
            .height(85.dp)
            .clip(RoundedCornerShape(16.dp))
            .let {
                if (isSelected) {
                    it.gradientBackground()
                } else {
                    it.background(MaterialTheme.colorScheme.primaryContainer)
                }
            }
            .clickable(onClick = { onPlayerSelected(player) })
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
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
