package com.quiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mushafconsolidated.Entities.RootVerbDetails



import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.databinding.FragmentArabicVerbQuizBinding
import com.example.mushafconsolidated.fragments.QuranMorphologyDetails
import com.example.utility.CorpusUtilityorig.Companion.NewSetWordSpan
import com.google.android.material.textview.MaterialTextView

class ArabicVerbQuizFragment : Fragment(), OnClickListener {



    private lateinit var answerButtons: Array<TextView>
    //private lateinit var cverb: wbwentity
    private lateinit var cverb: RootVerbDetails
    private var _binding: FragmentArabicVerbQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: TextView
    private lateinit var verbdetails: MaterialTextView
    private lateinit var answerButton2: TextView
    private lateinit var answerButton3: TextView
    private lateinit var answerButton4: TextView
    private lateinit var arabicsurahname:MaterialTextView
    private lateinit var correctanswer:MaterialTextView
    private lateinit var score:MaterialTextView
    private lateinit var answerButton5 : TextView //answerButton1
    private lateinit var answerButton6 : TextView //answerButton2
    private lateinit var answerButton7 : TextView //answerButton3
    private lateinit var answerButton8 : TextView //answerButton4

    private lateinit var answerButton9 : TextView //answerButton1
    private lateinit var answerButton10 : TextView //answerButton2
    private lateinit var answerButton11 : TextView //answerButton3
    private lateinit var answerButton12: TextView //answerButton4
    private lateinit var answerButton13: TextView //answerButton4






    private lateinit var submitButton: TextView





    private lateinit var tvProgress: TextView
    private lateinit var progressbar: ProgressBar
    private var mcurrentPosition: Int = 1
    private var mSelectedOptionPositon: Int = 0
    private var mcurrectanswer:Int =0
    private var mQuestionList: ArrayList<Question>? = null
    enum class QuestionType { TENSE, VOICE, MOOD, GENDER_NUMBER, PATTERN }
    class Question(
        val id: Int,
        val question: String,
        val answers: List<String>,
        var correctAnswerIndex:Int

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

            ),
0
            ),
        Question(
            5,
            "What is the Patter(form) of the VERB",
            listOf(
                "Form 1(Unaugmented)", "Form II(فَعَّلَ)", "Form III(فَاعَلَ)", "Form IV(أَفْعَلَ)", "Form V(تَفَعَّلَ)",
                "Form VI(تَفَاعَلَ)", "Form VII(إِنْفَعَلَ-)", "Form VIII(إِفْتَعَلَ)", "Form IX(إِفْعَلَّ)","Form X(إِسْتَفْعَلَ)"
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
       // cverb = allverbs.random()
        val allRootVerbDetails = utils.getAllRootVerbDetails()
         cverb = allRootVerbDetails?.random()!!
       /* val cverb = utils.getwbwQuranBySurahAyahWord(
            cverb.chapterno,
            cverb.verseno,
            cverb.wordno
        )*/
        if (_binding != null) {
            val binding = _binding!!
            verbdetails = binding.verbdetails
            questionTextView = binding.questionTextView
            answerButtons = arrayOf(
                binding.answerButton1, binding.answerButton2, binding.answerButton3,
                binding.answerButton4, binding.answerButton5, binding.answerButton6,
                binding.answerButton7, binding.answerButton8, binding.answerButton9,
                binding.answerButton10, binding.answerButton11, binding.answerButton12,
                binding.answerButton13,binding.answerButton14
            )

            for (button in answerButtons) {
                button.setOnClickListener(this)
            }
            arabicsurahname=binding.arabicsurahname
            submitButton = binding.submitButton
            progressbar = binding.progressBar
            tvProgress = binding.tvProgress
            correctanswer=binding.correctanswer
            score=binding.score
            submitButton.setOnClickListener(this)
        }




  /*      answerButton1.setOnClickListener(this)
        answerButton2.setOnClickListener(this)
        answerButton3.setOnClickListener(this)
        answerButton4.setOnClickListener(this)
        submitButton.setOnClickListener(this)*/
        val sb = StringBuilder()
        val spannableString = NewSetWordSpan(
            cverb?.tagone, cverb?.tagtwo, cverb?.tagthree, cverb?.tagfour, cverb?.tagfive,
            cverb?.araone!!, cverb?.aratwo!!, cverb?.arathree!!, cverb?.arafour!!, cverb?.arafive!!
        )
        sb.append(cverb?.ayah).append("  ").append(cverb?.namearabic).append("   ")
            .append(cverb?.surah).append(" ").append(cverb?.en)
        val sbs = SpannableString(sb)
        val charSequence = TextUtils.concat(spannableString, sb)


        val genderNumberdetails =
            QuranMorphologyDetails.getGenderNumberdetails(cverb?.gendernumber)
        var tensevoicemood = StringBuilder()
        tensevoicemood.append(cverb?.tense).append(":").append(cverb?.voice).append(":")
            .append(cverb?.mood_kananumbers)
         var thulathiName=""
        var thulathibab=""
        var formName=""
        if (cverb?.form == "I") {
            if (cverb?.thulathibab!!.length > 1) {
                val s = cverb?.thulathibab!!.substring(0, 1)
                 thulathiName = QuranMorphologyDetails.getThulathiName(s).toString()
            } else {
                 thulathibab = QuranMorphologyDetails.getThulathiName(cverb?.thulathibab).toString()
            }


            //   QuranMorphologyDetails.getThulathiName(cverb?.getThulathibab());
        } else {
             formName = QuranMorphologyDetails.getFormName(cverb?.form)
        }
        val correct=StringBuilder()
        correct.append(genderNumberdetails).append(" ").append(tensevoicemood).append(":").append(thulathiName).append(":").append(thulathibab).append(formName)


        arabicsurahname.text = charSequence
        arabicsurahname.text = cverb.namearabic
        val buildstr = StringBuilder()
   /*     buildstr.append(cverb?.get(0)?.).append(cverb?.get(0)?.aratwo)
            .append(cverb?.get(0)?.arathree).append(cverb?.get(0)?.arafour)
            .append(cverb?.get(0)?.arafive).append(":").append(cverb?.get(0)?.en)*/
        verbdetails.text = spannableString
        correctanswer.text=correct
        showNextQuestion()


    }

    private fun defaultOptionsView() {
        for (option in answerButtons) { // Iterate over the answerButtons array
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = android.graphics.Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.default_option_border_bg
            )
        }
    }
    private fun defaultOptionsViews() {
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
        tvProgress.text = "$mcurrentPosition" + "/" + progressbar.max






        if (this.mcurrentPosition -1== 0) {
            questionfirst()
        } else if (this.mcurrentPosition-1 == 1) {

            questiontwo()
        } else if (this.mcurrentPosition-1 == 2) {
            questionthree()
        }else if(this.mcurrentPosition-1==3){
            querstionfour()
        }else if(this.mcurrentPosition-1==4){
            questionfive()
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
/*
       "Form 1(Unaugmented)", "Form II(فَعَّلَ)", "Form III(فَاعَلَ)", "Form IV(أَفْعَلَ)", "Form V(تَفَعَّلَ)",
                "Form VI(تَفَاعَلَ)", "Form VII(إِنْفَعَلَ-)", "Form VIII(إِفْتَعَلَ)", "Form IX(إِفْعَلَّ)","Form X(إِسْتَفْعَلَ)"
 */
    private fun questionfive() {
        if(cverb.thulathibab!!.isEmpty()) {
            if (answerButtons[1].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[2].correctAnswerIndex = 1

            } else     if (answerButtons[2].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[3].correctAnswerIndex = 2

            } else     if (answerButtons[3].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[4].correctAnswerIndex = 3

            } else     if (answerButtons[4].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[5].correctAnswerIndex = 4

            } else     if (answerButtons[5].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[6].correctAnswerIndex = 5

            } else     if (answerButtons[6].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[7].correctAnswerIndex = 6

            } else     if (answerButtons[7].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[8].correctAnswerIndex = 7

            }else     if (answerButtons[8].text.contains(cverb.form.toString(), ignoreCase = true)) {

                questions[9].correctAnswerIndex = 8

            }else     if (answerButtons[9].text.contains(cverb.form.toString(), ignoreCase = true)) {
                println(answerButtons[0].text)
                println(cverb.tense)
                questions[10].correctAnswerIndex = 9

            }

        }else{

            questions[0].correctAnswerIndex=0
        }

    }

    private fun querstionfour() {


        for (button in answerButtons) {
            button.visibility = View.VISIBLE
        }
        val ThirdPersonSingularMasculine="3MS"
        val ThirdPersonDualMasculine="3MD"
        val ThirdPersonPluralMasculine="3MP"

        val ThirdPersonSingularFeminine="3FS"
        val ThirdPesonDualFeminine="3FD"
        val ThirdPersonPluralFeminine="3FP"


        val SecondPersonSingularMasculine="2MS"
        val SecondPersonDualMasculine="2MD"
        val SecondPersonPlularMasculine="2MP"

        val SecondPersonSingularFeminine="2FS"
        val SecondPersonDualFeminine="2FD"
        val SecondPersonPluralFeminine="2FP"

        val FirstPersonSingula="1S"
        val FirstPersonPlural="1P"
        if (ThirdPersonSingularMasculine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.tense)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[0].correctAnswerIndex=0
        }
        if (ThirdPersonDualMasculine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=1
        }
        if (ThirdPersonPluralMasculine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=2
        }
        if (ThirdPersonSingularFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=3
        }
        if (ThirdPesonDualFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=4
        }
        if (ThirdPersonPluralFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=5
        }

        if(SecondPersonSingularMasculine.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=6
        }

        if(SecondPersonDualMasculine.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=7
        }

        if(SecondPersonPlularMasculine.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=8
        }
        if(SecondPersonSingularFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=9
        }
        if(SecondPersonDualFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=10
        }
        if(SecondPersonPluralFeminine.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=11
        }
        if(FirstPersonSingula.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=12
        }
        if(FirstPersonPlural.contains(cverb.gendernumber.toString(), ignoreCase = true)){
            println(answerButtons[0].text)
            println(cverb.gendernumber)
            //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[3].correctAnswerIndex=13
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
            questions[1].correctAnswerIndex=0

        }





        if (answerButtons[1].text.contains(cverb.voice.toString(), ignoreCase = true)) {
            println(answerButtons[1].text)
            println(cverb.tense)
            questions[1].correctAnswerIndex=1
          //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }



    }

    private fun questionfirst() {

        var compare = "IMPF"
        answerButtons[0].text
        if (answerButtons[0].text.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButtons[0].text)
            println(cverb.tense)
       //     answerButtons[0].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
            questions[0].correctAnswerIndex=0

        }


         if (compare.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButtons[1].text)
            println(cverb.tense)
          //  answerButton2.background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
             questions[0].correctAnswerIndex=1
        }



      if (answerButtons[2].text.contains(cverb.tense.toString(), ignoreCase = true)) {
            println(answerButtons[2].text)
            println(cverb.tense)
             questions[0].correctAnswerIndex=2
         //   answerButtons[2].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }

    }

     fun questionthree() {


         answerButtons[3].visibility = View.GONE
         answerButtons[2].visibility = View.VISIBLE

        if (answerButtons[0].text.contains(
                cverb.mood_kananumbers.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButtons[0].text)
            println(cverb.tense)
            questions.get(2).correctAnswerIndex=0
           // answerButtons[0].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }





        if (answerButtons[1].text.contains(
                cverb.mood_kananumbers.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButtons[0].text)
            println(cverb.tense)
            questions[2].correctAnswerIndex=1
         //   answerButtons[1].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }




        if (answerButtons[2].text.contains(
                cverb.mood_kananumbers.toString(),
                ignoreCase = true
            )
        ) {
            println(answerButtons[0].text)
            println(cverb.tense)
            questions[2].correctAnswerIndex=2
          //  answerButtons[2].background=ContextCompat.getDrawable(requireContext(),R.drawable.correct_option_border_bg)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
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

            R.id.answer_button6-> {
                selectedOptionsView(answerButtons[5], 6)
            }
            R.id.answer_button7 -> {
                selectedOptionsView(answerButtons[6], 7)
            }

            R.id.answer_button8 -> {
                selectedOptionsView(answerButtons[7], 8)
            }

            R.id.answer_button9  -> {
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
            R.id.answer_button13-> {
                selectedOptionsView(answerButtons[12], 13)
            }

            R.id.answer_button14 -> {
                selectedOptionsView(answerButtons[13], 14)
            }








            R.id.submit_button -> {
                if(mSelectedOptionPositon==0) {

                    mcurrentPosition++
                    when {
                        mcurrentPosition <= questions.size -> {
                            showNextQuestion()
                        }

                        else -> {
                           score.text="your Score is $mcurrectanswer out of 5"
                        }
                    }
                }else{

                    val question=questions[mcurrentPosition-1]
                    if(question.correctAnswerIndex+1!=mSelectedOptionPositon){
                        answerView(mSelectedOptionPositon,R.drawable.wrong_option_border_bg)
                    }else{
                        mcurrectanswer++
                    }
                    answerView(question.correctAnswerIndex+1,R.drawable.correct_option_border_bg)
                    if(mcurrentPosition==questions.size){
                        submitButton.text="Finish"
                    }else{
                        submitButton.text="Go to Next Question"
                    }

                }
                mSelectedOptionPositon=0
            }
        }
    }
    fun answerView(answer:Int,drawableView:Int){
        when(answer){
            1->{
                answerButtons[0].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            2->{
                answerButtons[1].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            3->{
                answerButtons[2].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            4->{
                answerButtons[3].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            5->{
                answerButtons[4].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            6->{
                answerButtons[5].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            7->{
                answerButtons[6].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            8->{
                answerButtons[7].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            9->{
                answerButtons[8].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            10->{
                answerButtons[9].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            11->{
                answerButtons[10].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            12->{
                answerButtons[11].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            13->{
                answerButtons[12].background=ContextCompat.getDrawable(requireContext(),drawableView)
            }
            14->{
                answerButtons[13].background=ContextCompat.getDrawable(requireContext(),drawableView)
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