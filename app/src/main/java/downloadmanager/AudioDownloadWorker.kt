package downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class AudioDownloadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val verseNumber = inputData.getInt("verse_number", 0)
        val audioUrl = "https://example.com/quran/chapter2/verse_$verseNumber.mp3" // Construct URL dynamically

        val request = DownloadManager.Request(Uri.parse(audioUrl))
            .setTitle("Verse $verseNumber")
            .setDescription("Downloading audio for verse $verseNumber")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "verse_$verseNumber.mp3")

        val downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        return Result.success()
    }



}

/*
val workManager = WorkManager.getInstance(applicationContext)
for (verseNumber in 1..286) {
    val data = Data.Builder()
        .putInt("verse_number", verseNumber)
        .build()

    val downloadRequest = OneTimeWorkRequestBuilder<AudioDownloadWorker>()
        .setInputData(data)
        .build()

    workManager.enqueue(downloadRequest)
}*/
