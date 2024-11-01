//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
    surah             INTEGER NOT NULL,
    ayah              INTEGER NOT NULL,
    indexstart        INTEGER NOT NULL,
    indexend          INTEGER NOT NULL,
    ismstart          INTEGER NOT NULL,
    ismend            INTEGER NOT NULL,
    khabarstart       INTEGER NOT NULL,
    khabarend         INTEGER NOT NULL,
    harfwordno        INTEGER NOT NULL,
    ismstartwordno    INTEGER NOT NULL,
    ismendwordno      INTEGER NOT NULL,
    khabarstartwordno INTEGER NOT NULL,
    khabarendwordno   INTEGER NOT NULL,
    mahdoof           TEXT    NOT NULL,
    englishtext       TEXT    NOT NULL,
    comment           TEXT,
    id                INTEGER PRIMARY KEY AUTOINCREMENT
                              NOT NULL
);

 */
@Entity(tableName = "newnasb")
class NewNasbEntity constructor(
    var surah: Int,
    var ayah: Int,
    var indexstart: Int,
    var indexend: Int,
    var ismstart: Int,
    var ismend: Int,
    var khabarstart: Int,
    var khabarend: Int,
    var harfwordno: Int,
    var ismstartwordno: Int,
    var ismendwordno: Int,
    var khabarstartwordno: Int,
    var khabarendwordno: Int,
    var mahdoof: Int,
    var englishtext: String,
    var comment: String,
    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)