/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.utility

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager

import com.example.utility.ThemeHelper.applyTheme
import dagger.hilt.android.HiltAndroidApp
import database.verbrepo.VerbGraph
import leakcanary.LeakCanary

import java.util.Locale
@HiltAndroidApp
class QuranGrammarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
/*
       if (LeakCanary.isInAnalyzerProcess(this)) {
            // Skip LeakCanary initialization for the analyzer process
            return
        }
        LeakCanary.install(this)
*/


        if (context == null) {
            context = this
        }
      
        VerbGraph.provide(this)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //  String theme = sharedPreferences.getString("theme", 1);
        val themePref = sharedPreferences.getString("themepref", "white")
        applyTheme(themePref!!)
    }

    companion object {
        @JvmField
        var context: Context? = null

        //Check application language
        val instance: Context?
            get() {
                //Check application language
                var locale: Locale
                val config = Configuration()
                context!!.resources.updateConfiguration(config, context!!.resources.displayMetrics)
                return context
            }
    }
}