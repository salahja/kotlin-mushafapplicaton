package database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.entity.MujarradVerbs
import database.entity.QuranVerbsEntity

@Dao
interface quranVerbDao {
    @Query(value = "SELECT * FROM quranverbs where root=:root")
    fun getqurnicVerb(root: String?): List<QuranVerbsEntity>



}