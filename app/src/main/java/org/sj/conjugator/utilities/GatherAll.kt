package org.sj.conjugator.utilities



import VerbDetails
import org.sj.conjugator.activity.SystemConstants
import org.sj.data.MazeedResult
import org.sj.data.MujarradResult
import org.sj.nounConjugation.trilateral.augmented.AugmentedTrilateralActiveParticipleConjugator
import org.sj.nounConjugation.trilateral.augmented.AugmentedTrilateralPassiveParticipleConjugator
import org.sj.nounConjugation.trilateral.unaugmented.UnaugmentedTrilateralActiveParticipleConjugator
import org.sj.nounConjugation.trilateral.unaugmented.UnaugmentedTrilateralPassiveParticipleConjugator
import org.sj.nounConjugation.trilateral.unaugmented.instrumental.StandardInstrumentalConjugator
import org.sj.nounConjugation.trilateral.unaugmented.modifier.activeparticiple.ActiveParticipleModifier
import org.sj.nounConjugation.trilateral.unaugmented.modifier.instrumental.InstrumentalModifier
import org.sj.nounConjugation.trilateral.unaugmented.modifier.passiveparticiple.PassiveParticipleModifier
import org.sj.nounConjugation.trilateral.unaugmented.modifier.timeandplace.TimeAndPlaceModifier
import org.sj.nounConjugation.trilateral.unaugmented.timeandplace.TimeAndPlaceConjugator
import org.sj.verbConjugation.Amr
import org.sj.verbConjugation.FaelMafool
import org.sj.verbConjugation.IsmAlaMifaalun
import org.sj.verbConjugation.IsmAlaMifalatun
import org.sj.verbConjugation.IsmAlaMifalun
import org.sj.verbConjugation.IsmZarfMafalatun
import org.sj.verbConjugation.IsmZarfMafalun
import org.sj.verbConjugation.IsmZarfMafilun
import org.sj.verbConjugation.MadhiMudharay
import org.sj.verbConjugation.NahiAmr
import org.sj.verbConjugation.trilateral.augmented.AugmentedPastVerb
import org.sj.verbConjugation.trilateral.augmented.AugmentedPresentVerb


import org.sj.verbConjugation.trilateral.augmented.AugmentedTrilateralRoot
import org.sj.verbConjugation.trilateral.augmented.active.past.AugmentedActivePastConjugator
import org.sj.verbConjugation.trilateral.augmented.active.present.AugmentedActivePresentConjugator
import org.sj.verbConjugation.trilateral.augmented.imperative.AugmentedImperativeConjugator
import org.sj.verbConjugation.trilateral.augmented.imperative.AugmentedImperativeVerb
import org.sj.verbConjugation.trilateral.augmented.modifier.AugmentedTrilateralModifier
import org.sj.verbConjugation.trilateral.augmented.passive.past.AugmentedPassivePastConjugator
import org.sj.verbConjugation.trilateral.augmented.passive.present.AugmentedPassivePresentConjugator
import org.sj.verbConjugation.trilateral.unaugmented.ConjugationResult
import org.sj.verbConjugation.trilateral.unaugmented.UnaugmentedImperativeConjugator
import org.sj.verbConjugation.trilateral.unaugmented.UnaugmentedTrilateralRoot
import org.sj.verbConjugation.trilateral.unaugmented.active.ActivePastConjugator
import org.sj.verbConjugation.trilateral.unaugmented.active.ActivePresentConjugator
import org.sj.verbConjugation.trilateral.unaugmented.modifier.UnaugmentedTrilateralModifier
import org.sj.verbConjugation.trilateral.unaugmented.passive.PassivePastConjugator
import org.sj.verbConjugation.trilateral.unaugmented.passive.PassivePresentConjugator
import org.sj.verbConjugation.util.KovRulesManager
import org.sj.verbConjugation.util.OSarfDictionary
import org.sj.verbConjugation.util.TrilateralKovRule

class GatherAll {
    fun getMujarradListing(
        verbmood: String?,
        verbroot: String?,
        unaugmentedFormula: String,
                          ): MujarradResult? {
        return buildUnaugmentedLists(verbmood, verbroot, unaugmentedFormula)
    }

    fun getMujarradListing(verbmood: String, verbroot: String): ArrayList<ArrayList<*>> {
        return buildUnaugmentedLists(verbmood, verbroot)
        /*
          ArrayList<ArrayList> skabeer = buildUnaugmentedLists(verbmood, verbroot);
        return skabeer;
         */
    }

    fun getMazeedListing(verbmood: String, verbroot: String): MazeedResult? {
        return buildMazeedList(verbmood, verbroot)
    }
   /* fun getMazeedListingorss(verbmood: String, verbroot: String): ArrayList<ArrayList<*>> {
        return buildMazeedList(verbmood, verbroot)
    }*/
    fun getMujarradParticiple(
        verbroot: String?,
        unaugmentedFormula: String?,
                             ): ArrayList<ArrayList<*>> {
        return buildMujarradParticipleList(verbroot!!, unaugmentedFormula!!)
    }

    fun getMujarradIsmAla(verbroot: String?, unaugmentedFormula: String?): ArrayList<ArrayList<*>> {
        return buildUnAugmentedNounofInstrument(verbroot!!, unaugmentedFormula!!)
    }

    fun getMujarradZarf(verbroot: String, unaugmentedFormula: String): ArrayList<ArrayList<*>> {
        return buildUnAugmentedNounofTimeAndPlace(verbroot, unaugmentedFormula)
    }

    private fun buildUnAugmentedNounofTimeAndPlace(
        verbRoot: String,
        unaugmentedFormula: String,
                                                  ): ArrayList<ArrayList<*>> {
        val skabeer = ArrayList<ArrayList<*>>()
        val firstCharacter = verbRoot[0]
        val secondCharacter = verbRoot[1]
        val thirdCharacter = verbRoot[2]
        val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)


        val ismZarfMafilunarr = ArrayList<IsmZarfMafilun>()
        val ismzarfmafalatunarr = ArrayList<IsmZarfMafalatun>()
        val ismzarfmafalunarr = ArrayList<IsmZarfMafalun>()
        val unaugmentedTrilateralRoot =
            OSarfDictionary.instance.getUnaugmentedTrilateralRoots(verbRoot, unaugmentedFormula)
        /*
    "A" -> "مَفْعَل"
"B" -> "مَفْعِل"
"C" -> "مَفْعَلَة"
     */if (unaugmentedTrilateralRoot != null) {

            val zarfConjugator = TimeAndPlaceConjugator.instance
            val zarfModifier = TimeAndPlaceModifier.instance
            val mafalConjugationResult =
                zarfConjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعَل")
            val mafal = zarfModifier.build(
                unaugmentedTrilateralRoot,
                rule!!.kov,
                mafalConjugationResult,
                "مَفْعَل"
                                          )
            val zarfFinalMafal = mafal.finalResult
            val mafilconjugationlist =
                zarfConjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعِل")
            val mafil = zarfModifier.build(
                unaugmentedTrilateralRoot,
                rule.kov,
                mafilconjugationlist,
                "مَفْعِل"
                                          )
            val zarfFinalMafil = mafil.finalResult
            val mafalatunCOnjugationList =
                zarfConjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعَلَة")
            val mafalatun = zarfModifier.build(
                unaugmentedTrilateralRoot,
                rule.kov,
                mafalatunCOnjugationList,
                "مَفْعَلَة"
                                              )
            val zarffinalmafalatun = mafalatun.finalResult
            val zarfmafal: MutableList<String> = ArrayList()
            val zarfmafil: MutableList<String> = ArrayList()
            val zarfmafalatun: MutableList<String> = ArrayList()
            for (s in zarfFinalMafal) {
                if (s.toString().isNotEmpty()) {
                    zarfmafal.add(s.toString())
                }
            }
            for (s in zarfFinalMafil) {
                if (s.toString().isNotEmpty()) {
                    zarfmafil.add(s.toString())
                }
            }
            for (s in zarffinalmafalatun) {
                if (s.toString().isNotEmpty()) {
                    zarfmafalatun.add(s.toString())
                }
            }


            ismZarfMafilunarr.add( mafil.zarfMafilun)
            ismzarfmafalatunarr.add(  mafalatun.zarfMafalatun)
            ismzarfmafalunarr.add( mafal.zarfMafalun)
            skabeer.add(ismZarfMafilunarr)
            skabeer.add( ismzarfmafalatunarr )
            skabeer.add(ismzarfmafalunarr)
            return skabeer
        }
        return skabeer
    }

    private fun buildUnAugmentedNounofInstrument(
        verbroot: String,
        unaugmentedFormula: String,
                                                ): ArrayList<ArrayList<*>> {
        val skabeer = ArrayList<ArrayList<*>>()
        val firstCharacter = verbroot[0]
        val secondCharacter = verbroot[1]
        val thirdCharacter = verbroot[2]
        val ismalamifalunarr = ArrayList<IsmAlaMifalun>()
        val ismalamifalatunarr= ArrayList<IsmAlaMifalatun>()
        val ismaalamifaalunarr = ArrayList<IsmAlaMifaalun>()

        val alamifal :IsmAlaMifalun
        val alaMifalatun:IsmAlaMifalatun

        val alaMifaalun:IsmAlaMifaalun

        val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)
        val unaugmentedTrilateralRoot =
            OSarfDictionary.instance.getUnaugmentedTrilateralRoots(verbroot, unaugmentedFormula)
        if (unaugmentedTrilateralRoot != null) {
            val conjugator = StandardInstrumentalConjugator.instance
            val modifier = InstrumentalModifier.instance
            val mifal = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَل")
            val mifalunResult: ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule!!.kov, mifal, "مِفْعَل")

            val mifalatun = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَلَة")
            val mifalatunResult: ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule.kov, mifalatun, "مِفْعَلَة")

            val mifaal = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَال")
            val MifaalResult            : ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule.kov, mifaal, "مِفْعَال")

            val faalatun = conjugator.createNounList(unaugmentedTrilateralRoot, "فَعَّالَة")
            val conjResultfaalatun: ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule.kov, faalatun, "فَعَّالَة")
            val alamifal: MutableList<String> = ArrayList()
            val alamifalatun: MutableList<String> = ArrayList()
            val alamifaal: MutableList<String> = ArrayList()

            val ismfalamifalun = mifalunResult.alaMifalun
            val ismfalamifalatun = mifalatunResult.alaMifalatun
            val ismfalamifaaal = MifaalResult.alaMifaalun
            ismalamifalunarr.add(ismfalamifalun)
            ismalamifalatunarr.add(ismfalamifalatun)
            ismaalamifaalunarr.add(ismfalamifaaal)
            skabeer.add(ismalamifalunarr)
            skabeer.add(ismalamifalatunarr)
            skabeer.add(ismaalamifaalunarr)
            return skabeer
        }
        return skabeer
    }

    private fun buildMujarradParticipleList(
        verbroot: String,
        unaugmentedFormula: String,
                                           ): ArrayList<ArrayList<*>> {

        val firstCharacter = verbroot[0]
        val secondCharacter = verbroot[1]
        val thirdCharacter = verbroot[2]

        val finalList = ArrayList<ArrayList<*>>()
        val arrayofFaelMafool = ArrayList<FaelMafool>()
        val faelObj: FaelMafool
        val mafoolObj: FaelMafool

        val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)
        val unaugmentedTrilateralRoot =
            OSarfDictionary.instance.getUnaugmentedTrilateralRoots(verbroot, unaugmentedFormula)
        //ismfale and ismMafool
        val conjugatedIsmFael =
            UnaugmentedTrilateralActiveParticipleConjugator.instance.createNounList(
                unaugmentedTrilateralRoot,
                unaugmentedTrilateralRoot.conjugation!!
                                                                                   )
        val conjugationResult = ActiveParticipleModifier.instance
            .build(unaugmentedTrilateralRoot, rule!!.kov, conjugatedIsmFael, "")
        val conjugatedIsmMafool =
            UnaugmentedTrilateralPassiveParticipleConjugator.instance.createNounList(
                unaugmentedTrilateralRoot,
                unaugmentedTrilateralRoot.conjugation!!
                                                                                    )
        val ismmafoolresult = PassiveParticipleModifier.instance
            .build(unaugmentedTrilateralRoot, rule.kov, conjugatedIsmMafool, "")


        faelObj = conjugationResult.faelMafool
        mafoolObj = ismmafoolresult.faelMafool
        arrayofFaelMafool.add(faelObj)
        arrayofFaelMafool.add(mafoolObj)
        finalList.add(arrayofFaelMafool)
        val vdetails: MutableList<String> = ArrayList()
        vdetails.add(rule.desc.toString())
        vdetails.add(unaugmentedTrilateralRoot.conjugationname!!)





        return finalList//buildMujarradParticipleList



    }



    fun getMazeedListing(
        verbmood: String?,
        verbroot: String?,
        augmentedFormula: String?,
                        ): MazeedResult? {
        return buildMazeedList(verbmood!!, verbroot!!, augmentedFormula!!)
    }

    private fun buildUnaugmentedLists(verbmood: String, verbroot: String): ArrayList<ArrayList<*>> {
        val skabeer = ArrayList<ArrayList<*>>()
        val firstCharacter = verbroot[0]
        val secondCharacter = verbroot[1]
        val thirdCharacter = verbroot[2]

        var madhimajhool: List<*>
        var mudharay: List<*>? = null
        var amr: MutableList<*>
        var nahiamr: List<*>
        var madhi: List<*>
        var mudharaymajhool: List<*>? = null
        val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)
        val unaugmentedTrilateralRoot =
            OSarfDictionary.instance.getUnaugmentedTrilateralRoots(verbroot)
        if (unaugmentedTrilateralRoot != null) {
            //   madhi = org.sj.verb.trilateral.unaugmented.active.ActivePastConjugator.instance.createVerbList((UnaugmentedTrilateralRoot) unaugmentedTrilateralRoot);
            madhi = ActivePastConjugator.instance.createVerbList(unaugmentedTrilateralRoot)
            madhimajhool =
                PassivePastConjugator.instance.createVerbList(unaugmentedTrilateralRoot)
            when (verbmood) {
                "Indicative" -> {
                    mudharay = ActivePresentConjugator.instance
                        .createNominativeVerbList(unaugmentedTrilateralRoot)
                    mudharaymajhool = PassivePresentConjugator.instance
                        .createNominativeVerbList(unaugmentedTrilateralRoot)
                }

                "Subjunctive" -> {
                    mudharay = ActivePresentConjugator.instance
                        .createAccusativeVerbList(unaugmentedTrilateralRoot)
                    mudharaymajhool = PassivePresentConjugator.instance
                        .createAccusativeVerbList(unaugmentedTrilateralRoot)
                }

                "Jussive" -> {
                    mudharay = ActivePresentConjugator.instance
                        .createJussiveVerbList(unaugmentedTrilateralRoot)
                    mudharaymajhool = PassivePresentConjugator.instance
                        .createJussiveVerbList(unaugmentedTrilateralRoot)
                }

                "Emphasized" -> {
                    mudharay = ActivePresentConjugator.instance
                        .createEmphasizedVerbList(unaugmentedTrilateralRoot)
                    mudharaymajhool = PassivePresentConjugator.instance
                        .createEmphasizedVerbList(unaugmentedTrilateralRoot)
                }
            }
            nahiamr = ActivePresentConjugator.instance
                .createJussiveVerbList(unaugmentedTrilateralRoot)
            amr = UnaugmentedImperativeConjugator.instance
                .createVerbList(unaugmentedTrilateralRoot).toMutableList()
            //    public ConjugationResult build(UnaugmentedTrilateralRoot root!!, int kov, List conjugations, String tense, boolean active, boolean applyGemination) {
            //   result =  AugmentedActivePastConjugator.instance.createVerbList(augmentedRoot, getForm());
            val madhiconjresult = UnaugmentedTrilateralModifier.instance.build(
                unaugmentedTrilateralRoot, rule!!.kov, madhi as MutableList<*>,
                SystemConstants.PAST_TENSE, active = true, applyGemination = true
                                                                              )
            val madhimajhoolconj = UnaugmentedTrilateralModifier.instance.build(
                unaugmentedTrilateralRoot, rule.kov, madhimajhool as MutableList<*>,
                SystemConstants.PAST_TENSE, false, true
                                                                               )
            val mudharayconj = UnaugmentedTrilateralModifier.instance.build(
                unaugmentedTrilateralRoot, rule.kov, mudharay!! as MutableList<*>,
                SystemConstants.PRESENT_TENSE, true, true
                                                                           )
            val mudharaymajhoolconj = UnaugmentedTrilateralModifier.instance.build(
                unaugmentedTrilateralRoot, rule.kov, mudharaymajhool!! as MutableList<*>,
                SystemConstants.PRESENT_TENSE, false, true
                                                                                  )
            val amrconj = UnaugmentedTrilateralModifier.instance.build(
                unaugmentedTrilateralRoot, rule.kov, amr,
                SystemConstants.NOT_EMPHASIZED_IMPERATIVE_TENSE, true, true
                                                                      )
            val nahiamrconj = UnaugmentedTrilateralModifier.instance.build(
                unaugmentedTrilateralRoot, rule.kov, nahiamr as MutableList<*>,
                SystemConstants.PRESENT_TENSE, true, true
                                                                          )
            //ismfale and ismMafool
            val conjugatedIsmFael =
                UnaugmentedTrilateralActiveParticipleConjugator.instance.createNounList(
                    unaugmentedTrilateralRoot,
                    unaugmentedTrilateralRoot.conjugation!!
                                                                                       )
            val conjugationResult = ActiveParticipleModifier.instance
                .build(unaugmentedTrilateralRoot, rule.kov, conjugatedIsmFael, "")
            val finalIsmFael = conjugationResult.finalResult
            val conjugatedIsmMafool =
                UnaugmentedTrilateralPassiveParticipleConjugator.instance.createNounList(
                    unaugmentedTrilateralRoot,
                    unaugmentedTrilateralRoot.conjugation!!
                                                                                        )
            val ismmafoolresult = PassiveParticipleModifier.instance
                .build(unaugmentedTrilateralRoot, rule.kov, conjugatedIsmMafool, "")
            val ismmafoolresultFinalResult = ismmafoolresult.finalResult

            //ismala
            val conjugator = StandardInstrumentalConjugator.instance
            val modifier = InstrumentalModifier.instance

            val mifal = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَل")
            val conjResultmifal: ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule.kov, mifal, "مِفْعَل")
            val finalAlamifal = conjResultmifal.finalResult

            val mifalatun = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَلَة")
            val conjResult: ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule.kov, mifalatun, "مِفْعَلَة")
            val finalAlamifalatun = conjResult.finalResult as ArrayList<Any>

            val mifaal = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَال")
            val conjResultmifaal: ConjugationResult =
                modifier.build(unaugmentedTrilateralRoot, rule.kov, mifaal, "مِفْعَال")
            val finalAlamifaal = conjResultmifaal.finalResult




            //zarf
            val zarfconjugator = TimeAndPlaceConjugator.instance
            val zarfmodifier = TimeAndPlaceModifier.instance
            val mafalconjuationlist =
                zarfconjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعَل")
            val mafilconjugationlist =
                zarfconjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعِل")
            val mafil = zarfmodifier.build(
                unaugmentedTrilateralRoot,
                rule.kov,
                mafilconjugationlist,
                "مَفْعِل"
                                          )
            val zarffinalmafil = mafil.finalResult
            val mafalatunconjugationlist =
                zarfconjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعَلَة")
            val mafalatun = zarfmodifier.build(
                unaugmentedTrilateralRoot,
                rule.kov,
                mafalatunconjugationlist,
                "مَفْعَلَة"
                                              )
            val zarffinalmafalatun = mafalatun.finalResult
            madhi = madhiconjresult.finalResult
            madhimajhool = madhimajhoolconj.finalResult
            mudharay = mudharayconj.finalResult
            mudharaymajhool = mudharaymajhoolconj.finalResult
            amr = amrconj.finalResult.toMutableList()
            nahiamr = nahiamrconj.finalResult
            amr.removeAll(setOf<Any?>(null))
            val la = "لا"
            val list = nahiamr.subList(6, 11)
            val nm = ArrayList<String>()
            var sb: StringBuilder
            for (o in list) {
                sb = StringBuilder()
                nm.add(sb.append(la).append(" ").append(o.toString()).toString())
            }
            val listmadhi: MutableList<String> = ArrayList()
            val listmadhimajhool: MutableList<String> = ArrayList()
            val listmudharay: MutableList<String> = ArrayList()
            val listmudharymajhool: MutableList<String> = ArrayList()
            val listamr: MutableList<String> = ArrayList()
            val listamrnahi: MutableList<String> = ArrayList()
            val listismfael: MutableList<String> = ArrayList()
            val listismmafool: MutableList<String> = ArrayList()
            val listmifal: MutableList<String> = ArrayList()
            val listmifalatun: MutableList<String> = ArrayList()
            val listlmifaal: MutableList<String> = ArrayList()
            val listlzarfmafal: MutableList<String> = ArrayList()
            val listlzarfmafil: MutableList<String> = ArrayList()
            val listlzarfmafalatun: MutableList<String> = ArrayList()
            for (s in finalAlamifal) {
                if (s.toString().isNotEmpty()) {
                    listmifal.add(s.toString())
                }
            }
            for (s in finalAlamifalatun) {
                if (s.toString().isNotEmpty()) {
                    listmifalatun.add(s.toString())
                }
            }
            for (s in finalAlamifaal) {
                if (s.toString().isNotEmpty()) {
                    listlmifaal.add(s.toString())
                }
            }
            for (s in zarffinalmafil) {
                if (s.toString().isNotEmpty()) {
                    listlzarfmafal.add(s.toString())
                }
            }
            for (s in zarffinalmafil) {
                if (s.toString().isNotEmpty()) {
                    listlzarfmafil.add(s.toString())
                }
            }
            for (s in zarffinalmafalatun) {
                if (s.toString().isNotEmpty()) {
                    listlzarfmafalatun.add(s.toString())
                }
            }
            for (s in madhi) {
                listmadhi.add(s.toString())
            }
            for (s in madhimajhool) {
                try {
                    listmadhimajhool.add(s.toString())
                }
                catch (e: NullPointerException) {
                    listmadhimajhool.add("-")
                }
            }
            for (s in mudharay) {
                listmudharay.add(s.toString())
            }
            for (s in mudharaymajhool) {
                try {
                    listmudharymajhool.add(s.toString())
                }
                catch (e: NullPointerException) {
                    listmudharymajhool.add("-")
                }
            }
            for (s in finalIsmFael) {
                listismfael.add(s.toString())
            }
            for (s in ismmafoolresultFinalResult) {
                listismmafool.add(s.toString())
            }
            for (s in amr) {
                listamr.add(s.toString())
            }
            for (s in nm) {
                listamrnahi.add(s)
            }
            val vdetails: MutableList<String> = ArrayList()
            vdetails.add(rule.desc!!)
            vdetails.add(unaugmentedTrilateralRoot.conjugationname!!)
            vdetails.add(verbroot)
            vdetails.add(listmadhi[0])
            vdetails.add(listmadhimajhool[0])
            vdetails.add(listmudharay[0])
            vdetails.add(listmudharymajhool[0])
            vdetails.add(listamr[0])
            vdetails.add(listamrnahi[0])
            vdetails.add(listismfael[0])
            vdetails.add(listismmafool[0])
            vdetails.add(listmifal[0])
            vdetails.add(listmifalatun[0])
            vdetails.add(listlmifaal[0])
            // vdetails.add(listlmifaal.get(0));
            vdetails.add(listlzarfmafal[0])
            vdetails.add(listlzarfmafil[0])
            vdetails.add(listlzarfmafalatun[0])
            vdetails.add("mujarrad")
            vdetails.add(unaugmentedTrilateralRoot.conjugation!!)
            skabeer.add(vdetails as ArrayList<*>)
            skabeer.add(listmadhi as ArrayList<*>)
            skabeer.add(listmadhimajhool as ArrayList<*>)
            skabeer.add(listmudharay as ArrayList<*>)
            skabeer.add(listmudharymajhool as ArrayList<*>)
            skabeer.add(listamr as ArrayList<*>)
            skabeer.add(listamrnahi as ArrayList<*>)
            skabeer.add(listismfael as ArrayList<*>)
            skabeer.add(listismmafool as ArrayList<*>)
            skabeer.add(listmifal as ArrayList<*>)
            skabeer.add(listlmifaal as ArrayList<*>)
            skabeer.add(listmifalatun as ArrayList<*>)
            skabeer.add(listlzarfmafal as ArrayList<*>)
            skabeer.add(listlzarfmafil as ArrayList<*>)
            skabeer.add(listlzarfmafalatun as ArrayList<*>)

            //  skabeer.add((ArrayList) strings);
            return skabeer
        }
        return skabeer
    }


    private fun buildUnaugmentedLists(
        verbMood: String?,
        verbRoot: String?,
        unaugmentedFormula: String
    ): MujarradResult? {

        val madhiMudharayList = mutableListOf<MadhiMudharay>()
        val skabeerIsmList = mutableListOf<FaelMafool>()
        val amrList = mutableListOf<Amr>()
        val amrNahiList = mutableListOf<NahiAmr>()
        val ismZarfMafilunList = mutableListOf<IsmZarfMafilun>()
        val ismZarfMafalatunList = mutableListOf<IsmZarfMafalatun>()
        val ismZarfMafalunList = mutableListOf<IsmZarfMafalun>()
        val ismAlaMifalun = mutableListOf<IsmAlaMifalun>()
        val ismAlaMifalatun  = mutableListOf<IsmAlaMifalatun>()
        val ismAlaMifaalun = mutableListOf<IsmAlaMifaalun>()
        val verbDetailsList = mutableListOf<VerbDetails?>()

        val firstCharacter = verbRoot?.getOrNull(0)
        val secondCharacter = verbRoot?.getOrNull(1)
        val thirdCharacter = verbRoot?.getOrNull(2)

        if (firstCharacter != null && secondCharacter != null && thirdCharacter != null) {
            val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)

            val unaugmentedRoot = OSarfDictionary.instance.getUnaugmentedTrilateralRoots(verbRoot, unaugmentedFormula)
            if (unaugmentedRoot != null) {
                val madhi = ActivePastConjugator.instance.createVerbList(unaugmentedRoot)
                val madhimajhool = PassivePastConjugator.instance.createVerbList(unaugmentedRoot)

                val mudharay = when (verbMood) {
                    "Indicative" -> ActivePresentConjugator.instance.createNominativeVerbList(unaugmentedRoot)
                    "Subjunctive" -> ActivePresentConjugator.instance.createAccusativeVerbList(unaugmentedRoot)
                    "Jussive" -> ActivePresentConjugator.instance.createJussiveVerbList(unaugmentedRoot)
                    "Emphasized" -> ActivePresentConjugator.instance.createEmphasizedVerbList(unaugmentedRoot)
                    else -> null
                }
                val mudharaymajhool = when (verbMood) {
                    "Indicative" -> PassivePresentConjugator.instance.createNominativeVerbList(unaugmentedRoot)
                    "Subjunctive" -> PassivePresentConjugator.instance.createAccusativeVerbList(unaugmentedRoot)
                    "Jussive" -> PassivePresentConjugator.instance.createJussiveVerbList(unaugmentedRoot)
                    "Emphasized" -> PassivePresentConjugator.instance.createEmphasizedVerbList(unaugmentedRoot)
                    else -> null
                }

                val amr = UnaugmentedImperativeConjugator.instance.createVerbList(unaugmentedRoot)
                val nahiamr = ActivePresentConjugator.instance.createJussiveVerbList(unaugmentedRoot)

                val madhimodified = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedRoot, rule!!.kov, madhi as MutableList<*>, SystemConstants.PAST_TENSE, true, true
                ).madhiMudharay

                val madhimajhoolmodified = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedRoot, rule.kov, madhimajhool as MutableList<*>, SystemConstants.PAST_TENSE, false, true
                ).madhiMudharay

                val mudharayModified = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedRoot, rule.kov, mudharay!! as MutableList<*>, SystemConstants.PRESENT_TENSE, true, true
                ).madhiMudharay

                val mudharayMajhoolModified = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedRoot, rule.kov, mudharaymajhool!! as MutableList<*>, SystemConstants.PRESENT_TENSE, false, true
                ).madhiMudharay

                madhiMudharayList.addAll(listOf(madhimodified, madhimajhoolmodified, mudharayModified, mudharayMajhoolModified))

                val amrModified = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedRoot, rule.kov, amr.toMutableList(), SystemConstants.NOT_EMPHASIZED_IMPERATIVE_TENSE, true, true
                ).amrandnahi



                val new = NahiAmr()
                val fornahiamr = nahiamr.subList(6, 11)
                val la = "لا "
                new.anta = la + fornahiamr[0].toString()
                new.antuma = la + fornahiamr[1].toString()
                new.antum = la + fornahiamr[2].toString()
                new.anti = la + fornahiamr[3].toString()
                new.antumaf = la + fornahiamr[1].toString()
                new.antunna = la + fornahiamr[4].toString()


                val newAmr = Amr().apply {

                    anta = la + fornahiamr[0].toString()
                    antuma = la + fornahiamr[1].toString()
                    antum = la + fornahiamr[2].toString()
                    anti = la + fornahiamr[3].toString()
                    antumaf = la + fornahiamr[1].toString()
                    antunna = la + fornahiamr[4].toString()
                }



                amrList.addAll(listOf(amrModified))
                amrNahiList.addAll(listOf(new))

                // Add IsmFael and IsmMafool (FaelMafool)
                val fael = ActiveParticipleModifier.instance.build(
                    unaugmentedRoot, rule.kov, UnaugmentedTrilateralActiveParticipleConjugator.instance.createNounList(unaugmentedRoot, unaugmentedRoot.conjugation!!), ""
                ).faelMafool

                val mafool = PassiveParticipleModifier.instance.build(
                    unaugmentedRoot, rule.kov, UnaugmentedTrilateralPassiveParticipleConjugator.instance.createNounList(unaugmentedRoot, unaugmentedRoot.conjugation!!), ""
                ).faelMafool

                skabeerIsmList.addAll(listOf(fael, mafool))
                // Create noun lists for "مِفْعَل", "مِفْعَلَة", and "مِفْعَال"
   /*             val conjugator = StandardInstrumentalConjugator.instance
                val modifier = InstrumentalModifier.instance
                val mifal = conjugator.createNounList(unaugmentedRoot, "مِفْعَل")
                val conjResultmifal: ConjugationResult =
                    modifier.build(unaugmentedRoot, rule.kov, mifal, "مِفْعَل")
                val finalIsmAlaMifalun = conjResultmifal.finalResult

                val mifalatun = conjugator.createNounList(unaugmentedRoot, "مِفْعَلَة")
                val conjResultMifalatun: ConjugationResult =
                    modifier.build(unaugmentedRoot, rule.kov, mifalatun, "مِفْعَلَة")
                val finalIsmAlaMifalatun = conjResultMifalatun.finalResult as ArrayList<Any>

                val ismAlaMifaalun = conjugator.createNounList(unaugmentedRoot, "مِفْعَال")
                val conjResultMifaal: ConjugationResult =
                    modifier.build(unaugmentedRoot, rule.kov, ismAlaMifaalun, "مِفْعَال")
                val finalIsmAlaMifaalun = conjResultMifaal.finalResult
*/


                // Add IsmZarf and IsmAla (Time and Place Nouns)
               ismZarfMafilunList.addAll(buildIsmZarfMafilun(unaugmentedRoot, rule))
                ismZarfMafalatunList.addAll(buildIsmZarfMafalatun(unaugmentedRoot, rule))
                ismZarfMafalunList.addAll(buildIsmZarfMafalun(unaugmentedRoot, rule))

                 ismAlaMifalun.addAll(buildIsmAlaMifalun(unaugmentedRoot, rule))
                 ismAlaMifalatun.addAll(buildIsmAlaMifalatun(unaugmentedRoot, rule))
                ismAlaMifaalun.addAll(buildIsmAlaMifaalun(unaugmentedRoot, rule))


                // Add VerbDetails
                val verbDetails = VerbDetails().apply {
                    verbtype = rule.desc.toString()
                    babname = unaugmentedRoot.conjugationname.toString()
                    mazeedormujarad = "mujarrad"
                    wazannumberorname = unaugmentedRoot.rulename.toString()
                    verbroot = verbRoot
                }
                verbDetailsList.add(verbDetails)

                return MujarradResult(
                    madhiMudharayList,
                    skabeerIsmList,
                    amrList,
                    amrNahiList ,
                    ismZarfMafilunList,
                    ismZarfMafalatunList,
                    ismZarfMafalunList,
                    ismAlaMifalun ,
                    ismAlaMifalatun,
                    ismAlaMifaalun,
                    verbDetailsList
                )
            }
        }
        return null
    }


    private fun buildIsmAlaMifalun(
        unaugmentedRoot: UnaugmentedTrilateralRoot,
        rule: TrilateralKovRule
    ): Collection<IsmAlaMifalun> {

        val ismConjugator = StandardInstrumentalConjugator.instance
        val ismModifier = InstrumentalModifier.instance
        val mifallist = ismConjugator.createNounList(unaugmentedRoot, "مِفْعَل")
         return listOf(ismModifier.build(unaugmentedRoot, rule.kov, mifallist, "مِفْعَل").alaMifalun)
    }


    private fun buildIsmAlaMifalatun(
        unaugmentedRoot: UnaugmentedTrilateralRoot,
        rule: TrilateralKovRule
    ): Collection<IsmAlaMifalatun> {

        val ismConjugator = StandardInstrumentalConjugator.instance
        val ismModifier = InstrumentalModifier.instance
        val mifalatunlist = ismConjugator.createNounList(unaugmentedRoot, "مِفْعَلَة")

        return listOf(ismModifier.build(unaugmentedRoot, rule.kov, mifalatunlist, "مِفْعَلَة").alaMifalatun)
    }


    private fun buildIsmAlaMifaalun(
        unaugmentedRoot: UnaugmentedTrilateralRoot,
        rule: TrilateralKovRule
    ): Collection<IsmAlaMifaalun> {

        val ismConjugator = StandardInstrumentalConjugator.instance
        val ismModifier = InstrumentalModifier.instance
        val mifalatunlist = ismConjugator.createNounList(unaugmentedRoot, "مِفْعَال")

        return listOf(ismModifier.build(unaugmentedRoot, rule.kov, mifalatunlist, "مِفْعَال").alaMifaalun)
    }

    private fun buildIsmZarfMafilun(
        unaugmentedRoot: UnaugmentedTrilateralRoot,
       rule: TrilateralKovRule
    ): Collection<IsmZarfMafilun> {
        val zarfConjugator = TimeAndPlaceConjugator.instance
        val zarfModifier = TimeAndPlaceModifier.instance
        val zarfList = zarfConjugator.createNounList(unaugmentedRoot, "مَفْعِل")
        return listOf(zarfModifier.build(unaugmentedRoot, rule.kov, zarfList, "مَفْعِل").zarfMafilun)
    }




    private fun buildIsmZarfMafalatun(
        unaugmentedRoot: UnaugmentedTrilateralRoot,
        rule: TrilateralKovRule
    ): Collection<IsmZarfMafalatun> {
        val zarfConjugator = TimeAndPlaceConjugator.instance
        val zarfModifier = TimeAndPlaceModifier.instance
        val zarfList = zarfConjugator.createNounList(unaugmentedRoot, "مَفْعَلَة")
        return listOf(zarfModifier.build(unaugmentedRoot, rule.kov, zarfList, "مَفْعَلَة").zarfMafalatun)
    }

    private fun buildIsmZarfMafalun(
        unaugmentedRoot: UnaugmentedTrilateralRoot,
        rule: TrilateralKovRule
    ): Collection<IsmZarfMafalun> {


             val zarfconjugator = TimeAndPlaceConjugator.instance
                val zarfmodifier = TimeAndPlaceModifier.instance
                val IsmZarfMafalunResult =
                    zarfconjugator.createNounList(unaugmentedRoot, "مَفْعَل")
                val ismZarafMafalunResult = zarfmodifier.build(
                    unaugmentedRoot,
                    rule.kov,
                    IsmZarfMafalunResult,
                    "مَفْعَل"
                                                              )
                val zarfinalmafal = ismZarafMafalunResult.finalResult
                val mafilconjugationlist =
                    zarfconjugator.createNounList(unaugmentedRoot, "مَفْعِل")
                val ismZarfMafilunResult = zarfmodifier.build(
                    unaugmentedRoot,
                    rule.kov,
                    mafilconjugationlist,
                    "مَفْعِل"
                                                             )
                val zarffinalmafil = ismZarfMafilunResult.finalResult


        val zarfConjugator = TimeAndPlaceConjugator.instance
        val zarfModifier = TimeAndPlaceModifier.instance
        val zarfList = zarfConjugator.createNounList(unaugmentedRoot, "مَفْعَل")
        return listOf(zarfModifier.build(unaugmentedRoot, rule.kov, zarfList, "مَفْعَل").zarfMafalun)
    }



    private fun buildMazeedListOLD(
        verbmood: String,
         verbroot: String,
        augmentedFormula: String,
    ): ArrayList<ArrayList<*>> {
        val augmentedRoot =
            OSarfDictionary.instance.getAugmentedTrilateralRoot(verbroot, augmentedFormula)

        // If root not found, return an empty list
        if (augmentedRoot == null) return arrayListOf()
         var berb=verbroot
        // Get the first, second, third characters of the verb root
        val rule = KovRulesManager.instance.getTrilateralKovRule(
            verbroot[0], verbroot[1], verbroot[2]
        ) ?: return arrayListOf()

        // Initialize lists for Madhi/Mudhary conjugations
        val madhiMudharayList = ArrayList<MadhiMudharay>()
        val skabeerIsmList = ArrayList<FaelMafool>()
        val amrList = ArrayList<Amr>()
        val verbDetailsList = ArrayList<VerbDetails>()

        // Retrieve verb conjugations
        val madhi = AugmentedActivePastConjugator.instance
            .createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
        val madhimajhool = AugmentedPassivePastConjugator.instance
            .createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())

        // Handle verb mood and retrieve the appropriate conjugations
        val mudharay = when (verbmood) {
            "Indicative" -> AugmentedActivePresentConjugator.instance.nominativeConjugator
            "Subjunctive" -> AugmentedActivePresentConjugator.instance.accusativeConjugator
            "Jussive" -> AugmentedActivePresentConjugator.instance.jussiveConjugator
            "Emphasized" -> AugmentedActivePresentConjugator.instance.emphasizedConjugator
            else -> null
        }?.createVerbList(augmentedRoot, augmentedFormula.toInt())

        val mudharaymajhool = when (verbmood) {
            "Indicative" -> AugmentedPassivePresentConjugator.instance.nominativeConjugator
            "Subjunctive" -> AugmentedPassivePresentConjugator.instance.accusativeConjugator
            "Jussive" -> AugmentedPassivePresentConjugator.instance.jussiveConjugator
            "Emphasized" -> AugmentedPassivePresentConjugator.instance.emphasizedConjugator
            else -> null
        }?.createVerbList(augmentedRoot, augmentedFormula.toInt())

        // Process Amr and NahiAmr conjugations
        val amr = AugmentedImperativeConjugator.instance.notEmphsizedConjugator
            .createVerbList(augmentedRoot, augmentedFormula.toInt())
        val nahiamr = AugmentedActivePresentConjugator.instance.jussiveConjugator
            .createVerbList(augmentedRoot, augmentedFormula.toInt())

        // Apply modifications to each conjugation group
        val madhiconjresult = AugmentedTrilateralModifier.instance.build(
            augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhi,
            SystemConstants.PAST_TENSE, true, true
        )
        val madhimajhoolconj = AugmentedTrilateralModifier.instance.build(
            augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhimajhool,
            SystemConstants.PAST_TENSE, true, true
        )
        val mudharayconj = mudharay?.let {
            AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), it,
                SystemConstants.PRESENT_TENSE, true, true
            )
        }
        val mudharaymajhoolconj = mudharaymajhool?.let {
            AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), it,
                SystemConstants.PRESENT_TENSE, false, true
            )
        }

        // Process active and passive participles (ismFael and ismMafool)
        val ismFael = AugmentedTrilateralActiveParticipleConjugator.instance.createNounList(augmentedRoot, augmentedFormula.toInt())
        val ismfaleresult = org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismFael, true)

        val ismMafool = AugmentedTrilateralPassiveParticipleConjugator.instance
            .createNounList(augmentedRoot, augmentedFormula.toInt())
        val ismmafoolresult = org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple
            .ActiveParticipleModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismMafool, true)

        // Add conjugated results to lists
        madhiMudharayList.addAll(
            listOf(madhiconjresult.madhiMudharay, madhimajhoolconj.madhiMudharay)
        )
        mudharayconj?.let { madhiMudharayList.add(it.madhiMudharay) }
        mudharaymajhoolconj?.let { madhiMudharayList.add(it.madhiMudharay) }

        skabeerIsmList.addAll(
            listOf(ismfaleresult.faelMafool, ismmafoolresult.faelMafool)
        )

        // Handle Amr and NahiAmr
        // Handle Amr and NahiAmr
        val amrobj = Amr() // Create a new AmrNahiAmr object
        val nahiamrList = nahiamr.subList(6, 11) // Adjust this based on your needs
        amrobj.anta = "لا " + nahiamrList[0].toString()
        amrobj.antuma = "لا " + nahiamrList[1].toString()
        amrobj.antum = "لا " + nahiamrList[2].toString()
        amrobj.anti = "لا " + nahiamrList[3].toString()
// Add additional properties as necessary...

// Create a new instance for the other conjugation
        val newAmr = Amr().apply {
            anta = "لا " + nahiamrList[0].toString()
            antuma = "لا " + nahiamrList[1].toString()
            antum = "لا " + nahiamrList[2].toString()
            anti = "لا " + nahiamrList[3].toString()
            // Add other conjugations as needed...
        }

// Add the AmrNahiAmr objects to the list
        amrList.add(amrobj)
        amrList.add(newAmr)
        var verbDetailsss = VerbDetails(verbroot = "initialRoot")
// verbDetails is now mutable


        // Verb details
        val verbDetails = VerbDetails().apply {
            verbtype = rule.desc.toString()
            babname = augmentedRoot.babname.toString()
            mazeedormujarad = "mazeed"
            wazannumberorname = augmentedRoot.form!!
            berb = verbroot
        }
        verbDetailsList.add(verbDetails)

        // Return the final list
        val finalList = arrayListOf(
            madhiMudharayList,
            skabeerIsmList,
            amrList,
            verbDetailsList
        )

        return finalList
    }


    private fun buildMazeedList(
        verbMood: String,
        verbRoot: String,
        augmentedFormula: String
    ): MazeedResult? {
        // Get the augmented root
        val augmentedRoot = OSarfDictionary.instance.getAugmentedTrilateralRoot(verbRoot, augmentedFormula)
            ?: return null // Return null if the root is not found

        // Get the rule for the verb root
        val rule = KovRulesManager.instance.getTrilateralKovRule(verbRoot[0], verbRoot[1], verbRoot[2])
            ?: return null

        // Initialize lists
        val madhiMudharayList = mutableListOf<MadhiMudharay>()
        val skabeerIsmList = mutableListOf<FaelMafool>()
        val amrList = mutableListOf<Amr>()
        val nahiAmrList = mutableListOf<NahiAmr>()
        val verbDetailsList = mutableListOf<VerbDetails>()

        // Conjugate Madhi (past) and Madhimajhool (passive past)
        val madhi = AugmentedActivePastConjugator.instance.createVerbList(augmentedRoot, augmentedFormula.toInt())
        val madhiMajhool = AugmentedPassivePastConjugator.instance.createVerbList(augmentedRoot, augmentedFormula.toInt())

        // Handle different moods for Mudharay (present) and MudharayMajhool (passive present)
        val mudharay = getMudharayConjugation(verbMood, augmentedRoot, augmentedFormula.toInt(), true)
        val mudharayMajhool = getMudharayConjugation(verbMood, augmentedRoot, augmentedFormula.toInt(), false)

        // Process Amr and NahiAmr conjugations
        val amr = AugmentedImperativeConjugator.instance.notEmphsizedConjugator.createVerbList(augmentedRoot, augmentedFormula.toInt())
        val nahiAmr = AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedFormula.toInt())

        // Modify and apply conjugations
        madhiMudharayList.addAll(getModifiedConjugations(madhi, madhiMajhool, mudharay, mudharayMajhool, augmentedRoot, rule))

        // Process IsmFael and IsmMafool
        val instance1 = AugmentedTrilateralActiveParticipleConjugator.instance
        val ismFaelList = getModifiedActiveNouns(AugmentedTrilateralActiveParticipleConjugator.instance, augmentedRoot, augmentedFormula.toInt(), rule)

        val ismMafoolList = getModifiedPassiveNouns(AugmentedTrilateralPassiveParticipleConjugator.instance, augmentedRoot, augmentedFormula.toInt(), rule)


        skabeerIsmList.addAll(listOf(ismFaelList, ismMafoolList))

        // Handle Amr and NahiAmr conjugations
        amrList.addAll(buildAmr(amr))

       nahiAmrList.addAll(buildNahiAmr(nahiAmr))
        // Build VerbDetails
        val verbDetails = VerbDetails(
            verbtype = rule.desc,
            babname = augmentedRoot.babname ?: "",
            mazeedormujarad = "mazeed",
            wazannumberorname = augmentedRoot.form?.toString() ?: "",
            verbroot = verbRoot
        )
        verbDetailsList.add(verbDetails)

        // Return the final result using the MazeedResult data class
        return MazeedResult(
            madhiMudharayList = madhiMudharayList,
            skabeerIsmList = skabeerIsmList,
            amrList = amrList,
            nahiAmrList = nahiAmrList,
            verbDetailsList = verbDetailsList
        )
    }



// Helper functions

    // Get Mudharay or MudharayMajhool based on the verb mood
    private fun getMudharayConjugation(
        verbMood: String,
        augmentedRoot: AugmentedTrilateralRoot,
        augmentedFormula: Int,
        isActive: Boolean
    ): List<AugmentedPresentVerb?>? {
        return when (verbMood) {
            "Indicative" -> if (isActive) {
                AugmentedActivePresentConjugator.instance.nominativeConjugator.createVerbList(augmentedRoot,augmentedFormula)

            } else {
                AugmentedPassivePresentConjugator.instance.nominativeConjugator.createVerbList(augmentedRoot, augmentedFormula)
            }
            "Subjunctive" -> if (isActive) {
                AugmentedActivePresentConjugator.instance.accusativeConjugator.createVerbList(augmentedRoot, augmentedFormula)
            } else {
                AugmentedPassivePresentConjugator.instance.accusativeConjugator.createVerbList(augmentedRoot, augmentedFormula)
            }
            "Jussive" -> if (isActive) {
                AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedFormula)
            } else {
                AugmentedPassivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedFormula)
            }
            "Emphasized" -> if (isActive) {
                AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedFormula)
            } else {
                AugmentedPassivePresentConjugator.instance.emphasizedConjugator.createVerbList(augmentedRoot, augmentedFormula)
            }
            else -> null
        }
    }


    // Get modified conjugations for Madhi, Madhimajhool, Mudharay, and MudharayMajhool
    private fun getModifiedConjugations(
        madhi: List<AugmentedPastVerb?>,
        madhiMajhool: List<AugmentedPastVerb?>,
        mudharay: List<AugmentedPresentVerb?>?,
        mudharayMajhool: List<AugmentedPresentVerb?>?,
        augmentedRoot: AugmentedTrilateralRoot,
        rule: TrilateralKovRule
    ): List<MadhiMudharay> {
        val madhiResult = AugmentedTrilateralModifier.instance.build(
            augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhi, SystemConstants.PAST_TENSE, true, true
        )
        val madhiMajhoolResult = AugmentedTrilateralModifier.instance.build(
            augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhiMajhool, SystemConstants.PAST_TENSE, true, true
        )
        val mudharayResult = mudharay?.let {
            AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), it, SystemConstants.PRESENT_TENSE, true, true)
        }
        val mudharayMajhoolResult = mudharayMajhool?.let {
            AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), it, SystemConstants.PRESENT_TENSE, false, true)
        }

        return listOf(madhiResult.madhiMudharay, madhiMajhoolResult.madhiMudharay) +
                listOfNotNull(mudharayResult?.madhiMudharay, mudharayMajhoolResult?.madhiMudharay)
    }

    // Get modified IsmFael and IsmMafool nouns
    private fun getModifiedActiveNouns(
        instance: AugmentedTrilateralActiveParticipleConjugator,
        augmentedRoot: AugmentedTrilateralRoot,
        augmentedFormula: Int,
        rule: TrilateralKovRule
    ): FaelMafool {
        val nounList = instance.createNounList(augmentedRoot, augmentedFormula)
        return org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
            .build(augmentedRoot, rule.kov, augmentedFormula, nounList, true).faelMafool
    }
    // Get modified IsmFael and IsmMafool nouns
    private fun getModifiedPassiveNouns(
        instance: AugmentedTrilateralPassiveParticipleConjugator,
        augmentedRoot: AugmentedTrilateralRoot,
        augmentedFormula: Int,
        rule: TrilateralKovRule
    ): FaelMafool {
        val nounList = instance.createNounList(augmentedRoot, augmentedFormula)
        return org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
            .build(augmentedRoot, rule.kov, augmentedFormula, nounList, true).faelMafool
    }
    private fun buildAmr(amr: List<AugmentedImperativeVerb?>): List<Amr> {
        val amrObj = Amr().apply {
            anta = amr.getOrNull(2)?.toString() ?: ""
            antuma = amr.getOrNull(4)?.toString() ?: ""
            antum = amr.getOrNull(5)?.toString() ?: ""
            anti = amr.getOrNull(3)?.toString() ?: ""
            antumaf = amr.getOrNull(4)?.toString() ?: ""
            antunna = amr.getOrNull(6)?.toString() ?: ""
        }

/*
  var anta: String? = null
    var antuma: String? = null
    var antum: String? = null
    var anti: String? = null
    var antumaf: String? = null
    var antunna: String? = null
 */

        return listOf(amrObj)
    }

    // Build Amr and NahiAmr lists
    private fun buildNahiAmr(nahiAmr: List<AugmentedPresentVerb?>): List<NahiAmr> {

        val fornahiamr = nahiAmr.subList(6, 11)


        val nahiAmrObj = NahiAmr().apply {
            anta = "لا " + (fornahiamr.getOrNull(0)?.toString() ?: "")
            antuma = "لا " + (fornahiamr.getOrNull(1)?.toString() ?: "")
            antum = "لا " + (fornahiamr.getOrNull(2)?.toString() ?: "")
            anti = "لا " + (fornahiamr.getOrNull(3)?.toString() ?: "")
            antumaf = "لا " + (fornahiamr.getOrNull(1)?.toString() ?: "")
            antunna = "لا " + (fornahiamr.getOrNull(4)?.toString() ?: "")
        }

        return listOf(nahiAmrObj)
    }














    fun buildMazeedParticiples(
        verbroot: String,
        augmentedFormula: String,
                              ): ArrayList<ArrayList<*>> {
        var modifiedVebroot = verbroot
        val firstCharacter = modifiedVebroot.get(0)
        val secondCharacter = modifiedVebroot.get(1)
        val thirdCharacter = modifiedVebroot.get(2)
        val finalList = ArrayList<ArrayList<*>>()
        val arrayofFaelMafool = ArrayList<FaelMafool>()
        val faelObj: FaelMafool
        val mafoolObj: FaelMafool
        val alephIndex = modifiedVebroot.indexOf("ا")
        val alephHamzaIndex = modifiedVebroot.indexOf("أ")
        if (alephIndex != -1) {
            if (modifiedVebroot != null) {
                modifiedVebroot = modifiedVebroot.replace("ا", "ء")
            }
        } else if (alephHamzaIndex != -1) {
            if (modifiedVebroot != null) {
                modifiedVebroot = modifiedVebroot.replace("أ", "ء")
            }
        }
        val augmentedRoot =
            OSarfDictionary.instance.getAugmentedTrilateralRoot(modifiedVebroot, augmentedFormula)
        var ismFael: List<*>?
        var ismMafool: List<*>?

        if (augmentedRoot != null) {
            val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)
            ismFael = AugmentedTrilateralActiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedFormula.toInt())
            val conjResult =
                org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
                    .build(augmentedRoot, rule!!.kov, augmentedRoot.form!!.toInt(), ismFael, true)

            ismMafool = AugmentedTrilateralPassiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedFormula.toInt())
            val ismmafoolresult =
                org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
                    .build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismMafool, true)



            faelObj = conjResult.faelMafool
            mafoolObj = ismmafoolresult.faelMafool
            arrayofFaelMafool.add(faelObj)
            arrayofFaelMafool.add(mafoolObj)

            finalList.add(arrayofFaelMafool)

            return finalList
        }
        return finalList
    }


    private fun buildMazeedListold(verbmood: String, verbroot: String): ArrayList<ArrayList<*>> {
        val augmentedRoot = OSarfDictionary.instance.getAugmentedTrilateralRoot(verbroot)
        var ismFael: List<*>
        var ismMafool: List<*>
        var madhimajhool: List<*>
        var mudharay: List<*>? = null
        var amr: MutableList<*>
        var nahiamr: List<*>
        var madhi: List<*>
        var mudharaymajhool: List<*>? = null
        val finalList = ArrayList<ArrayList<*>>()
        val firstCharacter = verbroot[0]
        val secondCharacter = verbroot[1]
        val thirdCharacter = verbroot[2]
        val madhimuhdarymainobj = ArrayList<MadhiMudharay>()

        val arrayofamrobj = ArrayList<Amr>()
        val skabeer = ArrayList<ArrayList<*>>()
        val madhimajhoolobj: MadhiMudharay
        val mudharyobj: MadhiMudharay
        val mudharaymajhoolobj: MadhiMudharay
        val madhiobj: MadhiMudharay
        val faelObj: FaelMafool
        val mafoolObj: FaelMafool
        val amrobj: Amr

        val verbDetailsobj = VerbDetails()
        val skabeerismobj = ArrayList<FaelMafool>()

        val arrayofverbdetails = ArrayList<VerbDetails>()
        val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)
        if (augmentedRoot != null) {

            madhi = AugmentedActivePastConjugator.instance
                .createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            madhimajhool = AugmentedPassivePastConjugator.instance
                .createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            when (verbmood) {
                "Indicative" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.nominativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.nominativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                }

                "Subjunctive" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.accusativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.accusativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                }

                "Jussive" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.jussiveConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                }

                "Emphasized" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.emphasizedConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.emphasizedConjugator.createVerbList(
                            augmentedRoot,
                            augmentedRoot.form!!.toInt()
                        )
                }
            }
            nahiamr =
                AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(
                    augmentedRoot,
                    augmentedRoot.form!!.toInt()
                )
            amr = AugmentedImperativeConjugator.instance.notEmphsizedConjugator.createVerbList(
                augmentedRoot,
                augmentedRoot.form!!.toInt()
            )
                .toMutableList()
            val madhiconjresult = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule!!.kov, augmentedRoot.form!!.toInt(), madhi,
                SystemConstants.PAST_TENSE, true, true
            )
            val madhimajhoolconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhimajhool,
                SystemConstants.PAST_TENSE, true, true
            )
            val mudharayconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), mudharay!!,
                SystemConstants.PRESENT_TENSE, true, true
            )
            val mudharaymajhoolconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), mudharaymajhool!!,
                SystemConstants.PRESENT_TENSE, false, true
            )
            val amrconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), amr,
                SystemConstants.NOT_EMPHASIZED_IMPERATIVE_TENSE, true, true
            )
            val nahiamrconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), nahiamr,
                SystemConstants.PRESENT_TENSE, true, true
            )
            ismFael = AugmentedTrilateralActiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedRoot.form!!.toInt())
            ismMafool = AugmentedTrilateralPassiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedRoot.form!!.toInt())
            val conjResult =
                org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
                    .build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismFael, true)
            val mafoolresult =
                org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
                    .build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismMafool, true)


            val la = "لا "
            amrobj = amrconj.amrandnahi
            val new = Amr()
            val fornahiamr = nahiamr.subList(6, 11)

            new.anta = la + fornahiamr[0].toString()
            new.antuma = la + fornahiamr[1].toString()
            new.antum = la + fornahiamr[2].toString()
            new.anti = la + fornahiamr[3].toString()
            new.antumaf = la + fornahiamr[1].toString()
            new.antunna = la + fornahiamr[4].toString()
            arrayofamrobj.add(amrobj)
            arrayofamrobj.add(new)
            faelObj = conjResult.faelMafool
            mafoolObj = mafoolresult.faelMafool
            skabeerismobj.add(faelObj)
            skabeerismobj.add(mafoolObj)
            //     arrayofskabeerismobj.add(skabeerismobj)
            madhiobj = madhiconjresult.madhiMudharay
            madhimajhoolobj = madhimajhoolconj.madhiMudharay
            mudharyobj = mudharayconj.madhiMudharay
            mudharaymajhoolobj = mudharaymajhoolconj.madhiMudharay
            madhimuhdarymainobj.add(madhiobj)
            madhimuhdarymainobj.add(madhimajhoolobj)
            madhimuhdarymainobj.add(mudharyobj)
            madhimuhdarymainobj.add(mudharaymajhoolobj)
            verbDetailsobj.verbtype = rule.desc.toString()
            verbDetailsobj.babname = augmentedRoot.babname.toString()
            verbDetailsobj.mazeedormujarad = "mazeed"
            verbDetailsobj.wazannumberorname = augmentedRoot.form!!
            verbDetailsobj.verbroot = verbroot
            finalList.add(madhimuhdarymainobj)
            finalList.add(skabeerismobj)
            finalList.add(arrayofamrobj)
            arrayofverbdetails.add(verbDetailsobj)
            finalList.add(arrayofverbdetails)
            ismFael = conjResult.finalResult
            AugmentedTrilateralPassiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedRoot.form!!.toInt())
            ismMafool = conjResult.finalResult
            madhi = madhiconjresult.finalResult
            madhimajhool = madhimajhoolconj.finalResult
            mudharay = mudharayconj.finalResult
            mudharaymajhool = mudharaymajhoolconj.finalResult
            amr = amrconj.finalResult.toMutableList()
            nahiamr = nahiamrconj.finalResult
            amr.removeAll(setOf<Any?>(null))

            val list = nahiamr.subList(6, 11)
            val nm = ArrayList<String>()
            var sb: StringBuilder
            for (o in list) {
                sb = StringBuilder()
                nm.add(sb.append(la).append(" ").append(o.toString()).toString())
            }
            val listmadhi: MutableList<String> = ArrayList()
            val listmadhimajhool: MutableList<String> = ArrayList()
            val listmudharay: MutableList<String> = ArrayList()
            val listmudharymajhool: MutableList<String> = ArrayList()
            val listamr: MutableList<String> = ArrayList()
            val listamrnahi: MutableList<String> = ArrayList()
            val listismfael: MutableList<String> = ArrayList()
            val listismmafool: MutableList<String> = ArrayList()
            val vdetails: MutableList<String> = ArrayList()
            vdetails.add(rule.desc.toString())
            vdetails.add(augmentedRoot.babname!!)
            //vdetails.add(    rule.getDesc());
            vdetails.add(verbroot)
            for (s in madhi) {
                listmadhi.add(s.toString())
            }
            for (s in madhimajhool) {
                try {
                    listmadhimajhool.add(s.toString())
                }
                catch (e: NullPointerException) {
                    listmadhimajhool.add("-")
                }
            }
            for (s in mudharay) {
                listmudharay.add(s.toString())
            }
            for (s in mudharaymajhool) {
                try {
                    listmudharymajhool.add(s.toString())
                }
                catch (e: NullPointerException) {
                    listmudharymajhool.add("-")
                }
            }
            for (s in ismFael) {
                listismfael.add(s.toString())
            }
            for (s in ismMafool) {
                listismmafool.add(s.toString())
            }
            for (s in amr) {
                listamr.add(s.toString())
            }
            for (s in nm) {
                listamrnahi.add(s)
            }
            vdetails.add(listmadhi[0])
            vdetails.add(listmadhimajhool[0])
            vdetails.add(listmudharay[0])
            vdetails.add(listmudharymajhool[0])
            vdetails.add(listamr[0])
            vdetails.add(listamrnahi[0])
            vdetails.add(listismfael[0])
            vdetails.add(listismmafool[0])
            vdetails.add("")
            vdetails.add("")
            vdetails.add("")
            vdetails.add("")
            vdetails.add("")
            vdetails.add("")
            vdetails.add("mazeed")
            vdetails.add(augmentedRoot.form!!)
            skabeer.add(vdetails as ArrayList<*>)
            skabeer.add(listmadhi as ArrayList<*>)
            skabeer.add(listmadhimajhool as ArrayList<*>)
            skabeer.add(listmudharay as ArrayList<*>)
            skabeer.add(listmudharymajhool as ArrayList<*>)
            skabeer.add(listamr as ArrayList<*>)
            skabeer.add(listamrnahi as ArrayList<*>)
            skabeer.add(listismfael as ArrayList<*>)
            skabeer.add(listismmafool as ArrayList<*>)
            return finalList
        }
        return finalList
    }


    private fun buildMazeedList(verbMood: String, verbRoot: String): MazeedResult? {
        val augmentedRoot = OSarfDictionary.instance.getAugmentedTrilateralRoot(verbRoot) ?: return null
        val amrList = mutableListOf<Amr>()
        val nahiAmrList = mutableListOf<NahiAmr>()
        val rule = KovRulesManager.instance.getTrilateralKovRule(verbRoot[0], verbRoot[1], verbRoot[2])

        // Get lists of conjugated verbs for different tenses and moods
        val madhi = AugmentedActivePastConjugator.instance.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
        val madhimajhool = AugmentedPassivePastConjugator.instance.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
        val mudharay = getMudharayList(verbMood, augmentedRoot)
        val mudharaymajhool = getMudharayMajhoolList(verbMood, augmentedRoot)
        val amr = AugmentedImperativeConjugator.instance.notEmphsizedConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt()).toMutableList()
        val nahiamr = AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())

        // Apply modifiers to the verb conjugations
        val madhiModified = AugmentedTrilateralModifier.instance.build(augmentedRoot, rule!!.kov, augmentedRoot.form!!.toInt(), madhi, SystemConstants.PAST_TENSE, true, true)
        val madhimajhoolModified = AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhimajhool, SystemConstants.PAST_TENSE, true, true)
        val mudharayModified = AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), mudharay!!, SystemConstants.PRESENT_TENSE, true, true)
        val mudharaymajhoolModified = AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), mudharaymajhool!!, SystemConstants.PRESENT_TENSE, false, true)
        val amrModified = AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), amr, SystemConstants.NOT_EMPHASIZED_IMPERATIVE_TENSE, true, true)
        val nahiamrModified = AugmentedTrilateralModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), nahiamr, SystemConstants.PRESENT_TENSE, true, true)

        // Create ism fael and ism mafool
        val ismFael = AugmentedTrilateralActiveParticipleConjugator.instance.createNounList(augmentedRoot, augmentedRoot.form!!.toInt())
        val ismMafool = AugmentedTrilateralPassiveParticipleConjugator.instance.createNounList(augmentedRoot, augmentedRoot.form!!.toInt())

        // Modifying ism fael and ism mafool
        val faelObj = org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismFael, true).faelMafool
        val mafoolObj = org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance.build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismMafool, true).faelMafool

        // Create AmrNahiAmr objects
       amrList.addAll(buildAmr(amr))
        nahiAmrList.addAll(buildNahiAmr(nahiamr))
      //  val amrObj = amrModified.amrandnahi
       // val newNahiAmr = buildNahiAmr(nahiamr)

        // Populate lists
        val madhiMudharayList = listOf(
            madhiModified.madhiMudharay,
            madhimajhoolModified.madhiMudharay,
            mudharayModified.madhiMudharay,
            mudharaymajhoolModified.madhiMudharay
        )

        val skabeerIsmList = listOf(faelObj, mafoolObj)

        val verbDetailsList = listOf(
            augmentedRoot.babname?.let {
                VerbDetails(
                    verbtype = rule.desc.toString(),
                    babname = it,
                    mazeedormujarad = "mazeed",
                    wazannumberorname = augmentedRoot.form!!,
                    verbroot = verbRoot
                )
            }
        )

        return MazeedResult(
            madhiMudharayList = madhiMudharayList,
            skabeerIsmList = skabeerIsmList,
            amrList = amrList,
            nahiAmrList = nahiAmrList,
            verbDetailsList = verbDetailsList
        )
    }

    // Helper function to build Mudharay based on verb mood
    private fun getMudharayList(verbMood: String, augmentedRoot: AugmentedTrilateralRoot): List<*>? {
        return when (verbMood) {
            "Indicative" -> AugmentedActivePresentConjugator.instance.nominativeConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            "Subjunctive" -> AugmentedActivePresentConjugator.instance.accusativeConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            "Jussive" -> AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            "Emphasized" -> AugmentedActivePresentConjugator.instance.emphasizedConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            else -> null
        }
    }

    // Helper function to build MudharayMajhool based on verb mood
    private fun getMudharayMajhoolList(verbMood: String, augmentedRoot: AugmentedTrilateralRoot): List<*>? {
        return when (verbMood) {
            "Indicative" -> AugmentedPassivePresentConjugator.instance.nominativeConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            "Subjunctive" -> AugmentedPassivePresentConjugator.instance.accusativeConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            "Jussive" -> AugmentedPassivePresentConjugator.instance.jussiveConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            "Emphasized" -> AugmentedPassivePresentConjugator.instance.emphasizedConjugator.createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            else -> null
        }
    }

    // Helper function to build the negation of Amr
    private fun buildNahiAmr(nahiamr: List<*>): Amr {
        val la = "لا "

        val new = Amr()
        val fornahiamr = nahiamr.subList(6, 11)

        new.anta = la + fornahiamr[0].toString()
        new.antuma = la + fornahiamr[1].toString()
        new.antum = la + fornahiamr[2].toString()
        new.anti = la + fornahiamr[3].toString()
        new.antumaf = la + fornahiamr[1].toString()
        new.antunna = la + fornahiamr[4].toString()
      return new
    }


    /*
        private fun buildUnaugmentedListsold(
            verbmood: String?,
            verbroot: String?,
            unaugmentedFormula: String,
                                         ): ArrayList<ArrayList<*>> {

            val finalList = ArrayList<ArrayList<*>>()
            val madhimuhdarymainobj = ArrayList<MadhiMudharay>()
            val ismalamifalunarr = ArrayList<IsmAlaMifalun>()
            val ismalamifalatunarr= ArrayList<IsmAlaMifalatun>()
            val ismaalamifaalunarr = ArrayList<IsmAlaMifaalun>()

            val ismZarfMafilunarr = ArrayList<IsmZarfMafilun>()
            val ismzarfmafalatunarr = ArrayList<IsmZarfMafalatun>()
            val ismzarfmafalunarr = ArrayList<IsmZarfMafalun>()
            val skabeerismobj = ArrayList<FaelMafool>()
            val arrayofverbdetails = ArrayList<VerbDetails>()
            val arrayofamrobj = ArrayList<AmrNahiAmr>()
            val madhimajhoolobj: MadhiMudharay
            val mudharyobj: MadhiMudharay
            val mudharaymajhoolobj: MadhiMudharay
            val madhiobj: MadhiMudharay
            val faelObj: FaelMafool
            val mafoolObj: FaelMafool
            val amrobj: AmrNahiAmr
            val alamifal :IsmAlaMifalun
            val alaMifalatun:IsmAlaMifalatun

            val alaMifaalun:IsmAlaMifaalun

            val zarfMafilun :IsmZarfMafilun

            val zarfMafalun:IsmZarfMafalun
            val zarfMafalatun:IsmZarfMafalatun

            val verbDetailsobj = VerbDetails()
            val firstCharacter = verbroot?.get(0)
            val secondCharacter = verbroot?.get(1)
            val thirdCharacter = verbroot?.get(2)
            val madhimajhool: List<*>
            var mudharay: List<*>? = null
            val amr: MutableList<*>
            val nahiamr: List<*>
            val madhi: List<*>
            var mudharaymajhool: List<*>? = null
            val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter!!, secondCharacter!!, thirdCharacter!!)
            val unaugmentedTrilateralRoot =
                OSarfDictionary.instance.getUnaugmentedTrilateralRoots(verbroot, unaugmentedFormula)
            if (unaugmentedTrilateralRoot != null) {

                madhi = ActivePastConjugator.instance.createVerbList(unaugmentedTrilateralRoot)

                madhimajhool =
                    PassivePastConjugator.instance.createVerbList(unaugmentedTrilateralRoot)

                when (verbmood) {
                    "Indicative" -> {
                        mudharay = ActivePresentConjugator.instance
                            .createNominativeVerbList(unaugmentedTrilateralRoot)
                        mudharaymajhool = PassivePresentConjugator.instance
                            .createNominativeVerbList(unaugmentedTrilateralRoot)
                    }

                    "Subjunctive" -> {
                        mudharay = ActivePresentConjugator.instance
                            .createAccusativeVerbList(unaugmentedTrilateralRoot)
                        mudharaymajhool = PassivePresentConjugator.instance
                            .createAccusativeVerbList(unaugmentedTrilateralRoot)
                    }

                    "Jussive" -> {
                        mudharay = ActivePresentConjugator.instance
                            .createJussiveVerbList(unaugmentedTrilateralRoot)
                        mudharaymajhool = PassivePresentConjugator.instance
                            .createJussiveVerbList(unaugmentedTrilateralRoot)
                    }

                    "Emphasized" -> {
                        mudharay = ActivePresentConjugator.instance
                            .createEmphasizedVerbList(unaugmentedTrilateralRoot)
                        mudharaymajhool = PassivePresentConjugator.instance
                            .createEmphasizedVerbList(unaugmentedTrilateralRoot)
                    }
                }
                nahiamr = ActivePresentConjugator.instance
                    .createJussiveVerbList(unaugmentedTrilateralRoot)
                amr = UnaugmentedImperativeConjugator.instance
                    .createVerbList(unaugmentedTrilateralRoot).toMutableList()

                val madhiconjresult = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedTrilateralRoot, rule!!.kov, madhi as MutableList<*>,
                    SystemConstants.PAST_TENSE, true, true
                                                                                  )




                val madhimajhoolconj = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedTrilateralRoot, rule.kov, madhimajhool as MutableList<*>,
                    SystemConstants.PAST_TENSE, false, true
                                                                                   )
                val mudharayconj = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedTrilateralRoot, rule.kov, mudharay!! as MutableList<*>,
                    SystemConstants.PRESENT_TENSE, true, true
                                                                               )
                val mudharaymajhoolconj = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedTrilateralRoot, rule.kov, mudharaymajhool!! as MutableList<*>,
                    SystemConstants.PRESENT_TENSE, false, true
                                                                                      )
                val amrconj = UnaugmentedTrilateralModifier.instance.build(
                    unaugmentedTrilateralRoot, rule.kov, amr,
                    SystemConstants.NOT_EMPHASIZED_IMPERATIVE_TENSE, true, true
                                                                          )

                //ismfale and ismMafool
                val conjugatedIsmFael =
                    UnaugmentedTrilateralActiveParticipleConjugator.instance.createNounList(
                        unaugmentedTrilateralRoot,
                        unaugmentedTrilateralRoot.conjugation!!
                                                                                           )
                val faelconjugatoinResult = ActiveParticipleModifier.instance
                    .build(unaugmentedTrilateralRoot, rule.kov, conjugatedIsmFael, "")

                val conjugatedIsmMafool =
                    UnaugmentedTrilateralPassiveParticipleConjugator.instance.createNounList(
                        unaugmentedTrilateralRoot,
                        unaugmentedTrilateralRoot.conjugation!!
                                                                                            )
                val mafoolconjugationresult = PassiveParticipleModifier.instance
                    .build(unaugmentedTrilateralRoot, rule.kov, conjugatedIsmMafool, "")


                val conjugator = StandardInstrumentalConjugator.instance
                val modifier = InstrumentalModifier.instance

                val mifal = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَل")
                val ismAalaMifalunResult: ConjugationResult =
                    modifier.build(unaugmentedTrilateralRoot, rule.kov, mifal, "مِفْعَل")
                val finalAlamifal = ismAalaMifalunResult.finalResult
                val mifalatun = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَلَة")
                val ismALaMifalatunResult: ConjugationResult =
                    modifier.build(unaugmentedTrilateralRoot, rule.kov, mifalatun, "مِفْعَلَة")
                val finalAlamifalatun = ismALaMifalatunResult.finalResult
                val mifaal = conjugator.createNounList(unaugmentedTrilateralRoot, "مِفْعَال")
                val ismAlaMifaalun: ConjugationResult =
                    modifier.build(unaugmentedTrilateralRoot, rule.kov, mifaal, "مِفْعَال")
                val finalAlamifaal = ismAlaMifaalun.finalResult
                val faalatun = conjugator.createNounList(unaugmentedTrilateralRoot, "فَعَّالَة")
                val conjResultfaalatun: ConjugationResult =
                    modifier.build(unaugmentedTrilateralRoot, rule.kov, faalatun, "فَعَّالَة")
                //zarf
                val zarfconjugator = TimeAndPlaceConjugator.instance
                val zarfmodifier = TimeAndPlaceModifier.instance
                val IsmZarfMafalunResult =
                    zarfconjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعَل")
                val ismZarafMafalunResult = zarfmodifier.build(
                    unaugmentedTrilateralRoot,
                    rule.kov,
                    IsmZarfMafalunResult,
                    "مَفْعَل"
                                                              )
                val zarfinalmafal = ismZarafMafalunResult.finalResult
                val mafilconjugationlist =
                    zarfconjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعِل")
                val ismZarfMafilunResult = zarfmodifier.build(
                    unaugmentedTrilateralRoot,
                    rule.kov,
                    mafilconjugationlist,
                    "مَفْعِل"
                                                             )
                val zarffinalmafil = ismZarfMafilunResult.finalResult
                val mafalatunconjugationlist =
                    zarfconjugator.createNounList(unaugmentedTrilateralRoot, "مَفْعَلَة")
                val ismZarfMafalatunResult = zarfmodifier.build(
                    unaugmentedTrilateralRoot,
                    rule.kov,
                    mafalatunconjugationlist,
                    "مَفْعَلَة"
                                                               )
                val zarffinalmafalatun = ismZarfMafalatunResult.finalResult


                amrobj = amrconj.amrandnahi
                val la = "لا "
                val new = AmrNahiAmr()
                val fornahiamr = nahiamr.subList(6, 11)

                new.anta = la + fornahiamr[0].toString()
                new.antuma = la + fornahiamr[1].toString()
                new.antum = la + fornahiamr[2].toString()
                new.anti = la + fornahiamr[3].toString()
                new.antumaf = la + fornahiamr[1].toString()
                new.antunna = la + fornahiamr[4].toString()

                arrayofamrobj.add(amrobj)
                arrayofamrobj.add(new)

                faelObj = faelconjugatoinResult.faelMafool
                mafoolObj = mafoolconjugationresult.faelMafool

                skabeerismobj.add(faelObj)
                skabeerismobj.add(mafoolObj)
                //     arrayofskabeerismobj.add(skabeerismobj)
                madhiobj = madhiconjresult.madhiMudharay
                madhimajhoolobj = madhimajhoolconj.madhiMudharay


                mudharyobj = mudharayconj.madhiMudharay
                mudharaymajhoolobj = mudharaymajhoolconj.madhiMudharay

                 alamifal=ismAalaMifalunResult.alaMifalun
                 alaMifaalun=ismAlaMifaalun.alaMifaalun
                alaMifalatun=ismALaMifalatunResult.alaMifalatun
                 zarfMafilun =ismZarfMafilunResult.zarfMafilun
                 zarfMafalun=ismZarafMafalunResult.zarfMafalun
                 zarfMafalatun=ismZarfMafalatunResult.zarfMafalatun
                ismalamifalunarr.add(alamifal)
                ismalamifalatunarr.add(alaMifalatun)
                ismaalamifaalunarr.add(alaMifaalun)
                ismZarfMafilunarr.add(zarfMafilun)
                ismzarfmafalatunarr.add(zarfMafalatun)
                ismzarfmafalunarr.add(zarfMafalun)


                madhimuhdarymainobj.add(madhiobj)
                madhimuhdarymainobj.add(madhimajhoolobj)
                madhimuhdarymainobj.add(mudharyobj)
                madhimuhdarymainobj.add(mudharaymajhoolobj)

                verbDetailsobj.verbtype = rule.desc.toString()
                verbDetailsobj.babname = unaugmentedTrilateralRoot.conjugationname.toString()
                verbDetailsobj.mazeedormujarad = "mujarrad"
                verbDetailsobj.wazannumberorname = unaugmentedTrilateralRoot.rulename.toString()
                verbDetailsobj.verbroot = verbroot

                arrayofverbdetails.add(verbDetailsobj)
                //   arrayofskabeermadhimudharay.add(madhimuhdarymainobj)
                finalList.add(madhimuhdarymainobj)
                finalList.add(skabeerismobj)
                finalList.add(arrayofamrobj)
                finalList.add(arrayofverbdetails)
                val ismalazarf = IsmAlaZarfSagheer(
                    finalIsmAlaMifalatun.filterIsInstance<IsmAlaMifalatun>(),
                    finalIsmAlaMifaalun.filterIsInstance<IsmAlaMifaalun>(),
                    finalIsmAlaMifalun.filterIsInstance<IsmAlaMifalun>(),
                    ismZarfMafalatunList.filterIsInstance<IsmZarfMafalatun>(),
                    ismZarfMafalunList.filterIsInstance<IsmZarfMafalun>(),
                    ismZarfMafilunList.filterIsInstance<IsmZarfMafilun>()
                )

                ismalazarf.ismAlaMifal = finalAlamifal[0].toString()
                ismalazarf.ismALAMifalatun = finalAlamifalatun[1].toString()
                ismalazarf.ismAlaMifaal = finalAlamifaal[0].toString()

                ismalazarf.zarfMafilun = zarffinalmafil[0].toString()
                ismalazarf.zarfMafalun = zarfinalmafal[0].toString()
                ismalazarf.zarfMafalatun = zarffinalmafalatun[1].toString()
                val ismalazarfobject = ArrayList<IsmAlaZarfSagheer>()
                ismalazarfobject.add(ismalazarf)
                finalList.add(ismalazarfobject)

                finalList.add(ismalamifalunarr)
                finalList.add(ismalamifalatunarr)
                finalList.add(ismaalamifaalunarr)
                finalList.add(ismZarfMafilunarr)

                finalList.add(ismzarfmafalatunarr)
                finalList.add(ismzarfmafalunarr)





                //  skabeer.add((ArrayList) strings);
                return finalList
            }
            return finalList
        }*/

    private fun buildMazeedListold(
        verbmood: String,
        verbroot: String,
        augmentedFormula: String,
    ): ArrayList<ArrayList<*>> {
        val augmentedRoot =
            OSarfDictionary.instance.getAugmentedTrilateralRoot(verbroot, augmentedFormula)
        var ismFael: List<*>
        var ismMafool: List<*>
        var madhimajhool: List<*>
        lateinit var mudharay: List<*>
        var amr: MutableList<*>
        var nahiamr: MutableList<*>
        var madhi: List<*>
        lateinit var mudharaymajhool: List<*>
        val firstCharacter = verbroot[0]
        val secondCharacter = verbroot[1]
        val thirdCharacter = verbroot[2]
        val skabeer = ArrayList<ArrayList<*>>()
        val finalList = ArrayList<ArrayList<*>>()
        val madhimuhdarymainobj = ArrayList<MadhiMudharay>()
        val skabeerismobj = ArrayList<FaelMafool>()
        val arrayofamrobj = ArrayList<Amr>()
        val arrayofverbdetails = ArrayList<VerbDetails>()
        val madhimajhoolobj: MadhiMudharay
        val mudharyobj: MadhiMudharay
        val mudharaymajhoolobj: MadhiMudharay
        val madhiobj: MadhiMudharay
        val faelObj: FaelMafool
        val mafoolObj: FaelMafool
        val amrobj: Amr
        val verbDetailsobj = VerbDetails()



        if (augmentedRoot != null) {
            val rule = KovRulesManager.instance.getTrilateralKovRule(firstCharacter, secondCharacter, thirdCharacter)
            madhi = AugmentedActivePastConjugator.instance
                .createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            madhimajhool = AugmentedPassivePastConjugator.instance
                .createVerbList(augmentedRoot, augmentedRoot.form!!.toInt())
            when (verbmood) {
                "Indicative" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.nominativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.nominativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                }

                "Subjunctive" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.accusativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.accusativeConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                }

                "Jussive" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.jussiveConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                }

                "Emphasized" -> {
                    mudharay =
                        AugmentedActivePresentConjugator.instance.emphasizedConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                    mudharaymajhool =
                        AugmentedPassivePresentConjugator.instance.emphasizedConjugator.createVerbList(
                            augmentedRoot,
                            augmentedFormula.toInt()
                        )
                }
            }



            nahiamr =
                AugmentedActivePresentConjugator.instance.jussiveConjugator.createVerbList(
                    augmentedRoot,
                    augmentedFormula.toInt()
                )
                    .toMutableList()
            amr = AugmentedImperativeConjugator.instance.notEmphsizedConjugator.createVerbList(
                augmentedRoot,
                augmentedFormula.toInt()
            )
                .toMutableList()
            val madhiconjresult = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule!!.kov, augmentedRoot.form!!.toInt(), madhi,
                SystemConstants.PAST_TENSE, true, true
            )
            val madhimajhoolconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), madhimajhool,
                SystemConstants.PAST_TENSE, true, true
            )
            val mudharayconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), mudharay,
                SystemConstants.PRESENT_TENSE, true, true
            )
            val mudharaymajhoolconj =
                AugmentedTrilateralModifier.instance.build(
                    augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(),
                    mudharaymajhool,
                    SystemConstants.PRESENT_TENSE, false, true
                )
            val amrconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), amr,
                SystemConstants.NOT_EMPHASIZED_IMPERATIVE_TENSE, true, true
            )
            val nahiamrconj = AugmentedTrilateralModifier.instance.build(
                augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), nahiamr,
                SystemConstants.PRESENT_TENSE, true, true
            )
            ismFael = AugmentedTrilateralActiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedFormula.toInt())
            val ismfaleresult =
                org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
                    .build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismFael, true)

            ismMafool = AugmentedTrilateralPassiveParticipleConjugator.instance
                .createNounList(augmentedRoot, augmentedFormula.toInt())
            val ismmafoolresult =
                org.sj.nounConjugation.trilateral.augmented.modifier.activeparticiple.ActiveParticipleModifier.instance
                    .build(augmentedRoot, rule.kov, augmentedRoot.form!!.toInt(), ismMafool, true)
            val la = "لا "
            amrobj = amrconj.amrandnahi
            val new = Amr()
            val fornahiamr = nahiamr.subList(6, 11)

            new.anta = la + fornahiamr[0].toString()
            new.antuma = la + fornahiamr[1].toString()
            new.antum = la + fornahiamr[2].toString()
            new.anti = la + fornahiamr[3].toString()
            new.antumaf = la + fornahiamr[1].toString()
            new.antunna = la + fornahiamr[4].toString()


            arrayofamrobj.add(amrobj)
            arrayofamrobj.add(new)

            faelObj = ismfaleresult.faelMafool
            mafoolObj = ismmafoolresult.faelMafool

            skabeerismobj.add(faelObj)
            skabeerismobj.add(mafoolObj)
            //     arrayofskabeerismobj.add(skabeerismobj)
            madhiobj = madhiconjresult.madhiMudharay
            madhimajhoolobj = madhimajhoolconj.madhiMudharay


            mudharyobj = mudharayconj.madhiMudharay
            mudharaymajhoolobj = mudharaymajhoolconj.madhiMudharay
            madhimuhdarymainobj.add(madhiobj)
            madhimuhdarymainobj.add(madhimajhoolobj)
            madhimuhdarymainobj.add(mudharyobj)
            madhimuhdarymainobj.add(mudharaymajhoolobj)

            verbDetailsobj.verbtype = rule.desc.toString()
            verbDetailsobj.babname = augmentedRoot.babname.toString()
            verbDetailsobj.mazeedormujarad = "mazeed"
            verbDetailsobj.wazannumberorname = augmentedRoot.form!!
            verbDetailsobj.verbroot = verbroot
            finalList.add(madhimuhdarymainobj)
            finalList.add(skabeerismobj)
            finalList.add(arrayofamrobj)
            arrayofverbdetails.add(verbDetailsobj)
            finalList.add(arrayofverbdetails)









            return finalList//buildAugmentedLists
        }
        return finalList
    }





    companion object {
        val instance = GatherAll()
    }
}