//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lysa")
class lysaEnt constructor(
    var hua: String,
    var huma: String,
    var hum: String,


    var hia: String,
    var humaf: String,
    var hunna: String,

    var anta: String,
    var antuma: String,
    var antum: String,

    var anti: String,
    var antumaf: String,
    var antunna: String,

    var ana: String,
    var nahnu: String,
    var grammar: String,
    @field:PrimaryKey(
        autoGenerate = true
    ) var id: Int
)