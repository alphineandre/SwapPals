package com.example.swappals.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.swappals.R
import com.example.swappals.model.Item
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream

class ItemAdapter(private val context: Context, private val items: List<Item>) : BaseAdapter() {
    private val layoutInflater = LayoutInflater.from(context)
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.item_list_item, parent, false)
        val item = items[position]

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val imageViewItem = view.findViewById<ImageView>(R.id.imageViewItem)

        textViewName.text = item.title
        textViewDescription.text = item.description

        // Load the image from Firebase Storage
        loadItemImage(item.image, imageViewItem)

        return view
    }

    private fun loadItemImage(imageUrl: Bitmap, imageView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val storageReference = storageRef.child("images/$imageUrl.jpg")
                val maxImageSizeBytes: Long = 1024 * 1024 // 1MB
                val imageStream = storageReference.getBytes(maxImageSizeBytes).await()
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(imageStream))
                CoroutineScope(Dispatchers.Main).launch {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
