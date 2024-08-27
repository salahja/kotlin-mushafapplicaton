package database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mazeed")
class MazeedEntity(
             var root: String,
                   var form: String,
                   var verbtype: String,
                   var babname: String,
                   var kov: String,
                   var kovname: String,
                   @field:PrimaryKey var id: Int
)