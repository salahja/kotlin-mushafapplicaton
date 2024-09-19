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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.databinding.FragmentArabicVerbQuizBinding
import com.google.android.material.textview.MaterialTextView

class ArabicVerbQuizFragment : Fragment(), OnClickListener {


    //private lateinit var verbcorpus: wbwentity
    private lateinit var cverb: VerbCorpus
    private var _binding: FragmentArabicVerbQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: TextView
    private lateinit var verbdetails: MaterialTextView
    private lateinit var answerButton2: TextView
    private lateinit var answerButton3: TextView
    private lateinit var answerButton4: TextView
    private lateinit var submitButton: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressbar: ProgressBar
    private var mcurrentPosition: Int = 1
    private var mSelectedOptionPositon: Int = 0
    private var mQuestionList: ArrayList<Question>? = null

    class Question(
        val id: Int,
        val question: String,
        val answers: List<String>,
        var correct:Int

    )

    val questions = listOf(
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
0
            ),
        Question(
            5,
            "What is the Patter(form) of the VERB",
            listOf(
                "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4", "Pattern 5",
                "Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9"
            ),
0
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
        submitButton.setOnClickListener(this)
        val buildstr = StringBuilder()
        buildstr.append(verbcorpus?.get(0)?.araone).append(verbcorpus?.get(0)?.aratwo)
            .append(verbcorpus?.get(0)?.arathree).append(verbcorpus?.get(0)?.arafour)
            .append(verbcorpus?.get(0)?.arafive).append(":").append(verbcorpus?.get(0)?.en)
        verbdetails.text = buildstr

        showNextQuestion()


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

    private fun showNextQuestion() {
        //val currentQuestion = questions[0] // Access the first question in the list
        defaultOptionsView()
        if (questions.size == this.mcurrentPosition) {
            submitButton.text = "finish"
        }else{
            submitButton.text="Submit"
        }
        val currentQuestion = questions[this.mcurrentPosition-1]
        questionTextView.text = currentQuestion.question
        progressbar.progress = mcurrentPosition
        tvProgress.text = "$mcurrentPosition" + "/" + progressbar.max

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
        if (this.mcurrentPosition -1== 0) {
            questionfirst()
        } else if (this.mcurrentPosition-1 == 1) {

            questiontwo()
        } else if (this.mcurrentPosition-1 == 2) {
            questionthree()
        }
/*        submitButton.setOnClickListener(View.OnClickListener {
            this.currentPosition++
           if(currentPosition==questions.size) {
              submitButton.text="Finish"
           }else{
               submitButton.text="Go to Next Question"
               showNextQuestion()
           }
        })*/


    }


    private fun questiontwo() {

        answerButton3.visibility = View.GONE
        answerButton4.visibility = View.GONE

        answerButton1.text
        if (answerButton1.text.contains(cverb.voice.toString(), ignoreCase = true)) {
            println(answerButton1.text)
            println(cverb.tense)
            questions[1].correct=0

        }





        if (answerButton2.text.contains(cverb.voice.toString(), ignoreCase = true)) {
            println(answerButton2.text)
            println(cverb.tense)
            questions[1].correct=1
          //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }



    }

    private fun questionfirst() {

        var compare = "Imperfect"
        answerButton1.text
        if (answerButton1.text.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButton1.text)
            println(cverb.tense)
       //     answerButton1.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[0].correct=0

        }


         if (compare.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButton1.text)
            println(cverb.tense)
          //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
             questions[0].correct=1
        }



      if (answerButton3.text.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButton1.text)
            println(cverb.tense)
             questions[0].correct=2
         //   answerButton3.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }

    }

     fun questionthree() {


        answerButton4.visibility = View.GONE
        answerButton3.visibility = View.VISIBLE

        answerButton1.text
        if (answerButton1.text.contains(
                cverb.mood_kananumbers.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButton1.text)
            println(cverb.tense)
            questions.get(2).correct=0
           // answerButton1.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }





        if (answerButton2.text.contains(
                cverb.mood_kananumbers.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButton1.text)
            println(cverb.tense)
            questions[2].correct=1
         //   answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }




        if (answerButton3.text.contains(
                cverb.mood_kananumbers.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButton1.text)
            println(cverb.tense)
            questions[2].correct=2
          //  answerButton3.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.answer_button1 -> {
                selectedOptionsView(answerButton1, 1)
            }

            R.id.answer_button2 -> {
                selectedOptionsView(answerButton2, 2)
            }

            R.id.answer_button3 -> {
                selectedOptionsView(answerButton3, 3)
            }

            R.id.answer_button1 -> {
                selectedOptionsView(answerButton4, 4)
            }
            R.id.submit_button -> {
                if(mSelectedOptionPositon==0) {

                    mcurrentPosition++
                    when {
                        mcurrentPosition <= questions.size -> {
                            showNextQuestion()
                        }

                        else -> {
                            Toast.makeText(requireContext(), "quiz completed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }else{

                    val question=questions[mcurrentPosition-1]
                    if(question.correct+1!=mSelectedOptionPositon){
                        answerView(mSelectedOptionPositon,R.drawable.wrong_option_border_bg)
                    }
                    answerView(question.correct+1,R.drawable.correct_option_border_bg)
                    if(mcurrentPosition==questions.size){
                        submitButton.text="Finish"
                    }else{
                        submitButton.text="Go to Next Question"
                    }
                  /*  if(mcurrentPosition-1==0){
                       answerView()
                    }else if(mcurrentPosition-1==1){
                        questiontwo()
                    }else if(mcurrentPosition-1==2){
                        questionthree()
                    }*/
                }
                mSelectedOptionPositon=0
            }
        }
    }
    fun answerView(answer:Int,drawableView:Int){
        when(answer){
            1->{
                answerButton1.background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            2->{
                answerButton2.background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            3->{
                answerButton3.background=ContextCompat.getDrawable(requireContext(),drawableView)
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