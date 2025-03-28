package com.example.utility

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

import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.WordMorphologyDetails
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils
import android.graphics.Color
import android.graphics.Paint


import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


import com.example.mushafconsolidated.SpannableStringUtils
import com.example.mushafconsolidated.model.Word

import com.example.mushafconsolidated.quranrepo.QuranViewModel
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




    fun cachePhrasesData(
        quranModel: QuranViewModel,
        surah: Int,
        negationCache: MutableMap<Int, MutableMap<Int, MutableList<SpannableString>>>,
        isNightmode: Boolean
    ) {
        val negantionData = quranModel.getNegationFilterSurah(surah)
        val futureTenceCache: MutableMap<String, List<List<SpannableString>>> = mutableMapOf()
        val spannedStrings=   SpannableStringUtils.applySpans(negantionData,isNightmode)


        for ((surahid,ayahid:Int, type, spannableString) in spannedStrings) {

            negationCache[surahid]?.get(ayahid)?.add(spannableString) ?: run {
                negationCache[surahid]?.set(
                    ayahid,
                    mutableListOf(spannableString)
                ) ?: run {
                    negationCache[surahid] =
                        mutableMapOf(ayahid to mutableListOf(spannableString))
                }
            }
        }


    }


    fun processString(input: String): Triple<String, String, String> {
        val parts = input.split(":", limit = 2) // Split into two parts using ':' as the delimiter
        if (parts.size < 2) {
            throw IllegalArgumentException("Input string must contain ':' as a delimiter.")
        }
        val firstPart = parts[0].trim()
        val secondPart = parts[1].trim()

        // Split the first part into first word and subsequent words
        val firstPartWords = firstPart.split(" ", limit = 2)
        val firstWord = firstPartWords[0]
        val subsequentWords = if (firstPartWords.size > 1) firstPartWords[1] else ""

        return Triple(firstWord, subsequentWords, secondPart)
    }

    // ... other functions ...

    fun setWordTranslation(translation: TextView, word: CorpusEntity, wbw: String) {
        when (wbw) {
            "en" -> {
            //    translation.text = word.wordno.toString()

                    translation.text = word.en+" "+ "   "
           //     translation.paintFlags = translation.paintFlags or Paint.UNDERLINE_TEXT_FLAG
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
            "brown", "light" -> R.color.bg_black
            else -> R.color.background_color_light_brown
        }
        if (isNightmode == "dark" || isNightmode == "blue" || isNightmode == "green") {
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
        } else {
            SimpleTooltip.Builder(context)
                .anchorView(view)
                .text(workBreakDown)
                //  .backgroundColor(backgroundColor)
                .gravity(Gravity.TOP)
                .modal(true)
                .arrowDrawable(android.R.drawable.ic_media_previous)
                .arrowHeight(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
                .arrowWidth(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
                .build()
                .show()


        }
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


    fun getSpannedRoots(tag: CorpusEntity, rootword: String): SpannableString {
        return CorpusUtilityorig.ColorizeRootword(
            tag!!.tagone!!,
            tag!!.tagtwo!!,
            tag!!.tagthree!!,
            tag!!.tagfour!!,
            tag!!.tagfive!!,
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
        return CorpusUtilityorig.QuranWbWSpan(
            tag.tagone,
            tag.tagtwo,
            tag.tagthree,
            tag.tagfour,
            tag.tagfive,
            tag.araone!!,
            tag.aratwo!!,
            tag.arathree!!,
            tag.arafour!!,
            tag.arafive!!,
            tag.detailsone!!
        )
    }

    fun getSpannedWords(tag: CorpusEntity): SpannableString {
        return CorpusUtilityorig.NewSetWordSpan(
            tag!!.tagone,
            tag!!.tagtwo,
            tag!!.tagthree,
            tag!!.tagfour,
            tag!!.tagfive,
            tag!!.araone!!,
            tag!!.aratwo!!,
            tag!!.arathree!!,
            tag!!.arafour!!,
            tag!!.arafive!!
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

    fun extractConsonantsprev(input: String): String {
        val arabicDiacritics =
            "[\\u064B-\\u065F\\u0670\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]" // Arabic diacritic Unicode range
        val vowels = "[ًٌٍَُِْ]" // Short vowels (fatha, kasra, damma, etc.)
        val dots = "[\\.]{1,2}" // Matches single or double dots

        // Remove diacritics, vowels, and dots
        val result= input.replace(Regex(arabicDiacritics), "") .replace(Regex(vowels), "")      .replace(Regex(dots), "")
        return result
    }

    fun extractConsonantsssss(input: String): String {
        // Define Arabic diacritics and vowels explicitly
        val arabicDiacriticsAndVowels = "[\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]" // Includes all Arabic diacritics and vowels
        val arabicLetters = "[\\u0621-\\u064A]" // Matches Arabic letters

        // Letters without dots in Arabic script
        val lettersWithoutDots = setOf('ا', 'د', 'ذ', 'ر', 'ز', 'و', 'ى', 'ه', 'ء')

        // Remove diacritics and vowels first
        val cleanedInput = input.replace(Regex(arabicDiacriticsAndVowels), "")

        // Filter for consonants without dots
        return cleanedInput.filter { it in lettersWithoutDots || it.toString().matches(Regex(arabicLetters)) }
    }

    fun extractConsonants(input: String): String {
        // Define Arabic diacritics and vowels explicitly
        val arabicDiacriticsAndVowels = "[\\u064B-\\u0652\\u0670\\u06D6-\\u06ED]" // Includes diacritics, vowels, and related marks
        val arabicLetters = "[\\u0621-\\u064A]" // Matches only Arabic letters, excluding diacritics
        val alifkhan="[\\u0670-]"
        val removealifkhan=input.replace(Regex(alifkhan),"ا")
        // Remove diacritics and vowels
        val cleanedInput = input.replace(Regex(arabicDiacriticsAndVowels), "")

        // Filter out any remaining non-Arabic letters (for safety)
        return cleanedInput.filter { it.toString().matches(Regex(arabicLetters)) }
    }

    fun extractConsonantsgem(input: String): String {
        val arabicDiacritics = "[\\u064B-\\u065F\\u0670\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]" // Arabic diacritic Unicode range
        val vowels = "[ًٌٍَُِْ]" // Short vowels (fatha, kasra, damma, etc.)
        val dots = "[\\u064B-\\u0652]" // Matches Arabic dots (shadda, fathatan, dammatan, kasratan, etc.)
        val arabicLetters = "[\\u0621-\\u064A]" // Matches Arabic letters

        // Remove diacritics, vowels, and dots
        return input.replace(Regex("$arabicDiacritics|$vowels|$dots"), "")
            .filter { it.toString().matches(Regex(arabicLetters)) }
    }

    fun extractConsonantss(input: String): String {
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
