package com.quiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.databinding.FragmentArabicVerbQuizBinding
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView

class ArabicVerbQuizFragmentTwo : Fragment(), OnClickListener {


    //private lateinit var verbcorpus: wbwentity
    private lateinit var cverb: VerbCorpus
    private var _binding: FragmentArabicVerbQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: TextView
    private lateinit var verbdetails: Chip
    private lateinit var answerButton2: TextView
    private lateinit var answerButton3: TextView
    private lateinit var answerButton4: TextView
    private lateinit var submitButton: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressbar: ProgressBar
    private var currentPosition: Int = 0
    private var mSelectedOptionPositon: Int = 0
    private var mQuestionList: ArrayList<Question>? = null

    class Question(
        val id: Int,
        val question: String,
        val answers: List<String>

    )

    val questions = listOf(
        Question(
            1,
            "What is Tense of the verb?",
            listOf("Perfect", "Imperfect", "Imperative"),

        ),
        Question(
            2,
            "What is the Voice of the Verb?",
            listOf("Active", "Passive"),

        ),
        Question(
            3,
            "What is the Mood of the VERB",
            listOf("Indictive", "Subjunctive", "Jussive"),

        ),
        Question(
            4,
            "What is the Gender and Number of Verb?",
            listOf(
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
            ),

        ),
        Question(
            5,
            "What is the Patter(form) of the VERB",
            listOf(
                "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4", "Pattern 5",
                "Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9"
            ),

        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArabicVerbQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val utils = Utils(requireContext())
        val allverbs = utils.getAllverbs()
        cverb = allverbs.random()
        val verbcorpus = utils.getwbwQuranBySurahAyahWord(
            cverb.chapterno,
            cverb.verseno,
            cverb.wordno
        )
        verbdetails = binding.verbdetails
        questionTextView = binding.questionTextView
        answerButton1 = binding.answerButton1
        answerButton2 = binding.answerButton2
        answerButton3 = binding.answerButton3
        answerButton4 = binding.answerButton4
        submitButton = binding.submitButton
        progressbar = binding.progressBar
        tvProgress = binding.tvProgress
        answerButton1.setOnClickListener(this)
        answerButton2.setOnClickListener(this)
        answerButton3.setOnClickListener(this)
        answerButton4.setOnClickListener(this)
        val buildstr = StringBuilder()
        buildstr.append(verbcorpus?.get(0)?.araone).append(verbcorpus?.get(0)?.aratwo)
            .append(verbcorpus?.get(0)?.arathree).append(verbcorpus?.get(0)?.arafour)
            .append(verbcorpus?.get(0)?.arafive).append(":").append(verbcorpus?.get(0)?.en)
        verbdetails.text = buildstr
        progressbar.progress = currentPosition
        tvProgress.text = "$currentPosition" + "/" + progressbar.max
        showNextQuestion(currentPosition)



    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, answerButton1)
        options.add(1, answerButton2)
        options.add(2, answerButton3)
        options.add(3, answerButton4)
        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = android.graphics.Typeface.DEFAULT
            option.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionsView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPositon = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selected_option_border_bg)

    }

    private fun showNextQuestion(mSelectedOptionPositon: Int) {
        //val currentQuestion = questions[0] // Access the first question in the list
        defaultOptionsView()
        if (questions.size == this.currentPosition) {
            submitButton.text = "finish"
        }
        val currentQuestion = questions[this.currentPosition]
        questionTextView.text = currentQuestion.question

// Ensure you are only setting as many answer buttons as there are options
        if (currentQuestion.answers.size > 0) {
            answerButton1.text = currentQuestion.answers[0]
        }

        if (currentQuestion.answers.size > 1) {
            answerButton2.text = currentQuestion.answers[1]
        }

        if (currentQuestion.answers.size > 2) {
            answerButton3.text = currentQuestion.answers[2]
        }

// Assuming there is a fourth button and enough answers
        if (currentQuestion.answers.size > 3) {
            answerButton4.text = currentQuestion.answers[3]
        }
        if (this.currentPosition == 0) {
            questionfirst(1)
        } else if (this.currentPosition == 1) {

            questiontwo(1)
        } else if (this.currentPosition == 2) {
            questionthree(1)
        }
        submitButton.setOnClickListener(View.OnClickListener {

            showNextQuestion(this.currentPosition++)
        })


    }



    private fun questiontwo(selectedAnswerIndex: Int) {

            answerButton3.visibility = View.GONE
            answerButton4.visibility = View.GONE
            answerButton1.setOnClickListener {
                answerButton1.text
                if (answerButton1.text.contains(cverb.voice.toString(), ignoreCase = true)) {
                    println(answerButton1.text)
                    println(cverb.tense)

                }
            }

            answerButton2.setOnClickListener {

                if (answerButton2.text.contains(cverb.voice.toString(), ignoreCase = true)) {
                    println(answerButton2.text)
                    println(cverb.tense)

                }
            }

    }

    private fun questionfirst(selectedAnswerIndex: Int) {

        val selectedAnswerText = questions[currentPosition].answers[selectedAnswerIndex]
        if (selectedAnswerText.contains(cverb.tense.toString(), ignoreCase = true)) {
            println("Correct Answer for Question 1: $selectedAnswerText")
        } else {
            println("Incorrect Answer for Question 1")
        }

            answerButton1.setOnClickListener {
                answerButton1.text
                if (answerButton1.text.contains(cverb.tense.toString(), ignoreCase = true)) {
                    println(answerButton1.text)
                    println(cverb.tense)

                }
            }
            answerButton2.setOnClickListener {
                var compare = "Imperfect"
                if (answerButton2.text.equals("Imperfect")) {
                    compare = "IMPF"
                }
                if (compare.contains(cverb.tense.toString(), ignoreCase = true)) {
                    println(answerButton1.text)
                    println(cverb.tense)

                }
            }
            answerButton3.setOnClickListener {

                if (answerButton3.text.contains(cverb.tense.toString(), ignoreCase = true)) {
                    println(answerButton1.text)
                    println(cverb.tense)

                }
            }

    }
    private fun questionthree(selectedAnswerIndex: Int) {


        answerButton4.visibility = View.GONE
        answerButton3.visibility = View.VISIBLE
        answerButton1.setOnClickListener {
            answerButton1.text
            if (answerButton1.text.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButton1.text)
                println(cverb.tense)

            }
        }

        answerButton2.setOnClickListener {

            if (answerButton2.text.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButton1.text)
                println(cverb.tense)

            }
        }
        answerButton3.setOnClickListener {

            if (answerButton3.text.contains(
                    cverb.mood_kananumbers.toString(),
                    ignoreCase = true
                )
            ) {
                println(answerButton1.text)
                println(cverb.tense)

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onClick(v: View?) {
        val selectedAnswerIndex = when (v?.id) {
            R.id.answer_button1 -> 0
            R.id.answer_button2 -> 1
            R.id.answer_button3 -> 2
            R.id.answer_button4 -> 3
            else -> -1
        }

        if (selectedAnswerIndex != -1) {
            when (currentPosition) {
                0 -> questionfirst(selectedAnswerIndex)
                1 -> questiontwo(selectedAnswerIndex)
                2 -> questionthree(selectedAnswerIndex)
                // Add more cases for other questions
            }
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