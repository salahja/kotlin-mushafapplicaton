package mufradat

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.Constant.MAKKI_MADANI
import com.example.Constant.SURAH_ARABIC_NAME
import com.example.Constant.SURAH_ID
import com.example.Constant.VERSESCOUNT
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import org.sj.conjugator.activity.BaseActivity

class MufradatPagerActivity : BaseActivity() {
    var viewPagertwo: ViewPager2? = null
    var utils: Utils? = null
    var adapter: MufradatPagerAdapter? = null
    private var surah_id = 0
    private var versescount = 0
    private var suraharabicname: String? = null
    private var isMakkiMadani = 0
    private var materialToolbar: Toolbar? = null
    private var spin: Spinner? = null
    private var surahArray: ArrayList<ChaptersAnaEntity?>? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.grammer_menu_spinner, menu)
        val item = menu.findItem(R.id.grammarbarspinner)

        spin = MenuItemCompat.getActionView(item) as Spinner

        materialToolbar = findViewById(R.id.mufradattoolbar)
        materialToolbar!!.setTitle("Raghib AlSifhani")
        setuptoolbar()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setuptoolbar() {
        //  TypedArray imgs = getResources().obtainTypedArray(R.array.quran_imgs);
        val surahnamelist = resources.getStringArray(R.array.quran_names)
        val imgs = resources.obtainTypedArray(R.array.sura_imgsspinner)
        val check = 0

        val adapter
                : ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            surahnamelist
        )
        adapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        //  spin.setAdapter(adapter);
        val customAdapter = MCustomerAdapter(this, surahnamelist, imgs)

        spin!!.adapter = customAdapter
        // spin.setAdapter(customAdapter);
        spin!!.isSelected = false // must
        //   spin.setSelection(0, true);
        spin!!.setSelection(surah_id, true)
        spin!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val surahArray = utils!!.getSingleChapter(i) as ArrayList<ChaptersAnaEntity?>?
                val SELECTEDSURAH = 0
                val namearabic = surahArray!![SELECTEDSURAH]!!.namearabic

                val ismakki = surahArray[SELECTEDSURAH]!!.ismakki
                val chapterid = surahArray[SELECTEDSURAH]!!.chapterid

                MufradatInit(chapterid, namearabic, ismakki)

                //   new MufradatFragment.QuranAyatSetup(surahno,  arabicname).invoke();
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //   Log.d(TAG, "ON OPTION MENU");
        when (item.itemId) {
            R.id.grammarbarspinner -> {
                //         Log.d(TAG, "ON OPTION MENU");
                setuptoolbar()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager)
        materialToolbar = findViewById(R.id.mufradattoolbar)
        setSupportActionBar(materialToolbar)
        val intent = intent
        val isExpanded = false
        if (null != intent) {
            surah_id = intent.getIntExtra(SURAH_ID, 1)
            versescount = intent.getIntExtra(VERSESCOUNT, 7)
            suraharabicname = intent.getStringExtra(SURAH_ARABIC_NAME) ?: "null"
            isMakkiMadani = intent.getIntExtra(MAKKI_MADANI, 0)
        } else {
            surah_id = 1
            versescount = 7
            suraharabicname = "\"الْفَاتِحَة\""
            isMakkiMadani = 1
        }


        materialToolbar!!.inflateMenu(R.menu.grammer_menu_spinner)
        //    spin = findViewById(R.id.grammarbarspinner);
        MufradatInit(surah_id, suraharabicname, isMakkiMadani)
    }

    private fun MufradatInit(surahno: Int, arabicname: String?, ismakki: Int) {
        this.surah_id = surahno
        this.suraharabicname = arabicname
        this.isMakkiMadani = ismakki
        val viewPagertwo = findViewById<ViewPager2>(R.id.viewpager2)


        utils = Utils(this@MufradatPagerActivity)
        val tafseerList: List<MufradatEntity> = utils!!.getShaikhTafseer(surah_id)


        surahArray = utils!!.getSingleChapter(surah_id) as ArrayList<ChaptersAnaEntity?>?
        //   adapter.makkiMadani(isMakkiMadani);
        val tafseerAdapter = MufradatPagerAdapter(
            suraharabicname!!,
            tafseerList as ArrayList<MufradatEntity>,
            viewPagertwo,
            isMakkiMadani,
            this
        )
        viewPagertwo.adapter = tafseerAdapter
        viewPagertwo.currentItem = 0
        //viewPagertwo.setAdapter(new MufradatPagerAdapter(MufradatPagerActivity.this, suraharabicname, tafseerList, viewPagertwo,isMakkiMadani, this));
    }


    companion object {
        private const val TAG = " "
    }
}
