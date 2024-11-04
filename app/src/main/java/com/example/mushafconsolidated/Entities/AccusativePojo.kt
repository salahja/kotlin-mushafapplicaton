//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.room.Ignore
import androidx.room.PrimaryKey


class AccusativePojo constructor(){
    var surah: Int=0
    var ayah: Int=0



    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}