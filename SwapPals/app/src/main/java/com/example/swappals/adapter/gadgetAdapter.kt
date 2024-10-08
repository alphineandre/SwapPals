package com.example.swappals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.swappals.R
import com.example.swappals.model.Gadgets

class gadgetAdapter (private val gadgetList : List<Gadgets>) : RecyclerView.Adapter<gadgetAdapter.gadgetViewHolder>(){
    class gadgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val gadgetImageView : ImageView = itemView.findViewById(R.id.imageView3)
        val gadgetDescriptionView : TextView = itemView.findViewById(R.id.textGadget1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gadgetViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.user_dashboard_cardview, parent, false)
        return gadgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gadgetList.size
    }

    override fun onBindViewHolder(holder: gadgetViewHolder, position: Int) {
       val gadget = gadgetList[position]
        holder.gadgetImageView.setImageResource(gadget.gadgetName)
        holder.gadgetDescriptionView.text = gadget.gadgetDescription
    }
}