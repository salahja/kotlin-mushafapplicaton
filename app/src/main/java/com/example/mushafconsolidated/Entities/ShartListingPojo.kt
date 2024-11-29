//primaryKeys ={"translation_id","verse_id"}
package com.example.mushafconsolidated.Entities

import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.room.Ignore
import androidx.room.PrimaryKey


class ShartListingPojo constructor(){
    var surah: Int=0
    var ayah: Int=0
    var wordfrom: Int=0
    var wordto: Int=0
    var startindex: Int=0
    var endindex: Int=0

    var arabictext: String? = null
    var englishtext: String? = null
    var verse: String? = null
    var type: String? = null
    var has_prostration: Int=0
    var translation: String? = null
    var en_transliteration: String? = null
    var en_jalalayn: String? = null
    var en_arberry: String? = null
    var ar_irab_two: String? = null
    var ur_jalalayn: String? = null
    var ur_junagarhi: String? = null
    var passage_no: String? = null

    @Ignore
    var spannableVerse: SpannableString? = null
    @Ignore
    var erabspnabble: SpannableStringBuilder? = null


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}