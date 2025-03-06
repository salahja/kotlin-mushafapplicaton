package com.example.mushafconsolidated.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mushafconsolidated.R

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener

import java.util.Locale

class TextSpeechAct : AppCompatActivity(), OnInitListener {

  private var textToSpeech: TextToSpeech? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_text_speech)

    // Initialize TextToSpeech
    textToSpeech = TextToSpeech(this, this)
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      // Set language to Arabic
      val result = textToSpeech?.setLanguage(Locale("ar"))

      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        // Language not supported
        println("Arabic language is not supported.")
      } else {
        // Text to speech is ready
        val arabicText = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
        textToSpeech?.speak(arabicText, TextToSpeech.QUEUE_FLUSH, null, null)
      }
    } else {
      println("TextToSpeech initialization failed.")
    }
  }

  override fun onDestroy() {
    // Shutdown TextToSpeech
    if (textToSpeech != null) {
      textToSpeech?.stop()
      textToSpeech?.shutdown()
    }
    super.onDestroy()
  }
}