//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
CREATE TABLE newshart (
    surah                INTEGER NOT NULL,
    ayah                 INTEGER NOT NULL,
    indexstart           INTEGER NOT NULL,
    indexend             INTEGER NOT NULL,
    shartindexstart      INTEGER NOT NULL,
    shartindexend        INTEGER NOT NULL,
    jawabshartindexstart INTEGER NOT NULL,
    jawabshartindexend   INTEGER NOT NULL,
    harfwordno           INTEGER NOT NULL,
    shartstatwordno      INTEGER NOT NULL,
    shartendwordno       INTEGER NOT NULL,
    jawabstartwordno     INTEGER NOT NULL,
    jawabendwordno       INTEGER NOT NULL,
    englishtext          TEXT    NOT NULL,
    comment              TEXT,
    id                   INTEGER PRIMARY KEY AUTOINCREMENT
                                 NOT NULL
);

 */
@Entity(tableName = "newshart")
class NewShartEntity constructor(
    var surah: Int,
    var ayah: Int,
    var indexstart: Int,
    var indexend: Int,
    var shartindexstart: Int,
    var shartindexend: Int,
    var jawabshartindexstart: Int,
    var jawabshartindexend: Int,
    var harfwordno: Int,
    var shartstatwordno: Int,
    var shartendwordno: Int,
    var jawabstartwordno: Int,
    var jawabendwordno: Int,
    var englishtext:    String,
    var comment: String?,
    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)