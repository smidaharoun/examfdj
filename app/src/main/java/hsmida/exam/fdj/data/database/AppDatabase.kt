package hsmida.exam.fdj.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hsmida.exam.fdj.model.League

@Database(entities = [League::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun leagueDao(): LeagueDao
}