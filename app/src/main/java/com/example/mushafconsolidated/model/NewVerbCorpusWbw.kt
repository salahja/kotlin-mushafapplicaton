package com.example.mushafconsolidated.model

import android.text.SpannableString
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.VerbCorpus


class NewVerbCorpusWbw {
    @Ignore
    var spannableverse: SpannableString? = null


    @Embedded
    var corpus: CorpusEntity? = null

    @Relation(parentColumn = "id", entityColumn = "id")
    var wbw: CorpusEntity? = null

    @Embedded
    var verbCorpusWbw:VerbCorpus?=null
    @Relation(parentColumn = "id", entityColumn = "id")
    var detailedVerbCorpus:VerbCorpus?=null

    constructor(corpus: CorpusEntity?, wbw: CorpusEntity?) {
        this.corpus = corpus
        this.wbw = wbw
        this.verbCorpusWbw=detailedVerbCorpus
    }

    constructor()
}