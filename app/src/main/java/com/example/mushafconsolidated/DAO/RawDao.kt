package com.example.mushafconsolidated.DAO


import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.mushafconsolidated.Entities.AccusativePojo
import com.example.mushafconsolidated.Entities.CorpusExpandWbwPOJO
import com.example.mushafconsolidated.Entities.CorpusNounWbwOccurance
import com.example.mushafconsolidated.Entities.CorpusVerbWbwOccurance
import com.example.mushafconsolidated.Entities.FutureTenceListingPojo
import com.example.mushafconsolidated.Entities.InMaListingPOJO
import com.example.mushafconsolidated.Entities.KanaPOJO
import com.example.mushafconsolidated.Entities.MudhafPOJO
import com.example.mushafconsolidated.Entities.NasbListingPojo
import com.example.mushafconsolidated.Entities.NasbPOJO
import com.example.mushafconsolidated.Entities.NewCorpusExpandWbwPOJO
import com.example.mushafconsolidated.Entities.NounCorpusBreakup
import com.example.mushafconsolidated.Entities.PastTencePOJO
import com.example.mushafconsolidated.Entities.PresentTencePOJO
import com.example.mushafconsolidated.Entities.RootVerbDetails
import com.example.mushafconsolidated.Entities.RootWordDetails
import com.example.mushafconsolidated.Entities.ShartListingPojo
import com.example.mushafconsolidated.Entities.ShartPOJO
import com.example.mushafconsolidated.Entities.SifaEntityPojo
import com.example.mushafconsolidated.Entities.SifaListingPojo
import com.example.mushafconsolidated.Entities.SifaPOJO

import com.example.mushafconsolidated.Entities.VerbCorpusBreakup
import com.example.mushafconsolidated.Entities.qurandictionary
import com.example.mushafconsolidated.model.Juz


//import com.example.mushafconsolidated.Entities.JoinVersesTranslationDataTranslation;
@Dao
interface RawDao {
    @RawQuery
    fun getRootsbyLetter(query: SupportSQLiteQuery): List<qurandictionary>

    @RawQuery
    fun getnounoccurance(query: SupportSQLiteQuery): List<CorpusNounWbwOccurance?>?

    @RawQuery
    fun getrootdetails(query: SupportSQLiteQuery): List<RootWordDetails?>?

    @RawQuery
    fun getverbdetails(query: SupportSQLiteQuery): List<RootVerbDetails?>?

    @RawQuery
    fun getnounoccurancebysurahayah(query: SupportSQLiteQuery): List<CorpusNounWbwOccurance?>?


    @RawQuery
    fun getCorpusWbw(query: SupportSQLiteQuery): List<CorpusExpandWbwPOJO?>?


    @RawQuery
    fun getCorpusWbwSurahAyah(query: SupportSQLiteQuery): List<CorpusExpandWbwPOJO>

    @RawQuery
    fun getShartListing(query: SupportSQLiteQuery): List<ShartListingPojo>
    @RawQuery
    fun getAccusativeListing(query: SupportSQLiteQuery): List<AccusativePojo>

    @RawQuery
    fun getSifaListing(query: SupportSQLiteQuery): List<SifaListingPojo>

    @RawQuery
    fun getInMaisting(query: SupportSQLiteQuery): List<InMaListingPOJO>

    @RawQuery
    fun getFuturNegationList(query: SupportSQLiteQuery): List<FutureTenceListingPojo>
    @RawQuery
    fun getNasbListing(query: SupportSQLiteQuery): List<NasbListingPojo>

    @RawQuery
    fun getPastTenceListing(query: SupportSQLiteQuery): List<PastTencePOJO>



    @RawQuery
    fun getPresentTenceListing(query: SupportSQLiteQuery): List<PresentTencePOJO>

    @RawQuery
    fun getSifaIndexes(query: SupportSQLiteQuery): List<SifaEntityPojo?>?

    @RawQuery
    fun getMousufSifa(query: SupportSQLiteQuery): List<SifaPOJO?>?

    @RawQuery
    fun getShart(query: SupportSQLiteQuery): List<ShartPOJO?>?



    @RawQuery
    fun getMudhaf(query: SupportSQLiteQuery): List<MudhafPOJO?>?

    @RawQuery
    fun getNewCorpusWbw(query: SupportSQLiteQuery): List<NewCorpusExpandWbwPOJO?>?


    @RawQuery
    fun getKana(query: SupportSQLiteQuery): List<KanaPOJO?>?

    @RawQuery
    fun getNasb(query: SupportSQLiteQuery): List<NasbPOJO?>?

    @RawQuery
    fun getNounBreakup(query: SupportSQLiteQuery): List<NounCorpusBreakup>?

    @RawQuery
    fun getVerbBreakup(query: SupportSQLiteQuery): List<VerbCorpusBreakup?>?

    @RawQuery
    fun getVerbOccuranceBreakVerses(query: SupportSQLiteQuery): List<CorpusVerbWbwOccurance?>?



    @RawQuery
    fun getCorpusWbwfortameez(query: SupportSQLiteQuery): List<CorpusExpandWbwPOJO?>?





    @RawQuery
    fun getCollectionCount(query: SupportSQLiteQuery): List<BookMarksPojo?>?

    @RawQuery
    fun getJuz(query: SimpleSQLiteQuery): List<Juz>
}