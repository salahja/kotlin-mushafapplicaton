package com.example.mushafconsolidated.quranrepo


import androidx.lifecycle.LiveData
import com.example.mushafconsolidated.DAO.AbsoluteNegationDao
import com.example.mushafconsolidated.DAO.AnaQuranChapterDao

import com.example.mushafconsolidated.DAO.BookMarkDao
import com.example.mushafconsolidated.DAO.CorpusExpandedDao


import com.example.mushafconsolidated.DAO.HansDao



import com.example.mushafconsolidated.DAO.LaneRootDao
import com.example.mushafconsolidated.DAO.LughatDao



import com.example.mushafconsolidated.DAO.NegationDao
import com.example.mushafconsolidated.DAO.NewKanaDao
import com.example.mushafconsolidated.DAO.NewMudhafDao
import com.example.mushafconsolidated.DAO.NewNasbDao
import com.example.mushafconsolidated.DAO.NewShartDAO
import com.example.mushafconsolidated.DAO.NounCorpusDao

import com.example.mushafconsolidated.DAO.QuranDao
import com.example.mushafconsolidated.DAO.SifaDao
import com.example.mushafconsolidated.DAO.SifaMudhafDao
import com.example.mushafconsolidated.DAO.VerbCorpusDao
import com.example.mushafconsolidated.DAO.grammarRulesDao

import com.example.mushafconsolidated.DAO.surahsummaryDao

import com.example.mushafconsolidated.DAO.wbwDao
import com.example.mushafconsolidated.Entities.AbsoluteNegationEnt


import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity





import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.NewKanaEntity
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.Entities.NounCorpus

import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.SifaMudhafEnt

import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.hanslexicon

import com.example.mushafconsolidated.Entities.lanerootdictionary
import com.example.mushafconsolidated.Entities.surahsummary
import com.example.mushafconsolidated.model.QuranCorpusWbw
import javax.inject.Inject



 class QuranRepository @Inject constructor(


     var negationsDao: NegationDao,
     var sifaMudhafDao: SifaMudhafDao,
     var absoluteNegationDao:AbsoluteNegationDao,
     var wbwdao:wbwDao,
     var corpusDao: CorpusExpandedDao,
     var qurandao: QuranDao,
     val ssummary: surahsummaryDao,
     val chaptersdao: AnaQuranChapterDao,






     val bookm: BookMarkDao,

     val hansdao: HansDao,
     val lanesdao: LaneRootDao,



     val nouncorpusdao: NounCorpusDao,

     val verbcorpusdao: VerbCorpusDao,
     val kanaDao: NewKanaDao,
     val shartDAO: NewShartDAO,
     val nasbDao: NewNasbDao,
     val mousufSifa: SifaDao,
     val mudhafDao: NewMudhafDao,

     val lughatdao: LughatDao,
     val grammarrulesDao: grammarRulesDao,


     ) {

    fun getQuranCorpusBysurah(cid: Int): List<CorpusEntity> =
        corpusDao.getVersesBySurah(cid)


    fun getkana(surah: Int, ayah: Int): List<NewKanaEntity> =
        kanaDao.getkanabysurahAyah(surah, ayah)

    fun getshart(surah: Int, ayah: Int): List<NewShartEntity> =
        shartDAO.getShartBySurahAyah(surah, ayah)

    fun getnasab(surah: Int, ayah: Int): List<NewNasbEntity> =
        nasbDao.getHarfNasbIndicesSurahAyah(surah, ayah)!!

    fun getmudhaf(surah: Int, ayah: Int): List<NewMudhafEntity> =
        mudhafDao.getMudhafSurahAyah(surah, ayah)



    fun getsifa(surah: Int, ayah: Int): List<SifaEntity> =
        mousufSifa.getSifaindexesBySurahAyah(surah, ayah)
     fun getsifaFilterSurah(surah: Int, ): List<SifaEntity> =
         mousufSifa.getSifaindexesBySurah(surah)

     fun getSifaMousoofFileterSurah(surah: Int, ): List<SifaMudhafEnt> =
         sifaMudhafDao.getISIfaMudhaafFilterSurah(surah)

     fun getSifaMousoofFileterSurahType(surah: Int,type: String ): List<SifaMudhafEnt> =
         sifaMudhafDao.getISIfaMudhaafFilterSurahAndType(surah,type)


     fun getkanasurah(surah: Int): List<NewKanaEntity> = kanaDao.getkanabysurah(surah)

    fun getshartsurah(surah: Int): List<NewShartEntity> = shartDAO.getShartBySurah(surah)

    fun getnasabsurah(surah: Int): List<NewNasbEntity> = nasbDao.getHarfNasbIndices(surah)
    fun getmudhafsurah(surah: Int): List<NewMudhafEntity> = mudhafDao.getMudhafSurah(surah)
    fun getsifasurah(surah: Int): List<SifaEntity> = mousufSifa.getSifaindexesBySurah(surah)


    fun getVerbRootBySurahAyahWord(surah: Int, ayah: Int, wordno: Int): List<VerbCorpus> =
        verbcorpusdao.getVerbRootsurahayahwordid(surah, ayah, wordno)

     fun getAllVerbCorpus(): List<VerbCorpus> =
         verbcorpusdao.getAllVerbs()

    fun getQuranCorpusWbw(surah: Int, ayah: Int, wordno: Int): List<QuranCorpusWbw> =
        qurandao.getQuranCorpusWbw(surah, ayah, wordno)

     fun getCorpusEntityFilterbywordno(surah: Int, ayah: Int, wordno: Int): List<CorpusEntity> =
         corpusDao.getCorpusWord(surah, ayah, wordno)

     fun getCorpusEntityFilterSurahAya(surah: Int, ayah: Int): List<CorpusEntity> =
         corpusDao.getCorpusWordSurahAya(surah, ayah)

     fun getAbsoluteNegationFilerSurahAyah(surah: Int, ayah: Int): List<AbsoluteNegationEnt> =
         absoluteNegationDao.getAbsoluteNegationFilterSurahAyah(surah, ayah)

     fun getAbsoluteNegationAll(): List<AbsoluteNegationEnt> =
         absoluteNegationDao.getAbsoluteNegationAll()

     fun getAbsoluteNegationFilterSurah(surah: Int): List<AbsoluteNegationEnt> =
         absoluteNegationDao.getAbsoluteNegationFilterSurah(surah)

















     fun getNegationfilterSurahType(surah: Int,type:String): List<NegationEnt> =
         negationsDao.getINegationFilterSurahAndType(surah,type)

     fun getNegationFilerSurahAyaType(surah: Int, ayah: Int, type:String): List<NegationEnt> =
         negationsDao.getNasabFilterSurahAyahTypeSubType(surah, ayah,type)


     fun geNegationFilerSurahAyah(surah: Int, ayah: Int): List<NegationEnt> =
         negationsDao.getNegationFilterSurahAyah(surah, ayah)

     fun getNegationAll(): List<NegationEnt> =
         negationsDao.getNegationAll()

     fun getNegationFilterSurah(surah: Int): List<NegationEnt> =
         negationsDao.getINegationFilterSurah(surah)









     fun getCorpusEntityFilterSurah(surah: Int, ): List<CorpusEntity> =
         corpusDao.getVersesBySurahLive(surah)





    fun getNouncorpus(surah: Int, ayah: Int, wordno: Int): List<NounCorpus> =
        nouncorpusdao.getQuranNounsBysurahayahword(surah, ayah, wordno)

     fun getNouncorpusFilterSurahAyah(surah: Int, ayah: Int): List<NounCorpus> =
         nouncorpusdao.getQuranNounAyah(surah, ayah)

    val chapters: LiveData<List<ChaptersAnaEntity>> = chaptersdao.chaptersl()

    val bookmarlist: LiveData<List<BookMarks>> = bookm.getBookMarksLive()
    val bookmarckcollection: LiveData<List<BookMarks>> = bookm.getCollectionbygroupsLive()
    val chaptersmutable: List<ChaptersAnaEntity?>? = chaptersdao.chapterslist()



     fun getSingleChapters(cid: Int): List<ChaptersAnaEntity> =
         chaptersdao.getSingleChapters(cid)

    fun getQuranCorpusWbwbysurah(cid: Int): List<QuranCorpusWbw> =
        qurandao.getQuranCorpusWbwbysurah(cid)

    fun getQuranCorpusWbwbyroot(root: String): List<QuranCorpusWbw> =
        qurandao.getQuranCorpusWbwbyRoot(root)

    fun getQuranCorpusWbwbysurahAyah(cid: Int, aid: Int): List<QuranCorpusWbw> =
        qurandao.getQuranCorpusWbwSurhAyah(cid, aid)

    fun getsurahbychap(cid: Int): List<QuranEntity> = qurandao.getQuranVersesBySurahl(cid)


    fun getsurahbyayahlist(cid: Int, ayid: Int): List<QuranEntity> =
        qurandao.getsurahayahVerseslist(cid, ayid)

    fun getHansRoot(cid: String): List<hanslexicon> = hansdao.getHansDefinitionl(cid)
    fun getLanesRoot(cid: String): List<lanerootdictionary> = lanesdao.getLanesRootDefinition(cid)
    fun getsurahbyayah(cid: Int, ayid: Int): List<QuranEntity> =
        qurandao.getsurahayahVersesl(cid, ayid)

    fun getSurahSummary(cid: Int): LiveData<List<surahsummary>> = ssummary.getSurahSummary(cid)








    suspend fun insertlive(entity: BookMarks) {
        bookm.insertBookmark(entity)


    }


     suspend fun delete(entity: BookMarks) {
        bookm.deletebookmarkl(entity)


    }

    suspend fun deletecollection(entity: String) {
        bookm.deleteCollectionl(entity)


    }
   suspend fun getChapterData(chapterNo: Int): ChapterData {
        return ChapterData(

            allofQuran = qurandao.getQuranVersesBySurahl(chapterNo), // Fetch Quran verses
            corpusSurahWord = qurandao.getQuranCorpusWbwbysurah(chapterNo) //Fetch corpus data
        )
    }


      fun getQuranData(chapterNo: Int): QuranData {
         return QuranData(

             allofQuran = qurandao.getQuranVersesBySurahl(chapterNo), // Fetch Quran verses
             corpusSurahWord = qurandao.getQuranCorpusWbwbysurah(chapterNo), //Fetch corpus data
             copusExpandSurah = corpusDao.getVersesBySurah(chapterNo),
         )
     }

     fun CorpusAndQuranDataSurah(chapterNo: Int): CorpusAndQuranData {
         return CorpusAndQuranData(

             allofQuran = qurandao.getQuranVersesBySurahl(chapterNo), // Fetch Quran verses
            copusExpandSurah = corpusDao.getVersesBySurah(chapterNo),
         )
     }

     fun CorpusAndQuranDataSurahAyah(chapterNo: Int,verseid:Int): CorpusAndQuranData {
         return CorpusAndQuranData(

             allofQuran = qurandao.getQuranVersesBySurahAyah(chapterNo,verseid), // Fetch Quran verses
             copusExpandSurah = corpusDao.getVersesBySurahAndAya(chapterNo,verseid),
         )
     }


 }



data class CorpusAndQuranData(

    val allofQuran: List<QuranEntity>, // Assuming you need this as well

    val copusExpandSurah:List<CorpusEntity>// Assuming you need this as well

)

data class QuranData(

    val allofQuran: List<QuranEntity>, // Assuming you need this as well
    val corpusSurahWord: List<QuranCorpusWbw>,
    val copusExpandSurah:List<CorpusEntity>// Assuming you need this as well

)






data class ChapterData(


val allofQuran: List<QuranEntity>, // Assuming you need this as well
val corpusSurahWord: List<QuranCorpusWbw>// Assuming you need this as well
)