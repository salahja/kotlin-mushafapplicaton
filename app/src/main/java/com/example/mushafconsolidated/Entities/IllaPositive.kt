//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*CREATE TABLE illapositive (
    surahid     INTEGER NOT NULL,
    ayahid      INTEGER NOT NULL,
    wordfrom    INTEGER NOT NULL,
    wordto      INTEGER NOT NULL,
    startindex  INTEGER NOT NULL,
    endindex    INTEGER NOT NULL,
    arabictext  TEXT    NOT NULL,
    englishtext TEXT    NOT NULL,
    type        TEXT    NOT NULL,
    id          INTEGER PRIMARY KEY AUTOINCREMENT
                        NOT NULL

 */
@Entity(tableName = "illapositive")
class IllaPositive constructor(
    var surahid: Int,
    var ayahid: Int,
    var wordfrom: Int,
    var wordto:Int,
    var startindex: Int,
    var endindex: Int,
    var arabictext: String,
    var englishtext:String,
    var type:String,




    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)