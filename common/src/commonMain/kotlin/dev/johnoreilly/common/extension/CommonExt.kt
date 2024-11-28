package dev.johnoreilly.common.extension

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> fromJson(jsonString: String?): T? {
    if (jsonString == null) return null
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
    return runCatching { json.decodeFromString<T>(jsonString) }.getOrNull()
}

inline fun <reified T> toJson(value: T): String {
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
    return json.encodeToString(value)
}

suspend inline fun <reified T> DataStore<Preferences>.setObject(
    key: Preferences.Key<String>, value: T
) {
    edit {
        it[key] = toJson(value)
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getObject(
    key: Preferences.Key<String>
): T? {
    val string = data.map { it[key] }.firstOrNull() ?: return null
    return fromJson(string)
}

suspend inline fun DataStore<Preferences>.setBoolean(
    key: Preferences.Key<Boolean>, value: Boolean
) {
    edit {
        it[key] = value
    }
}