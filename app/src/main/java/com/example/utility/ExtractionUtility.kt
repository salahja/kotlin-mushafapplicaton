package com.example.utility

import Utility.ArabicLiterals
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.Constant.FORESTGREEN
import com.example.Constant.GOLD
import com.example.Constant.GREENDARK
import com.example.Constant.KASHMIRIGREEN
import com.example.Constant.ORANGE400
import com.example.Constant.WHOTPINK
import com.example.Constant.harfshartspanDark
import com.example.Constant.jawabshartspanDark
import com.example.Constant.shartspanDark
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NegationEnt

import com.example.mushafconsolidated.Entities.NewKanaEntity
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.data.CorpusRow
import com.example.mushafconsolidated.data.ShartCorpusRow
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.utility.CorpusUtilityorig.Companion.dark
import com.example.utility.CorpusUtilityorig.Companion.findWordOccurrencesArabic
import com.example.utility.QuranGrammarApplication.Companion.context
import com.example.utility.QuranViewUtils.extractCase
import com.example.utility.QuranViewUtils.extractConsonants
import com.example.utility.QuranViewUtils.extractGender
import com.example.utility.QuranViewUtils.extractValues
import com.example.utility.QuranViewUtils.extractVerbType
import com.example.utility.QuranViewUtils.hasNunation
import java.io.OutputStreamWriter
import kotlin.math.min

object ExtractionUtility {


    fun collectBrokenPlurals(corpus: List<CorpusEntity>, qurantext: String): List<String> {
        val brokenPluralPatterns = listOf(
            "فُعُوْلٌ",
            "أَفْعَالٌ",
            "فِعَالٌ",
            "فُعُلٌ",
            "فِعْلَانٌ",
            "فَعْلَانُ",
            "فُعَلٌ",
            "فُعُولٌ",
            "فُعَلَاءُ",
            "فُعْلَانٌ",
            "فَعَالِيلُ",
            "فُعُولَاتٌ",
            "مَفَاعِلُ",
            "فَوَاعِلُ",
            "مَفَاعِيلُ",
            "فُعْلَةٌ",
            "أَفَاعِيلُ",
            "أَفْعِلَةٌ",
            "فَعَالٍ",
            "فِعْلَةٌ",
            "فِعَالَاتٌ",
            "فِعْلَانٌ",
            "فِعَالِين",
            "أَفْعُلَةٌ",
            "فُعَلٌ",
            "أَفْعَالَ",
            "فُعُولَ",
            "فِعَالَ",
            "فُعَلَاءَ",
            "فُعْلَانَ",
            "فَعَالِيلَ",
            "فُعُولَاتَ",
            "مَفَاعِلَ",
            "فَوَاعِلَ",
            "مَفَاعِيلَ",
            "فُعْلَةَ",
            "أَفَاعِيلَ",
            "أَفْعِلَةَ",
            "فَعَالِيَ",
            "فِعْلَةَ",
            "فِعَالَاتَ",
            "فِعْلَانَ",
            "فِعَالِين",
            "أَفْعُلَةَ",
            "فُعَلَ"
        )

        val identifiedPlurals = mutableListOf<String>()

        for (entry in corpus) {
            var currentWord = ""
            var nounDetails = ""
            //     entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            if (entry.tagone == "N") {
                currentWord = entry.araone!!
                nounDetails = entry.detailsone!!
            } else if (entry.tagtwo == "N") {
                currentWord = entry.aratwo!!
                nounDetails = entry.detailstwo!!
            } else if (entry.tagthree == "N") {
                currentWord = entry.arathree!!
                nounDetails = entry.detailsthree!!
            } else if (entry.tagfour == "N") {
                currentWord = entry.arafour!!
                nounDetails = entry.detailsfour!!
            } else if (entry.tagfive == "N") {
                currentWord = entry.arafive!!
                nounDetails = entry.detailsfive!!
            }
            val wordForIndex =
                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            val wordConsonants = extractConsonants(currentWord)
            val currentmeaning = entry.en
            for (pattern in brokenPluralPatterns) {
                val patternConsonants = extractConsonants(pattern)

                // Ensure both word and pattern have at least 3 consonants
                if (wordConsonants.length >= 3 && patternConsonants.length >= 3) {
                    try {
                        // Replace consonants in the pattern with those of the current word
                        val replacedPattern =
                            pattern.replaceFirst(patternConsonants[0], wordConsonants[0])
                                .replaceFirst(patternConsonants[1], wordConsonants[1])
                                .replaceFirst(patternConsonants[2], wordConsonants[2])

                        // Compare the modified pattern with the current word
                        if (replacedPattern == currentWord) {

                            val inputString = nounDetails
                            val extractedValues = extractValues(inputString)
                            val arabic =
                                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
                            val english = entry.en
                            val startindex = qurantext.indexOf(arabic)
                            val endindex = startindex + arabic.length
                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$startindex|$endindex|$arabic|$pattern|$currentWord|$english|$extractedValues"
                            identifiedPlurals.add(dataString)
                        }
                    } catch (e: StringIndexOutOfBoundsException) {
                        // Handle case where replaceFirst fails
                        println("Error processing word: $currentWord with pattern: $pattern")
                    }
                }
            }
        }

        return identifiedPlurals
    }

    fun extractSifat(corpusList: List<CorpusEntity>, qurantext: String): List<String> {
        val mousufSifaPairs = mutableListOf<Pair<String, String>>()
        val extractedSentences = mutableListOf<String>()

        for (i in corpusList.indices) {
            val currentWord = corpusList[i]
            if (currentWord.surah == 27 && currentWord.ayah == 44 && currentWord.wordno == 15) {
                println("check")
            }

            val isAdjective = (currentWord.tagone == "ADJ" || currentWord.tagtwo == "ADJ" ||
                    currentWord.tagthree == "ADJ" || currentWord.tagfour == "ADJ" && currentWord.tagfive != "ADJ")


            var nounDef = false
            if (isAdjective) {
                var nounDetails = ""
                var currentWords = ""


                // Check for broken plural or gender number agreement
                currentWords =
                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive


                val sifa =
                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive


                var sifaword = currentWord.wordno
                val translationBuilder = StringBuilder()
                translationBuilder.append(currentWord.en)

                val extractedSentence = "$sifa"
                val startindex = qurantext.indexOf(extractedSentence)
                val endindex = startindex + extractedSentence.length
                val nountype = "sifa"
                val dataString =
                    "${currentWord.surah}|${currentWord.ayah}|$sifaword|$startindex|$endindex|$extractedSentence|$translationBuilder|$nountype"
                extractedSentences.add(dataString)
                // mousufSifaPairs.add(Pair(mousuf, sifa))


            }
        }
        return extractedSentences
    }

    fun extractMousufSifanew(corpusList: List<CorpusEntity>, qurantext: String): List<String> {
        val mousufSifaPairs = mutableListOf<Pair<String, String>>()
        val extractedSentences = mutableListOf<String>()

        for (i in corpusList.indices) {
            val currentWord = corpusList[i]
            if (currentWord.surah == 27 && currentWord.ayah == 44 && currentWord.wordno == 15) {
                println("check")
            }

            val isAdjective = (currentWord.tagone == "ADJ" || currentWord.tagtwo == "ADJ" ||
                    currentWord.tagthree == "ADJ" || currentWord.tagfour == "ADJ" && currentWord.tagfive != "ADJ")


            var nounDef = false
            if (isAdjective) {
                var nounDetails = ""
                var currentWords = ""


                // Check for broken plural or gender number agreement
                currentWords =
                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive



                if (i - 1 < corpusList.size) {


                    if (i - 1 >= 0) {
                        val previousWord = corpusList[i - 1]
                        if (previousWord.tagone == "N" || previousWord.tagtwo == "N" || previousWord.tagthree == "N" || previousWord.tagfour == "N"
                            || previousWord.tagfive == "N" ||
                            previousWord.tagone == "T" || previousWord.tagtwo == "T" || previousWord.tagthree == "T" || previousWord.tagfour == "T"
                            || previousWord.tagfive == "T" ||
                            previousWord.tagone == "PN" || previousWord.tagtwo == "" || previousWord.tagthree == "" || previousWord.tagfour == ""
                            || previousWord.tagfive == ""


                        ) {
                            val sifa =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var mousuf =
                                previousWord.araone + previousWord.aratwo + previousWord.arathree + previousWord.arafour + previousWord.arafive
                            var mousufword = previousWord.wordno
                            var sifaword = currentWord.wordno
                            val translationBuilder = StringBuilder()
                            translationBuilder.append(previousWord.en).append(" ")
                                .append(currentWord.en)

                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val nountype = "sifa"
                            val dataString =
                                "${previousWord.surah}|${previousWord.ayah}|$mousufword|$sifaword|$startindex|$endindex|$extractedSentence|$translationBuilder|$nountype"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))

                        }
                    }


                }
            }
        }
        return extractedSentences
    }

    fun extractMousufSifa(corpusList: List<CorpusEntity>, qurantext: String): List<String> {
        val mousufSifaPairs = mutableListOf<Pair<String, String>>()
        val extractedSentences = mutableListOf<String>()

        for (i in corpusList.indices) {
            val currentWord = corpusList[i]
            if (currentWord.surah == 27 && currentWord.ayah == 44 && currentWord.wordno == 15) {
                println("check")
            }
            // Check if the current word is a noun (N) or proper noun (PN)
            /*  val isIndefiniteNoun = (currentWord.tagone=="N" || currentWord.tagone=="PN")
                      || (currentWord.tagtwo=="N" || currentWord.tagtwo=="PN" && currentWord.tagone!="DET")
                      || (currentWord.tagthree=="N" || currentWord.tagthree=="PN" && currentWord.aratwo!="DET")
                      || (currentWord.tagfour=="N" || currentWord.tagfour=="PN" && currentWord.tagthree!="DET")*/
            val isIndefiniteNoun = (currentWord.tagone == "N" || currentWord.tagone == "PN") ||
                    (currentWord.tagtwo == "N" || currentWord.tagtwo == "PN") && currentWord.tagone != "DET" ||
                    (currentWord.tagthree == "N" || currentWord.tagthree == "PN") && currentWord.aratwo != "DET" ||
                    (currentWord.tagfour == "N" || currentWord.tagfour == "PN") && currentWord.tagthree != "DET"


            val isDefinitNounTagone =
                currentWord.tagone == "DET" && (currentWord.tagtwo!!.contains("N") || currentWord.tagtwo!!.contains(
                    "PN"
                ))
            val isDefinitNounTagtwo =
                currentWord.tagtwo!!.contains("DET") && (currentWord.tagthree!!.contains("N") || currentWord.tagthree!!.contains(
                    "PN"
                ))
            val isDefinitNounTagthree =
                currentWord.tagthree!!.contains("DET") && (currentWord.tagfour!!.contains("N") || currentWord.tagfour!!.contains(
                    "PN"
                ))
            val isDefinitNoun =
                (currentWord.tagone == "DET" && (currentWord.tagtwo!!.contains("N") || currentWord.tagtwo!!.contains(
                    "PN"
                )
                        || currentWord.tagtwo!!.contains("DET") && (currentWord.tagthree!!.contains(
                    "N"
                ) || currentWord.tagthree!!.contains("PN")
                        || currentWord.tagthree!!.contains("DET") && (currentWord.tagfour!!.contains(
                    "N"
                ) || currentWord.tagfour!!.contains("PN")
                        ))))
            var nounDef = false
            if (isIndefiniteNoun || isDefinitNounTagone || isDefinitNounTagtwo || isDefinitNounTagthree) {
                var nounDetails = ""
                var currentWords = ""
                var isBrokenPlural = false

                if (isIndefiniteNoun) {
                    nounDetails = currentWord.detailsone ?: ""

                } else if (isDefinitNounTagone) {
                    nounDetails = currentWord.detailstwo ?: ""
                    nounDef = currentWord.tagone == "DET"
                } else if (isDefinitNounTagtwo) {
                    nounDetails = currentWord.detailsthree ?: ""
                    nounDef = currentWord.tagtwo == "DET"
                } else if (isDefinitNounTagthree) {
                    nounDetails = currentWord.detailsfour ?: ""
                    nounDef = currentWord.tagthree == "DET"
                }


                // Check for broken plural or gender number agreement
                currentWords =
                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                isBrokenPlural =
                    isBrokenPluralPattern(currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive)

                // Handle broken plural: treat as FS (feminine singular)
                var nounGender = if (isBrokenPlural) "FS" else nounDetails.extractGender()
                val nounCase = nounDetails.extractCase()
                // Check definiteness

                // Look ahead to find the adjective (ADJ)
                if (i + 1 < corpusList.size) {
                    val nextWord = corpusList[i + 1]
                    var isAdjective = false
                    var isNoun = false
                    var adjDetails = ""
                    var nounDetails = ""
                    if (nextWord.tagone == "ADJ") {
                        isAdjective = true
                        adjDetails = nextWord.detailsone ?: ""
                    } else if (nextWord.tagone == "DET") {
                        isAdjective =
                            nextWord.tagtwo == "ADJ"
                        adjDetails = nextWord.detailstwo ?: ""
                    } else if (nextWord.tagtwo == "DET") {
                        isAdjective =
                            nextWord.tagthree == "ADJ"
                        adjDetails = nextWord.detailsthree ?: ""
                    } else if (nextWord.tagthree == "DET") {
                        isAdjective = nextWord.tagfour == "ADJ"
                        adjDetails = nextWord.detailsfour ?: ""
                    }
                    //check noun and noun
                    if (nextWord.tagone == "N") {
                        isNoun = true
                        nounDetails = nextWord.detailsone ?: ""
                    } else if (nextWord.tagone == "N") {
                        isAdjective =
                            nextWord.tagtwo == "N"
                        nounDetails = nextWord.detailstwo ?: ""
                    } else if (nextWord.tagtwo == "DET") {
                        isNoun =
                            nextWord.tagthree == "N"
                        nounDetails = nextWord.detailsthree ?: ""
                    } else if (nextWord.tagthree == "DET") {
                        isNoun = nextWord.tagfour == "N"
                        nounDetails = nextWord.detailsfour ?: ""
                    }
                    if (isNoun) {
                        val currentNounGender = nounDetails.extractGender()
                        val currentNounCase = nounDetails.extractCase()
                        val currnetNounDef = nextWord.tagone == "DET"
                        if (nounCase == currentNounCase && nounDef == currnetNounDef && nounGender == currentNounGender) {

                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            var adjword = nextWord.wordno
                            if (i + 2 < corpusList.size) {

                                val nextSecondAdjective = corpusList[i + 2]
                                if (nextSecondAdjective.tagone == "ADJ" || nextSecondAdjective.tagtwo == "ADJ" || nextSecondAdjective.tagthree == "ADJ" || nextSecondAdjective.tagfour == "ADJ") {
                                    nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    sifa += " " + nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    adjword = nextSecondAdjective.wordno
                                }
                            }

                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val nountype = "nountype"
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|$adjword|$startindex|$endindex|$extractedSentence|$translationBuilder|$nountype"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))
                        }
                    }

                    if (isAdjective) {
                        val adjGender = adjDetails.extractGender()
                        val adjCase = adjDetails.extractCase()
                        val adjDef = nextWord.tagone == "DET"
                        if (nounCase == adjCase && nounDef == adjDef) {
                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            var adjword = nextWord.wordno
                            if (i + 2 < corpusList.size) {

                                val nextSecondAdjective = corpusList[i + 2]
                                if (nextSecondAdjective.tagone == "ADJ" || nextSecondAdjective.tagtwo == "ADJ" || nextSecondAdjective.tagthree == "ADJ" || nextSecondAdjective.tagfour == "ADJ") {
                                    nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    sifa += " " + nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    adjword = nextSecondAdjective.wordno
                                }
                            }

                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|$adjword|$startindex|$endindex|$extractedSentence|$translationBuilder"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))

                        } else if (nounCase == adjCase && adjDef && !nounDef) {
                            var adjword = nextWord.wordno
                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            var sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            if (i + 2 < corpusList.size) {

                                val nextSecondAdjective = corpusList[i + 2]
                                if (nextSecondAdjective.tagone == "ADJ" || nextSecondAdjective.tagtwo == "ADJ" || nextSecondAdjective.tagthree == "ADJ" || nextSecondAdjective.tagfour == "ADJ") {
                                    nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    sifa += " " + nextSecondAdjective.araone + nextSecondAdjective.aratwo + nextSecondAdjective.arathree + nextSecondAdjective.arafour + nextSecondAdjective.arafive
                                    adjword = nextSecondAdjective.wordno
                                }
                            }
                            val check = "check"
                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|$adjword|$startindex|$endindex|$extractedSentence|$translationBuilder|$check"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))
                        }
                    } else if (isAdjective) {//1073
                        val adjGender = adjDetails.extractGender()
                        val adjCase = adjDetails.extractCase()
                        val adjDef = nextWord.tagone == "DET"
                        if ((adjGender == "FS" && nounGender == "MP" || (adjGender == "FS") && nounGender == "F") && (nounCase == adjCase && nounDef == adjDef)) {
                            val mousuf =
                                currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                            val sifa =
                                nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                            val translationBuilder = StringBuilder()
                            translationBuilder.append(nextWord.en).append(" ")
                                .append(currentWord.en)
                            val extractedSentence = "$mousuf $sifa"
                            val startindex = qurantext.indexOf(extractedSentence)
                            val endindex = startindex + extractedSentence.length
                            val dataString =
                                "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|${nextWord.wordno}|$startindex|$endindex|$extractedSentence|$translationBuilder"
                            extractedSentences.add(dataString)
                            mousufSifaPairs.add(Pair(mousuf, sifa))

                        } else
                            if (adjGender == "F" && nounCase == adjCase && nounDef == adjDef) {
                                val mousuf =
                                    currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                                val sifa =
                                    nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                                val translationBuilder = StringBuilder()
                                translationBuilder.append(nextWord.en).append(" ")
                                    .append(currentWord.en)
                                val extractedSentence = "$mousuf $sifa"
                                val startindex = qurantext.indexOf(extractedSentence)
                                val endindex = startindex + extractedSentence.length
                                val dataString =
                                    "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|${nextWord.wordno}|$startindex|$endindex|$extractedSentence|$translationBuilder"
                                extractedSentences.add(dataString)
                                mousufSifaPairs.add(Pair(mousuf, sifa) as Pair<String, String>)


                            } else {

                                //    val adjDetails = nextWord.detailstwo ?: ""
                                val adjGender = adjDetails.extractGender()
                                val adjCase = adjDetails.extractCase()
                                val adjDef = nextWord.tagone == "DET" // Check definiteness
                                if ((nounGender == "unknown" || nounGender == "M") && (adjGender == "MS" || adjGender == "FS")) {
                                    nounGender = adjGender
                                }
                                // Check for gender, case, and definiteness agreement
                                if (nounGender == adjGender && nounCase == adjCase && nounDef == adjDef) {
                                    // Found a mousuf-sifa pair
                                    val mousuf =
                                        currentWord.araone + currentWord.aratwo + currentWord.arathree + currentWord.arafour + currentWord.arafive
                                    val sifa =
                                        nextWord.araone + nextWord.aratwo + nextWord.arathree + nextWord.arafour + nextWord.arafive
                                    val translationBuilder = StringBuilder()
                                    translationBuilder.append(nextWord.en).append(" ")
                                        .append(currentWord.en)
                                    val extractedSentence = "$mousuf $sifa"
                                    val startindex = qurantext.indexOf(extractedSentence)
                                    val endindex = startindex + extractedSentence.length
                                    val dataString =
                                        "${nextWord.surah}|${nextWord.ayah}|${currentWord.wordno}|${nextWord.wordno}|$startindex|$endindex|$extractedSentence|$translationBuilder"
                                    extractedSentences.add(dataString)
                                    mousufSifaPairs.add(
                                        Pair(
                                            mousuf,
                                            sifa
                                        ) as Pair<String, String>
                                    )
                                }
                            }
                    }


                }
            }
        }
        return extractedSentences
    }

    fun isBrokenPluralPattern(currentWord: String?): Boolean {
        if (currentWord == null) return false


        val brokenPluralPatterns = listOf(
            "فُعُوْلٌ",
            "أَفْعَالٌ",
            "فِعَالٌ",
            "فُعُلٌ",
            "فِعْلَانٌ",
            "فَعْلَانُ",
            "فُعَلٌ",
            "فُعُولٌ",
            "فُعَلَاءُ",
            "فُعْلَانٌ",
            "فَعَالِيلُ",
            "فُعُولَاتٌ",
            "مَفَاعِلُ",
            "فَوَاعِلُ",
            "مَفَاعِيلُ",
            "فُعْلَةٌ",
            "أَفَاعِيلُ",
            "أَفْعِلَةٌ",
            "فَعَالٍ",
            "فِعْلَةٌ",
            "فِعَالَاتٌ",
            "فِعْلَانٌ",
            "فِعَالِين",
            "أَفْعُلَةٌ",
            "فُعَلٌ",
            // Add accusative/genitive cases
            "أَفْعَالَ",
            "فُعُولَ",
            "فِعَالَ",
            "فُعَلَاءَ",
            "فُعْلَانَ",
            "فَعَالِيلَ",
            "فُعُولَاتَ",
            "مَفَاعِلَ",
            "فَوَاعِلَ",
            "مَفَاعِيلَ",
            "فُعْلَةَ",
            "أَفَاعِيلَ",
            "أَفْعِلَةَ",
            "فَعَالِيَ",
            "فِعْلَةَ",
            "فِعَالَاتَ",
            "فِعْلَانَ",
            "فِعَالِين",
            "أَفْعُلَةَ",
            "فُعَلَ"
        )
        // Extract consonants from the current word
        val wordConsonants = extractConsonants(currentWord)
        if (wordConsonants.length != 3) return false // Assuming broken plurals have 3 root consonants

        // Loop through each broken plural pattern
        for (pattern in brokenPluralPatterns) {
            // Extract consonants from the pattern (should be 3 root letters in most cases)
            val patternConsonants = extractConsonants(pattern)

            // If both have the same number of consonants (usually 3), replace pattern consonants with word consonants
            if (patternConsonants.length == 3) {
                // Replace consonants in the pattern with those of the current word
                val replacedPattern = pattern.replaceFirst(patternConsonants[0], wordConsonants[0])
                    .replaceFirst(patternConsonants[1], wordConsonants[1])
                    .replaceFirst(patternConsonants[2], wordConsonants[2])

                // Compare the modified pattern with the current word
                if (replacedPattern == currentWord) {
                    return true // Match found
                }
            }
        }
        return false // No match found
    }

    fun extractMudafMudafIlaih(corpusList: List<CorpusEntity>, qurantext: String): List<String> {
        val results = mutableListOf<String>()

        for ((i, entry) in corpusList.withIndex()) {
            // Construct the current word and check if it's a valid mudaf
            if (entry.surah == 20 && entry.ayah == 100 && entry.wordno == 6) {

                println("check")
            }

            if (entry.surah == 22 && entry.ayah == 24 && entry.wordno == 8) {

                println("check")
            }
            val constructedWord =
                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            val englishtranslation = entry.en
            val isIndefniteNoun =
                (entry.tagone == "N") || (entry.tagone != "DET" && entry.tagtwo == "N")
                        || (entry.tagtwo != "DET" && entry.tagthree == "N") || (entry.tagthree != "DET" && entry.tagfour == "N")
            val isZarf =
                (entry.tagone == "T") || (entry.tagone != "DET" && entry.tagtwo == "T")
                        || (entry.tagtwo != "DET" && entry.tagthree == "T") || (entry.tagthree != "DET" && entry.tagfour == "T")
            var noundDetails = ""// Check for mudaf conditions
            if (entry.tagone == "T") {
                noundDetails = entry.detailsone.toString()
            } else if (entry.tagtwo == "T") {
                noundDetails = entry.detailstwo.toString()

            } else if (entry.tagthree == "T") {
                noundDetails = entry.detailsthree.toString()
            } else if (entry.tagfour == "T") {
                noundDetails = entry.detailsfour.toString()
            } else

                if (entry.tagone == "N") {
                    noundDetails = entry.detailsone.toString()
                } else if (entry.tagtwo == "N") {
                    noundDetails = entry.detailstwo.toString()

                } else if (entry.tagthree == "N") {
                    noundDetails = entry.detailsthree.toString()
                } else if (entry.tagfour == "N") {
                    noundDetails = entry.detailsfour.toString()
                }
            val nounCase = noundDetails.extractCase()
            val isMudafori = isIndefniteNoun || isZarf && // No DET tag

                    !constructedWord.contains("ٌ") && !constructedWord.contains("ً") && !constructedWord.contains(
                "ٍ"
            ) && // No nunation
                    (nounCase == "GEN" || nounCase == "ACC" || nounCase == "NOM") // Allowed cases
            //   val tanweenRegex = Regex("[\\u064B-\\u0652-\\U064C]")
            val tanweenRegex = Regex("[\\u064B-\\u064D-\\u064C]")
            //  val tanweenRegex = Regex("[ًٌٍ]") // Matches the three Tanween characters


            val result = hasNunation(constructedWord)
            if (result) {
                println("check")
            }
            val isMudaf = (isIndefniteNoun || isZarf) &&
                    !result && // No nunation
                    (nounCase == "GEN" || nounCase == "ACC" || nounCase == "NOM")

            // If mudaf conditions met, look for mudaf ilaih in the next entry
            if ((entry.tagone == "N" && entry.tagtwo == "PRON") || (entry.tagtwo == "N" && entry.tagthree == "PRON")) {
                val fullword = constructedWord
                val trimmedWord = trimWord(fullword)
                val startindex = qurantext.indexOf(trimmedWord)
                val endindex = startindex + constructedWord.length
                var extractedText = ""
                if (startindex != -1 && endindex != -1) {
                    extractedText = qurantext.substring(startindex, endindex)

                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$startindex|$endindex|$extractedText|$englishtranslation"
                    results.add(dataString)

                } else {
                    val fullword = constructedWord
                    var newstartindex = 0
                    val extracted = "chek"
                    val dataString =
                        "${entry.surah}|${entry.ayah}|${entry.wordno}|${entry.wordno}|$startindex|$endindex|$fullword|$englishtranslation"
                    results.add(dataString)
                }
            } else


                if (isMudaf && i + 1 < corpusList.size) {
                    val nextEntry = corpusList[i + 1]
                    val nextConstructedWord =
                        nextEntry.araone + nextEntry.aratwo + nextEntry.arathree + nextEntry.arafour + nextEntry.arafive
                    val nextenglishword = nextEntry.en
                    val englishtran = englishtranslation + " " + nextenglishword
                    var nextEntryDetails = ""
                    if (nextEntry.tagone == "PN" || (nextEntry.tagone == "N" || nextEntry.tagone == "ADJ")) {

                        nextEntryDetails = nextEntry.detailsone.toString()

                    } else if ((nextEntry.tagtwo == "PN" || nextEntry.tagtwo == "N" || nextEntry.tagtwo == "ADJ")) {
                        nextEntryDetails = nextEntry.detailstwo.toString()
                    } else if ((nextEntry.tagthree == "PN" || nextEntry.tagthree == "N" || nextEntry.tagthree == "ADJ")) {
                        nextEntryDetails = nextEntry.detailsthree.toString()
                    } else if (nextEntry.tagfour == "PN" || nextEntry.tagfour == "N" || nextEntry.tagfour == "ADJ") {
                        nextEntryDetails = nextEntry.detailsfour.toString()
                    }


                    // Check for mudaf ilaih conditions
                    val isNoun =
                        (nextEntry.tagone == "PN" || nextEntry.tagone == "N") ||
                                (nextEntry.tagtwo == "PN" || nextEntry.tagtwo == "N") ||
                                (nextEntry.tagthree == "PN" || nextEntry.tagthree == "N")

                    val isAdjective =
                        (nextEntry.tagone == "ADJ") ||
                                (nextEntry.tagtwo == "ADJ") ||
                                (nextEntry.tagthree == "ADJ")

                    val isMudafIlaih = isNoun || isAdjective && // Must be noun or pronoun
                            nextEntryDetails.contains("GEN") // Must be in genitive case



                    if (isMudafIlaih) {

                        val fullword = constructedWord + " " + nextConstructedWord
                        val startindex = qurantext.indexOf(fullword)
                        val endindex = startindex + fullword.length
                        var extractedText = ""
                        if (startindex != -1 && endindex != -1) {
                            extractedText = qurantext.substring(startindex, endindex)

                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${nextEntry.wordno}|$startindex|$endindex|$extractedText|$englishtran"
                            results.add(dataString)

                        } else {
                            val fullword = constructedWord + " " + nextConstructedWord
                            var newstartindex = 0
                            val extracted = "chek"
                            val dataString =
                                "${entry.surah}|${entry.ayah}|${entry.wordno}|${nextEntry.wordno}|$newstartindex|$endindex|$fullword|$englishtran"
                            results.add(dataString)
                        }

                    }
                }
        }

        return results
    }

    fun trimWord(word: String): String {
        return word.replace(Regex("[,ٓ]+$"), "")
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
                        (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَنْ" || previousEntry.araone == "لَن" || previousEntry.araone == "لَّن")) ||

                        (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "إِنْ" || previousEntry.arathree == "مَا" || previousEntry.arathree == "مَآ" || previousEntry.arathree == "مَّا")) ||
                        (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "إِنْ" || previousEntry.arafour == "مَا" || previousEntry.arafour == "مَآ" || previousEntry.arafour == "مَّا")) ||
                        (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "لَآ" || previousEntry.arathree == "لَا" || previousEntry.arathree == "لَّا")) ||
                        (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "لَآ" || previousEntry.arafour == "لَا" || previousEntry.arafour == "لَّا")) ||
                        (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "لَنْ" || previousEntry.arathree == "لَن" || previousEntry.arathree == "لَّن")) ||
                        (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "لَنْ" || previousEntry.arafour == "لَن" || previousEntry.arafour == "لَّن"))


                    ) {
                        // Negation condition found, skip this case
                        negationFound = true
                        break
                    }
                }

                // If no negation was found in the previous entries, proceed with extraction
                if (!negationFound) {
                    /*      val sentenceBuilder = StringBuilder()

                          // Capture 3 words before "إِلَّا"
                          val startWordNo =
                              corpus[maxOf(0, i -3)].wordno // Get up to 3 words before "إِلَّا"
                          for (k in maxOf(0, i - 3) until i) {
                              val word =
                                  "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                              sentenceBuilder.append(word).append(" ")
                          }*/

                    val sentenceBuilder = StringBuilder()

// Capture up to 4 words before "إِلَّا" or beginning of line
                    val startWordNo = corpus[maxOf(0, i - 4)].wordno
                    for (k in i - 1 downTo maxOf(0, i - 4)) { // Iterate in reverse
                        val word =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        sentenceBuilder.insert(0, "$word ") // Insert at the beginning
                    }


                    /*
                                        // Capture "إِلَّا" and 2 words after "إِلَّا"
                                        var endWordNo = startWordNo
                                        for (k in i until minOf(
                                            i + 2,
                                            corpus.size
                                        )) { // i is where "إِلَّا" is, capture 2 more after it
                                            val word =
                                                "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                                            sentenceBuilder.append(word).append(" ")

                                            endWordNo = corpus[k].wordno // Update the end word number
                                        }
                                        */

                    var endWordNo = startWordNo
                    var wordCount = 0

                    for (k in i until minOf(
                        i + 4,
                        corpus.size
                    )) { // Limit loop to 4 words or end of corpus
                        val word =
                            "${corpus[k].araone}${corpus[k].aratwo}${corpus[k].arathree}${corpus[k].arafour}${corpus[k].arafive}".trim()
                        sentenceBuilder.append(word).append(" ")

                        endWordNo = corpus[k].wordno
                        wordCount++
                    }


                    val fullSentenceStr = sentenceBuilder.toString().trim()

                    // Now find the combined sentence in the spannableVerse
                    val sentenceStartIndex = spannableVerse.indexOf(fullSentenceStr)
                    val sentenceEndIndex =
                        if (sentenceStartIndex != -1) sentenceStartIndex + fullSentenceStr.length else -1

                    // Check if this sentence is already added to avoid duplicates
                    if (sentenceStartIndex != -1 && !uniqueSentences.contains(fullSentenceStr)) {
                        val dataString =
                            "${entry.surah}|${entry.ayah}|$startWordNo|$endWordNo|$sentenceStartIndex|$sentenceEndIndex|$fullSentenceStr|$spannableVerse"
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

    fun extractSentenceAndTranslationFromNasabIndices(
        corpus: List<CorpusEntity>,
        nasbEntity: NewNasbEntity,
        quranText: String

    ): List<String> {
        val utils = Utils(QuranGrammarApplication.context)
        var harfofverse: String
        var ismofverse: String
        var khabarofverse: String
        val indexstart = nasbEntity.indexstart
        val indexend = nasbEntity.indexend
        val ismstartindex = nasbEntity.ismstart
        val ismendindex = nasbEntity.ismend
        val khabarstartindex = nasbEntity.khabarstart
        val khabarendindex = nasbEntity.khabarend
        val quranverses: String = quranText
        harfofverse = quranverses.substring(indexstart, indexend)
        ismofverse = quranverses.substring(ismstartindex, ismendindex)
        khabarofverse = quranverses.substring(khabarstartindex, khabarendindex)
        val isharfb = indexstart >= 0 && indexend > 0
        val isism = ismstartindex >= 0 && ismendindex > 0
        val iskhabar = khabarstartindex >= 0 && khabarendindex > 0
        val allPartOfIsm = isharfb && isism && iskhabar
        val harfAandKhabar = isharfb && iskhabar
        val harfAndIsm = isharfb && isism
        val harfword = nasbEntity.harfwordno
        val ismstartwordno = nasbEntity.ismstartwordno
        val ismendwordno = nasbEntity.ismendwordno
        val jawbSword = nasbEntity.khabarstartwordno
        val jawabEword = nasbEntity.khabarendwordno
        val translationBuilder = StringBuilder()
        val khabarsb = StringBuilder()
        var harfspannble: SpannableString
        var harfismspannable: SpannableString
        var khabarofversespannable: SpannableString
        val hasbarray: MutableList<SpannableString> = ArrayList()
        var arabicstring = ""
        var typeoneindexstart = indexstart
        var typeoneindexend = khabarendindex
        var typeonewordfrom = harfword
        var typeonewordto = 0
        if (jawbSword == 0) {
            typeonewordto = harfword + 1
        } else {
            typeonewordto = jawbSword
        }
        var datastrfull = "" //harfword,jawabEword,indexstart,khabarendindex
        var dataharfism = ""//harfword,shartEword,indexstart,ismendindex
        var dataharfkhabar = ""//harfword,jawabEword,indexstart,khabarendindex,
        if (nasbEntity.surah == 5 && nasbEntity.ayah == 49) {
            println("check")
        }
        if (allPartOfIsm) {
            val isismkhabarconnected = nasbEntity.khabarstart - nasbEntity.ismend
            harfspannble = SpannableString(harfofverse)
            harfismspannable = SpannableString(ismofverse)
            khabarofversespannable = SpannableString(khabarofverse)


            if (isismkhabarconnected == 1) {
                val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity!!.surah,
                    nasbEntity!!.ayah, harfword, jawabEword
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
            } else {
                val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                    nasbEntity.surah,
                    nasbEntity.ayah
                )
                if (wbwayah != null) {
                    for (w in wbwayah) {
                        StringBuilder()
                        val temp: StringBuilder = w?.let { getSelectedTranslation(it) }!!
                        if (w.wordno == harfword) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in ismstartwordno..ismendwordno) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in jawbSword..jawabEword) {
                            //     sb. append("... ");
                            khabarsb.append(temp).append(" ")
                        }
                    }
                }
                translationBuilder.append(".....")
                translationBuilder.append(khabarsb)
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
            harfofverse = quranverses.substring(indexstart, indexend)
            ismofverse = quranverses.substring(ismstartindex, ismendindex)
            khabarofverse = quranverses.substring(khabarstartindex, khabarendindex)
            hasbarray.add(SpannableString.valueOf(harfofverse + ' ' + ismofverse + ' ' + khabarofverse))
            arabicstring = harfofverse + ' ' + ismofverse + ' ' + khabarofverse
            hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
        } else if (harfAandKhabar) {
            harfspannble = SpannableString(harfofverse)
            khabarofversespannable = SpannableString(khabarofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            khabarofversespannable.setSpan(
                jawabshartspanDark,
                0,
                khabarofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble, " ", khabarofversespannable)
            arabicstring = harfofverse + ' ' + khabarofverse
            //     StringBuilder sb = new StringBuilder();
            val wordfrom = nasbEntity.harfwordno
            var wordto: Int
            val split = khabarofverse.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            wordto = if (split.size == 1) {
                nasbEntity.khabarstartwordno
            } else {
                nasbEntity.khabarendwordno
            }
            val isconnected = nasbEntity.khabarstart - nasbEntity.indexend
            if (isconnected == 1) {
                val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    wordfrom,
                    wordto
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                hasbarray.add(SpannableString.valueOf(charSequence))
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            } else {
                val wordfroms = nasbEntity.harfwordno
                val list = utils.getwbwQuranbTranslationbyrange(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    wordfrom,
                    wordfroms
                )
                val from = nasbEntity.khabarstartwordno
                var to = nasbEntity.khabarendwordno
                if (to == 0) {
                    to = from
                }
                translationBuilder.append(list[0].en).append(".......")
                when ("en") {
                    "en" -> translationBuilder.append(list[0].en).append(".......")
                    "ur" -> translationBuilder.append(list[0].ur).append(".......")
                    "bn" -> translationBuilder.append(list[0].bn).append(".......")
                    "id" -> translationBuilder.append(list[0].id).append(".......")
                }


                val lists: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    from,
                    to
                )
                if (lists != null) {
                    for (w in lists) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
        } else if (harfAndIsm) {
            harfshartspanDark = ForegroundColorSpan(GOLD)
            shartspanDark = ForegroundColorSpan(Color.GREEN)
            jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            harfspannble = SpannableString(harfofverse)
            harfismspannable = SpannableString(ismofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            harfismspannable.setSpan(
                shartspanDark,
                0,
                ismofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequences = TextUtils.concat(
                harfspannble,
                " $harfismspannable"
            )
            hasbarray.add(SpannableString.valueOf(charSequences))
            arabicstring = harfofverse + ' ' + ismofverse
            //    kanaarray.add(SpannableString.valueOf(charSequence));
            val ismconnected = nasbEntity.ismstart - nasbEntity.indexend
            val wordfrom = nasbEntity.harfwordno
            val wordto = nasbEntity.ismendwordno
            if (ismconnected == 1) {
                val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    wordfrom,
                    wordto
                )
                if (list != null) {
                    for (w in list) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            } else {
                //    kanaarray.add(SpannableString.valueOf(list.get(0).getEn()));
                val from = nasbEntity.harfwordno
                val ismfrom = nasbEntity.ismstartwordno
                val ismto = nasbEntity.ismendwordno
                //     sb.append(list.get(0).getEn()).append("----");
                val harf: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    from,
                    from
                )
                val ism: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                    nasbEntity.surah,
                    nasbEntity.ayah,
                    ismfrom,
                    ismto
                )
                if (harf != null) {
                    for (w in harf) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                translationBuilder.append(".....")
                if (ism != null) {
                    for (w in ism) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                harfofverse = quranverses.substring(indexstart, indexend)
                ismofverse = quranverses.substring(ismstartindex, ismendindex)
                hasbarray.add(SpannableString(harfofverse + ' ' + ismofverse))
                hasbarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
        } else if (isharfb) {
            harfspannble = SpannableString(harfofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble)
            hasbarray.add(SpannableString.valueOf(charSequence))
            arabicstring = harfofverse
            val wordfroms = nasbEntity.harfwordno

            val list = utils.getwbwQuranbTranslationbyrange(
                nasbEntity.surah,
                nasbEntity.ayah,
                wordfroms,
                wordfroms
            )


            val sbss = StringBuffer()
            when ("en") {
                "en" -> sbss.append(list[0].en).append(".......")
                "ur" -> sbss.append(list[0].ur).append(".......")
                "bn" -> sbss.append(list[0].bn).append(".......")
                "id" -> sbss.append(list[0].id).append("..........")
            }
            hasbarray.add(SpannableString(harfofverse))
            hasbarray.add(SpannableString.valueOf(sbss))
        }
        //  var datastrfull="" //harfword,jawabEword,indexstart,khabarendindex
        //  var dataharfism=""//harfword,shartEword,indexstart,ismendindex
        //  var datatypethreestr=""//harfword,harfword,indexstart,indexend,
        val result = mutableListOf<String>()
        if (allPartOfIsm) {
            val allpart = "allpart"
            val dataString =
                "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$khabarendindex|$harfword|$jawabEword|$arabicstring|$translationBuilder|$quranverses|$allpart"



            result.add(dataString)

        } else
            if (harfAndIsm) {
                //quranText.substring(indexstart,ismendindex)
                val harfism = "harfism-khabarmahdoof"
                val dataString =
                    "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$ismendindex|$harfword|$ismendwordno|$arabicstring|$translationBuilder|$quranverses|$harfism"
                result.add(dataString)
            } else if (isharfb && !harfAandKhabar && !harfAndIsm) {
                val isharf = "isharfmahdoof"
                val dataString =
                    "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$indexend|$harfword|$harfword|$arabicstring|$translationBuilder|$quranverses|$isharf"
                result.add(dataString)
            } else if (harfAandKhabar) { //   var dataharfkhabar=""//harfword,jawabEword,indexstart,khabarendindex,//might be zero hence khabarstartwordno  khabarendwordno
                val harfkhabar = "harfkhabar"
                if (jawbSword == 0) {
                    val hword = harfword + 1
                    val dataString =
                        "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$khabarendindex|$harfword|$hword|$arabicstring|$translationBuilder|$quranverses|$harfkhabar"
                    result.add(dataString)
                } else {
                    val dataString =
                        "${nasbEntity.surah}|${nasbEntity.ayah}|${nasbEntity.indexstart}|$khabarendindex|$harfword|$jawabEword|$arabicstring|$translationBuilder|$quranverses|$harfkhabar"
                    result.add(dataString)
                }
            }








        return result

    }


     fun getWordTranslation(list: List<wbwentity>): String {
        val wbwEntity = list.get(0) // Assuming list is not null and has at least one element
        return when ("en") {
            "en" -> wbwEntity.en
            "ur" -> wbwEntity.ur!!
            "bn" -> wbwEntity.bn
            "id" -> wbwEntity.id.toString()
            else -> {
                wbwEntity.en
            }
        }
    }

     fun appendTranslation(

        sb: StringBuilder,
        wordByWordEntities: List<wbwentity?>?,
        wordRange: IntRange? = null
    ) {
        if (wordByWordEntities != null) {
            for (w in wordByWordEntities) {
                /*
                val temp: StringBuilder
if (w != null) {
    val translation = getSelectedTranslation(w)
    temp = if (translation != null) {
        translation
    } else {
        StringBuilder()
    }
} else {
    temp = StringBuilder()
}
                 */
                val temp = w?.let { getSelectedTranslation(it) }
                    ?: StringBuilder() // Handle null wbwentity
                if (w != null) {
                    if (wordRange == null || w.wordno in wordRange) {
                        sb.append(temp).append(" ")
                    }
                }
            }
        }
    }

    fun extractKana(
        corpus: List<CorpusEntity>,
        kana: NewKanaEntity,
        quranText: String,
        translation: String,

        ): List<String> {
        //  val utils = Utils(requireContext())
        //  val kanaSurahAyahnew: List<NewKanaEntity?>? =
        //    utils.getKanaSurahAyahnew(chapterid, ayanumber)
        val result = mutableListOf<String>()
        var arabicString = ""
        var translationStr = ""
        val kanaarray: MutableList<SpannableString> = ArrayList()
        var harfkana: ForegroundColorSpan?
        var kanaism: ForegroundColorSpan?
        var ismconnected = 0
        var kanakhbar: ForegroundColorSpan?
        if (dark) {
            harfkana = ForegroundColorSpan(GOLD)
            kanaism = ForegroundColorSpan(ORANGE400)
            kanakhbar = ForegroundColorSpan(Color.CYAN)
        } else {
            harfkana = ForegroundColorSpan(FORESTGREEN)
            kanaism = ForegroundColorSpan(KASHMIRIGREEN)
            kanakhbar = ForegroundColorSpan(WHOTPINK)
        }
        if (kana != null) {

            //System.out.println("CHECK");
            var harfofverse = ""
            var ismofverse: String
            var khabarofverse = ""
            val start = kana.indexstart
            val end = kana.indexend
            val isstart = kana.ismkanastart
            val issend = kana.ismkanaend
            val khabarstart = kana.khabarstart
            val khabarend = kana.khabarend
            try {
                harfofverse = quranText.substring(start, end)
            } catch (e: StringIndexOutOfBoundsException) {
                quranText.substring(end, start)
            }

            ismofverse = if (issend > isstart) {
                quranText.substring(isstart, issend)
            } else {
                ""
            }
            try {
                khabarofverse = quranText.substring(khabarstart, khabarend)
            } catch (e: StringIndexOutOfBoundsException) {
                println("check")
            }

            val isharfb = start >= 0 && end > 0
            val isism = isstart >= 0 && issend > 0
            val isjawab = khabarstart >= 0 && khabarend > 0
            val allPartofKana = isharfb && isism && isjawab
            val kanaHarfAndJawabOnly = isharfb && isjawab
            val kanaHarfAndIsmOnly = isharfb && isism
            val harfword = kana.harfwordno
            val ismEword = kana.ismendword
            val ismSword = kana.ismwordo

            val khabarSword = kana.khabarstartwordno
            val khabarEword = kana.khabarendwordno
            var harfspannble: SpannableString
            var harfismspannable: SpannableString
            var khabarofversespannable: SpannableString
            if (allPartofKana) {
                if (corpus[0].surah == 2 && corpus[0].ayah == 114) {
                    println("check")
                }
                harfspannble = SpannableString(harfofverse)
                harfismspannable = SpannableString(ismofverse)
                khabarofversespannable = SpannableString(khabarofverse)

                harfspannble.setSpan(
                    harfkana,
                    0,
                    harfofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                harfismspannable.setSpan(
                    kanaism,
                    0,
                    ismofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                khabarofversespannable.setSpan(
                    kanakhbar,
                    0,
                    khabarofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (kana.ismkanastart > kana.khabarstart) {
                    val charSequence = TextUtils.concat(
                        harfspannble,
                        " ",
                        khabarofversespannable,
                        " ",
                        harfismspannable
                    )
                    arabicString = harfofverse + ' ' + khabarofverse + ' ' + ismofverse
                    kanaarray.add(SpannableString.valueOf(charSequence))
                } else {
                    val charSequence = TextUtils.concat(
                        harfspannble,
                        " ",
                        harfismspannable,
                        " ",
                        khabarofversespannable
                    )
                    arabicString = harfofverse + ' ' + ismofverse + ' ' + khabarofverse
                    kanaarray.add(SpannableString.valueOf(charSequence))
                }
                val sb = StringBuilder()
                val ismorkhabarsb = StringBuilder()
                val utils = Utils(QuranGrammarApplication.context)
                val wbwayah: List<wbwentity?>? = utils.getwbwQuranBySurahAyah(
                    corpus!![0].surah,
                    corpus!![0].ayah
                )
                if (wbwayah != null) {
                    for (w in wbwayah) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w!!)
                        if (w.wordno == harfword) {
                            sb.append(temp.append(" "))
                        } else if (w.wordno in ismSword..ismEword) {
                            sb.append(temp).append(" ")
                        } else if (w.wordno in khabarSword..khabarEword) {
                            ismorkhabarsb.append(temp).append(" ")
                        }
                    }
                }
                sb.append("... ")
                sb.append(ismorkhabarsb)
                kanaarray.add(SpannableString.valueOf(sb.toString()))
                var type = ""
                if (kana.ismkanastart > kana.khabarstart) {
                    type = "kanaallismlate"
                } else {
                    type = "kanallsequence"
                }

                val dataString =
                    "${kana.surah}|${kana.ayah}|${kana.indexstart}|${kana.khabarend}|${kana.harfwordno}|${kana.khabarendwordno}" +
                            "|${arabicString}|${sb.toString()}|$translation" +
                            "|$quranText|$type"
                result.add(dataString)
                return result
                //  CharSequence first = TextUtils.concat(harfspannble," ",shartofverse);
            } else if (kanaHarfAndJawabOnly) {
                if (corpus[0].surah == 2 && corpus[0].ayah == 89) {
                    println("check")
                }
                if (dark) {
                    harfkana = ForegroundColorSpan(GOLD)
                    kanaism = ForegroundColorSpan(ORANGE400)
                    kanakhbar = ForegroundColorSpan(Color.CYAN)
                } else {
                    harfkana = ForegroundColorSpan(FORESTGREEN)
                    kanaism = ForegroundColorSpan(KASHMIRIGREEN)
                    kanakhbar = ForegroundColorSpan(WHOTPINK)
                }
                harfspannble = SpannableString(harfofverse)
                khabarofversespannable = SpannableString(khabarofverse)
                harfspannble.setSpan(
                    harfkana,
                    0,
                    harfofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                khabarofversespannable.setSpan(
                    kanakhbar,
                    0,
                    khabarofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val charSequence = TextUtils.concat(harfspannble, " ", khabarofversespannable)
                kanaarray.add(SpannableString.valueOf(charSequence))

                val sb = StringBuilder()
                val wordfrom = kana.harfwordno
                var wordto: Int
                val split = khabarofverse.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                wordto = if (split.size == 1) {
                    kana.khabarstartwordno
                } else {
                    kana.khabarendwordno
                }
                val isconnected = kana.khabarstartwordno - kana.harfwordno
                if (isconnected == 1) {
                    val utils = Utils(QuranGrammarApplication.context)
                    val list: List<wbwentity?>? = utils.getwbwQuranbTranslation(
                        corpus!![0].surah,
                        corpus!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w!!)
                            sb.append(temp).append(" ")
                        }
                    }
                    arabicString = harfofverse + ' ' + khabarofverse
                    translationStr = sb.toString()
                    kanaarray.add(SpannableString.valueOf(sb.toString()))
                } else {
                    val wordfroms = kana.harfwordno
                    val ut = Utils(QuranGrammarApplication.context)
                    val list = ut.getwbwQuranbTranslationbyrange(
                        corpus!![0].surah,
                        corpus!![0].ayah,
                        wordfrom,
                        wordfroms
                    )
                    val from = kana.khabarstartwordno
                    var to = kana.khabarendwordno
                    if (to == 0) {
                        to = from
                    }
                    val sbs = getTranslationForLanguage(list)

                    //    sb.append(list).append("----");
                    val utils = Utils(QuranGrammarApplication.context)
                    val lists: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpus!![0].surah,
                        corpus!![0].ayah,
                        from,
                        to
                    )
                    if (lists != null) {
                        for (w in lists) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            sbs.append(temp).append(" ")
                        }
                    }
                    translationStr = sbs.toString()
                    kanaarray.add(SpannableString.valueOf(sb.toString()))
                    arabicString = harfofverse + "..." + khabarofverse
                }
                val type = "kanaandjawabonly"
                val dataString =
                    "${kana.surah}|${kana.ayah}|${kana.indexstart}|${kana.khabarend}|${kana.harfwordno}|${kana.khabarstartwordno}" +
                            "|${arabicString}|${translationStr.toString()}|$translation" +
                            "|$quranText|$type"
                result.add(dataString)
                return result
            } else if (kanaHarfAndIsmOnly) {
                if (dark) {
                    harfkana = ForegroundColorSpan(GOLD)
                    kanaism = ForegroundColorSpan(ORANGE400)
                    kanakhbar = ForegroundColorSpan(Color.CYAN)
                } else {
                    harfkana = ForegroundColorSpan(FORESTGREEN)
                    kanaism = ForegroundColorSpan(KASHMIRIGREEN)
                    kanakhbar = ForegroundColorSpan(WHOTPINK)
                }
                harfspannble = SpannableString(harfofverse)
                harfismspannable = SpannableString(ismofverse)
                harfspannble.setSpan(
                    harfkana,
                    0,
                    harfofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                harfismspannable.setSpan(
                    kanaism,
                    0,
                    ismofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val charSequences = TextUtils.concat(
                    harfspannble,
                    " $harfismspannable"
                )
                kanaarray.add(SpannableString.valueOf(charSequences))
                //    kanaarray.add(SpannableString.valueOf(charSequence));
                val sb = StringBuilder()
                val ismorkhabarsb = StringBuilder()
                ismconnected = kana.ismkanastart - kana.indexend
                val wordfrom = kana.harfwordno
                val wordto = kana.ismwordo
                if (ismconnected == 1) {
                    val utils = Utils(QuranGrammarApplication.context)
                    val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                        corpus!![0].surah,
                        corpus!![0].ayah,
                        wordfrom,
                        wordto
                    )
                    if (list != null) {
                        for (w in list) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            sb.append(temp).append(" ")
                        }
                    }
                    arabicString = harfofverse + ' ' + ismofverse
                    kanaarray.add(SpannableString.valueOf(sb.toString()))
                } else {
                    //  ArrayList<wbwentity> list = utils.getwbwQuranbTranslation(corpusSurahWord!!.get(0).getSurah(), corpusSurahWord!!.get(0).getAyah(), wordfroms, wordfroms);
                    val utils = Utils(QuranGrammarApplication.context)
                    val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                        corpus!![0].surah,
                        corpus!![0].ayah
                    )
                    if (wbwayah != null) {
                        for (w in wbwayah) {
                            StringBuilder()
                            val temp: StringBuilder = getSelectedTranslation(w)
                            if (w.wordno == harfword) {
                                sb.append(temp).append(" ")
                            } else if (w.wordno in ismSword..ismEword) {
                                ismorkhabarsb.append(temp).append(" ")
                            }
                        }
                    }
                    sb.append(".....")
                    sb.append(ismorkhabarsb)
                    kanaarray.add(SpannableString.valueOf(sb.toString()))

                    arabicString = harfofverse + ".... " + ismofverse
                }

                val type = "harfismonly"
                //quranText.substring(kana.indexstart, kana.ismkanaend)
                //           val harfword = kana.harfwordno
                //            val ismEword = kana.ismendword
                val dataString =
                    "${kana.surah}|${kana.ayah}|${kana.indexstart}|${kana.ismkanaend}|${kana.harfwordno}|${kana.ismendword}" +
                            "|${arabicString}|${sb.toString()}|$translation" +
                            "|$quranText|$type"
                result.add(dataString)
                return result


            } else if (isharfb) {
                harfspannble = SpannableString(harfofverse)
                harfspannble.setSpan(
                    harfkana,
                    0,
                    harfofverse.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val charSequence = TextUtils.concat(harfspannble)
                kanaarray.add(SpannableString.valueOf(charSequence))
                val wordfroms = kana.harfwordno
                val utils = Utils(QuranGrammarApplication.context)
                val list = utils.getwbwQuranbTranslationbyrange(
                    corpus!![0].surah,
                    corpus!![0].ayah,
                    wordfroms,
                    wordfroms
                )
                val sb = getTranslationForLanguage(list)
                kanaarray.add(SpannableString.valueOf(sb))
                //quranText.substring(kana.indexstart,kana.indexend)
                val type = "harfwithismpronoun"
                val dataString =
                    "${kana.surah}|${kana.ayah}|${kana.indexstart}|${kana.indexend}|${kana.harfwordno}|${kana.harfwordno}" +
                            "|${harfofverse}|${sb.toString()}|$translation" +
                            "|$quranText|$type"
                result.add(dataString)
                return result

            }
            // kanaarray.add(spannable);

        }
        return result
    }

     fun getTranslationForLanguage(list: List<wbwentity>): StringBuffer {
        val sb = StringBuffer()
        val whichwbw = "en"
        when (whichwbw) {
            "en" -> sb.append(list[0].en).append(".......")
            "ur" -> sb.append(list[0].ur).append(".......")
            "bn" -> sb.append(list[0].bn).append(".......")
            "id" -> sb.append(list[0].id).append(".......")
            else -> {
                sb.append(list[0].en).append(".......")

            }
        }
        return sb
    }

    fun extractSentenceAndTranslationFromShartIndices(
        corpus: List<CorpusEntity>,
        shartEntity: NewShartEntity,
        quranText: String

    ): List<String> {

        var arabicstring = ""
        // val result = mutableListOf<String>()
        val result = mutableListOf<String>()

        val utils = Utils(QuranGrammarApplication.context)
        var harfofverse: String
        var shartofverse: String
        var jawabofverrse: String
        var translationBuilder = StringBuilder()
        val indexstart = shartEntity.indexstart
        val comment = shartEntity.comment
        val indexend = shartEntity.indexend
        val shartindexstart = shartEntity.shartindexstart
        val shartindexend = shartEntity.shartindexend
        val jawabstart = shartEntity.jawabshartindexstart
        val jawabend = shartEntity.jawabshartindexend
        val harfword = shartEntity.harfwordno
        val shartSword = shartEntity.shartstatwordno
        val shartEword = shartEntity.shartendwordno
        val jawbSword = shartEntity.jawabstartwordno
        val jawabEword = shartEntity.jawabendwordno
        harfofverse = quranText.substring(indexstart, indexend)
        shartofverse = quranText.substring(shartindexstart, shartindexend)
        jawabofverrse = quranText.substring(jawabstart, jawabend)
        val isharfb = indexstart >= 0 && indexend > 0
        val isshart = shartindexstart >= 0 && shartindexend > 0
        val isjawab = jawabstart >= 0 && jawabend > 0
        val allPartofShart = isharfb && isshart && isjawab
        val harfAndShartOnly = isharfb && isshart
        var harfspannble: SpannableString
        var shartspoannable: SpannableString
        var jawabshartspannable: SpannableString
        var whichwbw = "en"
        val sbjawab = StringBuilder()
        var htmlString = ""
        val shartarray: MutableList<SpannableString> = ArrayList()
        val prefs =
            android.preference.PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context)
        val preferences = prefs.getString("theme", "dark")
        val dark = preferences == "dark" || preferences == "blue" || preferences == "green"
        if (allPartofShart) {
            harfspannble = SpannableString(harfofverse)
            shartspoannable = SpannableString(shartofverse)
            jawabshartspannable = SpannableString(jawabofverrse)
            val connected = jawabstart - shartindexend
            if (dark) {
                //    harfshartspanDark = new ForegroundColorSpan(MIDNIGHTBLUE);
                shartspanDark = ForegroundColorSpan(ORANGE400)
                jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            } else {
                //   harfshartspanDark = new ForegroundColorSpan(FORESTGREEN);
                shartspanDark = ForegroundColorSpan(GREENDARK)
                jawabshartspanDark = ForegroundColorSpan(WHOTPINK)
            }
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            shartspoannable.setSpan(
                shartspanDark,
                0,
                shartofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            jawabshartspannable.setSpan(
                jawabshartspanDark,
                0,
                jawabofverrse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence =
                TextUtils.concat(
                    harfspannble,
                    " ",
                    shartspoannable,
                    " ",
                    jawabshartspannable
                )
            shartarray.add(SpannableString.valueOf(charSequence))


            //  htmlString = Html.toHtml(charSequence as Spanned?)
            htmlString = Html.toHtml(SpannableString.valueOf(charSequence))
            if (connected == 1) {
                val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    shartEntity.surah,
                    shartEntity.ayah, harfword, jawabEword
                )
                if (list != null) {
                    for (w in list) {
                        val temp: StringBuilder = getSelectedTranslation(w)
                        translationBuilder.append(temp).append(" ")
                    }
                }
                //    trstr1 = getFragmentTranslations(quranverses, sb, charSequence, false);
                shartarray.add(SpannableString.valueOf(translationBuilder.toString()))
            } else {
                val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                    shartEntity.surah,
                    shartEntity.ayah
                )
                if (wbwayah != null) {
                    for (w in wbwayah) {
                        val temp: StringBuilder = getSelectedTranslation(w)
                        if (w.wordno == harfword) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in shartSword..shartEword) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in jawbSword..jawabEword) {
                            //     sb. append("... ");
                            sbjawab.append(temp).append(" ")
                        }
                    }
                }
                translationBuilder.append(".....")
                translationBuilder.append(sbjawab)
                shartarray.add(SpannableString.valueOf(translationBuilder.toString()))
            }
            harfofverse = quranText.substring(indexstart, indexend)
            shartofverse = quranText.substring(shartindexstart, shartindexend)
            jawabofverrse = quranText.substring(jawabstart, jawabend)
            val versebuilder = StringBuilder()
            versebuilder.append(harfofverse).append(":").append(shartofverse).append(":")
                .append(jawabofverrse)
            shartarray.add(SpannableString.valueOf(harfofverse + ' ' + shartofverse + ' ' + jawabofverrse))
            arabicstring = harfofverse + ' ' + shartofverse + ' ' + jawabofverrse
            shartarray.add(SpannableString.valueOf(translationBuilder.toString()))
            val type = "full"
            val dataString =
                "${shartEntity.surah}|${shartEntity.ayah}|$indexstart|$jawabend|${shartEntity.harfwordno}|${shartEntity.jawabstartwordno}" +
                        "|${shartEntity.jawabendwordno}|$versebuilder|" +
                        "${translationBuilder.toString()}|$quranText|$type"
            result.add(dataString)
        } else if (harfAndShartOnly) {
            harfspannble = SpannableString(harfofverse)
            shartspoannable = SpannableString(shartofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            shartspoannable.setSpan(
                shartspanDark,
                0,
                shartofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble, " ", shartspoannable)
            arabicstring = charSequence.toString()
            shartarray.add(SpannableString.valueOf(charSequence))
            //    shartarray.add(trstr);
            if (shartindexstart - indexend == 1) {
                val harfnshart: List<wbwentity>? = utils.getwbwQuranbTranslation(
                    shartEntity.surah,
                    shartEntity.ayah,
                    harfword,
                    shartEword
                )
                if (harfnshart != null) {
                    for (w in harfnshart) {
                        StringBuilder()
                        val temp: StringBuilder = getSelectedTranslation(w)
                        getSelectedTranslation(w)
                        translationBuilder.append(temp).append(" ")
                    }
                }
            } else {
                val wbwayah: List<wbwentity>? = utils.getwbwQuranBySurahAyah(
                    shartEntity.surah,
                    shartEntity.ayah
                )
                if (wbwayah != null) {
                    for (w in wbwayah) {
                        val temp: StringBuilder = getSelectedTranslation(w)
                        if (w.wordno == harfword) {
                            translationBuilder.append(temp).append(" ")
                        } else if (w.wordno in shartSword..shartEword) {
                            translationBuilder.append(temp).append(" ")
                        }
                    }
                }
                translationBuilder.append(".....")
                translationBuilder.append(sbjawab)
                //   shartarray.add(SpannableString.valueOf(sb.toString()));
            }
            val type = "shartonly"
            val nullnumber = 0
            val dataString =
                "${shartEntity.surah}|${shartEntity.ayah}|$indexstart|$shartindexend|${shartEntity.harfwordno}|$nullnumber" +
                        "|${shartEntity.shartendwordno}|$arabicstring|" +
                        "${translationBuilder.toString()}|${quranText}|$type"
            result.add(dataString)


            shartarray.add(SpannableString.valueOf(translationBuilder.toString()))
        } else if (isharfb) {
            harfspannble = SpannableString(harfofverse)
            harfspannble.setSpan(
                harfshartspanDark,
                0,
                harfofverse.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val charSequence = TextUtils.concat(harfspannble)
            val trstr = getFragmentTranslations(quranText, charSequence, shartEntity)
            shartarray.add(SpannableString.valueOf(charSequence))
            shartarray.add(trstr)
            arabicstring = harfofverse + ' ' + shartofverse
            val type = "harfonly"
            val nullnumber = 0
            val dataString =
                "${shartEntity.surah}|${shartEntity.ayah}|$indexstart|$shartindexend|${shartEntity.harfwordno - 1}|$nullnumber" +
                        "|$nullnumber|$arabicstring|" +
                        "${translationBuilder.toString()}|${quranText}|$type"
            result.add(dataString)
        }


        //        val indexstart = shartEntity.indexstart
        //        val indexend = shartEntity.indexend
        //        val shartindexstart = shartEntity.shartindexstart
        //        val shartindexend = shartEntity.shartindexend
        //        val jawabstart = shartEntity.jawabshartindexstart
        //        val jawabend = shartEntity.jawabshartindexend
        //        val harfword = shartEntity.harfwordno
        //        val shartSword = shartEntity.shartstatwordno
        //        val shartEword = shartEntity.shartendwordno
        //        val jawbSword = shartEntity.jawabstartwordno
        //        val jawabEword = shartEntity.jawabendwordno
        // Format the result string
        /*     val english = shartarray.get(1)
             val dataString =
                 "${shartEntity.surah}|${shartEntity.ayah}|$indexstart|$indexend|$shartindexstart|$shartindexend|$jawabstart|$jawabend|" +
                         "$harfword|$shartSword|$shartEword|$jawbSword|$jawabEword|$english|$comment"
             result.add(dataString)*/






        return result
    }

     fun getFragmentTranslations(
        quranverses: String,
        charSequence: CharSequence,
        shartEntity: NewShartEntity,
    ): SpannableString {
        //get the string firs wordno and last wordno
        val sb = StringBuilder()
        var firstwordindex = 0
        var lastwordindex = 0
        val split = SplitQuranVerses()
        val words = split.splitSingleVerse(quranverses)
        val trim = charSequence.toString().trim { it <= ' ' }
        val strings = trim.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val length = strings.size
        val firstword = strings[0]
        val lastword = strings[length - 1]
        for (w in words) {
            val wordsAr = w.wordsAr
            if (w.wordsAr == firstword) {
                firstwordindex = w.wordno
            }
            if (wordsAr == lastword) {
                lastwordindex = w.wordno
                break
            }
        }
        val utils = Utils(QuranGrammarApplication.context)
        //if the agove is false incase of the punctutaion marks,strip the punctuation and reloop
        if (lastwordindex == 0) {
            for (ww in words) {
                var wwordsAr = ww.wordsAr
                val sala = wwordsAr!!.indexOf(ArabicLiterals.SALA)
                val qala = wwordsAr.indexOf(ArabicLiterals.QALA)
                val smalllam = wwordsAr.indexOf(ArabicLiterals.SMALLLAM)
                val smalljeem = wwordsAr.indexOf(ArabicLiterals.SMALLJEEM) //small high geem
                val b = sala != -1 || qala != -1 || smalljeem != -1 || smalllam != -1
                if (b) {
                    wwordsAr = wwordsAr.substring(0, wwordsAr.length - 1)
                }
                if (wwordsAr == firstword) {
                    firstwordindex = ww.wordno
                }
                if (wwordsAr == lastword) {
                    lastwordindex = ww.wordno
                    break
                }
            }
        }
        val list: List<wbwentity>? = utils.getwbwQuranbTranslation(
            shartEntity.surah,
            shartEntity.ayah,
            firstwordindex,
            lastwordindex
        )
        if (list != null) {
            for (tr in list) {
                getSelectedTranslation(tr)
                sb.append(tr.en).append(" ")
            }
        }
        return SpannableString(sb)
    }

    fun getSelectedTranslation(tr: wbwentity): StringBuilder {
        val sb = StringBuilder()
        when ("en") {
            "en" -> sb.append(tr.en)
            "ur" -> sb.append(tr.ur)
            "bn" -> sb.append(tr.bn)
            "id" -> sb.append(tr.id)
            else -> {

                sb.append("tr.en") // Example: Append a default value

            }
        }
        // sb.append(" ") // Uncomment if you need to add a space after the translation
        return sb
    }

    fun extractSentenceAndTranslationFromWordIndicesNewNasab(
        corpus: List<CorpusEntity>,
        wordInfo: NewNasbEntity,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()


        var startIndex = -1
        var endIndex = -1
        var currentWordIndex = 0
        val wordsInVerse =
            quranText.split("\\s+".toRegex()) // Split Arabic verse by whitespace to get individual words
        var endword = 0
        var startword = 0
        // Find the start and end index for the words based on wordfrom and wordto
        for ((i, word) in wordsInVerse.withIndex()) {

            if (wordInfo.khabarendwordno == 0) {
                endword = wordInfo.harfwordno + 1
            } else if (wordInfo.khabarendwordno == 1) {
                endword = wordInfo.harfwordno + 2
            } else {
                endword = wordInfo.khabarendwordno
            }
            if (wordInfo.harfwordno == 0) {
                startword = wordInfo.ismstartwordno

            } else {
                startword = wordInfo.harfwordno
            }
            if (currentWordIndex + 1 == startword) {
                startIndex = quranText.indexOf(word)
            }
            if (currentWordIndex + 1 == endword) {
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
                println(wordInfo.surah + wordInfo.ayah)
                println(quranText)
            }


            // Extract translation using wordfrom and wordnoto
            val translationBuilder = StringBuilder()
            val arabictranslationBuilder = StringBuilder()
            for (entry in corpus) {
                if (entry.surah == 2 && entry.ayah == 13) {
                    println("check")
                }
                if (entry.wordno in startword..endword) {
                    translationBuilder.append(entry.en).append(" ")
                    arabictranslationBuilder.append(entry.araone)
                        .append(entry.aratwo)
                        .append(entry.arathree)
                        .append(entry.arafour)
                        .append(entry.arafive).append(" ")
                }
            }
            val extractedTranslation = translationBuilder.toString().trim()
            val arabictrimmed = arabictranslationBuilder.toString().trim()
            var type = ""
            try {
                if (corpus[startword - 1].tagone == "ACC" && corpus[startword].tagone == "PRON") {
                    type = "nasabone"
                } else {
                    type = "nasabtwo"
                }
            } catch (e: IndexOutOfBoundsException) {
                type = "unknown"
            }

            // Format the result string
            if (extractedSentence.isEmpty()) {
                val dataString =
                    "${wordInfo.surah}|${wordInfo.ayah}|$startword|$endword|$startIndex|$endIndex|$arabictrimmed|$type|$extractedTranslation|$quranText"
                result.add(dataString)
            } else {
                val dataString =
                    "${wordInfo.surah}|${wordInfo.ayah}|$startword|$endword|$startIndex|$endIndex|$extractedSentence|$type|$extractedTranslation|$quranText"
                result.add(dataString)
            }

        } else {
            val trans = ""
            val dataString =
                "${wordInfo.surah}|${wordInfo.ayah}|$startword|$endword|$startIndex|$endIndex|$extractedSentence|$trans|$quranText"
            // Handle the case when startIndex or endIndex is not found
            result.add(dataString)
        }


        return result
    }
    fun extractSentenceAndTranslationFromWordNumbers(
        corpus: List<CorpusEntity>,
        wordInfo: NegationEnt,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()
        var wordto = 0
        var translationBuilder = StringBuilder()
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



            // Format the result string
            val dataString =
                "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|${wordInfo.wordto}|$startIndex|$endIndex|${wordInfo.arabictext}|${wordInfo.englishtext}" +
                        "|${wordInfo.verse}|${wordInfo.translation}|${wordInfo.type}|$quranText"
            result.add(dataString)
        } else {
            val extractedTranslation = translationBuilder.toString().trim()
            if(wordInfo.type=="future"){
                println("check")
            }
            val type="check"
            // Handle the case when startIndex or endIndex is not found
            val dataString =
                "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|${wordInfo.wordto}|$startIndex|$endIndex|${wordInfo.arabictext}|${wordInfo.englishtext}" +
                        "|${wordInfo.verse}|${wordInfo.translation}|$type|$quranText"
            result.add(dataString)
        }


        return result
    }
    fun extractSentenceAndTranslationFromWordIndices(
        corpus: List<CorpusEntity>,
        wordInfo: NegationEnt,
        quranText: String

    ): List<String> {
        val result = mutableListOf<String>()
        var wordto = 0
        var translationBuilder = StringBuilder()
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

                if (wordInfo.type.equals("past")) {
                    wordto = wordInfo.wordto + 1
                } else {
                    wordto = wordInfo.wordto
                }
                if (currentWordIndex + 1 == wordto) {
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
                translationBuilder = StringBuilder()
                for (entry in corpus) {
                    if (wordInfo.type.equals("past")) {
                        wordto = wordInfo.wordto + 1
                    } else {
                        wordto = wordInfo.wordto
                    }
                    if (entry.wordno in wordInfo.wordfrom..wordto) {
                        translationBuilder.append("${entry.en} ").append(" ")
                    }
                }
                val extractedTranslation = translationBuilder.toString().trim()
                val type = wordInfo.type
                // Format the result string
                val dataString =
                    "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|$wordto|$startIndex|$endIndex|$extractedSentence|$extractedTranslation|${wordInfo.arabictext}|${wordInfo.englishtext}|$type|$quranText"
                result.add(dataString)
            } else {
                val extractedTranslation = translationBuilder.toString().trim()
                if(wordInfo.type=="future"){
                    println("check")
                }
                // Handle the case when startIndex or endIndex is not found
                val dataString =
                    "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|$wordto|$startIndex|$endIndex|$extractedSentence|$extractedTranslation|${wordInfo.arabictext}|${wordInfo.englishtext}|${wordInfo.type}|$quranText"
                result.add(dataString)
            }


        return result
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
            if (entry.surah == 11 && entry.ayah == 81) {
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


    fun extractSentenceAndTranslationFromSavedWordIndexes(
        corpus: List<CorpusEntity>,
        wordInfo: NegationEnt,
        quranText: String
    ): List<String> {
        val result = mutableListOf<String>()

        var extractedSentence = ""
        // Find the start and end indices based on wordfrom and wordto
        val startIndex = corpus.indexOfFirst { it.wordno == wordInfo.wordfrom }
        val endIndex = corpus.indexOfLast { it.wordno == wordInfo.wordto }

        if (startIndex != -1 && endIndex != -1) {
            // Extract the Arabic sentence using the found indices
            val sentenceBuilder = StringBuilder()
            for (i in startIndex..endIndex) {
                val word =
                    "${corpus[i].araone}${corpus[i].aratwo}${corpus[i].arathree}${corpus[i].arafour}${corpus[i].arafive}".trim()
                sentenceBuilder.append(word).append(" ")
            }
            extractedSentence = sentenceBuilder.toString().trim()

            // ... (rest of the code remains the same)
        } else {
            // Handle the case when wordfrom or wordto is not found
            result.add("Error: Couldn't find words ${wordInfo.wordfrom} to ${wordInfo.wordto} in the corpus")
        }

        return result
    }

    fun extractSentenceAndTranslationFromSavedIndexes(
        corpus: List<CorpusEntity>,
        wordInfo: NegationEnt,
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
                println(wordInfo.surahid + wordInfo.ayahid)
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
                "${wordInfo.surahid}|${wordInfo.ayahid}|${wordInfo.wordfrom}|${wordInfo.wordto}|$start|$end|$extractedSentence"
            result.add(dataString)
        } else {
            // Handle the case when startIndex or endIndex is not found
            result.add("Error: Couldn't find indices for Surah ${wordInfo.surahid}, Ayah ${wordInfo.ayahid}, Words ${wordInfo.wordfrom} to ${wordInfo.wordfrom}")

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
        if(info.type!="nountype") {
            val extractedSentence = if (info.startindex >= 0 && info.endindex <= quranText.length && info.startindex <= info.endindex) {
                quranText.substring(info.startindex, min(info.endindex, quranText.length)).trim() // Use min to ensure endindex is within bounds
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
            val types = info.type

            // Format the result string
            val dataString =
                "${info.surahid}|${info.ayahid}|${info.wordfrom}|${info.wordto}|${info.startindex}|${info.endindex}|$extractedSentence|$extractedTranslation|$types"
            result.add(dataString)

        }
        return result
    }




     fun addQuranVerseTranslationErab( wordInfo: NegationEnt, qurantext: String, translation: String, arIrabTwo: String): List<String> {
        val result = mutableListOf<String>()
        val surah=wordInfo.surahid
        val ayah=wordInfo.ayahid
        val wordfrom=wordInfo.wordfrom
        val wordto=wordInfo.wordto
        val start=wordInfo.startindex
        val end=wordInfo.endindex
        val arabic=wordInfo.arabictext
        val english=wordInfo.englishtext
        val verse=qurantext
        val trans=translation
        val erab=arIrabTwo
        val types=wordInfo.type

        val replacedText = erab.replace("\n", "")
        val dataString = "$surah|$ayah|$wordfrom|$wordto|$start|$end|$arabic|$english|$verse|$trans|$types"

        /*      val dataString =
                  "$surah|$ayah|$wordfrom|$wordto|$start|$end|$arabic|$english|$verse|$trans|$erab|$types"*/
        result.add(dataString)
        return result
    }

     fun shart(accusativeSentencesCollection: MutableList<Map<String, Any>>): Pair<ArrayList<List<String>>, ArrayList<String>> {
        val setenceCollection = ArrayList<List<String>>()
        val Sentences = ArrayList<String>()

        for (sentenceMap in accusativeSentencesCollection) {
            val type = sentenceMap["type"] as String
            val accWordNo = sentenceMap["accWordNo"] as Int

            val sequence = if (type == "Scenario 1") {
                sentenceMap["predicateSequence"] as List<CorpusRow>
            } else {
                sentenceMap["predicateSequence"] as List<CorpusRow>
            }

            val shartsequence = if (type == "Scenario 1") {
                sentenceMap["predicateSequence"] as List<ShartCorpusRow>
            } else {
                sentenceMap["predicateSequence"] as List<ShartCorpusRow>
            }
            var delimitedString = ""
            //  delimitedString = shartInCollection(shartsequence, sentenceMap, delimitedString, type)


            delimitedString = sequenceCollection(sequence, type, sentenceMap, delimitedString)

            // Create the pipe-delimited string


            // Add the delimited string as a list of strings to Sentences
            Sentences.add(delimitedString)
            setenceCollection.add(Sentences)
        }
        return Pair(setenceCollection, Sentences)
    }

     fun shartInCollection(
         shartsequence: List<ShartCorpusRow>,
         sentenceMap: Map<String, Any>,
         delimitedString: String,
         type: String
    ): String {
        var delimitedString1 = delimitedString
        if (shartsequence.isNotEmpty()) {

            val condword =
                sentenceMap["accWordNo"] as Int // Accessing the value for key "accWordNo"
            val firstVerbWordno =
                sentenceMap["firstVerb"] as Int // Accessing the value for key "emphaticVerbWordNo"
            val secondVerbWordno = sentenceMap["secondVerb"] as Int
            val endWord = sentenceMap["endword"] as Int
            val surah = shartsequence[0].surah
            val ayah = shartsequence[0].ayah
            val firstWordNo = shartsequence[0].wordno
            val wordNos = shartsequence.map { it.wordno }.joinToString(",")
            val arabicText = shartsequence.joinToString(" ") { it.arabicText }
            val englishText = shartsequence.joinToString(" ") { it.englishText }
            val quranVerse = shartsequence[0].quranText
            val translation = shartsequence[0].translation
            val lastword = shartsequence.last().wordno

            delimitedString1 =
                "$type|$surah|$ayah|$wordNos|$condword|$firstVerbWordno|$secondVerbWordno|$endWord|$arabicText|$englishText|$quranVerse|$translation"
        }
        return delimitedString1
    }

     fun sequenceCollection(
         sequence: List<CorpusRow>,
         type: String,
         sentenceMap: Map<String, Any>,
         delimitedString: String
    ): String {
        var delimitedString1 = delimitedString
        if (sequence.isNotEmpty()) {
            if (type == "ScenarioRSLT") {
                val condword =
                    sentenceMap["accWordNo"] as Int // Accessing the value for key "accWordNo"
                val firstVerbWordno =
                    sentenceMap["verbWordNo"] as Int // Accessing the value for key "emphaticVerbWordNo"
                val resultWordNo = sentenceMap["resultWordNo"] as Int
                val endWord = sentenceMap["endword"] as Int
                val surah = sequence[0].surah
                val ayah = sequence[0].ayah
                val firstWordNo = sequence[0].wordno
                val wordNos = sequence.map { it.wordno }.joinToString(",")
                val arabicText = sequence.joinToString(" ") { it.arabicText }
                val englishText = sequence.joinToString(" ") { it.englishText }
                val lastword = sequence.last().wordno
                delimitedString1 =
                    "$type|$surah|$ayah|$wordNos|$condword|$firstVerbWordno|$resultWordNo|$endWord|$arabicText|$englishText"
            } else
                if (type == "Scenario 1") {
                    val condword =
                        sentenceMap["accWordNo"] as Int // Accessing the value for key "accWordNo"
                    val emphaticVerbWordNo =
                        sentenceMap["emphaticVerbWordNo"] as Int // Accessing the value for key "emphaticVerbWordNo"
                    val endword = sentenceMap["endword"] as Int
                    val surah = sequence[0].surah
                    val ayah = sequence[0].ayah
                    val firstWordNo = sequence[0].wordno
                    val wordNos = sequence.map { it.wordno }.joinToString(",")
                    val arabicText = sequence.joinToString(" ") { it.arabicText }
                    val englishText = sequence.joinToString(" ") { it.englishText }
                    val lastword = sequence.last().wordno
                    delimitedString1 =
                        "$type|$surah|$ayah|$wordNos|$condword|$emphaticVerbWordNo|$endword|$arabicText|$englishText"
                } else {
                    val condword =
                        sentenceMap["accWordNo"] as Int // Accessing the value for key "accWordNo"
                    val firstVerbWordno =
                        sentenceMap["firstPerfectVerb"] as Int // Accessing the value for key "emphaticVerbWordNo"
                    val secondVerbWordno = sentenceMap["secondPerfectVerb"] as Int
                    val endWord = sentenceMap["endword"] as Int
                    val surah = sequence[0].surah
                    val ayah = sequence[0].ayah
                    val firstWordNo = sequence[0].wordno
                    val wordNos = sequence.map { it.wordno }.joinToString(",")
                    val arabicText = sequence.joinToString(" ") { it.arabicText }
                    val englishText = sequence.joinToString(" ") { it.englishText }

                    val lastword = sequence.last().wordno
                    delimitedString1 =
                        "$type|$surah|$ayah|$wordNos|$condword|$firstVerbWordno|$secondVerbWordno|$endWord|$arabicText|$englishText"
                }
        }
        return delimitedString1
    }

     fun nasab(accusativeSentencesCollection: MutableList<Map<String, Any>>): Pair<ArrayList<List<String>>, ArrayList<String>> {
        val setenceCollection = ArrayList<List<String>>()
        val Sentences = ArrayList<String>()
        for (sentenceMap in accusativeSentencesCollection) {
            val type = sentenceMap["type"] as String
            val accWordNo = sentenceMap["accWordNo"] as Int
            val verse=sentenceMap["quranverse"] as String
            val translation=sentenceMap["translation"] as String

            val sequence = if (type == "Scenario 1") {
                sentenceMap["predicateSequence"] as List<CorpusRow>
            } else {
                sentenceMap["accSequence"] as List<CorpusRow>
            }

            val surah = sequence[0].surah
            val ayah = sequence[0].ayah
            val firstWordNo = sequence[0].wordno
            val wordNos = sequence.map { it.wordno }.joinToString(",")
            val arabicText = sequence.joinToString(" ") { it.arabicText }
            val englishText = sequence.joinToString(" ") { it.englishText }
            val lastword = sequence.last().wordno


            // Create the pipe-delimited string
            val delimitedString =
                "$type|$surah|$ayah|$wordNos|$firstWordNo|$lastword|$arabicText|$englishText|$verse|$translation"

            // Add the delimited string as a list of strings to Sentences
            Sentences.add(delimitedString)
            setenceCollection.add(Sentences)
        }
        return Pair(setenceCollection, Sentences)
    }








    // Function to remove Arabic vowels and diacritics, leaving only consonants

    // Function to check for broken plural pattern by replacing consonants

    fun extractAccusativeSentences(corpusData: List<CorpusEntity>): List<Map<String, Any>> {
        val accusativeSentences = mutableListOf<Map<String, Any>>()

        for (i in corpusData.indices) {
            val row = corpusData[i]
            val tags = listOf(
                row.tagone,
                row.tagtwo,
                row.tagthree,
                row.tagfour,
                row.tagfive
            ) // Create tags list
            val detailstag = listOf(
                row.detailsone,
                row.detailstwo,
                row.detailsthree,
                row.detailsfour,
                row.detailsfive
            )

            if ("ACC" in tags && "PRON" in tags) {
                val accWordNo = row.wordno
                //     val predicateSequence = findSequenceEndingInNom(corpusData, startIndex = i)
                val predicateSequence = findSequenceEndingInNomold(corpusData, startIndex = i)
                if (predicateSequence != null) {
                    accusativeSentences.add(
                        mapOf(
                            "type" to "Scenario 1",
                            "accWordNo" to accWordNo,
                            "predicateSequence" to predicateSequence
                        )
                    )
                }
            } else if ("ACC" in tags) {
                val accWordNo = row.wordno
                //   val accSequence = findAccNounSequenceEndingInNom(corpusData, startIndex = i)
                // val accSequence = findAccNounSequenceEndingInNomoldnew(corpusData, startIndex = i)
                if (row.surah == 11 && row.ayah == 90) {
                    println("check")
                }
                val accSequence = findAccNounSequenceEndingInNomLatest(corpusData, startIndex = i)
                if (accSequence != null) {
                    accusativeSentences.add(
                        mapOf(
                            "type" to "Scenario 2",
                            "accWordNo" to accWordNo,
                            "accSequence" to accSequence
                        )
                    )
                }/* else {
                    // Capture surah and ayah if no sequence found
                    accusativeSentences.add(
                        mapOf(
                            "type" to "No sequence found for Scenario 2",
                            "surah" to row.surah,
                            "ayah" to row.ayah
                        )
                    )
                }*/
            }

        }

        return accusativeSentences
    }

    fun extractConditionalSentences(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]

            // Check if any tag is "COND" and the corresponding Arabic text is "لَوْ", "أَوَلَوْ", or "وَلَوْ"
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWords =
                listOf(row.araone + row.aratwo + row.arathree + row.arafour + row.arafive)

            val isConditional = tags.zip(arabicWords).any { (tag, word) ->
                tag == "COND" && (word == "لَوْ" || word == "أَوَلَوْ" || word == "وَلَوْ")
            }

            if (isConditional) {
                val sequence = mutableListOf<CorpusRow>()
                val accWordNo = row.wordno
                var foundEmphaticVerb = false

                // Start capturing the sequence from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentArabicWords = listOf(
                        currentRow.araone, currentRow.aratwo, currentRow.arathree,
                        currentRow.arafour, currentRow.arafive
                    )

                    // Create CorpusRow for the current entry
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = listOf(
                            currentRow.detailsone, currentRow.detailstwo,
                            currentRow.detailsthree, currentRow.detailsfour,
                            currentRow.detailsfive
                        ),
                        arabicText = currentArabicWords.joinToString(""),
                        englishText = currentRow.en
                    )

                    sequence.add(corpusRow)

                    // Check for an emphatic verb (EMPH followed by a verb starting with "V")
                    val emphIndex = currentTags.indexOf("EMPH")
                    foundEmphaticVerb = if (emphIndex != -1 && emphIndex + 1 < currentTags.size) {
                        currentTags[emphIndex + 1] == "V" && currentArabicWords[emphIndex + 1]!!.startsWith(
                            "V"
                        )
                    } else {
                        false
                    }

                    // Capture only the first emphatic verb sequence, if found
                    if (foundEmphaticVerb) {
                        break
                    }
                }

                // Set the end word number based on where the sequence stopped
                val endWordNo = sequence.lastOrNull()?.wordno ?: accWordNo

                // Add the captured data to the conditional sentences list
                conditionalSentences.add(
                    mapOf(
                        "type" to "Scenario 1",
                        "accWordNo" to accWordNo,
                        "emphaticVerbWordNo" to endWordNo,
                        "predicateSequence" to sequence
                    )
                )
                /*      conditionalSentences.add(
                          mapOf(
                              "type" to "Scenario 1",
                              "surah" to row.surah,
                              "ayah" to row.ayah,
                              "accWordNo" to accWordNo,
                              "emphaticVerbWordNo" to endWordNo,
                              "arabicText" to sequence.joinToString(" ") { it.arabicText },
                              "englishTranslation" to sequence.joinToString(" ") { it.englishText }
                          )
                      )*/
            }
        }

        return conditionalSentences
    }

    fun extractConditionalSentencesv2(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]

            // Check if any tag is "COND" and the corresponding Arabic text is "لَوْ", "أَوَلَوْ", or "وَلَوْ"
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWords =
                listOf(row.araone + row.aratwo + row.arathree + row.arafour + row.arafive)

            val isConditional = tags.zip(arabicWords).any { (tag, word) ->
                tag == "COND" && (word == "لَوْ" || word == "أَوَلَوْ" || word == "وَلَوْ")
            }

            if (isConditional) {
                val sequence = mutableListOf<CorpusRow>()
                val accWordNo = row.wordno
                var foundEmphaticVerb = false

                // Start capturing the sequence from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentArabicWords = listOf(
                        currentRow.araone, currentRow.aratwo, currentRow.arathree,
                        currentRow.arafour, currentRow.arafive
                    )

                    // Create CorpusRow for the current entry
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = listOf(
                            currentRow.detailsone, currentRow.detailstwo,
                            currentRow.detailsthree, currentRow.detailsfour,
                            currentRow.detailsfive
                        ),
                        arabicText = currentArabicWords.joinToString(""),
                        englishText = currentRow.en
                    )

                    sequence.add(corpusRow)

                    // Check for an emphatic verb (EMPH followed by a verb starting with "V")
                    val emphIndex = currentTags.indexOf("EMPH")
                    foundEmphaticVerb = if (emphIndex != -1 && emphIndex + 1 < currentTags.size) {
                        currentTags[emphIndex + 1] == "V" && currentArabicWords[emphIndex + 1]!!.startsWith(
                            "V"
                        )
                    } else {
                        false
                    }

                    if (foundEmphaticVerb || sequence.size >= 5) {
                        //      break
                    }
                }

                // Capture sequence up to the emphatic verb if found, otherwise capture the entire sentence
                val endWordNo =
                    if (foundEmphaticVerb) sequence.last().wordno else sequence.last().wordno

                // Add the captured data to the conditional sentences list
                conditionalSentences.add(
                    mapOf(
                        "type" to "Scenario 1",
                        "accWordNo" to accWordNo,
                        "emphaticVerbWordNo" to endWordNo,
                        "predicateSequence" to sequence
                    )
                )
            }
        }

        return conditionalSentences
    }

    fun extractConditionalSentencesIZAWITHVERB(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("T") == true }
            val isWord =
                arabicWord.contains("إِذَا") || arabicWord.contains("فَإِذَا") || arabicWord.contains(
                    "وَإِذَا"
                )

            if (isCondi && isWord) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno
                var firstPerfectVerb: Int? = null
                var secondPerfectVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0

                // Add ":" after the conditional word
                val initialCorpusRow = CorpusRow(
                    surah = row.surah,
                    ayah = row.ayah,
                    wordno = row.wordno,
                    tags = tags,
                    details = listOf(
                        row.detailsone,
                        row.detailstwo,
                        row.detailsthree,
                        row.detailsfour,
                        row.detailsfive
                    ),
                    arabicText = arabicWord + ":",  // Adding delimiter after the conditional word
                    englishText = row.en
                )
                sequence.add(initialCorpusRow)

                // Start capturing from the conditional word
                for (j in i + 1 until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )
                    val currentArabicWords =
                        currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive

                    // Check for perfect verbs in details
                    val isPerfectVerb = currentDetails.any { it?.contains("PERF") == true }

                    // Add ":" before the second perfect verb
                    val modifiedArabicText = when {
                        firstPerfectVerb == null && isPerfectVerb -> {
                            firstPerfectVerb = currentRow.wordno
                            currentArabicWords // No delimiter for the first perfect verb
                        }

                        secondPerfectVerb == null && isPerfectVerb -> {
                            secondPerfectVerb = currentRow.wordno
                            ":" + currentArabicWords // Add ":" before the second perfect verb
                        }

                        else -> currentArabicWords
                    }

                    // Create CorpusRow with modified arabicText
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = modifiedArabicText,
                        englishText = currentRow.en
                    )
                    sequence.add(corpusRow)

                    // Count words after the second perfect verb
                    if (secondPerfectVerb != null) {
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition for capturing up to 4 words after the second perfect verb
                    if (secondPerfectVerb != null && (wordsCapturedAfterSecondVerb == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 2",
                                "accWordNo" to condWord,
                                "firstPerfectVerb" to (firstPerfectVerb ?: -1),
                                "secondPerfectVerb" to (secondPerfectVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesIZAWITHVERBprevious(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("T") == true }
            val isWord =
                arabicWord.contains("إِذَا") || arabicWord.contains("فَإِذَا") || arabicWord.contains(
                    "وَإِذَا"
                )
            if (isCondi && isWord) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno
                var firstPerfectVerb: Int? = null
                var secondPerfectVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0

                // Start capturing from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = listOf(
                            currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                            currentRow.tagfour, currentRow.tagfive
                        ),
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en
                    )
                    sequence.add(corpusRow)

                    // Check for perfect verbs in details
                    if (currentDetails.any { it?.contains("PERF") == true }) {

                        when {
                            firstPerfectVerb == null -> firstPerfectVerb = currentRow.wordno
                            secondPerfectVerb == null -> secondPerfectVerb = currentRow.wordno
                        }
                    } else if (secondPerfectVerb != null) {
                        // Increment the words captured after second perfect verb
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition for capturing up to 4 words after second perfect verb
                    if (secondPerfectVerb != null && (wordsCapturedAfterSecondVerb == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 2",
                                "accWordNo" to condWord,
                                "firstPerfectVerb" to (firstPerfectVerb ?: -1),
                                "secondPerfectVerb" to (secondPerfectVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }
    fun extractConditionalSentencesWhenWithMANLAMMARSLT(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("مَن") || arabicWord.contains("مَنْ") || arabicWord.contains(
                    "أَمَّآ")  ||

                        arabicWord.contains("أَمَّا") || arabicWord.contains("وَأَمَّا") || arabicWord.contains(
                    "وَمَن" )||

                        arabicWord.contains("وَمَن") || arabicWord.contains("فَأَمَّا") || arabicWord.contains(
                    "فَأَمَّآ" ) ||

                        arabicWord.contains("وَمَن") || arabicWord.contains("وَأَمَّآ") || arabicWord.contains(
                    "فَأَمَّآ" )



            var verbWordNo = -1
            var resultWordNo = -1
            var wordsCapturedAfterResult = 0

            if (isCondi && isWord) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno
                var verbFound = false
                var resultFound = false

                // Add ":" after the conditional word
                val initialCorpusRow = CorpusRow(
                    surah = row.surah,
                    ayah = row.ayah,
                    wordno = row.wordno,
                    tags = tags,
                    details = listOf(
                        row.detailsone,
                        row.detailstwo,
                        row.detailsthree,
                        row.detailsfour,
                        row.detailsfive
                    ),
                    arabicText = arabicWord + ":",  // Adding delimiter after the conditional word
                    englishText = row.en
                )
                sequence.add(initialCorpusRow)

                // Start capturing from the conditional word
                for (j in i + 1 until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )
                    val currentArabicWords =
                        currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive


                    // After finding a verb, look for the RSLT tag and add ":" before it
                    if (   currentTags.any { it?.contains("RSLT") == true }) {
                        resultFound = true
                        resultWordNo = currentRow.wordno
                    } else if(currentArabicWords.contains("ف")){
                        resultFound = true
                        resultWordNo = currentRow.wordno
                    }

                    // Add ":" before the RSLT word
                    val modifiedArabicText = if (resultFound && resultWordNo == currentRow.wordno) {
                        ":" + currentArabicWords // Adding ":" before RSLT word
                    } else {
                        currentArabicWords
                    }

                    // Add the modified current row to the sequence
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = modifiedArabicText,
                        englishText = currentRow.en
                    )
                    sequence.add(corpusRow)

                    // Start counting words after the RSLT tag
                    if (resultFound) {
                        wordsCapturedAfterResult++
                    }

                    // Exit condition for capturing up to 4 words after the RSLT tag
                    if (resultFound && (wordsCapturedAfterResult == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "ScenarioRSLT",
                                "accWordNo" to condWord,
                                "verbWordNo" to verbWordNo,
                                "resultWordNo" to resultWordNo,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    } else if (j == data.size - 1) {
                        conditionalSentences.add(
                            mapOf(
                                "type" to "ScenarioRSLT",
                                "accWordNo" to condWord,
                                "verbWordNo" to verbWordNo,
                                "resultWordNo" to resultWordNo,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences
                    }
                }
            }
        }
        return conditionalSentences
    }
    fun extractConditionalSentencesWhenWithIZARSLT(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("T") == true }
            val isWord =
                arabicWord.contains("إِذَا") || arabicWord.contains("فَإِذَا") || arabicWord.contains(
                    "وَإِذَا"
                )
            var verbWordNo = -1
            var resultWordNo = -1
            var wordsCapturedAfterResult = 0

            if (isCondi && isWord) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno
                var verbFound = false
                var resultFound = false

                // Add ":" after the conditional word
                val initialCorpusRow = CorpusRow(
                    surah = row.surah,
                    ayah = row.ayah,
                    wordno = row.wordno,
                    tags = tags,
                    details = listOf(
                        row.detailsone,
                        row.detailstwo,
                        row.detailsthree,
                        row.detailsfour,
                        row.detailsfive
                    ),
                    arabicText = arabicWord + ":",  // Adding delimiter after the conditional word
                    englishText = row.en
                )
                sequence.add(initialCorpusRow)

                // Start capturing from the conditional word
                for (j in i + 1 until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )
                    val currentArabicWords =
                        currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive

                    // Look for the verb first
                    if (!verbFound && currentTags.any { it?.contains("V") == true }) {
                        verbFound = true
                        verbWordNo = currentRow.wordno
                    }
                    // After finding a verb, look for the RSLT tag and add ":" before it
                    else if (verbFound && !resultFound && currentTags.any { it?.contains("RSLT") == true }) {
                        resultFound = true
                        resultWordNo = currentRow.wordno
                    }

                    // Add ":" before the RSLT word
                    val modifiedArabicText = if (resultFound && resultWordNo == currentRow.wordno) {
                        ":" + currentArabicWords // Adding ":" before RSLT word
                    } else {
                        currentArabicWords
                    }

                    // Add the modified current row to the sequence
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = modifiedArabicText,
                        englishText = currentRow.en
                    )
                    sequence.add(corpusRow)

                    // Start counting words after the RSLT tag
                    if (resultFound) {
                        wordsCapturedAfterResult++
                    }

                    // Exit condition for capturing up to 4 words after the RSLT tag
                    if (resultFound && (wordsCapturedAfterResult == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "ScenarioRSLT",
                                "accWordNo" to condWord,
                                "verbWordNo" to verbWordNo,
                                "resultWordNo" to resultWordNo,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    } else if (j == data.size - 1) {
                        conditionalSentences.add(
                            mapOf(
                                "type" to "ScenarioRSLT",
                                "accWordNo" to condWord,
                                "verbWordNo" to verbWordNo,
                                "resultWordNo" to resultWordNo,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesWhenWithIZARSLTprevious(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("T") == true }
            val isWord =
                arabicWord.contains("إِذَا") || arabicWord.contains("فَإِذَا") || arabicWord.contains(
                    "وَإِذَا"
                )
            var verbWordNo = -1
            var resultWordNo = -1
            var endword = -1
            var wordsCapturedAfterSecondVerb = 0
            if (isCondi && isWord) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno
                var verbFound = false
                var resultFound = false
                var wordsCapturedAfterResult = 0

                // Start capturing from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en
                    )
                    sequence.add(corpusRow)

                    // Look for the verb first
                    if (!verbFound && currentTags.any { it?.contains("V") == true }) {
                        verbFound = true
                        verbWordNo = currentRow.wordno
                    }
                    // After finding a verb, look for the RSLT tag
                    else if (verbFound && !resultFound && currentTags.any { it?.contains("RSLT") == true }) {
                        resultFound = true
                        resultWordNo = currentRow.wordno
                    }
                    // Start counting words after RSLT tag
                    else if (resultFound) {
                        wordsCapturedAfterResult++
                    }

                    // Exit condition for capturing up to 4 words after the RSLT tag
                    if (resultFound && (wordsCapturedAfterResult == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "ScenarioRSLT",
                                "accWordNo" to condWord,
                                "verbWordNo" to verbWordNo,
                                "resultWordNo" to resultWordNo,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    } else if (j == data.size - 1) {
                        conditionalSentences.add(
                            mapOf(
                                "type" to "ScenarioRSLT",
                                "accWordNo" to condWord,
                                "verbWordNo" to verbWordNo,
                                "resultWordNo" to resultWordNo,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences
                    }
                }
            }
        }
        return conditionalSentences
    }


    fun extractConditionalSentencesWhenWithVerbsMAMINJUSSIVE(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive
            if (row.surah == 24) {
                println("check")
            }
            if (row.ayah == 74) {
                println("check")
            }
            // Check if this row is a conditional word
            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord = (arabicWord.contains("وَمَنْ") || arabicWord.contains("فَمَنْ") ||
                    arabicWord.contains("لَمَنِ") || arabicWord.contains("وَمَا") || arabicWord.contains(
                "فَأَيْنَمَا"
            ) ||
                    arabicWord.contains("فَمَا") || arabicWord.contains("مَآ") || arabicWord.contains(
                "مَّآ"
            ) ||
                    arabicWord.contains("أَمَّآ") || arabicWord.contains("مَن") || arabicWord.contains(
                "فَأَمَّآ"
            ) ||
                    arabicWord.contains("وَأَمَّآ") || arabicWord.contains("مَهْمَا") || arabicWord.contains(
                "فَأَمَّا"
            ) || arabicWord.contains("وَأَمَّا"))
            if (isCondi) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstVerb: Int? = null
                var secondVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0
                var scenarioType = ""

                // Add initial row with delimiter ":" after the conditional word
                val initialShartCorpusRow = ShartCorpusRow(
                    surah = row.surah,
                    ayah = row.ayah,
                    wordno = row.wordno,
                    tags = tags,
                    details = listOf(
                        row.detailsone,
                        row.detailstwo,
                        row.detailsthree,
                        row.detailsfour,
                        row.detailsfive
                    ),
                    arabicText = arabicWord + ":", // Add ":" after conditional word
                    englishText = row.en,
                    quranText = qurantext,
                    translation = translation
                )
                sequence.add(initialShartCorpusRow)

                for (j in i + 1 until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )
                    val currentArabicText =
                        currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive

                    var modifiedArabicText = currentArabicText

                    // Determine the scenario type and find the first and second verbs
                    if (scenarioType.isEmpty()) {
                        if (currentDetails.any { it?.contains("MOOD:JUS") == true }) {
                            if (firstVerb == null) {
                                firstVerb = currentRow.wordno
                                scenarioType = "Scenario injussive"
                            } else if (secondVerb == null) {
                                secondVerb = currentRow.wordno
                                modifiedArabicText =
                                    ":" + modifiedArabicText // Add ":" before second verb
                            }
                        } else if (currentDetails.any { it?.contains("RSLT") == true } && scenarioType.isEmpty()) {
                            scenarioType = "Scenario result"
                            secondVerb = currentRow.wordno
                        }
                    } else {
                        if (
                            (scenarioType == "Scenario injussive" && currentDetails.any {
                                it?.contains(
                                    "MOOD:JUS"
                                ) == true
                            } && secondVerb == null)) {
                            secondVerb = currentRow.wordno
                            modifiedArabicText =
                                ":" + modifiedArabicText // Add ":" before second verb
                        }
                    }

                    // Add the modified current row to the sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = modifiedArabicText,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Increment the words captured after the second verb
                    if (secondVerb != null || scenarioType == "Scenario result") {
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition based on the scenario
                    if ((secondVerb != null && wordsCapturedAfterSecondVerb == 4) ||
                        (scenarioType == "Scenario result" && wordsCapturedAfterSecondVerb == 4) ||
                        j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to scenarioType,
                                "accWordNo" to condWord,
                                "firstVerb" to (firstVerb ?: -1),
                                "secondVerb" to (secondVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }


    fun extractConditionalSentencesWhenWithVerbsIN(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word
            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord = arabicWord.contains("إِن") || arabicWord.contains("وَإِن") ||
                    arabicWord.contains("فَإِن") || arabicWord.contains("أَفَإِي۟ن") || arabicWord.contains(
                "وَلَئِنِ"
            )

            if (isCondi && isWord) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstVerb: Int? = null
                var secondVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0
                var scenarioType = ""

                // Add initial row with delimiter ":" after the conditional word
                val initialShartCorpusRow = ShartCorpusRow(
                    surah = row.surah,
                    ayah = row.ayah,
                    wordno = row.wordno,
                    tags = tags,
                    details = listOf(
                        row.detailsone,
                        row.detailstwo,
                        row.detailsthree,
                        row.detailsfour,
                        row.detailsfive
                    ),
                    arabicText = arabicWord + ":", // Add ":" after conditional word
                    englishText = row.en,
                    quranText = qurantext,
                    translation = translation
                )
                sequence.add(initialShartCorpusRow)

                for (j in i + 1 until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )
                    val currentArabicText =
                        currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive

                    var modifiedArabicText = currentArabicText

                    // Determine the scenario type and find the first and second verbs
                    if (scenarioType.isEmpty()) {
                        if (currentDetails.any { it?.contains("PERF") == true }) {
                            if (firstVerb == null) {
                                firstVerb = currentRow.wordno
                                scenarioType = "Scenario inmadhi"
                            } else if (secondVerb == null) {
                                secondVerb = currentRow.wordno
                                modifiedArabicText =
                                    ":" + modifiedArabicText // Add ":" before second verb
                            }
                        } else if (currentDetails.any { it?.contains("MOOD:JUS") == true }) {
                            if (firstVerb == null) {
                                firstVerb = currentRow.wordno
                                scenarioType = "Scenario injussive"
                            } else if (secondVerb == null) {
                                secondVerb = currentRow.wordno
                                modifiedArabicText =
                                    ":" + modifiedArabicText // Add ":" before second verb
                            }
                        } else if (currentDetails.any { it?.contains("RSLT") == true } && scenarioType.isEmpty()) {
                            scenarioType = "Scenario result"
                            secondVerb = currentRow.wordno
                        }
                    } else {
                        if ((scenarioType == "Scenario inmadhi" && currentDetails.any {
                                it?.contains(
                                    "PERF"
                                ) == true
                            } && secondVerb == null) ||
                            (scenarioType == "Scenario injussive" && currentDetails.any {
                                it?.contains(
                                    "MOOD:JUS"
                                ) == true
                            } && secondVerb == null)) {
                            secondVerb = currentRow.wordno
                            modifiedArabicText =
                                ":" + modifiedArabicText // Add ":" before second verb
                        }
                    }

                    // Add the modified current row to the sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = modifiedArabicText,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Increment the words captured after the second verb
                    if (secondVerb != null || scenarioType == "Scenario result") {
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition based on the scenario
                    if ((secondVerb != null && wordsCapturedAfterSecondVerb == 4) ||
                        (scenarioType == "Scenario result" && wordsCapturedAfterSecondVerb == 4) ||
                        j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to scenarioType,
                                "accWordNo" to condWord,
                                "firstVerb" to (firstVerb ?: -1),
                                "secondVerb" to (secondVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesWhenWithVerbsINnodelimi(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("إِن") || arabicWord.contains("وَإِن") || arabicWord.contains("فَإِن") || arabicWord.contains(
                    "أَفَإِي۟ن"
                )
                        || arabicWord.contains("وَلَئِنِ") || arabicWord.contains("")

            if (isCondi && isWord) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstVerb: Int? = null
                var secondVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0
                var scenarioType = ""

                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = listOf(
                            currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                            currentRow.tagfour, currentRow.tagfive
                        ),
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Check for verb types in details
                    if (scenarioType.isEmpty()) {
                        if (currentDetails.any { it?.contains("PERF") == true }) {
                            if (firstVerb == null) {
                                firstVerb = currentRow.wordno
                                scenarioType = "Scenario inmadhi"
                            } else if (secondVerb == null) {
                                secondVerb = currentRow.wordno
                            }
                        } else if (currentDetails.any { it?.contains("MOOD:JUS") == true }) {
                            if (firstVerb == null) {
                                firstVerb = currentRow.wordno
                                scenarioType = "Scenario injussive"
                            } else if (secondVerb == null) {
                                secondVerb = currentRow.wordno
                            }
                        } else if (currentDetails.any { it?.contains("RSLT") == true } && scenarioType.isEmpty()) {
                            scenarioType = "Scenario result"
                            secondVerb = currentRow.wordno
                        }
                    } else {
                        if ((scenarioType == "Scenario inmadhi" && currentDetails.any {
                                it?.contains(
                                    "PERF"
                                ) == true
                            } && secondVerb == null) ||
                            (scenarioType == "Scenario injussive" && currentDetails.any {
                                it?.contains(
                                    "MOOD:JUS"
                                ) == true
                            } && secondVerb == null)) {
                            secondVerb = currentRow.wordno
                        }
                    }

                    // Increment the words captured after the second verb
                    if (secondVerb != null || scenarioType == "Scenario result") {
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition based on the scenario
                    if ((secondVerb != null && wordsCapturedAfterSecondVerb == 4) ||
                        (scenarioType == "Scenario result" && wordsCapturedAfterSecondVerb == 4) ||
                        j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to scenarioType,
                                "accWordNo" to condWord,
                                "firstVerb" to (firstVerb ?: -1),
                                "secondVerb" to (secondVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesWhenWithVerbsINprevious(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("إِن") || arabicWord.contains("وَإِن") || arabicWord.contains("فَإِن") || arabicWord.contains(
                    "أَفَإِي۟ن"
                )
                        || arabicWord.contains("وَلَئِنِ") || arabicWord.contains("")

            if (isCondi && isWord) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstVerb: Int? = null
                var secondVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0
                var scenarioType = ""

                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = listOf(
                            currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                            currentRow.tagfour, currentRow.tagfive
                        ),
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Check for verb types in details
                    if (currentDetails.any { it?.contains("PERF") == true } && scenarioType.isEmpty()) {
                        if (firstVerb == null) {
                            firstVerb = currentRow.wordno
                            scenarioType = "Scenario inmadhi" // First scenario
                        } else if (secondVerb == null) {
                            secondVerb = currentRow.wordno
                        }
                        //   } else if (currentDetails.any { it?.contains("|MOOD:JUS") == true }) {
                    } else if (currentDetails.any { it?.contains("|MOOD:JUS") == true } && scenarioType.isEmpty()) {
                        if (firstVerb == null) {
                            firstVerb = currentRow.wordno
                            scenarioType = "Scenario injussive" // Second scenario
                        } else if (secondVerb == null) {
                            secondVerb = currentRow.wordno
                        }
                    } else if (currentDetails.any { it?.contains("RSLT") == true } && scenarioType.isEmpty()) {
                        // Third scenario with any verb followed by an RSLT-tagged word
                        scenarioType = "Scenario result"
                        secondVerb = currentRow.wordno
                    }

                    // Increment the words captured after the second verb
                    if (secondVerb != null || scenarioType == "Scenario result") {
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition based on the scenario
                    if ((secondVerb != null && wordsCapturedAfterSecondVerb == 4) ||
                        (scenarioType == "Scenario result" && wordsCapturedAfterSecondVerb == 4) ||
                        j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to scenarioType,
                                "accWordNo" to condWord,
                                "firstVerb" to (firstVerb ?: -1),
                                "secondVerb" to (secondVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }


    fun extractConditionalSentencesWhenWithVerbsINv1old(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("إِن") || arabicWord.contains("وَإِن") || arabicWord.contains("فَإِن") || arabicWord.contains(
                    "أَفَإِي۟ن"
                )
                        || arabicWord.contains("وَلَئِنِ") || arabicWord.contains("")

            if (isCondi && isWord) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstVerb: Int? = null
                var secondVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0
                var scenarioType = ""
                var resultWordNo = -1


                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = listOf(
                            currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                            currentRow.tagfour, currentRow.tagfive
                        ),
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Check for verb types in details
                    if (currentDetails.any { it?.contains("PERF") == true }) {
                        if (firstVerb == null) {
                            firstVerb = currentRow.wordno
                            scenarioType = "Scenario inmadhi" // First scenario
                        } else if (secondVerb == null) {
                            secondVerb = currentRow.wordno
                        }
                    } else if (currentDetails.any { it?.contains("|MOOD:JUS") == true }) {
                        if (firstVerb == null) {
                            firstVerb = currentRow.wordno
                            scenarioType = "Scenario injussive" // Second scenario
                        } else if (secondVerb == null) {
                            secondVerb = currentRow.wordno
                        }
                    } else if (currentDetails.any { it?.contains("RSLT") == true && scenarioType.isEmpty() }) {
                        // Third scenario with any verb followed by an RSLT-tagged word
                        scenarioType = "Scenario result"
                        secondVerb = currentRow.wordno
                    }

                    // Increment the words captured after the second verb
                    if (secondVerb != null) {
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition based on the scenario
                    if (secondVerb != null && wordsCapturedAfterSecondVerb == 4 ||
                        (scenarioType == "Scenario result" && wordsCapturedAfterSecondVerb == 4) ||
                        j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to scenarioType,
                                "accWordNo" to condWord,
                                "firstVerb" to (firstVerb ?: -1),
                                "secondVerb" to (secondVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesWhenWithVerbsINv1(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("إِن") || arabicWord.contains("وَإِن") || arabicWord.contains("فَإِن") || arabicWord.contains(
                    "أَفَإِي۟ن"
                )
                        || arabicWord.contains("وَلَئِنِ") || arabicWord.contains("")

            if (isCondi && isWord) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstPerfectVerb: Int? = null
                var secondPerfectVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0

                // Start capturing from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = listOf(
                            currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                            currentRow.tagfour, currentRow.tagfive
                        ),
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Check for perfect verbs in details
                    if (currentDetails.any { it?.contains("PERF") == true }) {

                        when {
                            firstPerfectVerb == null -> firstPerfectVerb = currentRow.wordno
                            secondPerfectVerb == null -> secondPerfectVerb = currentRow.wordno
                        }
                    } else if (secondPerfectVerb != null) {
                        // Increment the words captured after second perfect verb
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition for capturing up to 4 words after second perfect verb
                    if (secondPerfectVerb != null && (wordsCapturedAfterSecondVerb == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario inmadhi",
                                "accWordNo" to condWord,
                                "firstPerfectVerb" to (firstPerfectVerb ?: -1),
                                "secondPerfectVerb" to (secondPerfectVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    } else if (j == data.size - 1) { // Check if loop reached the end
                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 2",
                                "accWordNo" to condWord,
                                "firstPerfectVerb" to (firstPerfectVerb ?: -1),
                                "secondPerfectVerb" to (secondPerfectVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences // Return the conditionalSentence
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesWhenWithVerbsIZA(
        data: List<CorpusEntity>,
        qurantext: String,
        translation: String
    ): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("T") == true }
            val isWord =
                arabicWord.contains("إِذَا") || arabicWord.contains("فَإِذَا") || arabicWord.contains(
                    "وَإِذَا"
                )
            if (isCondi && isWord) {
                val sequence = mutableListOf<ShartCorpusRow>()
                val condWord = row.wordno
                var firstPerfectVerb: Int? = null
                var secondPerfectVerb: Int? = null
                var wordsCapturedAfterSecondVerb = 0

                // Start capturing from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )

                    // Add current row to sequence
                    val shartCorpusRow = ShartCorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = listOf(
                            currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                            currentRow.tagfour, currentRow.tagfive
                        ),
                        details = currentDetails,
                        arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree +
                                currentRow.arafour + currentRow.arafive,
                        englishText = currentRow.en,
                        quranText = qurantext,
                        translation = translation
                    )
                    sequence.add(shartCorpusRow)

                    // Check for perfect verbs in details
                    if (currentDetails.any { it?.contains("PERF") == true }) {

                        when {
                            firstPerfectVerb == null -> firstPerfectVerb = currentRow.wordno
                            secondPerfectVerb == null -> secondPerfectVerb = currentRow.wordno
                        }
                    } else if (secondPerfectVerb != null) {
                        // Increment the words captured after second perfect verb
                        wordsCapturedAfterSecondVerb++
                    }

                    // Exit condition for capturing up to 4 words after second perfect verb
                    if (secondPerfectVerb != null && (wordsCapturedAfterSecondVerb == 4 ||
                                j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah)
                    ) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 2",
                                "accWordNo" to condWord,
                                "firstPerfectVerb" to (firstPerfectVerb ?: -1),
                                "secondPerfectVerb" to (secondPerfectVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        break
                    } else if (j == data.size - 1) { // Check if loop reached the end
                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 2",
                                "accWordNo" to condWord,
                                "firstPerfectVerb" to (firstPerfectVerb ?: -1),
                                "secondPerfectVerb" to (secondPerfectVerb ?: -1),
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences // Return the conditionalSentence
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesIZAv1(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            // Check if this row is a conditional word (إِذَا, فَإِذَا, وَإِذَا)
            val isCondi = tags.any { it?.contains("T") == true }
            val isWord =
                arabicWord.contains("إِذَا") || arabicWord.contains("فَإِذَا") || arabicWord.contains(
                    "وَإِذَا"
                )
            if (isCondi && isWord) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno
                var firstPerfectVerb = -1
                var secondPerfectVerb = -1
                var wordsCapturedAfterSecondVerb = 0

                // Start capturing from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentDetails = listOf(
                        currentRow.detailsone, currentRow.detailstwo, currentRow.detailsthree,
                        currentRow.detailsfour, currentRow.detailsfive
                    )
                    val currentArabicWords = listOf(
                        currentRow.araone, currentRow.aratwo, currentRow.arathree,
                        currentRow.arafour, currentRow.arafive
                    )

                    // Add current row to sequence
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = currentDetails,
                        arabicText = currentArabicWords.joinToString(""),
                        englishText = currentRow.en
                    )
                    sequence.add(corpusRow)

                    // Check for perfect verbs in details
                    if (firstPerfectVerb == -1 && currentDetails.contains("PERF")) {
                        firstPerfectVerb = currentRow.wordno
                    } else if (firstPerfectVerb != -1 && secondPerfectVerb == -1 && currentDetails.contains(
                            "PERF"
                        )
                    ) {
                        secondPerfectVerb = currentRow.wordno
                    } else if (secondPerfectVerb != -1) {
                        // After finding the second perfect verb, capture up to 4 more words or until end of sentence
                        wordsCapturedAfterSecondVerb++
                        if (wordsCapturedAfterSecondVerb == 4 || j == data.size - 1 || currentRow.ayah != row.ayah || currentRow.surah != row.surah) {
                            conditionalSentences.add(
                                mapOf(
                                    "type" to "Scenario 2",
                                    "accWordNo" to condWord,
                                    "firstPerfectVerb" to firstPerfectVerb,
                                    "secondPerfectVerb" to secondPerfectVerb,
                                    "endword" to currentRow.wordno,
                                    "predicateSequence" to sequence
                                )
                            )
                            break
                        }
                    }
                }
            }
        }
        return conditionalSentences
    }

    fun extractConditionalSentencesLau(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()
        var endWordNo = 0
        var emphRsltWord = 0

        for (i in data.indices) {
            val row = data[i]
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("لَوْ") || arabicWord.contains("أَوَلَوْ") || arabicWord.contains(
                    "وَلَوْ"
                )
                        || arabicWord.contains("وَلَوْلَا") || arabicWord.contains("فَلَوْلَا")

            val isConditional = isWord && isCondi
            if (isConditional) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno

                // Append ":" after the condition word for clarity
                val initialCorpusRow = CorpusRow(
                    surah = row.surah,
                    ayah = row.ayah,
                    wordno = row.wordno,
                    tags = tags,
                    details = listOf(
                        row.detailsone,
                        row.detailstwo,
                        row.detailsthree,
                        row.detailsfour,
                        row.detailsfive
                    ),
                    arabicText = arabicWord + ":",  // Adding delimiter after the condition word
                    englishText = row.en
                )
                sequence.add(initialCorpusRow)

                // Start capturing the sequence from the conditional word
                for (j in i + 1 until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentArabicWords = listOf(
                        currentRow.araone, currentRow.aratwo, currentRow.arathree,
                        currentRow.arafour, currentRow.arafive
                    )

                    // Check for an emphatic verb (EMPH + V) or result verb (RSLT + V)
                    val emphIndex = currentTags.indexOf("EMPH")
                    val resultIndex = currentTags.indexOf("RSLT")
                    val isEmphaticVerb = if (emphIndex != -1 && emphIndex + 1 < currentTags.size) {
                        currentTags[emphIndex + 1] == "V" && currentTags[emphIndex] == "EMPH"
                    } else false

                    val isResultVerb =
                        if (resultIndex != -1 && resultIndex + 1 < currentTags.size) {
                            currentTags[resultIndex + 1] == "V" && currentTags[resultIndex] == "RSLT"
                        } else false

                    // Add ":" delimiter before the emphatic or result verb in arabicText
                    //  val modifiedArabicText = if (isEmphaticVerb || isResultVerb) {
                    val modifiedArabicText = if (isEmphaticVerb) {
                        ":" + currentArabicWords.joinToString("")
                    } else {
                        currentArabicWords.joinToString("")
                    }

                    // Create CorpusRow with modified arabicText
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = listOf(
                            currentRow.detailsone, currentRow.detailstwo,
                            currentRow.detailsthree, currentRow.detailsfour,
                            currentRow.detailsfive
                        ),
                        arabicText = modifiedArabicText,
                        englishText = currentRow.en
                    )

                    sequence.add(corpusRow)
//if (isEmphaticVerb || isResultVerb) {
                    if (isEmphaticVerb) {
                        emphRsltWord = currentRow.wordno
                        var wordsCaptured = 0

                        // Capture the next 4 unique words after the emphatic or result verb
                        for (k in j + 1 until min(data.size, j + 5)) {
                            val nextRow = data[k]
                            if (nextRow.ayah != currentRow.ayah || nextRow.surah != currentRow.surah) break

                            val nextTags = listOf(
                                nextRow.tagone, nextRow.tagtwo, nextRow.tagthree,
                                nextRow.tagfour, nextRow.tagfive
                            )
                            val nextArabicWords = listOf(
                                nextRow.araone, nextRow.aratwo, nextRow.arathree,
                                nextRow.arafour, nextRow.arafive
                            )

                            val nextCorpusRow = CorpusRow(
                                surah = nextRow.surah,
                                ayah = nextRow.ayah,
                                wordno = nextRow.wordno,
                                tags = nextTags,
                                details = listOf(
                                    nextRow.detailsone, nextRow.detailstwo,
                                    nextRow.detailsthree, nextRow.detailsfour,
                                    nextRow.detailsfive
                                ),
                                arabicText = nextArabicWords.joinToString(""),
                                englishText = nextRow.en
                            )
                            sequence.add(nextCorpusRow)
                            wordsCaptured++
                            endWordNo = nextRow.wordno

                            if (wordsCaptured == 4) {
                                conditionalSentences.add(
                                    mapOf(
                                        "type" to "Scenario 1",
                                        "accWordNo" to condWord,
                                        "emphaticVerbWordNo" to emphRsltWord,
                                        "endword" to nextRow.wordno,
                                        "predicateSequence" to sequence
                                    )
                                )
                                return conditionalSentences
                            }
                        }
                    } else if (j == data.size - 1) {
                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 1",
                                "accWordNo" to condWord,
                                "emphaticVerbWordNo" to emphRsltWord,
                                "endword" to currentRow.wordno,
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences
                    }
                }
            }
        }
        return conditionalSentences
    }


    fun extractConditionalSentencesLauone(data: List<CorpusEntity>): List<Map<String, Any>> {
        val conditionalSentences = mutableListOf<Map<String, Any>>()
        var endWordNo = 0
        var condWord = 0
        var emphRsltWord = 0

        for (i in data.indices) {
            val row = data[i]


            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)

            val arabicWord = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive

            val isCondi = tags.any { it?.contains("COND") == true }
            val isWord =
                arabicWord.contains("لَوْ") || arabicWord.contains("أَوَلَوْ") || arabicWord.contains(
                    "وَلَوْ"
                ) || arabicWord.contains("وَلَوْلَا") || arabicWord.contains("فَلَوْلَا")

            val isConditional = isWord && isCondi
            if (row.surah == 2 && row.ayah == 64) {

                println("check")
            }
            if (isConditional) {
                val sequence = mutableListOf<CorpusRow>()
                val condWord = row.wordno

                // Start capturing the sequence from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentArabicWords = listOf(
                        currentRow.araone, currentRow.aratwo, currentRow.arathree,
                        currentRow.arafour, currentRow.arafive
                    )

                    // Create CorpusRow for the current entry
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = listOf(
                            currentRow.detailsone, currentRow.detailstwo,
                            currentRow.detailsthree, currentRow.detailsfour,
                            currentRow.detailsfive
                        ),
                        arabicText = currentArabicWords.joinToString(""),
                        englishText = currentRow.en
                    )

                    sequence.add(corpusRow)

                    // Check for an emphatic verb (EMPH + V)
                    val emphIndex = currentTags.indexOf("EMPH")
                    val resultIndex = currentTags.indexOf("RSLT")
                    val isEmphaticVerb = if (emphIndex != -1 && emphIndex + 1 < currentTags.size) {
                        currentTags[emphIndex + 1] == "V" && currentTags[emphIndex] == "EMPH"


                    } else {
                        false
                    }

                    val isResultVerb =
                        if (resultIndex != -1 && resultIndex + 1 < currentTags.size) {
                            currentTags[resultIndex + 1] == "V" && currentTags[resultIndex] == "RSLT"


                        } else {
                            false
                        }

                    if (isEmphaticVerb || isResultVerb) {

                        if (currentRow.surah == 25 && currentRow.ayah == 77) {
                            println("check")
                        }
                        emphRsltWord = currentRow.wordno

                        var wordsCaptured = 0

                        // Capture the next 4 unique words after the emphatic verb
                        //         for (k in j + 1 until data.size) {
                        for (k in j + 1 until min(data.size, j + 4)) {
                            val nextRow = data[k]
                            if (nextRow.ayah != currentRow.ayah || nextRow.surah != currentRow.surah) break
                            val nextTags = listOf(
                                nextRow.tagone, nextRow.tagtwo, nextRow.tagthree,
                                nextRow.tagfour, nextRow.tagfive
                            )
                            val nextArabicWords = listOf(
                                nextRow.araone, nextRow.aratwo, nextRow.arathree,
                                nextRow.arafour, nextRow.arafive
                            )

                            // Create CorpusRow for the next entry
                            val nextCorpusRow = CorpusRow(
                                surah = nextRow.surah,
                                ayah = nextRow.ayah,
                                wordno = nextRow.wordno,
                                tags = nextTags,
                                details = listOf(
                                    nextRow.detailsone, nextRow.detailstwo,
                                    nextRow.detailsthree, nextRow.detailsfour,
                                    nextRow.detailsfive
                                ),
                                arabicText = nextArabicWords.joinToString(""),
                                englishText = nextRow.en
                            )
                            sequence.add(nextCorpusRow)
                            wordsCaptured++
                            endWordNo = nextRow.wordno
                            if (wordsCaptured == 4) {
                                conditionalSentences.add(
                                    mapOf(
                                        "type" to "Scenario 1",
                                        "accWordNo" to condWord,
                                        "emphaticVerbWordNo" to emphRsltWord,
                                        "endword" to nextRow.wordno,
                                        "predicateSequence" to sequence
                                    )!!
                                )
                                return conditionalSentences
                            }

                        }


                    } else if (j == data.size - 1) {

                        conditionalSentences.add(
                            mapOf(
                                "type" to "Scenario 1",
                                "accWordNo" to condWord,
                                "emphaticVerbWordNo" to emphRsltWord, // Set to 0 if not found
                                "endword" to currentRow.wordno, // Set to 0 if not found
                                "predicateSequence" to sequence
                            )
                        )
                        return conditionalSentences
                    }
                }
            }
        }

        return conditionalSentences
    }

    fun extractConditionalSentencesv1orgi(data: List<CorpusEntity>): List<Map<String, Any>> {
        //   val conditionalSentences = mutableListOf<List<CorpusRow>>()
        val conditionalSentences = mutableListOf<Map<String, Any>>()
        var endWordNo: Int? = null
        for (i in data.indices) {
            val row = data[i]
            if (row.surah == 2 && row.ayah == 20 && row.wordno == 14) {
                println("check")
            }

            // Check if any tag is "COND" and the corresponding Arabic text is "لَوْ"
            val tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive)
            val arabicWords =
                listOf(row.araone + row.aratwo + row.arathree + row.arafour + row.arafive)

            val isConditional = tags.zip(arabicWords).any { (tag, word) ->
                tag == "COND" && word == "لَوْ" || word == "أَوَلَوْ" || word == "وَلَوْ"

            }

            if (isConditional) {
                val sequence = mutableListOf<CorpusRow>()
                val accWordNo = row.wordno
                // Start capturing the sequence from the conditional word
                for (j in i until data.size) {
                    val currentRow = data[j]
                    val currentTags = listOf(
                        currentRow.tagone, currentRow.tagtwo, currentRow.tagthree,
                        currentRow.tagfour, currentRow.tagfive
                    )
                    val currentArabicWords = listOf(
                        currentRow.araone, currentRow.aratwo, currentRow.arathree,
                        currentRow.arafour, currentRow.arafive
                    )

                    // Create CorpusRow for the current entry
                    val corpusRow = CorpusRow(
                        surah = currentRow.surah,
                        ayah = currentRow.ayah,
                        wordno = currentRow.wordno,
                        tags = currentTags,
                        details = listOf(
                            currentRow.detailsone, currentRow.detailstwo,
                            currentRow.detailsthree, currentRow.detailsfour,
                            currentRow.detailsfive
                        ),
                        arabicText = currentArabicWords.joinToString(""),
                        englishText = currentRow.en
                    )

                    sequence.add(corpusRow)

                    val emphIndex = currentTags.indexOf("EMPH")
                    val isEmphaticVerb = if (emphIndex != -1 && emphIndex + 1 < currentTags.size) {
                        currentTags[emphIndex + 1] == "V" && currentTags[emphIndex] == ("EMPH")
                    } else {
                        false
                    }
                    if (isEmphaticVerb) {
                        endWordNo = currentRow.wordno

                    }

                    if (isEmphaticVerb) {
                        endWordNo = currentRow.wordno
                        // Capture 4 more words or until end of sentence
                        var wordsCaptured = 0

                        for (k in j + 1 until min(data.size, j + 4)) { // Capture up to 4 more words
                            val nextRow = data[k]
                            if (nextRow.surah == 2 && nextRow.ayah == 20) {
                                println("check")
                            }
                            val corpusRow = CorpusRow(
                                surah = nextRow.surah,
                                ayah = nextRow.ayah,
                                wordno = nextRow.wordno,
                                tags = currentTags,
                                details = listOf(
                                    nextRow.detailsone, nextRow.detailstwo,
                                    nextRow.detailsthree, nextRow.detailsfour,
                                    nextRow.detailsfive
                                ),
                                arabicText = currentArabicWords.joinToString(""),
                                englishText = currentRow.en
                            )
                            sequence.add(corpusRow)
                            wordsCaptured++
                            if (wordsCaptured == 3) {
                                conditionalSentences.add(
                                    mapOf(
                                        "type" to "Scenario 1",
                                        "accWordNo" to accWordNo,
                                        "predicateSequence" to sequence
                                    )
                                )
                                return conditionalSentences// Break when 3 words are captured

                            }

                        }
                    }

                    // Check if any tag indicates an emphatic verb to end the sequence
                    //  val isEmphaticVerb = currentTags.any { it == "EMPH" && currentArabicWords[it].startsWith("V") }


                }

                // Add the captured sequence to the list of conditional sentences
                //  conditionalSentences.add(sequence)
            }
        }

        return conditionalSentences
    }

    fun extractKanaSentences(corpusData: List<CorpusEntity>, qurantext: String, translation: String): List<Map<String, Any>> {
        val accusativeSentences = mutableListOf<Map<String, Any>>()

        for (i in corpusData.indices) {
            val row = corpusData[i]
            val tags = listOf(
                row.rootaraone,
                row.rootaratwo,
                row.rootarathree,
                row.rootarafour,
                row.rootarafive,
                row.tagone,
                row.tagtwo,
                row.tagthree,
                row.tagfour,
                row.tagfive
            ) // Create tags list
            val detailstag = listOf(
                row.detailsone,
                row.detailstwo,
                row.detailsthree,
                row.detailsfour,
                row.detailsfive
            )
            if (row.surah == 2 && row.ayah == 34 && row.wordno == 11) {
                println("check")
            }
            if ("كون" in tags && "PRON" in tags) {
                val accWordNo = row.wordno
                //     val predicateSequence = findSequenceEndingInNom(corpusData, startIndex = i)
                val predicateSequence = findSequenceEndingInAccu(corpusData, startIndex = i)
                if (predicateSequence != null) {
                    accusativeSentences.add(
                        mapOf(
                            "type" to "Scenario 1",
                            "accWordNo" to accWordNo,
                            "quranverse" to qurantext,
                            "translation" to translation,
                            "predicateSequence" to predicateSequence
                        )
                    )
                }
            } else if ("كون" in tags) {
                val accWordNo = row.wordno
                //   val accSequence = findAccNounSequenceEndingInNom(corpusData, startIndex = i)
                // val accSequence = findAccNounSequenceEndingInNomoldnew(corpusData, startIndex = i)
                if (row.surah == 2 && row.ayah == 34 && row.wordno == 11) {
                    println("check")
                }

                val accSequence = findAccNounSequenceEndingInACC(corpusData, startIndex = i)
                if (accSequence != null) {
                    accusativeSentences.add(
                        mapOf(
                            "type" to "Scenario 2",
                            "accWordNo" to accWordNo,
                            "quranverse" to qurantext,
                            "translation" to translation,
                            "accSequence" to accSequence
                        )
                    )
                }/* else {
                    // Capture surah and ayah if no sequence found
                    accusativeSentences.add(
                        mapOf(
                            "type" to "No sequence found for Scenario 2",
                            "surah" to row.surah,
                            "ayah" to row.ayah
                        )
                    )
                }*/
            }

        }

        return accusativeSentences
    }



    fun findSequenceEndingInAccu(
        data: List<CorpusEntity>,
        startIndex: Int,
    ): List<CorpusRow>? {
        val sequence = mutableListOf<CorpusRow>()

        for (i in startIndex until data.size) {
            val entity = data[i]
            val row = CorpusRow(
                surah = entity.surah,
                ayah = entity.ayah,
                wordno = entity.wordno,
                tags = listOf(
                    entity.tagone,
                    entity.tagtwo,
                    entity.tagthree,
                    entity.tagfour,
                    entity.tagfive
                ),
                details = listOf(
                    entity.detailsone,
                    entity.detailstwo,
                    entity.detailsthree,
                    entity.detailsfour,
                    entity.detailsfive
                ),
                arabicText = entity.araone + entity.aratwo + entity.arathree + entity.arafour + entity.arafive,
                englishText = entity.en
            )

            sequence.add(row)

            if (row.details.any { it!!.contains("ACC") }) {
                // Check if the next element exists and meets the conditions
                if (i + 1 < data.size) {
                    val nextEntity = data[i + 1]
                    if (nextEntity.tagone == "N" || nextEntity.tagtwo == "N" || nextEntity.tagthree == "N" || nextEntity.tagfour == "N") {
                        if (nextEntity.detailsone?.contains("ACC") == true || nextEntity.detailstwo?.contains(
                                "ACC"
                            ) == true ||
                            nextEntity.detailsthree?.contains("NOM") == true || nextEntity.detailsfour?.contains(
                                "ACC"
                            ) == true
                        ) {
                            val nextRow = CorpusRow(
                                surah = nextEntity.surah,
                                ayah = nextEntity.ayah,
                                wordno = nextEntity.wordno,
                                tags = listOf(
                                    nextEntity.tagone,
                                    nextEntity.tagtwo,
                                    nextEntity.tagthree,
                                    nextEntity.tagfour,
                                    nextEntity.tagfive
                                ),
                                details = listOf(
                                    nextEntity.detailsone,
                                    nextEntity.detailstwo,
                                    nextEntity.detailsthree,
                                    nextEntity.detailsfour,
                                    nextEntity.detailsfive
                                ),
                                arabicText = nextEntity.araone + nextEntity.aratwo + nextEntity.arathree + nextEntity.arafour + nextEntity.arafive,
                                englishText = nextEntity.en
                            )
                            sequence.add(nextRow)
                        }
                    }
                }
                return sequence
            }
        }

        return sequence
    }

    fun findSequenceEndingInNomold(
        data: List<CorpusEntity>,
        startIndex: Int,
    ): List<CorpusRow>? {
        val sequence = mutableListOf<CorpusRow>()

        for (i in startIndex until data.size) {
            val entity = data[i]
            val row = CorpusRow(
                surah = entity.surah,
                ayah = entity.ayah,
                wordno = entity.wordno,
                tags = listOf(
                    entity.tagone,
                    entity.tagtwo,
                    entity.tagthree,
                    entity.tagfour,
                    entity.tagfive
                ),
                details = listOf(
                    entity.detailsone,
                    entity.detailstwo,
                    entity.detailsthree,
                    entity.detailsfour,
                    entity.detailsfive
                ),
                arabicText = entity.araone + entity.aratwo + entity.arathree + entity.arafour + entity.arafive,
                englishText = entity.en
            )

            sequence.add(row)

            if (row.details.any { it!!.contains("NOM") }) {
                // Check if the next element exists and meets the conditions
                if (i + 1 < data.size) {
                    val nextEntity = data[i + 1]
                    if (nextEntity.tagone == "N" || nextEntity.tagtwo == "N" || nextEntity.tagthree == "N" || nextEntity.tagfour == "N") {
                        if (nextEntity.detailsone?.contains("NOM") == true || nextEntity.detailstwo?.contains(
                                "NOM"
                            ) == true ||
                            nextEntity.detailsthree?.contains("NOM") == true || nextEntity.detailsfour?.contains(
                                "NOM"
                            ) == true
                        ) {
                            val nextRow = CorpusRow(
                                surah = nextEntity.surah,
                                ayah = nextEntity.ayah,
                                wordno = nextEntity.wordno,
                                tags = listOf(
                                    nextEntity.tagone,
                                    nextEntity.tagtwo,
                                    nextEntity.tagthree,
                                    nextEntity.tagfour,
                                    nextEntity.tagfive
                                ),
                                details = listOf(
                                    nextEntity.detailsone,
                                    nextEntity.detailstwo,
                                    nextEntity.detailsthree,
                                    nextEntity.detailsfour,
                                    nextEntity.detailsfive
                                ),
                                arabicText = nextEntity.araone + nextEntity.aratwo + nextEntity.arathree + nextEntity.arafour + nextEntity.arafive,
                                englishText = nextEntity.en
                            )
                            sequence.add(nextRow)
                        }
                    }
                }
                return sequence
            }
        }

        return sequence
    }


    fun findAccNounSequenceEndingInACC(
        data: List<CorpusEntity>,
        startIndex: Int
    ): List<CorpusRow>? {
        val sequence = mutableListOf<CorpusRow>()

        for (i in startIndex until data.size) {
            val currentRow = data[i]
            val corpusRow = CorpusRow(
                surah = currentRow.surah,
                ayah = currentRow.ayah,
                wordno = currentRow.wordno,
                tags = listOf(
                    currentRow.tagone,
                    currentRow.tagtwo,
                    currentRow.tagthree,
                    currentRow.tagfour,
                    currentRow.tagfive
                ),
                details = listOf(
                    currentRow.detailsone,
                    currentRow.detailstwo,
                    currentRow.detailsthree,
                    currentRow.detailsfour,
                    currentRow.detailsfive
                ),
                arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree + currentRow.arafour + currentRow.arafive,
                englishText = currentRow.en
            )


            // Add the current row to the sequence
            sequence.add(corpusRow)
            if (currentRow.surah == 11 && currentRow.ayah == 90) {
                println("check")
            }

            // Check if the current row is nominative (NOM)
            val hasAccusative = corpusRow.details.any { it?.contains("ACC") == true }





            if (hasAccusative && i + 2 < data.size) {
                // Create CorpusRow objects for the next row and the one after it
                val nextRow = CorpusRow(
                    surah = data[i + 1].surah,
                    ayah = data[i + 1].ayah,
                    wordno = data[i + 1].wordno,
                    tags = listOf(
                        data[i + 1].tagone,
                        data[i + 1].tagtwo,
                        data[i + 1].tagthree,
                        data[i + 1].tagfour,
                        data[i + 1].tagfive
                    ),
                    details = listOf(
                        data[i + 1].detailsone,
                        data[i + 1].detailstwo,
                        data[i + 1].detailsthree,
                        data[i + 1].detailsfour,
                        data[i + 1].detailsfive
                    ),
                    arabicText = data[i + 1].araone + data[i + 1].aratwo + data[i + 1].arathree + data[i + 1].arafour + data[i + 1].arafive,
                    englishText = data[i + 1].en
                )
                var hasNextRowAcc = nextRow.details.any { it?.contains("ACC") == true }
                if (hasNextRowAcc)
                    sequence.add(nextRow)
                val nextNextRow = CorpusRow(
                    surah = data[i + 2].surah,
                    ayah = data[i + 2].ayah,
                    wordno = data[i + 2].wordno,
                    tags = listOf(
                        data[i + 2].tagone,
                        data[i + 2].tagtwo,
                        data[i + 2].tagthree,
                        data[i + 2].tagfour,
                        data[i + 2].tagfive
                    ),
                    details = listOf(
                        data[i + 2].detailsone,
                        data[i + 2].detailstwo,
                        data[i + 2].detailsthree,
                        data[i + 2].detailsfour,
                        data[i + 2].detailsfive
                    ),
                    arabicText = data[i + 2].araone + data[i + 2].aratwo + data[i + 2].arathree + data[i + 2].arafour + data[i + 2].arafive,
                    englishText = data[i + 2].en
                )
                val hasNextNextRowAcc = nextNextRow.details.any { it?.contains("ACC") == true }
                if (hasNextNextRowAcc) {
                    sequence.add(nextNextRow)
                    return sequence
                }
                /*       // Check if the next word is a preposition (P)
                       if (nextRow.tags.contains("P")) {
                           // Add the preposition row to the sequence
                           sequence.add(nextRow)

                           // Check if the following word is genitive (GEN)
                           if (nextNextRow.details.any { it?.contains("GEN") == true }) {
                               sequence.add(nextNextRow)
                               return sequence // Return the sequence with NOM, P, and GEN
                           }
                       }*/
            } else if (hasAccusative && i + 1 < data.size) {
                val nextRow = CorpusRow(
                    surah = data[i + 1].surah,
                    ayah = data[i + 1].ayah,
                    wordno = data[i + 1].wordno,
                    tags = listOf(
                        data[i + 1].tagone,
                        data[i + 1].tagtwo,
                        data[i + 1].tagthree,
                        data[i + 1].tagfour,
                        data[i + 1].tagfive
                    ),
                    details = listOf(
                        data[i + 1].detailsone,
                        data[i + 1].detailstwo,
                        data[i + 1].detailsthree,
                        data[i + 1].detailsfour,
                        data[i + 1].detailsfive
                    ),
                    arabicText = data[i + 1].araone + data[i + 1].aratwo + data[i + 1].arathree + data[i + 1].arafour + data[i + 1].arafive,
                    englishText = data[i + 1].en
                )

                if (nextRow.details.any { it?.contains("ACC") == true }) {
                    sequence.add(nextRow)
                    return sequence // Return the sequence with NOM, P, and GEN
                }

            }

            // Check if the current row ends with NOM; if so, return the sequence
            if (hasAccusative) {
                return sequence
            }
        }

        return sequence // Return the full sequence if no NOM or GEN after "P" is found
    }

    fun findAccNounSequenceEndingInNomLatest(
        data: List<CorpusEntity>,
        startIndex: Int
    ): List<CorpusRow>? {
        val sequence = mutableListOf<CorpusRow>()

        for (i in startIndex until data.size) {
            val currentRow = data[i]
            val corpusRow = CorpusRow(
                surah = currentRow.surah,
                ayah = currentRow.ayah,
                wordno = currentRow.wordno,
                tags = listOf(
                    currentRow.tagone,
                    currentRow.tagtwo,
                    currentRow.tagthree,
                    currentRow.tagfour,
                    currentRow.tagfive
                ),
                details = listOf(
                    currentRow.detailsone,
                    currentRow.detailstwo,
                    currentRow.detailsthree,
                    currentRow.detailsfour,
                    currentRow.detailsfive
                ),
                arabicText = currentRow.araone + currentRow.aratwo + currentRow.arathree + currentRow.arafour + currentRow.arafive,
                englishText = currentRow.en
            )


            // Add the current row to the sequence
            sequence.add(corpusRow)
            if (currentRow.surah == 11 && currentRow.ayah == 90) {
                println("check")
            }

            // Check if the current row is nominative (NOM)
            val hasNominative = corpusRow.details.any { it?.contains("NOM") == true }





            if (hasNominative && i + 2 < data.size) {
                // Create CorpusRow objects for the next row and the one after it
                val nextRow = CorpusRow(
                    surah = data[i + 1].surah,
                    ayah = data[i + 1].ayah,
                    wordno = data[i + 1].wordno,
                    tags = listOf(
                        data[i + 1].tagone,
                        data[i + 1].tagtwo,
                        data[i + 1].tagthree,
                        data[i + 1].tagfour,
                        data[i + 1].tagfive
                    ),
                    details = listOf(
                        data[i + 1].detailsone,
                        data[i + 1].detailstwo,
                        data[i + 1].detailsthree,
                        data[i + 1].detailsfour,
                        data[i + 1].detailsfive
                    ),
                    arabicText = data[i + 1].araone + data[i + 1].aratwo + data[i + 1].arathree + data[i + 1].arafour + data[i + 1].arafive,
                    englishText = data[i + 1].en
                )
                var hasNextRowNom = nextRow.details.any { it?.contains("NOM") == true }
                if (hasNextRowNom)
                    sequence.add(nextRow)
                val nextNextRow = CorpusRow(
                    surah = data[i + 2].surah,
                    ayah = data[i + 2].ayah,
                    wordno = data[i + 2].wordno,
                    tags = listOf(
                        data[i + 2].tagone,
                        data[i + 2].tagtwo,
                        data[i + 2].tagthree,
                        data[i + 2].tagfour,
                        data[i + 2].tagfive
                    ),
                    details = listOf(
                        data[i + 2].detailsone,
                        data[i + 2].detailstwo,
                        data[i + 2].detailsthree,
                        data[i + 2].detailsfour,
                        data[i + 2].detailsfive
                    ),
                    arabicText = data[i + 2].araone + data[i + 2].aratwo + data[i + 2].arathree + data[i + 2].arafour + data[i + 2].arafive,
                    englishText = data[i + 2].en
                )
                val hasNextNextRowNom = nextNextRow.details.any { it?.contains("NOM") == true }
                if (hasNextNextRowNom) {
                    sequence.add(nextNextRow)
                    return sequence
                }
                /*       // Check if the next word is a preposition (P)
                       if (nextRow.tags.contains("P")) {
                           // Add the preposition row to the sequence
                           sequence.add(nextRow)

                           // Check if the following word is genitive (GEN)
                           if (nextNextRow.details.any { it?.contains("GEN") == true }) {
                               sequence.add(nextNextRow)
                               return sequence // Return the sequence with NOM, P, and GEN
                           }
                       }*/
            } else if (hasNominative && i + 1 < data.size) {
                val nextRow = CorpusRow(
                    surah = data[i + 1].surah,
                    ayah = data[i + 1].ayah,
                    wordno = data[i + 1].wordno,
                    tags = listOf(
                        data[i + 1].tagone,
                        data[i + 1].tagtwo,
                        data[i + 1].tagthree,
                        data[i + 1].tagfour,
                        data[i + 1].tagfive
                    ),
                    details = listOf(
                        data[i + 1].detailsone,
                        data[i + 1].detailstwo,
                        data[i + 1].detailsthree,
                        data[i + 1].detailsfour,
                        data[i + 1].detailsfive
                    ),
                    arabicText = data[i + 1].araone + data[i + 1].aratwo + data[i + 1].arathree + data[i + 1].arafour + data[i + 1].arafive,
                    englishText = data[i + 1].en
                )

                if (nextRow.details.any { it?.contains("NOM") == true }) {
                    sequence.add(nextRow)
                    return sequence // Return the sequence with NOM, P, and GEN
                }

            }

            // Check if the current row ends with NOM; if so, return the sequence
            if (hasNominative) {
                return sequence
            }
        }

        return sequence // Return the full sequence if no NOM or GEN after "P" is found
    }


    fun findAccNounSequenceEndingInNomoldnew(
        data: List<CorpusEntity>,
        startIndex: Int
    ): List<CorpusRow>? {

        val sequence = mutableListOf<CorpusRow>()
        var expectGenitiveAfterPreposition = false // Flag to expect GEN after "P" tag
        for (i in startIndex until data.size) {
            val entity = data[i]
            //    for (row in data.drop(startIndex)) {
            val corpusRow = CorpusRow(
                surah = entity.surah,
                ayah = entity.ayah,
                wordno = entity.wordno,
                tags = listOf(
                    entity.tagone,
                    entity.tagtwo,
                    entity.tagthree,
                    entity.tagfour,
                    entity.tagfive
                ),
                details = listOf(
                    entity.detailsone,
                    entity.detailstwo,
                    entity.detailsthree,
                    entity.detailsfour,
                    entity.detailsfive
                ),
                arabicText = entity.araone + entity.aratwo + entity.arathree + entity.arafour + entity.arafive,
                englishText = entity.en
            )

            // Add the current entity to the sequence
            sequence.add(corpusRow)
            if (entity.surah == 11 && entity.ayah == 56) {
                println("check")
            }

            // If current entity has a preposition ("P") tag, set flag to expect GEN case next
            if (corpusRow.tags.contains("P")) {
                expectGenitiveAfterPreposition = true
                continue
            }

            // Check if the current entity has either NOM case or GEN case following a "P"
            val hasNominative = corpusRow.details.any { it?.contains("NOM") == true }
            val hasGenitiveAfterPreposition =
                expectGenitiveAfterPreposition && corpusRow.details.any { it?.contains("GEN") == true }

            if (hasNominative || hasGenitiveAfterPreposition) {
                return sequence // End sequence if NOM or valid GEN case is found
            }

            // Reset flag if no GEN case follows the preposition
            expectGenitiveAfterPreposition = false
        }

        return sequence // Return the full sequence if no NOM or GEN after "P" is found
    }

    fun findAccNounSequenceEndingInNomold(
        data: List<CorpusEntity>,
        startIndex: Int
    ): List<CorpusRow>? {

        val sequence = mutableListOf<CorpusRow>()
        for (row in data.drop(startIndex)) {
            val row = CorpusRow(
                surah = row.surah,
                ayah = row.ayah,
                wordno = row.wordno,
                tags = listOf(row.tagone, row.tagtwo, row.tagthree, row.tagfour, row.tagfive),
                details = listOf(
                    row.detailsone,
                    row.detailstwo,
                    row.detailsthree,
                    row.detailsfour,
                    row.detailsfive
                ),
                arabicText = row.araone + row.aratwo + row.arathree + row.arafour + row.arafive,
                englishText = row.en
            )

            sequence.add(row)


            val nom = row.details.any() { it!!.contains("NOM") }

            if (nom) { // End on NOM
                return sequence
            }
        }
        return null
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


    fun extractInMaIllaProhibitiveSentences(
        corpus: List<CorpusEntity>,
        spannableVerse: String
    ): List<String> {
        val extractedSentences = mutableListOf<String>()
        val uniqueSentences = mutableSetOf<String>() // Set to track unique sentences
        var eligibleNegationCount = 0 // To track valid "إِن" occurrences

        for (i in corpus.indices) {
            val entry = corpus[i]
            if (entry.surah == 11 && entry.ayah == 81) {
                println(spannableVerse)
            }

            // Check for "إِلَّا" with tagone == "EXP" or "RES"
            if ((entry.tagone == "EXP" || entry.tagone == "RES") && (entry.araone == "إِلَّا" || entry.araone == "إِلَّآ")) {
                var negationFound = false
                var startIndex = -1
                var startWordno = 0
                var endWordNo = 0
                //check if next word is noun in accusative cae
                if (i + 1 < corpus.size) {

                    val nextFirstNoun = corpus[i + 1]
                    val isNextWordNoun =
                        nextFirstNoun.tagone == "N" || nextFirstNoun.tagtwo == "N" || nextFirstNoun.tagthree == "N" || nextFirstNoun.tagfour == "N"
                    var currentWord = ""
                    var nounDetails = ""
                    //     entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
                    if (nextFirstNoun.tagone == "N") {
                        currentWord = nextFirstNoun.araone!!
                        nounDetails = nextFirstNoun.detailsone!!
                    } else if (nextFirstNoun.tagtwo == "N") {
                        currentWord = nextFirstNoun.aratwo!!
                        nounDetails = nextFirstNoun.detailstwo!!
                    } else if (nextFirstNoun.tagthree == "N") {
                        currentWord = nextFirstNoun.arathree!!
                        nounDetails = nextFirstNoun.detailsthree!!
                    } else if (nextFirstNoun.tagfour == "N") {
                        currentWord = nextFirstNoun.arafour!!
                        nounDetails = nextFirstNoun.detailsfour!!
                    } else if (nextFirstNoun.tagfive == "N") {
                        currentWord = nextFirstNoun.arafive!!
                        nounDetails = nextFirstNoun.detailsfive!!
                    }
                    var targetWordno = 0
                    var verbType = ""
                    var nounCase = nounDetails.extractCase()
                    var previousNegationCounter = 0
                    if (nounCase != "ACC")
                        break
                    else {
                        endWordNo = entry.wordno
                        for (j in i - 1 downTo 0) {
                            val previousEntry = corpus[j]

                            // Check if "إِن" or "مَا" qualifies as a valid negation for extraction
                            if ((previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "إِنْ" || previousEntry.aratwo == "مَا") || previousEntry.aratwo == "مَآ" || previousEntry.aratwo == "مَّا") ||
                                (previousEntry.tagone == "NEG" && (previousEntry.araone == "إِنْ" || previousEntry.araone == "مَا" || previousEntry.araone == "مَآ" || previousEntry.araone == "مَّا")) ||
                                (previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "لَآ" || previousEntry.aratwo == "لَا" || previousEntry.aratwo == "لَّا")) ||
                                (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَآ" || previousEntry.araone == "لَا" || previousEntry.araone == "لَّا")) ||
                                (previousEntry.tagtwo == "NEG" && (previousEntry.aratwo == "لَنْ" || previousEntry.aratwo == "لَن" || previousEntry.aratwo == "لَّن")) ||
                                (previousEntry.tagone == "NEG" && (previousEntry.araone == "لَنْ" || previousEntry.araone == "لَن" || previousEntry.araone == "لَّن")) ||

                                (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "إِنْ" || previousEntry.arathree == "مَا" || previousEntry.arathree == "مَآ" || previousEntry.arathree == "مَّا")) ||
                                (previousEntry.tagthree == "NEG" && (previousEntry.arathree == "لَآ" || previousEntry.arathree == "لَا" || previousEntry.arathree == "لَّا")) ||
                                (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "لَآ" || previousEntry.arafour == "لَا" || previousEntry.arafour == "لَّا")) ||
                                (previousEntry.tagfour == "NEG" && (previousEntry.arafour == "لَنْ" || previousEntry.arafour == "لَن" || previousEntry.arafour == "لَّن"))


                            ) {

                                // eligibleNegationCount += 1 // Increment the valid "إِن" or "مَا" occurrence count

                                // Allow extraction for all valid occurrences (not just second or third)
                                //  if (eligibleNegationCount >= 1) {
                                negationFound = true
                                previousNegationCounter = j
                                startIndex =
                                    j // Capture the index where this valid negation was found
                                startWordno = previousEntry.wordno
                                break
                                // }
                            }
                        }
                        if (!negationFound) {
                            val arabicString = StringBuilder()
                            for (iter in 2..endWordNo) {
                                arabicString.append(corpus[iter].araone)
                                    .append(corpus[iter].aratwo)
                                    .append(corpus[iter].arathree)
                                    .append(corpus[iter].arafour)
                                    .append(corpus[iter].arafive).append(" ")

                            }
                            val sentenceStartIndex =
                                spannableVerse.indexOf(arabicString.toString().trim())
                            val sentenceEndIndex = sentenceStartIndex + arabicString.length
                            val dataString =
                                "${entry.surah}|${entry.ayah}|$startWordno| $endWordNo|$$sentenceStartIndex|$sentenceEndIndex|$arabicString|$spannableVerse"
                            //  println("Searching for: $fullSentenceStr")

                            // Add the sentence to the set for uniqueness
                            //   uniqueSentences.add(fullSentenceStr)

                            // Add the unique sentence with word numbers and string indices
                            extractedSentences.add(dataString)

                            break

                        }


                    }

                }
//   listOf("لَّن", "لَن", "لَنْ", "وَلَن", "أَلَّن", "فَلَن", "وَلَنْ", "فَلَنْ")
                // Loop backwards to find valid "إِن" before "إِلَّا"

                //if a valid negation not found than check for next sentence to be noun and scan backward to catch a prohibtion which
                //      would have a LA and the verb in majzum/jussive
                if (!negationFound) {
                    println("check")
                }


            }
        }

        return extractedSentences
    }

    fun extractProhibitiveSentences(
        corpus: List<CorpusEntity>,
        spannableVerse: String
    ): List<String> {
        val extractedSentences = mutableListOf<String>()
        val uniqueSentences = mutableSetOf<String>() // Set to track unique sentences
        var eligibleNegationCount = 0 // To track valid "إِن" occurrences

        for (i in corpus.indices) {
            val entry = corpus[i]
            if (entry.surah == 3 && entry.ayah == 161) {
                println(spannableVerse)
            }
            val currentNegWOrd =
                entry.araone + entry.aratwo + entry.arathree + entry.arafour + entry.arafive
            // Check for "إِلَّا" with tagone == "EXP" or "RES"
            if ((entry.tagtwo == "NEG" && (entry.aratwo == "لَآ" || entry.aratwo == "لَا" || entry.aratwo == "لَّا")) ||
                (entry.tagone == "NEG" && (entry.araone == "لَآ" || entry.araone == "لَا" || entry.araone == "لَّا")) ||
                (entry.tagthree == "NEG" && (entry.arathree == "لَآ" || entry.arathree == "لَا" || entry.arathree == "لَّا")) ||
                (entry.tagfour == "NEG" && (entry.arafour == "لَآ" || entry.arafour == "لَا" || entry.arafive == "لَّا"))
            ) {
                var negationFound = false
                var startIndex = -1
                var startWordno = 0
                var endWordNo = 0
                //check if next word is noun in accusative cae
                if (i + 1 < corpus.size) {
                    var verbDetails = ""
                    val isNextWordVerb = corpus[i + 1]
                    val verbWord =
                        isNextWordVerb.araone + isNextWordVerb.aratwo + isNextWordVerb.arathree + isNextWordVerb.arafour + isNextWordVerb.arafive
                    var verbType = isNextWordVerb.detailsone!!.extractVerbType()
                    if (isNextWordVerb.tagone == "V") {

                        verbDetails = isNextWordVerb.detailsone!!
                    } else if (isNextWordVerb.tagtwo == "V") {

                        verbDetails = isNextWordVerb.detailstwo!!
                    } else if (isNextWordVerb.tagthree == "V") {

                        verbDetails = isNextWordVerb.detailsthree!!
                    } else if (isNextWordVerb.tagfour == "V") {

                        verbDetails = isNextWordVerb.detailsfour!!
                    } else if (isNextWordVerb.tagfive == "V") {

                        verbDetails = isNextWordVerb.detailsfive!!
                    }
                    verbType = verbDetails.extractVerbType()
                    if ((isNextWordVerb.tagone == "V") || (isNextWordVerb.tagtwo == "V") || isNextWordVerb.tagthree == "V" || isNextWordVerb.tagfour == "V" || isNextWordVerb.tagfive == "V") {
                        if (verbType == "JUS") {
                            val arabicString = currentNegWOrd + " " + verbWord

                            val sentenceStartIndex =
                                spannableVerse.indexOf(arabicString.toString().trim())
                            val sentenceEndIndex = sentenceStartIndex + arabicString.length
                            val dataString =
                                "${entry.surah}|${entry.ayah}|$startWordno| $endWordNo|$$sentenceStartIndex|$sentenceEndIndex|$arabicString|$spannableVerse"
                            //  println("Searching for: $fullSentenceStr")

                            // Add the sentence to the set for uniqueness
                            //   uniqueSentences.add(fullSentenceStr)

                            // Add the unique sentence with word numbers and string indices
                            extractedSentences.add(dataString)

                            break
                        }
                    }


                }

                if (!negationFound) {
                    println("check")
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
}

/*

     fun extractLaNafiya() {
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

     fun extractLaM() {
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

 */