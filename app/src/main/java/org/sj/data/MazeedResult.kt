package org.sj.data

import VerbDetails
import org.sj.verbConjugation.Amr
import org.sj.verbConjugation.FaelMafool
import org.sj.verbConjugation.MadhiMudharay
import org.sj.verbConjugation.NahiAmr


data class MazeedResult(
    val madhiMudharayList: List<MadhiMudharay>,
    val skabeerIsmList: List<FaelMafool>,
    val amrList: List<Amr>,
    val nahiAmrList: List<NahiAmr>,
    val verbDetailsList: List<VerbDetails?>
)

