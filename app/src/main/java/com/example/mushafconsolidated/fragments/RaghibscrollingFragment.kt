package com.example.mushafconsolidated.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.databinding.FragmentRaghibscrollingBinding
import com.example.utility.PreferenceUtil
import com.example.utility.QuranGrammarApplication
import org.sj.conjugator.activity.BaseActivity
import org.sj.conjugator.utilities.SharedPref


class ScrollingFragment : Fragment() {
    var URDU_FONTS: String = "fonts/Mehr.ttf"
    private var _binding: FragmentRaghibscrollingBinding? = null
    private val binding get() = _binding!!
    private var prefs: SharedPref? = null
    private val surahName: String? = null
    private var isdark: String? = null
    var pref: PreferenceUtil? = null
    companion object {
        private const val ARG_CHAPTER_NO = "chapterno"
        private const val ARG_VERSE_NO = "verseno"
        private const val ARG_SURA_NAME = "surahname"
        private const val ARG_ismakkimadani = "makkimadani"
        // Factory method to create a new instance of the fragment with arguments
        fun newInstance(chapterno: Int, verseno: Int, surahArabicName: String, isMakkiMadani: Int): ScrollingFragment {
            val fragment = ScrollingFragment()
            val args = Bundle().apply {
                putInt(ARG_CHAPTER_NO, chapterno)
                putInt(ARG_VERSE_NO, verseno)
                putString(ARG_SURA_NAME, surahArabicName)
                putInt(ARG_ismakkimadani, isMakkiMadani)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRaghibscrollingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the current theme (adjust this based on how you share data between activity and fragment)
        val currentTheme = arguments?.getString("currentTheme") ?: BaseActivity.DARK_THEME

        // Apply background color based on theme
        val backgroundColor = when (currentTheme){
            BaseActivity.DARK_THEME -> ContextCompat.getColor(requireContext(), R.color.bg_brown)
            else -> ContextCompat.getColor(requireContext(), R.color.md_db__theme_dark_background)
        }
        view.setBackgroundColor(backgroundColor)
        // Retrieve the arguments
        val chapterno = arguments?.getInt(ARG_CHAPTER_NO) ?: 0
        val verseno = arguments?.getInt(ARG_VERSE_NO) ?: 0
        val isMakkiMadani = arguments?.getInt(ARG_ismakkimadani) ?: 0
        val surhaname=arguments?.getString(ARG_SURA_NAME)
        val utils = Utils(QuranGrammarApplication.context)
        val tafseer = utils.getShaikhTafseerAya(chapterno,verseno)


        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context!!)

        isdark = sharedPreferences.getString("themepref", "dark").toString()

        setWordByWord(tafseer?.get(0)!!.words)


        // Display the chapter number and verse number
//        binding.tvChapterNo.text = "Chapter No: $chapterno"
//        binding.tvVerseNo.text = "Verse No: $verseno"
        if (chapterno== 1 || chapterno == 9 || verseno != 1) {
            binding.startText.setVisibility(View.GONE)
        }
      //  binding.ayahWords.text = tafseer?.get(0)!!.words
        val surahInfo = java.lang.StringBuilder()
        surahInfo.append(surhaname + ".")
        surahInfo.append(chapterno.toString() + ".")
        surahInfo.append(verseno)
        //   surahInfo.append( "");
        //  surahInfo.append(verse.getRukuSurahNumber());
        //  surahname,verseno,chapterno,chaperruku,surahrukuh
        //   getFormattedRukuInfo(verse.getSurahName(),verse.getAyahNumber(),verse.getParahNumber(),verse.getSurahNumber(),verse.getRukuParahNumber(),verse.getRukuSurahNumber())
        val drawableRes = if (isMakkiMadani == 1) R.drawable.ic_makkah_48 else R.drawable.ic_madinah_48
        binding.rukuInfoText.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)

        val tintColor = if (isdark == "dark") Color.GREEN else Color.BLACK
        binding.rukuInfoText.setCompoundDrawableTintList(ColorStateList.valueOf(tintColor))
        binding.rukuInfoText.text = surahInfo.toString()
 /*       val py = Python.getInstance()
        val module = py.getModule("python/langdetect_script.py") // Use your Python file name
        val result = module.callAttr("detect_languages", yourCombinedText)

        val arabicText = result.asList().get(0).toString()
        val urduText = result.asList().get(1).toString()*/
        binding.ayahText.text = tafseer?.get(0)!!.ayahTextQalam
        binding.ayahTranslation.text = tafseer?.get(0)!!.translation
        binding.ayahTafseer.text = tafseer?.get(0)!!.tafseer
        binding.ayahTranslation.setTypeface(
            Typeface.createFromAsset(requireContext().assets,
                URDU_FONTS
            )
        )


        // Implement additional logic here as needed
    }


    private fun setWordByWord(words: String) {
        var colorizedAyahWords: String

        if (isdark == "dark") {
            // if (SharedPref.themePreferences().equals("dark")) {
            colorizedAyahWords = getColorizedAyahWords(words, Color.YELLOW, Color.GREEN)
        } else {
            colorizedAyahWords = getColorizedAyahWords(
                words,
                Color.BLUE,
                ContextCompat.getColor(QuranGrammarApplication.context!!, R.color.burntamber)
            )
        }
        colorizedAyahWords = colorizedAyahWords.replace("\n", "<br/>").replace("\\n", "<br/>")
        binding.ayahWords.text = HtmlCompat.fromHtml(colorizedAyahWords, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setHtmlText(colorizedAyahWords: String) {
        TODO("Not yet implemented")
    }

    private fun getColorizedAyahWords(ayahwords: String, yellow: Int, green: Int): String {
        var ayahwords = ayahwords
        val stringBuilder2 = StringBuilder()
        stringBuilder2.append("<font color=\"")
        stringBuilder2.append(yellow)
        stringBuilder2.append("\">")
        val stringBuilder3 = StringBuilder()
        stringBuilder3.append("</font><font color=\"")
        stringBuilder3.append(green)
        stringBuilder3.append("\"> :")
        ayahwords = ayahwords.replace(":", stringBuilder3.toString())
        var stringBuilder1 = StringBuilder()
        stringBuilder1.append("</font><font color=\"")
        stringBuilder1.append(yellow)
        stringBuilder1.append("\">&nbsp;&nbsp;")
        ayahwords = ayahwords.replace("|", stringBuilder1.toString())
        stringBuilder1 = StringBuilder()
        stringBuilder1.append("</font><font color=\"")
        stringBuilder1.append(yellow)
        stringBuilder1.append("\">\n")
        stringBuilder2.append(
            ayahwords.replace("\n", stringBuilder1.toString()).replace("\n", "<br/>")
        )

        return stringBuilder2.toString()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

