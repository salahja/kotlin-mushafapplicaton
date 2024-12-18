@file:Suppress("LocalVariableName")

package com.quiz


import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.VerbWazan
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.databinding.FragmentArabicVerbQuizBinding
import com.example.mushafconsolidated.fragments.QuranMorphologyDetails

import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.utility.CorpusUtilityorig.Companion.NewSetWordSpan
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.color.DynamicColors
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sj.conjugator.activity.BaseActivity
import org.sj.conjugator.activity.ConjugatorTabsActivity

@AndroidEntryPoint
class ArabicVerbQuizActNew : BaseActivity(), View.OnClickListener {

    open var form = 0
    private var totalquestion: Int = 5
    private lateinit var answerButtons: Array<TextView>
    private var corpusSurahWord: List<CorpusEntity>? = null
    private var qurans: List<QuranEntity>? = null
    //private lateinit var cverb: wbwentity
    private lateinit var cverb: VerbCorpus
    private lateinit var mainViewModel: QuranViewModel
    // private var _binding: FragmentArabicVerbQuizBinding? = null
    lateinit var binding: FragmentArabicVerbQuizBinding

    private lateinit var questionTextView: TextView
    private lateinit var verbdetails: Chip
    private lateinit var arabicsurahname: MaterialTextView
    private lateinit var correctanswer: MaterialTextView
    private lateinit var score: MaterialTextView
    private lateinit var rootword:TextView


    private lateinit var submitButton: TextView
    private lateinit var restartButton: MaterialButton
    private lateinit var showanswer: MaterialButton
    private lateinit var word_trans_textView: TextView
    private lateinit var quran_verse: TextView
    private lateinit var ayah_translation: TextView
    private lateinit var mansubnotes: MaterialTextView
    private lateinit var majzoomnotes: MaterialTextView


    private lateinit var tvProgress: TextView
    private lateinit var progressbar: ProgressBar
    private var mcurrentPosition: Int = 1
    private var mSelectedOptionPositon: Int = 0
    private var mcurrectanswer: Int = 0

    class Question(
        val id: Int,
        val question: String,
        val answers: List<String>,
        var correctAnswerIndex: Int

    )
    val buttonIds = arrayOf(
        R.id.answer_button1,
        R.id.answer_button2,
        R.id.answer_button3,
        R.id.answer_button4,
        R.id.answer_button5,
        R.id.answer_button6,
        R.id.answer_button7,
        R.id.answer_button8,
        R.id.answer_button9,
        R.id.answer_button10,
        R.id.answer_button11,
        R.id.answer_button12,
        R.id.answer_button13,
        R.id.answer_button14,

        // ... other buttons ...
    )
    private val questions = listOf(
        Question(
            1,
            "What is Tense of the verb?",
            listOf("Perfect", "Imperfect", "Imperative"),
            0
        ),
        Question(
            2,
            "What is the Voice of the Verb?",
            listOf("Active", "Passive"),
            0
        ),
        Question(
            3,
            "What is the Mood of the VERB",
            listOf("Indictive", "Subjunctive", "Jussive","Emphasized"),
            0
        ),
        Question(
            4,
            "What is the Gender and Number of Verb?",
            listOf(
                "3rd Person Mas. Sing.",
                "3rd Person Mas. Dual.",
                "3rd Person Mas. Plur.",

                "3rd Person Fem. Sing.",
                "3rd Person Fem. Dual.",
                "3rd Person Fem. Plur.",


                "2nd Person Mas. Sing.",
                "2nd Person Mas, DUal.",
                "2nd Person Mas. Plur.",

                "2nd Person Fem. Sing.",
                "2nd Person Fem. Dual",
                "2nd Person Fem. Plur.",

                "1st Person Sing.",
                "1st Person PLur",

                ),
            0
        ),
        Question(
            5,
            "What is the Patter(form) of the VERB",
            listOf(
                "Form 1(Unaugmented)",
                "Form II(فَعَّلَ)",
                "Form III(فَاعَلَ)",
                "Form IV(أَفْعَلَ)",
                "Form V(تَفَعَّلَ)",
                "Form VI(تَفَاعَلَ)",
                "Form VII(إِنْفَعَلَ-)",
                "Form VIII(إِفْتَعَلَ)",
                "Form IX(إِفْعَلَّ)",
                "Form X(إِسْتَفْعَلَ)"
            ),
            0
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {


        currenttheme =
            PreferenceManager.getDefaultSharedPreferences(this).getString("themepref", "dark")
        switchTheme(currenttheme) // Call switchTheme before super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this.application)

        super.onCreate(savedInstanceState)
        binding = FragmentArabicVerbQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val utils = Utils(this)
        val item0="\u202Bللمضارع\u202C \u202Bالناصبة\u202C \u202Bالحروف\u202C"
        val item001 = "The follwoing Harf makes ther Verb Mansub-Subjunctive"
        val item01 = SpannableString(item0)
        item01.setSpan(BulletSpan(15), 0, item01.length, 0)
        val item1=SpannableString("To -  (أَنْ)")
        item1.setSpan(BulletSpan(15), 0, item1.length, 0)
        val item2 = SpannableString("(لَنْ) will not")
        item2.setSpan(BulletSpan(15), 0, item2.length, 0)
        val item3 = SpannableString("(لِكَىْ) so that,in order to")
        item3.setSpan(BulletSpan(15), 0, item3.length, 0)

        val item4 = SpannableString("(حَتِّى) Until, to the point that, so that")
        item4.setSpan(BulletSpan(15), 0, item4.length, 0)
        val item5 = SpannableString("1.harf لِكَىْ  is a compound حرف. The الم can be used on its own and so can كَي or they can be used\n" +
                "together. The meaning remains the same")
        val item6=SpannableString("2.harf حتى can come before a \u202Bماض\u202C \u202Bفعل\u202C as well. In this case, it means “until” or “to the point that”. Since\n" +
                "\u202Bالماضي\u202C \u202Bالفعل\u202C does not change, the \u202Bحرف\u202C has no effect.")

        item5.setSpan(BulletSpan(15), 0, item5.length, 0)
        item6.setSpan(BulletSpan(15), 0, item6.length, 0)
       // item3.setSpan(BulletSpan(15), 0, item6.length, 0)

        val majzoomitem0001="\u202Bللمضارع\u202C \u202Bالجازمة\u202C \u202Bالحروف\u202C"
        val majzoomitem001 = "The follwoing Harf makes ther Verb Majzoom-Jussive"
        val majzoomitem01 = SpannableString(item0)
        majzoomitem01.setSpan(BulletSpan(15), 0, majzoomitem01.length, 0)
        val majzoomitem1=SpannableString("if -  (إِنْ)")
        majzoomitem1.setSpan(BulletSpan(15), 0, majzoomitem1.length, 0)
        val majzoomitem2 = SpannableString("( َلَمْ) did not")
        majzoomitem2.setSpan(BulletSpan(15), 0, majzoomitem2.length, 0)
        val majzoomitem3 = SpannableString("(لَمَّا) not yet")
        majzoomitem3.setSpan(BulletSpan(15), 0, majzoomitem3.length, 0)

        val majzoomitem4 = SpannableString("(وَلْ) and shoud")
        val majzoomitem5 = SpannableString("(فَلْ) then should")
        val majzoomitem6 = SpannableString("(لِ) Should")
        val majzoomitem7=SpannableString("1-harf \u202Bإن\u202C can sometimes affect two \u202Bفعل\u202C and gives an “if, then” meaning")
        val majzoomitem8=SpannableString("2.harf \u202Bلم\u202C gives a past-tense meaning (did not) despite the fact that it comes only before a \u202Bمضارع\u202C \u202Bفعل\u202C.")
        val majzoomitem9=SpannableString("3-Also know that \u202Bلما\u202C can come before a \u202Bماض\u202C \u202Bفعل\u202C. In this case, it means “when” and has no effect on\n" +
                "the \u202Bفعل\u202C.")
        majzoomitem4.setSpan(BulletSpan(15), 0, majzoomitem4.length, 0)
        majzoomitem5.setSpan(BulletSpan(15), 0, majzoomitem5.length, 0)
        majzoomitem6.setSpan(BulletSpan(15), 0, majzoomitem6.length, 0)
        majzoomitem7.setSpan(BulletSpan(15), 0, majzoomitem7.length, 0)
        majzoomitem8.setSpan(BulletSpan(15), 0, majzoomitem8.length, 0)
        majzoomitem9.setSpan(BulletSpan(15), 0, majzoomitem9.length, 0)


        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val dialog = builder.create()
        lifecycleScope.launch {
            runOnUiThread { dialog.show() }
            if (binding != null) {

                verbdetails = binding.verbdetails
                questionTextView = binding.questionTextView
                answerButtons = arrayOf(
                    binding.answerButton1, binding.answerButton2, binding.answerButton3,
                    binding.answerButton4, binding.answerButton5, binding.answerButton6,
                    binding.answerButton7, binding.answerButton8, binding.answerButton9,
                    binding.answerButton10, binding.answerButton11, binding.answerButton12,
                    binding.answerButton13, binding.answerButton14
                )

                for (button in answerButtons) {
                    button.setOnClickListener(this@ArabicVerbQuizActNew)
                }
                mansubnotes=binding.mansubnotes
                rootword=binding.rootword
                arabicsurahname = binding.arabicsurahname
                submitButton = binding.submitButton
                progressbar = binding.progressBar
                tvProgress = binding.tvProgress
                correctanswer = binding.correctanswer
                score = binding.score
                restartButton = binding.restart
                word_trans_textView = binding.wordTransTextView
                quran_verse = binding.quranVerse
                ayah_translation = binding.ayahTranslation
                showanswer=binding.showanswer
                majzoomnotes=binding.majzoomnotes
                restartButton.setOnClickListener(this@ArabicVerbQuizActNew)
                submitButton.setOnClickListener(this@ArabicVerbQuizActNew)
                verbdetails.setOnClickListener(this@ArabicVerbQuizActNew)
                showanswer.setOnClickListener(this@ArabicVerbQuizActNew)
                mansubnotes.text=TextUtils.concat(item001,"\n",item0,"\n",item1, "\n", item2,"\n",item3,"\n",item4,"\n",item5,"\n",item6)
                majzoomnotes.text=TextUtils.concat(majzoomitem0001,"\n",majzoomitem001, "\n", majzoomitem2,"\n",majzoomitem3,"\n",majzoomitem4,
                    "\n",majzoomitem5,"\n",majzoomitem6,"\n",majzoomitem7,"\n",majzoomitem8,"\n",majzoomitem9)
            }
            val mainViewModel: QuranViewModel by viewModels()
         //   mainViewModel = ViewModelProvider(this@ArabicVerbQuizActNew)[QuranVIewModel::class.java]
           val allverbs = withContext(Dispatchers.IO) { utils.getAllverbs() }
          //  val allRootVerbDetails = withContext(Dispatchers.IO) { utils.getAllRootVerbDetails() }
            cverb = allverbs?.random()!!
            // Update UI with the results on the main thread`
            withContext(Dispatchers.Main) {

                corpusSurahWord = mainViewModel.getQuranCorpusWbw(cverb.chapterno,cverb.verseno,cverb.wordno).value
                qurans=                 mainViewModel.getsurahayahVerses(corpusSurahWord!!.get(0).surah, corpusSurahWord!!.get(0).ayah).value


                val surahayahdetails = StringBuilder()
                val spannableString = NewSetWordSpan(
                    corpusSurahWord!!.get(0).tagone, corpusSurahWord!!.get(0).tagtwo, corpusSurahWord!!.get(0).tagthree, corpusSurahWord!!.get(0).tagfour, corpusSurahWord!!.get(0).tagfive,
                    corpusSurahWord!!.get(0).araone!!, corpusSurahWord!!.get(0).aratwo!!, corpusSurahWord!!.get(0).arathree!!, corpusSurahWord!!.get(0).arafour!!, corpusSurahWord!!.get(0).arafive!!
                )

                surahayahdetails.append(corpusSurahWord!!.get(0).ayah).append("  ").append("").append("   ")
                    .append(corpusSurahWord!!.get(0).surah).append(" ")
                val sbs = SpannableString(surahayahdetails)
                val charSequence = TextUtils.concat(spannableString, surahayahdetails)
                val correct = StringBuilder()
                if (!cverb.gendernumber.equals("SP:kaAn")) {
                    val genderNumberdetails =
                        QuranMorphologyDetails.getGenderNumberdetails(cverb.gendernumber)
                    val tensevoicemood = StringBuilder()
                    tensevoicemood.append(cverb.tense).append(":").append(cverb.voice).append(":")
                        .append(cverb.mood_kananumbers)
                    var thulathiName = ""
                    var thulathibab = ""
                    var formName = ""
                    if (cverb.form == "I") {
                        if (cverb.thulathibab!!.length > 1) {
                            val s = cverb.thulathibab!!.substring(0, 1)
                            thulathiName = QuranMorphologyDetails.getThulathiName(s).toString()
                        } else {
                            thulathibab =
                                QuranMorphologyDetails.getThulathiName(cverb.thulathibab).toString()
                        }



                        //   QuranMorphologyDetails.getThulathiName(cverb?.getThulathibab());
                    } else {
                        formName = QuranMorphologyDetails.getFormName(cverb.form)
                    }
                    if(corpusSurahWord!![0].detailsone!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailstwo!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailsthree!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailsfour!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailsfive!!.contains("SUFFIX|+n:EMPH")
                    ) {

                        correct.append(genderNumberdetails).append(" ").append(tensevoicemood).append(":").append("EMPH")
                            .append(thulathiName).append(":").append(thulathibab).append(formName)
                    }else{
                        correct.append(genderNumberdetails).append(" ").append(tensevoicemood).append(":")
                            .append(thulathiName).append(":").append(thulathibab).append(formName)
                    }
 

                } else {
                    val genderNumberdetails =
                        QuranMorphologyDetails.getGenderNumberdetails(cverb.mood_kananumbers)
                    val tensevoicemood = StringBuilder()
                    tensevoicemood.append(cverb.tense).append(":").append(cverb.voice).append(":")
                        .append(cverb.kana_mood)
                    var thulathiName = ""
                    var thulathibab = ""
                    var formName = ""
                    if (cverb.form == "I") {
                        if (cverb.thulathibab!!.length > 1) {
                            val s = cverb.thulathibab!!.substring(0, 1)
                            thulathiName = QuranMorphologyDetails.getThulathiName(s).toString()
                        } else {
                            thulathibab =
                                QuranMorphologyDetails.getThulathiName(cverb.thulathibab).toString()
                        }


                        //   QuranMorphologyDetails.getThulathiName(cverb?.getThulathibab());
                    } else {
                        formName = QuranMorphologyDetails.getFormName(cverb.form)
                    }

                    correct.append(genderNumberdetails).append(" ").append(tensevoicemood).append(":")
                        .append(thulathiName).append(":").append(thulathibab).append(formName)


                }

                arabicsurahname.text = surahayahdetails
                val buildstr = StringBuilder()
                /*     buildstr.append(cverb?.get(0)?.).append(cverb?.get(0)?.aratwo)
                         .append(cverb?.get(0)?.arathree).append(cverb?.get(0)?.arafour)
                         .append(cverb?.get(0)?.arafive).append(":").append(cverb?.get(0)?.en)*/
                verbdetails.text = spannableString
                word_trans_textView.text = corpusSurahWord!!.get(0).en
                correctanswer.text = correct
                val span = SpannableString(qurans!!.get(0).qurantext)
                val arabicword = corpusSurahWord!!.get(0).araone!!+corpusSurahWord!!.get(0).aratwo!!+ corpusSurahWord!!.get(0).arathree!!+
                        corpusSurahWord!!.get(0).arafour!!+ corpusSurahWord!!.get(0).arafive!!
                val indexOf = span.indexOf(arabicword)
                try {


                span.setSpan(
                    ForegroundColorSpan(Color.RED),
                    indexOf,
                    indexOf+arabicword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                } catch (e: IndexOutOfBoundsException) {
                    System.out.println(e.localizedMessage);
                }

                quran_verse.text =span
                ayah_translation.text= qurans?.get(0)!!.en_arberry
                rootword.text=cverb.root_a

                dialog.dismiss()
                showNextQuestion()
            }
        }











    }


    private fun defaultOptionsView() {
        for (option in answerButtons) { // Iterate over the answerButtons array
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }


    private fun selectedOptionsView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPositon = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background =
            ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)

    }

    private fun showNextQuestion() {
        //val currentQuestion = questions[0] // Access the first question in the list
        defaultOptionsView()
        if (questions.size == this.mcurrentPosition) {
            submitButton.text = getString(R.string.finish)
        } else {
            submitButton.text = getString(R.string.submit)
        }
        val currentQuestion = questions[this.mcurrentPosition - 1]
        for (i in 0 until currentQuestion.answers.size) {
            if (i < answerButtons.size) {
                answerButtons[i].text = currentQuestion.answers[i]
                answerButtons[i].visibility = View.VISIBLE
            }
        }
        for (i in currentQuestion.answers.size until answerButtons.size) {
            answerButtons[i].visibility = View.GONE
        }

        questionTextView.text = currentQuestion.question
        progressbar.progress = mcurrentPosition
        tvProgress.text = "$mcurrentPosition" + getString(R.string.back_slash) + progressbar.max






        if (this.mcurrentPosition - 1 == 0) {
            questionfirst()
        } else if (this.mcurrentPosition - 1 == 1) {

            questiontwo()
        } else if (this.mcurrentPosition - 1 == 2) {
            questionthree()
        } else if (this.mcurrentPosition - 1 == 3) {
            querstionfour()
        } else if (this.mcurrentPosition - 1 == 4) {
            questionfive()
        }



    }

    private fun questionfive() {
        if (cverb.thulathibab!!.isEmpty()) {
            if (answerButtons[1].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[4].correctAnswerIndex = 1

            } else if (answerButtons[2].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[4].correctAnswerIndex = 2

            } else if (answerButtons[3].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[4].correctAnswerIndex = 3

            } else if (answerButtons[4].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[4].correctAnswerIndex = 4

            } else if (answerButtons[5].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[4].correctAnswerIndex = 5

            } else if (answerButtons[6].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[4].correctAnswerIndex = 6

            } else if (answerButtons[7].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[4].correctAnswerIndex = 7

            } else if (answerButtons[8].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[4].correctAnswerIndex = 8

            } else if (answerButtons[9].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[4].correctAnswerIndex = 9

            }

        } else {

            questions[4].correctAnswerIndex = 0
        }

    }

    @Suppress("LocalVariableName")
    private fun querstionfour() {
        for (button in answerButtons) {
            button.visibility = View.GONE
        }

        val ThirdPersonSingularMasculine = "3MS"
        val ThirdPersonDualMasculine = "3MD"
        val ThirdPersonPluralMasculine = "3MP"

        val ThirdPersonSingularFeminine = "3FS"
        val ThirdPesonDualFeminine = "3FD"
        val ThirdPersonPluralFeminine = "3FP"


        val SecondPersonSingularMasculine = "2MS"
        val SecondPersonDualMasculine = "2MD"
        val SecondPersonPlularMasculine = "2MP"

        val SecondPersonSingularFeminine = "2FS"
        val SecondPersonDualFeminine = "2FD"
        val SecondPersonPluralFeminine = "2FP"

        val FirstPersonSingula = "1S"
        val FirstPersonPlural = "1P"
        if(!cverb.tense.equals("IMPV")){


            for (button in answerButtons) {
                button.visibility = View.VISIBLE
            }


        }else{
            for (i in 6..11) {
                answerButtons[i].visibility = View.VISIBLE
            }


        }
        if (ThirdPersonSingularMasculine.contains(
                cverb.gendernumber.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButtons[0].text)
            println(cverb.tense)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[0].correctAnswerIndex = 0
        }
        if (!cverb.gendernumber.equals("SP:kaAn")) {
            if (ThirdPersonDualMasculine.contains(
                    cverb.gendernumber.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.gendernumber)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 1
            }
            if (ThirdPersonPluralMasculine.contains(
                    cverb.gendernumber.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.gendernumber)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 2
            }
            if (ThirdPersonSingularFeminine.contains(
                    cverb.gendernumber.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.gendernumber)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 3
            }
            if (ThirdPesonDualFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.gendernumber)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 4
            }
            if (ThirdPersonPluralFeminine.contains(
                    cverb.gendernumber.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.gendernumber)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 5
            } else

                if (SecondPersonSingularMasculine.contains(
                        cverb.gendernumber.toString(),
                        ignoreCase = true
                    )
                ) {
                    println(answerButtons[0].text)
                    println(cverb.gendernumber)
                    //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                    questions[3].correctAnswerIndex = 6
                } else

                    if (SecondPersonDualMasculine.contains(
                            cverb.gendernumber.toString(),
                            ignoreCase = true
                        )
                    ) {
                        println(answerButtons[0].text)
                        println(cverb.gendernumber)
                        //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                        questions[3].correctAnswerIndex = 7
                    } else

                        if (SecondPersonPlularMasculine.contains(
                                cverb.gendernumber.toString(),
                                ignoreCase = true
                            )
                        ) {
                            println(answerButtons[0].text)
                            println(cverb.gendernumber)
                            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                            questions[3].correctAnswerIndex = 8
                        } else
                            if (SecondPersonSingularFeminine.contains(
                                    cverb.gendernumber.toString(),
                                    ignoreCase = true
                                )
                            ) {
                                println(answerButtons[0].text)
                                println(cverb.gendernumber)
                                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                questions[3].correctAnswerIndex = 9
                            } else
                                if (SecondPersonDualFeminine.contains(
                                        cverb.gendernumber.toString(),
                                        ignoreCase = true
                                    )
                                ) {
                                    println(answerButtons[0].text)
                                    println(cverb.gendernumber)
                                    //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                    questions[3].correctAnswerIndex = 10
                                } else
                                    if (SecondPersonPluralFeminine.contains(
                                            cverb.gendernumber.toString(),
                                            ignoreCase = true
                                        )
                                    ) {
                                        println(answerButtons[0].text)
                                        println(cverb.gendernumber)
                                        //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                        questions[3].correctAnswerIndex = 11
                                    } else
                                        if (FirstPersonSingula.contains(
                                                cverb.gendernumber.toString(),
                                                ignoreCase = true
                                            )
                                        ) {
                                            println(answerButtons[0].text)
                                            println(cverb.gendernumber)
                                            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                            questions[3].correctAnswerIndex = 12
                                        } else
                                            if (FirstPersonPlural.contains(
                                                    cverb.gendernumber.toString(),
                                                    ignoreCase = true
                                                )
                                            ) {
                                                println(answerButtons[0].text)
                                                println(cverb.gendernumber)
                                                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                                questions[3].correctAnswerIndex = 13
                                            }
        } else {
            if (ThirdPersonDualMasculine.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.mood_kananumbers)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 1
            }
            if (ThirdPersonPluralMasculine.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.mood_kananumbers)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 2
            }
            if (ThirdPersonSingularFeminine.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.mood_kananumbers)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 3
            }
            if (ThirdPesonDualFeminine.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.mood_kananumbers)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 4
            }
            if (ThirdPersonPluralFeminine.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.mood_kananumbers)
                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                questions[3].correctAnswerIndex = 5
            } else

                if (SecondPersonSingularMasculine.contains(
                        cverb.mood_kananumbers.toString(),
                        ignoreCase = true
                    )
                ) {
                    println(answerButtons[0].text)
                    println(cverb.mood_kananumbers)
                    //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                    questions[3].correctAnswerIndex = 6
                } else

                    if (SecondPersonDualMasculine.contains(
                            cverb.mood_kananumbers.toString(),
                            ignoreCase = true
                        )
                    ) {
                        println(answerButtons[0].text)
                        println(cverb.mood_kananumbers)
                        //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                        questions[3].correctAnswerIndex = 7
                    } else

                        if (SecondPersonPlularMasculine.contains(
                                cverb.mood_kananumbers.toString(),
                                ignoreCase = true
                            )
                        ) {
                            println(answerButtons[0].text)
                            println(cverb.mood_kananumbers)
                            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                            questions[3].correctAnswerIndex = 8
                        } else
                            if (SecondPersonSingularFeminine.contains(
                                    cverb.mood_kananumbers.toString(),
                                    ignoreCase = true
                                )
                            ) {
                                println(answerButtons[0].text)
                                println(cverb.mood_kananumbers)
                                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                questions[3].correctAnswerIndex = 9
                            } else
                                if (SecondPersonDualFeminine.contains(
                                        cverb.mood_kananumbers.toString(),
                                        ignoreCase = true
                                    )
                                ) {
                                    println(answerButtons[0].text)
                                    println(cverb.mood_kananumbers)
                                    //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                    questions[3].correctAnswerIndex = 10
                                } else
                                    if (SecondPersonPluralFeminine.contains(
                                            cverb.mood_kananumbers.toString(),
                                            ignoreCase = true
                                        )
                                    ) {
                                        println(answerButtons[0].text)
                                        println(cverb.mood_kananumbers)
                                        //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                        questions[3].correctAnswerIndex = 11
                                    } else
                                        if (FirstPersonSingula.contains(
                                                cverb.mood_kananumbers.toString(),
                                                ignoreCase = true
                                            )
                                        ) {
                                            println(answerButtons[0].text)
                                            println(cverb.mood_kananumbers)
                                            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                            questions[3].correctAnswerIndex = 12
                                        } else
                                            if (FirstPersonPlural.contains(
                                                    cverb.mood_kananumbers.toString(),
                                                    ignoreCase = true
                                                )
                                            ) {
                                                println(answerButtons[0].text)
                                                println(cverb.mood_kananumbers)
                                                //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
                                                questions[3].correctAnswerIndex = 13
                                            }

        }







        answerButtons[0].text
    }


    private fun questiontwo() {

        answerButtons[2].visibility = View.GONE
        answerButtons[3].visibility = View.GONE

        answerButtons[0].text
        if (answerButtons[0].text.contains(cverb.voice.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.tense)
            questions[1].correctAnswerIndex = 0

        }





        if (answerButtons[1].text.contains(cverb.voice.toString(), ignoreCase = true)) {
            println(answerButtons[1].text)
            println(cverb.tense)
            questions[1].correctAnswerIndex = 1
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }


    }

    private fun questionfirst() {

        val imperfect = "IMPF"
        val imperative = "IMPV"
        answerButtons[0].text
        if (answerButtons[0].text.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.tense)
            //     answerButtons[0].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[0].correctAnswerIndex = 0

        }


        if (imperfect.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButtons[1].text)
            println(cverb.tense)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[0].correctAnswerIndex = 1
        }



        if (imperative.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButtons[2].text)
            println(cverb.tense)
            questions[0].correctAnswerIndex = 2
            //   answerButtons[2].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }

    }

    private fun questionthree() {


        answerButtons[3].visibility = View.GONE
        answerButtons[2].visibility = View.VISIBLE
        if (!cverb.gendernumber.equals("SP:kaAn")) {

            if (answerButtons[0].text.contains(
                    cverb.kana_mood.toString(),
                    ignoreCase = true
                )
            ) {
                questions[2].correctAnswerIndex = 0

            } else if (answerButtons[1].text.contains(
                    cverb.kana_mood.toString(),
                    ignoreCase = true
                )
            ) {
                questions[2].correctAnswerIndex = 1

            } else if (answerButtons[2].text.contains(
                    cverb.kana_mood.toString(),
                    ignoreCase = true
                )
            ) {

                questions[2].correctAnswerIndex = 2
                //  answerButtons[2].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            }
        } else {
            val subj="MOOD:SUBJ"
            if (answerButtons[0].text.contains(
                    cverb.kana_mood.toString(),
                    ignoreCase = true
                )
            ) {
                questions[2].correctAnswerIndex = 0

            } else if (subj.contains(
                    cverb.kana_mood.toString(),
                    ignoreCase = true
                )
            ) {
                questions[2].correctAnswerIndex = 1

            } else if (answerButtons[2].text.contains(
                    cverb.kana_mood.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[2].correctAnswerIndex = 2
                //  answerButtons[2].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            }
        }


    }


    private fun reloadActivity() {
        window.setWindowAnimations(0)
        val originalIntent = intent
        overridePendingTransition(0, 0)
        startActivity(originalIntent)
        finish()
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
    }
   override  fun onClick(v: View?) {
        when (v?.id) {
            R.id.verbdetails->{
            var chapterno=0;
                var verse=0;
                var wordno=0
                var root=""
                var wazan=""
                var isaugmented=""
                var verbmood=""
                if(cverb.thulathibab!!.isEmpty()){
                    wazan= cverb.form.toString()
                 root= cverb.root_a.toString()
                     isaugmented="yes"
                     chapterno=cverb.chapterno
                     verse=cverb.verseno
                     wordno=cverb.wordno
                    verbmood= cverb.mood_kananumbers!!
                    convertForms(wazan).toString()
                    val dataBundle = Bundle()
                    //      ArrayList arrayList = ThulathiMazeedConjugatonList.get(position);
                    //   arrayList.get(0).ge
                    if(cverb.mood_kananumbers.equals("IND")){
                        verbmood="Indicative"
                    }else if(cverb.mood_kananumbers.equals("SUBJ")){
                        verbmood="Subjunctive"
                    }else if(cverb.mood_kananumbers.equals("JUS")){
                        verbmood="Jussive"
                    } else if(corpusSurahWord!![0].detailsone!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailstwo!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailsthree!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailsfour!!.contains("SUFFIX|+n:EMPH")
                        || corpusSurahWord!![0].detailsfive!!.contains("SUFFIX|+n:EMPH")
                    ){
                        verbmood="Emphatic"
                    }

                        dataBundle.putString(VERBTYPE, "mazeed")


                        dataBundle.putString(VERBMOOD, verbmood)




                    dataBundle.putString(QURAN_VERB_WAZAN, form.toString())
                    dataBundle.putString(QURAN_VERB_ROOT, root)
                    val intent = Intent(this, ConjugatorTabsActivity::class.java)
                    intent.putExtras(dataBundle)
                    startActivity(intent)
                }else{
                  val  mujarradwazan= cverb.thulathibab!!
                    val vb= VerbWazan()

                    isaugmented="no"
                    chapterno=cverb.chapterno
                    verse=cverb.verseno
                    wordno=cverb.wordno
                    verbmood= cverb.mood_kananumbers!!
                    if(cverb.mood_kananumbers.equals("IND")){
                        verbmood="Indicative"
                    }else if(cverb.mood_kananumbers.equals("SUBJ")){
                        verbmood="Subjunctive"
                    }else if(cverb.mood_kananumbers.equals("JUS")){
                        verbmood="Jussive"
                    }
                    root= cverb.root_a.toString()
                    vb.root = root
                    vb.wazan = mujarradwazan
                    val dataBundle = Bundle()
                    //      ArrayList arrayList = ThulathiMazeedConjugatonList.get(position);
                    //   arrayList.get(0).ge

                    dataBundle.putString(VERBTYPE, "mujarrad")


                    dataBundle.putString(VERBMOOD, verbmood)

                    dataBundle.putString(QURAN_VERB_WAZAN, vb.wazan)
                    dataBundle.putString(QURAN_VERB_ROOT, root)
                    val intent = Intent(this, ConjugatorTabsActivity::class.java)
                    intent.putExtras(dataBundle)
                    startActivity(intent)

                }
            }
            R.id.restart -> reloadActivity()
            R.id.showanswer ->{

                correctanswer.visibility=  View.VISIBLE
            }
            R.id.submit_button -> {

                    if (mSelectedOptionPositon == 0) {

                        mcurrentPosition++
                        when {
                            mcurrentPosition <= questions.size -> {
                                if (mcurrentPosition == 3 && cverb.kana_mood!!.isEmpty()&& cverb.gendernumber.equals("SP:kaAn")) {
                                    mcurrentPosition++
                                    totalquestion = 4
                                }
                                showNextQuestion()
                            }

                            else -> {
                                score.text = "your Score is $mcurrectanswer out of $totalquestion"
                                restartButton.visibility = View.VISIBLE

                            }
                        }
                    } else {

                        val question = questions[mcurrentPosition - 1]
                        if (question.correctAnswerIndex + 1 != mSelectedOptionPositon) {
                            answerView(mSelectedOptionPositon, R.drawable.wrong_option_border_bg)
                        } else {
                            mcurrectanswer++
                        }
                        answerView(question.correctAnswerIndex + 1, R.drawable.correct_option_border_bg)
                        if (mcurrentPosition == questions.size) {
                            submitButton.text = "Finish"
                        } else {
                            submitButton.text = "Go to Next Question"
                        }

                    }
                    mSelectedOptionPositon = 0
                }

            else -> {

                for (i in buttonIds.indices) {
                    if (v?.id == buttonIds[i]) {
                        selectedOptionsView(answerButtons[i], i + 1)
                        break
                    }
                }


            }
        }
    }
    private fun convertForms(mform: String?) {
        when (mform) {
            "IV" -> form = 1
            "II" -> form = 2
            "III" -> form = 3
            "VII" -> form = 4
            "VIII" -> form = 5
            "VI" -> form = 7
            "V" -> form = 8
            "X" -> form = 9

        }
    }

    fun onClicksss(v: View?) {
        when (v?.id) {
            R.id.answer_button1 -> {
                selectedOptionsView(answerButtons[0], 1)
            }

            R.id.answer_button2 -> {
                selectedOptionsView(answerButtons[1], 2)
            }

            R.id.answer_button3 -> {
                selectedOptionsView(answerButtons[2], 3)
            }

            R.id.answer_button4 -> {
                selectedOptionsView(answerButtons[3], 4)
            }

            R.id.answer_button5 -> {
                selectedOptionsView(answerButtons[4], 5)
            }

            R.id.answer_button6 -> {
                selectedOptionsView(answerButtons[5], 6)
            }

            R.id.answer_button7 -> {
                selectedOptionsView(answerButtons[6], 7)
            }

            R.id.answer_button8 -> {
                selectedOptionsView(answerButtons[7], 8)
            }

            R.id.answer_button9 -> {
                selectedOptionsView(answerButtons[8], 9)
            }

            R.id.answer_button10 -> {
                selectedOptionsView(answerButtons[9], 10)
            }

            R.id.answer_button11 -> {
                selectedOptionsView(answerButtons[10], 11)
            }

            R.id.answer_button12 -> {
                selectedOptionsView(answerButtons[11], 12)
            }

            R.id.answer_button13 -> {
                selectedOptionsView(answerButtons[12], 13)
            }

            R.id.answer_button14 -> {
                selectedOptionsView(answerButtons[13], 14)
            }

            R.id.restart -> {

                reloadActivity()

            }


            R.id.submit_button -> {
                if (mSelectedOptionPositon == 0) {

                    mcurrentPosition++
                    when {
                        mcurrentPosition <= questions.size -> {
                            if (mcurrentPosition == 3 && cverb.kana_mood!!.isEmpty()) {
                                mcurrentPosition++
                                totalquestion = 4
                            }
                            showNextQuestion()
                        }

                        else -> {
                            score.text = "your Score is $mcurrectanswer out of $totalquestion"
                            restartButton.visibility = View.VISIBLE

                        }
                    }
                } else {

                    val question = questions[mcurrentPosition - 1]
                    if (question.correctAnswerIndex + 1 != mSelectedOptionPositon) {
                        answerView(mSelectedOptionPositon, R.drawable.wrong_option_border_bg)
                    } else {
                        mcurrectanswer++
                    }
                    answerView(question.correctAnswerIndex + 1, R.drawable.correct_option_border_bg)
                    if (mcurrentPosition == questions.size) {
                        submitButton.text = getString(R.string.finish)
                    } else {
                        submitButton.text = getString(R.string.next_question)
                    }

                }
                mSelectedOptionPositon = 0
            }
        }
    }

    private fun answerViewdd(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                answerButtons[0].background = ContextCompat.getDrawable(this, drawableView)
            }

            2 -> {
                answerButtons[1].background = ContextCompat.getDrawable(this, drawableView)
            }

            3 -> {
                answerButtons[2].background = ContextCompat.getDrawable(this, drawableView)
            }

            4 -> {
                answerButtons[3].background = ContextCompat.getDrawable(this, drawableView)
            }

            5 -> {
                answerButtons[4].background = ContextCompat.getDrawable(this, drawableView)
            }

            6 -> {
                answerButtons[5].background = ContextCompat.getDrawable(this, drawableView)
            }

            7 -> {
                answerButtons[6].background = ContextCompat.getDrawable(this, drawableView)
            }

            8 -> {
                answerButtons[7].background = ContextCompat.getDrawable(this, drawableView)
            }

            9 -> {
                answerButtons[8].background = ContextCompat.getDrawable(this, drawableView)
            }

            10 -> {
                answerButtons[9].background = ContextCompat.getDrawable(this, drawableView)
            }

            11 -> {
                answerButtons[10].background = ContextCompat.getDrawable(this, drawableView)
            }

            12 -> {
                answerButtons[11].background = ContextCompat.getDrawable(this, drawableView)
            }

            13 -> {
                answerButtons[12].background = ContextCompat.getDrawable(this, drawableView)
            }

            14 -> {
                answerButtons[13].background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        if (answer in 1..answerButtons.size) {
            answerButtons[answer - 1].background = ContextCompat.getDrawable(this, drawableView)
        }
    }




}


/*
     val  questionsd = arrayOf(

            TenseQuestion(1, verb = cverb.toString(), TenseData(options = listOf("Perfect", "Imperfect", "Imperative"))),
            VoiceQuestion(2, verb = cverb.toString(), VoiceData(options = listOf("Active", "Passive"))),
            MoodQuestion(3, verb = cverb.toString(), MoodData(options = listOf("Subjunctive", "Jussive", "Indicative"))),
            PatternQuestion(5, verb = cverb.toString(), PatternData(options = listOf(   "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4", "Pattern 5",
                "Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9"))),
            GenderNumberQuestion(
                4,
                verb = cverb.toString(),
                GenderNumberData(
                    options = listOf(
                        "ThirdPersonSingularMasculine",
                        "ThirdPersonDualMasculine",
                        "ThirdPersonPluralMasculine",

                        "ThirdPersonSingularFeminine",
                        "ThirdPesonDualFeminine",
                        "ThirdPersonPluralFeminine",


                        "SecondPersonSingularMasculine",
                        "SecondPersonDualMasculine",
                        "SecondPersonPlularMasculine",
                        "SecondPersonSingularFeminine",
                        "SecondPersonDualFeminine",
                        "SecondPersonPluralFeminine",

                        "FirstPersonSingula",
                        "FirstPersonPlural",
                        "First Person Singular Masculine"
                    )
                )
            )

        )
 */