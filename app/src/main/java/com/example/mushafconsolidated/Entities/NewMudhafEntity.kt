//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
CREATE TABLE newmudhaf (
    surah       INTEGER,
    ayah        INTEGER,
    wordfrom    INTEGER,
    wordto      INTEGER,
    startindex  INTEGER,
    endindex    INTEGER,
    arabictext  TEXT,
    englishtext TEXT,
    type        TEXT
);

 */
@Entity(tableName = "newmudhaf")
class NewMudhafEntity constructor(
    var surah: Int,
    var ayah: Int,
    var wordfrom: Int,
    var wordto: Int,
    var startindex: Int,
    var endindex: Int,
    var arabictext: String,
    var englishtext: String,
    var comment: String,
    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)