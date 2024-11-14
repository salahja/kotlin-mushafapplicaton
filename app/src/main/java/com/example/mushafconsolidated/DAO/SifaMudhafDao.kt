package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query

import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.SifaMudhafEnt

@Dao
interface SifaMudhafDao {
    @Query("SELECT * FROM newmudhaf where surah=:surah and ayah=:ayah")
    fun getSIfaMudhaafFilterSurahAyah(surah: Int, ayah: Int): List<SifaMudhafEnt>

    @Query("SELECT * FROM newmudhaf where surah=:surah and ayah=:ayah and comment=:type")
    fun getSIfaMudhaafFilterSurahAyahType(surah: Int, ayah: Int,type: String): List<SifaMudhafEnt>

    @Query("SELECT * FROM newmudhaf ")
    fun getSIfaMudhaafAll(): List<SifaMudhafEnt>

    @Query("SELECT * FROM newmudhaf where surah=:surah ")
    fun getISIfaMudhaafFilterSurah(surah: Int): List<SifaMudhafEnt>

    @Query("SELECT * FROM newmudhaf where surah=:surah  and comment=:type")
    fun getISIfaMudhaafFilterSurahAndType(surah: Int, type: String): List<SifaMudhafEnt>
}