package com.example.utility

import FileUtility
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.Constant
import com.example.Constant.harfshartspanDark
import com.example.Constant.jawabshartspanDark

import com.example.Constant.shartspanDark
import com.example.justJava.FrameSpan
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.CorpusExpandWbwPOJO
import com.example.mushafconsolidated.Entities.FutureTenceListingPojo
import com.example.mushafconsolidated.Entities.InMaListingPOJO
import com.example.mushafconsolidated.Entities.NasbListingPojo
import com.example.mushafconsolidated.Entities.NewMudhafEntity

import com.example.mushafconsolidated.Entities.PastTencePOJO
import com.example.mushafconsolidated.Entities.PresentTencePOJO
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.ShartListingPojo
import com.example.mushafconsolidated.Entities.SifaEntity
import com.example.mushafconsolidated.Entities.SifaListingPojo
import com.example.mushafconsolidated.R


import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.model.NewNewQuranCorpusWbw
import com.example.mushafconsolidated.model.NewQuranCorpusWbw
import com.example.mushafconsolidated.model.QuranCorpusWbw
import com.example.mushafconsolidated.model.QuranEntityCorpusEntityWbwEntity
import com.example.utility.QuranGrammarApplication.Companion.context
import org.ahocorasick.trie.Trie
import java.util.regex.Pattern

class CorpusUtilityorig(private var context: Context?) {
    var ayah = 0

    constructor(context: Context, suraid: Int, ayah: Int) : this(context) {
        this.context=context
        this.ayah = ayah
    }

    // --Commented out by Inspection (15/08/23, 4:17 pm):final ArrayList<MousufSifa> NEWmousufSifaArrayList = new ArrayList<>();
    private val preferences: String?
    var activity: Activity? = null

    init {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        preferences = prefs.getString("theme", "dark")
        val preferences = prefs.getString("theme", "dark")
        dark = preferences == "dark" || preferences == "blue" || preferences == "green"

    }

    fun SetMousufSifaDB(
        corpusayahWordArrayList: LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>,
        surah_id: Int,
        ayah: Int,
        size: Int,
    ) {
        val utils = Utils(QuranGrammarApplication.context!!)
        val surah = utils.getSifabySurahAyah(surah_id, ayah)
        //  SpannableStringBuilder spannableverse = null;
        // SpannableString spannableString = null;
//todo 2 188 iza ahudu
        //todo 9;92 UNCERTAIN
        //TODO 9:94 JAWABHARMAHDOOF 9 95 JAWABHSARMAHODFF
        //TO 9;118 IZA IN THE MEANING OF HEENA AND 9 122 IZA AS HEENA
        for (sifaEntity in surah!!) {
            val indexstart = sifaEntity.startindex
            val indexend = sifaEntity.endindex
            //  sifaspans = new BackgroundColorSpan(WBURNTUMBER);
            SifaSpansSetup(corpusayahWordArrayList, sifaEntity, indexstart, indexend, size)
        }
    }

    private fun SifaSpansSetup(
        corpusayahWordArrayList: LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>,
        sifaEntity: SifaEntity,
        indexstart: Int,
        indexend: Int,
        size: Int,
    ) {
        val spannableverse: SpannableString
        try {
            //  spannableverse = corpusayahWordArrayList[sifaEntity.ayah - 1].spannableverse!!
            spannableverse =
                corpusayahWordArrayList[size - 1]!![0].spannableverse!!
            //spannableString = SpannableString.valueOf(corpusayahWordArrayList.get(sifaEntity.getAyah() - 1).getSpannableverse());
            try {
                if (indexstart == 0 || indexstart > 0) {
                    //   sifaspansDark = getSpancolor(preferences, false);
                    if (dark) {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                    } else {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                    }
                    spannableverse.setSpan(
                        Constant.sifaspansDark,
                        indexstart,
                        indexend,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }
        } catch (e: IndexOutOfBoundsException) {
            //System.out.println(e.getMessage());
        }
    }




    private fun MudhafSpansSetup(
        frameshartharf: FrameSpan,
        corpusayahWordArrayList: LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>,
        mudhafen: NewMudhafEntity,
        indexstart: Int,
        indexend: Int,
        size: Int,
    ) {
        lateinit var spannableverse: SpannableString

        try {
            //   spannableverse = corpusayahWordArrayList!!.get(0)!!.get(0).spannableverse
            //  spannableverse = corpusayahWordArrayList[sifaEntity.ayah - 1].spannableverse!!
            try {
                spannableverse = corpusayahWordArrayList[size - 1]?.get(0)!!.spannableverse!!
            } catch (e: NullPointerException) {

                println(e.localizedMessage)
                println(corpusayahWordArrayList[0]?.get(0)?.corpus!!.ayah)
                println(corpusayahWordArrayList[mudhafen.ayah]?.get(0)!!.corpus!!.ayah)
            }

            // spannableString = SpannableString.valueOf(corpusayahWordArrayList.get(mudhafen.getAyah() - 1).getSpannableverse());
            try {
                if (indexstart == 0 || indexstart > 0) {
                    val colorSpan = if (dark) {
                        Constant.mudhafspanDarks
                    } else {
                        Constant.mudhafspanLight
                    }
                    spannableverse.setSpan(
                        colorSpan,
                        indexstart,
                        indexend,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }
        } catch (e: IndexOutOfBoundsException) {
            //System.out.println(e.getMessage());
        }
    }





//overload

    fun SetMousufSifaDB(
        corpusayahWordArrayList: LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>,
        surah_id: Int,
    ) {
        val utils = Utils(QuranGrammarApplication.context!!)
        val surah = utils.getSifabySurah(surah_id)
        //  SpannableStringBuilder spannableverse = null;
        // SpannableString spannableString = null;
//todo 2 188 iza ahudu
        //todo 9;92 UNCERTAIN
        //TODO 9:94 JAWABHARMAHDOOF 9 95 JAWABHSARMAHODFF
        //TO 9;118 IZA IN THE MEANING OF HEENA AND 9 122 IZA AS HEENA
        for (sifaEntity in surah!!) {
            val indexstart = sifaEntity!!.startindex
            val indexend = sifaEntity.endindex
            //  sifaspans = new BackgroundColorSpan(WBURNTUMBER);
            SifaSpansSetupbysurah(corpusayahWordArrayList, sifaEntity, indexstart, indexend)
        }
    }

    private fun SifaSpansSetupbysurah(
        corpusayahWordArrayList: LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>,
        sifaEntity: SifaEntity,
        indexstart: Int,
        indexend: Int,
    ) {
        val spannableverse: SpannableString
        try {
            //  spannableverse = corpusayahWordArrayList[sifaEntity.ayah - 1].spannableverse!!
            spannableverse =
                corpusayahWordArrayList[sifaEntity.ayah - 1]!![0].spannableverse!!
            //spannableString = SpannableString.valueOf(corpusayahWordArrayList.get(sifaEntity.getAyah() - 1).getSpannableverse());
            try {
                if (indexstart == 0 || indexstart > 0) {
                    //   sifaspansDark = getSpancolor(preferences, false);
                    if (dark) {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                    } else {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                    }
                    spannableverse.setSpan(
                        Constant.sifaspansDark,
                        indexstart,
                        indexend,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }
        } catch (e: IndexOutOfBoundsException) {
            //System.out.println(e.getMessage());
        }
    }

    fun SetsifaListing(

        sifa: List<SifaListingPojo>,


        ) {
        if (dark) {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
        } else {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
        }
        var spannableverse: SpannableString
        for (sif in sifa!!) {
            val indexstart = sif.startindex
            val indexend = sif.endindex
            spannableverse = sif.spannableVerse!!
            try {
                spannableverse.setSpan(
                    Constant.sifaspansDark,
                    indexstart,
                    indexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }

        }


    }

    fun setPresentSpanListing(

        sifa: List<PresentTencePOJO>,


        ) {
        if (dark) {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
        } else {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
        }
        var spannableverse: SpannableString
        for (sif in sifa!!) {
            val indexstart = sif.startindex
            val indexend = sif.endindex
            spannableverse = sif.spannableVerse!!
            try {
                spannableverse.setSpan(
                    Constant.sifaspansDark,
                    indexstart,
                    indexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }

        }


    }

    fun setPastSpanListing(

        sifa: List<PastTencePOJO>,


        ) {
        if (dark) {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
        } else {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
        }
        var spannableverse: SpannableString
        for (sif in sifa!!) {
            val indexstart = sif.startindex
            val indexend = sif.endindex
            spannableverse = sif.spannableVerse!!
            try {
                spannableverse.setSpan(
                    Constant.sifaspansDark,
                    indexstart,
                    indexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }

        }


    }

    fun setInMaSpanlisting(

        sifa: List<InMaListingPOJO>,


        ) {
        if (dark) {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
        } else {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
        }
        var spannableverse: SpannableString
        for (sif in sifa!!) {
            val indexstart = sif.startindex
            val indexend = sif.endindex
            spannableverse = sif.spannableVerse!!
            try {
                spannableverse.setSpan(
                    Constant.sifaspansDark,
                    indexstart,
                    indexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }

        }


    }

    fun setSpanFutureTenceNegationListing(

        sifa: List<FutureTenceListingPojo>,


        ) {
        if (dark) {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
        } else {
            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
        }
        var spannableverse: SpannableString
        for (sif in sifa!!) {
            val indexstart = sif.startindex
            val indexend = sif.endindex
            spannableverse = sif.spannableVerse!!
            try {
                spannableverse.setSpan(
                    Constant.sifaspansDark,
                    indexstart,
                    indexend,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }

        }


    }


    fun HarfNasbDb(


        accusativesSentences: List<NasbListingPojo>,
    ) {


        var counter = 0

        var spannableverse: SpannableString
        val err = ArrayList<String>()
        for (nasb in accusativesSentences!!) {
            val indexstart = nasb!!.indexstart
            val indexend = nasb.indexend
            val ismstartindex = nasb.ismstart
            val ismendindex = nasb.ismend
            val khabarstart = nasb.khabarstart
            val khabarend = nasb.khabarend
            var ayahequals = false

            if (dark) {
                Constant.harfinnaspanDark = ForegroundColorSpan(Color.GREEN)
            } else {
                Constant.harfinnaspanDark =
                    ForegroundColorSpan(Constant.KASHMIRIGREEN)
            }
            try {


                spannableverse = nasb.spannableVerse!!
                try {

                    //  harfinnaspanDark=new ForegroundColorSpan(GREEN);
                    spannableverse.setSpan(
                        Constant.harfinnaspanDark,
                        indexstart,
                        indexend,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } catch (e: IndexOutOfBoundsException) {
                    //    System.out.println(nasb.getSurah() + ":" + nasb.getAyah());
                    err.add(nasb.surah.toString() + ":" + nasb.ayah)
                }
                try {
                    //    spannableverse.setSpan(new ForegroundColorSpan(GOLD), ismindexone, ismindexone + lenism1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (dark) {
                        Constant.harfismspanDark = ForegroundColorSpan(Constant.BCYAN)
                    } else {
                        Constant.harfismspanDark =
                            ForegroundColorSpan(Constant.prussianblue)
                    }
                    spannableverse.setSpan(
                        Constant.harfismspanDark,
                        ismstartindex,
                        ismendindex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } catch (e: IndexOutOfBoundsException) {
                    //     System.out.println(nasb.getSurah() + ":" + nasb.getAyah());
                    err.add(nasb.surah.toString() + ":" + nasb.ayah)
                }
                try {
                    if (dark) {
                        Constant.harfkhabarspanDark = ForegroundColorSpan(Color.YELLOW)
                    } else {
                        Constant.harfkhabarspanDark =
                            ForegroundColorSpan(Constant.deepburnsienna)
                    }
                    spannableverse.setSpan(
                        Constant.harfkhabarspanDark,
                        khabarstart,
                        khabarend,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } catch (e: IndexOutOfBoundsException) {
                    //   System.out.println(nasb.getSurah() + ":" + nasb.getAyah());
                    err.add(nasb.surah.toString() + ":" + nasb.ayah)
                }

            } catch (e: IndexOutOfBoundsException) {
                println("check")
            }


        }

    }





    private fun MudhafSpansSetup(
        frameshartharf: FrameSpan,
        corpusayahWordArrayList: LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>,
        mudhafen: NewMudhafEntity,
        indexstart: Int,
        indexend: Int,
    ) {
        lateinit var spannableverse: SpannableString

        try {
            //   spannableverse = corpusayahWordArrayList!!.get(0)!!.get(0).spannableverse
            //  spannableverse = corpusayahWordArrayList[sifaEntity.ayah - 1].spannableverse!!
            try {
                spannableverse =
                    corpusayahWordArrayList[mudhafen.ayah - 1]?.get(0)!!.spannableverse!!
            } catch (e: NullPointerException) {

                println(e.localizedMessage)
                println(corpusayahWordArrayList[0]?.get(0)?.corpus!!.ayah)
                println(corpusayahWordArrayList[mudhafen.ayah]?.get(0)!!.corpus!!.ayah)
                spannableverse =
                    corpusayahWordArrayList[mudhafen.ayah]?.get(0)!!.spannableverse!!
            }

            // spannableString = SpannableString.valueOf(corpusayahWordArrayList.get(mudhafen.getAyah() - 1).getSpannableverse());
            try {
                if (indexstart == 0 || indexstart > 0) {

                    val colorSpan = if (dark) {
                        Constant.mudhafspanDarks
                    } else {
                        Constant.mudhafspanLight
                    }

                    spannableverse.setSpan(
                        colorSpan,
                        indexstart,
                        indexend,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }
        } catch (e: IndexOutOfBoundsException) {
            //System.out.println(e.getMessage());
        }
    }

    fun setShartDisplay(
        shartSentences: List<ShartListingPojo>,


        ) {

        val builder = androidx.appcompat.app.AlertDialog.Builder(QuranGrammarApplication.context!!)

            if (dark) {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.BYELLOW)
                Constant.shartspanDark = ForegroundColorSpan(Constant.BCYAN)
                Constant.jawabshartspanDark = ForegroundColorSpan(Color.CYAN)
            } else {
                Constant.harfshartspanDark = ForegroundColorSpan(Constant.FORESTGREEN)
                Constant.shartspanDark = ForegroundColorSpan(Constant.WMIDNIHTBLUE)
                Constant.jawabshartspanDark = ForegroundColorSpan(Constant.WHOTPINK)
            }


            for (shart in shartSentences!!) {
                val indexstart = shart!!.startindex
                val indexend = shart.endindex

                try {
                    if(shart.type=="shartonly"){

                        val singleSpacedText = shart.arabictext!!.replace("\\s+".toRegex(), " ")
                        //    val (firstWord, restOfString) = singleSpacedText.split(" ", limit = 2)
                        val firstWord = singleSpacedText.substringBefore(" ")
                        val restOfString = singleSpacedText.substringAfter(" ")
                        val harfindex= shart.spannableVerse!!.indexOf(firstWord)
                        val shartindex=shart.spannableVerse!!.indexOf(restOfString)
                        if(harfindex!=-1 && shartindex!=-1 ) {

                            shart.spannableVerse!!.setSpan(
                                Constant.harfshartspanDark,
                                harfindex,
                                harfindex + firstWord.length,
                                0
                            )

                            shart.spannableVerse!!.setSpan(
                                Constant.shartspanDark,
                                harfindex + firstWord.length,
                                shartindex + restOfString.length,
                                0


                            )
                            shart.spannableVerse!!.setSpan(
                                UnderlineSpan(),
                                harfindex,
                                shartindex+restOfString.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }

                    }
                    else    if(shart.type=="shartnojawab"){
                        val singleSpacedText = shart.arabictext!!.replace("\\s+".toRegex(), " ")
                        val (firstWord, restOfString) = singleSpacedText.split(" ", limit = 2)
                        val firstWordStripped = firstWord.replace(":", "")
                        val harfindex=shart.spannableVerse!!.indexOf(firstWordStripped)
                        val shartindex=shart.spannableVerse!!.indexOf(restOfString)
                        if(harfindex!=-1 && shartindex!=-1 ) {
                            shart.spannableVerse!!.setSpan(
                                harfshartspanDark,
                                harfindex,
                                harfindex + firstWordStripped.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            shart.spannableVerse!!.setSpan(
                                Constant.shartspanDark,
                                harfindex + firstWordStripped.length,
                                shartindex + restOfString.length,

                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )

                            shart.spannableVerse!!.setSpan(
                                UnderlineSpan(),
                                harfindex,
                                harfindex+shartindex+restOfString.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }

                    }
                    else if(shart.type=="shartall"){
                        val singleSpacedText = shart.arabictext!!.replace("\\s+".toRegex(), " ")
                        val split = singleSpacedText.split(":")
                        //    if (split.size >= 2) {
                        val regex = Regex("\\.\\.+")
                        val firstWordt = split[0]
                        val firstWord = regex.replace(firstWordt, "")
                        var seconthirdstring = split[1]

                        val laststring = split[2]
                        val totalLength = firstWord.length + seconthirdstring.length
                        val harfindex=shart.spannableVerse!!.indexOf(firstWordt)
                        val shartindex=shart.spannableVerse!!.indexOf(seconthirdstring)
                        val jawabindex=shart.spannableVerse!!.indexOf(laststring)

                        if(harfindex!=-1 && shartindex!=-1 && jawabindex!=-1){
//    Constant.harfshartspanDark,
                            //    Constant.jawabshartspanDark,
                            //         Constant.harfkhabarspanDark,
                            shart.spannableVerse!!.setSpan(
                                harfshartspanDark,
                                harfindex,
                                harfindex+firstWordt.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                            shart.spannableVerse!!.setSpan(
                                shartspanDark,
                                harfindex+firstWordt.length,
                                shartindex+seconthirdstring.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            shart.spannableVerse!!.setSpan(
                                jawabshartspanDark,
                                jawabindex,
                                jawabindex+laststring.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            shart.spannableVerse!!.setSpan(
                                UnderlineSpan(),
                                harfindex,
                                jawabindex+laststring.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )

                        }



                    }
                } catch (e: IndexOutOfBoundsException) {

                    Toast.makeText(QuranGrammarApplication.context!!, "Surah ID: ${shart.surah}, Ayah ID: ${shart.ayah}", Toast.LENGTH_LONG).show()

                }
            }




    }







    fun composeWBWCollection(
        allofQuran: List<QuranEntity>?,
        corpusSurahWord: List<QuranCorpusWbw>?
    ): LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>> {

        val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>()
        var qurancorpusarray = ArrayList<NewQuranCorpusWbw>()


        var aindex = 0
        var secondindex = 0

        while (aindex <= allofQuran!!.size) {

            var ayahWord = NewQuranCorpusWbw()

            try {
                while (corpusSurahWord!![secondindex].corpus.ayah <= allofQuran[aindex].ayah) {
                    if (corpusSurahWord[secondindex].corpus.ayah != allofQuran[aindex].ayah) {
                        break
                    }

                    ayahWord.spannableverse =
                        SpannableString.valueOf(allofQuran[aindex].qurantext)
                    ayahWord.wbw = corpusSurahWord[secondindex].wbw
                    ayahWord.corpus = corpusSurahWord[secondindex++].corpus
                    qurancorpusarray.add(ayahWord)

                    ayahWord = NewQuranCorpusWbw()
                }
            } catch (e: IndexOutOfBoundsException) {
                println(e.message)
            }

            if (qurancorpusarray.isNotEmpty()) {
                newnewadapterlist[aindex] = qurancorpusarray
                val ayahWord = NewQuranCorpusWbw()
            }
            qurancorpusarray = ArrayList()
            aindex++
        }

        return newnewadapterlist

    }


    companion object {
        var dark = true

        @JvmStatic
        fun NewSetWordSpanTag(
            tagone: String,
            tagtwo: String,
            tagthree: String,
            tagfour: String,
            tagfive: String,
            arafive: String,
            arafour: String,
            arathree: String,
            aratwo: String,
            araone: String,
        ): SpannableString? {
            var arafive = arafive
            var arafour = arafour
            var arathree = arathree
            var aratwo = aratwo
            var araone = araone
            var str: SpannableString? = null
            var tagcounter = 0
            val b = tagone.isNotEmpty()
            val bb = tagtwo.isNotEmpty()
            val bbb = tagthree.isNotEmpty()
            val bbbb = tagfour.isNotEmpty()
            val bbbbb = tagfive.isNotEmpty()
            if (b && !bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 1
            } else if (b && bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 2
            } else if (b && bb && bbb && !bbbb && !bbbbb) {
                tagcounter = 3
            } else if (b && bb && bbb && bbbb && !bbbbb) {
                tagcounter = 4
            } else if (b && bb && bbb && bbbb) {
                tagcounter = 5
            }
            araone = araone.trim { it <= ' ' }
            aratwo = aratwo.trim { it <= ' ' }
            arathree = arathree.trim { it <= ' ' }
            arafour = arafour.trim { it <= ' ' }
            arafive = arafive.trim { it <= ' ' }
            //
            val spanhash: Map<String?, ForegroundColorSpan> = stringForegroundColorSpanMap
            when (tagcounter) {
                1 -> {
                    //   Set<String> strings = spanhash.keySet();
                    str =
                        SpannableString(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagone],
                        0,
                        araone.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                2 -> {
                    val strone = SpannableString(araone.trim { it <= ' ' })
                    val strtwo = SpannableString(aratwo.trim { it <= ' ' })
                    strtwo.setSpan(
                        spanhash[tagtwo],
                        0,
                        aratwo.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strone.setSpan(
                        spanhash[tagone],
                        0,
                        araone.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(strtwo, strone)
                    str = SpannableString(charSequence)
                }

                3 -> {
                    spanhash[tagone]
                    val strone = SpannableString(araone.trim { it <= ' ' })
                    val strtwo = SpannableString(aratwo.trim { it <= ' ' })
                    val strthree = SpannableString(arathree.trim { it <= ' ' })
                    strthree.setSpan(
                        spanhash[tagthree],
                        0,
                        arathree.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strtwo.setSpan(
                        spanhash[tagtwo],
                        0,
                        aratwo.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strone.setSpan(
                        spanhash[tagone],
                        0,
                        araone.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(strthree, strtwo, strone)
                    str = SpannableString(charSequence)
                }

                4 -> {
                    //  str = new SpannableString(arafour.trim() + arathree.trim() + aratwo.trim() + araone.trim());
                    val strone = SpannableString(araone.trim { it <= ' ' })
                    val strtwo = SpannableString(aratwo.trim { it <= ' ' })
                    val strthree = SpannableString(arathree.trim { it <= ' ' })
                    val strfour = SpannableString(arafour.trim { it <= ' ' })
                    strfour.setSpan(
                        spanhash[tagfour],
                        0,
                        arafour.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strthree.setSpan(
                        spanhash[tagthree],
                        0,
                        arathree.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strtwo.setSpan(
                        spanhash[tagtwo],
                        0,
                        aratwo.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strone.setSpan(
                        spanhash[tagone],
                        0,
                        araone.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(strfour, strthree, strtwo, strone)
                    str = SpannableString(charSequence)
                }

                5 -> {
                    val strone = SpannableString(araone.trim { it <= ' ' })
                    val strtwo = SpannableString(aratwo.trim { it <= ' ' })
                    val strthree = SpannableString(arathree.trim { it <= ' ' })
                    val strfour = SpannableString(arafour.trim { it <= ' ' })
                    val strfive = SpannableString(arafive.trim { it <= ' ' })
                    strfive.setSpan(
                        spanhash[tagone],
                        0,
                        arafive.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strfour.setSpan(
                        spanhash[tagtwo],
                        0,
                        arafour.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strthree.setSpan(
                        spanhash[tagthree],
                        0,
                        arathree.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strtwo.setSpan(
                        spanhash[tagfour],
                        0,
                        aratwo.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strone.setSpan(
                        spanhash[tagfive],
                        0,
                        araone.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val charSequence = TextUtils.concat(strone, strtwo, strthree, strfour, strfive)
                    str = SpannableString(charSequence)
                }
            }
            return str
        }

        @JvmStatic
        fun ColorizeRootword(
            tagone: String, tagtwo: String, tagthree: String, tagfour: String, tagfive: String,
            rootword: String,
        ): SpannableString? {
            var str: SpannableString? = null
            var tagcounter = 0
            val b = tagone.isNotEmpty()
            val bb = tagtwo.isNotEmpty()
            val bbb = tagthree.isNotEmpty()
            val bbbb = tagfour.isNotEmpty()
            val bbbbb = tagfive.isNotEmpty()
            if (b && !bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 1
            } else if (b && bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 2
            } else if (b && bb && bbb && !bbbb && !bbbbb) {
                tagcounter = 3
            } else if (b && bb && bbb && bbbb && !bbbbb) {
                tagcounter = 4
            } else if (b && bb && bbb && bbbb) {
                tagcounter = 5
            }
            //   SharedPreferences sharedPreferences =
            //      androidx.preference.PreferenceManager.getDefaultSharedPreferences(DarkThemeApplication.context!!);
            //   String isNightmode = sharedPreferences.getString("themepref", "dark" );
            //   if (isNightmode.equals("dark")||isNightmode.equals("blue")) {
            val spanhash: Map<String?, ForegroundColorSpan> = stringForegroundColorSpanMap
            //  }else{
            //   spanhash = getColorSpanforPhrasesLight();
            //  }
            if (tagcounter == 1) {
                str = SpannableString(rootword.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, rootword.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else if (tagcounter == 2) {
                if (tagone == "N" || tagone == "ADJ" || tagone == "PN" || tagone == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagone],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else if (tagtwo == "N" || tagtwo == "ADJ" || tagtwo == "PN" || tagtwo == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagtwo],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else if (tagcounter == 3) {
                if (tagone == "N" || tagone == "ADJ" || tagone == "PN" || tagone == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagone],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else if (tagtwo == "N" || tagtwo == "ADJ" || tagtwo == "PN" || tagtwo == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagtwo],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else if (tagthree == "N" || tagthree == "ADJ" || tagthree == "PN" || tagthree == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagthree],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else if (tagcounter == 4) {
                if (tagone == "N" || tagone == "ADJ" || tagone == "PN" || tagone == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagone],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else if (tagtwo == "N" || tagtwo == "ADJ" || tagtwo == "PN" || tagtwo == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagtwo],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else if (tagthree == "N" || tagthree == "ADJ" || tagthree == "PN" || tagthree == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagthree],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else if (tagfour == "N" || tagfour == "ADJ" || tagfour == "PN" || tagfour == "V") {
                    str = SpannableString(rootword.trim { it <= ' ' })
                    str.setSpan(
                        spanhash[tagthree],
                        0,
                        rootword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            return str
        }

        @JvmStatic
        fun searchForTameez(allofQuran: List<QuranEntity>?)  :List<String> {
            var mudhafColoragainstBlack = 0
            var mausofColoragainstBlack = 0
            var sifatColoragainstBlack = 0
            var brokenPlurarColoragainstBlack = 0
            var shartagainstback = 0

            val inshartiastr = "«إِنْ» شرطية"
            val izazarfshartsrt = "وإذا ظرف يتضمن معنى الشرط"
            val izashartiastr = "«إِذا» ظرف يتضمن معنى الشرط"
            val jawabshartstr = "جواب شرط"
            val jawabsharttwostr = "لجواب الشرط"
            val jawabalshart = "جواب الشرط"
            val jawab = "جواب"
            val shart = ArrayList<String>()
            val mutlaq = ArrayList<String>()

            mutlaq.add("")
            shart.add(inshartiastr)
            shart.add(izazarfshartsrt)
            shart.add(izashartiastr)
            shart.add(jawabshartstr)
            shart.add(jawabsharttwostr)
            shart.add(jawabalshart)
            shart.add(jawab)
            shart.add("شرطية")
            shart.add("شرطية.")
            shart.add("ظرف متضمن معنى الشرط")
            shart.add("وإذا ظرف زمان يتضمن معنى الشرط")
            shart.add("ظرف زمان يتضمن معنى الشرط")
            shart.add("ولو حرف شرط غير جازم")
            shart.add("حرف شرط غير جازم")
            shart.add("اللام واقعة في جواب لو")
            shart.add("حرف شرط جازم")
            shart.add("الشرطية")
            val mudhafilahistr = "مضاف إليه"
            val sifastr = "صفة"
            val mudhaflenght = mudhafilahistr.length
            val sifalength = sifastr.length
            val hal = ArrayList<String>()
            hal.add("في محل نصب حال")
            hal.add("في محل نصب حال.")
            hal.add("والجملة حالية")
            hal.add("والجملة حالية.")
            hal.add("حالية")
            hal.add("حالية.")
            hal.add("حالية:")
            hal.add("حال")
            hal.add("حال:")
            hal.add("حال.")
            hal.add("الواو حالية")

            val badal = ArrayList<String>()
            badal.add("بدل")
            badal.add("بدل.")

            val mafoolbihi = ArrayList<String>()
            mafoolbihi.add("مفعول به")
            mafoolbihi.add("مفعول به.")
            mafoolbihi.add("مفعول به.(")
            mafoolbihi.add("في محل نصب مفعول")
            mafoolbihi.add("مفعول")
            val tameez = ArrayList<String>()
            tameez.add("تمييز")
            tameez.add("تمييز.")
            tameez.add("التمييز")

            val result = mutableListOf<String>()
            var capturelist: List<String> = emptyList()

            mutlaq.add("مطلق")
            mutlaq.add("مفعولا مطلقا")
            mutlaq.add("مفعولا مطلقا،")
            mutlaq.add("مطلق.")
            val ajilihi = ArrayList<String>()
            ajilihi.add("مفعول لأجله")
            ajilihi.add("لأجله")
            ajilihi.add("لأجله.")
        //   val regex = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(?=تمييز|تمييز\\.|التمييز)"//good one
             val regex = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(تمييز|تمييز\\.|التمييز)" //for capture keyword also
            val regexm = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(مطلق|مفعولا\\s+مطلقا|مفعولا\\s+مطلقا،|مطلق\\.)"
            val regexajlihi = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(لأجله.|لأجله\\.|مفعول لأجله)" //

           // val regexsifa="\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(صفة.|صفة\\.| الصفة)"
            val regexsifa = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(?:صفة\\.|صفة|الصفة)\\s+(\\w+)"
            val regexmudhaf = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(?:مضاف إليه.\\.|مضاف إليه|مضاف إليه.)\\s+(\\w+)"

            val regexbadal = "\\(([^)]+)\\)(?:\\s+\\w+)*\\s+(بدل.|بدل\\.|بدل)"
            val pattern = Pattern.compile(regexmudhaf)


            for (pojo in allofQuran!!) {
                //  String ar_irab_two = pojo.getAr_irab_two();

                val cleanedString = pojo.ar_irab_two.replace(Regex("[\n\r\r]"), "")

               // val ar_irab_two = pojo.ar_irab_two.replace("\n", "");
              //  val ar_irab_twos = pojo.ar_irab_two.replace("\r", "");
                val str = SpannableStringBuilder(cleanedString)
                val matcher = pattern.matcher(cleanedString)
                var dataString =""

                while (matcher.find()) {
                    val start = matcher.start()
                    val end = matcher.end()
                    val extracted=str.subSequence(start,end)
                     dataString =
                        "${pojo.surah}|${pojo.ayah}|$extracted|"
                    result.add(dataString)

                }



            }
           return result
        }

        @JvmStatic
        fun highlightKeywords(allofQuran: List<QuranEntity>?) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context)
            val theme = prefs.getString("themepref", "dark")

            val colorMap = when (theme) {
                "dark", "blue", "green" -> mapOf(
                    "shart" to prefs.getInt("shartback", Color.GREEN),
                    "mausof" to prefs.getInt("mausoofblack", Color.RED),
                    "mudhaf" to prefs.getInt("mudhafblack", Color.CYAN),
                    "sifat" to prefs.getInt("sifatblack", Color.YELLOW),
                    "broken" to prefs.getInt("brokenblack", Color.GREEN)
                )
                else -> mapOf(
                    "shart" to prefs.getInt("shartback", Constant.INDIGO),
                    "mausof" to prefs.getInt("mausoofwhite", Color.GREEN),
                    "mudhaf" to prefs.getInt("mudhafwhite", Constant.MIDNIGHTBLUE),
                    "sifat" to prefs.getInt("sifatwhite", Constant.ORANGE400),
                    "broken" to prefs.getInt("brokenwhite", Constant.DARKMAGENTA)
                )
            }

            val keywordGroups = mapOf(
                "shart" to listOf(
                    "«إِنْ» شرطية", "وإذا ظرف يتضمن معنى الشرط", "«إِذا» ظرف يتضمن معنى الشرط",
                    "جواب شرط", "لجواب الشرط", "جواب الشرط", "جواب", "شرطية", "شرطية.",
                    "ظرف متضمن معنى الشرط", "وإذا ظرف زمان يتضمن معنى الشرط", "ظرف زمان يتضمن معنى الشرط",
                    "ولو حرف شرط غير جازم", "حرف شرط غير جازم", "اللام واقعة في جواب لو", "حرف شرط جازم", "الشرطية"
                ),
                "mutlaq" to listOf("مطلق", "مفعولا مطلقا", "مفعولا مطلقا،", "مطلق."),
                "hal" to listOf(
                    "في محل نصب حال", "في محل نصب حال.", "والجملة حالية", "والجملة حالية.",
                    "حالية", "حالية.", "حالية:", "حال", "حال:", "حال.", "الواو حالية"
                ),
                "tameez" to listOf("تمييز", "تمييز.", "التمييز"),
                "mudhaf" to listOf("مضاف إليه.","مضاف إليه" ,"مضاف"),
                "badal" to listOf("بدل", "بدل."),
                "ajilihi" to listOf("مفعول لأجله", "لأجله", "لأجله."),
                "mafoolbihi" to listOf(
                    "مفعول به", "مفعول به.", "مفعول به.(", "في محل نصب مفعول", "مفعول"
                )
            )

            for (pojo in allofQuran!!) {
                val ar_irab_two = pojo.ar_irab_two.replace("\n", "")
                val str = SpannableStringBuilder(ar_irab_two)

                for ((key, keywords) in keywordGroups) {
                    val trie = Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(keywords).build()
                    val emits = trie.parseText(ar_irab_two)

                    for (emit in emits) {
                        str.setSpan(
                            ForegroundColorSpan(colorMap[key] ?: Color.BLACK), // Default to black if color not found
                            emit.start,
                            emit.start + emit.keyword.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }

                pojo.erabspnabble = str
            }
        }

        @JvmStatic
        fun HightLightKeyWordold(allofQuran: List<QuranEntity>?) {
            var mudhafColoragainstBlack = 0
            var mausofColoragainstBlack = 0
            var sifatColoragainstBlack = 0
            var brokenPlurarColoragainstBlack = 0
            var shartagainstback = 0
            val prefs =
                android.preference.PreferenceManager.getDefaultSharedPreferences(
                    QuranGrammarApplication.context
                )
            val preferences = prefs.getString("themepref", "dark")
            if (preferences == "dark" || preferences == "blue" || preferences == "green") {
                shartagainstback = prefs.getInt("shartback", Color.GREEN)
                mausofColoragainstBlack = prefs.getInt("mausoofblack", Color.RED)
                mudhafColoragainstBlack = prefs.getInt("mudhafblack", Color.CYAN)
                sifatColoragainstBlack = prefs.getInt("sifatblack", Color.YELLOW)
                brokenPlurarColoragainstBlack = prefs.getInt("brokenblack", Color.GREEN)
            } else {
                shartagainstback = prefs.getInt("shartback", Constant.INDIGO)
                mudhafColoragainstBlack = prefs.getInt("mausoofwhite", Color.GREEN)
                mausofColoragainstBlack = prefs.getInt("mudhafwhite", Constant.MIDNIGHTBLUE)
                sifatColoragainstBlack = prefs.getInt("sifatwhite", Constant.GREENDARK)
                brokenPlurarColoragainstBlack = prefs.getInt("brokenwhite", Constant.DARKMAGENTA)
            }
            val inshartiastr = "«إِنْ» شرطية"
            val izazarfshartsrt = "وإذا ظرف يتضمن معنى الشرط"
            val izashartiastr = "«إِذا» ظرف يتضمن معنى الشرط"
            val jawabshartstr = "جواب شرط"
            val jawabsharttwostr = "لجواب الشرط"
            val jawabalshart = "جواب الشرط"
            val jawab = "جواب"
            val shart = ArrayList<String>()
            val mutlaq = ArrayList<String>()
            val fael=ArrayList<String>()
            val mudhaf=ArrayList<String>()
            mutlaq.add("مطلق")
            mutlaq.add("مفعولا مطلقا")
            mutlaq.add("مفعولا مطلقا،")
            mutlaq.add("مطلق.")
            mutlaq.add("")
            shart.add(inshartiastr)
            shart.add(izazarfshartsrt)
            shart.add(izashartiastr)
            shart.add(jawabshartstr)
            shart.add(jawabsharttwostr)
            shart.add(jawabalshart)
            shart.add(jawab)
            shart.add("شرطية")
            shart.add("شرطية.")
            shart.add("ظرف متضمن معنى الشرط")
            shart.add("وإذا ظرف زمان يتضمن معنى الشرط")
            shart.add("ظرف زمان يتضمن معنى الشرط")
            shart.add("ولو حرف شرط غير جازم")
            shart.add("حرف شرط غير جازم")
            shart.add("اللام واقعة في جواب لو")
            shart.add("حرف شرط جازم")
            shart.add("الشرطية")
            val mudhafilahistr=ArrayList<String>()
            mudhafilahistr.add("مضاف")
            mudhafilahistr.add("مضاف إليه")
            mudhafilahistr.add("مضاف إليه.")
         //   val mudhafilahistr = "مضاف إليه"
            val sifastr = "صفة"
            val mudhaflenght = mudhafilahistr[0].length
            val sifalength = sifastr.length
            val hal = ArrayList<String>()
            fael.add("وفاعله")
            fael.add("فاعله")
            fael.add("والواو فاعله")
            fael.add("فاعله مستتر")
            fael.add("الفاعل مستتر")
            fael.add("فاعل")
            fael.add("فاعل.")
            fael.add("والفاعل")
            fael.add("الفاعل")
            fael.add("والفاعل مستتر")
            hal.add("في محل نصب حال")
            hal.add("في محل نصب حال.")
            hal.add("والجملة حالية")
            hal.add("والجملة حالية.")
            hal.add("حالية")
            hal.add("حالية.")
            hal.add("حالية:")
            hal.add("حال")
            hal.add("حال:")
            hal.add("حال.")
            hal.add("الواو حالية")
            val tameez = ArrayList<String>()
            tameez.add("تمييز")
            tameez.add("تمييز.")
            tameez.add("التمييز")
            val badal = ArrayList<String>()
            badal.add("بدل")
            badal.add("بدل.")
            val ajilihi = ArrayList<String>()
            ajilihi.add("مفعول لأجله")
            ajilihi.add("لأجله")
            ajilihi.add("لأجله.")
            val mafoolbihi = ArrayList<String>()
            mafoolbihi.add("مفعول به")
            mafoolbihi.add("مفعول به.")
            mafoolbihi.add("مفعول به.(")
            mafoolbihi.add("في محل نصب مفعول")
            mafoolbihi.add("مفعول")
            for (pojo in allofQuran!!) {
                //  String ar_irab_two = pojo.getAr_irab_two();
                val ar_irab_two = pojo.ar_irab_two.replace("\n", "");
                val str = SpannableStringBuilder(ar_irab_two)
                val mudhaftrie = Trie.builder().onlyWholeWords().addKeywords(mudhafilahistr).build()
                val mudhafemit = mudhaftrie.parseText(ar_irab_two)
                val sifatrie = Trie.builder().onlyWholeWords().addKeywords(sifastr).build()
                val sifaemit = sifatrie.parseText(ar_irab_two)
                val jawabsharttwotrie =
                    Trie.builder().onlyWholeWords().addKeywords(jawabsharttwostr).build()
                val jawabsharttwoemit = jawabsharttwotrie.parseText(ar_irab_two)
                val trieBuilder =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(shart).build()
                val emits = trieBuilder.parseText(ar_irab_two)
                val mutlaqbuilder =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(mutlaq).build()
                val mutlaqemits = mutlaqbuilder.parseText(ar_irab_two)
                val haltrie =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(hal).build()
                val halemits = haltrie.parseText(ar_irab_two)
                val tameeztrie =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(tameez).build()
                val tameezemit = tameeztrie.parseText(ar_irab_two)
                val badaltrie =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(badal).build()
                val badalemit = badaltrie.parseText(ar_irab_two)
                val ajilihitrie =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(ajilihi).build()
                val ajilihiemit = ajilihitrie.parseText(ar_irab_two)
                val mafoolbihitri =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(mafoolbihi)
                        .build()
                val mafoolbihiemit = mafoolbihitri.parseText(ar_irab_two)
                val faeltri =
                    Trie.builder().onlyWholeWordsWhiteSpaceSeparated().addKeywords(fael)
                        .build()
                val faelemit = faeltri.parseText(ar_irab_two)

                for (emit in faelemit) {
                    str.setSpan(
                        ForegroundColorSpan(sifatColoragainstBlack),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                for (emit in mafoolbihiemit) {
                    str.setSpan(
                        ForegroundColorSpan(sifatColoragainstBlack),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in ajilihiemit) {
                    str.setSpan(
                        ForegroundColorSpan(sifatColoragainstBlack),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in tameezemit) {
                    str.setSpan(
                        ForegroundColorSpan(sifatColoragainstBlack),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in badalemit) {
                    str.setSpan(
                        ForegroundColorSpan(sifatColoragainstBlack),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in halemits) {
                    str.setSpan(
                        ForegroundColorSpan(shartagainstback),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in emits) {
                    str.setSpan(
                        ForegroundColorSpan(shartagainstback),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in mutlaqemits) {
                    str.setSpan(
                        ForegroundColorSpan(shartagainstback),
                        emit.start,
                        emit.start + emit.keyword.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in mudhafemit) {
                    str.setSpan(
                        ForegroundColorSpan(mausofColoragainstBlack),
                        emit.start,
                        emit.start + mudhaflenght,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                for (emit in sifaemit) {
                    str.setSpan(
                        ForegroundColorSpan(mudhafColoragainstBlack),
                        emit.start,
                        emit.start + sifalength,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                //    colorerab.get(0).setErabspnabble(str);

                pojo.erabspnabble = str
            }
        }
        ////
        @JvmStatic
        fun NewSetWordSpan(
            tagone: String?,
            tagtwo: String?,
            tagthree: String?,
            tagfour: String?,
            tagfive: String?,
            araone: String,
            aratwo: String,
            arathree: String,
            arafour: String,
            arafive: String,
        ): SpannableString {
            var araone = araone
            var aratwo = aratwo
            var arathree = arathree
            var arafour = arafour
            var arafive = arafive
            var str: SpannableString
            val istagnull =
                tagone == null || tagtwo == null || tagthree == null || tagfour == null || tagfive == null
            if (istagnull) {
                return SpannableString(araone).also { str = it }
            }
            var tagcounter = 0
            val b = tagone!!.isNotEmpty()
            val bb = tagtwo!!.isNotEmpty()
            val bbb = tagthree!!.isNotEmpty()
            val bbbb = tagfour!!.isNotEmpty()
            val bbbbb = tagfive!!.isNotEmpty()
            if (b && !bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 1
            } else if (b && bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 2
            } else if (b && bb && bbb && !bbbb && !bbbbb) {
                tagcounter = 3
            } else if (b && bb && bbb && bbbb && !bbbbb) {
                tagcounter = 4
            } else if (b && bb && bbb && bbbb) {
                tagcounter = 5
            }
            araone = araone.trim { it <= ' ' }
            aratwo = aratwo.trim { it <= ' ' }
            arathree = arathree.trim { it <= ' ' }
            arafour = arafour.trim { it <= ' ' }
            arafive = arafive.trim { it <= ' ' }
            //   SharedPreferences sharedPreferences =
            //      androidx.preference.PreferenceManager.getDefaultSharedPreferences(DarkThemeApplication.context!!);
            //   String isNightmode = sharedPreferences.getString("themepref", "dark" );
            //   if (isNightmode.equals("dark")||isNightmode.equals("blue")) {
            val spanhash: Map<String?, ForegroundColorSpan> = stringForegroundColorSpanMap
            //  }else{
            //   spanhash = getColorSpanforPhrasesLight();
            //  }
            if (tagcounter == 1) {
                str = SpannableString(araone.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else if (tagcounter == 2) {
                str = SpannableString(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else if (tagcounter == 3) {
                spanhash[tagone]
                str =
                    SpannableString(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagthree],
                    araone.length + aratwo.length,
                    araone.length + aratwo.length + arathree.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else if (tagcounter == 4) {
                str =
                    SpannableString(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' } + arafour.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagthree],
                    araone.length + aratwo.length,
                    araone.length + aratwo.length + arathree.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagfour],
                    araone.length + aratwo.length + arathree.length,
                    araone.length + aratwo.length + arathree.length + arafour.trim { it <= ' ' }.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                //    str.setSpan(attachedpronoun, araone.length() + aratwo.length() + arathree.length() + arafour.length(), araone.length() + aratwo.length() + arathree.length() + arafour.trim().length() + arafive.trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (tagcounter == 5) {
                str =
                    SpannableString(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' } + arafour.trim { it <= ' ' } + arafive.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagthree],
                    araone.length + aratwo.length,
                    araone.length + aratwo.length + arathree.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagfour],
                    araone.length + aratwo.length + arathree.length,
                    araone.length + aratwo.length + arathree.length + arafour.trim { it <= ' ' }.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagfive],
                    araone.length + aratwo.length + arathree.length + arafour.length,
                    araone.length + aratwo.length + arathree.length + arafour.trim { it <= ' ' }.length + arafive.trim { it <= ' ' }.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                str =
                    SpannableString(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' } + arafour.trim { it <= ' ' } + arafive.trim { it <= ' ' })
            }
            return str
        }

        @JvmStatic
        val stringForegroundColorSpanMap: Map<String?, ForegroundColorSpan>
            get() {
                val spanhash: MutableMap<String?, ForegroundColorSpan> = HashMap()
                if (dark) {
                    spanhash["PN"] = Constant.propernounspanDark
                    spanhash["REL"] = Constant.relativespanDark
                    spanhash["DEM"] = Constant.demonstrativespanDark
                    spanhash["N"] = Constant.nounspanDark
                    spanhash["PRON"] = Constant.pronounspanDark
                    spanhash["DET"] = Constant.determinerspanDark
                    spanhash["V"] = Constant.verbspanDark
                    spanhash["P"] = Constant.prepositionspanDark
                    spanhash["T"] = Constant.timeadverbspanDark
                    spanhash["LOC"] = Constant.locationadverspanDark
                    spanhash["ADJ"] = Constant.adjectivespanDark
                    spanhash["VN"] = Constant.verbalnounspanDark
                    spanhash["EMPH"] = Constant.emphspanDark // "Emphatic lām prefix(لام التوكيد) ",
                    spanhash["IMPV"] =
                        Constant.lamimpvspanDark // "Imperative lāmprefix(لام الامر)",
                    spanhash["PRP"] =
                        Constant.lamtaleelspandDark // "Purpose lāmprefix(لام التعليل)",
                    spanhash["SUB"] =
                        Constant.masdariaspanDark // "	Subordinating conjunction(حرف مصدري)",
                    spanhash["ACC"] = Constant.nasabspanDark // "	Accusative particle(حرف نصب)",
                    spanhash["ANS"] = Constant.answerspanDark // "	Answer particle	(حرف جواب)",
                    spanhash["CAUS"] =
                        Constant.harfsababiaspanDark // "Particle of cause	(حرف سببية)",
                    spanhash["CERT"] =
                        Constant.certainityspanDark // "Particle of certainty	(حرف تحقيق)",
                    spanhash["CIRC"] = Constant.halspanDark // "Circumstantial particle	(حرف حال)",
                    spanhash["CONJ"] =
                        Constant.particlespanDark // "Coordinating conjunction(حرف عطف)",
                    spanhash["COND"] =
                        Constant.harfshartspanDark // "Conditional particle(حرف شرط)",
                    spanhash["AMD"] =
                        Constant.particlespanDark // "	Amendment particle(حرف استدراك)	",
                    spanhash["AVR"] = Constant.particlespanDark // "	Aversion particle	(حرف ردع)",
                    spanhash["COM"] =
                        Constant.particlespanDark // "	Comitative particle	(واو المعية)",
                    spanhash["EQ"] =
                        Constant.particlespanDark // "	Equalization particle(حرف تسوية)",
                    spanhash["EXH"] =
                        Constant.particlespanDark // "	Exhortation particle(حرف تحضيض)",
                    spanhash["EXL"] =
                        Constant.particlespanDark // "	Explanation particle(حرف تفصيل)",
                    spanhash["EXP"] =
                        Constant.particlespanDark // "	Exceptive particle	(أداة استثناء)",
                    spanhash["FUT"] = Constant.particlespanDark // "	Future particle	(حرف استقبال)",
                    spanhash["INC"] =
                        Constant.particlespanDark // "	Inceptive particle	(حرف ابتداء)",
                    spanhash["INT"] =
                        Constant.particlespanDark // "	Particle of interpretation(حرف تفسير)",
                    spanhash["RET"] =
                        Constant.particlespanDark // "	Retraction particle	(حرف اضراب)",
                    spanhash["PREV"] = Constant.particlespanDark // "Preventive particle	(حرف كاف)",
                    spanhash["VOC"] = Constant.particlespanDark // "	Vocative particle	(حرف نداء)",
                    spanhash["INL"] =
                        Constant.particlespanDark // "	Quranic initials(	(حروف مقطعة	";
                    spanhash["INTG"] =
                        Constant.interrogativespanDark // "Interogative particle	(حرف استفهام)",
                    spanhash["NEG"] = Constant.negativespanDark // "	Negative particle(حرف نفي)",
                    spanhash["PRO"] =
                        Constant.prohibitionspanDark // "	Prohibition particle(حرف نهي)",
                    spanhash["REM"] =
                        Constant.resumtionspanDark // "	Resumption particle	(حرف استئنافية)",
                    spanhash["RES"] =
                        Constant.restrictivespanDark // "	Restriction particle(أداة حصر)",
                    spanhash["RSLT"] =
                        Constant.resultparticlespanDark // "Result particle(حرف واقع في جواب الشرط)",
                    spanhash["SUP"] =
                        Constant.supplementspoanDark // "	Supplemental particle	(حرف زائد)",
                    spanhash["SUR"] = Constant.surprisespanDark // "	Surprise particle	(حرف فجاءة)",
                } else {
                    spanhash["PN"] = Constant.propernounspanLight
                    spanhash["REL"] = Constant.relativespanLight
                    spanhash["DEM"] = Constant.demonstrativespanLight
                    spanhash["N"] = Constant.nounspanLight
                    spanhash["PRON"] = Constant.pronounspanLight
                    spanhash["DET"] = Constant.determinerspanLight
                    spanhash["V"] = Constant.verbspanLight
                    spanhash["P"] = Constant.prepositionspanLight
                    spanhash["T"] = Constant.timeadverbspanLight
                    spanhash["LOC"] = Constant.locationadverspanLight
                    spanhash["ADJ"] = Constant.adjectivespanLight
                    spanhash["VN"] = Constant.verbalnounspanLight
                    spanhash["EMPH"] =
                        Constant.emphspanLight // "Emphatic lām prefix(لام التوكيد) ",
                    spanhash["IMPV"] =
                        Constant.lamimpvspanLight // "Imperative lāmprefix(لام الامر)",
                    spanhash["PRP"] =
                        Constant.lamtaleelspandLight // "Purpose lāmprefix(لام التعليل)",
                    spanhash["SUB"] =
                        Constant.masdariaspanLight // "	Subordinating conjunction(حرف مصدري)",
                    spanhash["ACC"] = Constant.nasabspanLight // "	Accusative particle(حرف نصب)",
                    spanhash["ANS"] = Constant.answerspanLight // "	Answer particle	(حرف جواب)",
                    spanhash["CAUS"] =
                        Constant.harfsababiaspanLight // "Particle of cause	(حرف سببية)",
                    spanhash["CERT"] =
                        Constant.certainityspanLight // "Particle of certainty	(حرف تحقيق)",
                    spanhash["CIRC"] =
                        Constant.particlespanLight // "Circumstantial particle	(حرف حال)",
                    spanhash["CONJ"] =
                        Constant.particlespanLight // "Coordinating conjunction(حرف عطف)",
                    spanhash["COND"] = Constant.eqspanlight // "Conditional particle(حرف شرط)",
                    spanhash["AMD"] =
                        Constant.ammendedparticle // "	Amendment particle(حرف استدراك)	",
                    spanhash["AVR"] = Constant.particlespanLight // "	Aversion particle	(حرف ردع)",
                    spanhash["COM"] =
                        Constant.particlespanLight // "	Comitative particle	(واو المعية)",
                    spanhash["EQ"] =
                        Constant.particlespanLight // "	Equalization particle(حرف تسوية)",
                    spanhash["EXH"] =
                        Constant.particlespanLight // "	Exhortation particle(حرف تحضيض)",
                    spanhash["EXL"] =
                        Constant.particlespanLight // "	Explanation particle(حرف تفصيل)",
                    spanhash["EXP"] =
                        Constant.particlespanLight // "	Exceptive particle	(أداة استثناء)",
                    spanhash["FUT"] =
                        Constant.particlespanLight // "	Future particle	(حرف استقبال)",
                    spanhash["INC"] = Constant.nasabspanLight // "	Inceptive particle	(حرف ابتداء)",
                    spanhash["INT"] =
                        Constant.particlespanLight // "	Particle of interpretation(حرف تفسير)",
                    spanhash["RET"] =
                        Constant.particlespanLight // "	Retraction particle	(حرف اضراب)",
                    spanhash["PREV"] = Constant.inceptivepartile // "Preventive particle	(حرف كاف)",
                    spanhash["VOC"] = Constant.particlespanLight // "	Vocative particle	(حرف نداء)",
                    spanhash["INL"] =
                        Constant.particlespanLight // "	Quranic initials(	(حروف مقطعة	";
                    spanhash["INTG"] =
                        Constant.interrogativespanLight // "Interogative particle	(حرف استفهام)",
                    spanhash["NEG"] = Constant.negativespanLight // "	Negative particle(حرف نفي)",
                    spanhash["PRO"] =
                        Constant.prohibitionspanLight // "	Prohibition particle(حرف نهي)",
                    spanhash["REM"] =
                        Constant.particlespanLight // "	Resumption particle	(حرف استئنافية)",
                    spanhash["RES"] =
                        Constant.restrictivespanLight // "	Restriction particle(أداة حصر)",
                    spanhash["RSLT"] =
                        Constant.resultparticlespanLight // "Result particle(حرف واقع في جواب الشرط)",
                    spanhash["SUP"] =
                        Constant.supplementspanLight // "	Supplemental particle	(حرف زائد)",
                    spanhash["SUR"] =
                        Constant.surprisespanLight // "	Surprise particle	(حرف فجاءة)",
                }
                return spanhash
            }

        @JvmStatic
        fun getSpancolor(b: Boolean): BackgroundColorSpan {
            val sifaspansDark = Constant.sifaspansDark
            val mudhafspansDark = Constant.mudhafspansDark
            return if (b) {
                if (dark) {
                    Constant.mudhafspansDark = BackgroundColorSpan(Constant.MIDNIGHTBLUE)
                } else {
                    Constant.mudhafspansDark = BackgroundColorSpan(Constant.GREENYELLOW)
                }
                mudhafspansDark
            } else {
                if (dark) {
                    Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                } else {
                    Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                }
                sifaspansDark
            }
        }

        @JvmStatic
        fun getSpannableVerses(arabicword: String, quranverses: String): Spannable {
            val wordlen = arabicword.length
            val str: SpannableString
            val indexOf = quranverses.indexOf(arabicword)
            if (indexOf != -1) {
                str = SpannableString(quranverses)
                if (dark) {
                    str.setSpan(
                        ForegroundColorSpan(Color.CYAN),
                        indexOf,
                        indexOf + wordlen,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    str.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                QuranGrammarApplication.context!!,
                                R.color.midnightblue
                            )
                        ),
                        indexOf,
                        indexOf + wordlen,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                str = SpannableString(quranverses)
            }
            return str
        }

        @JvmStatic
        fun getSpannable(text: String): Spannable {
            val spannable: Spannable = SpannableString(text)
            val REGEX = "لل"
            val p = Pattern.compile(REGEX)
            val m = p.matcher(text)
            var start: Int
            var end: Int
            //region allah match
            while (m.find()) {
                start = m.start()
                while (text[start] != ' ' && start != 0) {
                    start--
                }
                end = m.end()
                while (text[end] != ' ') {
                    end++
                }
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            //endregion
            return spannable
        }

        fun PassageSetWordSpan(
            tagone: String?,
            tagtwo: String?,
            tagthree: String?,
            tagfour: String?,
            tagfive: String?,
            araone: String,
            aratwo: String,
            arathree: String,
            arafour: String,
            arafive: String,
        ): SpannableStringBuilder {
            var araone = araone
            var aratwo = aratwo
            var arathree = arathree
            var arafour = arafour
            var arafive = arafive
            var str: SpannableStringBuilder
            val istagnull =
                tagone == null || tagtwo == null || tagthree == null || tagfour == null || tagfive == null
            if (istagnull) {
                return SpannableStringBuilder(araone).also { str = it }
            }
            var tagcounter = 0
            val b = tagone!!.isNotEmpty()
            val bb = tagtwo!!.isNotEmpty()
            val bbb = tagthree!!.isNotEmpty()
            val bbbb = tagfour!!.isNotEmpty()
            val bbbbb = tagfive!!.isNotEmpty()
            if (b && !bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 1
            } else if (b && bb && !bbb && !bbbb && !bbbbb) {
                tagcounter = 2
            } else if (b && bb && bbb && !bbbb && !bbbbb) {
                tagcounter = 3
            } else if (b && bb && bbb && bbbb && !bbbbb) {
                tagcounter = 4
            } else if (b && bb && bbb && bbbb) {
                tagcounter = 5
            }
            araone = araone.trim { it <= ' ' }
            aratwo = aratwo.trim { it <= ' ' }
            arathree = arathree.trim { it <= ' ' }
            arafour = arafour.trim { it <= ' ' }
            arafive = arafive.trim { it <= ' ' }
            //   SharedPreferences sharedPreferences =
            //      androidx.preference.PreferenceManager.getDefaultSharedPreferences(DarkThemeApplication.context!!);
            //   String isNightmode = sharedPreferences.getString("themepref", "dark" );
            //   if (isNightmode.equals("dark")||isNightmode.equals("blue")) {
            val spanhash: Map<String?, ForegroundColorSpan> = stringForegroundColorSpanMap
            //  }else{
            //   spanhash = getColorSpanforPhrasesLight();
            //  }
            if (tagcounter == 1) {
                str = SpannableStringBuilder(araone.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else if (tagcounter == 2) {
                val stro = SpannableStringBuilder(araone.trim { it <= ' ' })
                val strt = SpannableStringBuilder(aratwo.trim { it <= ' ' })
                stro.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                strt.setSpan(spanhash[tagtwo], 0, aratwo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                val concat = TextUtils.concat(stro, strt)
                str = concat as SpannableStringBuilder
            } else if (tagcounter == 3) {
                spanhash[tagone]
                str =
                    SpannableStringBuilder(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagthree],
                    araone.length + aratwo.length,
                    araone.length + aratwo.length + arathree.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else if (tagcounter == 4) {
                str =
                    SpannableStringBuilder(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' } + arafour.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagthree],
                    araone.length + aratwo.length,
                    araone.length + aratwo.length + arathree.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagfour],
                    araone.length + aratwo.length + arathree.length,
                    araone.length + aratwo.length + arathree.length + arafour.trim { it <= ' ' }.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                //    str.setSpan(attachedpronoun, araone.length() + aratwo.length() + arathree.length() + arafour.length(), araone.length() + aratwo.length() + arathree.length() + arafour.trim().length() + arafive.trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (tagcounter == 5) {
                str =
                    SpannableStringBuilder(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' } + arafour.trim { it <= ' ' } + arafive.trim { it <= ' ' })
                str.setSpan(spanhash[tagone], 0, araone.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.setSpan(
                    spanhash[tagtwo],
                    araone.length,
                    araone.length + aratwo.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagthree],
                    araone.length + aratwo.length,
                    araone.length + aratwo.length + arathree.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagfour],
                    araone.length + aratwo.length + arathree.length,
                    araone.length + aratwo.length + arathree.length + arafour.trim { it <= ' ' }.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                str.setSpan(
                    spanhash[tagfive],
                    araone.length + aratwo.length + arathree.length + arafour.length,
                    araone.length + aratwo.length + arathree.length + arafour.trim { it <= ' ' }.length + arafive.trim { it <= ' ' }.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                str =
                    SpannableStringBuilder(araone.trim { it <= ' ' } + aratwo.trim { it <= ' ' } + arathree.trim { it <= ' ' } + arafour.trim { it <= ' ' } + arafive.trim { it <= ' ' })
            }
            return str
        }

        fun newcomposeWBWCollectiondd(
            quranandCorpusandWbwbySurah: List<QuranEntityCorpusEntityWbwEntity>?,
            size: Int
        ): LinkedHashMap<Int, ArrayList<NewNewQuranCorpusWbw>> {

            val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewNewQuranCorpusWbw>>()
            if (quranandCorpusandWbwbySurah == null) return newnewadapterlist
            for (i in 1..size) {
                for (i in quranandCorpusandWbwbySurah.indices) {
                    val qurancorpusarray = ArrayList<NewNewQuranCorpusWbw>()
                    val item = quranandCorpusandWbwbySurah[i]
                    val ayahWord = NewNewQuranCorpusWbw()
                    ayahWord.spannableverse = SpannableString.valueOf(item.qurantext)
                    ayahWord.corpus = item // Directly assign the item as it contains all the data
                    qurancorpusarray.add(ayahWord)

                    if (qurancorpusarray.isNotEmpty()) {
                        newnewadapterlist[item.ayah] = qurancorpusarray // Use ayah as key
                    }
                }
            }
            return newnewadapterlist
        }


        fun newcomposeWBWCollection(
            quranandCorpusandWbwbySurah: List<QuranEntityCorpusEntityWbwEntity>?,
            size: Int
        ): LinkedHashMap<Int, ArrayList<NewNewQuranCorpusWbw>> {

            val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewNewQuranCorpusWbw>>()
            if (quranandCorpusandWbwbySurah == null) return newnewadapterlist

            for (i in 1..size) {
                val qurancorpusarray = ArrayList<NewNewQuranCorpusWbw>()
                for (j in quranandCorpusandWbwbySurah.indices) {

                    val item = quranandCorpusandWbwbySurah[i]
                    if (quranandCorpusandWbwbySurah[j].ayah == i) {
                        val ayahWord = NewNewQuranCorpusWbw()
                        ayahWord.spannableverse =
                            SpannableString.valueOf(quranandCorpusandWbwbySurah[i].qurantext)
                        ayahWord.corpus = QuranEntityCorpusEntityWbwEntity(
                            item.surah,
                            item.ayah,
                            item.wordno,
                            item.wordcount,
                            item.qurantext,
                            item.translation,
                            item.en_arberry,
                            item.ar_irab_two,
                            item.araone,
                            item.aratwo,
                            item.arathree,
                            item.arafour,
                            item.arafive,
                            item.tagone,
                            item.tagtwo,
                            item.tagthree,
                            item.tagfour,
                            item.tagfive,
                            item.detailsone,
                            item.detailstwo,
                            item.detailsthree,
                            item.detailsfour,
                            item.detailsfive,
                            item.en,
                            item.bn,
                            item.`in`,
                            item.rootaraone,
                            item.rootaratwo,
                            item.rootarathree,
                            item.rootarafour,
                            item.rootarafive
                        )
                        qurancorpusarray.add(ayahWord)
                    } else if (quranandCorpusandWbwbySurah[j].ayah > i) {
                        break
                    }
                }
                if (qurancorpusarray.isNotEmpty()) {
                    newnewadapterlist[i] = qurancorpusarray
                }


            }

            return newnewadapterlist
        }

        fun composeWBWCollection(
            allofQuran: List<QuranEntity>?,
            corpusSurahWord: List<QuranCorpusWbw>?
        ): LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>> {

            val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>()
            if (allofQuran == null || corpusSurahWord == null) return newnewadapterlist

            // Group corpusSurahWord by ayah to avoid redundant looping
            val corpusGroupedByAyah = corpusSurahWord.groupBy { it.corpus.ayah }

            for (quranEntity in allofQuran) {
                val qurancorpusarray = ArrayList<NewQuranCorpusWbw>()
                val matchingCorpusList = corpusGroupedByAyah[quranEntity.ayah]

                if (matchingCorpusList != null) {
                    for (corpusWbw in matchingCorpusList) {
                        val ayahWord = NewQuranCorpusWbw()
                        ayahWord.spannableverse = SpannableString.valueOf(quranEntity.qurantext)
                        ayahWord.wbw = corpusWbw.wbw
                        ayahWord.corpus = corpusWbw.corpus
                        qurancorpusarray.add(ayahWord)
                    }
                    newnewadapterlist[quranEntity.ayah] = qurancorpusarray
                }
            }

            return newnewadapterlist
        }

        fun NotcomposeWBWCollection(
            allofQuran: List<QuranEntity>?,
            corpusSurahWord: List<QuranCorpusWbw>?
        ): LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>> {

            val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>()
            if (allofQuran == null || corpusSurahWord == null) return newnewadapterlist

            // Group corpusSurahWord by ayah to avoid redundant looping
            val corpusGroupedByAyah = corpusSurahWord.groupBy { it.corpus.ayah }

            for (quranEntity in allofQuran) {
                val qurancorpusarray = ArrayList<NewQuranCorpusWbw>()
                val matchingCorpusList = corpusGroupedByAyah[quranEntity.ayah]

                if (matchingCorpusList != null) {
                    for (corpusWbw in matchingCorpusList) {
                        val ayahWord = NewQuranCorpusWbw()
                        ayahWord.spannableverse = SpannableString.valueOf(quranEntity.qurantext)
                        ayahWord.wbw = corpusWbw.wbw
                        ayahWord.corpus = corpusWbw.corpus
                        qurancorpusarray.add(ayahWord)
                    }
                    newnewadapterlist[quranEntity.ayah] = qurancorpusarray
                }
            }

            return newnewadapterlist
        }

        fun composeWBWCollectionold(
            allofQuran: List<QuranEntity>?,
            corpusSurahWord: List<QuranCorpusWbw>?
        ): LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>> {

            val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>()
            if (allofQuran == null || corpusSurahWord == null) return newnewadapterlist

            for (i in allofQuran.indices) {
                val qurancorpusarray = ArrayList<NewQuranCorpusWbw>()
                for (j in corpusSurahWord.indices) {
                    if (corpusSurahWord[j].corpus.ayah == allofQuran[i].ayah) {
                        val ayahWord = NewQuranCorpusWbw()
                        ayahWord.spannableverse = SpannableString.valueOf(allofQuran[i].qurantext)
                        ayahWord.wbw = corpusSurahWord[j].wbw
                        ayahWord.corpus = corpusSurahWord[j].corpus
                        qurancorpusarray.add(ayahWord)
                    } else if (corpusSurahWord[j].corpus.ayah > allofQuran[i].ayah) {
                        break
                    }
                }
                if (qurancorpusarray.isNotEmpty()) {
                    newnewadapterlist[i] = qurancorpusarray
                }
            }

            return newnewadapterlist
        }

        fun composeWBWCollectionorig(
            allofQuran: List<QuranEntity>?,
            corpusSurahWord: List<QuranCorpusWbw>?
        ): LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>> {

            val newnewadapterlist = LinkedHashMap<Int, ArrayList<NewQuranCorpusWbw>>()
            var qurancorpusarray = ArrayList<NewQuranCorpusWbw>()


            var aindex = 0
            var secondindex = 0

            while (aindex <= allofQuran!!.size) {

                var ayahWord = NewQuranCorpusWbw()

                try {
                    while (corpusSurahWord!![secondindex].corpus.ayah <= allofQuran[aindex].ayah) {
                        if (corpusSurahWord[secondindex].corpus.ayah != allofQuran[aindex].ayah) {
                            break
                        }

                        ayahWord.spannableverse =
                            SpannableString.valueOf(allofQuran[aindex].qurantext)
                        ayahWord.wbw = corpusSurahWord[secondindex].wbw
                        ayahWord.corpus = corpusSurahWord[secondindex++].corpus
                        qurancorpusarray.add(ayahWord)

                        ayahWord = NewQuranCorpusWbw()
                    }
                } catch (e: IndexOutOfBoundsException) {
                    println(e.message)
                }

                if (qurancorpusarray.isNotEmpty()) {
                    newnewadapterlist[aindex] = qurancorpusarray
                    val ayahWord = NewQuranCorpusWbw()
                }
                qurancorpusarray = ArrayList()
                aindex++
            }

            return newnewadapterlist

        }

        fun setAyahGrammaticalPhrases(
            spannableverse: SpannableString?,
            surah: Int,
            ayah: Int,

        ) {

            if (spannableverse != null) {

                setMudhafFromDBforAyah(spannableverse, surah, ayah)
                setMausoofforayah(spannableverse, surah, ayah)
                //  setPastNegation(spannableverse, surah,ayah)
                // setPresentNegation(spannableverse, surah,ayah)
                //  setFutureNegation(spannableverse, surah,ayah)


            }


        }






        fun setPastNegation(
            spannableverse: SpannableString,
            surah_id: Int,
            ayah_id: Int
        ) {
            val utils = Utils(QuranGrammarApplication.context!!)
            val surah = utils.getNegationFilterSurahAyahType(surah_id, ayah_id, "past")
//todo 2 188 iza ahudu
            //todo 9;92 UNCERTAIN
            //TODO 9:94 JAWABHARMAHDOOF 9 95 JAWABHSARMAHODFF
            //TO 9;118 IZA IN THE MEANING OF HEENA AND 9 122 IZA AS HEENA
            val negationForegroundColorSpan = if (dark) Constant.GOLD else Constant.FORESTGREEN
            for (mudhafen in surah!!) {
                val indexstart = mudhafen!!.startindex
                val indexend = mudhafen.endindex
                try {

                    try {
                        if (indexstart == 0 || indexstart > 0) {


                            spannableverse.setSpan(
                                negationForegroundColorSpan,
                                indexstart,
                                indexend,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        //System.out.println(e.getMessage());
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //System.out.println(e.getMessage());
                }
            }
        }

        fun setFutureNegation(
            spannableverse: SpannableString,
            surah_id: Int,
            ayah_id: Int
        ) {
            val utils = Utils(QuranGrammarApplication.context!!)
            val surah = utils.getNegationFilterSurahAyahType(surah_id, ayah_id, "future")
//todo 2 188 iza ahudu
            //todo 9;92 UNCERTAIN
            //TODO 9:94 JAWABHARMAHDOOF 9 95 JAWABHSARMAHODFF
            //TO 9;118 IZA IN THE MEANING OF HEENA AND 9 122 IZA AS HEENA
            val negationForegroundColorSpan = if (dark) Constant.GOLD else Constant.FORESTGREEN
            for (mudhafen in surah!!) {
                val indexstart = mudhafen!!.startindex
                val indexend = mudhafen.endindex
                try {

                    try {
                        if (indexstart == 0 || indexstart > 0) {


                            spannableverse.setSpan(
                                negationForegroundColorSpan,
                                indexstart,
                                indexend,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        //System.out.println(e.getMessage());
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //System.out.println(e.getMessage());
                }
            }
        }


        fun setMudhafFromDBforAyah(
            spannableverse: SpannableString,
            surah_id: Int,
            ayah_id: Int
        ) {
            val utils = Utils(QuranGrammarApplication.context!!)
            val surah = utils.getFilterSurahAyahType(surah_id, ayah_id,"mudhaf")
//todo 2 188 iza ahudu
            //todo 9;92 UNCERTAIN
            //TODO 9:94 JAWABHARMAHDOOF 9 95 JAWABHSARMAHODFF
            //TO 9;118 IZA IN THE MEANING OF HEENA AND 9 122 IZA AS HEENA
            for (mudhafen in surah!!) {
                val indexstart = mudhafen!!.startindex
                val indexend = mudhafen.endindex
                try {

                    try {
                        if (indexstart == 0 || indexstart > 0) {
                            if (dark) {
                                Constant.mudhafspansDark =
                                    BackgroundColorSpan(Constant.MIDNIGHTBLUE)
                            } else {
                                Constant.mudhafspansDark = BackgroundColorSpan(Constant.GREENYELLOW)
                            }

                            spannableverse.setSpan(
                                Constant.mudhafspansDark,
                                indexstart,
                                indexend,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        //System.out.println(e.getMessage());
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //System.out.println(e.getMessage());
                }
            }
        }

        private fun setMausoofforayah(spannableverse: SpannableString, surah: Int, ayah: Int) {

            val utils = Utils(QuranGrammarApplication.context!!)
            val surah =  utils.getFilterSurahAyahType(surah, ayah,"sifa")

            for (sifaEntity in surah!!) {
                val indexstart = sifaEntity!!.startindex
                val indexend = sifaEntity.endindex
                //  sifaspans = new BackgroundColorSpan(WBURNTUMBER);

                try {
                    if (indexstart == 0 || indexstart > 0) {
                        //   sifaspansDark = getSpancolor(preferences, false);
                        if (dark) {
                            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                        } else {
                            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                        }
                        spannableverse.setSpan(
                            Constant.sifaspansDark,
                            indexstart,
                            indexend,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //System.out.println(e.getMessage());
                }
            }


        }

        private fun SifaSpansSetupbysurah(
            spannableverse: SpannableString,

            indexstart: Int,
            indexend: Int,
        ) {

            try {

                //spannableString = SpannableString.valueOf(corpusayahWordArrayList.get(sifaEntity.getAyah() - 1).getSpannableverse());
                try {
                    if (indexstart == 0 || indexstart > 0) {
                        //   sifaspansDark = getSpancolor(preferences, false);
                        if (dark) {
                            Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                        } else {
                            Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                        }
                        spannableverse.setSpan(
                            Constant.sifaspansDark,
                            indexstart,
                            indexend,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        //   spannableverse.setSpan(new UnderlineSpan(),indexstart, indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //System.out.println(e.getMessage());
                }
            } catch (e: IndexOutOfBoundsException) {
                //System.out.println(e.getMessage());
            }
        }


        fun setAbsoluteNegationlatest(
            corpus: java.util.ArrayList<CorpusEntity>?,
            spannableVerse: SpannableString
        ) {
            var validNegFound = false
            var negOccurrences =
                mutableListOf<Pair<Int, Int>>()  // Stores wordno and index of each "NEG" occurrence

            // Loop through the corpus to find all occurrences of "NEG"
            for (i in corpus?.indices!!) {
                val word = corpus[i]

                // Check if the word has a "NEG" tag
                if (word.tagone == "NEG") {
                    val negWordNo = word.wordno
                    val negIndex = spannableVerse.indexOf(word.araone!!)
                    negOccurrences.add(Pair(negWordNo, negIndex))
                }
            }

            // Now process each occurrence of "NEG" and apply negation rule if valid
            for (i in corpus.indices) {
                val word = corpus[i]

                // Check for the scenario: Noun (ACC), preceding valid NEG, following P + PRON
                val isAcc = word?.detailsone?.contains("ACC")
                if (isAcc == true &&
                    (i > 0 && corpus[i - 1].tagone == "NEG") && // Preceding NEG tag
                    (i < corpus.size - 2) && // Ensure valid following words
                    (corpus[i + 1].tagone == "P") && // Following P tag
                    (corpus[i + 1].tagtwo == "PRON") // Following PRON tag
                ) {
                    // Get the valid NEG occurrence (assuming the last one should be used if there are multiple)
                    val validNegIndex =
                        negOccurrences.lastOrNull { it.first == corpus[i - 1].wordno }?.second

                    // If valid NEG index is found, continue
                    if (validNegIndex != null && !validNegFound) {
                        validNegFound = true // Mark the valid NEG found

                        // Get the start and end index of the NEG word in the verse
                        val negStartIndex = validNegIndex
                        val negEndIndex = negStartIndex + corpus[i - 1].araone!!.length

                        // Get the prepositional word and pronoun word from the corpus
                        val pWord = corpus[i + 1].araone ?: ""
                        val pronWord = corpus[i + 1].aratwo ?: ""

                        // Find the indices for the prepositional phrase
                        val phraseStartIndex = spannableVerse.indexOf(pWord, negEndIndex)
                        val phraseEndIndex = phraseStartIndex + pWord.length + pronWord.length

                        // Apply underline span for the absolute negation phrase
                        spannableVerse.setSpan(
                            UnderlineSpan(),
                            negStartIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }

        fun setAbsoluteNegation(
            corpus: java.util.ArrayList<CorpusEntity>?,
            spannableVerse: SpannableString
        ) {
            for (i in corpus?.indices!!) {
                val word = corpus[i]
                if (corpus[0].ayah == 256) {
                    println("check")
                }
                val targetWords = listOf("لَآ", "لَا", "لَّا")
                val occurrences = findWordOccurrencesArabic(spannableVerse.toString(), targetWords)
                // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON
                val isAcc = word?.detailsone?.contains("ACC")
                if (isAcc!! && // Current word is ACC
                    i > 0 && corpus[i - 1].tagone == "NEG" && // Preceding word is NEG
                    corpus[i].tagone == "N" && // Current word is N
                    i < corpus.size - 2 && // Ensure enough following words
                    ((corpus[i + 1].tagone == "P") && (corpus[i + 1].tagtwo!!.contains("PRON")) || (corpus[i + 1].tagone == "P")) // Following word is P with or without PRON
                ) {


                    var startIndex = 0
                    // Find the start index of the NEG word

                    /*    for ((wordNo, index) in occurrences) {
                            if(corpus[i-1].wordno == wordNo) {

                                startIndex = index
                                break
                            }

                        } */

                    startIndex =
                        occurrences.firstOrNull { (wordNo, _) -> corpus[i - 1].wordno == wordNo }?.second
                            ?: -1
                    // Find the start index of the NEG word
                    var prepositionalPhrase = ""
                    if (corpus[i + 1].tagone == "P" && (corpus[i + 1].tagtwo!!.contains("PRON"))) {
                        prepositionalPhrase = corpus[i + 1].araone!! + corpus[i + 1].aratwo!!
                    } else if (corpus[i + 1].tagone == "P") {
                        prepositionalPhrase = corpus[i + 1].araone!!
                    }

                    // Find the indices for the prepositional phrase
                    val phraseStartIndex = spannableVerse.indexOf(prepositionalPhrase, startIndex)
                    val phraseEndIndex = phraseStartIndex + prepositionalPhrase.length
                    val fileu = FileUtility(QuranGrammarApplication.context!!)
                    val chapterno = corpus[0].surah
                    //  val fileName = "surah$chapterno.csv"
                    //  fileu.writetofile(fileName,corpus[0].surah,corpus[0].ayah,corpus[0].wordno,startIndex,phraseEndIndex)
                    // Apply underline span for the absolute negation phrase
                    if (startIndex != -1 && phraseStartIndex != -1) {
                        spannableVerse.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            phraseEndIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }

        fun findWordOccurrencesArabic(
            text: String,
            targetWords: List<String>
        ): List<Pair<Int, Int>> {
            val occurrences = mutableListOf<Pair<Int, Int>>()
            val words = text.split(Regex("\\s+")) // Split the text into words
            var wordIndex = 0
            var wordNo = 1

            for (word in words) {
                if (targetWords.any {
                        it.equals(
                            word,
                            ignoreCase = true
                        )
                    }) { // Check against all flavors
                    occurrences.add(Pair(wordNo, wordIndex))
                }
                wordIndex += word.length + 1 // Add 1 for the space
                wordNo++
            }

            return occurrences
        }

        fun setAbsoluteNegationthree(
            corpus: java.util.ArrayList<CorpusEntity>?,

            spannableverse: SpannableString
        ) {
            // Split the Quran text into individual words
            val quranWords = spannableverse.split(" ")

            // Iterate through the corpus
            for (i in corpus?.indices!!) {
                val word = corpus[i]

                // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON
                val isAcc = word?.detailsone?.contains("ACC")
                if (isAcc == true
                    && i > 0 && corpus[i - 1].tagone == "NEG" // Preceding tag is NEG
                    && i < (corpus.size - 2) // Ensure enough following words
                    && corpus[i + 1].tagone == "P" // Following word has tag P
                    && corpus[i + 1].tagtwo == "PRON" // Following word has tag PRON
                ) {
                    // Find start index using wordno of "NEG"
                    val negWordStartIndex = findWordIndexByWordNo(quranWords, corpus[i - 1])

                    // Find end index using wordno of the last part of the pattern (PRON word)
                    val pronWordEndIndex = findWordIndexByWordNo(quranWords, corpus[i + 1])
                    // + (corpus[i + 1].araone?.length ?: 0) +(corpus[i + 1].aratwo?.length ?: 0)
                    println(spannableverse.subSequence(negWordStartIndex, pronWordEndIndex))
                    // Apply span (underline) to the sentence between start and end index
                    spannableverse.setSpan(
                        UnderlineSpan(),
                        negWordStartIndex,
                        pronWordEndIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        // Helper function to find the index of a Quran word in the text using wordno
        fun findWordIndexByWordNo(
            quranWords: List<String>,
            corpusWord: CorpusEntity
        ): Int {
            var currentIndex = 0
            for (i in quranWords.indices) {
                // Match based on the wordno in CorpusEntity
                if (i == corpusWord.wordno) {
                    // Get the index of the word in the entire Quran text
                    println(quranWords.take(i).joinToString(" "))
                    currentIndex = quranWords.take(i).joinToString(" ").length
                    break
                }
            }
            return currentIndex
        }


        fun twosetAbsoluteNegation(
            corpus: java.util.ArrayList<CorpusEntity>?,
            // Assuming this has the verse text
            spannableverse: SpannableString
        ) {
            // Split the Quran text into individual words
            val quranWords = spannableverse.split(" ")

            // Iterate through the corpus
            for (i in corpus?.indices!!) {
                val word = corpus[i]

                // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON
                val isAcc = word?.detailsone?.contains("ACC")
                if (isAcc == true
                    && i > 0 && corpus[i - 1].tagone == "NEG" // Preceding tag is NEG
                    && i < (corpus.size - 2) // Ensure enough following words
                    && corpus[i + 1].tagone == "P" // Following word has tag P
                    && corpus[i + 1].tagtwo == "PRON" // Following word has tag PRON
                ) {
                    // Find the start index by matching the Quran word with the Corpus word (NEG)
                    val negWordIndex = twofindWordIndexByWordNo(quranWords, corpus[i - 1])
                    val oldnegWordIndex = twofindWordIndexByWordNo(quranWords, corpus[i - 1])
                    // Calculate end index by matching Quran words for P and PRON
                    val pWordLength = corpus[i + 1].araone?.length ?: 0
                    val pronWordLength = corpus[i + 1].aratwo?.length ?: 0
                    val phraseEndIndex = negWordIndex + pWordLength + pronWordLength

                    // Apply span (underline) to the sentence between start and end index
                    spannableverse.setSpan(
                        UnderlineSpan(),
                        negWordIndex,
                        phraseEndIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        // Helper function to find the index of a Quran word in the text using wordno
        fun twofindWordIndexByWordNo(
            quranWords: List<String>,
            corpusWord: CorpusEntity
        ): Int {
            var currentIndex = 0
            for (i in quranWords.indices) {
                // Match based on the wordno in CorpusEntity
                if (i == corpusWord.wordno) {
                    // Get the index of the word in the entire Quran text
                    currentIndex = quranWords.take(i).joinToString(" ").length
                    break
                }
            }
            return currentIndex
        }


        fun setAbsoluteNegationtwo(
            corpus: java.util.ArrayList<CorpusEntity>?,
            spannableverse: SpannableString
        ) {
            var startIndex = 0
            var currentSentence = spannableverse.toString()

            // Iterate through the corpus list
            for (i in corpus?.indices!!) {
                val word = corpus[i]

                // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON
                val isAcc = word?.detailsone?.contains("ACC")
                if (isAcc == true
                    && i > 0 && corpus[i - 1].tagone == "NEG" // Check for preceding NEG tag
                    && i < (corpus.size - 2) // Ensure there are enough following words
                    && corpus[i + 1].tagone == "P" // Following word has tag P (preposition)
                    && corpus[i + 1].tagtwo == "PRON" // Following word has tag PRON (pronoun)
                ) {
                    // Get the index of the NEG tag before the ACC noun
                    startIndex = findNegTagIndex(currentSentence, corpus, i - 1)

                    // Calculate the length of the following phrase (P + PRON)
                    val firstCharLength = corpus[i + 1].araone?.length ?: 0
                    val secondCharLength = corpus[i + 1].aratwo?.length ?: 0
                    val nounLength = corpus[i].araone?.length ?: 0


                    var endIndex =
                        currentSentence.indexOf(corpus[i + 1].araone + corpus[i + 1].aratwo!!)
                    endIndex += firstCharLength + secondCharLength
                    // Apply span (underline) to the sentence between startIndex and endIndex
                    spannableverse.setSpan(
                        UnderlineSpan(),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        // Helper function to find the correct index of the NEG tag in the sentence
        fun findNegTagIndex(
            sentence: String,
            corpus: ArrayList<CorpusEntity>,
            negIndex: Int
        ): Int {
            var startIndex = 0
            var currentIndex = 0

            // Iterate through the sentence to find the correct NEG tag index
            for (i in 0..negIndex) {
                val negWord = corpus[i].araone
                currentIndex = sentence.indexOf(negWord ?: "", currentIndex)

                // If the current word is the NEG tag, return its index
                if (i == negIndex) {
                    startIndex = currentIndex
                    break
                }
            }
            return startIndex
        }


        fun setAbsoluteNegations(
            corpus: java.util.ArrayList<CorpusEntity>?,
            spannableverse: SpannableString
        ) {
            var startIndex = 0
            var currentSentence = ""
            for (i in corpus?.indices!!) {
                val word = corpus.get(i)


                // Check for the scenario: Noun (ACC), preceding NEG, following P + PRON
                currentSentence = spannableverse.toString()
                val isAcc = word?.detailsone?.contains("ACC")
                if ((isAcc == true)
                    && // Noun is ACC
                    (i > 0) && (corpus[i - 1].tagone == "NEG") && // Preceding tag is NEG
                    (i < (corpus.size - 2)) && // Check for enough following words
                    (corpus[i + 1].tagone == "P") && // Following tag is P
                    (corpus[i + 1].tagtwo == "PRON") // Following tag is PRON
                ) {
                    startIndex = currentSentence.indexOf(corpus[i - 1].araone!!)
                    val firstChar = corpus[i + 1].araone?.length
                    val secondCHar = corpus[i + 1].aratwo?.length!!
                    val endIndex =
                        currentSentence.indexOf(corpus[i + 1].araone + corpus[i + 1].aratwo!!)

                    spannableverse.setSpan(
                        UnderlineSpan(),
                        startIndex,
                        endIndex + firstChar!! + secondCHar!!,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );


                }
            }
        }

        fun setPresentFromCache(
            spannableverse: SpannableString,
            presentIndexList: MutableList<List<Int>>?
        ) {
            if (presentIndexList != null) {
                for (indexes in presentIndexList) {
                    val startIndex = indexes[0]
                    val endIndex = indexes[1]
                    if (dark) {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                    } else {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                    }

                    if (spannableverse != null) {
                        spannableverse.setSpan(
                            Constant.sifaspansDark,
                            startIndex,
                            endIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }

        fun setMausoofSifaFromCache(
            spannableverse: SpannableString,
            sifaIndexList: MutableList<List<Int>>?
        ) {
            if (sifaIndexList != null) {
                for (indexes in sifaIndexList) {
                    val startIndex = indexes[0]
                    val endIndex = indexes[1]
                    if (dark) {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.WBURNTUMBER)
                    } else {
                        Constant.sifaspansDark = BackgroundColorSpan(Constant.CYANLIGHTEST)
                    }

                    if (spannableverse != null) {
                        try {
                            spannableverse.setSpan(
                                Constant.sifaspansDark,
                                startIndex,
                                endIndex,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        } catch (e:IndexOutOfBoundsException){
                            println("ch")
                        }


                    }
                }
            }
        }

        fun setMudhafFromCache(
            spannableverse: SpannableString,
            mudhafIndexList: MutableList<List<Int>>?
        ) {
            if (mudhafIndexList != null) {
                for (indexes in mudhafIndexList) {
                    val startIndex = indexes[0]
                    val endIndex = indexes[1]


                    val colorSpan = if (dark) {
                        BackgroundColorSpan(Constant.MIDNIGHTBLUE) // Create a new BackgroundColorSpan instance
                    } else {
                        BackgroundColorSpan(Constant.GREENYELLOW) // Create a new BackgroundColorSpan instance
                    }
                    try {
                        spannableverse.setSpan(
                            colorSpan,
                            startIndex,
                            endIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } catch (e:IndexOutOfBoundsException){
                 println(startIndex)
                        println(endIndex)
                        println(e.localizedMessage)
                    }


                }
            }

        }

        fun setAbsoluteNegationFromCache(
            spannableverse: SpannableString,
            absoluteNegationIndexes: List<Any>
        ) {
            val underlineSpan = UnderlineSpan()
            val offsetUnderlineSpan = object : UnderlineSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.baselineShift += ds.ascent().toInt() / 2 // Adjust offset as needed
                    super.updateDrawState(ds)
                }
            }

            if (absoluteNegationIndexes.isNotEmpty()) {

                spannableverse.setSpan(
                    underlineSpan,
                    absoluteNegationIndexes[0] as Int,
                    absoluteNegationIndexes[1] as Int,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableverse.setSpan(
                    offsetUnderlineSpan,
                    absoluteNegationIndexes[0] as Int,
                    absoluteNegationIndexes[1] as Int,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val boldSpan = StyleSpan(Typeface.BOLD)
                spannableverse.setSpan(
                    boldSpan,
                    absoluteNegationIndexes[0] as Int,
                    absoluteNegationIndexes[1] as Int,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

            }

        }


    }

}
