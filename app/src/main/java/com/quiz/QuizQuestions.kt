package com.quiz


data class TenseQuestion(
    val id: Int,
    val tense: TenseData,

)
data class VoiceQuestion(
    val id: Int,
    val voice: VoiceData,

)
data class MoodQuestion(
    val id: Int,
    val mood: MoodData,

    )

data class GenderNumberQuestion(
    val id: Int,
    val genderNumber: GenderNumberData,

    )
data class PatternQuestion(
    val id: Int,
    val pattern: PatternData

    )



data class TenseData(
    val question: String = "What is the tense of this verb?",
    val options: List<String> = listOf("Perfect", "Imperfect", "Imperative"),

) {
    enum class Tense {
        PERF, IMPF, IMPV
    }
}

data class VoiceData(
    val question: String = "What is the voice of this verb?",
    val options: List<String> = listOf("Active", "Passive"),

) {
    enum class Voice {
        ACTI, PASS
    }
}

data class MoodData(
    val question: String = "What is the mood of this verb?",
    val options: List<String> = listOf("Subjunctive", "Jussive", "Indicative"),

) {
    enum class Mood {
        SUB, JUS, IND
    }
}

data class GenderNumberData(
    val question: String = "What is the gender and number of this verb?",
    val options: List<String> = listOf(
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
        "First Person Singular Masculine",
        // ... other 12 options
    ),

) {
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
        FirstPersonPlural, FirstPersonSingularMasculine,
        // ... other 12 options
    }
}

data class PatternData(
    val question: String = "What is the pattern of this verb?",
    val options: List<String> = listOf(
        "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4", "Pattern 5",
        "Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9"
    ),

) {
    enum class Pattern(val value: Int) {
        Pattern1(1), Pattern2(2), Pattern3(3), Pattern4(4), Pattern5(5),
        Pattern6(6), Pattern7(7), Pattern8(8), Pattern9(9)
    }
}