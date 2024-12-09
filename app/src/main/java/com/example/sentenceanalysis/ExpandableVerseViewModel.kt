package com.example.sentenceanalysis

import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Constant
import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.model.QuranCorpusWbw
import com.example.mushafconsolidated.model.SarfSagheerPOJO
import com.example.mushafconsolidated.model.Word
import com.example.utility.QuranGrammarApplication
import com.example.utility.QuranViewUtils.processString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ExpandableVerseViewModel(
    chapterid: Int,
    verseid: Int,
    thememode: Boolean,
    wbwchoice: Int
) : ViewModel() {
    private val itemsList = MutableStateFlow(listOf<VerseAnalysisModel>())
    private var quran: List<QuranEntity>? = null

    //  val model: QuranVIewModel by viewModels()
    private var dark: Boolean = false

    private var corpusSurahWord: List<QuranCorpusWbw>? = null

    private lateinit var negaTionList: List<NegationEnt>
    var vbdetail = HashMap<String, String>()
    var wordbdetail: HashMap<String, SpannableStringBuilder>? = null
    var sarf: SarfSagheerPOJO? = null
    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    var counter = 0
    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList

    data class VerseAnalysisModel(
        val id: Int,
        val grammarrule: String,
        val result: List<AnnotatedString>
    )

    val items: StateFlow<List<VerseAnalysisModel>> get() = itemsList

    private val itemIdsList = MutableStateFlow(listOf<Int>())


    init {
        getData(thememode, wbwchoice, chapterid, verseid)
    }

    private fun getData(
        thememode: Boolean,
        wbwchoice: Int,
        chapterid: Int,
        verseid: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)

        negaTionList = utils.geTNegatonFilerSurahAyah(chapterid, verseid)
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val testList = arrayListOf<VerseAnalysisModel>()
                val mudhafarray: MutableList<AnnotatedString> = ArrayList()
                setMudhaf(mudhafarray, thememode)
                val mausoofsifaarray: MutableList<AnnotatedString> = ArrayList()
                setMausoof(mausoofsifaarray, thememode)
                val shartarray: MutableList<AnnotatedString> = ArrayList()
                newSetShart(shartarray, thememode)


                val harfnasbarray: MutableList<AnnotatedString> = ArrayList()
                setNewNasb(harfnasbarray, thememode)

                val kanaarray: MutableList<AnnotatedString> = ArrayList()
                newsetKana(kanaarray)

                val silaAnmasdarArray: MutableList<AnnotatedString> = ArrayList()
                setSialAnMasdariya(silaAnmasdarArray)


                val verse: MutableList<AnnotatedString> = ArrayList()
                val translation: MutableList<AnnotatedString> = ArrayList()
                verse.add(
                    AnnotatedString(
                        ":-" + negaTionList[0].verse
                    )
                )
                translation.add(AnnotatedString(negaTionList[0].translation))


                testList += VerseAnalysisModel(
                    1,
                    "verse",
                    verse
                )

                testList += VerseAnalysisModel(
                    2,
                    "Translation",
                    translation
                )
                itemsList.emit(testList)

                if (mudhafarray.isNotEmpty()) {
                    testList += VerseAnalysisModel(
                        3,
                        "Possessive Construction (إضافَة)",
                        mudhafarray
                    )
                    itemsList.emit(testList)
                }
                if (mausoofsifaarray.isNotEmpty()) {
                    testList += VerseAnalysisModel(
                        4,
                        "Adjective Phrases(مرکب توصیفی)",
                        mausoofsifaarray
                    )
                    itemsList.emit(testList)

                }
                if (shartarray.isNotEmpty()) {
                    testList += VerseAnalysisModel(
                        5,
                        "Condition/ (جُمْلةُ الشَّرْطِ)",
                        shartarray
                    )
                    itemsList.emit(testList)
                }

                if (harfnasbarray.isNotEmpty()) {
                    testList += VerseAnalysisModel(
                        6,
                        "The Particle inna (ان واخواتها",
                        harfnasbarray
                    )
                    itemsList.emit(testList)
                }

                if (kanaarray.isNotEmpty()) {
                    testList += VerseAnalysisModel(
                        7,
                        "Verb kāna/كان واخواتها",
                        kanaarray
                    )
                    itemsList.emit(testList)
                }
                if (silaAnmasdarArray.isNotEmpty()) {
                    testList += VerseAnalysisModel(
                        7,
                        "Subordinate Clause/كان ",
                        silaAnmasdarArray
                    )
                    itemsList.emit(testList)
                }


            }
        }
    }

    private fun setSialAnMasdariya(silaAnmasdarArray: MutableList<AnnotatedString>) {

        val harfcolor = if (dark) Color(Constant.GOLD) else Color(Constant.FORESTGREEN)
        val clauseColor = if (dark) Color(Constant.ORANGE400) else Color(Constant.KASHMIRIGREEN)
        for (silaEnt in negaTionList) {
            if (silaEnt.type == "sila" || silaEnt.type == "anmasdar" || silaEnt.type == "silaverify" || silaEnt.type == "anmasdarverify") {
                val (firstWord, restOfString) = silaEnt.arabictext.split(" ", limit = 2)
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = harfcolor, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(restOfString))
                builder.addStyle(
                    SpanStyle(color = clauseColor, textDecoration = TextDecoration.Underline),
                    builder.length - restOfString.length,
                    builder.length
                )



                silaAnmasdarArray.add(builder.toAnnotatedString())
                silaAnmasdarArray.add(AnnotatedString(silaEnt.englishtext))

            }
        }


    }

    private fun newsetKana(
        kanaarray: MutableList<AnnotatedString>,

        ) {


        val harfKanaColor = if (dark) Color(Constant.GOLD) else Color(Constant.FORESTGREEN)
        val kanaIsmColor = if (dark) Color(Constant.ORANGE400) else Color(Constant.KASHMIRIGREEN)
        val kanaKhabarColor =
            if (dark) Color(android.graphics.Color.CYAN) else Color(Constant.WHOTPINK)
        for (kanaEntity in negaTionList) {
            if (kanaEntity.type == "kanatwo" || kanaEntity.type == "kadatwo") {
                val parts = kanaEntity.arabictext.split(" ", limit = 3)


                val words = kanaEntity.arabictext.split(
                    " ",
                    limit = 3
                ) // Split with limit to avoid more than needed
                val firstWord = words.getOrNull(0) ?: ""
                val seconthirdstring = words.getOrNull(1) ?: ""

                val finalString = words.drop(2).joinToString(" ")
                val totalLength = firstWord.length + seconthirdstring.length + finalString.length
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = harfKanaColor, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = kanaIsmColor, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(finalString))
                builder.addStyle(
                    SpanStyle(color = kanaKhabarColor, textDecoration = TextDecoration.Underline),
                    builder.length - finalString.length,
                    builder.length
                )

                kanaarray.add(builder.toAnnotatedString())
                kanaarray.add(AnnotatedString(kanaEntity.englishtext))


            } else if (kanaEntity.type == "khabarkanaism") {
                val parts = kanaEntity.arabictext.split(":")
                val firstWord = parts[0]
                var seconthirdstring = parts[1]
                var thirdPartWord = parts[2]
                val finalString = firstWord + seconthirdstring

                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = kanaKhabarColor, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = harfKanaColor, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(finalString))
                builder.addStyle(
                    SpanStyle(color = kanaIsmColor, textDecoration = TextDecoration.Underline),
                    builder.length - finalString.length,
                    builder.length
                )

                kanaarray.add(builder.toAnnotatedString())
                kanaarray.add(AnnotatedString(kanaEntity.englishtext))


            } else if (kanaEntity.type == "kanaharfismlater" || kanaEntity.type == "kadaismlater") {
                val parts = kanaEntity.arabictext.split(":")
                val firstPartWords = parts[0].split(" ")


                val result = processString(kanaEntity.arabictext)
                val builder = AnnotatedString.Builder()
                println("First part: ${result.first}")
                println("Second part: ${result.second}")

                builder.append(AnnotatedString(result.first))
                builder.addStyle(
                    SpanStyle(color = harfKanaColor, textDecoration = TextDecoration.Underline),
                    builder.length - result.first.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(result.second))
                builder.addStyle(
                    SpanStyle(color = kanaKhabarColor, textDecoration = TextDecoration.Underline),
                    builder.length - result.second.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(result.third))
                builder.addStyle(
                    SpanStyle(color = kanaIsmColor, textDecoration = TextDecoration.Underline),
                    builder.length - result.third.length,
                    builder.length
                )

                kanaarray.add(builder.toAnnotatedString())
                kanaarray.add(AnnotatedString(kanaEntity.englishtext))


            } else if (kanaEntity.type == "kanatwo-twoism") {
                val parts = kanaEntity.arabictext.split(" ", limit = 3)

                val split = kanaEntity.arabictext.split(":")
                //    if (split.size >= 2) {
                val regex = Regex("\\.\\.+")
                val firstWordt = split[0]
                val firstWord = regex.replace(firstWordt, "")
                val seconthirdstring = split[1]
                val finalString = split[2]
                val totalLength = firstWord.length + seconthirdstring.length

                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = harfKanaColor, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = kanaIsmColor, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(finalString))
                builder.addStyle(
                    SpanStyle(color = kanaKhabarColor, textDecoration = TextDecoration.Underline),
                    builder.length - finalString.length,
                    builder.length
                )

                kanaarray.add(builder.toAnnotatedString())
                kanaarray.add(AnnotatedString(kanaEntity.englishtext))
            } else if (kanaEntity.type == "kanaone" || kanaEntity.type == "kadaone") {


                val (firstWord, restOfString) = kanaEntity.arabictext.split(" ", limit = 2)
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = harfKanaColor, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(restOfString))
                builder.addStyle(
                    SpanStyle(color = kanaKhabarColor, textDecoration = TextDecoration.Underline),
                    builder.length - restOfString.length,
                    builder.length
                )



                kanaarray.add(builder.toAnnotatedString())
                kanaarray.add(AnnotatedString(kanaEntity.englishtext))


            }


        }

    }


    private fun setNewNasb(
        hasbarray: MutableList<AnnotatedString>,
        thememode: Boolean,

        ) {

        val tagColorOne =
            if (thememode) Color(Constant.GREENDARK) else Color(ComposeConstant.KASHMIRIGREEN)
        val tagColorTwo = if (thememode) Color(Constant.BCYAN) else Color(Constant.prussianblue)
        val tagColorThree =
            if (thememode) Color(android.graphics.Color.YELLOW) else Color(Constant.deepburnsienna)


        for (nasbEntity in negaTionList) {
            if (nasbEntity.type == "nasabone") {

                val (firstWord, seconthirdstring) = nasbEntity.arabictext.split(
                    " ",
                    limit = 2
                )
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )
                hasbarray.add(builder.toAnnotatedString())
                hasbarray.add(AnnotatedString(nasbEntity.englishtext))

            } else if (nasbEntity.type == "nasabtwo") {

                val words = nasbEntity.arabictext.split(
                    " ",
                    limit = 3
                ) // Split with limit to avoid more than needed
                val firstWord = words.getOrNull(0) ?: ""
                val seconthirdstring = words.getOrNull(1) ?: ""

                val finalString = words.drop(2).joinToString(" ")
                val totalLength =
                    firstWord.length + seconthirdstring.length + finalString.length


                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(finalString))
                builder.addStyle(
                    SpanStyle(color = tagColorThree, textDecoration = TextDecoration.Underline),
                    builder.length - finalString.length,
                    builder.length
                )

                hasbarray.add(builder.toAnnotatedString())
                hasbarray.add(AnnotatedString(nasbEntity.englishtext))


            } else if (nasbEntity.type == "nasabtwoismlater") {
                val parts = nasbEntity.arabictext.split(":")
                val firstPartWords = parts[0].split(" ")


                val result = processString(nasbEntity.arabictext)

                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(result.first))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - result.first.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(result.second))
                builder.addStyle(
                    SpanStyle(color = tagColorThree, textDecoration = TextDecoration.Underline),
                    builder.length - result.second.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(result.third))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - result.third.length,
                    builder.length
                )

                hasbarray.add(builder.toAnnotatedString())
                hasbarray.add(AnnotatedString(nasbEntity.englishtext))


            } else if (nasbEntity.type == "nasabtwo-twoism") {
                val parts = nasbEntity.arabictext.split(" ", limit = 4)


                // Get the first word and the rest of the string
                val firstWord = parts.getOrNull(0) ?: ""
                val secondWord = parts.getOrNull(1) ?: ""
                val thirdWord = parts.getOrNull(2) ?: ""
                val restOfString = parts.getOrNull(3) ?: ""
                val seconthirdstring = secondWord + " " + thirdWord
                val totalLength =
                    firstWord.length + seconthirdstring.length + restOfString.length

                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = tagColorThree, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(restOfString))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - restOfString.length,
                    builder.length
                )

                hasbarray.add(builder.toAnnotatedString())
                hasbarray.add(AnnotatedString(nasbEntity.englishtext))


            }


        }


    }

    private fun setMausoof(
        mausoofsifaarray: MutableList<AnnotatedString>,
        thememode: Boolean,

        ) {


        val tagColor =
            if (thememode) ComposeConstant.sifaspansDark else ComposeConstant.sifaspansLight
        for (sifaEntity in negaTionList) {
            if (sifaEntity.type == "sifa") {
                val quranVerses = sifaEntity.arabictext


                val builder = AnnotatedString.Builder(quranVerses)
                builder.addStyle(
                    SpanStyle(color = tagColor, textDecoration = TextDecoration.Underline),
                    0,
                    quranVerses.length
                )
                mausoofsifaarray.add(builder.toAnnotatedString())



                mausoofsifaarray.add(AnnotatedString(sifaEntity.englishtext))

            }

        }

    }


    val kana: List<SpannableString>
        get() {
            //newsetKana(kanaarray)
            return ArrayList()
        }


    private fun newSetShart(
        shartarray: MutableList<AnnotatedString>,
        thememode: Boolean,

        ) {
        val utils = Utils(QuranGrammarApplication.context)


        val tagColorOne =
            if (thememode) Color(Constant.GOLD) else Color(ComposeConstant.FORESTGREEN)
        val tagColorTwo =
            if (thememode) Color(Constant.ORANGE400) else Color(Constant.KASHMIRIGREEN)
        val tagColorThree =
            if (thememode) Color(android.graphics.Color.CYAN) else Color(Constant.WHOTPINK)
        for (shartEntity in negaTionList) {

            if (shartEntity.type == "shartnojawab") {
                val quranVerses = shartEntity.arabictext

                val (firstWord, restOfString) = shartEntity.arabictext.split(" ", limit = 2)
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(restOfString))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - restOfString.length,
                    builder.length
                )
                shartarray.add(builder.toAnnotatedString())
                shartarray.add(AnnotatedString(shartEntity.englishtext))
            } else if (shartEntity.type == "shartonly") {
                val (firstWord, restOfString) = shartEntity.arabictext.split(
                    " ",
                    limit = 2
                )
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(restOfString))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - restOfString.length,
                    builder.length
                )
                shartarray.add(builder.toAnnotatedString())
                shartarray.add(AnnotatedString(shartEntity.englishtext))
            } else if (shartEntity.type == "shartall") {
                val split = shartEntity.arabictext.split(":")
                //    if (split.size >= 2) {
                val regex = Regex("\\.\\.+")
                val firstWordt = split[0]
                val firstWord = regex.replace(firstWordt, "")
                val seconthirdstring = split[1]
                val totalLength = firstWord.length + seconthirdstring.length
                val finalString = split[2]
                val builder = AnnotatedString.Builder()

                builder.append(AnnotatedString(firstWord))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - firstWord.length,
                    builder.length
                )
                builder.append(" ")

                builder.append(AnnotatedString(seconthirdstring))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - seconthirdstring.length,
                    builder.length
                )

                builder.append(" ")
                builder.append(AnnotatedString(finalString))
                builder.addStyle(
                    SpanStyle(color = tagColorThree, textDecoration = TextDecoration.Underline),
                    builder.length - finalString.length,
                    builder.length
                )

                shartarray.add(builder.toAnnotatedString())
                shartarray.add(AnnotatedString(shartEntity.englishtext))

            }
        }

    }


    private fun setMudhaf(
        mudhafarray: MutableList<AnnotatedString>,
        thememode: Boolean,

        ) {


        val tagColor =
            if (thememode) Color(ComposeConstant.WBURNTUMBER) else Color(ComposeConstant.MIDNIGHTBLUE)


        for (data in negaTionList) {
            val surahid = data.surahid
            val ayahid = data.ayahid

            val arabicString = data.arabictext
            val englishString = data.englishtext
            var type = data.type
            var combinedString = ""
            if (type == "mudhaf") {
                val quranVerses = data.arabictext
                val translation = data.englishtext
                //  val substr = quranVerses.substring(mudhafEntity.startindex, mudhafEntity.endindex)

                val builder = AnnotatedString.Builder(quranVerses)
                builder.addStyle(
                    SpanStyle(color = tagColor, textDecoration = TextDecoration.Underline),
                    0,
                    quranVerses.length
                )
                mudhafarray.add(builder.toAnnotatedString())




                mudhafarray.add(AnnotatedString(translation.toString()))
            }

        }
    }


    private fun getSelectedTranslation(tr: wbwentity, value: Int): StringBuilder {
        val sb = StringBuilder()
        when (value) {
            0 -> sb.append(tr.en)
            1 -> sb.append(tr.ur)
            2 -> sb.append(tr.bn)
            3 -> sb.append(tr.id)
        }
        sb.append(" ")
        return sb
    }


    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}


internal class SplitQuranVerses  // --Commented out by Inspection (26/04/22, 12:48 AM):private List<CorpusAyahWord> corpusayahWordArrayList;
{
    fun splitSingleVerse(quraverses: String): java.util.ArrayList<Word> {
        val ayahWordArrayList = java.util.ArrayList<Word>()
        val s = quraverses.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (i in s.indices) {
            val word = Word()
            word.wordsAr = (s[i])
            word.wordno = (i + 1)
            ayahWordArrayList.add(word)
        }
        return ayahWordArrayList
        //     return ayahWords;
    }
}

operator fun AnnotatedString.plus(other: AnnotatedString): AnnotatedString {
    return buildAnnotatedString {
        append(this@plus)
        append(other)
    }
}
