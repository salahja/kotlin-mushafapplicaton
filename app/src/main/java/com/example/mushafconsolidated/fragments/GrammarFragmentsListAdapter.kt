package com.example.mushafconsolidated.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.example.mushafconsolidated.R
import com.example.utility.QuranGrammarApplication

class GrammarFragmentsListAdapter(
    private val context: Context, private val expandableListTitle: List<String>,
    private val expandableListDetail: HashMap<String, List<SpannableString>>
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return expandableListDetail[expandableListTitle[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView: View? = null
        val expandedListText =
            getChild(listPosition, expandedListPosition)
        if (convertView == null) {
            val layoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.lgrammar_item, parent, false)
        }
        val mequran =
            Typeface.createFromAsset(QuranGrammarApplication.context!!.assets, "Roboto.ttf")
        val expandedListTextView =
            convertView?.findViewById<View>(R.id.expandedListItem) as TextView

            expandedListTextView.text = expandedListText as CharSequence?
        //    expandedListTextView.setLineSpacing(0f, 0f)
        // Hide group indicator (arrow)
        val expandableListView = parent as ExpandableListView // Cast parent to ExpandableListView
        expandableListView.expandGroup(listPosition) // Expand the group initially
        expandableListView.setGroupIndicator(null)

        //    expandedListTextView.setTypeface(mequran);
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return expandableListDetail[expandableListTitle[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView: View? = null
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, parent, false)
        }
        val listTitleTextView = convertView
            ?.findViewById<View>(R.id.listTitle) as TextView
        listTitleTextView.gravity = Gravity.CENTER
        convertView?.setPadding(50, 0, 50, 0)
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(QuranGrammarApplication.context)
        val preferences = prefs.getString("theme", "dark")
        val dark = preferences == "dark" || preferences == "blue" || preferences == "green"
        if (dark) {
            listTitleTextView.setTextColor(Color.CYAN)
        } else {
            listTitleTextView.setTextColor(
                ContextCompat.getColor(
                    QuranGrammarApplication.context!!,
                    R.color.burntamber
                )
            )
        }
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        // Hide group view if child list is empty
        if (getChildrenCount(listPosition) == 0) {
            convertView?.visibility = View.GONE
            val expandableListView = parent as ExpandableListView
            expandableListView.setGroupIndicator(null)
        } else {
            convertView?.visibility = View.VISIBLE
        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}