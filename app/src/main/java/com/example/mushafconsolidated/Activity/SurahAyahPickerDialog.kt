package com.example.mushafconsolidated.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mushafconsolidated.Entities.ChaptersAnaEntity
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.Utils
import wheel.OnWheelChangedListener
import wheel.WheelView

class SurahAyahPickerDialog(
    private val context: Context,
    private val listener: SurahAyahPickerListener,
   private var currentSelectSurah: Int,
    private var ayah: Int
) {

    companion object {
        const val CHAPTER = "chapter"
        const val SURAH_ARABIC_NAME = "suraharabicname"
    }

    private lateinit var dialog: AlertDialog
    private lateinit var surahWheel: WheelView
    private lateinit var ayahWheel: WheelView
    private lateinit var textView: TextView
   // private var currentSelectSurah: Int = 1 // Default to Surah 1
  //  private var ayah: Int = 1 // Default to Ayah 1
    private lateinit var surahNameEnglish: String
    private lateinit var surahArabicName: String

    // Data sources (can be passed in or fetched here)
    private val surahArrays: Array<String> = context.resources.getStringArray(R.array.surahdetails)
    private val verseArrays: Array<String> = context.resources.getStringArray(R.array.versescounts)

    private var soraList: List<ChaptersAnaEntity?>? = null // Fetch this from your repository

    fun show(isRefresh: Boolean, startTrue: Boolean, onSelectionComplete: (surah: Int, ayah: Int) -> Unit) {
        val builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_wheel_t, null) // Use your layout here
    val   utils=Utils(context)
        soraList=utils.getAllAnaChapters()
        textView = view.findViewById(R.id.textView2)
        surahWheel = view.findViewById(R.id.wv_year)
        ayahWheel = view.findViewById(R.id.wv_month)

        initializeWheels()

        builder.setView(view)
            .setPositiveButton("Done") { _: DialogInterface, _: Int ->
                handleSelection(isRefresh, startTrue, onSelectionComplete)
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        dialog = builder.create()
        styleDialog()
        dialog.show()
    }

    private fun initializeWheels() {

        surahWheel.setEntries(*surahArrays)
        surahWheel.currentIndex = currentSelectSurah - 1
        ayahWheel.currentIndex=ayah-1

        updateAyahWheel(currentSelectSurah - 1)

        surahWheel.onWheelChangedListener = object : OnWheelChangedListener {
            override fun onChanged(wheel: WheelView, oldIndex: Int, newIndex: Int) {
                updateAyahWheel(newIndex)
            }
        }

        ayahWheel.onWheelChangedListener = object : OnWheelChangedListener {
            override fun onChanged(wheel: WheelView, oldIndex: Int, newIndex: Int) {
                ayah = newIndex + 1 // Update selected ayah
            }
        }
    }

    private fun updateAyahWheel(surahIndex: Int) {
        currentSelectSurah = surahIndex + 1
        val verseCount = verseArrays[surahIndex].toInt()
        val ayahEntries = (1..verseCount).map { it.toString() }.toTypedArray()
        ayahWheel.setEntries(*ayahEntries)
        ayahWheel.currentIndex = ayah - 1 // Set selected ayah
        updateTextView()
    }

    private fun updateTextView() {
        val surahText = surahArrays[currentSelectSurah - 1]
        val ayahText = ayah.toString()
        textView.text = "$surahText/$ayahText"
    }

    private fun handleSelection(isRefresh: Boolean, startTrue: Boolean, onSelectionComplete: (surah: Int, ayah: Int) -> Unit) {
        val selectedSurah = currentSelectSurah
        val selectedAyah = ayah

        // Update surah details (fetch from repository if needed)
        if (soraList != null && selectedSurah in 1..soraList!!.size) {
            val surahEntity = soraList!![selectedSurah - 1]
            surahNameEnglish = surahEntity?.nameenglish ?: ""
            surahArabicName = surahEntity?.namearabic ?: ""

            // Save last read surah (if needed)
            val pref: SharedPreferences = context.getSharedPreferences("lastreadmushaf", AppCompatActivity.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(CHAPTER, selectedSurah)
            editor.putString(SURAH_ARABIC_NAME, surahArabicName)
            editor.apply()
        }

        // Handle refresh or range updates (adapt this logic to your needs)
        if (isRefresh && startTrue) {
            listener.onReleasePlayer()
            listener.onRefreshActivity(selectedSurah.toString(), selectedAyah.toString(), false)
        //    RefreshActivity(sura, aya, false)
        } else if (startTrue) {
            listener.onUpdateStartRange(selectedAyah,selectedSurah,surahNameEnglish)
          //  rangevalues.add(verse)
        } else {
            listener.onUpdateEndRange(selectedAyah,selectedSurah,surahNameEnglish)
           // rangevalues.add(verse)
        }

        onSelectionComplete(selectedSurah, selectedAyah) // Notify the caller
    }

    private fun styleDialog() {
        // Apply styling based on preferences (adapt as needed)
        val preferences = context.getSharedPreferences("your_prefs_name", AppCompatActivity.MODE_PRIVATE)
            .getString("themepref", "dark")

        val backgroundColor = when (preferences) {
            "light" -> R.color.md_theme_dark_onSecondary
            "brown" -> R.color.background_color_light_brown
            "blue" -> R.color.prussianblue
            "green" -> R.color.mdgreen_theme_dark_onPrimary
            else -> R.color.odd_item_bg_dark_blue_light // Default
        }

        dialog.window?.setBackgroundDrawableResource(backgroundColor)

        // Style buttons (adapt as needed)
        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        // ... set colors based on preferences

        // Set dialog window attributes (adapt as needed)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp
        dialog.window?.setGravity(Gravity.TOP)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
}