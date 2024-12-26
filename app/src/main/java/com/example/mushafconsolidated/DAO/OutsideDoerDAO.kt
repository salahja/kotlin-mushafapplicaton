package com.example.mushafconsolidated.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.OutSideDoer


@Dao
interface OutsideDoerDAO {
    @Query("SELECT * FROM outsidedoer ORDER BY surah")
    fun outsidedoerlist(): List<OutSideDoer>

    @Query("SELECT * FROM outsidedoer where surah = :cid ORDER BY surah")
    fun doerlistSurah(cid:Int): List<OutSideDoer>
}

