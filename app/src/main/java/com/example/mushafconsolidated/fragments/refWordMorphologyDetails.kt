package com.example.mushafconsolidated.fragments


import android.text.SpannableString
import android.text.SpannableStringBuilder
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.utility.CorpusConstants.verbfeaturesenglisharabic
import com.example.utility.CorpusUtilityorig


class refWordMorphologyDetails(
    private var word: CorpusEntity,
    corpusNounWord: List<*>,
    verbCorpusRootWord: List<*>
) : QuranMorphologyDetails() {
    private var verbCorpusRootWord: ArrayList<VerbCorpus>? = null
    private var corpusNounWord: ArrayList<NounCorpus>? = null

    init {
        this.corpusNounWord = corpusNounWord as ArrayList<NounCorpus>?
        this.verbCorpusRootWord = verbCorpusRootWord as ArrayList<VerbCorpus>?
    }

    // --Commented out by Inspection START (13/11/22, 3:02 PM):
    //    public WordMorphologyDetails() {
    //    }
    // --Commented out by Inspection STOP (13/11/22, 3:02 PM)
    /*

     */


    val workBreakDown: SpannableString?
        get() {
            //   SpannableString  tagspannable = null;
            var tagspannable: SpannableString? = null
            when (word.wordcount) {
                1 -> {
                    val tagone: String = word.tagone!!
                    var expandTagsone: String = expandTags(tagone)
                    if ((tagone == "N")) {
                        expandTagsone = getNounDetails(word.detailsone)
                    }
                    if ((tagone == "V")) {
                        expandTagsone = getVerbDetails(expandTagsone, word.detailsone!!)
                    }
                    tagspannable = SpannableString(expandTagsone)
                    //   tagspannable.setSpan(new ForegroundColorSpan(CYAN), 0, expandTagsone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tagspannable = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!, word.tagtwo!!, word.tagthree!!,
                        word.tagfour!!, word.tagfive!!, " ", " ", "", "", expandTagsone
                    )
                }

                2 -> {
                    val tagtwo: String = word.tagtwo!!
                    val tagone: String = word.tagone!!
                    var expandTagsone: String = expandTags(tagone)
                    val tagnounone: Boolean =
                        (tagone == "N") || (tagone == "ADJ") || (tagone == "PN")
                    val tagnountwo: Boolean =
                        (tagtwo == "N") || (tagtwo == "ADJ") || (tagtwo == "PN")
                    var expandTagstwo: String = expandTags(tagtwo)
                    if (tagnounone) {
                        expandTagsone = getNounDetails(word.detailsone)
                    } else if (tagnountwo) {
                        expandTagstwo = getNounDetails(word.detailstwo)
                    }
                    if ((tagone == "V")) {
                        expandTagsone = getVerbDetails(expandTagsone, word.detailsone!!)
                    } else if ((tagtwo == "V")) {
                        expandTagstwo = getVerbDetails(expandTagstwo, word.detailstwo!!)
                    }
                    tagspannable = CorpusUtilityorig.NewSetWordSpanTag(
                        word.tagone!!, word.tagtwo!!, word.tagthree!!,
                        word.tagfour!!, word.tagfive!!, " ", " ", "", expandTagstwo, expandTagsone
                    )
                }

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
                    tagspannable = CorpusUtilityorig.NewSetWordSpanTag(
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
                    tagspannable = CorpusUtilityorig.NewSetWordSpanTag(
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
                    tagspannable = CorpusUtilityorig.NewSetWordSpanTag(
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
            return tagspannable
        }

    private fun getNounDetails(wordDetails: String?): String {
        var expandedTag = ""
        val wordDetailMap: HashMap<String, SpannableStringBuilder?> = HashMap()
        val stringBuilder = StringBuilder()

        corpusNounWord?.let {
            getNdetails(it, wordDetailMap, stringBuilder)
            val genderNumberDetails = getGenderNumberdetails(it[0].gendernumber).toString()
            if (wordDetailMap["noun"] != null) {
                expandedTag += wordDetailMap["noun"].toString()
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
            result += verbfeaturesenglisharabic.IND
        } else {
            val secondLastPart = split[split.size - 2]
            result += getGenderNumberdetails(secondLastPart).toString()
            result = "$result(${expandVerbTense(word.detailsone!!)})"
        }

        result += if (!lastPart.contains("PASS")) verbfeaturesenglisharabic.ACT else verbfeaturesenglisharabic.PASS
        verbCorpusRootWord?.let {
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