package com.example.mushafconsolidated.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Constant
import com.example.Constant.SURAH_ID
import com.example.mushafconsolidated.Activity.TafsirFullscreenActivity
import com.example.mushafconsolidated.Adapters.TopicAdapter
import com.example.mushafconsolidated.Entities.BookMarks
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.SurahSummary
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListenerOnLong
import com.example.mushafconsolidated.model.CorpusAyahWord
import com.example.mushafconsolidated.quranrepo.QuranRepository
import com.example.mushafconsolidated.quranrepo.QuranViewModel
import com.example.sentenceanalysis.SentenceGrammarAnalysis
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sj.hisnul.fragments.NamesDetail
import javax.inject.Inject
import androidx.fragment.app.Fragment as Fragment1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment1] subclass.
 * Use the [TopicFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TopicFrag : DialogFragment(), OnItemClickListenerOnLong {
    private lateinit var maps: HashMap<String, String>
    private lateinit var flowAyahWordAdapter: TopicAdapter
    @Inject
    lateinit var quranRepository: QuranRepository
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var corpusayahWordArrayList: ArrayList<CorpusAyahWord>
    private var corpusSurahWord: List<CorpusEntity>? = null
    private var newnewadapterlist = LinkedHashMap<Int, ArrayList<CorpusEntity>>()
    private var allofQuran: List<QuranEntity>? = null
    private lateinit var mainViewModel: QuranViewModel
    private lateinit var newcorpusayahWordArrayList: ArrayList<CorpusEntity>
    private lateinit var arrayofquran: ArrayList<ArrayList<QuranEntity>>
    private lateinit var arrayofadapterlist: ArrayList<LinkedHashMap<Int, ArrayList<CorpusEntity>>>
    private  var corpusGroupedByAyah:LinkedHashMap<Int, ArrayList<CorpusEntity>> =
        LinkedHashMap()
    //   private lateinit var layoutBottomSheet: RelativeLayout
    //   private var sheetBehavior: BottomSheetBehavior<RelativeLayout?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = arguments
        assert(bundle != null)
        maps =
            bundle!!.getSerializable(TopicFrag.ARG_OPTIONS_DATA) as HashMap<String, String>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keys: Set<String> = maps.keys
        corpusayahWordArrayList = ArrayList()
        arrayofquran = ArrayList()
        arrayofadapterlist = ArrayList()
        val bundle: Bundle? = arguments
        // layoutBottomSheet = view.findViewById(R.id.bottom_sheet)
        //  sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet)
        mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
        if (maps.isNotEmpty()) {

            val surahname = ""
            val listener: OnItemClickListenerOnLong = this
            val dialog = showLoadingDialog()
            val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
            //   val value = GlobalScope.async {
            //  delay(1000)
            println("thread running on [${Thread.currentThread().name}]")

            val surahArrays = mainViewModel.loadListschapter().value

            scope.launch {
                for (key: String in keys) {
                    val splits = maps[key]
                    assert(splits != null)
                    val split = splits!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    for (s: String in split) {
                        if(!s.contains("null")) {
                            getwbwy(s, key)
                        }
                    }
                }
                requireActivity().runOnUiThread {

                    setupRecyclerView(listener, surahArrays, view)
                    dialog.dismiss()
                }
            }

        } else {


            val surah = bundle!!.getInt(Constant.SURAH_ID)
            val ayah = bundle.getInt(Constant.SURAH_ID)
            //       ..   val header = bundle.extras!!.getString(Constant.ARABICWORD)
            //  surahname = bundle.extras!!.getString(Constant.SURAH_ARABIC_NAME)!!
            corpusayahWordArrayList = ArrayList()

            mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
            val surahArrays = mainViewModel.loadListschapter().value
       /*     allofQuran =
                mainViewModel.getsurahayahVerses(surah, ayah).value as ArrayList<QuranEntity>?
            corpusSurahWord = mainViewModel.getQuranCorpusWbwbysurahAyah(surah, ayah).value
            val corpus = CorpusUtilityorig(requireContext())
            newnewadapterlist = CorpusUtilityorig.NotcomposeWBWCollection(allofQuran, corpusSurahWord)
            allofQuran?.let { arrayofquran.add(it) }*/

            mainViewModel = ViewModelProvider(this)[QuranViewModel::class.java]
           // allofQuran =                mainViewModel.getsurahayahVerses(surah, ayah).value as ArrayList<QuranEntity>?
            corpusSurahWord = mainViewModel.getCorpusEntityFilterSurahAya(surah, ayah)

            val corpusAndQurandata = quranRepository.CorpusAndQuranDataSurahAyah(surah,ayah)

            corpusSurahWord=corpusAndQurandata.copusExpandSurah
            allofQuran=corpusAndQurandata.allofQuran
            corpusGroupedByAyah = corpusSurahWord!!.groupBy { it.ayah } as LinkedHashMap<Int, ArrayList<CorpusEntity>>

            /*         newnewadapterlist =
                         CorpusUtilityorig.composeWBWCollectionold(allofQuran, corpusSurahWord)*/
            allofQuran?.let { arrayofquran.add(it as java.util.ArrayList<QuranEntity>) }


        /*    corpus.setKana(newnewadapterlist, surah, ayah, newnewadapterlist.size)

            corpus.setMudhafFromDB(newnewadapterlist, surah, ayah, newnewadapterlist.size)
            corpus.SetMousufSifaDB(newnewadapterlist, surah, ayah, newnewadapterlist.size)
            corpus.setKana(newnewadapterlist, surah, ayah, newnewadapterlist.size)
            corpus.setShart(newnewadapterlist, surah, ayah, newnewadapterlist.size)
            corpus.newnewHarfNasbDb(newnewadapterlist, surah, ayah, newnewadapterlist.size)*/

            arrayofadapterlist.add(corpusGroupedByAyah)
            //    final Object o6 = wbwa.get(verseglobal).get(0);
            val sb = StringBuilder()
            val activity = requireActivity() as AppCompatActivity
            val listener: OnItemClickListenerOnLong = this
            flowAyahWordAdapter = TopicAdapter(activity,requireContext(),childFragmentManager,
                arrayofadapterlist,
                listener,

                arrayofquran,
                surahArrays
            )
            val linearLayoutManager = LinearLayoutManager(requireContext())
            val parentRecyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
            parentRecyclerView.layoutManager = linearLayoutManager
            flowAyahWordAdapter.addContext(requireContext())
            parentRecyclerView.setHasFixedSize(true)
            parentRecyclerView.adapter = flowAyahWordAdapter
            flowAyahWordAdapter.notifyDataSetChanged()

        }
    }

    private fun showLoadingDialog(): AlertDialog {
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.Theme_MaterialComponents_DayNight_DarkActionBar
        )
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val dialog = builder.create()
        requireActivity().runOnUiThread { dialog.show() }
        return dialog
    }

    private fun setupRecyclerView(
        listener: OnItemClickListenerOnLong,
        surahArrays: List<ChaptersAnaEntity>?,
        view: View
    ) {
        val activity = requireActivity() as AppCompatActivity
        val linearLayoutManager = LinearLayoutManager(requireContext())

        /*       val flowAyahWordAdapter =
                           TopicFlowAyahWordAdapter(corpusayahWordArrayList, listener, surahname)*/
        flowAyahWordAdapter = TopicAdapter(
            activity, requireContext(), childFragmentManager,
            arrayofadapterlist,
            listener,

            arrayofquran,
            surahArrays
        )


        val parentRecyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        parentRecyclerView.layoutManager = linearLayoutManager
        flowAyahWordAdapter.addContext(requireContext())
        parentRecyclerView.setHasFixedSize(true)
        parentRecyclerView.adapter = flowAyahWordAdapter
        flowAyahWordAdapter.notifyDataSetChanged()
    }
    private fun getwbwy(str: String, header: String) {
        val (surahId, ayahId) = str.split(":").map { it.trim().toInt() }
        newpreparewbwarray(header, surahId, ayahId)
    }
  /*  private fun getwbwy(str: String, header: String) {
        val ss = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

            val suraid = ss[0].trim { it <= ' ' }.toInt()
            val ayah = ss[1].trim { it <= ' ' }.toInt()
            //    preparewbwarray(header, suraid, ayah)
            newpreparewbwarray(header, suraid, ayah)

    }*/

    private fun newpreparewbwarray(header: String?, surahid: Int, ayah: Int) {


    /*    mainViewModel = ViewModelProvider(this)[QuranVIewModel::class.java]
        allofQuran =
            mainViewModel.getsurahayahVerses(suraid, ayah).value as ArrayList<QuranEntity>?
        corpusSurahWord = mainViewModel.getQuranCorpusWbwbysurahAyah(suraid, ayah).value
        val corpus = CorpusUtilityorig(requireContext())
        this.newnewadapterlist = CorpusUtilityorig.composeWBWCollectionold(allofQuran, corpusSurahWord)

        this.allofQuran?.let { arrayofquran.add(it) }*/

       // mainViewModel = ViewModelProvider(this)[QuranVIewModel::class.java]
       // allofQuran =            mainViewModel.getsurahayahVerses(surahid, ayah).value as ArrayList<QuranEntity>?
       // corpusSurahWord = mainViewModel.getCorpusEntityFilterSurahAya(surahid, ayah).value

        val corpusAndQurandata = quranRepository.CorpusAndQuranDataSurahAyah(surahid,ayah)
        allofQuran = corpusAndQurandata.allofQuran
        corpusSurahWord=corpusAndQurandata.copusExpandSurah

        corpusGroupedByAyah = corpusSurahWord!!.groupBy { it.ayah } as LinkedHashMap<Int, ArrayList<CorpusEntity>>

        /*         newnewadapterlist =
                     CorpusUtilityorig.composeWBWCollectionold(allofQuran, corpusSurahWord)*/
        allofQuran?.let { arrayofquran.add(it as java.util.ArrayList<QuranEntity>) }



      /*  corpus.setKana(newnewadapterlist, suraid, ayah, newnewadapterlist.size)

        corpus.setMudhafFromDB(newnewadapterlist, suraid, ayah, newnewadapterlist.size)
        corpus.SetMousufSifaDB(newnewadapterlist, suraid, ayah, newnewadapterlist.size)
        corpus.setKana(newnewadapterlist, suraid, ayah, newnewadapterlist.size)
        corpus.setShart(newnewadapterlist, suraid, ayah, newnewadapterlist.size)
        corpus.newnewHarfNasbDb(newnewadapterlist, suraid, ayah, newnewadapterlist.size)
*/
        arrayofadapterlist.add(corpusGroupedByAyah)
        //    final Object o6 = wbwa.get(verseglobal).get(0);
        val sb = StringBuilder()





        println("check")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    companion object {
        private const val ARG_OPTIONS_DATA: String = "map"
        fun newInstance(dataBundle: Bundle): Fragment1 {
            val fragment: TopicFrag = TopicFrag()
            val args: Bundle = Bundle()
            args.putString(TopicFrag.ARG_OPTIONS_DATA, dataBundle.toString())
            fragment.arguments = args
            return fragment
        }
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
    }

    override fun onItemClick(view: View, position: Int) {
        qurangrammarmenu(view, position)
    }

    @SuppressLint("InflateParams")
    fun qurangrammarmenu(view: View, position: Int) {
        var view = view
        val tag = view.tag
        var bookmarkview: View? = null
        val overflow = view.findViewById<View>(R.id.ivActionOverflow)
        if(tag=="summary"){
            view = view.findViewById(R.id.summbaryfb)

            //  HideFabMenu();
            val chapter_no = arrayofquran[position][0].surah
            //   int verse = ayahWord.getWord().get(0).getVerseId();

            val dataBundle = Bundle()
            if (chapter_no != null) {
                dataBundle.putInt(SURAH_ID, chapter_no)
            }
            val item = SurahSummary()
            item.arguments = dataBundle
            //  FragmentTransaction transactions = fragmentManager.beginTransaction().setCustomAnimations(R.anim.abc_slide_in_top, android.R.anim.fade_out);
            //   transactions.show(item);
            if (chapter_no != null) {
                SurahSummary.newInstance(chapter_no).show(
                   requireActivity().supportFragmentManager,
                    NamesDetail.TAG
                )
            }

        }
        if ((tag == "bookmark")) {
            bookMarkSelected(position, bookmarkview)
            //  SurahAyahPicker();
        } else if ((tag == "overflow_img")) {
            val builder = AlertDialog.Builder(requireContext())
            val factory = LayoutInflater.from(requireContext())
            view = factory.inflate(R.layout.topic_popup_layout, null)
            val tafsirtag = view.findViewById<View>(R.id.tafsir)
            bookmarkview = view.findViewById(R.id.bookmark)
            builder.setView(view)
            val location = IntArray(2)
            overflow.getLocationOnScreen(location)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val wmlp = dialog.window!!.attributes
            wmlp.gravity = Gravity.TOP or Gravity.START
            wmlp.x = location[0] //x position
            wmlp.y = location[1] //y position
            dialog.window!!.attributes.windowAnimations =
                R.style.WindowAnimationTransition //style id
            dialog.show()
            tafsirtag.setOnClickListener { view12: View? ->
                val readingintent: Intent = Intent(
                    requireContext(),
                    TafsirFullscreenActivity::class.java
                )
                //  flowAyahWordAdapter.getItem(position);
                val chapter_no: Int =
                    corpusayahWordArrayList[position].word[0].surahId
                val verse: Int =
                    corpusayahWordArrayList[position].word[0].verseId
                val surahArrays: Array<String> =
                    resources.getStringArray(R.array.suraharabic)
                val surahname: String = surahArrays[chapter_no - 1]
                readingintent.putExtra(Constant.SURAH_ID, chapter_no)
                readingintent.putExtra(Constant.AYAH_ID, verse)
                readingintent.putExtra(Constant.SURAH_ARABIC_NAME, surahname)
                startActivity(readingintent)
                dialog.dismiss()
            }
            val finalBookmarkview = bookmarkview
            bookmarkview.setOnClickListener(View.OnClickListener { view1: View? ->
                bookMarkSelected(position, finalBookmarkview)
                dialog.dismiss()
            })
            println("check")
        } else if ((tag == "qurantext")) {
            val word: QuranEntity

            word = if (position != 0) {
                arrayofquran[position][0]
            } else {
                arrayofquran[position][0]
            }
            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, arrayofquran[position][0].surah)
            dataBundle.putInt(Constant.AYAHNUMBER, arrayofquran[position][0].ayah)
            LoadItemList(dataBundle, word)
        }
    }

    private fun LoadItemList(dataBundle: Bundle, word: QuranEntity) {
       /* val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val item = GrammerFragmentsBottomSheet()
        val fragmentManager: FragmentManager = childFragmentManager
        item.arguments = dataBundle
        val data = arrayOf(
            word.surah.toString(),
            word.ayah.toString(),
            word.translation,
            (1).toString()
        )
        @SuppressLint("CommitTransaction") val transactions = fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.abc_slide_in_top, android.R.anim.fade_out)
        transactions.show(item)
        GrammerFragmentsBottomSheet.newInstance(data)
            .show(childFragmentManager, WordAnalysisBottomSheet.TAG)*/
        val dataBundle = Bundle()
        dataBundle.putInt(Constant.SURAH_ID, word.surah)
        dataBundle.putInt(Constant.AYAH_ID, Math.toIntExact(word.ayah.toLong()))
        //LoadItemList(dataBundle, word)
        val homeactivity = Intent(activity, SentenceGrammarAnalysis::class.java)
        homeactivity.putExtras(dataBundle!!)
        //  val homeactivity = Intent(this@MainActivity, DownloadListActivity::class.java)

       // intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(homeactivity)


    }

    private fun bookMarkSelected(position: Int, bookmarkview: View?) {
        val chapter_no = corpusayahWordArrayList[position].word[0].surahId
        val verse = corpusayahWordArrayList[position].word[0].verseId
        val surahArrays: Array<String> = resources.getStringArray(R.array.suraharabic)
        val surahname = surahArrays[chapter_no - 1]
        val en = BookMarks()
        en.chapterno = chapter_no.toString()
        en.verseno = verse.toString()
        en.surahname = surahname
        //     Utils utils = new Utils(ReadingSurahPartActivity.this);
        //  val utils = Utils(this)
        val vm: QuranViewModel by viewModels()
        vm.Insertbookmark(en)
        // utils.insertBookMark(en)
        val snackbar = Snackbar
            .make((bookmarkview)!!, "BookMark Created", Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.setTextColor(Color.CYAN)
        snackbar.setBackgroundTint(Color.BLACK)
        snackbar.show()
    }

    override fun onItemLongClick(position: Int, v: View) {
        Toast.makeText(requireContext(), "long click", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
//todo        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        if ((activity as AppCompatActivity?)?.supportActionBar != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        }
    }

    override fun onStop() {
        super.onStop()
        if ((activity as AppCompatActivity?)?.supportActionBar != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }
    }
}