package org.sj.conjugator.activity

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.Constant.QURAN_VERB_ROOT
import com.example.Constant.QURAN_VERB_WAZAN
import com.example.Constant.VERBMOOD
import com.example.Constant.VERBTYPE
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.databinding.ActivityNewtabsBinding
import com.example.utility.QuranGrammarApplication
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import org.sj.conjugator.fragments.FragmentIsmIsmAla
import org.sj.conjugator.fragments.FragmentIsmZarf
import org.sj.conjugator.fragments.FragmentIsmfaelIsmMafools
import org.sj.conjugator.fragments.FragmentVerb
import org.sj.conjugator.fragments.MazeedTabSagheerFragmentVerb
import org.sj.conjugator.utilities.ArabicLiterals
import org.sj.conjugator.utilities.SharedPref
import com.example.Constant
import com.example.Constant.AYAHNUMBER
import com.example.Constant.SURAH_ID
import com.example.Constant.WORDNUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConjugatorTabsActivity :  BaseActivity() {
    // Arrey of strings FOR TABS TITLES
    private val viewModel by viewModels<ConjugatorViewModel>()
    private var _binding: ActivityNewtabsBinding? = null
    private val thulathientitles = arrayOf(
        "Sarf Sagheer",
        "Verb Conjugaton",
        "Active/Passive PCPL",
        "N.Of Instrument",
        "N.Place/Time"
    )
    private val thulathiartitles = arrayOf(
        "صرف صغير",
        "تصريف الأفعال ",
        "لاسم الفاعل/الاسم المفعول",
        "الاسم الآلة",
        "الاسم الظرف"
    )
    private val mazeedentitles =
        arrayOf("Sarf Sagheer", "Verb Conjugaton", "Active/Passive Participle")
    private val mazeedartitles = arrayOf("صرف صغير", "تصريف الأفعال ", "لاسم الفاعل/الاسم المفعول")
    var dataBundle: Bundle? = null
    private var ismujarrad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtabs)

        // Handle data persistence
        if (savedInstanceState == null) {
            viewModel.tabData.value = intent.extras?.let {
                Bundle().apply { putAll(it) }
            }
        }
        setupViewPager()
        val fm: FragmentManager = supportFragmentManager
        val sa: ViewStateAdapter = ViewStateAdapter(fm, lifecycle)
        val viewPager: ViewPager2 = findViewById(R.id.pager)
        viewPager.adapter = sa
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        dataBundle = Bundle()
        val bundle: Bundle? = intent.extras
        val root = bundle?.getString(QURAN_VERB_ROOT)
        val ss = root!!.replace("[\\[\\]]".toRegex(), "")
        var verbroot = ss.replace("[,;\\s]".toRegex(), "")
        val starts = verbroot.indexOf(ArabicLiterals.LALIF)
        if (starts != -1) {
            verbroot = verbroot.replace(ArabicLiterals.LALIF, "ء")
        }
        var verbform: String? = null
        var verbmood: String? = null
        var verbtype: String? = null
        var surahid: String? = null
        var ayahnumber: String? = null
        var wordno: String? = null
        var meaning: String?=null

        if (bundle != null) {
            verbform = bundle.getString(QURAN_VERB_WAZAN)
            verbmood = bundle.getString(VERBMOOD)
            verbtype = bundle.getString(VERBTYPE)
            surahid = bundle.getString(SURAH_ID)
            ayahnumber = bundle.getString(AYAHNUMBER)
            wordno = bundle.getString(WORDNUMBER)


        }
        ismujarrad = verbtype == "mujarrad"
        dataBundle!!.putSerializable(QURAN_VERB_ROOT, verbroot)
        dataBundle!!.putString(QURAN_VERB_WAZAN, verbform)
        dataBundle!!.putString(VERBMOOD, verbmood)
        dataBundle!!.putString(VERBTYPE, verbtype)
        dataBundle!!.putString(SURAH_ID, surahid)
        dataBundle!!.putString(AYAHNUMBER, ayahnumber)
        dataBundle!!.putString(WORDNUMBER, wordno)
        val sharedPref = SharedPref(
            QuranGrammarApplication.context!!
        )

        // Up to here, we have working scrollable pages
        if (ismujarrad) {
            if (SharedPref.language.equals("en")) {
                TabLayoutMediator(
                    tabLayout,
                    viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = thulathientitles[position]
                }.attach()
            } else {
                TabLayoutMediator(
                    tabLayout,
                    viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = thulathiartitles[position]
                }.attach()
            }
        } else {
            if (SharedPref.language.equals("en")) {
                TabLayoutMediator(
                    tabLayout,
                    viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = mazeedentitles[position]
                }.attach()
            } else {
                TabLayoutMediator(
                    tabLayout,
                    viewPager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.text = mazeedartitles[position]
                }.attach()
            }
        }
        // Now we have tabs, NOTE: I am hardcoding the order, you'll want to do something smarter
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        // And now we have tabs that, when clicked, navigate to the correct page
    }


    private fun setupViewPager() {
        val adapter = ViewStateAdapter(supportFragmentManager, lifecycle)
        _binding?.pager?.adapter = adapter
        // ... rest of your setup
    }
    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= 33) {
            // Preemptively handle media permissions
            val permission = Manifest.permission.READ_MEDIA_IMAGES
            if (checkSelfPermission(permission) != PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 101)
            }
        }
    }

    private inner class ViewStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return if (ismujarrad) {
                NUM_PAGES_THULATHI
            } else {
                NUM_PAGES_MAZEED
            }
        }
        // ... (other code)

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MazeedTabSagheerFragmentVerb().apply {  arguments = viewModel.tabData.value?.clone() as Bundle }
                1 -> FragmentVerb().apply {  arguments = viewModel.tabData.value?.clone() as Bundle }
                2 -> FragmentIsmfaelIsmMafools().apply {  arguments = viewModel.tabData.value?.clone() as Bundle }
                3 -> FragmentIsmIsmAla().apply {  arguments = viewModel.tabData.value?.clone() as Bundle }
                4 -> FragmentIsmZarf().apply {  arguments = viewModel.tabData.value?.clone() as Bundle }
                else -> FragmentVerb().apply {  arguments = viewModel.tabData.value?.clone() as Bundle }
            }
        }

        // ... (other code)
    }


    companion object {
        private const val NUM_PAGES_THULATHI = 5
        private const val NUM_PAGES_MAZEED = 3
    }
}
class ConjugatorViewModel : ViewModel() {
    val tabData = MutableLiveData<Bundle>()
}

