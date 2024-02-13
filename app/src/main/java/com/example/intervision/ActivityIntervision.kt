package com.example.intervision

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class ActivityIntervision : ActivityIntervisionLeader() {
    private var itemDiscusionUser: ItemDiscusionUser? = null

    override fun initItems() {
        itemVote = ItemVote(firestore!!, thesesID)
        itemVote!!.init()
        itemDiscusionUser = ItemDiscusionUser(storage!!,firestore!!,partisipantsIdS!!)
        itemDiscusionUser!!.init()
        Log.d(TAG, "initConnect")
        initConnection()
    }

    override fun initConnection() {
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
                    val value: String? = if (dataSnapshot.value is String) {
                        dataSnapshot.getValue(String::class.java)
                    } else {
                        val valueGot = dataSnapshot.getValue(Long::class.java)
                        valueGot.toString()
                    }



                    Log.d(TAG, "Value = $value")
                    Log.d(TAG, "Value 1 = " + value!![0])
                    if (value.length > 1) {
                        Log.d(TAG, "Value 2 = " + value[1])
                    }

                    currentRound = if (value.length > 1) {
                        value[0].toString().toInt()
                    } else {
                        value.toInt()
                    }
                    changeRound(currentRound!!)
                    if (value.length > 1) {
                        itemDiscusionUser!!.giveTurn(value[1])
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
                    @Suppress("UNCHECKED_CAST")
                    partisipantsIdS = document.data!!["Participant Sid"] as ArrayList<String>?
                    Log.d(TAG, "List of users: " + document.data!!["Participant Sid"])
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    override fun fillContent() {
        Log.d(TAG, "FillConent ")
        intervisionRounds = arrayOfNulls(rOUNDNUMBERS)
        for (i in 0 until rOUNDNUMBERS) {
            intervisionRounds[i] = IntervisionRound()
        }
        initItems()
    }
    override fun changeRound(roundNumber: Int) {
        when (roundNumber) {
            0 -> {
                setContent {
                    FirstRound()
                }
                Log.d(TAG, "round 1")
            }
            1 ->{
                setContent {
                    SecondRound()
                }
                Log.d(TAG, "round 2")
            }
            2 -> {
                setContent {
                    ThirdRound()
                }
                Log.d(TAG, "round 3")
            }
            3 -> {
                setContent {
                    ForthRound()
                }
                Log.d(TAG, "round 4")
            }
            4 -> {
                setContent {
                    FifthRound()
                }
                Log.d(TAG, "round 5")
            }
        }
    }
    @Composable
    override fun DefaultButtonRow(){

    }
    @Composable
    override fun ForthRound(){
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemDiscusionUser!!.Component()
            }
        }
    }

    companion object {
        private const val TAG = "IntervisionActivity"
    }
}