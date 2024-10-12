package org.sj.conjugator.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mushafconsolidated.Entities.lysaEnt
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.databinding.LysalayoutBinding
import com.example.utility.QuranGrammarApplication
import org.sj.conjugator.interfaces.OnItemClickListener
import org.sj.conjugator.utilities.SharedPref

class LaysaAdapter(
    private var sarfSagheer: List<lysaEnt>,
    private val context: Context
) :
    RecyclerView.Adapter<LaysaAdapter.ViewHolder>() {
    var bookmarkpostion = 0
    var mItemClickListener: OnItemClickListener? = null
    private var arabicTypeface: Typeface? = null
    private var defaultfont = false
    private var aBoolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: LysalayoutBinding = LysalayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            QuranGrammarApplication.context!!
        )
        val arabic_font_selection =
            sharedPreferences.getString("Arabic_Font_Selection", "me_quran.ttf")
        arabicTypeface = Typeface.createFromAsset(
            context.assets,
            arabic_font_selection
        )



        val madhi=sarfSagheer
        madhi.get(0).grammar
        MadhiMaroof(madhi[position], holder)
        holder.wordDictionary.loadDataWithBaseURL(
            "file:///android_asset/",
            madhi.get(0).grammar,
            "text/html",
            "utf-8",
            null
        )

    }







    private fun FontSIzeSelection(holder: ViewHolder) {
        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            QuranGrammarApplication.context!!
        )
        val width = sharedPreferences.getString("width", "compactWidth")
        val arabicFontsize = sharedPreferences.getInt("pref_font_arabic_key", 20)
        if (!defaultfont) {
            if (width == "mediumWidth" || width == "expandedWidth") {
                //   final Integer arabicFontsize = SharedPref.arabicFontsize();

                holder.madhihua.textSize = arabicFontsize.toFloat()
                holder.madhihuma.textSize = arabicFontsize.toFloat()
                holder.madhihum.textSize = arabicFontsize.toFloat()
                holder.madhihia.textSize = arabicFontsize.toFloat()
                holder.madhihumaf.textSize = arabicFontsize.toFloat()
                holder.madhihunna.textSize = arabicFontsize.toFloat()
                holder.madhianta.textSize = arabicFontsize.toFloat()
                holder.madhiantuma.textSize = arabicFontsize.toFloat()
                holder.madhiantum.textSize = arabicFontsize.toFloat()
                holder.madhianti.textSize = arabicFontsize.toFloat()
                holder.madhiantunna.textSize = arabicFontsize.toFloat()
                holder.madhiantumaf.textSize = arabicFontsize.toFloat()
                holder.madhiana.textSize = arabicFontsize.toFloat()
                holder.madhinahnu.textSize = arabicFontsize.toFloat()

           /*     if (!aBoolean) {
                    holder.huaid!!.textSize = arabicFontsize.toFloat() //(array[0]);
                    holder.humamid!!.textSize = arabicFontsize.toFloat() //(array[1]);
                    holder.humid!!.textSize = arabicFontsize.toFloat() //(array[2]);
                    holder.hiaid!!.textSize = arabicFontsize.toFloat() //(array[3]);
                    holder.humafid!!.textSize = arabicFontsize.toFloat() //(array[4]);
                    holder.hunnaid!!.textSize = arabicFontsize.toFloat() //(array[5]);
                    holder.antaid!!.textSize = arabicFontsize.toFloat() //(array[6]);
                    holder.antumamid!!.textSize = arabicFontsize.toFloat() //(array[7]);
                    holder.antumid!!.textSize = arabicFontsize.toFloat() //(array[8]);
                    holder.antiid!!.textSize = arabicFontsize.toFloat() //(array[9]);
                    holder.antumafid!!.textSize = arabicFontsize.toFloat() //(array[10]);
                    holder.antunnaid!!.textSize = arabicFontsize.toFloat() //(array[11]);
                    holder.anaid!!.textSize = arabicFontsize.toFloat() //(array[12]);
                    holder.nahnuid!!.textSize = arabicFontsize.toFloat() //(array[13]);
                }*/
            }
        }
    }




    private fun MadhiMaroof(madhi: lysaEnt, holder: ViewHolder) {
        val hua: String
        val huma: String
        val hum: String
        val hia: String
        val humaf: String
        val hunna: String
        val anta: String
        val antuma: String
        val antum: String
        val anti: String
        val antumaf: String
        val antunna: String
        val ana: String
        val nahnu: String
        val sf = SharedPref(
            context
        )
        val isTraditional = SharedPref.GetSarfKabeerVerb()
        val arraypronouns = context.resources.getStringArray(R.array.arabicpronouns)
        var sb: StringBuilder
        if (isTraditional) {
            sb = StringBuilder()
            sb.append(arraypronouns[0])
            sb.append("-")
            sb.append(madhi.hua.toString().replace("[", "").replace("]", ""))//[0].toString())
            hua = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[1])
            sb.append("-")
            sb.append(madhi.huma.toString())//[1].toString())
            huma = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[2])
            sb.append("-")
            sb.append(madhi.hum.toString())//[2].toString())
            hum = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[3])
            sb.append("-")
            sb.append(madhi.hia.toString())//[3].toString())
            hia = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[4])
            sb.append("-")
            sb.append(madhi.humaf.toString())//[4].toString())
            humaf = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[5])
            sb.append("-")
            sb.append(madhi.hunna.toString())//[5].toString())
            hunna = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[6])
            sb.append("-")
            sb.append(madhi.anta.toString())//[6].toString())
            anta = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(madhi.antuma.toString())//[7].toString())
            antuma = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[8])
            sb.append("-")
            sb.append(madhi.antum.toString())//[8].toString())
            antum = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[9])
            sb.append("-")
            sb.append(madhi.anti.toString())//[9].toString())
            anti = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(madhi.antumaf.toString())//[7].toString())
            antumaf = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[11])
            sb.append("-")
            sb.append(madhi.antunna.toString())//[10].toString())
            antunna = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[12])
            sb.append("-")
            sb.append(madhi.ana.toString())//[11].toString())
            ana = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[13])
            sb.append("-")
            sb.append(madhi.nahnu.toString().replace("[", "").replace("]", ""))//[12].toString())
            nahnu = sb.toString()
        } else {
            hua = madhi.hua.toString().replace("[", "").replace("]", "")
            huma =madhi.huma.toString()
            hum = madhi.hum.toString()
            hia =madhi.hia.toString()//[position][3].toString()
            humaf =madhi.humaf.toString()//[position][4].toString()
            hunna =madhi.hunna.toString()//[position][5].toString()
            anta =madhi.anta.toString()//[position][6].toString()
            antuma =madhi.antuma.toString()//[position][7].toString()
            antum =madhi.antum.toString()//[position][8].toString()
            anti =madhi.anti.toString()//[position][9].toString()
            antumaf =madhi.antumaf.toString()//[position][7].toString()
            antunna =madhi.antunna.toString()//[position][10].toString()
            ana =madhi.ana.toString()//[position][11].toString()
            nahnu =madhi.nahnu.toString().replace("[", "").replace("]", "")//[position][12].toString()
        }
        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            context
        )
        val language = sharedPreferences.getString("lan", "en")
        val arrayheadings: Array<String> =
            if (language == "en") context.resources.getStringArray(R.array.enverbheadings) else {
                context.resources.getStringArray(R.array.arverbheadings)
            }

        holder.madhihua.typeface = arabicTypeface
        //        holder.madhihuma.setTypeface(arabicTypeface);
        holder.madhihum.typeface = arabicTypeface
        holder.madhihia.typeface = arabicTypeface
        holder.madhihumaf.typeface = arabicTypeface
        holder.madhihunna.typeface = arabicTypeface
        holder.madhianta.typeface = arabicTypeface
        holder.madhiantuma.typeface = arabicTypeface
        holder.madhiantum.typeface = arabicTypeface
        holder.madhianti.typeface = arabicTypeface
        holder.madhiantunna.typeface = arabicTypeface
        holder.madhiantumaf.typeface = arabicTypeface
        holder.madhiana.typeface = arabicTypeface
        holder.madhinahnu.typeface = arabicTypeface
        holder.madhihua.text = hua
        holder.madhihua.text = hua
        holder.madhihuma.text = huma
        holder.madhihum.text = hum
        holder.madhihia.text = hia
        holder.madhihumaf.text = humaf
        holder.madhihunna.text = hunna
        holder.madhianta.text = anta
        holder.madhiantuma.text = antuma
        holder.madhiantum.text = antum
        holder.madhianti.text = anti
        holder.madhiantumaf.text = antumaf
        holder.madhiantunna.text = antunna
        holder.madhiana.text = ana
        holder.madhinahnu.text = nahnu
    }

    override fun getItemId(position: Int): Long {
        //  Surah surah = surahArrayList.get(position);
        return sarfSagheer.size.toLong()
    }

    fun getItem(position: Int): Any {
        return sarfSagheer[position]
    }

    override fun getItemCount(): Int {
        //    return sarfSagheer.size();
        return 1
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    inner class  ViewHolder(binding: LysalayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //  public final TextView ayah_number;
        val madhihua: TextView
        val madhihuma: TextView
        val madhihum: TextView
        val madhihia: TextView
        val madhihumaf: TextView
        val madhihunna: TextView
        val madhianta: TextView
        val madhiantuma: TextView
        val madhiantum: TextView
        val madhianti: TextView
        val madhiantumaf: TextView
        val madhiantunna: TextView
        val madhiana: TextView
        val madhinahnu: TextView
        var wordDictionary: WebView

        init {
          //  pronouns = view.findViewById(R.id.pronouns)
           // pastactive = view.findViewById(R.id.pastactive)
            wordDictionary=binding.laysa
            madhihua = binding.madhihua
            madhihuma = binding.madhihuma
            madhihum = binding.madihum
            madhihia = binding.madhihia
            madhihumaf = binding.madhihumaf
            madhihunna = binding.madihunna
            madhianta = binding.madhianta
            madhiantuma = binding.madhiantuma
            madhiantum = binding.madhiantum
            madhianti = binding.madhianti
            madhiantumaf = binding.madhiantumaf
            madhiantunna = binding.madhiantunna
            madhiana = binding.madhiana
            madhinahnu = binding.madhinahnu

        }


    }
}