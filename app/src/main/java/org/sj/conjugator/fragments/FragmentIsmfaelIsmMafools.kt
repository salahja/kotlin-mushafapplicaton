package org.sj.conjugator.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.sj.conjugator.adapter.IsmFaelIsmMafoolSarfKabeerAdapter
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.IsmFaelMafoolResult
import java.util.Locale


class FragmentIsmfaelIsmMafools : Fragment() {
    private val handler = Handler(Looper.getMainLooper())
    private val speechDelay = 1000L // 1 second delay between each speech
    var mujarradListing: IsmFaelMafoolResult? = null
    var mazeedlist: IsmFaelMafoolResult? = null
    private var textToSpeech: TextToSpeech? = null
    var recyclerView: RecyclerView? = null
    private var isAugmented = false
    private var isUnAugmented = false
    private var skabeer = ArrayList<ArrayList<*>>()
    private var augmentedFormula: String? = null
    private var unaugmentedFormula: String? = null
    private var verbroot: String? = null
    private var verbmood: String? = null
    private var isAllFabsVisible: Boolean? = null
    private lateinit var actPartMasBtn: FloatingActionButton
    private lateinit var actPartFemBtn: FloatingActionButton
    private lateinit var passPartMasBtn: FloatingActionButton
    private lateinit var passPartFemBtn: FloatingActionButton
    // Use the ExtendedFloatingActionButton to handle the
    // parent FAB
    private lateinit var callButton: ExtendedFloatingActionButton

    // These TextViews are taken to make visible and
    // invisible along with FABs except parent FAB's action
    // name
    private lateinit var   actPartMasTv: TextView
    private lateinit var actPartFemTv: TextView
    private lateinit var   passPartMasTv: TextView
    private lateinit var passPartFemTv: TextView
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
        if (textToSpeech?.isSpeaking == true) {
            textToSpeech!!.stop()
        }
        textToSpeech?.shutdown()
        super.onDestroy()


        super.onDestroyView()
        recyclerView?.adapter = null // Clear adapter reference
        skabeer.clear() // Clear the ArrayList
        // ... unregister other listeners if necessary
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.participleheader, container, false)
       

        callButton = view.findViewById<ExtendedFloatingActionButton>(R.id.action_buttons)

        actPartMasBtn = view.findViewById<FloatingActionButton>(R.id.activeParticipleMasculineFb)
        actPartFemBtn = view.findViewById<FloatingActionButton>(R.id.activeParticipleFeminineFb)
        actPartMasTv=view.findViewById<TextView>(R.id.activeParticipleMasculineTv)
        actPartFemTv=view.findViewById<TextView>(R.id.activeParticipleFeminineTv)

        passPartMasBtn = view.findViewById<FloatingActionButton>(R.id.passiveParticipleMasculineFb)
        passPartFemBtn = view.findViewById<FloatingActionButton>(R.id.passiveParticipleFeminineFb)


        passPartMasTv=view.findViewById<TextView>(R.id.passiveParticipleMasculineTv)
        passPartFemTv=view.findViewById<TextView>(R.id.passiveParticipleFeminineTv)

        val dataBundle = arguments
        isAllFabsVisible = false
        actPartMasBtn.visibility = View.GONE
        actPartFemBtn.visibility = View.GONE
        actPartMasTv.visibility = View.GONE
        actPartFemTv.visibility = View.GONE



        passPartMasBtn.visibility = View.GONE
        passPartFemBtn.visibility = View.GONE
        passPartMasTv.visibility = View.GONE
        passPartFemTv.visibility = View.GONE
        // Set the Extended floating action button to
        // shrinked state initially
        callButton.shrink()
        callButton.setOnClickListener {
            if (!isAllFabsVisible!!) {
                // when isAllFabsVisible becomes
                // true make all the action name
                // texts and FABs VISIBLE.

                actPartMasBtn.show()
                actPartFemBtn.show()
                actPartMasTv.setVisibility(View.VISIBLE)
                actPartFemTv.setVisibility(View.VISIBLE)

                passPartMasBtn.show()
                passPartFemBtn.show()
                passPartMasTv.setVisibility(View.VISIBLE)
                passPartFemTv.setVisibility(View.VISIBLE)

                // Now extend the parent FAB, as
                // user clicks on the shrinked
                // parent FAB
                callButton.extend()

                // make the boolean variable true as
                // we have set the sub FABs
                // visibility to GONE
                isAllFabsVisible = true
            } else {
                // when isAllFabsVisible becomes
                // true make all the action name
                // texts and FABs GONE.

                actPartMasBtn.hide()
                actPartFemBtn.hide()
                actPartMasTv.setVisibility(View.GONE)
                actPartFemTv.setVisibility(View.GONE)

                passPartMasBtn.hide()
                passPartFemBtn.hide()
                passPartMasTv.setVisibility(View.GONE)
                passPartFemTv.setVisibility(View.GONE)

                // Set the FAB to shrink after user
                // closes all the sub FABs
                callButton.shrink()

                // make the boolean variable false
                // as we have set the sub FABs
                // visibility to GONE
                isAllFabsVisible = false
            }
        }
        actPartMasBtn.setOnClickListener {
            initializeTextToSpeech("activemasculine")

        }


        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        actPartFemBtn.setOnClickListener {
            initializeTextToSpeech("activefeminine")
        }

        passPartMasBtn.setOnClickListener {
            initializeTextToSpeech("passsivemasculine")
        }
        passPartFemBtn.setOnClickListener {
            initializeTextToSpeech("passsivefeminine")
        }


/*
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
*/

     /*   callButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }*/
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
    private fun initializeTextToSpeech(whichConjugation: String) {
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("ar"))
                //     val result = textToSpeech!!.setLanguage(Locale.US) // Or your desired locale
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "The Language not supported!")
                } else {
                    Log.i("TextToSpeech", "Initialization successful")
                    // TextToSpeech is ready, you can now use it
                    speakPastTense(whichConjugation)
                }
            } else {
                Log.e("TextToSpeech", "Initialization Failed!")
            }
        }
    }


    private fun speakPastTense(whichConjugation: String) {
        var splitListIntoSublists = emptyList<List<String>>()
        if(isAugmented) {
              splitListIntoSublists = emptyList<List<String>>()

            val stringArray: Array<String> = extractMazeenParticiples()
            val cleanedList = removeBracketsFromStringList(stringArray.toList())
            splitListIntoSublists = splitListIntoSublists(cleanedList)
        }else{


            val stringArray: Array<String> = extractMujarradParticples()
            val cleanedList = removeBracketsFromStringList(stringArray.toList())
            splitListIntoSublists = splitListIntoSublists(cleanedList)

        }

        val result = textToSpeech?.setLanguage(Locale("ar"))
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Language not supported
            println("Arabic language is not supported.")
        } else {
            // Text to speech is ready
            when (whichConjugation) {
                "activemasculine" -> {
                    speakListWithDelay(splitListIntoSublists[0])
                }
                "activefeminine" -> {
                    speakListWithDelay(splitListIntoSublists[1])
                }
                "passsivemasculine" -> {
                    speakListWithDelay(splitListIntoSublists[2])
                }
                "passsivefeminine" -> {
                    speakListWithDelay(splitListIntoSublists[3])
                }
            }

            //  val arabicText = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
            //  textToSpeech?.speak(arabicText, TextToSpeech.QUEUE_FLUSH, null, null)
        }


        /*  if (textToSpeech!!.language == Locale("ar")) {
            speakListWithDelay(cleanedList)
          } else {
            Log.e("TextToSpeech", "Arabic language is not supported.")
          }*/
    }

    private fun extractMazeenParticiples(): Array<String> {
        // val madhiMudharayList = mujarradListing?.madhiMudharayList ?: emptyList()

        val stringList = mutableListOf<String>()
        val madhiMudharayList = mazeedlist?.ismFaelMafoolList
        // val madhiMudharayList = mujarradListing?.madhiMudharayList?.get(0)
        madhiMudharayList?.forEach { ismFael ->
            // Add each value to the list
            stringList.add(ismFael.nomsinM.toString())
            stringList.add(ismFael.accsinM.toString())
            stringList.add(ismFael.gensinM.toString())

            stringList.add(ismFael.nomdualM.toString())
            stringList.add(ismFael.accdualM.toString())
            stringList.add(ismFael.gendualM.toString())
            stringList.add(ismFael.nomplurarM.toString())
            stringList.add(ismFael.accplurarlM.toString())
            stringList.add(ismFael.genplurarM.toString())
            stringList.add(ismFael.nomsinF.toString())
            stringList.add(ismFael.accsinF.toString())
            stringList.add(ismFael.gensinF.toString())
            stringList.add(ismFael.nomdualF.toString())
            stringList.add(ismFael.accdualF.toString())
            stringList.add(ismFael.gendualF.toString())
            stringList.add(ismFael.nomplurarF.toString())
            stringList.add(ismFael.accplurarlF.toString())
            stringList.add(ismFael.genplurarF.toString())


        }
   return stringList.toTypedArray()
    }
    private fun splitListIntoSublists(list: List<String>): List<List<String>> {
        val sublistSize = (list.size + 3) / 4 // Calculate sublist size, rounding up
        return list.chunked(sublistSize)
    }


    private fun removeBracketsFromStringList(stringList: List<String>): List<String> {
        return stringList.map { it.replace("[", "").replace("]", "") }
    }

    private fun speakListWithDelay(list: List<String>) {
        var index = 0
        val runnable = object : Runnable {
            override fun run() {
                if (index < list.size) {
                    val str = list[index]
                    textToSpeech?.speak(str, TextToSpeech.QUEUE_FLUSH, null, "")
                    index++
                    handler.postDelayed(this, speechDelay)
                }
            }
        }
        handler.post(runnable)
    }


    private fun extractMujarradParticples(): Array<String> {
        val stringList = mutableListOf<String>()
        val madhiMudharayList = mujarradListing?.ismFaelMafoolList
        // val madhiMudharayList = mujarradListing?.madhiMudharayList?.get(0)
        madhiMudharayList?.forEach { ismFael ->
            // Add each value to the list
            stringList.add(ismFael.nomsinM.toString())
            stringList.add(ismFael.accsinM.toString())
            stringList.add(ismFael.gensinM.toString())

            stringList.add(ismFael.nomdualM.toString())
            stringList.add(ismFael.accdualM.toString())
            stringList.add(ismFael.gendualM.toString())
            stringList.add(ismFael.nomplurarM.toString())
            stringList.add(ismFael.accplurarlM.toString())
            stringList.add(ismFael.genplurarM.toString())
            stringList.add(ismFael.nomsinF.toString())
            stringList.add(ismFael.accsinF.toString())
            stringList.add(ismFael.gensinF.toString())
            stringList.add(ismFael.nomdualF.toString())
            stringList.add(ismFael.accdualF.toString())
            stringList.add(ismFael.gendualF.toString())
            stringList.add(ismFael.nomplurarF.toString())
            stringList.add(ismFael.accplurarlF.toString())
            stringList.add(ismFael.genplurarF.toString())


        }
        return stringList.toTypedArray()


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
        mazeedlist =
            GatherAll.instance.buildMazeedParticiples(verbroot!!, augmentedFormula!!)

            val ska = IsmFaelIsmMafoolSarfKabeerAdapter(mazeedlist, requireContext(), false)
            recyclerView!!.adapter = ska
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    private fun ninitThulathiAdapter() {
        mujarradListing =             GatherAll.instance.getMujarradParticiple(verbroot, unaugmentedFormula)

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