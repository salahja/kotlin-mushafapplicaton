package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query

import com.example.mushafconsolidated.Entities.NegationEnt

@Dao
interface NegationDao {
    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah")
    fun getNegationFilterSurahAyah(surah: Int, ayah: Int): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah and type=:type")
    fun getNegationFilterSurahAyahType(surah: Int, ayah: Int,type: String): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata ")
    fun getNegationAll(): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah ")
    fun getINegationFilterSurah(surah: Int): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah  and type=:type")
    fun getINegationFilterSurahAndType(surah: Int, type: String): List<NegationEnt>
}