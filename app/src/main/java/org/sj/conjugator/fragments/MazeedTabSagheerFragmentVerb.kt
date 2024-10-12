package org.sj.conjugator.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.fragments.WordAnalysisBottomSheet
import com.example.mushafconsolidatedimport.VerbFormsDialogFrag
import org.sj.conjugator.adapter.SarfMujarradSarfSagheerListingAdapter
import org.sj.conjugator.interfaces.OnItemClickListener
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.MazeedResult
import org.sj.data.MujarradResult


class MazeedTabSagheerFragmentVerb(private val context: Context) : Fragment() {
    // --Commented out by Inspection (31/1/21 5:51 AM):ArrayList<String> sarfkabeer = new ArrayList<>();
    var recyclerView: RecyclerView? = null
    var isAugmented = false
    private var isUnAugmented = false
    private var sarfsagheerAdapter: SarfMujarradSarfSagheerListingAdapter? = null
    private lateinit var augmentedFormula: String
    private lateinit var unaugmentedFormula: String
    private var verbroot: String? = null
    private var verbmood: String? = null
    private var skabeer = ArrayList<ArrayList<*>>()
    var ssagheer: ArrayList<SarfSagheer>? = null
    private var dataBundle: Bundle? = null
    fun newInstance(): MazeedTabSagheerFragmentVerb {
        val f = MazeedTabSagheerFragmentVerb(context)
        val dataBundle = requireArguments()
        if (null != dataBundle) {
            dataBundle.getString(QURAN_VERB_ROOT)
            dataBundle.getString(QURAN_VERB_WAZAN) //verb formula depnding upon the verbtype mujjarad or mazeed
            dataBundle.getString(VERBMOOD)
            dataBundle.getString(VERBTYPE)
        }
        f.arguments = dataBundle
        return f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.sarfkabeerheader, container, false)
        dataBundle = arguments
        if (dataBundle!!.getString(VERBTYPE) == "mujarrad") {
            isUnAugmented = true
            unaugmentedFormula = dataBundle!!.getString(QURAN_VERB_WAZAN)!!
        } else {
            augmentedFormula = dataBundle!!.getString(QURAN_VERB_WAZAN)!!
            isAugmented = true
        }
        verbroot = dataBundle!!.getString(QURAN_VERB_ROOT)
        verbmood = dataBundle!!.getString(VERBMOOD)
        recyclerView = view.findViewById(R.id.sarfrecview)
        skabeer = setUparrays(view)
        return view
    }

    private fun setUparrays(view: View): ArrayList<ArrayList<*>> {
        if (isUnAugmented) {
            ninitThulathiAdapter()
        } else {
            initMazeedAdapterNew()
        }
        recyclerView = view.findViewById(R.id.sarfrecview)
        return skabeer
    }



    private fun initMazeedAdapterNew() {

        val listing: MazeedResult? =
            GatherAll.instance.getMazeedListing(verbmood, verbroot, augmentedFormula)

        val ss = SarfSagheer()
        ss.weakness = listing?.verbDetailsList?.get(0)?.verbtype
        ss.wazanname = listing?.verbDetailsList?.get(0)?.babname
        ss.verbroot = listing?.verbDetailsList?.get(0)?.verbroot
        ss.madhi = listing?.madhiMudharayList?.get(0)?.hua
        ss.madhimajhool = listing?.madhiMudharayList?.get(1)?.hua
        ss.mudharay = listing?.madhiMudharayList?.get(2)?.hua
        ss.mudharaymajhool = listing?.madhiMudharayList?.get(3)?.hua
        ss.amrone= listing?.amrList?.get(0)?.anta
        ss.nahiamrone= listing?.nahiAmrList?.get(0)?.anta
        ss.ismfael= listing?.skabeerIsmList?.get(0)?.nomsinM
        ss.ismmafool= listing?.skabeerIsmList?.get(1)?.nomsinM

        ss.verbtype = listing?.verbDetailsList?.get(0)?.mazeedormujarad
        ss.wazan = listing?.verbDetailsList?.get(0)?.wazannumberorname




        ss.ismalaone =  ""
        ss.ismalatwo =  ""
        ss.ismalathree =  ""
        ss.zarfone =  ""
        ss.zarftwo =  ""
        ss.zarfthree =  ""

        ssagheer = ArrayList()
        ssagheer!!.add(ss)
        sarfsagheerAdapter = SarfMujarradSarfSagheerListingAdapter(ssagheer!!, requireActivity())
        recyclerView!!.adapter = sarfsagheerAdapter
    }

    private fun ninitThulathiAdapter() {
        val babno = 0
        //  OldSarfSagheer(babno);
        val listing: MujarradResult? =
            GatherAll.instance.getMujarradListing(verbmood, verbroot, unaugmentedFormula)
        //InsertSarfSagheerThulathi(ANAQISYAYI);
        val ss = SarfSagheer()
       ss.verbtype = listing?.verbDetailsList?.get(0)?.verbtype

        ss.wazanname = listing?.verbDetailsList?.get(0)?.babname
        ss.verbroot =  listing?.verbDetailsList?.get(0)?.verbroot
          ss.madhi= listing?.madhiMudharayList?.get(0)?.hua


         ss.madhimajhool = listing?.madhiMudharayList?.get(1)?.hua!!.replace("[", "").replace("]", "")
        ss.mudharay = listing?.madhiMudharayList?.get(2)?.hua!!.replace("[", "").replace("]", "")
        ss.mudharaymajhool =listing?.madhiMudharayList?.get(3)?.hua!!.replace("[", "").replace("]", "")
        ss.amrone =listing.amrList?.get(0)?.anta!!.replace("[", "").replace("]", "")
        ss.nahiamrone= listing?.nahiAmr?.get(0)?.anta!!.replace("[", "").replace("]", "")
        ss.ismfael = listing.ismFaelMafoolList.get(0).nomsinM!!.replace("[", "").replace("]", "")
        ss.ismmafool = listing.ismFaelMafoolList.get(1).nomsinM!!.replace("[", "").replace("]", "")
        ss.ismalaone = listing.ismAlaMifal.get(0).nomsinMifalun
        ss.ismalatwo = listing.ismALAMifalatun.get(0).nomsinMifalatun
        ss.ismalathree = listing.ismAlaMifal.get(0).nomsinMifalun
        ss.zarfone =  listing.ismZarfMafalun.get(0).nomsinMafalun
        ss.zarftwo =   listing.ismZarfMafilun.get(0).nomsinMafilun
        ss.zarfthree =  listing.ismZarfMafalatun.get(0).nomsinMafalatun
        ss.verbtype = listing?.verbDetailsList?.get(0)?.verbtype
        ss.wazan =  listing?.verbDetailsList?.get(0)?.babname
        ss.weakness =  listing?.verbDetailsList?.get(0)?.verbtype
        ss.wazanname =  listing?.verbDetailsList?.get(0)?.babname
        val ssagheer = ArrayList<SarfSagheer>()
        ssagheer.add(ss)
        sarfsagheerAdapter = SarfMujarradSarfSagheerListingAdapter(ssagheer, requireActivity())
        recyclerView!!.adapter = sarfsagheerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.sarfrecview)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        sarfsagheerAdapter!!.SetOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val wordEntity = ssagheer!![position]
                if (v != null) {
                    if (v.tag != null) {
                        var form = "Form "
                        if (v.tag == "form") {
                            if (wordEntity.wazan == "2") {
                                form += "II"
                            } else if (wordEntity.wazan == "3") {
                                form += "III"
                            } else if (wordEntity.wazan == "1") {
                                form += "IV"
                            } else if (wordEntity.wazan == "7") {
                                form += "V"
                            } else if (wordEntity.wazan == "8") {
                                form += "VI"
                            } else if (wordEntity.wazan == "4") {
                                form += "VII"
                            } else if (wordEntity.wazan == "5") {
                                form += "VIII"
                            } else if (wordEntity.wazan == "9") {
                                form += "X"
                            }
                            val item = VerbFormsDialogFrag()
                            //    item.setdata(root!!WordMeanings,wbwRootwords,grammarRootsCombined);
                            val fragmentManager = requireActivity().supportFragmentManager
                            val data = arrayOf<String>(form)
                            val transactions = fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.abc_slide_in_top, android.R.anim.fade_out)
                            transactions.show(item)
                            VerbFormsDialogFrag.newInstance(data)
                                .show(activity!!.supportFragmentManager, WordAnalysisBottomSheet.TAG)
                        }
                    }
                }
                //      GrammarWordEntity wordEntity = (GrammarWordEntity) sarfsagheerAdapter.getItem(position);
                val dataBundles = Bundle()
                val wazan = wordEntity.wazan
                val weakness = wordEntity.weakness
                val root = wordEntity.verbroot
                if (isAugmented) {
                    /*  isUnAugmented = true;
                    augmentedFormula = dataBundle.getString(QURAN_VERB_WAZAN);
                    dataBundle.putString(VERBTYPE, "mujarrad");
                    int color = context.getResources().getColor(R.color.background_color_light_brown);
                    final ArrayList<ArrayList> indictive = GatherAll.getInstance().getMazeedListing(verbmood, root!!, augmentedFormula);
                    VerbconjuationBottom frag=new VerbconjuationBottom();
                    Bundle bundle=new Bundle();
                    ArrayList list = indictive.get(1);
                ;;
                    bundle.putParcelableArrayList("list",list);
                    frag.setArguments(bundle);

                    frag.newInstance(list).show(((AppCompatActivity) context).getSupportFragmentManager(), WordAnalysisBottomSheet.TAG);
*/
                } else {

                    /*unaugmentedFormula = dataBundle.getString(QURAN_VERB_WAZAN);
                    dataBundle.putString(VERBTYPE, "mazeed");
                    isAugmented = true;
                    final ArrayList<ArrayList> lists = GatherAll.getInstance().getMujarradListing(verbmood, root!!, unaugmentedFormula);
                    VerbconjuationBottom frag=new VerbconjuationBottom();
                    Bundle bundle=new Bundle();
                    ArrayList list = lists.get(1);
                    ;;
                    bundle.putParcelableArrayList("list",list);
                    frag.setArguments(bundle);

                    frag.newInstance(list).show(((AppCompatActivity) context).getSupportFragmentManager(), WordAnalysisBottomSheet.TAG);*/
                }


                /*       int word_no = wordEntity.getWord_no();
                Bundle dataBundle = new Bundle();
                dataBundle.putString(SURAH_ID, surah_id);
                dataBundle.putInt(AYAHNUMBER, ayah_number);
                dataBundle.putInt(WORDNUMBER, word_no);
                dataBundle.putString(SURAH_ARABIC_NAME, suraharabicname);
                RootDialog root!!Dialog = new RootDialog();
                FragmentManager fragmentManager = getFragmentManager();
                root!!Dialog.setArguments(dataBundle);
                assert fragmentManager != null;
                fragmentManager.beginTransaction().add(R.id.fragmentParentViewGroup, root!!Dialog).addToBackStack(ROOTDIALOGFRAG).commit();
*/
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null // Clear adapter reference
        skabeer.clear()
        ssagheer?.clear()// Clear the ArrayList
        // ... unregister other listeners if necessary
    }
    companion object {
    }
}