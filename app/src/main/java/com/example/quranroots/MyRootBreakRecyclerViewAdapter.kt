package com.example.quranroots

import android.preference.PreferenceManager
import android.text.SpannableString
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.RootWordDetails
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.WordMorphologyDetails
import com.example.mushafconsolidatedimport.Config
import com.example.utility.CorpusUtilityorig.Companion.NewSetWordSpan
import com.example.utility.QuranGrammarApplication
import com.example.utility.QuranGrammarApplication.Companion.context
import com.google.android.material.chip.Chip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils

import org.sj.conjugator.interfaces.OnItemClickListener

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyRootBreakRecyclerViewAdapter(
    private val corpusSurahWord: List<CorpusEntity>?,
    private val chapters: MutableLiveData<List<ChaptersAnaEntity>>
) :
    RecyclerView.Adapter<MyRootBreakRecyclerViewAdapter.ViewHolder>() {
    private lateinit var quranCorpusWbw: CorpusEntity
    private lateinit var wordDetails: RootWordDetails
    private lateinit var mItemClickListener: OnItemClickListener
    private var arabicfontSize: Int = 0
    private var translationfontsize: Int = 0
    private var defaultfont: Boolean = false

    init {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            context
        )
        sharedPreferences.getBoolean(Config.SHOW_Erab, Config.defaultShowErab)

        arabicfontSize = sharedPreferences.getInt("pref_font_arabic_key", 18)
        translationfontsize = sharedPreferences.getInt("pref_font_englsh_key", 18)
        defaultfont = sharedPreferences.getBoolean("default_font", true)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        //      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sarfkabeercolumn, parent, false);
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rec_arabicroot_detail, parent, false)
        //    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thulathisarfsagheer, parent, false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        quranCorpusWbw = corpusSurahWord!![position]
        val sb = StringBuilder()
        val spannableString = NewSetWordSpan(
            quranCorpusWbw.tagone,
            quranCorpusWbw.tagtwo,
            quranCorpusWbw.tagthree,
            quranCorpusWbw.tagfour,
            quranCorpusWbw.tagfive,
            quranCorpusWbw.araone!!,
            quranCorpusWbw.aratwo!!,
            quranCorpusWbw.arathree!!,
            quranCorpusWbw.arafour!!,
            quranCorpusWbw.arafive!!
        )

        //  sb.append(lughat.getSurah()).append("   ").append(lughat.getNamearabic()).append(lughat.getAyah()).append(" ").append(lughat.getArabic());
        sb.append(quranCorpusWbw.ayah).append("  ")
            .append(chapters.value!!.get(quranCorpusWbw.surah).namearabic).append("   ")
            .append(quranCorpusWbw.surah).append(" ").append(quranCorpusWbw.en)
        val sbs = SpannableString(sb)
        val charSequence = TextUtils.concat(spannableString, sb)
        // charSequence=TextUtils.concat(sb);
        //   sb.append(lughat.getSurah()).append(":").append(lughat.getAyah()).append(":").append(lughat.getArabic()).append("-").append(lughat.getAbjadname());
        holder.arabicroot_detail.text = charSequence
        setTextSizes(holder)
        /*
                holder.arabicroot_detail.setOnLongClickListener(View.OnLongClickListener {


                })*/
    }


    private fun setTextSizes(holder: ViewHolder) {
        if (!defaultfont) {
            holder.arabicroot_detail.textSize = arabicfontSize.toFloat()

        }
    }


    override fun getItemCount(): Int {
        return corpusSurahWord!!.size
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        if (mItemClickListener != null) {
            this.mItemClickListener = mItemClickListener
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener // current clickListerner
    {
        // public final ImageView id;
        val arabicroot_detail: Chip
        var cardview: CardView? = null

        init {
            view.tag = this
            itemView.setOnClickListener(this)
            //  id = view.findViewById(R.id.imgview);
            arabicroot_detail = view.findViewById(R.id.recarabicroot_detail)
            arabicroot_detail.tag = "root"
            arabicroot_detail.setOnClickListener(this)
            view.setOnClickListener(this)
            arabicroot_detail.setOnLongClickListener(View.OnLongClickListener {
                view
                val utils = Utils(QuranGrammarApplication.context!!)
                val verbCorpusRootWords =
                    utils.getQuranRoot(
                        quranCorpusWbw.surah,
                        quranCorpusWbw.ayah,
                        quranCorpusWbw.wordno
                    )
                if (verbCorpusRootWords!!.isNotEmpty() && verbCorpusRootWords[0]!!.tag == "V") {
                    //    vbdetail = ams.getVerbDetails();
                    print("check")
                }
                val corpusNounWord: List<NounCorpus> =
                    utils.getQuranNouns(
                        quranCorpusWbw.surah,
                        quranCorpusWbw.ayah,
                        quranCorpusWbw.wordno
                    )
                val verbCorpusRootWord: List<VerbCorpus> =
                    utils.getQuranRoot(
                        quranCorpusWbw.surah,
                        quranCorpusWbw.ayah,
                        quranCorpusWbw.wordno
                    )
                val qm = WordMorphologyDetails(
                    quranCorpusWbw,
                    corpusNounWord!!, verbCorpusRootWord!!
                )
                val sharedPreferences =
                    androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                        QuranGrammarApplication.context!!
                    )
                val isNightmode = sharedPreferences!!.getString("themepref", "dark").toString()
                val workBreakDown = qm.workBreakDown
                var color =
                    ContextCompat.getColor(
                        QuranGrammarApplication.context!!,
                        R.color.background_color_light_brown
                    )
                when (isNightmode) {
                    "dark", "blue", "green" -> color =
                        ContextCompat.getColor(
                            QuranGrammarApplication.context!!,
                            R.color.background_color
                        )

                    "brown" -> color =
                        ContextCompat.getColor(QuranGrammarApplication.context!!, R.color.neutral0)

                    "light" ->                             //  case "white":
                        color = ContextCompat.getColor(
                            QuranGrammarApplication.context!!,
                            R.color.background_color_light_brown
                        )
                }

                SimpleTooltip.Builder(QuranGrammarApplication.context!!)
                    .anchorView(view)
                    .text(workBreakDown)
                    .gravity(Gravity.TOP)
                    .modal(true)
                    .arrowDrawable(android.R.drawable.ic_media_previous)
                    .arrowHeight(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
                    .arrowWidth(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
                    .build()
                    .show()

                return@OnLongClickListener true
            })
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, layoutPosition)
            }
        }


    }
}