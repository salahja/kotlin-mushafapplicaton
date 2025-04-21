package org.sj.conjugator.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.mushafconsolidated.R
import com.google.android.material.color.DynamicColors

open class BaseActivity : AppCompatActivity() {
    var currenttheme: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        currenttheme =
            PreferenceManager.getDefaultSharedPreferences(this).getString("themepref", "dark")
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivitiesIfAvailable(this.application)
        switchTheme(currenttheme)
    }
     fun switchTheme(currenttheme: String?) {
        val currentAppliedTheme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "dark")
        when (currenttheme) {
            BaseActivity.LIGHT_THEME -> setTheme(R.style.AppTheme)
            BaseActivity.DARK_THEME -> setTheme(R.style.AppThemeDark)
            BaseActivity.DARK_BLUE -> setTheme(R.style.AppTheme_materialdarkblue)
            BaseActivity.DARK_GREEN -> setTheme(R.style.AppTheme_DarkGreen)
            BaseActivity.BROWN_MODE -> setTheme(R.style.Theme_Browns)
            else -> setTheme(R.style.AppThemeDark)
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("theme", currenttheme)
            .apply()

        if (currenttheme != currentAppliedTheme) {
            recreate()
        }

    }

    companion object {
        const val LIGHT_THEME = "light"
        const val DARK_THEME = "dark"
        const val DARK_BLUE = "blue"
        const val DARK_GREEN = "green"
        const val BROWN_MODE = "brown"
    }
}