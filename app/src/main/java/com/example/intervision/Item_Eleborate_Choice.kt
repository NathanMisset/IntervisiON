package com.example.intervision

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.net.URL

class Item_Eleborate_Choice(
    parent: Activity_Intervision_Leader,
    parentView: View?,
    activity: Activity,
    uIdList: ArrayList<String>?,
    user: FirebaseAuth,
    db: FirebaseFirestore,
    storage: FirebaseStorage
) {
    var parent: Activity_Intervision_Leader
    var parentView: View?
    var layout: View
    var activity: Activity

    //Firebase
    private val user: FirebaseAuth
    private val db: FirebaseFirestore
    private val storage: FirebaseStorage
    var uIdList: ArrayList<String>?

    //Lists
    private var userNames: ArrayList<String>? = null
    private var userIcons: ArrayList<URL>? = null

    //View
    private var userPortraits: ArrayList<ImageButton>? = null
    private var randomButton: ImageButton? = null
    private val chars = charArrayOf('A', 'B', 'C', 'D', 'E', 'F')

    init {
        Init()
        this.activity = activity
        this.parentView = parentView
        this.uIdList = uIdList
        this.parent = parent
        this.user = user
        this.db = db
        this.storage = storage
        layout = LayoutInflater.from(parentView!!.context).inflate(
            R.layout.item_eleborate_choice,
            parentView as ViewGroup?,
            false
        )
        GetPortraits()
        GetViews()
        GetNames()
        SetNames()
        setButtons()
    }

    private fun Init() {
        uIdList = ArrayList()
        userNames = ArrayList()
        userIcons = ArrayList()
        userPortraits = ArrayList()
    }

    private fun GetViews() {
        userPortraits!!.add(0, layout.findViewById(R.id.imageview_user_1_eleborate_choice))
        userPortraits!!.add(1, layout.findViewById(R.id.imageview_user_2_eleborate_choice))
        userPortraits!!.add(2, layout.findViewById(R.id.imageview_user_3_eleborate_choice))
        userPortraits!!.add(3, layout.findViewById(R.id.imageview_user_4_eleborate_choice))
        userPortraits!!.add(4, layout.findViewById(R.id.imageview_user_5_eleborate_choice))
        userPortraits!!.add(5, layout.findViewById(R.id.imageview_user_6_eleborate_choice))
        randomButton = layout.findViewById(R.id.random_button_eleborate_choice)
    }

    private fun setButtons() {
        for (i in userPortraits!!.indices) {
            userPortraits!![i].setOnClickListener {
                Log.d("BUTTONS", "User tapped the RegisterButton")
                parent.ChangeValue(chars[i])
            }
        }
    }

    private fun GetPortraits() {
        val storageRef = storage.reference
        Log.d(TAG, "uIdList$uIdList")
        for (i in uIdList!!.indices) {
            val pathReference = storageRef.child("ProfilePictures/" + uIdList!![i])
            val ONE_MEGABYTE = (1024 * 1024).toLong()
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                userPortraits!![i].setImageBitmap(bitmap)
            }.addOnFailureListener { }
        }
    }

    private fun GetNames() {
        db.collection("User Data")
            .whereArrayContains("User UID", uIdList!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        userNames!!.add(document.data["Voornaam"].toString())
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun SetNames() {
        for (i in userNames!!.indices) {
            //userPortraits.get(i).setText(userNames.get(i));
        }
        Log.d("BUTTONS", "uIdList.size() " + uIdList!!.size)
        for (i in uIdList!!.size..5) {
            userPortraits!![i].visibility = View.INVISIBLE
        }
    }

    fun Enable() {}
    fun Disable() {}

    companion object {
        //Basics
        private const val TAG = "EleborateChoiceItem"
    }
}