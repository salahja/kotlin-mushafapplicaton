package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.AbsoluteNegationEnt


@Dao
interface AbsoluteNegationDao {
    @Query("SELECT * FROM absolute_negation where surahid=:surah and ayahid=:ayah")
    fun getAbsoluteNegationFilterSurahAyah(surah: Int, ayah: Int): List<AbsoluteNegationEnt>

    @Query("SELECT * FROM absolute_negation ")
    fun getAbsoluteNegationAll(): List<AbsoluteNegationEnt>

    @Query("SELECT * FROM absolute_negation where surahid=:surah ")
    fun getAbsoluteNegationFilterSurah(surah: Int): List<AbsoluteNegationEnt>
}