package com.example.swappals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swappals.R
import com.example.swappals.model.Cars

class CarAdapter(private val carList: List<Cars>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>(){
    class CarViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val carImage : ImageView = itemView.findViewById(R.id.imageView4)
        val carName : TextView = itemView.findViewById(R.id.textCars1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_dashboard_cardview2, parent, false)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.carImage.setImageResource(car.carImage)
        holder.carName.text = car.carName
    }


}