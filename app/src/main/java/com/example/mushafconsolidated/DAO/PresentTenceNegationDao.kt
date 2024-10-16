package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.PresentTenceNegatonEnt

@Dao
interface PresentTenceNegationDao {
    @Query("SELECT * FROM presenttencenegation where surahid=:surah and ayahid=:ayah")
    fun getPresentTenceNegationFilterSurahAyah(surah: Int, ayah: Int): List<PresentTenceNegatonEnt>

    @Query("SELECT * FROM presenttencenegation ")
    fun getPresentTenceNegationAll(): List<PresentTenceNegatonEnt>

    @Query("SELECT * FROM presenttencenegation where surahid=:surah ")
    fun getPresentTenceNegationFilterSurah(surah: Int): List<PresentTenceNegatonEnt>
}