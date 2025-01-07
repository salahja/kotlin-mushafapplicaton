package com.example.mushafconsolidated.model


import android.text.SpannableString
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.example.mushafconsolidated.Entities.CorpusEntity



class NewQuranCorpusWbw {
    @Ignore
    var spannableverse: SpannableString? = null


    @Embedded
    var corpus: CorpusEntity? = null

    @Relation(parentColumn = "id", entityColumn = "id")
    var wbw: CorpusEntity? = null

    constructor(corpus: CorpusEntity?, wbw: CorpusEntity?) {
        this.corpus = corpus
        this.wbw = wbw
    }

    constructor()
}


