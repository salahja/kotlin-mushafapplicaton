package com.example.mushafconsolidated.fragments


import android.text.SpannableString
import android.text.SpannableStringBuilder
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.utility.CorpusConstants.verbfeaturesenglisharabic
import com.example.utility.CorpusUtilityorig


// Rename the class to follow Kotlin conventions (PascalCase) and be more descriptive.
class WordMorphologyDetails(
    private val word: CorpusEntity,
    corpusNounWords: List<NounCorpus>, // Use specific types instead of '*'
    verbCorpusRootWords: List<VerbCorpus>
) : QuranMorphologyDetails() {
    private val verbCorpusRootWords: List<VerbCorpus>? = verbCorpusRootWords
    private val corpusNounWords: List<NounCorpus>? = corpusNounWords

    // No need for an init block since properties are initialized directly.

    val workBreakDown: SpannableString? // Rename to be more descriptive
        get() {
            var spannableString: SpannableString? = null
            when (word.wordcount) {
                1 -> {
                    val tagOne = word.tagone!!
                    var expandedTagOne = expandTags(tagOne)
                    expandedTagOne = when (tagOne) {
                        "N" -> getNounDetails(word.detailsone)
                        "V" -> getVerbDetails(expandedTagOne, word.detailsone!!)
                        else -> expandedTagOne
                    }
                    spannableString = SpannableString(expandedTagOne)
                    spannableString = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!, word.tagtwo!!, word.tagthree!!,
                        word.tagfour!!, word.tagfive!!, " ", " ", "", "", expandedTagOne
                    )
                }

                2 -> {
                    val tagTwo = word.tagtwo!!
                    val tagOne = word.tagone!!
                    var expandedTagOne = expandTags(tagOne)
                    val isNounOne = tagOne in listOf("N", "ADJ", "PN")
                    val isNounTwo = tagTwo in listOf("N", "ADJ", "PN")
                    var expandedTagTwo = expandTags(tagTwo)
                    if (isNounOne) {
                        expandedTagOne = getNounDetails(word.detailsone)
                    } else if (isNounTwo) {
                        expandedTagTwo = getNounDetails(word.detailstwo)
                    }
                    if (tagOne == "V") {
                        expandedTagOne = getVerbDetails(expandedTagOne, word.detailsone!!)
                    } else if (tagTwo == "V") {
                        expandedTagTwo = getVerbDetails(expandedTagTwo, word.detailstwo!!)
                    }
                    spannableString = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!, word.tagtwo!!, word.tagthree!!,
                        word.tagfour!!, word.tagfive!!, " ", " ", "", expandedTagTwo, expandedTagOne
                    )
                }

                // Refactor cases 3, 4, and 5 to avoid repetitive code
                3 -> {
                    val sb: StringBuilder = StringBuilder()
                    var expandTagsone: String =
                        expandTags(word.tagone!!)
                    var expandTagstwo: String =
                        expandTags(word.tagtwo!!)
                    var expandTagsthree: String =
                        expandTags(word.tagthree!!)
                    val tagtwo: String = word.tagtwo!!
                    val tagone: String = word.tagone!!
                    val tagthree: String = word.tagthree!!
                    val tagnounone: Boolean =
                        (tagone == "N") || (tagone == "ADJ") || (tagone == "PN")
                    val tagnountwo: Boolean =
                        (tagtwo == "N") || (tagtwo == "ADJ") || (tagtwo == "PN")
                    val tagnounthree: Boolean =
                        (tagthree == "N") || (tagthree == "ADJ") || (tagthree == "PN")
                    if (tagnounone) {
                        expandTagsone = getNounDetails(word.detailsone)
                    } else if (tagnountwo) {
                        expandTagstwo = getNounDetails(word.detailstwo)
                    } else if (tagnounthree) {
                        expandTagsthree = getNounDetails(word.detailsthree)
                    }
                    if ((word.tagone == "V")) {
                        expandTagsone = getVerbDetails(expandTagsone, word.detailsone!!)
                    } else if ((word.tagtwo == "V")) {
                        expandTagstwo = getVerbDetails(expandTagstwo, word.detailstwo!!)
                    }
                    if ((word.tagthree == "V")) {
                        expandTagsthree = getVerbDetails(expandTagsthree, word.detailsthree!!)
                    }
                    "$expandTagsone "
                    "$expandTagstwo "
                    "$expandTagsthree "
                    sb.append(word.tagthree)
                    sb.append("|")
                    sb.append(word.tagtwo)
                    sb.append("|")
                    sb.append(word.tagone)
                    spannableString = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!,
                        word.tagtwo!!,
                        word.tagthree!!,
                        word.tagfour!!,
                        word.tagfive!!,
                        " ",
                        " ",
                        expandTagsthree,
                        expandTagstwo,
                        expandTagsone
                    )
                }

                4 -> {
                    val sb: StringBuilder = java.lang.StringBuilder()
                    var expandTagsone: String =
                        expandTags(word.tagone!!)
                    var expandTagstwo: String =
                        expandTags(word.tagtwo!!)
                    var expandTagsthree: String =
                        expandTags(word.tagthree!!)
                    var expandTagsfour: String =
                        expandTags(word.tagfour!!)
                    val tagtwo: String = word.tagtwo!!
                    val tagone: String = word.tagone!!
                    val tagthree: String = word.tagthree!!
                    val tagfour: String = word.tagfour!!
                    val tagnounone: Boolean =
                        (tagone == "N") || (tagone == "ADJ") || (tagone == "PN")
                    val tagnountwo: Boolean =
                        (tagtwo == "N") || (tagtwo == "ADJ") || (tagtwo == "PN")
                    val tagnounthree: Boolean =
                        (tagthree == "N") || (tagthree == "ADJ") || (tagthree == "PN")
                    val tagnounfour: Boolean =
                        (tagfour == "N") || (tagfour == "ADJ") || (tagfour == "PN")
                    if (tagnounone) {
                        expandTagsone = getNounDetails(word.detailsone)
                        "$expandTagsone "
                    } else if (tagnountwo) {
                        expandTagstwo = getNounDetails(word.detailstwo)
                        "$expandTagstwo "
                    } else if (tagnounthree) {
                        expandTagsthree = getNounDetails(word.detailsthree)
                        "$expandTagsthree "
                    } else if (tagnounfour) {
                        expandTagsfour = getNounDetails(word.detailsfour)
                    }
                    if ((word.tagone == "V")) {
                        expandTagsone = getVerbDetails(expandTagsone, word.detailsone!!)
                    } else if ((word.tagtwo == "V")) {
                        expandTagstwo = getVerbDetails(expandTagstwo, word.detailstwo!!)
                    } else if ((word.tagthree == "V")) {
                        expandTagsthree = getVerbDetails(expandTagsthree, word.detailsthree!!)
                    } else if ((word.tagfour == "V")) {
                        expandTagsfour = getVerbDetails(expandTagsfour, word.detailsfour!!)
                    }
                    sb.append(word.tagfour!!)
                    sb.append("|")
                    sb.append(word.tagthree!!)
                    sb.append("|")
                    sb.append(word.tagtwo)
                    sb.append("|")
                    sb.append(word.tagone)
                    spannableString = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!,
                        word.tagtwo!!,
                        word.tagthree!!,
                        word.tagfour!!,
                        word.tagfive!!,
                        " ",
                        expandTagsfour,
                        expandTagsthree,
                        expandTagstwo,
                        expandTagsone
                    )
                }

                5 -> {
                    val sb: StringBuilder = java.lang.StringBuilder()
                    var expandTagsone: String =
                        expandTags(word.tagone!!)
                    var expandTagstwo: String =
                        expandTags(word.tagtwo!!)
                    var expandTagsthree: String =
                        expandTags(word.tagthree!!)
                    var expandTagsfour: String =
                        expandTags(word.tagfour!!)
                    var expandTagsfive: String =
                        expandTags(word.tagfive!!)
                    if ((word.tagone == "V")) {
                        expandTagsone = getVerbDetails(expandTagsone, word.detailsone!!)
                    } else if ((word.tagtwo == "V")) {
                        expandTagstwo = getVerbDetails(expandTagstwo, word.detailstwo!!)
                    } else if ((word.tagthree == "V")) {
                        expandTagsthree = getVerbDetails(expandTagsthree, word.detailsthree!!)
                    } else if ((word.tagfour == "V")) {
                        expandTagsfour = getVerbDetails(expandTagsfour, word.detailsfour!!)
                    } else if ((word.tagfive == "V")) {
                        expandTagsfive = getVerbDetails(expandTagsfive, word.detailsfour!!)
                    }
                    sb.append(word.tagfour!!)
                    sb.append("|")
                    sb.append(word.tagthree!!)
                    sb.append("|")
                    sb.append(word.tagtwo!!)
                    sb.append("|")
                    sb.append(word.tagone!!)
                    spannableString = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!,
                        word.tagtwo!!,
                        word.tagthree!!,
                        word.tagfour!!,
                        word.tagfive!!,
                        expandTagsone,
                        expandTagstwo,
                        expandTagsthree,
                        expandTagsfour,
                        expandTagsfive
                    )
                    sb.append("|")
                    sb.append(word.tagfour!!)
                    sb.append("|")
                    sb.append(word.tagthree)
                    sb.append("|")
                    sb.append(word.tagtwo)
                    sb.append("|")
                    sb.append(word.tagone)
                }

            }
            return spannableString
        }

    private fun getNounDetails(wordDetails: String?): String {
        var expandedTag = ""
        val wordDetailMap = HashMap<String, SpannableStringBuilder?>()
        val stringBuilder = StringBuilder()

        corpusNounWords?.let {
            val arrayList = ArrayList(it)
            getNdetails(arrayList, wordDetailMap, stringBuilder) // Assuming this function exists
            val genderNumberDetails = getGenderNumberdetails(it[0].gendernumber).toString()
            wordDetailMap["noun"]?.let { nounDetails ->
                expandedTag += nounDetails.toString()
                expandedTag += "Root:-${it[0].root_a}"
            }
        }
        return expandedTag
    }

    private fun getVerbDetails(expandedTag: String, wordDetails: String): String {
        var result = expandedTag
        val split = wordDetails.split("|").filter { it.isNotEmpty() }
        val lastPart = split.last()

        if (!lastPart.contains("MOOD")) {
            result += getGenderNumberdetails(lastPart).toString()
            result = "$result(${expandVerbTense(word.detailsone!!)})"
            result += verbfeaturesenglisharabic.IND // Assuming this constant exists
        } else {
            val secondLastPart = split[split.size - 2]
            result += getGenderNumberdetails(secondLastPart).toString()
            result = "$result(${expandVerbTense(word.detailsone!!)})"
        }

        result += if (!lastPart.contains("PASS")) verbfeaturesenglisharabic.ACT else verbfeaturesenglisharabic.PASS // Assuming these constants exist
        verbCorpusRootWords?.let {
            result += "Root:-${it[0].root_a}"
        }
        return result
    }

    private fun expandVerbTense(verbDetails: String): String {
        var tense = when {
            verbDetails.contains("IMPF") -> verbfeaturesenglisharabic.IMPF
            verbDetails.contains("IMPV") -> verbfeaturesenglisharabic.IMPV
            verbDetails.contains("PERF") -> verbfeaturesenglisharabic.PERF
            else -> ""
        }
        tense += when {
            verbDetails.contains("SUBJ") -> verbfeaturesenglisharabic.SUBJ
            verbDetails.contains("JUS") -> verbfeaturesenglisharabic.JUS
            else -> ""
        }
        return tense
    }
}