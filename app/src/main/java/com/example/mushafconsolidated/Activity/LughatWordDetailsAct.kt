package com.example.mushafconsolidated.Activity


import com.example.mushafconsolidated.Activityimport.BaseActivity
import SharedPref
import Utility.ArabicLiterals
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.Constant

import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.fragments.Dictionary_frag
import com.example.utility.QuranGrammarApplication
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.sj.conjugator.fragments.FragmentIsmIsmAla
import org.sj.conjugator.fragments.FragmentIsmZarf
import org.sj.conjugator.fragments.FragmentIsmfaelIsmMafools
import org.sj.conjugator.fragments.FragmentVerb
import org.sj.conjugator.fragments.LaysaVerbFrag


@AndroidEntryPoint
class LughatWordDetailsAct : BaseActivity() {
    // Arrey of strings FOR TABS TITLES


    private var shared: SharedPreferences? = null
    data class TabInfo(val title: String, val languageKey: String? = null)
    private val dictionarytitle = arrayOf("Lane Lexicon", "Hans Weir")

    private val dictionaryTabs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans")
    )

    private val arabicwordharfnasb = arrayOf("English lughat", "Urdu Lughat", "Harf ")

    private val arabicWordHarfNasbTabs = arrayOf(
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu"),
        TabInfo("Harf ") // Consider adding a language key for consistency
    )
    private val vocabularytitles =
        arrayOf("Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat")
    private val vocabularyTabs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans"),
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu")
    )

    private val laysatitles = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "كانَ وَأخَواتُها (The كانَ family)",
        "Past Perfect",

    )
    private val thulathientitles = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "English lughat",
        "Urdu Lughat",
        "Verb Conjugaton",
        "Active/Passive PCPL",
        "N. Instrument",
        "N.Place/Time"
    )
    private val thulathiEntitlesTabs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans"),
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu"),
        TabInfo("Verb Conjugation"),
        TabInfo("Active/Passive PCPL"),
        TabInfo("N. Instrument"),
        TabInfo("N.Place/Time")
    )

    private val thulathientitlesmansub = arrayOf(
        "Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat", "Subjunctive",
        "Verb Conjugaton", "Active/Passive PCPL", "N. Instrument", "N.Place/Time"
    )
    private val thulathientitlesmansubs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans"),
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu"),
        TabInfo("Subjunctive"),
        TabInfo("Verb Conjugation"),
        TabInfo("Active/Passive PCPL"),
        TabInfo("N. Instrument"),
        TabInfo("N. Instrument"),
        TabInfo("N.Place/Time")
    )




    private val mujarradparticple = arrayOf(
        "Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat", " ",
        "Verb Conjugaton", "Active/Passive PCPL", "N. Instrument", "N.Place/Time"
    )
    private val mazeedparticpletitle = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "English lughat",
        "Urdu Lughat",
        " ",
        "Verb Conjugaton",
        "Active/Passive Participle"
    )
    private val thulathientitlesmajzoom = arrayOf(
        "Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat",
        "Jussive", "Verb Conjugaton", "Active/Passive PCPL", "N. Instrument", "N.Place/Time"
    )
    private val ismmansubtitle =
        arrayOf("Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat", "")
    private val ismmarfutitle =
        arrayOf("Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat", "")
    private val mazeedentitlesmajzoom = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "English lughat",
        "Urdu Lughat",
        "Jussive",
        "Verb Conjugaton",
        "Active/Passive Participle"
    )

    private val mazeedimperative = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "English lughat",
        "Urdu Lughat",
        "Imperative",
        "Verb Conjugaton",
        "Active/Passive Participle"
    )
    private val mujarradimperative = arrayOf(
        "Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat", "Imperative",
        "Verb Conjugaton", "Active/Passive PCPL", "N. Instrument", "N.Place/Time"
    )
    private val mazeedentitlesmansub = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "English lughat",
        "Urdu Lughat",
        "Subjunctive",
        "Verb Conjugaton",
        "Active/Passive Participle"
    )
    private val getMazeedentitles = arrayOf(
        "Lane Lexicon",
        "Hans Weir",
        "English lughat",
        "Urdu Lughat",
        "Verb Conjugaton",
        "Active/Passive Participle"
    )
    private var languages = Array(10) { "" } // Initialize with empty strings
   // val defaultLanguages = arrayOf("lanes", "hans", "english", "urdu")
    var dataBundle: Bundle? = null
    private var isProperNoun=false
    private var isUnaugmentedWazan = false
    private var isAugmentedWazan = false
    private var isnoconjugation = false
    private var isonlyarabicword = false
    private var isVerbMajzoom = false
    private var isVerbMansub = false
    private var isVerbEmpha = false
    private var isnoun = false
    private var isHarf = false
    private var nouncase: String? = null
    private var isIsmMarfu = false
    private var isIsmMansub = false
    private var isIsmMajroor = false
    private var isimperative = false
    private var isdictionary = false
    private var isVerbMarfu = false
    private var isparticple = false
    private var ismujarrad = false
    private var isharfnasab=false
    private var isdem=false
    private var isrelative=false
    private var isShart=false
    private var isprep=false
    private var isLaysa=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtabs)
        val sharedpref = SharedPref(this@LughatWordDetailsAct)
        val fm = supportFragmentManager
        val sa = ViewStateAdapter(fm, lifecycle)
        val viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = sa
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        originalSetup(tabLayout,viewPager)
        shared = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            QuranGrammarApplication.context!!
        )


        // Now we have tabs, NOTE: I am hardcoding the order, you'll want to do something smarter
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        // And now we have tabs that, when clicked, navigate to the correct page
    }

    private fun originalSetup(tabLayout: TabLayout, viewPager: ViewPager2) {

        dataBundle = Bundle()

        val bundle = intent.extras
        if (bundle != null) {
            var conjugationRoot = bundle.getString(Constant.QURAN_VERB_ROOT) ?: ""
            val vocabRoot = bundle.getString(Constant.QURAN_VERB_ROOT) ?: ""
            val verbFormThulathi = bundle.getString(Constant.QURAN_VERB_WAZAN) ?: ""

            // Cleaning the verb root
            val ss = conjugationRoot.replace("[\\[\\]]".toRegex(), "")
            val verbRoot = ss.replace("[,;\\s]".toRegex(), "")
            if(verbRoot.equals("ليس")){
                isLaysa=true
            }

            // Replace 'ل' (ALIF) with hamza if necessary
            val starts = conjugationRoot.indexOf(ArabicLiterals.LALIF)
            if (starts != -1) {
                conjugationRoot = conjugationRoot.replace(ArabicLiterals.LALIF, "ء")
            }

            // Check for noun case
            nouncase = bundle.getString(Constant.NOUNCASE)
            isProperNoun = bundle.getBoolean(Constant.PROPERNOUN)

            isnoun = nouncase != null
            when (nouncase) {
                "NOM" -> isIsmMarfu = true
                "ACC" -> isIsmMansub = true
                "GEN" -> isIsmMajroor = true
            }

            val verbMood = bundle.getString(Constant.VERBMOOD) ?: ""
            val verbType = bundle.getString(Constant.VERBTYPE) ?: ""
            val arabicWord = bundle.getString("arabicword") ?: ""

            isdictionary = bundle.getBoolean("dictionary", false)
            isimperative = bundle.getBoolean(Constant.IMPERATIVE, false)
            isparticple = bundle.getBoolean(Constant.ISPARTICPLE, false)
            isharfnasab = bundle.getBoolean(Constant.ACCUSATIVE, false)
            isdem = bundle.getBoolean(Constant.DEMONSTRATIVE, false)
            isrelative = bundle.getBoolean(Constant.RELATIVE, false)
            isShart = bundle.getBoolean(Constant.CONDITIONAL, false)
            isprep = bundle.getBoolean(Constant.PREPOSITION, false)

            isHarf = isShart || isrelative || isharfnasab || isprep || isdem

            // Verb mood conditions
            when (verbMood) {
                "Jussive" -> isVerbMajzoom = true
                "Subjunctive" -> isVerbMansub = true
                "Indicative" -> isVerbMarfu = true
                "Emphasized" -> isVerbEmpha = true
            }

            ismujarrad = verbType == "mujarrad"
            if (isdictionary) {
                dataBundle!!.putString(Constant.QURAN_VERB_ROOT, verbRoot)
            } else if (arabicWord.isEmpty()) {
                try {
                    dataBundle?.apply {
                        putString(Constant.QURAN_VERB_ROOT, verbRoot)
                        putString(Constant.QURAN_VERB_WAZAN, bundle.getString(Constant.QURAN_VERB_WAZAN))
                        putString(Constant.VERBMOOD, verbMood)
                        putString(Constant.VERBTYPE, verbType)
                        putString(Constant.NOUNCASE, nouncase)
                    }

                    when (verbType) {
                        "mujarrad" -> isUnaugmentedWazan = true
                        "mazeed" -> isAugmentedWazan = true
                        else -> isnoconjugation = true
                    }

                } catch (e: Exception) {
                    dataBundle?.apply {
                        putString(Constant.QURAN_VOCUBALORY_ROOT, vocabRoot)
                        putString(Constant.QURAN_VERB_WAZAN, verbFormThulathi)
                        putSerializable(Constant.QURAN_VERB_ROOT, conjugationRoot)
                    }
                    isUnaugmentedWazan = true
                }
            } else {
                isonlyarabicword = true
                dataBundle?.apply {
                    putString("arabicword", arabicWord)
                    putString(Constant.QURAN_VERB_WAZAN, "")
                    putSerializable(Constant.QURAN_VERB_ROOT, "")
                }
                isnoconjugation = false
            }

            // Setup tab layout
            tabLanguageSetup(tabLayout, viewPager, verbMood, isrelative, isdem, isharfnasab, isShart, isprep)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
        }
    }

    private fun tabLanguageSetup(
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        verbMood: String,
        isRelative: Boolean,
        isDem: Boolean,
        isHarfNasab: Boolean,
        isShart: Boolean,
        isPrep: Boolean
    ) {
        // Default languages setup
        //val defaultLanguages = arrayOf("lanes", "hans", "english", "urdu")
       // languages = defaultLanguages
/*
     } else if (ismujarrad && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu)) {
                return getMujarradParticiple(position)
            } else if (ismujarrad && (isVerbMajzoom || isVerbMansub||isVerbEmpha)) {
                return getMujarradMajzoomOrMansub(position)
            } else if (ismujarrad && isVerbMarfu && !isimperative) {
                return getMujarradMarfuu(position)
            } else if (isAugmentedWazan && (isVerbMajzoom || isimperative)) {
                return getMazeedMajzoomOrMarfuImp(position)
            } else if (ismujarrad && (isVerbMajzoom || isimperative)) {
                return getMujarradImperative(position)
            } else if (isAugmentedWazan && (isVerbMajzoom || isVerbMansub||isVerbEmpha)) {
                return getMazeedMajzoomOrMarfu(position)
            } else if (isAugmentedWazan && isVerbMarfu) {
                return getMazeedMarfu(position)
            } else if (isIsmMajroor || isIsmMansub || isIsmMarfu) {
                return getMujarradMajzoomOrMansub(position)
            } else if (isonlyarabicword && isHarf) //isShart == isrelative == isharfnasab == isprep == isdem;
            {
                return getHarf(position)
            }
 */

        when {
            isLaysa ->
            {
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "kanainna"
                setupTabLayout(tabLayout, viewPager, laysatitles)
            }
            (ismujarrad && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu))-> {
                if (verbMood != null) {
                    setLanguages(isimperative,ismujarrad,isparticple,isIsmMajroor,isIsmMansub,isIsmMarfu,isAugmentedWazan,isonlyarabicword,isrelative,isdem,isharfnasab,isShart,isprep,isnoconjugation,isdictionary,verbMood)

                }
                setupTabLayout(tabLayout, viewPager, mujarradparticple)
            }

             isimperative  -> {

                val tabTitles = if (isAugmentedWazan) mazeedimperative else mujarradimperative
               //  setLanguages(isimperative,ismujarrad,isparticple,isIsmMajroor,isIsmMansub,isIsmMarfu,isAugmentedWazan,isonlyarabicword,isrelative,isdem,isharfnasab,isShart,isprep,isnoconjugation,isdictionary,verbMood)
                 setLanguages(isimperative,ismujarrad,isparticple,isIsmMajroor,isIsmMansub,isIsmMarfu,isAugmentedWazan,isonlyarabicword,isrelative,isdem,isharfnasab,isShart,isprep,isnoconjugation,isdictionary,verbMood)

                 setupTabLayout(tabLayout, viewPager, tabTitles)
            }

            ismujarrad && isparticple && isAnyIsm() -> {
                languages += getIsmType()
                setupTabLayout(tabLayout, viewPager, mujarradparticple)
            }

            isAugmentedWazan && isparticple  -> {
                setLanguages(isimperative,ismujarrad,isparticple,isIsmMajroor,isIsmMansub,isIsmMarfu,isAugmentedWazan,isonlyarabicword,isrelative,isdem,isharfnasab,isShart,isprep,isnoconjugation,isdictionary,verbMood)

                setupTabLayout(tabLayout, viewPager, mazeedparticpletitle)
            }

            isProperNoun ->
            {
                handleWordNotFound()
              //  Toast.makeText(this, "It is Proper Noun", Toast.LENGTH_SHORT).show()
            }

            isAnyIsm() -> {
                setLanguages(isimperative,ismujarrad,isparticple,isIsmMajroor,isIsmMansub,isIsmMarfu,isAugmentedWazan,isonlyarabicword,isrelative,isdem,isharfnasab,isShart,isprep,isnoconjugation,isdictionary,verbMood)

                setupTabLayout(tabLayout, viewPager, ismmansubtitle)
            }

          /*  isOnlyArabicWord() -> {
                languages += getArabicWordType(isRelative, isDem, isHarfNasab, isShart, isPrep)
                setupTabLayout(tabLayout, viewPager, arabicwordharfnasb)
            }*/

            isnoconjugation -> {
                setupTabLayout(tabLayout, viewPager, vocabularytitles)
            }

            isdictionary -> {
                setupTabLayout(tabLayout, viewPager, dictionarytitle)
            }
             (ismujarrad && isVerbMarfu && !isimperative) ->
                 setupTabLayout(tabLayout,viewPager,thulathientitles)

            (isAugmentedWazan && isVerbMajzoom) -> {
                setLanguages(
                    isimperative,
                    ismujarrad,
                    isparticple,
                    isIsmMajroor,
                    isIsmMansub,
                    isIsmMarfu,
                    isAugmentedWazan,
                    isonlyarabicword,
                    isrelative,
                    isdem,
                    isharfnasab,
                    isShart,
                    isprep,
                    isnoconjugation,
                    isdictionary,
                    verbMood
                )

                setupTabLayout(tabLayout, viewPager, mazeedentitlesmajzoom)
            }
            (isAugmentedWazan && isVerbMansub) -> {
                setLanguages(
                    isimperative,
                    ismujarrad,
                    isparticple,
                    isIsmMajroor,
                    isIsmMansub,
                    isIsmMarfu,
                    isAugmentedWazan,
                    isonlyarabicword,
                    isrelative,
                    isdem,
                    isharfnasab,
                    isShart,
                    isprep,
                    isnoconjugation,
                    isdictionary,
                    verbMood
                )

                setupTabLayout(tabLayout, viewPager, mazeedentitlesmansub)
            }
            (isAugmentedWazan && isVerbMarfu) -> {
                setLanguages(
                    isimperative,
                    ismujarrad,
                    isparticple,
                    isIsmMajroor,
                    isIsmMansub,
                    isIsmMarfu,
                    isAugmentedWazan,
                    isonlyarabicword,
                    isrelative,
                    isdem,
                    isharfnasab,
                    isShart,
                    isprep,
                    isnoconjugation,
                    isdictionary,
                    verbMood
                )

                setupTabLayout(tabLayout, viewPager, getMazeedentitles)
            }

            else -> {
                handleWordNotFound()
            //    Toast.makeText(this, "Not Configured", Toast.LENGTH_SHORT).show()
                /*  val view = findViewById<View>(R.id.bookmark)
             val snackbar = Snackbar.make(view, "Not Configured", Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLUE)
             snackbar.setTextColor(Color.CYAN)
             snackbar.setBackgroundTint(Color.BLACK)
             snackbar.show()

             finish()*/
            }
        }
    }

    // When the word is not found
    private fun handleWordNotFound() {
        val resultIntent = Intent().apply {
            putExtra("result_message", "Word not found")
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish() // Finish the activity and send the result back to Activity A
    }

    fun setLanguages(
        isimperative: Boolean,
        ismujarrad: Boolean,
        isparticple: Boolean,
        isIsmMajroor: Boolean,
        isIsmMansub: Boolean,
        isIsmMarfu: Boolean,
        isAugmentedWazan: Boolean,
        isonlyarabicword: Boolean,
        isrelative: Boolean,
        isdem: Boolean,
        isharfnasab: Boolean,
        isShart: Boolean,
        isprep: Boolean,
        isnoconjugation: Boolean,
        isdictionary: Boolean,
        verbmood: String
    ) {
        // Default initialization
        languages[0] = "lanes"
        languages[1] = "hans"
        languages[2] = "english"
        languages[3] = "urdu"

        when {
            isimperative -> {
                languages[4] = "imperative"
             //   languages[5] = "Jussive"
            }
            ismujarrad && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu) -> {
                when {
                    isIsmMajroor -> {
                        languages[4] = "genetivenoun"
                        mujarradparticple[4] = "Genitive.Noun"
                    }
                    isIsmMansub -> {
                        languages[4] = "accusativenoun"
                        mujarradparticple[4] = "Accusative.Noun"
                    }
                    isIsmMarfu -> {
                        languages[4] = "nominativenoun"
                        mujarradparticple[4] = "Nominative.Noun"
                    }
                }
            }
            isAugmentedWazan && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu) -> {
                when {
                    isIsmMajroor -> {
                        languages[4] = "genetivenoun"
                        mazeedparticpletitle[4] = "Genitive.Noun"
                    }
                    isIsmMansub -> {
                        languages[4] = "accusativenoun"
                        mazeedparticpletitle[4] = "Accusative.Noun"
                    }
                    isIsmMarfu -> {
                        languages[4] = "nominativenoun"
                        mazeedparticpletitle[4] = "Nominative.Noun"
                    }
                }
            }
            isIsmMajroor || isIsmMansub || isIsmMarfu -> {
                when {
                    isIsmMajroor -> {
                        languages[4] = "genetivenoun"
                        ismmansubtitle[4] = "Genitive.Noun"
                    }
                    isIsmMansub -> {
                        languages[4] = "accusativenoun"
                        ismmansubtitle[4] = "Accusative.Noun"
                    }
                    isIsmMarfu -> {
                        languages[4] = "nominativenoun"
                        ismmansubtitle[4] = "Nominative.Noun"
                    }
                }
            }
            ismujarrad -> {
                when {
                    SharedPref.language == "en" && verbmood == "Jussive" -> {
                        languages[4] = "Jussive"
                    }
                    SharedPref.language == "en" && verbmood == "Subjunctive" -> {
                        languages[4] = "Subjunctive"
                    }
                    SharedPref.language == "en" && (verbmood == "Indicative" || verbmood == "Emphasized") -> {
                        // No specific change for these moods
                    }
                }
            }
            isAugmentedWazan -> {
                when {
                    SharedPref.language == "en" && verbmood == "Jussive" -> {
                        languages[4] = "Jussive"
                    }
                    SharedPref.language == "en" && verbmood == "Subjunctive" -> {
                        languages[4] = "Subjunctive"
                    }
                    else -> {
                        // No specific change here
                    }
                }
            }
            isonlyarabicword && isrelative -> {
                languages[0] = "english"
                languages[1] = "urdu"
                languages[2] = "relative"
            }
            isonlyarabicword && isdem -> {
                languages[0] = "english"
                languages[1] = "urdu"
                languages[2] = "demonstrative"
            }
            isonlyarabicword && isharfnasab -> {
                languages[0] = "english"
                languages[1] = "urdu"
                languages[2] = "accusative"
            }
            isonlyarabicword && isShart -> {
                languages[0] = "english"
                languages[1] = "urdu"
                languages[2] = "conditional"
            }
            isonlyarabicword && isprep -> {
                languages[0] = "english"
                languages[1] = "urdu"
                languages[2] = "preposition"
            }
            isnoconjugation -> {
                // Already initialized with default values
            }
            isdictionary -> {
                languages[0] = "lanes"
                languages[1] = "hans"
            }
        }
    }

    // Function to check if the condition is for Arabic word types
    private fun isOnlyArabicWord(): Boolean {
        return isrelative || isdem || isharfnasab || isShart || isprep
    }


    // Helper function to determine if any ism type is true
    private fun isAnyIsm() = isIsmMajroor || isIsmMansub || isIsmMarfu

    // Helper function to return appropriate ism type
    private fun getIsmType(): Array<String> {
        return when {
            isIsmMajroor -> arrayOf("genetivenoun")
            isIsmMansub -> arrayOf("accusativenoun")
            isIsmMarfu -> arrayOf("nominativenoun")
            else -> arrayOf()
        }
    }

    // Helper function to setup the TabLayout and ViewPager
    private fun setupTabLayout(tabLayout: TabLayout, viewPager: ViewPager2, titles: Array<String>) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    // Helper function to determine Arabic word types
    private fun getArabicWordType(
        isRelative: Boolean, isDem: Boolean, isHarfNasab: Boolean, isShart: Boolean, isPrep: Boolean
    ): Array<String> {
        return when {
            isRelative -> arrayOf("relative")
            isDem -> arrayOf("demonstrative")
            isHarfNasab -> arrayOf("accusative")
            isShart -> arrayOf("conditional")
            isPrep -> arrayOf("preposition")
            else -> arrayOf()
        }
    }


    private inner class ViewStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            if(isLaysa){
                return getLaysa(position)
            }
            if (isdictionary) {
                return getdictionary(position)
            } else if (ismujarrad && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu)) {
                return getMujarradParticiple(position)
            } else if (ismujarrad && (isVerbMajzoom || isVerbMansub||isVerbEmpha)) {
                return getMujarradMajzoomOrMansub(position)
            } else if (ismujarrad && isVerbMarfu && !isimperative) {
                return getMujarradMarfuu(position)
            } else if (isAugmentedWazan &&  isimperative) {
                return getMazeedImperative(position)
            } else if (ismujarrad &&  isimperative) {
                return getMujarradImperatives(position)
            } else if (isAugmentedWazan && (isVerbMajzoom || isVerbMansub||isVerbEmpha)) {
                return getMazeedMajzoomOrMarfu(position)
            } else if (isAugmentedWazan && isVerbMarfu) {
                return getMazeedMarfu(position)
            } else if (isIsmMajroor || isIsmMansub || isIsmMarfu) {
                return getMujarradMajzoomOrMansub(position)
            } else if (isonlyarabicword && isHarf) //isShart == isrelative == isharfnasab == isprep == isdem;
            {
                return getHarf(position)
            }
            return getMujarradMajzoomOrMansub(position)
        }

        private fun getdictionary(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())

                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            }
            /*     val fragv = FragmentVerb()
                 fragv.arguments = dataBundle
                 return fragv.newInstance()*/
            return TODO("Provide the return value")
        }
/*
"Lane Lexicon", "Hans Weir", "English lughat", "Urdu Lughat", " ",
        "Verb Conjugaton", "Active/Passive PCPL", "N. Instrument", "N.Place/Time"
 */
        private fun getMujarradParticiple(position: Int): Fragment {
            if (position == 0) {
                //lanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                //verb
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                //active participle
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 6) {
                //
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            } else if (position == 7) {
                //instrument
                val fragvsi = FragmentIsmIsmAla()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            } else if (position == 8) {
                //noun of place
                val fragvsi = FragmentIsmZarf()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            }
            val fragv = FragmentVerb()
            fragv.arguments = dataBundle
            //return fragv.newInstance()
            return TODO("Provide the return value")
        }
        private fun getMujarradImperatives(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                //jussive/sujunnctive/marfu/mansub/majroor
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 6) {
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            } else if (position == 7) {
                val fragvsi = FragmentIsmIsmAla()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            } else if (position == 8) {
                val fragvsi = FragmentIsmZarf()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            }
            val fragv = FragmentVerb()
            fragv.arguments = dataBundle
            return fragv.newInstance()

        }
        private fun getMujarradMarfuu(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            } else if (position == 6) {
                val fragvsi = FragmentIsmIsmAla()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            } else if (position == 7) {
                val fragvsi = FragmentIsmZarf()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            }
            val fragv = FragmentVerb()
            fragv.arguments = dataBundle
            return fragv.newInstance()

        }

        private fun getMujarradMajzoomOrMansub(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                //jussive/sujunnctive/marfu/mansub/majroor
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 6) {
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            } else if (position == 7) {
                val fragvsi = FragmentIsmIsmAla()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            } else if (position == 8) {
                val fragvsi = FragmentIsmZarf()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            }
            val fragv = FragmentVerb()
            fragv.arguments = dataBundle
            return fragv.newInstance()

        }

        private fun getMazeedMarfu(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            }
            val fragv = FragmentVerb()
            fragv.arguments = dataBundle
            return fragv.newInstance()

        }

        private fun getHarf(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            }
            /* val fragv = FragmentVerb()
             fragv.arguments = dataBundle
             return fragv.newInstance()*/
            return TODO("Provide the return value")
        }
        private fun getLaysa(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()

            } else if (position == 2) {
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {

                val fragv = LaysaVerbFrag()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            }
            val fragv = LaysaVerbFrag()
            fragv.arguments = dataBundle
            return fragv.newInstance()

        }
        private fun getMujarradImperative(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                //jussive/sujunnctive/marfu/mansub/majroor
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                // org.sj.conjugator.fragments.FragmentVerb fragv = new org.sj.conjugator.fragments.FragmentVerb();
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[5].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } /*else if (position == 6) {
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 7) {
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            } else if (position == 8) {
                val fragvsi = FragmentIsmIsmAla()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            } else if (position == 9) {
                val fragvsi = FragmentIsmZarf()
                fragvsi.arguments = dataBundle
                return fragvsi.newInstance()
            }
            val fragv = FragmentVerb()
            fragv.arguments = dataBundle
            return fragv.newInstance()*/
            return TODO("Provide the return value")
        }

        private fun getMazeedImperative(position: Int): Fragment {
            if (position == 0) {
                //hanes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 1) {
                //kabes
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 2) {
                //english
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 3) {
                //urudu
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 4) {
                //jussive/sujunnctive/marfu/mansub/majroor
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 5) {
                // org.sj.conjugator.fragments.FragmentVerb fragv = new org.sj.conjugator.fragments.FragmentVerb();
                val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 6) {
                val fragv = FragmentVerb()
                fragv.arguments = dataBundle
                return fragv.newInstance()
            } else if (position == 7) {
                val fragvs = FragmentIsmfaelIsmMafools()
                fragvs.arguments = dataBundle
                return fragvs.newInstance()
            }
            val fragvs = FragmentIsmfaelIsmMafools()
            fragvs.arguments = dataBundle
            return fragvs.newInstance()
        }


        private fun getMazeedMajzoomOrMarfu(position: Int): Fragment {
            when (position) {
                0 -> {
                    //hanes
                    val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[0].toString())
                    fragv.arguments = dataBundle
                    return fragv.newInstance()
                }
                1 -> {
                    //kabes
                    val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[1].toString())
                    fragv.arguments = dataBundle
                    return fragv.newInstance()
                }
                2 -> {
                    //english
                    val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[2].toString())
                    fragv.arguments = dataBundle
                    return fragv.newInstance()
                }
                3 -> {
                    //urudu
                    val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[3].toString())
                    fragv.arguments = dataBundle
                    return fragv.newInstance()
                }
                4 -> {
                    //jussive/sujunnctive/marfu/mansub/majroor
                    val fragv = Dictionary_frag(this@LughatWordDetailsAct, languages[4].toString())
                    fragv.arguments = dataBundle
                    return fragv.newInstance()
                }

                5->  {
                    val fragv = FragmentVerb()
                    fragv.arguments = dataBundle
                    return fragv.newInstance()
                }
                 6->  {
                        val fragvs = FragmentIsmfaelIsmMafools()
                        fragvs.arguments = dataBundle
                        return fragvs.newInstance()
                    }
               else ->{

                   val fragvs = FragmentIsmfaelIsmMafools()
                   fragvs.arguments = dataBundle
                   return fragvs.newInstance()
               }

            }
        }

        override fun getItemCount(): Int {
            if(isLaysa){
                return 4
            }
          else  if (isdictionary) {
                return 2
            } else if (isimperative && isAugmentedWazan) {
                return 7
            } else if (isimperative && ismujarrad) {
                return 9
            } else if (isAugmentedWazan && isparticple) {
                return 7
            } else if (ismujarrad && isparticple) {
                return 9
            } else if (isnoun) {
                return NUM_PAGES_ISMMARFU
            }
            return if (isHarf) {
                NUM_PAGES_ARABICWORD_HARFNASB
            } else if (isUnaugmentedWazan && (isVerbMajzoom || isVerbMansub)) {
                NUM_PAGES_THULATHI_MAJZOOMMANSUB
            } else if (isUnaugmentedWazan) {
                NUM_PAGES_THULATHI
            } else if (isAugmentedWazan && (isVerbMansub || isVerbMajzoom)) {
                NUM_PAGES_MAZEED_MAJZOOMMANSUB
            } else if (isAugmentedWazan) {
                NUM_PAGES_MAZEED
            } else if (isnoconjugation) {
                NUM_PAGES_VOCABULARY
            } else {
                0
            }
        }
    }

    companion object {
        private const val NUM_PAGES_THULATHI = 8
        private const val NUM_PAGES_THULATHI_MAJZOOMMANSUB = 9
        private const val NUM_PAGES_MAZEED = 6
        private const val NUM_PAGES_MAZEED_MAJZOOMMANSUB = 7
        private const val NUM_PAGES_VOCABULARY = 4
        private const val NUM_PAGES_ARABICWORD_HARFNASB = 3
        private const val NUM_PAGES_ISMMARFU = 5
    }
}
/*
@AndroidEntryPoint
class LughatWordDetailsAct : BaseActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    // Use a data class to represent tab titles and associated data
    data class TabInfo(val title: String, val languageKey: String? = null)

    // Define tab configurations based on word type and properties
    private val dictionaryTabs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans")
    )

    private val arabicWordHarfNasbTabs = arrayOf(
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu"),
        TabInfo("Harf ") // Consider adding a language key for consistency
    )

    private val vocabularyTabs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans"),
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu")
    )

    private val thulathiEntitlesTabs = arrayOf(
        TabInfo("Lane Lexicon", "lanes"),
        TabInfo("Hans Weir", "hans"),
        TabInfo("English lughat", "english"),
        TabInfo("Urdu Lughat", "urdu"),
        TabInfo("Verb Conjugation"),
        TabInfo("Active/Passive PCPL"),
        TabInfo("N. Instrument"),
        TabInfo("N.Place/Time")
    )

    // ... (Similarly define other tab configurations)

    private var dataBundle: Bundle? = null
    private var isUnaugmentedWazan = false
    private var isAugmentedWazan = false
    // ... (Other flags)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtabs)

        val callButton = findViewById<FloatingActionButton>(R.id.action_button)
        callButton.setOnClickListener {
            finish() // Close the activity
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // ... (Other initialization)

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        dataBundle = Bundle()

        val bundle = intent.extras
        val conjugationRoot = bundle?.getString(Constant.QURAN_VERB_ROOT) ?: ""
        val vocabularyRoot = bundle?.getString(Constant.QURAN_VERB_ROOT) ?: ""
        val verbFormThulathi = bundle?.getString(Constant.QURAN_VERB_WAZAN) ?: ""

        // Clean up the conjugation root
        val verbRoot = conjugationRoot
            .replace("[\\[\\]]".toRegex(), "")
            .replace("[,;\\s]".toRegex(), "")
            .replaceFirst(ArabicLiterals.LALIF, "ء") // Replace first occurrence only

        // ... (Process noun case, verb properties, etc.)

        // Determine tab configuration based on word type and properties
        val tabs: Array<TabInfo> = when {
            isdictionary -> dictionaryTabs
            ismujarrad && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu) -> {
                // Update mujarradparticple array based on noun case
                // ...
                mujarradparticple
            }
            // ... (Other conditions)
            else -> thulathiEntitlesTabs // Default to thulathiEntitlesTabs
        }

        // Set up ViewPager and TabLayout
        val fm = supportFragmentManager
        val viewStateAdapter = ViewStateAdapter(fm, lifecycle, tabs, dataBundle)
        viewPager.adapter = viewStateAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position].title
        }.attach()

        // ... (Tab and ViewPager event listeners)
    }

    private inner class ViewStateAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val tabs: Array<TabInfo>,
        private val dataBundle: Bundle?
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = tabs.size

        override fun createFragment(position: Int): Fragment {
            val tab = tabs[position]
            return when (tab.languageKey) {
                "lanes" -> Dictionary_frag(this@LughatWordDetailsAct, "lanes").apply {
                    arguments = dataBundle
                }
                "hans" -> Dictionary_frag(this@LughatWordDetailsAct, "hans").apply {
                    arguments = dataBundle
                }
                // ... (Handle other language keys)
                else -> {
                    // Handle cases without language keys (e.g., Verb Conjugation)
                    // ...
                    Fragment() // Placeholder, replace with appropriate Fragment
                }
            }
        }
    }

    // ... (Other methods)
}
 */

/*

dataBundle = Bundle()
        val bundle = intent.extras
        //  bundle.getParcelableArray("dictionary");
        var conjugationroot = bundle?.getString(Constant.QURAN_VERB_ROOT) ?: ""
        val vocubaluryroot = bundle?.getString(Constant.QURAN_VERB_ROOT) ?: ""
        val verbformthulathi = bundle?.getString(Constant.QURAN_VERB_WAZAN) ?: ""
        val ss = conjugationroot!!.replace("[\\[\\]]".toRegex(), "")
        val verbroot = ss.replace("[,;\\s]".toRegex(), "")
        val starts = conjugationroot.indexOf(ArabicLiterals.LALIF)
        val hamza = "ء"
        if (starts != -1) {
            conjugationroot =
                conjugationroot.replace(ArabicLiterals.LALIF, hamza.trim { it <= ' ' })
        }
        if (bundle?.getString("nouncase") != null) {
            nouncase = bundle.getString(Constant.NOUNCASE)
            isnoun = true
            when (nouncase) {
                "NOM" -> isIsmMarfu = true
                "ACC" -> isIsmMansub = true
                "GEN" -> isIsmMajroor = true
            }
        }
        val verbform: String? = bundle?.getString(Constant.QURAN_VERB_WAZAN) ?: ""
        val verbmood: String? = bundle?.getString(Constant.VERBMOOD) ?: ""
        val verbtype: String? = bundle?.getString(Constant.VERBTYPE) ?: ""
        val arabicword: String? = bundle?.getString("arabicword") ?: ""
        isdictionary = bundle?.getBoolean("dictionary",false) ?: false
        isimperative = bundle?.getBoolean(Constant.IMPERATIVE, false) ?: false
        isparticple = bundle?.getBoolean(Constant.ISPARTICPLE, false) ?: false
        val isharfnasab = bundle?.getBoolean(Constant.ACCUSATIVE, false) ?: false
        val isdem = bundle?.getBoolean(Constant.DEMONSTRATIVE, false) ?: false
        val isrelative = bundle?.getBoolean(Constant.RELATIVE, false) ?: false
        val isShart = bundle?.getBoolean(Constant.CONDITIONAL, false) ?: false
        val isprep = bundle?.getBoolean(Constant.PREPOSITION, false) ?: false
        isHarf = isShart == isrelative == isharfnasab == isprep == isdem
        try {
            when (verbmood) {
                "Jussive" -> isVerbMajzoom = true
                "Subjunctive" -> isVerbMansub = true
                "Indicative" -> isVerbMarfu = true
                "Emphasized"->isVerbEmpha=true
            }
        } catch (e: NullPointerException) {
            println(e.message)
        }
        ismujarrad = verbtype != null && verbtype == "mujarrad"
        if (isdictionary) {
            dataBundle!!.putString(Constant.QURAN_VERB_ROOT, verbroot)
        } else if (arabicword != null && arabicword.isEmpty()) {
            try {
                dataBundle!!.putString(Constant.QURAN_VERB_ROOT, verbroot)
                dataBundle!!.putString(Constant.QURAN_VERB_WAZAN, verbform)
                dataBundle!!.putString(Constant.VERBMOOD, verbmood)
                dataBundle!!.putString(Constant.VERBTYPE, verbtype)
                if (bundle != null) {
                    if (bundle.getString("nouncase") != null) {
                        dataBundle!!.putString(Constant.NOUNCASE, nouncase)
                    }
                }
                assert(verbtype != null)


                when (verbtype) {
                    "mujarrad" -> {
                        isUnaugmentedWazan = true
                    }
                    "mazeed" -> {
                        isAugmentedWazan = true
                    }
                    else -> {
                        isnoconjugation = true
                    }
                }
           /*     if (verbtype == "mujarrad") {
                    isUnaugmentedWazan = true
                } else if (verbtype == "mazeed") {
                    isAugmentedWazan = true
                } else {
                    isnoconjugation = true
                }*/
            } catch (e: Exception) {
                dataBundle!!.putString(Constant.QURAN_VOCUBALORY_ROOT, vocubaluryroot)
                dataBundle!!.putString(Constant.QURAN_VERB_WAZAN, verbformthulathi)
                dataBundle!!.putSerializable(Constant.QURAN_VERB_ROOT, conjugationroot)
                isUnaugmentedWazan = true
            }
        } else {
            isonlyarabicword = true
            dataBundle!!.putString("arabicword", arabicword)
            dataBundle!!.putString(Constant.QURAN_VERB_WAZAN, "")
            dataBundle!!.putSerializable(Constant.QURAN_VERB_ROOT, "")
            isnoconjugation = false
        }
        // Up to here, we have working scrollable pages
        if (isimperative) {
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
            languages[4] = "imperative"
            languages[5] = "Jussive"
            if (isAugmentedWazan) {
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = mazeedimperative[position]
                }.attach()
            } else if (ismujarrad) {
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = mujarradimperative[position]
                }.attach()
            }
        } else if (ismujarrad && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu)) {
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
            if (isIsmMajroor) {
                languages[4] = "genetivenoun"
                mujarradparticple[4] = "Genitive.Noun"
            } else if (isIsmMansub) {
                languages[4] = "accusativenoun"
                mujarradparticple[4] = "Accusative.Noun"
            } else if (isIsmMarfu) {
                languages[4] = "nominativenoun"
                mujarradparticple[4] = "Nominative.Noun"
            }
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = mujarradparticple[position]
            }.attach()
        } else if (isAugmentedWazan && isparticple && (isIsmMajroor || isIsmMansub || isIsmMarfu)) {
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
            languages[4] = "genetivenoun"
            if (isIsmMajroor) {
                languages[4] = "genetivenoun"
                mazeedparticpletitle[4] = "Genitive.Noun"
            } else if (isIsmMansub) {
                languages[4] = "accusativenoun"
                mazeedparticpletitle[4] = "Accusative.Noun"
            } else if (isIsmMarfu) {
                languages[4] = "nominativenoun"
                mazeedparticpletitle[4] = "Nominative.Noun"
            }
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = mazeedparticpletitle[position]
            }.attach()
        } else if (isIsmMajroor || isIsmMansub || isIsmMarfu) {
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
            languages[4] = "genetivenoun"
            mujarradparticple[4] = "Genitive"
            if (isIsmMajroor) {
                languages[4] = "genetivenoun"
                ismmansubtitle[4] = "Genitive.Noun"
            } else if (isIsmMansub) {
                languages[4] = "accusativenoun"
                ismmansubtitle[4] = "Accusative.Noun"
            } else if (isIsmMarfu) {
                languages[4] = "nominativenoun"
                ismmansubtitle[4] = "Nominative.Noun"
            }
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = ismmansubtitle[position]
            }.attach()
        } else if (isIsmMansub) {
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
            languages[4] = "accusativenoun"
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = ismmansubtitle[position]
            }.attach()
        } else if (isIsmMarfu) {
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
            languages[4] = "nominativenoun"
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = ismmarfutitle[position]
            }.attach()
        } else if (ismujarrad) {
            if (SharedPref.language == "en" && verbmood == "Jussive") {
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "english"
                languages[3] = "urdu"
                languages[4] = "Jussive"
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = thulathientitlesmajzoom[position]
                }.attach()
            } else if (SharedPref.language == "en" && verbmood == "Subjunctive") {
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "english"
                languages[3] = "urdu"
                languages[4] = "Subjunctive"
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = thulathientitlesmansub[position]
                }.attach()
            } else if (SharedPref.language == "en" && (verbmood == "Indicative" || verbmood=="Emphasized")) {
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "english"
                languages[3] = "urdu"
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = thulathientitles[position]
                }.attach()
            }
        } else if (isAugmentedWazan) {


            if (SharedPref.language == "en" && verbmood == "Jussive") {
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "english"
                languages[3] = "urdu"
                languages[4] = "Jussive"
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = mazeedentitlesmajzoom[position]
                }.attach()
            } else if (SharedPref.language == "en" && verbmood == "Subjunctive") {
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "english"
                languages[3] = "urdu"
                languages[4] = "Subjunctive"
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = mazeedentitlesmansub[position]
                }.attach()
            } else {
                TabLayoutMediator(
                    tabLayout, viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = getMazeedentitles[position]
                }.attach()
                languages[0] = "lanes"
                languages[1] = "hans"
                languages[2] = "english"
                languages[3] = "urdu"
            }
        } else if (isonlyarabicword && isrelative) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = arabicwordharfnasb[position]
            }.attach()
            languages[0] = "english"
            languages[1] = "urdu"
            languages[2] = "relative"
        } else if (isonlyarabicword && isdem) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = arabicwordharfnasb[position]
            }.attach()
            languages[0] = "english"
            languages[1] = "urdu"
            languages[2] = "demonstrative"
        } else if (isonlyarabicword && isharfnasab) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = arabicwordharfnasb[position]
            }.attach()
            languages[0] = "english"
            languages[1] = "urdu"
            languages[2] = "accusative"
        } else if (isonlyarabicword && isShart) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = arabicwordharfnasb[position]
            }.attach()
            languages[0] = "english"
            languages[1] = "urdu"
            languages[2] = "conditonal"
        } else if (isonlyarabicword && isprep) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = arabicwordharfnasb[position]
            }.attach()
            languages[0] = "english"
            languages[1] = "urdu"
            languages[2] = "preposition"
        } else if (isnoconjugation) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = vocabularytitles[position]
            }.attach()
            languages[0] = "lanes"
            languages[1] = "hans"
            languages[2] = "english"
            languages[3] = "urdu"
        } else if (isdictionary) {
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = dictionarytitle[position]
            }.attach()
            languages[0] = "lanes"
            languages[1] = "hans"
        } else {
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show()
            finish()
        }
 */