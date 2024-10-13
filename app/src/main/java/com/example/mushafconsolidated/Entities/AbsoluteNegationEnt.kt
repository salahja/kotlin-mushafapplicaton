//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
CREATE TABLE absolute_negation (
    surahid    INTEGER NOT NULL,
    ayahid     INTEGER NOT NULL,
    wordno     INTEGER NOT NULL,
    startindex INTEGER NOT NULL,
    endindex   INTEGER NOT NULL,
    id         INTEGER PRIMARY KEY AUTOINCREMENT
                       NOT NULL
 */
@Entity(tableName = "absolute_negation")
class AbsoluteNegationEnt constructor(
    var surahid: Int,
    var ayahid: Int,
    var wordno: Int,
    var startindex: Int,
    var endindex: Int,

    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)