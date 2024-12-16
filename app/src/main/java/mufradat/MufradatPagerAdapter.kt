package mufradat

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListener
import com.example.utility.PreferenceUtil
import com.example.utility.QuranGrammarApplication.Companion.context
import com.google.android.material.card.MaterialCardView
import mufradat.MufradatPagerAdapter.MufradatViewHolder
import org.sj.conjugator.utilities.SharedPref
import java.util.regex.Pattern


//import com.example.utility.SharedPref;
class MufradatPagerAdapter(
    val surahName: String,
    private val tafseeArrayList: ArrayList<MufradatEntity>,
    viewPagertwo: ViewPager2?,
    private val isMakkiMadani: Int,
    activity: FragmentActivity
) : RecyclerView.Adapter<MufradatViewHolder>() {
    var URDU_FONTS: String = "fonts/Mehr.ttf"
    var QURAN_FONTS: String = "fonts/AlQalam.ttf"

    //  private final Typeface alQalamFont;
    //  private final boolean applyLineSpacing;
    //  private final Typeface arabicFont;
    private val isExpanded = false

    //    private final Integer arabicFontSize;
    private val prefs: SharedPref
    private val isNightmode: String

    private var bookChapterno = 0
    private var bookVerseno = 0
    var bookmarkpostion: Int = 0


    private val ayahNumber: Int? = null

    //   LayoutInflater mInflater = LayoutInflater.from(context);
    private val mufradContext: Context = activity


    var mItemClickListener: OnItemClickListener? = null


    var pref: PreferenceUtil

    private var arabic_font_selection: String? = null


    fun setBookChapterno(bookChapterno: Int) {
        this.bookChapterno = bookChapterno
    }


    fun setBookVerseno(bookVerseno: Int) {
        this.bookVerseno = bookVerseno
    }

    init {
        prefs = SharedPref(mufradContext)

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(mufradContext)
        pref = PreferenceUtil(sharedPreferences)
        isNightmode = sharedPreferences.getString("themepref", "dark").toString()
    }


    // }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MufradatViewHolder {
        //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mufradat_ayah_list_row, parent, false);

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ayah_slide_two, parent, false)

        val viewHolder = MufradatViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: MufradatViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val verse = tafseeArrayList.get(position)

        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            context!!

        )

        var isNightmode = sharedPreferences.getString("themepref", "dark").toString()
        if (position % 2 == 1) {
            when (isNightmode) {
                "brown" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!, R.color.bg_brown
                    )
                )

                "dark" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.odd_item_bg_black
                    )
                )

                "blue" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.bg_surface_dark_blue
                    )
                )
            }
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            when (isNightmode) {
                "brown" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!, R.color.odd_item_bg_brown
                    )
                )

                "dark" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.bg_black
                    )
                )

                "blue" -> holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.bg_dark_blue
                    )
                )
            }
        }

        setAyahText(holder, verse)
        setStartText(holder, verse)
        setTranslationAndTafseer(holder, verse)
        setrukuinfo(holder, verse)
        setWordByWord(holder, verse)
        setBookMarkFeilds(holder, verse, position)
        setTextViewStyles(holder)


    }


    private fun setBookMarkFeilds(
        holder: MufradatViewHolder,
        verse: MufradatEntity,
        position: Int
    ) {
        setBookChapterno(verse.surahNumber)
        setBookVerseno(verse.AyahNumber)
        bookmarkpostion = position
    }

    private fun setWordByWord(holder: MufradatViewHolder, verse: MufradatEntity) {





        val colorizedAyahWords =if (isNightmode == "dark" || isNightmode == "blue" || isNightmode == "green") {
            // if (SharedPref.themePreferences().equals("dark")) {
            getColorizedAyahWords(
                verse.words,
                Color.YELLOW,
                Color.GREEN
            )
        } else {
            getColorizedAyahWords(
                verse.words,
                Color.BLUE,
                ContextCompat.getColor(mufradContext, R.color.burntamber)
            )
        }
        setHtmlText(colorizedAyahWords)
        holder.ayah_words.text = Html.fromHtml(colorizedAyahWords)
    }

    private fun setTranslationAndTafseer(holder: MufradatViewHolder, verse: MufradatEntity) {
        // assert arabic_font_size != null;
        //  int anInt = Integer.parseInt(arabic_font_size);
        //   Typeface typeface = Typeface.createFromAsset(applicationContext.getAssets(), urdu_font_selection);
        //   TextView urdu = holder.ayah_translation;
        //  urdu.setTypeface(typeface);

        holder.ayah_translation.typeface =
            Typeface.createFromAsset(mufradContext.assets, URDU_FONTS)
        holder.ayah_tafseer.text = verse.tafseer
        holder.ayah_translation.text = verse.translation
       // holder.ayah_tafseer.setTextColor(ContextCompat.getColor(mufradContext, R.color.green))
        holder.ayah_tafseer.text = verse.tafseer
        //if (SharedPref.themePreferences().equals("light")) {
      /*  if (isdark != "blue") {
            holder.ayah_translation.setTextColor(
                ContextCompat.getColor(
                    mufradContext,
                    R.color.burntamber
                )
            )
            holder.ayah_translation.text = verse.translation
            holder.ayah_tafseer.setTextColor(
                ContextCompat.getColor(
                    mufradContext,
                    R.color.burntamber
                )
            )
            holder.ayah_tafseer.text = verse.tafseer
        } else {
            holder.ayah_translation.setTextColor(
                ContextCompat.getColor(
                    mufradContext,
                    R.color.cyan
                )
            )
            holder.ayah_tafseer.text = verse.tafseer
            holder.ayah_translation.text = verse.translation
            holder.ayah_tafseer.setTextColor(ContextCompat.getColor(mufradContext, R.color.green))
            holder.ayah_tafseer.text = verse.tafseer
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setrukuinfo(holder: MufradatViewHolder, verse: MufradatEntity) {
        val surahInfo = StringBuilder()
        surahInfo.append(verse.surahName + ".")
        surahInfo.append(verse.surahNumber.toString() + ".")
        surahInfo.append(verse.ayahNumber)
        //   surahInfo.append( "");
        //  surahInfo.append(verse.getRukuSurahNumber());
        //  surahname,verseno,chapterno,chaperruku,surahrukuh
        //   getFormattedRukuInfo(verse.getSurahName(),verse.getAyahNumber(),verse.getParahNumber(),verse.getSurahNumber(),verse.getRukuParahNumber(),verse.getRukuSurahNumber())
        if (isMakkiMadani == 1) {
            holder.ruku_info_text.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_makkah_48,
                0,
                0,
                0
            )
            if (isNightmode == "dark") {
                holder.ruku_info_text.compoundDrawableTintList =
                    ColorStateList.valueOf(Color.GREEN)
            } else {
                holder.ruku_info_text.compoundDrawableTintList =
                    ColorStateList.valueOf(Color.BLACK)
            }
        } else {
            holder.ruku_info_text.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_madinah_48,
                0,
                0,
                0
            )

            if (isNightmode == "dark") {
                holder.ruku_info_text.compoundDrawableTintList =
                    ColorStateList.valueOf(Color.GREEN)
            } else {
                holder.ruku_info_text.compoundDrawableTintList = ColorStateList.valueOf(Color.BLACK)
            }
        }

        holder.ruku_info_text.text = surahInfo
    }


    //    holder.ruku_info_text.setText(String.valueOf(verse.getRuku()));
    private fun setStartText(holder: MufradatViewHolder, verse: MufradatEntity) {
        holder.starttext.text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ"
        if (verse.surahNumber == 1 || verse.surahNumber == 9 || verse.ayahNumber != 1) {
            holder.starttext.visibility = View.GONE
        }
    }

    private fun setAyahText(holder: MufradatViewHolder, verse: MufradatEntity) {
        arabic_font_selection =
            SharedPref.sharedPreferences.getString("Arabic_Font_Selection", "AlQalam.ttf")


        // holder.ayah_text.setTextSize(SharedPref.SeekarabicFontsize());
        val arabicFontSelection = arabic_font_selection
        holder.ayah_text.typeface =
            Typeface.createFromAsset(mufradContext.assets, arabicFontSelection)


        //  holder.ayah_text.setTextSize(Float.parseFloat(pref.getArabicTextFonts()));
        //   holder.ayah_text.setText(verse.getAyahTextQalam());
        holder.ayah_text.text = verse.ayahTextQalam


        //        if (SharedPref.arabicFontSelection().equals("Muhammadi.ttf")) {
//            holder.ayah_text.setLineSpacing(1.3F, 1.3F);
//        } else
//        if(SharedPref.arabicFontSelection().equals("AlQalam.ttf")){
//            holder.ayah_text.setLineSpacing(1.08F, 1.08F);
//        }
        //   holder.ayah_text.setTextSize(this.arabicFontSize.intValue());
    }

    private fun setTextViewStyles(holder: MufradatViewHolder) {
        //        colorwordfont = Typeface.createFromAsset(
//                QuranGrammarApplication.context!!.assets,
//                FONTS_LOCATION_PATH
//        )

        holder.ayah_text.typeface = Typeface.createFromAsset(mufradContext.assets, QURAN_FONTS)
        holder.ayah_tafseer.typeface = Typeface.createFromAsset(mufradContext.assets, URDU_FONTS)


        /// TODO: 23/8/20         holder.ayah_translation.setTypeface(this.urduFont);
        /// TODO: 23/8/20         holder.ayah_tafseer.setTypeface(this.urduFont);

        /*  if (SharedPref.urduFontSelection().equals("Mehr.TTF"))
        {
            holder.ayah_translation.setLineSpacing(1.07F, 1.07F);
            holder.ayah_tafseer.setLineSpacing(1.07F, 1.07F);
        }
        holder.ayah_translation.setTextSize(SharedPref.urduFontsize());
        holder.ayah_tafseer.setTextSize(SharedPref.urduFontsize());
        //   SharedPref.urduFontSelection();

        holder.ayah_translation.setTypeface(Typeface.createFromAsset(mufradContext.getAssets(), SharedPref.urduFontSelection()));
        holder.ayah_tafseer.setTypeface(Typeface.createFromAsset(mufradContext.getAssets(), SharedPref.urduFontSelection()));

        */
        //TODO
        /*
              if (this.applyLineSpacing) {
            holder.ayah_translation.setLineSpacing(1.07F, 1.07F);
            holder.ayah_tafseer.setLineSpacing(1.07F, 1.07F);
        }
        if (this.muhammadiFontSelected) {
            holder.ayah_text.setLineSpacing(1.3F, 1.3F);
            return;
        }
        if (this.qalamFontSelected)
            holder.ayah_text.setLineSpacing(1.08F, 1.08F);


        */
    }

    private fun setHtmlText(colorizedAyahWords: String) {
        var colorizedAyahWords: String? = colorizedAyahWords
        colorizedAyahWords =
            colorizedAyahWords?.replace("\n", "<br/>")?.replace("\\n", "<br/>") ?: ""
    }


    override fun getItemId(position: Int): Long {
        //  Surah surah = surahArrayList.get(position);

        return tafseeArrayList[position].surahNumber.toLong()
    }

    fun getItem(position: Int): Any {
        return tafseeArrayList[position]
    }

    override fun getItemCount(): Int {
        return tafseeArrayList.size
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    inner class MufradatViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        // Views in the layout
        val ayah_text: TextView = view.findViewById(R.id.ayah_text)
        val ayah_words: TextView = view.findViewById(R.id.ayah_words)
        val ayah_translation: TextView = view.findViewById(R.id.ayah_translation)
        val ayah_tafseer: TextView = view.findViewById(R.id.ayah_tafseer)
        val ruku_info_text: TextView = view.findViewById(R.id.ruku_info_text)

        // Initialize the views
        val ruku_info_text_separator: View = view.findViewById(R.id.ruku_info_text_separator)
        val starttext: TextView = view.findViewById(R.id.start_text)
        var tafseerCardView: MaterialCardView = view.findViewById(R.id.tafseer_card_view)
        var toggleIcon: ImageView = view.findViewById(R.id.toggle_icon)

        init {
            view.setOnClickListener(this)
            toggleIcon.setOnClickListener(this)
            toggleIcon.setOnClickListener {
                if (ayah_tafseer.visibility == View.GONE) {
                    ayah_tafseer.visibility = View.VISIBLE
                    // AnimationUtility.slide_down(context, erabexpand);
                    //AnimationUtility.AnimateArrow(90.0f, toggleIcon);
                } else {
                    ayah_tafseer.visibility = View.GONE
                    // AnimationUtility.AnimateArrow(0.0f, toggleIcon);
                    // Fader.slide_down(context, expandImageButton);
                }
            }
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(v, layoutPosition)
            }
        }
    }


    private fun getArabicVerseWwords(ayah_words: String) {
        val pattern = Pattern.compile("^(.+?):|(?<=\\|)(.*?)(?=\\:)")
        val m = pattern.matcher(ayah_words)

        var lastMatchPos = 0
        while (m.find()) {
            println("  " + m.group(1) + " - " + m.group(2))
            lastMatchPos = m.end()
        }
        if (lastMatchPos != ayah_words.length) {
            println("  Invalid string!")
        }
    }

    private fun getColorizedAyahWords(ayahwords: String, yellow: Int, green: Int): String {
        var ayahwords = ayahwords
        val stringBuilder2 = StringBuilder()
        stringBuilder2.append("<font color=\"")
        stringBuilder2.append(yellow)
        stringBuilder2.append("\">")
        val stringBuilder3 = StringBuilder()
        stringBuilder3.append("</font><font color=\"")
        stringBuilder3.append(green)
        stringBuilder3.append("\"> :")
        ayahwords = ayahwords.replace(":", stringBuilder3.toString())
        var stringBuilder1 = StringBuilder()
        stringBuilder1.append("</font><font color=\"")
        stringBuilder1.append(yellow)
        stringBuilder1.append("\">&nbsp;&nbsp;")
        ayahwords = ayahwords.replace("|", stringBuilder1.toString())
        stringBuilder1 = StringBuilder()
        stringBuilder1.append("</font><font color=\"")
        stringBuilder1.append(yellow)
        stringBuilder1.append("\">\n")
        stringBuilder2.append(
            ayahwords.replace("\n", stringBuilder1.toString()).replace("\n", "<br/>")
        )

        return stringBuilder2.toString()
    }


    companion object {
        fun getFormattedRukuInfo(
            paramInteger1: Int,
            paramInteger2: Int, paramString: String,
            paramInteger3: Int, paramInteger4: Int,
            paramInteger5: Int
        ): String {
            return String.format(
                "%s   %s  %d       %s %d       %s  %d       %s  %d       %s  %d", *arrayOf<Any>(
                    "سورۃ", paramString, paramInteger1, "آیت نمبر", paramInteger2, "پارہ",
                    paramInteger3, "پارہ   رکوع", paramInteger4, "سورۃ   رکوع",
                    paramInteger5
                )
            )
        }
    }
}
