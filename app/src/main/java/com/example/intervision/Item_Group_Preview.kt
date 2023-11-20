package com.example.intervision

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Item_Group_Preview(
    private val name: String, private val participantsSiD: ArrayList<String?>?,
    parent: View?, private val activity: Activity?, private val dataBase: FirebaseFirestore?,
    protected var user: FirebaseAuth?, var SessionId: String, private val leaderId: String
) {
    private var header: TextView? = null
    private var button: Button? = null
    private val participantsTextView: ArrayList<TextView>
    private val dataSession: Map<String, Any>? = null
    private val dataUsers: Map<String, Any>? = null
    private val storage: FirebaseStorage

    init {
        storage = FirebaseStorage.getInstance()
        Log.d(TAG, participantsSiD.toString())
        participantsTextView = ArrayList()
        val Layout = LayoutInflater.from(parent!!.context).inflate(
            R.layout.item_group_preview,
            parent as ViewGroup?,
            false
        )
        (parent as ViewGroup?)!!.addView(Layout)
        getViews(Layout)
        userData
        SetName()
        SetButton()
    }

    private fun getViews(layout: View) {
        header = layout.findViewById(R.id.header_group_card_group_preview)
        button = layout.findViewById(R.id.join_group_group_preview)
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_1_group_preview))
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_2_group_preview))
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_3_group_preview))
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_4_group_preview))
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_5_group_preview))
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_6_group_preview))
        val participantN: Int
        participantN = participantsSiD?.size ?: 0
        for (i in participantsTextView.indices) {
            participantsTextView[i].text = null
            participantsTextView[i].setCompoundDrawables(null, null, null, null)
        }
    }

    private val userData: Unit
        private get() {
            dataBase!!.collection("User Data")
                .whereIn("User UID", participantsSiD!!)
                .get()
                .addOnCompleteListener { task ->
                    var i = 0
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                            SetParticipant(i, document.data)
                            i += 1
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    private fun SetButton() {
        button!!.setOnClickListener {
            Log.d("BUTTONS", "User tapped $button")
            CheckIfLeader()
        }
    }

    private fun SetName() {
        header!!.text = name
    }

    private fun SetParticipant(i: Int, data: Map<String, Any>) {
        Log.d(TAG, "$data => ")
        participantsTextView[i].text = data["Voornaam"] as String?
        getImage(data["User UID"].toString(), participantsTextView[i])
    }

    private fun getImage(Uid: String, view: TextView) {
        val storageRef = storage.reference

        // Create a reference with an initial file path and name
        val pathReference = storageRef.child("ProfilePictures/$Uid")
        //Log.d(TAG, "pathReference " + pathReference);
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        pathReference.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes -> // Data for "images/island.jpg" is returns, use this as needed
                var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                bitmap = Bitmap.createScaledBitmap(bitmap!!, 180, 180, false)
                view.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    BitmapDrawable(view.resources, bitmap),
                    null,
                    null
                )
            }.addOnFailureListener {
            // Handle any errors
        }
    }

    private fun CheckIfLeader() {
        Log.d(TAG, "leaderId " + leaderId + " userid " + user!!.uid.toString())
        if (user!!.uid.toString() == leaderId) {
            ToIntervisionLeader()
        } else {
            ToIntervision()
        }
    }

    private fun ToIntervision() {
        val i = Intent(activity, Activity_Intervision::class.java)
        Log.d(TAG, "SessionId ")
        Log.d(TAG, "SessionId $SessionId")
        i.putExtra("SessionID", SessionId)
        activity!!.startActivity(i)
    }

    private fun ToIntervisionLeader() {
        val i = Intent(activity, Activity_Intervision_Leader::class.java)
        Log.d(TAG, "SessionId ")
        Log.d(TAG, "SessionId $SessionId")
        i.putExtra("SessionID", SessionId)
        activity!!.startActivity(i)
    }

    companion object {
        private const val TAG = "GroupPreviewItem"
    }
}