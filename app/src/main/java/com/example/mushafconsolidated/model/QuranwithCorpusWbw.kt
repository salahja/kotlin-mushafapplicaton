package com.example.mushafconsolidated.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.wbwentity

data class QuranwithCorpusWbw constructor(

    @Embedded val quran: QuranEntity,
    @Embedded val corpus: CorpusEntity,
    @Embedded val wbw: wbwentity,



    ) {

}
