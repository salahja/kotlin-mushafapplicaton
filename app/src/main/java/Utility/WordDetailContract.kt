package Utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.mushafconsolidated.Activity.LughatWordDetailsAct

class WordDetailContract : ActivityResultContract<String, Boolean>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, LughatWordDetailsAct::class.java).putExtra("word", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK && intent?.getBooleanExtra("wordFound", false) ?: false
    }
}