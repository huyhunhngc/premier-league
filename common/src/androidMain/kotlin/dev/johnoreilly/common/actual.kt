package dev.johnoreilly.common

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.johnoreilly.common.database.AppDatabase
import dev.johnoreilly.common.database.dbFileName
import dev.johnoreilly.common.database.migration1to2
import io.ktor.client.engine.android.*
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual fun platformModule() = module {
    single { Android.create() }
    single { dataStore(get()) }

    single<AppDatabase> { createRoomDatabase(get()) }
}

actual fun String.format(vararg args: Any?): String {
    return String.format(this, *args)
}

fun createRoomDatabase(ctx: Context): AppDatabase {
    val dbFile = ctx.getDatabasePath(dbFileName)
    return Room.databaseBuilder<AppDatabase>(ctx, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .addMigrations(migration1to2)
        .build()
}


fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = { context.filesDir.resolve("fpl.preferences_pb").absolutePath }
    )