package org.sj.conjugator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant.MUJARRADVERBTAG
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.sj.conjugator.adapter.IsmFaelIsmMafoolSarfKabeerAdapter
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.IsmFaelMafoolResult


class FragmentIsmfaelIsmMafools : Fragment() {
    var recyclerView: RecyclerView? = null
    private var isAugmented = false
    private var isUnAugmented = false
    private var skabeer = ArrayList<ArrayList<*>>()
    private var augmentedFormula: String? = null
    private var unaugmentedFormula: String? = null
    private var verbroot: String? = null
    private var verbmood: String? = null
    fun newInstance(): FragmentIsmfaelIsmMafools {
        val f = FragmentIsmfaelIsmMafools()
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

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null // Clear adapter reference
        skabeer.clear()
    // Clear the ArrayList
        // ... unregister other listeners if necessary
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.sarfkabeerheader, container, false)
        val callButton = view.findViewById<FloatingActionButton>(R.id.action_buttons)
        val dataBundle = arguments
        if (dataBundle != null) {
            val callingfragment = dataBundle.getString(MUJARRADVERBTAG)
            if (callingfragment != null) {
                if (callingfragment == "tverblist") {
                    callButton.visibility = View.VISIBLE
                } else {
                    callButton.visibility = View.GONE
                }
            } else {
                callButton.visibility = View.GONE
            }
        }

        callButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
     /*   callButton.setOnClickListener {
            val fm = activity
                ?.supportFragmentManager
            activity
                ?.supportFragmentManager?.popBackStack()
        }*/
        assert(dataBundle != null)
        if (dataBundle!!.getString(VERBTYPE) == "mujarrad") {
            isUnAugmented = true
            unaugmentedFormula = dataBundle.getString(QURAN_VERB_WAZAN)
        } else {
            augmentedFormula = dataBundle.getString(QURAN_VERB_WAZAN)
            isAugmented = true
        }
        verbroot = dataBundle.getString(QURAN_VERB_ROOT)
        verbmood = dataBundle.getString(VERBMOOD)
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
        val arrayLists: IsmFaelMafoolResult? =
            GatherAll.instance.buildMazeedParticiples(verbroot!!, augmentedFormula!!)

            val ska = IsmFaelIsmMafoolSarfKabeerAdapter(arrayLists, requireContext(), false)
            recyclerView!!.adapter = ska
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    private fun ninitThulathiAdapter() {
        val mujarradListing: IsmFaelMafoolResult? =
            GatherAll.instance.getMujarradParticiple(verbroot, unaugmentedFormula)

            val newsarf = true
            val ska = IsmFaelIsmMafoolSarfKabeerAdapter(mujarradListing, requireContext(), newsarf)
            recyclerView!!.adapter = ska
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.sarfrecview)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        var ref: ImageView
    }

    companion object
}