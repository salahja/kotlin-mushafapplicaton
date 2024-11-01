package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.MutlaqBadalaAlihiTameezEnt

import com.example.mushafconsolidated.Entities.NegationEnt

@Dao
interface MutlaqBadalaAlihiTameezDao {
    @Query("SELECT * FROM mutlaqbadalajlihitameez where surahid=:surah and ayahid=:ayah")
    fun getMBATFilterSurahAyah(surah: Int, ayah: Int): List<MutlaqBadalaAlihiTameezEnt>

    @Query("SELECT * FROM mutlaqbadalajlihitameez where surahid=:surah and ayahid=:ayah and type=:type")
    fun getMBATFilterSurahAyahType(surah: Int, ayah: Int,type: String): List<MutlaqBadalaAlihiTameezEnt>

    @Query("SELECT * FROM mutlaqbadalajlihitameez ")
    fun getMBATAll(): List<MutlaqBadalaAlihiTameezEnt>

    @Query("SELECT * FROM mutlaqbadalajlihitameez where surahid=:surah ")
    fun getIMBATFilterSurah(surah: Int): List<MutlaqBadalaAlihiTameezEnt>

    @Query("SELECT * FROM mutlaqbadalajlihitameez where surahid=:surah  and type=:type")
    fun getIMBATFilterSurahAndType(surah: Int, type: String): List<MutlaqBadalaAlihiTameezEnt>
}