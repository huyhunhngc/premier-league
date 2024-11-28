package dev.johnoreilly.common.ui.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FixtureDetailDestination(
    @SerialName("fixtureId") val fixtureId: Int
)
