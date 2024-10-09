package com.example.sentenceanalysis

import Utility.ArabicLiterals
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Constant
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.GrammerFragmentsBottomSheet
import com.example.mushafconsolidated.model.QuranCorpusWbw
import com.example.mushafconsolidated.model.SarfSagheerPOJO
import com.example.utility.QuranGrammarApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.text.append


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
        quran = utils.getsurahayahVerses(chapterid, verseid) as List<QuranEntity>?
        utils.getQuranCorpusWbwSurhAyah(chapterid, verseid, 1)

        corpusSurahWord = utils.getQuranCorpusWbwSurhAyah(chapterid, verseid, 1)
        val list = java.util.LinkedHashMap<String, List<AnnotatedString>>()

        val harfnasbarray: MutableList<AnnotatedString> = ArrayList()
        /*     setNewNasb(harfnasbarray)
             val mausoofsifaarray: MutableList<AnnotatedString> =  ArrayList()
             setMausoof(mausoofsifaarray)


             val kanaarray: MutableList<AnnotatedString> =  ArrayList()
             newsetKana(kanaarray)*/

        val mudhafarray: MutableList<AnnotatedString> = ArrayList()
        /*        expandableListDetail["Verse"] = verse
                expandableListDetail["Translation"] = translation
                expandableListDetail["Conditional/جملة شرطية\""] = shartarray
                expandableListDetail["Accusative/ "] = harfnasbarray
                expandableListDetail["Verb kāna/كان واخواتها"] = kanaarray
                expandableListDetail["Adjectival Phrases/مرکب توصیفی"] = mausoofsifaarray
                expandableListDetail["Possessive/إضافَة"] = mudhafarray*/
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val testList = arrayListOf<VerseAnalysisModel>()
                val mudhafarray: MutableList<AnnotatedString> = ArrayList()
                setMudhaf(mudhafarray, thememode, wbwchoice, chapterid, verseid)
                val mausoofsifaarray: MutableList<AnnotatedString> = ArrayList()
                setMausoof(mausoofsifaarray, thememode, wbwchoice, chapterid, verseid)
                val shartarray: MutableList<AnnotatedString> = ArrayList()
                newSetShart(shartarray, thememode, wbwchoice, chapterid, verseid)


                val harfnasbarray: MutableList<AnnotatedString> = ArrayList()
                setNewNasb(harfnasbarray, thememode, wbwchoice, chapterid, verseid)

                val kanaarray: MutableList<AnnotatedString> = ArrayList()
                newsetKana(kanaarray, wbwchoice, chapterid, verseid)


                val verse: MutableList<AnnotatedString> = ArrayList()
                val translation: MutableList<AnnotatedString> = ArrayList()
                verse.add(
                    AnnotatedString(
                        ":-" + quran!![0].qurantext
                    )
                )
                translation.add(AnnotatedString(quran!![0].translation))


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

            }
        }
    }
    private fun newsetKana(
        kanaarray: MutableList<AnnotatedString>,
        wbwchoice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        val kanaSurahAyahnew = utils.getKananewSurahAyah(chapterid, ayanumber)

        val harfKanaColor = if (dark) Color(Constant.GOLD) else Color(Constant.FORESTGREEN)
        val kanaIsmColor = if (dark) Color(Constant.ORANGE400) else Color(Constant.KASHMIRIGREEN)
        val kanaKhabarColor = if (dark) Color(android.graphics.Color.CYAN) else Color(Constant.WHOTPINK)

        kanaSurahAyahnew?.forEach { kana ->
            val quranVerses = quran!![0].qurantext
            val harfOfVerse = quranVerses.substring(kana.indexstart, kana.indexend)
            val ismOfVerse = if (kana.ismkanaend > kana.ismkanastart) quranVerses.substring(kana.ismkanastart, kana.ismkanaend) else ""
            val khabarOfVerse = quranVerses.substring(kana.khabarstart, kana.khabarend)

            val hasHarf = kana.indexstart >= 0 && kana.indexend > 0
            val hasIsm = kana.ismkanastart >= 0 && kana.ismkanaend > 0
            val hasKhabar = kana.khabarstart >= 0 && kana.khabarend > 0

            val builder = AnnotatedString.Builder()
            if (hasHarf) {
                builder.append(AnnotatedString(harfOfVerse))
                builder.addStyle(
                    SpanStyle(color = harfKanaColor, textDecoration = TextDecoration.Underline),
                    builder.length - harfOfVerse.length,
                    builder.length
                )
                builder.append(" ")
            }
            if (hasIsm) {
                builder.append(AnnotatedString(ismOfVerse))
                builder.addStyle(
                    SpanStyle(color = kanaIsmColor, textDecoration = TextDecoration.Underline),
                    builder.length - ismOfVerse.length,
                    builder.length
                )
                builder.append(" ")
            }
            if (hasKhabar) {
                builder.append(AnnotatedString(khabarOfVerse))
                builder.addStyle(
                    SpanStyle(color = kanaKhabarColor, textDecoration = TextDecoration.Underline),
                    builder.length - khabarOfVerse.length,
                    builder.length
                )
            }

            if (kana.ismkanastart > kana.khabarstart && hasIsm && hasKhabar) {
                val parts = builder.toAnnotatedString().split(" ")
                kanaarray.add(buildAnnotatedString {
                    append(parts[0])
                    append(" ")
                    append(parts[2])
                    append(" ")
                    append(parts[1])
                })
            } else {
                kanaarray.add(builder.toAnnotatedString())
            }

            val sb = StringBuilder()
            if (hasHarf && (hasIsm || hasKhabar)) {
                val wbwAyah = utils.getwbwQuranBySurahAyah(
                    corpusSurahWord!![0].corpus.surah,
                    corpusSurahWord!![0].corpus.ayah
                )
                val sbIsmOrKhabar = StringBuilder()
                wbwAyah?.forEach { w ->
                    val temp = getSelectedTranslation(w, wbwchoice)
                    when (w.wordno) {
                        kana.harfwordno -> sb.append(temp).append(" ")
                        in kana.ismwordo..kana.ismendword -> sb.append(temp).append(" ")
                        in kana.khabarstartwordno..kana.khabarendwordno -> sbIsmOrKhabar.append(temp).append(" ")
                    }
                }
                sb.append("... ").append(sbIsmOrKhabar)
                kanaarray.add(AnnotatedString(sb.toString()))
            } else if (hasHarf && hasKhabar) {
                val wordTo = if (khabarOfVerse.split("\\s").size == 1) kana.khabarstartwordno else kana.khabarendwordno
                if (kana.khabarstartwordno - kana.harfwordno == 1) {
                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        kana.harfwordno,
                        wordTo
                    )
                    list?.forEach { w ->
                        sb.append(getSelectedTranslation(w, wbwchoice)).append(" ")
                    }
                } else {
                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        kana.harfwordno,
                        kana.harfwordno
                    )
                    wbwselection(wbwchoice, sb, list)
                    val list2 = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        kana.khabarstartwordno,
                        wordTo
                    )
                    list2?.forEach { w ->
                        sb.append(getSelectedTranslation(w, wbwchoice)).append(" ")
                    }
                }
                kanaarray.add(AnnotatedString(sb.toString()))
            } else if (hasHarf && hasIsm) {
                if (kana.ismkanastart - kana.indexend == 1) {
                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        kana.harfwordno,
                        kana.ismwordo
                    )
                    list?.forEach { w ->
                        sb.append(getSelectedTranslation(w, wbwchoice)).append(" ")
                    }
                } else {
                    val wbwAyah = utils.getwbwQuranBySurahAyah(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah
                    )
                    val sbIsmOrKhabar = StringBuilder()
                    wbwAyah?.forEach { w ->
                        val temp = getSelectedTranslation(w, wbwchoice)
                        when (w.wordno) {
                            kana.harfwordno -> sb.append(temp).append(" ")
                            in kana.ismwordo..kana.ismendword -> sbIsmOrKhabar.append(temp).append(" ")
                        }
                    }
                    sb.append(".....").append(sbIsmOrKhabar)
                }
                kanaarray.add(AnnotatedString(sb.toString()))
            } else if (hasHarf) {
                val list = utils.getwbwQuranbTranslation(
                    corpusSurahWord!![0].corpus.surah,
                    corpusSurahWord!![0].corpus.ayah,
                    kana.harfwordno,
                    kana.harfwordno
                )
                wbwSelection(wbwchoice, sb, list)
                kanaarray.add(AnnotatedString(sb.toString()))
            }
        }
    }

    private fun newsetKanas(
        kanaarray: MutableList<AnnotatedString>,
        wbwchoice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        //  val kanaSurahAyahnew: List<NewKanaEntity?>? =
        //   utils.getKanaSurahAyahnew(chapterid, ayanumber)
        val kanaSurahAyahnew = utils.getKananewSurahAyah(chapterid, ayanumber)

        //   val kanaSurahAyahnew=     utils.getkana(chapterid,ayanumber).value


        var harfkana: Color? = null
        var kanaism: Color? = null
        var kanakhbar: Color? = null
        val builder = AnnotatedString.Builder()
        val builder1 = AnnotatedString.Builder()
        val builder2 = AnnotatedString.Builder()
        if (dark) {
            harfkana = Color(Constant.GOLD)
            kanaism = Color(Constant.ORANGE400)
            kanakhbar = Color(android.graphics.Color.CYAN)
        } else {
            harfkana = Color(Constant.FORESTGREEN)
            kanaism = Color(Constant.KASHMIRIGREEN)
            kanakhbar = Color(Constant.WHOTPINK)
        }
        if (kanaSurahAyahnew != null) {
            for (kana in kanaSurahAyahnew) {
                //System.out.println("CHECK");
                var harfofverse: String
                var ismofverse: String
                var khabarofverse: String
                val start = kana.indexstart
                val end = kana.indexend
                val isstart = kana.ismkanastart
                val issend = kana.ismkanaend
                val khabarstart = kana.khabarstart
                val khabarend = kana.khabarend
                val quranverses: String = quran!![0].qurantext
                harfofverse = quranverses.substring(start, end)
                ismofverse = if (issend > isstart) {
                    quranverses.substring(isstart, issend)
                } else {
                    ""
                }
                khabarofverse = quranverses.substring(khabarstart, khabarend)
                val isharfb = start >= 0 && end > 0
                val isism = isstart >= 0 && issend > 0
                val isjawab = khabarstart >= 0 && khabarend > 0
                val a = isharfb && isism && isjawab
                val d = isharfb && isjawab
                val b = isharfb && isism
                val harfword = kana.harfwordno
                val ismSword = kana.ismwordo
                val ismEword = kana.ismendword
                val khabarSword = kana.khabarstartwordno
                val habarEword = kana.khabarendwordno
                var harfspannble: AnnotatedString
                var harfismspannable: AnnotatedString
                var khabarofversespannable: AnnotatedString
                if (a) {
                    harfspannble = AnnotatedString(harfofverse)
                    harfismspannable = AnnotatedString(ismofverse)
                    khabarofversespannable = AnnotatedString(khabarofverse)

                    val sourceone = harfspannble
                    val sourcetwo = harfismspannable
                    val sourcethree = khabarofversespannable
                    val tagonestyle = SpanStyle(
                        color = harfkana,
                        textDecoration = TextDecoration.Underline
                    )
                    val tagtwostyle = SpanStyle(
                        color = kanaism,
                        textDecoration = TextDecoration.Underline
                    )
                    val tagthreestyle = SpanStyle(
                        color = kanakhbar,
                        textDecoration = TextDecoration.Underline
                    )
                    val space = AnnotatedString(" ")

                    builder.append(sourceone)
                    builder.addStyle(tagonestyle, 0, harfofverse.length)
                    builder.append(space)
                    builder1.append(sourcetwo)
                    builder1.addStyle(tagthreestyle, 0, harfismspannable.length)

                    builder2.append(sourcethree)
                    builder2.addStyle(tagthreestyle, 0, khabarofversespannable.length)


                    val annotatedString =
                        builder.toAnnotatedString() + space + builder1.toAnnotatedString() + builder2.toAnnotatedString()


                    if (kana.ismkanastart > kana.khabarstart) {

                        val annotatedString =
                            builder.toAnnotatedString() + space + builder2.toAnnotatedString() + builder1.toAnnotatedString()

                        /*        val charSequence = TextUtils.concat(
                                    harfspannble,
                                    " ",
                                    khabarofversespannable,
                                    " ",
                                    harfismspannable
                                )*/
                        kanaarray.add(annotatedString)
                    } else {

                        val annotatedString =
                            builder.toAnnotatedString() + space + builder1.toAnnotatedString() + builder2.toAnnotatedString()

                        /*     val charSequence = TextUtils.concat(
                                 harfspannble,
                                 " ",
                                 harfismspannable,
                                 " ",
                                 khabarofversespannable
                             )*/
                        kanaarray.add(annotatedString)
                    }
                    val sb = StringBuilder()
                    val ismorkhabarsb = StringBuilder()

                    val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah
                    )
                    if (wbwayah != null) {
                        for (w in wbwayah) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w!!, wbwchoice)
                            when (w.wordno) {
                                harfword -> {
                                    sb.append(temp.append(" "))
                                }
                                in ismSword..ismEword -> {
                                    sb.append(temp).append(" ")
                                }
                                in khabarSword..habarEword -> {
                                    ismorkhabarsb.append(temp).append(" ")
                                }
                            }
                        }
                    }
                    sb.append("... ")
                    sb.append(ismorkhabarsb)
                    kanaarray.add(AnnotatedString(sb.toString()))

                    //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
                } else if (d) {
                    val builder = AnnotatedString.Builder()
                    val builder1 = AnnotatedString.Builder()
                    val builder2 = AnnotatedString.Builder()
// ismkana && khabarkana
                    harfspannble = AnnotatedString(harfofverse)
                    khabarofversespannable = AnnotatedString(khabarofverse)


                    val sourceone = harfspannble

                    val sourcetwo = khabarofversespannable
                    val tagonestyle = SpanStyle(
                        color = harfkana,
                        textDecoration = TextDecoration.Underline
                    )

                    val tagthreestyle = SpanStyle(
                        color = kanakhbar,
                        textDecoration = TextDecoration.Underline
                    )
                    val space = AnnotatedString(" ")

                    builder.append(sourceone)
                    builder.addStyle(tagonestyle, 0, harfofverse.length)
                    builder.append(space)
                    builder2.append(sourcetwo)
                    builder2.addStyle(tagthreestyle, 0, khabarofverse.length)

                    // builder2.append(sourcethree)
                    // builder2.addStyle(tagthreestyle, 0, khabarofversespannable.length)


                    val annotatedString =
                        builder.toAnnotatedString() + space + builder2.toAnnotatedString()


                    val charSequence =
                        TextUtils.concat(harfspannble, " ", khabarofversespannable)
                    kanaarray.add(annotatedString)
                    val sb = StringBuilder()
                    val wordfrom = kana.harfwordno
                    var wordto: Int
                    val split =
                        khabarofverse.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    wordto = if (split.size == 1) {
                        kana.khabarstartwordno
                    } else {
                        kana.khabarendwordno
                    }
                    val isconnected = kana.khabarstartwordno - kana.harfwordno
                    if (isconnected == 1) {

                        val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!, wbwchoice)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(AnnotatedString(sb.toString()))
                    } else {
                        val wordfroms = kana.harfwordno

                        val list = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            wordfrom,
                            wordfroms
                        )
                        val from = kana.khabarstartwordno
                        var to = kana.khabarendwordno
                        if (to == 0) {
                            to = from
                        }

                        wbwselection(wbwchoice, sb, list)

                        //    sb.append(list).append("----");

                        val lists: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            from,
                            to
                        )
                        if (lists != null) {
                            for (w in lists) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w, wbwchoice)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(AnnotatedString(sb.toString()))
                    }
                } else if (b) {

                    harfspannble = AnnotatedString(harfofverse)
                    harfismspannable = AnnotatedString(ismofverse)

                    val sourceone = harfspannble

                    val sourcetwo = harfismspannable
                    val tagonestyle = SpanStyle(
                        color = harfkana,
                        textDecoration = TextDecoration.Underline
                    )

                    val tagtwostyle = SpanStyle(
                        color = kanaism,
                        textDecoration = TextDecoration.Underline
                    )
                    val space = AnnotatedString(" ")

                    builder.append(sourceone)
                    builder.addStyle(tagonestyle, 0, harfofverse.length)
                    builder.append(space)


                    builder1.append(sourcetwo)
                    builder1.addStyle(tagtwostyle, 0, harfismspannable.length)


                    val annotatedString =
                        builder.toAnnotatedString() + space + builder1.toAnnotatedString()








                    kanaarray.add(annotatedString)
                    //    kanaarray.add(SpannableString.valueOf(charSequence));
                    val sb = StringBuilder()
                    val ismorkhabarsb = StringBuilder()
                    val ismconnected = kana.ismkanastart - kana.indexend
                    val wordfrom = kana.harfwordno
                    val wordto = kana.ismwordo
                    if (ismconnected == 1) {

                        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w, wbwchoice)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(AnnotatedString(sb.toString()))
                    } else {
                        //  ArrayList<wbwentity> list = utils.getwbwQuranbTranslation(corpusSurahWord!!.get(0).corpus.getSurah(), corpusSurahWord!!.get(0).corpus.getAyah(), wordfroms, wordfroms);

                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w, wbwchoice)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in ismSword..ismEword) {
                                    ismorkhabarsb.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(ismorkhabarsb)
                        kanaarray.add(AnnotatedString(sb.toString()))
                    }
                } else if (isharfb) {
                    harfspannble = AnnotatedString(harfofverse)


                    val sourceone = harfspannble

                    val tagonestyle = SpanStyle(
                        color = harfkana,
                        textDecoration = TextDecoration.Underline
                    )


                    val space = AnnotatedString(" ")

                    builder.append(sourceone)
                    builder.addStyle(tagonestyle, 0, harfofverse.length)


                    val charSequence = TextUtils.concat(harfspannble)
                    kanaarray.add(builder.toAnnotatedString())
                    val wordfroms = kana.harfwordno

                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        wordfroms,
                        wordfroms
                    )
                    val sb = StringBuilder()
                    wbwSelection(wbwchoice, sb, list)

                    kanaarray.add(AnnotatedString(sb.toString()))
                }
            }
        }
    }

    private fun wbwSelection(
        wbwchoice: Int,
        sb: StringBuilder,
        list: List<wbwentity>?
    ) {
        when (wbwchoice) {
            0 -> sb.append(list!![0].en).append(".......")
            1 -> sb.append(list!![0].ur).append(".......")
            2 -> sb.append(list!![0].bn).append(".......")
            3 -> sb.append(list!![0].id).append(".......")
        }
    }


    private fun setNewNasb(
        hasbarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        wbwchoice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)

        val nasabarray = utils.getHarfNasbIndicesSurahAyah(chapterid, ayanumber)

        if (nasabarray != null) {
            for (nasbEntity in nasabarray) {
                var tagcolorone: Color? = null
                var tagcolortwo: Color? = null
                var tagcolorthree: Color? = null
                val builder = AnnotatedString.Builder()
                val builder1 = AnnotatedString.Builder()
                if (thememode) {
                    tagcolorone = Color(ComposeConstant.GREENDARK)
                    tagcolortwo = Color(ComposeConstant.BCYAN)
                    tagcolorthree = Color(android.graphics.Color.YELLOW)
                } else {
                    tagcolorone = Color(ComposeConstant.KASHMIRIGREEN)
                    tagcolortwo = Color(ComposeConstant.prussianblue)
                    tagcolorthree = Color(ComposeConstant.deepburnsienna)
                }


                var harfofverse: String
                var ismofverse: String
                var khabarofverse: String
                val indexstart = nasbEntity!!.indexstart
                val indexend = nasbEntity.indexend
                val ismstartindex = nasbEntity.ismstart
                val ismendindex = nasbEntity.ismend
                val khabarstartindex = nasbEntity.khabarstart
                val khabarendindex = nasbEntity.khabarend
                val quranverses: String = quran!![0].qurantext
                harfofverse = quranverses.substring(indexstart, indexend)
                ismofverse = quranverses.substring(ismstartindex, ismendindex)
                khabarofverse = quranverses.substring(khabarstartindex, khabarendindex)
                val isharfb = indexstart >= 0 && indexend > 0
                val isism = ismstartindex >= 0 && ismendindex > 0
                val iskhabar = khabarstartindex >= 0 && khabarendindex > 0
                val harfismkhabarFound = isharfb && isism && iskhabar
                val harfAndKhabrFound = isharfb && iskhabar
                val harfAndIsmOnly = isharfb && isism
                val harfword = nasbEntity.harfwordno
                val shartSword = nasbEntity.ismstartwordno
                val shartEword = nasbEntity.ismendwordno
                val jawbSword = nasbEntity.khabarstartwordno
                val jawabEword = nasbEntity.khabarendwordno
                val sb = StringBuilder()
                val khabarsb = StringBuilder()
                var harfspannble: AnnotatedString
                if (harfismkhabarFound) {
                    val isismkhabarconnected = nasbEntity.khabarstart - nasbEntity.ismend

                    val tagOneStyle = SpanStyle(color = tagcolorone, textDecoration = TextDecoration.Underline)
                    val tagTwoStyle = SpanStyle(color = tagcolortwo, textDecoration = TextDecoration.Underline)
                    val tagThreeStyle = SpanStyle(color = tagcolorthree, textDecoration = TextDecoration.Underline)

                    builder.append(quranverses)
                    builder.addStyle(tagOneStyle, indexstart, indexend)
                    builder.addStyle(tagTwoStyle, ismstartindex, ismendindex)
                    builder.addStyle(tagThreeStyle, khabarstartindex, khabarendindex)
                    hasbarray.add(builder.toAnnotatedString())

                    val sb = StringBuilder()

                    if (isismkhabarconnected == 1) {
                        val list = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah, harfword, jawabEword
                        )
                        if (list != null) {
                            list.forEach { w ->
                                sb.append(getSelectedTranslation(w!!, wbwchoice)).append(" ")
                            }
                            hasbarray.add(AnnotatedString(sb.toString()))
                        }
                    } else {
                        val wbwayah = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah
                        )
                        if (wbwayah != null) {
                            wbwayah.forEach { w ->
                                val temp = w?.let { getSelectedTranslation(it, wbwchoice) } ?: StringBuilder()
                                when (w?.wordno) {
                                    harfword -> sb.append(temp).append(" ")
                                    in shartSword..shartEword -> sb.append(temp).append(" ")
                                    in jawbSword..jawabEword -> khabarsb.append(temp).append(" ")
                                }
                            }
                            sb.append(".....").append(khabarsb)
                            hasbarray.add(AnnotatedString(sb.toString()))
                        }
                    }
                  //  hasbarray.add(AnnotatedString(sb.toString()))
                    //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
                } else if (harfAndKhabrFound) {
                    val tagOneStyle = SpanStyle(color = tagcolorone, textDecoration = TextDecoration.Underline)
                    val tagThreeStyle = SpanStyle(color = tagcolorthree, textDecoration = TextDecoration.Underline)

                    val harfSpannable = AnnotatedString(harfofverse)
                    val khabarSpannable = AnnotatedString(khabarofverse)

                    builder.append(harfSpannable)
                    builder.addStyle(tagOneStyle, 0, harfofverse.length)

                    builder1.append(khabarSpannable)
                    builder1.addStyle(tagThreeStyle, 0, khabarofverse.length)

                    val annotatedString = buildAnnotatedString {
                        append(builder.toAnnotatedString())
                        append(" ") // Add a space as an AnnotatedString
                        append(builder1.toAnnotatedString())
                    }
                    hasbarray.add(annotatedString)

                    val wordFrom = nasbEntity.harfwordno
                    val wordTo = if (khabarofverse.split("\\s").size == 1) nasbEntity.khabarstartwordno else nasbEntity.khabarendwordno

                    val sb = StringBuilder()
                    if (nasbEntity.khabarstart - nasbEntity.indexend == 1) {
                        val list = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            wordFrom,
                            wordTo
                        )
                        if (list != null) {
                            list.forEach { w ->
                                sb.append(getSelectedTranslation(w, wbwchoice)).append(" ")
                            }
                        }
                    } else {
                        val list = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            wordFrom,
                            wordFrom
                        )
                        wbwselection(wbwchoice, sb, list) // Assuming this function handles word translation based on wbwchoice

                        val lists = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            nasbEntity.khabarstartwordno,
                            wordTo
                        )
                        if (lists != null) {
                            lists.forEach { w ->
                                sb.append(getSelectedTranslation(w!!, wbwchoice)).append(" ")
                            }
                        }
                    }
                    hasbarray.add(AnnotatedString(sb.toString()))

                } else if (harfAndIsmOnly) {
                    val tagOneStyle = SpanStyle(color = tagcolorone, textDecoration = TextDecoration.Underline)
                    val tagTwoStyle = SpanStyle(color = tagcolortwo, textDecoration = TextDecoration.Underline)

                    builder.append(AnnotatedString(harfofverse))
                    builder.addStyle(tagOneStyle, 0, harfofverse.length)

                    builder1.append(AnnotatedString(ismofverse))
                    builder1.addStyle(tagTwoStyle, 0, ismofverse.length)

                    val annotatedString = buildAnnotatedString {
                        append(builder.toAnnotatedString())
                        append(" ")
                        append(builder1.toAnnotatedString())
                    }
                    hasbarray.add(annotatedString)

                    if (nasbEntity.ismstart - nasbEntity.indexend == 1) {
                        val list = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            nasbEntity.harfwordno,
                            nasbEntity.ismendwordno
                        )
                        if (list != null) {
                            val sb = StringBuilder()
                            list.forEach { w ->
                                sb.append(getSelectedTranslation(w!!, wbwchoice)).append(" ")
                            }
                            hasbarray.add(AnnotatedString(sb.toString()))
                        }
                    }
                } else if (isharfb) {
                    harfspannble = AnnotatedString(harfofverse)
                    val sourceone = harfspannble

                    builder.append(sourceone)
                    val tagonestyle = SpanStyle(
                        color = tagcolorone,
                        textDecoration = TextDecoration.Underline
                    )
                    builder.addStyle(tagonestyle, 0, harfofverse.length)

                    val charSequence = TextUtils.concat(harfspannble)
                    hasbarray.add(builder1.toAnnotatedString())
                    val wordfroms = nasbEntity.harfwordno

                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        wordfroms,
                        wordfroms
                    )
                    val sbss = StringBuffer()

                    wbwselection(wbwchoice, sb, list)

                    hasbarray.add(AnnotatedString(sbss.toString()))
                }

            }
        }
    }

    private fun setWordTranslation(
        sb: StringBuilder,
        list: List<wbwentity>?,
        wbwchoice: Int
    ) {
        sb.append(list!![0].en).append(".......")
        when (wbwchoice) {
            0 -> sb.append(list[0].en).append(".......")
            1 -> sb.append(list[0].ur).append(".......")
            2 -> sb.append(list[0].bn).append(".......")
            3 -> sb.append(list[0].id).append(".......")
        }
    }
    private fun setMausoof(
        mausoofsifaarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        choice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        val sifabySurahAyah = utils.getSifabySurahAyah(chapterid, ayanumber)
        val tagColor = if (thememode) ComposeConstant.sifaspansDark else ComposeConstant.sifaspansLight

        sifabySurahAyah?.forEach { shartEntity ->
            val quranVerses = quran!![0].qurantext
            val substr = quranVerses.substring(shartEntity.startindex, shartEntity.endindex)

            val builder = AnnotatedString.Builder(substr)
            builder.addStyle(
                SpanStyle(color = tagColor, textDecoration = TextDecoration.Underline),
                0,
                substr.length
            )
            mausoofsifaarray.add(builder.toAnnotatedString())

            val list = utils.getwbwQuranbTranslation(
                corpusSurahWord!![0].corpus.surah,
                corpusSurahWord!![0].corpus.ayah,
                shartEntity.wordno - 1,
                shartEntity.wordno
            )
            val sb = StringBuilder()
            list?.forEach { w ->
                sb.append(getSelectedTranslation(w, choice)).append(" ")
            }
            mausoofsifaarray.add(AnnotatedString(sb.toString()))
        }
    }
    private fun setMausoofs(
        mausoofsifaarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        choice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        val sifabySurahAyah: List<SifaEntity>? =
            utils.getSifabySurahAyah(chapterid, ayanumber)
        var tagcolor: Color? = null
        tagcolor = if (thememode) {
            ComposeConstant.sifaspansDark
        } else {
            ComposeConstant.sifaspansLight

        }

        if (sifabySurahAyah != null) {
            for (shartEntity in sifabySurahAyah) {
                val builder = AnnotatedString.Builder()
                val quranverses: String = quran!![0].qurantext

                val start = shartEntity.startindex
                val end = shartEntity.endindex
                //   builder.append(source)
                val tagonestyle = SpanStyle(
                    color = tagcolor,
                    textDecoration = TextDecoration.Underline
                )

                val substr =
                    quranverses.substring(shartEntity.startindex, shartEntity.endindex)
                builder.append(substr)
                builder.addStyle(
                    tagonestyle, 0,
                    substr.length
                )
                mausoofsifaarray.add(builder.toAnnotatedString())


                val sequence =
                    quranverses.substring(shartEntity.startindex, shartEntity.endindex)

                val wordfrom = shartEntity.wordno - 1
                val wordto = shartEntity.wordno
                val ssb = StringBuilder()
                val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    corpusSurahWord!![0].corpus.surah,
                    corpusSurahWord!![0].corpus.ayah,
                    wordfrom,
                    wordto
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w, choice)
                        ssb.append(temp).append(" ")
                    }
                }
                mausoofsifaarray.add(AnnotatedString(ssb.toString()))
            }
        }
    }

    private fun getFragmentTranslations(
        quranverses: String,
        charSequence: CharSequence,
    ): SpannableString {
//get the string firs wordno and last wordno
        val sb = StringBuilder()
        var firstwordindex = 0
        var lastwordindex = 0
        val split = GrammerFragmentsBottomSheet.SplitQuranVerses()
        val words = split.splitSingleVerse(quranverses)
        val trim = charSequence.toString().trim { it <= ' ' }
        val strings = trim.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val length = strings.size
        val firstword = strings[0]
        val lastword = strings[length - 1]
        for (w in words) {
            val wordsAr = w.wordsAr
            if (w.wordsAr == firstword) {
                firstwordindex = w.wordno
            }
            if (wordsAr == lastword) {
                lastwordindex = w.wordno
                break
            }
        }
        val utils = Utils(QuranGrammarApplication.context)
//if the agove is false incase of the punctutaion marks,strip the punctuation and reloop
        if (lastwordindex == 0) {
            for (ww in words) {
                var wwordsAr = ww.wordsAr
                val sala = wwordsAr!!.indexOf(ArabicLiterals.SALA)
                val qala = wwordsAr.indexOf(ArabicLiterals.QALA)
                val smalllam = wwordsAr.indexOf(ArabicLiterals.SMALLLAM)
                val smalljeem = wwordsAr.indexOf(ArabicLiterals.SMALLJEEM) //small high geem
                val b = sala != -1 || qala != -1 || smalljeem != -1 || smalllam != -1
                if (b) {
                    wwordsAr = wwordsAr.substring(0, wwordsAr.length - 1)
                }
                if (wwordsAr == firstword) {
                    firstwordindex = ww.wordno
                }
                if (wwordsAr == lastword) {
                    lastwordindex = ww.wordno
                    break
                }
            }
        }
        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
            corpusSurahWord!![0].corpus.surah,
            corpusSurahWord!![0].corpus.ayah,
            firstwordindex,
            lastwordindex
        )
        if (list != null) {
            for (tr in list) {
                getSelectedTranslation(tr, 0)
                sb.append(tr.en).append(" ")
            }
        }
        return SpannableString(sb)
    }

    val kana: List<SpannableString>
        get() {
            //newsetKana(kanaarray)
            return ArrayList()
        }



    private fun newSetShart(
        shartarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        choice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        val quranVerses = quran!![0].qurantext
        val shartList = utils.getShartSurahAyahNew(chapterid, ayanumber)

        val tagColorOne = if (thememode) Color(Constant.GOLD) else Color(ComposeConstant.FORESTGREEN)
        val tagColorTwo = if (thememode) Color(Constant.ORANGE400) else Color(Constant.KASHMIRIGREEN)
        val tagColorThree = if (thememode) Color(android.graphics.Color.CYAN) else Color(Constant.WHOTPINK)

        shartList?.forEach { shartEntity ->
            val harfOfVerse = quranVerses.substring(shartEntity.indexstart, shartEntity.indexend)
            val shartOfVerse = quranVerses.substring(shartEntity.shartindexstart, shartEntity.shartindexend)
            val jawabOfVerse = quranVerses.substring(shartEntity.jawabshartindexstart, shartEntity.jawabshartindexend)

            val hasHarf = shartEntity.indexstart >= 0 && shartEntity.indexend > 0
            val hasShart = shartEntity.shartindexstart >= 0 && shartEntity.shartindexend > 0
            val hasJawab = shartEntity.jawabshartindexstart >= 0 && shartEntity.jawabshartindexend > 0

            val builder = AnnotatedString.Builder()
            if (hasHarf) {
                builder.append(AnnotatedString(harfOfVerse))
                builder.addStyle(
                    SpanStyle(color = tagColorOne, textDecoration = TextDecoration.Underline),
                    builder.length - harfOfVerse.length,
                    builder.length
                )
            }
            if (hasShart) {
                builder.append(AnnotatedString(shartOfVerse))
                builder.addStyle(
                    SpanStyle(color = tagColorTwo, textDecoration = TextDecoration.Underline),
                    builder.length - shartOfVerse.length,
                    builder.length
                )
            }
            if (hasJawab) {
                builder.append(AnnotatedString(jawabOfVerse))
                builder.addStyle(
                    SpanStyle(color = tagColorThree, textDecoration = TextDecoration.Underline),
                    builder.length - jawabOfVerse.length,
                    builder.length
                )
            }
            shartarray.add(builder.toAnnotatedString())

            val sb = StringBuilder()
            if (hasHarf && hasShart && hasJawab) {
                if (shartEntity.jawabshartindexstart - shartEntity.shartindexend == 1) {
                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        shartEntity.harfwordno,
                        shartEntity.jawabendwordno
                    )
                    list?.forEach { w ->
                        sb.append(getSelectedTranslation(w, choice)).append(" ")
                    }
                } else {
                    val wbwAyah = utils.getwbwQuranBySurahAyah(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah
                    )
                    val sbJawab = StringBuilder()
                    wbwAyah?.forEach { w ->
                        val temp = getSelectedTranslation(w, choice)
                        when (w.wordno) {
                            shartEntity.harfwordno -> sb.append(temp).append(" ")
                            in shartEntity.shartstatwordno..shartEntity.shartendwordno -> sb.append(temp).append(" ")
                            in shartEntity.jawabstartwordno..shartEntity.jawabendwordno -> sbJawab.append(temp).append(" ")
                        }
                    }
                    sb.append(".....").append(sbJawab)
                }
                shartarray.add(AnnotatedString(sb.toString()))
            } else if (hasHarf && hasShart) {
                if (shartEntity.shartindexstart - shartEntity.indexend == 1) {
                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        shartEntity.harfwordno,
                        shartEntity.shartendwordno
                    )
                    list?.forEach { w ->
                        sb.append(getSelectedTranslation(w, choice)).append(" ")
                    }
                } else {
                    val wbwAyah = utils.getwbwQuranBySurahAyah(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah
                    )
                    wbwAyah?.forEach { w ->
                        val temp = getSelectedTranslation(w, choice)
                        when (w.wordno) {
                            shartEntity.harfwordno -> sb.append(temp).append(" ")
                            in shartEntity.shartstatwordno..shartEntity.shartendwordno -> sb.append(temp).append(" ")
                        }
                    }
                    sb.append(".....")
                }
                shartarray.add(AnnotatedString(sb.toString()))
            } else if (hasHarf) {
                val charSequence = TextUtils.concat(harfOfVerse)
                val trstr = getFragmentTranslations(quranVerses, charSequence)
                shartarray.add(AnnotatedString(trstr.toString()))
            }
        }
    }


    private fun newSetSharts(
        shartarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        choice: Int,
        chapterid: Int,
        ayanumber: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        val quranverses: String = quran!![0].qurantext
        val shart: List<NewShartEntity>? =
            utils.getShartSurahAyahNew(chapterid, ayanumber)

        var sb: StringBuilder
        val sbjawab = StringBuilder()
        val builder = AnnotatedString.Builder()
        var tagcolorone: Color? = null
        var tagcolortwo: Color? = null
        var tagcolorthree: Color? = null


//   builder.append(source)


        if (thememode) {
            tagcolorone = Color(Constant.GOLD)
            tagcolortwo = Color(Constant.ORANGE400)
            tagcolorthree = Color(android.graphics.Color.CYAN)
        } else {
            tagcolorone = Color(ComposeConstant.FORESTGREEN)
            tagcolortwo = Color(Constant.KASHMIRIGREEN)
            tagcolorthree = Color(Constant.WHOTPINK)
        }

        if (ayanumber == 23) {
            println("check")
        }
        if (shart != null) {
            for (shartEntity in shart) {
                var harfofverse: String
                var shartofverse: String
                var jawabofverrse: String
                sb = StringBuilder()
                val indexstart = shartEntity.indexstart
                val indexend = shartEntity.indexend
                val shartindexstart = shartEntity.shartindexstart
                val shartindexend = shartEntity.shartindexend
                val jawabstart = shartEntity.jawabshartindexstart
                val jawabend = shartEntity.jawabshartindexend
                val harfword = shartEntity.harfwordno
                val shartSword = shartEntity.shartstatwordno
                val shartEword = shartEntity.shartendwordno
                val jawbSword = shartEntity.jawabstartwordno
                val jawabEword = shartEntity.jawabendwordno
                harfofverse = quranverses.substring(indexstart, indexend)
                shartofverse = quranverses.substring(shartindexstart, shartindexend)
                jawabofverrse = quranverses.substring(jawabstart, jawabend)
                val isharfb = indexstart >= 0 && indexend > 0
                val isshart = shartindexstart >= 0 && shartindexend > 0
                val isjawab = jawabstart >= 0 && jawabend > 0
                val a = isharfb && isshart && isjawab
                val b = isharfb && isshart
                var harfspannble: AnnotatedString
                var shartspoannable: AnnotatedString
                var jawabshartspannable: AnnotatedString

                if (a) {
                    harfspannble = AnnotatedString(harfofverse)
                    shartspoannable = AnnotatedString(shartofverse)
                    jawabshartspannable = AnnotatedString(jawabofverrse)
                    val space = AnnotatedString(" ")
                    val length = space.length
                    val sourceone = harfspannble + space
                    val sourcetwo = shartspoannable + space
                    val sourcethree = jawabshartspannable
                    val tagonestyle = SpanStyle(
                        color = tagcolorone,
                        textDecoration = TextDecoration.Underline
                    )
                    val tagtwostyle = SpanStyle(
                        color = tagcolortwo,
                        textDecoration = TextDecoration.Underline
                    )
                    val tagthreestyle = SpanStyle(
                        color = tagcolorthree,
                        textDecoration = TextDecoration.Underline
                    )
                    builder.append(quranverses)

                    if (indexstart == 0 || indexstart > 0) {
                        builder.addStyle(
                            style = SpanStyle(
                                color = tagcolorone,
                                textDecoration = TextDecoration.Underline
                            ), start = indexstart, end = indexend
                        )

                    }
                    if (shartindexstart == 0 || shartindexstart > 0) {
                        builder.addStyle(
                            style = SpanStyle(
                                color = tagcolortwo,
                                textDecoration = TextDecoration.Underline
                            ), start = shartindexstart, end = shartindexend
                        )
                    }


                    builder.addStyle(
                        style = SpanStyle(
                            color = tagcolorthree,
                            textDecoration = TextDecoration.Underline
                        ), start = jawabstart, end = jawabend
                    )


                    /*              builder.append(sourceone)
                                builder.addStyle(tagonestyle, 0,  harfspannble.length+length)
                                builder.append(sourcetwo)
                                builder.addStyle(tagtwostyle, 0,  shartspoannable.length+length)
                                builder.append(sourcethree)
                                builder.addStyle(tagthreestyle, 0,  jawabshartspannable.length+length)
                                val str=AnnotatedString(" ")

                                val annotatedString = harfspannble +str+ shartspoannable +str+ jawabshartspannable
                    */

                    //     val charSequence =
                    //    TextUtils.concat(harfspannble, " ", shartspoannable, " ", jawabshartspannable)
                    shartarray.add(builder.toAnnotatedString())
                    val connected = jawabstart - shartindexend
                    if (connected == 1) {
                        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah, harfword, jawabEword
                        )
                        if (list != null) {
                            for (w in list) {
                                val temp: StringBuilder = getSelectedTranslation(w, choice)
                                sb.append(temp).append(" ")
                            }
                        }
                        //    trstr1 = getFragmentTranslations(quranverses, sb, charSequence, false);
                        shartarray.add(AnnotatedString(sb.toString()))
                    } else {
                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                val temp: StringBuilder = getSelectedTranslation(w, choice)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in shartSword..shartEword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in jawbSword..jawabEword) {
                                    //     sb. append("... ");
                                    sbjawab.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(sbjawab)
                        shartarray.add(AnnotatedString(sb.toString()))
                    }
                } else if (b) {
                    harfspannble = AnnotatedString(harfofverse)
                    shartspoannable = AnnotatedString(shartofverse)
                    val sourceone = harfspannble
                    val sourcetwo = shartspoannable
                    val tagonestyle = SpanStyle(
                        color = tagcolorone,
                        textDecoration = TextDecoration.Underline
                    )
                    val tagtwostyle = SpanStyle(
                        color = tagcolortwo,
                        textDecoration = TextDecoration.Underline
                    )

                    val space = AnnotatedString(" ")
                    val length = space.length
                    builder.append(sourceone)
                    builder.addStyle(tagonestyle, 0, harfofverse.length)
                    builder.append(sourcetwo)
                    builder.addStyle(tagtwostyle, 0, shartofverse.length)


                    val annotatedString = harfspannble + space + shartspoannable


                    val charSequence = TextUtils.concat(harfspannble, " ", shartspoannable)
                    shartarray.add(annotatedString)
                    //    shartarray.add(trstr);
                    if (shartindexstart - indexend == 1) {
                        val harfnshart: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            harfword,
                            shartEword
                        )
                        if (harfnshart != null) {
                            for (w in harfnshart) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w, choice)
                                getSelectedTranslation(w, choice)
                                sb.append(temp).append(" ")
                            }
                        }
                    } else {
                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                val temp: StringBuilder = getSelectedTranslation(w, choice)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in shartSword..shartEword) {
                                    sb.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(sbjawab)
                        //   shartarray.add(SpannableString.valueOf(sb.toString()));
                    }
                    shartarray.add(AnnotatedString(sb.toString()))
                } else if (isharfb) {
                    harfspannble = AnnotatedString(harfofverse)
                    val sourceone = harfspannble

                    val tagonestyle = SpanStyle(
                        color = tagcolorone,
                        textDecoration = TextDecoration.Underline
                    )



                    builder.append(sourceone)
                    builder.addStyle(tagonestyle, 0, harfofverse.length)


                    val annotatedString = harfspannble


                    val charSequence = TextUtils.concat(harfspannble)
                    val trstr = getFragmentTranslations(quranverses, charSequence)
                    shartarray.add(annotatedString)
                    shartarray.add(AnnotatedString(trstr.toString()))
                }
            }
        }
    }
    private fun setMudhaf(
        mudhafarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        choice: Int,
        chapterid: Int,
        verseid: Int
    ) {
        val utils = Utils(QuranGrammarApplication.context)
        val mudhafSurahAyah = utils.getMudhafSurahAyahNew(chapterid, verseid)
        val tagColor = if (thememode) Color(ComposeConstant.WBURNTUMBER) else Color(ComposeConstant.MIDNIGHTBLUE)

        mudhafSurahAyah?.forEach { mudhafEntity ->
            val quranVerses = quran!![0].qurantext
            val substr = quranVerses.substring(mudhafEntity.startindex, mudhafEntity.endindex)

            val builder = AnnotatedString.Builder(substr)
            builder.addStyle(
                SpanStyle(color = tagColor, textDecoration = TextDecoration.Underline),
                0,
                substr.length
            )
            mudhafarray.add(builder.toAnnotatedString())

            val words = substr.split("\\s")
            val sb = StringBuilder()
            if (words.size == 2) {
                val list = utils.getwbwQuranbTranslation(
                    corpusSurahWord!![0].corpus.surah,
                    corpusSurahWord!![0].corpus.ayah,
                    mudhafEntity.wordfrom,
                    mudhafEntity.wordto
                )
                list?.forEach { w ->
                    sb.append(getSelectedTranslation(w, choice)).append(" ")
                }
            } else {
                val list = utils.getwbwQuranbTranslation(
                    corpusSurahWord!![0].corpus.surah,
                    corpusSurahWord!![0].corpus.ayah,
                    mudhafEntity.wordto,
                    mudhafEntity.wordto
                )
                if (choice == 0) {
                    wbwselection(choice, sb, list)
                }
            }
            mudhafarray.add(AnnotatedString(sb.toString()))
        }
    }
    private fun setMudhafs(
        mudhafarray: MutableList<AnnotatedString>,
        thememode: Boolean,
        choice: Int,
        chapterid: Int,
        verseid: Int
    ) {

        val utils = Utils(QuranGrammarApplication.context)
        val mudhafSurahAyah: List<NewMudhafEntity>? =
            utils.getMudhafSurahAyahNew(chapterid, verseid)
        var tagonecolor: Color? = null
        tagonecolor = if (thememode) {
            Color(ComposeConstant.WBURNTUMBER)

        } else {
            Color(ComposeConstant.MIDNIGHTBLUE)
        }

        if (mudhafSurahAyah != null) {
            for (mudhafEntity in mudhafSurahAyah) {
                val builder = AnnotatedString.Builder()
                val quranverses: String = quran!![0].qurantext

                val start = mudhafEntity.startindex
                val end = mudhafEntity.endindex
                //   builder.append(source)
                val tagonestyle = SpanStyle(
                    color = tagonecolor,
                    textDecoration = TextDecoration.Underline
                )

                val substr =
                    quranverses.substring(mudhafEntity.startindex, mudhafEntity.endindex)
                builder.append(substr)
                builder.addStyle(
                    tagonestyle, 0,
                    substr.length
                )
                mudhafarray.add(builder.toAnnotatedString())

                val sequence =
                    quranverses.subSequence(mudhafEntity.startindex, mudhafEntity.endindex)
                val str = sequence.toString()
                val source = sequence


                //    mudhafarray.add(AnnotatedString(sequence.toString()) )

                val sb = StringBuilder()
                val wordfrom = mudhafEntity.wordfrom
                val wordto = mudhafEntity.wordto
                val strings =
                    sequence.toString().split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val ssb = StringBuilder()
                if (strings.size == 2) {
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w, choice)
                            ssb.append(temp).append(" ")
                        }
                    }
                    mudhafarray.add(AnnotatedString(ssb.toString()))
                } else {
                    val list = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].corpus.surah,
                        corpusSurahWord!![0].corpus.ayah,
                        wordto,
                        wordto
                    )
                    if (choice == 0)
                        wbwselection(choice, sb, list)
                    mudhafarray.add(AnnotatedString(sb.toString()))
                }
            }
        }
    }

    private fun wbwselection(
        wbwchoice: Int,
        sb: StringBuilder,
        list: List<wbwentity>?
    ) {
        when (wbwchoice) {
            0 -> sb.append(list!![0].en).append(".......")
            1 -> sb.append(list!![0].ur).append(".......")
            2 -> sb.append(list!![0].bn).append(".......")
            3 -> sb.append(list!![0].id).append(".......")
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


    /*    private fun newSetShart(shartarray: MutableList<AnnotatedString>) {
        val utils = Utils(QuranGrammarApplication.context!!)
        val quranverses: String = quran!![0].qurantext
        val shart: List<NewShartEntity>? =
            utils.getShartSurahAyahNew(chapterid, ayanumber)
        // String quranverses = corpusSurahWord!!.get(0).corpus.getQurantext();
        var sb: StringBuilder
        val sbjawab = StringBuilder()

        if (shart != null) {
            for (shartEntity in shart) {
                var harfofverse: String
                var shartofverse: String
                var jawabofverrse: String
                sb = StringBuilder()
                val indexstart = shartEntity.indexstart
                val indexend = shartEntity.indexend
                val shartindexstart = shartEntity.shartindexstart
                val shartindexend = shartEntity.shartindexend
                val jawabstart = shartEntity.jawabshartindexstart
                val jawabend = shartEntity.jawabshartindexend
                val harfword = shartEntity.harfwordno
                val shartSword = shartEntity.shartstatwordno
                val shartEword = shartEntity.shartendwordno
                val jawbSword = shartEntity.jawabstartwordno
                val jawabEword = shartEntity.jawabendwordno
                harfofverse = quranverses.substring(indexstart, indexend)
                shartofverse = quranverses.substring(shartindexstart, shartindexend)
                jawabofverrse = quranverses.substring(jawabstart, jawabend)
                val isharfb = indexstart >= 0 && indexend > 0
                val isshart = shartindexstart >= 0 && shartindexend > 0
                val isjawab = jawabstart >= 0 && jawabend > 0
                val a = isharfb && isshart && isjawab
                val b = isharfb && isshart
                var harfspannble: SpannableString
                var shartspoannable: SpannableString
                var jawabshartspannable: SpannableString
                if (dark) {
                    Constant.harfshartspanDark = ForegroundColorSpan(Constant.GOLD)
                    Constant.shartspanDark = ForegroundColorSpan(Constant.ORANGE400)
                    Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
                } else {
                    Constant.harfshartspanDark = ForegroundColorSpan(Constant.FORESTGREEN)
                    Constant.shartspanDark = ForegroundColorSpan(Constant.KASHMIRIGREEN)
                    Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
                }
                if (a) {
                    harfspannble = SpannableString(harfofverse)
                    shartspoannable = SpannableString(shartofverse)
                    jawabshartspannable = SpannableString(jawabofverrse)
                    if (dark) {
                        //    harfshartspanDark = new ForegroundColorSpan(MIDNIGHTBLUE);
                        Constant.shartspanDark = ForegroundColorSpan(Constant.ORANGE400)
                        Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
                    } else {
                        //   harfshartspanDark = new ForegroundColorSpan(FORESTGREEN);
                        Constant.shartspanDark = ForegroundColorSpan(Constant.GREENDARK)
                        Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
                    }
                    harfspannble.setSpan(
                        Constant.harfshartspanDark,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    shartspoannable.setSpan(
                        Constant.shartspanDark,
                        0,
                        shartofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    jawabshartspannable.setSpan(
                        Constant.jawabshartspanDark,
                        0,
                        jawabofverrse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence =
                        TextUtils.concat(harfspannble, " ", shartspoannable, " ", jawabshartspannable)
                    shartarray.add(SpannableString.valueOf(charSequence))
                    val connected = jawabstart - shartindexend
                    if (connected == 1) {
                        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah, harfword, jawabEword
                        )
                        if (list != null) {
                            for (w in list) {
                                val temp: StringBuilder = getSelectedTranslation(w)
                                sb.append(temp).append(" ")
                            }
                        }
                        //    trstr1 = getFragmentTranslations(quranverses, sb, charSequence, false);
                        shartarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                val temp: StringBuilder = getSelectedTranslation(w)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in shartSword..shartEword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in jawbSword..jawabEword) {
                                    //     sb. append("... ");
                                    sbjawab.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(sbjawab)
                        shartarray.add(SpannableString.valueOf(sb.toString()))
                    }
                } else if (b) {
                    harfspannble = SpannableString(harfofverse)
                    shartspoannable = SpannableString(shartofverse)
                    harfspannble.setSpan(
                        Constant.harfshartspanDark,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    shartspoannable.setSpan(
                        Constant.shartspanDark,
                        0,
                        shartofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(harfspannble, " ", shartspoannable)
                    shartarray.add(SpannableString.valueOf(charSequence))
                    //    shartarray.add(trstr);
                    if (shartindexstart - indexend == 1) {
                        val harfnshart: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah,
                            harfword,
                            shartEword
                        )
                        if (harfnshart != null) {
                            for (w in harfnshart) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                getSelectedTranslation(w)
                                sb.append(temp).append(" ")
                            }
                        }
                    } else {
                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].corpus.surah,
                            corpusSurahWord!![0].corpus.ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                val temp: StringBuilder = getSelectedTranslation(w)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in shartSword..shartEword) {
                                    sb.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(sbjawab)
                        //   shartarray.add(SpannableString.valueOf(sb.toString()));
                    }
                    shartarray.add(SpannableString.valueOf(sb.toString()))
                } else if (isharfb) {
                    harfspannble = SpannableString(harfofverse)
                    harfspannble.setSpan(
                        Constant.harfshartspanDark,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(harfspannble)
                    val trstr = getFragmentTranslations(quranverses, charSequence)
                    shartarray.add(SpannableString.valueOf(charSequence))
                    shartarray.add(trstr)
                }
            }
        }
    }*/


    fun onItemClicked(itemId: Int) {
        itemIdsList.value = itemIdsList.value.toMutableList().also { list ->
            if (list.contains(itemId)) {
                list.remove(itemId)
            } else {
                list.add(itemId)
            }
        }
    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}

operator fun AnnotatedString.plus(other: AnnotatedString): AnnotatedString {
    return buildAnnotatedString {
        append(this@plus)
        append(other)
    }
}
