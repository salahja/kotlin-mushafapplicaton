package com.example.mushafconsolidated.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mushafconsolidated.R
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import com.example.mushafconsolidated.Activity.ShowMushafActivity.Companion.downloadLink
import com.example.mushafconsolidated.Activity.ShowMushafActivity.Companion.readerID
import com.example.mushafconsolidated.Entities.Qari
import com.example.mushafconsolidated.Entities.QuranEntity
import com.example.mushafconsolidated.Utils
import org.sj.conjugator.activity.BaseActivity
import java.util.Locale

import com.example.mushafconsolidated.databinding.PrdownloaderBinding
import com.example.mushafconsolidated.receiversimport.AudioAppConstants
import com.example.mushafconsolidated.receiversimport.QuranValidateSources
import timber.log.Timber


class PrdownloaderAct : BaseActivity(){

    private var editTextUrl: EditText? = null
    private var path: String? = null
    private var file_downloaded_path: TextView? = null
    private var file_name: TextView? = null
    private var downloading_percent: TextView? = null
    private var progressBar: ProgressBar? = null
    private var btnStart: Button? = null
    private var btnCancel: Button? = null
    private var buttonDownload: Button? = null
    private var details: LinearLayout? = null
    var downloadID: Int = 0
    var totalBytesToDownload = 0L
    var totalBytesDownloaded = 0L
    lateinit var binding: PrdownloaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrdownloaderBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // Initializing PRDownloader library
        PRDownloader.initialize(this)
        buttonDownload = binding.btnDownload


        // finding textview by its id
        file_downloaded_path = binding.txtUrl


        // finding textview by its id
        file_name = binding.fileName


        // finding progressbar by its id
        progressBar = binding.progressHorizontal


        // finding textview by its id
        downloading_percent = binding.downloadingPercentage


        // finding button by its id
        btnStart = binding.btnStart


        // finding button by its id
        btnCancel = binding.btnStop


        // finding linear layout by its id
        details = findViewById<LinearLayout>(R.id.details_box)

        //storing the path of the file
        path = Utils.getRootDirPath(this)

        val createDownloadLinks = createDownloadLinkss()
   /*     Log.d("TotalBytes", "Total bytes to download: $totalBytesToDownload")
        CoroutineScope(Dispatchers.Main).launch {
            totalBytesToDownload = calculateTotalBytesAsync(createDownloadLinks).await()
            Log.d("TotalBytes", "Total bytes to download: $totalBytesToDownload")
        }*/





         downloadFilesnew(createDownloadLinks)
    // downloadFilesop(createDownloadLinks)
    }

    private fun updateOverallProgress(filesDownloaded: Int, totalFiles: Int) {
        runOnUiThread {
            val progressText = "$filesDownloaded / $totalFiles files downloaded"
            Log.d("OverallProgress", "Overall progress = $progressText")

            // Calculate progress percentage based on files downloaded
            val progressPercentage = (filesDownloaded * 100) / totalFiles
            progressBar!!.progress = progressPercentage

            downloading_percent!!.text = progressText
        }
    }
    private fun downloadFilesnew(downloadLinks: List<String>) {
        val activeDownloads = mutableMapOf<Int, String>() // Track active downloads
        var filesDownloaded = 0
        val downloadId=0
        btnStart!!.setOnClickListener {
            val totalFiles = downloadLinks.size
            progressBar!!.max = 100 // Progress bar max set to 100% (we'll track this manually)
            progressBar!!.progress = 0
            downloading_percent!!.text = "0 / $totalFiles files downloaded"

            downloadLinks.forEachIndexed { index, url ->
                val fileName = URLUtil.guessFileName(url, null, null)
                file_name!!.text = "Downloading multiple files..."

                val downloadId = PRDownloader.download(url, path, fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        activeDownloads[downloadId] = fileName
                        updateDownloadStatus(activeDownloads)
                        btnStart!!.isEnabled = false
                        btnCancel!!.isEnabled = true
                        Toast.makeText(this@PrdownloaderAct, "Downloading $fileName started", Toast.LENGTH_SHORT).show()
                    }
                    .setOnPauseListener {
                        Toast.makeText(this@PrdownloaderAct, "Downloading $fileName paused", Toast.LENGTH_SHORT).show()
                    }
                    .setOnCancelListener {
                        activeDownloads.remove(downloadId)
                        updateDownloadStatus(activeDownloads)
                        Toast.makeText(this@PrdownloaderAct, "Downloading $fileName cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .setOnProgressListener { progress ->
                        // You could handle progress for individual files here if needed
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            activeDownloads.remove(downloadId)
                            filesDownloaded++
                            updateOverallProgress(filesDownloaded, totalFiles)
                            Toast.makeText(this@PrdownloaderAct, "Downloading $fileName completed", Toast.LENGTH_SHORT).show()

                            if (filesDownloaded == totalFiles) { // All downloads finished
                                btnStart!!.isEnabled = false
                                btnCancel!!.isEnabled = false
                                btnStart!!.text = "All Completed"
                            }
                        }

                        override fun onError(error: Error) {
                            activeDownloads.remove(downloadId)
                            updateDownloadStatus(activeDownloads)
                            Toast.makeText(this@PrdownloaderAct, "Error downloading $fileName", Toast.LENGTH_SHORT).show()
                            Log.d("DownloadError", "Error downloading $fileName: ${error.serverErrorMessage}")
                        }
                    })
            }
        }

        btnCancel!!.setOnClickListener {
            activeDownloads.keys.forEach { downloadId ->
                PRDownloader.cancel(downloadId)
            }
            activeDownloads.clear()
            updateDownloadStatus(activeDownloads)
            btnStart!!.text = "Start"
            btnStart!!.isEnabled = true
            btnCancel!!.isEnabled = false
        }
    }














    private fun createDownloadLinkss(): List<String> {

        val utils= Utils(this)
        val chap = utils.getSingleChapter(73)
        val quranbySurah: List<QuranEntity?>? = utils.getQuranbySurah(73)
         lateinit var readersList: List<Qari>
      val   selectedqari="Hani Al-Rifai"
        readersList = utils.qaris
        //   int ayaID=0;
        var counter = 0
        //   quranbySurah.add(0, new QuranEntity(1, 1, 1));
        val downloadLinks: MutableList<String> = java.util.ArrayList()
        //   ayaList.add(0, new Aya(1, 1, 1));
        //loop for all page ayat
//check if readerID is 0
        if (readerID == 0) {
            for (qari in readersList) {
                if (qari.name_english == selectedqari) {
                    readerID = qari.id
                    downloadLink = qari.url
                    break
                }
            }
        }
        if (quranbySurah != null) {
            for (ayaItem in quranbySurah) {
                //validate if aya download or not
                if (!QuranValidateSources.validateAyaAudiorx(
                        this,
                        readerID,
                        ayaItem!!.ayah,
                        ayaItem.surah
                    )
                ) {

                    //create aya link


                    //create aya link
                    val suraLength: Int =
                        chap!![0]!!.chapterid.toString().trim { it <= ' ' }.length
                    var suraID: String = chap[0]!!.chapterid.toString() + ""


                    val ayaLength = ayaItem.ayah.toString().trim { it <= ' ' }.length
                    //   int ayaLength = String.valueOf(ayaItem.ayaID).trim().length();
                    //   int ayaLength = String.valueOf(ayaItem.ayaID).trim().length();
                    var ayaID = java.lang.StringBuilder(
                        java.lang.StringBuilder().append(ayaItem.ayah).append("").toString()
                    )
                    if (suraLength == 1) suraID =
                        "00" + ayaItem.surah else if (suraLength == 2) suraID = "0" + ayaItem.surah

                    if (ayaLength == 1) {
                        ayaID = java.lang.StringBuilder("00" + ayaItem.ayah)
                    } else if (ayaLength == 2) {
                        ayaID = java.lang.StringBuilder("0" + ayaItem.ayah)
                    }
                    counter++
                    //add aya link to list
                    //chec
                    downloadLinks.add(downloadLink + suraID + ayaID + AudioAppConstants.Extensions.MP3)
                    Timber.tag("DownloadLinks").d(downloadLink + suraID + ayaID + AudioAppConstants.Extensions.MP3)
                }
            }
        }
        return downloadLinks
    }
    private fun updateOverallProgress(filesDownloaded: Int) {
        runOnUiThread {
            val overallPercentage = (filesDownloaded.toDouble() / progressBar!!.max) * 100
            val formattedPercentage = String.format("%.2f%%", overallPercentage)
            Log.d("OverallProgress", "Overall progress = $formattedPercentage")
            progressBar!!.progress = filesDownloaded
            downloading_percent!!.text = formattedPercentage
        }
    }
    private fun downloadFiles(downloadLinks: List<String>) {
        val activeDownloads = mutableMapOf<Int, String>() // Track active downloads
        val individualProgress = mutableMapOf<Int, Long>() // Track individual download progress
        val downloadId=0
       // totalBytesToDownload = calculateTotalBytes(downloadLinks)
        var filesDownloaded = 0


        btnStart!!.setOnClickListener {
            progressBar!!.max = downloadLinks.size * 100 // Set max based on total potential progress
            progressBar!!.progress = 0
            progressBar!!.max = 100
            progressBar!!.progress = 0
            downloadLinks.forEach { url ->
                val fileName = URLUtil.guessFileName(url, null, null)
                file_name!!.text = "Downloading multiple files..."

                PRDownloader.download(url, path, fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        activeDownloads[downloadId] = fileName
                        individualProgress[downloadId] = 0L // Initialize progress for this download
                        updateDownloadStatus(activeDownloads)
                        btnStart!!.isEnabled = false
                        btnCancel!!.isEnabled = true
                        Toast.makeText(this@PrdownloaderAct, "Downloading $fileName started", Toast.LENGTH_SHORT).show()
                    }
                    .setOnPauseListener {
                        Toast.makeText(this@PrdownloaderAct, "Downloading $fileName paused", Toast.LENGTH_SHORT).show()
                    }
                    .setOnCancelListener {
                        activeDownloads.remove(downloadId)
                        individualProgress.remove(downloadId)
                        updateDownloadStatus(activeDownloads)
                        filesDownloaded++
                        updateOverallProgress(filesDownloaded)
                      //;;  updateOverallProgress(individualProgress)
                        Toast.makeText(this@PrdownloaderAct, "Downloading $fileName cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .setOnProgressListener { progress ->
                        val progressPercentage= (progress.currentBytes * 100 / progress.totalBytes).toLong()
                        individualProgress[downloadId] = progressPercentage
                         }

                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            activeDownloads.remove(downloadId)
                            individualProgress[downloadId] = 100L // Mark as fully downloaded
                            updateDownloadStatus(activeDownloads)
                            filesDownloaded++
                            updateOverallProgress(filesDownloaded)
                                                 filesDownloaded++
                            updateOverallProgress(filesDownloaded)
                            Toast.makeText(this@PrdownloaderAct, "Downloading $fileName completed", Toast.LENGTH_SHORT).show()

                            if (activeDownloads.isEmpty()) { // All downloads finished
                                btnStart!!.isEnabled = false
                                btnCancel!!.isEnabled = false
                                btnStart!!.text = "All Completed"
                            }
                        }

                        override fun onError(error: Error) {
                            activeDownloads.remove(downloadId)
                            individualProgress.remove(downloadId)
                            updateDownloadStatus(activeDownloads)
                        //    updateOverallProgress(individualProgress)
                        Toast.makeText(this@PrdownloaderAct, "Error downloading $fileName", Toast.LENGTH_SHORT).show()
                            Log.d("DownloadError", "Error downloading $fileName: ${error.serverErrorMessage}")
                        }
                    })
            }

        }
        btnCancel!!.setOnClickListener {
            activeDownloads.keys.forEach { downloadId ->
                PRDownloader.cancel(downloadId)
            }
            activeDownloads.clear()
            individualProgress.clear()
            updateDownloadStatus(activeDownloads)
          //  updateOverallProgress(individualProgress)
            btnStart!!.text = "Start"
            btnStart!!.isEnabled = true
            btnCancel!!.isEnabled = false
        }
    }








    private fun updateDownloadStatus(activeDownloads: Map<Int, String>) {
        if (activeDownloads.isEmpty()) {
         file_name!!.text = "No active downloads"
            progressBar!!.progress = 0
            //downloading_percent!!.text = ""
            progressBar!!.isIndeterminate = false
        } else {
            file_name!!.text = "Downloading: ${activeDownloads.values.joinToString(", ")}"
        }
    }








    private fun updateOverallProgressxx(individualProgress: Map<Int, Long>, totalFiles: Int) {
        if (individualProgress.isNotEmpty()) {
            val overallProgress = individualProgress.values.sum().toDouble() / totalFiles
            val scaledProgress = (overallProgress / totalFiles) * 100

            // Ensure UI updates on the main thread
            runOnUiThread {
                val formattedPercentage= String.format("%.2f%%", scaledProgress * 100)

                Log.d("OverallProgress", "Scaled overall progress = $scaledProgress%")
                progressBar!!.progress = scaledProgress.toInt()
                downloading_percent!!.text = formattedPercentage
            }
        } else {
            runOnUiThread {
                progressBar!!.progress = 0
                downloading_percent!!.text = "0%"
            }
        }
    }




    /*
        @SuppressLint("SetTextI18n")
        private fun downloadFiles(downloadLinks: List<String>) {
            val activeDownloads= mutableMapOf<Int, String>() // Track active downloads

            btnStart!!.setOnClickListener {
                // Start downloading each file
                downloadLinks.forEach { url ->
                    val fileName = URLUtil.guessFileName(url, null, null)
                    file_name!!.text = "Downloadingmultiple files..." // Update text

                     downloadId = PRDownloader.download(url, path, fileName)
                        .build()
                        .setOnStartOrResumeListener {

                            activeDownloads[downloadId] = fileName
                            updateDownloadStatus(activeDownloads)
                            btnStart!!.isEnabled = false // Disable start once downloads begin
                            btnCancel!!.isEnabled = true
                            Toast.makeText(this@Prdownloader, "Downloading $fileName started", Toast.LENGTH_SHORT).show()
                        }
                        .setOnPauseListener {
                            Toast.makeText(this@Prdownloader, "Downloading$fileName paused", Toast.LENGTH_SHORT).show()
                        }
                        .setOnCancelListener {
                            activeDownloads.remove(downloadId)
                            updateDownloadStatus(activeDownloads)
                            Toast.makeText(this@Prdownloader, "Downloading $fileName cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .setOnProgressListener { progress ->
                            // Handle individual file progress if needed
                        }
                        .start(object : OnDownloadListener {
                            override fun onDownloadComplete() {
                                activeDownloads.remove(downloadId)
                                updateDownloadStatus(activeDownloads)
                                Toast.makeText(this@Prdownloader, "Downloading $fileName completed", Toast.LENGTH_SHORT).show()
                                if (activeDownloads.isEmpty()) { // All downloads finished
                                    btnStart!!.isEnabled = false
                                    btnCancel!!.isEnabled = false
                                    btnStart!!.text = "All Completed"
                                }
                            }

                            override fun onError(error: Error) {
                                activeDownloads.remove(downloadId)
                                updateDownloadStatus(activeDownloads)
                                Toast.makeText(this@Prdownloader, "Error downloading $fileName", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }

            btnCancel!!.setOnClickListener {
                activeDownloads.keys.forEach { downloadId ->
                    PRDownloader.cancel(downloadId)
                }
                activeDownloads.clear()
                updateDownloadStatus(activeDownloads)
                btnStart!!.text = "Start"
                btnStart!!.isEnabled = true
                btnCancel!!.isEnabled = false
            }
        }

        private fun updateDownloadStatus(activeDownloads: Map<Int, String>) {
            if (activeDownloads.isEmpty()) {
                file_name!!.text = "No active downloads"
                progressBar!!.progress = 0
                downloading_percent!!.text = ""
                progressBar!!.isIndeterminate = false
            } else {
                file_name!!.text = "Downloading: ${activeDownloads.values.joinToString(", ")}"
                // You might want to update progress bar and percentage based on overall progress here
            }
        }
    */



    @SuppressLint("SetTextI18n")
    private fun downloadFilesop(downloadLinks: List<String>) {

            // Disables the start button during the batch download process
            btnStart!!.isEnabled = false

            // Iterating through each URL in the downloadLinks list
            for (url in downloadLinks) {
                if (Status.RUNNING == PRDownloader.getStatus(downloadID)) {
                    PRDownloader.pause(downloadID)
                    return
                }

                // Checks if the status is paused
                if (Status.PAUSED == PRDownloader.getStatus(downloadID)) {
                    PRDownloader.resume(downloadID)
                    return
                }

                val fileName = URLUtil.guessFileName(url, null, null)
                file_name!!.text = "Downloading $fileName"

                // Initiates the download process
                downloadID = PRDownloader.download(url, path, fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        progressBar!!.isIndeterminate = false
                        btnStart!!.isEnabled = true
                        btnStart!!.text = "Pause"
                        btnCancel!!.isEnabled = true
                        Toast.makeText(this@PrdownloaderAct, "Downloading started", Toast.LENGTH_SHORT).show()
                    }
                    .setOnPauseListener {
                        btnStart!!.text = "Resume"
                        Toast.makeText(this@PrdownloaderAct, "Downloading Paused", Toast.LENGTH_SHORT).show()
                    }
                    .setOnCancelListener {
                        downloadID = 0
                        btnStart!!.text = "Start"
                        btnCancel!!.isEnabled = false
                        progressBar!!.progress = 0
                        downloading_percent!!.text = ""
                        progressBar!!.isIndeterminate = false
                        Toast.makeText(this@PrdownloaderAct, "Downloading Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .setOnProgressListener { progress ->
                        val progressPer = progress.currentBytes * 100 / progress.totalBytes
                        progressBar!!.progress = progressPer.toInt()
                        downloading_percent!!.text = Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        progressBar!!.isIndeterminate = false
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            // Notify the user about the completion of the current file
                            Toast.makeText(this@PrdownloaderAct, "$fileName Downloaded", Toast.LENGTH_SHORT).show()

                            // Check if the current download is the last one
                            if (url == downloadLinks.last()) {
                                // Last file in the batch, update the UI accordingly
                                btnStart!!.isEnabled = false
                                btnCancel!!.isEnabled = false
                                btnStart!!.text = "Completed"
                                file_downloaded_path!!.text = "All files downloaded to: $path"
                                Toast.makeText(this@PrdownloaderAct, "All Downloads Completed", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onError(error: Error) {
                            btnStart!!.text = "Start"
                            downloading_percent!!.text = "0"
                            progressBar!!.progress = 0
                            downloadID = 0
                            btnStart!!.isEnabled = true
                            btnCancel!!.isEnabled = false
                            progressBar!!.isIndeterminate = false
                            Toast.makeText(this@PrdownloaderAct, "Error Occurred: ${error.serverErrorMessage}", Toast.LENGTH_SHORT).show()
                        }
                    })

                // Adding a short delay between each download (optional)
               Thread.sleep(5000)
            }

            // Handling click event on cancel button
            btnCancel!!.setOnClickListener {
                btnStart!!.text = "Start"
                PRDownloader.cancel(downloadID)
            }

    }

    object Utils {
        fun getRootDirPath(context: Context): String {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val file = ContextCompat.getExternalFilesDirs(context.applicationContext, null)[0]
                return file.absolutePath
            } else {
                return context.applicationContext.filesDir.absolutePath
            }
        }

        fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String {
            return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
        }

        private fun getBytesToMBString(bytes: Long): String {
            return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
        }
    }
}
