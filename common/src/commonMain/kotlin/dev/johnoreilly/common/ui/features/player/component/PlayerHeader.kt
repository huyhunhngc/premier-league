package dev.johnoreilly.common.ui.features.player.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import dev.johnoreilly.common.ui.extension.extractTeamCode
import fantasypremierleague.common.generated.resources.Res
import fantasypremierleague.common.generated.resources.background_header
import fantasypremierleague.common.generated.resources.background_header_mobile
import fantasypremierleague.common.generated.resources.img_player_missing
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    windowSize: WindowSizeClass,
    player: Player
) {
    val isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
    if (isExpanded) {
        HeaderExpanded(modifier, player)
    } else {
        HeaderMobile(modifier, player)
    }
}

@Composable
fun HeaderMobile(modifier: Modifier, player: Player) {
    val (firstname, lastname) = remember(player.name) {
        player.name.split(" ").run { firstOrNull().orEmpty() to lastOrNull().orEmpty() }
    }
    val colorHeader = player.extractTeamCode()

    Box(
        modifier = modifier
            .background(Color(colorHeader.second))
            .paint(
                painterResource(Res.drawable.background_header_mobile),
                colorFilter = ColorFilter.tint(Color(colorHeader.first).copy(alpha = 0.4f)),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Image(
            painter = rememberImagePainter(
                player.photoUrl,
                placeholderPainter = { painterResource(Res.drawable.img_player_missing) },
                errorPainter = { painterResource(Res.drawable.img_player_missing) }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd),
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
                .align(Alignment.BottomStart),
            contentScale = ContentScale.Fit,
        )
        Text(
            text = player.points.toString(),
            modifier = Modifier.align(Alignment.Center),
            color = Color(colorHeader.third),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold)
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 42.dp)
        ) {
            Text(
                text = firstname,
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
            Text(
                text = lastname,
                fontWeight = FontWeight.Bold,
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
            )
        }
    }
}

@Composable
fun HeaderExpanded(modifier: Modifier, player: Player) {
    val (firstname, lastname) = remember(player.name) {
        player.name.split(" ").run { firstOrNull().orEmpty() to lastOrNull().orEmpty() }
    }
    val colorHeader = player.extractTeamCode()
    Box(
        modifier = modifier
            .background(Color(colorHeader.second))
            .paint(
                painterResource(Res.drawable.background_header),
                colorFilter = ColorFilter.tint(Color(colorHeader.first).copy(alpha = 0.4f)),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Image(
            painter = rememberImagePainter(
                player.photoUrl,
                placeholderPainter = { painterResource(Res.drawable.img_player_missing) },
                errorPainter = { painterResource(Res.drawable.img_player_missing) }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart),
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
                .align(Alignment.BottomEnd),
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Text(
                text = firstname,
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 36.sp)
            )
            Text(
                text = lastname,
                fontWeight = FontWeight.Bold,
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 40.sp)
            )
            Text(
                text = player.points.toString(),
                color = Color(colorHeader.third),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
