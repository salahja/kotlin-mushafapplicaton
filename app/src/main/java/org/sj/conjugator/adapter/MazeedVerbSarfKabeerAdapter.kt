package org.sj.conjugator.adapter

import android.content.Context
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mushafconsolidated.R
import com.example.utility.QuranGrammarApplication
import org.sj.conjugator.interfaces.OnItemClickListener
import org.sj.data.MazeedResult
import org.sj.verbConjugation.Amr
import org.sj.verbConjugation.MadhiMudharay
import org.sj.verbConjugation.NahiAmr


class MazeedVerbSarfKabeerAdapter(
    private var sarfSagheer: MazeedResult?,
    private val context: Context
) :
    RecyclerView.Adapter<MazeedVerbSarfKabeerAdapter.ViewHolder>() {
    var bookmarkpostion = 0
    var mItemClickListener: OnItemClickListener? = null
    private var arabicTypeface: Typeface? = null
    private var defaultfont = false
    private var aBoolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(
                QuranGrammarApplication.context
            )
        defaultfont = prefs.getBoolean("default_font", true)
        aBoolean = prefs.getBoolean("sarfkabeer_format_verb", true)
        val view: View = if (aBoolean) {
            LayoutInflater.from(parent.context)
             //   .inflate(R.layout.newverbsarfkabeertraditional, parent, false)
                .inflate(R.layout.vtwonewverbsarfkabeertraditional, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.verbscolumnkabeer, parent, false)
        }
        return ViewHolder(view)
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

        val madhimudhary =  sarfSagheer?.madhiMudharayList?.get(0)
        val madhimajhool =  sarfSagheer?.madhiMudharayList?.get(1)
        val mudharaymaroof =  sarfSagheer?.madhiMudharayList?.get(2)
        val mudharaymajhool =  sarfSagheer?.madhiMudharayList?.get(3)
        val madhi=madhimudhary


        val ismfael=sarfSagheer?.skabeerIsmList?.get(0)
        val ismmafool=sarfSagheer?.skabeerIsmList?.get(1)
        val amr=sarfSagheer?.amrList?.get(0)
        val nahiamr=sarfSagheer?.nahiAmrList?.get(0)



        this.MadhiMaroof(madhi as MadhiMudharay,holder)

   // this.MadhiMaroof(madhimudhary as MadhiMudharay,holder)
        improveMudhariMaroof(mudharaymaroof as MadhiMudharay,holder, arabicTypeface!!)
        MadhiMajhool(madhimajhool as MadhiMudharay,holder)
        improveMudhariMajhool(mudharaymajhool as MadhiMudharay,holder, arabicTypeface!!)

      Amar(amr as Amr,holder  )
   AmarNahi(nahiamr as NahiAmr,holder  )
        if (!aBoolean) {
            pronouns(holder)
        }
    }

    private fun pronouns(holder: ViewHolder) {
        val array = context.resources.getStringArray(R.array.arabicpronouns)
        holder.huaid!!.text = array[0]
        holder.humamid!!.text = array[1]
        holder.humid!!.text = array[2]
        holder.hiaid!!.text = array[3]
        holder.humafid!!.text = array[4]
        holder.hunnaid!!.text = array[5]
        holder.antaid!!.text = array[6]
        holder.antumamid!!.text = array[7]
        holder.antumid!!.text = array[8]
        holder.antiid!!.text = array[9]
        holder.antumafid!!.text = array[10]
        holder.antunnaid!!.text = array[11]
        holder.anaid!!.text = array[12]
        holder.nahnuid!!.text = array[13]
        holder.huaid.typeface = arabicTypeface //(array[0]);
        holder.humamid.typeface = arabicTypeface //(array[1]);
        holder.humid.typeface = arabicTypeface //(array[2]);
        holder.hiaid.typeface = arabicTypeface //(array[3]);
        holder.humafid.typeface = arabicTypeface //(array[4]);
        holder.hunnaid.typeface = arabicTypeface //(array[5]);
        holder.antaid.typeface = arabicTypeface //(array[6]);
        holder.antumamid.typeface = arabicTypeface //(array[7]);
        holder.antumid.typeface = arabicTypeface //(array[8]);
        holder.antiid.typeface = arabicTypeface //(array[9]);
        holder.antumafid.typeface = arabicTypeface //(array[10]);
        holder.antunnaid.typeface = arabicTypeface //(array[11]);
        holder.anaid.typeface = arabicTypeface //(array[12]);
        holder.nahnuid.typeface = arabicTypeface //(array[13]);
    }

    private fun AmarNahi(amr: NahiAmr, holder: ViewHolder  ) {
        val anta: String
        val antuma: String
        val antum: String
        val anti: String
        val antumaf: String
        val antunna: String
        val sf = SharedPref(
            context
        )
        val isTraditional = SharedPref.GetSarfKabeerVerb()
        val arraypronouns = context.resources.getStringArray(R.array.arabicpronouns)
        var sb: StringBuilder
        if (isTraditional) {
            sb = StringBuilder()
            sb.append(arraypronouns[6])
            sb.append("-")
            sb.append(amr.anta)
            anta = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(amr.antuma)
            antuma = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[8])
            sb.append("-")
            sb.append(amr.antum)
            antum = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[9])
            sb.append("-")
            sb.append(amr.anti)
            anti = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(amr.antumaf)
            antumaf = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[11])
            sb.append("-")
            sb.append(amr.antunna)
            antunna = sb.toString()
        } else {
            anta = amr.anta.toString()//[0].toString()
            antuma = amr.antuma.toString()//[2].toString()
            antum = amr.antum.toString()//[3].toString()
            anti = amr.anti.toString()//[1].toString()
            antumaf = amr.antumaf.toString()//[2].toString()
            antunna = amr.antunna.toString()//[4].toString()
        }
        holder.nahiamranta.typeface = arabicTypeface
        holder.nahiamrantuma.typeface = arabicTypeface
        holder.nahiamrantum.typeface = arabicTypeface
        holder.nahiamranti.typeface = arabicTypeface
        holder.nahiamrantumaf.typeface = arabicTypeface
        holder.nahiamrantunna.typeface = arabicTypeface
        holder.nahiamranta.text = anta
        holder.nahiamrantuma.text = antuma
        holder.nahiamrantum.text = antum
        holder.nahiamranti.text = anti
        holder.nahiamrantumaf.text = antumaf
        holder.nahiamrantunna.text = antunna
    }

    private fun Amar(amr: Amr, holder: ViewHolder  ) {
        val anta: String
        val antuma: String
        val antum: String
        val anti: String
        val antumaf: String
        val antunna: String
        val sf = SharedPref(
            context
        )
        val isTraditional = SharedPref.GetSarfKabeerVerb()
        val arraypronouns = context.resources.getStringArray(R.array.arabicpronouns)
        var sb: StringBuilder
        if (isTraditional) {
            sb = StringBuilder()
            sb.append(arraypronouns[6])
            sb.append("-")
            sb.append(amr.anta)
            anta = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(amr.antuma)
            antuma = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[8])
            sb.append("-")
            sb.append(amr.antum)
            antum = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[9])
            sb.append("-")
            sb.append(amr.anti)
            anti = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(amr.antumaf)
            antumaf = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[11])
            sb.append("-")
            sb.append(amr.antunna)
            antunna = sb.toString()
        } else {
            anta = amr.anta.toString()//[0].toString()
            antuma = amr.antuma.toString()//[2].toString()
            antum = amr.antum.toString()//[3].toString()
            anti = amr.anti.toString()//[1].toString()
            antumaf = amr.antumaf.toString()//[2].toString()
            antunna = amr.antunna.toString()//[4].toString()
        }
        holder.amranta.typeface = arabicTypeface
        holder.amrantuma.typeface = arabicTypeface
        holder.amrantum.typeface = arabicTypeface
        holder.amranti.typeface = arabicTypeface
        holder.amrantumaf.typeface = arabicTypeface
        holder.amrantunna.typeface = arabicTypeface
        holder.amranta.text = anta
        holder.amrantuma.text = antuma
        holder.amrantum.text = antum
        holder.amranti.text = anti
        holder.amrantumaf.text = antumaf
        holder.amrantunna.text = antunna
    }



    class SharedPref(context: Context) {
        companion object {
            fun GetSarfKabeerVerb(): Boolean {
                // Replace this with actual implementation to get the value from Shared Preferences
                return true // Example: Assuming true for traditional conjugation
            }
        }
    }




    fun improveMudhariMajhool(
        imperfectPassiveConjugation: MadhiMudharay,
        viewHolder: ViewHolder,

        arabicTypeface: Typeface
    ) {
        val isTraditionalConjugation = SharedPref.GetSarfKabeerVerb()
        val pronounList = context.resources.getStringArray(R.array.arabicpronouns)

        // Helper function to format the conjugation string
        fun formatConjugation(pronoun: String, conjugation: Any): String {
            val formattedConjugation = conjugation.toString().removeSurrounding("[", "]")
            return if (isTraditionalConjugation) "$pronoun-$formattedConjugation" else formattedConjugation
        }

        // Create a map to hold the conjugations and their corresponding text views
        val conjugationsMap = mapOf(
            "masculineSingularThirdPerson" to Pair(pronounList[0], imperfectPassiveConjugation.hua),
            "masculineDualThirdPerson" to Pair(pronounList[1], imperfectPassiveConjugation.huma),
            "masculinePluralThirdPerson" to Pair(pronounList[2], imperfectPassiveConjugation.hum),
            "feminineSingularThirdPerson" to Pair(pronounList[3], imperfectPassiveConjugation.hia),
            "feminineDualThirdPerson" to Pair(pronounList[4], imperfectPassiveConjugation.humaf),
            "femininePluralThirdPerson" to Pair(pronounList[5], imperfectPassiveConjugation.hunna),
            "masculineSingularSecondPerson" to Pair(pronounList[6], imperfectPassiveConjugation.anta),
            "masculineDualSecondPerson" to Pair(pronounList[7], imperfectPassiveConjugation.antuma),
            "masculinePluralSecondPerson" to Pair(pronounList[8], imperfectPassiveConjugation.antum),
            "feminineSingularSecondPerson" to Pair(pronounList[9], imperfectPassiveConjugation.anti),
            "feminineDualSecondPerson" to Pair(pronounList[7], imperfectPassiveConjugation.antumaf),
            "femininePluralSecondPerson" to Pair(pronounList[11], imperfectPassiveConjugation.antunna),
            "firstPersonSingular" to Pair(pronounList[12], imperfectPassiveConjugation.ana),
            "firstPersonPlural" to Pair(pronounList[13], imperfectPassiveConjugation.nahnu)
        )

        // Map of TextViews to their corresponding keys in the conjugationsMap
        val textViewMap = mapOf(
            viewHolder.muzmajhua to "masculineSingularThirdPerson",
            viewHolder.muzmajhuma to "masculineDualThirdPerson",
            viewHolder.muzmajhum to "masculinePluralThirdPerson",
            viewHolder.muzmajhia to "feminineSingularThirdPerson",
            viewHolder.muzmajhumaf to "feminineDualThirdPerson",
            viewHolder.muzmajhunna to "femininePluralThirdPerson",
            viewHolder.muzmajanta to "masculineSingularSecondPerson",
            viewHolder.muzmajantuma to "masculineDualSecondPerson",
            viewHolder.muzmajantum to "masculinePluralSecondPerson",
            viewHolder.muzmajanti to "feminineSingularSecondPerson",
            viewHolder.muzmajantumaf to "feminineDualSecondPerson",
            viewHolder.muzmajantunna to "femininePluralSecondPerson",
            viewHolder.muzmajana to "firstPersonSingular",
            viewHolder.muzmajnahnu to "firstPersonPlural"
        )

        // Iterate through the map and set the text and typeface
        textViewMap.forEach { (textView, key) ->
            textView.typeface = arabicTypeface
            val (pronoun, conjugation) = conjugationsMap[key] ?: Pair("", "")
            textView.text = conjugation?.let { formatConjugation(pronoun, it) }
        }

        FontSIzeSelection(viewHolder)
    }
    private fun MudhariMajhool(imperfectActivePassiveConjugation: MadhiMudharay, viewHolder: ViewHolder, position: Int) {
        val masculineSingularThirdPerson: String
        val masculineDualThirdPerson: String
        val masculinePluralThirdPerson: String
        val feminineSingularThirdPerson: String
        val feminineDualThirdPerson: String
        val femininePluralThirdPerson: String
        val masculineSingularSecondPerson: String
        val masculineDualSecondPerson: String
        val masculinePluralSecondPerson: String
        val feminineSingularSecondPerson: String
        val feminineDualSecondPerson: String
        val femininePluralSecondPerson: String
        val firstPersonSingular: String
        val firstPersonPlural: String
        val sharedPreferences = SharedPref(
            context
        )
        val isTraditionalConjugation = SharedPref.GetSarfKabeerVerb()
        val pronounList = context.resources.getStringArray(R.array.arabicpronouns)
        var conjugationBuilder: StringBuilder
        if (isTraditionalConjugation) {
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[0])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.hua.toString().replace("[", "").replace("]", ""))//[0].toString())
            masculineSingularThirdPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[1])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.huma.toString())//[1].toString())
            masculineDualThirdPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[2])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.hum.toString())//[2].toString())
            masculinePluralThirdPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[3])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.hia.toString())//[3].toString())
            feminineSingularThirdPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[4])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.humaf.toString())//[4].toString())
            feminineDualThirdPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[5])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.hunna.toString())//[5].toString())
            femininePluralThirdPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[6])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.anta.toString())//[6].toString())
            masculineSingularSecondPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[7])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.antuma.toString())//[7].toString())
            masculineDualSecondPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[8])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.antum.toString())//[8].toString())
            masculinePluralSecondPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[9])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.anti.toString())//[9].toString())
            feminineSingularSecondPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[7])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.antumaf.toString())//[7].toString())
            feminineDualSecondPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[11])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.antunna.toString())//[10].toString())
            femininePluralSecondPerson = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[12])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.ana.toString())//[11].toString())
            firstPersonSingular = conjugationBuilder.toString()
            conjugationBuilder = StringBuilder()
            conjugationBuilder.append(pronounList[13])
            conjugationBuilder.append("-")
            conjugationBuilder.append(imperfectActivePassiveConjugation.nahnu.toString().replace("[", "").replace("]", ""))//[12].toString())
            firstPersonPlural = conjugationBuilder.toString()
        } else {
            masculineSingularThirdPerson = imperfectActivePassiveConjugation.hua.toString().replace("[", "").replace("]", "")
            masculineDualThirdPerson =imperfectActivePassiveConjugation.huma.toString()
            masculinePluralThirdPerson = imperfectActivePassiveConjugation.hum.toString()
            feminineSingularThirdPerson =imperfectActivePassiveConjugation.hia.toString()//[position][3].toString()
            feminineDualThirdPerson =imperfectActivePassiveConjugation.humaf.toString()//[position][4].toString()
            femininePluralThirdPerson =imperfectActivePassiveConjugation.hunna.toString()//[position][5].toString()
            masculineSingularSecondPerson =imperfectActivePassiveConjugation.anta.toString()//[position][6].toString()
            masculineDualSecondPerson =imperfectActivePassiveConjugation.antuma.toString()//[position][7].toString()
            masculinePluralSecondPerson =imperfectActivePassiveConjugation.antum.toString()//[position][8].toString()
            feminineSingularSecondPerson =imperfectActivePassiveConjugation.anti.toString()//[position][9].toString()
            feminineDualSecondPerson =imperfectActivePassiveConjugation.antumaf.toString()//[position][7].toString()
            femininePluralSecondPerson =imperfectActivePassiveConjugation.antunna.toString()//[position][10].toString()
            firstPersonSingular =imperfectActivePassiveConjugation.ana.toString()//[position][11].toString()
            firstPersonPlural =imperfectActivePassiveConjugation.nahnu.toString().replace("[", "").replace("]", "")//[position][12].toString()
        }
        //     FontSIzeSelection(viewHolder);
//ismfaile
        //   SharedPref.arabicFontSelection();
        viewHolder.muzmajhua.typeface = arabicTypeface
        viewHolder.muzmajhuma.typeface = arabicTypeface
        viewHolder.muzmajhum.typeface = arabicTypeface
        viewHolder.muzmajhia.typeface = arabicTypeface
        viewHolder.muzmajhumaf.typeface = arabicTypeface
        viewHolder.muzmajhunna.typeface = arabicTypeface
        viewHolder.muzmajanta.typeface = arabicTypeface
        viewHolder.muzmajantuma.typeface = arabicTypeface
        viewHolder.muzmajantum.typeface = arabicTypeface
        viewHolder.muzmajanti.typeface = arabicTypeface
        viewHolder.muzmajantumaf.typeface = arabicTypeface
        viewHolder.muzmajantunna.typeface = arabicTypeface
        viewHolder.muzmajana.typeface = arabicTypeface
        viewHolder.muzmajnahnu.typeface = arabicTypeface
        viewHolder.muzmajhua.text = masculineSingularThirdPerson
        viewHolder.muzmajhuma.text = masculineDualThirdPerson
        viewHolder.muzmajhum.text = masculinePluralThirdPerson
        viewHolder.muzmajhia.text = feminineSingularThirdPerson
        viewHolder.muzmajhumaf.text = feminineDualThirdPerson
        viewHolder.muzmajhunna.text = femininePluralThirdPerson
        viewHolder.muzmajanta.text = masculineSingularSecondPerson
        viewHolder.muzmajantuma.text = masculineDualSecondPerson
        viewHolder.muzmajantum.text = masculinePluralSecondPerson
        viewHolder.muzmajanti.text = feminineSingularSecondPerson
        viewHolder.muzmajantumaf.text = feminineDualSecondPerson
        viewHolder.muzmajantunna.text = femininePluralSecondPerson
        viewHolder.muzmajana.text = firstPersonSingular
        //
        viewHolder.muzmajnahnu.text = firstPersonPlural
        FontSIzeSelection(viewHolder)
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
                holder.muzhua.textSize = arabicFontsize.toFloat()
                holder.muzhuma.textSize = arabicFontsize.toFloat()
                holder.muzhum.textSize = arabicFontsize.toFloat()
                holder.muzhia.textSize = arabicFontsize.toFloat()
                holder.muzhumaf.textSize = arabicFontsize.toFloat()
                holder.muzhunna.textSize = arabicFontsize.toFloat()
                holder.muzanta.textSize = arabicFontsize.toFloat()
                holder.muzantuma.textSize = arabicFontsize.toFloat()
                holder.muzantum.textSize = arabicFontsize.toFloat()
                holder.muzanti.textSize = arabicFontsize.toFloat()
                holder.muzantumaf.textSize = arabicFontsize.toFloat()
                holder.muzantunna.textSize = arabicFontsize.toFloat()
                holder.muzana.textSize = arabicFontsize.toFloat()
                holder.muznahnu.textSize = arabicFontsize.toFloat()
                holder.muzmajhua.textSize = arabicFontsize.toFloat()
                holder.muzmajhuma.textSize = arabicFontsize.toFloat()
                holder.muzmajhum.textSize = arabicFontsize.toFloat()
                holder.muzmajhia.textSize = arabicFontsize.toFloat()
                holder.muzmajhumaf.textSize = arabicFontsize.toFloat()
                holder.muzmajhunna.textSize = arabicFontsize.toFloat()
                holder.muzmajanta.textSize = arabicFontsize.toFloat()
                holder.muzmajantuma.textSize = arabicFontsize.toFloat()
                holder.muzmajantum.textSize = arabicFontsize.toFloat()
                holder.muzmajanti.textSize = arabicFontsize.toFloat()
                holder.muzmajantumaf.textSize = arabicFontsize.toFloat()
                holder.muzmajantunna.textSize = arabicFontsize.toFloat()
                holder.muzmajana.textSize = arabicFontsize.toFloat()
                holder.muzmajnahnu.textSize = arabicFontsize.toFloat()
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
                holder.madimajhua.textSize = arabicFontsize.toFloat()
                holder.madimajhuma.textSize = arabicFontsize.toFloat()
                holder.madimajhum.textSize = arabicFontsize.toFloat()
                holder.madimajhia.textSize = arabicFontsize.toFloat()
                holder.madimajhumaf.textSize = arabicFontsize.toFloat()
                holder.madimajhunna.textSize = arabicFontsize.toFloat()
                holder.madimajanta.textSize = arabicFontsize.toFloat()
                holder.madimajantuma.textSize = arabicFontsize.toFloat()
                holder.madimajantum.textSize = arabicFontsize.toFloat()
                holder.madimajanti.textSize = arabicFontsize.toFloat()
                holder.madimajantumaf.textSize = arabicFontsize.toFloat()
                holder.madimajantunna.textSize = arabicFontsize.toFloat()
                holder.madimajana.textSize = arabicFontsize.toFloat()
                holder.madimajnahnu.textSize = arabicFontsize.toFloat()
                holder.amranta.textSize = arabicFontsize.toFloat()
                holder.amrantuma.textSize = arabicFontsize.toFloat()
                holder.amrantum.textSize = arabicFontsize.toFloat()
                holder.amranti.textSize = arabicFontsize.toFloat()
                holder.amrantumaf.textSize = arabicFontsize.toFloat()
                holder.amrantunna.textSize = arabicFontsize.toFloat()
                holder.nahiamranta.textSize = arabicFontsize.toFloat()
                holder.nahiamrantuma.textSize = arabicFontsize.toFloat()
                holder.nahiamrantum.textSize = arabicFontsize.toFloat()
                holder.nahiamranti.textSize = arabicFontsize.toFloat()
                holder.nahiamrantumaf.textSize = arabicFontsize.toFloat()
                holder.nahiamrantunna.textSize = arabicFontsize.toFloat()
                if (!aBoolean) {
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
                }
            }
        }
    }

    private fun MadhiMajhool(madhimajhool : MadhiMudharay,   holder: ViewHolder ) {
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
            sb.append(madhimajhool.hua.toString().replace("[", "").replace("]", ""))//[0].toString())
            hua = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[1])
            sb.append("-")
            sb.append(madhimajhool.huma.toString())//[1].toString())
            huma = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[2])
            sb.append("-")
            sb.append(madhimajhool.hum.toString())//[2].toString())
            hum = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[3])
            sb.append("-")
            sb.append(madhimajhool.hia.toString())//[3].toString())
            hia = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[4])
            sb.append("-")
            sb.append(madhimajhool.humaf.toString())//[4].toString())
            humaf = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[5])
            sb.append("-")
            sb.append(madhimajhool.hunna.toString())//[5].toString())
            hunna = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[6])
            sb.append("-")
            sb.append(madhimajhool.anta.toString())//[6].toString())
            anta = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(madhimajhool.antuma.toString())//[7].toString())
            antuma = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[8])
            sb.append("-")
            sb.append(madhimajhool.antum.toString())//[8].toString())
            antum = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[9])
            sb.append("-")
            sb.append(madhimajhool.anti.toString())//[9].toString())
            anti = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[7])
            sb.append("-")
            sb.append(madhimajhool.antumaf.toString())//[7].toString())
            antumaf = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[11])
            sb.append("-")
            sb.append(madhimajhool.antunna.toString())//[10].toString())
            antunna = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[12])
            sb.append("-")
            sb.append(madhimajhool.ana.toString())//[11].toString())
            ana = sb.toString()
            sb = StringBuilder()
            sb.append(arraypronouns[13])
            sb.append("-")
            sb.append(madhimajhool.nahnu.toString().replace("[", "").replace("]", ""))//[12].toString())
            nahnu = sb.toString()
        } else {
            hua = madhimajhool.hua.toString().replace("[", "").replace("]", "")
            huma =madhimajhool.huma.toString()
            hum = madhimajhool.hum.toString()
            hia =madhimajhool.hia.toString()//[position][3].toString()
            humaf =madhimajhool.humaf.toString()//[position][4].toString()
            hunna =madhimajhool.hunna.toString()//[position][5].toString()
            anta =madhimajhool.anta.toString()//[position][6].toString()
            antuma =madhimajhool.antuma.toString()//[position][7].toString()
            antum =madhimajhool.antum.toString()//[position][8].toString()
            anti =madhimajhool.anti.toString()//[position][9].toString()
            antumaf =madhimajhool.antumaf.toString()//[position][7].toString()
            antunna =madhimajhool.antunna.toString()//[position][10].toString()
            ana =madhimajhool.ana.toString()//[position][11].toString()
            nahnu =madhimajhool.nahnu.toString().replace("[", "").replace("]", "")//[position][12].toString()
        }
        holder.madimajhua.typeface = arabicTypeface
        holder.madimajhuma.typeface = arabicTypeface
        holder.madimajhum.typeface = arabicTypeface
        holder.madimajhia.typeface = arabicTypeface
        holder.madimajhumaf.typeface = arabicTypeface
        holder.madimajhunna.typeface = arabicTypeface
        holder.madimajanta.typeface = arabicTypeface
        holder.madimajantuma.typeface = arabicTypeface
        holder.madimajantum.typeface = arabicTypeface
        holder.madimajanti.typeface = arabicTypeface
        holder.madimajantumaf.typeface = arabicTypeface
        holder.madimajantunna.typeface = arabicTypeface
        holder.madimajana.typeface = arabicTypeface
        holder.madimajnahnu.typeface = arabicTypeface
        holder.madimajhua.text = hua
        holder.madimajhuma.text = huma
        holder.madimajhum.text = hum
        holder.madimajhia.text = hia
        holder.madimajhumaf.text = humaf
        holder.madimajhunna.text = hunna
        holder.madimajanta.text = anta
        holder.madimajantuma.text = antuma
        holder.madimajantum.text = antum
        holder.madimajanti.text = anti
        holder.madimajantumaf.text = antumaf
        holder.madimajantunna.text = antunna
        holder.madimajana.text = ana
        holder.madimajnahnu.text = nahnu
    }

    private fun MudhariMaroof(imperfectActiveVoice: MadhiMudharay, holder: ViewHolder, position: Int) {
        val heForm: String
        val theyTwoMaleForm: String
        val theyMaleForm: String
        val hia: String
        val theyTwoFemaleForm: String
        val theyFemaleForm: String
        val youMaleForm: String
        val youTwoMaleForm: String
        val youMalePluralForm: String
        val youFemaleForm: String
        val youTwoFemaleForm: String
        val youFemalePluralForm: String
        val ana: String
        val weForm: String
        val preferences = SharedPref(
            context
        )
        val isKabeerVerb = SharedPref.GetSarfKabeerVerb()
        val pronouns = context.resources.getStringArray(R.array.arabicpronouns)
        var stringBuilder: StringBuilder
        if (isKabeerVerb) {
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[0])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.hua.toString().replace("[", "").replace("]", ""))//[0].toString())
            heForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[1])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.huma.toString())//[1].toString())
            theyTwoMaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[2])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.hum.toString())//[2].toString())
            theyMaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[3])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.hia.toString())//[3].toString())
            hia = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[4])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.humaf.toString())//[4].toString())
            theyTwoFemaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[5])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.hunna.toString())//[5].toString())
            theyFemaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[6])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.anta.toString())//[6].toString())
            youMaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[7])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.antuma.toString())//[7].toString())
            youTwoMaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[8])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.antum.toString())//[8].toString())
            youMalePluralForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[9])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.anti.toString())//[9].toString())
            youFemaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[7])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.antumaf.toString())//[7].toString())
            youTwoFemaleForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[11])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.antunna.toString())//[10].toString())
            youFemalePluralForm = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[12])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.ana.toString())//[11].toString())
            ana = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[13])
            stringBuilder.append("-")
            stringBuilder.append(imperfectActiveVoice.nahnu.toString().replace("[", "").replace("]", ""))//[12].toString())
            weForm = stringBuilder.toString()
        } else {
            heForm = imperfectActiveVoice.hua.toString().replace("[", "").replace("]", "")
            theyTwoMaleForm =imperfectActiveVoice.huma.toString()
            theyMaleForm = imperfectActiveVoice.hum.toString()
            hia =imperfectActiveVoice.hia.toString()//[position][3].toString()
            theyTwoFemaleForm =imperfectActiveVoice.humaf.toString()//[position][4].toString()
            theyFemaleForm =imperfectActiveVoice.hunna.toString()//[position][5].toString()
            youMaleForm =imperfectActiveVoice.anta.toString()//[position][6].toString()
            youTwoMaleForm =imperfectActiveVoice.antuma.toString()//[position][7].toString()
            youMalePluralForm =imperfectActiveVoice.antum.toString()//[position][8].toString()
            youFemaleForm =imperfectActiveVoice.anti.toString()//[position][9].toString()
            youTwoFemaleForm =imperfectActiveVoice.antumaf.toString()//[position][7].toString()
            youFemalePluralForm =imperfectActiveVoice.antunna.toString()//[position][10].toString()
            ana =imperfectActiveVoice.ana.toString()//[position][11].toString()
            weForm =imperfectActiveVoice.nahnu.toString().replace("[", "").replace("]", "")//[position][12].toString()
        }
        holder.muzhua.typeface = arabicTypeface
        holder.muzhuma.typeface = arabicTypeface
        holder.muzhum.typeface = arabicTypeface
        holder.muzhia.typeface = arabicTypeface
        holder.muzhumaf.typeface = arabicTypeface
        holder.muzhunna.typeface = arabicTypeface
        holder.muzanta.typeface = arabicTypeface
        holder.muzantuma.typeface = arabicTypeface
        holder.muzantum.typeface = arabicTypeface
        holder.muzanti.typeface = arabicTypeface
        holder.muzantumaf.typeface = arabicTypeface
        holder.muzantunna.typeface = arabicTypeface
        holder.muzana.typeface = arabicTypeface
        holder.muznahnu.typeface = arabicTypeface
        holder.muzhua.text = heForm
        holder.muzhuma.text = theyTwoMaleForm
        holder.muzhum.text = theyMaleForm
        holder.muzhia.text = hia
        holder.muzhumaf.text = theyTwoFemaleForm
        holder.muzhunna.text = theyFemaleForm
        holder.muzanta.text = youMaleForm
        holder.muzantuma.text = youTwoMaleForm
        holder.muzantum.text = youMalePluralForm
        holder.muzanti.text = youFemaleForm
        holder.muzantumaf.text = youTwoFemaleForm
        holder.muzantunna.text = youFemalePluralForm
        holder.muzana.text = ana
        holder.muznahnu.text = weForm
    }






    fun improveMudhariMaroof(
        imperfectActiveVoice: MadhiMudharay,
        holder: ViewHolder,

        arabicTypeface: Typeface
    ) {
        val isTraditionalConjugation = SharedPref.GetSarfKabeerVerb()
        val pronouns = context.resources.getStringArray(R.array.arabicpronouns)

        // Helper function to format the conjugation string
        fun formatConjugation(pronoun: String, conjugation: Any): String {
            val formattedConjugation = conjugation.toString().removeSurrounding("[", "]")
            return if (isTraditionalConjugation) "$pronoun-$formattedConjugation" else formattedConjugation
        }

        // Create a map to hold the conjugations and their corresponding text views
        val conjugationsMap = mapOf(
            "heForm" to Pair(pronouns[0], imperfectActiveVoice.hua),
            "theyTwoMaleForm" to Pair(pronouns[1], imperfectActiveVoice.huma),
            "theyMaleForm" to Pair(pronouns[2], imperfectActiveVoice.hum),
            "hia" to Pair(pronouns[3], imperfectActiveVoice.hia),
            "theyTwoFemaleForm" to Pair(pronouns[4], imperfectActiveVoice.humaf),
            "theyFemaleForm" to Pair(pronouns[5], imperfectActiveVoice.hunna),
            "youMaleForm" to Pair(pronouns[6], imperfectActiveVoice.anta),
            "youTwoMaleForm" to Pair(pronouns[7], imperfectActiveVoice.antuma),
            "youMalePluralForm" to Pair(pronouns[8], imperfectActiveVoice.antum),
            "youFemaleForm" to Pair(pronouns[9], imperfectActiveVoice.anti),
            "youTwoFemaleForm" to Pair(pronouns[7], imperfectActiveVoice.antumaf),
            "youFemalePluralForm" to Pair(pronouns[11], imperfectActiveVoice.antunna),
            "ana" to Pair(pronouns[12], imperfectActiveVoice.ana),
            "weForm" to Pair(pronouns[13], imperfectActiveVoice.nahnu)
        )

        // Map of TextViews to their corresponding keys in the conjugationsMap
        val textViewMap = mapOf(
            holder.muzhua to "heForm",
            holder.muzhuma to "theyTwoMaleForm",
            holder.muzhum to "theyMaleForm",
            holder.muzhia to "hia",
            holder.muzhumaf to "theyTwoFemaleForm",
            holder.muzhunna to "theyFemaleForm",
            holder.muzanta to "youMaleForm",
            holder.muzantuma to "youTwoMaleForm",
            holder.muzantum to "youMalePluralForm",
            holder.muzanti to "youFemaleForm",
            holder.muzantumaf to "youTwoFemaleForm",
            holder.muzantunna to "youFemalePluralForm",
            holder.muzana to "ana",
            holder.muznahnu to "weForm"
        )

        // Iterate through the map and set the text and typeface
        textViewMap.forEach { (textView, key) ->
            textView.typeface = arabicTypeface
            val (pronoun, conjugation) = conjugationsMap[key] ?: Pair("", "")
            textView.text = conjugation?.let { formatConjugation(pronoun, it) }
        }
    }

    private fun MadhiMaroof(verbConjugation: MadhiMudharay, viewContainer: ViewHolder) {
        val thirdPersonMasculineSingular: String
        val thirdPersonMascul: String
        val thirdPersonMasculinePlural: String
        val thirdPersonFeminineSingular: String
        val thirdPersonFeminineDual: String
        val thirdPersonFemininePlural: String
        val secondPersonMasculineSingular: String
        val secondPersonMasculineDual: String
        val secondPersonMasculinePlural: String
        val secondPersonFeminineSingular: String
        val secondPersonFeminineDual: String
        val secondPersonFemininePlural: String
        val firstPersonSingular: String
        val firstPersonPlural: String
        val sharedPreferences = SharedPref(
            context
        )
        val isTraditionalMode = SharedPref.GetSarfKabeerVerb()
        val pronouns = context.resources.getStringArray(R.array.arabicpronouns)
        var stringBuilder: StringBuilder
        if (isTraditionalMode) {
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[0])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.hua.toString().replace("[", "").replace("]", ""))//[0].toString())
            thirdPersonMasculineSingular = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[1])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.huma.toString())//[1].toString())
            thirdPersonMascul = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[2])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.hum.toString())//[2].toString())
            thirdPersonMasculinePlural = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[3])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.hia.toString())//[3].toString())
            thirdPersonFeminineSingular = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[4])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.humaf.toString())//[4].toString())
            thirdPersonFeminineDual = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[5])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.hunna.toString())//[5].toString())
            thirdPersonFemininePlural = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[6])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.anta.toString())//[6].toString())
            secondPersonMasculineSingular = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[7])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.antuma.toString())//[7].toString())
            secondPersonMasculineDual = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[8])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.antum.toString())//[8].toString())
            secondPersonMasculinePlural = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[9])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.anti.toString())//[9].toString())
            secondPersonFeminineSingular = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[7])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.antumaf.toString())//[7].toString())
            secondPersonFeminineDual = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[11])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.antunna.toString())//[10].toString())
            secondPersonFemininePlural = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[12])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.ana.toString())//[11].toString())
            firstPersonSingular = stringBuilder.toString()
            stringBuilder = StringBuilder()
            stringBuilder.append(pronouns[13])
            stringBuilder.append("-")
            stringBuilder.append(verbConjugation.nahnu.toString().replace("[", "").replace("]", ""))//[12].toString())
            firstPersonPlural = stringBuilder.toString()
        } else {
            thirdPersonMasculineSingular = verbConjugation.hua.toString().replace("[", "").replace("]", "")
            thirdPersonMascul =verbConjugation.huma.toString()
            thirdPersonMasculinePlural = verbConjugation.hum.toString()
            thirdPersonFeminineSingular =verbConjugation.hia.toString()//[position][3].toString()
            thirdPersonFeminineDual =verbConjugation.humaf.toString()//[position][4].toString()
            thirdPersonFemininePlural =verbConjugation.hunna.toString()//[position][5].toString()
            secondPersonMasculineSingular =verbConjugation.anta.toString()//[position][6].toString()
            secondPersonMasculineDual =verbConjugation.antuma.toString()//[position][7].toString()
            secondPersonMasculinePlural =verbConjugation.antum.toString()//[position][8].toString()
            secondPersonFeminineSingular =verbConjugation.anti.toString()//[position][9].toString()
            secondPersonFeminineDual =verbConjugation.antumaf.toString()//[position][7].toString()
            secondPersonFemininePlural =verbConjugation.antunna.toString()//[position][10].toString()
            firstPersonSingular =verbConjugation.ana.toString()//[position][11].toString()
            firstPersonPlural =verbConjugation.nahnu.toString().replace("[", "").replace("]", "")//[position][12].toString()
        }
        val userPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
            context
        )
        val userLanguage = userPreferences.getString("lan", "en")
        val conjugationHeadings: Array<String> =
            if (userLanguage == "en") context.resources.getStringArray(R.array.enverbheadings) else {
                context.resources.getStringArray(R.array.arverbheadings)
            }
        //    viewContainer.pronouns.setText(array[0]);
        viewContainer.pastactive.text = pronouns[1]
        viewContainer.presentactive.text = pronouns[2]
        viewContainer.pastpassive.text = pronouns[3]
        viewContainer.presentpassive.text = pronouns[4]
        viewContainer.command.text = pronouns[5]
        viewContainer.negcommand.text = pronouns[6]
        viewContainer.madhihua.typeface = arabicTypeface
        //        viewContainer.madhihuma.setTypeface(arabicTypeface);
        viewContainer.madhihum.typeface = arabicTypeface
        viewContainer.madhihia.typeface = arabicTypeface
        viewContainer.madhihumaf.typeface = arabicTypeface
        viewContainer.madhihunna.typeface = arabicTypeface
        viewContainer.madhianta.typeface = arabicTypeface
        viewContainer.madhiantuma.typeface = arabicTypeface
        viewContainer.madhiantum.typeface = arabicTypeface
        viewContainer.madhianti.typeface = arabicTypeface
        viewContainer.madhiantunna.typeface = arabicTypeface
        viewContainer.madhiantumaf.typeface = arabicTypeface
        viewContainer.madhiana.typeface = arabicTypeface
        viewContainer.madhinahnu.typeface = arabicTypeface
        viewContainer.madhihua.text = thirdPersonMasculineSingular
        viewContainer.madhihua.text = thirdPersonMasculineSingular
        viewContainer.madhihuma.text = thirdPersonMascul
        viewContainer.madhihum.text = thirdPersonMasculinePlural
        viewContainer.madhihia.text = thirdPersonFeminineSingular
        viewContainer.madhihumaf.text = thirdPersonFeminineDual
        viewContainer.madhihunna.text = thirdPersonFemininePlural
        viewContainer.madhianta.text = secondPersonMasculineSingular
        viewContainer.madhiantuma.text = secondPersonMasculineDual
        viewContainer.madhiantum.text = secondPersonMasculinePlural
        viewContainer.madhianti.text = secondPersonFeminineSingular
        viewContainer.madhiantumaf.text = secondPersonFeminineDual
        viewContainer.madhiantunna.text = secondPersonFemininePlural
        viewContainer.madhiana.text = firstPersonSingular
        viewContainer.madhinahnu.text = firstPersonPlural
    }

  /*  override fun getItemId(position: Int): Long {
        //  Surah surah = surahArrayList.get(position);
        return sarfSagheer.size.toLong()
    }

    fun getItem(position: Int): Any {
        return sarfSagheer[position]
    }*/

    override fun getItemCount(): Int {
        //    return sarfSagheer.size();
        return 1
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener // current clickListerner
    {
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
        val muzhua: TextView
        val muzhuma: TextView
        val muzhum: TextView
        val muzhia: TextView
        val muzhumaf: TextView
        val muzhunna: TextView
        val muzanta: TextView
        val muzantuma: TextView
        val muzantum: TextView
        val muzanti: TextView
        val muzantumaf: TextView
        val muzantunna: TextView
        val muzana: TextView
        val muznahnu: TextView
        val madimajhua: TextView
        val madimajhuma: TextView
        val madimajhum: TextView
        val madimajhia: TextView
        val madimajhumaf: TextView
        val madimajhunna: TextView
        val madimajanta: TextView
        val madimajantuma: TextView
        val madimajantum: TextView
        val madimajanti: TextView
        val madimajantumaf: TextView
        val madimajantunna: TextView
        val madimajana: TextView
        val madimajnahnu: TextView
        val muzmajhua: TextView
        val muzmajhuma: TextView
        val muzmajhum: TextView
        val muzmajhia: TextView
        val muzmajhumaf: TextView
        val muzmajhunna: TextView
        val muzmajanta: TextView
        val muzmajantuma: TextView
        val muzmajantum: TextView
        val muzmajanti: TextView
        val muzmajantumaf: TextView
        val muzmajantunna: TextView
        val muzmajana: TextView
        val muzmajnahnu: TextView
        val amranta: TextView
        val amrantuma: TextView
        val amrantum: TextView
        val amranti: TextView
        val amrantumaf: TextView
        val amrantunna: TextView
        val nahiamranta: TextView
        val nahiamrantuma: TextView
        val nahiamrantum: TextView
        val nahiamranti: TextView
        val nahiamrantumaf: TextView
        val nahiamrantunna: TextView
        val huaid: TextView?
        val humamid: TextView?
        val humid: TextView?
        val hiaid: TextView?
        val humafid: TextView?
        val hunnaid: TextView?
        val antaid: TextView?
        val antumamid: TextView?
        val antumid: TextView?
        val antiid: TextView?
        val antumafid: TextView?
        val antunnaid: TextView?
        val anaid: TextView?
        val nahnuid: TextView?
       // val pronouns: TextView
        val pastactive: TextView
        val presentactive: TextView
        val pastpassive: TextView
        val presentpassive: TextView
        val command: TextView
        val negcommand: TextView

        init {
          //  pronouns = view.findViewById(R.id.pronouns)
            pastactive = view.findViewById(R.id.pastactive)
            presentactive = view.findViewById(R.id.presentactive)
            pastpassive = view.findViewById(R.id.pastpassive)
            presentpassive = view.findViewById(R.id.presentpassive)
            command = view.findViewById(R.id.command)
            negcommand = view.findViewById(R.id.negcommand)
            huaid = view.findViewById(R.id.huaid)
            humamid = view.findViewById(R.id.humamid)
            humid = view.findViewById(R.id.humid)
            hiaid = view.findViewById(R.id.hiaid)
            humafid = view.findViewById(R.id.humafid)
            hunnaid = view.findViewById(R.id.hunnaid)
            antaid = view.findViewById(R.id.antaid)
            antumamid = view.findViewById(R.id.antumamid)
            antumid = view.findViewById(R.id.antumid)
            antiid = view.findViewById(R.id.antiid)
            antumafid = view.findViewById(R.id.antumafid)
            antunnaid = view.findViewById(R.id.antunnaid)
            anaid = view.findViewById(R.id.anaid)
            nahnuid = view.findViewById(R.id.nahnuid)
            madhihua = view.findViewById(R.id.madhihua)
            madhihuma = view.findViewById(R.id.madhihuma)
            madhihum = view.findViewById(R.id.madihum)
            madhihia = view.findViewById(R.id.madhihia)
            madhihumaf = view.findViewById(R.id.madhihumaf)
            madhihunna = view.findViewById(R.id.madihunna)
            madhianta = view.findViewById(R.id.madhianta)
            madhiantuma = view.findViewById(R.id.madhiantuma)
            madhiantum = view.findViewById(R.id.madhiantum)
            madhianti = view.findViewById(R.id.madhianti)
            madhiantumaf = view.findViewById(R.id.madhiantumaf)
            madhiantunna = view.findViewById(R.id.madhiantunna)
            madhiana = view.findViewById(R.id.madhiana)
            madhinahnu = view.findViewById(R.id.madhinahnu)
            muzhua = view.findViewById(R.id.muzhua)
            muzhuma = view.findViewById(R.id.muzhuma)
            muzhum = view.findViewById(R.id.muzhum)
            muzhia = view.findViewById(R.id.muzhia)
            muzhumaf = view.findViewById(R.id.muzhumaf)
            muzhunna = view.findViewById(R.id.muzhunna)
            muzanta = view.findViewById(R.id.muzanta)
            muzantuma = view.findViewById(R.id.muzantuma)
            muzantum = view.findViewById(R.id.muzantum)
            muzanti = view.findViewById(R.id.muzanti)
            muzantumaf = view.findViewById(R.id.muzantumaf)
            muzantunna = view.findViewById(R.id.muzantunna)
            muzana = view.findViewById(R.id.muzana)
            muznahnu = view.findViewById(R.id.muznahnu)
            //
            madimajhua = view.findViewById(R.id.madimajhua)
            madimajhuma = view.findViewById(R.id.madimajhuma)
            madimajhum = view.findViewById(R.id.madimajhum)
            madimajhia = view.findViewById(R.id.madimajhia)
            madimajhumaf = view.findViewById(R.id.madimajhumaf)
            madimajhunna = view.findViewById(R.id.madimajhunna)
            madimajanta = view.findViewById(R.id.madimajanta)
            madimajantuma = view.findViewById(R.id.madimajantuma)
            madimajantum = view.findViewById(R.id.madimajantum)
            madimajanti = view.findViewById(R.id.madimajanti)
            madimajantumaf = view.findViewById(R.id.madimajantumaf)
            madimajantunna = view.findViewById(R.id.madimajantunna)
            madimajana = view.findViewById(R.id.madimajana)
            madimajnahnu = view.findViewById(R.id.madimajnahnu)
            ///muzmajhool
            muzmajhua = view.findViewById(R.id.muzmajhua)
            muzmajhuma = view.findViewById(R.id.muzmajhuma)
            muzmajhum = view.findViewById(R.id.muzmajhum)
            muzmajhia = view.findViewById(R.id.muzmajhia)
            muzmajhumaf = view.findViewById(R.id.muzmajhumaf)
            muzmajhunna = view.findViewById(R.id.muzmajhunna)
            muzmajanta = view.findViewById(R.id.muzmajanta)
            muzmajantuma = view.findViewById(R.id.muzmajantuma)
            muzmajantum = view.findViewById(R.id.muzmajantum)
            muzmajanti = view.findViewById(R.id.muzmajanti)
            muzmajantumaf = view.findViewById(R.id.muzmajantumaf)
            muzmajantunna = view.findViewById(R.id.muzmajantunna)
            muzmajana = view.findViewById(R.id.muzmajana)
            muzmajnahnu = view.findViewById(R.id.muzmajnahnu)
            //
            amranta = view.findViewById(R.id.amranta)
            amrantuma = view.findViewById(R.id.amrantuma)
            amrantum = view.findViewById(R.id.amrantum)
            amranti = view.findViewById(R.id.amranti)
            amrantumaf = view.findViewById(R.id.amrantumaf)
            amrantunna = view.findViewById(R.id.amrantunna)
            nahiamranta = view.findViewById(R.id.nahiamranta)
            nahiamrantuma = view.findViewById(R.id.nahiamrantuma)
            nahiamrantum = view.findViewById(R.id.nahiamrantum)
            nahiamranti = view.findViewById(R.id.nahiamranti)
            nahiamrantumaf = view.findViewById(R.id.nahiamrantumaf)
            nahiamrantunna = view.findViewById(R.id.nahiamrantunna)
            view.setOnClickListener(this) // current clickListerner
            view.setOnClickListener(this) // current clickListerner
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(v, layoutPosition)
            }
        }
    }
}