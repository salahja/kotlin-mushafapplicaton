package com.example.utility

import Utility.ArabicLiterals
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat

import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity

import com.example.Constant
import com.example.mushafconsolidated.Adapters.RevalationCity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.WordMorphologyDetails
import com.example.mushafconsolidated.model.QuranCorpusWbw
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils
import android.graphics.Color
import android.graphics.Paint
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.Constant.GOLD
import com.example.Constant.GREENDARK
import com.example.Constant.ORANGE400
import com.example.Constant.WHOTPINK
import com.example.Constant.harfinnaspanDark
import com.example.Constant.harfkhabarspanDark
import com.example.Constant.harfshartspanDark
import com.example.Constant.jawabshartspanDark
import com.example.Constant.shartspanDark
import com.example.mushafconsolidated.Entities.IllaPositive
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.fragments.GrammerFragmentsBottomSheet.SplitQuranVerses
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.utility.CorpusUtilityorig.Companion.dark
import kotlin.collections.getOrPut
import kotlin.text.indexOf
import kotlin.text.toIntOrNull

object QuranViewUtils {
    private val absoluteNegationCache = HashMap<Pair<Int, Int>, List<Int>>()
    private val sifaCache = HashMap<Pair<Int, Int>, MutableList<List<Int>>>()
    private val mudhafCache = HashMap<Pair<Int, Int>, MutableList<List<Int>>>()
    private lateinit var quranModel: QuranViewModel


    fun initialize(quranModel: QuranViewModel) {
        this.quranModel = quranModel // Initialize quranModel
    }

    fun extractInMaIllaPositiveSentences(
        corpus: List<CorpusEntity>,
        spannableVerse: String
    ): List<String> {
        val extractedSentences = mutableListOf<String>()

        val uniqueSentences = mutableSetOf<String>() // Set to track unique sentences

        for (i in corpus.indices) {
            val entry = corpus[i]

            // Check for "إِلَّا" with tagone == "EXP" or "RES"
            if ((entry.tagone == "EXP" || entry.tagone == "RES") && (entry.araone == "إِلَّا" || entry.araone == "إِلَّآ")) {
                var negationFound = false
                var negationStartIndex = -1

                // Loop backwards to find if any negation condition is present
                for (j in i - 1 downTo 0) {
                    val previousEntry = corpus[j]

                    // Check if any negation tag or word is found, and break if true (skip extraction)
                    if ((previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "إِنْ" || previousEntry.aratwo == "مَا" || previousEntry.aratwo == "مَآ" || previousEntry.aratwo == "مَّا")) ||
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "إِنْ" || previousEntry.araone == "مَا" || previousEntry.araone == "مَآ" || previousEntry.araone == "مَّا")) ||
                        (previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "لَآ" || previousEntry.aratwo == "لَا" || previousEntry.aratwo == "لَّا")) ||
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَآ" || previousEntry.araone == "لَا" || previousEntry.araone == "لَّا")) ||
                        (previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "لَنْ" || previousEntry.aratwo == "لَن" || previousEntry.aratwo == "لَّن")) ||
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَنْ" || previousEntry.araone == "لَن" || previousEntry.araone == "لَّن")) ||

                        (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "إِنْ" || previousEntry.arathree == "مَا" || previousEntry.arathree == "مَآ" || previousEntry.arathree == "مَّا")) ||
                        (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "إِنْ" || previousEntry.arafour == "مَا" || previousEntry.arafour == "مَآ" || previousEntry.arafour == "مَّا")) ||
                        (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "لَآ" || previousEntry.arathree == "لَا" || previousEntry.arathree == "لَّا")) ||
                        (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "لَآ" || previousEntry.arafour == "لَا" || previousEntry.arafour == "لَّا")) ||
                        (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "لَنْ" || previousEntry.arathree == "لَن" || previousEntry.arathree == "لَّن")) ||
                        (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "لَنْ" || previousEntry.arafour == "لَن" || previousEntry.arafour == "لَّن"))


                    ) {
                        // Negation condition found, skip this case
                        negationFound = true
                        break
                    }
                }

                // If no negation was found in the previous entries, proceed with extraction
                if (!negationFound) {
                    /*      val sentenceBuilder = StringBuilder()

                          // Capture 3 words before "إِلَّا"
                          val startWordNo =
                              corpus[maxOf(0, i -3)].wordno // Get up to 3 words before "إِلَّا"
                          for (k in maxOf(0, i - 3) until i) {
                              val word =
                                  "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                              sentenceBuilder.append(word).append(" ")
                          }*/

                    val sentenceBuilder = StringBuilder()

// Capture up to 4 words before "إِلَّا" or beginning of line
                    val startWordNo = corpus[maxOf(0, i - 4)].wordno
                    for (k in i - 1 downTo maxOf(0, i - 4)) { // Iterate in reverse
                        val word =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        sentenceBuilder.insert(0, "$word ") // Insert at the beginning
                    }


                    /*
                                        // Capture "إِلَّا" and 2 words after "إِلَّا"
                                        var endWordNo = startWordNo
                                        for (k in i until minOf(
                                            i + 2,
                                            corpus.size
                                        )) { // i is where "إِلَّا" is, capture 2 more after it
                                            val word =
                                                "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                                            sentenceBuilder.append(word).append(" ")

                                            endWordNo = corpus[k].wordno // Update the end word number
                                        }
                                        */

                    var endWordNo = startWordNo
                    var wordCount = 0

                    for (k in i until minOf(
                        i + 4,
                        corpus.size
                    )) { // Limit loop to 4 words or end of corpus
                        val word =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        sentenceBuilder.append(word).append(" ")

                        endWordNo = corpus[k].wordno
                        wordCount++
                    }


                    val fullSentenceStr = sentenceBuilder.toString().trim()

                    // Now find the combined sentence in the spannableVerse
                    val sentenceStartIndex = spannableVerse.indexOf(fullSentenceStr)
                    val sentenceEndIndex =
                        if (sentenceStartIndex != -1) sentenceStartIndex + fullSentenceStr.length else -1

                    // Check if this sentence is already added to avoid duplicates
                    if (sentenceStartIndex != -1 && !uniqueSentences.contains(fullSentenceStr)) {
                        val dataString =
                            "${entry.surah}|${entry.ayah}|$startWordNo|$endWordNo|$sentenceStartIndex|$sentenceEndIndex|$fullSentenceStr|$spannableVerse"
                        println("Searching for: $fullSentenceStr")

                        // Add the sentence to the set for uniqueness
                        uniqueSentences.add(fullSentenceStr)

                        // Add the unique sentence with word numbers and string indices
                        extractedSentences.add(dataString)
                    }
                }
            }
        }

        return extractedSentences
    }

    fun extractSentenceAndTranslationFromNasabIndices(
        corpus: List<CorpusEntity>,
        nasbEntity: NewNasbEntity,
        quranText: String

    ): List<String> {
        val utils = Utils(QuranGrammarApplication.context)
        var harfofverse: String
        var ismofverse: String
        var khabarofverse: String
        val indexstart = nasbEntity.indexstart
        val indexend = nasbEntity.indexend
        val ismstartindex = nasbEntity.ismstart
        val ismendindex = nasbEntity.ismend
        val khabarstartindex = nasbEntity.khabarstart
        val khabarendindex = nasbEntity.khabarend
        val quranverses: String = quranText
        harfofverse = quranverses.substring(indexstart, indexend)
        ismofverse = quranverses.substring(ismstartindex, ismendindex)
        khabarofverse = quranverses.substring(khabarstartindex, khabarendindex)
        val isharfb = indexstart >= 0 && indexend > 0
        val isism = ismstartindex >= 0 && ismendindex > 0
        val iskhabar = khabarstartindex >= 0 && khabarendindex > 0
        val allPartOfIsm = isharfb && isism && iskhabar
        val harfAandKhabar = isharfb && iskhabar
        val harfAndIsm = isharfb && isism
        val harfword = nasbEntity.harfwordno
        val ismstartwordno = nasbEntity.ismstartwordno
        val ismendwordno = nasbEntity.ismendwordno
        val jawbSword = nasbEntity.khabarstartwordno
        val jawabEword = nasbEntity.khabarendwordno
        val translationBuilder = StringBuilder()
        val khabarsb = StringBuilder()
        var harfspannble: SpannableString
        var harfismspannable: SpannableString
        var khabarofversespannable: SpannableString
        val hasbarray: MutableList<SpannableString> = ArrayList()
        var arabicstring = ""
        var typeoneindexstart = indexstart
        var typeoneindexend = khabarendindex
        var typeonewordfrom = harfword
        var typeonewordto = 0
        if (jawbSword == 0) {
            typeonewordto = harfword + 1
        } else {
            typeonewordto = jawbSword
        }
        var datastrfull = "" //harfword,jawabEword,indexstart,khabarendindex
        var dataharfism = ""//harfword,shartEword,indexstart,ismendindex
        var dataharfkhabar = ""//harfword,jawabEword,indexstart,khabarendindex,
        if (nasbEntity.surah == 5 && nasbEntity.ayah == 49) {
            println("check")
        }
        if (allPartOfIsm) {
            val isismkhabarconnected = nasbEntity.khabarstart - nasbEntity.ismend
            harfspannble = SpannableString(harfofverse)
            harfismspannable = SpannableString(ismofverse)
            khabarofversespannable = SpannableString(khabarofverse)


            if (isismkhabarconnected == 1) {
                val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity!!.surah,
                    nasbEntity!!.ayah, harfword, jawabEword
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
            } else {
                val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                    nasbEntity.surah,
                    nasbEntity.ayah
                )
                if (wbwayah != null) {
                    for (w in wbwayah) {
                        StringBuilder()
                        val temp: StringBuilder = w?.let { getSelectedTranslation(it) }!!
                        if (w.wordno == harfword) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in ismstartwordno..ismendwordno) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in jawbSword..jawabEword) {
                            //     sb. append("... ");
                            khabarsb.append(temp).append(" ")
                        }
                    }
                }
                translationBuilder.append(".....")
                translationBuilder.append(khabarsb)
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
            harfofverse = quranverses.substring(indexstart, indexend)
            ismofverse = quranverses.substring(ismstartindex, ismendindex)
            khabarofverse = quranverses.substring(khabarstartindex, khabarendindex)
            hasbarray.add(SpannableString.valueOf(harfofverse + ' ' + ismofverse + ' ' + khabarofverse))
            arabicstring = harfofverse + ' ' + ismofverse + ' ' + khabarofverse
            hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
        } else if (harfAandKhabar) {
            harfspannble = SpannableString(harfofverse)
            khabarofversespannable = SpannableString(khabarofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            khabarofversespannable.setSpan(
                jawabshartspanDark,
                0,
                khabarofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble, " ", khabarofversespannable)
            arabicstring = harfofverse + ' ' + khabarofverse
            //     StringBuilder sb = new StringBuilder();
            val wordfrom = nasbEntity.harfwordno
            var wordto: Int
            val split = khabarofverse.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            wordto = if (split.size == 1) {
                nasbEntity.khabarstartwordno
            } else {
                nasbEntity.khabarendwordno
            }
            val isconnected = nasbEntity.khabarstart - nasbEntity.indexend
            if (isconnected == 1) {
                val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    wordfrom,
                    wordto
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                hasbarray.add(SpannableString.valueOf(charSequence))
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            } else {
                val wordfroms = nasbEntity.harfwordno
                val list = utils.getwbwQuranbTranslationbyrange(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    wordfrom,
                    wordfroms
                )
                val from = nasbEntity.khabarstartwordno
                var to = nasbEntity.khabarendwordno
                if (to == 0) {
                    to = from
                }
                translationBuilder.append(list[0].en).append(".......")
                when ("en") {
                    "en" -> translationBuilder.append(list[0].en).append(".......")
                    "ur" -> translationBuilder.append(list[0].ur).append(".......")
                    "bn" -> translationBuilder.append(list[0].bn).append(".......")
                    "id" -> translationBuilder.append(list[0].id).append(".......")
                }


                val lists: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    from,
                    to
                )
                if (lists != null) {
                    for (w in lists) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
        } else if (harfAndIsm) {
            harfshartspanDark = ForegroundColorSpan(GOLD)
            shartspanDark = ForegroundColorSpan(Color.GREEN)
            jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            harfspannble = SpannableString(harfofverse)
            harfismspannable = SpannableString(ismofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            harfismspannable.setSpan(
                shartspanDark,
                0,
                ismofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequences = TextUtils.concat(
                harfspannble,
                " $harfismspannable"
            )
            hasbarray.add(SpannableString.valueOf(charSequences))
            arabicstring = harfofverse + ' ' + ismofverse
            //    kanaarray.add(SpannableString.valueOf(charSequence));
            val ismconnected = nasbEntity.ismstart - nasbEntity.indexend
            val wordfrom = nasbEntity.harfwordno
            val wordto = nasbEntity.ismendwordno
            if (ismconnected == 1) {
                val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    wordfrom,
                    wordto
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            } else {
                //    kanaarray.add(SpannableString.valueOf(list.get(0).getEn()));
                val from = nasbEntity.harfwordno
                val ismfrom = nasbEntity.ismstartwordno
                val ismto = nasbEntity.ismendwordno
                //     sb.append(list.get(0).getEn()).append("----");
                val harf: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    from,
                    from
                )
                val ism: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    ismfrom,
                    ismto
                )
                if (harf != null) {
                    for (w in harf) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                translationBuilder.append(".....")
                if (ism != null) {
                    for (w in ism) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                harfofverse = quranverses.substring(indexstart, indexend)
                ismofverse = quranverses.substring(ismstartindex, ismendindex)
                hasbarray.add(SpannableString(harfofverse + ' ' + ismofverse))
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
        } else if (isharfb) {
            harfspannble = SpannableString(harfofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble)
            hasbarray.add(SpannableString.valueOf(charSequence))
            arabicstring = harfofverse
            val wordfroms = nasbEntity.harfwordno

            val list = utils.getwbwQuranbTranslationbyrange(
                nasbEntity.surah,
                nasbEntity.ayah,
                wordfroms,
                wordfroms
            )


            val sbss = StringBuffer()
            when ("en") {
                "en" -> sbss.append(list[0].en).append(".......")
                "ur" -> sbss.append(list[0].ur).append(".......")
                "bn" -> sbss.append(list[0].bn).append(".......")
                "id" -> sbss.append(list[0].id).append("..........")
            }
            hasbarray.add(SpannableString(harfofverse))
            hasbarray.add(SpannableString.valueOf(sbss))
        }
        //  var datastrfull="" //harfword,jawabEword,indexstart,khabarendindex
        //  var dataharfism=""//harfword,shartEword,indexstart,ismendindex
        //  var datatypethreestr=""//harfword,harfword,indexstart,indexend,
        val result = mutableListOf<String>()
        if (allPartOfIsm) {
            val allpart = "allpart"
            val dataString =
                "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$khabarendindex|$harfword|$jawabEword|$arabicstring|$translationBuilder|$quranverses|$allpart"



            result.add(dataString)

        } else
            if (harfAndIsm) {
                //quranText.substring(indexstart,ismendindex)
                val harfism = "harfism-khabarmahdoof"
                val dataString =
                    "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$ismendindex|$harfword|$ismendwordno|$arabicstring|$translationBuilder|$quranverses|$harfism"
                result.add(dataString)
            } else if (isharfb && !harfAandKhabar && !harfAndIsm) {
                val isharf = "isharfmahdoof"
                val dataString =
                    "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$indexend|$harfword|$harfword|$arabicstring|$translationBuilder|$quranverses|$isharf"
                result.add(dataString)
            } else if (harfAandKhabar) { //   var dataharfkhabar=""//harfword,jawabEword,indexstart,khabarendindex,//might be zero hence khabarstartwordno  khabarendwordno
                val harfkhabar = "harfkhabar"
                if (jawbSword == 0) {
                    val hword = harfword + 1
                    val dataString =
                        "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$khabarendindex|$harfword|$hword|$arabicstring|$translationBuilder|$quranverses|$harfkhabar"
                    result.add(dataString)
                } else {
                    val dataString =
                        "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$khabarendindex|$harfword|$jawabEword|$arabicstring|$translationBuilder|$quranverses|$harfkhabar"
                    result.add(dataString)
                }
            }








        return result

    }


    private fun getWordTranslation(list: List<wbwentity>): String {
        val wbwEntity = list.get(0) // Assuming list is not null and has at least one element
        return when ("en") {
            "en" -> wbwEntity.en
            "ur" -> wbwEntity.ur!!
            "bn" -> wbwEntity.bn
            "id" -> wbwEntity.id.toString()
            else -> {
                wbwEntity.en
            }
        }
    }

    private fun appendTranslation(
        utils: Utils,
        sb: StringBuilder,
        wordByWordEntities: List<wbwentity?>?,
        wordRange: IntRange? = null
    ) {
        if (wordByWordEntities != null) {
            for (w in wordByWordEntities) {
                /*
                val temp: StringBuilder
if (w != null) {
    val translation = getSelectedTranslation(w)
    temp = if (translation != null) {
        translation
    } else {
        StringBuilder()
    }
} else {
    temp = StringBuilder()
}
                 */
                val temp = w?.let { getSelectedTranslation(it) }
                    ?: StringBuilder() // Handle null wbwentity
                if (w != null) {
                    if (wordRange == null || w.wordno in wordRange) {
                        sb.append(temp).append(" ")
                    }
                }
            }
        }
    }


    fun extractSentenceAndTranslationFromShartIndices(
        corpus: List<CorpusEntity>,
        shartEntity: NewShartEntity,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()
        val utils = Utils(QuranGrammarApplication.context)
        var harfofverse: String
        var shartofverse: String
        var jawabofverrse: String
        var sb = StringBuilder()
        val indexstart = shartEntity.indexstart
        val comment = shartEntity.comment
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
        harfofverse = quranText.substring(indexstart, indexend)
        shartofverse = quranText.substring(shartindexstart, shartindexend)
        jawabofverrse = quranText.substring(jawabstart, jawabend)
        val isharfb = indexstart >= 0 && indexend > 0
        val isshart = shartindexstart >= 0 && shartindexend > 0
        val isjawab = jawabstart >= 0 && jawabend > 0
        val allPartofShart = isharfb && isshart && isjawab
        val harfAndShartOnly = isharfb && isshart
        var harfspannble: SpannableString
        var shartspoannable: SpannableString
        var jawabshartspannable: SpannableString
        var whichwbw = "en"
        val sbjawab = StringBuilder()
        var htmlString = ""
        val shartarray: MutableList<SpannableString> = ArrayList()
        val prefs =
            android.preference.PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context)
        val preferences = prefs.getString("theme", "dark")
        val dark = preferences == "dark" || preferences == "blue" || preferences == "green"
        if (allPartofShart) {
            harfspannble = SpannableString(harfofverse)
            shartspoannable = SpannableString(shartofverse)
            jawabshartspannable = SpannableString(jawabofverrse)
            val connected = jawabstart - shartindexend
            if (dark) {
                //    harfshartspanDark = new ForegroundColorSpan(MIDNIGHTBLUE);
                shartspanDark = ForegroundColorSpan(ORANGE400)
                jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            } else {
                //   harfshartspanDark = new ForegroundColorSpan(FORESTGREEN);
                shartspanDark = ForegroundColorSpan(GREENDARK)
                jawabshartspanDark = ForegroundColorSpan(WHOTPINK)
            }
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            shartspoannable.setSpan(
                shartspanDark,
                0,
                shartofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            jawabshartspannable.setSpan(
                jawabshartspanDark,
                0,
                jawabofverrse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence =
                TextUtils.concat(
                    harfspannble,
                    " ",
                    shartspoannable,
                    " ",
                    jawabshartspannable
                )
            shartarray.add(SpannableString.valueOf(charSequence))
            //  htmlString = Html.toHtml(charSequence as Spanned?)
            htmlString = Html.toHtml(SpannableString.valueOf(charSequence))
            if (connected == 1) {
                val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    shartEntity.surah,
                    shartEntity.ayah, harfword, jawabEword
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
                    shartEntity.surah,
                    shartEntity.ayah
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

        } else if (harfAndShartOnly) {
            harfspannble = SpannableString(harfofverse)
            shartspoannable = SpannableString(shartofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            shartspoannable.setSpan(
                shartspanDark,
                0,
                shartofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble, " ", shartspoannable)
            shartarray.add(SpannableString.valueOf(charSequence))
            //    shartarray.add(trstr);
            if (shartindexstart - indexend == 1) {
                val harfnshart: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    shartEntity.surah,
                    shartEntity.ayah,
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
                    shartEntity.surah,
                    shartEntity.ayah
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
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble)
            val trstr = getFragmentTranslations(quranText, charSequence, shartEntity)
            shartarray.add(SpannableString.valueOf(charSequence))
            shartarray.add(trstr)
        }


        //        val indexstart = shartEntity.indexstart
        //        val indexend = shartEntity.indexend
        //        val shartindexstart = shartEntity.shartindexstart
        //        val shartindexend = shartEntity.shartindexend
        //        val jawabstart = shartEntity.jawabshartindexstart
        //        val jawabend = shartEntity.jawabshartindexend
        //        val harfword = shartEntity.harfwordno
        //        val shartSword = shartEntity.shartstatwordno
        //        val shartEword = shartEntity.shartendwordno
        //        val jawbSword = shartEntity.jawabstartwordno
        //        val jawabEword = shartEntity.jawabendwordno
        // Format the result string
        val english = shartarray.get(1)
        val dataString =
            "${shartEntity.surah}|${shartEntity.ayah}|$indexstart|$indexend|$shartindexstart|$shartindexend|$jawabstart|$jawabend|" +
                    "$harfword|$shartSword|$shartEword|$jawbSword|$jawabEword|$english|$comment"
        result.add(dataString)






        return result
    }

    private fun getFragmentTranslations(
        quranverses: String,
        charSequence: CharSequence,
        shartEntity: NewShartEntity,
    ): SpannableString {
        //get the string firs wordno and last wordno
        val sb = StringBuilder()
        var firstwordindex = 0
        var lastwordindex = 0
        val split = SplitQuranVerses()
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
            shartEntity.surah,
            shartEntity.ayah,
            firstwordindex,
            lastwordindex
        )
        if (list != null) {
            for (tr in list) {
                getSelectedTranslation(tr)
                sb.append(tr.en).append(" ")
            }
        }
        return SpannableString(sb)
    }

    private fun getSelectedTranslation(tr: wbwentity): StringBuilder {
        val sb = StringBuilder()
        when ("en") {
            "en" -> sb.append(tr.en)
            "ur" -> sb.append(tr.ur)
            "bn" -> sb.append(tr.bn)
            "id" -> sb.append(tr.id)
            else -> {

                sb.append("tr.en") // Example: Append a default value

            }
        }
        // sb.append(" ") // Uncomment if you need to add a space after the translation
        return sb
    }

    fun extractSentenceAndTranslationFromWordIndicesNewNasab(
        corpus: List<CorpusEntity>,
        wordInfo: NewNasbEntity,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()


        var startIndex = -1
        var endIndex = -1
        var currentWordIndex = 0
        val wordsInVerse =
            quranText.split("\\s+".toRegex()) // Split Arabic verse by whitespace to get individual words
        var endword = 0
        var startword = 0
        // Find the start and end index for the words based on wordfrom and wordto
        for ((i, word) in wordsInVerse.withIndex()) {

            if (wordInfo.khabarendwordno == 0) {
                endword = wordInfo.harfwordno + 1
            } else if (wordInfo.khabarendwordno == 1) {
                endword = wordInfo.harfwordno + 2
            } else {
                endword = wordInfo.khabarendwordno
            }
            if (wordInfo.harfwordno == 0) {
                startword = wordInfo.ismstartwordno

            } else {
                startword = wordInfo.harfwordno
            }
            if (currentWordIndex + 1 == startword) {
                startIndex = quranText.indexOf(word)
            }
            if (currentWordIndex + 1 == endword) {
                endIndex = quranText.indexOf(word) + word.length
                break
            }
            currentWordIndex++
        }
        var extractedSentence = ""
        if (startIndex != -1 && endIndex != -1) {
            // Extract the Arabic sentence between the start and end indexes
            try {
                extractedSentence = quranText.substring(startIndex, endIndex).trim()
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
                println(wordInfo.surah + wordInfo.ayah)
                println(quranText)
            }


            // Extract translation using wordfrom and wordnoto
            val translationBuilder = StringBuilder()
            val arabictranslationBuilder = StringBuilder()
            for (entry in corpus) {
                if (entry.surah == 2 && entry.ayah == 13) {
                    println("check")
                }
                if (entry.wordno in startword..endword) {
                    translationBuilder.append(entry.en).append(" ")
                    arabictranslationBuilder.append(entry.araone)
                        .append(entry.aratwo)
                        .append(entry.arathree)
                        .append(entry.arafour)
                        .append(entry.arafive).append(" ")
                }
            }
            val extractedTranslation = translationBuilder.toString().trim()
            val arabictrimmed = arabictranslationBuilder.toString().trim()
            var type = ""
            try {
                if (corpus[startword - 1].tagone == "ACC" && corpus[startword].tagone == "PRON") {
                    type = "nasabone"
                } else {
                    type = "nasabtwo"
                }
            } catch (e: IndexOutOfBoundsException) {
                type = "unknown"
            }

            // Format the result string
            if (extractedSentence.isEmpty()) {
                val dataString =
                    "${wordInfo.surah}|${wordInfo.ayah}|$startword|$endword|$startIndex|$endIndex|$arabictrimmed|$type|$extractedTranslation|$quranText"
                result.add(dataString)
            } else {
                val dataString =
                    "${wordInfo.surah}|${wordInfo.ayah}|$startword|$endword|$startIndex|$endIndex|$extractedSentence|$type|$extractedTranslation|$quranText"
                result.add(dataString)
            }

        } else {
            val trans = ""
            val dataString =
                "${wordInfo.surah}|${wordInfo.ayah}|$startword|$endword|$startIndex|$endIndex|$extractedSentence|$trans|$quranText"
            // Handle the case when startIndex or endIndex is not found
            result.add(dataString)
        }


        return result
    }

    fun extractSentenceAndTranslationFromWordIndices(
        corpus: List<CorpusEntity>,
        wordInfo: IllaPositive,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()


        var startIndex = -1
        var endIndex = -1
        var currentWordIndex = 0
        val wordsInVerse =
            quranText.split("\\s+".toRegex()) // Split Arabic verse by whitespace to get individual words

        // Find the start and end index for the words based on wordfrom and wordto
        for ((i, word) in wordsInVerse.withIndex()) {
            if (currentWordIndex + 1 == wordInfo.wordfrom) {
                startIndex = quranText.indexOf(word)
            }
            if (currentWordIndex + 1 == wordInfo.wordto) {
                endIndex = quranText.indexOf(word) + word.length
                break
            }
            currentWordIndex++
        }
        var extractedSentence = ""
        if (startIndex != -1 && endIndex != -1) {
            // Extract the Arabic sentence between the start and end indexes
            try {
                extractedSentence = quranText.substring(startIndex, endIndex).trim()
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
                println(wordInfo.surahid + wordInfo.ayahid)
                println(quranText)
            }


            // Extract translation using wordfrom and wordnoto
            val translationBuilder = StringBuilder()
            for (entry in corpus) {
                if (entry.wordno in wordInfo.wordfrom..wordInfo.wordto) {
                    translationBuilder.append("${entry.en} ").append(" ")
                }
            }
            val extractedTranslation = translationBuilder.toString().trim()

            // Format the result string
            val dataString =
                "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|${wordInfo.wordto}|$startIndex|$endIndex|$extractedSentence|$extractedTranslation|$quranText"
            result.add(dataString)
        } else {
            // Handle the case when startIndex or endIndex is not found
            result.add("Error: Couldn't find indices for Surah ${wordInfo.surahid}, Ayah ${wordInfo.ayahid}, Words ${wordInfo.wordfrom} to ${wordInfo.wordto}")
        }


        return result
    }


    fun extractInMaIllaNegativeSentences(
        corpus: List<CorpusEntity>,
        spannableVerse: String
    ): List<String> {
        val extractedSentences = mutableListOf<String>()
        val uniqueSentences = mutableSetOf<String>() // Set to track unique sentences
        var eligibleNegationCount = 0 // To track valid "إِن" occurrences

        for (i in corpus.indices) {
            val entry = corpus[i]
            if (entry.surah == 11 && entry.ayah == 81) {
                println(spannableVerse)
            }

            // Check for "إِلَّا" with tagone == "EXP" or "RES"
            if ((entry.tagone == "EXP" || entry.tagone == "RES") && (entry.araone == "إِلَّا" || entry.araone == "إِلَّآ")) {
                var negationFound = false
                var startIndex = -1
//   listOf("لَّن", "لَن", "لَنْ", "وَلَن", "أَلَّن", "فَلَن", "وَلَنْ", "فَلَنْ")
                // Loop backwards to find valid "إِن" before "إِلَّا"
                for (j in i - 1 downTo 0) {
                    val previousEntry = corpus[j]

                    // Check if "إِن" or "مَا" qualifies as a valid negation for extraction
                    if ((previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "إِنْ" || previousEntry.aratwo == "مَا") || previousEntry.aratwo == "مَآ" || previousEntry.aratwo == "مَّا") ||
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "إِنْ" || previousEntry.araone == "مَا" || previousEntry.araone == "مَآ" || previousEntry.araone == "مَّا")) ||
                        (previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "لَآ" || previousEntry.aratwo == "لَا" || previousEntry.aratwo == "لَّا")) ||
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَآ" || previousEntry.araone == "لَا" || previousEntry.araone == "لَّا")) ||
                        (previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "لَنْ" || previousEntry.aratwo == "لَن" || previousEntry.aratwo == "لَّن")) ||
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَنْ" || previousEntry.araone == "لَن" || previousEntry.araone == "لَّن"))


                    ) {

                        eligibleNegationCount += 1 // Increment the valid "إِن" or "مَا" occurrence count

                        // Allow extraction for all valid occurrences (not just second or third)
                        if (eligibleNegationCount >= 1) {
                            negationFound = true
                            startIndex = j // Capture the index where this valid negation was found
                            break
                        }
                    }
                }

                // If a valid negation "إِن" or "مَا" is found, start capturing words from that point
                if (negationFound && startIndex != -1) {
                    val sb = StringBuilder()
                    val startWordNo = corpus[startIndex].wordno // Capture starting word number
                    var endWordNo = startWordNo

                    // Build the full sentence to search for in the spannableVerse
                    val fullSentence = StringBuilder()
                    for (k in startIndex until minOf(
                        i + 3,
                        corpus.size
                    )) { // Capture up to 3 words after "إِلَّا"
                        val completeWord =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        fullSentence.append(completeWord).append(" ")

                        // Update the end word number as we progress
                        endWordNo = corpus[k].wordno
                    }

                    val fullSentenceStr = fullSentence.toString().trim()

                    // Now find the combined sentence in the spannableVerse
                    val sentenceStartIndex = spannableVerse.indexOf(fullSentenceStr)
                    val sentenceEndIndex =
                        if (sentenceStartIndex != -1) sentenceStartIndex + fullSentenceStr.length else -1

                    // Check if this sentence is already added to avoid duplicates
                    if (sentenceStartIndex != -1 && !uniqueSentences.contains(fullSentenceStr)) {
                        val extracted =
                            spannableVerse.subSequence(sentenceStartIndex, sentenceEndIndex)
                        val dataString =
                            "${entry.surah}|${entry.ayah}|${startWordNo}| ${endWordNo}|$sentenceStartIndex|$sentenceEndIndex|$extracted|$spannableVerse"
                        println("Searching for: $fullSentenceStr")

                        // Add the sentence to the set for uniqueness
                        uniqueSentences.add(fullSentenceStr)

                        // Add the unique sentence with word numbers and string indices
                        extractedSentences.add(dataString)
                    }
                }
            }
        }

        return extractedSentences
    }


    // Function to load and cache Absolute Negation Data
    fun cacheAbsoluteNegationData(
        quranModel: QuranViewModel,
        surah: Int,
        absoluteNegationCache: MutableMap<Pair<Int, Int>, List<Int>>
    ) {
        val absoluteNegationData = quranModel.getAbsoluteNegationFilterSurah(surah)
        for (data in absoluteNegationData) {
            val key = Pair(data.surahid, data.ayahid)
            val indexes = listOf(data.startindex, data.endindex)
            absoluteNegationCache[key] = indexes
        }
    }

    // Function to load and cache Sifa Data
    fun cacheSifaData(
        quranModel: QuranViewModel,
        surah: Int,
        sifaCache: MutableMap<Pair<Int, Int>, MutableList<List<Int>>>
    ) {
        val sifaData = quranModel.getSifaMousoofFileterSurahType(surah, "sifa")
        for (data in sifaData) {
            val key = Pair(data.surah, data.ayah)
            val indexes = listOf(data.startindex, data.endindex)
            sifaCache.getOrPut(key) { mutableListOf() }.add(indexes)
        }
    }

    // Function to load and cache Mudhaf Data
    fun cacheMudhafData(
        quranModel: QuranViewModel,
        surah: Int,
        mudhafCache: HashMap<Pair<Int, Int>, MutableList<List<Int>>>
    ) {
        val mudhafData = quranModel.getSifaMousoofFileterSurahType(surah, "mudhaf")
        for (data in mudhafData) {

            val key = Pair(data.surah, data.ayah)
            val indexes = listOf(data.startindex, data.endindex)
            mudhafCache.getOrPut(key) { mutableListOf() }.add(indexes)
        }
    }


    fun cacheMansubat(
        quranModel: QuranViewModel,
        surah: Int,
        mansubatCache: MutableMap<Int, MutableMap<Int, MutableList<SpannableString>>>,
        isNightmode: String
    ) {
        val negantionData = quranModel.getIMBATFilterSurah(surah)
        val futureTenceCache: MutableMap<String, List<List<SpannableString>>> = mutableMapOf()
        for (data in negantionData) {
            val surahid = data.surahid
            val ayahid = data.ayahid
            val arabicString = data.word
            //  val englishString = data.englishtext
            var type = data.type
            type += " "

            val spannableString = SpannableString(arabicString)
            dark = isNightmode == "dark" || isNightmode == "blue" || isNightmode == "green"
            val colorSpan = if (dark) {
                Constant.mudhafspanDarks
            } else {
                Constant.mudhafspanLight
            }
            if (dark) {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.BYELLOW)
                Constant.shartspanDark = ForegroundColorSpan(Constant.BCYAN)
                Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            } else {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.FORESTGREEN)
                Constant.shartspanDark = ForegroundColorSpan(Constant.WMIDNIHTBLUE)
                Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
            }

            val backgroundColorArabic = when (isNightmode) {
                "dark", "blue", "green" -> R.color.yellow
                "brown", "light" -> R.color.rustred
                else -> R.color.background_color_light_brown
            }
            val backgroundColorEnglish = when (isNightmode) {
                "dark", "blue", "green" -> R.color.kashmirigreen
                "brown", "light" -> R.color.midnightblue
                else -> R.color.background_color_light_brown
            }


            //     spannableString.setSpan( UnderlineSpan(), 0, type.length, 0)
            spannableString.setSpan(
                harfshartspanDark,
                0,
                arabicString.length,
                0
            ) // Span for Arabic


            mansubatCache[surahid]?.get(ayahid)?.add(spannableString) ?: run {
                mansubatCache[surahid]?.set(
                    ayahid,
                    mutableListOf(spannableString)
                ) ?: run {
                    mansubatCache[surahid] =
                        mutableMapOf(ayahid to mutableListOf(spannableString))
                }
            }
            /*
            negationCache.getOrPut(surahid) { mutableMapOf() }
                .put(ayahid, Pair(spannableString, englishString))*/
        }
    }


    fun cacheNegationData(
        quranModel: QuranViewModel,
        surah: Int,
        negationCache: MutableMap<Int, MutableMap<Int, MutableList<SpannableString>>>,
        isNightmode: String
    ) {
        val negantionData = quranModel.getNegationFilterSurah(surah)
        val futureTenceCache: MutableMap<String, List<List<SpannableString>>> = mutableMapOf()
        for (data in negantionData) {
            val surahid = data.surahid
            val ayahid = data.ayahid

            val arabicString = data.arabictext
            val englishString = data.englishtext
            var type = data.type
            //  type += " "
            val combinedString = "$arabicString\n$englishString"
            val spannableString = SpannableString(combinedString)
            dark = isNightmode == "dark" || isNightmode == "blue" || isNightmode == "green"
            val colorSpan = if (dark) {
                Constant.mudhafspanDarks
            } else {
                Constant.mudhafspanLight
            }
            if (dark) {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.BYELLOW)
                Constant.shartspanDark = ForegroundColorSpan(Constant.BCYAN)
                Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            } else {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.FORESTGREEN)
                Constant.shartspanDark = ForegroundColorSpan(Constant.WMIDNIHTBLUE)
                Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
            }
            if (dark) {
                Constant.harfinnaspanDark = ForegroundColorSpan(Color.GREEN)
                Constant.harfismspanDark = ForegroundColorSpan(Constant.BCYAN)
                Constant.harfkhabarspanDark = ForegroundColorSpan(Color.YELLOW)
            } else {
                Constant.harfinnaspanDark = ForegroundColorSpan(Constant.KASHMIRIGREEN)
                Constant.harfismspanDark = ForegroundColorSpan(Constant.prussianblue)
                Constant.harfkhabarspanDark =
                    ForegroundColorSpan(Constant.deepburnsienna)


            }
            val backgroundColorArabic = when (isNightmode) {
                "dark", "blue", "green" -> R.color.yellow
                "brown", "light" -> R.color.rustred
                else -> R.color.background_color_light_brown
            }
            val backgroundColorEnglish = when (isNightmode) {
                "dark", "blue", "green" -> R.color.kashmirigreen
                "brown", "light" -> R.color.midnightblue
                else -> R.color.background_color_light_brown
            }

            if (type == "nasabone") {

                val (firstWord, restOfString) = arabicString.split(" ", limit = 2)
                spannableString.setSpan(
                    Constant.harfinnaspanDark,
                    0,
                    firstWord.length,
                    0
                )

                spannableString.setSpan(
                    Constant.harfkhabarspanDark,
                    firstWord.length,
                    arabicString.length,
                    0


                )

            } else if (type == "nasabtwo") {
                try {
                    val words = arabicString.split(
                        " ",
                        limit = 3
                    ) // Split with limit to avoid more than needed
                    val firstWord = words.getOrNull(0) ?: ""
                    val secondWord = words.getOrNull(1) ?: ""

                    val restOfString = words.drop(2).joinToString(" ")
                    val totalLength = firstWord.length + secondWord.length + restOfString.length

                    //    val (firstWord, secondWord, thirdWord, restOfString) = arabicString.split(" ", limit = 3)
                    spannableString.setSpan(
                        Constant.harfinnaspanDark,
                        0,
                        firstWord.length,
                        0
                    )
                    spannableString.setSpan(
                        Constant.harfismspanDark,
                        firstWord.length,
                        firstWord.length + secondWord.length,
                        0
                    )
                    spannableString.setSpan(
                        harfkhabarspanDark,
                        firstWord.length + secondWord.length,
                        totalLength + 2, // Use totalLength as the end index
                        0
                    )
                } catch (e: IndexOutOfBoundsException) {

                    println("check")
                }


            } else if(type=="nasabtwoharfism-one"){
                val words = arabicString.split(
                    " ",
                    limit = 3
                ) // Split with limit to avoid more than needed
                val firstWord = words.getOrNull(0) ?: ""
                val secondWord = words.getOrNull(1) ?: ""

                val restOfString = words.drop(2).joinToString(" ")
                val totalLength = firstWord.length + secondWord.length + restOfString.length

                spannableString.setSpan(
                    Constant.harfinnaspanDark,
                    0,
                    firstWord.length,
                    0
                )
                spannableString.setSpan(
                    Constant.harfkhabarspanDark,
                    firstWord.length,
                    firstWord.length+secondWord.length,
                    0


                )
                spannableString.setSpan(
                    Constant.harfismspanDark,
                    firstWord.length+secondWord.length,
                    secondWord.length+totalLength+2,
                    0
                )



            }




            else if(type=="nasabtwoharfism-two"){
                val parts = arabicString.split(" ", limit = 4)


                // Get the first word and the rest of the string
                val firstWord = parts.getOrNull(0) ?: ""
                val secondWord = parts.getOrNull(1) ?: ""
                val thirdWord = parts.getOrNull(2) ?: ""
                val restOfString = parts.getOrNull(3) ?: ""
                val seconthirdstring=secondWord+" "+thirdWord
                val totalLength = firstWord.length + seconthirdstring.length+ restOfString.length

                spannableString.setSpan(
                    Constant.harfinnaspanDark,
                    0,
                    firstWord.length,
                    0
                )
                spannableString.setSpan(
                    Constant.harfismspanDark,
                    seconthirdstring.length,
                    firstWord.length+totalLength,
                    0


                )
                spannableString.setSpan(
                    Constant.harfkhabarspanDark,
                    firstWord.length,
                    seconthirdstring.length+firstWord.length,
                    0
                )



            }

            else if (type == "nasabtwoharfism") {

          //      val words =  arabicString.split(" ", limit = 3) // Split with limit to avoid more than needed
                //     val firstWord = words.getOrNull(0) ?: ""
            //    val secondWord = words.getOrNull(1) ?: ""

                //  val restOfString = words.drop(2).joinToString(" ")
                //   val totalLength = firstWord.length + secondWord.length + restOfString.length
                //   val cleanedInput = arabicString.replace("...", "")
                val parts = arabicString.split(" ", limit = 2)

                // Get the first word and the rest of the string
                val firstWord = parts.getOrNull(0) ?: ""
                val restOfString = parts.getOrNull(1) ?: ""
                val totalLength = firstWord.length + restOfString.length

                spannableString.setSpan(
                    Constant.harfinnaspanDark,
                    0,
                    firstWord.length,
                    0
                )
                spannableString.setSpan(
                    Constant.harfismspanDark,
                    firstWord.length,
                    totalLength,
                    0
                )


            } else {

                //     spannableString.setSpan( UnderlineSpan(), 0, type.length, 0)
                spannableString.setSpan(
                    harfinnaspanDark,
                    0,
                    arabicString.length,
                    0
                ) // Span for Arabic
                spannableString.setSpan(
                    harfkhabarspanDark,
                    arabicString.length + 1,
                    combinedString.length,
                    0
                )

            }
            negationCache[surahid]?.get(ayahid)?.add(spannableString) ?: run {
                negationCache[surahid]?.set(
                    ayahid,
                    mutableListOf(spannableString)
                ) ?: run {
                    negationCache[surahid] =
                        mutableMapOf(ayahid to mutableListOf(spannableString))
                }
            }
            /*
            negationCache.getOrPut(surahid) { mutableMapOf() }
                .put(ayahid, Pair(spannableString, englishString))*/
        }
    }


    fun setWordClickListener(
        view: View,
        context: Context,
        word: QuranCorpusWbw,
        surahName: String,
        loadItemList: (Bundle, wbwentity) -> Unit
    ) {
        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle(word.corpus!!.araone + word.corpus!!.aratwo + word.corpus!!.arathree + word.corpus!!.arafour + word.corpus!!.arafive)

            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.corpus!!.surah)
            dataBundle.putInt(Constant.AYAHNUMBER, word.corpus!!.ayah)
            dataBundle.putInt(Constant.WORDNUMBER, word.corpus!!.wordno)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahName)
            loadItemList(dataBundle, word.wbw)

        }
    }

    // ... other functions ...

    fun setWordTranslation(translation: TextView, word: CorpusEntity, wbw: String) {
        when (wbw) {
            "en" -> {
                // translation.text = word.wordno.toString()
                translation.text = word.en
                translation.paintFlags = translation.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }

            "bn" -> {
                translation.text = word.bn
                translation.paintFlags = translation.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }

            "in" -> {
                translation.text = word.ind
                translation.paintFlags = translation.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }

            "ur" -> {
                translation.text = word.ur
                translation.paintFlags = translation.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    fun NewsetWordClickListener(
        view: View,
        context: Context,
        word: CorpusEntity,
        surahName: String,
        loadItemList: (Bundle, CorpusEntity) -> Unit
    ) {
        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle(word.araone + word.aratwo + word.arathree + word.arafour + word.arafive)

            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.surah)
            dataBundle.putInt(Constant.AYAHNUMBER, word.ayah)
            dataBundle.putInt(Constant.WORDNUMBER, word.wordno)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahName)
            loadItemList(dataBundle, word)

        }
    }


    fun setTranslationText(
        textView: TextView,
        translationNote: TextView,
        entity: QuranEntity?,
        translationKey: String,
        noteResId: Int
    ) {
        if (entity != null) {
            textView.text = when (translationKey) {
                "en_arberry" -> entity.en_arberry
                "en_sahih" -> entity.translation
                "en_jalalayn" -> entity.en_jalalayn
                "ur_jalalayn" -> entity.ur_jalalayn
                "ur_junagarhi" -> entity.ur_junagarhi

                // ... other translations
                else -> return
            }
        }
        translationNote.setText(noteResId)
        textView.visibility = View.VISIBLE
        translationNote.visibility = View.VISIBLE
    }

    fun showWordMorphologyTooltip(
        context: Context,
        view: View,
        word: QuranCorpusWbw,
        isNightmode: String
    ) {
        val utils = Utils(QuranGrammarApplication.context!!)

        val verbCorpusRootWords =
            utils.getQuranRoot(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        if (verbCorpusRootWords.isNotEmpty() && verbCorpusRootWords[0].tag == "V") {
            //    vbdetail = ams.getVerbDetails();
            print("check")
        }
        val corpusNounWord: List<NounCorpus> =
            utils.getQuranNouns(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        val verbCorpusRootWord: List<VerbCorpus> =
            utils.getQuranRoot(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        val qm = WordMorphologyDetails(
            word.corpus,
            corpusNounWord, verbCorpusRootWord
        )
        val workBreakDown = qm.workBreakDown

        val backgroundColor = when (isNightmode) {
            "dark", "blue", "green" -> R.color.background_color
            "brown", "light" -> R.color.color_surface
            else -> R.color.black_overlay
        }

        SimpleTooltip.Builder(context)
            .anchorView(view)
            .text(workBreakDown)
            .backgroundColor(ContextCompat.getColor(context, R.color.background_color))
            .gravity(Gravity.TOP)
            .modal(true)
            .arrowDrawable(android.R.drawable.ic_media_previous)
            .arrowHeight(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .arrowWidth(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .build()
            .show()
    }

    fun NewshowWordMorphologyTooltip(
        context: Context,
        view: View,
        word: CorpusEntity,
        isNightmode: String
    ) {
        val utils = Utils(QuranGrammarApplication.context!!)

        val verbCorpusRootWords =
            utils.getQuranRoot(
                word.surah,
                word.ayah,
                word.wordno
            )
        if (verbCorpusRootWords.isNotEmpty() && verbCorpusRootWords[0].tag == "V") {
            //    vbdetail = ams.getVerbDetails();
            print("check")
        }
        val corpusNounWord: List<NounCorpus> =
            utils.getQuranNouns(
                word.surah,
                word.ayah,
                word.wordno
            )
        val verbCorpusRootWord: List<VerbCorpus> =
            utils.getQuranRoot(
                word.surah,
                word.ayah,
                word.wordno
            )
        val qm = WordMorphologyDetails(
            word,
            corpusNounWord, verbCorpusRootWord
        )
        val workBreakDown = qm.workBreakDown

        val backgroundColor = when (isNightmode) {
            "dark", "blue", "green" -> R.color.background_color
            "brown", "light" -> R.color.background_color
            else -> R.color.background_color_light_brown
        }

        SimpleTooltip.Builder(context)
            .anchorView(view)
            .text(workBreakDown)
            .backgroundColor(backgroundColor)
            .gravity(Gravity.TOP)
            .modal(true)
            .arrowDrawable(android.R.drawable.ic_media_previous)
            .arrowHeight(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .arrowWidth(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .build()
            .show()
    }

    fun setIconColors(surahInfoTextView: TextView, isNightMode: String, isMakkiMadani: Int) {
        val icon = if (isMakkiMadani == 1) {
            R.drawable.ic_makkah_48
        } else {
            R.drawable.ic_madinah_48
        }

        val tintColor =
            if (isNightMode == "dark" || isNightMode == "blue" || isNightMode == "green") {
                Color.CYAN
            } else {
                Color.BLUE
            }

        surahInfoTextView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        surahInfoTextView.compoundDrawableTintList = ColorStateList.valueOf(tintColor)
    }

    fun setLocationVisibility(
        makkiIcon: ImageView,
        madaniIcon: ImageView,
        location: RevalationCity
    ) {
        makkiIcon.visibility = if (location == RevalationCity.MAKKI) View.VISIBLE else View.GONE
        madaniIcon.visibility = if (location == RevalationCity.MADANI) View.VISIBLE else View.GONE
    }


    fun setBackgroundColor(context: Context, view: View, isNightmode: String, isOdd: Boolean) {
        val color = when (isNightmode) {
            "brown" -> if (isOdd) R.color.bg_brown else R.color.odd_item_bg_brown
            "dark" -> if (isOdd) R.color.odd_item_bg_black else R.color.bg_black
            "blue" -> if (isOdd) R.color.bg_surface_dark_blue else R.color.bg_dark_blue
            else -> return
        }
        view.setBackgroundColor(ContextCompat.getColor(context, color))
    }


    fun getSpannedRoots(tag: QuranCorpusWbw, rootword: String): SpannableString {
        return CorpusUtilityorig.ColorizeRootword(
            tag.corpus!!.tagone!!,
            tag.corpus!!.tagtwo!!,
            tag.corpus!!.tagthree!!,
            tag.corpus!!.tagfour!!,
            tag.corpus!!.tagfive!!,
            rootword
        )!!
    }

    fun NewgetSpannedRoots(tag: CorpusEntity, rootword: String): SpannableString {
        return CorpusUtilityorig.ColorizeRootword(
            tag.tagone!!,
            tag.tagtwo!!,
            tag.tagthree!!,
            tag.tagfour!!,
            tag.tagfive!!,
            rootword
        )!!
    }

    fun NewgetSpannedWords(tag: CorpusEntity): SpannableString {
        return CorpusUtilityorig.NewSetWordSpan(
            tag.tagone,
            tag.tagtwo,
            tag.tagthree,
            tag.tagfour,
            tag.tagfive,
            tag.araone!!,
            tag.aratwo!!,
            tag.arathree!!,
            tag.arafour!!,
            tag.arafive!!
        )
    }

    fun getSpannedWords(tag: QuranCorpusWbw): SpannableString {
        return CorpusUtilityorig.NewSetWordSpan(
            tag.corpus!!.tagone,
            tag.corpus!!.tagtwo,
            tag.corpus!!.tagthree,
            tag.corpus!!.tagfour,
            tag.corpus!!.tagfive,
            tag.corpus!!.araone!!,
            tag.corpus!!.aratwo!!,
            tag.corpus!!.arathree!!,
            tag.corpus!!.arafour!!,
            tag.corpus!!.arafive!!
        )
    }

    fun storepreferences(context: Context, entity: QuranEntity, SurahName: String) {
        val pref = context.getSharedPreferences("lastread", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(Constant.SURAH_ID, entity.surah)
        editor.putInt(Constant.AYAH_ID, entity.ayah)
        editor.putString(Constant.SURAH_ARABIC_NAME, SurahName)
        editor.apply()
        // editor.commit();  // You don't need both apply() and commit()
    }

    fun collectBrokenPlurals(corpus: List<CorpusEntity>, qurantext: String): List<String> {
        val brokenPluralPatterns = listOf(
            "فُعُوْلٌ",
            "أَفْعَالٌ",
            "فِعَالٌ",
            "فُعُلٌ",
            "فِعْلَانٌ",
            "فَعْلَانُ",
            "فُعَلٌ",
            "فُعُولٌ",
            "فُعَلَاءُ",
            "فُعْلَانٌ",
            "فَعَالِيلُ",
            "فُعُولَاتٌ",
            "مَفَاعِلُ",
            "فَوَاعِلُ",
            "مَفَاعِيلُ",
            "فُعْلَةٌ",
            "أَفَاعِيلُ",
            "أَفْعِلَةٌ",
            "فَعَالٍ",
            "فِعْلَةٌ",
            "فِعَالَاتٌ",
            "فِعْلَانٌ",
            "فِعَالِين",
            "أَفْعُلَةٌ",
            "فُعَلٌ",
            "أَفْعَالَ",
            "فُعُولَ",
            "فِعَالَ",
            "فُعَلَاءَ",
            "فُعْلَانَ",
            "فَعَالِيلَ",
            "فُعُولَاتَ",
            "مَفَاعِلَ",
            "فَوَاعِلَ",
            "مَفَاعِيلَ",
            "فُعْلَةَ",
            "أَفَاعِيلَ",
            "أَفْعِلَةَ",
            "فَعَالِيَ",
            "فِعْلَةَ",
            "فِعَالَاتَ",
            "فِعْلَانَ",
            "فِعَالِين",
            "أَفْعُلَةَ",
            "فُعَلَ"
        )

        val identifiedPlurals = mutableListOf<String>()

        for (entry in corpus) {
            var currentWord = ""
            var nounDetails = ""
            //     entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            if (entry.tagone == "N") {
                currentWord = entry.araone!!
                nounDetails = entry.detailsone!!
            } else if (entry.tagtwo == "N") {
                currentWord = entry.aratwo!!
                nounDetails = entry.detailstwo!!
            } else if (entry.tagthree == "N") {
                currentWord = entry.arathree!!
                nounDetails = entry.detailsthree!!
            } else if (entry.tagfour == "N") {
                currentWord = entry.arafour!!
                nounDetails = entry.detailsfour!!
            } else if (entry.tagfive == "N") {
                currentWord = entry.arafive!!
                nounDetails = entry.detailsfive!!
            }
            val wordForIndex =
                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            val wordConsonants = extractConsonants(currentWord)
            val currentmeaning = entry.en
            for (pattern in brokenPluralPatterns) {
                val patternConsonants = extractConsonants(pattern)

                // Ensure both word and pattern have at least 3 consonants
                if (wordConsonants.length >= 3 && patternConsonants.length >= 3) {
                    try {
                        // Replace consonants in the pattern with those of the current word
                        val replacedPattern =
                            pattern.replaceFirst(patternConsonants[0], wordConsonants[0])
                                .replaceFirst(patternConsonants[1], wordConsonants[1])
                                .replaceFirst(patternConsonants[2], wordConsonants[2])

                        // Compare the modified pattern with the current word
                        if (replacedPattern == currentWord) {

                            val inputString = nounDetails
                            val extractedValues = extractValues(inputString)
                            val arabic =
                                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
                            val english = entry.en
                            val startindex = qurantext.indexOf(arabic)
                            val endindex = startindex + arabic.length
                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$startindex|$endindex|$arabic|$pattern|$currentWord|$english|$extractedValues"
                            identifiedPlurals.add(dataString)
                        }
                    } catch (e: StringIndexOutOfBoundsException) {
                        // Handle case where replaceFirst fails
                        println("Error processing word: $currentWord with pattern: $pattern")
                    }
                }
            }
        }

        return identifiedPlurals
    }

    fun extractValues(inputString: String): String {
        val values = inputString.split("|")
        val extractedValues = mutableListOf<String>()

        for (value in values) {
            if (value in listOf("F", "FP", "FS", "P", "M", "MP", "MS")) {
                extractedValues.add(value)
            }
        }

        return extractedValues.joinToString(",") // Join the values with a comma
            .replace(Regex("[\\[\\]]"), "") // Remove square brackets
    }

    fun extractMousufSifa(corpusList: List<CorpusEntity>, qurantext: String): List<String> {
        val mousufSifaPairs = mutableListOf<Pair<String, String>>()
        val extractedSentences = mutableListOf<String>()

        for (i in corpusList.indices) {
            val currentWord = corpusList[i]
            if (currentWord.surah == 27 && currentWord.ayah == 44 && currentWord.wordno == 15) {
                println("check")
            }
            // Check if the current word is a noun (N) or proper noun (PN)
            /*  val isIndefiniteNoun = (currentWord.tagone=="N" || currentWord.tagone=="PN")
                      || (currentWord.tagtwo=="N" || currentWord.tagtwo=="PN" && currentWord.tagone!="DET")
                      || (currentWord.tagthree=="N" || currentWord.tagthree=="PN" && currentWord.aratwo!="DET")
                      || (currentWord.tagfour=="N" || currentWord.tagfour=="PN" && currentWord.tagthree!="DET")*/
            val isIndefiniteNoun = (currentWord.tagone == "N" || currentWord.tagone == "PN") ||
                    (currentWord.tagtwo == "N" || currentWord.tagtwo == "PN") && currentWord.tagone != "DET" ||
                    (currentWord.tagthree == "N" || currentWord.tagthree == "PN") && currentWord.aratwo != "DET" ||
                    (currentWord.tagfour == "N" || currentWord.tagfour == "PN") && currentWord.tagthree != "DET"


            val isDefinitNounTagone =
                currentWord.tagone == "DET" && (currentWord.tagtwo!!.contains("N") || currentWord.tagtwo!!.contains(
                    "PN"
                ))
            val isDefinitNounTagtwo =
                currentWord.tagtwo!!.contains("DET") && (currentWord.tagthree!!.contains("N") || currentWord.tagthree!!.contains(
                    "PN"
                ))
            val isDefinitNounTagthree =
                currentWord.tagthree!!.contains("DET") && (currentWord.tagfour!!.contains("N") || currentWord.tagfour!!.contains(
                    "PN"
                ))
            val isDefinitNoun =
                (currentWord.tagone == "DET" && (currentWord.tagtwo!!.contains("N") || currentWord.tagtwo!!.contains(
                    "PN"
                )
                        || currentWord.tagtwo!!.contains("DET") && (currentWord.tagthree!!.contains(
                    "N"
                ) || currentWord.tagthree!!.contains("PN")
                        || currentWord.tagthree!!.contains("DET") && (currentWord.tagfour!!.contains(
                    "N"
                ) || currentWord.tagfour!!.contains("PN")
                        ))))
            var nounDef = false
            if (isIndefiniteNoun || isDefinitNounTagone || isDefinitNounTagtwo || isDefinitNounTagthree) {
                var nounDetails = ""
                var currentWords = ""
                var isBrokenPlural = false

                if (isIndefiniteNoun) {
                    nounDetails = currentWord.detailsone ?: ""

                } else if (isDefinitNounTagone) {
                    nounDetails = currentWord.detailstwo ?: ""
                    nounDef = currentWord.tagone == "DET"
                } else if (isDefinitNounTagtwo) {
                    nounDetails = currentWord.detailsthree ?: ""
                    nounDef = currentWord.tagtwo == "DET"
                } else if (isDefinitNounTagthree) {
                    nounDetails = currentWord.detailsfour ?: ""
                    nounDef = currentWord.tagthree == "DET"
                }


                // Check for broken plural or gender number agreement
                currentWords =
                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                isBrokenPlural =
                    isBrokenPluralPattern(currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive)

                // Handle broken plural: treat as FS (feminine singular)
                var nounGender = if (isBrokenPlural) "FS" else nounDetails.extractGender()
                val nounCase = nounDetails.extractCase()
                // Check definiteness

                // Look ahead to find the adjective (ADJ)
                if (i + 1 < corpusList.size) {
                    val nextWord = corpusList[i + 1]
                    var isAdjective = false
                    var isNoun = false
                    var adjDetails = ""
                    var nounDetails = ""
                    if (nextWord.tagone == "ADJ") {
                        isAdjective = true
                        adjDetails = nextWord.detailsone ?: ""
                    } else if (nextWord.tagone == "DET") {
                        isAdjective =
                            nextWord.tagtwo == "ADJ"
                        adjDetails = nextWord.detailstwo ?: ""
                    } else if (nextWord.tagtwo == "DET") {
                        isAdjective =
                            nextWord.tagthree == "ADJ"
                        adjDetails = nextWord.detailsthree ?: ""
                    } else if (nextWord.tagthree == "DET") {
                        isAdjective = nextWord.tagfour == "ADJ"
                        adjDetails = nextWord.detailsfour ?: ""
                    }
                    //check noun and noun
                    if (nextWord.tagone == "N") {
                        isNoun = true
                        nounDetails = nextWord.detailsone ?: ""
                    } else if (nextWord.tagone == "N") {
                        isAdjective =
                            nextWord.tagtwo == "N"
                        nounDetails = nextWord.detailstwo ?: ""
                    } else if (nextWord.tagtwo == "DET") {
                        isNoun =
                            nextWord.tagthree == "N"
                        nounDetails = nextWord.detailsthree ?: ""
                    } else if (nextWord.tagthree == "DET") {
                        isNoun = nextWord.tagfour == "N"
                        nounDetails = nextWord.detailsfour ?: ""
                    }
                    if (isNoun) {
                        val currentNounGender = nounDetails.extractGender()
                        val currentNounCase = nounDetails.extractCase()
                        val currnetNounDef = nextWord.tagone == "DET"
                        if (nounCase == currentNounCase && nounDef == currnetNounDef && nounGender == currentNounGender) {

                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            var adjword = nextWord.wordno
                            if (i + 2 < corpusList.size) {

                                val nextSecondAdjective = corpusList[i + 2]
                                if (nextSecondAdjective.tagone == "ADJ" || nextSecondAdjective.tagtwo == "ADJ" || nextSecondAdjective.tagthree == "ADJ" || nextSecondAdjective.tagfour == "ADJ") {
                                    nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    sifa += " " + nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    adjword = nextSecondAdjective.wordno
                                }
                            }

                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val nountype = "nountype"
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|$adjword|$startindex|$endindex|$extractedSentence|$translationBuilder|$nountype"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))
                        }
                    }

                    if (isAdjective) {
                        val adjGender = adjDetails.extractGender()
                        val adjCase = adjDetails.extractCase()
                        val adjDef = nextWord.tagone == "DET"
                        if (nounCase == adjCase && nounDef == adjDef) {
                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            var adjword = nextWord.wordno
                            if (i + 2 < corpusList.size) {

                                val nextSecondAdjective = corpusList[i + 2]
                                if (nextSecondAdjective.tagone == "ADJ" || nextSecondAdjective.tagtwo == "ADJ" || nextSecondAdjective.tagthree == "ADJ" || nextSecondAdjective.tagfour == "ADJ") {
                                    nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    sifa += " " + nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    adjword = nextSecondAdjective.wordno
                                }
                            }

                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|$adjword|$startindex|$endindex|$extractedSentence|$translationBuilder"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))

                        } else if (nounCase == adjCase && adjDef && !nounDef) {
                            var adjword = nextWord.wordno
                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            if (i + 2 < corpusList.size) {

                                val nextSecondAdjective = corpusList[i + 2]
                                if (nextSecondAdjective.tagone == "ADJ" || nextSecondAdjective.tagtwo == "ADJ" || nextSecondAdjective.tagthree == "ADJ" || nextSecondAdjective.tagfour == "ADJ") {
                                    nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    sifa += " " + nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    adjword = nextSecondAdjective.wordno
                                }
                            }
                            val check = "check"
                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|$adjword|$startindex|$endindex|$extractedSentence|$translationBuilder|$check"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))
                        }
                    } else if (isAdjective) {//1073
                        val adjGender = adjDetails.extractGender()
                        val adjCase = adjDetails.extractCase()
                        val adjDef = nextWord.tagone == "DET"
                        if ((adjGender == "FS" && nounGender == "MP" || (adjGender == "FS") && nounGender == "F") && (nounCase == adjCase && nounDef == adjDef)) {
                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            val sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|${nextWord.wordno}|$startindex|$endindex|$extractedSentence|$translationBuilder"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))

                        } else
                            if (adjGender == "F" && nounCase == adjCase && nounDef == adjDef) {
                                val mousuf =
                                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                                val sifa =
                                    nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                                val translationBuilder = StringBuilder()
                                translationBuilder.append(nextWord.en).append(" ")
                                    .append(currentWord.en)
                                val extractedSentence = "$mousuf $sifa"
                                val startindex = qurantext.indexOf(extractedSentence)
                                val endindex = startindex + extractedSentence.length
                                val dataString =
                                    "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|${nextWord.wordno}|$startindex|$endindex|$extractedSentence|$translationBuilder"
                                extractedSentences.add(dataString)
                                mousufSifaPairs.add(Pair(mousuf, sifa) as Pair<String, String>)


                            } else {

                                //    val adjDetails = nextWord.detailstwo ?: ""
                                val adjGender = adjDetails.extractGender()
                                val adjCase = adjDetails.extractCase()
                                val adjDef = nextWord.tagone == "DET" // Check definiteness
                                if ((nounGender == "unknown" || nounGender == "M") && (adjGender == "MS" || adjGender == "FS")) {
                                    nounGender = adjGender
                                }
                                // Check for gender, case, and definiteness agreement
                                if (nounGender == adjGender && nounCase == adjCase && nounDef == adjDef) {
                                    // Found a mousuf-sifa pair
                                    val mousuf =
                                        currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                                    val sifa =
                                        nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                                    val translationBuilder = StringBuilder()
                                    translationBuilder.append(nextWord.en).append(" ")
                                        .append(currentWord.en)
                                    val extractedSentence = "$mousuf $sifa"
                                    val startindex = qurantext.indexOf(extractedSentence)
                                    val endindex = startindex + extractedSentence.length
                                    val dataString =
                                        "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|${nextWord.wordno}|$startindex|$endindex|$extractedSentence|$translationBuilder"
                                    extractedSentences.add(dataString)
                                    mousufSifaPairs.add(
                                        Pair(
                                            mousuf,
                                            sifa
                                        ) as Pair<String, String>
                                    )
                                }
                            }
                    }


                }
            }
        }
        return extractedSentences
    }

    fun isBrokenPluralPattern(currentWord: String?): Boolean {
        if (currentWord == null) return false


        val brokenPluralPatterns = listOf(
            "فُعُوْلٌ",
            "أَفْعَالٌ",
            "فِعَالٌ",
            "فُعُلٌ",
            "فِعْلَانٌ",
            "فَعْلَانُ",
            "فُعَلٌ",
            "فُعُولٌ",
            "فُعَلَاءُ",
            "فُعْلَانٌ",
            "فَعَالِيلُ",
            "فُعُولَاتٌ",
            "مَفَاعِلُ",
            "فَوَاعِلُ",
            "مَفَاعِيلُ",
            "فُعْلَةٌ",
            "أَفَاعِيلُ",
            "أَفْعِلَةٌ",
            "فَعَالٍ",
            "فِعْلَةٌ",
            "فِعَالَاتٌ",
            "فِعْلَانٌ",
            "فِعَالِين",
            "أَفْعُلَةٌ",
            "فُعَلٌ",
            // Add accusative/genitive cases
            "أَفْعَالَ",
            "فُعُولَ",
            "فِعَالَ",
            "فُعَلَاءَ",
            "فُعْلَانَ",
            "فَعَالِيلَ",
            "فُعُولَاتَ",
            "مَفَاعِلَ",
            "فَوَاعِلَ",
            "مَفَاعِيلَ",
            "فُعْلَةَ",
            "أَفَاعِيلَ",
            "أَفْعِلَةَ",
            "فَعَالِيَ",
            "فِعْلَةَ",
            "فِعَالَاتَ",
            "فِعْلَانَ",
            "فِعَالِين",
            "أَفْعُلَةَ",
            "فُعَلَ"
        )
        // Extract consonants from the current word
        val wordConsonants = extractConsonants(currentWord)
        if (wordConsonants.length != 3) return false // Assuming broken plurals have 3 root consonants

        // Loop through each broken plural pattern
        for (pattern in brokenPluralPatterns) {
            // Extract consonants from the pattern (should be 3 root letters in most cases)
            val patternConsonants = extractConsonants(pattern)

            // If both have the same number of consonants (usually 3), replace pattern consonants with word consonants
            if (patternConsonants.length == 3) {
                // Replace consonants in the pattern with those of the current word
                val replacedPattern = pattern.replaceFirst(patternConsonants[0], wordConsonants[0])
                    .replaceFirst(patternConsonants[1], wordConsonants[1])
                    .replaceFirst(patternConsonants[2], wordConsonants[2])

                // Compare the modified pattern with the current word
                if (replacedPattern == currentWord) {
                    return true // Match found
                }
            }
        }
        return false // No match found
    }

    fun extractMudafMudafIlaih(corpusList: List<CorpusEntity>, qurantext: String): List<String> {
        val results = mutableListOf<String>()

        for ((i, entry) in corpusList.withIndex()) {
            // Construct the current word and check if it's a valid mudaf
            if (entry.surah == 20 && entry.ayah == 100 && entry.wordno == 6) {

                println("check")
            }

            if (entry.surah == 22 && entry.ayah == 24 && entry.wordno == 8) {

                println("check")
            }
            val constructedWord =
                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            val englishtranslation = entry.en
            val isIndefniteNoun =
                (entry.tagone == "N") || (entry.tagone != "DET" && entry.tagtwo == "N")
                        || (entry.tagtwo != "DET" && entry.tagthree == "N") || (entry.tagthree != "DET" && entry.tagfour == "N")
            val isZarf =
                (entry.tagone == "T") || (entry.tagone != "DET" && entry.tagtwo == "T")
                        || (entry.tagtwo != "DET" && entry.tagthree == "T") || (entry.tagthree != "DET" && entry.tagfour == "T")
            var noundDetails = ""// Check for mudaf conditions
            if (entry.tagone == "T") {
                noundDetails = entry.detailsone.toString()
            } else if (entry.tagtwo == "T") {
                noundDetails = entry.detailstwo.toString()

            } else if (entry.tagthree == "T") {
                noundDetails = entry.detailsthree.toString()
            } else if (entry.tagfour == "T") {
                noundDetails = entry.detailsfour.toString()
            } else

                if (entry.tagone == "N") {
                    noundDetails = entry.detailsone.toString()
                } else if (entry.tagtwo == "N") {
                    noundDetails = entry.detailstwo.toString()

                } else if (entry.tagthree == "N") {
                    noundDetails = entry.detailsthree.toString()
                } else if (entry.tagfour == "N") {
                    noundDetails = entry.detailsfour.toString()
                }
            val nounCase = noundDetails.extractCase()
            val isMudafori = isIndefniteNoun || isZarf && // No DET tag

                    !constructedWord.contains("ٌ") && !constructedWord.contains("ً") && !constructedWord.contains(
                "ٍ"
            ) && // No nunation
                    (nounCase == "GEN" || nounCase == "ACC" || nounCase == "NOM") // Allowed cases
            //   val tanweenRegex = Regex("[\\u064B-\\u0652-\\U064C]")
            val tanweenRegex = Regex("[\\u064B-\\u064D-\\u064C]")
            //  val tanweenRegex = Regex("[ًٌٍ]") // Matches the three Tanween characters


            val result = hasNunation(constructedWord)
            if (result) {
                println("check")
            }
            val isMudaf = (isIndefniteNoun || isZarf) &&
                    !result && // No nunation
                    (nounCase == "GEN" || nounCase == "ACC" || nounCase == "NOM")

            // If mudaf conditions met, look for mudaf ilaih in the next entry
            if ((entry.tagone == "N" && entry.tagtwo == "PRON") || (entry.tagtwo == "N" && entry.tagthree == "PRON")) {
                val fullword = constructedWord
                val trimmedWord = trimWord(fullword)
                val startindex = qurantext.indexOf(trimmedWord)
                val endindex = startindex + constructedWord.length
                var extractedText = ""
                if (startindex != -1 && endindex != -1) {
                    extractedText = qurantext.substring(startindex, endindex)

                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$startindex|$endindex|$extractedText|$englishtranslation"
                    results.add(dataString)

                } else {
                    val fullword = constructedWord
                    var newstartindex = 0
                    val extracted = "chek"
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$startindex|$endindex|$fullword|$englishtranslation"
                    results.add(dataString)
                }
            } else


                if (isMudaf && i + 1 < corpusList.size) {
                    val nextEntry = corpusList[i + 1]
                    val nextConstructedWord =
                        nextEntry.araone + nextEntry.aratwo + nextEntry.arathree + nextEntry.arafour + nextEntry.arafive
                    val nextenglishword = nextEntry.en
                    val englishtran = englishtranslation + " " + nextenglishword
                    var nextEntryDetails = ""
                    if (nextEntry.tagone == "PN" || (nextEntry.tagone == "N" || nextEntry.tagone == "ADJ")) {

                        nextEntryDetails = nextEntry.detailsone.toString()

                    } else if ((nextEntry.tagtwo == "PN" || nextEntry.tagtwo == "N" || nextEntry.tagtwo == "ADJ")) {
                        nextEntryDetails = nextEntry.detailstwo.toString()
                    } else if ((nextEntry.tagthree == "PN" || nextEntry.tagthree == "N" || nextEntry.tagthree == "ADJ")) {
                        nextEntryDetails = nextEntry.detailsthree.toString()
                    } else if (nextEntry.tagfour == "PN" || nextEntry.tagfour == "N" || nextEntry.tagfour == "ADJ") {
                        nextEntryDetails = nextEntry.detailsfour.toString()
                    }


                    // Check for mudaf ilaih conditions
                    val isNoun =
                        (nextEntry.tagone == "PN" || nextEntry.tagone == "N") ||
                                (nextEntry.tagtwo == "PN" || nextEntry.tagtwo == "N") ||
                                (nextEntry.tagthree == "PN" || nextEntry.tagthree == "N")

                    val isAdjective =
                        (nextEntry.tagone == "ADJ") ||
                                (nextEntry.tagtwo == "ADJ") ||
                                (nextEntry.tagthree == "ADJ")

                    val isMudafIlaih = isNoun || isAdjective && // Must be noun or pronoun
                            nextEntryDetails.contains("GEN") // Must be in genitive case



                    if (isMudafIlaih) {

                        val fullword = constructedWord + " " + nextConstructedWord
                        val startindex = qurantext.indexOf(fullword)
                        val endindex = startindex + fullword.length
                        var extractedText = ""
                        if (startindex != -1 && endindex != -1) {
                            extractedText = qurantext.substring(startindex, endindex)

                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${nextEntry.wordno}|$startindex|$endindex|$extractedText|$englishtran"
                            results.add(dataString)

                        } else {
                            val fullword = constructedWord + " " + nextConstructedWord
                            var newstartindex = 0
                            val extracted = "chek"
                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${nextEntry.wordno}|$newstartindex|$endindex|$fullword|$englishtran"
                            results.add(dataString)
                        }

                    }
                }
        }

        return results
    }

    fun trimWord(word: String): String {
        return word.replace(Regex("[,ٓ]+$"), "")
    }

    // Extract gender (e.g., FS, MS, MP, FP) from the details string
    fun String.extractGender(): String {
        return when {
            contains("MS") -> "MS"
            contains("FS") -> "FS"
            contains("MP") -> "MP"
            contains("FP") -> "FP"
            contains("|M|") -> "M"
            contains("|F|") -> "F"

            else -> "unknown"
        }
    }

    // Extract case (e.g., NOM, ACC, GEN) from the details string
    fun String.extractCase(): String {
        return when {
            contains("NOM") -> "NOM"
            contains("ACC") -> "ACC"
            contains("GEN") -> "GEN"
            else -> "unknown"
        }
    }

    fun String.extractVerbType(): String {
        return when {
            contains("MOOD:JUS") -> "JUS"
            contains("MOOD:SUBJ") -> "SUBJ"

            else -> "INDI"
        }
    }


    fun hasNunation(text: String): Boolean {
        val nunationRegex = Regex("[\\u064B\\u064D\\u064C]")
        return nunationRegex.containsMatchIn(text)
    }

    fun extractConsonants(input: String): String {
        val arabicDiacritics =
            "[\\u064B-\\u065F\\u0670\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]" // Arabic diacritic Unicode range
        val vowels = "[ًٌٍَُِْ]" // Short vowels (fatha, kasra, damma, etc.)

        // Remove diacritics and vowels
        return input.replace(Regex(arabicDiacritics), "").replace(Regex(vowels), "")
    }

    fun showIndexOfWindow(context: Context, text: String, wordToCheck: String) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Check indexOf")
            .setView(R.layout.index_of_window)
            .setPositiveButton("Check") { dialog, _ ->
                val startIndexInput =
                    (dialog as AlertDialog).findViewById<EditText>(R.id.start_index_input)
                val endIndexInput = dialog.findViewById<EditText>(R.id.end_index_input)

                val startIndex = startIndexInput!!.text.toString().toIntOrNull() ?: 0
                val endIndex = endIndexInput!!.text.toString().toIntOrNull() ?: text.length

                val index =
                    text.indexOf(wordToCheck, startIndex, ignoreCase = true) //ignoreCase added

                val resultText = if (index != -1 && index in startIndex..endIndex) {
                    "Word found at index: $index"
                } else {
                    "Word not found within the specified range."
                }

                Toast.makeText(context, resultText, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

}