package com.example.swappals.model

import java.io.Serializable

class Category() : Serializable {
    var id: Long = 0
    lateinit var name: String
    var goalItem: Int? = null

    constructor(name: String, goalItem: Int?) : this() {
        this.name = name
        this.goalItem = goalItem
    }
}