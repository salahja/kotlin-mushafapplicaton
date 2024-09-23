package com.example.mushafconsolidated

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.Constant.WORDDETAILS
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.databinding.FragmentVerbrootListBinding
import com.example.mushafconsolidated.databinding.NewFragmentAjroomiyaListBinding
import com.example.mushafconsolidated.databinding.VtwonewverbsarfkabeertraditionalBinding
import com.example.mushafconsolidated.fragments.NewQuranMorphologyDetails
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet.Companion
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListener
import com.example.mushafconsolidatedimport.MushafApplication.Companion.appContext
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sj.conjugator.adapter.VerbSarfKabeerAdapter
import org.sj.conjugator.utilities.GatherAll


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * FontQuranListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
</pre> *
 */
class VerbQuizBottomOptionDialog : BottomSheetDialogFragment() {
    private var isAugmented = false
    private var isUnAugmented = false
    private var skabeer = ArrayList<ArrayList<*>>()
    private var augmentedFormula: String? = null
    private lateinit var unaugmentedFormula: String
    private var chap_id: Int = 0
    private var verse_id: Int = 0
    private var word_no: Int = 0
    private var wazan: String? = null
    private var root: String? = null
    private var verbmood:String?=null
    private var isaugmented: String? = null
    open var form = 0
    open var Thulathi: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = this.requireArguments()
        val stringArray = bundle.getStringArray(VerbQuizBottomOptionDialog.ARG_OPTIONS_DATA)
        chap_id = stringArray!![0].toInt()
        verse_id = stringArray!![1].toInt()
        word_no = stringArray!![2].toInt()
        wazan = stringArray!![3]
        root = stringArray!![4]
        isaugmented=stringArray[5]
        verbmood=stringArray[6]


//    val data = arrayOf(chapterno.toString(), verse.toString(), wordno.toString(),wazan,root,isaugmented,verbtype)
   /*     if (arguments != null) {

            name = arguments?.getString("name")
            chap_id = arguments?.getInt("chap_id")!!
            verse_id = arguments?.getInt("verse_no")!!
        }*/
        val utils: Utils = Utils(activity)
        var appContext: Context = requireContext()
        //  setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Material3_BottomSheetDialog);
    }

    private fun setUparrays(view: View): ArrayList<ArrayList<*>> {
        if (isUnAugmented) {
            ninitThulathiAdapter()
        } else {



          convertForms(wazan).toString()
            val indictive: ArrayList<ArrayList<*>> =
                GatherAll.instance.getMazeedListing(verbmood, root, form.toString())
            val sk: VerbSarfKabeerAdapter
            if (indictive.isNotEmpty()) {
                // indictive.removeAt(0)
                sk = VerbSarfKabeerAdapter(indictive, requireContext())

            }
        }

        return skabeer
    }

    private fun convertForms(mform: String?) {
        when (mform) {
            "IV" -> form = 1
            "II" -> form = 2
            "III" -> form = 3
            "VII" -> form = 4
            "VIII" -> form = 5
            "VI" -> form = 7
            "V" -> form = 8
            "X" -> form = 9

        }
    }

    private fun ninitThulathiAdapter() {
        val mujarradListing: ArrayList<ArrayList<*>> =
            GatherAll.instance.getMujarradListing(verbmood, root, unaugmentedFormula)
        //  mujarradListing.removeAt(0)
        val ska = VerbSarfKabeerAdapter(mujarradListing, requireContext())

    }

    lateinit var mItemClickListener: OnItemClickListener
    var radioGroup: RadioGroup? = null
    private var fontQuranAdapter: FontQuranAdapter? = null
    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        if (mItemClickListener != null) {
            this.mItemClickListener = mItemClickListener
        }
    }
    private var binding: VtwonewverbsarfkabeertraditionalBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sarfkabeerheader, container, false)
        if (isaugmented!!.equals("no")) {
            isUnAugmented = true
            unaugmentedFormula = wazan!!
        } else {
            augmentedFormula = wazan
            isAugmented = true
        }

        skabeer = setUparrays(view)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val details: ArrayList<String> = ArrayList()
        fontQuranAdapter = FontQuranAdapter()
        recyclerView.adapter = fontQuranAdapter
        fontQuranAdapter!!.SetOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val checkedRadioButtonId: Int = radioGroup!!.checkedRadioButtonId
            }
        })
    }


    private class ViewHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.vtwonewverbsarfkabeertraditional, parent, false)),
        View.OnClickListener {


        var hua: TextView
        var hum: TextView
        var huma: TextView
        var anta: TextView
        var antuma: TextView
        var antum: TextView
        var anti: TextView
        var antumaf: TextView
        var antunna: TextView
        var hia: TextView
        var humaf: TextView
        var hunna: TextView

        init {
            // TODO: Customize the item layout
            //  super(inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false));
            hua = itemView.findViewById(R.id.hua)
            huma = itemView.findViewById(R.id.huma)
            hum = itemView.findViewById(R.id.hum)
            hia = itemView.findViewById(R.id.hia)
            humaf = itemView.findViewById(R.id.humaf)
            hunna = itemView.findViewById(R.id.hunna)
            anta = itemView.findViewById(R.id.anta)
            antuma = itemView.findViewById(R.id.antuma)
            antum = itemView.findViewById(R.id.antum)
            anti = itemView.findViewById(R.id.anti)
            antumaf = itemView.findViewById(R.id.antumaf)
            antunna = itemView.findViewById(R.id.antunna)


            var frameLayout = itemView.findViewById<RelativeLayout>(R.id.bottomSheet)
            itemView.setOnClickListener(this)

        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }


    }

    private inner class FontQuranAdapter :
        RecyclerView.Adapter<ViewHolder>() {
        private var mItemClickListener: OnItemClickListener? = null
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context!!), parent)


        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val sharedPreferences: SharedPreferences =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                    requireContext()
                )
            holder.hua.text = skabeer[position][0].toString()
            holder.huma.text = skabeer[position][1].toString()
            holder.hum.text = skabeer[position][2].toString()
            holder.hia.text = skabeer[position][3].toString()
            holder.humaf.text = skabeer[position][4].toString()
            holder.hunna.text = skabeer[position][5].toString()
            holder.anta.text = skabeer[position][6].toString()
            holder.antuma.text = skabeer[position][7].toString()
            holder.antum.text = skabeer[position][8].toString()
            holder.anti.text = skabeer[position][9].toString()
            holder.antumaf.text = skabeer[position][10].toString()
            holder.antunna.text = skabeer[position][11].toString()


            /*      holder.ivShare.setOnClickListener(convertView -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        holder.duaheader.getText() + "\n\n" +
                                holder.tvDuaArabic.getText() + "\n\n" +
                                holder.tvDuaTranslation.getText() + "\n\n" +
                                holder.tvDuaReference.getText() + "\n\n" +
                                convertView.getResources().getString(R.string.action_share_credit)
                );
                intent.setType("text/plain");
                convertView.context!!.startActivity(
                        Intent.createChooser(
                                intent,
                                convertView.getResources().getString(R.string.action_share_title)
                        )
                );
            });
*/
        }

        override fun getItemCount(): Int {
            return 1
        }

        fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
            this.mItemClickListener = mItemClickListener
        }
    }

    companion object {
        const val TAG: String = "opton"

        // TODO: Customize parameter argument names
        private const val ARG_OPTIONS_DATA: String = "item_count"

        // TODO: Customize parameters
        fun newInstance(data: Array<String>): VerbQuizBottomOptionDialog {
            val fragment: VerbQuizBottomOptionDialog = VerbQuizBottomOptionDialog()
            val args: Bundle = Bundle()
            args.putStringArray(BottomOptionDialog.ARG_OPTIONS_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }
}