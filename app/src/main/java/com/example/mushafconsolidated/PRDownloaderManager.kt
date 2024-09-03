package com.example.mushafconsolidated

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.semantics.text
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader


class PRDownloaderManager (private val context: Context,private val mainHandler: Handler) {
    private val activeDownloads = mutableMapOf<Int, String>()

    fun downloadFiles(
        downloadLinks: List<String>,
        path: String,
        btnStart: Button,
        btnCancel: Button,
        progressBar: ProgressBar,
        progressTextView: TextView,
        onDownloadCompleteListener: () -> Unit // Callback for completion
    ) {
        var filesDownloaded = 0
        val downloadId=0
        btnStart.setOnClickListener {
            val totalFiles = downloadLinks.size
            progressBar.max = 100
            progressBar.progress = 0
            progressTextView.text = "0 / $totalFiles files downloaded"

            btnStart.isEnabled = false
            btnCancel.isEnabled = true

            downloadLinks.forEachIndexed { index, url ->
                val fileName = URLUtil.guessFileName(url, null, null)
                //file_name!!.text = "Downloading multiple files..."

                val downloadId = PRDownloader.download(url, path, fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        activeDownloads[downloadId] = fileName
                        updateDownloadStatus(
                            progressBar,
                            progressTextView,
                            activeDownloads
                        )
                        btnStart!!.isEnabled = false
                        btnCancel!!.isEnabled = true
                        Toast.makeText(
                            context,
                            "Downloading $fileName started",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setOnPauseListener {
                        Toast.makeText(
                            context,
                            "Downloading $fileName paused",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setOnCancelListener {
                        activeDownloads.remove(downloadId)
                        updateDownloadStatus(       progressBar,
                            progressTextView,
                            activeDownloads)
                        Toast.makeText(
                            context,
                            "Downloading $fileName cancelled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setOnProgressListener { progress ->
                        // You could handle progress for individual files here if needed
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            activeDownloads.remove(downloadId)
                            filesDownloaded++
                            updateOverallProgress(filesDownloaded, totalFiles,progressBar,progressTextView)
                            Toast.makeText(
                                context,
                                "Downloading $fileName completed",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (filesDownloaded == totalFiles) { // All downloads finished
                                btnStart!!.visibility = View.GONE
                                btnCancel!!.visibility = View.GONE
                                progressBar!!.visibility = View.GONE
                                btnStart!!.text = "All Completed"
                            //    downloadFooter.visibility = View.GONE


                                progressTextView!!.visibility = View.GONE
                               // initializePlayer()
                             //   playerFooter.visibility = View.VISIBLE
                             //   audioSettingsBottom.visibility = View.GONE
                            }
                        }

                        override fun onError(error: com.downloader.Error) {
                            activeDownloads.remove(downloadId)
                            updateDownloadStatus(       progressBar,
                                progressTextView,
                                activeDownloads)
                            Toast.makeText(
                                context,
                                "Error downloading $fileName",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(
                                "DownloadError",
                                "Error downloading $fileName: ${error.serverErrorMessage}"
                            )
                        }
                    })

            }

        }


        btnCancel!!.setOnClickListener {
            activeDownloads.keys.forEach { downloadId ->
                PRDownloader.cancel(downloadId)
            }
            activeDownloads.clear()
           // updateDownloadStatus(activeDownloads)
            btnStart!!.text = "Start"
            btnStart!!.isEnabled = true
            btnCancel!!.isEnabled = false
        }
    }

    private fun updateDownloadStatus(progressBar: ProgressBar, progressTextView: TextView,activeDownloads: Map<Int, String>) {
        if (activeDownloads.isEmpty()) {
            //file_name!!.text = "No active downloads"
            progressBar!!.progress = 0
            //progressTextView!!.text = ""
            progressBar!!.isIndeterminate = false
        } else {
            //file_name!!.text = "Downloading: ${activeDownloads.values.joinToString(", ")}"
        }
    }


    private fun updateOverallProgress(
        filesDownloaded: Int,
        totalFiles: Int,
        progressBar: ProgressBar,
        progressTextView: TextView
    ) {
        mainHandler.post  {
            val progressText = "$filesDownloaded / $totalFiles files downloaded"
            Log.d("OverallProgress", "Overall progress = $progressText")

            // Calculate progress percentage based on files downloaded
            val progressPercentage = (filesDownloaded * 100) / totalFiles
            progressBar!!.progress = progressPercentage

            progressTextView!!.text = progressText
        }
    }




}