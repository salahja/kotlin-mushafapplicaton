package com.example.mushafconsolidated.Activityimport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.mushafconsolidated.R


open class BaseActivity : AppCompatActivity() {
    private var currenttheme: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        currenttheme =
            PreferenceManager.getDefaultSharedPreferences(this).getString("themepref", "dark")
        super.onCreate(savedInstanceState)
      //  DynamicColors.applyToActivitiesIfAvailable(this@BaseActivity.application)
        switchTheme(currenttheme)
    }

    //  protected  void setTheme(){
    //  switchTheme(currenttheme);
    // }
    protected fun switchTheme(currenttheme: String?) {
        when (currenttheme) {
            LIGHT_THEME -> setTheme(R.style.AppThemeLight)
            DARK_THEME -> setTheme(R.style.AppThemeDark)
            DARK_BLUE -> setTheme(R.style.AppTheme_materialdarkblue)
            DARK_GREEN -> setTheme(R.style.AppTheme_DarkGreen)
            BROWN_MODE -> setTheme(R.style.Theme_Browns)
            else -> setTheme(R.style.AppThemeDark)
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("theme", currenttheme)
            .apply()
    }

    companion object {
        const val LIGHT_THEME = "light"
        const val DARK_THEME = "dark"
        const val DARK_BLUE = "blue"
        const val DARK_GREEN = "green"
        const val BROWN_MODE = "brown"
    }
}