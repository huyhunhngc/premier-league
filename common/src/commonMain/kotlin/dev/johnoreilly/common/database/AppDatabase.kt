package dev.johnoreilly.common.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import dev.johnoreilly.common.model.GameFixture
import dev.johnoreilly.common.model.Player
import dev.johnoreilly.common.model.Team
import kotlinx.datetime.LocalDateTime

internal expect object AppDatabaseCtor : RoomDatabaseConstructor<AppDatabase>

@Database(entities = [Team::class, Player::class, GameFixture::class], version = 2)
@ConstructedBy(AppDatabaseCtor::class)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fantasyPremierLeagueDao(): FantasyPremierLeagueDao
}

val migration1to2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        super.migrate(connection)
        connection.execSQL("ALTER TABLE Player ADD COLUMN teamPhotoUrl TEXT NOT NULL DEFAULT ''")
    }
}

internal const val dbFileName = "fantasypremierleague.db"


class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
