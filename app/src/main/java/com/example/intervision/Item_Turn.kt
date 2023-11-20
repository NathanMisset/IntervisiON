package com.example.intervision

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Item_Turn(
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    parent: View,
    activity: Activity,
    partisipantsIdS: ArrayList<String>,
    layout: View?
) : Manager_Basic_Item() {
    //Firebase
    private val storage: FirebaseStorage
    private val firestore: FirebaseFirestore

    //Parent
    private val parent: View
    private val activity: Activity

    //Variables
    var notificationItems: ArrayList<Item_Notification>? = null
    public var layout: View
    private val partisipantsIdS: ArrayList<String>
    private var UserNames: ArrayList<String>? = null
    private val notificationItemDefault: Item_Notification? = null

    init {
        Log.d(TAG, "storage => $storage")
        this.storage = storage
        this.firestore = firestore
        this.parent = parent
        this.activity = activity
        this.partisipantsIdS = partisipantsIdS
        this.layout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_turn,
            parent as ViewGroup,
            false
        )
        GetText()
    }

    override fun Init() {
        notificationItems = ArrayList()
        Log.d(TAG, "partisipantsIdS => $partisipantsIdS")
        Log.d(TAG, "UserNames => $UserNames")
        for (i in partisipantsIdS.indices) {
            Log.d(TAG, "Make" + " => " + "NotificationItem")
            notificationItems!!.add(
                Item_Notification(
                    storage,
                    firestore,
                    layout,
                    activity,
                    "ProfilePictures/",
                    partisipantsIdS[i],
                    UserNames!![i] + " is aan de beurt"
                )
            )
        }
        Log.d(TAG, "notificationItems => $notificationItems")
    }

    fun GiveTurn(i: Char) {
        when (i) {
            'A' -> SwitchTurn(0)
            'B' -> SwitchTurn(1)
            'C' -> SwitchTurn(2)
            'D' -> SwitchTurn(3)
            'E' -> SwitchTurn(4)
            'F' -> SwitchTurn(5)
            else -> DefaultTurn()
        }
    }

    private fun SwitchTurn(i: Int) {
        Log.d(TAG, "switchnumber => $i")
        Log.d(TAG, "notficationItems => $notificationItems")
        (layout as ViewGroup).removeAllViews()
        (layout as ViewGroup).addView(notificationItems!![i].layout)
    }

    private fun DefaultTurn() {
        (layout as ViewGroup).removeAllViews()
    }

    private fun GetText() {
        Log.d(TAG, "GetText")
        UserNames = ArrayList()
        firestore.collection("User Data")
            .whereIn("User UID", partisipantsIdS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                        UserNames!!.add(document.data["Voornaam"].toString())
                    }
                    Log.d(TAG, "UserNames$UserNames")
                    Init()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }


    companion object {
        private const val TAG = "NotificationItem"
    }
}