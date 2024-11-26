package dev.johnoreilly.common.ui.features.main.section

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.johnoreilly.common.ui.features.main.NavigationItem

@Composable
fun FantasyPremierLeagueBottomNavigation(
    bottomNavigationItems: List<NavigationItem>,
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