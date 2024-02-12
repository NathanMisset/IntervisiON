package com.example.intervision

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
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
open class ActivityIntervisionLeader : ComponentActivity() {
    //
    protected var sessionID: String? = null
    protected var partisipantsIdS: ArrayList<String>? = null
    protected var thesesID: String? = null
    protected var rOUNDNUMBERS = 5
    protected lateinit var intervisionRounds: Array<IntervisionRound?>
    protected var currentRound : Int? = null
    protected var data: String? = null
    protected var firebaseevent: ValueEventListener? = null

    //Items
    protected var itemVote: ItemVote? = null
    protected var itemElborateChose: ItemDiscusionLeader? = null

    //Firebase
    protected var database: FirebaseDatabase? = null
    protected var firestore: FirebaseFirestore? = null
    protected var myRef: DatabaseReference? = null
    protected var storage: FirebaseStorage? = null
    protected var user: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intervisionRounds = arrayOfNulls(rOUNDNUMBERS)
        Log.d(TAG, "Start IntervisionsLeader")
        sessionID = intent.extras!!.getString("SessionID")
        IIIIInit()
    }

    protected fun IIIIInit() {
        initVar()
        getData()
        initLayout()
        currentRound = 0
    }

    protected fun getData() {
        firestore!!.collection("Sessions")
            .document(sessionID!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    @Suppress("UNCHECKED_CAST")
                    partisipantsIdS = document.data!!["Participant Sid"] as ArrayList<String>?
                    Log.d(TAG, "List of users: " + document.data!!["Participant Sid"])

                    thesesID = document.data!!["ThesisID"].toString()
                    Log.d(TAG, "ThesesID $thesesID")
                    fillContent()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    protected open fun initConnection() {
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
                    currentRound = value[0].toString().toInt()
                } else if (value == "W") {
                    currentRound = 0
                    myRef!!.setValue(currentRound.toString())
                } else {
                    currentRound = value.toInt()
                    changeRound(currentRound!!)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
    protected fun initVar(){
        //firebase
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        user = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        myRef = database!!.getReference(sessionID!!)
        // Items
    }
    protected open fun initItems(){
        itemVote = ItemVote(firestore!!, thesesID)
        itemVote!!.init()
        itemElborateChose = ItemDiscusionLeader(this,user!!,firestore!!,partisipantsIdS!!)
        itemElborateChose!!.init()
        Log.d(TAG, "initConnect")
        initConnection()
    }
    protected fun initLayout() {
        setContent {
            FirstRound()
        }
    }

    protected open fun fillContent() {
        for (i in 0 until rOUNDNUMBERS) {
            intervisionRounds[i] = IntervisionRound()
        }
        initItems()
    }



    protected open fun changeRound(roundNumber: Int) {
        when (roundNumber) {
            0 ->
                setContent {
                    FirstRound()
                }
            1 ->
                setContent {
                    SecondRound()
                }
            2 ->
                setContent {
                    ThirdRound()
                }
            3 ->
                setContent {
                    ForthRound()
                }
            4 ->
                setContent {
                    FifthRound()
                }
        }
    }

    fun changeValue(a: Char) {
        myRef!!.setValue(currentRound.toString() + a)
    }

    protected inner class IntervisionRound


//    @Composable
//    protected fun DefaultColumn() {
//        MyApplicationTheme {
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth()
//                    .background(color = MaterialTheme.colorScheme.background),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.SpaceAround
//            ) {
//
//            }
//        }
//    }

    @Composable
    protected open fun DefaultButtonRow(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                Log.d("BUTTONS", "User tapped the previousRoundButton")
                val newval = currentRound!! - 1
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = "Terug")
            }
            Button(onClick = {
                Log.d("BUTTONS", "User tapped the nextRoundButton")
                val newval = currentRound!! + 1
                myRef!!.setValue(newval.toString())
            }) {
                Text(text = "Volgende")
            }
        }
    }

    @Composable
    protected fun FirstRound() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ItemRoundsExplained().Component()
                DefaultButtonRow()
            }
        }
    }
    @Composable
    protected fun SecondRound() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemVote!!.Component()
                DefaultButtonRow()
            }
        }
    }
    @Preview(device = "spec:width=1080px,height=2280px,dpi=400")
    @Composable
    protected fun ThirdRound() {

        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ItemRoundThird().Component()
                DefaultButtonRow()
            }
        }
    }

    @Composable
    protected open fun ForthRound() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                itemElborateChose!!.Component()
                DefaultButtonRow()
            }
        }
    }
    @Composable
    protected fun FifthRound() {
        MyApplicationTheme {
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
    companion object {
        private const val TAG = "Activity Leader"
    }
}
