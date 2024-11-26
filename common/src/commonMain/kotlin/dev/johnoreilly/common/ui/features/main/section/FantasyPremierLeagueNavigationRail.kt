package dev.johnoreilly.common.ui.features.main.section

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import dev.johnoreilly.common.ui.features.main.BottomNavigationItem

@Composable
fun FantasyPremierLeagueNavigationRail(
    bottomNavigationItems: List<BottomNavigationItem>,
    onTabSelected: ((String) -> Unit),
    currentRoute: String?
) {
    NavigationRail {
        bottomNavigationItems.forEach { bottomNavigationItem ->
            NavigationRailItem(
                icon = {
                    Icon(
                        bottomNavigationItem.icon,
                        contentDescription = bottomNavigationItem.iconContentDescription
                    )
                },
                selected = currentRoute == bottomNavigationItem.route,
                onClick = {
                    onTabSelected(bottomNavigationItem.route)
                }
            )
        }
    }
}