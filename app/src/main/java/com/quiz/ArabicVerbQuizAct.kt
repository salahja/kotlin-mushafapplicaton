@file:Suppress("LocalVariableName")

package com.quiz


import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.BottomOptionDialog
import com.example.mushafconsolidated.Entities.RootVerbDetails
import com.example.mushafconsolidated.Entities.VerbWazan
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.databinding.FragmentArabicVerbQuizBinding
import com.example.mushafconsolidated.fragments.QuranMorphologyDetails
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet
import com.example.mushafconsolidated.model.NewQuranCorpusWbw
import com.example.mushafconsolidated.model.NewVerbCorpusWbw
import com.example.utility.CorpusUtilityorig.Companion.NewSetWordSpan
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.color.DynamicColors
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sj.conjugator.activity.BaseActivity
import org.sj.conjugator.activity.ConjugatorTabsActivity

class ArabicVerbQuizAct : BaseActivity(), View.OnClickListener {

    open var form = 0
    private var totalquestion: Int = 5
    private lateinit var answerButtons: Array<TextView>

    //private lateinit var cverb: wbwentity
    private lateinit var cverb: RootVerbDetails

    // private var _binding: FragmentArabicVerbQuizBinding? = null
    lateinit var binding: FragmentArabicVerbQuizBinding

    private lateinit var questionTextView: TextView
    private lateinit var verbdetails: Chip
    private lateinit var arabicsurahname: MaterialTextView
    private lateinit var correctanswer: MaterialTextView
    private lateinit var score: MaterialTextView


    private lateinit var submitButton: TextView
    private lateinit var restartButton: MaterialButton
    private lateinit var word_trans_textView: TextView
    private lateinit var quran_verse: TextView
    private lateinit var ayah_translation: TextView

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
            listOf("Indictive", "Subjunctive", "Jussive"),
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
        var qurancorpusarray = ArrayList<NewVerbCorpusWbw>()

        currenttheme =
            PreferenceManager.getDefaultSharedPreferences(this).getString("themepref", "dark")
        switchTheme(currenttheme) // Call switchTheme before super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this.application)

        super.onCreate(savedInstanceState)
        binding = FragmentArabicVerbQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val utils = Utils(this)
     /*    val allverbs = utils.getAllverbs()
        // cverb = allverbs.random()
        val allRootVerbDetails = utils.getAllRootVerbDetails()
      //  val allRootVerbDetails = utils.getRootVerbDetailsbyRootword("كون")
        cverb = allRootVerbDetails?.random()!!
*/
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
                    button.setOnClickListener(this@ArabicVerbQuizAct)
                }
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
                restartButton.setOnClickListener(this@ArabicVerbQuizAct)
                submitButton.setOnClickListener(this@ArabicVerbQuizAct)
                verbdetails.setOnClickListener(this@ArabicVerbQuizAct)

            }
          //  val allverbs = withContext(Dispatchers.IO) { utils.getAllverbs() }
            val allRootVerbDetails = withContext(Dispatchers.IO) { utils.getAllRootVerbDetails() }

            // Update UI with the results on the main thread`
            withContext(Dispatchers.Main) {
                cverb = allRootVerbDetails?.random()!!



                val surahayahdetails = StringBuilder()
                val spannableString = NewSetWordSpan(
                    cverb.tagone, cverb.tagtwo, cverb.tagthree, cverb.tagfour, cverb.tagfive,
                    cverb.araone!!, cverb.aratwo!!, cverb.arathree!!, cverb.arafour!!, cverb.arafive!!
                )
                surahayahdetails.append(cverb.ayah).append("  ").append(cverb.namearabic).append("   ")
                    .append(cverb.surah).append(" ")
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

                    correct.append(genderNumberdetails).append(" ").append(tensevoicemood).append(":")
                        .append(thulathiName).append(":").append(thulathibab).append(formName)

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
                word_trans_textView.text = cverb.en
                correctanswer.text = correct

                quran_verse.text = cverb.qurantext
                ayah_translation.text=cverb.en_arberry

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
            button.visibility = View.VISIBLE
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
                 root= cverb.rootarabic.toString()
                     isaugmented="yes"
                     chapterno=cverb.surah
                     verse=cverb.ayah
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
                    }else if(cverb.mood_kananumbers.equals("JUSS")){
                        verbmood="Jussive"
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
                    chapterno=cverb.surah
                    verse=cverb.ayah
                    wordno=cverb.wordno
                    verbmood= cverb.mood_kananumbers!!
                    if(cverb.mood_kananumbers.equals("IND")){
                        verbmood="Indicative"
                    }else if(cverb.mood_kananumbers.equals("SUBJ")){
                        verbmood="Subjunctive"
                    }else if(cverb.mood_kananumbers.equals("JUSS")){
                        verbmood="Jussive"
                    }
                    root= cverb.rootarabic.toString()
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
           /*     for (i in 1..14) {
                    if (v?.id == resources.getIdentifier("answer_button$i", "id", packageName)) {
                        selectedOptionsView(answerButtons[i - 1], i)
                        return // Exit the loop and method once found
                    }
                }*/
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