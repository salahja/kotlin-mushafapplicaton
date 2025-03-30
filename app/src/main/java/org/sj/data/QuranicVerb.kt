package org.sj.data

import com.example.mushafconsolidated.Entities.VerbCorpus
import database.entity.QuranVerbsEntity






// Data classes to hold verb info
data class QuranicVerb(
  val verb: String, // Arabic verb
  val root: String, // Arabic root
  val thulathibab: String?, // Thulathi bab (e.g., "N")
  val form: String, // Form (e.g., "IV", "X")
  val romanformat: String?, // Romanized format (e.g., "2")
  val frequency: Int, // Frequency in Quran
  val meaning: String // English meaning (e.g., "to help")
)

data class VerbCorpus(
  val chapterno: Int,
  val verseno: Int,
  val wordno: Int,
  val token: Int,
  val root_a: String, // Arabic root
  val form: String, // Form (e.g., "IV", "X")
  val thulathibab: String?,
  val tag: String,
  val details: String,
  val pOS: String,
  val tense: String, // Tense (e.g., "IMPF", "PERF")
  val voice: String, // Voice (e.g., "ACTI", "PASS")
  val lemma_b: String, // Lemma (Romanized)
  val root_b: String, // Romanized root
  val gendernumber: String, // Gender/number (e.g., "3MS", "2FP")
  val mood_kananumbers: String, // Mood (e.g., "IND", "SUBJ")
  val kana_mood: String, // Kana mood
  val lemma_a: String // Lemma (Arabic)
)

object VerbConjugator {

  // Lookup tables
  private val pronominalLookup = mapOf(
    "3MS" to "he", "3FS" to "she", "3MD" to "they(dual)", "3FD" to "they(dual)",
    "3MP" to "they(masculine)", "3FP" to "they(feminine)",
    "2MS" to "you(masculine)", "2FS" to "you(feminine)", "2MD" to "you(dual)", "2FD" to "you(dual)",
    "2MP" to "you(masculine plural)", "2FP" to "you(feminine plural)",
    "1S" to "I", "1P" to "we"
  )
  private val tenseLookup = mapOf(
    "IMPF" to "present", "PERF" to "past", "IMPV" to "imperative"
  )
  private val moodLookup = mapOf(
    "IND" to "", "SUBJ" to "should", "JUS" to "let"
  )
  private val genderNumberList = listOf(
    "3MS", "3FS", "3MD", "3FD", "3MP", "3FP", // Third person (6)
    "2MS", "2FS", "2MD", "2FD", "2MP", "2FP", // Second person (6)
    "1S", "1P" // First person (2)
  )
  /*
  "I": This is the base form, the "thulathi mujarrad" (simple triliteral) verb. No special nuance needed.
"II" (تفعيل - Tafeel): This form often indicates intensification or repetition of the verb's action.
Example: "to break" -> "to shatter," "to teach" -> "to instruct thoroughly"
"III" (مفاعلة - Mufaa'alah): This form typically denotes interaction or reciprocity between two parties.
Example: "to help" -> "to help each other," "to fight" -> "to combat"
"IV" (إفعال - If'aal): This is the causative form, meaning that the subject causes the action to happen.
Example: "to go out" -> "to send out," "to know" -> "to inform"
"V" (تفعّل - Tafa''ul): Often indicates receiving the action or undergoing a process.
Example: "to learn"-> "to be learned", "to teach" -> "to be taught"
"VI" (تفاعل - Tafaa'ul): Mutual or reciprocal action.
Example: "to help" -> "to help each other," "to correspond" -> "to correspond with each other"
"VII" (إنفعال - Infi'aal): Becoming or being affected by the action.
"VIII" (إفتعال - Ifti'aal): Doing the action oneself.
Example: to ask -> self-ask.
"IX" (إفعِلاّل - If'illaal): Color or defects.
"X" (إستفعال - Istif'aal): Seeking or requesting the verb's action.
Example: "to know" -> "to seek to know," "to forgive" -> "to ask for forgiveness"
"XI" (إفْعِلاّل - If'ellaal): Used for color or other qualities.
"XII" (إفْعَوْعال - If'aw'aal): Used for emphasis.
"XIII" (إفْعَنْلال - If'anlaal): Used for emphasis.
"XIV" (إفْعَنْلال - If'annlaal): Used for emphasis.
"XV" (إفْعَنْلَل - If'anllal): Used for emphasis.
   */
  // Main conjugation function
  fun conjugateEnglish(
    quranicVerb: List<QuranVerbsEntity>, // This is from quranicverbs
    verbCorpus: List<VerbCorpus>?   // This is from verbcorpus/ This is from verbcorpus
  ): Map<String,List<String>> {
    val conjugationsMap = mutableMapOf<String, List<String>>()

    if (verbCorpus != null) {
      for(corpus in verbCorpus){
        val pronounList = genderNumberList.map { pronominalLookup[it] ?: "it" }
        val tense = tenseLookup[corpus.tense] ?: "unknown tense"
        val mood = moodLookup[corpus.mood_kananumbers] ?: ""
        val verbMeaning = quranicVerb.get(1).meaning
        // Determine form-based nuance (example)
        val formNuance = when (quranicVerb.get(1).form) {
          "I" -> verbMeaning // Basic meaning (thulathi mujarrad)
          "II" -> "intensify $verbMeaning" // Tafeel
          "III" -> "interact $verbMeaning" // mufaa'alah
          "IV" -> "cause to $verbMeaning" // if'aal
          "V" -> "receive $verbMeaning" //tafa''ul
          "VI" -> "mutually $verbMeaning" //tafaa'ul
          "VII" -> "become $verbMeaning"//infe'aal
          "VIII" -> "self-$verbMeaning"//iftee'aal
          "IX" -> "become $verbMeaning"//if'ellaal
          "X" -> "seek to $verbMeaning" //istef'aal
          "XI" -> "become $verbMeaning"//if'ellaal
          "XII" -> "intensify $verbMeaning"//if'awlal
          "XIII" -> "intensify $verbMeaning"//if'anlal
          "XIV" -> "intensify $verbMeaning"//if'annlal
          "XV" -> "intensify $verbMeaning"//if'anllal
          else -> "related to $verbMeaning" // Default if form is unknown
        }
        // Handle tense and voice
        val conjugatedVerbs = mutableListOf<String>()

        for (genderNumber in genderNumberList) {
          val pronoun = pronominalLookup[genderNumber] ?: "it"

          val conjugatedVerb = when (tense) {
            "past" -> when (corpus.voice) {
              "ACTI" -> {
                val aux = if (pronoun == "he" || pronoun == "she" || pronoun == "it") "" else ""
                "$aux $formNuance"
              }

              "PASS" -> {
                "was $formNuance"
              }

              else -> "$formNuance"
            }

            "present" -> when (corpus.voice) {
              "ACTI" -> {
                val s =
                  if (pronoun == "he" || pronoun == "she" || pronoun == "it") "s" else ""
                "$formNuance$s"
              }

              "PASS" -> {
                "is $formNuance"
              }

              else -> "$formNuance"
            }

            "imperative" -> when (corpus.voice) {
              "ACTI" -> {
                ""
              }

              "PASS" -> {
                ""
              }

              else -> "$formNuance"
            }

            else -> formNuance // Default
          }
          val formattedPronoun = pronoun.replace("(", "").replace(")", "")

          val finalVerb = if (corpus.tense.equals("IMPV")) {
            val formattedVerb = formNuance?.replace("to ", "")
            if (formattedPronoun.equals("you") || formattedPronoun.contains("dual") || formattedPronoun.contains("plural"))
              formattedVerb
            else ""
          } else {
            "$formattedPronoun $mood $conjugatedVerb".trim()
          }
          if (finalVerb != null) {
            if (finalVerb.isNotEmpty()) {
              conjugatedVerbs.add(finalVerb.toString())
            }
          }
        }
        conjugationsMap[corpus.tense+corpus.voice] = conjugatedVerbs

      }
    }
    return conjugationsMap
  }
}

object VerbConjugatorprev {

  // Lookup tables
  private val pronominalLookup = mapOf(
    "3MS" to "he", "3FS" to "she", "3MD" to "they(dual)", "3FD" to "they(dual)",
    "3MP" to "they(masculine)", "3FP" to "they(feminine)",
    "2MS" to "you(masculine)", "2FS" to "you(feminine)", "2MD" to "you(dual)", "2FD" to "you(dual)",
    "2MP" to "you(masculine plural)", "2FP" to "you(feminine plural)",
    "1S" to "I", "1P" to "we"
  )
  private val tenseLookup = mapOf(
    "IMPF" to "present", "PERF" to "past", "IMPV" to "imperative"
  )
  private val moodLookup = mapOf(
    "IND" to "", "SUBJ" to "should", "JUS" to "let"
  )

  // Main conjugation function
  fun conjugateEnglish(
    quranicVerb: List<QuranVerbsEntity>, // This is from quranicverbs
    verbCorpus: VerbCorpus   // This is from verbcorpus
  ): String {
    val pronoun = pronominalLookup[verbCorpus.gendernumber] ?: "it"
    val tense = tenseLookup[verbCorpus.tense] ?: "unknown tense"
    val mood = moodLookup[verbCorpus.mood_kananumbers]?:""
    quranicVerb.get(0).meaning
    val verbMeaning =     quranicVerb.get(1).meaning

    // Determine form-based nuance (example)
    val formNuance = when (quranicVerb.get(1).form) {
      "I" -> verbMeaning // Basic meaning (thulathi mujarrad)
      "II" -> "intensify $verbMeaning" // Tafeel
      "III" -> "interact $verbMeaning" // mufaa'alah
      "IV" -> "cause to $verbMeaning" // if'aal
      "V" -> "receive $verbMeaning" //tafa''ul
      "VI" -> "mutually $verbMeaning" //tafaa'ul
      "VII" -> "become $verbMeaning"//infe'aal
      "VIII" -> "self-$verbMeaning"//iftee'aal
      "IX" -> "become $verbMeaning"//if'ellaal
      "X" -> "seek to $verbMeaning" //istef'aal
      "XI" -> "become $verbMeaning"//if'ellaal
      "XII" -> "intensify $verbMeaning"//if'awlal
      "XIII" -> "intensify $verbMeaning"//if'anlal
      "XIV" -> "intensify $verbMeaning"//if'annlal
      "XV" -> "intensify $verbMeaning"//if'anllal
      else -> "related to $verbMeaning" // Default if form is unknown
    }
    // Handle tense and voice
    val conjugatedVerb = when (tense) {
      "past" -> when (verbCorpus.voice) {
        "ACTI" -> {
          val aux = if (pronoun == "he" || pronoun == "she" || pronoun == "it") "" else ""
          "$aux $formNuance"
        }
        "PASS" -> {
          "was $formNuance"
        }

        else -> "$formNuance"
      }
      "present" -> when (verbCorpus.voice) {
        "ACTI" -> {
          val s = if (pronoun == "he" || pronoun == "she" || pronoun == "it") "s" else ""
          "$formNuance$s"
        }
        "PASS" -> {
          "is $formNuance"
        }

        else -> "$formNuance"
      }
      "imperative" -> when (verbCorpus.voice) {
        "ACTI" -> {
          ""
        }
        "PASS" -> {
          ""
        }
        else -> "$formNuance"
      }
      else -> formNuance // Default
    }
    val formattedPronoun= pronoun.replace("(","").replace(")","")

    return if(verbCorpus.tense.equals("IMPV")){
      val formatedverb = formNuance?.replace("to ","")
      if(formattedPronoun.equals("you") || formattedPronoun.contains("dual") || formattedPronoun.contains("plural"))
        "$formatedverb"
      else ""
    }else{
      "$formattedPronoun $mood $conjugatedVerb".trim()
    }
  }
}