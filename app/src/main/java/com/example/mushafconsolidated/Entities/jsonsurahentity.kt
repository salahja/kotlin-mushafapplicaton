//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "surahjson")
class jsonsurahentity constructor(

    var surah: Int,
    var jasonstr: String,
    @field:PrimaryKey(
        autoGenerate = true
    ) var docid: Int
) {
    // Secondary constructor without docid
    constructor(surah: Int, jasonstr: String) : this(surah, jasonstr, 0) {
        // You can add additional initialization logic here if needed
    }
}