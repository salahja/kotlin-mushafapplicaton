package database.verbrepo

import android.content.Context
import com.example.mushafconsolidated.DAO.QuranDao
import com.example.mushafconsolidated.QuranAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import database.Dao.BuckwaterDao
import database.Dao.NamesDao
import database.Dao.kovDao
import database.Dao.mazeedDao
import database.Dao.mujarradDao
import database.VerbDatabase
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object VerbDbModule {

    @Singleton
    @Provides
    fun getVerbDb(@ApplicationContext context: Context): VerbDatabase {
        return VerbDatabase.getInstance(context)!!
    }


    @Singleton
    @Provides
    fun getQuranDao(appDB: VerbDatabase): kovDao {

        return appDB.kovDao()
    }


    @Singleton
    @Provides
    fun getmazeedDao(appDB: VerbDatabase): mazeedDao {

        return appDB.mazeedDao()
    }

    @Singleton
    @Provides
    fun getMujarradDao(appDB: VerbDatabase): mujarradDao {

        return appDB.mujarradDao()
    }

    @Singleton
    @Provides
    fun getBuckwaterDao(appDB: VerbDatabase): BuckwaterDao {

        return appDB.BuckwaterDao()
    }




}