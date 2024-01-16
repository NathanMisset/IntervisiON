package com.example.intervision

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Activity_Intervision : Activity_Intervision_Leader() {
    protected var turnItem: Item_Turn? = null
    protected var PreviousValue: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "load " + TAG)
        super.onCreate(savedInstanceState)

    }

    override fun InitLayout() {
        setContentView(R.layout.activity_intervision)
        roundText = findViewById(R.id.round_text_intevision)
        headerText = findViewById(R.id.header_intervision)
        statusText = findViewById(R.id.status_intervision)
        progressBar = findViewById(R.id.progressBar_intervision)
        special = findViewById(R.id.special_layout_intervision)
    }

    override fun InitConnection() {
        Log.d(TAG, "InitConnection ")
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = database!!.getReference(sessionID!!)

            // Read from the database
            firebaseevent = myRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Log.d(TAG, "data type: " + dataSnapshot.value!!.javaClass)
                    val value: String?
                    value = if (dataSnapshot.value is String) {
                        dataSnapshot.getValue(String::class.java)
                    } else {
                        val valueGot = dataSnapshot.getValue(Long::class.java)
                        valueGot.toString()
                    }
                    Log.d(TAG, "Value = $value")
                    Log.d(TAG, "PreviousValue = $PreviousValue")
                    if (value.toString() == PreviousValue.toString()) {
                        Log.d(TAG, "EventCanceled")
                        myRef!!.removeEventListener(this)
                    }

                    Log.d(TAG, "Value = $value")
                    Log.d(TAG, "Value 1 = " + value!![0])
                    if (value.length > 1) {
                        Log.d(TAG, "Value 2 = " + value[1])
                    }

                    if (value!!.length > 1) {
                        currentRound = value[0].toString().toInt()
                    } else {
                        currentRound = value.toInt()
                    }
                    ChangeRound(
                        intervisionRounds[currentRound]!!.roundTitle,
                        intervisionRounds[currentRound]!!.round,
                        "statusText",
                        intervisionRounds[currentRound]!!.roundSpecific
                    )
                    if (value.length > 1) {
                        turnItem!!.GiveTurn(value[1])
                    }

                    PreviousValue = value
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
                    Log.d(TAG, "List of users: " + document.data!!["Participant Sid"])
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    override fun FillContent() {
        Log.d(TAG, "FillConent ")
        val headers = arrayOf(
            getString(R.string.header_1_intervision),
            getString(R.string.header_2_intervision),
            getString(R.string.header_3_intervision),
            getString(R.string.header_4_intervision),
            getString(R.string.header_5_intervision)
        )
        user = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        val itemThesis = Item_thesis(
            storage!!,
            firestore!!,
            this.special!!,
            this,
            partisipantsIdS!!,
            this.special,
            this.ThesesID
        )
        turnItem = Item_Turn(storage!!, firestore!!, special!!, this, partisipantsIdS!!, special)
        val Specials = arrayOf(
            LayoutInflater.from(special!!.context).inflate(
                R.layout.item_rounds_explained,
                special as ViewGroup,
                false
            ),
            itemThesis.layout,
            LayoutInflater.from(special!!.context).inflate(
                R.layout.item_single_image,
                special as ViewGroup,
                false
            ),
            turnItem!!.layout,
            turnItem!!.layout
        )
        val images = arrayOf(
            Item_Single_View(R.drawable.image_chat_128x128, Specials[2])
        )
        intervisionRounds = arrayOfNulls(ROUNDNUMBERS)
        for (i in 0 until ROUNDNUMBERS) {
            intervisionRounds[i] = IntervisionRound(
                headers[i],
                i + 1,
                Specials[i]
            )
        }
        //        ((ViewGroup) special).addView(intervisionRounds[0].roundSpecific);
        InitProgressButton()
        InitConnection()
    }

    companion object {
        private const val TAG = "IntervisionActivity"
    }
}