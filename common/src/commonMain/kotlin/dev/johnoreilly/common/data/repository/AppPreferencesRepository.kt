package dev.johnoreilly.common.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.johnoreilly.common.extension.fromJson
import dev.johnoreilly.common.extension.getObject
import dev.johnoreilly.common.extension.setObject
import dev.johnoreilly.common.model.AppPreferences
import dev.johnoreilly.common.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun saveTheme(theme: AppTheme) {
        val settings = getPreferences()
        savePreferences(settings.copy(theme = theme))
    }

    fun getTheme(): Flow<AppTheme> {
        return dataStore.data.map { preferences ->
            (fromJson<AppPreferences>(preferences[APP_PREFERENCES]) ?: AppPreferences()).theme
        }
    }

    private suspend fun getPreferences(): AppPreferences {
        return dataStore.getObject<AppPreferences>(APP_PREFERENCES) ?: AppPreferences()
    }

    private suspend fun savePreferences(preferences: AppPreferences) {
        dataStore.setObject(APP_PREFERENCES, preferences)
    }

    companion object {
        val APP_PREFERENCES = stringPreferencesKey("app_preferences")
    }
}