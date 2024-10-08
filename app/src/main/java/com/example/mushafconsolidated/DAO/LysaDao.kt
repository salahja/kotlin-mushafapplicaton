package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.mushafconsolidated.Entities.lysaEnt


@Dao
interface LysaDao {
    @Query("SELECT * FROM lysa")
    fun getLysa(): List<lysaEnt>




}