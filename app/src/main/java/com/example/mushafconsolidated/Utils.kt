package com.example.mushafconsolidated


import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.mushafconsolidated.DAO.BookMarkDao
import com.example.mushafconsolidated.DAO.BookMarksPojo
import com.example.mushafconsolidated.Entities.AccusativePojo
import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.CorpusExpandWbwPOJO
import com.example.mushafconsolidated.Entities.CorpusNounWbwOccurance
import com.example.mushafconsolidated.Entities.CorpusVerbWbwOccurance
import com.example.mushafconsolidated.Entities.FutureTenceListingPojo

import com.example.mushafconsolidated.Entities.GrammarRules


import com.example.mushafconsolidated.Entities.InMaListingPOJO

import com.example.mushafconsolidated.Entities.NasbListingPojo
import com.example.mushafconsolidated.Entities.NegationEnt


import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.NounCorpusBreakup
import com.example.mushafconsolidated.Entities.OutSideDoer
import com.example.mushafconsolidated.Entities.PastTencePOJO

import com.example.mushafconsolidated.Entities.PresentTencePOJO
import com.example.mushafconsolidated.Entities.Qari
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.RootVerbDetails
import com.example.mushafconsolidated.Entities.RootWordDetails
import com.example.mushafconsolidated.Entities.ShartListingPojo
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.SifaListingPojo
import com.example.mushafconsolidated.Entities.SifaMudhafEnt
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.VerbCorpusBreakup
import com.example.mushafconsolidated.Entities.hanslexicon
import com.example.mushafconsolidated.Entities.jsonsurahentity

import com.example.mushafconsolidated.Entities.lanerootdictionary
import com.example.mushafconsolidated.Entities.lughat
import com.example.mushafconsolidated.Entities.lysaEnt
import com.example.mushafconsolidated.Entities.qurandictionary
import com.example.mushafconsolidated.Entities.quranexplorer

import com.example.mushafconsolidated.model.Juz
import com.example.mushafconsolidated.model.QuranEntityCorpusEntityWbwEntity
import database.entity.AllahNames
import mufradat.MufradatEntity


//import com.example.mushafconsolidated.Entities.JoinVersesTranslationDataTranslation;
class Utils {
    private var thiscontext: Context? = null

    constructor(context: Context?) {
        /*   database = context?.let { QuranAppDatabase.Companion.getDatabase(context) }!!
          database= QuranAppDatabase.getDatabase(context)
          thiscontext = context*/

        database = context?.let { QuranAppDatabase.getInstance(it) }!!
        thiscontext = context

    }

    constructor()

    private class deleteWordAsyncTask(private val mAsyncTaskDao: BookMarkDao) :
        AsyncTask<BookMarks?, Void?, Void?>() {


        override fun doInBackground(vararg params: BookMarks?): Void? {
            params[0]?.let { mAsyncTaskDao.deletebookmark(it) }
            return null
        }
    }




    fun getShaikhTafseer(id: Int): List<MufradatEntity> {
        Log.d(TAG, "getShaikhTafseer: started")
        return database.MufradatDao().getShaikTafseer(id)
    }
    fun getShaikhTafseerAya(id: Int,aid:Int): MutableList<MufradatEntity>? {
        Log.d(TAG, "getShaikhTafseer: started")
        return database.MufradatDao().getShaikTafseerAya(id,aid)
    }


    fun getCorpusVersesBySurah(cid:Int ): List<CorpusEntity> {
        return database.getCorpusExpandDao().getCorpusVersesBySurah(cid)
    }




    fun getNegationFilterSurahAyahType(cid: Int, aid: Int,type: String ): List<NegationEnt> {
        return  database.NegationDao().getNegationFilterSurahAyahType(cid, aid,type)

    }
    fun getFilterSurahAyahType(cid: Int, aid: Int,type: String ): List<SifaMudhafEnt> {
        return  database.SifaMudhafDao().getSIfaMudhaafFilterSurahAyahType(cid, aid,type)

    }


    fun getNegationFilterSurahAndType(cid: Int,type:String,   ): List<NegationEnt> {
        return  database.NegationDao().getINegationFilterSurahAndType(cid,type)

    }
    fun getNegationFilterSurahAndTypetemp( type:String,   ): List<NegationEnt> {
        return  database.NegationDao().getNegationFilterSurahAndTypetemp(type)

    }






    fun geTNegatonFilerSurahAyah(cid: Int, aid: Int, ): List<NegationEnt> {
        return  database.NegationDao().getNegationFilterSurahAyah(cid, aid)

    }

    fun getNegationall(): List<NegationEnt> {
        return  database.NegationDao().getNegationAll()

    }



    fun getNegationFilterSurah(cid: Int, aid: Int, ): List<NegationEnt> {
        return  database.NegationDao().getINegationFilterSurah(cid)

    }

    fun getNasabFilterSubType(type:String): List<NegationEnt> {
        return  database.NegationDao().getNasabFilterSubType(type)

    }




    fun getSurahJson(sid:    Int): List<jsonsurahentity?>? {
        Log.d(TAG, "getQuranRoot: started")
        return database.JasonSurahDao().getSurahJson(sid)
    }
    fun getSurahJson(): List<jsonsurahentity> {
        Log.d(TAG, "getQuranRoot: started")
        return database.JasonSurahDao().getSurahJsonall()
    }
    fun getAllAnaChapters(): List<ChaptersAnaEntity?>? {

        return database.AnaQuranChapterDao().chapterslist()
    }
    fun outSideDoer(): List<OutSideDoer> {

        return database.OutsideDoerDAO().outsidedoerlist()
    }
    fun outSideDoerSurah(cid: Int): List<OutSideDoer> {

        return database.OutsideDoerDAO().doerlistSurah(cid)
    }

    fun getCorpusAll(): List<CorpusEntity> {

        return database.getCorpusExpandDao().getCorpusAll()
    }



    fun getSingleChapter(id: Int): List<ChaptersAnaEntity?>? {
        Log.d(TAG, "getSingleChapter: started")
        return database.AnaQuranChapterDao().getSingleChapters(id)
    }

    fun getQuranRoot(id: Int, verseid: Int, wordid: Int): List<VerbCorpus> {
        Log.d(TAG, "getQuranRoot: started")
        return database.VerbCorpusDao().getVerbRootsurahayahwordid(id, verseid, wordid)
    }
    fun getAllverbs(): List<VerbCorpus> {
        Log.d(TAG, "getQuranRoot: started")
        return database.VerbCorpusDao().getAllverbs()
    }

    fun getQuranNouns(id: Int, verseid: Int, wordid: Int): List<NounCorpus> {
        Log.d(TAG, "getQuranNouns: started")
        return database.NounCorpusDao().getQuranNounsBysurahayahword(id, verseid, wordid)
    }

    fun insertBookMark(entity: BookMarks?) {
        //    database?.BookMarkDao().deleteAllBookMakrs();
        AsyncTask.execute {
            AsyncTask.execute(object : Runnable {
                override fun run() {
                    // and deleting
                    if (entity != null) {
                        database.BookMarkDao().insertBookmark(entity)
                    }
                    //  runOnUiThread(new Runnable() {
                    //  public void run() {
                    //   itemTextView.setText("item deleted");
                    //    }
                    //  });
                }
            })
        }
    }

    fun getCorpusWbwBySurah(tid: Int): List<CorpusExpandWbwPOJO?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.rootaraone || rootaratwo || rootarathree || rootarafour || rootarafive AS root_a,\n" +
                    "       CorpusExpand.surah,\n" +
                    "       CorpusExpand.ayah,\n" +
                    "       CorpusExpand.wordno,\n" +
                    "       CorpusExpand.wordcount,\n" +
                    "       Qurans.translation,\n" +
                    "       Qurans.passage_no,\n" +
                    "       Qurans.ar_irab_two,\n" +
                    "       Qurans.tafsir_kathir,\n" +
                    "       Qurans.en_transliteration,\n" +
                    "       Qurans.en_jalalayn,\n" +
                    "       Qurans.en_arberry,\n" +
                    "       Qurans.ur_jalalayn,\n" +
                    "       Qurans.ur_junagarhi,\n" +
                    "       CorpusExpand.araone,\n" +
                    "       CorpusExpand.aratwo,\n" +
                    "       CorpusExpand.arathree,\n" +
                    "       CorpusExpand.arafour,\n" +
                    "       CorpusExpand.arafive,\n" +
                    "       CorpusExpand.tagone,\n" +
                    "       CorpusExpand.tagtwo,\n" +
                    "       CorpusExpand.tagthree,\n" +
                    "       CorpusExpand.tagfour,\n" +
                    "       CorpusExpand.tagfive,\n" +
                    "       CorpusExpand.detailsone,\n" +
                    "       CorpusExpand.detailstwo,\n" +
                    "       CorpusExpand.detailsthree,\n" +
                    "       CorpusExpand.detailsfour,\n" +
                    "       CorpusExpand.detailsfive,\n" +
                    "       CorpusExpand.en,\n" +
                    "       CorpusExpand.bn,\n" +
                    "       CorpusExpand.[in],\n" +
                    "       CorpusExpand.ur,\n" +
                    "       qurans.qurantext\n" +
                    "  FROM corpusexpand,\n" +
                    "       qurans,\n" +

                    " WHERE CorpusExpand.surah == \""
                    + tid + "\""
                    + "AND \n" +

                    "       corpusexpand.surah = qurans.surah AND \n" +
                    "       corpusexpand.ayah = qurans.ayah  \n" +
                    " ORDER BY corpusexpand.surah,\n" +
                    "          corpusexpand.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getCorpusWbw(query)
    }

    fun getShart(tid:Int):List<ShartListingPojo>{
        val sqlshart:String=("select negationfulldata.surahid as surah,negationfulldata.ayahid as ayah,negationfulldata.wordfrom,negationfulldata.wordto,negationfulldata.startindex,negationfulldata.endindex,\n" +
                "negationfulldata.arabictext,negationfulldata.englishtext,negationfulldata.verse,negationfulldata.type,\n" +
                "qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from negationfulldata,qurans where negationfulldata.surahid=qurans.surah and negationfulldata.ayahid=qurans.ayah and " +
                "negationfulldata.type like '%shart%' and negationfulldata.surahid ==  \""
                + tid + "\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getShartListing(query)


    }


    fun getNASAB():List<AccusativePojo>{
        val accusative:String=("SELECT \n" +
                "    corpusexpand.surah,\n" +
                "    corpusexpand.ayah\n" +
                "    \n" +
                "FROM \n" +
                "    corpusexpand, qurans\n" +
                "WHERE \n" +
                "    (corpusexpand.tagone = \"ACC\" OR corpusexpand.tagtwo = \"ACC\" OR corpusexpand.tagthree = \"ACC\" OR corpusexpand.tagfour = \"ACC\" OR corpusexpand.tagfive = \"ACC\")\n" +
                " \n" +
                "    AND corpusexpand.surah = qurans.surah \n" +
                "    AND corpusexpand.ayah = qurans.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(accusative)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getKadaSisters():List<AccusativePojo>{

        val kada:String=("select * from corpusexpand where tagone=\"V\" and (rootaraone=\"صبح\" or rootaraone=\"كود\" or rootaraone=\"ليس\" or rootaraone=\"ظلل\") or ( tagtwo=\"V\" and rootaratwo=\"صبح\" or rootaratwo=\"كود\" or rootaratwo=\"ليس\" or rootaratwo=\"ظلل\")\n" +
                "or ( tagthree=\"V\" and rootarathree=\"صبح\" or rootarathree=\"كود\" or rootarathree=\"ليس\" or rootarathree=\"ظلل\")")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kada)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getRelativePronouns():List<AccusativePojo>{

        val kada:String=("select * from corpusexpand where tagone=\"REL\" or tagtwo=\"REL\" or tagthree=\"REL\" or tagfour=\"REL\" or tagfive=\"REL\"")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kada)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }
    fun getSubordinateClause():List<AccusativePojo>{

        val kada:String=("select * from corpusexpand where tagone=\"SUB\" or tagtwo=\"SUB\" or tagthree=\"SUB\" or tagfour=\"SUB\" or tagfive=\"SUB\"")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kada)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getSelectedChapter():List<AccusativePojo>{

        val kada:String=("select * from corpusexpand where surah=41")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kada)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }
    fun getSelectedChapters(tid:Int):List<AccusativePojo>{
        val sqlshart:String=("select * from corpusexpand where surah ==  \""
            + tid + "\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)


    }





    fun getKanaAll():List<AccusativePojo>{
        val kana:String=("select \n" +
                "*\n" +
                "    \n" +
                "FROM \n" +
                "    corpusexpand, qurans\n" +
                "WHERE \n" +
                "    (corpusexpand.lemma = \"كَانَ\" \n" +
                " \n" +
                "    AND corpusexpand.surah = qurans.surah \n" +
                "    AND corpusexpand.ayah = qurans.ayah and qurans.surah>8 ")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getLauAll():List<AccusativePojo>{
        val kana:String=("SELECT *\n" +
                "  FROM corpusexpand,\n" +
                "       qurans\n" +
                " WHERE ( (corpusexpand.araone = \"لَوْ\" AND \n" +
                "          corpusexpand.tagone = \"COND\" OR \n" +
                "          corpusexpand.aratwo = \"لَوْ\" AND \n" +
                "          corpusexpand.tagtwo = \"COND\" OR \n" +
                "          corpusexpand.arathree = \"لَوْ\" AND \n" +
                "          corpusexpand.tagthree = \"COND\" OR \n" +
                "          corpusexpand.arafour = \"لَوْ\" OR \n" +
                "          corpusexpand.arafive = \"لَوْ\") OR \n" +
                "         (corpusexpand.araone = \"لَوْلَا\" OR \n" +
                "          corpusexpand.aratwo = \"لَوْلَا\" AND \n" +
                "          corpusexpand.tagtwo = \"COND\" OR \n" +
                "          corpusexpand.arathree = \"لَوْلَا\" AND \n" +
                "          corpusexpand.tagthree = \"COND\" OR \n" +
                "          corpusexpand.arafour = \"لَوْلَا\" OR \n" +
                "          corpusexpand.arafive = \"لَوْلَا\") ) AND \n" +
                "       corpusexpand.surah = qurans.surah AND \n" +
                "       corpusexpand.ayah = qurans.ayah AND \n" +
                "       qurans.surah > 9 AND \n" +
                "       qurans.surah < 58\n" +
                " ORDER BY qurans.surah,\n" +
                "          qurans.ayah;\n")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getIMMA():List<AccusativePojo>{
        val kana:String=("SELECT *\n" +
            "  FROM corpusexpand,\n" +
            "       qurans\n" +
            " WHERE (corpusexpand.araone = \"إِمَّا\" OR \n" +
            "        corpusexpand.aratwo = \"إِمَّا\" OR \n" +
            "        corpusexpand.aratwo = \"إِمَّآ\" OR \n" +
            "        corpusexpand.arathree = \"إِمَّآ\") AND \n" +
            "       corpusexpand.surah = qurans.surah AND \n" +
            "       corpusexpand.ayah = qurans.ayah")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getInALL():List<AccusativePojo>{
        val kana:String=("SELECT *\n" +
                "  FROM corpusexpand,\n" +
                "       qurans\n" +
                " WHERE (corpusexpand.araone = \"إِن\" OR \n" +
                "        corpusexpand.aratwo = \"إِن\" OR \n" +
                "        corpusexpand.arathree = \"ئِنِ\" OR \n" +
                "        corpusexpand.arathree = \"إِي۟ن\") AND \n" +
                "       corpusexpand.surah = qurans.surah AND \n" +
                "       corpusexpand.ayah = qurans.ayah AND qurans.surah>9 AND qurans.surah<58 order by qurans.surah,qurans.ayah")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getManAmmaConditional():List<AccusativePojo>{
        val kana:String=("SELECT *\n" +
                "  FROM corpusexpand,\n" +
                "       qurans\n" +
                " WHERE (corpusexpand.araone = \"مَن\" OR \n" +
                "        corpusexpand.aratwo = \"مَن\" OR \n" +
                "        corpusexpand.arathree = \"مَن\" OR \n" +
                "        corpusexpand.araone=\"مَنْ\" OR\n" +
                "         corpusexpand.aratwo=\"مَنْ\" OR\n" +
                "                  corpusexpand.arathree=\"مَنْ\" OR\n" +
                "        corpusexpand.araone = \"أَمَّا\" OR \n" +
                "        corpusexpand.araone=\"أَمَّآ\" or\n" +
                "        \n" +
                "        corpusexpand.araone = \"مَا\" OR \n" +
                "        corpusexpand.araone=\"مَآ\" or\n" +
                "        corpusexpand.aratwo = \"أَمَّآ\" OR \n" +
                "        corpusexpand.aratwo = \"أَمَّا\" OR \n" +
                "        corpusexpand.arathree = \"مَا\" OR \n" +
                "        corpusexpand.aratwo = \"مَن\" OR \n" +
                "        corpusexpand.araone = \"مَآ\" OR \n" +
                "        corpusexpand.aratwo = \"مَنِ\") AND \n" +
                "       (corpusexpand.tagone = \"COND\" OR \n" +
                "        corpusexpand.tagtwo = \"COND\") AND \n" +
                "       corpusexpand.surah = qurans.surah AND \n" +
                "       corpusexpand.ayah = qurans.ayah AND \n" +
                "       qurans.surah > 9 AND \n" +
                "       qurans.surah < 58\n" +
                " ORDER BY qurans.surah,\n" +
                "          qurans.ayah")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }

    fun getMaMinALL():List<AccusativePojo>{
        val kana:String=("SELECT *\n" +
                "  FROM corpusexpand,\n" +
                "       qurans\n" +
                " WHERE (corpusexpand.araone = \"مَن\" OR \n" +
                "        corpusexpand.aratwo = \"مَن\" OR \n" +
                "        corpusexpand.arathree = \"مَن\" OR \n" +
                "        corpusexpand.araone=\"مَنْ\" OR\n" +
                "         corpusexpand.aratwo=\"مَنْ\" OR\n" +
                "                  corpusexpand.arathree=\"مَنْ\" OR\n" +
                "        corpusexpand.araone = \"أَمَّا\" OR \n" +
                "        corpusexpand.araone=\"أَمَّآ\" or\n" +
                "        \n" +
                "        corpusexpand.araone = \"مَا\" OR \n" +
                "        corpusexpand.aratwo = \"أَمَّآ\" OR \n" +
                "        corpusexpand.aratwo = \"أَمَّا\" OR \n" +
                "        corpusexpand.arathree = \"مَا\" OR \n" +
                "        corpusexpand.aratwo = \"مَن\" OR \n" +
                "        corpusexpand.araone = \"مَآ\" OR \n" +
                "        corpusexpand.aratwo = \"مَنِ\") AND \n" +
                "       (corpusexpand.tagone = \"COND\" OR \n" +
                "        corpusexpand.tagtwo = \"COND\") AND \n" +
                "       corpusexpand.surah = qurans.surah AND \n" +
                "       corpusexpand.ayah = qurans.ayah AND \n" +
                "       qurans.surah > 9 AND \n" +
                "       qurans.surah < 58\n" +
                " ORDER BY qurans.surah,\n" +
                "          qurans.ayah")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }






    fun getIzaAll():List<AccusativePojo>{
        val kana:String=("select \n" +
                "*\n" +
                "    \n" +
                "FROM \n" +
                "    corpusexpand, qurans\n" +
                "WHERE \n" +
                "    (corpusexpand.araone = \"إِذَا\" AND tagone==\"T\" OR corpusexpand.aratwo = \"إِذَا\"   AND tagtwo==\"T\" OR corpusexpand.arathree = \"إِذَا\"  AND tagone==\"T\" )\n" +
                " \n" +
                "    AND corpusexpand.surah = qurans.surah \n" +
                "    AND corpusexpand.ayah = qurans.ayah and qurans.surah>9 and qurans.surah<58")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(kana)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getAccusativeListing(query)

    }





    fun getSifalisting(tid:Int):List<SifaListingPojo>{
        val sqlshart:String=("select sifa.surah,sifa.ayah,sifa.startindex,sifa.endindex,sifa.wordfrom,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from sifa,qurans where sifa.surah=qurans.surah and sifa.ayah=qurans.ayah and sifa.surah ==  \""
                + tid + "\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getSifaListing(query)


    }
    fun getSifalistingType(tid:Int,type: String):List<SifaListingPojo>{
        val sqlshart:String=("select sifa.surah,sifa.ayah,sifa.startindex,sifa.endindex,sifa.wordfrom,sifa.comment,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from sifa,qurans where sifa.surah=qurans.surah and sifa.ayah=qurans.ayah and sifa.surah ==  \""
                + tid + "\""       +" and sifa.comment ==  \""+type+"\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getSifaListing(query)


    }

    fun getInMaNegationListing(tid:Int,type: String):List<InMaListingPOJO>{
        val sqlshart:String=("select negationfulldata.surahid,negationfulldata.ayahid,negationfulldata.startindex,negationfulldata.endindex,negationfulldata.wordfrom,negationfulldata.wordnoto,negationfulldata.arabictext,negationfulldata.englishtext,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from negationfulldata,qurans where negationfulldata.surahid=qurans.surah and negationfulldata.ayahid=qurans.ayah and negationfulldata.surahid ==  \""
                + tid + "\"" +" and negationfulldata.type ==  \""+type+"\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getInMaisting(query)


    }

    fun getFutureTnegaionListing(tid:Int,type:String):List<FutureTenceListingPojo>{
        val sqlshart:String=("select negationfulldata.surahid,negationfulldata.ayahid,negationfulldata.startindex,negationfulldata.endindex,negationfulldata.wordfrom,negationfulldata.wordnoto,negationfulldata.arabictext,negationfulldata.englishtext,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from negationfulldata,qurans where negationfulldata.surahid=qurans.surah and negationfulldata.ayahid=qurans.ayah and negationfulldata.surahid ==  \""
                + tid + "\"" +" and negationfulldata.type ==  \""+type+"\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getFuturNegationList(query)


    }

    fun getPresentnegaionListing(tid: Int, type: String):List<PresentTencePOJO>{
        val sqlshart:String=("select negationfulldata.surahid,negationfulldata.ayahid,negationfulldata.startindex,negationfulldata.endindex,negationfulldata.wordfrom,negationfulldata.wordnoto,negationfulldata.arabictext,negationfulldata.englishtext,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from negationfulldata,qurans where negationfulldata.surahid=qurans.surah and negationfulldata.ayahid=qurans.ayah and negationfulldata.surahid ==  \""
                + tid + "\"" +" and negationfulldata.type ==  \""+type+"\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getPresentTenceListing(query)


    }

    fun getPastnegaionListing(tid:Int, type: String):List<PastTencePOJO>{
        val sqlshart:String=("select negationfulldata.surahid,negationfulldata.ayahid,negationfulldata.startindex,negationfulldata.endindex,negationfulldata.wordfrom,negationfulldata.wordnoto,negationfulldata.arabictext,negationfulldata.englishtext,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from negationfulldata,qurans where negationfulldata.surahid=qurans.surah and negationfulldata.ayahid=qurans.ayah and negationfulldata.surahid ==  \""
                + tid + "\"" +" and negationfulldata.type ==  \""+type+"\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getPastTenceListing(query)


    }




    fun getNasb(tid:Int):List<NasbListingPojo>{
        val sqlshart:String=("select newnasb.surah,newnasb.ayah,newnasb.indexstart,newnasb.indexend,newnasb.ismstart,newnasb.ismend,\n" +
                "newnasb.khabarstart,newnasb.khabarend,newnasb.harfwordno,newnasb.ismstartwordno,newnasb.ismendwordno,\n" +
                "newnasb.khabarstartwordno,newnasb.khabarendwordno,newnasb.mahdoof,qurans.page,qurans.passage_no,qurans.qurantext,qurans.has_prostration,qurans.translation,\n" +
                "qurans.en_transliteration,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from newnasb,qurans where newnasb.surah=qurans.surah and newnasb.ayah=qurans.ayah and " +
                "newnasb.surah ==  \""
                + tid + "\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getNasbListing(query)


    }

    fun getSIfa(tid:Int):List<SifaListingPojo>{
        val sqlshart:String=("select  sifa.surah,sifa.ayah,sifa.startindex,sifa.endindex,sifa.wordfrom,qurans.en_arberry,qurans.en_jalalayn,qurans.ur_jalalayn,qurans.tafsir_kathir,qurans.ur_junagarhi,qurans.ar_irab_two\n" +
                " from newnasb,qurans where newnasb.surah=qurans.surah and newnasb.ayah=qurans.ayah and " +
                "newnasb.surah ==  \""
                + tid + "\"")

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlshart)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getSifaListing(query)


    }



    fun getConditional(tid: Int): List<CorpusExpandWbwPOJO> {
        val sqlverb: String =
            ("SELECT CorpusExpand.rootaraone || rootaratwo || rootarathree || rootarafour || rootarafive AS root_a,\n" +
                    "       CorpusExpand.surah,\n" +
                    "       CorpusExpand.ayah,\n" +
                    "       CorpusExpand.wordno,\n" +
                    "       CorpusExpand.wordcount,\n" +
                    "       Qurans.translation,\n" +
                    "       Qurans.passage_no,\n" +
                    "       Qurans.ar_irab_two,\n" +
                    "       Qurans.tafsir_kathir,\n" +
                    "       Qurans.en_transliteration,\n" +
                    "       Qurans.en_jalalayn,\n" +
                    "       Qurans.en_arberry,\n" +
                    "       Qurans.ur_jalalayn,\n" +
                    "       Qurans.ur_junagarhi,\n" +
                    "       CorpusExpand.araone,\n" +
                    "       CorpusExpand.aratwo,\n" +
                    "       CorpusExpand.arathree,\n" +
                    "       CorpusExpand.arafour,\n" +
                    "       CorpusExpand.arafive,\n" +
                    "       CorpusExpand.tagone,\n" +
                    "       CorpusExpand.tagtwo,\n" +
                    "       CorpusExpand.tagthree,\n" +
                    "       CorpusExpand.tagfour,\n" +
                    "       CorpusExpand.tagfive,\n" +
                    "       CorpusExpand.detailsone,\n" +
                    "       CorpusExpand.detailstwo,\n" +
                    "       CorpusExpand.detailsthree,\n" +
                    "       CorpusExpand.detailsfour,\n" +
                    "       CorpusExpand.detailsfive,\n" +
                    "       CorpusExpand.en,\n" +
                    "       CorpusExpand.bn,\n" +
                    "       CorpusExpand.[in],\n" +
                    "       CorpusExpand.ur,\n" +
                    "       qurans.qurantext\n" +
                    "  FROM corpusexpand,\n" +
                    "       qurans,\n" +

                    " WHERE CorpusExpand.surah == \""
                    + tid + "\""
                    + "AND \n" +
                    "(CorpusExpand.tagone=\"T\" OR CorpusExpand.tagtwo=\"T\" OR CorpusExpand.tagthree=\"T\"\tOR CorpusExpand.tagfour=\"T\" or CorpusExpand.tagfive=\"T\" or CorpusExpand.tagone=\"COND\" OR CorpusExpand.tagtwo=\"COND\" OR CorpusExpand.tagthree=\"COND\"\tOR CorpusExpand.tagfour=\"COND\" or CorpusExpand.tagfive=\"COND\") AND \n"+

                    "       corpusexpand.surah = qurans.surah AND \n" +
                    "       corpusexpand.ayah = qurans.ayah  \n" +
                    " ORDER BY corpusexpand.surah,\n" +
                    "          corpusexpand.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getCorpusWbwSurahAyah(query)
    }


    fun getaccusatives(tid: Int): List<CorpusExpandWbwPOJO> {
        val sqlverb: String =
            ("SELECT CorpusExpand.rootaraone || rootaratwo || rootarathree || rootarafour || rootarafive AS root_a,\n" +
                    "       CorpusExpand.surah,\n" +
                    "       CorpusExpand.ayah,\n" +
                    "       CorpusExpand.wordno,\n" +
                    "       CorpusExpand.wordcount,\n" +
                    "       Qurans.translation,\n" +
                    "       Qurans.passage_no,\n" +
                    "       Qurans.ar_irab_two,\n" +
                    "       Qurans.tafsir_kathir,\n" +
                    "       Qurans.en_transliteration,\n" +
                    "       Qurans.en_jalalayn,\n" +
                    "       Qurans.en_arberry,\n" +
                    "       Qurans.ur_jalalayn,\n" +
                    "       Qurans.ur_junagarhi,\n" +
                    "       CorpusExpand.araone,\n" +
                    "       CorpusExpand.aratwo,\n" +
                    "       CorpusExpand.arathree,\n" +
                    "       CorpusExpand.arafour,\n" +
                    "       CorpusExpand.arafive,\n" +
                    "       CorpusExpand.tagone,\n" +
                    "       CorpusExpand.tagtwo,\n" +
                    "       CorpusExpand.tagthree,\n" +
                    "       CorpusExpand.tagfour,\n" +
                    "       CorpusExpand.tagfive,\n" +
                    "       CorpusExpand.detailsone,\n" +
                    "       CorpusExpand.detailstwo,\n" +
                    "       CorpusExpand.detailsthree,\n" +
                    "       CorpusExpand.detailsfour,\n" +
                    "       CorpusExpand.detailsfive,\n" +
                    "       CorpusExpand.en,\n" +
                    "       CorpusExpand.bn,\n" +
                    "       CorpusExpand.[in],\n" +
                    "       CorpusExpand.ur,\n" +
                    "       qurans.qurantext\n" +
                    "  FROM corpusexpand,\n" +
                    "       qurans,\n" +

                    " WHERE CorpusExpand.surah == \""
                    + tid + "\""
                    + "AND \n" +
                    "(CorpusExpand.tagone=\"ACC\" OR CorpusExpand.tagtwo=\"ACC\" OR CorpusExpand.tagthree=\"ACC\"\tOR CorpusExpand.tagfour=\"ACC\" or CorpusExpand.tagfive=\"ACC\") AND \n"+

                    "       corpusexpand.surah = qurans.surah AND \n" +
                    "       corpusexpand.ayah = qurans.ayah  \n" +
                    " ORDER BY corpusexpand.surah,\n" +
                    "          corpusexpand.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getCorpusWbwSurahAyah(query)
    }

    fun getnounoccuranceHarfNasbZarf(tid: String): List<CorpusNounWbwOccurance?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.rootaraone || rootaratwo || rootarathree || rootarafour || rootarafive as root_a,\n" +
                    "       CorpusExpand.surah,\n" +
                    "       CorpusExpand.ayah,\n" +
                    "       CorpusExpand.wordno,\n" +
                    "       CorpusExpand.wordcount,\n" +
                    "         Qurans.qurantext,\n" +
                    "       qurans.translation,\n" +
                    "       CorpusExpand.araone,\n" +
                    "       CorpusExpand.aratwo,\n" +
                    "       CorpusExpand.arathree,\n" +
                    "       CorpusExpand.arafour,\n" +
                    "       CorpusExpand.arafive,\n" +
                    "       CorpusExpand.tagone,\n" +
                    "       CorpusExpand.tagtwo,\n" +
                    "       CorpusExpand.tagthree,\n" +
                    "       CorpusExpand.tagfour,\n" +
                    "       CorpusExpand.tagfive,\n" +
                    "       CorpusExpand.detailsone,\n" +
                    "       CorpusExpand.detailstwo,\n" +
                    "       CorpusExpand.detailsthree,\n" +
                    "       CorpusExpand.detailsfour,\n" +
                    "       CorpusExpand.detailsfive,\n" +
                    "       nouncorpus.tag,\n" +
                    "       nouncorpus.propone ,\n" +
                    "       nouncorpus.proptwo,\n" +
                    "       nouncorpus.form,\n" +
                    "       nouncorpus.gendernumber,\n" +
                    "       nouncorpus.type,\n" +
                    "       nouncorpus.cases,\n" +
                    "       corpusexpand.en\n" +
                    "      FROM corpusexpand,nouncorpus,\n" +
                    "       qurans\n" +
                    "    where   nouncorpus.tag = \""
                    + tid + "\""
                    + "    AND    corpusexpand.surah = nouncorpus.surah AND \n" +
                    "         corpusexpand.ayah = nouncorpus.ayah AND \n" +
                    "         corpusexpand.wordno = nouncorpus.wordno " +
                    "and corpusexpand.surah = qurans.surah AND   corpusexpand.ayah = qurans.ayah  order by corpusexpand.surah,corpusexpand.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getnounoccurance(query)
    }

    fun getVerbOccuranceBreakVerses(tid: String): List<CorpusVerbWbwOccurance?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.rootaraone || rootaratwo || rootarathree || rootarafour || rootarafive as root_a,\n" +
                    "       CorpusExpand.surah,\n" +
                    "       CorpusExpand.ayah,\n" +
                    "       CorpusExpand.wordno,\n" +
                    "       CorpusExpand.wordcount,\n" +
                    "       Qurans.qurantext,\n" +
                    "       Qurans.translation,\n" +
                    "       Qurans.ur_jalalayn,\n" +
                    "       Qurans.en_jalalayn,\n" +
                    "       Qurans.en_arberry,\n" +
                    "       CorpusExpand.araone,\n" +
                    "       CorpusExpand.aratwo,\n" +
                    "       CorpusExpand.arathree,\n" +
                    "       CorpusExpand.arafour,\n" +
                    "       CorpusExpand.arafive,\n" +
                    "       CorpusExpand.tagone,\n" +
                    "       CorpusExpand.tagtwo,\n" +
                    "       CorpusExpand.tagthree,\n" +
                    "       CorpusExpand.tagfour,\n" +
                    "       CorpusExpand.tagfive,\n" +
                    "       CorpusExpand.detailsone,\n" +
                    "       CorpusExpand.detailstwo,\n" +
                    "       CorpusExpand.detailsthree,\n" +
                    "       CorpusExpand.detailsfour,\n" +
                    "       CorpusExpand.detailsfive,\n" +
                    "       verbcorpus.voice,\n" +
                    "       verbcorpus.form ,\n" +
                    "       verbcorpus.thulathibab,\n" +
                    "       verbcorpus.tense,\n" +
                    "       verbcorpus.gendernumber,\n" +
                    "       verbcorpus.mood_kananumbers,\n" +
                    "       verbcorpus.kana_mood,\n" +
                    "       corpusexpand.en\n" +
                    "      FROM corpusexpand,verbcorpus,\n" +
                    "       qurans\n" +
                    " WHERE (corpusexpand.tagone = \"V\" OR \n" +
                    "        corpusexpand.tagtwo = \"V\" OR \n" +
                    "        corpusexpand.tagthree = \"V\" OR \n" +
                    "        Corpusexpand.tagfour = \"V\" OR \n" +
                    "        corpusexpand.tagfive = \"V\") AND \n" +
                    "       corpusexpand.lemma=  \""
                    + tid + "\""
                    + "    AND  corpusexpand.surah = verbcorpus.chapterno AND \n" +
                    "           corpusexpand.ayah = verbcorpus.verseno AND \n" +
                    "           corpusexpand.wordno = verbcorpus.wordno and corpusexpand.surah = qurans.surah AND   corpusexpand.ayah = qurans.ayah order by corpusexpand.surah,corpusexpand.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getVerbOccuranceBreakVerses(query)
    }

    fun getNounOccuranceBreakVerses(tid: String): List<CorpusNounWbwOccurance?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.rootaraone || rootaratwo || rootarathree || rootarafour || rootarafive as root_a,\n" +
                    "       CorpusExpand.surah,\n" +
                    "       CorpusExpand.ayah,\n" +
                    "       CorpusExpand.wordno,\n" +
                    "       CorpusExpand.wordcount,\n" +
                    "       Qurans.qurantext,\n" +
                    "       qurans.translation,\n" +
                    "       Qurans.ur_jalalayn,\n" +
                    "       Qurans.en_jalalayn,\n" +
                    "       Qurans.en_arberry,\n" +
                    "       CorpusExpand.araone,\n" +
                    "       CorpusExpand.aratwo,\n" +
                    "       CorpusExpand.arathree,\n" +
                    "       CorpusExpand.arafour,\n" +
                    "       CorpusExpand.arafive,\n" +
                    "       CorpusExpand.tagone,\n" +
                    "       CorpusExpand.tagtwo,\n" +
                    "       CorpusExpand.tagthree,\n" +
                    "       CorpusExpand.tagfour,\n" +
                    "       CorpusExpand.tagfive,\n" +
                    "       CorpusExpand.detailsone,\n" +
                    "       CorpusExpand.detailstwo,\n" +
                    "       CorpusExpand.detailsthree,\n" +
                    "       CorpusExpand.detailsfour,\n" +
                    "       CorpusExpand.detailsfive,\n" +
                    "       nouncorpus.tag,\n" +
                    "       nouncorpus.propone ,\n" +
                    "       nouncorpus.proptwo,\n" +
                    "       nouncorpus.form,\n" +
                    "       nouncorpus.gendernumber,\n" +
                    "       nouncorpus.type,\n" +
                    "       nouncorpus.cases,\n" +
                    "       corpusexpand.en\n" +
                    "      FROM corpusexpand,nouncorpus,\n" +
                    "       qurans\n" +
                    "    where  corpusexpand.lemma=  \""
                    + tid + "\""
                    + "    AND    corpusexpand.surah = nouncorpus.surah AND \n" +
                    "           corpusexpand.ayah = nouncorpus.ayah AND \n" +
                    "         corpusexpand.wordno = nouncorpus.wordno" +
                    " and corpusexpand.surah = qurans.surah AND   corpusexpand.ayah = qurans.ayah order by corpusexpand.surah,corpusexpand.ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getnounoccurance(query)
    }

    fun getRootDetails(tid: String): List<RootWordDetails?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.araone ||CorpusExpand. aratwo ||CorpusExpand. arathree || CorpusExpand.arafour ||CorpusExpand. arafive as arabic,\n" +
                    "CorpusExpand.lemma as lemma,\n" +
                    "CorpusExpand.araone,CorpusExpand.aratwo,CorpusExpand.arathree,CorpusExpand.arafour,CorpusExpand.arafive,\n" +
                    "CorpusExpand.tagone,CorpusExpand.tagtwo,CorpusExpand.tagthree,CorpusExpand.tagfour,CorpusExpand.tagfive,CorpusExpand.wordno,\n" +
                    "CorpusExpand.tagone||\"-\" ||CorpusExpand. tagtwo||\"-\" ||CorpusExpand. tagthree ||\"-\"|| CorpusExpand.tagfour ||CorpusExpand. tagfive as tag,\n" +
                    "       qurandictionary.surah,\n" +
                    "       qurandictionary.ayah,\n" +
                    "       qurandictionary.rootarabic,\n" +
                    "\t   wbbw.en,\n" +
                    "\t   chaptersana.abjadname,chaptersana.namearabic,chaptersana.nameenglish \n" +
                    "\t  \n" +
                    " \n" +
                    "      FROM corpusexpand,qurandictionary,chaptersana\n" +
                    "\t  where  qurandictionary.surah = CorpusExpand.surah AND  qurandictionary.ayah = CorpusExpand.ayah  \n" +
                    "\tand qurandictionary.wordno = CorpusExpand.wordno  AND  \n" +
                    "and  qurandictionary.surah=chaptersana.chapterid and qurandictionary.rootarabic=  \""
                    + tid + "\"")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getrootdetails(query)
    }

    fun getRootVerbDetailsbyRootword(tid: String): List<RootVerbDetails?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.araone ||CorpusExpand. aratwo ||CorpusExpand. arathree || CorpusExpand.arafour ||CorpusExpand.arafive as arabic,\n" +
                    "CorpusExpand.lemma as lemma,\n" +
                    "CorpusExpand.araone,CorpusExpand.aratwo,CorpusExpand.arathree,CorpusExpand.arafour,CorpusExpand.arafive,\n" +
                    "CorpusExpand.tagone,CorpusExpand.tagtwo,CorpusExpand.tagthree,CorpusExpand.tagfour,CorpusExpand.tagfive,\n" +
                    "       qurandictionary.surah,\n" +
                    "       qurandictionary.ayah,\n" +
                    "       qurandictionary.rootarabic,qurandictionary.wordno,\n" +
                    "\t   CorpusExpand.en,\n" +
                    "\t   chaptersana.abjadname,chaptersana.namearabic,chaptersana.nameenglish,\n" +
                    "\t   verbcorpus.form,verbcorpus.thulathibab,verbcorpus.gendernumber,verbcorpus.tense,verbcorpus.voice,verbcorpus.mood_kananumbers,verbcorpus.kana_mood,verbcorpus.lemma_a\n" +
                    "\t  \n" +
                    " \n" +
                    "      FROM corpusexpand,qurandictionary,chaptersana,verbcorpus\n" +
                    "\t  where (CorpusExpand.tagone=\"V\" OR CorpusExpand.tagtwo=\"V\" OR CorpusExpand.tagthree=\"V\" OR CorpusExpand.tagfour=\"V\" \n" +
                    "\t or CorpusExpand.tagfive==\"V\" )and qurandictionary.surah = CorpusExpand.surah AND  qurandictionary.ayah = CorpusExpand.ayah  \n" +
                    "\t \t  AND qurandictionary.surah=verbcorpus.chapterno and qurandictionary.ayah=verbcorpus.verseno\n" +
                    "\tand qurandictionary.wordno = CorpusExpand.wordno" +
                    " and qurandictionary.surah=chaptersana.chapterid and qurandictionary.rootarabic=  \""
                    + tid + "\"")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getverbdetails(query)
    }
    fun getAllRootVerbDetails(): List<RootVerbDetails?>? {
        val sqlverb: String =
            ("SELECT CorpusExpand.araone ||CorpusExpand. aratwo ||CorpusExpand. arathree || CorpusExpand.arafour ||CorpusExpand.arafive as arabic,\n" +
                    "CorpusExpand.lemma as lemma,\n" +
                    "CorpusExpand.araone,CorpusExpand.aratwo,CorpusExpand.arathree,CorpusExpand.arafour,CorpusExpand.arafive,\n" +
                    "CorpusExpand.tagone,CorpusExpand.tagtwo,CorpusExpand.tagthree,CorpusExpand.tagfour,CorpusExpand.tagfive,\n" +
                    " \t   CorpusExpand.en,\n" +
                    "\t   verbcorpus.form,verbcorpus.thulathibab,verbcorpus.gendernumber,verbcorpus.tense,verbcorpus.voice,verbcorpus.mood_kananumbers,verbcorpus.kana_mood,verbcorpus.lemma_a,qurans.qurantext,qurans.en_arberry\n" +
                    "\t   FROM corpusexpand,verbcorpus,qurans\n" +
                    "\t  where (CorpusExpand.tagone=\"V\" OR CorpusExpand.tagtwo=\"V\" OR CorpusExpand.tagthree=\"V\" OR CorpusExpand.tagfour=\"V\" \n" +
                    "\t or CorpusExpand.tagfive==\"V\" )\n"
                    )
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getverbdetails(query)
    }



    //  List<Book> result = booksDao.getBooks(query);
    /*    val mudhaf: List<MudhafPOJO?>?
            get() {
                val sqlverb: String =
                    "select newmudhaf.surah,newmudhaf.ayah,newmudhaf.startindex,newmudhaf.endindex, newmudhaf.comment,qurans.qurantext,qurans.translation from newmudhaf,qurans where \n" +
                            "newmudhaf.surah=qurans.surah and newmudhaf.ayah=qurans.ayah"
                val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
                //  List<Book> result = booksDao.getBooks(query);
                return database.RawDao().getMudhaf(query)
            }

        //     "shart.ayah=qurans.ayah and shart.sharttype=\"laula\" order by shart.surah";
        val tameez: List<TameezPojoList?>?
            get() {
                val sqlverb: String = ("select tameez.surah,tameez.ayah,tameez.wordno,tameez.word \n" +
                        "                 ,qurans.qurantext,qurans.translation from tameez,qurans where tameez.surah=qurans.surah and  \n" +
                        "               tameez.ayah=qurans.ayah  order by tameez.surah,tameez.ayah")
                //     "shart.ayah=qurans.ayah and shart.sharttype=\"laula\" order by shart.surah";
                val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
                //  List<Book> result = booksDao.getBooks(query);
                return database.RawDao().getTameez(query)
            }*/

    fun getSifabySurah(id: Int): List<SifaEntity?>? {
        return database.SifaDao().getSifaindexesBySurah(id)
    }

    fun getSifaAll(): List<SifaEntity> {
        return database.SifaDao().getSifaAll()
    }

    fun getSifabySurahAyah(id: Int, aid: Int): List<SifaEntity>? {
        return database.SifaDao().getSifaindexesBySurahAyah(id, aid)
    }



    fun getRootDictionary(id: String): List<lughat> {
        return database.LughatDao().getRootWordDictionary(id.trim { it <= ' ' })
    }


    fun getAlllughat( ): List<lughat> {
        return database.LughatDao().getall()
    }

    fun getLanesRootDifinition(id: String): List<lanerootdictionary?>? {
        return database.LaneRootDao()?.getLanesRootDefinition(id.trim { it <= ' ' })
    }


    fun getHansDifinition(id: String): List<hanslexicon?>? {
        return database.HansDao().getHansDefinition(id.trim { it <= ' ' })
    }

    fun getArabicWord(id: String?): List<lughat?>? {
        return database.LughatDao().getArabicWord(id)
    }

    val quranDictionary: List<qurandictionary?>?
        get() {
            return database.qurandictionaryDao().dictionary
        }

    fun getQuranDictionarybyroot(root: String?): List<qurandictionary?>? {
        return database.qurandictionaryDao().getDictionaryroot(root)
    }

    val topicSearchAll: List<quranexplorer>?
        get() {
            return database.QuranExplorerDao()?.aLL
        }













    fun getNounBreakup(tid: String): List<NounCorpusBreakup>? {
        val sqlverb: String =
            ("SELECT count(root_a) as count,id,surah,ayah, lemma_a,form,araword,tag,propone,proptwo FROM nouncorpus where root_a =\""
                    + tid + "\""
                    + " group by lemma_a,root_a,tag,propone,proptwo order by surah,ayah")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getNounBreakup(query)
    }

    fun getVerbBreakUp(tid: String): List<VerbCorpusBreakup?>? {
        val sqlverb: String =
            ("SELECT count(root_a) as count,root_a,lemma_a,form,thulathibab,tense,voice FROM verbcorpus where root_a =\""
                    + tid + "\""
                    + " group by root_a,form order by chapterno,verseno")
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sqlverb)
        //  List<Book> result = booksDao.getBooks(query);
        return database.RawDao().getVerbBreakup(query)
    }

    val grammarRules: List<GrammarRules>
        get() {
            return database.grammarRulesDao().grammarRules
        }

    fun getGrammarRulesByHarf(form:String): List<GrammarRules>{

            return database.grammarRulesDao().getGrammarRulesByHarf(form)
        }

    fun getQuranbySurah(id: Int): List<QuranEntity> {
        return database.QuranDao().getQuranVersesBySurah(id)
    }

    fun getVerbBreakUps(id: String): List<VerbCorpus> {
        return database.VerbCorpusDao().getVerbBreakUp(id)
    }

    fun getNounBreakups(id: String): List<NounCorpus> {
        return database.NounCorpusDao().getNounBreakup(id)
    }


    fun getAyahsByPageQuran(surah: Int, pageno: Int): List<QuranEntity?>? {
        return database.QuranDao().getAyahsByPage(surah, pageno)
    }

    fun getsurahayahVerses(id: Int, aid: Int): List<QuranEntity?>? {
        return database.QuranDao().getsurahayahVerses(id, aid)
    }

    fun getQuranbySurahAyahrange(surahid: Int, from: Int, to: Int): List<QuranEntity?>? {
        return database.QuranDao().getQuranbySurahAyahrange(surahid, from, to)
    }

    val names: ArrayList<AllahNames>
        get() {
            return database.NamesDao()?.ALLAH_NAMES_LIST() as ArrayList<AllahNames>
        }


    /*

        fun gethDuadetailsitems(id: String?): List<hduadetailsEnt?>? {
            return database.hDuaItemDao().getDitem(id)
        }
    */

    //muslim mate


    //  List<Book> result = booksDao.getBooks(query);
    val collectionC: List<BookMarksPojo?>?
        get() {
            val sql: String =
                "select  count(*) as count, * from bookmark where header != \"pins\" group by header"
            val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sql)
            //  List<Book> result = booksDao.getBooks(query);
            return database.RawDao().getCollectionCount(query)
        }

    //  List<Book> result = booksDao.getBooks(query);
    val juz: List<Juz>
        get() {
            val sql: String =
                "select  a.nameenglish,a.namearabic ,a.chapterid , b. juz, min(b.page) page,b.ayah,b.qurantext from chaptersana a, qurans b where a.chapterid=b.surah group by juz"
            val query: SimpleSQLiteQuery = SimpleSQLiteQuery(sql)
            //  List<Book> result = booksDao.getBooks(query);
            return database.RawDao().getJuz(query)
        }

    val qaris: List<Qari>
        get() {
            return database.QariDao().qaris
        }

    fun getByfirstletter(id: String): List<qurandictionary> {
        return database.qurandictionaryDao().getByfirstletter(id)
    }

    fun getQuranVerbsByfirstletter(id: String): List<VerbCorpus> {
        return database.VerbCorpusDao().getQuranVerbsByfirstletter(id)
    }


    fun getLaysa()  :List<lysaEnt> {
        return database.LysaDao().getLysa()
    }


    companion object {
        private const val TAG: String = "Utils"
        private lateinit var database: QuranAppDatabase
        fun getsurahayahVerses(id: Int, aid: Int): List<QuranEntity?>? {
            return database.QuranDao().getsurahayahVerses(id, aid)
        }


        fun getQuranbySurahAyahrange(surahid: Int, from: Int, to: Int): List<QuranEntity?>? {
            return database.QuranDao().getQuranbySurahAyahrange(surahid, from, to)
        }

        //select * from qurans where ayah>=50 and ayah<=78 and surah=9


        /*
                fun quran() :List<QuranEntity> {
                    return  database.QuranDao().allQuran()

                }

        */
        fun getQuranbyJuz(juz: Int): List<QuranEntity?>? {
            return database.QuranDao().getQuranbyJuz(juz)
        }

        fun getAyahsByPagejuz(juz: Int, pageno: Int): List<QuranEntity?>? {
            return database.QuranDao().getAyahsByPagejuz(juz, pageno)
        }
    }
}


