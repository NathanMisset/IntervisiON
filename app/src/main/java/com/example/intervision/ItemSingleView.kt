/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * This item can be initiated as an object in an activity
 * This shows and image
 *
 */

class ItemSingleView(imageID: Int, layout: View?) : AppCompatActivity() {
    init {
        val view = layout!!.findViewById<ImageView>(R.id.ImageView_single_image_item)
        view.setImageResource(imageID)
    }
}