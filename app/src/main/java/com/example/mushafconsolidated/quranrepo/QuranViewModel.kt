package com.example.mushafconsolidated.quranrepo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mushafconsolidated.Entities.AbsoluteNegationEnt
import com.example.mushafconsolidated.Entities.BadalErabNotesEnt
import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.FutureTenceNegatonEnt
import com.example.mushafconsolidated.Entities.GrammarRules
import com.example.mushafconsolidated.Entities.HalEnt
import com.example.mushafconsolidated.Entities.PastTenceNegatonEnt
import com.example.mushafconsolidated.Entities.LiajlihiEnt
import com.example.mushafconsolidated.Entities.MafoolBihi
import com.example.mushafconsolidated.Entities.MafoolMutlaqEnt
import com.example.mushafconsolidated.Entities.NegationEnt
import com.example.mushafconsolidated.Entities.NewKanaEntity
import com.example.mushafconsolidated.Entities.NewMudhafEntity
import com.example.mushafconsolidated.Entities.NewNasbEntity
import com.example.mushafconsolidated.Entities.NewShartEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.PresentTenceNegatonEnt
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.TameezEnt
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.hanslexicon

import com.example.mushafconsolidated.Entities.lanerootdictionary
import com.example.mushafconsolidated.Entities.lughat
import com.example.mushafconsolidated.Entities.surahsummary
import com.example.mushafconsolidated.Entities.wbwentity

import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.model.Juz
import com.example.mushafconsolidated.model.QuranCorpusWbw
import com.example.utility.QuranGrammarApplication
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(


    private val quranRepository: QuranRepository,

) : ViewModel() {

     private lateinit var chapterData: ChapterData
    private var allquran: MutableLiveData<List<QuranEntity>> = MutableLiveData()
    private val qurancorpus: MutableLiveData<List<QuranCorpusWbw>> = MutableLiveData()
    private var sursumm: LiveData<List<surahsummary>> = MutableLiveData()
    private var chapters: LiveData<List<ChaptersAnaEntity>> = MutableLiveData()
    private var chapterslist: MutableLiveData<List<ChaptersAnaEntity>> = MutableLiveData()
    var juz: LiveData<List<Juz>> = MutableLiveData()
    val util = Utils(QuranGrammarApplication.context)
    private var mafoolb: MutableLiveData<List<MafoolBihi>> = MutableLiveData()
    private var haliya: MutableLiveData<List<HalEnt>> = MutableLiveData()
    var tameez: MutableLiveData<List<TameezEnt>> = MutableLiveData()
    var mutlaq: MutableLiveData<List<MafoolMutlaqEnt>> = MutableLiveData()
    var liajlihi: MutableLiveData<List<LiajlihiEnt>> = MutableLiveData()
    var badal: MutableLiveData<List<BadalErabNotesEnt>> = MutableLiveData()
    private var bokmarks: LiveData<List<BookMarks>> = MutableLiveData()
    var wbw: MutableLiveData<List<wbwentity>> = MutableLiveData()
    private var hanslist: MutableLiveData<List<hanslexicon>> = MutableLiveData()
    private var laneslist: MutableLiveData<List<lanerootdictionary>> = MutableLiveData()

    private var halword: MutableLiveData<List<HalEnt>> = MutableLiveData()
    private var ajlihiword: MutableLiveData<List<LiajlihiEnt>> = MutableLiveData()
    private var mutlaqword: MutableLiveData<List<MafoolMutlaqEnt>> = MutableLiveData()
    private var mafoolBihi: MutableLiveData<List<MafoolBihi>> = MutableLiveData()
    private var nounbywordno: MutableLiveData<List<NounCorpus>> = MutableLiveData()
    private var verbcorpuslist: MutableLiveData<List<VerbCorpus>> = MutableLiveData()


    private var corpuswbwlist: MutableLiveData<List<QuranCorpusWbw>> = MutableLiveData()
    private var corpusentity: MutableLiveData<List<CorpusEntity>> = MutableLiveData()
    private var quranlist: MutableLiveData<List<QuranEntity>> = MutableLiveData()

    var kana: MutableLiveData<List<NewKanaEntity>> = MutableLiveData()
    var shart: MutableLiveData<List<NewShartEntity>> = MutableLiveData()
    private var nasab: MutableLiveData<List<NewNasbEntity>> = MutableLiveData()
    var mudhaf: MutableLiveData<List<NewMudhafEntity>> = MutableLiveData()
    private var sifa: MutableLiveData<List<SifaEntity>> = MutableLiveData()

    private var absolutNegation: MutableLiveData<List<AbsoluteNegationEnt>> = MutableLiveData()
    var lughat: MutableLiveData<List<lughat>> = MutableLiveData()
    private var grammarules: MutableLiveData<List<GrammarRules>> = MutableLiveData()



    fun getGramarRulesbyHarf(root: String): LiveData<List<GrammarRules>> {
        grammarules.value = quranRepository.grammarrulesDao.getGrammarRulesByHarf(root)
        return grammarules
    }

    fun getGramarRules(): LiveData<List<GrammarRules>> {
        grammarules.value = quranRepository.grammarrulesDao.grammarRules
        return grammarules
    }


    fun getRootWordDictionary(root: String): LiveData<List<lughat>> {
        lughat.value = quranRepository.lughatdao.getRootWordDictionary(root)
        return lughat
    }

    fun getArabicWord(root: String): LiveData<List<lughat>> {
        lughat.value = quranRepository.lughatdao.getArabicWord(root)
        return lughat
    }






    fun getkana(surah: Int, ayah: Int): LiveData<List<NewKanaEntity>> {

        kana.value = this.quranRepository.getkana(surah, ayah)
        return kana
    }

    fun getshart(surah: Int, ayah: Int): LiveData<List<NewShartEntity>> {

        shart.value = this.quranRepository.getshart(surah, ayah)
        return shart
    }

    fun getnasab(surah: Int, ayah: Int): LiveData<List<NewNasbEntity>> {
        nasab.value = this.quranRepository.getnasab(surah, ayah)
        return nasab
    }

    fun getmudhafFilterSurahAyah(surah: Int, ayah: Int): List<NewMudhafEntity> {
   return this.quranRepository.getmudhaf(surah, ayah)

    }
    fun getmudhafFilterSurah(surah: Int): List<NewMudhafEntity> {
        return this.quranRepository.getmudhafsurah(surah)

    }



    fun getsifaFileterSurahAyah(surah: Int, ayah: Int): List<SifaEntity> {
        return  this.quranRepository.getsifa(surah, ayah)

    }
    fun getsifaFileterSurah(surah: Int): List<SifaEntity> {
        return  this.quranRepository.getsifaFilterSurah(surah)

    }




    fun getVerbRootBySurahAyahWord(
        cid: Int,
        aid: Int,
        wid: Int,
    ): MutableLiveData<List<VerbCorpus>> {
        verbcorpuslist.value = this.quranRepository.getVerbRootBySurahAyahWord(cid, aid, wid)
        return verbcorpuslist
    }


    fun getAllVerbCorpus(): MutableLiveData<List<VerbCorpus>> {
        verbcorpuslist.value = this.quranRepository.getAllVerbCorpus()
        return verbcorpuslist
    }


    fun getQuranCorpusWbw(cid: Int, aid: Int, wid: Int): MutableLiveData<List<QuranCorpusWbw>> {
        corpuswbwlist.value = this.quranRepository.getQuranCorpusWbw(cid, aid, wid)
        return corpuswbwlist
    }


    fun getCorpusEntityFilterbywordno(cid: Int, aid: Int, wid: Int): MutableLiveData<List<CorpusEntity>> {
        corpusentity.value = this.quranRepository.getCorpusEntityFilterbywordno(cid, aid, wid)
        return corpusentity
    }


    fun getCorpusEntityFilterSurahAya(cid: Int, aid: Int, ): List<CorpusEntity> {
      return  this.quranRepository.getCorpusEntityFilterSurahAya(cid, aid)

    }

    fun getAbsoluteNegationFilerSurahAyah(cid: Int, aid: Int, ): List<AbsoluteNegationEnt> {
        return  this.quranRepository.getAbsoluteNegationFilerSurahAyah(cid, aid)

    }
    fun getAbsoluteNegationFilterSurah(cid: Int ): List<AbsoluteNegationEnt> {
        return  this.quranRepository.getAbsoluteNegationFilterSurah(cid)

    }
    fun getAbsoluteNegationAll(cid: Int ): List<AbsoluteNegationEnt> {
        return  this.quranRepository.getAbsoluteNegationFilterSurah(cid)

    }


    fun getLamMudharyNegationFilerSurahAyah(cid: Int, aid: Int, ): List<PastTenceNegatonEnt> {
        return  this.quranRepository.getLamMudharyNegationFilerSurahAyah(cid, aid)

    }
    fun getPastTencefilterSurah(cid: Int ): List<PastTenceNegatonEnt> {
        return  this.quranRepository.getPastTencefilterSurah(cid)

    }

    fun getPresentNegationFilterSurah(cid: Int ): List<PresentTenceNegatonEnt> {
        return  this.quranRepository.getPresentNegationFilterSurah(cid)

    }

    fun getFutureTencefilterSurah(cid: Int ): List<FutureTenceNegatonEnt> {
        return  this.quranRepository.getFutureTencefilterSurah(cid)

    }


    fun geNegationFilerSurahAyah(cid: Int,aid: Int, ): List<NegationEnt> {
        return  this.quranRepository.geNegationFilerSurahAyah(cid,aid)

    }

    fun getNegationAll(): List<NegationEnt> {
        return  this.quranRepository.getNegationAll()

    }

    fun getNegationFilterSurah(cid: Int ): List<NegationEnt> {
        return  this.quranRepository.getNegationFilterSurah(cid)

    }








    fun getLamMudharyNegationAll(): List<PastTenceNegatonEnt> {
        return  this.quranRepository.getLamMudharyNegationAll()

    }

    fun getCorpusEntityFilterSurah(cid: Int ): MutableLiveData<List<CorpusEntity>> {
        corpusentity.value = this.quranRepository.getCorpusEntityFilterSurah(cid)
        return corpusentity
    }




    fun gethalsurahayah(cid: Int, aid: Int): MutableLiveData<List<HalEnt>> {
        halword.value = this.quranRepository.gethalsurahayah(cid, aid)
        return halword
    }

    fun getMafoolbihiword(surah: Int, ayah: Int, wordno: Int): MutableLiveData<List<MafoolBihi>> {
        mafoolBihi.value = this.quranRepository.getMafoolbihi(surah, ayah, wordno)
        return mafoolBihi
    }


    fun getAjlihiword(surah: Int, ayah: Int, wordno: Int): MutableLiveData<List<LiajlihiEnt>> {
        ajlihiword.value = this.quranRepository.getAjlihiword(surah, ayah, wordno)
        return ajlihiword
    }


    fun getMutlaqWOrd(surah: Int, ayah: Int, wordno: Int): MutableLiveData<List<MafoolMutlaqEnt>> {
        mutlaqword.value = this.quranRepository.getMutlaqWOrd(surah, ayah, wordno)
        return mutlaqword
    }

    fun getTameezword(surah: Int, ayah: Int, wordno: Int): MutableLiveData<List<TameezEnt>> {
        tameez.value = this.quranRepository.getTameezword(surah, ayah, wordno)
        return tameez

    }



    fun getHans(root: String): MutableLiveData<List<hanslexicon>> {
        viewModelScope.launch {
            hanslist.value = quranRepository.getHansRoot(root)
        }
        return hanslist
    }

    fun getLanes(root: String): MutableLiveData<List<lanerootdictionary>> {
        viewModelScope.launch {
            laneslist.value = quranRepository.getLanesRoot(root)
        }
        return laneslist
    }


    fun getBookmarks(): LiveData<List<BookMarks>> {
        viewModelScope.launch {
            bokmarks = quranRepository.bookmarlist
        }
        return bokmarks
    }

    fun getBookmarksCollection(): LiveData<List<BookMarks>> {
        viewModelScope.launch {
            bokmarks = quranRepository.bookmarckcollection
        }
        return bokmarks
    }


    fun getHalsurah(cid: Int): MutableLiveData<List<HalEnt>> {
        viewModelScope.launch {
            haliya.value = quranRepository.gethalsurah(cid)
        }
        return haliya
    }

    fun getTameezsurah(cid: Int): MutableLiveData<List<TameezEnt>> {
        viewModelScope.launch {
            tameez.value = quranRepository.gettameezsurah(cid)
        }
        return tameez
    }

    fun getMutlaqSurah(cid: Int): MutableLiveData<List<MafoolMutlaqEnt>> {
        viewModelScope.launch {
            mutlaq.value = quranRepository.getmutlaqsura(cid)
        }
        return mutlaq
    }

    fun getLiajlihiSurah(cid: Int): MutableLiveData<List<LiajlihiEnt>> {
        viewModelScope.launch {
            liajlihi.value = quranRepository.getliajlihsura(cid)
        }
        return liajlihi
    }


    fun getMafoolSurah(cid: Int): MutableLiveData<List<MafoolBihi>> {
        viewModelScope.launch {
            mafoolb.value = quranRepository.getMafoolbihiSurah(cid)
        }
        return mafoolb
    }


    //  private val allUsers: LiveData<List<ChaptersAnaEntity>> get() = allchapters


    fun loadListschapter(): MutableLiveData<List<ChaptersAnaEntity>> {

        //delay is simulating network request delay
        //delay(1000)
        //listOf is simulating usersRepository.getUsers()
        chapterslist.value = this.quranRepository.chaptersmutable as List<ChaptersAnaEntity>?
        return chapterslist
    }
    fun getChapterBySurah(chapterId: Int): MutableLiveData<List<ChaptersAnaEntity>> {

        //delay is simulating network request delay
        //delay(1000)
        //listOf is simulating usersRepository.getUsers()
        chapterslist.value = this.quranRepository.getSingleChapters(chapterId)
        return chapterslist
    }



    fun getAllChapters(): LiveData<List<ChaptersAnaEntity>> {


        viewModelScope.launch {
            chapters = quranRepository.chapters
        }


        return chapters
    }

    fun getbadalSurah(cid: Int): MutableLiveData<List<BadalErabNotesEnt>> {
        viewModelScope.launch {
            badal.value = quranRepository.getbadalnotes(cid)
        }
        return badal
    }

    fun getVersesBySurahLive(cid: Int): MutableLiveData<List<QuranEntity>> {


        viewModelScope.launch {
            allquran.value = quranRepository.getsurahbychap(cid)
        }


        return allquran
    }

    fun getsurahayahVerses(cid: Int, ayid: Int): MutableLiveData<List<QuranEntity>> {


        viewModelScope.launch {
            allquran.value = quranRepository.getsurahbyayah(cid, ayid)
        }


        return allquran
    }

    fun getquranbySUrah(cid: Int): LiveData<List<QuranEntity>> {


        viewModelScope.launch {
            allquran.value = quranRepository.getsurahbychap(cid)
        }


        return allquran
    }


    fun getQuranCorpusWbwbysurah(cid: Int): LiveData<List<QuranCorpusWbw>> {


        viewModelScope.launch {
            qurancorpus.value = quranRepository.getQuranCorpusWbwbysurah(cid)
        }


        return qurancorpus
    }

    fun getQuranCorpusWbwbyroot(root: String): LiveData<List<QuranCorpusWbw>> {


        viewModelScope.launch {
            qurancorpus.value = quranRepository.getQuranCorpusWbwbyroot(root)
        }


        return qurancorpus
    }


    fun getQuranCorpusWbwbysurahAyah(cid: Int, aid: Int): LiveData<List<QuranCorpusWbw>> {


        viewModelScope.launch {
            qurancorpus.value = quranRepository.getQuranCorpusWbwbysurahAyah(cid, aid)
        }


        return qurancorpus
    }

    fun getsurahayahVerseslist(cid: Int, ayid: Int): MutableLiveData<List<QuranEntity>> {



            quranlist.value = quranRepository.getsurahbyayahlist(cid, ayid)



        return quranlist
    }
    fun getNouncorpus(surah: Int, ayah: Int, wordno: Int): MutableLiveData<List<NounCorpus>> {

        nounbywordno.value = this.quranRepository.getNouncorpus(surah, ayah, wordno)
        return nounbywordno
    }




    fun getSurahSummary(cid: Int): LiveData<List<surahsummary>> {


        viewModelScope.launch {
            sursumm = quranRepository.getSurahSummary(cid)
        }


        return sursumm
    }

    fun Insertbookmark(bookmar: BookMarks) = viewModelScope.launch {

        quranRepository.insertlive(bookmar)
    }


    fun deletebookmark(bookmar: BookMarks) = viewModelScope.launch {

        quranRepository.delete(bookmar)
    }

    fun deleteCollection(bookmar: String) = viewModelScope.launch {

        quranRepository.deletecollection(bookmar)
    }

    fun getwbwQuranTranslationRange(surahid: Int, ayahid: Int, startindex: Int, endindex: Int):
            LiveData<List<wbwentity>> {
        wbw.value =
            this.quranRepository.wbwdao.getwbwQuranbTranslationbyrange(
                surahid,
                ayahid,
                startindex,
                endindex
            )
        return wbw

    }


    fun getwbwTranslationbywordno(
        surahid: Int,
        ayahid: Int,
        wordno: Int,
    ): LiveData<List<wbwentity>> {
        wbw.value = this.quranRepository.wbwdao.getwbwTranslationbywordno(surahid, ayahid, wordno)
        return wbw

    }
}

