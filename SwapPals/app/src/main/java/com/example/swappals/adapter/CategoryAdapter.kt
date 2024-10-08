package com.example.swappals.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.swappals.R
import com.example.swappals.model.Category

class CategoryAdapter (context: Context, private val categories:
List<Category>) : BaseAdapter() {
    private val layoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return categories[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent:
    ViewGroup?): View {

        val view = convertView ?:
        layoutInflater.inflate(R.layout.item_list_category,
            parent, false)

        val category = categories[position]

        view.findViewById<TextView>(R.id.CategoryItem).text =
            category.name

        view.findViewById<TextView>(R.id.textViewGoalSet).text = category.goalItem.toString()


        return view

    }
}