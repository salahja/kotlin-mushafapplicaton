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
import com.example.mushafconsolidated.Adapters.FlowAyahWordAdapterNoMafoolat
import com.example.mushafconsolidated.BottomOptionDialog
import com.example.mushafconsolidated.Entities.BadalErabNotesEnt
import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.HalEnt
import com.example.mushafconsolidated.Entities.LiajlihiEnt
import com.example.mushafconsolidated.Entities.MafoolBihi
import com.example.mushafconsolidated.Entities.MafoolMutlaqEnt
import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SurahHeader
import com.example.mushafconsolidated.Entities.TameezEnt
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.SurahSummary
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.ajroomiya.NewAjroomiyaDetailHostActivity
import com.example.mushafconsolidated.databinding.NewFragmentReadingBinding
import com.example.mushafconsolidated.fragments.BookMarkCreateFrag
import com.example.mushafconsolidated.fragments.BookmarkFragment
import com.example.mushafconsolidated.fragments.FlowAyahWordAdapter
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
import com.example.utility.CorpusUtilityorig.Companion.findWordOccurrencesArabic
import com.example.utility.QuranGrammarApplication.Companion.context
import com.example.utility.QuranViewUtils
import com.example.utility.ScreenshotUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ibm.icu.text.Collator
import com.ibm.icu.util.ULocale
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
import java.util.regex.Matcher
import javax.inject.Inject
import kotlin.collections.List
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


    //  private OnClickListener onClickListener;
    //  private val newadapterlist = LinkedHashMap<Int, ArrayList<NewCorpusAyahWord>>()
    private var mafoolbihiwords: CollectionsList<MafoolBihi?> = emptyList()
    private var liajlihient: CollectionsList<LiajlihiEnt?>? = emptyList()
    private var mutlaqEntList: CollectionsList<MafoolMutlaqEnt?> = emptyList()
    private var tameezEntList: CollectionsList<TameezEnt?> = emptyList()
    private var badalErabNotesEnts: CollectionsList<BadalErabNotesEnt?> = emptyList()
    private var jumlahaliya: CollectionsList<HalEnt?> = emptyList()
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
        mausoof = shared.getBoolean("mausoof", true)
        mudhaf = shared.getBoolean("mudhaf", true)
        harfnasb = shared.getBoolean("harfnasb", true)
        shart = shared.getBoolean("shart", true)
        kana = shared.getBoolean("kana", true)
        mafoolat = shared.getBoolean("mafoolat", false)
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
        val start = false
        if (start) {
        //      mainLoopFromIndexExtraction()
            mainLoopforIndexEXTRACTION()
        }


        supportFragmentManager.commit {
            replace<NewSurahDisplayFrag>(R.id.frame_container_qurangrammar, SURAHFRAGTAG)
            setReorderingAllowed(true)
            addToBackStack(null)
            setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
            supportFragmentManager.saveBackStack("replacement")
        }
    }


    fun extractSentenceAndTranslationFromSavedIndexes(
        corpus: List<CorpusEntity>,
        wordInfo: NewMudhafEntity,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()


        var extractedSentence = ""
        if (wordInfo.startindex != -1 && wordInfo.endindex != -1) {
            // Extract the Arabic sentence between the start and end indexes
            try {
                extractedSentence =
                    quranText.substring(wordInfo.startindex, wordInfo.endindex).trim()
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
                println(wordInfo.surah + wordInfo.ayah)
                println(quranText)
            }


            // Extract translation using wordfrom and wordnoto
            /* val translationBuilder = StringBuilder()
             val wordto=wordInfo.wordno+1
             for (entry in corpus) {
                 if (entry.wordno in wordInfo.wordno..wordto) {
                     translationBuilder.append("${entry.en} ").append(" ")
                 }
             }
             val extractedTranslation = translationBuilder.toString().trim()*/
            val type = "sifa"
            val start = wordInfo.startindex
            val end = wordInfo.endindex
            val nextword = wordInfo.wordfrom + 1
            // Format the result string
            val dataString =
                "${wordInfo.surah}|${wordInfo.ayah}|${wordInfo.wordfrom}|${wordInfo.wordto}|$start|$end|$extractedSentence"
            result.add(dataString)
        } else {
            // Handle the case when startIndex or endIndex is not found
            result.add("Error: Couldn't find indices for Surah ${wordInfo.surah}, Ayah ${wordInfo.ayah}, Words ${wordInfo.wordfrom} to ${wordInfo.wordfrom}")

        }

        return result
    }

    fun extractSentenceAndTranslationFromIndices(
        corpus: List<CorpusEntity>,
        wordInfo: NegationEnt,
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
                "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|${wordInfo.wordto}|$startIndex|$endIndex|$extractedSentence|$extractedTranslation|${wordInfo.type}"
            result.add(dataString)
        } else {
            // Handle the case when startIndex or endIndex is not found
            result.add("Error: Couldn't find indices for Surah ${wordInfo.surahid}, Ayah ${wordInfo.ayahid}, Words ${wordInfo.wordfrom} to ${wordInfo.wordto}")
        }


        return result
    }

    fun extractSentencesFromIndexdata(
        corpus: List<CorpusEntity>,
        info: NegationEnt,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()


        // Extract Arabic sentence based on startindex and endindex
        val extractedSentence = if (info.startindex >= 0 && info.endindex <= quranText.length) {
            quranText.substring(info.startindex, info.endindex).trim()
        } else {
            "Invalid index range"
        }

        // Extract translation using wordfrom and wordnoto
        val translationBuilder = StringBuilder()
        for (entry in corpus) {
            if (entry.wordno in info.wordfrom..info.wordto) {
                // Assuming we're extracting the English translation (you can replace 'en' with another language field if needed)
                translationBuilder.append("${entry.en} ").append(" ")
            }
        }
        val extractedTranslation = translationBuilder.toString().trim()

        // Format the result string
        val dataString =
            "${info.surahid}|${info.ayahid}|${info.wordfrom}|${info.wordto}|${info.startindex}|${info.endindex}|$extractedSentence|$extractedTranslation"
        result.add(dataString)


        return result
    }

    /*




5. فُعَ

6. فِعْلَةٌ

    Example: صَحِيفَةٌ (newspaper) → صُحُفٌ (newspapers)
    Example: سَفِينَةٌ (ship) → سُفُنٌ (ships)

7. فِعْلٌ

    Example: قِطٌّ (cat) → قِطَطٌ (cats)
    Example: طِفْلٌ (child) → أَطْفَالٌ (children)

8. فُعُولَةٌ

    Example: كَفِيفٌ (blind) → كُفُوفٌ (blind people)
    Example: سَرِيرٌ (bed) → سُرُرٌ (beds)

9. فَعَالِلُ

    Example: مِفْتَاحٌ (key) → مَفَاتِيحٌ (keys)
    Example: مَسْجِدٌ (mosque) → مَسَاجِدٌ (mosques)

10. فَعَائِلُ

    Example: عَجُوزٌ (old woman) → عَجَائِزُ (old women)
    Example: طَائِرَةٌ (airplane) → طَائِرَاتٌ (airplanes)

11. فَعَالِيلُ

    Example: طَالِبٌ (student) → طُلَّابٌ (students)
    Example: تَاجِرٌ (merchant) → تُجَّارٌ (merchants)

12. فَعَالٌ

    Example: كَاتِبٌ (writer) → كُتَّابٌ (writers)
    Example: سَالِمٌ (safe) → سُلَامٌ (safe ones)

13. أَفْعِلَةٌ

    Example: طَرِيقَةٌ (method) → طَرَائِقُ (methods)
    Example: قَصِيدَةٌ (poem) → قَصَائِدُ (poems)

14. فَعَائِلَةٌ

    Example: قَرِيبٌ (relative) → أَقَارِبُ (relatives)
    Example: حَفِيدٌ (grandchild) → أَحْفَادٌ (grandchildren)

15. مَفَاعِيلُ

    Example: مَلْجَأٌ (shelter) → مَلَاجِئُ (shelters)
    Example: مَكْتَبٌ (office) → مَكَاتِبٌ (offices)

16. فَعَلَةٌ

    Example: طَرِيقٌ (path) → طُرُقَةٌ (paths)
    Example: مَلَكٌ (angel) → مَلَائِكَةٌ (angels)

17. أَفْعُلٌ

    Example: شَيْءٌ (thing) → أَشْيَاءٌ (things)
    Example: نَبِيٌّ (prophet) → أَنْبِيَاءٌ (prophets)

18. أَفْعِيلَةٌ

    Example: جَدِيدٌ (new) → جُدُدٌ (new ones)
    Example: سَرِيعٌ (fast) → سُرْعَانٌ (fast ones)

19. فَعْلَانُ

    Example: غَضْبَانُ (angry) → غُضْبَانُ (angry people)
    Example: عَطْشَانُ (thirsty) → عُطْشَانُ (thirsty people)

20. فِعْلَانٌ

    Example: سَبَاقٌ (race) → سِبَاقٌ (races)
    Example: نَحْلٌ (bee) → نَحْلَانٌ (bees)
     */


    private fun mainLoopFromIndexExtraction() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

        val allLamNegativeSenteces = ArrayList<List<String>>()


        //  val corpus = mainViewModel.getCorpusEntityFilterSurah(1)
        //   val quran = mainViewModel.getquranbySUrah(i)
        val utils = Utils(this)
        // val wordino = utils.getExpInMaIllaNegationall()
        val wordino = utils.getMudhafALl()
        //   val wordino=  utils.getfutureall()
        //  val wordino= utils.getpresentall()
        //   val wordino=mainViewModel.getLamMudharyNegationAll()
        for (s in wordino.indices) {
            val ss = wordino[s]
            val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                ss.surah, ss.ayah
            )
                    as ArrayList<CorpusEntity>
            val quran = mainViewModel.getsurahayahVerses(ss.surah, ss.ayah)

            val lamNegationDataList = extractSentenceAndTranslationFromSavedIndexes(
                corpusEntity,
                ss,
                quran.value!![0].qurantext
            )
            //    val lamNegationDataList =              extractSentencesFromIndexdata(corpusEntity,ss, quran.value!![0].qurantext)
            // val lamNegationDataList=         maaPastTenceNegation(corpusEntity, quran.value!![s].qurantext)
            //    val lamNegationDataList =                setPresentTenceNegationwithLA(corpusEntity, quran.value!![s].qurantext)
            //    val lamNegationDataList=         setMaaNegationPresent(corpusEntity, quran.value!![s].qurantext)
            // val lamNegationDataList=         setLunNegation(corpusEntity, quran.value!![s].qurantext)
            //    val lamNegationDataList=         setLaaNegationPresent(corpusEntity, quran.value!![s].qurantext)

            // val lamNegationDataList=         setJumlaIsmiyaNegationMaaLaysa(corpusEntity, quran.value!![s].qurantext)
            // val lamNegationDataList =                extractInMaIllaSentences(corpusEntity, quran.value!![s].qurantext)


            if (lamNegationDataList.isNotEmpty()) {
                allLamNegativeSenteces.add(lamNegationDataList)
                //  allLamNegativeSenteces.add(ExtractedSentence)
            }
            println(quran.value!![0].ayah)
        }


        val fileName = "newmudhafwithsentences.csv"
        writeNegationDataToFile(context!!, allLamNegativeSenteces, fileName)
    }

    private fun extractExpNegationSentences() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

        val allLamNegativeSenteces = ArrayList<List<String>>()
        val utils = Utils(this)
        val explist = utils.getexp("EXP", "RES")

        for (exp in explist) {

            val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                exp.surah, exp.ayah
            ) as ArrayList<CorpusEntity>
            val quran = mainViewModel.getsurahayahVerseslist(exp.surah, exp.ayah)


            // val lamNegationDataList =                extractInMaIllaPositiveSentences(corpusEntity, quran.value!!.get(0).qurantext)


            val lamNegationDataList =
                extractInMaIllaNegativeSentences(corpusEntity, quran.value!!.get(0).qurantext)


            if (lamNegationDataList.isNotEmpty()) {
                allLamNegativeSenteces.add(lamNegationDataList)
                //  allLamNegativeSenteces.add(ExtractedSentence)
            }

        }


        val fileName = "illanegativesentence.csv"
        writeNegationDataToFile(context!!, allLamNegativeSenteces, fileName)
    }

    private fun mainLoopforIndexEXTRACTION() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

        val allLamNegativeSenteces = ArrayList<List<String>>()
        // val allLamNegativeSenteces =                             ArrayList<List<Pair<String, String>>>()


        for (i in 1..114) {
            val corpus = mainViewModel.getCorpusEntityFilterSurah(1)
            val quran = mainViewModel.getquranbySUrah(i)
            for (s in quran.value!!.indices) {

                val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                    i, quran.value!![s].ayah
                ) as ArrayList<CorpusEntity>
                // val lamNegationDataList = collectBrokenPlurals(corpusEntity)
                // val lamNegationDataList =             setPresentTenceNegationwithLA(corpusEntity, quran.value!![s].qurantext)//GOOD


                //    val lamNegationDataList=         setMaaNegationPresent(corpusEntity, quran.value!![s].qurantext)//good
                // val lamNegationDataList=         setLunNegation(corpusEntity, quran.value!![s].qurantext)//good


                //  val lamNegationDataList =      setLamNegation(corpusEntity, quran.value!![s].qurantext)good
                //   val lamNegationDataList =     maaPastTenceNegation(corpusEntity, quran.value!![s].qurantext)


                // val lamNegationDataList=         setJumlaIsmiyaNegationMaaLaysa(corpusEntity, quran.value!![s].qurantext)

                // val lamNegationDataList =                extractInMaIllaSentences(corpusEntity, quran.value!![s].qurantext)
                val lamNegationDataList =  QuranViewUtils.extractMudafMudafIlaih(corpusEntity, quran.value!![s].qurantext)
                /*    val list = extractMousufSifa(corpusEntity,quran.value!![s].qurantext)//working

                    if (list.isNotEmpty()) {
                        allLamNegativeSenteces.add(list)
                    }*/
                if (lamNegationDataList.isNotEmpty()) {
                    allLamNegativeSenteces.add(lamNegationDataList)
                    //  allLamNegativeSenteces.add(ExtractedSentence)
                }

                println(quran.value!![s].ayah)
            }

        }
        val fileName = "checkecingtanweenmudhaf.csv"
        writeNegationDataToFile(context!!, allLamNegativeSenteces, fileName)
    }







    // Function to remove Arabic vowels and diacritics, leaving only consonants

    // Function to check for broken plural pattern by replacing consonants










    private fun extractLaNafiya() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

        val allAbsoluteNegationData = ArrayList<List<String>>()


        for (i in 2..114) {
            val corpus = mainViewModel.getCorpusEntityFilterSurah(1)
            val quran = mainViewModel.getquranbySUrah(i)
            for (s in quran.value!!.indices) {

                val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                    i, quran.value!![s].ayah
                ) as ArrayList<CorpusEntity>


                val absoluteNegationDataList =
                    setAbsoluteNegations(corpusEntity, quran.value!![s].qurantext)
                if (absoluteNegationDataList.isNotEmpty()) {
                    allAbsoluteNegationData.add(absoluteNegationDataList)
                }
                println(quran.value!![s].ayah)
            }

        }
        val fileName = "absolute_negation_data.txt"
        writeNegationDataToFile(context!!, allAbsoluteNegationData, fileName)
    }

    private fun extractLaM() {
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]

        val allNegationData = ArrayList<List<String>>()


        for (i in 2..114) {
            val corpus = mainViewModel.getCorpusEntityFilterSurah(1)
            val quran = mainViewModel.getquranbySUrah(i)
            for (s in quran.value!!.indices) {

                val corpusEntity = mainViewModel.getCorpusEntityFilterSurahAya(
                    i, quran.value!![s].ayah
                ) as ArrayList<CorpusEntity>


                val absoluteNegationDataList =
                    setAbsoluteNegations(corpusEntity, quran.value!![s].qurantext)
                if (absoluteNegationDataList.isNotEmpty()) {
                    allNegationData.add(absoluteNegationDataList)
                }
                println(quran.value!![s].ayah)
            }

        }
        val fileName = "lam_negation_data.txt"
        writeNegationDataToFile(context!!, allNegationData, fileName)
    }

    @OptIn(UnstableApi::class)
    fun writeNegationDataToFile(
        context: Context, allAbsoluteNegationData: ArrayList<List<String>>, fileName: String
    ) {
        try {
            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)

            for (absoluteNegationDataList in allAbsoluteNegationData) {
                for (dataString in absoluteNegationDataList) {
                    outputStreamWriter.write(dataString + "\n") // Write each data string to a new line
                }
            }

            outputStreamWriter.close()
        } catch (e: Exception) {
            Log.e("FileIO", "Error writing to file: ${e.message}")
        }
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
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَنْ" || previousEntry.araone == "لَن" || previousEntry.araone == "لَّن"))
                    ) {
                        // Negation condition found, skip this case
                        negationFound = true
                        break
                    }
                }

                // If no negation was found in the previous entries, proceed with extraction
                if (!negationFound) {
                    val sentenceBuilder = StringBuilder()

                    // Capture 3 words before "إِلَّا"
                    val startWordNo =
                        corpus[maxOf(0, i - 3)].wordno // Get up to 3 words before "إِلَّا"
                    for (k in maxOf(0, i - 3) until i) {
                        val word =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        sentenceBuilder.append(word).append(" ")
                    }

                    // Capture "إِلَّا" and 2 words after "إِلَّا"
                    var endWordNo = startWordNo
                    for (k in i until minOf(
                        i + 3,
                        corpus.size
                    )) { // i is where "إِلَّا" is, capture 2 more after it
                        val word =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        sentenceBuilder.append(word).append(" ")

                        endWordNo = corpus[k].wordno // Update the end word number
                    }

                    val fullSentenceStr = sentenceBuilder.toString().trim()

                    // Now find the combined sentence in the spannableVerse
                    val sentenceStartIndex = spannableVerse.indexOf(fullSentenceStr)
                    val sentenceEndIndex =
                        if (sentenceStartIndex != -1) sentenceStartIndex + fullSentenceStr.length else -1

                    // Check if this sentence is already added to avoid duplicates
                    if (sentenceStartIndex != -1 && !uniqueSentences.contains(fullSentenceStr)) {
                        val dataString =
                            "${entry.surah}|${entry.ayah}|$startWordNo|$endWordNo|$sentenceStartIndex|$sentenceEndIndex|$fullSentenceStr"
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



    fun extractInMaIllaNegativeSentences(
        corpus: List<CorpusEntity>,
        spannableVerse: String
    ): List<String> {
        val extractedSentences = mutableListOf<String>()
        val uniqueSentences = mutableSetOf<String>() // Set to track unique sentences
        var eligibleNegationCount = 0 // To track valid "إِن" occurrences

        for (i in corpus.indices) {
            val entry = corpus[i]
            if (entry.surah == 74 && entry.ayah == 31) {
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


    @OptIn(UnstableApi::class)
    fun extractInMaIllaSentencess(
        corpus: List<CorpusEntity>,
        spannableVerse: String
    ): List<String> {

        var expindex = 0

        var nextword = ""
        var nexttonextword = ""
        var phraseStartIndex = 0
        var phraseEndIndex = 0
        val negativeSentences = mutableListOf<String>() // List to store the data

        var startIndex = 0
        if (corpus == null) return negativeSentences // Handle null corpus

        for (i in corpus.indices) {
            var expwordno = 0
            var lastWord = 0
            var targetWordno = 0
            val entry = corpus[i]
            val expFound =
                (entry.tagone == "EXP" && (entry.araone == "إِلَّا" || entry.araone == "إِلَّآ"))
            var expindex = spannableVerse.indexOf("إِلَّا")
            val targetStart = listOf("مَا", "مَّا", "مَآ", "وَمَا", "فَمَا", "إِن", "وَإِن")

            if (entry.surah == 68 && entry.ayah == 52) {
                println("check")
            }
            if (expFound) {
                expwordno = entry.wordno

                var targetIndex = -1 // Store the index of the target word
                for (j in i - 1 downTo 0) {
                    val previousEntry = corpus[j]

                    if ((previousEntry.tagone == "NEG") || (previousEntry.tagtwo == "NEG")) {
                        targetIndex = j
                        targetWordno = previousEntry.wordno
                        break
                    }

                }
                if (targetIndex != -1) {
                    if (corpus[targetIndex].tagtwo == "NEG" && (corpus[targetIndex].tagtwo == "إِن")

                        || (corpus[targetIndex].tagone == "NEG" && (corpus[targetIndex].araone == "إِن"))
                    ) {

                        startIndex = -1
                        val targetWords = listOf("إِن", "وَإِن")
                        //   val targetWords = listOf("مَا", "مَّا", "مَآ", "وَمَا", "فَمَا", "إِن", "وَإِن")
                        // )//, "وَمَا", "فَمَا", "وَلَمَّا")
                        val occurrences =
                            findWordOccurrencesArabic(spannableVerse.toString(), targetWords)
                        // Find the start index of the NEG word
                        for ((wordNo, index) in occurrences) {
                            if (targetWordno == wordNo) {

                                startIndex = index
                                break
                            }

                        }
                        if (startIndex == -1 || startIndex > 0) {
                            val targetWords =
                                listOf("إِلَّا", "إِلَّآ")//, "وَمَا", "فَمَا", "وَلَمَّا")
                            val occurrences =
                                findWordOccurrencesArabic(spannableVerse.toString(), targetWords)
                            for ((wordNo, index) in occurrences) {
                                if (corpus[i].wordno == wordNo) {

                                    expindex = index
                                    break
                                }

                            }


                        }
                    }
                }
                if (targetIndex != -1 && startIndex > 0) {
                    var isnextWordNoun = false
                    var inNexttoNextWordNoun = false
                    val indexOf = spannableVerse.indexOf(corpus[targetIndex].araone!!, targetIndex)
                    if (corpus[i].tagone == "EXP") {

                        if (corpus[i + 1].tagone == "DET" || corpus[i + 1].tagone == "N" || corpus[i + 1].tagone == "ADJ" || corpus[i + 1].tagone == "PN") {

                            nextword =
                                corpus[i + 1].araone + corpus[i + 1].aratwo + corpus[i + 1].arathree + corpus[i + 1].arafour + corpus[i + 1].arafive
                            isnextWordNoun = true
                            lastWord = corpus[i + 1].wordno
                        }
                        if (i + 2 < corpus.size) {
                            if (corpus[i + 2].tagone == "DET" || corpus[i + 2].tagone == "N" || corpus[i + 2].tagone == "ADJ" || corpus[i + 2].tagone == "PN") {
                                nexttonextword =
                                    corpus[i + 2].araone + corpus[i + 2].aratwo + corpus[i + 2].arathree + corpus[i + 2].arafour + corpus[i + 2].arafive
                                inNexttoNextWordNoun = true
                                lastWord = corpus[i + 1].wordno
                            }
                        }
                        if (isnextWordNoun && inNexttoNextWordNoun) {

                            phraseStartIndex =
                                spannableVerse.indexOf(nexttonextword.toString(), expindex)
                            phraseEndIndex =
                                phraseStartIndex + nextword.length //+ nexttonextword.length
                            lastWord = corpus[i + 2].wordno
                        } else if (isnextWordNoun) {
                            phraseStartIndex =
                                spannableVerse.indexOf(nextword.toString(), expindex)
                            phraseEndIndex = phraseStartIndex + nextword.length


                        } else {
                            startIndex = -1
                            phraseEndIndex = -1
                        }
                        // Apply underline span
                        if (startIndex != -1 && phraseStartIndex != -1) {
                            try {
                                val str = spannableVerse.subSequence(startIndex, phraseEndIndex)
                            } catch (e: StringIndexOutOfBoundsException) {
                                phraseEndIndex = phraseEndIndex - 1
                                Log.d(corpus[i].surah.toString(), corpus[i].ayah.toString())
                                val str = spannableVerse.subSequence(startIndex, phraseEndIndex)
                            }
                            Log.d("spanaable", "str")/*    spannableVerse.setSpan(
                                    UnderlineSpan(),
                                    startIndex,
                                    phraseEndIndex,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )*/
                            val type = "inmaillasentece"
                            // Add data to the list
                            val dataString =
                                "${entry.surah}|${entry.ayah}|${targetWordno}| ${lastWord}|$startIndex|$phraseEndIndex|$type|"
                            negativeSentences.add(dataString)
                        }
                    }


                }


            }


        }
        if (negativeSentences.isNotEmpty()) {
            print(negativeSentences)
        }


        return negativeSentences
    }


    fun setJumlaIsmiyaNegationMaaLaysa(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val negativeSentences = mutableListOf<String>() // List to store the data
        var currentSentence = mutableListOf<CorpusEntity>()

        if (corpus == null) return negativeSentences // Handle null corpus

        for (i in corpus.indices) {
            val entry = corpus[i]
            //  val lamFound = entry.tagone == "NEG" && entry.araone == "لَّا" || entry.araone=="لَا" || entry.araone=="لَآ"
            val maaFound =
                entry.tagone == "NEG" && (entry.araone == "مَا" || entry.araone == "مَّا" || entry.araone == "مَآ")
            var lamcombination = ""

            val maafound2 =
                ((entry.tagone == "RSLT" || entry.tagone == "INTG" || entry.tagone == "CONJ" || entry.tagone == "REM" || entry.tagone == "CIRC") && (entry.aratwo == "مَا" || entry.aratwo == "مَّا" || entry.aratwo == "مَآ"))
            val maafound3 =
                ((entry.tagone == "SUBJ" || entry.tagone == "INTG" || entry.tagtwo == "SUP" || entry.tagtwo == "CONJ") && (entry.arathree == "مَا" || entry.arathree == "مَّا" || entry.arathree == "مَآ"))
            var isIndictiveVerb = false
            var isNominativeNound = false
            if (i + 1 < corpus.size) {
                isNominativeNound =
                    corpus[i + 1].detailsone!!.contains("NOM") && (corpus[i + 1].tagone == "N" || corpus[1 + 1].tagone == "ADJ" || corpus[i + 1].tagone == "PN")
                isIndictiveVerb =
                    corpus[i + 1].detailsone?.contains("IMPF") == true && !(corpus[i + 1].detailsone?.contains(
                        "MOOD:JUS"
                    ) == true || corpus[i + 1].detailsone?.contains("MOOD:SUBJ") == true)
                lamcombination = "مَا"
            }




            if ((maaFound) && isNominativeNound) {
                // val targetWords = listOf("لَآ", "لَا", "لَّا")
                val targetWords = listOf("مَا", "مَّا", "مَآ", "وَمَا", "فَمَا", "وَلَمَّا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)

                // Find the start index of the NEG word
                val startIndex =
                    occurrences.firstOrNull { (wordNo, _) -> corpus[i].wordno == wordNo }?.second
                        ?: -1

                // Build the compeleteverb
                val completeverb =
                    corpus[i + 1].araone + corpus[i + 1].aratwo + corpus[i + 1].arathree + corpus[i + 1].arafour

                val nextword = corpus[i + 1].wordno
                // Find the indices for the prepositional phrase
                val phraseStartIndex = spannableVerse.indexOf(completeverb.toString(), startIndex)
                var nextWordDetail = ""
                if (i + 2 < corpus.size) {
                    nextWordDetail =
                        corpus[i + 2].araone + corpus[i + 2].aratwo + corpus[i + 2].arathree + corpus[i + 2].arafour + corpus[i + 2].arafive

                } else if (i + 3 < corpus.size) {

                    nextWordDetail =
                        corpus[i + 3].araone + corpus[i + 3].aratwo + corpus[i + 3].arathree + corpus[i + 3].arafour + corpus[i + 3].arafive
                }
                val phraseEndIndex =
                    phraseStartIndex + completeverb.length + nextWordDetail.length

                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {
                    spannableVerse.subSequence(startIndex, phraseEndIndex)/*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/

                    // Add data to the list
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}| ${nextword}|$startIndex|$phraseEndIndex|$lamcombination"
                    negativeSentences.add(dataString)
                }
            }
        }

        return negativeSentences // Return the list of data strings
    }

    fun setMaaNegationPresent(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val negativeSentences = mutableListOf<String>() // List to store the data
        var currentSentence = mutableListOf<CorpusEntity>()

        if (corpus == null) return negativeSentences // Handle null corpus

        for (i in corpus.indices) {
            val entry = corpus[i]
            //  val lamFound = entry.tagone == "NEG" && entry.araone == "لَّا" || entry.araone=="لَا" || entry.araone=="لَآ"
            val maaFound =
                entry.tagone == "NEG" && (entry.araone == "مَا" || entry.araone == "مَّا" || entry.araone == "مَآ")
            var lamcombination = ""

            val maafound2 =
                (entry.aratwo == "مَا" || entry.aratwo == "مَّا" || entry.aratwo == "مَآ")
            val maafound3 =
                (entry.arathree == "مَا" || entry.arathree == "مَّا" || entry.arathree == "مَآ")
            var isIndictiveVerb = false
            var isImperfect = false
            var containsmood = false
            var onlyimperfect = false
            if (i + 1 < corpus.size) {
                isImperfect = corpus[i + 1].detailsone!!.contains("IMPF")
                containsmood = corpus[i + 1].detailsone!!.contains("MOOD")
                onlyimperfect = false
                if (containsmood) {
                    onlyimperfect =
                        !(corpus[i + 1].detailsone?.contains("MOOD:JUS") == true || entry.detailsone?.contains(
                            "MOOD:SUBJ"
                        ) == true)
                }
            }
            if (i + 1 < corpus.size) {

                isIndictiveVerb =
                    corpus[i + 1].detailsone?.contains("IMPF") == true && !(corpus[i + 1].detailsone?.contains(
                        "MOOD:JUS"
                    ) == true || corpus[i + 1].detailsone?.contains("MOOD:SUBJ") == true)
                lamcombination = "مَا"
            }

            if (maaFound || maafound2 || maafound3) {
                println("check")
            }


            if ((maaFound || maafound2 || maafound3) && (isImperfect && !onlyimperfect)) {
                // val targetWords = listOf("لَآ", "لَا", "لَّا")
                val targetWords = listOf("مَا", "مَّا", "مَآ", "وَمَا", "فَمَا", "وَلَمَّا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)

                // Find the start index of the NEG word
                val startIndex =
                    occurrences.firstOrNull { (wordNo, _) -> corpus[i].wordno == wordNo }?.second
                        ?: -1

                // Build the compeleteverb
                val completeverb =
                    corpus[i + 1].araone + corpus[i + 1].aratwo + corpus[i + 1].arathree + corpus[i + 1].arafour

                val nextword = corpus[i + 1].wordno
                // Find the indices for the prepositional phrase
                val phraseStartIndex = spannableVerse.indexOf(completeverb.toString(), startIndex)
                var nextWordDetail = ""
                if (i + 2 < corpus.size) {
                    nextWordDetail =
                        corpus[i + 2].araone + corpus[i + 2].aratwo + corpus[i + 2].arathree + corpus[i + 2].arafour + corpus[i + 2].arafive

                } else if (i + 3 < corpus.size) {

                    nextWordDetail =
                        corpus[i + 3].araone + corpus[i + 3].aratwo + corpus[i + 3].arathree + corpus[i + 3].arafour + corpus[i + 3].arafive
                }
                val phraseEndIndex =
                    phraseStartIndex + completeverb.length + nextWordDetail.length
                val translationBuilder = StringBuilder()
                //      if (entry.wordno in info.wordfrom..info.wordnoto) {
                val startword = entry.wordno
                for (entry in corpus) {
                    if (entry.wordno in startword..nextword) {
                        // Assuming we're extracting the English translation (you can replace 'en' with another language field if needed)
                        translationBuilder.append("${entry.en} ").append(" ")
                    }
                }
                val extractedTranslation = translationBuilder.toString().trim()
                val type = "present"
                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {
                    spannableVerse.subSequence(startIndex, phraseEndIndex)/*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/

                    // Add data to the list
                    val sentence = spannableVerse.subSequence(startIndex, phraseEndIndex)
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}| ${nextword}|$startIndex|$phraseEndIndex|$sentence|$extractedTranslation|$type"
                    negativeSentences.add(dataString)
                }
            }
        }

        return negativeSentences // Return the list of data strings
    }

    @OptIn(UnstableApi::class)
    fun setLunNegation(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val negativeSentences = mutableListOf<String>() // List to store the data
        var currentSentence = mutableListOf<CorpusEntity>()

        if (corpus == null) return negativeSentences // Handle null corpus

        for (i in corpus.indices) {

            val entry = corpus[i]
            if (entry.surah == 2 && entry.ayah == 80) {
                println("check")
            }
            val lamFound =
                entry.tagone == "NEG" && (entry.araone == "لَّن" || entry.araone == "لَن" || entry.araone == "لَنْ")
            val maaFound = entry.tagone == "NEG" && entry.araone == "مَا"
            var lamcombination = ""

            val lamFound2 =
                ((entry.tagone == "SUB" || entry.tagone == "INTG" || entry.tagone == "CONJ" || entry.tagone == "REM" || entry.tagone == "RSLT") && (entry.aratwo == "لَّن" || entry.aratwo == "لَن" || entry.aratwo == "لَنْ"))
            //lun no tagthree

            var subjunctiveVerb = false
            if (lamFound) {

                // if (i > 0 && corpus[i + 1].tagone == "V" && i < corpus.size) {
                subjunctiveVerb =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("MOOD:SUBJ")!!
                lamcombination = "لَن"

            } else if (lamFound2) {
                subjunctiveVerb =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("MOOD:SUBJ")!!
                lamcombination = entry.araone + entry.aratwo
            }


            var startIndex = -1

            if ((lamFound || lamFound2) && (subjunctiveVerb)) {
                val targetWords =
                    listOf("لَّن", "لَن", "لَنْ", "وَلَن", "أَلَّن", "فَلَن", "وَلَنْ", "فَلَنْ")
                if (corpus[i].surah == 2 && corpus[i].ayah == 24) {
                    println("check")
                }
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)

                // Find the start index of the NEG word
                for ((wordNo, index) in occurrences) {
                    if (corpus[i].wordno == wordNo) {

                        startIndex = index
                        break
                    }

                }

                // Build the compeleteverb
                val completeverb =
                    corpus[i + 1].araone + corpus[i + 1].aratwo + corpus[i + 1].arathree + corpus[i + 1].arafour

                var nextword = corpus[i + 1].wordno
                // Find the indices for the prepositional phrase
                val phraseStartIndex = spannableVerse.indexOf(completeverb.toString(), startIndex)
                var nextWordDetail = ""
                var nexttoNextWordDetail = ""
                if (i + 2 < corpus.size) {
                    nextWordDetail =
                        corpus[i + 2].araone + corpus[i + 2].aratwo + corpus[i + 2].arathree + corpus[i + 2].arafour + corpus[i + 2].arafive
                    nextword = corpus[i + 2].wordno
                } else if (i + 3 < corpus.size) {

                    nexttoNextWordDetail =
                        corpus[i + 3].araone + corpus[i + 3].aratwo + corpus[i + 3].arathree + corpus[i + 3].arafour + corpus[i + 3].arafive
                    nextword = corpus[i + 3].wordno
                }
                val phraseEndIndex =
                    phraseStartIndex + completeverb.length + nextWordDetail.length

                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {/*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/

                    // Add data to the list
                    val sentence = spannableVerse.subSequence(startIndex, phraseEndIndex).toString()
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}| ${nextword}|$startIndex|$phraseEndIndex|$sentence"
                    negativeSentences.add(dataString)
                    Log.d(
                        "check",
                        spannableVerse.subSequence(startIndex, phraseEndIndex).toString()
                    )


                }
            }
        }

        return negativeSentences // Return the list of data strings
    }

    fun setLamNegation(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val negativeSentences = mutableListOf<String>() // List to store the data
        var currentSentence = mutableListOf<CorpusEntity>()

        if (corpus == null) return negativeSentences // Handle null corpus

        for (i in corpus.indices) {
            val entry = corpus[i]
            val lamFound =
                (entry.tagone == "NEG") && (entry.araone == "لَمْ" || entry.araone == "لَّمْ" || entry.araone == "لَمَّا")
            val maaFound = entry.tagone == "NEG" && entry.araone == "مَا"
            var lamcombination = ""

            val lamFound2 =
                ((entry.tagone == "INTG" || entry.tagone == "CONJ" || entry.tagone == "REM" || entry.tagone == "CIRC") && entry.aratwo == "لَمْ")
            val lamFound3 =
                ((entry.tagone == "INTG" && (entry.tagtwo == "SUP" || entry.tagtwo == "CONJ") && entry.arathree == "لَمْ"))
            var jussinvVerbfound = false
            if (lamFound) {

                // if (i > 0 && corpus[i + 1].tagone == "V" && i < corpus.size) {
                jussinvVerbfound =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("MOOD:JUS")!!
                lamcombination = "لَمْ"

            } else if (lamFound2) {
                jussinvVerbfound =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("MOOD:JUS")!!
                lamcombination = entry.araone + entry.aratwo
            } else if (lamFound3) {

                jussinvVerbfound =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("MOOD:JUS")!!
                lamcombination = entry.araone + entry.aratwo + entry.arathree
            }





            if (lamFound || jussinvVerbfound || jussinvVerbfound) {
                val targetWords = listOf("لَمْ", "أَلَمْ", "وَلَمْ", "فَلَمْ", "أَوَلَمْ")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)

                // Find the start index of the NEG word
                val startIndex =
                    occurrences.firstOrNull { (wordNo, _) -> corpus[i].wordno == wordNo }?.second
                        ?: -1

                // Build the compeleteverb
                val completeverb =
                    corpus[i + 1].araone + corpus[i + 1].aratwo + corpus[i + 1].arathree + corpus[i + 1].arafour

                val nextword = corpus[i + 1].wordno
                // Find the indices for the prepositional phrase
                val phraseStartIndex = spannableVerse.indexOf(completeverb.toString(), startIndex)
                var nextWordDetail = ""
                if (i + 2 < corpus.size) {
                    nextWordDetail =
                        corpus[i + 2].araone + corpus[i + 2].aratwo + corpus[i + 2].arathree + corpus[i + 2].arafour + corpus[i + 2].arafive
                }

                /*  } else if (i + 3 < corpus.size) {

                      nextWordDetail =
                          corpus[i + 3].araone + corpus[i + 3].aratwo + corpus[i + 3].arathree + corpus[i + 3].arafour + corpus[i + 3].arafive
                  }*/
                val phraseEndIndex =
                    phraseStartIndex + completeverb.length + nextWordDetail.length
                val translationBuilder = StringBuilder()
                val firstword = entry.wordno
                for (entry in corpus) {
                    if (entry.wordno in firstword..nextword) {
                        // Assuming we're extracting the English translation (you can replace 'en' with another language field if needed)
                        translationBuilder.append("${entry.en} ").append(" ")
                    }
                }
                val extractedTranslation = translationBuilder.toString().trim()

                val type = "past"
                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {/*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/

                    // Add data to the list
                    val sentence = spannableVerse.subSequence(startIndex, phraseEndIndex)
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${firstword}| ${nextword}|$startIndex|$phraseEndIndex|$sentence|$extractedTranslation|$type"
                    negativeSentences.add(dataString)
                }
            }
        }

        return negativeSentences // Return the list of data strings
    }

    fun maaPastTenceNegation(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val negativeSentences = mutableListOf<String>() // List to store the data
        var currentSentence = mutableListOf<CorpusEntity>()

        if (corpus == null) return negativeSentences // Handle null corpus

        for (i in corpus.indices) {
            val entry = corpus[i]
            if (entry.surah == 3 && entry.ayah == 24 && entry.wordno == 13) {

                println(entry.wordno)
            }
            val maaFound =
                entry.tagone == "NEG" && (entry.araone == "مَا" || entry.araone == "مَّا" || entry.araone == "مَآ")
            var lamcombination = ""

            // val maaFound2 =      (( entry.tagone=="CONJ"    || entry.tagone=="REM"   || entry.tagone=="CIRC") && entry.tagtwo=="NEG" && entry.aratwo=="مَا" || entry.aratwo=="مَّا")
            //    val maaFound3 =      ((entry.tagone=="INTG"    || entry.tagtwo=="SUP"  ) && entry.tagthree=="NEG" && entry.araone=="مَا" || entry.araone=="مَّا"|| entry.araone=="مَآ")

            val maaFound2 = (entry.tagone in listOf(
                "CONJ", "REM", "CIRC"
            ) && entry.tagtwo == "NEG" && entry.aratwo in listOf("مَا", "مَّا"))
            val maaFound3 = (entry.tagone in listOf(
                "INTG", "SUP"
            ) && entry.tagthree == "NEG" && entry.araone in listOf("مَا", "مَّا", "مَآ"))
            var jussinvVerbfound = false
            if (maaFound) {

                // if (i > 0 && corpus[i + 1].tagone == "V" && i < corpus.size) {
                jussinvVerbfound =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("PERF")!!
                lamcombination = "مَا"

            } else if (maaFound2) {
                jussinvVerbfound =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("PERF")!!
                lamcombination = entry.araone + entry.aratwo
            } else if (maaFound3) {

                jussinvVerbfound =
                    corpus[i + 1].tagone == "V" && corpus[i + 1].detailsone?.contains("PERF")!!
                lamcombination = entry.araone + entry.aratwo + entry.arathree
            }





            if (maaFound || jussinvVerbfound || jussinvVerbfound) {
                // val targetWords = listOf("لَمْ","أَلَمْ","وَلَمْ","فَلَمْ", "أَوَلَمْ","أَفَلَ")
                val targetWords = listOf("مَا", "مَّا", "مَآ", "وَمَا", "فَمَا", "وَلَمَّا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)

                // Find the start index of the NEG word
                val startIndex =
                    occurrences.firstOrNull { (wordNo, _) -> corpus[i].wordno == wordNo }?.second
                        ?: -1

                // Build the compeleteverb
                val completeverb =
                    corpus[i + 1].araone + corpus[i + 1].aratwo + corpus[i + 1].arathree + corpus[i + 1].arafour

                val nextword = corpus[i + 1].wordno
                // Find the indices for the prepositional phrase
                val phraseStartIndex = spannableVerse.indexOf(completeverb.toString(), startIndex)
                var nextWordDetail = ""
                if (i + 2 < corpus.size) {
                    nextWordDetail =
                        corpus[i + 2].araone + corpus[i + 2].aratwo + corpus[i + 2].arathree + corpus[i + 2].arafour + corpus[i + 2].arafive


                }
                val phraseEndIndex =
                    phraseStartIndex + completeverb.length + nextWordDetail.length

                val translationBuilder = StringBuilder()
                val firstword = corpus[i].wordno
                for (entry in corpus) {
                    if (entry.wordno in firstword..nextword) {
                        // Assuming we're extracting the English translation (you can replace 'en' with another language field if needed)
                        translationBuilder.append("${entry.en} ").append(" ")
                    }
                }
                val extractedTranslation = translationBuilder.toString().trim()

                val type = "past"
                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {/*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/

                    val sentence = spannableVerse.subSequence(startIndex, phraseEndIndex)
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${firstword}| ${nextword}|$startIndex|$phraseEndIndex|$sentence|$extractedTranslation|$type"
                    negativeSentences.add(dataString)
                }
            }
        }

        return negativeSentences // Return the list of data strings
    }


    fun setPresentTenceNegationwithLA(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val absoluteNegationData = mutableListOf<String>() // List to store the data

        if (corpus == null) return absoluteNegationData // Handle null corpus
        val targetWordss = listOf("مَا", "مَّا", "مَآ", "وَمَا", "فَمَا", "وَلَمَّا")
        for (i in corpus.indices) {
            val word = corpus[i]
            var startIndex = 0
            if (word.surah == 2 && word.ayah == 123) {
                println(corpus[i].araone)
            }
            // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON

            val isImperfect = word.detailsone!!.contains("IMPF")
            val containsmood = word.detailsone!!.contains("MOOD")
            var onlyimperfect = false
            if (containsmood) {
                onlyimperfect =
                    !(word.detailsone?.contains("MOOD:JUS") == true || word.detailsone?.contains("MOOD:SUBJ") == true)
            }
            val isIndictiveVerb =
                word.detailsone?.contains("IMPF") == true && !(word.detailsone?.contains("MOOD:JUS") == true || word.detailsone?.contains(
                    "MOOD:SUBJ"
                ) == true)
            if (word.wordno == 10) {
                println("check")
            }
            if (word.wordno == 14) {
                println("check")
            }

            if ((isImperfect && !onlyimperfect) && i > 0 &&
                ((corpus[i - 1].tagone.equals(
                    "NEG",
                    ignoreCase = true
                ) && (corpus[i - 1].araone == "لَا" || corpus[i - 1].araone == "لَآ" || corpus[i - 1].araone == "لَّا")) ||
                        (corpus[i - 1].tagtwo.equals(
                            "neg",
                            ignoreCase = true
                        ) && (corpus[i - 1].aratwo == "لَا" || corpus[i - 1].aratwo == "لَآ" || corpus[i - 1].aratwo == "لَّا")))
            ) {

                val targetWords = listOf("لَآ", "لَا", "لَّا", "وَلَا", "فَلَا", "أَلَا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)
                for ((wordNo, index) in occurrences) {
                    if (corpus[i - 1].wordno == wordNo) {

                        startIndex = index
                        break
                    }

                }
                // Build the prepositional phrase
                var prepositionalPhrase = ""
                // if (i+1 < corpus.size) {

                prepositionalPhrase =
                    corpus[i].araone + corpus[i].aratwo + corpus[i].arathree + corpus[i].arafour + corpus[i].arafive
                //    }

                val currentword = corpus[i].wordno
                val firstword = corpus[i - 1].wordno
                // Find the indices for the prepositional phrase
                val phraseStartIndex =
                    spannableVerse.indexOf(prepositionalPhrase.toString(), startIndex)
                val phraseEndIndex = phraseStartIndex + prepositionalPhrase.length
                val translationBuilder = StringBuilder()
                for (entry in corpus) {
                    if (entry.wordno in firstword..currentword) {
                        // Assuming we're extracting the English translation (you can replace 'en' with another language field if needed)
                        translationBuilder.append("${entry.en} ").append(" ")
                    }
                }
                val extractedTranslation = translationBuilder.toString().trim()


                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {
//                    println("spannable"+spannableVerse.subSequence(startIndex,phraseEndIndex))
                    /*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/
                    val sentence = spannableVerse.subSequence(startIndex, phraseEndIndex)
                    val type = "present"
                    // Add data to the list
                    val dataString =
                        "${word.surah}|${word.ayah}|${firstword}|${currentword}|$startIndex|$phraseEndIndex|$sentence|$extractedTranslation|$type"
                    absoluteNegationData.add(dataString)
                }
            }




            if ((isImperfect && !onlyimperfect) && i > 1 &&
                ((corpus[i - 1].tagone.equals(
                    "PRON",
                    ignoreCase = true
                ) && (corpus[i - 2].araone == "لَا" || corpus[i - 2].araone == "لَآ" || corpus[i - 2].araone == "لَّا")) ||
                        (corpus[i - 2].tagtwo.equals(
                            "neg",
                            ignoreCase = true
                        ) && (corpus[i - 2].aratwo == "لَا" || corpus[i - 2].aratwo == "لَآ" || corpus[i - 2].aratwo == "لَّا")))
            ) {

                val targetWords =
                    listOf("لَآ", "لَا", "لَّا", "وَلَا", "فَلَا", "أَلَا", "فَلَآ", "أَلَّا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)
                for ((wordNo, index) in occurrences) {
                    if (corpus[i - 2].wordno == wordNo) {

                        startIndex = index
                        break
                    }

                }
                // Build the prepositional phrase
                var prepositionalPhrase = ""
                // if (i+1 < corpus.size) {

                prepositionalPhrase =
                    corpus[i].araone + corpus[i].aratwo + corpus[i].arathree + corpus[i].arafour + corpus[i].arafive
                //    }

                val currentword = corpus[i].wordno
                val firstword = corpus[i - 2].wordno


                // Find the indices for the prepositional phrase
                val phraseStartIndex =
                    spannableVerse.indexOf(prepositionalPhrase.toString(), startIndex)
                val phraseEndIndex = phraseStartIndex + prepositionalPhrase.length
                val translationBuilder = StringBuilder()
                for (entry in corpus) {
                    if (entry.wordno in firstword..currentword) {
                        // Assuming we're extracting the English translation (you can replace 'en' with another language field if needed)
                        translationBuilder.append("${entry.en} ").append(" ")
                    }
                }
                val extractedTranslation = translationBuilder.toString().trim()
                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {
//                    println("spannable"+spannableVerse.subSequence(startIndex,phraseEndIndex))
                    /*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/
                    val sentence = spannableVerse.subSequence(startIndex, phraseEndIndex)
                    val type = "present"
                    // Add data to the list
                    val dataString =
                        "${word.surah}|${word.ayah}|${firstword}|${currentword}|$startIndex|$phraseEndIndex|$sentence|$extractedTranslation|$type"
                    absoluteNegationData.add(dataString)
                }
            }
        }
        if (absoluteNegationData.size > 0) {
            println(absoluteNegationData)

        }
        return absoluteNegationData // Return the list of data strings
    }

    fun setAbsoluteNegations(
        corpus: java.util.ArrayList<CorpusEntity>?, spannableVerse: String
    ): List<String> {
        val absoluteNegationData = mutableListOf<String>() // List to store the data

        if (corpus == null) return absoluteNegationData // Handle null corpus

        for (i in corpus.indices) {
            val word = corpus[i]

            // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON
            val isAcc = word.detailsone!!.contains("ACC")
            if (isAcc && i > 0 && corpus[i - 1].tagone == "NEG" && corpus[i].tagone == "N" && i < corpus.size - 2 && corpus[i + 1].tagone == "P" // Following word is P (with or without PRON)
            ) {
                val targetWords = listOf("لَآ", "لَا", "لَّا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)

                // Find the start index of the NEG word
                val startIndex =
                    occurrences.firstOrNull { (wordNo, _) -> corpus[i - 1].wordno == wordNo }?.second
                        ?: -1

                /*  for ((wordNo, index) in occurrences) {
                      if(corpus[i-1].wordno == wordNo) {

                          startIndex = index
                          break
                      }

                  }*/

                // Build the prepositional phrase
                val prepositionalPhrase = if (corpus[i + 1].tagtwo!!.contains("PRON")) {
                    corpus[i + 1].araone + corpus[i + 1].aratwo
                } else {
                    corpus[i + 1].araone
                }

                // Find the indices for the prepositional phrase
                val phraseStartIndex =
                    spannableVerse.indexOf(prepositionalPhrase.toString(), startIndex)
                val phraseEndIndex = phraseStartIndex + prepositionalPhrase!!.length

                // Apply underline span
                if (startIndex != -1 && phraseStartIndex != -1) {/*    spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )*/

                    // Add data to the list
                    val dataString =
                        "${word.surah}|${word.ayah}|${word.wordno}|$startIndex|$phraseEndIndex"
                    absoluteNegationData.add(dataString)
                }
            }
        }

        return absoluteNegationData // Return the list of data strings
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
        val corpus = CorpusUtilityorig(this)
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

                    if (mafoolat) {
                        val chapterData = quranRepository.getChapterData(chapterno)
                        mafoolbihiwords = chapterData.mafoolbihiwords
                        jumlahaliya = chapterData.jumlahaliya
                        tameezEntList = chapterData.tammezent
                        mutlaqEntList = chapterData.mutlaqent
                        badalErabNotesEnts = chapterData.badalErabNotesEnt
                    }

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

                    //   HightLightKeyWord(allofQuran)

                    val adapter = if (!mushafview && mafoolat) {
                        FlowAyahWordAdapter(
                            false,
                            mutlaqEntList,
                            tameezEntList,
                            badalErabNotesEnts,
                            liajlihient,
                            jumlahaliya,
                            mafoolbihiwords,
                            header,
                            allofQuran,
                            corpusGroupedByAyah,
                            this@QuranGrammarAct,
                            surahArabicName,
                            isMakkiMadani,
                            listener,
                            mainViewModel,
                        )
                    } else {
                        FlowAyahWordAdapterNoMafoolat(
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
                    }
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

