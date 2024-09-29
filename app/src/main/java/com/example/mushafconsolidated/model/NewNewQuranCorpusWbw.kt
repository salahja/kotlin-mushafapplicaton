package com.example.mushafconsolidated.model

import android.text.SpannableString
import androidx.room.Embedded
import androidx.room.Ignore

class NewNewQuranCorpusWbw {
    @Ignore
    var spannableverse: SpannableString? = null
    @Ignore


    @Embedded
    var corpus: QuranEntityCorpusEntityWbwEntity? = null


    constructor()
}