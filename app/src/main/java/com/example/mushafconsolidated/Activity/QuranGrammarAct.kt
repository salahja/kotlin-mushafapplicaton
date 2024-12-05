package com.example.mushafconsolidated.Activity


import ArabicrootListFragment
import Utility.WordDetailContract
import VerbrootListFragment
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.Constant
import com.example.Constant.SURAHFRAGTAG
import com.example.mushafconsolidated.Adapters.QuranDisplayAdapter
import com.example.mushafconsolidated.BottomOptionDialog

import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity



import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.QuranEntity


import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.SurahSummary
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.ajroomiya.NewAjroomiyaDetailHostActivity
import com.example.mushafconsolidated.data.CorpusRow
import com.example.mushafconsolidated.data.ShartCorpusRow
import com.example.mushafconsolidated.data.SurahHeader
import com.example.mushafconsolidated.databinding.NewFragmentReadingBinding
import com.example.mushafconsolidated.fragments.BookMarkCreateFrag
import com.example.mushafconsolidated.fragments.BookmarkFragment

import com.example.mushafconsolidated.fragments.GrammerFragmentsBottomSheet
import com.example.mushafconsolidated.fragments.NewSurahDisplayFrag
import com.example.mushafconsolidated.fragments.PhrasesDisplayFrag
import com.example.mushafconsolidated.fragments.ScrollingFragment
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListenerOnLong
import com.example.mushafconsolidated.quranrepo.QuranRepository
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.mushafconsolidated.settingsimport.Constants
import com.example.mushafconsolidatedimport.ParticleColorScheme
import com.example.sentenceanalysis.SentenceGrammarAnalysis
import com.example.utility.CorpusUtilityorig
import com.example.utility.CorpusUtilityorig.Companion.HightLightKeyWordold

import com.example.utility.CorpusUtilityorig.Companion.findWordOccurrencesArabic
import com.example.utility.CorpusUtilityorig.Companion.searchForTameez
import com.example.utility.ExtractionUtility.extractAccusativeSentences
import com.example.utility.ExtractionUtility.extractInMaIllaNegativeSentences
import com.example.utility.ExtractionUtility.extractKanaSistersSentences
import com.example.utility.ExtractionUtility.extractMousufSifa
import com.example.utility.ExtractionUtility.extractMousufSifanew
import com.example.utility.ExtractionUtility.extractSentenceAndTranslationFromWordIndices
import com.example.utility.ExtractionUtility.extractSentenceAndTranslationFromWordNumbers
import com.example.utility.ExtractionUtility.extractSifat
import com.example.utility.ExtractionUtility.extractsilaMousula
import com.example.utility.ExtractionUtility.nasab
import com.example.utility.ExtractionUtility.writeNegationDataToFile
import com.example.utility.QuranGrammarApplication.Companion.context

import com.example.utility.QuranViewUtils.extractCase

import com.example.utility.QuranViewUtils.extractVerbType
import com.example.utility.ScreenshotUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import database.NamesGridImageAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mufradat.MufradatPagerActivity
import org.sj.conjugator.activity.BaseActivity
import org.sj.conjugator.activity.ConjugatorAct
import sj.hisnul.fragments.NamesDetail
import wheel.OnWheelChangedListener
import wheel.WheelView
import java.io.File
import java.io.OutputStreamWriter
import javax.inject.Inject
import kotlin.collections.List
import kotlin.math.min
import kotlin.collections.List as CollectionsList


//import com.example.mushafconsolidated.Entities.JoinVersesTranslationDataTranslation;
@AndroidEntryPoint
class QuranGrammarAct : BaseActivity(), OnItemClickListenerOnLong {


    private lateinit var extractedSentencesWithIndices: CollectionsList<Pair<String, Pair<Int, Int>>>
    private val wordDetailLauncher = registerForActivityResult(WordDetailContract()) { wordFound ->
        if (!wordFound) {
            Toast.makeText(this, "Word detail not found", Toast.LENGTH_SHORT).show()
        }
    }

    private var bundles: Bundle? = null
    private lateinit var mainViewModel: QuranViewModel
    private var corpusSurahWord: CollectionsList<CorpusEntity>? = null

    private var corpusGroupedByAyah: LinkedHashMap<Int, ArrayList<CorpusEntity>> = LinkedHashMap()


    lateinit var binding: NewFragmentReadingBinding

    private lateinit var btnBottomSheet: FloatingActionButton
    lateinit var surahArabicName: String
    private lateinit var surahEnglishName: String
    private var jumptostatus = false
    var surahId = 0
    private var verseNumber = 0
    private var rukucount = 0
    var surahname: String? = null

    private var surahorpart = 0

    // TextView tvsurah, tvayah, tvrukus;
    private var currentSelectSurah = 0

    // --Commented out by Inspection (13/08/23, 4:31 pm):RelativeLayout layoutBottomSheet;
    var versescount = 0
    private var chapterorpart = false

    // --Commented out by Inspection (14/08/21, 7:26 PM):ChipNavigationBar chipNavigationBar;
    private var verseNo = 0
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView

    // ChipNavigationBar chipNavigationBar;
    private lateinit var materialToolbar: Toolbar

    //goes with extracttwothree

    // private lateinit var flowAyahWordAdapterpassage: FlowAyahWordAdapterPassage
    // private UpdateMafoolFlowAyahWordAdapter flowAyahWordAdapter;
    private var mausoof = false
    private var mudhaf = false
    private var harfnasb = false
    private var shart = false
    private lateinit var soraList: ArrayList<ChaptersAnaEntity>
    private var kana = false
    private var mafoolat = false
    private lateinit var allofQuran: CollectionsList<QuranEntity>
    private lateinit var shared: SharedPreferences


    @Inject
    lateinit var quranRepository: QuranRepository
    // @Inject


    private var isMakkiMadani = 0
    var chapterno = 0
    private lateinit var parentRecyclerView: RecyclerView
    private var mushafview = false


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val myFragment: NewSurahDisplayFrag? =
            supportFragmentManager.findFragmentByTag(SURAHFRAGTAG) as NewSurahDisplayFrag?


        val mngr = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = mngr.getRunningTasks(10)

        val contains = taskList[0].baseActivity!!.className.contains("QuranGrammar")
        if (myFragment != null && myFragment.isVisible && contains) {
            finish()
        }


        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dua_group, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView?
        searchView!!.queryHint = "Type something…"
        val sear = ContextCompat.getDrawable(this, R.drawable.custom_search_box)
        searchView.clipToOutline = true
        searchView.setBackgroundDrawable(sear)
        searchView.gravity = View.TEXT_ALIGNMENT_CENTER
        searchView.maxWidth = Int.MAX_VALUE
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return true
    }


    @OptIn(UnstableApi::class)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.jumpto -> {
                surahAyahPicker()
            }

            R.id.settings -> {
                val settingint = Intent(this, ActivitySettings::class.java)
                startActivity(settingint)
                navigationView.setCheckedItem(R.id.Names)
            }

            R.id.mushafview -> {
                val settingint = Intent(this, WordbywordMushafAct::class.java)
                startActivity(settingint)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        setupThemeAndPreferences()

        super.onCreate(savedInstanceState)
        binding = NewFragmentReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        materialToolbar = binding.toolbarmain
        setSupportActionBar(materialToolbar)


        if (isFirstTime) {
            val intents = Intent(this@QuranGrammarAct, ActivitySettings::class.java)
            startActivity(intents)
        }
        android.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        getpreferences()
        //bundle = intent
        bundles = intent.extras
        if (bundles != null) {
            loadSurahFromIntentData()

        } else {
            loadDefaultSurahData()
        }
    }

    private fun loadDefaultSurahData() {
        initView()
        initnavigation()

        val list = mainViewModel.loadListschapter().value
        //    final boolean chapterorpartb = bundle.getBooleanExtra(CHAPTERORPART, true);
        initView()
        initnavigation()
        versescount = list!![chapterno - 1].versescount
        isMakkiMadani = list[chapterno - 1].ismakki
        rukucount = list[chapterno - 1].rukucount
        surahArabicName = surahname.toString()
        surahEnglishName = list[chapterno - 1].nameenglish
        val utils = Utils(this)
        //  extractLaNafiyaJinsone()
        // extractLaNafiya()
        //   extractExpNegationSentences()
        // extractNegativeSentences()

        val verse =
            "وَلَقَدْ مَكَّنَّٰكُمْ فِى ٱلْأَرْضِ وَجَعَلْنَا لَكُمْ فِيهَا مَعَٰيِشَۗ قَلِيلًا مَّا تَشْكُرُونَ  "

        val word = "عَهْدَهُۥٓۖ"
        //   QuranViewUtils.showIndexOfWindow(this,verse,word)
        val start = false
        if (start) {
         mainLoopFromIndexExtraction()
         //mainLoopforErabStringEXTRACTION()
         //    mainLoopforIndexEXTRACTION()
            //extractExpNegationSentences()
        }


        supportFragmentManager.commit {
            replace<NewSurahDisplayFrag>(R.id.frame_container_qurangrammar, SURAHFRAGTAG)
            setReorderingAllowed(true)
            addToBackStack(null)
            setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
            supportFragmentManager.saveBackStack("replacement")
        }
    }

    fun mainLoopforIndexEXTRACTION() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        val utils = Utils(this)
        val allLamNegativeSenteces = ArrayList<List<String>>()
        // val allLamNegativeSenteces =                             ArrayList<List<Pair<String, String>>>()
        var allofQuran: List<QuranEntity>? = null
        /*
                val text =
                    "(مِنْ آيَةٍ) جار ومجرور متعلقان بمحذوف صفة من ما والمعنى أي شيء ننسخ من الآيات وقيل متعلقان بمحذوف حال من ما، وقال بعضهم من زائدة وآية تمييز"
                val regex = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(تمييز|تمييز\\.|التمييز)".toRegex()

                val match = regex.find(text)
                if (match != null) {
                    val wordInParentheses = match.groupValues[1]
                    val keyword = match.groupValues[2]
                    println("Word in parentheses: $wordInParentheses")
                    println("Keyword: $keyword")
                }
        */

        for (i in 11..114) {
            //   val quran = utils.getQuranbySurah(i)
            val quran = mainViewModel.getquranbySUrah(i)


            val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                i, quran.value!![1].ayah
            ) as ArrayList<CorpusEntity>
            val extractAccusativeSentences = extractAccusativeSentences(corpusEntity)
            println("check")
            //     val lamNegationDataList = searchForTameez(quran)

            //test with surah 11 ayah 81


            //     val lamNegationDataList = QuranViewUtils.collectBrokenPlurals(corpusEntity,quran.value!![s].qurantext)
            // val lamNegationDataList =             setPresentTenceNegationwithLA(corpusEntity, quran.value!![s].qurantext)//GOOD


            //    val lamNegationDataList=         setMaaNegationPresent(corpusEntity, quran.value!![s].qurantext)//good
            // val lamNegationDataList=         setLunNegation(corpusEntity, quran.value!![s].qurantext)//good


            //  val lamNegationDataList =      setLamNegation(corpusEntity, quran.value!![s].qurantext)good
            //   val lamNegationDataList =     maaPastTenceNegation(corpusEntity, quran.value!![s].qurantext)


            // val lamNegationDataList=         setJumlaIsmiyaNegationMaaLaysa(corpusEntity, quran.value!![s].qurantext)

            //   val lamNegationDataList =             extractInMaIllaNegativeSentences(corpusEntity, quran.value!![s].qurantext)

            //   val lamNegationDataList =           extractInMaIllaPositiveSentences(corpusEntity, quran.value!![s].qurantext)

            //  val lamNegationDataList =        extractProhibitiveSentences(corpusEntity, quran.value!![s].qurantext)
            //  val lamNegationDataList =  QuranViewUtils.extractMudafMudafIlaih(corpusEntity, quran.value!![s].qurantext)
            /*    val list = extractMousufSifa(corpusEntity,quran.value!![s].qurantext)//working

                if (list.isNotEmpty()) {
                    allLamNegativeSenteces.add(list)
                }*/
            /*     if (lamNegationDataList.isNotEmpty()) {
                     allLamNegativeSenteces.add(lamNegationDataList)

                 }
     */


        }
        val fileName = "mudhaftr.csv"
        writeNegationDataToFile(context!!, allLamNegativeSenteces, fileName)
    }
    private fun mainLoopforErabStringEXTRACTION() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        val utils = Utils(this)
        val allLamNegativeSenteces = ArrayList<List<String>>()
        // val allLamNegativeSenteces =                             ArrayList<List<Pair<String, String>>>()
        var allofQuran: List<QuranEntity>? = null


        for (i in 1..114) {
            val quran = utils.getQuranbySurah(i)
            //     val quran = mainViewModel.getquranbySUrah(i)
            for( qd in quran.indices) {
                val info=      quran[qd]
                val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                    i, info.ayah
                ) as ArrayList<CorpusEntity>
                val list = extractMousufSifanew(corpusEntity,info.qurantext)
                //   val list = extractSifat(corpusEntity,info.qurantext)


                if (list.isNotEmpty()) {
                    allLamNegativeSenteces.add(list)
                }
            }

            // val lamNegationDataList = searchForTameez(quran)

            //test with surah 11 ayah 81


            //     val lamNegationDataList = QuranViewUtils.collectBrokenPlurals(corpusEntity,quran.value!![s].qurantext)
            // val lamNegationDataList =             setPresentTenceNegationwithLA(corpusEntity, quran.value!![s].qurantext)//GOOD


            //    val lamNegationDataList=         setMaaNegationPresent(corpusEntity, quran.value!![s].qurantext)//good
            // val lamNegationDataList=         setLunNegation(corpusEntity, quran.value!![s].qurantext)//good


            //  val lamNegationDataList =      setLamNegation(corpusEntity, quran.value!![s].qurantext)good
            //   val lamNegationDataList =     maaPastTenceNegation(corpusEntity, quran.value!![s].qurantext)


            // val lamNegationDataList=         setJumlaIsmiyaNegationMaaLaysa(corpusEntity, quran.value!![s].qurantext)

            //   val lamNegationDataList =             extractInMaIllaNegativeSentences(corpusEntity, quran.value!![s].qurantext)

            //   val lamNegationDataList =           extractInMaIllaPositiveSentences(corpusEntity, quran.value!![s].qurantext)

            //  val lamNegationDataList =        extractProhibitiveSentences(corpusEntity, quran.value!![s].qurantext)
            //  val lamNegationDataList =  QuranViewUtils.extractMudafMudafIlaih(corpusEntity, quran.value!![s].qurantext)

            /*   if (lamNegationDataList.isNotEmpty()) {
                   allLamNegativeSenteces.add(lamNegationDataList)
                   //  allLamNegativeSenteces.add(ExtractedSentence)
               }*/


        }
        val fileName = "NEWSIFA.csv"
        writeNegationDataToFile(context!!, allLamNegativeSenteces, fileName)
    }
private   fun mainLoopFromIndexExtraction() {
    mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

    val allLamNegativeSenteces = ArrayList<List<String>>()
    val accsentece = ArrayList<Map<String, Any>>()
    val accusativeSentencesCollection = mutableListOf<Map<String, Any>>()
    var lamNeationDataList: List<String> = emptyList()
    //  val corpus = mainViewModel.getCorpusEntityFilterSurah(1)
    //   val quran = mainViewModel.getquranbySUrah(i)
    val utils = Utils(this)
    // val wordino = utils.getExpInMaIllaNegationall()
    //   val wordino = utils.getIllaPositiveAll()
    //   val wordInfo: List<IllaPositive> =         utils.getIllaPositiveAll()
    //   val wordino=  utils.getfutureall()
    //  val wordino= utils.getpresentall()
    //   val wordino=mainViewModel.getLamMudharyNegationAll()


    // val wordInfo = utils.getNASAB()
    //  val wordInfo=       utils.getNasbAall()
    //    val wordInfo=utils.getKanaAll()
    // val wordInfo=  utils.getManAmmaConditional()
    //   val wordInfo=    utils.getNegationall()
  //  val wordInfo=        utils.getNasabFilterSubType("shart")
  //  val wordInfo=    utils.getKadaSisters()
   // val wordInfo=    utils.getRelativePronouns()
    val wordInfo=    utils.getSubordinateClause()
    //  val wordInfo = utils.getLauAll()
    //  val wordInfoss= utils.getIzaAll()
    // val wordInfo=utils.getInALL()
    //   val wordInfo=utils.getShartALL()
    //  val wordInfo=    utils.getKanaAlls()
    //  val wordInfo = utils.getMaMinALL()
    for (s in wordInfo!!.indices) {
        val ss = wordInfo!![s]
        val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
            ss.surah, ss.ayah
        )
                as ArrayList<CorpusEntity>
        val quran = mainViewModel.getsurahayahVerses(ss.surah, ss.ayah)

        // val lamNegationDataList =             ExtractionUtility.extractKana(corpusEntity,ss, quran.value!![0].qurantext,quran.value!![0].translation)
        //   val lamNegationDataList =             extractSentenceAndTranslationFromShartIndices(corpusEntity,ss, quran.value!![0].qurantext)
        //     val extractedSentences=extractConditionalSentencesLau(corpusEntity)
        //  val lamNegationDataList =        extractSentenceAndTranslationFromNasabIndices(corpusEntity,ss, quran.value!![0].qurantext)
        //val extractedSentences = extractAccusativeSentences(corpusEntity)
        // val extractedSentences     = extractKanaSentences(corpusEntity,quran.value!![0].qurantext,quran.value!![0].translation)

      //  val extractedSentences     =     extractKanaSistersSentences(corpusEntity,quran.value!![0].qurantext,quran.value!![0].translation)
          val extractedSentences     =       extractsilaMousula(corpusEntity,quran.value!![0].qurantext,quran.value!![0].translation)
        //   val extractedSentences = extractConditionalSentencesWhenWithVerbsIN(corpusEntity,quran.value!![0].qurantext,quran.value!![0].translation)

        //     val extractedSentences = extractConditionalSentencesWhenWithVerbsMAMINJUSSIVE(corpusEntity,quran.value!![0].qurantext, quran.value!![0].translation)


        //   val extractedSentences =    extractConditionalSentencesIZAWITHVERB(corpusEntity)
        //   val extractedSentences =           extractConditionalSentencesWhenWithMANLAMMARSLT(corpusEntity)
        //  val extractedSentences = extractConditionalSentencesIZAWITHRSLT(corpusEntity)

        /*     lamNegationDataList = extractSentenceAndTranslationFromWordIndices(
                 corpusEntity,
                 ss,
                 quran.value!![0].qurantext
             )
 */

        // val lamNegationDataList =              extractSentenceAndTranslationFromWordIndices(corpusEntity,ss, quran.value!![0].qurantext)
        //    val lamNegationDataList =              addQuranVerseTranslationErab(ss, quran.value!![0].qurantext,quran.value!![0].translation,quran.value!![0].ar_irab_two)
   //     val lamNegationDataList =             extractSentenceAndTranslationFromWordNumbers(corpusEntity,ss,quran.value!![0].qurantext)


        // val lamNegationDataList=         maaPastTenceNegation(corpusEntity, quran.value!![s].qurantext)
        //    val lamNegationDataList =                setPresentTenceNegationwithLA(corpusEntity, quran.value!![s].qurantext)
        //    val lamNegationDataList=         setMaaNegationPresent(corpusEntity, quran.value!![s].qurantext)
        // val lamNegationDataList=         setLunNegation(corpusEntity, quran.value!![s].qurantext)
        //    val lamNegationDataList=         setLaaNegationPresent(corpusEntity, quran.value!![s].qurantext)

        // val lamNegationDataList=         setJumlaIsmiyaNegationMaaLaysa(corpusEntity, quran.value!![s].qurantext)
        // val lamNegationDataList =                extractInMaIllaSentences(corpusEntity, quran.value!![s].qurantext)


     /*   if (lamNegationDataList.isNotEmpty()) {
            allLamNegativeSenteces.add(lamNegationDataList)

        }*/
          if (extractedSentences.isNotEmpty()) {
                accusativeSentencesCollection.addAll(extractedSentences)


            }

    }
        val (setenceCollection, Sentences) = nasab(accusativeSentencesCollection)

    // val (setenceCollection, Sentences) = shart(accusativeSentencesCollection)


    val fileName = "anmasdariya.csv"
    writeNegationDataToFile(context!!, setenceCollection, fileName)

  //  writeNegationDataToFile(context!!, allLamNegativeSenteces, fileName)
}
    private fun loadSurahFromIntentData() {
        bundles = intent.extras

        val chapter = bundles!!.getInt(Constant.SURAH_ID, 1)
        mushafview = bundles!!.getBoolean("passages", false)
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        val list = mainViewModel.loadListschapter().value

        initView()
        initnavigation()
        chapterno = chapter

        surahArabicName = list!![chapter - 1].abjadname
        surahEnglishName = list[chapter - 1].nameenglish
        versescount = list[chapter - 1].versescount
        isMakkiMadani = list[chapter - 1].ismakki
        rukucount = list[chapter - 1].rukucount
        bundles = null

        setTranslation()
    }

    private fun setupThemeAndPreferences() {
        this.shared = PreferenceManager.getDefaultSharedPreferences(this@QuranGrammarAct)

        currenttheme =
            PreferenceManager.getDefaultSharedPreferences(this).getString("themepref", "dark")
        switchTheme(currenttheme) // Call switchTheme before super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this.application)
    }

    @OptIn(UnstableApi::class)
    private fun initnavigation() {
        btnBottomSheet = binding.fab
        btnBottomSheet = binding.fab
        drawerLayout = binding.drawer
        navigationView = binding.navigationView
        bottomNavigationView = binding.bottomNavView
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, materialToolbar, R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        btnBottomSheet.setOnClickListener { v: View? -> toggleBottomSheets() }
        //  bottomNavigationView.setOnItemSelectedListener { new  }OnItemReselectedListener
        bottomNavigationView.setOnItemReselectedListener { item: MenuItem ->
            if (item.itemId == R.id.surahnav) {
                materialToolbar.title = "Surah"
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
                val newCustomFragment = NewSurahDisplayFrag.newInstance()
                transaction.replace(R.id.frame_container_qurangrammar, newCustomFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                navigationView.setCheckedItem(R.id.surahnav)
            }
            if (item.itemId == R.id.mushafview) {
                val settingints = Intent(this@QuranGrammarAct, ShowMushafActivity::class.java)

                startActivity(settingints)
            }
            if (item.itemId == R.id.conjugationnav) {
                materialToolbar.title = "Conjugator"
                val conjugatorintent = Intent(this@QuranGrammarAct, ConjugatorAct::class.java)
                startActivity(conjugatorintent)
            }/*      */



            if (item.itemId == R.id.names) {
                materialToolbar.title = "Quran Audio"
                val settingint = Intent(this@QuranGrammarAct, NamesGridImageAct::class.java)
                settingint.putExtra(Constants.SURAH_INDEX, chapterno)
                startActivity(settingint)
            }

            if (item.itemId == R.id.quiz) {
                /*  materialToolbar.title = "Verb Quiz"
                  val settingint = Intent(this@QuranGrammarAct, ArabicVerbQuizActNew::class.java)
                  settingint.putExtra(Constants.SURAH_INDEX, chapterno)
                  startActivity(settingint)*/


                val phrasesDisplayFrag = PhrasesDisplayFrag()
                //  TameezDisplayFrag bookmarkFragment=new TameezDisplayFrag();
                val transactions = supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transactions.add(R.id.frame_container_qurangrammar, phrasesDisplayFrag)
                    .addToBackStack("mujarrad")
                transactions.commit()
            }


        }
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.bookmark) {
                drawerLayout.closeDrawers()
                val bookmarkFragment = BookmarkFragment()
                //  TameezDisplayFrag bookmarkFragment=new TameezDisplayFrag();
                val transactions = supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transactions.add(R.id.frame_container_qurangrammar, bookmarkFragment)
                    .addToBackStack("mujarrad")
                transactions.commit()
            }
            if (item.itemId == R.id.rootdetails) {
                drawerLayout.closeDrawers()/*       val bundle = Bundle()
                       val roots = Intent(this, arabicrootDetailHostActivity::class.java)
                       bundle.putString(Constant.WORDDETAILS, "word")
                       roots.putExtras(bundle)

                       startActivity(roots)*/
                val fragmentManager: FragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
                val newCustomFragment = ArabicrootListFragment.newInstance("word")
                //    newCustomFragment.arguments = bundle
                transaction.replace(
                    R.id.frame_container_qurangrammar, newCustomFragment, "rootfragment"
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
            if (item.itemId == R.id.verbdetails) {/*                drawerLayout.closeDrawers()
                                   drawerLayout.closeDrawers()
                                    val verbdetails = Intent(this, QuranVerbRootDetailHostActivity::class.java)
                                    verbdetails.putExtra(Constant.WORDDETAILS, "verb")
                                    startActivity(verbdetails)*/
                drawerLayout.closeDrawers()
                val fragmentManager: FragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
                val verbrootfragment = VerbrootListFragment.newInstance("VERB")
                //  QuranVerbRootListFragment

                //    newCustomFragment.arguments = bundle
                transaction.replace(
                    R.id.frame_container_qurangrammar, verbrootfragment, "verbfragment"
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
            if (item.itemId == R.id.ajroomiya_detail) {
                drawerLayout.closeDrawers()
                val grammar = Intent(this, NewAjroomiyaDetailHostActivity::class.java)
                startActivity(grammar)
            }
            if (item.itemId == R.id.phrases) {

                drawerLayout.closeDrawers()
                materialToolbar.title = "Topics"


                // val conjugatorintent = Intent(this@QuranGrammarAct, PhrasesGrammarAct::class.java)
                val conjugatorintent =
                    Intent(this@QuranGrammarAct, MufradatPagerActivity::class.java)
                startActivity(conjugatorintent)

            }

            if (item.itemId == R.id.settings) {
                drawerLayout.closeDrawers()
                materialToolbar.title = "Settings"
                val settingint = Intent(this, ActivitySettings::class.java)
                startActivity(settingint)


            }

            if (item.itemId == R.id.searchtopic) {
                drawerLayout.closeDrawers()
                materialToolbar.title = "Topics"
                val searchs = Intent(this, QuranTopicSearchActivity::class.java)
                startActivity(searchs)
            }
            false
        }
    }// first time

    ////////////////
    private val isFirstTime: Boolean
        get() {
            val preferences = getPreferences(MODE_PRIVATE)
            val ranBefore = preferences.getBoolean("RanBefore", false)
            if (!ranBefore) {
                // first time
                val editor = preferences.edit()
                editor.putBoolean("RanBefore", true)
                editor.apply()
            }
            return !ranBefore
        }

    private fun getpreferences() {
        val pref = applicationContext.getSharedPreferences("lastread", MODE_PRIVATE)
        chapterno = pref.getInt(Constant.SURAH_ID, 1)
        verseNo = pref.getInt(Constant.AYAH_ID, 1)
        surahname = pref.getString(Constant.SURAH_ARABIC_NAME, "")
        surahArabicName = surahname.toString()
    }


    private fun initDialogComponents(readPosition: Int) {
        val quranEntity = allofQuran[readPosition - 1]
        var suraNumber = 0
        var verseNumber = 0

        // Inflate the dialog view
        val jumpDialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.backupjumb_to_popup, null)
        jumpDialog.setContentView(dialogView)

        // Get references to the Spinners and Button
        val suraNames: Spinner = dialogView.findViewById(R.id.suras)
        val verses: Spinner = dialogView.findViewById(R.id.verses)
        val ok: Button = dialogView.findViewById(R.id.ok)
        soraList = mainViewModel.loadListschapter().value as ArrayList<ChaptersAnaEntity>
        // Set up Surah Spinner
        val surahOptions = soraList.mapIndexed { index, entity ->
            "${index + 1} - ${entity.nameenglish} - ${entity.abjadname}"
        }
        val surahAdapter = ArrayAdapter(this, R.layout.myspinner, surahOptions)
        suraNames.adapter = surahAdapter
        suraNames.setSelection(quranEntity.surah - 1)

        // Set up Verse Spinner (initially for the selected Surah)
        updateVerseSpinner(verses, quranEntity.surah, quranEntity.ayah, soraList)

        // Surah selection listener

        suraNames.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                suraNumber = position + 1
                updateVerseSpinner(
                    verses, suraNumber, 1, soraList
                ) // Reset verse selection to 1 when Surah changes
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        // Verse selection listener
        // Verse selection listener
        verses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                verseNumber = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // OK button click listener
        ok.setOnClickListener {
            jumpDialog.dismiss()
            verseNo = verseNumber
            surahArabicName = soraList[suraNumber - 1].abjadname
            versescount = soraList[suraNumber - 1].versescount
            isMakkiMadani = soraList[suraNumber - 1].ismakki
            rukucount = soraList[suraNumber - 1].rukucount
            currentSelectSurah = suraNumber

            if (currentSelectSurah == chapterno) {
                parentRecyclerView.post { parentRecyclerView.scrollToPosition(verseNo) }
            } else {
                jumptostatus = true
                surahorpart = currentSelectSurah
                surahId = currentSelectSurah
                chapterno = currentSelectSurah
                executeSurahWordByWord()
            }
        }

        jumpDialog.show()
    }

    // Helper function to update the Verse Spinner based on selected Surah
    private fun updateVerseSpinner(
        verseSpinner: Spinner,
        surahNumber: Int,
        selectedVerse: Int,
        soraList: ArrayList<ChaptersAnaEntity>
    ) {
        val verseOptions = (1..soraList[surahNumber - 1].versescount).map { it.toString() }
        val verseAdapter = ArrayAdapter(this, R.layout.spinner_layout_larg, verseOptions)
        verseSpinner.adapter = verseAdapter
        verseSpinner.setSelection(selectedVerse)
    }

    private fun surahAyahPicker() {
        val surahOptions = resources.getStringArray(R.array.surahdetails)
        val verseCounts = resources.getIntArray(R.array.versescount)

        val dialogView = layoutInflater.inflate(R.layout.activity_wheel_t, null)
        val surahWheel: WheelView = dialogView.findViewById(R.id.wv_year)
        val verseWheel: WheelView = dialogView.findViewById(R.id.wv_month)
        val textView: TextView = dialogView.findViewById(R.id.textView2)

        surahWheel.setEntries(*surahOptions)
        surahWheel.currentIndex = chapterno - 1

        // Update verse options based on initial Surah selection
        updateVerseOptions(verseWheel, chapterno - 1, verseCounts)
        updateTextView(textView, surahWheel, verseWheel)

        val dialogPicker =
            AlertDialog.Builder(this).setView(dialogView).setPositiveButton("Done") { dialog, _ ->
                val selectedSurahIndex = surahWheel.currentIndex
                val selectedVerseIndex = verseWheel.currentIndex + 1 // Verse index starts from 1
                soraList = mainViewModel.loadListschapter().value as ArrayList<ChaptersAnaEntity>
                try {
                    // Update Surah-related information
                    surahArabicName = soraList[selectedSurahIndex].abjadname
                    verseNo = selectedVerseIndex
                    versescount = soraList[selectedSurahIndex].versescount
                    isMakkiMadani = soraList[selectedSurahIndex].ismakki
                    rukucount = soraList[selectedSurahIndex].rukucount
                    currentSelectSurah = soraList[selectedSurahIndex].chapterid
                } catch (e: ArrayIndexOutOfBoundsException) {
                    // Handle potential index issues (e.g., if array sizes don't match)
                    // ... (Similar to your original code)
                }

                // Handle navigation to the selected Surah and Verse
                handleNavigation(selectedSurahIndex)

                // Save last read position


                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }.create()

        // Customize dialog appearance based on theme preference
        customizeDialogAppearance(dialogPicker)

        // Surah wheel listener
        surahWheel.onWheelChangedListener = OnWheelChangedListener { wheel, oldIndex, newIndex ->
            updateVerseOptions(verseWheel, newIndex, verseCounts)
            updateTextView(textView, surahWheel, verseWheel)
        }

        // Verse wheel listener
        verseWheel.onWheelChangedListener = OnWheelChangedListener { _, _, newIndex ->
            updateTextView(textView, surahWheel, verseWheel)
        }

        dialogPicker.show()
    }

    // Helper functions for better organization
    private fun updateVerseOptions(verseWheel: WheelView, surahIndex: Int, verseCounts: IntArray) {
        val verseOptions = (1..verseCounts[surahIndex]).map { it.toString() }.toTypedArray()
        verseWheel.setEntries(*verseOptions)
        verseWheel.currentIndex = 0 // Reset verse selection to 1 when Surah changes
    }

    private fun updateTextView(textView: TextView, surahWheel: WheelView, verseWheel: WheelView) {
        val selectedSurah = surahWheel.getItem(surahWheel.currentIndex) as String
        val selectedVerse = verseWheel.getItem(verseWheel.currentIndex) as String
        textView.text = "$selectedSurah/$selectedVerse"
    }

    private fun handleNavigation(selectedSurahIndex: Int) {
        val chapterno = IntArray(1)
        parentRecyclerView = binding.overlayViewRecyclerView
        val myFragment: NewSurahDisplayFrag? =
            supportFragmentManager.findFragmentByTag(SURAHFRAGTAG) as NewSurahDisplayFrag?


        val mngr = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = mngr.getRunningTasks(10)

        if (myFragment != null && myFragment.isVisible) {
            val settingint = Intent(this, QuranGrammarAct::class.java)
            settingint.putExtra(Constant.SURAH_ID, this.chapterno)

            settingint.putExtra(Constant.RUKUCOUNT, rukucount)
            settingint.putExtra(Constant.ISMAKKI, isMakkiMadani)
            settingint.putExtra(Constant.VERSESCOUNT, versescount)
            settingint.putExtra(Constant.SURAHNAME, surahArabicName)
            startActivity(settingint)
        } else

            if (currentSelectSurah == this.chapterno) {
                parentRecyclerView.post { parentRecyclerView.scrollToPosition(verseNo) }
            } else {
                jumptostatus = true
                surahorpart = currentSelectSurah
                surahId = currentSelectSurah
                this.chapterno = currentSelectSurah
//                println(soraList[chapterno[0]].chapterid)
                println("chapterno" + this.chapterno)
                println(this.chapterno)
                executeSurahWordByWord()
                //     asyncTaskcorpus = new refactoringcurrentSurahSyncWordByWord().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        val sura: String = this.soraList[selectedSurahIndex].chapterid.toString()
        this.chapterno = this.soraList[selectedSurahIndex].chapterid
        surahArabicName = this.soraList[selectedSurahIndex].nameenglish
        surahArabicName = this.soraList[selectedSurahIndex].namearabic
        val pref = getSharedPreferences("lastreadmushaf", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(Constant.CHAPTER, sura.toInt())
        //  editor.putInt("page", page.getAyahItemsquran().get(0).getPage());
        editor.putString(
            Constant.SURAH_ARABIC_NAME, this.soraList[selectedSurahIndex - 1].namearabic
        )
        editor.apply()
    }


    private fun customizeDialogAppearance(alertDialog: AlertDialog) {
        val preferences = shared.getString("themepref", "dark")
        when (preferences) {
            "light" -> alertDialog.window!!.setBackgroundDrawableResource(R.color.md_theme_dark_onSecondary)
            "brown" -> alertDialog.window!!.setBackgroundDrawableResource(R.color.background_color_light_brown)
            "blue" -> alertDialog.window!!.setBackgroundDrawableResource(R.color.prussianblue)
            "green" -> alertDialog.window!!.setBackgroundDrawableResource(R.color.mdgreen_theme_dark_onPrimary)
        }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(alertDialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //   alertDialog.show();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.show()
        val buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonPositive.setTextColor(ContextCompat.getColor(this@QuranGrammarAct, R.color.green))
        val buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(this@QuranGrammarAct, R.color.red))
        when (preferences) {
            "light", "brown" -> {
                buttonPositive.setTextColor(
                    ContextCompat.getColor(
                        this@QuranGrammarAct, R.color.colorMuslimMate
                    )
                )
                buttonNegative.setTextColor(
                    ContextCompat.getColor(
                        this@QuranGrammarAct, R.color.red
                    )
                )
            }

            "blue" -> {
                buttonPositive.setTextColor(
                    ContextCompat.getColor(
                        this@QuranGrammarAct, R.color.yellow
                    )
                )
                buttonNegative.setTextColor(
                    ContextCompat.getColor(
                        this@QuranGrammarAct, R.color.Goldenrod
                    )
                )
            }

            "green" -> {
                buttonPositive.setTextColor(
                    ContextCompat.getColor(
                        this@QuranGrammarAct, R.color.yellow
                    )
                )
                buttonNegative.setTextColor(
                    ContextCompat.getColor(
                        this@QuranGrammarAct, R.color.cyan_light
                    )
                )
            }
        }
        //  wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        alertDialog.window!!.attributes = lp
        alertDialog.window!!.setGravity(Gravity.TOP)
    }


    private fun setTranslation() {
        shared.getBoolean("prefs_show_erab", true)
        executeSurahWordByWord()
    }

    private fun executeSurahWordByWord() {
        val builder = AlertDialog.Builder(
            this, R.style.Theme_MaterialComponents_DayNight_NoActionBar_NoActionBar
        )
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val dialog = builder.create()
        val listener: OnItemClickListenerOnLong = this

        val scope = CoroutineScope(Dispatchers.Main)

        //bysurah(dialog, scope,  listener)
        bysurahjsonStorage(dialog, scope, listener)

    }


    private fun loadJsonFromFile(context: Context, fileName: String): String? {
        val file = File(context.filesDir, fileName)
        return try {
            file.readText()
        } catch (e: Exception) {
            // e.printStackTrace()
            null // Return null if an error occurs
        }
    }


    @OptIn(UnstableApi::class)
    private fun bysurahjsonStorage(
        dialog: AlertDialog,
        ex: CoroutineScope,
        listener: OnItemClickListenerOnLong,
    ) {
        runOnUiThread {
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.color.bg_brown)
        }

        ex.launch(Dispatchers.IO) {
            try {
                val fileName = "surah$chapterno.json"
                //  var newnewadapterlist: LinkedHashMap<Int, ArrayList<NewCorpusEntity>>? = null
                val corpusAndQurandata = quranRepository.CorpusAndQuranDataSurah(chapterno)
                val jsonString = loadJsonFromFile(context!!, fileName)
                if (jsonString != null) {
                    val mapType =
                        object : TypeToken<LinkedHashMap<Int, ArrayList<CorpusEntity>>>() {}.type
                    val gson = Gson()
                    allofQuran = corpusAndQurandata.allofQuran
                    corpusGroupedByAyah = gson.fromJson(jsonString, mapType)
                    println("check")
                } else {

                    allofQuran = corpusAndQurandata.allofQuran
                    corpusSurahWord = corpusAndQurandata.copusExpandSurah

                    corpusGroupedByAyah =
                        corpusSurahWord!!.groupBy { it.ayah } as LinkedHashMap<Int, ArrayList<CorpusEntity>>


                    val gson = Gson()
                    val json = gson.toJson(corpusGroupedByAyah)
                    saveJsonFile(context!!, fileName, json)
                }

                withContext(Dispatchers.Main) {
                    dialog.dismiss()

                    parentRecyclerView = binding.overlayViewRecyclerView

                    if (jumptostatus) {
                        surahorpart = chapterno
                    }
                    val header =
                        SurahHeader(rukucount, versescount, chapterno, surahArabicName, " ")

                    HightLightKeyWordold(allofQuran)

                    val adapter =


                        QuranDisplayAdapter(
                            false,
                            header,
                            allofQuran,
                            corpusGroupedByAyah,
                            this@QuranGrammarAct,
                            surahArabicName,
                            isMakkiMadani,
                            listener,
                            mainViewModel,


                            )

                    //      adapter.addContext(this@QuranGrammarAct)
                    //  adapter.addContext(this@QuranGrammarAct)
                    parentRecyclerView.setHasFixedSize(true)
                    parentRecyclerView.adapter = adapter
                    parentRecyclerView.post { parentRecyclerView.scrollToPosition(verseNo) }
                }
            } catch (e: Exception) {
                // Handle JSON loading or parsing errors
                Log.e("QuranGrammarAct", "Error loading data", e)
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    // Show error message to the user
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun saveJsonFile(context: Context, fileName: String, jsonData: String) {
        try {
            val fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE)
            fileOutputStream.write(jsonData.toByteArray())
            fileOutputStream.close()
            Log.d("JSON", "File saved successfully: $fileName")
        } catch (e: Exception) {
            Log.e("JSON", "Error saving file: $fileName", e)
        }
    }

    private fun loadItemListGrammarLineWise(surah: Int, ayah: Int) {


        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val item = GrammerFragmentsBottomSheet()
        val fragmentManager = supportFragmentManager
        // item.arguments = dataBundle

        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.abc_slide_in_top, android.R.anim.fade_out).show(item)
        // transactions.show(item);
        val data = arrayOf(surah.toString(), ayah.toString())
        GrammerFragmentsBottomSheet.newInstance(data)
            .show(supportFragmentManager, WordAnalysisBottomSheet.TAG)


    }

    private fun loadItemListCompose(dataBundle: Bundle) {
        val homeactivity = Intent(this@QuranGrammarAct, SentenceGrammarAnalysis::class.java)
        homeactivity.putExtras(dataBundle)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        startActivity(homeactivity)


    }

    @SuppressLint("RestrictedApi")
    override fun onItemClick(v: View, position: Int) {

        //   val tag=v.tag as String
        val colortag = shared.getBoolean("colortag", true)
        val tag = v.tag
        if (tag != null && tag.equals("header")) {
            // Handle header click
            Toast.makeText(this@QuranGrammarAct, "Header clicked!", Toast.LENGTH_SHORT).show()
        } else {


            if (tag == "overflow_img") {
                @SuppressLint("RestrictedApi") val menuBuilder = MenuBuilder(this)
                val inflater = MenuInflater(this)
                inflater.inflate(R.menu.popup_menu, menuBuilder)
                @SuppressLint("RestrictedApi") val optionsMenu =
                    MenuPopupHelper(this, menuBuilder, v)
                optionsMenu.setForceShowIcon(true)

                menuBuilder.setCallback(object : MenuBuilder.Callback {
                    @SuppressLint("RestrictedApi")
                    override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                        if (item.itemId == R.id.actionTafsir) { // Handle option1 Click
                            val readingintent =
                                Intent(this@QuranGrammarAct, TafsirFullscreenActivity::class.java)
                            val chapterno = corpusSurahWord!![position - 1].surah
                            val verse = corpusSurahWord!![position - 1].ayah
                            readingintent.putExtra(Constant.SURAH_ID, chapterno)
                            readingintent.putExtra(Constant.AYAH_ID, verse)
                            readingintent.putExtra(Constant.SURAH_ARABIC_NAME, surahArabicName)
                            startActivity(readingintent)
                            optionsMenu.dismiss()
                            return true
                        }
                        if (item.itemId == R.id.bookmark) { // Handle option2 Click
                            bookMarkSelected(position)
                            optionsMenu.dismiss()
                            return true
                        }
                        if (item.itemId == R.id.jumpto) { // Handle option2 Click
                            //  SurahAyahPicker();
                            initDialogComponents(position)
                            optionsMenu.dismiss()
                            return true
                        }
                        if (item.itemId == R.id.action_share) {
                            ScreenshotUtils.takeScreenshot(
                                window.decorView, context!!
                            )
                            optionsMenu.dismiss()
                            return true
                        }
                        if (item.itemId == R.id.ivHelp) { // Handle option2 Click
                            ParticleColorScheme.newInstance().show(
                                this@QuranGrammarAct.supportFragmentManager,
                                WordAnalysisBottomSheet.TAG
                            )
                            optionsMenu.dismiss()
                            return true
                        }
                        if (item.itemId == R.id.colorized) { // Handle option2 Click
                            if (colortag) {
                                val editor =
                                    android.preference.PreferenceManager.getDefaultSharedPreferences(
                                        this@QuranGrammarAct
                                    ).edit()
                                //     SharedPreferences.Editor editor = getActivity().getSharedPreferences("properties", 0).edit();
                                editor.putBoolean("colortag", false)
                                editor.apply()
                                reloadActivity()
                            } else {
                                val editor =
                                    android.preference.PreferenceManager.getDefaultSharedPreferences(
                                        this@QuranGrammarAct
                                    ).edit()
                                //     SharedPreferences.Editor editor = getActivity().getSharedPreferences("properties", 0).edit();
                                editor.putBoolean("colortag", true)
                                editor.apply()
                                reloadActivity()
                            }
                            optionsMenu.dismiss()
                            return true
                        }
                        return false
                    }

                    override fun onMenuModeChange(menu: MenuBuilder) {}
                })
                optionsMenu.show()
            } else {
                qurangrammarmenu(v, position)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun qurangrammarmenu(view: View, position: Int) {
        val tag = view.tag as String

        // Get the QuranEntity, handling potential IndexOutOfBoundsException
        val quranEntity = allofQuran.getOrElse(position) { allofQuran[position - 1] }

        when (tag) {
            "bookmarfb" -> bookMarkSelected(position)
            "arrowforward" -> handleArrowForward(quranEntity)
            "arrowback" -> handleArrowBack(quranEntity)
            "colorize" -> handleColorize(view)
            "overflowbottom" -> handleOverflowBottom(position)
            "jumptofb" -> handleJumpTo(position)
            "sharefb" -> handleShare()
            "helpfb" -> handleHelp(position)
            //  "overflow_img" ->   overFlowMenu(view, position)
            "help_img" -> handleHelpImage(position)
            "tafsir" -> handleTafsir(position)
            "qurantext" -> handleQuranText(position)
        }
    }


    private fun handleOverflowBottom(position: Int) {
        val chapterno = corpusSurahWord!![position - 1].surah
        val verse = corpusSurahWord!![position - 1].ayah
        val name = surahArabicName
        val data = arrayOf(chapterno.toString(), verse.toString(), name)
        BottomOptionDialog.newInstance(data)
            .show(this@QuranGrammarAct.supportFragmentManager, WordAnalysisBottomSheet.TAG)
    }

    private fun handleShare() {
        ScreenshotUtils.takeScreenshot(window.decorView, this)
    }

    private fun handleJumpTo(position: Int) {
        initDialogComponents(position)
    }

    private fun handleColorize(view: View) {
        val colorsentence = view.findViewById<SwitchCompat>(R.id.colorized)
        val colortag = shared.getBoolean("colortag", true)
        if (colortag) {
            val editor =
                android.preference.PreferenceManager.getDefaultSharedPreferences(this@QuranGrammarAct)
                    .edit()
            //     SharedPreferences.Editor editor = getActivity().getSharedPreferences("properties", 0).edit();
            editor.putBoolean("colortag", false)
            editor.apply()
            reloadActivity(colorsentence)
        } else {
            val editor =
                android.preference.PreferenceManager.getDefaultSharedPreferences(this@QuranGrammarAct)
                    .edit()
            //     SharedPreferences.Editor editor = getActivity().getSharedPreferences("properties", 0).edit();
            editor.putBoolean("colortag", true)
            editor.apply()
            reloadActivity(colorsentence)
        }
    }

    private fun handleArrowForward(quranEntity: QuranEntity) {
        val currentsurah = quranEntity.surah
        val mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        if (currentsurah != 114) {
            soraList = mainViewModel.loadListschapter().value as ArrayList<ChaptersAnaEntity>

            verseNumber = quranEntity.ayah
            val intent = intent.putExtra("chapter", soraList[0].chapterid)
                .putExtra("chapterorpart", chapterorpart).putExtra(
                    Constant.SURAH_ARABIC_NAME, soraList[0].abjadname
                ).putExtra(Constant.VERSESCOUNT, soraList[0].versescount)
                .putExtra(Constant.RUKUCOUNT, soraList[0].rukucount).putExtra(
                    Constant.MAKKI_MADANI, soraList[0].ismakki
                )
            overridePendingTransition(0, 0)
            startActivity(intent)
            finish()
            overridePendingTransition(
                android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }
    }

    private fun handleArrowBack(quranEntity: QuranEntity) {
        val currentsurah = quranEntity.surah
        if (currentsurah != 1) {
            val mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

            soraList = mainViewModel.loadListschapter().value as ArrayList<ChaptersAnaEntity>


            verseNumber = quranEntity.ayah
            val intent = intent.putExtra("chapter", soraList[0].chapterid)
                .putExtra("chapterorpart", chapterorpart).putExtra(
                    Constant.SURAH_ARABIC_NAME, soraList[0].abjadname
                ).putExtra(Constant.VERSESCOUNT, soraList[0].versescount)
                .putExtra(Constant.RUKUCOUNT, soraList[0].rukucount).putExtra(
                    Constant.MAKKI_MADANI, soraList[0].ismakki
                )
            overridePendingTransition(0, 0)
            startActivity(intent)
            finish()
            overridePendingTransition(
                android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }
    }

    @SuppressLint("RestrictedApi", "InflateParams")
    fun qurangrammarmenus(view: View, position: Int) {
        val tag = view.tag
        //  val quranEntity: QuranEntity = flowAyahWordAdapter.getItem(position) as QuranEntity
        val quranEntity: QuranEntity = try {
            allofQuran[position]
        } catch (e: IndexOutOfBoundsException) {
            allofQuran[position - 1]
        }
        val colorsentence = view.findViewById<SwitchCompat>(R.id.colorized)
        val colortag = shared.getBoolean("colortag", true)
        if (tag == "bookmarfb") {
            bookMarkSelected(position)
        } else if (tag == "collection") {
            val dataBundle = Bundle()
            val chapterno = corpusSurahWord!![position - 1].surah
            val verse = corpusSurahWord!![position - 1].ayah


            dataBundle.putInt(Constant.SURAH_ID, chapterno)
            dataBundle.putInt(Constant.AYAHNUMBER, verse)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahArabicName)
            val item = BookMarkCreateFrag()
            item.arguments = dataBundle
            val data = arrayOf(chapterno.toString(), verse.toString(), surahArabicName)
            //    item.setdata(root!!WordMeanings,wbwRootwords,grammarRootsCombined);
            //   transactions.show(item);
            BookMarkCreateFrag.newInstance(data)
                .show(this@QuranGrammarAct.supportFragmentManager, WordAnalysisBottomSheet.TAG)


        }
        if (tag == "arrowforward") {
            val currentsurah = quranEntity.surah
            val mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
            if (currentsurah != 114) {
                soraList = mainViewModel.loadListschapter().value as ArrayList<ChaptersAnaEntity>

                verseNumber = quranEntity.ayah
                val intent = intent.putExtra("chapter", soraList[0].chapterid)
                    .putExtra("chapterorpart", chapterorpart).putExtra(
                        Constant.SURAH_ARABIC_NAME, soraList[0].abjadname
                    ).putExtra(Constant.VERSESCOUNT, soraList[0].versescount)
                    .putExtra(Constant.RUKUCOUNT, soraList[0].rukucount).putExtra(
                        Constant.MAKKI_MADANI, soraList[0].ismakki
                    )
                overridePendingTransition(0, 0)
                startActivity(intent)
                finish()
                overridePendingTransition(
                    android.R.anim.slide_out_right, android.R.anim.slide_in_left
                )
            }
        } else if (tag == "arrowback") {
            val currentsurah = quranEntity.surah
            if (currentsurah != 1) {
                val mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

                soraList = mainViewModel.loadListschapter().value as ArrayList<ChaptersAnaEntity>


                verseNumber = quranEntity.ayah
                val intent = intent.putExtra("chapter", soraList[0].chapterid)
                    .putExtra("chapterorpart", chapterorpart).putExtra(
                        Constant.SURAH_ARABIC_NAME, soraList[0].abjadname
                    ).putExtra(Constant.VERSESCOUNT, soraList[0].versescount)
                    .putExtra(Constant.RUKUCOUNT, soraList[0].rukucount).putExtra(
                        Constant.MAKKI_MADANI, soraList[0].ismakki
                    )
                overridePendingTransition(0, 0)
                startActivity(intent)
                finish()
                overridePendingTransition(
                    android.R.anim.slide_out_right, android.R.anim.slide_in_left
                )
            }
        } else if (tag == "colorize") {
            if (colortag) {
                val editor =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(this@QuranGrammarAct)
                        .edit()
                //     SharedPreferences.Editor editor = getActivity().getSharedPreferences("properties", 0).edit();
                editor.putBoolean("colortag", false)
                editor.apply()
                reloadActivity(colorsentence)
            } else {
                val editor =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(this@QuranGrammarAct)
                        .edit()
                //     SharedPreferences.Editor editor = getActivity().getSharedPreferences("properties", 0).edit();
                editor.putBoolean("colortag", true)
                editor.apply()
                reloadActivity(colorsentence)
            }
        } else if (tag == "overflowbottom") {
            val chapterno = corpusSurahWord!![position - 1].surah
            val verse = corpusSurahWord!![position - 1].ayah
            val name = surahArabicName
            val data = arrayOf(chapterno.toString(), verse.toString(), name)
            BottomOptionDialog.newInstance(data)
                .show(this@QuranGrammarAct.supportFragmentManager, WordAnalysisBottomSheet.TAG)
        } else if (tag == "jumptofb") {
            initDialogComponents(position)
        } else if (tag == "sharefb") {
            ScreenshotUtils.takeScreenshot(window.decorView, this)
        } else if (tag == "helpfb") {
            val chapterno = corpusSurahWord!![position - 1].surah
            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, chapterno)
            val item = SurahSummary()
            item.arguments = dataBundle
            SurahSummary.newInstance(chapterno).show(supportFragmentManager, NamesDetail.TAG)
        } else if (tag == "overflow_img") {
            // overFlowMenu(view, position, colortag)
        } else if (tag == "help_img") {
            println("check")/* ParticleColorScheme.newInstance()
                 .show(this@QuranGrammarAct.supportFragmentManager, WordAnalysisBottomSheet.TAG)*/

            val fragment = ScrollingFragment.newInstance(
                allofQuran[position].surah,
                allofQuran[position - 1].ayah,
                surahArabicName,
                isMakkiMadani
            )
            supportFragmentManager.beginTransaction().replace(
                R.id.frame_container_qurangrammar, fragment
            ) // Replaces the current fragment or view
                .addToBackStack(null) // Optional: adds the transaction to the back stack, so the user can navigate back
                .commit()

        } else if (tag == "tafsir") {

            val readingintent = Intent(this@QuranGrammarAct, TafsirFullscreenActivity::class.java)
            val chapterno = allofQuran[position - 1].surah
            val verse = allofQuran[position - 1].ayah
            readingintent.putExtra(Constant.SURAH_ID, chapterno)
            readingintent.putExtra(Constant.AYAH_ID, verse)
            readingintent.putExtra(Constant.SURAH_ARABIC_NAME, surahArabicName)
            startActivity(readingintent)
        } else if (tag == "qurantext") {
            val word: QuranEntity = if (position != 0) {
                allofQuran[position - 1]
            } else {
                allofQuran[position]
            }
            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.surah)
            dataBundle.putInt(Constant.AYAH_ID, Math.toIntExact(word.ayah.toLong()))
            loadItemListCompose(dataBundle)
            //   loadItemListGrammarLineWise(word.surah, word.ayah)

        }
    }


    private fun handleHelp(position: Int) {
        val chapterno = corpusSurahWord!![position - 1].surah
        val dataBundle = Bundle()
        dataBundle.putInt(Constant.SURAH_ID, chapterno)
        val item = SurahSummary()
        item.arguments = dataBundle
        SurahSummary.newInstance(chapterno).show(supportFragmentManager, NamesDetail.TAG)

    }

    private fun handleHelpImage(position: Int) {
        println("check")/* ParticleColorScheme.newInstance()
                 .show(this@QuranGrammarAct.supportFragmentManager, WordAnalysisBottomSheet.TAG)*/
        val fragment = ScrollingFragment.newInstance(
            allofQuran[position].surah,
            allofQuran[position - 1].ayah,
            surahArabicName,
            isMakkiMadani
        )
        supportFragmentManager.beginTransaction().replace(
            R.id.frame_container_qurangrammar, fragment
        ) // Replaces the current fragment or view
            .addToBackStack(null) // Optional: adds the transaction to the back stack, so the user can navigate back
            .commit()


    }

    private fun handleQuranText(position: Int) {
        val word: QuranEntity = if (position != 0) {
            allofQuran[position - 1]
        } else {
            allofQuran[position]
        }/*   val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.surah)
            dataBundle.putInt(Constant.AYAH_ID, Math.toIntExact(word.ayah.toLong()))
            loadItemListCompose(dataBundle)*/


        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val item = GrammerFragmentsBottomSheet()
        val fragmentManager = supportFragmentManager
        // item.arguments = dataBundle

        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.abc_slide_in_top, android.R.anim.fade_out).show(item)
        // transactions.show(item);
        val data = arrayOf(word.surah.toString(), word.ayah.toString())
        GrammerFragmentsBottomSheet.newInstance(data)
            .show(supportFragmentManager, WordAnalysisBottomSheet.TAG)


    }

    private fun handleTafsir(position: Int) {
        val readingintent = Intent(this@QuranGrammarAct, TafsirFullscreenActivity::class.java)
        val chapterno = allofQuran[position - 1].surah
        val verse = allofQuran[position - 1].ayah
        readingintent.putExtra(Constant.SURAH_ID, chapterno)
        readingintent.putExtra(Constant.AYAH_ID, verse)
        readingintent.putExtra(Constant.SURAH_ARABIC_NAME, surahArabicName)
        startActivity(readingintent)

    }

    private fun getBitmapFromView(view: View, defaultColor: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(defaultColor)
        view.draw(canvas)
        return bitmap
    }

    private fun reloadActivity() {

        val intent =
            intent.putExtra("chapter", chapterno).putExtra("chapterorpart", chapterorpart).putExtra(
                Constant.SURAH_ARABIC_NAME, surahArabicName
            ).putExtra("passages", mushafview).putExtra(Constant.VERSESCOUNT, versescount)
                .putExtra(Constant.RUKUCOUNT, rukucount).putExtra(
                    Constant.MAKKI_MADANI, isMakkiMadani
                )
        overridePendingTransition(0, 0)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
    }

    private fun bookMarkSelected(position: Int) {
        //  position = flowAyahWordAdapter.getAdapterposition();
        val chapterno = allofQuran[position - 1].surah
        val verse = allofQuran[position - 1].ayah
        val en = BookMarks()
        en.header = "pins"
        en.chapterno = chapterno.toString()
        en.verseno = verse.toString()
        en.surahname = surahArabicName
        val viewmodel: QuranViewModel by viewModels()

        viewmodel.Insertbookmark(en)
        val view = findViewById<View>(R.id.bookmark)
        val snackbar = Snackbar.make(view, "BookMark Created", Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.setTextColor(Color.CYAN)
        snackbar.setBackgroundTint(Color.BLACK)
        snackbar.show()
    }

    private fun reloadActivity(ignoredColorsentence: SwitchCompat) {

        val intent =
            intent.putExtra("chapter", chapterno).putExtra("chapterorpart", chapterorpart).putExtra(
                Constant.SURAH_ARABIC_NAME, surahArabicName
            ).putExtra(Constant.VERSESCOUNT, versescount).putExtra(Constant.RUKUCOUNT, rukucount)
                .putExtra(
                    Constant.MAKKI_MADANI, isMakkiMadani
                )
        overridePendingTransition(0, 0)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
    }

    override fun onItemLongClick(position: Int, v: View) {

    }

    private fun initView() {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        btnBottomSheet = binding.fab
        val verlayViewRecyclerView = findViewById<RecyclerView>(R.id.overlayViewRecyclerView)
        verlayViewRecyclerView.layoutManager = linearLayoutManager

    }

    private fun toggleBottomSheets() {
        if (bottomNavigationView.visibility == View.VISIBLE) {
            bottomNavigationView.visibility = View.GONE
            //    btnBottomSheet.setText("Close sheet");
        } else {
            bottomNavigationView.visibility = View.VISIBLE
            //    btnBottomSheet.setText("Expand sheet");
        }
    }

    companion object
}

/*    fun extractAccusativeSentences(corpusData: List<CorpusRow>): List<Map<String, Any>> {
        val accusativeSentences = mutableListOf<Map<String, Any>>()

        for (i in corpusData.indices) {
            val row = corpusData[i]

            // Scenario 1: ACC tag followed by PRON, ending in NOM (in details)
            if ("ACC" in row.tags && "PRON" in row.tags) {
                val accWordNo = row.wordno

                // Find sequence ending with a nominative case (NOM) in details fields
                val predicateSequence = findSequenceEndingInNom(corpusData, startIndex = i + 1)
                if (predicateSequence != null) {
                    accusativeSentences.add(
                        mapOf(
                            "type" to "Scenario 1",
                            "accWordNo" to accWordNo,
                            "predicateSequence" to predicateSequence
                        )
                    )
                }
            }

            // Scenario 2: ACC tag, noun sequence, ending in NOM (in details)
            else if ("ACC" in row.tags) {
                val accWordNo = row.wordno

                val accSequence = findAccNounSequenceEndingInNom(corpusData, startIndex = i + 1)
                if (accSequence != null) {
                    accusativeSentences.add(
                        mapOf(
                            "type" to "Scenario 2",
                            "accWordNo" to accWordNo,
                            "accSequence" to accSequence
                        )
                    )
                }
            }
        }

        return accusativeSentences
    }

    fun findSequenceEndingInNom(data: List<CorpusRow>, startIndex: Int): List<CorpusRow>? {
        val sequence = mutableListOf<CorpusRow>()
        for (row in data.drop(startIndex)) {
            sequence.add(row)
            if (isNominative(row.details)) {
                return sequence
            }
        }
        return null
    }

    fun findAccNounSequenceEndingInNom(data: List<CorpusRow>, startIndex: Int): List<CorpusRow>? {
        val sequence = mutableListOf<CorpusRow>()
        for (row in data.drop(startIndex)) {
            if ("ACC" in row.tags || "N" in row.tags) { // Continue with ACC and nouns
                sequence.add(row)
            }
            if (isNominative(row.details)) { // End on NOM in details fields
                return sequence
            }
        }
        return null
    }

    fun isNominative(details: List<String>): Boolean {
        return details.any { it == "NOM" }
    }*/