package org.sj.data

import VerbDetails
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


data class MujarradResult(
    val madhiMudharayList: List<MadhiMudharay>,
    val ismFaelMafoolList: List<FaelMafool>,
    val amrList: List<Amr>,
    val nahiAmr:  List<NahiAmr>,
    val ismZarfMafilun: List<IsmZarfMafilun>,
    val ismZarfMafalatun: List <IsmZarfMafalatun>,
    val ismZarfMafalun: MutableList<IsmZarfMafalun>,
    val  ismAlaMifal: List<IsmAlaMifalun>,
    val ismALAMifalatun: List<IsmAlaMifalatun>,
    val  ismAlaMifaalun: List<IsmAlaMifaalun>,


    val verbDetailsList: List<VerbDetails?>
)

