//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
CREATE TABLE absolute_negation (
CREATE TABLE lammudharynegation (
    surahid    INTEGER NOT NULL,
    ayahid     INTEGER NOT NULL,
    wordfrom   INTEGER NOT NULL,
    wordnoto   INTEGER NOT NULL,
    startindex INTEGER NOT NULL,
    endindex   INTEGER NOT NULL,
    harf       TEXT    NOT NULL,
    id         INTEGER PRIMARY KEY AUTOINCREMENT
                       NOT NULL
);    arabictext  TEXT    NOT NULL,
    englishtext TEXT    NOT NULL,

 */
@Entity(tableName = "negationfulldata")
class NegationEnt constructor(
    var surahid: Int,
    var ayahid: Int,
    var wordfrom: Int,
    var wordto:Int,
    var startindex: Int,
    var endindex: Int,
    var arabictext: String,
    var englishtext: String,
    var verse: String,
    var translation: String,
    var type: String,



    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)