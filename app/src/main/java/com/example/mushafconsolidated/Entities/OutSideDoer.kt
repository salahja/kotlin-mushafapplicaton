package com.example.mushafconsolidated.Entities

import android.text.SpannableStringBuilder
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "outsidedoer",

)
class OutSideDoer constructor(

    var surah: Int,
    var ayah: Int,
    var wordno: Int,

    var wordtext: String,

    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int,


    )