package org.sj.conjugator.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.R
import com.example.utility.QuranGrammarApplication
import dagger.hilt.android.AndroidEntryPoint
import database.entity.MazeedEntity
import database.verbrepo.VerbModel
import org.sj.conjugator.activity.ConjugatorTabsActivity
import org.sj.conjugator.adapter.SarfMujarradSarfSagheerListingAdapter
import org.sj.conjugator.interfaces.OnItemClickListener
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.MazeedResult

import java.util.concurrent.Executors
@AndroidEntryPoint
class RulesMazeedVerbList : Fragment {

    lateinit var verbmood: String
    lateinit   var verbtype: String
       var ssagheer = ArrayList<SarfSagheer>()
    private lateinit  var contexts: Context
    lateinit   var recyclerView: RecyclerView

    //   private ArrayList sarfSagheerThulathiArray = new ArrayList();

    private var sarfsagheerAdapter: SarfMujarradSarfSagheerListingAdapter? = null

    constructor()
    constructor(context: Context?) {
        if (context != null) {
            this.contexts = context
        }
    }

    // --Commented out by Inspection (13/6/21 6:51 PM):private Bitmap bitmap;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val view: View = inflater.inflate(R.layout.thulathilistingnotoolbar, container, false)
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context!!)

        //  String theme = sharedPreferences.getString("theme", 1);
        val indictive: String? = sharedPreferences.getString(VERBMOOD, "Indicative")
        if (indictive != null) {
            verbmood = indictive
        }
        recyclerView = view.findViewById(R.id.sarfrecview)
        if (indictive != null) {
            verbmood = indictive
        }

        recyclerView = view.findViewById(R.id.sarfrecview)
        if (arguments != null) {
            val rule = requireArguments().getString(QURAN_VERB_WAZAN)
            MazeedListing(ssagheer, rule)
        } else {
            MazeedListing(ssagheer, 24.toString())
        }
        MazeedListing(ssagheer, 24.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  recyclerView.setAdapter(sarfsagheerAdapter);
        recyclerView.setHasFixedSize(true)
        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
    }

    private fun MazeedListing(ssagheer: java.util.ArrayList<SarfSagheer>, kov: String?) {
          verbtype= "mazeed"
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_verblist)
        dialog = builder.create()
        val viewmodel: VerbModel by viewModels()
        val ex = Executors.newSingleThreadExecutor()
        if (kov != null) {
            viewmodel.getMazeedWeakness(kov).observe(viewLifecycleOwner){
                listingMazeedWeakness(ssagheer, it as ArrayList<MazeedEntity>)
        ex.execute {
            requireActivity().runOnUiThread { dialog.show() }
          //  val utils = VerbDatabaseUtils(QuranGrammarApplication.context)
         //   val mazeedEntityWeaknesses: java.util.ArrayList<MazeedEntity> = utils.getMazeedWeakness(kov) as java.util.ArrayList<MazeedEntity>

                    requireActivity().runOnUiThread {
                        ex.shutdown()
                        sarfsagheerAdapter = SarfMujarradSarfSagheerListingAdapter(
                            ssagheer,
                            requireActivity()
                        )
                        recyclerView.adapter = sarfsagheerAdapter
                        dialog.dismiss()
                        setupOnItemClickThulathiAdapter()
                    }
                }
            }

        }
        // setupOnItemClickThulathiAdapter();
    }


    private fun listingMazeedWeakness(
        ssagheer: ArrayList<SarfSagheer>,
        mazeedEntityWeaknesses: ArrayList<MazeedEntity>
    ) {
        for (s in mazeedEntityWeaknesses) {
            val result: MazeedResult? = GatherAll.instance.getMazeedListing(verbmood, s.root)

            if (result == null) continue

            val ss = SarfSagheer().apply {
                weakness = result.verbDetailsList[0]?.verbtype
                wazanname = result.verbDetailsList[0]?.babname
                verbroot = result.verbDetailsList[0]?.verbroot
                madhi = result.madhiMudharayList[0].hua
                madhimajhool = result.madhiMudharayList[1].hua
                mudharay = result.madhiMudharayList[2].hua
                mudharaymajhool = result.madhiMudharayList[3].hua
                amrone = result.amrList[0].anta
                nahiamrone = result.nahiAmrList[0].anta
                ismfael = result.skabeerIsmList[0].nomsinM
                ismmafool = result.skabeerIsmList[1].nomsinM
                 ismalaone=""
                 ismalatwo=""
                 ismalathree=""
                 zarfone=""
                 zarftwo=""
                 zarfthree=""
                verbtype = result.verbDetailsList[0]?.mazeedormujarad
                wazan = result.verbDetailsList[0]?.wazannumberorname
            }

            // Add the populated SarfSagheer object to the list
            ssagheer.add(ss)
        }
    }

    /*

        private fun listingMazeedWeakness(
            ssagheer: ArrayList<SarfSagheer>,
            mazeedEntityWeaknesses: ArrayList<MazeedEntity>
        ) {
            for (s in mazeedEntityWeaknesses) {
                val listing: ArrayList<ArrayList<*>> = GatherAll.instance.getMazeedListing(
                    verbmood, s.root
                )
                val ss = SarfSagheer()
                ss.weakness = (listing[3][0] as VerbDetails).verbtype
                ss.wazanname =(listing[3][0] as VerbDetails).babname
                ss.verbroot = (listing[3][0] as VerbDetails).verbroot
                ss.madhi =(listing[0][0] as MadhiMudharay).hua
                ss.madhimajhool = (listing[0][1] as MadhiMudharay).hua
                ss.mudharay = (listing[0][2] as MadhiMudharay).hua
                ss.mudharaymajhool = (listing[0][3] as MadhiMudharay).hua
                ss.amrone =  (listing[2][0] as AmrNahiAmr).anta
                ss.nahiamrone = (listing[2][1] as AmrNahiAmr).anta
                ss.ismfael = (listing[1][0] as FaelMafool).nomsinM
                ss.ismmafool = (listing[1][1] as FaelMafool).nomsinM
                ss.verbtype = (listing[3][0] as VerbDetails).mazeedormujarad
                ss.wazan = (listing[3][0] as VerbDetails).wazannumberorname


                ssagheer.add(ss)
            }
        }
    */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .")
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .")
            }
        }
    }

    private fun setupOnItemClickThulathiAdapter() {
        sarfsagheerAdapter?.SetOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val arrayList = ssagheer[position]
                val rootParadigm = ArrayList<String>()
                val dataBundle = Bundle()
                if (v!!.findViewById<View?>(R.id.conjugateall) != null) {
                    //  root!!Paradigm = Harakah.getMujarradRootParadigm(arrayList);
                    // dataBundle.putString(QURAN_VERB_WAZAN, arrayList.get(2));
                    dataBundle.putString(QURAN_VERB_WAZAN, arrayList.wazan)
                    dataBundle.putString(QURAN_VERB_ROOT, arrayList.verbroot)
                    dataBundle.putString(VERBTYPE, arrayList.verbtype)
                    dataBundle.putString(VERBMOOD, verbmood)
                    val intent = Intent(activity, ConjugatorTabsActivity::class.java)
                    intent.putExtras(dataBundle)
                    startActivity(intent)
                }
            }
        })
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}