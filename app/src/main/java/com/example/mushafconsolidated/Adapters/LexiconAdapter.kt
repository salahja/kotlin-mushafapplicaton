package com.example.mushafconsolidated.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.databinding.DictionaryLayoutBinding
import com.example.mushafconsolidated.databinding.LysalayoutBinding
import com.example.mushafconsolidated.intrfaceimport.OnItemClickListener
import com.example.utility.QuranGrammarApplication


class LexiconAdapter(
    private val lanesdictionary: ArrayList<String>,
    private val context: Context?,
    var language: String

) : RecyclerView.Adapter<LexiconAdapter.ItemViewAdapter>() {
    var mItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewAdapter {
        val binding: DictionaryLayoutBinding = DictionaryLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewAdapter(binding)
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    override fun onBindViewHolder(holder: ItemViewAdapter, position: Int) {

        val mequran: Typeface =
            Typeface.createFromAsset(QuranGrammarApplication.context!!.assets, "Taha.ttf")
        var lanes: String = lanesdictionary[position]
        holder.wordDictionary.requestFocus()
        holder.wordDictionary.settings.lightTouchEnabled = true
        holder.wordDictionary.settings.builtInZoomControls = true
        holder.wordDictionary.settings.builtInZoomControls = true
        holder.wordDictionary.settings.setSupportZoom(true)
        val webSettings: WebSettings = holder.wordDictionary.settings
        webSettings.javaScriptEnabled = true

        val data: StringBuilder = StringBuilder()
        if (((language == "imperative") ||language=="kanainna"|| (language == "genetivenoun") || (language == "accusativenoun") || (language == "nominativenoun") || (language == "accusative") || (language == "preposition") || (language == "conditonal") || (language == "relative") || (language == "dem") || (language == "Jussive") || (language == "Subjunctive"))) {
            // webView.loadDataWithBaseURL(htmlData, "text/html", "utf-8", null);
            holder.wordDictionary.loadDataWithBaseURL(
                "file:///android_asset/",
                lanes,
                "text/html",
                "utf-8",
                null
            )
            holder.wordDictionary.settings.builtInZoomControls = true
        } else if ((language == "lanes")) {
            //   wv.loadDataWithBaseURL(null,myHtmlString, "text/html", "UTF-8", null);
            data.append("<!DOCTYPE html><html lang=\"en\"><head>")
            data.append("<LINK href=\"lexicon.css\" type=\"text/css\" rel=\"stylesheet\"/></head>")
                .append(lanes)

            lanes = "<link rel=\"stylesheet\" type=\"text/css\" href=\"lexicon.css\" />$lanes"

            holder.wordDictionary.settings.builtInZoomControls = true
            holder.wordDictionary.loadDataWithBaseURL(
                "file:///android_asset/",
                lanes,
                "text/html",
                "utf-8",
                null
            )
        } else if ((language == "hans")) {

            holder.wordDictionary.loadDataWithBaseURL(
                null, lanes //  data .append("</body></HTML>");

                , "text/html", "utf-8", null
            )
            holder.wordDictionary.settings.builtInZoomControls = true
        }
    }

    override fun getItemId(position: Int): Long {
        return lanesdictionary[position].length.toLong()
    }

    override fun getItemCount(): Int {
        return lanesdictionary.size
    }


    inner class ItemViewAdapter(binding: DictionaryLayoutBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener // current clickListerner
    {
        private val wordDictionaryUrdu: TextView
        var meaning: TextView? = null
        private val rootwowrd: TextView = binding.rootward
        var arabicword: TextView? = null
        private var referenceView: TextView? = null
        var dismissview: ImageView? = null
        var wordDictionary: WebView
        var i: Int = ContextCompat.getColor(context!!, R.color.kashmirigreen)

        init {
           //  arabicword = binding.arabicword
            wordDictionary = binding.wordDictionary
            wordDictionaryUrdu = binding.wordDictionaryUrdu
          //  meaning = binding.meaning
           // referenceView = binding.referenceView
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(v, layoutPosition)
            }
        }
    }
}