package com.example.mushafconsolidated.model

import android.text.SpannableString
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.wbwentity

class NewNewQuranCorpusWbw {
    @Ignore
    var spannableverse: SpannableString? = null
    @Ignore
    var spanInfoList: List<SpanInfo> = emptyList()

    @Embedded
    var corpus: QuranEntityCorpusEntityWbwEntity? = null


    constructor(corpus: QuranEntityCorpusEntityWbwEntity?) {
        this.corpus = corpus

    }

    constructor()
}