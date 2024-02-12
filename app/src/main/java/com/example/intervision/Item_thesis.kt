package com.example.intervision

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Itemthesis(
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    parent: View,
    activity: Activity,
    partisipantsIdS: ArrayList<String>,
    stellingID: String?
) : Manager_Basic_Item() {
        //Firebase
        private val storage: FirebaseStorage
        private val firestore: FirebaseFirestore

        //Parent
        private val parent: View
        private val activity: Activity

        //Variables
        var layout: View
        private val partisipantsIdS: ArrayList<String>
        private var Against: ArrayList<String>? = null
        private var InFavour: ArrayList<String>? = null
        private var stellingID: String? = null

        //Views
        private var stelling: TextView? = null
        private var againstTV: TextView? = null
        private var inFavourTV: TextView? = null

        init {
            Log.d(TAG, "storage => $storage")
            this.storage = storage
            this.firestore = firestore
            this.parent = parent
            this.activity = activity
            this.partisipantsIdS = partisipantsIdS
            this.stellingID = stellingID
            this.layout = LayoutInflater.from(parent.context).inflate(
                R.layout.item_thesis,
                parent as ViewGroup,
                false
            )
            GetAgainstVotes()
        }

        override fun Init() {
            stelling = layout.findViewById(R.id.tv_thesis_item_thesis)
            againstTV = layout.findViewById(R.id.tv_Against_item_thesis)
            inFavourTV = layout.findViewById(R.id.tv_InFavour_item_thesis)
            stelling!!.text = stellingID
            againstTV!!.text = Against!!.size.toString()
            inFavourTV!!.text = InFavour!!.size.toString()
        }

        private fun GetAgainstVotes() {
            Against = ArrayList()
            InFavour = ArrayList()
            Log.d(TAG, "stellingID "+stellingID)

            firestore.collection("Votes")
                .whereEqualTo("Id", stellingID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                            Against!!.add(document.data.get("Against").toString())
                            InFavour!!.add(document.data.get("In Favour").toString())
                        }
                        Log.d(TAG, "Against $Against")
                        Log.d(TAG, "InFavour $InFavour")
                        Init()
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

        companion object {
            private const val TAG = "Thesis Item"
        }
}