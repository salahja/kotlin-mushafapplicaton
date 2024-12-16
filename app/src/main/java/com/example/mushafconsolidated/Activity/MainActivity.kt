package com.example.mushafconsolidated.Activity


import com.example.mushafconsolidated.Activityimport.BaseActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.result.launch
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import androidx.window.layout.WindowMetricsCalculator

import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.settingsimport.Constants.Companion.DATABASENAME
import com.example.mushafconsolidated.settingsimport.Constants.Companion.DATABASEZIP
import com.example.mushafconsolidated.settingsimport.Constants.Companion.FILEPATH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class MainActivity : BaseActivity() {
    private var newquran: File? = null
    private var recview: RecyclerView? = null

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
        switchTheme("brown")
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            // Simulate work being done, replace with your actual logic
            runBlocking { delay(1000) }
            false
        }
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED
        computeWindowSizeClasses()
        //  setContentView(R.layout.fragment_reading);
        setContentView(R.layout.main_activity)
        recview = findViewById(R.id.recycler_views)
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        if (sp.getInt("spl", 0) != 1) {
            PreferenceManager.setDefaultValues(this, R.xml.preferences, true)
            sp.edit().putInt("spl", 1).apply()
        }
        newquran = File("$FILEPATH/$DATABASENAME")
        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE
            )
        } else {
            try {
                validateFilesAndDownload()
            } catch (e: IOException) {
                Log.e("MainActivity", "Error validating or downloading files", e)
            }
        }
        //  PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val editor = PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit()
        val widthDp = metrics.bounds.width() / resources.displayMetrics.density
        if (widthDp < 600f) {
            editor.putString("width", "compactWidth")
            editor.apply()
        } else if (widthDp < 840f) {
            editor.putString("width", "mediumWidth")
            editor.apply()
        } else {
            // widthWindowSizeClass = WindowSizeClass.EXPANDED;
            editor.putString("width", "expandedWidth")
            editor.apply()
        }
        val heightDp = metrics.bounds.height() / resources.displayMetrics.density
        val heightWindowSizeClass: WindowSizeClass = if (heightDp < 480f) {
            WindowSizeClass.COMPACT
        } else if (heightDp < 900f) {
            WindowSizeClass.MEDIUM
        } else {
            WindowSizeClass.EXPANDED
        }
        // Use widthWindowSizeClass and heightWindowSizeClass
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, // Use 'out' for non-nullable strings
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    validateFilesAndDownload()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    @Throws(IOException::class)
    private fun validateFilesAndDownload() {
        if (!newquran!!.exists()) {
            // first install copy newquran.db.zip and unzip
            //   new CopyDatabase().execute();
            copyDatbases()
        } else {

            val homeactivity = Intent(this@MainActivity, QuranGrammarAct::class.java)
            startActivity(homeactivity)

        }
    }

    private fun copyDatbases() {
        val ex = Executors.newSingleThreadExecutor()
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog)
        val dialog = builder.create()
        ex.execute(object : Runnable {
            override fun run() {
                runOnUiThread { dialog.show() }
                val canWrie = canWriteInSDCard()
                if (canWrie) {
                    try {
                        val databaseDirectory = File(FILEPATH)
                        if (!databaseDirectory.exists()) {
                            val cr = databaseDirectory.mkdirs()
                            println(cr)
                        }
                        val databaseFile = File(databaseDirectory, DATABASEZIP)
                        databaseFile.parentFile
                        if (!databaseFile.exists()) {
                            databaseFile.createNewFile()
                        }
                        //    InputStream inputStream = getApplicationContext().getAssets().open("newquran.db");
                        val inputStream = applicationContext.assets.open(DATABASEZIP)
                        val outputStream = FileOutputStream(databaseFile)
                        //   publishProgress(0, fileSize);
                        var copylength = 0
                        val buffer = ByteArray(1024)
                        while (true) {
                            val read = inputStream.read(buffer)
                            if (read == -1) break
                            copylength += read
                            //   publishProgress(copylength, fileSize);
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                        outputStream.close()
                        inputStream.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
                sevenExtraction(ex, dialog)
            }

            private fun canWriteInSDCard(): Boolean {
                val state = Environment.getExternalStorageState()
                return Environment.MEDIA_MOUNTED == state
            }
        })
    }

    private fun sevenExtraction(
        ex: ExecutorService,
        dialog: AlertDialog
    ) {
        runOnUiThread {
            val zipfile =
                File(getExternalFilesDir(null)!!.absolutePath + getString(R.string.app_folder_path) + File.separator + DATABASEZIP)
            val targetDirectory = File(FILEPATH)
            val mainDatabasesZIP = File(zipfile.toString())
            var zis: ZipInputStream? = null
            var progress = 1

            val sevenZFile = SevenZFile(mainDatabasesZIP)
            try {
                var entry: SevenZArchiveEntry?
                val buffer= ByteArray(8192)
                while (sevenZFile.nextEntry.also { entry = it } != null) {
                    if (entry!!.isDirectory) continue // Skip directories

                    val file = File(targetDirectory, entry!!.name)
                    val dir = file.parentFile
                    if (!dir.isDirectory && !dir.mkdirs()) {
                        throw FileNotFoundException("Failed to ensure directory: " + dir.absolutePath)
                    }

                    FileOutputStream(file).use { fout ->
                        var count: Int
                        while (sevenZFile.read(buffer).also { count = it } != -1) {
                            fout.write(buffer, 0, count)
                            progress += 1
                            // Update progress bar if needed
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle errors appropriately
            } finally {
                sevenZFile.close()
                mainDatabasesZIP.delete()
                // Dismiss progress dialog if used
            }
            ex.shutdown()
            dialog.dismiss()
            val zipintent = Intent(this@MainActivity, QuranGrammarAct::class.java)
            startActivity(zipintent)
            finish()
        }
    }





    enum class WindowSizeClass {
        COMPACT, MEDIUM, EXPANDED
    }

    companion object {
        private const val REQUEST_WRITE_STORAGE = 112
    }
}


