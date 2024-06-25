/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.UiString
import com.example.intervision.ui.spacing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *
 * This activity controls waitingroom. This is where the user wait before the session begins.
 *
 */

class ActivityWaitingRoom : ComponentActivity() {

    /** Class variables */
    private var sessionID: String? = null
    private var participantsIdS: ArrayList<String>? = null
    private var participantsNamesTemp: ArrayList<String>? = null
    private lateinit var participantsNames: MutableList<List<String>>
    private lateinit var groupName: MutableState<String>
    private var currentRound = 0
    private var leader: Boolean? = null

    /** Firebase */
    private var database: FirebaseDatabase? = null
    private var firestore: FirebaseFirestore? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var myRef: DatabaseReference? = null
    private var user: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionID = intent.extras!!.getString("SessionID")
        leader = intent.extras!!.getBoolean("Leader")
        init()
    }

    private fun init() {
        /** Firebase */
        firebaseStorage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        currentRound = 0
        participantsNamesTemp = ArrayList()
        fillLists()
        initConnection()
    }

    private fun fillLists(){
        groupName = mutableStateOf("Groepname: Leeg")
        participantsNames = mutableStateListOf(listOf("A","B","C","D","E","F"))
    }

    /**
     *
     * initConnection will listen to the session variable.
     * If the value is W it will stay in this activity.
     * Based on if the user is the leader they will go to the different type of intervision activity
     *
     */
    private fun initConnection() {
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = database!!.getReference(sessionID!!)

        myRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value: String? = if (dataSnapshot.value is String) {
                    dataSnapshot.getValue(String::class.java)
                } else {
                    val valueGot = dataSnapshot.getValue(Long::class.java)
                    valueGot.toString()
                }
                if(value != "W" && !leader!!){
                    myRef!!.removeEventListener(this)
                    toIntervision()
                } else if(value != "W" && leader!!){
                    myRef!!.removeEventListener(this)
                    toIntervisionLeader()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                /** Failed to read value */
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
                    participantsIdS = document.data!!["Participant Sid"] as ArrayList<String>?
                    groupName.value = "Groepsnaam: " + document.data!!["Group Name"].toString()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
                getUserData()
            }
    }

    private fun getUserData(){
            firestore!!.collection("User Data")
                .whereIn("User UID", participantsIdS!!)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            participantsNamesTemp!!.add(document.data["Voornaam"].toString())
                        }
                        for(i in participantsNamesTemp!!.size..5){
                            participantsNamesTemp!!.add("Leeg")
                        }
                        participantsNames = mutableListOf(participantsNamesTemp!!.toList())
                        setContent {
                            Screen()
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
    }

    private fun toIntervision() {
        val i = Intent(this, ActivityIntervision::class.java)
        i.putExtra(UiString.intentPutExtraSessionIDWaitingRoom, sessionID)
        i.putExtra(UiString.intentPutExtraSessionIDWaitingRoom, false)
        this.startActivity(i)
    }

    private fun toIntervisionLeader() {
        val i = Intent(this, ActivityIntervisionLeader::class.java)
        i.putExtra(UiString.intentPutExtraSessionIDWaitingRoom, sessionID)
        i.putExtra(UiString.intentPutExtraSessionIDWaitingRoom, true)
        this.startActivity(i)
    }

    @Preview(device = "spec:width=1080px,height=2280px,dpi=400")
    @Composable
    fun Screen() {
        IntervisionBaseTheme {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = ComposableUiString.desciptionWaitingRoom)
                Text(text = groupName.value,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = spacing.small,
                            vertical = spacing.default
                        ),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    ElevatedCard {
                        Image(painter = painterResource(id = R.drawable.temp_profile_picture_192x192)
                            , contentDescription = ComposableUiString.profile1ImageDescriptionWaitingRoom,
                            modifier = Modifier
                                .padding(spacing.small))
                        Text(text = participantsNames[0][0],
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(spacing.small))
                    }
                    ElevatedCard {
                        Image(painter = painterResource(id = R.drawable.temp_profile_picture_192x192)
                            , contentDescription = ComposableUiString.profile2ImageDescriptionWaitingRoom,
                            modifier = Modifier
                                .padding(spacing.small))
                        Text(text = participantsNames[0][1],
                            modifier = Modifier
                                .padding(spacing.small))
                    }
                    ElevatedCard {
                        Image(painter = painterResource(id = R.drawable.temp_profile_picture_192x192)
                            , contentDescription = ComposableUiString.profile3ImageDescriptionWaitingRoom,
                            modifier = Modifier
                                .padding(spacing.small))
                        Text(text = participantsNames[0][2],
                            modifier = Modifier
                                .padding(spacing.small))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = spacing.small,
                            vertical = spacing.default
                        ),
                horizontalArrangement = Arrangement.SpaceAround) {
                    ElevatedCard {
                        Image(painter = painterResource(id = R.drawable.temp_profile_picture_192x192)
                            , contentDescription = ComposableUiString.profile4ImageDescriptionWaitingRoom,
                            modifier = Modifier
                                .padding(spacing.small))
                        Text(text = participantsNames[0][3],
                            modifier = Modifier
                                .padding(spacing.small))
                    }
                    ElevatedCard {
                        Image(painter = painterResource(id = R.drawable.temp_profile_picture_192x192)
                            , contentDescription = ComposableUiString.profile5ImageDescriptionWaitingRoom,
                            modifier = Modifier
                                .padding(spacing.small))
                        Text(text = participantsNames[0][4],
                            modifier = Modifier
                                .padding(spacing.small))
                    }
                    ElevatedCard {
                        Image(painter = painterResource(id = R.drawable.temp_profile_picture_192x192)
                            , contentDescription = ComposableUiString.profile6ImageDescriptionWaitingRoom,
                            modifier = Modifier
                                .padding(spacing.small))
                        Text(text = participantsNames[0][5],
                            modifier = Modifier
                                .padding(spacing.small),
                            )
                    }
                }
                Column(verticalArrangement = Arrangement.SpaceEvenly){
                    if(leader == true) {
                        Button(
                            onClick = { toIntervisionLeader() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = spacing.default,
                                    bottom = spacing.default,
                                    start = spacing.medium,
                                    end = spacing.medium
                                )
                        ) {
                            Text(text = ComposableUiString.startButtonWaitingRoom)
                        }
                    }

                    Button(onClick = { finish() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = spacing.default,
                                bottom = spacing.default,
                                start = spacing.medium,
                                end = spacing.medium
                            )
                    ) {
                        Text(text = ComposableUiString.leaveButtonWaitingRoom)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "WaitingRoomActivity"
    }
}