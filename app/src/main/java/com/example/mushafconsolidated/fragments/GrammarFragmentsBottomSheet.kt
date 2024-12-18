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


import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager

import com.example.mushafconsolidated.Entities.NegationEnt


import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.SpannableStringUtils

import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.model.SarfSagheerPOJO
import com.example.mushafconsolidated.quranrepo.QuranRepository
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.sortWith

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



    private lateinit var negaTionList: List<NegationEnt>
    private var whichwbw: String? = null


    val model: QuranViewModel by viewModels()
    private var dark: Boolean = false


    var chapterid = 0
    private var ayanumber = 0
    private var isverbconjugaton = false
    private var participles = false
    lateinit var expandableListView: ExpandableListView
    var expandableListTitle: List<String>? = null
    var expandableListDetail: LinkedHashMap<String, List<SpannableString>>? = null

    var vbdetail = HashMap<String, String>()

    private var showGrammarFragments = false
    private var thulathiMazeedConjugatonList: ArrayList<ArrayList<*>>? = null
    var sarf: SarfSagheerPOJO? = null
    private var dialog: AlertDialog? = null

    @Inject
    lateinit var quranRepository: QuranRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.expand_list, container, false)

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


        negaTionList = utils.geTNegatonFilerSurahAyah(chapterid, ayanumber)

        expandableListDetail = getData()
        //  kanaExpandableListDetail = kana
        expandableListTitle = ArrayList(expandableListDetail!!.keys)
        val groupComparator = Comparator<String> { group1, group2 ->
            val hasChildren1 = expandableListDetail!![group1]?.isNotEmpty() ?: false
            val hasChildren2 = expandableListDetail!![group2]?.isNotEmpty() ?: false

            when {
                hasChildren1 && !hasChildren2 -> -1 // group1 has children, group2 doesn't
                !hasChildren1 && hasChildren2 -> 1 // group1 doesn't have children, group2 does
                else -> 0 // both have children or both don't have children
            }
        }
        (expandableListTitle as ArrayList<String>).sortWith(groupComparator)
        thulathiMazeedConjugatonList = ArrayList()
        isverbconjugaton = false
        participles = false


        //  val   corpusNounWord=  model.getNouncorpus(chapterid,ayanumber,1)
        ex.execute {
            requireActivity().runOnUiThread { dialog!!.show() }

            requireActivity().runOnUiThread {
                ex.shutdown()
                dialog!!.dismiss()
                val grammarFragmentsListAdapter =
                    GrammarFragmentsListAdapter(
                        requireContext(), expandableListTitle as ArrayList<String>,
                        expandableListDetail!!
                    )
                expandableListView.setAdapter(grammarFragmentsListAdapter)
                for (i in 0 until grammarFragmentsListAdapter.groupCount) {
                    expandableListView.collapseGroup(i)
                }
            }
        }
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

        whichwbw = prefs.getString("wbw", "en")
        val expandableListDetail = java.util.LinkedHashMap<String, List<SpannableString>>()
        val verse: MutableList<SpannableString> = ArrayList()
        val translation: MutableList<SpannableString> = ArrayList()

        translation.add(SpannableString.valueOf(negaTionList[0].translation))
        verse.add(SpannableString.valueOf(negaTionList[0].verse))

        val mausoofsifaarray: MutableList<SpannableString> = ArrayList()


        val mudhafarray: MutableList<SpannableString> = ArrayList()


        val shartarray: MutableList<SpannableString> = ArrayList()
        val harfnasbarray: MutableList<SpannableString> = ArrayList()
        val kanaarray: MutableList<SpannableString> = ArrayList()
        val pastTenceNegationArray: MutableList<SpannableString> = ArrayList()
        val presentTenceNegationArray: MutableList<SpannableString> = ArrayList()
        val futureTenceNegationArray: MutableList<SpannableString> = ArrayList()

        val inMaIllaNegationArray: MutableList<SpannableString> = ArrayList()

        val  anMasdarArray: MutableList<SpannableString> = ArrayList()
        val  silaMousalArray: MutableList<SpannableString> = ArrayList()
        val  halArray: MutableList<SpannableString> = ArrayList()


        setAllPhrases(
            shartarray,
            harfnasbarray,
            kanaarray,
            pastTenceNegationArray,
            presentTenceNegationArray,
            futureTenceNegationArray,
            inMaIllaNegationArray,
            mudhafarray,
            mausoofsifaarray,
            anMasdarArray,
            silaMousalArray,
            halArray
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
        if (pastTenceNegationArray.isNotEmpty()) {
            pastTenceNegationArray.add(SpannableString.valueOf(pastNegNoteOne))
            pastTenceNegationArray.add(SpannableString.valueOf(pastNegNoteTwo))
        }
        if (pastTenceNegationArray.isNotEmpty()) {
            presentTenceNegationArray.add(SpannableString.valueOf(presentNegNote))
        }



        expandableListDetail["Verse"] = verse
        expandableListDetail["Translation"] = translation
        expandableListDetail["Verb sentence-Past Tence Negation- Stences(لَمْ/مَا))"] =
            pastTenceNegationArray
        expandableListDetail["Verb sentence-Present Tence Negation- Stences(مَا/لَا)"] =
            presentTenceNegationArray
        expandableListDetail["Verb sentence-Future Tence Negation- Stences(لَّن)"] =
            futureTenceNegationArray
        expandableListDetail["Exceptive Sentences with (إلاّ-(أداة الاستْثناء)) & Restriction/Exclusive (الحَصْر) "] =
            inMaIllaNegationArray
        expandableListDetail["Conditional/جملة شرطية\""] = shartarray
        expandableListDetail["Accusative/ "] = harfnasbarray
        expandableListDetail["Verb kāna/كان واخواتها"] = kanaarray
        expandableListDetail["Adjectival Phrases/مرکب توصیفی"] = mausoofsifaarray
        expandableListDetail["Possessive/إضافَة"] = mudhafarray
        expandableListDetail["The Subordinate Clause (صلة)"] = silaMousalArray
        expandableListDetail["The Subordinate Clause (حرف مصدري)"] = anMasdarArray
        expandableListDetail["The Circumstantil Clause (حرف مصدري)"] = halArray
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
        mausoofsifaarray: MutableList<SpannableString>,
        anMasdarArray: MutableList<SpannableString>,
        silaMousalArray: MutableList<SpannableString>,
        halArray: MutableList<SpannableString>
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
            }else if (type.contains("sila")) {
                anMasdarArray.add(spannableString)
            } else if (type.contains("anmasdar")) {
                silaMousalArray.add(spannableString)
            }else if (type.contains("hal")) {
                halArray.add(spannableString)
            }

        }


    }




}

