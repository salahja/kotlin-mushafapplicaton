package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.PastTenceNegatonEnt

@Dao
interface PastTenceNegatonDao {
    @Query("SELECT * FROM lammudharynegation where surahid=:surah and ayahid=:ayah")
    fun getLamMudharyNegationFilterSurahAyah(surah: Int, ayah: Int): List<PastTenceNegatonEnt>

    @Query("SELECT * FROM lammudharynegation ")
    fun getLamMudharyNegationAll(): List<PastTenceNegatonEnt>

    @Query("SELECT * FROM lammudharynegation where surahid=:surah ")
    fun getLamMudharyNegationFilterSurah(surah: Int): List<PastTenceNegatonEnt>
}