package com.example.swappals.model

import android.graphics.Bitmap

class Item(
    val title: String,
    val description: String,
    val image: Bitmap,
    val category: Category?
) {
    var id: Long = 0

    constructor(title: String, category: String, description: String, image: String) :
            this(title, description, Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8), null)
}