package presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import io.github.ifa.compose_windown_title.HitSpots
import io.github.ifa.compose_windown_title.icons.Close
import io.github.ifa.compose_windown_title.icons.Floating
import io.github.ifa.compose_windown_title.icons.Maximize
import io.github.ifa.compose_windown_title.icons.Minimize
import io.github.ifa.compose_windown_title.windowFrameItem

@Composable
fun SystemButton(
    onClick: () -> Unit,
    background: Color = Color.Transparent,
    onBackground: Color = LocalContentColor.current,
    hoveredBackgroundColor: Color = background,
    onHoveredBackgroundColor: Color = LocalContentColor.current,
    icon: Painter,
    modifier: Modifier = Modifier,
) {
    val isFocused = isWindowFocused()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Icon(
        painter = icon,
        contentDescription = null,
        tint = animateColorAsState(
            when {
                isHovered -> onHoveredBackgroundColor
                else -> onBackground
            }.copy(
                alpha = if (isFocused || isHovered) {
                    1f
                } else {
                    0.25f
                }
            )
        ).value,
        modifier = modifier
            .clickable { onClick() }
            .background(
                animateColorAsState(
                    when {
                        isHovered -> hoveredBackgroundColor
                        else -> background
                    }
                ).value
            )
            .hoverable(interactionSource)
            .windowButton()
    )
}


@Composable
fun CloseButton(
    onRequestClose: () -> Unit,
    modifier: Modifier,
) {
    SystemButton(
        onRequestClose,
        background = Color.Transparent,
        onBackground = MaterialTheme.colorScheme.onBackground,
        hoveredBackgroundColor = Color(0xFFc42b1c),
        onHoveredBackgroundColor = Color.White,
        icon = rememberVectorPainter(Close),
        modifier=modifier,
    )
}

private fun Modifier.windowButton(): Modifier {
    return padding(
        vertical = 10.dp, horizontal = 20.dp
    ).size(8.dp)
}

@Composable
fun FrameWindowScope.WindowsActionButtons(
    onRequestClose: () -> Unit,
    onRequestMinimize: (() -> Unit)?,
    onToggleMaximize: (() -> Unit)?,
) {
    Row(
        // Toolbar is aligned center vertically so I fill that and place it on top
        modifier = Modifier,
        verticalAlignment = Alignment.Top
    ) {
        onRequestMinimize?.let {
            SystemButton(
                icon = rememberVectorPainter(Minimize),
                onClick = onRequestMinimize,
                modifier = Modifier.windowFrameItem("minimize", HitSpots.MINIMIZE_BUTTON)
            )
        }

        onToggleMaximize?.let {
            SystemButton(
                icon = rememberVectorPainter(
                    if (isWindowMaximized()) {
                        Floating
                    } else {
                        Maximize
                    }
                ),
                onClick = onToggleMaximize,
                modifier = Modifier.windowFrameItem("maximize", HitSpots.MAXIMIZE_BUTTON)
            )
        }

        CloseButton(
            onRequestClose=onRequestClose,
            modifier = Modifier.windowFrameItem("close", HitSpots.CLOSE_BUTTON)
        )
    }
}