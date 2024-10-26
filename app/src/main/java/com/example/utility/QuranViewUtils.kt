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




    fun cacheNegationData(quranModel: QuranViewModel, surah: Int, negationCache: MutableMap<Int, MutableMap<Int, MutableList<SpannableString>>>) {
        val negantionData = quranModel.getNegationFilterSurah(surah)
        for (data in negantionData) {
            val surahid = data.surahid
            val ayahid = data.ayahid
            val arabicString = data.arabictext
            val englishString = data.englishtext
            val combinedString = "$arabicString\n$englishString"
            val spannableString = SpannableString(combinedString)





            spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, arabicString.length, 0) // Span for Arabic
            spannableString.setSpan(ForegroundColorSpan(Color.BLUE), arabicString.length + 1, combinedString.length, 0)


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
    fun collectBrokenPlurals(corpus: List<CorpusEntity>): List<String> {
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
            val currentWord =
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
                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$pattern|$currentWord|$currentmeaning"
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
            val englishtranslation=entry.en
            val isIndefniteNoun =
                (entry.tagone == "N") || (entry.tagone != "DET" && entry.tagtwo == "N")
                        || (entry.tagtwo != "DET" && entry.tagthree == "N") || (entry.tagthree != "DET" && entry.tagfour == "N")
            val isZarf =
                (entry.tagone == "T") || (entry.tagone != "DET" && entry.tagtwo == "T")
                        || (entry.tagtwo != "DET" && entry.tagthree == "T") || (entry.tagthree != "DET" && entry.tagfour == "T")
            var noundDetails = ""// Check for mudaf conditions
            if (entry.tagone == "T" ) {
                noundDetails = entry.detailsone.toString()
            } else if (entry.tagtwo == "T") {
                noundDetails = entry.detailstwo.toString()

            } else if (entry.tagthree == "T") {
                noundDetails = entry.detailsthree.toString()
            } else if (entry.tagfour == "T") {
                noundDetails = entry.detailsfour.toString()
            } else

                if (entry.tagone == "N" ) {
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

                    !constructedWord.contains("ٌ") && !constructedWord.contains("ً") && !constructedWord.contains("ٍ") && // No nunation
                    (nounCase == "GEN" || nounCase == "ACC" || nounCase == "NOM") // Allowed cases
            //   val tanweenRegex = Regex("[\\u064B-\\u0652-\\U064C]")
            val tanweenRegex= Regex( "[\\u064B-\\u064D-\\u064C]")
            //  val tanweenRegex = Regex("[ًٌٍ]") // Matches the three Tanween characters


            val result = hasNunation(constructedWord)
            if(result){
                println("check")
            }
            val isMudaf = (isIndefniteNoun || isZarf) &&
                    !result && // No nunation
                    (nounCase == "GEN" || nounCase == "ACC" || nounCase == "NOM")

            // If mudaf conditions met, look for mudaf ilaih in the next entry
            if((entry.tagone=="N" && entry.tagtwo=="PRON") || (entry.tagtwo=="N" && entry.tagthree=="PRON")){
                val fullword = constructedWord
                val startindex = qurantext.indexOf(fullword)
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
            }else



                if (isMudaf && i + 1 < corpusList.size) {
                    val nextEntry = corpusList[i + 1]
                    val nextConstructedWord =
                        nextEntry.araone + nextEntry.aratwo + nextEntry.arathree + nextEntry.arafour + nextEntry.arafive
                    val nextenglishword=nextEntry.en
                    val englishtran=englishtranslation+" "+nextenglishword
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
                        (nextEntry.tagone == "PN" || nextEntry.tagone == "N" ) ||
                                (nextEntry.tagtwo == "PN" || nextEntry.tagtwo == "N" ) ||
                                (nextEntry.tagthree == "PN" || nextEntry.tagthree == "N" )

                    val isAdjective =
                        (nextEntry.tagone == "ADJ" )||
                                (nextEntry.tagtwo == "ADJ") ||
                                (nextEntry.tagthree == "ADJ")

                    val isMudafIlaih = isNoun || isAdjective && // Must be noun or pronoun
                            nextEntryDetails.contains("GEN") // Must be in genitive case



                    if(isMudafIlaih) {

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
}