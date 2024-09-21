package com.quiz


data class Question(
    val id: Int,
    val question: String,
    val tense: Tense,
    val voice: Voice,
    val mood: Mood,
    val genderNumber: GenderNumber,
    val pattern: Pattern,
    val correctAnswer: String
) {

    enum class Tense {
        Perfect, Imperfect, Imperative
    }

    enum class Voice {
        Active, Passive
    }

    enum class Mood {
        Subjunctive, Jussive, Indicative
    }

    enum class GenderNumber {
        ThirdPersonSingularMasculine,
        ThirdPersonDualMasculine,
        ThirdPersonPluralMasculine,

        ThirdPersonSingularFeminine,
        ThirdPesonDualFeminine,
        ThirdPersonPluralFeminine,


        SecondPersonSingularMasculine,
        SecondPersonDualMasculine,
        SecondPersonPlularMasculine,
        SecondPersonSingularFeminine,
        SecondPersonDualFeminine,
        SecondPersonPluralFeminine,

        FirstPersonSingula,
        FirstPersonPlural,
        // Add your 13 options here

        // ... other options
    }

    enum class Pattern(val value: Int) {
        Pattern1(1), Pattern2(2), Pattern3(3), Pattern4(4), Pattern5(5),
        Pattern6(6), Pattern7(7), Pattern8(8), Pattern9(9)
    }
}