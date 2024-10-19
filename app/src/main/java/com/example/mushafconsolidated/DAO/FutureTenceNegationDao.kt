package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.FutureTenceNegatonEnt
import com.example.mushafconsolidated.Entities.PastTenceNegatonEnt

@Dao
interface FutureTenceNegationDao {
    @Query("SELECT * FROM futuretencenegation where surahid=:surah and ayahid=:ayah")
    fun getFuturTenceNegationFilterSurahAyah(surah: Int, ayah: Int): List<FutureTenceNegatonEnt>

    @Query("SELECT * FROM futuretencenegation ")
    fun getFuturTenceNegationAll(): List<FutureTenceNegatonEnt>

    @Query("SELECT * FROM futuretencenegation where surahid=:surah ")
    fun getFuturTenceNegationFilterSurah(surah: Int): List<FutureTenceNegatonEnt>
}