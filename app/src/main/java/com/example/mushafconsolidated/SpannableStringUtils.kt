package com.example.mushafconsolidated

import android.graphics.Color
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import com.example.Constant
import com.example.Constant.FORESTGREEN
import com.example.Constant.GOLD
import com.example.Constant.KASHMIRIGREEN
import com.example.Constant.ORANGE400
import com.example.Constant.WHOTPINK
import com.example.Constant.harfinnaspanDark
import com.example.Constant.harfismspanDark
import com.example.Constant.harfkhabarspanDark
import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.data.Quadruple
import com.example.utility.CorpusUtilityorig.Companion.dark
import com.example.utility.QuranViewUtils.processString

object SpannableStringUtils {

    private lateinit var spannableString: SpannableString

    fun applySpans(
        negantionData: List<NegationEnt>,
        isNightmode: String
    ): List<Quadruple<Int, Int, String, SpannableString>> {
        //   val spannedStrings = mutableListOf<Triple<Int, String, SpannableString>>()
        val spannedStrings = mutableListOf<Quadruple<Int, Int, String, SpannableString>>()

        for (data in negantionData) {
            val surahid = data.surahid
            val ayahid = data.ayahid

            val arabicString = data.arabictext
            val englishString = data.englishtext
            var type = data.type
            var combinedString = ""
            //  type += " "
            if (type == "silaverify" || type == "anmasdarverify") {
                val combinedString = StringBuilder().apply {
                    append("(Not Verified-)")
                    append(" ")// Append "X" at the beginning
                    append(arabicString)
                    append("\n")
                    append(englishString)
                }.toString()
                spannableString = SpannableString(combinedString)

            }
           else if (type == "badal" || type == "mutlaq" || type == "tameez" || type == "ajlihi") {
                spannableString = SpannableString("$arabicString\n")
            } else {
                combinedString = "$arabicString\n$englishString"
                spannableString = SpannableString(combinedString)

            }

            dark = isNightmode == "dark" || isNightmode == "blue" || isNightmode == "green"

            if (dark) {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.BYELLOW)
                Constant.shartspanDark = ForegroundColorSpan(Constant.BCYAN)
                Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            } else {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.FORESTGREEN)
                Constant.shartspanDark = ForegroundColorSpan(Constant.WMIDNIHTBLUE)
                Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
            }
            if (dark) {
                Constant.harfinnaspanDark = ForegroundColorSpan(Color.GREEN)
                Constant.harfismspanDark = ForegroundColorSpan(Constant.BCYAN)
                Constant.harfkhabarspanDark = ForegroundColorSpan(Color.YELLOW)
            } else {
                Constant.harfinnaspanDark = ForegroundColorSpan(Constant.KASHMIRIGREEN)
                Constant.harfismspanDark = ForegroundColorSpan(Constant.prussianblue)
                Constant.harfkhabarspanDark =
                    ForegroundColorSpan(Constant.deepburnsienna)


            }



            if (dark) {
                Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
            } else {
                Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
            }

            val colorSpan = if (dark) {
                BackgroundColorSpan(Constant.MIDNIGHTBLUE) // Create a new BackgroundColorSpan instance
            } else {
                BackgroundColorSpan(Constant.GREENYELLOW) // Create a new BackgroundColorSpan instance
            }


            var harfkana: ForegroundColorSpan?
            var kanaism: ForegroundColorSpan?
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

            if (type == "badal" || type == "mutlaq" || type == "tameez" || type == "ajlihi") {

                spannableString.setSpan(
                    Constant.sifaspansDark,
                    0,
                    arabicString.length,
                    0
                )

            } else
                if (type == "sifa") {

                    spannableString.setSpan(
                        Constant.sifaspansDark,
                        0,
                        arabicString.length,
                        0
                    )
                } else if (type == "mudhaf") {
                    spannableString.setSpan(
                        colorSpan,
                        0,
                        arabicString.length,
                        0
                    )
                } else if (type == "khabarkanaism") {
                    val parts = arabicString.split(":")
                    val firstPartWords = parts[0]
                    var secondPartWords = parts[1]
                    var thirdPartWord = parts[2]
                    val finalString = firstPartWords + secondPartWords
                    spannableString.setSpan(
                        kanakhbar,
                        0,
                        firstPartWords.length,
                        0
                    )
                    spannableString.setSpan(
                        harfkana,
                        firstPartWords.length,
                        firstPartWords.length + secondPartWords.length,
                        0
                    )
                    spannableString.setSpan(
                        kanaism,
                        firstPartWords.length + secondPartWords.length,
                        arabicString.length + 2, // Use totalLength as the end index
                        0
                    )


                } else if (type == "kanaharfismlater" || type == "kadaismlater") {
                    val parts = arabicString.split(":")
                    val firstPartWords = parts[0].split(" ")


                    val result = processString(arabicString)

                    println("First part: ${result.first}")
                    println("Second part: ${result.second}")
                    spannableString.setSpan(
                        harfkana,
                        0,
                        result.first.length,
                        0
                    )
                    spannableString.setSpan(
                        kanakhbar,
                        result.first.length,
                        result.first.length + result.second.length,
                        0
                    )
                    spannableString.setSpan(
                        kanaism,
                        result.first.length + result.second.length,
                        arabicString.length + 2, // Use totalLength as the end index
                        0
                    )


                } else if (type == "kanatwo-twoism") {
                    val parts = arabicString.split(" ", limit = 3)

                    val split = arabicString.split(":")
                    //    if (split.size >= 2) {
                    val regex = Regex("\\.\\.+")
                    val firstWordt = split[0]
                    val firstWord = regex.replace(firstWordt, "")
                    val seconthirdstring = split[1]
                    val totalLength = firstWord.length + seconthirdstring.length

                    spannableString.setSpan(
                        Constant.harfshartspanDark,
                        0,
                        firstWord.length,
                        0
                    )
                    spannableString.setSpan(
                        Constant.shartspanDark,
                        firstWord.length,
                        firstWord.length + seconthirdstring.length,
                        0


                    )
                    try {
                        spannableString.setSpan(
                            Constant.jawabshartspanDark,
                            firstWord.length + seconthirdstring.length,
                            seconthirdstring.length + firstWord.length + totalLength,
                            0
                        )
                    } catch (e: IndexOutOfBoundsException) {
                        println(ayahid)
                        println(surahid)
                    }

                    //    val (firstWord, secondWord, thirdWord, restOfString) = arabicString.split(" ", limit = 3)
                    spannableString.setSpan(
                        harfkana,
                        0,
                        firstWord.length,
                        0
                    )
                    spannableString.setSpan(
                        kanaism,
                        firstWord.length,
                        firstWord.length + seconthirdstring.length,

                        0
                    )
                    spannableString.setSpan(
                        kanakhbar,
                        firstWord.length + seconthirdstring.length,
                        seconthirdstring.length + firstWord.length + totalLength,// Use totalLength as the end index
                        0
                    )

                } else if (type == "kanatwo" || type == "kadatwo") {
                    val parts = arabicString.split(" ", limit = 3)


                    val words = arabicString.split(
                        " ",
                        limit = 3
                    ) // Split with limit to avoid more than needed
                    val firstWord = words.getOrNull(0) ?: ""
                    val secondWord = words.getOrNull(1) ?: ""

                    val restOfString = words.drop(2).joinToString(" ")
                    val totalLength = firstWord.length + secondWord.length + restOfString.length

                    //    val (firstWord, secondWord, thirdWord, restOfString) = arabicString.split(" ", limit = 3)
                    spannableString.setSpan(
                        harfkana,
                        0,
                        firstWord.length,
                        0
                    )
                    spannableString.setSpan(
                        kanaism,
                        firstWord.length,
                        firstWord.length + secondWord.length,
                        0
                    )
                    spannableString.setSpan(
                        kanakhbar,
                        firstWord.length + secondWord.length,
                        totalLength + 2, // Use totalLength as the end index
                        0
                    )

                } else if (type == "sila" || type == "anmasdar") {

                        val combinedString = "$arabicString\n$englishString"
                        val spannableStrings = SpannableString(combinedString)


                        val (firstWord, restOfString) = arabicString.split(" ", limit = 2)



                        spannableString.setSpan(
                            harfkana,
                            0,
                            firstWord.length,
                            0
                        )

                        spannableString.setSpan(
                            kanakhbar,
                            firstWord.length,
                            arabicString.length,
                            0


                        )

                    }
                else if (type == "silaverify" || type == "anmasdarverify") {




                    val (firstWord, restOfString) = arabicString.split(" ", limit = 2)



                    spannableString.setSpan(
                        harfkana,
                        16,
                        16+firstWord.length,
                        0
                    )

                    spannableString.setSpan(
                        kanakhbar,
                        16+firstWord.length,
                        arabicString.length+16,
                        0


                    )

                }




                else  if (type == "kanaone" || type == "kadaone") {

                            val combinedString = "$arabicString\n$englishString"
                            val spannableStrings = SpannableString(combinedString)


                            val (firstWord, restOfString) = arabicString.split(" ", limit = 2)



                            spannableString.setSpan(
                                harfkana,
                                0,
                                firstWord.length,
                                0
                            )

                            spannableString.setSpan(
                                kanakhbar,
                                firstWord.length,
                                arabicString.length,
                                0


                            )

                        } else


                            if (type == "shartnojawab") {
                                val combinedString = "$arabicString\n$englishString"
                                val spannableStrings = SpannableString(combinedString)
                                val regex = Regex(":")
                                val (firstWord, restOfString) = arabicString.split(" ", limit = 2)

                                spannableString.setSpan(
                                    Constant.harfshartspanDark,
                                    0,
                                    firstWord.length,
                                    0
                                )

                                spannableString.setSpan(
                                    Constant.shartspanDark,
                                    firstWord.length,
                                    arabicString.length,
                                    0


                                )

                            } else
                                if (type == "shartonly") {
                                    val combinedString = "$arabicString\n$englishString"
                                    val spannableStrings = SpannableString(combinedString)
                                    val regex = Regex(":")
                                    val (firstWord, restOfString) = arabicString.split(
                                        " ",
                                        limit = 2
                                    )

                                    spannableString.setSpan(
                                        Constant.harfshartspanDark,
                                        0,
                                        firstWord.length,
                                        0
                                    )

                                    spannableString.setSpan(
                                        Constant.shartspanDark,
                                        firstWord.length,
                                        arabicString.length,
                                        0


                                    )
                                } else if (type == "shartall") {
                                    val split = arabicString.split(":")
                                    //    if (split.size >= 2) {
                                    val regex = Regex("\\.\\.+")
                                    val firstWordt = split[0]
                                    val firstWord = regex.replace(firstWordt, "")
                                    val seconthirdstring = split[1]
                                    val totalLength = firstWord.length + seconthirdstring.length

                                    spannableString.setSpan(
                                        Constant.harfshartspanDark,
                                        0,
                                        firstWord.length,
                                        0
                                    )
                                    spannableString.setSpan(
                                        Constant.shartspanDark,
                                        firstWord.length,
                                        firstWord.length + seconthirdstring.length,
                                        0


                                    )
                                    try {
                                        spannableString.setSpan(
                                            Constant.jawabshartspanDark,
                                            firstWord.length + seconthirdstring.length,
                                            seconthirdstring.length + firstWord.length + totalLength,
                                            0
                                        )
                                    } catch (e: IndexOutOfBoundsException) {
                                        println(ayahid)
                                        println(surahid)
                                    }

                                    //  }

                                } else if (type == "nasabone") {

                                    val (firstWord, restOfString) = arabicString.split(
                                        " ",
                                        limit = 2
                                    )
                                    spannableString.setSpan(
                                        Constant.harfinnaspanDark,
                                        0,
                                        firstWord.length,
                                        0
                                    )

                                    spannableString.setSpan(
                                        Constant.harfkhabarspanDark,
                                        firstWord.length,
                                        arabicString.length,
                                        0


                                    )

                                } else if (type == "nasabtwo") {
                                    try {
                                        val words = arabicString.split(
                                            " ",
                                            limit = 3
                                        ) // Split with limit to avoid more than needed
                                        val firstWord = words.getOrNull(0) ?: ""
                                        val secondWord = words.getOrNull(1) ?: ""

                                        val restOfString = words.drop(2).joinToString(" ")
                                        val totalLength =
                                            firstWord.length + secondWord.length + restOfString.length

                                        //    val (firstWord, secondWord, thirdWord, restOfString) = arabicString.split(" ", limit = 3)
                                        spannableString.setSpan(
                                            Constant.harfinnaspanDark,
                                            0,
                                            firstWord.length,
                                            0
                                        )
                                        spannableString.setSpan(
                                            Constant.harfismspanDark,
                                            firstWord.length,
                                            firstWord.length + secondWord.length,
                                            0
                                        )
                                        spannableString.setSpan(
                                            harfkhabarspanDark,
                                            firstWord.length + secondWord.length,
                                            totalLength + 2, // Use totalLength as the end index
                                            0
                                        )
                                    } catch (e: IndexOutOfBoundsException) {

                                        println("check")
                                    }


                                } else if (type == "nasabtwoismlater") {
                                    val parts = arabicString.split(":")
                                    val firstPartWords = parts[0].split(" ")


                                    val result = processString(arabicString)

                                    println("First part: ${result.first}")
                                    println("Second part: ${result.second}")
                                    spannableString.setSpan(
                                        harfinnaspanDark,
                                        0,
                                        result.first.length,
                                        0
                                    )
                                    spannableString.setSpan(
                                        harfkhabarspanDark,
                                        result.first.length,
                                        result.first.length + result.second.length,
                                        0
                                    )
                                    spannableString.setSpan(
                                        harfismspanDark,
                                        result.first.length + result.second.length,
                                        arabicString.length + 2, // Use totalLength as the end index
                                        0
                                    )


                                } else if (type == "nasabtwo-twoism") {
                                    val parts = arabicString.split(" ", limit = 4)


                                    // Get the first word and the rest of the string
                                    val firstWord = parts.getOrNull(0) ?: ""
                                    val secondWord = parts.getOrNull(1) ?: ""
                                    val thirdWord = parts.getOrNull(2) ?: ""
                                    val restOfString = parts.getOrNull(3) ?: ""
                                    val seconthirdstring = secondWord + " " + thirdWord
                                    val totalLength =
                                        firstWord.length + seconthirdstring.length + restOfString.length

                                    spannableString.setSpan(
                                        Constant.harfinnaspanDark,
                                        0,
                                        firstWord.length,
                                        0
                                    )
                                    spannableString.setSpan(
                                        Constant.harfismspanDark,
                                        seconthirdstring.length,
                                        firstWord.length + totalLength,
                                        0


                                    )
                                    spannableString.setSpan(
                                        Constant.harfkhabarspanDark,
                                        firstWord.length,
                                        seconthirdstring.length + firstWord.length,
                                        0
                                    )


                                } else {

                                    //     spannableString.setSpan( UnderlineSpan(), 0, type.length, 0)
                                    spannableString.setSpan(
                                        harfinnaspanDark,
                                        0,
                                        arabicString.length,
                                        0
                                    ) // Span for Arabic
                                    spannableString.setSpan(
                                        harfkhabarspanDark,
                                        arabicString.length + 1,
                                        combinedString.length,
                                        0
                                    )

                                }
            var phrasetype = ""
            if (type.contains("kana")) {
                phrasetype = "kana"
            } else if (type.contains("nasab")) {

                phrasetype = "nasab"
            } else if (type.contains("shart")) {
                phrasetype = "shart"
            } else if (type.contains("present")) {
                phrasetype = "present"
            } else if (type.contains("past")) {
                phrasetype = "past"
            } else if (type.contains("future")) {
                phrasetype = "future"
            } else if (type.contains("exp")) {
                phrasetype = "exp"
            } else if (type.contains("sifa")) {
                phrasetype = "sifa"
            } else if (type.contains("mudhaf")) {
                phrasetype = "mudhaf"
            }


            spannedStrings.add(Quadruple(data.surahid, data.ayahid, phrasetype, spannableString))
        }





        return spannedStrings
    }
}