package com.example.swappals.model

import android.graphics.Bitmap

class YourDataClass() {
    var title: String = ""
    var description: String = ""
    var image: String = ""
    var category: String = ""

    constructor(title: String, description: String, image: String, category: String) : this() {
        this.title = title
        this.description = description
        this.image = image
        this.category = category
    }
}