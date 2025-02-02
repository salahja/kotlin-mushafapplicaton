package com.example.mushafconsolidated.DAO

import androidx.room.Dao
import androidx.room.Query

import com.example.mushafconsolidated.Entities.NegationEnt

@Dao
interface NegationDao {
    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah")
    fun getNegationFilterSurahAyah(surah: Int, ayah: Int): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah  and type LIKE :type || '%'")
    fun getNegationFilterSurahAyahType(surah: Int, ayah: Int,type: String): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata order by surahid,ayahid")
    fun getNegationAll(): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah ")
    fun getINegationFilterSurah(surah: Int): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where surahid=:surah  and translation=:type")
    fun getINegationFilterSurahAndType(surah: Int, type: String): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata where    translation=:type")
    fun getNegationFilterSurahAndTypetemp(  type: String): List<NegationEnt>



    @Query("SELECT * FROM negationfulldata where surahid=:surah  and type=:type")
    fun getINasabFilterSurahAndType(surah: Int, type: String): List<NegationEnt>
    @Query("SELECT * FROM negationfulldata where surahid=:surah and ayahid=:ayah  and type=:type")
    fun getNasabFilterSurahAyahType(surah: Int, ayah: Int,type: String): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata WHERE surahid = :surah AND ayahid = :ayah AND type LIKE :type || '%'")
    fun getNasabFilterSurahAyahTypeSubType(surah: Int, ayah: Int, type: String): List<NegationEnt>

    @Query("SELECT * FROM negationfulldata WHERE startindex=0 and endindex=0 and  type LIKE :type || '%'")
    fun getNasabFilterSubType( type: String): List<NegationEnt>
}