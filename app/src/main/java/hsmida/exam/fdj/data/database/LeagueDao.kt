package hsmida.exam.fdj.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hsmida.exam.fdj.model.League

@Dao
interface LeagueDao {

    @Query("SELECT * FROM League ORDER BY idLeague")
    fun getAll(): List<League>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(leagues: List<League>)

}