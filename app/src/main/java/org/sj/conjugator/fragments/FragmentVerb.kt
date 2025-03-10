package org.sj.conjugator.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.sj.conjugator.adapter.MazeedVerbSarfKabeerAdapter
import org.sj.conjugator.adapter.MujarradVerbSarfKabeerAdapter
import org.sj.conjugator.utilities.GatherAll
import org.sj.data.MazeedResult
import org.sj.data.MujarradResult
import java.util.Locale


class FragmentVerb : Fragment() {
  private val handler = Handler(Looper.getMainLooper())
  private val speechDelay = 1000L // 1 second delay between each speech

  private var textToSpeech: TextToSpeech? = null
  private var mujarradListing: MujarradResult? = null
  private var mazeedlisting: MazeedResult? = null
  var recyclerView: RecyclerView? = null
  private var isAugmented = false
  private var isUnAugmented = false
  private var skabeer = ArrayList<ArrayList<*>>()
  private var augmentedFormula: String? = null
  private lateinit var unaugmentedFormula: String
  private var verbroot: String? = null
  private var verbmood: String? = null
  private lateinit var pastActiveButton: FloatingActionButton
  private lateinit var pastPassiveButton: FloatingActionButton
  private lateinit var presentActiveButton: FloatingActionButton
  private lateinit var presentPassiveButton: FloatingActionButton

  // Use the ExtendedFloatingActionButton to handle the
  // parent FAB
  private lateinit var callButton: ExtendedFloatingActionButton

  // These TextViews are taken to make visible and
  // invisible along with FABs except parent FAB's action
  // name
  private lateinit var pastActiveActionText: TextView
  private lateinit var pastPassiveActionText: TextView
  private lateinit var presentActiveActionText: TextView
  private lateinit var presentPassiveActionText: TextView
  // These TextViews are taken to make visible and
  // invisible along with FABs except parent FAB's action


  // to check whether sub FABs are visible or not
  private var isAllFabsVisible: Boolean? = null
  fun newInstance(): FragmentVerb {
    val f = FragmentVerb()
    val dataBundle = requireArguments()
    dataBundle.getString(QURAN_VERB_ROOT)
    dataBundle.getString(QURAN_VERB_WAZAN) //verb formula depnding upon the verbtype mujjarad or mazeed
    dataBundle.getString(VERBMOOD)
    dataBundle.getString(VERBTYPE)
    f.arguments = dataBundle
    return f
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    setHasOptionsMenu(true)
    val view = inflater.inflate(R.layout.sarfkabeerheader, container, false)
    callButton = view.findViewById<ExtendedFloatingActionButton>(R.id.action_buttons)
    pastActiveButton = view.findViewById<FloatingActionButton>(R.id.pastactiveFb)
    pastPassiveButton = view.findViewById<FloatingActionButton>(R.id.pastpassiveFb)
    pastActiveActionText = view.findViewById<TextView>(R.id.pastactiveTv)
    pastPassiveActionText = view.findViewById<TextView>(R.id.pastpassiveTv)

    presentActiveButton = view.findViewById<FloatingActionButton>(R.id.presentactiveFb)
    presentPassiveButton = view.findViewById<FloatingActionButton>(R.id.presentpassiveFb)

    pastActiveActionText = view.findViewById<TextView>(R.id.pastactiveTv)
    pastPassiveActionText = view.findViewById<TextView>(R.id.pastpassiveTv)

    presentActiveActionText = view.findViewById<TextView>(R.id.presentactivTv)
    presentPassiveActionText = view.findViewById<TextView>(R.id.presentpassiveTv)

    val dataBundle = arguments
    isAllFabsVisible = false
    pastActiveButton.visibility = View.GONE
    pastPassiveButton.visibility = View.GONE
    pastActiveActionText.visibility = View.GONE
    pastPassiveActionText.visibility = View.GONE



    presentActiveButton.visibility = View.GONE
    presentPassiveButton.visibility = View.GONE
    presentActiveActionText.visibility = View.GONE
    presentPassiveActionText.visibility = View.GONE
    // Set the Extended floating action button to
    // shrinked state initially
    callButton.shrink()

    callButton.setOnClickListener {
      if (!isAllFabsVisible!!) {
        // when isAllFabsVisible becomes
        // true make all the action name
        // texts and FABs VISIBLE.

        pastActiveButton.show()
        pastPassiveButton.show()
        pastActiveActionText.setVisibility(View.VISIBLE)
        pastPassiveActionText.setVisibility(View.VISIBLE)

        presentActiveButton.show()
        presentPassiveButton.show()
        presentActiveActionText.setVisibility(View.VISIBLE)
        presentPassiveActionText.setVisibility(View.VISIBLE)

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

        pastActiveButton.hide()
        pastPassiveButton.hide()
        pastActiveActionText.setVisibility(View.GONE)
        pastPassiveActionText.setVisibility(View.GONE)

        presentActiveButton.hide()
        presentPassiveButton.hide()
        presentActiveActionText.setVisibility(View.GONE)
        presentPassiveActionText.setVisibility(View.GONE)

        // Set the FAB to shrink after user
        // closes all the sub FABs
        callButton.shrink()

        // make the boolean variable false
        // as we have set the sub FABs
        // visibility to GONE
        isAllFabsVisible = false
      }
    }
    pastActiveButton.setOnClickListener {
      initializeTextToSpeech("pastactive")

    }


    // below is the sample action to handle add alarm
    // FAB. Here it shows simple Toast msg The Toast
    // will be shown only when they are visible and only
    // when user clicks on them
    pastPassiveButton.setOnClickListener {
      initializeTextToSpeech("pastpassive")
    }

    presentActiveButton.setOnClickListener {
      initializeTextToSpeech("presentactive")
    }
    presentPassiveButton.setOnClickListener {
      initializeTextToSpeech("presentpassive")
    }


    /*    if (dataBundle != null) {
          val callingfragment = dataBundle.getString(MUJARRADVERBTAG)
          if (callingfragment != null) {
            if (callingfragment == "tverblist") {
              callButton.visibility = View.VISIBLE
            } else {
              callButton.visibility = View.GONE
            }
          } else {
            callButton.visibility = View.VISIBLE
          }
        }*/
    //   callButton.setVisibility(View.VISIBLE);
    /*    callButton.setOnClickListener {
          activity?.supportFragmentManager?.popBackStack()
          initializeTextToSpeech()
          val past = mujarradListing?.madhiMudharayList?.get(0)

          // Set language to Arabic


        }*/


    assert(dataBundle != null)
    if (dataBundle!!.getString(VERBTYPE) == "mujarrad") {
      isUnAugmented = true
      unaugmentedFormula = dataBundle.getString(QURAN_VERB_WAZAN)!!
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
    if (isUnAugmented) {
      val stringArray: Array<String> = extractMujarradMadhiMudharayListToStringArray()
      val cleanedList = removeBracketsFromStringList(stringArray.toList())
      splitListIntoSublists = splitListIntoSublists(cleanedList)


    } else {

      val stringArray: Array<String> = extractMazeedMadhiMudharayListToStringArray()
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
        "pastactive" -> {
          speakListWithDelay(splitListIntoSublists[0])
        }

        "pastpassive" -> {
          speakListWithDelay(splitListIntoSublists[1])
        }

        "presentactive" -> {
          speakListWithDelay(splitListIntoSublists[2])
        }

        "presentpassive" -> {
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


  private fun splitListIntoSublists(list: List<String>): List<List<String>> {
    val sublistSize = (list.size + 3) / 4 // Calculate sublist size, rounding up
    return list.chunked(sublistSize)
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


  private fun removeBracketsFromStringList(stringList: List<String>): List<String> {
    return stringList.map { it.replace("[", "").replace("]", "") }
  }

  private fun extractMazeedMadhiMudharayListToStringArray(): Array<String> {
    // val madhiMudharayList = mujarradListing?.madhiMudharayList ?: emptyList()

    val stringList = mutableListOf<String>()
    val madhiMudharayList = mazeedlisting?.madhiMudharayList
    // val madhiMudharayList = mujarradListing?.madhiMudharayList?.get(0)
    madhiMudharayList?.forEach { madhiMudharay ->
      // Add each value to the list
      stringList.add(madhiMudharay.hua.toString())
      stringList.add(madhiMudharay.huma.toString())
      stringList.add(madhiMudharay.hum.toString())
      stringList.add(madhiMudharay.hia.toString())

      stringList.add(madhiMudharay.humaf.toString())
      stringList.add(madhiMudharay.hunna.toString())
      stringList.add(madhiMudharay.anta.toString())
      stringList.add(madhiMudharay.antuma.toString())

      stringList.add(madhiMudharay.antum.toString())
      stringList.add(madhiMudharay.anti.toString())
      stringList.add(madhiMudharay.antumaf.toString())
      stringList.add(madhiMudharay.antunna.toString())

      stringList.add(madhiMudharay.ana!!)
      stringList.add(madhiMudharay.nahnu.toString())
    }


    /*    for (item in madhiMudharayList) {
          Log.d("TextToSpeech", "Item: ${item.toString()}")
        }*/
    //val stringArray = madhiMudharayList.map { it.toString() }.toTypedArray()
    return stringList.toTypedArray()
  }

  private fun extractMujarradMadhiMudharayListToStringArray(): Array<String> {
    // val madhiMudharayList = mujarradListing?.madhiMudharayList ?: emptyList()
    val madhiMudharayLists = mujarradListing?.madhiMudharayList
    val stringList = mutableListOf<String>()
    val madhiMudharayList = mujarradListing?.madhiMudharayList
    // val madhiMudharayList = mujarradListing?.madhiMudharayList?.get(0)
    madhiMudharayList?.forEach { madhiMudharay ->
      // Add each value to the list
      stringList.add(madhiMudharay.hua.toString())
      stringList.add(madhiMudharay.huma.toString())
      stringList.add(madhiMudharay.hum.toString())
      stringList.add(madhiMudharay.hia.toString())

      stringList.add(madhiMudharay.humaf.toString())
      stringList.add(madhiMudharay.hunna.toString())
      stringList.add(madhiMudharay.anta.toString())
      stringList.add(madhiMudharay.antuma.toString())

      stringList.add(madhiMudharay.antum.toString())
      stringList.add(madhiMudharay.anti.toString())
      stringList.add(madhiMudharay.antumaf.toString())
      stringList.add(madhiMudharay.antunna.toString())

      stringList.add(madhiMudharay.ana!!)
      stringList.add(madhiMudharay.nahnu.toString())
    }


    /*    for (item in madhiMudharayList) {
          Log.d("TextToSpeech", "Item: ${item.toString()}")
        }*/
    //val stringArray = madhiMudharayList.map { it.toString() }.toTypedArray()
    return stringList.toTypedArray()
  }


  // Remember to shut down TextToSpeech when you're done with it
  override fun onDestroy() {
    if (textToSpeech?.isSpeaking == true) {
      textToSpeech!!.stop()
    }
    textToSpeech?.shutdown()
    super.onDestroy()
  }

  private fun setUparrays(view: View): ArrayList<ArrayList<*>> {
    if (isUnAugmented) {
      ninitThulathiAdapter()
    } else {
      initMazeedAdapter()

    }
    recyclerView = view.findViewById(R.id.sarfrecview)
    return skabeer
  }

  private fun initMazeedAdapter() {
    mazeedlisting = GatherAll.instance.getMazeedListing(verbmood, verbroot, augmentedFormula)

    val sk: MazeedVerbSarfKabeerAdapter =
      MazeedVerbSarfKabeerAdapter(mazeedlisting, requireContext())
    recyclerView!!.adapter = sk
    recyclerView!!.setHasFixedSize(true)
    recyclerView!!.layoutManager = LinearLayoutManager(context)
  }

  private fun ninitThulathiAdapter() {
    mujarradListing = GatherAll.instance.getMujarradListing(verbmood, verbroot, unaugmentedFormula)
    //  mujarradListing.removeAt(0)
    val ska = MujarradVerbSarfKabeerAdapter(mujarradListing, requireContext())
    recyclerView!!.adapter = ska
    recyclerView!!.setHasFixedSize(true)
    recyclerView!!.layoutManager = LinearLayoutManager(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val recyclerView: RecyclerView = view.findViewById(R.id.sarfrecview)
    val layoutManager = LinearLayoutManager(activity)
    recyclerView.layoutManager = layoutManager
  }

  override fun onDestroyView() {
    // 1. Always call super.onDestroyView() first.
    super.onDestroyView()

    // 2. Handle TextToSpeech cleanup more gracefully.
    textToSpeech?.let { tts ->
      if (tts.isSpeaking) {
        tts.stop()
      }
      tts.shutdown()
    }
    textToSpeech = null //release tts resources

    // 3. Clear RecyclerView adapter reference.
    recyclerView?.adapter = null

    // 4. Clear the ArrayList.
    skabeer.clear()

    // 5. Unregister other listeners if necessary (add comment).
    // Unregister any other listeners here (e.g., BroadcastReceivers, sensor listeners, etc.)
  }
 


  }




