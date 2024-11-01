package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.IllaPositive

import com.example.mushafconsolidated.Entities.NegationEnt

@Dao
interface IllaPositiveDao {
    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah")
    fun getIllaPositiveFilterSurahAyah(surah: Int, ayah: Int): List<IllaPositive>



    @Query("SELECT * FROM illapositive ")
    fun getIllaPositiveAll(): List<IllaPositive>

    @Query("SELECT * FROM illapositive where surahid=:surah ")
    fun getIllaPositiveFilterSurah(surah: Int): List<IllaPositive>


}