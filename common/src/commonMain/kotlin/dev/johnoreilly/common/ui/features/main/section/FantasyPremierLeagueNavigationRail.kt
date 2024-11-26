package dev.johnoreilly.common.ui.features.main.section

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.johnoreilly.common.ui.features.main.NavigationItem

@Composable
fun FantasyPremierLeagueNavigationRail(
    navigationItems: List<NavigationItem>,
    onTabSelected: ((String) -> Unit),
    currentRoute: String?
) {
    NavigationRail {
        navigationItems.forEach { bottomNavigationItem ->
            NavigationRailItem(
                icon = {
                    Icon(
                        bottomNavigationItem.icon,
                        contentDescription = bottomNavigationItem.iconContentDescription
                    )
                },
                label = {
                    Text(text = bottomNavigationItem.iconContentDescription)
                },
                selected = currentRoute == bottomNavigationItem.route,
                onClick = {
                    onTabSelected(bottomNavigationItem.route)
                }
            )
        }
    }
}