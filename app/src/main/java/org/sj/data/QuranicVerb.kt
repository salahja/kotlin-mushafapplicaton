package org.sj.data

import com.example.mushafconsolidated.Entities.VerbCorpus
import database.entity.QuranVerbsEntity
import org.sj.verbConjugation.MadhiMudharay

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

object VerbUtils {
  // Shared lookup tables
  internal val pronominalLookup = mapOf(
    "3MS" to "he",
    "3FS" to "she",
    "3MD" to "they (dual)",
    "3FD" to "they (dual)",
    "3MP" to "they (masculine)",
    "3FP" to "they (feminine)",
    "2MS" to "you (masculine)",
    "2FS" to "you (feminine)",
    "2MD" to "you (dual)",
    "2FD" to "you (dual)",
    "2MP" to "you (masculine plural)",
    "2FP" to "you (feminine plural)",
    "1S" to "I",
    "1P" to "we"
  )

  internal val tenseLookup = mapOf(
    "IMPF" to "present",
    "PERF" to "past",
    "IMPV" to "imperative"
  )

  internal val moodLookup = mapOf(
    "IND" to "",
    "SUBJ" to "should",
    "JUS" to "let"
  )

  internal val genderNumberList = listOf(
    "3MS", "3FS", "3MD", "3FD", "3MP", "3FP", // Third person (6)
    "2MS", "2FS", "2MD", "2FD", "2MP", "2FP", // Second person (6)
    "1S", "1P" // First person (2)
  )

  internal val formNuances = mapOf(
    "I" to "", // Basic meaning (thulathi mujarrad)
    "II" to "intensify", // Tafeel
    "III" to "interact", // mufaa'alah
    "IV" to "cause to", // if'aal
    "V" to "receive", // tafa''ul
    "VI" to "mutually", // tafaa'ul
    "VII" to "become", // infe'aal
    "VIII" to "self-", // iftee'aal
    "IX" to "become", // if'ellaal
    "X" to "seek to", // istef'aal
    "XI" to "become", // if'ellaal
    "XII" to "intensify", // if'awlal
    "XIII" to "intensify", // if'anlal
    "XIV" to "intensify", // if'annlal
    "XV" to "intensify" // if'anllal
  )

  internal fun getFormNuance(form: String?, baseMeaning: String): String {
    val normalizedForm = form ?: "I"
    val prefix = formNuances[normalizedForm] ?: "related to"
    return if (prefix.isEmpty()) baseMeaning else "$prefix $baseMeaning"
  }

  internal fun formatPronoun(pronoun: String): String {
    return pronoun.replace("(", " (")
      .replace("masculine", "masculine")
      .replace("feminine", "feminine")
      .replace("dual", "dual")
      .replace("plural", "plural")
  }
}

object VerbConjugators {
  fun conjugateEnglish(
    tense: String,
    voice: String?,
    quranicVerb: List<QuranVerbsEntity>
  ): Map<String, List<String>> {
    if (quranicVerb.isEmpty()) return emptyMap()

    val verbMeaning = quranicVerb[0].meaning ?: return emptyMap()
    val baseMeaning = verbMeaning.replaceFirst("to", "").trim()
    val form = quranicVerb[0].formroman
    val formNuance = VerbUtils.getFormNuance(form, baseMeaning)

    return mapOf(
      "$tense$voice" to VerbUtils.genderNumberList.mapNotNull { genderNumber ->
        conjugateForGenderNumber(genderNumber, tense, voice, formNuance)
      }
    )
  }

  private fun conjugateForGenderNumber(
    genderNumber: String,
    tense: String,
    voice: String?,
    formNuance: String
  ): String? {
    val pronoun = VerbUtils.formatPronoun(VerbUtils.pronominalLookup[genderNumber] ?: return null)

    return when (tense) {
      "past" -> when (voice) {
        "ACTI" -> if (pronoun in listOf("he", "she", "it")) formNuance else formNuance
        "PASS" -> "was $formNuance"
        else -> formNuance
      }
      "present" -> when (voice) {
        "ACTI" -> {
          val s = if (pronoun in listOf("he", "she", "it")) "s" else ""
          "$formNuance$s"
        }
        "PASS" -> "is $formNuance"
        else -> formNuance
      }
      "imperative" -> when (voice) {
        "ACTI", "PASS" -> if (pronoun.startsWith("you")) formNuance.replace("to ", "") else null
        else -> formNuance
      }
      else -> formNuance
    }?.let { conjugatedVerb ->
      if (tense == "imperative") conjugatedVerb else "$pronoun $conjugatedVerb".trim()
    }
  }
}

object VerbConjugator {
  fun conjugateEnglish(
    quranicVerb: List<QuranVerbsEntity>,
    corpusList: List<VerbCorpus>?
  ): MadhiMudharay {
    val result = MadhiMudharay()

    if (quranicVerb.isEmpty() || corpusList.isNullOrEmpty()) {
      return result
    }

    val verbMeaning = quranicVerb[0].meaning ?: return result
    val baseMeaning = verbMeaning.replaceFirst("to", "").trim()
    val form = quranicVerb[0].formroman
    val formNuance = VerbUtils.getFormNuance(form, baseMeaning)

    corpusList.firstOrNull()?.let { corpus ->
      val tense = VerbUtils.tenseLookup[corpus.tense] ?: "unknown"
      val mood = VerbUtils.moodLookup[corpus.mood_kananumbers] ?: ""

      val conjugations = VerbUtils.genderNumberList.mapNotNull { genderNumber ->
        conjugateForGenderNumber(genderNumber, tense, corpus.voice.toString(), mood, formNuance)
      }

      if (conjugations.size >= 14) {
        with(result) {
          hua = conjugations[0]
          huma = conjugations[1]
          hum = conjugations[2]
          hia = conjugations[3]
          humaf = conjugations[4]
          hunna = conjugations[5]
          anta = conjugations[6]
          antuma = conjugations[7]
          antum = conjugations[8]
          anti = conjugations[9]
          antumaf = conjugations[10]
          antunna = conjugations[11]
          ana = conjugations[12]
          nahnu = conjugations[13]
        }
      }
    }

    return result
  }

  private fun conjugateForGenderNumber(
    genderNumber: String,
    tense: String,
    voice: String,
    mood: String,
    formNuance: String
  ): String? {
    val pronoun = VerbUtils.formatPronoun(VerbUtils.pronominalLookup[genderNumber] ?: return null)

    val conjugatedVerb = when (tense) {
      "past" -> when (voice) {
        "ACTI" -> if (pronoun in listOf("he", "she", "it")) formNuance else formNuance
        "PASS" -> "was $formNuance"
        else -> formNuance
      }
      "present" -> when (voice) {
        "ACTI" -> {
          val s = if (pronoun in listOf("he", "she", "it")) "s" else ""
          "$formNuance$s"
        }
        "PASS" -> "was $formNuance"
        else -> formNuance
      }
      "imperative" -> when (voice) {
        "ACTI", "PASS" -> if (pronoun.startsWith("you")) formNuance.replace("to ", "") else null
        else -> formNuance
      }
      else -> formNuance
    } ?: return null

    return if (tense == "imperative") {
      conjugatedVerb
    } else {
      "$pronoun $mood $conjugatedVerb".trim()
    }
  }
}


/*
package org.sj.data

import com.example.mushafconsolidated.Entities.VerbCorpus
import database.entity.QuranVerbsEntity
import org.sj.verbConjugation.MadhiMudharay


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

object VerbConjugators {

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
  */
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
   *//*

  // Main conjugation function
  fun conjugateEnglish(
    tense: String, // This is from quranicverbs
    voice: String?,
   quranicVerb: List<QuranVerbsEntity>
  ): Map<String,List<String>> {
    val conjugationsMap = mutableMapOf<String, List<String>>()
 val verbMeaning = quranicVerb.get(0).meaning
    if (verbMeaning != null) {

        val pronounList = genderNumberList.map { pronominalLookup[it] ?: "it" }
      //  val tense = tenseLookup[corpus.tense] ?: "unknown tense"
       // val mood = moodLookup[corpus.mood_kananumbers] ?: ""
       // val verbMeaning = quranicVerb.get(1).meaning
        // Determine form-based nuance (example)
        val formNuance = when (quranicVerb.get(0).formroman) {
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
            "past" -> when (voice) {
              "ACTI" -> {
                val aux = if (pronoun == "he" || pronoun == "she" || pronoun == "it") "" else ""
                "$aux $formNuance"
              }

              "PASS" -> {
                "was $formNuance"
              }

              else -> "$formNuance"
            }

            "present" -> when (voice) {
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

            "imperative" -> when (voice) {
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

          val finalVerb = if (tense.equals("IMPV")) {
            val formattedVerb = formNuance?.replace("to ", "")
            if (formattedPronoun.equals("you") || formattedPronoun.contains("dual") || formattedPronoun.contains("plural"))
              formattedVerb
            else ""
          } else {
            "$formattedPronoun $voice $conjugatedVerb".trim()
          }
          if (finalVerb != null) {
            if (finalVerb.isNotEmpty()) {
              conjugatedVerbs.add(finalVerb.toString())
            }
          }
        }
        conjugationsMap[tense+voice] = conjugatedVerbs

    }
    return conjugationsMap
  }
}



object VerbConjugator {

  // Lookup tables
  private val pronominalLookup = mapOf(
    "3MS" to "he",
    "3FS" to "she",
    "3MD" to "they(dual)",
    "3FD" to "they(dual)",
    "3MP" to "they(masculine)",
    "3FP" to "they(feminine)",
    "2MS" to "you(masculine)",
    "2FS" to "you(feminine)",
    "2MD" to "you(dual)",
    "2FD" to "you(dual)",
    "2MP" to "you(masculine plural)",
    "2FP" to "you(feminine plural)",
    "1S" to "I",
    "1P" to "we"
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

  // Main conjugation function
  fun conjugateEnglish(
    quranicVerb: List<QuranVerbsEntity>, // This is from quranicverbs
    corpusList: List<VerbCorpus>?   // This is from verbcorpus
  ): MadhiMudharay{
    val madhiMudharay = MadhiMudharay()
    val conjugationsMap = mutableMapOf<String, List<String>>()

    if (corpusList != null) {
      for (corpus in corpusList) {
        val pronounList = genderNumberList.map { pronominalLookup[it] ?: "it" }
        val tense = tenseLookup[corpus.tense] ?: "unknown tense"
        val mood = moodLookup[corpus.mood_kananumbers] ?: ""
        val verbMeaning = quranicVerb.get(0).meaning!!.replaceFirst("to","")
        var conjuationPattern=  quranicVerb.get(0).formroman
        // Determine form-based nuance (example)
        if(quranicVerb.get(0).formroman==null){
          conjuationPattern="I"
        }
        val formNuance = when (conjuationPattern) {
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
          var pronoun = pronominalLookup[genderNumber] ?: "it"
        pronoun = if (pronoun.contains("they(dual)")) {
            pronoun.replace("they(dual)", "they  (dual)")
        } else if (pronoun.contains("you(dual)")) {
            pronoun.replace("you(dual)", "you  (dual)")
        } else {
            pronoun
        }
          pronoun = if (pronoun.contains("they(masculine)")) {
            pronoun.replace("they(masculine)", "they  (masculine)")
          } else if (pronoun.contains("they(feminine)")) {
            pronoun.replace("they(feminine)", "they  (feminine)")
          } else {
            pronoun
          }
          pronoun = if (pronoun.contains("you(masculine)")) {
            pronoun.replace("you(masculine)", "you  (masculine)")
          } else if (pronoun.contains("you(feminine)")) {
            pronoun.replace("you(feminine)", "you  (feminine)")
          } else {
            pronoun
          }


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

          val finalVerb = if (corpus.tense == "IMPV") {
            val formattedVerb = formNuance?.replace("to ", "")
            if (formattedPronoun == "you" || formattedPronoun.contains("dual") || formattedPronoun.contains("plural"))
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
        conjugationsMap[corpus.tense + corpus.voice] = conjugatedVerbs
        */
/*
        0 = "he   worships"
1 = "she   worships"
2 = "they  dual   worship"
3 = "they  dual   worship"
4 = "theymasculine   worship"
5 = "they  feminine   worship"
6 = "youmasculine   worship"
7 = "you  feminine   worship"
8 = "you  dual   worship"
9 = "you  dual   worship"
10 = "youmasculine plural   worship"
11 = "youfeminine plural   worship"
12 = "I   worship"
13 = "we   worship"
         *//*

        madhiMudharay.hua=conjugatedVerbs?.get(0).toString()
        madhiMudharay.huma=conjugatedVerbs.get(1).toString()
        madhiMudharay.hum=conjugatedVerbs.get(2).toString()
        madhiMudharay.hia=conjugatedVerbs.get(3).toString()
        madhiMudharay.humaf=conjugatedVerbs.get(4).toString()
        madhiMudharay.hunna=conjugatedVerbs.get(5).toString()
        madhiMudharay.anta=conjugatedVerbs.get(6).toString()
        madhiMudharay.antuma=conjugatedVerbs.get(7).toString()
        madhiMudharay.antum=conjugatedVerbs.get(8).toString()
        madhiMudharay.anti=conjugatedVerbs.get(9).toString()
        madhiMudharay.antumaf=conjugatedVerbs.get(10).toString()
        madhiMudharay.antunna=conjugatedVerbs.get(11).toString()
        madhiMudharay.ana=conjugatedVerbs.get(12).toString()
        madhiMudharay.nahnu=conjugatedVerbs.get(13).toString()

      }
    }
         return madhiMudharay
    //return conjugationsMap
  }
}
*/
