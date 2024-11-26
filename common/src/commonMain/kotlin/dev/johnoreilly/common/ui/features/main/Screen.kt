package dev.johnoreilly.common.ui.features.main

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val title: String) {
    data object MainScreen : Screen("MainScreen")
    data object PlayerListScreen : Screen("PlayerListScreen")
    data object PlayerSummaryScreen : Screen("PlayerSummaryScreen")
    data object PlayerDetailsScreen : Screen("PlayerDetailsScreen")
    data object FixtureListScreen : Screen("FixtureListScreen")
    data object FixtureDetailsScreen : Screen("FixtureDetailsScreen")
    data object LeagueStandingsListScreen : Screen("LeagueStandingsListScreen")
    data object SettingsScreen : Screen("SettingsScreen")
}

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

enum class NavigationType {
    BottomNavigation,
    NavigationRail,
}
