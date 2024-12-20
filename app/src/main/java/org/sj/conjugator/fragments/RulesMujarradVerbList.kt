package org.sj.conjugator.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import database.entity.MujarradVerbs
import database.verbrepo.VerbModel
import org.sj.conjugator.activity.ConjugatorTabsActivity
import org.sj.conjugator.adapter.SarfMujarradSarfSagheerListingAdapter
import org.sj.conjugator.interfaces.OnItemClickListener
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.MujarradResult
import java.util.concurrent.Executors
@AndroidEntryPoint
class RulesMujarradVerbList : Fragment {

    lateinit var verbmood: String

    // --Commented out by Inspection (13/6/21 6:51 PM):Button llPdf;
    lateinit var verbtype: String
    private lateinit var contexts: Context
    var ssagheer = ArrayList<SarfSagheer>()
    lateinit var recyclerView: RecyclerView


    private lateinit var sarfsagheerAdapter: SarfMujarradSarfSagheerListingAdapter

    constructor(context: Context?) {
        if (context != null) {
            this.contexts = context
        }
    }

    constructor() {}

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
        if (arguments != null) {
            val rule = requireArguments().getString(QURAN_VERB_WAZAN)
            MujarradListing(ssagheer, rule)
        } else {
            MujarradListing(ssagheer, 24.toString())
        }
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

    private fun MujarradListing(ssagheer: java.util.ArrayList<SarfSagheer>, kov: String?) {
        verbtype="mujarrad"
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
            viewmodel.getMujarradWeakness(kov).observe(viewLifecycleOwner){
                ex.execute {
                    requireActivity().runOnUiThread { dialog.show() }

                    listingMujarradWeakness(ssagheer, it as ArrayList<MujarradVerbs>)
                    verbtype="mujarrad"
                    requireActivity().runOnUiThread {
                        ex.shutdown()
                        sarfsagheerAdapter = SarfMujarradSarfSagheerListingAdapter(
                            ssagheer,
                            requireActivity()
                        )
                        recyclerView.adapter = sarfsagheerAdapter
                        setupOnItemClickThulathiAdapter()
                        dialog.dismiss()
                    }
                }
            }
        }

        //   setupOnItemClickThulathiAdapter();
    }

    private fun listingMujarradWeakness(
        ssagheer: ArrayList<SarfSagheer>,
        mujarradBYWeakness: ArrayList<MujarradVerbs>
    ) {
        for (s in mujarradBYWeakness) {
            val result: MujarradResult? = GatherAll.instance.getMujarradListing(
                verbmood, s.root
            )


            val ss = SarfSagheer().apply {
                weakness = result?.verbDetailsList?.get(0)?.verbtype
                wazanname = result?.verbDetailsList?.get(0)?.babname
                verbroot = result?.verbDetailsList?.get(0)?.verbroot
                madhi = result?.madhiMudharayList?.get(0)?.hua
                madhimajhool = result?.madhiMudharayList?.get(1)?.hua
                mudharay = result?.madhiMudharayList?.get(2)?.hua
                mudharaymajhool = result?.madhiMudharayList?.get(3)?.hua
                amrone = result?.amrList?.get(0)?.anta
                nahiamrone = result?.nahiAmr?.get(0)?.anta
                ismfael = result?.ismFaelMafoolList?.get(0)?.nomsinM
                ismmafool = result?.ismFaelMafoolList?.get(1)?.nomsinM
                ismalaone=""
                ismalatwo=""
                ismalathree=""
                zarfone=""
                zarftwo=""
                zarfthree=""
                verbtype = result?.verbDetailsList?.get(0)?.mazeedormujarad
                wazan = result?.verbDetailsList?.get(0)?.wazannumberorname
            }
            /*
      ss.setWeakness(listing.get(0).get(0).toString());
      ss.setWazanname(listing.get(0).get(1).toString());
      ss.setVerbroot(listing.get(0).get(2).toString());

      ss.setMadhi(listing.get(0).get(3).toString());
      ss.setMadhimajhool(listing.get(0).get(4).toString());
      ss.setMudharay(listing.get(0).get(5).toString());


      ss.setMudharaymajhool(listing.get(0).get(6).toString());
      ss.setAmrone(listing.get(0).get(7).toString());
      ss.setNahiamrone(listing.get(0).get(8).toString());
      ss.setIsmfael(listing.get(0).get(9).toString());
      ss.setIsmmafool(listing.get(0).get(10).toString());
      ss.setIsmalaone(listing.get(0).get(11).toString());
      ss.setIsmalatwo(listing.get(0).get(12).toString());
      ss.setIsmalathree(listing.get(0).get(13).toString());

      ss.setZarfone(listing.get(0).get(13).toString());
      ss.setZarftwo(listing.get(0).get(15).toString());
      ss.setZarfthree(listing.get(0).get(16).toString());
      ss.setVerbtype(listing.get(0).get(17).toString());
      ss.setWazan(listing.get(0).get(18).toString());
      */ssagheer.add(ss)
        }
    }

    private fun setupOnItemClickThulathiAdapter() {
        sarfsagheerAdapter.SetOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val arrayList = ssagheer[position]
                val rootParadigm = ArrayList<String>()
                val dataBundle = Bundle()
                if (v!!.findViewById<View?>(R.id.conjugateall) != null) {
                    //  rootParadigm = Harakah.getMujarradRootParadigm(arrayList);
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
    }
}