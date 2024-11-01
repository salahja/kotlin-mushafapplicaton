package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.NewShartEntity


//.NewShartEntity


@Dao
interface NewShartDAO {
    @Query("SELECT * FROM newshart ORDER BY surah,ayah")
    fun shartAll(): List<NewShartEntity>

    @Query("SELECT * FROM newshart where surah=:id")
    fun getShartBySurah(id: Int): List<NewShartEntity>

    @Query("SELECT * FROM newshart where surah=:id and ayah=:ayah order by indexstart")
    fun getShartBySurahAyah(id: Int, ayah: Int): List<NewShartEntity>
}