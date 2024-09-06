package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.jsonsurahentity


//.lanelexicon


@Dao
interface JasonSurahDao {
    @Query("SELECT * FROM surahjson where surah=:sid")
    fun getSurahJson(sid: Int): List<jsonsurahentity>?

    @Query("SELECT * FROM surahjson ")
    fun getSurahJsonall(): List<jsonsurahentity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJsonsurah(entity: jsonsurahentity): Long // Return the row ID

}

