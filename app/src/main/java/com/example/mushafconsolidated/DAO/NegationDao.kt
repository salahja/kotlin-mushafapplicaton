package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.InMaIllaNegationEnt
import com.example.mushafconsolidated.Entities.NegationEnt

@Dao
interface NegationDao {
    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah")
    fun getNegationFilterSurahAyah(surah: Int, ayah: Int): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata ")
    fun getNegationAll(): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah ")
    fun getINegationFilterSurah(surah: Int): List<NegationEnt>
}