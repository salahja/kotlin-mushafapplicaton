package com.example.mushafconsolidated.fragments


import NewRootWordDisplayAdapter
import android.annotation.SuppressLint
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant
import com.example.Constant.ACCUSATIVE
import com.example.Constant.AYAHNUMBER
import com.example.Constant.CONDITIONAL
import com.example.Constant.DEMONSTRATIVE
import com.example.Constant.IMPERATIVE
import com.example.Constant.INDICATIVE
import com.example.Constant.ISPARTICPLE
import com.example.Constant.NOUNCASE
import com.example.Constant.PREPOSITION
import com.example.Constant.PROPERNOUN
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
import com.example.mushafconsolidated.Entities.HalEnt
import com.example.mushafconsolidated.Entities.LiajlihiEnt
import com.example.mushafconsolidated.Entities.MafoolBihi
import com.example.mushafconsolidated.Entities.MafoolMutlaqEnt
import com.example.mushafconsolidated.Entities.NewKanaEntity
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.TameezEnt
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.VerbWazan
import com.example.mushafconsolidated.Entities.lughat
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.R.layout
import com.example.mushafconsolidated.databinding.RootDialogFragmentBinding
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListener
import com.example.mushafconsolidated.quranrepo.QuranVIewModel
import com.example.mushafconsolidatedimport.VerbFormsDialogFrag
import com.example.utility.CorpusUtilityorig
import com.example.utility.CorpusUtilityorig.Companion.getSpancolor
import com.example.utility.QuranGrammarApplication
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import database.entity.MujarradVerbs
import database.verbrepo.VerbModel

import org.sj.conjugator.activity.ConjugatorTabsActivity
import org.sj.conjugator.fragments.SarfSagheer
import org.sj.conjugator.utilities.ArabicLiterals
import org.sj.conjugator.utilities.GatherAll
import org.sj.verbConjugation.AmrNahiAmr
import org.sj.verbConjugation.FaelMafool
import org.sj.verbConjugation.IsmAlaZarfSagheer
import org.sj.verbConjugation.MadhiMudharay
import org.sj.verbConjugation.VerbDetails
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
    var mafoolbihi : List<MafoolBihi> = ArrayList()
    private var haliaSentence : List<HalEnt> = ArrayList()
    private var tameezWord : List<TameezEnt> = ArrayList()
    private var liajlihiEntArrayList :  List<LiajlihiEnt> = ArrayList()
    private var mutlaqword : List<MafoolMutlaqEnt> = ArrayList()
     var corpusSurahWord: List<CorpusEntity> = ArrayList()
    private var wazannumberslist = ArrayList<String>()
    private var rwAdapter: NewRootWordDisplayAdapter? = null
    var chapterid = 0
    var ayanumber = 0
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
    private var isProperNoun=false
    private var ismujarradparticple = false
    var isimperative = false
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
    private var isrelative = false
    private var isdem = false
    private var isprep = false
    private var isharfnasb = false
    private var isShart = false
    lateinit var harfNasbAndZarf: String

    // --Commented out by Inspection (11/01/22, 8:24 AM):private View selectedview;
    private lateinit var dialog: AlertDialog
    private var ismfaelmafool: ArrayList<ArrayList<*>>? = null
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

     //   val view = inflater.inflate(layout.root_dialog_fragment, container, false)
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context!!)
        themepreference = prefs.getString("theme", "dark")
         recyclerView = binding.wordByWordRecyclerView
        recyclerView.layoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        val bundle = this.requireArguments()
        val stringArray = bundle.getStringArray(ARG_OPTIONS_DATA)
        chapterid = stringArray!![0].toInt()
        val shared =
            PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context!!)
        showGrammarFragments = shared.getBoolean("fragments", false)
        ayanumber = stringArray[1].toInt()
        val wbwtranslation = stringArray[2]
        val wordno = stringArray[3].toInt()
        if (stringArray.size > 4) { //ignore if the call is from wordoccurance
            storepreferences(chapterid, ayanumber, stringArray[4])
        }
        val dark =
            themepreference == "dark" || themepreference == "blue" || themepreference == "green"
    //    rwAdapter = NewRootWordDisplayAdapter(requireContext())

        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(layout.layout_loading_dialog)
        dialog = builder.create()
        val models: QuranVIewModel by viewModels()
        val mainViewModel = ViewModelProvider(requireActivity())[QuranVIewModel::class.java]
         dialog.show()
        val quran = mainViewModel.getsurahayahVerseslist(chapterid, ayanumber).value
        val corpusNounWord = mainViewModel.getNouncorpus(chapterid, ayanumber, wordno).value
        val verbCorpusRootWord =
            mainViewModel.getVerbRootBySurahAyahWord(chapterid, ayanumber, wordno).value
         corpusSurahWord = mainViewModel.getCorpusEntityFilterbywordno(chapterid, ayanumber, wordno).value!!
        val am = NewQuranMorphologyDetails(
            corpusSurahWord,
            corpusNounWord as ArrayList<NounCorpus>?,
            verbCorpusRootWord as ArrayList<VerbCorpus>?,
            context
        )
        vb = VerbWazan()
        wordbdetail = am.wordDetails
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
            isimperative = true
        }
        mafoolbihi = mainViewModel.getMafoolbihiword(chapterid, ayanumber, wordno).value ?: emptyList()
        haliaSentence = mainViewModel.gethalsurahayah(chapterid, ayanumber).value ?: emptyList()
        tameezWord = mainViewModel.getTameezword(chapterid, ayanumber, wordno).value ?: emptyList()
        liajlihiEntArrayList = mainViewModel.getAjlihiword(chapterid, ayanumber, wordno).value ?: emptyList()
        mutlaqword = mainViewModel.getMutlaqWOrd(chapterid, ayanumber, wordno).value ?: emptyList()


//Simplified using the Elvis operator ?:
        wordbdetail["tameez"] = if (tameezWord.isNotEmpty()) SpannableStringBuilder.valueOf("yes") else null
        wordbdetail["liajlihi"] = if (liajlihiEntArrayList.isNotEmpty()) SpannableStringBuilder.valueOf("yes") else null
        wordbdetail["mafoolbihi"] = if (mafoolbihi.isNotEmpty()) SpannableStringBuilder.valueOf("yes") else null
        wordbdetail["mutlaqword"] = if (mutlaqword.isNotEmpty()) SpannableStringBuilder.valueOf("yes") else null
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
                    chapterid,
                    ayanumber,
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
        }
        val corpus = CorpusUtilityorig(requireContext())
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
                haliaSentence as ArrayList<HalEnt>?,
                tameezWord as ArrayList<TameezEnt>?,
                mafoolbihi as ArrayList<MafoolBihi>?,
                mutlaqword as ArrayList<MafoolMutlaqEnt>?,
                liajlihiEntArrayList as ArrayList<LiajlihiEnt>?,
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

       // }//scope


        return binding.root
    }


    // Helper functions to handle different cases
    private fun handleMujarradVerb() {
        val listing = GatherAll.instance.getMujarradListing(verbmood, root!!, vb.wazan!!)
        val ss = SarfSagheer()
        val alazarf = listing[4][0]

        // ... (Set properties of ss using listing)
        ss.weakness = (listing[3][0] as VerbDetails).verbtype
        ss.wazanname = (listing[3][0] as VerbDetails).babname
        ss.verbroot = (listing[3][0] as VerbDetails).verbroot
        ss.madhi = (listing[0][0] as MadhiMudharay).hua
        ss.madhimajhool = (listing[0][1] as MadhiMudharay).hua
        ss.mudharay = (listing[0][2] as MadhiMudharay).hua
        ss.mudharaymajhool = (listing[0][3] as MadhiMudharay).hua
        ss.amrone = (listing[2][0] as AmrNahiAmr).anta
        ss.nahiamrone = (listing[2][1] as AmrNahiAmr).anta
        ss.ismfael = (listing[1][0] as FaelMafool).nomsinM
        ss.ismmafool = (listing[1][1] as FaelMafool).nomsinM
        val ismAlaZarfSagheer = listing[4][0] as? IsmAlaZarfSagheer
        ss.ismalaone = ismAlaZarfSagheer?.ismALAMifalatun
        ss.ismalatwo = ismAlaZarfSagheer?.ismAlaMifaal
      ss.ismalathree = ismAlaZarfSagheer?.ismAlaMifal
         ss.zarfone= ismAlaZarfSagheer?.zarfMafalatun
         ss.zarftwo= ismAlaZarfSagheer?.zarfMafalun
         ss.zarfthree = ismAlaZarfSagheer?.zarfMafilun


        ss.verbtype = (listing[3][0] as VerbDetails).mazeedormujarad
        ss.wazan = (listing[3][0] as VerbDetails).wazannumberorname
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
                ss.weakness = (listing[3][0] as VerbDetails).verbtype
                ss.wazanname = (listing[3][0] as VerbDetails).babname
                ss.verbroot = (listing[3][0] as VerbDetails).verbroot
                ss.madhi = (listing[0][0] as MadhiMudharay).hua
                ss.madhimajhool = (listing[0][1] as MadhiMudharay).hua
                ss.mudharay = (listing[0][2] as MadhiMudharay).hua
                ss.mudharaymajhool = (listing[0][3] as MadhiMudharay).hua
                ss.amrone = (listing[2][0] as AmrNahiAmr).anta
                ss.nahiamrone = (listing[2][1] as AmrNahiAmr).anta
                ss.ismfael = (listing[1][0] as FaelMafool).nomsinM
                ss.ismmafool = (listing[1][1] as FaelMafool).nomsinM
                ss.verbtype = (listing[3][0] as VerbDetails).mazeedormujarad
                ss.wazan = (listing[3][0] as VerbDetails).wazannumberorname
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
        val models: QuranVIewModel by viewModels()
        val arabicWord = models.getArabicWord(wordbdetail["arabicword"].toString()).value as? ArrayList<lughat>
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

    private fun setHarfNasb(model: QuranVIewModel) {
        val harfnasb =
            model.getnasab(chapterid, ayanumber).value as ArrayList<NewNasbEntity>
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

    private fun setMudhaf(model: QuranVIewModel) {
        val mudhafSurahAyah =
            model.getmudhaf(chapterid, ayanumber).value as ArrayList<NewMudhafEntity>
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
        model: QuranVIewModel,
        corpusSurahWord: ArrayList<QuranEntity>,
    ) {
        val sifabySurahAyah =
            model.getsifa(chapterid, ayanumber).value as ArrayList<SifaEntity>
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

    private fun setShart(model: QuranVIewModel) {
        val shart = model.getshart(chapterid, ayanumber).value as ArrayList<NewShartEntity>
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

    private fun setKana(model: QuranVIewModel) {
        val kana =
            model.getkana(chapterid, ayanumber).value as ArrayList<NewKanaEntity>
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
            if(wordbdetail["arabicword"] !=null) {
                putString("arabicword", wordbdetail["arabicword"].toString())
            }
            putString(VERBMOOD, "")
            putString(QURAN_VERB_WAZAN, "")
            putString(QURAN_VERB_ROOT, "")
            putString(VERBTYPE, "")
        }

        if (isHarf) {
            handleHarfCases(dataBundle)
        } else if (isroot) {
            handleRootCases(dataBundle)
        } else {
            cleanArabicWord(dataBundle)
        }

        startActivity(Intent(activity, LughatWordDetailsAct::class.java).apply { putExtras(dataBundle) })
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
        dataBundle. putString(VERBTYPE, if (ismujarrad) "mujarrad" else "mazeed")
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
            putInt(SURAH_ID, chapterid)
            putInt(AYAHNUMBER, ayanumber)
        }
        val data = arrayOf(chapterid.toString(), ayanumber.toString())

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

            val intent = Intent(activity, ConjugatorTabsActivity::class.java).apply { putExtras(dataBundle) }
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


         fun onViewCreateds(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            rwAdapter!!.SetOnItemClickListener(object : OnItemClickListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onItemClick(v: View?, position: Int) {

                    val text: CharSequence
                    val text2: CharSequence? = null
                    val viewVerbConjugation = v?.findViewById<View>(R.id.verbconjugationbtn)
                    val verboccuranceid = v?.findViewById<View>(R.id.verboccurance)
                    val nouns = v?.findViewById<View>(R.id.wordoccurance)
                    val verse = v?.findViewById<View>(R.id.spannableverse)
                    val wordview = v?.findViewById<View>(R.id.wordView)
                    val formview = v?.findViewById<View>(R.id.mazeedmeaning)


                    if (formview != null) {
                        // val item = VerbFormsDialogFrag()
                        //    item.setdata(root!!WordMeanings,wbwRootwords,grammarRootsCombined);
                        //   val fragmentManager = requireActivity().supportFragmentManager
                        var vbform: String?
                        if (vbdetail.isNotEmpty()) {
                            vbform = vbdetail["formnumber"]
                        } else {
                            vbform = wordbdetail["formnumber"].toString()
                            vbform = vbform.replace("\\(".toRegex(), "").replace("\\)".toRegex(), "")
                        }
                        if (null != vbform) {
                            val data = arrayOf<String>(vbform)
                            VerbFormsDialogFrag.newInstance(data).show(
                                Objects.requireNonNull(requireActivity()).supportFragmentManager,
                                TAG
                            )
                        }
                    } else if (wordview != null) {
                        val dataBundle = Bundle()
                        if (isHarf) {
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
                        } else if (quadrilateral) {
                            dataBundle.putString(QURAN_VERB_ROOT, vb.root)
                            dataBundle.putString(QURAN_VERB_WAZAN, " ")
                            dataBundle.putString("arabicword", "")
                        } else if (isarabicword && (!isroot && !isnoun)) {
                            dataBundle.putString("arabicword", wordbdetail["arabicword"].toString())
                            dataBundle.putString(QURAN_VERB_WAZAN, " ")
                            dataBundle.putString(QURAN_VERB_ROOT, " ")
                        } else if(isnoun && isProperNoun) {
                            dataBundle.putString(VERBMOOD, "")
                            dataBundle.putString(QURAN_VERB_WAZAN, "")
                            dataBundle.putString(QURAN_VERB_ROOT, "")
                            dataBundle.putString(VERBTYPE, "")
                            dataBundle.putString(NOUNCASE, wordbdetail["nouncase"].toString())
                            dataBundle.putBoolean(PROPERNOUN, true)
                            val intent = Intent(activity, LughatWordDetailsAct::class.java)
                            intent.putExtras(dataBundle)
                            startActivity(intent)
                        }




                        else if (isroot) {
                            dataBundle.putString("arabicword", "")
                            if (vbdetail["emph"] != null) {

                                dataBundle.putString(VERBMOOD, "Emphasized")
                            } else {
                                dataBundle.putString(VERBMOOD, vbdetail["verbmood"])
                            }

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
                        } else {
                            //   dataBundle.putString("arabicword", String.valueOf(wordbdetail.get("arabicword")));
                            var arabicword = wordbdetail["arabicword"].toString()
                            val inkasra = arabicword.indexOf(ArabicLiterals.Kasra)
                            val intshadda = arabicword.indexOf(SHADDA)
                            val intfatha = arabicword.indexOf(ArabicLiterals.Fatha)
                            val intdamma = arabicword.indexOf(ArabicLiterals.Damma)
                            if (inkasra != -1) {
                                arabicword = arabicword.replace(ArabicLiterals.Kasra.toRegex(), "")
                            }
                            if (intshadda != -1) {
                                arabicword = arabicword.replace(SHADDA.toRegex(), "")
                            }
                            if (intfatha != -1) {
                                arabicword = arabicword.replace(ArabicLiterals.Fatha.toRegex(), "")
                            }
                            if (intdamma != -1) {
                                arabicword = arabicword.replace(ArabicLiterals.Damma.toRegex(), "")
                            }
                            dataBundle.putString("arabicword", arabicword)
                            dataBundle.putString(VERBMOOD, "")
                            dataBundle.putString(QURAN_VERB_WAZAN, "")
                            dataBundle.putString(QURAN_VERB_ROOT, "")
                            dataBundle.putString(VERBTYPE, "")
                            //    Intent intent = new Intent(getActivity(), WordDictionaryAct.class);
                            val intent = Intent(activity, LughatWordDetailsAct::class.java)
                            intent.putExtras(dataBundle)
                            startActivity(intent)
                        }
                        if (isroot || isarabicword) {
                            try {
                                if (ismujarrad) {
                                    dataBundle.putString(VERBTYPE, "mujarrad")
                                } else if (ismazeed) {
                                    dataBundle.putString(VERBTYPE, "mazeed")
                                } else {
                                    dataBundle.putString(VERBTYPE, "")
                                }
                                if (isimperative) {
                                    dataBundle.putBoolean(IMPERATIVE, true)
                                }
                                //     Intent intent = new Intent(getActivity(), WordDictionaryAct.class);
                                val intent = Intent(activity, LughatWordDetailsAct::class.java)
                                intent.putExtras(dataBundle)
                                startActivity(intent)
                            } catch (e: NullPointerException) {
                                println("null pointer")
                            }
                        } else {
                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show()
                        }
                    } else if (verse != null) {
                        val item = GrammerFragmentsBottomSheet()
                        //    item.setdata(root!!WordMeanings,wbwRootwords,grammarRootsCombined);
                        //   val fragmentManager = requireActivity().supportFragmentManager
                        val dataBundle = Bundle()
                        dataBundle.putInt(SURAH_ID, chapterid)
                        dataBundle.putInt(AYAHNUMBER, ayanumber)
                        item.arguments = dataBundle
                        val data = arrayOf(chapterid.toString(), ayanumber.toString())
                        GrammerFragmentsBottomSheet.newInstance(data)
                            .show(Objects.requireNonNull(requireActivity()).supportFragmentManager, TAG)
                    } else if (nouns != null) {
                        val bundle = Bundle()

                        val intent = Intent(activity, WordOccuranceAct::class.java)
                        //          val intent = Intent(activity, ComposeAct::class.java)
                        try {
                            if (vb.root!! != null) {
                                bundle.putString(QURAN_VERB_ROOT, vb.root)
                            } else if (harfNasbAndZarf != null) {
                                bundle.putString(QURAN_VERB_ROOT, harfNasbAndZarf)
                            }
                        } catch (e1: NullPointerException) {
                            bundle.putString(QURAN_VERB_ROOT, harfNasbAndZarf)
                            e1.printStackTrace()
                        }
                        intent.putExtras(bundle)
                        //   intent.putExtra(QURAN_VERB_ROOT,vb.getRoot());
                        startActivity(intent)
                    } else if (viewVerbConjugation != null) {
                        text = (viewVerbConjugation as MaterialButton).text
                        if (text.toString() == "Click for Verb Conjugation") {
                            if (isroot && isconjugation || isparticple) {
                                val dataBundle = Bundle()
                                //      ArrayList arrayList = ThulathiMazeedConjugatonList.get(position);
                                //   arrayList.get(0).ge
                                if (ismujarrad) {
                                    dataBundle.putString(VERBTYPE, "mujarrad")
                                } else if (ismazeed) {
                                    dataBundle.putString(VERBTYPE, "mazeed")
                                }
                                if (vbdetail.isEmpty()) {
                                    dataBundle.putString(VERBMOOD, "Indicative")
                                } else {

                                    if (vbdetail["emph"] != null) {

                                        dataBundle.putString(VERBMOOD, "Emphasized")
                                    } else {
                                        dataBundle.putString(VERBMOOD, vbdetail["verbmood"])
                                    }

                                }
                                dataBundle.putString(QURAN_VERB_WAZAN, vb.wazan)
                                dataBundle.putString(QURAN_VERB_ROOT, vb.root)
                                val intent = Intent(activity, ConjugatorTabsActivity::class.java)
                                intent.putExtras(dataBundle)
                                startActivity(intent)
                            }
                        }
                    } else if (verboccuranceid != null) {
                        //      text2 = ((MaterialButton) verboccuranceid).getText();
                        if (vb != null) {
                            val bundle = Bundle()
                            val intent = Intent(activity, WordOccuranceAct::class.java)
                            bundle.putString(QURAN_VERB_ROOT, vb.root)
                            intent.putExtras(bundle)
                            //   intent.putExtra(QURAN_VERB_ROOT,vb.getRoot());
                            startActivity(intent)
                        }
                    }
                }


            })


        }

    companion object {
        const val TAG = "bottom"

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