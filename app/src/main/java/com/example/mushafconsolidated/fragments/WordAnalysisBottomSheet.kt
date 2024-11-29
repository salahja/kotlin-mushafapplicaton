package com.example.mushafconsolidated.fragments


import NewRootWordDisplayAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant
import com.example.Constant.ACCUSATIVE
import com.example.Constant.AYAHNUMBER
import com.example.Constant.CONDITIONAL
import com.example.Constant.DEMONSTRATIVE
import com.example.Constant.INDICATIVE
import com.example.Constant.ISPARTICPLE
import com.example.Constant.NOUNCASE
import com.example.Constant.PREPOSITION
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.RELATIVE
import com.example.Constant.SHADDA
import com.example.Constant.SURAH_ID
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.Activity.LughatWordDetailsAct

import com.example.mushafconsolidated.Activity.WordOccuranceAct
import com.example.mushafconsolidated.Entities.CorpusEntity

import com.example.mushafconsolidated.Entities.NewKanaEntity
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity

import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.VerbWazan
import com.example.mushafconsolidated.Entities.lughat
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.R.layout
import com.example.mushafconsolidated.databinding.RootDialogFragmentBinding
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListener
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.mushafconsolidatedimport.VerbFormsDialogFrag
import com.example.utility.CorpusUtilityorig.Companion.getSpancolor
import com.example.utility.QuranGrammarApplication
import dagger.hilt.android.AndroidEntryPoint
import database.entity.MujarradVerbs
import database.verbrepo.VerbModel
import kotlinx.coroutines.launch

import org.sj.conjugator.activity.ConjugatorTabsActivity
import org.sj.conjugator.fragments.SarfSagheer
import org.sj.conjugator.utilities.ArabicLiterals
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.IsmFaelMafoolResult

import java.util.Objects


/**
 * Word analysis bottom sheet
 *
 * @constructor Create empty Word analysis bottom sheet
 */
@AndroidEntryPoint
class WordAnalysisBottomSheet : DialogFragment() {
    private lateinit var recyclerView: RecyclerView
    private val sarfSagheerList = ArrayList<SarfSagheer>()

    var corpusSurahWord: List<CorpusEntity> = ArrayList()
    private var wazannumberslist = ArrayList<String>()
    private var rwAdapter: NewRootWordDisplayAdapter? = null
    var chapterId = 0
    var ayahNumber = 0
    private var _binding: RootDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private var isMazeedSarfSagheer = false
    private var isThulathiSarfSagheer = false
    private var isVerb = false
    var isNoun = false
    private lateinit var verbDictList: MutableList<MujarradVerbs>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListDetail: HashMap<String, List<SpannableString>>

    var vbdetail = HashMap<String, String?>()
    var wordbdetail = HashMap<String, SpannableStringBuilder?>()
    private var showGrammarFragments = false
    var isroot = false
    var isarabicword = false
    var quadrilateral = false
    private var isnoun = false
    private var isProperNoun = false
    private var ismujarradparticple = false
    var isImperative = false
    private var spannable: SpannableStringBuilder? = null
    private var themepreference: String? = null
    private var ismujarrad = false
    private var ismazeed = false
    private var isparticple = false
    private var isconjugation = false
    private var root: String? = null
    private lateinit var mujarradwazan: String
    private lateinit var mazeedwazan: String
    private var verbmood: String = ""
    private var isHarf = false
    private var isLysa=false
    private var isrelative = false
    private var isdem = false
    private var isprep = false
    private var isharfnasb = false
    private var isShart = false
    lateinit var harfNasbAndZarf: String

    // --Commented out by Inspection (11/01/22, 8:24 AM):private View selectedview;
    private lateinit var dialog: AlertDialog
    private var ismfaelmafool:            IsmFaelMafoolResult? = null
    private lateinit var vb: VerbWazan
    private lateinit var arabicword: VerbWazan
    private val dark = false

    // --Commented out by Inspection (11/01/22, 8:26 AM):private SentenceRootWordDisplayAdapter sentenceRootWordDisplayAdapter;


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = RootDialogFragmentBinding.inflate(inflater, container, false)
        val shared =
            PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context!!)

        themepreference = shared.getString("theme", "dark")
        recyclerView = binding.wordByWordRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        val bundle = this.requireArguments()
        val stringArray = bundle.getStringArray(ARG_OPTIONS_DATA)
        chapterId = stringArray!![0].toInt()

        showGrammarFragments = shared.getBoolean("fragments", false)
        ayahNumber = stringArray[1].toInt()

        val wbwtranslation = stringArray[2]
        val wordNo = stringArray[3].toInt()
        if (stringArray.size > 4) { //ignore if the call is from wordoccurance
            storepreferences(chapterId, ayahNumber, stringArray[4])
        }
        val dark =
            themepreference == "dark" || themepreference == "blue" || themepreference == "green"

//show
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(layout.layout_loading_dialog)
        dialog = builder.create()

        dialog.show()
        val mainViewModel: QuranViewModel by viewModels()
        //  val mainViewModel = ViewModelProvider(requireActivity())[QuranVIewModel::class.java]


        lifecycleScope.launch {
            val quran = mainViewModel.getsurahayahVerseslist(chapterId, ayahNumber).value

            val corpusNounWord = mainViewModel.getNouncorpus(chapterId, ayahNumber, wordNo).value
            val verbCorpusRootWord =
                mainViewModel.getVerbRootBySurahAyahWord(chapterId, ayahNumber, wordNo).value
            val corpusSurahWord =
                mainViewModel.getCorpusEntityFilterbywordno(chapterId, ayahNumber, wordNo).value
                    ?: return@launch

            // Continue with data processing...
            val am = NewQuranMorphologyDetails(
                corpusSurahWord,
                corpusNounWord as? ArrayList<NounCorpus>,
                verbCorpusRootWord as? ArrayList<VerbCorpus>,
                requireContext()
            )

            handleWordDetailsorig(
                am,
                verbCorpusRootWord,
                mainViewModel,
                wordNo,
                corpusNounWord,
                wbwtranslation,
                mainViewModel,
                quran
            )

            // Handle UI updates after data is ready
            requireActivity().runOnUiThread {
                dialog.dismiss()
                if (showGrammarFragments) {
                    GrammarFragmentsListAdapter(
                        requireContext(),
                        expandableListTitle, expandableListDetail
                    )
                } else {
                    //          rwAdapter = RootWordDisplayAdapter(context!!)

                    rwAdapter = NewRootWordDisplayAdapter(
                        requireContext(),

                        isVerb,
                        wazannumberslist,
                        spannable,
                        isNoun,
                        ismfaelmafool,
                        isparticple,
                        isconjugation,
                        corpusSurahWord as ArrayList<CorpusEntity>,
                        wordbdetail,
                        vbdetail,
                        isMazeedSarfSagheer,
                        isThulathiSarfSagheer,
                        sarfSagheerList
                    )
                    recyclerView.adapter = rwAdapter
                }
            }


        }

        return binding.root
    }

    private fun handleWordDetails(
        am: NewQuranMorphologyDetails,
        verbCorpusRootWord: List<VerbCorpus>?,
        mainViewModel: QuranViewModel,
        wordNo: Int,
        corpusNounWord: List<NounCorpus>?,
        wbwTranslation: String?,
        models: QuranViewModel,
        quran: List<QuranEntity>?
    ) {
        val shared = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val mafoolatEnabled = shared.getBoolean("mafoolat", false)

        vb = VerbWazan()
        wordbdetail = am.wordDetails


       /* isVerb = verbCorpusRootWord?.firstOrNull()?.tag == "V"
        isImperative = vbdetail["tense"]?.contains("Imperative") == true

        vb = VerbWazan()
        wordbdetail = am.wordDetails*/
        if (verbCorpusRootWord != null) {
            if (verbCorpusRootWord.isNotEmpty() && verbCorpusRootWord[0].tag.equals("V")) {
                vbdetail = am.verbDetails
                isVerb = true
            }
        }
        //if any true..good for verb conjugation
        if (!(vbdetail.isEmpty() || !Objects.requireNonNull(vbdetail["tense"])
                ?.contains("Imperative")!!)
        ) {
            isImperative = true
        }





        if (mafoolatEnabled) {
            loadMafoolat(mainViewModel, wordNo)
        }

        // Check for specific grammatical forms
        isarabicword = wordbdetail["arabicword"] != null
        ismujarrad = vbdetail["wazan"] != null
        ismazeed = vbdetail["form"] != null
        isparticple = wordbdetail["particple"] != null
        isconjugation = listOf(ismujarrad, ismazeed, isparticple).any { it }
        isroot = wordbdetail["root"] != null

        isrelative = wordbdetail["relative"] != null
        isharfnasb = wordbdetail["harfnasb"] != null
        isprep = wordbdetail["prep"] != null
        isdem = wordbdetail["dem"] != null
        isShart = wordbdetail["cond"] != null
        isHarf = listOf(isShart, isrelative, isharfnasb, isprep, isdem).any { it }
        isnoun = wordbdetail["noun"] != null
        //   isProperNoun= wordbdetail["noun"]!!.contains("Proper Noun:")

        wordbdetail["noun"]?.let { noun ->
            isProperNoun = noun.contains("Proper Noun:")
        }

        processRootDetails()
        processNounDetails(corpusNounWord)
        updateTranslation(wbwTranslation, wordNo, models)

        quran?.let { updateSpannableText(it) }
        requireActivity().runOnUiThread {
            // ex.shutdown()
            dialog.dismiss()

            if (showGrammarFragments) {
                GrammarFragmentsListAdapter(
                    requireContext(),
                    expandableListTitle, expandableListDetail
                )
            } else {
                //          rwAdapter = RootWordDisplayAdapter(context!!)

                rwAdapter = NewRootWordDisplayAdapter(
                    requireContext(),

                    isVerb,
                    wazannumberslist,
                    spannable,
                    isNoun,
                    ismfaelmafool,
                    isparticple,
                    isconjugation,
                    corpusSurahWord as ArrayList<CorpusEntity>,
                    wordbdetail,
                    vbdetail,
                    isMazeedSarfSagheer,
                    isThulathiSarfSagheer,
                    sarfSagheerList
                )
                recyclerView.adapter = rwAdapter
            }
        }
    }
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the result data from Activity B
                val message = result.data?.getStringExtra("result_message")
                message?.let {
                    // Show the Toast message in Activity A
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val wordFound = data?.getBooleanExtra("WORD_FOUND", true) ?: true
            if (!wordFound) {
                // Show Toast in Activity A
                Toast.makeText(requireContext(), "Word not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processRootDetails() {
        root = vbdetail["root"]?.toString() ?: wordbdetail["root"]?.toString()


        if (ismujarrad && !isparticple) {
            mujarradwazan = vbdetail["wazan"]?.toString() ?: "N"
            verbmood = vbdetail["emph"]?.let { "Emphasized" } ?: vbdetail["verbmood"].toString()
        } else if (ismazeed && !isparticple) {
            mazeedwazan = vbdetail["form"]?.toString() ?: wordbdetail["form"].toString()
            verbmood = vbdetail["emph"]?.let { "Emphasized" } ?: vbdetail["verbmood"].toString()
        }

        // Handle root-related conditions
        if (isroot) {
          //  vb = VerbWazan().apply { this.root = root }
            vb = VerbWazan()
            vb.root = root

            when {
                ismujarrad && !isparticple -> {
                    vb.wazan = mujarradwazan
                    handleMujarradVerb()
                }

                ismazeed && !isparticple -> {
                    vb.wazan = vbdetail["form"]
                    handleMazeedVerb()
                }
                isparticple -> handleParticiple()
                isnoun -> isNoun = true
                else -> handleArabicWord()
            }


         /*       when {
                ismujarrad && !isparticple -> handleMujarradVerb()
                ismazeed && !isparticple -> handleMazeedVerb()
                isparticple -> handleParticiple()
                isnoun -> isNoun = true
                else -> handleArabicWord()
            }*/
        }
    }

    private fun processNounDetails(corpusNounWord: List<NounCorpus>?) {
        corpusNounWord?.firstOrNull()?.let { noun ->
            when (noun.tag) {
                "COND", "T", "LOC" -> {
                    harfNasbAndZarf = noun.tag
                    isNoun = true
                    isparticple = false
                }

                "ACC" -> {
                    harfNasbAndZarf = "ACC"
                    isNoun = true
                    isparticple = false
                }

                else -> handleNounRoot()
            }
        }
    }

    private fun handleNounRoot() {
        val rootWord = wordbdetail["root"]
        if (rootWord?.length == 4) {
            quadrilateral = true
            vb.root = vbdetail["root"].toString()
            isMazeedSarfSagheer = false
            isThulathiSarfSagheer = false
            isconjugation = false
            isroot = true
        } else if (rootWord != null && wordbdetail["noun"] != null) {
            vb.root = rootWord.toString()
            isroot = true
            isNoun = true
        }
    }

    private fun updateTranslation(wbwTranslation: String?, wordNo: Int, models: QuranViewModel) {
        wordbdetail["translation"] = SpannableStringBuilder(
            wbwTranslation ?: models.getwbwTranslationbywordno(chapterId, ayahNumber, wordNo)
                .value?.firstOrNull()?.en.orEmpty()
        )
    }

    private fun updateSpannableText(quran: List<QuranEntity>) {
        spannable = SpannableStringBuilder(quran.first().qurantext)
    }



    private fun handleWordDetailsorig(
        am: NewQuranMorphologyDetails,
        verbCorpusRootWord: List<VerbCorpus>?,
        mainViewModel: QuranViewModel,
        wordno: Int,
        corpusNounWord: List<NounCorpus>?,
        wbwtranslation: String?,
        models: QuranViewModel,
        quran: List<QuranEntity>?
    ) {
        val shared = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val mafoolat = shared.getBoolean("mafoolat", false)
        vb = VerbWazan()
        wordbdetail = am.wordDetails
        if (verbCorpusRootWord != null) {
            if (verbCorpusRootWord.isNotEmpty() && verbCorpusRootWord[0].tag.equals("V")) {
                vbdetail = am.verbDetails
                isVerb = true
            }
        }
        if(root.equals("ليس")){
           isLysa=true
        }else {
            //if any true..good for verb conjugation
            if (!(vbdetail.isEmpty() || !Objects.requireNonNull(vbdetail["tense"])
                    ?.contains("Imperative")!!)
            ) {
                isImperative = true
            }
            if (mafoolat) {
                loadMafoolat(mainViewModel, wordno)
            }

            isarabicword = wordbdetail["arabicword"] != null
            ismujarrad = vbdetail["wazan"] != null
            ismazeed = vbdetail["form"] != null
            isparticple = wordbdetail["particple"] != null
            isconjugation = ismujarrad || ismazeed || isparticple
            isroot = wordbdetail["root"] != null

            isrelative = wordbdetail["relative"] != null
            isharfnasb = wordbdetail["harfnasb"] != null
            isprep = wordbdetail["prep"] != null
            isdem = wordbdetail["dem"] != null
            isShart = wordbdetail["cond"] != null
            //   isHarf =  isrelative == isharfnasb == isprep == isdem
            isHarf = listOf(isShart, isrelative, isharfnasb, isprep, isdem).any { it }

            if (isroot) {
                root = if (vbdetail.isEmpty()) {

                    wordbdetail["root"].toString()
                } else {
                    vbdetail["root"]
                }

            }
            isnoun = wordbdetail["noun"] != null
            //   isProperNoun= wordbdetail["noun"]!!.contains("Proper Noun:")

            wordbdetail["noun"]?.let { noun ->
                isProperNoun = noun.contains("Proper Noun:")
            }

            if (isparticple) {
                // Avoid unnecessary Objects.requireNonNull
                ismujarradparticple = wordbdetail["form"]?.toString() == "I"
                ismujarrad = ismujarradparticple
                ismazeed = !ismujarradparticple
            }

            if (ismujarrad && !isparticple) {
                mujarradwazan = vbdetail["wazan"].toString()
                verbmood = when {
                    vbdetail["emph"] != null -> "Emphasized"
                    else -> vbdetail["verbmood"].toString()
                }
            } else if (ismazeed && !isparticple) {
                mazeedwazan = vbdetail["form"].toString()
                verbmood = when {
                    vbdetail["emph"] != null -> "Emphasized"
                    else -> vbdetail["verbmood"].toString()
                }
            } else if (ismujarrad) {
                mujarradwazan = "N"
            } else if (ismazeed) {
                mazeedwazan = wordbdetail["form"].toString()
            }


            if (isShart || isdem || isharfnasb || isprep || isrelative) {
                isroot = false
                isThulathiSarfSagheer = false
                isMazeedSarfSagheer = false
                isconjugation = true
            } else if (isroot) {
                vb = VerbWazan()
                vb.root = root

                when {
                    ismujarrad && !isparticple -> {
                        vb.wazan = mujarradwazan
                        handleMujarradVerb()
                    }

                    ismazeed && !isparticple -> {
                        vb.wazan = vbdetail["form"]
                        handleMazeedVerb()
                    }

                    isparticple -> {
                        handleParticiple()
                    }

                    isnoun -> {
                        isNoun = true
                    }

                    else -> {
                        handleArabicWord()
                    }
                }
            }

            if (!corpusNounWord.isNullOrEmpty()) {
                when (corpusNounWord[0].tag) {
                    "COND", "T", "LOC" -> {
                        harfNasbAndZarf = corpusNounWord[0].tag  // Directly assign the tag
                        isNoun = true
                        isparticple = false
                    }

                    "ACC" -> {
                        harfNasbAndZarf = "ACC"
                        isNoun = true
                        isparticple = false
                    }

                    else -> {
                        if (wordbdetail["root"] != null && wordbdetail["root"]?.length == 4) {
                            isMazeedSarfSagheer = false
                            isThulathiSarfSagheer = false
                            isconjugation = false
                            isroot = true
                            quadrilateral = true
                            vb.root = vbdetail["root"]
                        } else if (wordbdetail["root"] != null && wordbdetail["noun"] != null) {
                            vb.root = wordbdetail["root"].toString()
                            isroot = true
                            isNoun = true
                        }
                    }
                }
            }


            if (wbwtranslation == null) {
                val allwords =
                    models.getwbwTranslationbywordno(
                        chapterId,
                        ayahNumber,
                        wordno
                    ).value as ArrayList<wbwentity>
                wordbdetail["translation"] = SpannableStringBuilder.valueOf(allwords[0].en)
            } else {
                wordbdetail["translation"] = SpannableStringBuilder.valueOf(wbwtranslation)
            }
            if (corpusSurahWord.isNotEmpty()) {
                val quranverses: String = quran!![0].qurantext
                spannable = SpannableStringBuilder(quranverses)
                setShart(models)
                setHarfNasb(models)
                setMausoofSifa(models, quran as ArrayList<QuranEntity>)
                setMudhaf(models)
                setKana(models)
            } else {
                val quranverses: String = quran!![0].qurantext
                spannable = SpannableStringBuilder(quranverses)
            }

            requireActivity().runOnUiThread {
                // ex.shutdown()
                dialog.dismiss()
            }
            if (showGrammarFragments) {
                GrammarFragmentsListAdapter(
                    requireContext(),
                    expandableListTitle, expandableListDetail
                )
            } else {
                //          rwAdapter = RootWordDisplayAdapter(context!!)

                rwAdapter = NewRootWordDisplayAdapter(
                    requireContext(),

                    isVerb,
                    wazannumberslist,
                    spannable,
                    isNoun,
                    ismfaelmafool,
                    isparticple,
                    isconjugation,
                    corpusSurahWord as ArrayList<CorpusEntity>,
                    wordbdetail,
                    vbdetail,
                    isMazeedSarfSagheer,
                    isThulathiSarfSagheer,
                    sarfSagheerList
                )
                recyclerView.adapter = rwAdapter
            }
        }
        // }//scope
    }

    private fun loadMafoolat(
        mainViewModel: QuranViewModel,
        wordno: Int
    ) {


    }


    // Helper functions to handle different cases
    private fun handleMujarradVerb() {
       // val listing = GatherAll.instance.getMujarradListing(verbmood, root!!, vb.wazan!!)
        val listing = GatherAll.instance.getMujarradListing(verbmood, root ?: "", vb.wazan ?: "")
        val ss = SarfSagheer()
     //   val alazarf = listing[4][0]

        // ... (Set properties of ss using listing)
        ss.weakness = listing?.verbDetailsList?.get(0)?.verbtype
        ss.wazanname = listing?.verbDetailsList?.get(0)?.babname
        ss.verbroot = listing?.verbDetailsList?.get(0)?.verbroot
        ss.madhi = listing?.madhiMudharayList?.get(0)?.hua
        ss.madhimajhool = listing?.madhiMudharayList?.get(1)?.hua
        ss.mudharay = listing?.madhiMudharayList?.get(2)?.hua
        ss.mudharaymajhool = listing?.madhiMudharayList?.get(3)?.hua
        ss.amrone= listing?.amrList?.get(0)?.anta
        ss.nahiamrone= listing?.nahiAmr?.get(0)?.anta
        ss.ismfael= listing?.ismFaelMafoolList?.get(0)?.nomsinM
        ss.ismmafool= listing?.ismFaelMafoolList?.get(1)?.nomsinM
        ss.ismalaone = listing?.ismALAMifalatun?.get(0)?.nomsinMifalatun
        ss.ismalatwo = listing?.ismAlaMifal?.get(0)?.nomdualMifalun
        ss.ismalathree = listing?.ismAlaMifal?.get(0)?.nomsinMifalun
        ss.zarfone = listing?.ismZarfMafalatun?.get(0)?.nomsinMafalatun
        ss.zarftwo = listing?.ismZarfMafalun?.get(0)?.nomsinMafalun
        ss.zarfthree = listing?.ismZarfMafilun?.get(0)?.nomsinMafilun
        ss.verbtype = listing?.verbDetailsList?.get(0)?.mazeedormujarad
        ss.wazan = listing?.verbDetailsList?.get(0)?.wazannumberorname
        sarfSagheerList.add(ss)

        sarfSagheerList.add(ss)
        isThulathiSarfSagheer = true
        isconjugation = true

        // Normalize root by replacing "أ" with "ء"
        root = root?.replace("أ", "ء")

        loadMujarradVerbsFromModel()
    }

    private fun handleMazeedVerb() {
        val model: VerbModel by viewModels()
        val mazeedEntityRoots = model.getMazeelist(root!!).value

        if (mazeedEntityRoots != null) {
            if (mazeedEntityRoots.isNotEmpty()) {
                isMazeedSarfSagheer = true
                isconjugation = true
                val listing = GatherAll.instance.getMazeedListing(verbmood, root!!, mazeedwazan)
                val ss = SarfSagheer()

                ss.weakness = listing?.verbDetailsList?.get(0)?.verbtype
                ss.wazanname = listing?.verbDetailsList?.get(0)?.babname
                ss.verbroot = listing?.verbDetailsList?.get(0)?.verbroot
                ss.madhi = listing?.madhiMudharayList?.get(0)?.hua
                ss.madhimajhool = listing?.madhiMudharayList?.get(1)?.hua
                ss.mudharay = listing?.madhiMudharayList?.get(2)?.hua
                ss.mudharaymajhool = listing?.madhiMudharayList?.get(3)?.hua
                ss.amrone= listing?.amrList?.get(0)?.anta
                ss.nahiamrone= listing?.nahiAmrList?.get(0)?.anta
                ss.ismfael= listing?.skabeerIsmList?.get(0)?.nomsinM
                ss.ismmafool= listing?.skabeerIsmList?.get(1)?.nomsinM

                ss.verbtype = listing?.verbDetailsList?.get(0)?.mazeedormujarad
                ss.wazan = listing?.verbDetailsList?.get(0)?.wazannumberorname




                sarfSagheerList.add(ss)
            } else {
                isMazeedSarfSagheer = false
                isconjugation = false
            }
        }
    }

    private fun handleParticiple() {
        isparticple = true
        isconjugation = false
        if (!ismujarradparticple) {
            val form1 = wordbdetail["form"].toString()
            vb.wazan = form1
            ismfaelmafool = GatherAll.instance.buildMazeedParticiples(root!!, form1)
            isNoun = false
        } else {
            // Normalize root by replacing "أ" with "ء"
            root = root?.replace("أ", "ء")
            loadMujarradVerbsFromModel()
            val model: VerbModel by viewModels()
            val triVerb = model.getMujjarlist(root!!).value // Assuming 'model' is available here
            if (triVerb?.isNotEmpty() == true) {
                ismfaelmafool = GatherAll.instance.getMujarradParticiple(root!!, triVerb[0].bab)
                vb.wazan = triVerb[0].bab
            } else {
                isparticple = false
            }
        }
    }

    private fun handleArabicWord() {
        val concat = corpusSurahWord[0].araone + "|" + corpusSurahWord[0].aratwo
        arabicword = VerbWazan()
        val models: QuranViewModel by viewModels()
        val arabicWord =
            models.getArabicWord(wordbdetail["arabicword"].toString()).value as? ArrayList<lughat>
        val rootDictionary = models.getRootWordDictionary(concat).value as ArrayList<lughat>

        isarabicword = arabicWord?.isNotEmpty() ?: false || rootDictionary.isNotEmpty()
        isroot = false
        if (arabicWord?.isNotEmpty() == true) {
            arabicword.arabicword = arabicWord[0].arabicword
        }
    }

    private fun loadMujarradVerbsFromModel() {
        val model: VerbModel by viewModels()
        val triVerb = model.getMujjarlist(root!!).value
        verbDictList = ArrayList()
        wazannumberslist = ArrayList()
        triVerb?.forEach { tri ->
            verbDictList.add(
                MujarradVerbs(
                    tri.verb,
                    tri.root,
                    tri.babname,
                    tri.verbtype,
                    ""
                )
            )
            wazannumberslist.add("${tri.babname}-${tri.verbtype}-")
        }
    }


    /**
     * Storepreferences.
     *
     * @param chapterid Chapterid
     * @param ayanumber Ayanumber
     * @param s S
     */
    private fun storepreferences(chapterid: Int, ayanumber: Int, s: String) {
        val pref: SharedPreferences? =
            requireContext().getSharedPreferences("lastread", Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor!!.putInt(SURAH_ID, chapterid)
        editor.putInt(Constant.AYAH_ID, ayanumber)
        editor.putString(Constant.SURAH_ARABIC_NAME, s)
        editor.apply()
        editor.apply()
    }

    private fun setHarfNasb(model: QuranViewModel) {
        val harfnasb =
            model.getnasab(chapterId, ayahNumber).value as ArrayList<NewNasbEntity>
        if (harfnasb != null) {
            for (nasb in harfnasb) {
                if (dark) {
                    Constant.harfinnaspanDark = ForegroundColorSpan(Color.GREEN)
                    Constant.harfismspanDark = ForegroundColorSpan(Constant.BCYAN)
                    Constant.harfkhabarspanDark = ForegroundColorSpan(Color.YELLOW)
                } else {
                    Constant.harfinnaspanDark = ForegroundColorSpan(Constant.WHOTPINK)
                    Constant.harfismspanDark = ForegroundColorSpan(Constant.prussianblue)
                    Constant.harfkhabarspanDark = ForegroundColorSpan(Constant.DeepPink)
                }
                val start: Int = nasb.indexstart
                val end: Int = nasb.indexend
                val ismstart: Int = nasb.ismstart
                val ismend: Int = nasb.ismend
                val khabarstart: Int = nasb.khabarstart
                val khabarend: Int = nasb.khabarend
                spannable!!.setSpan(
                    Constant.harfinnaspanDark,
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable!!.setSpan(
                    Constant.harfismspanDark,
                    ismstart,
                    ismend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable!!.setSpan(
                    Constant.harfkhabarspanDark,
                    khabarstart,
                    khabarend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun setMudhaf(model: QuranViewModel) {
        val mudhafSurahAyah =
            model.getmudhafFilterSurahAyah(chapterId, ayahNumber) as ArrayList<NewMudhafEntity>
        if (mudhafSurahAyah != null) {
            for (mudhafEntity in mudhafSurahAyah) {
                Constant.mudhafspansDark = getSpancolor(true)
                spannable!!.setSpan(
                    Constant.mudhafspansDark,
                    mudhafEntity.startindex,
                    mudhafEntity.endindex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun setMausoofSifa(
        model: QuranViewModel,
        corpusSurahWord: ArrayList<QuranEntity>,
    ) {
        val sifabySurahAyah =
            model.getsifaFileterSurahAyah(chapterId, ayahNumber) as ArrayList<SifaEntity>
        val quranverses: String = corpusSurahWord[0].qurantext
        for (shartEntity in sifabySurahAyah) {
            Constant.sifaspansDark = getSpancolor(false)
            try {
                spannable!!.setSpan(
                    Constant.sifaspansDark,
                    shartEntity.startindex,
                    shartEntity.endindex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: IndexOutOfBoundsException) {
                println(("  " + shartEntity.surah + shartEntity.ayah) + "  " + quranverses)
            }
        }
    }

    private fun setShart(model: QuranViewModel) {
        val shart = model.getshart(chapterId, ayahNumber).value as ArrayList<NewShartEntity>
        //  this.spannable = new SpannableStringBuilder(quranverses);
        if (shart != null) {
            for (shartEntity in shart) {
                if (dark) {
                    Constant.harfshartspanDark = ForegroundColorSpan(Constant.GOLD)
                    Constant.shartspanDark = ForegroundColorSpan(Constant.ORANGE400)
                    Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
                } else {
                    Constant.harfshartspanDark = ForegroundColorSpan(Constant.FORESTGREEN)
                    Constant.shartspanDark = ForegroundColorSpan(Constant.GREENDARK)
                    Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
                }
                spannable!!.setSpan(
                    Constant.harfshartspanDark,
                    shartEntity.indexstart,
                    shartEntity.indexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable!!.setSpan(
                    Constant.shartspanDark,
                    shartEntity.shartindexstart,
                    shartEntity.shartindexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable!!.setSpan(
                    Constant.jawabshartspanDark,
                    shartEntity.jawabshartindexstart,
                    shartEntity.jawabshartindexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun setKana(model: QuranViewModel) {
        val kana =
            model.getkana(chapterId, ayahNumber).value as ArrayList<NewKanaEntity>
        //  this.spannable = new SpannableStringBuilder(quranverses);
        val harfkana: ForegroundColorSpan
        val kanaism: ForegroundColorSpan
        val kanakhbar: ForegroundColorSpan
        if (dark) {
            harfkana = ForegroundColorSpan(Constant.GOLD)
            kanaism = ForegroundColorSpan(Constant.ORANGE400)
            kanakhbar = ForegroundColorSpan(Color.CYAN)
        } else {
            harfkana = ForegroundColorSpan(Constant.FORESTGREEN)
            kanaism = ForegroundColorSpan(Constant.KASHMIRIGREEN)
            kanakhbar = ForegroundColorSpan(Constant.WHOTPINK)
        }
        for (kanaEntity in kana) {
            spannable!!.setSpan(
                harfkana,
                kanaEntity.indexstart,
                kanaEntity.indexend,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            try {
                spannable!!.setSpan(
                    kanaism,
                    kanaEntity.ismkanastart,
                    kanaEntity.ismkanaend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (indexOutOfBoundsException: IndexOutOfBoundsException) {
                println(indexOutOfBoundsException.message)
            }
            spannable!!.setSpan(
                kanakhbar,
                kanaEntity.khabarstart,
                kanaEntity.khabarend,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rwAdapter?.SetOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val viewVerbConjugation = v?.findViewById<View>(R.id.verbconjugationbtn)
                val verbOccuranceId = v?.findViewById<View>(R.id.verboccurance)
                val nouns = v?.findViewById<View>(R.id.wordoccurance)
                val verse = v?.findViewById<View>(R.id.spannableverse)
                val wordView = v?.findViewById<View>(R.id.wordView)
                val formView = v?.findViewById<View>(R.id.mazeedmeaning)

                formView?.let {
                    handleFormViewClick()
                } ?: wordView?.let {
                    handleWordViewClick()
                } ?: verse?.let {
                    handleVerseClick()
                } ?: nouns?.let {
                    handleNounsClick()
                } ?: viewVerbConjugation?.let {
                    handleVerbConjugationClick()
                } ?: verbOccuranceId?.let {
                    handleVerbOccuranceClick()
                } ?: run {
                    Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun handleFormViewClick() {
        val vbform = vbdetail["formnumber"] ?: wordbdetail["formnumber"].toString()
        val cleanedForm = vbform.replace(Regex("[()]"), "")

        val data = arrayOf(cleanedForm)
        VerbFormsDialogFrag.newInstance(data).show(requireActivity().supportFragmentManager, TAG)
    }

    private fun handleWordViewClick() {
        val dataBundle = Bundle().apply {
            if (wordbdetail["arabicword"] != null) {
                putString("arabicword", wordbdetail["arabicword"].toString())
            }
            putString(VERBMOOD, "")
            putString(QURAN_VERB_WAZAN, "")
            putString(QURAN_VERB_ROOT, "")
            putString(VERBTYPE, "")
        }
        if(isLysa){
            dataBundle.putString(VERBMOOD, INDICATIVE)
            dataBundle.putString(QURAN_VERB_ROOT, "ليس")

        }
       else if (isHarf) {
            handleHarfCases(dataBundle)
        } else if (isroot) {
            handleRootCases(dataBundle)
        } else {
            cleanArabicWord(dataBundle)
        }



        launchActivityB(dataBundle)
    }


    private fun launchActivityB(dataBundle: Bundle) {
        val intent = Intent(requireContext(), LughatWordDetailsAct::class.java)
        intent.putExtras(dataBundle) // Corrected line
        startForResult.launch(intent)
    }
    private fun cleanArabicWord(dataBundle: Bundle) {
        var arabicWord = wordbdetail["arabicword"].toString()

        // Remove unwanted Arabic diacritics (Kasra, Shadda, Fatha, Damma)
        arabicWord = arabicWord.replace(ArabicLiterals.Kasra.toRegex(), "")
            .replace(SHADDA.toRegex(), "")
            .replace(ArabicLiterals.Fatha.toRegex(), "")
            .replace(ArabicLiterals.Damma.toRegex(), "")

        // Add the cleaned Arabic word to the data bundle
        dataBundle.putString("arabicword", arabicWord)

        // Add default values for other keys
        dataBundle.putString(VERBMOOD, "")
        dataBundle.putString(QURAN_VERB_WAZAN, "")
        dataBundle.putString(QURAN_VERB_ROOT, "")
        dataBundle.putString(VERBTYPE, "")
    }


    private fun handleRootCases(dataBundle: Bundle) {
        dataBundle.putString("arabicword", "")
        if (vbdetail["emph"] != null) {

            dataBundle.putString(VERBMOOD, "Emphasized")
        } else {
            dataBundle.putString(VERBMOOD, vbdetail["verbmood"])
        }
        dataBundle.putString(VERBTYPE, if (ismujarrad) "mujarrad" else "mazeed")
        if (vbdetail.isNotEmpty()) {
            dataBundle.putString(QURAN_VERB_WAZAN, vb.wazan)
            dataBundle.putString(QURAN_VERB_ROOT, vb.root)
        } else if (isparticple) {
            dataBundle.putString(VERBMOOD, INDICATIVE)
            dataBundle.putString(QURAN_VERB_WAZAN, vb.wazan)
            dataBundle.putString(QURAN_VERB_ROOT, vb.root)
            dataBundle.putBoolean(ISPARTICPLE, true)
            if (isNoun) {
                dataBundle.putString(NOUNCASE, wordbdetail["nouncase"].toString())
            }
        } else {
            if (isNoun) {
                dataBundle.putString(NOUNCASE, wordbdetail["nouncase"].toString())
            }
            dataBundle.putString(QURAN_VERB_ROOT, vb.root)
            dataBundle.putString(QURAN_VERB_WAZAN, " ")
        }
    }

    private fun handleHarfCases(dataBundle: Bundle) {
        if (isharfnasb) {
            dataBundle.putBoolean(ACCUSATIVE, true)
        } else if (isprep) {
            dataBundle.putBoolean(PREPOSITION, true)
        } else if (isrelative) {
            dataBundle.putBoolean(RELATIVE, true)
        } else if (isdem) {
            dataBundle.putBoolean(DEMONSTRATIVE, true)
        } else if (isShart) {
            dataBundle.putBoolean(CONDITIONAL, true)
        }
        dataBundle.putString("arabicword", wordbdetail["arabicword"].toString())
        dataBundle.putString(VERBMOOD, "")
        dataBundle.putString(QURAN_VERB_WAZAN, "")
        dataBundle.putString(QURAN_VERB_ROOT, "")
        dataBundle.putString(VERBTYPE, "")
        val intent = Intent(activity, LughatWordDetailsAct::class.java)
        intent.putExtras(dataBundle)
        startActivity(intent)
    }

    private fun handleVerseClick() {
        val item = GrammerFragmentsBottomSheet()
        val dataBundle = Bundle().apply {
            putInt(SURAH_ID, chapterId)
            putInt(AYAHNUMBER, ayahNumber)
        }
        val data = arrayOf(chapterId.toString(), ayahNumber.toString())

        item.arguments = dataBundle
        GrammerFragmentsBottomSheet.newInstance(data)
            .show(requireActivity().supportFragmentManager, TAG)
    }

    private fun handleNounsClick() {
        val bundle = Bundle().apply {
            putString(QURAN_VERB_ROOT, vb.root ?: harfNasbAndZarf)
        }

        val intent = Intent(activity, WordOccuranceAct::class.java).apply { putExtras(bundle) }
        startActivity(intent)
    }

    private fun handleVerbConjugationClick() {
        if (isroot && isconjugation || isparticple) {
            val dataBundle = Bundle().apply {
                putString(VERBTYPE, if (ismujarrad) "mujarrad" else "mazeed")
                putString(VERBMOOD, vbdetail["emph"]?.let { "Emphasized" } ?: "Indicative")
                putString(QURAN_VERB_WAZAN, vb.wazan)
                putString(QURAN_VERB_ROOT, vb.root)
            }

            val intent =
                Intent(activity, ConjugatorTabsActivity::class.java).apply { putExtras(dataBundle) }
            startActivity(intent)
        }
    }

    private fun handleVerbOccuranceClick() {
        val bundle = Bundle().apply {
            putString(QURAN_VERB_ROOT, vb.root)
        }

        val intent = Intent(activity, WordOccuranceAct::class.java).apply { putExtras(bundle) }
        startActivity(intent)
    }


    companion object {
        const val TAG = "bottom"
        const val REQUEST_CODE = 1001
        // TODO: Customize parameter argument names
        const val ARG_OPTIONS_DATA = "options_data"

        // TODO: Customize parameters
        fun newInstance(data: Array<String?>): WordAnalysisBottomSheet {
            val fragment = WordAnalysisBottomSheet()
            val args = Bundle()
            args.putStringArray(ARG_OPTIONS_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }
}