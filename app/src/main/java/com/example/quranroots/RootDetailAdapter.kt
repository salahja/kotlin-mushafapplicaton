package com.example.quranroots

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.text
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.databinding.FragmentArabicrootDetailBinding

import com.example.mushafconsolidated.intrfaceimport.OnItemClickListener
import com.example.utility.QuranGrammarApplication
import com.google.android.material.chip.Chip
import java.lang.ref.WeakReference
class RootDetailAdapter(
    private var catTwoArrayList: List<String>,
    context: Context
) : RecyclerView.Adapter<RootDetailAdapter.ViewHolder>() {

    private val contextRef = WeakReference(context)
    private var mItemClickListener: OnItemClickListener? = null

    fun updateList(newList: List<String>) {
        catTwoArrayList = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentArabicrootDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = contextRef.get()
        if (context != null) {
            val imgs = context.resources.obtainTypedArray(R.array.cat_img)
            imgs.recycle()
        }

        val catOne = catTwoArrayList[position]
        holder.binding.arabicrootDetail.text = catOne
    }

    override fun getItemCount(): Int {
        return catTwoArrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItem(position: Int): String {
        return catTwoArrayList[position]
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: FragmentArabicrootDetailBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.arabicrootDetail.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mItemClickListener?.onItemClick(v, layoutPosition)
        }
    }
}