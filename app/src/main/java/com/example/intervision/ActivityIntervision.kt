/*
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.UiString
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

/**
 *
 * This activity controls the Intervision for a non leader user
 * It contains mulitiple complex items that are control from a different class
 *
 */
class ActivityIntervision : ActivityIntervisionLeader() {

    private var itemDiscusionUser: ItemDiscusionUser? = null

    /**
     *
     * fillContent
     *
     */
    override fun fillContent() {
        intervisionRounds = arrayOfNulls(rOUNDNUMBERS)
        for (i in 0 until rOUNDNUMBERS) {
            intervisionRounds[i] = IntervisionRound()
        }
        initItems()
    }

    /**
     *
     * initItems Instantiate items that a more complex
     *
     */
    override fun initItems() {
        itemFinalRound = ItemFinalRound()
        itemVote = ItemVote(firestore!!, thesesID)
        itemVote!!.init()
        itemDiscusionUser = ItemDiscusionUser(storage!!, firestore!!, partisipantsIdS!!)
        itemDiscusionUser!!.init()
        initConnection()
    }

    /**
     *
     * initConnection creates a connection with the firebase firestore database
     * Then creates a connection with the firebase realtime database as an event
     * It will listen to the varaible connected to the session ID
     * When the Activity Leader Changes the this value this event will be called
     * And Make sure that changeRound is called
     *
     * Lastly it get the data of all member that are part of the group
     *
     */
    override fun initConnection() {
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = database!!.getReference(sessionID!!)

            firebaseevent = myRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value: String? = if (dataSnapshot.value is String) {
                        dataSnapshot.getValue(String::class.java)
                    } else {
                        val valueGot = dataSnapshot.getValue(Long::class.java)
                        valueGot.toString()
                    }

                    if (value!!.length > 1) {
                        currentRound = value[0].toString().toInt()
                        changeRound(currentRound!!)
                    } else if (value == "S") {
                         myRef!!.removeEventListener(this)
                         setContent {
                             QuitRound()
                         }
                    } else {
                        currentRound = value.toInt()
                        changeRound(currentRound!!)
                    }

                    if (value.length > 1) {
                        itemDiscusionUser!!.giveTurn(value[1])
                    }

                }

                override fun onCancelled(error: DatabaseError) {
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
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    /**
     *
     * changeRound is mostly called from the event in initConnection
     *
     */
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
            5 -> {
                setContent {
                    FinalRound()
                }
                Log.d(TAG, "final round")
            }
        }
    }

    /** Composables */
    @Composable
    override fun FirstRound() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ItemRoundsExplained().ComponentParticipant()
            }
        }
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

    @Composable
    override fun FifthRound() {
        /** TODO  Implement Fifth Round*/
    }
    @Composable
    override fun FinalRound() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemFinalRound!!.ComponentParticipant()
                Text(text = UiString.wachtTekstIntervisie)
            }
        }
    }

    companion object {
        private const val TAG = "IntervisionActivity"
    }
}