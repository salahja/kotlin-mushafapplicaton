//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
CREATE TABLE mutlaqbadalajlihitameez (
    surah  INTEGER NOT NULL,
    ayah   INTEGER NOT NULL,
    wordno INTEGER NOT NULL,
    word   TEXT    NOT NULL,
    type   TEXT    NOT NULL,
    id     INTEGER PRIMARY KEY AUTOINCREMENT
                   NOT NULL
);

 */
@Entity(tableName = "mutlaqbadalajlihitameez")
class MutlaqBadalaAlihiTameezEnt constructor(
    var surahid: Int,
    var ayahid: Int,
    var wordno: Int,

    var word: String,
    var type: String,

    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)