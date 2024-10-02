package com.example.mushafconsolidated.model

import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.wbwentity

data class QuranEntityCorpusEntityWbwEntity constructor(


    var surah: Int,
    var ayah: Int,
    var wordno: Int,
    var wordcount: Int,
    var qurantext : String,
    var translation: String,
    var en_arberry: String,
    var ar_irab_two: String,


    var araone: String,
    var aratwo: String,
    var arathree: String,
    var arafour: String,
    var arafive: String,
    var  rootaraone: String,
    var  rootaratwo: String,
    var  rootarathree: String,
    var  rootarafour: String,
    var  rootarafive: String,
    var tagone: String,
    var tagtwo: String,
    var tagthree: String,
    var tagfour: String,
    var tagfive: String,
    var detailsone: String,
    var detailstwo: String,
    var detailsthree: String,
    var detailsfour: String,
    var detailsfive: String,
    var en: String,
    var bn: String,
    var `in`: String,

)
