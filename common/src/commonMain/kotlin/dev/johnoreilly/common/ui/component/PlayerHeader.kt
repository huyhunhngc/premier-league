package dev.johnoreilly.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.ui.ext.extractTeamCode
import fantasypremierleague.common.generated.resources.Res
import fantasypremierleague.common.generated.resources.background_header
import fantasypremierleague.common.generated.resources.background_header_mobile
import fantasypremierleague.common.generated.resources.img_player_missing
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayerHeader(windowSizeClass: WindowSizeClass, player: Player) {
    val (firstname, lastname) = remember(player.name) {
        player.name.split(" ").run { firstOrNull().orEmpty() to lastOrNull().orEmpty() }
    }
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val colorHeader = player.extractTeamCode()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(colorHeader.second))
            .paint(
                painterResource(
                    if (isExpanded) Res.drawable.background_header
                    else Res.drawable.background_header_mobile
                ),
                colorFilter = ColorFilter.tint(Color(colorHeader.first).copy(alpha = 0.4f)),
                contentScale = ContentScale.FillBounds
            )
    ) {
        val imageSize = if (isExpanded) 180.dp else 150.dp
        val alignment = if (isExpanded) Alignment.BottomStart else Alignment.BottomEnd
        Image(
            painter = rememberImagePainter(
                player.photoUrl,
                placeholderPainter = { painterResource(Res.drawable.img_player_missing) },
            ),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .align(alignment),
            contentScale = ContentScale.Fit,
        )
        Image(
            painter = rememberImagePainter(player.teamPhotoUrl),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .size(60.dp)
                .background(Color.White, CircleShape)
                .padding(4.dp)
                .align(if (isExpanded) Alignment.BottomEnd else Alignment.BottomStart),
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier.align(
                if (isExpanded) Alignment.TopCenter else Alignment.TopStart
            ).padding(
                if (isExpanded) PaddingValues(16.dp) else PaddingValues(
                    start = 16.dp,
                    top = 42.dp
                )
            )
        ) {
            Text(
                text = firstname,
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.bodyMedium
                    .copy(fontSize = if (isExpanded) 36.sp else 18.sp)
            )
            Text(
                text = lastname,
                fontWeight = FontWeight.Bold,
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.titleMedium
                    .copy(fontSize = if (isExpanded) 40.sp else 20.sp)
            )
        }
    }
}