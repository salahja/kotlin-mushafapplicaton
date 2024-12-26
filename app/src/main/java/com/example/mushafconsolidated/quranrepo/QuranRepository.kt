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

import com.example.mushafconsolidated.DAO.NewMudhafDao


import com.example.mushafconsolidated.DAO.NounCorpusDao

import com.example.mushafconsolidated.DAO.QuranDao
import com.example.mushafconsolidated.DAO.SifaDao
import com.example.mushafconsolidated.DAO.SifaMudhafDao
import com.example.mushafconsolidated.DAO.VerbCorpusDao
import com.example.mushafconsolidated.DAO.grammarRulesDao

import com.example.mushafconsolidated.DAO.surahsummaryDao


import com.example.mushafconsolidated.Entities.AbsoluteNegationEnt


import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity





import com.example.mushafconsolidated.Entities.NegationEnt




import com.example.mushafconsolidated.Entities.NounCorpus

import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.SifaMudhafEnt

import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.hanslexicon

import com.example.mushafconsolidated.Entities.lanerootdictionary
import com.example.mushafconsolidated.Entities.surahsummary
import javax.inject.Inject



 class QuranRepository @Inject constructor(


     var negationsDao: NegationDao,
     var sifaMudhafDao: SifaMudhafDao,
     var absoluteNegationDao:AbsoluteNegationDao,

     var corpusDao: CorpusExpandedDao,
     var qurandao: QuranDao,
     val ssummary: surahsummaryDao,
     val chaptersdao: AnaQuranChapterDao,






     val bookm: BookMarkDao,

     val hansdao: HansDao,
     val lanesdao: LaneRootDao,



     val nouncorpusdao: NounCorpusDao,

     val verbcorpusdao: VerbCorpusDao,

     val mousufSifa: SifaDao,
     val mudhafDao: NewMudhafDao,

     val lughatdao: LughatDao,
     val grammarrulesDao: grammarRulesDao,


     ) {

    fun getQuranCorpusBysurah(cid: Int): List<CorpusEntity> =
        corpusDao.getCorpusVersesBySurah(cid)






    fun getsifa(surah: Int, ayah: Int): List<SifaEntity> =
        mousufSifa.getSifaindexesBySurahAyah(surah, ayah)
     fun getsifaFilterSurah(surah: Int, ): List<SifaEntity> =
         mousufSifa.getSifaindexesBySurah(surah)

     fun getSifaMousoofFileterSurah(surah: Int, ): List<SifaMudhafEnt> =
         sifaMudhafDao.getISIfaMudhaafFilterSurah(surah)

     fun getSifaMousoofFileterSurahType(surah: Int,type: String ): List<SifaMudhafEnt> =
         sifaMudhafDao.getISIfaMudhaafFilterSurahAndType(surah,type)



    fun getsifasurah(surah: Int): List<SifaEntity> = mousufSifa.getSifaindexesBySurah(surah)


    fun getVerbRootBySurahAyahWord(surah: Int, ayah: Int, wordno: Int): List<VerbCorpus> =
        verbcorpusdao.getVerbRootsurahayahwordid(surah, ayah, wordno)

     fun getAllVerbCorpus(): List<VerbCorpus> =
         verbcorpusdao.getAllVerbs()

    fun getQuranCorpusWbw(surah: Int, ayah: Int, wordno: Int): List<CorpusEntity> =
        corpusDao.getCorpusWord(surah, ayah, wordno)

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

   fun getNouncorpusFilterSurah(surah: Int): List<NounCorpus> =
     nouncorpusdao.getQuranNounSurah(surah)

    val chapters: LiveData<List<ChaptersAnaEntity>> = chaptersdao.chaptersl()

    val bookmarlist: LiveData<List<BookMarks>> = bookm.getBookMarksLive()
    val bookmarckcollection: LiveData<List<BookMarks>> = bookm.getCollectionbygroupsLive()
    val chaptersmutable: List<ChaptersAnaEntity?>? = chaptersdao.chapterslist()



     fun getSingleChapters(cid: Int): List<ChaptersAnaEntity> =
         chaptersdao.getSingleChapters(cid)

    fun getQuranCorpusWbwbysurah(cid: Int): List<CorpusEntity> =
      corpusDao.getCorpusVersesBySurah(cid)

    fun getQuranCorpusWbwbyroot(root: String): List<CorpusEntity> =
      corpusDao.getQuranCorpusWbwbyroot(root)

    fun getQuranCorpusWbwbysurahAyahWord(cid: Int, aid: Int,wid:Int): List<CorpusEntity> =
        corpusDao.getCorpusWord(cid, aid,wid)

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


   fun getQuranData(chapterNo: Int): QuranData {
         return QuranData(

             allofQuran = qurandao.getQuranVersesBySurahl(chapterNo), // Fetch Quran verses
             corpusSurahWord = corpusDao.getCorpusVersesBySurah(chapterNo), //Fetch corpus data
             copusExpandSurah = corpusDao.getCorpusVersesBySurah(chapterNo),
         )
     }

     fun CorpusAndQuranDataSurah(chapterNo: Int): CorpusAndQuranData {
         return CorpusAndQuranData(

             allofQuran = qurandao.getQuranVersesBySurahl(chapterNo), // Fetch Quran verses
            copusExpandSurah = corpusDao.getCorpusVersesBySurah(chapterNo),
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
    val corpusSurahWord: List<CorpusEntity>,
    val copusExpandSurah:List<CorpusEntity>// Assuming you need this as well

)






data class ChapterData(


val allofQuran: List<QuranEntity>, // Assuming you need this as well
val corpusSurahWord: List<CorpusEntity>// Assuming you need this as well
)