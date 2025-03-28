package com.example.mushafconsolidated.Activity


import com.example.mushafconsolidated.Activityimport.BaseActivity
import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import androidx.window.layout.WindowMetricsCalculator

import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.settingsimport.Constants.Companion.DATABASENAME
import com.example.mushafconsolidated.settingsimport.Constants.Companion.DATABASEZIP
import com.example.mushafconsolidated.settingsimport.Constants.Companion.FILEPATH
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var newquran: File? = null
    private var recview: RecyclerView? = null
    private val viewModel: MainViewModel by viewModels()

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Splash screen setup
        installSplashScreen().setKeepOnScreenCondition { true }

        // Theme and layout setup
        switchTheme("brown")
        setContentView(R.layout.main_activity)

        // Initialize UI components
        recview = findViewById(R.id.recycler_views)

        // Initialize preferences
        initPreferences()

        // Setup window size classes
        computeWindowSizeClasses()

        // Check storage and database
        checkStorageAndDatabase()
    }

    private fun initPreferences() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        if (sp.getInt("spl", 0) != 1) {
            PreferenceManager.setDefaultValues(this, R.xml.preferences, true)
            sp.edit().putInt("spl", 1).apply()
        }
    }

    private fun checkStorageAndDatabase() {
        lifecycleScope.launch {
            try {
                // Get proper storage directory
                val appDir = getExternalFilesDir("Mushafapplication") ?:
                throw IOException("External storage not available")

                newquran = File(appDir, DATABASENAME)

                when {
                    newquran?.exists() == true -> {
                        launchQuranGrammarActivity()
                    }
                    else -> {
                        checkStoragePermission()
                    }
                }
            } catch (e: Exception) {
                showErrorDialog("Initialization failed", e)
            }
        }
    }

    private fun checkStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> checkStoragePermissionApi34()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> checkStoragePermissionApi33()
            else -> checkStoragePermissionLegacy()
        }
    }

    @OptIn(UnstableApi::class)
    private fun checkStoragePermissionApi34() {
        val permissionsToRequest = mutableListOf<String>().apply {
            if (!hasPermission(Manifest.permission.READ_MEDIA_IMAGES)) add(Manifest.permission.READ_MEDIA_IMAGES)
            if (!hasPermission(Manifest.permission.READ_MEDIA_VIDEO)) add(Manifest.permission.READ_MEDIA_VIDEO)
            if (!hasPermission(Manifest.permission.READ_MEDIA_AUDIO)) add(Manifest.permission.READ_MEDIA_AUDIO)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_READ_MEDIA_IMAGES)
        } else {
            startDatabaseSetup()
        }
    }

    @OptIn(UnstableApi::class)
    private fun checkStoragePermissionApi33() {
        if (!hasPermission(Manifest.permission.READ_MEDIA_IMAGES)) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_READ_MEDIA_IMAGES)
        } else {
            startDatabaseSetup()
        }
    }

    private fun checkStoragePermissionLegacy() {
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            startDatabaseSetup()
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun startDatabaseSetup() {
        lifecycleScope.launch {
            try {
                showProgressDialog("Preparing database...")
                validateFilesAndDownload()
            } catch (e: Exception) {
                showErrorDialog("Database setup failed", e)
            }
        }
    }

    @Throws(IOException::class)
    private suspend fun validateFilesAndDownload() {
        if (newquran?.exists() != true) {
            copyDatabase()
        }
        launchQuranGrammarActivity()
    }

    private suspend fun copyDatabase() {
        withContext(Dispatchers.IO) {
            val appDir = getExternalFilesDir("Mushafapplication") ?:
            throw IOException("External storage not available")

            val databaseFile = File(appDir, DATABASEZIP).apply {
                parentFile?.mkdirs()
                if (!exists()) createNewFile()
            }

            // Copy from assets
            assets.open(DATABASEZIP).use { input ->
                FileOutputStream(databaseFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Extract database
            extractDatabase(databaseFile, appDir)

            // Verify extraction
            val extractedDb = File(appDir, DATABASENAME)
            if (!extractedDb.exists() || extractedDb.length() < MIN_DB_SIZE) {
                throw IOException("Database extraction incomplete")
            }
        }
    }

    private suspend fun extractDatabase(zipFile: File, targetDir: File) {
        SevenZFile(zipFile).use { sevenZFile ->
            val buffer = ByteArray(8 * 1024 * 1024) // 8MB buffer
            var entry: SevenZArchiveEntry?

            while (sevenZFile.nextEntry.also { entry = it } != null) {
                if (entry?.isDirectory == true) continue

                val outputFile = File(targetDir, entry?.name ?: continue).apply {
                    parentFile?.mkdirs()
                }

                FileOutputStream(outputFile).use { output ->
                    var bytesRead: Int
                    while (sevenZFile.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                    }
                }
            }
        }
        zipFile.delete()
    }

    private fun launchQuranGrammarActivity() {
        if (isQuranGrammarActRunning()) return

        startActivity(Intent(this, QuranGrammarAct::class.java).also {
            finish()
        })
    }

    private fun isQuranGrammarActRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.appTasks?.any { task ->
            task.taskInfo?.topActivity?.className == QuranGrammarAct::class.java.name
        } ?: false
    }

    private fun showProgressDialog(message: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setView(R.layout.layout_loading_dialog)
            .show().also {
                viewModel.currentDialog = it
            }
    }

    private fun showErrorDialog(title: String, exception: Exception) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(exception.localizedMessage ?: "Unknown error")
            .setPositiveButton("Retry") { _, _ -> checkStorageAndDatabase() }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_MEDIA_IMAGES,
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    startDatabaseSetup()
                } else {
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Storage permissions are required for the app to function")
            .setPositiveButton("Retry") { _, _ -> checkStoragePermission() }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .show()
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)
        val widthDp = metrics.bounds.width() / resources.displayMetrics.density

        PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
            putString("width", when {
                widthDp < 600f -> "compactWidth"
                widthDp < 840f -> "mediumWidth"
                else -> "expandedWidth"
            })
            apply()
        }
    }

    companion object {
        private const val REQUEST_READ_MEDIA_IMAGES = 101
        private const val REQUEST_READ_EXTERNAL_STORAGE = 102
        private const val MIN_DB_SIZE = 250 * 1024 * 1024 // 250MB
    }
}


class MainViewModel : ViewModel() {
    var currentDialog: AlertDialog? = null
}

