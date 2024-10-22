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
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.WordMorphologyDetails
import com.example.mushafconsolidated.model.QuranCorpusWbw
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils
import android.graphics.Color
import android.graphics.Paint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.preference.PreferenceManager
import com.example.mushafconsolidated.Adapters.ArabicIrabProvider
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import org.ahocorasick.trie.Trie
import kotlin.collections.getOrPut

object QuranViewUtils {
    private val absoluteNegationCache = HashMap<Pair<Int, Int>, List<Int>>()
    private val sifaCache = HashMap<Pair<Int, Int>, MutableList<List<Int>>>()
    private val mudhafCache = HashMap<Pair<Int, Int>, MutableList<List<Int>>>()
    private lateinit var quranModel: QuranViewModel


    fun initialize(quranModel: QuranViewModel) {
        this.quranModel = quranModel // Initialize quranModel
    }



    fun <T : ArabicIrabProvider> HightLightKeyWord(
        dataList: List<T>,
        getArabicIrabTwo: (T) -> String? = { it.ar_irab_two } // Now it resolves
    ) {
        val inshartiastr = "«إِنْ» شرطية"
        val izazarfshartsrt = "وإذا ظرف يتضمن معنى الشرط"
        val izashartiastr = "«إِذا» ظرف يتضمن معنى الشرط"
        val jawabshartstr = "جواب شرط"
        val jawabsharttwostr = "لجواب الشرط"
        val jawabalshart = "جواب الشرط"
        val jawab = "جواب"
        val shart = ArrayList<String>()
        val mutlaq = ArrayList<String>()
        mutlaq.add("مطلق")
        mutlaq.add("مفعولا مطلقا")
        mutlaq.add("مفعولا مطلقا،")
        mutlaq.add("مطلق.")
        mutlaq.add("")
        shart.add(inshartiastr)
        shart.add(izazarfshartsrt)
        shart.add(izashartiastr)
        shart.add(jawabshartstr)
        shart.add(jawabsharttwostr)
        shart.add(jawabalshart)
        shart.add(jawab)
        shart.add("شرطية")
        shart.add("شرطية.")
        shart.add("ظرف متضمن معنى الشرط")
        shart.add("وإذا ظرف زمان يتضمن معنى الشرط")
        shart.add("ظرف زمان يتضمن معنى الشرط")
        shart.add("ولو حرف شرط غير جازم")
        shart.add("حرف شرط غير جازم")
        shart.add("اللام واقعة في جواب لو")
        shart.add("حرف شرط جازم")
        shart.add("الشرطية")
        val mudhafilahistr = "مضاف إليه"
        val sifastr = "صفة"
        val mudhaflenght = mudhafilahistr.length
        val sifalength = sifastr.length
        val hal = ArrayList<String>()
        hal.add("في محل نصب حال")
        hal.add("في محل نصب حال.")
        hal.add("والجملة حالية")
        hal.add("والجملة حالية.")
        hal.add("حالية")
        hal.add("حالية.")
        hal.add("حالية:")
        hal.add("حال")
        hal.add("حال:")
        hal.add("حال.")
        hal.add("الواو حالية")
        val tameez = ArrayList<String>()
        tameez.add("تمييز")
        tameez.add("تمييز.")
        tameez.add("التمييز")
        val badal = ArrayList<String>()
        badal.add("بدل")
        badal.add("بدل.")
        val ajilihi = ArrayList<String>()
        ajilihi.add("مفعول لأجله")
        ajilihi.add("لأجله")
        ajilihi.add("لأجله.")
        val mafoolbihi = ArrayList<String>()
        mafoolbihi.add("مفعول به")
        mafoolbihi.add("مفعول به.")
        mafoolbihi.add("مفعول به.(")
        mafoolbihi.add("في محل نصب مفعول")
        mafoolbihi.add("مفعول")
        val prefs =
            android.preference.PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context)
         var mudhafColoragainstBlack = 0
         var mausofColoragainstBlack = 0
         var sifatColoragainstBlack = 0
         var brokenPlurarColoragainstBlack = 0
         var shartagainstback = 0
         var surahorpart = 0
    val shared = PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context!!)
        val preferences = shared.getString("themepref", "dark")
        if (preferences == "dark" || preferences == "blue" || preferences == "green") {
            shartagainstback = prefs.getInt("shartback", Color.GREEN)
            mausofColoragainstBlack = prefs.getInt("mausoofblack", Color.RED)
            mudhafColoragainstBlack = prefs.getInt("mudhafblack", Color.CYAN)
            sifatColoragainstBlack = prefs.getInt("sifatblack", Color.YELLOW)
            brokenPlurarColoragainstBlack = prefs.getInt("brokenblack", Color.GREEN)
        } else {
            shartagainstback = prefs.getInt("shartback", Constant.INDIGO)
            mudhafColoragainstBlack = prefs.getInt("mausoofwhite", Color.GREEN)
            mausofColoragainstBlack = prefs.getInt("mudhafwhite", Constant.MIDNIGHTBLUE)
            sifatColoragainstBlack = prefs.getInt("sifatwhite", Constant.ORANGE400)
            brokenPlurarColoragainstBlack = prefs.getInt("brokenwhite", Constant.DARKMAGENTA)
        }
        for (dataItem in dataList) {
            val ar_irab_two = getArabicIrabTwo(dataItem)
            // ... (rest of your highlighting logic using ar_irab_two)
        }

        for (dataItem in dataList) {
            //  String ar_irab_two = pojo.getAr_irab_two();
            val ar_irab_two = getArabicIrabTwo(dataItem)
            val strstr = ar_irab_two!!.replace("\n", "-")
            val str = SpannableStringBuilder(strstr)
            val mudhaftrie = Trie.builder().onlyWholeWords().addKeywords(mudhafilahistr).build()
            val mudhafemit = mudhaftrie.parseText(ar_irab_two)
            val sifatrie = Trie.builder().onlyWholeWords().addKeywords(sifastr).build()
            val sifaemit = sifatrie.parseText(ar_irab_two)
            val jawabsharttwotrie =
                Trie.builder().onlyWholeWords().addKeywords(jawabsharttwostr).build()
            val jawabsharttwoemit = jawabsharttwotrie.parseText(ar_irab_two)
            val trieBuilder =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(shart).build()
            val emits = trieBuilder.parseText(ar_irab_two)
            val mutlaqbuilder =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(mutlaq).build()
            val mutlaqemits = mutlaqbuilder.parseText(ar_irab_two)
            val haltrie =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(hal).build()
            val halemits = haltrie.parseText(ar_irab_two)
            val tameeztrie =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(tameez).build()
            val tameezemit = tameeztrie.parseText(ar_irab_two)
            val badaltrie =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(badal).build()
            val badalemit = badaltrie.parseText(ar_irab_two)
            val ajilihitrie =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(ajilihi).build()
            val ajilihiemit = ajilihitrie.parseText(ar_irab_two)
            val mafoolbihitri =
                Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(mafoolbihi)
                    .build()
            val mafoolbihiemit = mafoolbihitri.parseText(ar_irab_two)
            for (emit in mafoolbihiemit) {
                str.setSpan(
                    ForegroundColorSpan(sifatColoragainstBlack),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in ajilihiemit) {
                str.setSpan(
                    ForegroundColorSpan(sifatColoragainstBlack),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in tameezemit) {
                str.setSpan(
                    ForegroundColorSpan(sifatColoragainstBlack),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in badalemit) {
                str.setSpan(
                    ForegroundColorSpan(sifatColoragainstBlack),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in halemits) {
                str.setSpan(
                    ForegroundColorSpan(shartagainstback),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in emits) {
                str.setSpan(
                    ForegroundColorSpan(shartagainstback),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in mutlaqemits) {
                str.setSpan(
                    ForegroundColorSpan(shartagainstback),
                    emit.start,
                    emit.start + emit.keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in mudhafemit) {
                str.setSpan(
                    ForegroundColorSpan(mausofColoragainstBlack),
                    emit.start,
                    emit.start + mudhaflenght,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            for (emit in sifaemit) {
                str.setSpan(
                    ForegroundColorSpan(mudhafColoragainstBlack),
                    emit.start,
                    emit.start + sifalength,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            //    colorerab.get(0).setErabspnabble(str);
            //dataItem.ar_irab_two = str
            println("check")
        }







        // ... (rest of your function)
    }


        // Function to load and cache Absolute Negation Data
        fun cacheAbsoluteNegationData(quranModel: QuranViewModel, surah: Int, absoluteNegationCache: MutableMap<Pair<Int, Int>, List<Int>>) {
            val absoluteNegationData = quranModel.getAbsoluteNegationFilterSurah(surah)
            for (data in absoluteNegationData) {
                val key = Pair(data.surahid, data.ayahid)
                val indexes = listOf(data.startindex, data.endindex)
                absoluteNegationCache[key] = indexes
            }
        }

        // Function to load and cache Sifa Data
        fun cacheSifaData(quranModel: QuranViewModel, surah: Int, sifaCache: MutableMap<Pair<Int, Int>, MutableList<List<Int>>>) {
            val sifaData = quranModel.getsifaFileterSurah(surah)
            for (data in sifaData) {
                val key = Pair(data.surah, data.ayah)
                val indexes = listOf(data.startindex, data.endindex)
                sifaCache.getOrPut(key) { mutableListOf() }.add(indexes)
            }
        }

        // Function to load and cache Mudhaf Data
        fun cacheMudhafData(quranModel: QuranViewModel, surah: Int, mudhafCache: HashMap<Pair<Int, Int>, MutableList<List<Int>>>) {
            val mudhafData = quranModel.getmudhafFilterSurah(surah)
            for (data in mudhafData) {
                val key = Pair(data.surah, data.ayah)
                val indexes = listOf(data.startindex, data.endindex)
                mudhafCache.getOrPut(key) { mutableListOf() }.add(indexes)
            }
        }

    fun cachePastTenceData(quranModel: QuranViewModel, surah: Int, pastTenceCache: MutableMap<Int, MutableMap<Int, Pair<SpannableString, String>>>) {
        val mudhafData = quranModel.getPastTencefilterSurah(surah)
        for (data in mudhafData) {
            val surahid = data.surahid
            val ayahid = data.ayahid
            val arabicString = data.arabictext
            val englishString = data.englishtext
            val spannableString = SpannableString(arabicString)
            spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, arabicString.length, 0)

            pastTenceCache.getOrPut(surahid) { mutableMapOf() }
                .put(ayahid, Pair(spannableString, englishString))
        }
    }
    fun cachePresentTenceData(quranModel: QuranViewModel, surah: Int, presentTenceCache: MutableMap<Int, MutableMap<Int, Pair<SpannableString, String>>>) {
        val mudhafData = quranModel.getPresentNegationFilterSurah(surah)
        for (data in mudhafData) {
            val surahid = data.surahid
            val ayahid = data.ayahid
            val arabicString = data.arabictext
            val englishString = data.englishtext
            val spannableString = SpannableString(arabicString)
            spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, arabicString.length, 0)

            presentTenceCache.getOrPut(surahid) { mutableMapOf() }
                .put(ayahid, Pair(spannableString, englishString))
        }
    }
    fun cacheFutureTenceData(quranModel: QuranViewModel, surah: Int, futureTenceCache: MutableMap<Int, MutableMap<Int, Pair<SpannableString, String>>>) {
        val mudhafData = quranModel.getFutureTencefilterSurah(surah)
        for (data in mudhafData) {
            val surahid = data.surahid
            val ayahid = data.ayahid
            val arabicString = data.arabictext
            val englishString = data.englishtext
            val spannableString = SpannableString(arabicString)
            spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, arabicString.length, 0)

            futureTenceCache.getOrPut(surahid) { mutableMapOf() }
                .put(ayahid, Pair(spannableString, englishString))
        }
    }
    fun cacheInMaIllaDta(quranModel: QuranViewModel, surah: Int, mudhafCache: MutableMap<Pair<Int, Int>, MutableList<List<Int>>>) {
        val mudhafData = quranModel.getmudhafFilterSurah(surah)
        for (data in mudhafData) {
            val key = Pair(data.surah, data.ayah)
            val indexes = listOf(data.startindex, data.endindex)
            mudhafCache.getOrPut(key) { mutableListOf() }.add(indexes)
        }
    }




    fun setWordClickListener(view: View, context: Context, word: QuranCorpusWbw, surahName: String, loadItemList: (Bundle, wbwentity) -> Unit) {
        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle(word.corpus!!.araone + word.corpus!!.aratwo + word.corpus!!.arathree + word.corpus!!.arafour + word.corpus!!.arafive)

            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.corpus!!.surah)
            dataBundle.putInt(Constant.AYAHNUMBER, word.corpus!!.ayah)
            dataBundle.putInt(Constant.WORDNUMBER, word.corpus!!.wordno)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahName)
            loadItemList(dataBundle,word.wbw)

        }
    }

        // ... other functions ...

        fun setWordTranslation(translation: TextView, word: CorpusEntity, wbw: String) {
            when (wbw) {
                "en" -> {
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

    fun NewsetWordClickListener(view: View, context: Context, word: CorpusEntity, surahName: String, loadItemList: (Bundle, CorpusEntity) -> Unit) {
        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle(word.araone + word.aratwo + word.arathree + word.arafour + word.arafive)

            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.surah)
            dataBundle.putInt(Constant.AYAHNUMBER, word.ayah)
            dataBundle.putInt(Constant.WORDNUMBER, word.wordno)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahName)
            loadItemList(dataBundle,word)

        }
    }


    fun setTranslationText(textView: TextView, translationNote: TextView, entity: QuranEntity?, translationKey: String, noteResId: Int) {
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

    fun showWordMorphologyTooltip(context: Context, view: View, word: QuranCorpusWbw, isNightmode: String) {
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
        val corpusNounWord:List<NounCorpus> =
            utils.getQuranNouns(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        val verbCorpusRootWord : List<VerbCorpus> =
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
            "brown","light" -> R.color.background_color
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

    fun NewshowWordMorphologyTooltip(context: Context, view: View, word: CorpusEntity, isNightmode: String) {
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
        val corpusNounWord:List<NounCorpus> =
            utils.getQuranNouns(
                word.surah,
                word.ayah,
                word.wordno
            )
        val verbCorpusRootWord : List<VerbCorpus> =
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
            "brown","light" -> R.color.background_color
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

        val tintColor = if (isNightMode == "dark" || isNightMode == "blue" || isNightMode == "green") {
            Color.CYAN
        } else {
            Color.BLUE
        }

        surahInfoTextView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        surahInfoTextView.compoundDrawableTintList = ColorStateList.valueOf(tintColor)
    }

    fun setLocationVisibility(makkiIcon: ImageView, madaniIcon: ImageView, location: RevalationCity) {
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

    fun storepreferences(context: Context,entity: QuranEntity, SurahName: String) {
        val pref = context.getSharedPreferences("lastread", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(Constant.SURAH_ID, entity.surah)
        editor.putInt(Constant.AYAH_ID, entity.ayah)
        editor.putString(Constant.SURAH_ARABIC_NAME, SurahName)
        editor.apply()
        // editor.commit();  // You don't need both apply() and commit()
    }



}