package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.InMaIllaNegationEnt

@Dao
interface InMaIllaNegationDao {
    @Query("SELECT * FROM inmailla where surahid=:surah and ayahid=:ayah")
    fun getInMaIllaNegationFilterSurahAyah(surah: Int, ayah: Int): List<InMaIllaNegationEnt>

    @Query("SELECT * FROM inmailla ")
    fun getInMaIllaNegationAll(): List<InMaIllaNegationEnt>

    @Query("SELECT * FROM inmailla where surahid=:surah ")
    fun getInMaIllaNegationFilterSurah(surah: Int): List<InMaIllaNegationEnt>
}