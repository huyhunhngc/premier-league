package presentation.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.Player

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
            items(items = playerList, itemContent = { player ->
                PlayerView(player, selectedPlayer, playerSelected)
            })
        }
    }
}