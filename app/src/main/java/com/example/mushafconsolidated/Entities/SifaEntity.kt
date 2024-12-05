//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*

CREATE TABLE sifa (
    surah       INTEGER NOT NULL,
    ayah        INTEGER NOT NULL,
    wordfrom    INTEGER NOT NULL,
    wordto      INTEGER NOT NULL,
    startindex  INTEGER NOT NULL,
    endindex    INTEGER NOT NULL,
    arabictext  TEXT    NOT NULL,
    englishtext TEXT    NOT NULL,
    comment     TEXT    NOT NULL,
    id          INTEGER PRIMARY KEY AUTOINCREMENT
                        NOT NULL
);

 */
@Entity(tableName = "sifa")
class SifaEntity constructor(
    var surah: Int,
    var ayah: Int,
    var wordfrom: Int,
    var wordto: Int,
    var startindex: Int,
    var endindex: Int,
    var arabictext: String,
    var englishtext: String,
    var verse:String,
    var translation:String,
    var comment: String,
    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)