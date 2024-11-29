package com.example.mushafconsolidated.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mushafconsolidated.Entities.Page

import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.data.SurahHeader
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListenerOnLong
import com.example.mushafconsolidatedimport.Config
import com.example.utility.CorpusUtilityorig.Companion.getSpannable
import com.example.utility.QuranViewUtils
import java.text.MessageFormat


//import com.example.mushafconsolidated.Entities.JoinVersesTranslationDataTranslation;
//public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemClickListenerOnLong {
class PageMushaAudioAdapter(
    private val fullQuranPages: ArrayList<Page>,
    val context: Context,
    listener: OnItemClickListenerOnLong?,
    surah_id: Long,
    ismakki: Int,
    header: SurahHeader,
) :
    RecyclerView.Adapter<PageMushaAudioAdapter.ItemViewAdapter>() //implements OnItemClickListenerOnLong {
{
    val surah_id: Long
    private val header: SurahHeader
    private val isMakkiMadani: Int

    // --Commented out by Inspection (25/08/23, 7:28 pm):public TextView arabic, rootword;
    private val arabicfontSize: Int
    private val translationfontsize: Int
    val mItemClickListener: OnItemClickListenerOnLong?

    // --Commented out by Inspection (25/08/23, 7:28 pm):private CorpusAyahWord ayahWord;
    init {
        mItemClickListener = listener
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            context
        )
        sharedPreferences.getBoolean(Config.SHOW_Erab, Config.defaultShowErab)
        //   this.header = header;
        arabicfontSize = sharedPreferences.getInt("pref_font_arabic_key", 18)
        translationfontsize = sharedPreferences.getInt("pref_font_englsh_key", 18)
        this.surah_id = surah_id
        isMakkiMadani = ismakki
        this.header = header
    }

    // --Commented out by Inspection START (25/08/23, 7:28 pm):
    //    public void addContext(Context context) {
    //        this.context = context;
    //
    //    }
    // --Commented out by Inspection STOP (25/08/23, 7:28 pm)
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return fullQuranPages.size + 1

        //     return  quran.size();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAdapter {
        val view: View
        view = if (viewType == 0) {
            LayoutInflater.from(parent.context).inflate(R.layout.surah_header, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.mushaf_row_ayah_word, parent, false)
        }
        return ItemViewAdapter(view, viewType)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ItemViewAdapter, position: Int) {
        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            context
        )
        val isNightmode = sharedPreferences.getString("themepref", "dark")
        val islamicfont = "AlQalam.ttf"
        val arabic_font_selection =
            sharedPreferences.getString("Arabic_Font_Selection", "quranicfontregular.ttf")
        val custom_font = Typeface.createFromAsset(
            context.assets,
            islamicfont
        )
        val FONTS_LOCATION_PATH = "font/dejavusans.ttf"
        val defaultfont = sharedPreferences.getBoolean("default_font", true)
        //bg_black
        if (position % 2 == 1) {
            when (isNightmode) {
                "brown" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context, R.color.bg_brown
                    )
                )

                "dark" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.odd_item_bg_black
                    )
                )

                "blue" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bg_dark_blue
                    )
                )
            }
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            when (isNightmode) {
                "brown" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context, R.color.odd_item_bg_brown
                    )
                )

                "dark" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bg_black
                    )
                )

                "blue" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bg_dark_blue
                    )
                )
            }
        }
        val whichtranslation = sharedPreferences.getString("selecttranslation", "en_sahih")
        if (getItemViewType(position) == 0) {
            val imgs = context.resources.obtainTypedArray(R.array.sura_imgs)


            // You have to set your header items values with the help of model class and you can modify as per your needs
            holder.tvRukus!!.text = String.format("Ruku's :%s", header.rukus)
            holder.tvVerses!!.text = String.format("Aya's :%s", header.verses)
            holder.tvSura!!.text = header.surahNameAr
            val chapterno = header.chapterNumber
            val tauba = chapterno.toInt()
            if (header.chapterNumber == TAUBA_CHAPTER_NUMBER) {
                holder.ivBismillah!!.visibility = View.GONE
            }


            val revelationCity = if (isMakkiMadani == 1) RevalationCity.MAKKI else RevalationCity.MADANI
            QuranViewUtils.setLocationVisibility(holder.ivLocationmakki!!,
                holder.ivLocationmadani!!,revelationCity)
            //   setLocationVisibility(holder, if (isMakkiMadani == 1) RevalationCity.MAKKI else RevalationCity.MADANI)
            val drawable = imgs.getDrawable(header.chapterNumber - 1)
            imgs.recycle()
            holder.ivSurahIcon!!.setImageDrawable(drawable)

            var headercolor: Int
            if (isNightmode == "dark" || isNightmode == "blue" || isNightmode == "green") {
                holder.ivLocationmakki!!.setColorFilter(Color.CYAN)
                holder.ivSurahIcon!!.setColorFilter(Color.CYAN)
                holder.ivLocationmadani!!.setColorFilter(Color.CYAN)
            } else {
                holder.ivLocationmakki!!.setColorFilter(Color.BLACK)
                holder.ivSurahIcon!!.setColorFilter(Color.BLACK)
                holder.ivLocationmadani!!.setColorFilter(Color.BLACK)
            }
        } else {
            val page = fullQuranPages[position - 1]
            //  String s=      passage.get(position);
            val pageNum = page.pageNum.toString()
            holder.pagenum!!.text = pageNum
            var aya: String
            val builder = StringBuilder()
            for (ayahItem in page.ayahItemsquran!!) {
                aya = ayahItem!!.qurantext
                builder.append(MessageFormat.format("{0} ﴿ {1} ﴾ ", aya, ayahItem.ayah))
            }
            holder.quran_textView.setText(
                getSpannable(builder.toString()),
                TextView.BufferType.SPANNABLE
            )
            holder.quran_textView.typeface = custom_font
            if (!defaultfont) {
                holder.quran_textView.textSize = arabicfontSize.toFloat()
            }
        }
    }


    inner class ItemViewAdapter internal constructor(view: View, viewType: Int) :
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        var pagenum: TextView? = null
        var ivBismillah: ImageView? = null
        var tvSura: TextView? = null
        var tvRukus: TextView? = null
        var tvVerses: TextView? = null
        lateinit var quran_textView: TextView
        var ivSurahIcon: ImageView? = null
        var ivLocationmakki: ImageView? = null
        var ivLocationmadani: ImageView? = null

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
                pagenum = view.findViewById(R.id.pagenum)


                //    verse_idTextView = view.findViewById(R.id.verse_id_textView);


                //     erab_textView.setTextIsSelectable(true);
                quran_textView = view.findViewById(R.id.quran_textView)
                view.setOnClickListener(this)
                view.setOnLongClickListener(this)
                quran_textView.setOnClickListener(this)
                quran_textView.tag = "verse"
            }
        }

        override fun onClick(v: View) {
            mItemClickListener?.onItemClick(v, layoutPosition)
        }

        override fun onLongClick(v: View): Boolean {
            //   mItemClickListener.onItemLongClick(getAdapterPosition(), v);
            mItemClickListener?.onItemLongClick(bindingAdapterPosition, v)
            return true
        }
    } //View.OnClickListener, View.OnLongClickListener

    companion object {
        // --Commented out by Inspection (25/08/23, 7:28 pm):BroadcastReceiver broadcastReceiver;
        // --Commented out by Inspection (25/08/23, 7:28 pm):private final boolean isFABOpen = false;
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}