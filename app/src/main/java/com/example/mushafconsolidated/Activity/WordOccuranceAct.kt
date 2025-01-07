package com.example.mushafconsolidated.Activity

import Utility.ArabicLiterals.AlifMaksuraString
import Utility.ArabicLiterals.Hamza
import Utility.ArabicLiterals.LALIF
import Utility.ArabicLiterals.Ya
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.Constant
import com.example.Constant.AYAHNUMBER
import com.example.Constant.SURAH_ARABIC_NAME
import com.example.Constant.SURAH_ID
import com.example.Constant.WORDNUMBER
import com.example.Constant.particlespanDark
import com.example.mushafconsolidated.Activityimport.BaseActivity
import com.example.mushafconsolidated.Adapters.NounVerbOccuranceListAdapter
import com.example.mushafconsolidated.Entities.CorpusNounWbwOccurance
import com.example.mushafconsolidated.Entities.CorpusVerbWbwOccurance
import com.example.mushafconsolidated.Entities.NounCorpusBreakup
import com.example.mushafconsolidated.Entities.VerbCorpusBreakup
import com.example.mushafconsolidated.Entities.hanslexicon

import com.example.mushafconsolidated.Entities.lanerootdictionary
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.QuranMorphologyDetails
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet
import com.example.utility.CorpusUtilityorig.Companion.getSpannableVerses
import com.example.utility.QuranGrammarApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects
import java.util.concurrent.Executors
import java.util.regex.Pattern

@AndroidEntryPoint
open class WordOccuranceAct : BaseActivity() {
    private var expandableListView: ExpandableListView? = null
    private var harf = false
    lateinit var expandNounTitles: MutableList<String>
    lateinit var expandVerbTitles: List<String>
    var root: String? = null
    var dialog: AlertDialog? = null
    private enum class WordType { HANS, LANES, NOUN, VERB }
    val expandNounVerses = LinkedHashMap<String, ArrayList<SpannableString>>()
    val expandVerbVerses = LinkedHashMap<String, ArrayList<SpannableString>>()
    lateinit var utils: Utils
    private var firstcolortat = 0
    private var maincolortag = 0
    private var pronouncolortag = 0
    private var fourcolortag = 0
    private var verbCorpusArrayList: ArrayList<VerbCorpusBreakup>? = null
    private var occurrences: ArrayList<CorpusNounWbwOccurance>? = null
    private var nounCorpusArrayList: ArrayList<NounCorpusBreakup>? = null
    private lateinit var shared: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expand_list)

        utils = Utils(this@WordOccuranceAct)
        // String preferences = SharedPref.themePreferences();
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
        shared = PreferenceManager.getDefaultSharedPreferences(this)
        val whichtranslation = shared.getString("selecttranslation", "en_sahih")
        val showtranslation = shared.getBoolean("prefs_show_translation", true)
        expandableListView = findViewById(R.id.expandableListView)
        val prefs =
            android.preference.PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context)
        val preferences = prefs.getString("theme", "dark")
        if (preferences == "dark" || preferences == "blue" || preferences == "green") {
            firstcolortat = Constant.BCYAN
            maincolortag = Constant.BYELLOW
            pronouncolortag = Constant.BBLUE
            fourcolortag = Constant.BWHITE
        } else {
            firstcolortat = Constant.WBURNTUMBER
            maincolortag =
                ContextCompat.getColor(QuranGrammarApplication.context!!, R.color.prussianblue)
            pronouncolortag = Constant.WMIDNIHTBLUE
            fourcolortag = Constant.GREENDARK
        }
        val callBackbutton: FloatingActionButton = findViewById(R.id.action_button)


        callBackbutton.setOnClickListener { view: View? ->
            //  Intent quranintnt = new Intent(VerbOccuranceAsynKAct.this, ReadingSurahPartActivity.class);
            finish()
        }
        val bundle: Bundle? = intent.extras
        root = bundle?.getString(Constant.QURAN_VERB_ROOT)
        if (root == "ACC" || root == "LOC" || root == "T") {
            occurrences =
                utils.getnounoccuranceHarfNasbZarf(root!!) as ArrayList<CorpusNounWbwOccurance>?
            harf = true
            ExecuteNounOcurrance()
        } else {
            harf = false
            ExecuteNounOcurrance()
        }
    }

    private fun ExecuteNounOcurrance() {
        val ex = Executors.newSingleThreadExecutor()
        ex.execute {
            runOnUiThread { dialog!!.show() }
            val nounroot = if (root!!.contains("ء")) root!!.replace("ء", "ا") else root
            val verbroot = if (root!!.contains("ا")) root!!.replace("ا", "ء") else root

   /*         val vroot = root!!.indexOf("ء")
            var nounroot: String? = ""
            val verb = root!!.indexOf("ا")
            var verbroot: String? = ""
            nounroot = if (vroot != -1) {
                root!!.replace("ء", "ا")
            } else {
                root
            }
            verbroot = if (verb != -1) {
                root!!.replace("ا", "ء")
            } else {
                root
            }*/
            nounCorpusArrayList =
                nounroot?.let { utils.getNounBreakup(it) } as ArrayList<NounCorpusBreakup>?
            verbCorpusArrayList =
                verbroot?.let { utils.getVerbBreakUp(it) } as ArrayList<VerbCorpusBreakup>?
            val alist = ArrayList<SpannableString>()
            if (harf) {
                for (verse in occurrences!!) {
                    val arabicText = "${verse.araone}${verse.aratwo}${verse.arathree}${verse.arafour}${verse.arafive}"
                    val spannableVerses = getSpannableVerses(arabicText, verse.qurantext!!)

                    val header = "${verse.surah}:${verse.ayah}:${verse.wordno}-${verse.en}-"
                    val ref = SpannableString(header).apply {
                        setSpan(particlespanDark, 0, header.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    val translation = when (shared.getString("selecttranslation", "en_sahih")) {
                        "en_sahih" -> SpannableString(verse.translation)
                        "ur_jalalayn" -> SpannableString(verse.ur_jalalayn)
                        "en_jalalayn" -> SpannableString(verse.en_jalalayn)
                        else -> null
                    }

                    val content = TextUtils.concat(ref, "\n ", spannableVerses)
                    val list = arrayListOf(SpannableString(content))
                    translation?.let { list.add(it) }

                    expandNounVerses[header] = list
                }

            }
            verbandnouns()
            expandNounTitles = ArrayList(expandNounVerses.keys)
            expandVerbTitles = ArrayList(expandVerbVerses.keys)
            expandNounVerses.putAll(expandVerbVerses)
            expandNounTitles.addAll(expandVerbTitles)
            //post
            runOnUiThread {
                dialog!!.dismiss()
                //   listAdapter = new NounVerbOccuranceListAdapter(WordOccuranceAct.this, expandNounTitles, expandNounVerses);
                val listAdapter = NounVerbOccuranceListAdapter(
                    this@WordOccuranceAct,
                    expandNounTitles,
                    expandNounVerses,
                    expandVerbVerses,
                    expandVerbTitles
                )


                expandableListView?.setAdapter(listAdapter)
                expandableListView!!.setOnGroupExpandListener { groupPosition ->
                    val split = expandNounTitles[groupPosition].split("\\s".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()

                    if (!harf) {
                        val title = expandNounTitles[groupPosition]
                        val wordType = when {
                            title.contains("Hans") -> WordType.HANS
                            title.contains("lanes") -> WordType.LANES
                            title.contains("Noun") || title.contains("Adverb") || title.contains("Adjective") -> WordType.NOUN
                            else -> WordType.VERB
                        }

                        getWordOccurrences(wordType, split[1]) { occurrences ->
                            val formattedOccurrences = formatOccurrences(wordType, occurrences)
                            expandNounVerses[expandNounTitles[groupPosition]] = formattedOccurrences
                            listAdapter.notifyDataSetChanged()
                        }
                    }

                }
                expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                    val child = listAdapter.getChild(groupPosition, childPosition) as CharSequence
                    val split = child.toString().split("-")
                    val (surah, ayah, wordno) = split[0].split(":")

                    if (split.size > 1 && surah.toIntOrNull() != null && ayah.toIntOrNull() != null && wordno.toIntOrNull() != null) {
                        val dataBundle = Bundle()
                        dataBundle.putInt(SURAH_ID, surah.toInt())
                        dataBundle.putInt(AYAHNUMBER, ayah.toInt())
                        dataBundle.putInt(WORDNUMBER, wordno.toInt())
                        dataBundle.putString(SURAH_ARABIC_NAME, "SurahName")
                        LoadItemList(dataBundle, surah, ayah, wordno)
                        true
                    } else {
                        false
                    }
                }

            }
        }
    }
    private fun getWordOccurrences(wordType: WordType, word: String, callback: (List<Any>) -> Unit) {
        val ex = Executors.newSingleThreadExecutor()

        runOnUiThread { dialog!!.show() }
        ex.execute {
            val occurrences = when (wordType) {
                WordType.HANS -> {
                    var verbRoot: String? = ""
                    val indexOfHamza: Int = root!!.indexOf(Hamza)
                    val indexofAlifMaksura: Int = root!!.indexOf(Ya)
                    verbRoot = if (indexOfHamza != -1) {
                        root!!.replace(Hamza.toRegex(), LALIF)
                    } else {
                        root
                    }
                    if (indexofAlifMaksura != -1) {
                        verbRoot = verbRoot?.replace(
                            Ya.toRegex(),
                            AlifMaksuraString
                        )
                    }
                  //  val verbRoot = getVerbRoot(root!!)
                    verbRoot?.let { utils.getHansDifinition(it) } as ArrayList<hanslexicon>
                }

                WordType.LANES -> utils.getLanesRootDifinition(root!!) as ArrayList<lanerootdictionary>
                WordType.NOUN -> utils.getNounOccuranceBreakVerses(word) as ArrayList<CorpusNounWbwOccurance>
                WordType.VERB -> utils.getVerbOccuranceBreakVerses(word) as ArrayList<CorpusVerbWbwOccurance>
            }
            runOnUiThread {
                ex.shutdown()
                dialog!!.dismiss()
                callback(occurrences)
            }
        }
    }
    private fun formatOccurrences(wordType: WordType, occurrences: List<Any>): ArrayList<SpannableString> {
        val list = ArrayList<SpannableString>()
        for (occurrence in occurrences) {
            val (ref, spannableVerses) = when (wordType) {
                WordType.HANS, WordType.LANES -> {
                    val definition = if (occurrence is hanslexicon) occurrence.definition else (occurrence as lanerootdictionary).definition
                    Pair(SpannableString(""), SpannableString(definition)) // Return a Pair
                }
                WordType.NOUN -> formatNounOccurrence(occurrence as CorpusNounWbwOccurance)
                WordType.VERB -> formatVerbOccurrence(occurrence as CorpusVerbWbwOccurance)
            }

            val translation = when (wordType) {
                WordType.HANS, WordType.LANES -> null
                WordType.NOUN -> setSelectedTranslation(shared.getString("selecttranslation", "en_sahih"), occurrence as CorpusNounWbwOccurance)
                WordType.VERB -> verbSetSelectedTranslation(shared.getString("selecttranslation", "en_sahih"), occurrence as CorpusVerbWbwOccurance)
            }

            val content = TextUtils.concat(ref, "\n ", spannableVerses, if (translation != null) "\n $translation" else "")
            list.add(SpannableString(content))
        }
        return if (wordType == WordType.HANS) highLightParadigm(list) as ArrayList<SpannableString> else list
    }

    private fun formatNounOccurrence(occurrence: CorpusNounWbwOccurance): Pair<SpannableString, Spannable> {
        val sb = StringBuilder()
        val spannableVerses = getSpannableVerses(
            "${occurrence.araone}${occurrence.aratwo}${occurrence.arathree}${occurrence.arafour}${occurrence.arafive}",
            occurrence.qurantext!!
        )
        sb.append(occurrence.surah).append(":").append(occurrence.ayah)
            .append(":").append(occurrence.wordno).append("-")
            .append(occurrence.en).append("-")
        val ref = SpannableString(sb.toString())
        ref.setSpan(particlespanDark, 0, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return Pair(ref, spannableVerses)
    }

    private fun formatVerbOccurrence(occurrence: CorpusVerbWbwOccurance): Pair<SpannableString, Spannable> {
        val sb = StringBuilder()
        val spannableVerses = getSpannableVerses(
            "${occurrence.araone}${occurrence.aratwo}${occurrence.arathree}${occurrence.arafour}${occurrence.arafive}",
            occurrence.qurantext!!
        )
        sb.append(occurrence.surah).append(":").append(occurrence.ayah)
            .append(":").append(occurrence.wordno).append("-")
            .append(occurrence.en).append("-")
        val ref = SpannableString(sb.toString())
        ref.setSpan(maincolortag, 0, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return Pair(ref, spannableVerses)
    }

    private fun highLightParadigm(list: List<*>): MutableList<*> {
        val lists: MutableList<SpannableString> =
            ArrayList()
        val REGEX = "aor.([\\s\\S]){3}"
        val pattern = Pattern.compile(REGEX)
        for (l in list) {
            val replaceAll =
                l.toString().replace("<br></br>".toRegex(), "")
            val m = pattern.matcher(replaceAll)
            var sb: SpannableString? = null
            var indexof = 0
            if (m.find()) {
                println("Found value: " + m.group(0))
                println("Found value: " + m.group(1))
                indexof = l.toString().indexOf(
                    Objects.requireNonNull(
                        m.group(0)
                    )
                )
                sb = SpannableString(l.toString())
                sb.setSpan(
                    particlespanDark,
                    indexof,
                    Objects.requireNonNull(
                        m.group(0)
                    ).length + indexof,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                lists.add(sb)
                //   System.out.println("Found value: " + m.group(2) );
            } else {
                lists.add(SpannableString.valueOf(replaceAll))
            }
        }
        return lists
    }

    private fun verbSetSelectedTranslation(
        which: String?,
        vers: CorpusVerbWbwOccurance
    ): SpannableString? {
        var trans: SpannableString? = null
        when (which) {
            "en_sahih" -> trans =
                SpannableString.valueOf(vers.translation)

            "ur_jalalayn" -> trans =
                SpannableString.valueOf(vers.ur_jalalayn)

            "en_jalalayn" -> trans =
                SpannableString.valueOf(vers.en_jalalayn)

            "en_arberry" -> trans =
                SpannableString.valueOf(vers.en_arberry)
        }
        return trans
    }

    private fun setSelectedTranslation(
        which: String?,
        vers: CorpusNounWbwOccurance
    ): SpannableString? {
        var trans: SpannableString? = null
        when (which) {
            "en_sahih" -> trans =
                SpannableString.valueOf(vers.translation)

            "ur_jalalayn" -> trans =
                SpannableString.valueOf(vers.ur_jalalayn)

            "en_jalalayn" -> trans =
                SpannableString.valueOf(vers.en_jalalayn)

            "en_arberry" -> trans =
                SpannableString.valueOf(vers.en_arberry)
        }
        return trans
    }


    private fun LoadItemList(
        dataBundle: Bundle,
        surah: String,
        ayah: String,
        wordno: String
    ) {
        val item = WordAnalysisBottomSheet()
        //    item.setdata(rootWordMeanings,wbwRootwords,grammarRootsCombined);
        item.arguments = dataBundle
        val data = arrayOf<String?>(surah, ayah, "", wordno)
        WordAnalysisBottomSheet.newInstance(data)
            .show(supportFragmentManager, WordAnalysisBottomSheet.TAG)
        //   WordAnalysisBottomSheet.newInstance(data).show(WordOccuranceAsynKAct.this).getSupportFragmentManager(), WordAnalysisBottomSheet.TAG);
    }

    private fun verbandnouns() {
        for (noun in nounCorpusArrayList!!) {
            val list: ArrayList<SpannableString> =
                ArrayList()
            list.add(SpannableString.valueOf(""))
            if (noun.form == "null") {
                val sb = StringBuilder()
                val nounexpand = QuranMorphologyDetails.expandTags(noun.tag!!)
                var times = ""
                times = if (noun.count == 1) {
                    "Once"
                } else {
                    val count = noun.count
                    val timess = count.toString()
                    "$timess-times"
                }
                sb.append(times).append(" ").append(noun.lemma_a).append(" ")
                    .append("occurs as the").append(" ").append(nounexpand)
                expandNounVerses[sb.toString()] = list
            } else {
                val sb = StringBuilder()
                val s = QuranMorphologyDetails.expandTags(noun.propone + noun.proptwo)
                //  String s1 = QuranMorphologyDetails.expandTags(noun.getProptwo());
                val s2 = QuranMorphologyDetails.expandTags(noun.tag!!)
                var times = ""
                times = if (noun.count == 1) {
                    "Once"
                } else {
                    val count = noun.count
                    val timess = count.toString()
                    "$timess-times"
                }
                sb.append(times).append(" ").append(noun.lemma_a).append(" ")
                    .append("occurs as the").append(" ").append(noun.form)
                    .append(" ")
                if (s != "null") {
                    sb.append(s).append(" ").append(" ")
                }
                sb.append(s2)
                expandNounVerses[sb.toString()] = list
            }
        }
        for (verbCorpusBreakup in verbCorpusArrayList!!) {
            val list = ArrayList<SpannableString>()
            list.add(SpannableString.valueOf(""))
            if (verbCorpusBreakup.form == "I") {
                val sb = StringBuilder()
                val mujarrad = java.lang.String.valueOf(
                    QuranMorphologyDetails.getThulathiName(verbCorpusBreakup.thulathibab)
                )
                sb.append(verbCorpusBreakup.count).append("-").append("times").append(" ")
                    .append(verbCorpusBreakup.lemma_a).append(" ").append("occurs as the")
                    .append(" ").append(mujarrad)
                expandVerbVerses[sb.toString()] = list
            } else {
                val sb = StringBuilder()
                val s = verbCorpusBreakup.tense?.let { QuranMorphologyDetails.expandTags(it) }
                val s1 = verbCorpusBreakup.voice?.let { QuranMorphologyDetails.expandTags(it) }
                val mazeed = java.lang.String.valueOf(
                    QuranMorphologyDetails.getFormName(verbCorpusBreakup.form)
                )
                sb.append(verbCorpusBreakup.count).append("-").append("times").append(" ")
                    .append(verbCorpusBreakup.lemma_a).append(" ").append("occurs as the")
                    .append(" ").append(mazeed)
                    .append(" ").append(s).append(" ").append(" ").append(s1)
                expandVerbVerses[sb.toString()] = list
            }
        }
    }
}