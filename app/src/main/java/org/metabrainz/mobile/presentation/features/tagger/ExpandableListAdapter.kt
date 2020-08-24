package org.metabrainz.mobile.presentation.features.tagger

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import org.metabrainz.mobile.R

class ExpandableListAdapter internal constructor(
        private val context: Context,
        private val titleList: List<String>,
        private val dataList: HashMap<String, ArrayList<Pair<String, String>>>?
) : BaseExpandableListAdapter() {

    override fun getGroup(p0: Int): Any? {
        return this.titleList[p0]
    }


    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        var convertView = p2
        val listTitle = getGroup(p0) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.expandablelv_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.expandable_group_title)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun getChildrenCount(p0: Int): Int {
        return this.titleList.size
    }

    override fun getChild(p0: Int, p1: Int): Pair<String, String>? {
        return this.dataList?.get(this.titleList[p0])!![p1]
    }


    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val expandedListText = getChild(p0, p1) as Pair<String, String>
        var covertView = p3
        if (covertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            covertView = layoutInflater.inflate(R.layout.expandablelv_child, null)
        }
        val TagKeyTextview = covertView!!.findViewById<TextView>(R.id.tag_key)
        val TagValueTextview = covertView.findViewById<TextView>(R.id.tag_value)
        TagKeyTextview.text = expandedListText.first
        TagValueTextview.text = expandedListText.second
        return covertView
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

}