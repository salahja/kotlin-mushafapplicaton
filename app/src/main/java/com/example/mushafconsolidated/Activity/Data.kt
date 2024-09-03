package com.example.mushafconsolidated.Activity

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.mushafconsolidated.receiversimport.AudioAppConstants
import com.example.mushafconsolidated.receiversimport.QuranValidateSources.getSaveSurahDirs


object Data {
    val sampleUrls = arrayOf(
        "http://speedtest.ftp.otenet.gr/files/test100Mb.db",
        "https://download.blender.org/peach/bigbuckbunny_movies/big_buck_bunny_720p_stereo.avi",
        "http://media.mongodb.org/zips.json",
        "http://www.exampletonyotest/some/unknown/123/Errorlink.txt",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Android_logo_2019.svg/687px-Android_logo_2019.svg.png",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    )





    private fun getFilePath(url: String, context: Context): String {
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment
        val dir = getSaveDir(context)
        return "$dir/DownloadList/$fileName"
    }

    fun getNameFromUrl(url: String?): String {
        return Uri.parse(url).lastPathSegment!!
    }


    fun getSaveDir(context: Context): String {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/fetch"
    }

    fun getFilePath(url: String, context: Context, filepath: String, readerid: String): String {
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment
        val dir = getSaveDirs(context, readerid)
        return "$dir/$fileName"
    }
    fun getFilePathJson(context: Context, filepath: String, readerid: String, jsonString: String): String {

        val fileName = readerid+ AudioAppConstants.Extensions.Companion.JSON
        val dir = getSaveSurahDirs(context, readerid)
        return "$dir/$fileName"
    }

    fun getSaveDirs(context: Context, readerid: String): Any {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/fetch/" + readerid
    }

}