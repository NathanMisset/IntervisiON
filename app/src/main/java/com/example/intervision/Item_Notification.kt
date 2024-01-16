package com.example.intervision

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Item_Notification(//Firebase
    private val storage: FirebaseStorage, private val firestore: FirebaseFirestore, //Parent
    private val parent: View,
    private val activity: Activity, //Variables
    private val imageLocation: String,
    private val imageID: String, private val message: String
) {
    private val userID: String? = null
    var layout: View
    private var imageView: ImageView? = null
    private var messageView: TextView? = null

    init {
        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_turn_notification,
            parent as ViewGroup,
            false
        )
        //Log.d(TAG, "layout => $layout")
        GetView()
        SetImage()
        SetText()
    }

    private fun GetView() {
        messageView = layout.findViewById(R.id.message_notification_item)
        imageView = layout.findViewById(R.id.profile_image_notification_item)
    }

    private fun SetImage() {
        val storageRef = storage.reference
        val pathReference = storageRef.child(imageLocation + imageID)
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        pathReference.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes -> // Data for "images/island.jpg" is returns, use this as needed
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView!!.setImageBitmap(bitmap)
            }.addOnFailureListener {
            // Handle any errors
        }
    }

    private fun SetText() {
        messageView!!.text = message
    }

    companion object {
        private const val TAG = "NotificationItem"
    }
}