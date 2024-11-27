package dev.johnoreilly.common.ui.features.main.section

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.johnoreilly.common.ui.features.main.NavigationItem

@Composable
fun FantasyPremierLeagueNavigationRail(
    navigationItems: List<NavigationItem>,
    onTabSelected: ((String) -> Unit),
    currentRoute: String?,
    content: (@Composable ColumnScope.() -> Unit)? = null
) {
    NavigationRail {
        Spacer(modifier = Modifier.height(16.dp))
        navigationItems.forEach { item ->
            NavigationRailItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.iconContentDescription
                    )
                },
                label = {
                    Text(text = item.iconContentDescription, fontWeight = FontWeight.Bold)
                },
                selected = currentRoute == item.route,
                onClick = {
                    onTabSelected(item.route)
                }
            )
        }
        if (content != null) {
            content()
        }
    }
}