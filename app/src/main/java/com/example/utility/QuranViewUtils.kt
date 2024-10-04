package com.example.utility

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.compose.ui.semantics.setText
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.core.text.color
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import androidx.media3.common.C
import com.example.Constant
import com.example.mushafconsolidated.Adapters.FlowAyahWordAdapterNoMafoolat.ItemViewAdapter
import com.example.mushafconsolidated.Adapters.RevalationCity
import com.example.mushafconsolidated.Entities.CorpusEntity
import com.example.mushafconsolidated.Entities.NounCorpus
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Entities.VerbCorpus
import com.example.mushafconsolidated.Entities.wbwentity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import com.example.mushafconsolidated.fragments.WordMorphologyDetails
import com.example.mushafconsolidated.model.QuranCorpusWbw
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils

object QuranViewUtils {

    fun setWordClickListener(view: View, context: Context, word: QuranCorpusWbw, surahName: String, loadItemList: (Bundle, wbwentity) -> Unit) {
        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle(word.corpus!!.araone + word.corpus!!.aratwo + word.corpus!!.arathree + word.corpus!!.arafour + word.corpus!!.arafive)

            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.corpus!!.surah)
            dataBundle.putInt(Constant.AYAHNUMBER, word.corpus!!.ayah)
            dataBundle.putInt(Constant.WORDNUMBER, word.corpus!!.wordno)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahName)
            loadItemList(dataBundle,word.wbw)

        }
    }

    fun NewsetWordClickListener(view: View, context: Context, word: CorpusEntity, surahName: String, loadItemList: (Bundle, CorpusEntity) -> Unit) {
        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle(word.araone + word.aratwo + word.arathree + word.arafour + word.arafive)

            val dataBundle = Bundle()
            dataBundle.putInt(Constant.SURAH_ID, word.surah)
            dataBundle.putInt(Constant.AYAHNUMBER, word.ayah)
            dataBundle.putInt(Constant.WORDNUMBER, word.wordno)
            dataBundle.putString(Constant.SURAH_ARABIC_NAME, surahName)
            loadItemList(dataBundle,word)

        }
    }


    fun setTranslationText(textView: TextView, translationNote: TextView, entity: QuranEntity?, translationKey: String, noteResId: Int) {
        if (entity != null) {
            textView.text = when (translationKey) {
                "en_arberry" -> entity.en_arberry
                "en_sahih" -> entity.translation
                "en_jalalayn" -> entity.en_jalalayn
                "ur_jalalayn" -> entity.ur_jalalayn
                "ur_junagarhi" -> entity.ur_junagarhi

                // ... other translations
                else -> return
            }
        }
        translationNote.setText(noteResId)
        textView.visibility = View.VISIBLE
        translationNote.visibility = View.VISIBLE
    }

    fun showWordMorphologyTooltip(context: Context, view: View, word: QuranCorpusWbw, isNightmode: String) {
        val utils = Utils(QuranGrammarApplication.context!!)

        val verbCorpusRootWords =
            utils.getQuranRoot(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        if (verbCorpusRootWords.isNotEmpty() && verbCorpusRootWords[0].tag == "V") {
            //    vbdetail = ams.getVerbDetails();
            print("check")
        }
        val corpusNounWord:List<NounCorpus> =
            utils.getQuranNouns(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        val verbCorpusRootWord : List<VerbCorpus> =
            utils.getQuranRoot(
                word.corpus.surah,
                word.corpus.ayah,
                word.corpus.wordno
            )
        val qm = WordMorphologyDetails(
            word.corpus,
            corpusNounWord, verbCorpusRootWord
        )
        val workBreakDown = qm.workBreakDown

        val backgroundColor = when (isNightmode) {
            "dark", "blue", "green" -> R.color.background_color
            "brown","light" -> R.color.background_color
            else -> R.color.background_color_light_brown
        }

        SimpleTooltip.Builder(context)
            .anchorView(view)
            .text(workBreakDown)
            .backgroundColor(backgroundColor)
            .gravity(Gravity.TOP)
            .modal(true)
            .arrowDrawable(android.R.drawable.ic_media_previous)
            .arrowHeight(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .arrowWidth(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .build()
            .show()
    }

    fun NewshowWordMorphologyTooltip(context: Context, view: View, word: CorpusEntity, isNightmode: String) {
        val utils = Utils(QuranGrammarApplication.context!!)

        val verbCorpusRootWords =
            utils.getQuranRoot(
                word.surah,
                word.ayah,
                word.wordno
            )
        if (verbCorpusRootWords.isNotEmpty() && verbCorpusRootWords[0].tag == "V") {
            //    vbdetail = ams.getVerbDetails();
            print("check")
        }
        val corpusNounWord:List<NounCorpus> =
            utils.getQuranNouns(
                word.surah,
                word.ayah,
                word.wordno
            )
        val verbCorpusRootWord : List<VerbCorpus> =
            utils.getQuranRoot(
                word.surah,
                word.ayah,
                word.wordno
            )
        val qm = WordMorphologyDetails(
            word,
            corpusNounWord, verbCorpusRootWord
        )
        val workBreakDown = qm.workBreakDown

        val backgroundColor = when (isNightmode) {
            "dark", "blue", "green" -> R.color.background_color
            "brown","light" -> R.color.background_color
            else -> R.color.background_color_light_brown
        }

        SimpleTooltip.Builder(context)
            .anchorView(view)
            .text(workBreakDown)
            .backgroundColor(backgroundColor)
            .gravity(Gravity.TOP)
            .modal(true)
            .arrowDrawable(android.R.drawable.ic_media_previous)
            .arrowHeight(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .arrowWidth(SimpleTooltipUtils.pxFromDp(50f).toInt().toFloat())
            .build()
            .show()
    }
    fun setIconColors(surahInfoTextView: TextView, isNightMode: String, isMakkiMadani: Int) {
        val icon = if (isMakkiMadani == 1) {
            R.drawable.ic_makkah_48
        } else {
            R.drawable.ic_madinah_48
        }

        val tintColor = if (isNightMode == "dark" || isNightMode == "blue" || isNightMode == "green") {
            Color.CYAN
        } else {
            Color.BLUE
        }

        surahInfoTextView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        surahInfoTextView.compoundDrawableTintList = ColorStateList.valueOf(tintColor)
    }

    fun setLocationVisibility(makkiIcon: ImageView, madaniIcon: ImageView, location: RevalationCity) {
        makkiIcon.visibility = if (location == RevalationCity.MAKKI) View.VISIBLE else View.GONE
        madaniIcon.visibility = if (location == RevalationCity.MADANI) View.VISIBLE else View.GONE
    }




    fun setBackgroundColor(context: Context, view: View, isNightmode: String, isOdd: Boolean) {
        val color = when (isNightmode) {
            "brown" -> if (isOdd) R.color.bg_brown else R.color.odd_item_bg_brown
            "dark" -> if (isOdd) R.color.odd_item_bg_black else R.color.bg_black
            "blue" -> if (isOdd) R.color.bg_surface_dark_blue else R.color.bg_dark_blue
            else -> return
        }
        view.setBackgroundColor(ContextCompat.getColor(context, color))
    }


    fun getSpannedRoots(tag: QuranCorpusWbw, rootword: String): SpannableString {
        return CorpusUtilityorig.ColorizeRootword(
            tag.corpus!!.tagone!!,
            tag.corpus!!.tagtwo!!,
            tag.corpus!!.tagthree!!,
            tag.corpus!!.tagfour!!,
            tag.corpus!!.tagfive!!,
            rootword
        )!!
    }

    fun NewgetSpannedRoots(tag: CorpusEntity, rootword: String): SpannableString {
        return CorpusUtilityorig.ColorizeRootword(
            tag.tagone!!,
            tag.tagtwo!!,
            tag.tagthree!!,
            tag.tagfour!!,
            tag.tagfive!!,
            rootword
        )!!
    }

    fun NewgetSpannedWords(tag: CorpusEntity): SpannableString {
        return CorpusUtilityorig.NewSetWordSpan(
            tag.tagone,
            tag.tagtwo,
            tag.tagthree,
            tag.tagfour,
            tag.tagfive,
            tag.araone!!,
            tag.aratwo!!,
            tag.arathree!!,
            tag.arafour!!,
            tag.arafive!!
        )
    }

    fun getSpannedWords(tag: QuranCorpusWbw): SpannableString {
        return CorpusUtilityorig.NewSetWordSpan(
            tag.corpus!!.tagone,
            tag.corpus!!.tagtwo,
            tag.corpus!!.tagthree,
            tag.corpus!!.tagfour,
            tag.corpus!!.tagfive,
            tag.corpus!!.araone!!,
            tag.corpus!!.aratwo!!,
            tag.corpus!!.arathree!!,
            tag.corpus!!.arafour!!,
            tag.corpus!!.arafive!!
        )
    }

    fun storepreferences(context: Context,entity: QuranEntity, SurahName: String) {
        val pref = context.getSharedPreferences("lastread", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(Constant.SURAH_ID, entity.surah)
        editor.putInt(Constant.AYAH_ID, entity.ayah)
        editor.putString(Constant.SURAH_ARABIC_NAME, SurahName)
        editor.apply()
        // editor.commit();  // You don't need both apply() and commit()
    }



}