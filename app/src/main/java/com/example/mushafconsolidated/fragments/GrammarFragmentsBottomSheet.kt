package com.example.mushafconsolidated.fragments


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
</pre> *
 */


import Utility.ArabicLiterals
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.Constant

import com.example.Constant.BCYAN
import com.example.Constant.CYANLIGHTEST
import com.example.Constant.FORESTGREEN
import com.example.Constant.GOLD
import com.example.Constant.GREENDARK
import com.example.Constant.KASHMIRIGREEN
import com.example.Constant.ORANGE400
import com.example.Constant.WBURNTUMBER
import com.example.Constant.WHOTPINK
import com.example.Constant.deepburnsienna
import com.example.Constant.harfinnaspanDark
import com.example.Constant.harfismspanDark
import com.example.Constant.harfkhabarspanDark
import com.example.Constant.harfshartspanDark
import com.example.Constant.jawabshartspanDark
import com.example.Constant.prussianblue
import com.example.Constant.shartspanDark
import com.example.Constant.sifaspansDark
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NegationEnt

import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity

import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.model.SarfSagheerPOJO
import com.example.mushafconsolidated.model.Word
import com.example.mushafconsolidated.quranrepo.QuranRepository
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.utility.CorpusUtilityorig
import com.example.utility.CorpusUtilityorig.Companion
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.toTypedArray

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
</pre> *
 */
@AndroidEntryPoint
class GrammerFragmentsBottomSheet : BottomSheetDialogFragment() {

    enum class NasbPart { HARF, ISM, KHABAR }

    private var whichwbw: String? = null

    //   private enum class NasbPart { HARF, ISM, KHABAR }
    val model: QuranViewModel by viewModels()
    private var dark: Boolean = false
    private var quran: List<QuranEntity>? = null
    private var corpusSurahWord: List<CorpusEntity>? = null
    var chapterid = 0
    private var ayanumber = 0
    private var isverbconjugaton = false
    private var participles = false
    lateinit var expandableListView: ExpandableListView
    var expandableListTitle: List<String>? = null
    var expandableListDetail: LinkedHashMap<String, List<SpannableString>>? = null
    private var kanaExpandableListDetail: List<SpannableString>? = null
    var vbdetail = HashMap<String, String>()
    var wordbdetail: HashMap<String, SpannableStringBuilder>? = null
    private var showGrammarFragments = false
    private var ThulathiMazeedConjugatonList: ArrayList<ArrayList<*>>? = null
    var sarf: SarfSagheerPOJO? = null
    private var dialog: AlertDialog? = null

    @Inject
    lateinit var quranRepository: QuranRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.expand_list, container, false)
        // RecyclerView recyclerView = view.findViewById(R.id.wordByWordRecyclerView);
        expandableListView = view.findViewById<View>(R.id.expandableListView) as ExpandableListView
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
        val bundle = this.requireArguments()
        val stringArray = bundle.getStringArray(ARG_OPTIONS_DATA)

        chapterid = stringArray!![0].toInt()
        val shared = PreferenceManager.getDefaultSharedPreferences(
            requireContext()
        )
        showGrammarFragments = shared.getBoolean("fragments", false)
        ayanumber = stringArray[1].toInt()
        val ex = Executors.newSingleThreadExecutor()
        val utils = Utils(activity)
        val model = ViewModelProvider(requireActivity())[QuranViewModel::class.java]
        val corpusAndQurandata = quranRepository.CorpusAndQuranDataSurah(chapterid)
        corpusSurahWord = model.getCorpusEntityFilterSurahAya(chapterid, ayanumber)
        quran = model.getsurahayahVerseslist(chapterid, ayanumber).value

        expandableListDetail = getData()
        kanaExpandableListDetail = kana
        expandableListTitle = ArrayList(expandableListDetail!!.keys)
        ThulathiMazeedConjugatonList = ArrayList()
        isverbconjugaton = false
        participles = false
        //  val   corpusNounWord=  model.getNouncorpus(chapterid,ayanumber,1)
        ex.execute(object : Runnable {
            override fun run() {
                activity!!.runOnUiThread { dialog!!.show() }

                activity!!.runOnUiThread {
                    ex.shutdown()
                    dialog!!.dismiss()
                    val grammarFragmentsListAdapter: GrammarFragmentsListAdapter =
                        GrammarFragmentsListAdapter(
                            context!!, expandableListTitle as ArrayList<String>,
                            expandableListDetail!!
                        )
                    expandableListView.setAdapter(grammarFragmentsListAdapter)
                    for (i in 0 until grammarFragmentsListAdapter.groupCount) {
                        expandableListView.expandGroup(i)
                    }
                }
            }
        })
        return view
    }

    companion object {
        const val TAG = "bottom"

        // TODO: Customize parameter argument names
        private const val ARG_OPTIONS_DATA = "options_data"

        // TODO: Customize parameters
        fun newInstance(data: Array<String>): GrammerFragmentsBottomSheet {
            val fragment = GrammerFragmentsBottomSheet()
            val args = Bundle()
            args.putStringArray(ARG_OPTIONS_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }


    private fun getData(): java.util.LinkedHashMap<String, List<SpannableString>> {
        //  val utils=Utils(requireContext())
        val prefs =
            android.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        val preferences = prefs.getString("theme", "dark")
        dark = preferences == "dark" || preferences == "blue" || preferences == "green"
        val whichtranslation = prefs.getString("selecttranslation", "en_sahih")
        whichwbw = prefs.getString("wbw", "en")
        val expandableListDetail = java.util.LinkedHashMap<String, List<SpannableString>>()
        val verse: MutableList<SpannableString> = ArrayList()
        val translation: MutableList<SpannableString> = ArrayList()
        verse.add(
            SpannableString.valueOf(
                corpusSurahWord!![0].surah.toString() + ":" + corpusSurahWord!![0].surah + ":-" + quran!![0].qurantext
            )
        )
        if (whichtranslation == "en_sahih") {
            translation.add(SpannableString.valueOf(quran!![0].translation))
        } else if (whichtranslation == "en_arberry") {
            translation.add(SpannableString.valueOf(quran!![0].en_arberry))
        } else if (whichtranslation == "en_jalalayn") {
            translation.add(SpannableString.valueOf(quran!![0].en_jalalayn))
        } else {
            translation.add(SpannableString.valueOf(quran!![0].translation))
        }
        val shartarray: MutableList<SpannableString> = ArrayList()
        newSetShart(shartarray)
        val harfnasbarray: MutableList<SpannableString> = ArrayList()
        setNewNasb(harfnasbarray)
        val mausoofsifaarray: MutableList<SpannableString> = ArrayList()
        setMausoof(mausoofsifaarray)
        val mudhafarray: MutableList<SpannableString> = ArrayList()
        setMudhaf(mudhafarray)
        val kanaarray: MutableList<SpannableString> = ArrayList()
        newsetKana(kanaarray)
        val PastTenceNegationArray:MutableList<SpannableString> = ArrayList()
        setPastTenceNegation(PastTenceNegationArray)

        val presentTenceNegationArray:MutableList<SpannableString> = ArrayList()
        setPresentTenceNegation(presentTenceNegationArray)


        val futureTenceNegationArray:MutableList<SpannableString> = ArrayList()
        setFutureTenceNegation(futureTenceNegationArray)

        val InMaIllaNegationArray:MutableList<SpannableString> = ArrayList()
        setInMaIllaNegation(InMaIllaNegationArray)

                val note1 = String.format(
            "harf (لَمْ) only occurs with an imperfect/mudhary verb and it can push the meaning to the past tense "

        )
        val note2 = String.format(
                    " and harf (%s) only occurs with a perfect/mudhary verb and is used for refutation or in the context of debate",
            "\u202Bمَا\u202C"
        )

        if(PastTenceNegationArray.isNotEmpty()){
            PastTenceNegationArray.add(SpannableString.valueOf(note1))
            PastTenceNegationArray.add(SpannableString.valueOf(note2))
        }

        expandableListDetail["Verse"] = verse
        expandableListDetail["Translation"] = translation
        expandableListDetail["Verb sentence-Past Tence Negation- Stences(لَمْ/مَا))"] = PastTenceNegationArray
        expandableListDetail["Verb sentence-Present Tence Negation- Stences(مَا/لَا)"] = presentTenceNegationArray
        expandableListDetail["Verb sentence-Future Tence Negation- Stences(لَّن)"] = futureTenceNegationArray
        expandableListDetail["Exceptive Sentences with (إلاّ-(أداة الاستْثناء)) & Restriction/Exclusive (الحَصْر) "] = InMaIllaNegationArray
        expandableListDetail["Conditional/جملة شرطية\""] = shartarray
        expandableListDetail["Accusative/ "] = harfnasbarray
        expandableListDetail["Verb kāna/كان واخواتها"] = kanaarray
        expandableListDetail["Adjectival Phrases/مرکب توصیفی"] = mausoofsifaarray
        expandableListDetail["Possessive/إضافَة"] = mudhafarray

        return expandableListDetail
    }

    private fun setInMaIllaNegation(inMaIllaNegationArray: MutableList<SpannableString>) {


        val utils = Utils(requireContext())
        //   ArrayList<MudhafEntity> mudhafSurahAyah = utils.getMudhafSurahAyah(chapterid, ayanumber);
        val mudhafSurahAyah: List<NegationEnt>? =
            utils.getNegationFilterSurahAyahType(chapterid, ayanumber,"exp")
        if (mudhafSurahAyah != null) {
            for (mudhafEntity in mudhafSurahAyah) {

                val negationForegroundColorSpan = if (CorpusUtilityorig.dark) Constant.GOLD else Constant.FORESTGREEN

                val quranverses: String = quran!![0].qurantext
                val spannable = SpannableString(quranverses)
                spannable.setSpan(
                    negationForegroundColorSpan,
                    mudhafEntity.startindex,
                    mudhafEntity.endindex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val sequence =
                    spannable.subSequence(mudhafEntity.startindex, mudhafEntity.endindex)
                inMaIllaNegationArray.add(sequence as SpannableString)
                val sb = StringBuilder()
                val wordfrom = mudhafEntity.wordfrom
                val wordto = mudhafEntity.wordto
                val strings =
                    sequence.toString().split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val mudhafTranslation = StringBuilder()
                if (strings.size >= 2) {
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            mudhafTranslation.append(temp).append(" ")
                        }
                    }

                    inMaIllaNegationArray.add(SpannableString.valueOf(mudhafTranslation.toString()))
                    inMaIllaNegationArray.add(SpannableString.valueOf("\n"))

                } else {
                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordto,
                        wordto
                    )
                    getWordTranslation(sb, list)

                    inMaIllaNegationArray.add(SpannableString.valueOf(sb))



                }

            }
        }



    }

    private fun setFutureTenceNegation(futureTenceNegationArray: MutableList<SpannableString>) {
        val utils = Utils(requireContext())
        //   ArrayList<MudhafEntity> mudhafSurahAyah = utils.getMudhafSurahAyah(chapterid, ayanumber);
        val mudhafSurahAyah: List<NegationEnt>? =
            utils.getNegationFilterSurahAyahType(chapterid, ayanumber,"future")
        if (mudhafSurahAyah != null) {
            for (mudhafEntity in mudhafSurahAyah) {
                val colorSpan = if (CorpusUtilityorig.dark) {
                    Constant.mudhafspanDarks
                } else {
                    Constant.mudhafspanLight
                }

                val quranverses: String = quran!![0].qurantext
                val spannable = SpannableString(quranverses)
                spannable.setSpan(
                    colorSpan,
                    mudhafEntity.startindex,
                    mudhafEntity.endindex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val sequence =
                    spannable.subSequence(mudhafEntity.startindex, mudhafEntity.endindex)
                futureTenceNegationArray.add(sequence as SpannableString)
                val sb = StringBuilder()
                val wordfrom = mudhafEntity.wordfrom
                val wordto = mudhafEntity.wordto
                val strings =
                    sequence.toString().split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val mudhafTranslation = StringBuilder()
                if (strings.size >= 2) {
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            mudhafTranslation.append(temp).append(" ")
                        }
                    }

                    futureTenceNegationArray.add(SpannableString.valueOf(mudhafTranslation.toString()))
                    futureTenceNegationArray.add(SpannableString.valueOf("\n"))

                } else {
                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordto,
                        wordto
                    )
                    getWordTranslation(sb, list)

                    futureTenceNegationArray.add(SpannableString.valueOf(sb))



                }

            }
        }





    }

    private fun setPresentTenceNegation(presentTenceNegationArray: MutableList<SpannableString>) {

        val utils = Utils(requireContext())
        //   ArrayList<MudhafEntity> mudhafSurahAyah = utils.getMudhafSurahAyah(chapterid, ayanumber);
        val mudhafSurahAyah: List<NegationEnt>? =
            utils.getNegationFilterSurahAyahType(chapterid, ayanumber,"present")
        if (mudhafSurahAyah != null) {
            for (mudhafEntity in mudhafSurahAyah) {
                val colorSpan = if (CorpusUtilityorig.dark) {
                    Constant.mudhafspanDarks
                } else {
                    Constant.mudhafspanLight
                }

                val quranverses: String = quran!![0].qurantext
                val spannable = SpannableString(quranverses)
                spannable.setSpan(
                    colorSpan,
                    mudhafEntity.startindex,
                    mudhafEntity.endindex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val sequence =
                    spannable.subSequence(mudhafEntity.startindex, mudhafEntity.endindex)
                presentTenceNegationArray.add(sequence as SpannableString)
                val sb = StringBuilder()
                val wordfrom = mudhafEntity.wordfrom
                val wordto = mudhafEntity.wordto
                val strings =
                    sequence.toString().split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val mudhafTranslation = StringBuilder()
                if (strings.size >= 2) {
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            mudhafTranslation.append(temp).append(" ")
                        }
                    }

                    presentTenceNegationArray.add(SpannableString.valueOf(mudhafTranslation.toString()))
                    presentTenceNegationArray.add(SpannableString.valueOf("\n"))

                } else {
                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordto,
                        wordto
                    )
                    getWordTranslation(sb, list)

                    presentTenceNegationArray.add(SpannableString.valueOf(sb))



                }

            }
        }



    }

    private fun setPastTenceNegation(lamPastTenceNegationArray: MutableList<SpannableString>) {
        val utils = Utils(requireContext())
        //   ArrayList<MudhafEntity> mudhafSurahAyah = utils.getMudhafSurahAyah(chapterid, ayanumber);
        val mudhafSurahAyah: List<NegationEnt>? =
            utils.getNegationFilterSurahAyahType(chapterid, ayanumber,"past")
        if (mudhafSurahAyah != null) {
            for (mudhafEntity in mudhafSurahAyah) {
                val colorSpan = if (CorpusUtilityorig.dark) {
                    Constant.mudhafspanDarks
                } else {
                    Constant.mudhafspanLight
                }

                val quranverses: String = quran!![0].qurantext
                val spannable = SpannableString(quranverses)
                spannable.setSpan(
                    colorSpan,
                    mudhafEntity.startindex,
                    mudhafEntity.endindex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val sequence =
                    spannable.subSequence(mudhafEntity.startindex, mudhafEntity.endindex)
                lamPastTenceNegationArray.add(sequence as SpannableString)
                val sb = StringBuilder()
                val wordfrom = mudhafEntity.wordfrom
                val wordto = mudhafEntity.wordto
                val strings =
                    sequence.toString().split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val mudhafTranslation = StringBuilder()
                if (strings.size >= 2) {
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            mudhafTranslation.append(temp).append(" ")
                        }
                    }

                    lamPastTenceNegationArray.add(SpannableString.valueOf(mudhafTranslation.toString()))
                    lamPastTenceNegationArray.add(SpannableString.valueOf("\n"))

                } else {
                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordto,
                        wordto
                    )
                    getWordTranslation(sb, list)

                    lamPastTenceNegationArray.add(SpannableString.valueOf(sb))



                }

            }
        }
    }

    private fun setNewNasb(hasbarray: MutableList<SpannableString>) {
        val utils = Utils(requireContext())
        val nasabarray = model.getnasab(chapterid, ayanumber).value
        if (nasabarray != null) {
            for (nasbEntity in nasabarray) {
                val harfComponent = createNasbComponent(nasbEntity, NasbPart.HARF)
                val ismComponent = createNasbComponent(nasbEntity, NasbPart.ISM)
                val khabarComponent = createNasbComponent(nasbEntity, NasbPart.KHABAR)

                val components = listOfNotNull(harfComponent, ismComponent, khabarComponent)
                if (components.isNotEmpty()) {
                    val spannableComponents = components.map { component ->
                        createSpannable(component.text, getSpanColor(component.part, dark))
                    }

                    val combinedText = TextUtils.concat(*spannableComponents.toTypedArray())
                    hasbarray.add(SpannableString.valueOf(combinedText))

                    val translation = getTranslation(
                        utils,
                        nasbEntity,
                        harfComponent,
                        ismComponent,
                        khabarComponent
                    )
                    if (translation.isNotEmpty()) {
                        hasbarray.add(SpannableString.valueOf(translation))
                    }
                }
            }
        }
    }

    private fun createSpannable(text: String, color: Int): SpannableString {
        val spannable = SpannableString("$text ")

        spannable.setSpan(
            ForegroundColorSpan(color),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun getSpanColor(part: NasbPart, dark: Boolean): Int {
        return when (part) {
            NasbPart.HARF -> if (dark) Color.GREEN else R.color.kashmirigreen
            NasbPart.ISM -> if (dark) BCYAN else prussianblue
            NasbPart.KHABAR -> if (dark) Color.YELLOW else deepburnsienna
        }
    }

    private fun createNasbComponent(nasbEntity: NewNasbEntity, part: NasbPart): NasbComponent? {
        val quranText = quran!![0].qurantext
        return when (part) {
            NasbPart.HARF -> {
                if (nasbEntity.indexstart >= 0 && nasbEntity.indexend > 0) {
                    val text = quranText.substring(nasbEntity.indexstart, nasbEntity.indexend)
                    val wordRange = nasbEntity.harfwordno..nasbEntity.harfwordno
                    NasbComponent(part, text, wordRange)
                } else null
            }

            NasbPart.ISM -> {
                if (nasbEntity.ismstart >= 0 && nasbEntity.ismend > 0) {
                    val text = quranText.substring(nasbEntity.ismstart, nasbEntity.ismend)
                    val wordRange = nasbEntity.ismstartwordno..nasbEntity.ismendwordno
                    NasbComponent(part, text, wordRange)
                } else null
            }

            NasbPart.KHABAR -> {
                if (nasbEntity.khabarstart >= 0 && nasbEntity.khabarend > 0) {
                    val text = quranText.substring(nasbEntity.khabarstart, nasbEntity.khabarend)
                    val wordRange = nasbEntity.khabarstartwordno..nasbEntity.khabarendwordno
                    NasbComponent(part, text, wordRange)
                } else null
            }
        }
    }

    private fun getTranslation(
        utils: Utils,
        nasbEntity: NewNasbEntity,
        harfComponent: NasbComponent?,
        ismComponent: NasbComponent?,
        khabarComponent: NasbComponent?
    ): String {
        val sb = StringBuilder()
        val surah = corpusSurahWord!![0].surah
        val ayah = corpusSurahWord!![0].ayah

        if (harfComponent != null && ismComponent != null && khabarComponent != null) {
            if (nasbEntity.khabarstart - nasbEntity.ismend == 1) {
                appendTranslation(
                    utils,
                    sb,
                    utils.getwbwQuranbTranslation(
                        surah,
                        ayah,
                        harfComponent.wordRange.first,
                        khabarComponent.wordRange.last
                    )
                )
            } else {
                val wordByWordEntities = utils.getwbwQuranBySurahAyah(surah, ayah)
                appendTranslation(utils, sb, wordByWordEntities, harfComponent.wordRange)
                sb.append(".....")
                appendTranslation(utils, sb, wordByWordEntities, khabarComponent.wordRange)
            }
        } else if (harfComponent != null && khabarComponent != null) {
            val wordFrom = harfComponent.wordRange.first
            val wordTo =
                if (khabarComponent.text.split("\\s").size == 1) khabarComponent.wordRange.first else khabarComponent.wordRange.last
            if (nasbEntity.khabarstart - nasbEntity.indexend == 1) {
                appendTranslation(
                    utils,
                    sb,
                    utils.getwbwQuranbTranslation(surah, ayah, wordFrom, wordTo)
                )
            } else {
                val list = model.getwbwQuranTranslationRange(surah, ayah, wordFrom, wordFrom)

                sb.append(getWordTranslation(list)).append(".......")
                appendTranslation(
                    utils,
                    sb,
                    utils.getwbwQuranbTranslation(
                        surah,
                        ayah,
                        khabarComponent.wordRange.first,
                        khabarComponent.wordRange.last
                    )
                )
            }
        } else if (harfComponent != null && ismComponent != null) {
            if (nasbEntity.ismstart - nasbEntity.indexend == 1) {
                appendTranslation(
                    utils,
                    sb,
                    utils.getwbwQuranbTranslation(
                        surah,
                        ayah,
                        harfComponent.wordRange.first,
                        ismComponent.wordRange.last
                    )
                )
            } else {
                val harfList = model.getwbwQuranTranslationRange(
                    surah,
                    ayah,
                    harfComponent.wordRange.first,
                    harfComponent.wordRange.first
                )
                appendTranslation(utils, sb, harfList.value)
                sb.append(".....")
                appendTranslation(
                    utils,
                    sb,
                    utils.getwbwQuranbTranslation(
                        surah,
                        ayah,
                        ismComponent.wordRange.first,
                        ismComponent.wordRange.last
                    )
                )
            }
        } else if (harfComponent != null) {
            val list = model.getwbwQuranTranslationRange(
                surah,
                ayah,
                harfComponent.wordRange.first,
                harfComponent.wordRange.first
            )
            sb.append(getWordTranslation(list)).append(".......")
        }
        return sb.toString()
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

    private fun setNewNasbs(hasbarray: MutableList<SpannableString>) {
        val utils = Utils(requireContext())
        //    val nasabarray: List<NewNasbEntity> =
        //   utils.getHarfNasbIndSurahAyahSnew(chapterid, ayanumber)
        val nasabarray = model.getnasab(chapterid, ayanumber).value
        if (nasabarray != null) {
            for (nasbEntity in nasabarray) {
                //System.out.println("CHECK");
                if (dark) {
                    harfinnaspanDark = ForegroundColorSpan(Color.GREEN)
                    harfismspanDark = ForegroundColorSpan(BCYAN)
                    harfkhabarspanDark = ForegroundColorSpan(Color.YELLOW)
                } else {
                    harfinnaspanDark = ForegroundColorSpan(KASHMIRIGREEN)
                    harfismspanDark = ForegroundColorSpan(prussianblue)
                    harfkhabarspanDark = ForegroundColorSpan(deepburnsienna)
                }
                var harfofverse: String
                var ismofverse: String
                var khabarofverse: String
                val indexstart = nasbEntity.indexstart
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
                val a = isharfb && isism && iskhabar
                val d = isharfb && iskhabar
                val b = isharfb && isism
                val harfword = nasbEntity.harfwordno
                val shartSword = nasbEntity.ismstartwordno
                val shartEword = nasbEntity.ismendwordno
                val jawbSword = nasbEntity.khabarstartwordno
                val jawabEword = nasbEntity.khabarendwordno
                val sb = StringBuilder()
                val khabarsb = StringBuilder()
                var harfspannble: SpannableString
                var harfismspannable: SpannableString
                var khabarofversespannable: SpannableString
                if (a) {
                    val isismkhabarconnected = nasbEntity.khabarstart - nasbEntity.ismend
                    harfspannble = SpannableString(harfofverse)
                    harfismspannable = SpannableString(ismofverse)
                    khabarofversespannable = SpannableString(khabarofverse)
                    harfspannble.setSpan(
                        harfinnaspanDark,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    harfismspannable.setSpan(
                        harfismspanDark,
                        0,
                        ismofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    khabarofversespannable.setSpan(
                        harfkhabarspanDark,
                        0,
                        khabarofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    if (nasbEntity.ismstart > nasbEntity.khabarstart) {
                        val charSequence = TextUtils.concat(
                            harfspannble,
                            " ",
                            khabarofversespannable,
                            " ",
                            harfismspannable
                        )
                        hasbarray.add(SpannableString.valueOf(charSequence))
                    } else {
                        val charSequence = TextUtils.concat(
                            harfspannble,
                            " ",
                            harfismspannable,
                            " ",
                            khabarofversespannable
                        )
                        hasbarray.add(SpannableString.valueOf(charSequence))
                    }
                    if (isismkhabarconnected == 1) {
                        val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah, harfword, jawabEword
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                    } else {
                        val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                StringBuilder()
                                val temp: StringBuilder = w?.let { getSelectedTranslation(it) }!!
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in shartSword..shartEword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in jawbSword..jawabEword) {
                                    //     sb. append("... ");
                                    khabarsb.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(khabarsb)
                        hasbarray.add(SpannableString.valueOf(sb.toString()))
                    }
                    hasbarray.add(SpannableString.valueOf(sb.toString()))
                    //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
                } else if (d) {
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
                    hasbarray.add(SpannableString.valueOf(charSequence))
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
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                sb.append(temp).append(" ")
                            }
                        }
                        hasbarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        val wordfroms = nasbEntity.harfwordno
                        val list = model.getwbwQuranTranslationRange(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordfroms
                        )
                        val from = nasbEntity.khabarstartwordno
                        var to = nasbEntity.khabarendwordno
                        if (to == 0) {
                            to = from
                        }
                        getWordTranslation(sb, list)


                        val lists: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            from,
                            to
                        )
                        if (lists != null) {
                            for (w in lists) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                        hasbarray.add(SpannableString.valueOf(sb.toString()))
                    }
                } else if (b) {
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
                    //    kanaarray.add(SpannableString.valueOf(charSequence));
                    val ismconnected = nasbEntity.ismstart - nasbEntity.indexend
                    val wordfrom = nasbEntity.harfwordno
                    val wordto = nasbEntity.ismendwordno
                    if (ismconnected == 1) {
                        val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                        hasbarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        //    kanaarray.add(SpannableString.valueOf(list.get(0).getEn()));
                        val from = nasbEntity.harfwordno
                        val ismfrom = nasbEntity.ismstartwordno
                        val ismto = nasbEntity.ismendwordno
                        //     sb.append(list.get(0).getEn()).append("----");
                        val harf: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            from,
                            from
                        )
                        val ism: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            ismfrom,
                            ismto
                        )
                        if (harf != null) {
                            for (w in harf) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                        sb.append(".....")
                        if (ism != null) {
                            for (w in ism) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                        hasbarray.add(SpannableString.valueOf(sb.toString()))
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
                    val wordfroms = nasbEntity.harfwordno

                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfroms,
                        wordfroms
                    )


                    val sbs = getTranslationForLanguage(list)
                    hasbarray.add(SpannableString.valueOf(sbs))
                }
                // kanaarray.add(spannable);
            }
        }
    }

    private fun getWordTranslation(list: LiveData<List<wbwentity>>): String {
        val wbwEntity = list.value!![0] // Assuming list is not null and has at least one element
        return when (whichwbw) {
            "en" -> wbwEntity.en
            "ur" -> wbwEntity.ur!!
            "bn" -> wbwEntity.bn
            "id" -> wbwEntity.id.toString()
            else -> {
                wbwEntity.en
            }
        }
    }

    private fun getWordTranslation(
        sb: StringBuilder,
        list: LiveData<List<wbwentity>>
    ) {
      //  sb.append(list.value!![0].en).append(".......")
        when (whichwbw) {
            "en" -> sb.append(list.value!![0].en).append(".......")
            "ur" -> sb.append(list.value!![0].ur).append(".......")
            "bn" -> sb.append(list.value!![0].bn).append(".......")
            "id" -> sb.append(list.value!![0].id).append(".......")
            else -> {
                sb.append("tr.en")
            }
        }
    }

    private fun getSelectedTranslation(tr: wbwentity): StringBuilder {
        val sb = StringBuilder()
        when (whichwbw) {
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
    private fun newsetKana(kanaarray: MutableList<SpannableString>) {
        val kanaSurahAyahnew = model.getkana(chapterid, ayanumber).value
        val (harfkana, kanaism, kanakhbar) = if (dark) {
            Triple(ForegroundColorSpan(GOLD), ForegroundColorSpan(ORANGE400), ForegroundColorSpan(Color.CYAN))
        } else {
            Triple(ForegroundColorSpan(FORESTGREEN), ForegroundColorSpan(KASHMIRIGREEN), ForegroundColorSpan(WHOTPINK))
        }

        kanaSurahAyahnew?.forEach { kana ->
            val quranverses = quran!![0].qurantext
            val harfofverse = quranverses.substring(kana.indexstart, kana.indexend)
            val ismofverse = if (kana.ismkanaend > kana.ismkanastart) quranverses.substring(kana.ismkanastart, kana.ismkanaend) else ""
            val khabarofverse = quranverses.substring(kana.khabarstart, kana.khabarend)

            val isharfb = kana.indexstart >= 0 && kana.indexend > 0
            val isism = kana.ismkanastart >= 0 && kana.ismkanaend > 0
            val isjawab = kana.khabarstart >= 0 && kana.khabarend > 0

            val harfspannble = SpannableString(harfofverse).apply { setSpan(harfkana, 0, harfofverse.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
            val harfismspannable = SpannableString(ismofverse).apply { setSpan(kanaism, 0, ismofverse.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
            val khabarofversespannable = SpannableString(khabarofverse).apply { setSpan(kanakhbar, 0, khabarofverse.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
            val harfword = kana.harfwordno
            val ismSword = kana.ismwordo
            val ismEword = kana.ismendword
            val khabarSword = kana.khabarstartwordno
            val khabarEword = kana.khabarendwordno
            when {
                isharfb && isism && isjawab -> {

                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    harfismspannable.setSpan(
                        kanaism,
                        0,
                        ismofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    khabarofversespannable.setSpan(
                        kanakhbar,
                        0,
                        khabarofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    if (kana.ismkanastart > kana.khabarstart) {
                        val charSequence = TextUtils.concat(
                            harfspannble,
                            " ",
                            khabarofversespannable,
                            " ",
                            harfismspannable
                        )
                        kanaarray.add(SpannableString.valueOf(charSequence))
                    } else {
                        val charSequence = TextUtils.concat(
                            harfspannble,
                            " ",
                            harfismspannable,
                            " ",
                            khabarofversespannable
                        )
                        kanaarray.add(SpannableString.valueOf(charSequence))
                    }
                    val sb = StringBuilder()
                    val ismorkhabarsb = StringBuilder()
                    val utils = Utils(requireContext())
                    val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah
                    )
                    if (wbwayah != null) {
                        for (w in wbwayah) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w!!)
                            if (w.wordno == harfword) {
                                sb.append(temp.append(" "))
                            } else if (w.wordno in ismSword..ismEword) {
                                sb.append(temp).append(" ")
                            } else if (w.wordno in khabarSword..khabarEword) {
                                ismorkhabarsb.append(temp).append(" ")
                            }
                        }
                    }
                    sb.append("... ")
                    sb.append(ismorkhabarsb)
                    kanaarray.add(SpannableString.valueOf(sb.toString()))
                }
                isharfb && isjawab -> {
                    // ... (Logic for kanaHarfAndJawabOnly)

                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    khabarofversespannable.setSpan(
                        kanakhbar,
                        0,
                        khabarofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(harfspannble, " ", khabarofversespannable)
                    kanaarray.add(SpannableString.valueOf(charSequence))
                    val sb = StringBuilder()
                    val wordfrom = kana.harfwordno
                    var wordto: Int
                    val split = khabarofverse.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    wordto = if (split.size == 1) {
                        kana.khabarstartwordno
                    } else {
                        kana.khabarendwordno
                    }
                    val isconnected = kana.khabarstartwordno - kana.harfwordno
                    if (isconnected == 1) {
                        val utils = Utils(requireContext())
                        val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        val wordfroms = kana.harfwordno

                        val list = model.getwbwQuranTranslationRange(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordfroms
                        )
                        val from = kana.khabarstartwordno
                        var to = kana.khabarendwordno
                        if (to == 0) {
                            to = from
                        }
                        val sbs = getTranslationForLanguage(list)

                        //    sb.append(list).append("----");
                        val utils = Utils(requireContext())
                        val lists: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            from,
                            to
                        )
                        if (lists != null) {
                            for (w in lists) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                sbs.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    }


                }
                isharfb && isism -> {
                    // ... (Logic for kanaHarfAndIsmOnly)

                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    harfismspannable.setSpan(
                        kanaism,
                        0,
                        ismofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequences = TextUtils.concat(
                        harfspannble,
                        " $harfismspannable"
                    )
                    kanaarray.add(SpannableString.valueOf(charSequences))
                    //    kanaarray.add(SpannableString.valueOf(charSequence));
                    val sb = StringBuilder()
                    val ismorkhabarsb = StringBuilder()
                    val ismconnected = kana.ismkanastart - kana.indexend
                    val wordfrom = kana.harfwordno
                    val wordto = kana.ismwordo
                    if (ismconnected == 1) {
                        val utils = Utils(requireContext())
                        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        //  ArrayList<wbwentity> list = utils.getwbwQuranbTranslation(corpusSurahWord!!.get(0).getSurah(), corpusSurahWord!!.get(0).getAyah(), wordfroms, wordfroms);
                        val utils = Utils(requireContext())
                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in ismSword..ismEword) {
                                    ismorkhabarsb.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(ismorkhabarsb)
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    }
                }
                isharfb -> {
                    // ... (Logic for isharfb only)


                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(harfspannble)
                    kanaarray.add(SpannableString.valueOf(charSequence))
                    val wordfroms = kana.harfwordno
                    val utils = Utils(requireContext())
                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfroms,
                        wordfroms
                    )
                    val sb = getTranslationForLanguage(list)
                    kanaarray.add(SpannableString.valueOf(sb))
                }
            }
        }
    }
    private fun newsetKanas(kanaarray: MutableList<SpannableString>) {
        //  val utils = Utils(requireContext())
        //  val kanaSurahAyahnew: List<NewKanaEntity?>? =
        //    utils.getKanaSurahAyahnew(chapterid, ayanumber)

        val kanaSurahAyahnew = model.getkana(chapterid, ayanumber).value
        var harfkana: ForegroundColorSpan?
        var kanaism: ForegroundColorSpan?
        var kanakhbar: ForegroundColorSpan?
        if (dark) {
            harfkana = ForegroundColorSpan(GOLD)
            kanaism = ForegroundColorSpan(ORANGE400)
            kanakhbar = ForegroundColorSpan(Color.CYAN)
        } else {
            harfkana = ForegroundColorSpan(FORESTGREEN)
            kanaism = ForegroundColorSpan(KASHMIRIGREEN)
            kanakhbar = ForegroundColorSpan(WHOTPINK)
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
                val allPartofKana = isharfb && isism && isjawab
                val kanaHarfAndJawabOnly = isharfb && isjawab
                val kanaHarfAndIsmOnly = isharfb && isism
                val harfword = kana.harfwordno
                val ismSword = kana.ismwordo
                val ismEword = kana.ismendword
                val khabarSword = kana.khabarstartwordno
                val khabarEword = kana.khabarendwordno
                var harfspannble: SpannableString
                var harfismspannable: SpannableString
                var khabarofversespannable: SpannableString
                if (allPartofKana) {
                    harfspannble = SpannableString(harfofverse)
                    harfismspannable = SpannableString(ismofverse)
                    khabarofversespannable = SpannableString(khabarofverse)
                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    harfismspannable.setSpan(
                        kanaism,
                        0,
                        ismofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    khabarofversespannable.setSpan(
                        kanakhbar,
                        0,
                        khabarofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    if (kana.ismkanastart > kana.khabarstart) {
                        val charSequence = TextUtils.concat(
                            harfspannble,
                            " ",
                            khabarofversespannable,
                            " ",
                            harfismspannable
                        )
                        kanaarray.add(SpannableString.valueOf(charSequence))
                    } else {
                        val charSequence = TextUtils.concat(
                            harfspannble,
                            " ",
                            harfismspannable,
                            " ",
                            khabarofversespannable
                        )
                        kanaarray.add(SpannableString.valueOf(charSequence))
                    }
                    val sb = StringBuilder()
                    val ismorkhabarsb = StringBuilder()
                    val utils = Utils(requireContext())
                    val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah
                    )
                    if (wbwayah != null) {
                        for (w in wbwayah) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w!!)
                            if (w.wordno == harfword) {
                                sb.append(temp.append(" "))
                            } else if (w.wordno in ismSword..ismEword) {
                                sb.append(temp).append(" ")
                            } else if (w.wordno in khabarSword..khabarEword) {
                                ismorkhabarsb.append(temp).append(" ")
                            }
                        }
                    }
                    sb.append("... ")
                    sb.append(ismorkhabarsb)
                    kanaarray.add(SpannableString.valueOf(sb.toString()))

                    //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
                } else if (kanaHarfAndJawabOnly) {
                    if (dark) {
                        harfkana = ForegroundColorSpan(GOLD)
                        kanaism = ForegroundColorSpan(ORANGE400)
                        kanakhbar = ForegroundColorSpan(Color.CYAN)
                    } else {
                        harfkana = ForegroundColorSpan(FORESTGREEN)
                        kanaism = ForegroundColorSpan(KASHMIRIGREEN)
                        kanakhbar = ForegroundColorSpan(WHOTPINK)
                    }
                    harfspannble = SpannableString(harfofverse)
                    khabarofversespannable = SpannableString(khabarofverse)
                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    khabarofversespannable.setSpan(
                        kanakhbar,
                        0,
                        khabarofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(harfspannble, " ", khabarofversespannable)
                    kanaarray.add(SpannableString.valueOf(charSequence))
                    val sb = StringBuilder()
                    val wordfrom = kana.harfwordno
                    var wordto: Int
                    val split = khabarofverse.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    wordto = if (split.size == 1) {
                        kana.khabarstartwordno
                    } else {
                        kana.khabarendwordno
                    }
                    val isconnected = kana.khabarstartwordno - kana.harfwordno
                    if (isconnected == 1) {
                        val utils = Utils(requireContext())
                        val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w!!)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        val wordfroms = kana.harfwordno

                        val list = model.getwbwQuranTranslationRange(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordfroms
                        )
                        val from = kana.khabarstartwordno
                        var to = kana.khabarendwordno
                        if (to == 0) {
                            to = from
                        }
                        val sbs = getTranslationForLanguage(list)

                        //    sb.append(list).append("----");
                        val utils = Utils(requireContext())
                        val lists: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            from,
                            to
                        )
                        if (lists != null) {
                            for (w in lists) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                sbs.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    }
                } else if (kanaHarfAndIsmOnly) {
                    if (dark) {
                        harfkana = ForegroundColorSpan(GOLD)
                        kanaism = ForegroundColorSpan(ORANGE400)
                        kanakhbar = ForegroundColorSpan(Color.CYAN)
                    } else {
                        harfkana = ForegroundColorSpan(FORESTGREEN)
                        kanaism = ForegroundColorSpan(KASHMIRIGREEN)
                        kanakhbar = ForegroundColorSpan(WHOTPINK)
                    }
                    harfspannble = SpannableString(harfofverse)
                    harfismspannable = SpannableString(ismofverse)
                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    harfismspannable.setSpan(
                        kanaism,
                        0,
                        ismofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequences = TextUtils.concat(
                        harfspannble,
                        " $harfismspannable"
                    )
                    kanaarray.add(SpannableString.valueOf(charSequences))
                    //    kanaarray.add(SpannableString.valueOf(charSequence));
                    val sb = StringBuilder()
                    val ismorkhabarsb = StringBuilder()
                    val ismconnected = kana.ismkanastart - kana.indexend
                    val wordfrom = kana.harfwordno
                    val wordto = kana.ismwordo
                    if (ismconnected == 1) {
                        val utils = Utils(requireContext())
                        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                sb.append(temp).append(" ")
                            }
                        }
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    } else {
                        //  ArrayList<wbwentity> list = utils.getwbwQuranbTranslation(corpusSurahWord!!.get(0).getSurah(), corpusSurahWord!!.get(0).getAyah(), wordfroms, wordfroms);
                        val utils = Utils(requireContext())
                        val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah
                        )
                        if (wbwayah != null) {
                            for (w in wbwayah) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                if (w.wordno == harfword) {
                                    sb.append(temp).append(" ")
                                } else if (w.wordno in ismSword..ismEword) {
                                    ismorkhabarsb.append(temp).append(" ")
                                }
                            }
                        }
                        sb.append(".....")
                        sb.append(ismorkhabarsb)
                        kanaarray.add(SpannableString.valueOf(sb.toString()))
                    }
                } else if (isharfb) {
                    harfspannble = SpannableString(harfofverse)
                    harfspannble.setSpan(
                        harfkana,
                        0,
                        harfofverse.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(harfspannble)
                    kanaarray.add(SpannableString.valueOf(charSequence))
                    val wordfroms = kana.harfwordno
                    val utils = Utils(requireContext())
                    val list = model.getwbwQuranTranslationRange(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfroms,
                        wordfroms
                    )
                    val sb = getTranslationForLanguage(list)
                    kanaarray.add(SpannableString.valueOf(sb))
                }
                // kanaarray.add(spannable);
            }
        }
    }

    private fun getTranslationForLanguage(list: LiveData<List<wbwentity>>): StringBuffer {
        val sb = StringBuffer()
        when (whichwbw) {
            "en" -> sb.append(list.value!![0].en).append(".......")
            "ur" -> sb.append(list.value!![0].ur).append(".......")
            "bn" -> sb.append(list.value!![0].bn).append(".......")
            "id" -> sb.append(list.value!![0].id).append(".......")
            else -> {
                sb.append(list.value!![0].en).append(".......")

            }
        }
            return sb
        }

        private fun setMausoof(mausoofsifaarray: MutableList<SpannableString>) {
            val utils = Utils(requireContext())
            val sifabySurahAyah: List<SifaEntity>? =
                utils.getSifabySurahAyah(chapterid, ayanumber)
            if (sifabySurahAyah != null) {
                for (shartEntity in sifabySurahAyah) {
                    sifaspansDark = if (dark) {
                        BackgroundColorSpan(WBURNTUMBER)
                    } else {
                        BackgroundColorSpan(CYANLIGHTEST)
                    }

                    //   sifaspansDark = new BackgroundColorSpan(WBURNTUMBER);
                    val quranverses: String = quran!![0].qurantext
                    val spannable = SpannableString(quranverses)
                    try {
                        spannable.setSpan(
                            sifaspansDark,
                            shartEntity.startindex,
                            shartEntity.endindex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } catch (e: IndexOutOfBoundsException) {
                        println(shartEntity.surah.toString() + "  " + shartEntity.ayah + "  " + quranverses)
                    }
                    val sequence =
                        spannable.subSequence(shartEntity.startindex, shartEntity.endindex)
                    mausoofsifaarray.add(sequence as SpannableString)
                    val wordfrom = shartEntity.wordfrom - 1
                    val wordto = shartEntity.wordfrom
                    val ssb = StringBuilder()
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpusSurahWord!![0].surah,
                        corpusSurahWord!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            ssb.append(temp).append(" ")
                        }
                    }
                    mausoofsifaarray.add(SpannableString.valueOf(ssb.toString()))
                }
            }
        }

        private fun setMudhaf(mudhafarray: MutableList<SpannableString>) {
            val utils = Utils(requireContext())
            //   ArrayList<MudhafEntity> mudhafSurahAyah = utils.getMudhafSurahAyah(chapterid, ayanumber);
            val mudhafSurahAyah: List<NewMudhafEntity>? =
                utils.getMudhafSurahAyahNew(chapterid, ayanumber)
            if (mudhafSurahAyah != null) {
                for (mudhafEntity in mudhafSurahAyah) {
                    val colorSpan = if (CorpusUtilityorig.dark) {
                        Constant.mudhafspanDarks
                    } else {
                        Constant.mudhafspanLight
                    }

                    val quranverses: String = quran!![0].qurantext
                    val spannable = SpannableString(quranverses)
                    spannable.setSpan(
                        colorSpan,
                        mudhafEntity.startindex,
                        mudhafEntity.endindex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val sequence =
                        spannable.subSequence(mudhafEntity.startindex, mudhafEntity.endindex)
                    mudhafarray.add(sequence as SpannableString)
                    val sb = StringBuilder()
                    val wordfrom = mudhafEntity.wordfrom
                    val wordto = mudhafEntity.wordto
                    val strings =
                        sequence.toString().split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val mudhafTranslation = StringBuilder()
                    if (strings.size == 2) {
                        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordfrom,
                            wordto
                        )
                        if (list != null) {
                            for (w in list) {
                                StringBuilder()
                                val temp: StringBuilder = getSelectedTranslation(w)
                                mudhafTranslation.append(temp).append(" ")
                            }
                        }

                        mudhafarray.add(SpannableString.valueOf(mudhafTranslation.toString()))
                        mudhafarray.add(SpannableString.valueOf("\n"))
                    } else {
                        val list = model.getwbwQuranTranslationRange(
                            corpusSurahWord!![0].surah,
                            corpusSurahWord!![0].ayah,
                            wordto,
                            wordto
                        )
                        getWordTranslation(sb, list)

                        mudhafarray.add(SpannableString.valueOf(sb))
                    }
                }
            }
        }

        private fun newSetShart(shartarray: MutableList<SpannableString>) {
            val utils = Utils(requireContext())
            val quranverses: String = quran!![0].qurantext
            val shart: List<NewShartEntity>? =
                utils.getShartSurahAyahNew(chapterid, ayanumber)
            // String quranverses = corpusSurahWord!!.get(0).getQurantext();
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
                    val allPartofShart = isharfb && isshart && isjawab
                    val harfAndShartOnly = isharfb && isshart
                    var harfspannble: SpannableString
                    var shartspoannable: SpannableString
                    var jawabshartspannable: SpannableString
                    if (dark) {
                        harfshartspanDark = ForegroundColorSpan(GOLD)
                        shartspanDark = ForegroundColorSpan(ORANGE400)
                        jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
                    } else {
                        harfshartspanDark = ForegroundColorSpan(FORESTGREEN)
                        shartspanDark = ForegroundColorSpan(KASHMIRIGREEN)
                        jawabshartspanDark = ForegroundColorSpan(WHOTPINK)
                    }
                    if (allPartofShart) {
                        harfspannble = SpannableString(harfofverse)
                        shartspoannable = SpannableString(shartofverse)
                        jawabshartspannable = SpannableString(jawabofverrse)
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
                        val connected = jawabstart - shartindexend
                        if (connected == 1) {
                            val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                                corpusSurahWord!![0].surah,
                                corpusSurahWord!![0].ayah, harfword, jawabEword
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
                                corpusSurahWord!![0].surah,
                                corpusSurahWord!![0].ayah
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
                                corpusSurahWord!![0].surah,
                                corpusSurahWord!![0].ayah,
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
                                corpusSurahWord!![0].surah,
                                corpusSurahWord!![0].ayah
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
                        val trstr = getFragmentTranslations(quranverses, charSequence)
                        shartarray.add(SpannableString.valueOf(charSequence))
                        shartarray.add(trstr)
                    }
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
            val utils = Utils(requireContext())
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
                corpusSurahWord!![0].surah,
                corpusSurahWord!![0].ayah,
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

        val kana: List<SpannableString>
        get() {
            val kanaarray: MutableList<SpannableString> = ArrayList()
            newsetKana(kanaarray)
            return kanaarray
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
    }

    private data class NasbComponent(
        val part: GrammerFragmentsBottomSheet.NasbPart,
        val text: String,
        val wordRange: IntRange
    )