package dev.johnoreilly.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.johnoreilly.common.database.AppDatabase
import dev.johnoreilly.common.database.dbFileName
import dev.johnoreilly.common.database.migration1to2
import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun platformModule() = module {
    single { Darwin.create() }
    single { dataStore() }

    single<AppDatabase> { createRoomDatabase() }
}

actual fun String.format(vararg args: Any?): String {
    // https://stackoverflow.com/questions/64495182/kotlin-native-ios-string-formatting-with-vararg
    var returnString = ""
    val regEx = "%[\\d|.]*[sdf]|%".toRegex()
    val singleFormats = regEx.findAll(this).map {
        it.groupValues.first()
    }.toList()
    val newStrings = this.split(regEx)
    for (i in 0 until args.count()) {
        val arg = args[i]
        returnString += when (arg) {
            is Double -> {
                NSString.stringWithFormat(newStrings[i] + singleFormats[i], args[i] as Double)
            }
            is Int -> {
                NSString.stringWithFormat(newStrings[i] + singleFormats[i], args[i] as Int)
            }
            else -> {
                NSString.stringWithFormat(newStrings[i] + "%@", args[i])
            }
        }
    }
    return returnString
}

fun createRoomDatabase(): AppDatabase {
    val dbFile = "${fileDirectory()}/$dbFileName"
    return Room.databaseBuilder<AppDatabase>(name = dbFile)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .addMigrations(migration1to2)
        .build()
}


private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory).path!!
}


fun dataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/fpl.preferences_pb"
    }
)
