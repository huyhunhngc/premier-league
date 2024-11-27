package dev.johnoreilly.common.ui.extension

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import dev.johnoreilly.common.ui.theme.gradientDefault
import fantasypremierleague.common.generated.resources.Res
import fantasypremierleague.common.generated.resources.background_selected_item
import org.jetbrains.compose.resources.painterResource

@Composable
fun Modifier.gradientBackground() = this.then(
    background(Brush.linearGradient(gradientDefault))
        .paint(
            painter = painterResource(Res.drawable.background_selected_item),
            contentScale = ContentScale.FillBounds
        ).clipToBounds()
)

