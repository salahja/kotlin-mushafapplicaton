package com.example.mushafconsolidated.fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.text.Html
import android.text.SpannableString
import android.text.SpannableString.valueOf
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.format.DateFormat
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.Constant
import com.example.mushafconsolidated.Activity.TafsirFullscreenActivity
import com.example.mushafconsolidated.Adapters.RevalationCity
import com.example.mushafconsolidated.Entities.BadalErabNotesEnt
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.HalEnt
import com.example.mushafconsolidated.Entities.LiajlihiEnt
import com.example.mushafconsolidated.Entities.MafoolBihi
import com.example.mushafconsolidated.Entities.MafoolMutlaqEnt
import com.example.mushafconsolidated.Entities.NasbListingPojo
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.ShartListingPojo
import com.example.mushafconsolidated.Entities.SurahHeader
import com.example.mushafconsolidated.Entities.TameezEnt
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.SurahSummary
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet.Companion.TAG
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListenerOnLong
import com.example.mushafconsolidated.model.NewQuranCorpusWbw
import com.example.mushafconsolidatedimport.Config
import com.example.mushafconsolidatedimport.ParticleColorScheme
import com.example.utility.AnimationUtility
import com.example.utility.CorpusUtilityorig
import com.example.utility.FlowLayout
import com.example.utility.QuranGrammarApplication
import com.example.utility.QuranViewUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils

import sj.hisnul.fragments.NamesDetail
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

//import com.example.mushafconsolidated.Entities.JoinVersesTranslationDataTranslation;
//public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemClickListenerOnLong {
class PhrasesFlowAdapter(

    private var accusativesSentences: List<NasbListingPojo>,


    isaudio: Boolean,

    private val header: SurahHeader,
    private val allofQuran: List<QuranEntity?>?,
    private val ayahWordArrayList: LinkedHashMap<Int, ArrayList<CorpusEntity>>,
    var context: Context,
    private val SurahName: String,
    private val isMakkiMadani: Int,
    listener: OnItemClickListenerOnLong?,
) : RecyclerView.Adapter<PhrasesFlowAdapter.ItemViewAdapter>() //implements OnItemClickListenerOnLong {
{
    private var ayahWord: ArrayList<CorpusEntity>? = null
    private var defaultfont: Boolean = false
    private var isFABOpen = false
    private var issentence: Boolean = false
    lateinit var rootword: TextView
    private val spannedWordsCache = HashMap<CorpusEntity, SpannableString>()
    private var wordByWordDisplay: Boolean = false
    //MaterialTextView arabic;
    private lateinit var arabicChipview: Chip
    private lateinit var arabicTv: MaterialTextView
    private var arabicfontSize: Int = 0
    private var translationfontsize: Int = 0
    lateinit var mItemClickListener: OnItemClickListenerOnLong
    private lateinit var isNightmode: String
    private var headercolor = 0

    private lateinit var colorwordfont: Typeface

    //   private lateinit var  ayahWord: CorpusAyahWord
    //  private  var ayahWord: QuranCorpusWbw? = null
    private val isaudio: Boolean


    init {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            context
        )
        sharedPreferences.getBoolean(Config.SHOW_Erab, Config.defaultShowErab)
        issentence = sharedPreferences.getBoolean("grammarsentence", false)
        arabicfontSize = sharedPreferences.getInt("pref_font_arabic_key", 18)
        translationfontsize = sharedPreferences.getInt("pref_font_englsh_key", 18)
        defaultfont = sharedPreferences.getBoolean("default_font", true)
        if (listener != null) {
            mItemClickListener = listener
        }
        this.isaudio = isaudio
    }

    fun addContext(context: Context) {
        this.context = context
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return ayahWordArrayList.size + 1
        //     return  quran.size();
    }



    override fun getItemId(position: Int): Long {
        val ayahWord = ayahWordArrayList[position]
        var itemId: Long = 0

        itemId = ayahWord!![position].ayah.toLong()

        return itemId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAdapter {
        val view: View = if (viewType == 0) {
            LayoutInflater.from(parent.context).inflate(R.layout.surah_header, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.row_ayah_word, parent, false)
            //   view = LayoutInflater.from(parent.context!!).inflate(R.layout.item_viewer_aya_cardview, parent, false);
        }
        return ItemViewAdapter(view, viewType)
    }

    fun getItem(position: Int): QuranEntity? {
        //    return ayahWordArrayList!![0].word!![position]
        return allofQuran!![position]
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ItemViewAdapter, position: Int) {
        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            context
        )




        isNightmode = sharedPreferences.getString("themepref", "dark").toString()
        val arabic_font_selection =
            sharedPreferences.getString("Arabic_Font_Selection", "quranicfontregular.ttf")
        val custom_font = Typeface.createFromAsset(
            context.assets,
            arabic_font_selection
        )
        val FONTS_LOCATION_PATH = "fonts/DejaVuSans.ttf"
        colorwordfont = Typeface.createFromAsset(
            QuranGrammarApplication.context!!.assets,
            FONTS_LOCATION_PATH
        )
        wordByWordDisplay = sharedPreferences.getBoolean("wordByWordDisplay", true)
        val showrootkey = sharedPreferences.getBoolean("showrootkey", true)
        val showErab = sharedPreferences.getBoolean("showErabKey", true)
        val showWordColor = sharedPreferences.getBoolean("colortag", true)
        val showTransliteration = sharedPreferences.getBoolean("showtransliterationKey", true)
        val showJalalayn = sharedPreferences.getBoolean("showEnglishJalalayn", true)
        val showTranslation = sharedPreferences.getBoolean("showTranslationKey", true)
        val showWordByword = sharedPreferences.getBoolean("wordByWord", false)


        QuranViewUtils.setBackgroundColor(context, holder.itemView, isNightmode, position % 2 == 1)


        val whichtranslation = sharedPreferences.getString("selecttranslation", "en_sahih")
        if (getItemViewType(position) == 0) {
            val imgs = context.resources.obtainTypedArray(R.array.sura_imgs)

            // You have to set your header items values with the help of model class and you can modify as per your needs
            holder.tvRukus.text = String.format("Ruku's :%s", header.rukus)
            holder.tvVerses.text = String.format("Aya's :%s", header.verses)
            holder.tvSura.text = header.surahNameAr
            val chapterno = header.chapterNumber
            val tauba = chapterno.toInt()
            if (header.chapterNumber == com.example.mushafconsolidated.Adapters.TAUBA_CHAPTER_NUMBER) {
                holder.ivBismillah.visibility = View.GONE
            }

            val revelationCity =
                if (isMakkiMadani == 1) RevalationCity.MAKKI else RevalationCity.MADANI
            QuranViewUtils.setLocationVisibility(
                holder.ivLocationmakki,
                holder.ivLocationmadani,
                revelationCity
            )
            val drawable = imgs.getDrawable(header.chapterNumber - 1)
            imgs.recycle()
            holder.ivSurahIcon.setImageDrawable(drawable)
        } else {
            displayAyats(
                showrootkey,
                holder,
                position - 1,
                sharedPreferences,
                custom_font,
                showErab,
                showWordColor,
                showTransliteration,
                showJalalayn,
                showTranslation,
                showWordByword,
                whichtranslation
            )
        }
    }

    private fun displayAyats(
        showrootkey: Boolean,
        holder: ItemViewAdapter,
        position: Int,
        sharedPreferences: SharedPreferences,
        custom_font: Typeface,
        showErab: Boolean,
        showWordColor: Boolean,
        showTransliteration: Boolean,
        showJalalayn: Boolean,
        showTranslation: Boolean,
        showWordByword: Boolean,
        whichtranslation: String?,
    ) {
        //   holder.flowwbw.setBackgroundColor(R.style.Theme_DarkBlue);

        //   var entity: QuranEntity? = null

        val wbw = sharedPreferences.getString("wbw", "en")
        val entity = accusativesSentences[position]
        val quranEntity= allofQuran?.get(position)

        holder.quran_textView.text = entity.spannableVerse
        //   holder.quran_textView.setTextSize(arabicfontSize);
        holder.quran_textView.typeface = custom_font
        holder.base_cardview.visibility = View.GONE
        //  }

        holder.mafoolbihi.visibility = View.GONE
        holder.base_cardview.visibility = View.GONE
        setChapterInfo(holder, quranEntity)

        wbw?.let {
            wordBywordWithTranslation(
                showrootkey,
                holder,
                showWordColor,
                it,
                ayahWord,
                ayahWordArrayList,
                showWordByword,
                position
            )
        }


        if (showTransliteration) {
            if (entity != null) {
                holder.quran_transliteration.text =
                    Html.fromHtml(entity.en_transliteration, Html.FROM_HTML_MODE_LEGACY)
            }
            holder.quran_transliteration.visibility = View.VISIBLE
        }
        if (showJalalayn) {
            //   holder.quran_jalalaynnote.setText(enjalalayn.getAuthor_name());
            if (entity != null) {
                holder.quran_jalalayn.text = entity.en_jalalayn
            }
            //
            holder.quran_jalalayn.visibility = View.VISIBLE
            holder.quran_jalalaynnote.visibility = View.VISIBLE
        }
        if (showTranslation) {
            when (whichtranslation) {
                "en_arberry" -> QuranViewUtils.setTranslationText(
                    holder.translate_textView,
                    holder.translate_textViewnote,
                    quranEntity,
                    whichtranslation,
                    R.string.arberry
                )

                "en_sahih" -> QuranViewUtils.setTranslationText(
                    holder.translate_textView,
                    holder.translate_textViewnote,
                    quranEntity,
                    whichtranslation,
                    R.string.ensahih
                )

                "en_jalalayn" -> QuranViewUtils.setTranslationText(
                    holder.translate_textView,
                    holder.translate_textViewnote,
                    quranEntity,
                    whichtranslation,
                    R.string.enjalalayn
                )

                "ur_jalalayn" -> QuranViewUtils.setTranslationText(
                    holder.translate_textView,
                    holder.translate_textViewnote,
                    quranEntity,
                    whichtranslation,
                    R.string.enjalalayn
                )

                "ur_junagarhi" -> QuranViewUtils.setTranslationText(
                    holder.translate_textView,
                    holder.translate_textViewnote,
                    quranEntity,
                    whichtranslation,
                    R.string.urjunagadi
                )
                // Add more cases as needed
                // ... other translations
            }
            //
            holder.translate_textView.visibility = View.VISIBLE
            holder.translate_textViewnote.visibility = View.VISIBLE
        }
        if (showErab) {
            holder.erabexpand.visibility = View.VISIBLE
            if (entity != null) {
                if (entity.ar_irab_two.isNullOrBlank()) {
                    holder.erab_textView.text = entity.erabspnabble


                } else {
                    holder.erab_textView.text = entity.erabspnabble
                }
            }
            //
            holder.erab_textView.typeface = custom_font
            //     holder.erab_textView.setVisibility(View.VISIBLE);
            holder.erab_textViewnote.visibility = View.VISIBLE
        } else {
            holder.erabexpand.visibility = View.GONE
        }
        setTextSizes(holder)
    }

    private fun setTextSizes(holder: ItemViewAdapter) {
        if (!defaultfont) {
            holder.quran_transliteration.textSize = translationfontsize.toFloat()
            holder.erab_textView.textSize = translationfontsize.toFloat()
            holder.translate_textView.textSize = translationfontsize.toFloat()
            holder.translate_textViewnote.textSize = translationfontsize.toFloat()
            holder.quran_jalalayn.textSize = translationfontsize.toFloat()
        }
    }



    private fun wordBywordWithTranslation(
        showrootkey: Boolean,
        holder: ItemViewAdapter,
        showWordColor: Boolean,
        wbw: String,
        ayahWord: ArrayList<CorpusEntity>?,
        ayahWordArrayList: LinkedHashMap<Int, ArrayList<CorpusEntity>>,
        showWbwTranslation: Boolean,
        position: Int,
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        holder.flow_word_by_word.removeAllViews()


        val quranEntity = allofQuran?.get(position) // Get the QuranEntity at this position



        // Get the corresponding ayahId from QuranEntity
        val ayahId = quranEntity!!.ayah

        // Retrieve the corresponding words from ayahWordArrayList using ayahId as key
        val wordArray = ayahWordArrayList[ayahId]

        for (word in wordArray!!) {
            var aindex = 0
            @SuppressLint("InflateParams") val view = inflater.inflate(R.layout.word_by_word, null)
            arabicChipview = view.findViewById(R.id.word_arabic_chipview)
            arabicTv = view.findViewById(R.id.word_arabic_textView)
            rootword = view.findViewById(R.id.root_word)
            //   wordno = view.findViewById(R.id.wordno);
            val translationTextView = view.findViewById<TextView>(R.id.word_trans_textView)
            var spannedroot: SpannableString? = null
            val sb = StringBuilder()

            sb.append(word.wordno)
            val rootwords =
                word.rootaraone + word.rootaratwo + word.rootarathree + word.rootarafour + word.rootarafive


            if (showrootkey) {
                spannedroot = if (rootwords.isNotEmpty()) {
                    QuranViewUtils.NewgetSpannedRoots(word, rootwords)
                } else {
                    valueOf(rootwords)
                }
                rootword.text = spannedroot
                //
                rootword.visibility = View.VISIBLE
            }


            //arabicTv
            var spannedWord = SpannableString(" ")
            if (showWordColor) {

                // Check if the word is already cached
                spannedWord = spannedWordsCache.getOrPut(word) {
                    // Log when we're generating a new SpannableString and storing it in the cache
                    Log.d(TAG, "FROM FILE (generated new SpannableString)")
                    QuranViewUtils.NewgetSpannedWords(word)
                }

                // Log when we're fetching the word from cache
                if (spannedWordsCache.containsKey(word)) {
                    Log.d(TAG, "FROM CACHE")
                }

                val arabicView =
                    if (showWbwTranslation && wordByWordDisplay) arabicChipview else arabicTv
                arabicView.text = spannedWord
                arabicView.textSize = arabicfontSize.toFloat()
                arabicView.visibility = View.VISIBLE
            } else {
                if (showWbwTranslation) {
                    arabicChipview.text =
                        word.araone + word.aratwo + word.arathree + word.arafour + word.arafive
                    arabicChipview.visibility = View.VISIBLE
                } else {
                    arabicTv.text =
                        word.araone + word.aratwo + word.arathree + word.arafour + word.arafive
                    arabicTv.visibility = View.VISIBLE

                }
            }
            rootword.text = spannedroot
            rootword.textSize = arabicfontSize.toFloat()
            arabicTv.typeface = colorwordfont
            arabicChipview.typeface = colorwordfont

            if (showWbwTranslation) {
                QuranViewUtils.setWordTranslation(translationTextView, word, wbw)


            }
            if (!defaultfont) {
                rootword.textSize = arabicfontSize.toFloat()
                arabicChipview.textSize = arabicfontSize.toFloat()
                translationTextView.textSize = arabicfontSize.toFloat()
            }
            holder.flow_word_by_word.addView(view)
            view.isLongClickable = true
            //   if (!showWbwTranslation) {

            arabicChipview.setOnLongClickListener { v ->
                // showWordMorphologyTooltip(v, word)
                QuranViewUtils.NewshowWordMorphologyTooltip(context, v, word, isNightmode)
                true
            }


            //   } else {
            view.setOnLongClickListener { v ->
                // showWordMorphologyTooltip(v, word)
                QuranViewUtils.NewshowWordMorphologyTooltip(context, v, word, isNightmode)
                true
            }

            QuranViewUtils.NewsetWordClickListener(
                arabicChipview,
                context,
                word,
                SurahName
            ) { bundle, word ->
                NewLoadItemList(bundle, word)
            }
            QuranViewUtils.NewsetWordClickListener(view, context, word, SurahName) { bundle, word ->
                NewLoadItemList(bundle, word)
            }


        }
        holder.flow_word_by_word.visibility = View.VISIBLE
    }

    private fun NewLoadItemList(dataBundle: Bundle, word: CorpusEntity) {
        if (issentence) {
            /* val item = SentenceAnalysisBottomSheet()
             item.arguments = dataBundle
             val data = arrayOf(
                 word.surah.toString(),
                 word.ayah.toString(),
                 word.translateEn,
                 word.wordno.toString()
             )
             SentenceAnalysisBottomSheet.newInstance(data).show(
                 (context as AppCompatActivity).supportFragmentManager,
                 SentenceAnalysisBottomSheet.TAG
             )*/
        } else {

            val item = WordAnalysisBottomSheet()
            item.arguments = dataBundle

            val data = arrayOf<String?>(
                word.surah.toString(),
                word.ayah.toString(),
                word.en,
                word.wordno.toString(),
                SurahName
            )
            WordAnalysisBottomSheet.newInstance(data).show(
                (context as AppCompatActivity).supportFragmentManager,
                WordAnalysisBottomSheet.TAG
            )
        }
    }

    private fun getSpannedRoots(tag: CorpusEntity, rootword: String): SpannableString {
        /*   val b = corpus.corpus.ayah == 20 && (corpus.corpus.wordno == 2 || corpus.corpus.wordno == 9)
           if (b) {
               println("check")
           }*/
        return CorpusUtilityorig.ColorizeRootword(
            tag.tagone!!,
            tag.tagtwo!!,
            tag.tagthree!!,
            tag.tagfour!!,
            tag.tagfive!!,
            rootword
        )!!
    }




    private fun setChapterInfo(holder: ItemViewAdapter, quran: QuranEntity?) {
        val surahInfo = java.lang.StringBuilder()
        //        surahInfo.append(surahName+".");
        surahInfo.append(quran!!.surah).append(".")
        surahInfo.append(quran!!.ayah).append("-")
        surahInfo.append(SurahName)
        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            context
        )
        val isNightmode = sharedPreferences.getString("theme", "dark")


        if (isNightmode != null) {
            QuranViewUtils.setIconColors(holder.surah_info, isNightmode, isMakkiMadani)
            holder.surah_info.text = surahInfo
            holder.surah_info.textSize = 16f
        }

    }


    inner class ItemViewAdapter @SuppressLint("CutPasteId") internal constructor(
        view: View,
        viewType: Int,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        lateinit var quran_jalalayn: TextView

        //  lateinit var  kathir_translation: TextView
        lateinit var quran_transliteration: TextView
        lateinit var translate_textView: TextView

        //   public TextView erab_textView;
        lateinit var erab_textView: TextView
        lateinit var surah_info: TextView
        lateinit var mafoolbihi: TextView
        private lateinit var erab_notes: TextView
        lateinit var quran_textView: MaterialTextView
        private lateinit var quran_transliterationnote: TextView
        lateinit var quran_jalalaynnote: TextView
        lateinit var erab_textViewnote: TextView
        lateinit var translate_textViewnote: TextView
        lateinit var bookmark: ImageView
        lateinit var jumpto: ImageView
        private lateinit var ivSummary: ImageView
        lateinit var ivBismillah: ImageView
        lateinit var erabexpand: ImageView
        private lateinit var erab_notes_expand: ImageView
        lateinit var tvSura: TextView
        lateinit var tvRukus: TextView
        lateinit var tvVerses: TextView
        lateinit var ivSurahIcon: ImageView
        lateinit var ivLocationmakki: ImageView
        lateinit var ivLocationmadani: ImageView
        private lateinit var ivhelp: ImageView
        private lateinit var ivoverflow: ImageView
        private lateinit var ivoverflow2: ImageView

        //  public   com.nex3z.flowlayout.FlowLayout  flow_word_by_word;
        lateinit var flow_word_by_word: FlowLayout

        //   RelativeLayout colllayout;
        lateinit var erabnotescardView: CardView
        private lateinit var mafoolatarow: ImageView
        private lateinit var hiddenGroup: Group
        lateinit var base_cardview: MaterialCardView
        lateinit var tafsir: FloatingActionButton
        lateinit var jumptofb: FloatingActionButton
        lateinit var bookmarfb: FloatingActionButton
        lateinit var fabmenu: FloatingActionButton
        lateinit var helpfb: FloatingActionButton
        lateinit var summbaryfb: FloatingActionButton
        lateinit var sharescreenfb: FloatingActionButton
        lateinit var collectionfb: FloatingActionButton

        init {
            view.tag = this
            itemView.setOnClickListener(this)
            if (viewType == 0) {
                ivLocationmakki = view.findViewById(R.id.ivLocationmakki)
                ivLocationmadani = view.findViewById(R.id.ivLocationmadani)
                ivSurahIcon = view.findViewById(R.id.ivSurahIcon)
                tvVerses = view.findViewById(R.id.tvVerses)
                tvRukus = view.findViewById(R.id.tvRukus)
                tvSura = view.findViewById(R.id.tvSura)
                ivBismillah = view.findViewById(R.id.ivBismillah)
            } else {
                //     kathir_note = view.findViewById(R.id.kathir_note);
                //   kathir_translation = view.findViewById(R.id.katheer_textview)
                ivSummary = view.findViewById(R.id.ivSumarry)
                ivSummary.tag = "summary"
                ivSummary.setOnClickListener(this)
                jumpto = view.findViewById(R.id.jumpto)
                ivhelp = view.findViewById(R.id.ivHelp)
                ivoverflow = view.findViewById(R.id.ivActionOverflow)
                ivhelp.setOnClickListener(this)
                ivoverflow.setOnClickListener(this)
                ivoverflow.tag = "overflow_img"
                ivoverflow2 = view.findViewById(R.id.ivActionOverflowtwo)
                ivoverflow2.setOnClickListener(this)
                ivoverflow2.tag = "overflowbottom"
                //   makkimadaniicon = view.findViewById(R.id.makkimadaniicon)
                //    jumpto = view.findViewById(R.id.jumpto);
                //   bismilla = view.findViewById(R.id.bismillah)
                quran_transliterationnote = view.findViewById(R.id.quran_transliterationnote)
                quran_jalalaynnote = view.findViewById(R.id.quran_jalalaynnote)
                translate_textViewnote = view.findViewById(R.id.translate_textViewnote)
                erab_textViewnote = view.findViewById(R.id.erab_textViewnote)
                quran_transliteration = view.findViewById(R.id.quran_transliteration)
                quran_jalalayn = view.findViewById(R.id.quran_jalalayn)
                surah_info = view.findViewById(R.id.chaptername)
                //    verse_idTextView = view.findViewById(R.id.verse_id_textView);
                flow_word_by_word = view.findViewById(R.id.flow_word_by_word)
                translate_textView = view.findViewById(R.id.translate_textView)
                erab_textView = view.findViewById(R.id.erab_textView)
                //     erab_textView.setTextIsSelectable(true);
                quran_textView = view.findViewById(R.id.quran_textView)
                erab_notes = view.findViewById(R.id.erab_notes)
                //     bookmark = view.findViewById(R.id.bookmarkView);
                erabexpand = view.findViewById(R.id.erabexpand)
                erab_notes_expand = view.findViewById(R.id.erab_img)
//                expandImageButton = view.findViewById(R.id.expandImageButton)
                quran_textView.setOnClickListener(this)
                quran_textView.tag = "qurantext"
                erab_notes_expand.setOnClickListener(this)
                erab_notes_expand.tag = "erab_notes"
                erabnotescardView = view.findViewById(R.id.base_cardview)
                mafoolatarow = view.findViewById(R.id.show)
                hiddenGroup = view.findViewById(R.id.card_group)
                mafoolatarow.setOnClickListener(this)
                mafoolbihi = view.findViewById(R.id.directobject)
                base_cardview = view.findViewById(R.id.base_cardview)
                collectionfb = view.findViewById(R.id.collectionfb)
                collectionfb.setOnClickListener(this)
                collectionfb.tag = "collection"
                fabmenu = view.findViewById(R.id.expandfabs)
                tafsir = view.findViewById(R.id.tafsirfb)
                jumptofb = view.findViewById(R.id.jumptofb)
                bookmarfb = view.findViewById(R.id.bookmarfb)
                summbaryfb = view.findViewById(R.id.summbaryfb)
                helpfb = view.findViewById(R.id.helpfb)
                sharescreenfb = view.findViewById(R.id.sharescreenfb)
                sharescreenfb.setOnClickListener(this)
                fabmenu.setOnClickListener(this)
                tafsir.setOnClickListener(this)
                jumptofb.setOnClickListener(this)
                tafsir.setOnClickListener(this)
                bookmarfb.setOnClickListener(this)
                summbaryfb.setOnClickListener(this)
                helpfb.setOnClickListener(this)
                fabmenu.tag = "fabmenu"
                tafsir.tag = "tafsir"
                jumptofb.tag = "jumptofb"
                bookmarfb.tag = "bookmarfb"
                helpfb.tag = "help"
                summbaryfb.tag = "summaryfb"
                sharescreenfb.tag = "sharefb"
                view.setOnClickListener(this)
                view.setOnLongClickListener(this)
                val shared = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                    QuranGrammarApplication.context!!
                )
                val colortag = shared.getBoolean("colortag", true)
                fabmenu.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        if (!isFABOpen) {
                            showFABMenu()
                        } else {
                            closeFABMenu()
                            //     HideFabMenu();
                        }
                    }

                    private fun showFABMenu() {
                        isFABOpen = true
                        fabmenu.animate().rotationBy(180f)
                        if (!isaudio) {
                            tafsir.visibility = View.VISIBLE
                            tafsir.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_55)
                            )
                            tafsir.animate().rotationBy(360f)
                            tafsir.animate().duration = 1500



                            jumptofb.visibility = View.VISIBLE
                            jumptofb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_105)
                            )
                            jumptofb.animate().rotationBy(360f)
                            bookmarfb.visibility = View.VISIBLE
                            bookmarfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_155)
                            )
                            bookmarfb.animate().rotationBy(360f)
                            bookmarfb.animate().duration = 600
                            summbaryfb.visibility = View.VISIBLE
                            summbaryfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_205)
                            )
                            summbaryfb.animate().rotationBy(360f)
                            helpfb.visibility = View.VISIBLE
                            helpfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_255)
                            )
                            helpfb.animate().rotationBy(360f)
                            sharescreenfb.visibility = View.VISIBLE
                            sharescreenfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_305)
                            )
                            sharescreenfb.animate().rotationBy(360f)
                            sharescreenfb.animate().duration = 500
                            collectionfb.visibility = View.VISIBLE
                            collectionfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_405)
                            )
                            collectionfb.animate().rotationBy(360f)
                            collectionfb.animate().duration = 500
                        } else {
                            tafsir.visibility = View.VISIBLE
                            tafsir.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.standard_55)
                            )
                            tafsir.animate().rotationBy(360f)
                            tafsir.animate().duration = 1500
                            jumptofb.visibility = View.VISIBLE
                            jumptofb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.audio_105)
                            )
                            jumptofb.animate().rotationBy(360f)
                            bookmarfb.visibility = View.VISIBLE
                            bookmarfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.audio_155)
                            )
                            bookmarfb.animate().rotationBy(360f)
                            bookmarfb.animate().duration = 600
                            summbaryfb.visibility = View.VISIBLE
                            summbaryfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.audio_205)
                            )
                            summbaryfb.animate().rotationBy(360f)
                            helpfb.visibility = View.VISIBLE
                            helpfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.audio_255)
                            )
                            helpfb.animate().rotationBy(360f)
                            sharescreenfb.visibility = View.VISIBLE
                            sharescreenfb.animate().translationX(
                                -QuranGrammarApplication.instance!!.resources.getDimension(R.dimen.audio_305)
                            )
                            sharescreenfb.animate().rotationBy(360f)
                            sharescreenfb.animate().duration = 500
                        }
                        tafsir.setOnClickListener { view12: View? ->
                            closeFABMenu()
                            val readingintent =
                                Intent(context, TafsirFullscreenActivity::class.java)
                            val chapter_no = ayahWord!![0].surah
                            val verse = ayahWordArrayList[position - 1]?.get(0)!!.ayah

                            readingintent.putExtra(Constant.SURAH_ID, chapter_no)
                            readingintent.putExtra(Constant.AYAH_ID, verse)
                            readingintent.putExtra(Constant.SURAH_ARABIC_NAME, SurahName)
                            context.startActivity(readingintent)
                        }
                        summbaryfb.setOnClickListener { v: View? ->
                            closeFABMenu()
                            //  HideFabMenu();
                            val chapter_no = ayahWord!![0].surah
                            //   int verse = ayahWord.getWord().get(0).getVerseId();
                            val verse = ayahWordArrayList[position - 1]?.get(0)!!.ayah
                            val dataBundle = Bundle()
                            dataBundle.putInt(Constant.SURAH_ID, chapter_no)
                            val item = SurahSummary()
                            item.arguments = dataBundle
                            SurahSummary.newInstance(chapter_no).show(
                                (context as AppCompatActivity).supportFragmentManager,
                                NamesDetail.TAG
                            )
                        }
                        helpfb.setOnClickListener { v: View? ->
                            closeFABMenu()
                            ParticleColorScheme.newInstance().show(
                                (context as AppCompatActivity).supportFragmentManager,
                                WordAnalysisBottomSheet.TAG
                            )
                        }
                        sharescreenfb.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View) {
                                closeFABMenu()
                                //HideFabMenu();

                                takeScreenShot((context as AppCompatActivity).window.decorView)
                            }

                            private fun takeScreenShot(view: View) {
                                val date = Date()
                                val format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date)
                                try {
                                    val mainDir = File(
                                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                        "FilShare"
                                    )
                                    if (!mainDir.exists()) {
                                        val mkdir = mainDir.mkdir()
                                    }
                                    val path = "$mainDir/Mushafapplication-$format.jpeg"
                                    //    File zipfile = new File(getExternalFilesDir(null).getAbsolutePath() + getString(R.string.app_folder_path) + File.separator + DATABASEZIP);
                                    view.isDrawingCacheEnabled = true
                                    val color = Color.RED
                                    val bitmap = getBitmapFromView(view, color)
                                    val imageFile = File(path)
                                    val fileOutputStream = FileOutputStream(imageFile)
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
                                    fileOutputStream.flush()
                                    fileOutputStream.close()
                                    shareScreenShot(imageFile)
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }

                            fun getBitmapFromView(view: View, defaultColor: Int): Bitmap {
                                val bitmap = Bitmap.createBitmap(
                                    view.width, view.height, Bitmap.Config.ARGB_8888
                                )
                                val canvas = Canvas(bitmap)
                                canvas.drawColor(defaultColor)
                                view.draw(canvas)
                                return bitmap
                            }

                            private fun shareScreenShot(imageFile: File) {
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    QuranGrammarApplication.context!!.packageName + ".provider",
                                    imageFile
                                )
                                val intent = Intent()
                                intent.action = Intent.ACTION_SEND
                                intent.type = "image/*"
                                intent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Download Application from Instagram"
                                )
                                intent.putExtra(Intent.EXTRA_STREAM, uri)
                                val resInfoList = context.packageManager.queryIntentActivities(
                                    intent,
                                    PackageManager.MATCH_DEFAULT_ONLY
                                )
                                for (resolveInfo in resInfoList) {
                                    val packageName = resolveInfo.activityInfo.packageName
                                    context.grantUriPermission(
                                        packageName,
                                        uri,
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    )
                                }
                                //  startActivity(Intent.createChooser(intent, "Share PDF using.."));
                                try {
                                    context.startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Share With"
                                        )
                                    )
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        })
                    }

                    private fun closeFABMenu() {
                        isFABOpen = false
                        fabmenu.animate().rotationBy(-180f)
                        tafsir.animate().translationX(0f)
                        tafsir.animate().rotationBy(0f)
                        jumptofb.animate().translationX(0f)
                        bookmarfb.animate().translationX(0f)
                        bookmarfb.animate().rotationBy(360f)
                        summbaryfb.animate().translationX(0f)
                        helpfb.animate().translationX(0f)
                        sharescreenfb.animate().translationX(0f)
                        sharescreenfb.animate().rotationBy(360f)
                        collectionfb.animate().translationX(0f)
                        collectionfb.animate().rotationBy(360f)
                    }
                })
                mafoolatarow.setOnClickListener { view1: View? ->
                    TransitionManager.beginDelayedTransition(erabnotescardView, AutoTransition())
                    if (hiddenGroup.visibility == View.VISIBLE) {
                        hiddenGroup.visibility = View.GONE
                        mafoolatarow.setImageResource(android.R.drawable.arrow_down_float)
                    } else {
                        //     colllayout.setLayoutParams(params);
                        hiddenGroup.visibility = View.VISIBLE
                        mafoolatarow.setImageResource(android.R.drawable.arrow_up_float)
                    }
                }
                erabexpand.setOnClickListener { view1: View? ->
                    if (erab_textView.visibility == View.GONE) {
                        erab_textView.visibility = View.VISIBLE
                        //  AnimationUtility.slide_down(context, erabexpand);
                        AnimationUtility.AnimateArrow(90.0f, erabexpand)
                    } else {
                        erab_textView.visibility = View.GONE
                        AnimationUtility.AnimateArrow(0.0f, erabexpand)
                        //   Fader.slide_down(context,expandImageButton);
                    }
                }
                flow_word_by_word.setOnClickListener { view1: View? ->
                    if (translate_textView.visibility == View.GONE) translate_textView.visibility =
                        View.VISIBLE else translate_textView.visibility = View.VISIBLE
                }
                translate_textView.setOnClickListener { view1: View? ->
                    if (translate_textView.visibility == View.VISIBLE) translate_textView.visibility =
                        View.GONE else translate_textView.visibility = View.VISIBLE
                }
                erab_textView.setOnClickListener { view1: View? ->
                    if (erab_textView.visibility == View.VISIBLE) erab_textView.visibility =
                        View.GONE else erab_textView.visibility = View.VISIBLE
                }
            }
        }

        override fun onClick(v: View) {
            mItemClickListener.onItemClick(v, layoutPosition)
        }

        override fun onLongClick(v: View): Boolean {
            //   mItemClickListener.onItemLongClick(getAdapterPosition(), v);
            mItemClickListener.onItemLongClick(bindingAdapterPosition, v)
            return true
        }
    } //View.OnClickListener, View.OnLongClickListener

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}