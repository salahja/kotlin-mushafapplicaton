package com.example.mushafconsolidated.model

import androidx.compose.runtime.Immutable
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.QuranEntity

@Immutable
data class QuranArrays(
    val newnewadapterlist : LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>> =  LinkedHashMap(),
    val corpusSurahWord: List<CorpusEntity> =listOf<CorpusEntity>(),
    val quranbySurah: List<QuranEntity> = listOf<QuranEntity>(),
    val chapterlist: List<ChaptersAnaEntity> = listOf<ChaptersAnaEntity>()
)