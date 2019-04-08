package com.example.calories

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.calories.model.Food
import org.w3c.dom.Text

class FoodAdapter(private val getContext: Context, private val id: Int, private val food: ArrayList<Food>)
    : ArrayAdapter<Food>(getContext, id, food) {

    // Implement ViewHolder to hold references to the views, which can void using findViewById
    class ViewHolder {
        internal var t1: TextView? = null
        internal var t2: TextView? = null
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView

        if(rowView == null) {
            val inflater: LayoutInflater = (getContext as Activity).layoutInflater
            rowView = inflater.inflate(R.layout.listview_layout, null)

            val holder = ViewHolder()
            holder.t1 = rowView.findViewById(R.id.text1) as TextView
            holder.t2 = rowView.findViewById(R.id.text2) as TextView
            rowView.setTag(holder)
        }

        val holder2: ViewHolder = rowView!!.getTag() as ViewHolder
        val one = food[position]
        holder2.t1!!.setText(one.name)
        holder2.t2!!.setText(one.calorie.toString())
        return rowView
    }
}