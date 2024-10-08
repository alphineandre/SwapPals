package com.example.swappals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.swappals.adapter.CarAdapter
import com.example.swappals.adapter.gadgetAdapter
import com.example.swappals.model.Cars
import com.example.swappals.model.Gadgets
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.collections.Collection

class User_Dashboard : AppCompatActivity() {
    private lateinit var  recyclerView: RecyclerView
    private lateinit var gadgetList:ArrayList<Gadgets>
    private lateinit var gadgetAdapter: gadgetAdapter

    private lateinit var carList:ArrayList<Cars>
    private lateinit var carAdapter: CarAdapter
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        //Gadgets
        init()
        //Cars
        carsInfo()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, User_Dashboard::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_collection -> {
                    startActivity(Intent(applicationContext, Collection::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_add -> {
                    startActivity(Intent(applicationContext, Categories::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_profile -> {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_logout -> {
                    startActivity(Intent(applicationContext, Login::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }

    }
    //Gadgets
    private fun init(){
        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val snapHelper : SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        gadgetList = ArrayList()

        addToList()
        gadgetAdapter = gadgetAdapter(gadgetList)
        recyclerView.adapter = gadgetAdapter
    }
    //Gadgets
    private fun addToList(){
        gadgetList.add(Gadgets(R.drawable.pic1, "Beats Earpods"))
        gadgetList.add(Gadgets(R.drawable.pic8, "TrackFit Watch"))
        gadgetList.add(Gadgets(R.drawable.pic9, "American Red Cross"))
        gadgetList.add(Gadgets(R.drawable.pic_3, "AnkerWork"))
        gadgetList.add(Gadgets(R.drawable.pic_4, "Longines"))
        gadgetList.add(Gadgets(R.drawable.appleipod, "Apple Ipods"))
        gadgetList.add(Gadgets(R.drawable.img, "Apple Iphone"))
        gadgetList.add(Gadgets(R.drawable.img_1, "Console Joysticks"))

    }


    //Cars
    private fun carsInfo(){
        recyclerView = findViewById(R.id.recycler_view_1)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val snapHelper2 : SnapHelper = LinearSnapHelper()
        snapHelper2.attachToRecyclerView(recyclerView)
        carList = ArrayList()

        addDataToCarList()
        carAdapter = CarAdapter(carList)
        recyclerView.adapter = carAdapter
    }
    //Cars
    private fun addDataToCarList(){
        carList.add(Cars(R.drawable.pic3, "Maserati MC20"))
        carList.add(Cars(R.drawable.pic_2, "Chevrolet Corvette"))
        carList.add(Cars(R.drawable.img_2, "Bugatti"))
        carList.add(Cars(R.drawable.img_3, "Range Rover"))
        carList.add(Cars(R.drawable.img_4, "Mercedes Benz"))
        carList.add(Cars(R.drawable.img_5, "BMW"))
        carList.add(Cars(R.drawable.img_6, "VW Golf Polo"))
        carList.add(Cars(R.drawable.img_7, "Audi"))

    }
}