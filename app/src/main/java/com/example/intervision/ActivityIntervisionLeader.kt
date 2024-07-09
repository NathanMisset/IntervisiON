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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.intervision.ui.IntervisionBaseTheme
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
 * This activity controls the Intervision for a leader user
 * It contains mulitiple complex items that are control from a different class
 *
 */

open class ActivityIntervisionLeader : ComponentActivity() {

    /** Class variables */
    protected var sessionID: String? = null
    protected var thesesID: String? = null
    protected var partisipantsIdS: ArrayList<String>? = null
    protected var currentRound : Int? = null
    protected var data: String? = null

    /** Items */
    protected var itemVote: ItemVote? = null
    private var itemElborateChose: ItemDiscusionLeader? = null
    protected var itemFinalRound: ItemFinalRound? = null

    /** Firebase */
    protected var database: FirebaseDatabase? = null
    protected var firestore: FirebaseFirestore? = null
    protected var myRef: DatabaseReference? = null
    protected var storage: FirebaseStorage? = null
    protected var user: FirebaseAuth? = null
    protected var firebaseevent: ValueEventListener? = null
    private var firebaseURL: String = "https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app"

    /** Initialisation */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionID = intent.extras!!.getString("SessionID")
        init()
    }

    private fun init() {
        initVar()
        getData()
    }

    private fun initVar(){
        //firebase
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        user = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance(firebaseURL)
        myRef = database!!.getReference(sessionID!!)
        // Items
        currentRound = 0
    }

    private fun getData() {
        firestore!!.collection("Sessions")
            .document(sessionID!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    @Suppress("UNCHECKED_CAST")
                    partisipantsIdS = document.data!!["Participant Sid"] as ArrayList<String>?
                    thesesID =  document.data!!["ThesisID"].toString().substring(1, document.data!!["ThesisID"].toString().length - 1)
                    initItems()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    protected open fun initItems() {
        itemFinalRound = ItemFinalRound()
        itemVote = ItemVote(firestore!!, thesesID)
        itemVote!!.init()
        itemElborateChose = ItemDiscusionLeader(this, user!!, firestore!!, partisipantsIdS!!)
        itemElborateChose!!.init()
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
     * The value can be 1 to 5 for the round
     * This can have a character from 'a' to 'f' this is needed to give user turns
     * W for Waitingroom
     * S for Stop meaning end of session
     *
     */

    protected open fun initConnection() {
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
                } else if (value == "W") {
                    currentRound = 0
                    myRef!!.setValue(currentRound.toString())
                } else if(value == "S"){
                    myRef!!.removeEventListener(this)
                    setContent {
                        QuitRound()
                    }
                } else {
                    currentRound = value.toInt()
                    changeRound(currentRound!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    /**
     *
     * changes the character to give a new user a turn
     * More info in ItemDiscusionLeader.kt
     *
     */
    fun changeValue(a: Char) {
        myRef!!.setValue(currentRound.toString() + a)
    }

    protected open fun changeRound(roundNumber: Int) {
        when (roundNumber) {
            0 ->
                setContent {
                    Round1()
                }
            1 ->
                setContent {
                    Round2()
                }
            2 ->
                setContent {
                    Round3()
                }
            3 ->
                setContent {
                    Round4()
                }
            4 ->
                setContent {
                    Round5()
                }
            5 ->
                setContent {
                    SessionEnd()
                }
        }
    }

    private fun toHome() {
        startActivity(Intent(this, ActivityNavigation::class.java))
        finish()
    }

    /** Composables */
    @Composable
    protected open fun DefaultButtonRow(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val newval = currentRound!! - 1
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = getString(R.string.backButtonApp))
            }
            Button(onClick = {
                val newval = currentRound!! + 1
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = getString(R.string.nextButtonApp))
            }
        }
    }
    @Composable
    protected open fun FirstButtonRow(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val newval = currentRound!! + 1
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = getString(R.string.nextButtonApp))
            }
        }
    }
    @Composable
    protected open fun LastButtonRow(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val newval = currentRound!! - 1
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = getString(R.string.backButtonApp))
            }
            Button(onClick = {
                val newval = 'S'
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = getString(R.string.buttonTextItemFinalRound))
            }
        }
    }

    @Composable
    protected open fun Round1() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ItemRoundsExplained().Screen()
                FirstButtonRow()
            }
        }
    }
    @Composable
    protected open fun Round2() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemVote!!.Screen()
                DefaultButtonRow()
            }
        }
    }
    @Preview(device = "spec:width=1080px,height=2280px,dpi=400")
    @Composable
    protected open fun Round3() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemElborateChose!!.Screen()
                DefaultButtonRow()
            }
        }
    }

    @Composable
    protected open fun Round4() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ItemRound3().Screen()
                DefaultButtonRow()
            }
        }
    }
    @Composable
    protected open fun Round5() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ItemAction().Component()
                DefaultButtonRow()
            }
        }
    }
    @Composable
    protected open fun SessionEnd() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemFinalRound!!.Screen()
                LastButtonRow()
            }
        }
    }
    @Composable
    protected open fun QuitRound() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    toHome()
                    }) {
                    Text(text = getString(R.string.backHomeIntervisionLeader))
                }
            }
        }
    }
    companion object {
        private const val TAG = "Activity Leader"
    }
}
