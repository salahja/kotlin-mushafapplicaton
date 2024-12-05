package Utility

import android.content.Context
import com.example.mushafconsolidated.DAO.AbsoluteNegationDao
import com.example.mushafconsolidated.DAO.AnaQuranChapterDao

import com.example.mushafconsolidated.DAO.BookMarkDao
import com.example.mushafconsolidated.DAO.CorpusExpandedDao


import com.example.mushafconsolidated.DAO.HansDao



import com.example.mushafconsolidated.DAO.LaneDao
import com.example.mushafconsolidated.DAO.LaneRootDao
import com.example.mushafconsolidated.DAO.LughatDao



import com.example.mushafconsolidated.DAO.NegationDao

import com.example.mushafconsolidated.DAO.NewMudhafDao

import com.example.mushafconsolidated.DAO.NounCorpusDao

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

import com.example.mushafconsolidated.DAO.wbwDao
import com.example.mushafconsolidated.Entities.NamesDetailsDao
import com.example.mushafconsolidated.QuranAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import database.Dao.NamesDao
import mufradat.MufradatDao

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun getAppDB(@ApplicationContext context: Context): QuranAppDatabase {
        return QuranAppDatabase.getInstance(context)!!
    }

    @Singleton
    @Provides
    fun getQuranDao(appDB: QuranAppDatabase): QuranDao {

        return appDB.QuranDao()
    }

    @Singleton
    @Provides
    fun getAbsoluteNegationDao(appDB: QuranAppDatabase): AbsoluteNegationDao {

        return appDB.AbsoluteNegationDao()
    }






    @Singleton
    @Provides
    fun getNegationDao(appDB: QuranAppDatabase): NegationDao {

        return appDB.NegationDao()
    }
    @Singleton
    @Provides
    fun getSIfaMudhafnDao(appDB: QuranAppDatabase): SifaMudhafDao {

        return appDB.SifaMudhafDao()
    }




    @Singleton
    @Provides
    fun AnaQuranChapterDao(appDB: QuranAppDatabase): AnaQuranChapterDao {

        return appDB.AnaQuranChapterDao()
    }


    @Singleton
    @Provides
    fun getBookMarkDao(appDB: QuranAppDatabase): BookMarkDao {

        return appDB.BookMarkDao()
    }

    @Singleton
    @Provides
    fun getRawdao(appDB: QuranAppDatabase): RawDao {

        return appDB.RawDao()
    }


    @Singleton
    @Provides
    fun getCorpusExpandDao(appDB: QuranAppDatabase): CorpusExpandedDao {

        return appDB.getCorpusExpandDao()
    }


    @Singleton
    @Provides
    fun VerbCorpusDao(appDB: QuranAppDatabase): VerbCorpusDao {

        return appDB.VerbCorpusDao()
    }

    @Singleton
    @Provides
    fun NounCorpusDao(appDB: QuranAppDatabase): NounCorpusDao {

        return appDB.NounCorpusDao()
    }

    @Singleton
    @Provides
    fun wbwDao(appDB: QuranAppDatabase): wbwDao {

        return appDB.wbwDao()
    }


    @Singleton
    @Provides
    fun SifaDao(appDB: QuranAppDatabase): SifaDao {

        return appDB.SifaDao()
    }





    @Singleton
    @Provides
    fun NewMudhafDao(appDB: QuranAppDatabase): NewMudhafDao {

        return appDB.NewMudhafDao()
    }





    @Singleton
    @Provides
    fun LughatDao(appDB: QuranAppDatabase): LughatDao {

        return appDB.LughatDao()
    }


    @Singleton
    @Provides
    fun LaneDao(appDB: QuranAppDatabase): LaneDao? {

        return appDB.LaneDao()
    }


    @Singleton
    @Provides
    fun LaneRootDao(appDB: QuranAppDatabase): LaneRootDao {

        return appDB.LaneRootDao()
    }


    @Singleton
    @Provides
    fun HansDao(appDB: QuranAppDatabase): HansDao {

        return appDB.HansDao()
    }


    @Singleton
    @Provides
    fun qurandictionaryDao(appDB: QuranAppDatabase): qurandictionaryDao {

        return appDB.qurandictionaryDao()
    }


    @Singleton
    @Provides
    fun grammarRulesDao(appDB: QuranAppDatabase): grammarRulesDao {

        return appDB.grammarRulesDao()
    }






    @Singleton
    @Provides
    fun NamesDetailsDao(appDB: QuranAppDatabase): NamesDetailsDao {

        return appDB.NamesDetailsDao()
    }


    @Singleton
    @Provides
    fun QuranExplorerDao(appDB: QuranAppDatabase): QuranExplorerDao {

        return appDB.QuranExplorerDao()
    }


    @Singleton
    @Provides
    fun surahsummaryDao(appDB: QuranAppDatabase): surahsummaryDao {

        return appDB.surahsummaryDao()
    }


    @Singleton
    @Provides
    fun QariDao(appDB: QuranAppDatabase): QariDao {

        return appDB.QariDao()
    }


    @Singleton
    @Provides
    fun NamesDao(appDB: QuranAppDatabase): NamesDao {

        return appDB.NamesDao()
    }




    @Singleton
    @Provides
    fun MufradatDao(appDB: QuranAppDatabase): MufradatDao {

        return appDB.MufradatDao()
    }

}

