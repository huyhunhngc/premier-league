package dev.johnoreilly.common

import androidx.datastore.core.DataStore
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import dev.johnoreilly.common.database.AppDatabase
import dev.johnoreilly.common.database.dbFileName
import dev.johnoreilly.common.database.migration1to2
import io.ktor.client.engine.java.*
import org.koin.dsl.module
import java.io.File

actual fun platformModule() = module {
    single { Java.create() }
    single { dataStore()}
    single<AppDatabase> { createRoomDatabase() }
}

actual fun String.format(vararg args: Any?): String {
    return String.format(this, *args)
}

fun createRoomDatabase(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbFileName)
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath,)
        .setDriver(BundledSQLiteDriver())
        .addMigrations(migration1to2)
        .build()
}

fun dataStore(): DataStore<androidx.datastore.preferences.core.Preferences> =
    createDataStore(
        producePath = { "fpl.preferences_pb" }
    )