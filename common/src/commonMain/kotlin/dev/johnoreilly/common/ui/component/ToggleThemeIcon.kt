package dev.johnoreilly.common.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.model.AppTheme
import fantasypremierleague.common.generated.resources.Res
import fantasypremierleague.common.generated.resources.ic_dark_mode
import fantasypremierleague.common.generated.resources.ic_light_mode
import org.jetbrains.compose.resources.painterResource

@Composable
fun ToggleThemeIcon(
    currentTheme: AppTheme,
    isSystemInDarkTheme: Boolean,
    toggleTheme: (AppTheme) -> Unit
) {
    val isDarkTheme = currentTheme == AppTheme.DARK || (currentTheme == AppTheme.SYSTEM && isSystemInDarkTheme)

    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(CircleShape)
            .clickable { toggleTheme(if (isDarkTheme) AppTheme.LIGHT else AppTheme.DARK) }
            .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            .padding(10.dp)
    ) {
        AnimatedContent(targetState = isDarkTheme) { isDark ->
            Icon(
                painter = if (isDark) {
                    painterResource(Res.drawable.ic_dark_mode)
                } else {
                    painterResource(Res.drawable.ic_light_mode)
                },
                contentDescription = "Toggle Theme",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}