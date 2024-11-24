package dev.johnoreilly.common.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val title: String) {
    data object PlayerListScreen : Screen("PlayerListScreen")
    data object PlayerSummaryScreen : Screen("PlayerSummaryScreen")
    data object PlayerDetailsScreen : Screen("PlayerDetailsScreen")
    data object FixtureListScreen : Screen("FixtureListScreen")
    data object FixtureDetailsScreen : Screen("FixtureDetailsScreen")
    data object LeagueStandingsListScreen : Screen("LeagueStandingsListScreen")
    data object SettingsScreen : Screen("SettingsScreen")
}

data class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Screen.PlayerListScreen.title,
        Icons.Default.Person,
        "Player"
    ),
    BottomNavigationItem(
        Screen.FixtureListScreen.title,
        Icons.Filled.DateRange,
        "Fixtures"
    ),
    BottomNavigationItem(
        Screen.LeagueStandingsListScreen.title,
        Icons.Filled.List,
        "League"
    )
)