package dev.johnoreilly.common.model

import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val theme: AppTheme = AppTheme.SYSTEM,
)

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM;
}