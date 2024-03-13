package com.example.intervision

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ItemSingleView(imageID: Int, layout: View?) : AppCompatActivity() {
    init {
        val view = layout!!.findViewById<ImageView>(R.id.ImageView_single_image_item)
        view.setImageResource(imageID)
    }
}