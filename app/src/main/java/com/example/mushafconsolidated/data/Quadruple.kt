package com.example.mushafconsolidated.data

import Utility.AudioPlayed

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

data class CorpusRow(
    val surah: Int,
    val ayah: Int,
    val wordno: Int,
    val tags: List<String?>,  // Represents tagone to tagfive
    val details: List<String?>,
    val arabicText: String,
    val englishText: String  // Represents concatenated araone to arafive

)
data class SilaCorpusRow(
    val surah: Int,
    val ayah: Int,
    val wordno: Int,
    val tags: List<String?>,  // Represents tagone to tagfive
    val details: List<String?>,
    val arabicText: String,
    val englishText: String  // Represents concatenated araone to arafive

)

data class ShartCorpusRow(
    val surah: Int,
    val ayah: Int,
    val wordno: Int,
    val tags: List<String?>,  // Represents tagone to tagfive
    val details: List<String?>,
    val arabicText: String,
    val englishText: String,  // Represents concatenated araone to arafive
    val quranText: String,
    val translation: String
)

data class SurahHeader(
    val rukus: Int,
    val verses: Int,
    val chapterNumber: Int,
    val surahNameAr: String,
    val surahNameEn: String
)
data class AudioPositionSaved(var audiopsaved: List<AudioPlayed>)

data class PhraseItem(val phrase: String, val grammaticalGroup: String)
