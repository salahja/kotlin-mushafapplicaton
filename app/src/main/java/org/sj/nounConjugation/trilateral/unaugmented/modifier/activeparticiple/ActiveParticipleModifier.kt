package org.sj.nounConjugation.trilateral.unaugmented.modifier.activeparticiple

import org.sj.nounConjugation.NounLamAlefModifier
import org.sj.nounConjugation.NounSunLamModifier
import org.sj.nounConjugation.trilateral.unaugmented.modifier.ConjugationResult
import org.sj.nounConjugation.trilateral.unaugmented.modifier.IUnaugmentedTrilateralNounModifier
import org.sj.verbConjugation.FaelMafool
import org.sj.verbConjugation.trilateral.unaugmented.UnaugmentedTrilateralRoot


/**
 *
 * Title: Sarf Program
 *
 *
 * Description: تطبيق المعالجة الخاصة على اسم الفاعل
 * ابتداء بالادغام ثم الاعلال واخيرا الهمزة
 *
 *
 *
 * Copyright: Copyright (c) 2006
 *
 *
 * Company: ALEXO
 *
 * @author Haytham Mohtasseb Billah
 * @version 1.0
 */
class ActiveParticipleModifier private constructor() : IUnaugmentedTrilateralNounModifier {
    private val geminator = Geminator()
    private val vocalizer = Vocalizer()
    private val mahmouz = Mahmouz()

    override fun build(
        root: UnaugmentedTrilateralRoot,
        kov: Int,
        conjugations: List<*>,
        formula: String
    ): ConjugationResult {
        val conjResult = ConjugationResult(kov, root, conjugations as List<*>, formula)

        // Apply various modifications
        if (geminator.isApplied(conjResult)) {
            geminator.apply(conjResult.finalResult as MutableList<Any>, root)
        }
        vocalizer.apply(conjResult)
        mahmouz.apply(conjResult)

        // Apply other noun modifiers
        NounLamAlefModifier.instance.apply(conjResult)
        NounSunLamModifier.instance.apply(conjResult)

        // Process final result string
        val splitResults = processConjugationResult(conjResult.finalResult.toString())

        // Safely assign values to the `FaelMafool` object
        assignFaelMafool(conjResult.faelMafool, splitResults)

        return conjResult
    }

    // Helper method to safely process the conjugation result string
    private fun processConjugationResult(resultString: String): List<String> {
        return resultString
            .replace("[", "") // Remove any brackets if present
            .replace("]", "")
            .split(",")
            .map { it.trim() } // Ensure trimming spaces
    }

    // Helper method to assign conjugation results to `FaelMafool`
    private fun assignFaelMafool(faelMafool: FaelMafool, results: List<String>) {
        try {
            faelMafool.nomsinM = results.getOrNull(0) ?: ""
            faelMafool.nomdualM = results.getOrNull(2) ?: ""
            faelMafool.nomplurarM = results.getOrNull(4) ?: ""
            faelMafool.accsinM = results.getOrNull(6) ?: ""
            faelMafool.accdualM = results.getOrNull(8) ?: ""
            faelMafool.accplurarlM = results.getOrNull(10) ?: ""
            faelMafool.gensinM = results.getOrNull(12) ?: ""
            faelMafool.gendualM = results.getOrNull(14) ?: ""
            faelMafool.genplurarM = results.getOrNull(16) ?: ""

            faelMafool.nomsinF = results.getOrNull(1) ?: ""
            faelMafool.nomdualF = results.getOrNull(3) ?: ""
            faelMafool.nomplurarF = results.getOrNull(5) ?: ""
            faelMafool.accsinF = results.getOrNull(7) ?: ""
            faelMafool.accdualF = results.getOrNull(9) ?: ""
            faelMafool.accplurarlF = results.getOrNull(11) ?: ""
            faelMafool.gensinF = results.getOrNull(13) ?: ""
            faelMafool.gendualF = results.getOrNull(15) ?: ""
            faelMafool.genplurarF = results.getOrNull(17) ?: ""
        } catch (e: Exception) {
            // Optionally log the error for debugging purposes
            println("Error assigning FaelMafool: ${e.message}")
        }
    }

    companion object {
        val instance = ActiveParticipleModifier()
    }
}

/*class ActiveParticipleModifier private constructor() : IUnaugmentedTrilateralNounModifier {
    private val geminator = Geminator()
    private val vocalizer = Vocalizer()
    private val mahmouz = Mahmouz()
    override fun build(
        root: UnaugmentedTrilateralRoot,
        kov: Int,
        conjugations: List<*>,
        formula: String

    ): ConjugationResult {
        val conjResult = ConjugationResult(kov, root!!, conjugations as List<*>, formula)
        if (geminator.isApplied(conjResult)) geminator.apply(conjResult.finalResult as MutableList<Any>, root!!)
        vocalizer.apply(conjResult)
        mahmouz.apply(conjResult)

        NounLamAlefModifier.instance.apply(conjResult)
        NounSunLamModifier.instance.apply(conjResult)



            val toString = conjResult.finalResult.toString()
            val split = toString.split(",")
            conjResult.faelMafool.nomsinM = split[0]//sinM
            conjResult.faelMafool.nomdualM = split[2]//dualM
            conjResult.faelMafool.nomplurarM = split[4]//plurarM
            conjResult.faelMafool.accsinM = split[6]//sinM
            conjResult.faelMafool.accdualM = split[8]//dualM
            conjResult.faelMafool.accplurarlM = split[10]//plurarlM
            conjResult.faelMafool.gensinM = split[12]//sinM
            conjResult.faelMafool.gendualM = split[14]//dualM
            conjResult.faelMafool.genplurarM = split[16]//plurarM
            conjResult.faelMafool.nomsinF = split[1]//sinF
            conjResult.faelMafool.nomdualF = split[3]//dualF
            conjResult.faelMafool.nomplurarF = split[5]//plurarF
            conjResult.faelMafool.accsinF = split[7]//sinF
            conjResult.faelMafool.accdualF = split[9]//dualF
            conjResult.faelMafool.accplurarlF = split[11]//plurarlF
            conjResult.faelMafool.gensinF = split[13]//sinF
            conjResult.faelMafool.gendualF = split[15]//dualF
            conjResult.faelMafool.genplurarF = split[17]//plurarF



        return conjResult
    }

    companion object {
        val instance = ActiveParticipleModifier()
    }


}*/
