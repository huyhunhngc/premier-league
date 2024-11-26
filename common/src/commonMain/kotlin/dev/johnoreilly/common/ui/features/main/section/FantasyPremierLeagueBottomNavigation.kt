package dev.johnoreilly.common.ui.features.main.section

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import dev.johnoreilly.common.ui.features.main.BottomNavigationItem

@Composable
fun FantasyPremierLeagueBottomNavigation(
    bottomNavigationItems: List<BottomNavigationItem>,
    onTabSelected: ((String) -> Unit),
    currentRoute: String?
) {
    NavigationBar {
        bottomNavigationItems.forEach { bottomNavigationItem ->
            NavigationBarItem(
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