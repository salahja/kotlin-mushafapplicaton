package com.example.mushafconsolidated


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mushafconsolidated.DAO.AbsoluteNegationDao
import com.example.mushafconsolidated.DAO.AnaQuranChapterDao

import com.example.mushafconsolidated.DAO.BookMarkDao
import com.example.mushafconsolidated.DAO.CorpusExpandedDao


import com.example.mushafconsolidated.DAO.HansDao


import com.example.mushafconsolidated.DAO.JasonSurahDao


import com.example.mushafconsolidated.DAO.LaneRootDao
import com.example.mushafconsolidated.DAO.LughatDao
import com.example.mushafconsolidated.DAO.LysaDao
import com.example.mushafconsolidated.DAO.NegationDao


import com.example.mushafconsolidated.DAO.NewMudhafDao

import com.example.mushafconsolidated.DAO.NounCorpusDao
import com.example.mushafconsolidated.DAO.OutsideDoerDAO

import com.example.mushafconsolidated.DAO.QariDao
import com.example.mushafconsolidated.DAO.QuranDao
import com.example.mushafconsolidated.DAO.QuranExplorerDao
import com.example.mushafconsolidated.DAO.RawDao
import com.example.mushafconsolidated.DAO.SifaDao
import com.example.mushafconsolidated.DAO.SifaMudhafDao
import com.example.mushafconsolidated.DAO.VerbCorpusDao
import com.example.mushafconsolidated.DAO.grammarRulesDao

import com.example.mushafconsolidated.DAO.qurandictionaryDao
import com.example.mushafconsolidated.DAO.surahsummaryDao


import com.example.mushafconsolidated.Entities.AbsoluteNegationEnt

import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity

import com.example.mushafconsolidated.Entities.GrammarRules





import com.example.mushafconsolidated.Entities.NamesDetailsDao
import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.OutSideDoer

import com.example.mushafconsolidated.Entities.Qari
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity

import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.hanslexicon
import com.example.mushafconsolidated.Entities.jsonsurahentity


import com.example.mushafconsolidated.Entities.lanerootdictionary
import com.example.mushafconsolidated.Entities.lughat
import com.example.mushafconsolidated.Entities.lysaEnt
import com.example.mushafconsolidated.Entities.qurandictionary
import com.example.mushafconsolidated.Entities.quranexplorer
import com.example.mushafconsolidated.Entities.surahsummary

import com.example.mushafconsolidated.settingsimport.Constants.Companion.DATABASENAME
import database.Dao.NamesDao
import database.entity.AllahNames
import mufradat.MufradatDao
import mufradat.MufradatEntity

import sj.hisnul.entity.AllahNamesDetails
import java.io.File

/*@Module
@InstallIn(SingletonComponent::class)*/
@Database(
    entities = [NegationEnt::class,
        AbsoluteNegationEnt::class,
        lysaEnt::class,
        jsonsurahentity::class,
        MufradatEntity::class,
        AllahNamesDetails::class,
        lanerootdictionary::class, Qari::class,
        surahsummary::class, quranexplorer::class,
        AllahNames::class, GrammarRules::class, hanslexicon::class, qurandictionary::class,
        lughat::class,
        SifaEntity::class,
        NewMudhafEntity::class,
        OutSideDoer::class,
         NounCorpus::class, VerbCorpus::class, QuranEntity::class, CorpusEntity::class, BookMarks::class, ChaptersAnaEntity::class],
    version = 1
)
abstract class QuranAppDatabase : RoomDatabase() {
    /**
     * Ana quran chapter dao
     *
     * @return
     */
    abstract fun AnaQuranChapterDao(): AnaQuranChapterDao
    abstract fun JasonSurahDao() : JasonSurahDao
    abstract fun OutsideDoerDAO(): OutsideDoerDAO
    /**
     * Book mark dao
     *
     * @return
     */// public abstract WordbywordPojoDao WordbywordPojoDao();
    abstract fun BookMarkDao(): BookMarkDao
    abstract fun        AbsoluteNegationDao(): AbsoluteNegationDao


    abstract fun NegationDao(): NegationDao


    abstract fun NewMudhafDao(): NewMudhafDao




    /**
     * Raw dao
     *
     * @return
     */
    abstract fun LysaDao(): LysaDao

    abstract fun RawDao(): RawDao
    abstract fun SifaMudhafDao(): SifaMudhafDao


    /**
     * Get corpus expand dao
     *
     * @return
     */
    abstract fun getCorpusExpandDao(): CorpusExpandedDao

    /**
     * Quran dao
     *
     * @return
     */
    abstract fun QuranDao(): QuranDao



    /**
     * Verb corpus dao
     *
     * @return
     */
    abstract fun VerbCorpusDao(): VerbCorpusDao

    /**
     * Noun corpus dao
     *
     * @return
     */
    abstract fun NounCorpusDao(): NounCorpusDao

    /**
     * Wbw dao
     *
     * @return
     */


    /**
     * Sifa dao
     *
     * @return
     */
    abstract fun SifaDao(): SifaDao




    abstract fun LughatDao(): LughatDao

    /**
     * Lane dao
     *
     * @return
     */


    /**
     * Lane root dao
     *
     * @return
     */
    abstract fun LaneRootDao(): LaneRootDao

    /**
     * Hans dao
     *
     * @return
     */
    abstract fun HansDao(): HansDao

    /**
     * Qurandictionary dao
     *
     * @return
     */
    abstract fun qurandictionaryDao(): qurandictionaryDao

    /**
     * Grammar rules dao
     *
     * @return
     */
    abstract fun grammarRulesDao(): grammarRulesDao






    /**
     * Names details dao
     *
     * @return
     */
    abstract fun NamesDetailsDao(): NamesDetailsDao

    /**
     * Quran explorer dao
     *
     * @return
     */
    abstract fun QuranExplorerDao(): QuranExplorerDao

    /**
     * Surahsummary dao
     *
     * @return
     */
    abstract fun surahsummaryDao(): surahsummaryDao

    /**
     * Qari dao
     *
     * @return
     */
    abstract fun QariDao(): QariDao


    /**
     * Names dao
     *
     * @return
     */
    abstract fun NamesDao(): NamesDao



    abstract fun MufradatDao(): MufradatDao
    //abstract fun QuranDao(): QuranDao




    /*
        abstract fun CountryDao(): CountryDao?
        abstract fun CitiesDAO(): CitiesDAO?
    */


    companion object {
        //  public static  <QuranAppDatabase> quranAppDatabaseInstance;
        private val initialCallBack: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //  new InitialAsyncTask(quranAppDatabaseInstance).execute();
            }
        }


        private var quranAppDatabaseInstance: QuranAppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): QuranAppDatabase? {
            val FILEPATH =
                context.getExternalFilesDir(null)!!.absolutePath + "/" + context.resources.getString(
                    R.string.app_folder_path
                )
            if (null == quranAppDatabaseInstance) {
                val mainDatabase = File("$FILEPATH/$DATABASENAME")
            /*    quranAppDatabaseInstance = Room.databaseBuilder(
                                 context,
                                 QuranAppDatabase::class.java, "qurangrammar.db"
                             )
                                 .createFromAsset("databases/qurangrammar.db")
                                 .fallbackToDestructiveMigration()
                                 .addCallback(initialCallBack)
                                 .allowMainThreadQueries()
                                 .build()
*/



               quranAppDatabaseInstance = Room.databaseBuilder(
                    context,
                    QuranAppDatabase::class.java, "qurangrammar.db"
                )
                    .createFromFile(mainDatabase) //          .fallbackToDestructiveMigration()
                    .addCallback(initialCallBack)
                    .allowMainThreadQueries()
                    .build()

            }
             return quranAppDatabaseInstance
            //   return quranAppDatabaseInstanceasset;
        }


    }
}