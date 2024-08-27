package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "bookmark")
class BookMarks(

    var header: String,
    var verseno: String,
    var chapterno: String,
    var surahname: String,
    val datetime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int


) {

    @Ignore
    var count: String? = null

    constructor() : this("dummy title", "dummy author", "dummy abstract", "", "", 0)




}