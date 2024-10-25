package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.SifaEntity


//.SifaEntity


@Dao
interface SifaDao {
    @Query("SELECT * FROM sifa order by surah,ayah ")
    fun getSifaAll(): List<SifaEntity>

    @Query("SELECT * FROM sifa where surah=:id order by ayah,wordfrom ")
    fun getSifaindexesBySurah(id: Int): List<SifaEntity>

    @Query("SELECT * FROM sifa where surah=:id and ayah=:ayd order by surah,ayah,wordfrom ")
    fun getSifaindexesBySurahAyah(id: Int, ayd: Int): List<SifaEntity>
}