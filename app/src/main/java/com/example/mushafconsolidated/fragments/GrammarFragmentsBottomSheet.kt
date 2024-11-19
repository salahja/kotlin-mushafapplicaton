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
import android.text.SpannableString.valueOf
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
import com.example.mushafconsolidated.SpannableStringUtils

import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.model.SarfSagheerPOJO
import com.example.mushafconsolidated.model.Word
import com.example.mushafconsolidated.quranrepo.QuranRepository
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.utility.CorpusUtilityorig
import com.example.utility.QuranGrammarApplication
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

    private lateinit var negaTionList: List<NegationEnt>
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
        // corpusSurahWord = model.getCorpusEntityFilterSurahAya(chapterid, ayanumber)
        quran = model.getsurahayahVerseslist(chapterid, ayanumber).value
        negaTionList = utils.geTNegatonFilerSurahAyah(chapterid, ayanumber)

        expandableListDetail = getData()
        //  kanaExpandableListDetail = kana
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
                        expandableListView.collapseGroup(i)
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
        val utils = Utils(QuranGrammarApplication.context)


        /*verse.add(
              SpannableString.valueOf(
                  corpusSurahWord!![0].surah.toString() + ":" + corpusSurahWord!![0].surah + ":-" + quran!![0].qurantext
              )
          )*/
        verse.add(SpannableString.valueOf(quran!![0].qurantext))
        if (whichtranslation == "en_sahih") {
            translation.add(SpannableString.valueOf(quran!![0].translation))
        } else if (whichtranslation == "en_arberry") {
            translation.add(SpannableString.valueOf(quran!![0].en_arberry))
        } else if (whichtranslation == "en_jalalayn") {
            translation.add(SpannableString.valueOf(quran!![0].en_jalalayn))
        } else {
            translation.add(SpannableString.valueOf(quran!![0].translation))
        }

        val mausoofsifaarray: MutableList<SpannableString> = ArrayList()


        val mudhafarray: MutableList<SpannableString> = ArrayList()


        val shartarray: MutableList<SpannableString> = ArrayList()
        val harfnasbarray: MutableList<SpannableString> = ArrayList()
        val kanaarray: MutableList<SpannableString> = ArrayList()
        val PastTenceNegationArray: MutableList<SpannableString> = ArrayList()
        val presentTenceNegationArray: MutableList<SpannableString> = ArrayList()
        val futureTenceNegationArray: MutableList<SpannableString> = ArrayList()

        val InMaIllaNegationArray: MutableList<SpannableString> = ArrayList()





        setAllPhrases(
            shartarray,
            harfnasbarray,
            kanaarray,
            PastTenceNegationArray,
            presentTenceNegationArray,
            futureTenceNegationArray,
            InMaIllaNegationArray,
            mudhafarray,
            mausoofsifaarray
        )

        val pastNegNoteOne = String.format(
            "harf (-لَمْ-) only occurs with an imperfect/mudhary verb and it can push the meaning to the past tense "

        )
        val pastNegNoteTwo = String.format(
            " and harf (%s) only occurs with a perfect/madhi verb and is used for refutation or in the context of debate",
            "\u202Bمَا\u202C"
        )
        val presentNegNote = String.format(
            " two combination La-Nafiya+Mudharay  and Ma-Mudhary for refutation or in the context of debate",
            "\u202Bمَا\u202C"
        )
        if (PastTenceNegationArray.isNotEmpty()) {
            PastTenceNegationArray.add(SpannableString.valueOf(pastNegNoteOne))
            PastTenceNegationArray.add(SpannableString.valueOf(pastNegNoteTwo))
        }
        if (PastTenceNegationArray.isNotEmpty()) {
            presentTenceNegationArray.add(SpannableString.valueOf(presentNegNote))
        }



        expandableListDetail["Verse"] = verse
        expandableListDetail["Translation"] = translation
        expandableListDetail["Verb sentence-Past Tence Negation- Stences(لَمْ/مَا))"] =
            PastTenceNegationArray
        expandableListDetail["Verb sentence-Present Tence Negation- Stences(مَا/لَا)"] =
            presentTenceNegationArray
        expandableListDetail["Verb sentence-Future Tence Negation- Stences(لَّن)"] =
            futureTenceNegationArray
        expandableListDetail["Exceptive Sentences with (إلاّ-(أداة الاستْثناء)) & Restriction/Exclusive (الحَصْر) "] =
            InMaIllaNegationArray
        expandableListDetail["Conditional/جملة شرطية\""] = shartarray
        expandableListDetail["Accusative/ "] = harfnasbarray
        expandableListDetail["Verb kāna/كان واخواتها"] = kanaarray
        expandableListDetail["Adjectival Phrases/مرکب توصیفی"] = mausoofsifaarray
        expandableListDetail["Possessive/إضافَة"] = mudhafarray

        return expandableListDetail
    }

    private fun setAllPhrases(
        shartarray: MutableList<SpannableString>,
        harfnasbarray: MutableList<SpannableString>,
        kanaarray: MutableList<SpannableString>,
        pastTenceNegationArray: MutableList<SpannableString>,
        presentTenceNegationArray: MutableList<SpannableString>,
        futureTenceNegationArray: MutableList<SpannableString>,
        inMaIllaNegationArray: MutableList<SpannableString>,
        mudhafarray: MutableList<SpannableString>,
        mausoofsifaarray: MutableList<SpannableString>
    ) {
        val spannedStrings = SpannableStringUtils.applySpans(negaTionList, "light")

        for ((i, j: Int, type, spannableString) in spannedStrings) {

            if (type.contains("kana")) {
                kanaarray.add(spannableString)
            } else if (type.contains("nasab")) {

                harfnasbarray.add(spannableString)
            } else if (type.contains("shart")) {
                shartarray.add(spannableString)
            } else if (type.contains("present")) {
                presentTenceNegationArray.add(spannableString)
            } else if (type.contains("past")) {
                pastTenceNegationArray.add(spannableString)
            } else if (type.contains("future")) {
                futureTenceNegationArray.add(spannableString)
            } else if (type.contains("exp")) {
                inMaIllaNegationArray.add(spannableString)
            } else if (type.contains("sifa")) {
                mausoofsifaarray.add(spannableString)
            } else if (type.contains("mudhaf")) {
                mudhafarray.add(spannableString)
            }

        }


    }


    /*val kana: List<SpannableString>
        get() {
            val kanaarray: MutableList<SpannableString> = ArrayList()
            newsetKana(kanaarray)
            return kanaarray
        }
*/

}

