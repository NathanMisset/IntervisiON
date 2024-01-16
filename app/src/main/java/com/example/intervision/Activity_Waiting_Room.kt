package com.example.intervision

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class Activity_Waiting_Room : AppCompatActivity() {
    protected var sessionID: String? = null
    protected var partisipantsIdS: ArrayList<String>? = null
    protected var currentRound = 0
    protected var data: String? = null
    private var leader: Boolean? = null

    //Views
    private var userAndIcons: ArrayList<TextView>? = null
    private var groupName: TextView? = null
    private var startButton: AppCompatButton? = null
    private var stopButton: AppCompatButton? = null

    //Firebase
    protected var database: FirebaseDatabase? = null
    protected var firestore: FirebaseFirestore? = null
    private var firebaseStorage: FirebaseStorage? = null
    protected var myRef: DatabaseReference? = null
    protected var user: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Start IntervisionsLeader")
        sessionID = intent.extras!!.getString("SessionID")
        leader = intent.extras!!.getBoolean("Leader")
        init()
    }

    protected fun init() {
        firebaseStorage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()
        getItems()
        InitConnection()
        currentRound = 0
    }

    fun InitConnection() {
        Log.d(TAG, "sessionID" + sessionID)
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = database!!.getReference(sessionID!!)


        myRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value: String?
                value = if (dataSnapshot.value is String) {
                    dataSnapshot.getValue(String::class.java)
                } else {
                    val valueGot = dataSnapshot.getValue(Long::class.java)
                    valueGot.toString()
                }
                Log.d(TAG, "value " + value)
                Log.d(TAG, "leader " + leader)

                if(value != "W" && !leader!!){
                    ToIntervision()
                } else if(value != "W" && leader!!){
                    ToIntervisionLeader()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        firestore = FirebaseFirestore.getInstance()
        firestore!!.collection("Sessions")
            .document(sessionID!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    partisipantsIdS = document.data!!["Participant Sid"] as ArrayList<String>?
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
                GetPortraits()
            }
    }

    fun buttons(){
        if (!leader!!){
            startButton!!.visibility = View.INVISIBLE
        }else {
            startButton!!.setOnClickListener {
                Log.d("BUTTONS", "User tapped " + startButton)
                CheckIfLeader()
            }
        }
    }

    fun getItems(){
        setContentView(R.layout.activity_waiting_room)
        userAndIcons = ArrayList()
        userAndIcons!!.add(findViewById(R.id.user_and_icon_1_group_preview))
        userAndIcons!!.add(findViewById(R.id.user_and_icon_2_group_preview))
        userAndIcons!!.add(findViewById(R.id.user_and_icon_3_group_preview))
        userAndIcons!!.add(findViewById(R.id.user_and_icon_4_group_preview))
        userAndIcons!!.add(findViewById(R.id.user_and_icon_5_group_preview))
        userAndIcons!!.add(findViewById(R.id.user_and_icon_6_group_preview))
        groupName = findViewById(R.id.tv_groupname_waiting_room)
        startButton = findViewById(R.id.button_start_waiting_room)
        stopButton = findViewById(R.id.button_afzeggen_waiting_room)
        buttons()
    }

    private fun GetPortraits() {
        val storageRef = firebaseStorage!!.reference
        Log.d(TAG, "uIdList$partisipantsIdS")
        for (i in partisipantsIdS!!.indices) {
            Log.d(TAG, i.toString())
            val pathReference = storageRef.child("ProfilePictures/" + partisipantsIdS!![i.toInt()])
            val ONE_MEGABYTE = (1024 * 1024).toLong()

            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                bitmap = Bitmap.createScaledBitmap(bitmap!!, 180, 180, false)
                userAndIcons!![i.toInt()].setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    BitmapDrawable(resources, bitmap),
                    null,
                    null)
                userAndIcons!![i].text = partisipantsIdS!![i]
            }.addOnFailureListener { }
        }
    }
    private fun CheckIfLeader() {
        Log.d(TAG, "leaderId " + partisipantsIdS!![0] + " userid " + user!!.uid.toString())
        if (user!!.uid.toString() == partisipantsIdS!![0] ) {
            ToIntervisionLeader()
        } else {
            ToIntervision()
        }
    }

    private fun ToIntervision() {
        val i = Intent(this, Activity_Intervision::class.java)
        Log.d(TAG, "SessionId ")
        Log.d(TAG, "SessionId $sessionID")
        i.putExtra("SessionID", sessionID)
        i.putExtra("Leader", false)
        this.startActivity(i)
    }

    private fun ToIntervisionLeader() {
        val i = Intent(this, Activity_Intervision_Leader::class.java)
        Log.d(TAG, "SessionId ")
        Log.d(TAG, "SessionId $sessionID")
        i.putExtra("SessionID", sessionID)
        i.putExtra("Leader", true)
        this.startActivity(i)
    }

    companion object {
        private const val TAG = "WaitingRoomActivity"
    }
}