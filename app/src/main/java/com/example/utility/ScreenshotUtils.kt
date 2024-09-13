package com.example.utility


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.text.format.DateFormat
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

object ScreenshotUtils {

    fun takeScreenshot(view: View, context: Context) {
        val date = Date()
        val format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date)

        try {
            val mainDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare"
            )
            if (!mainDir.exists()) {
                mainDir.mkdirs()
            }
            val path = "$mainDir/Mushafapplication-$format.jpeg"

            view.isDrawingCacheEnabled = true
            val bitmap = getBitmapFromView(view)
            val imageFile = File(path)
            val fileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            shareScreenshot(imageFile, context)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun shareScreenshot(imageFile: File, context: Context) {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            imageFile
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        context.startActivity(Intent.createChooser(intent, "Share Screenshot"))
    }
}