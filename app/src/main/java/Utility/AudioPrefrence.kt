package Utility

import android.content.Context
import com.example.mushafconsolidated.data.AudioPositionSaved
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException


object AudioPrefrence {
    private var jsonObject: JsonObject?=null
    private var result: AudioPositionSaved? = null
    private const val MAIN_CONFIG = "application_settings"
 
    fun setLastPlayedAudio(
        context: Context,
        audioPlayed: java.util.ArrayList<AudioPlayed>,
        surah: String?
    ) {
        val editor = context.getSharedPreferences(MAIN_CONFIG, Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(audioPlayed)
        //   String json = gson.toJson(new AudioPositionSaved(audioPlayed));
        editor.putString(surah, json)
        editor.apply()
    }

    fun getLastPlayedAudio(
        context: Context,
        surah: String?
    ): JsonObject? {
        val sharedPreferences = context.getSharedPreferences(
            MAIN_CONFIG,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPreferences.getString(surah, "")
        try {
            result = gson.fromJson(json, AudioPositionSaved::class.java)
        } catch (e: JsonSyntaxException) {

                val jsonArray: JsonArray = JsonParser()
                    .parse(json)
                    .getAsJsonArray()
                for (i in 0 until jsonArray.size()) {
                    val user: AudioPositionSaved = gson.fromJson(jsonArray[i],AudioPositionSaved::class.java)
                val chapter=     (jsonArray[i] as JsonObject).get("surah")
                    val checked = chapter.toString()


                    if(checked.equals(surah)){
                      //  result = gson.fromJson(jsonArray[i].asJsonObject, AudioPositionSaved::class.java)
                        jsonObject = jsonArray[i] as JsonObject
                        break

                    }
                    println(user)
                }


            return jsonObject

        }



        return jsonObject
    }

}