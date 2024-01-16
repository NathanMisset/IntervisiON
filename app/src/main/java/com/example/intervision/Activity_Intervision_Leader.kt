package com.example.intervision

import android.animation.LayoutTransition
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class Activity_Intervision_Leader : AppCompatActivity() {
    protected var sessionID: String? = null
    protected var progressBar: ProgressBar? = null
    protected var roundText: TextView? = null
    protected var headerText: TextView? = null
    protected var statusText: TextView? = null
    protected var special: View? = null
    protected var partisipantsIdS: ArrayList<String>? = null
    protected var ThesesID: String? = null
    protected var ROUNDNUMBERS = 5
    protected var intervisionManager: IntervisionManager? = null
    protected lateinit var intervisionRounds: Array<IntervisionRound?>
    protected var currentRound = 0
    protected var data: String? = null
    protected var firebaseevent: ValueEventListener? = null


    //Firebase
    protected var database: FirebaseDatabase? = null
    protected var firestore: FirebaseFirestore? = null
    protected var myRef: DatabaseReference? = null
    protected var storage: FirebaseStorage? = null
    protected var user: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intervisionRounds = arrayOfNulls(ROUNDNUMBERS)
        Log.d(TAG, "Start IntervisionsLeader")
        sessionID = intent.extras!!.getString("SessionID")
        init()
    }

    protected fun init() {
        getData()
        InitLayout()
        currentRound = 0
    }

    protected fun getData() {
        firestore = FirebaseFirestore.getInstance()
        firestore!!.collection("Sessions")
            .document(sessionID!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    partisipantsIdS = document.data!!["Participant Sid"] as ArrayList<String>?
                    Log.d(TAG, "List of users: " + document.data!!["Participant Sid"])

                    ThesesID = document.data!!["ThesisID"].toString()
                    Log.d(TAG, "ThesesID " + ThesesID)
                    FillContent()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    protected open fun InitConnection() {

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
                    Log.d(TAG, "Value 1 = " + value!![0])
                    if (value.length > 1) {
                        Log.d(TAG, "Value 2 = " + value[1])
                    }
                    if (value!!.length > 1) {
                        currentRound = value[0].toString().toInt()
                    } else if (value == "W") {
                        currentRound = 0
                        myRef!!.setValue(currentRound.toString())
                    } else {
                        currentRound = value.toInt()
                    }

                    Log.d(TAG, "currentRound = $currentRound")
                    Log.d(
                        TAG,
                        "intervisionRounds[currentRound]!!.roundTitle," + intervisionRounds[currentRound]!!.roundTitle,
                    )
                    ChangeRound(
                        intervisionRounds[currentRound]!!.roundTitle,
                        intervisionRounds[currentRound]!!.round,
                        "statusText",
                        intervisionRounds[currentRound]!!.roundSpecific
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

    }

    protected inner class IntervisionManager(
        var intervisionRounds: Array<IntervisionRound?>,
        private val participentIdS: ArrayList<String>?,
        var rounds: Int
    )

    protected open fun InitLayout() {
        setContentView(R.layout.activity_intervision_leaderview)
        roundText = findViewById(R.id.round_textView_intervision_leaderView)
        headerText = findViewById(R.id.header_intervision_leaderView)
        statusText = findViewById(R.id.status_text_intervision_leaderView)
        progressBar = findViewById(R.id.progressBar_intervision_leaderview)
        special = findViewById(R.id.special_layout_intervision_leaderView)
    }

    protected open fun FillContent() {
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
        val eleborateChoiceItem = Item_Eleborate_Choice(
            this,
            special,
            this,
            partisipantsIdS,
            user!!,
            firestore!!,
            storage!!
        )
        val Specials = arrayOf(
            LayoutInflater.from(special!!.context).inflate(
                R.layout.item_rounds_explained,
                special as ViewGroup?,
                false
            ),
            itemThesis.layout,
            LayoutInflater.from(special!!.context).inflate(
                R.layout.item_single_image,
                special as ViewGroup?,
                false
            ),
            eleborateChoiceItem.layout,
            LayoutInflater.from(special!!.context).inflate(
                R.layout.item_single_image,
                special as ViewGroup?,
                false
            )
        )

        for (i in 0 until ROUNDNUMBERS) {
            intervisionRounds[i] = IntervisionRound(
                headers[i],
                i + 1,
                Specials[i]
            )
        }
        intervisionManager = IntervisionManager(intervisionRounds, partisipantsIdS, ROUNDNUMBERS)
        //        ((ViewGroup) special).addView(intervisionRounds[0].roundSpecific);
        InitButtons()
        InitProgressButton()
        //        ChangeRound(intervisionRounds[currentRound].roundTitle,
//                intervisionRounds[currentRound].round,
//                "statusText",
//                intervisionRounds[currentRound].roundSpecific);
        InitConnection()
    }

    protected fun InitProgressButton() {
        val hdlr = Handler()
        Thread(object : Runnable {
            var i = 0
            override fun run() {
                while (i < 100) {
                    i += 1
                    // Update the progress bar and display the current value in text view
                    hdlr.post { progressBar!!.progress = i }
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }).start()
    }

    protected fun InitButtons() {
        val nextRoundButton = findViewById<View>(R.id.next_button_intervision_leaderView) as Button
        nextRoundButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the nextRoundButton")
            var newval = currentRound + 1
            myRef!!.setValue(newval.toString())
//            ChangeRound(
//                intervisionRounds[currentRound]!!.roundTitle,
//                intervisionRounds[currentRound]!!.round,
//                "statusText",
//                intervisionRounds[currentRound]!!.roundSpecific
//            )
        }
        val previousRoundButton = findViewById<View>(R.id.back_intervision_leaderView) as Button
        previousRoundButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the previousRoundButton")
            var newval = currentRound - 1
            myRef!!.setValue(newval.toString())
//            ChangeRound(
//                intervisionRounds[currentRound]!!.roundTitle,
//                intervisionRounds[currentRound]!!.round,
//                "statusText",
//                intervisionRounds[currentRound]!!.roundSpecific
//            )
        }
    }

    protected fun ChangeRound(
        roundTitle: String?,
        roundNumber: Int,
        statusText: String?,
        special: View?
    ) {
        roundText!!.text = "Ronde $roundNumber van 5"
        headerText!!.text = roundTitle
        (this.special as ViewGroup?)!!.removeAllViews()
        if (special!!.parent != null) {
            (special as ViewGroup?)!!.removeAllViews()
        }
        (this.special as ViewGroup?)!!.addView(special)

    }

    fun ChangeValue(a: Char) {
        myRef!!.setValue(currentRound.toString() + a)
    }

    protected inner class IntervisionRound(
        var roundTitle: String,
        var round: Int,
        var roundSpecific: View?
    ) {
        protected var participantTurn: String? = null

    }

    companion object {
        private const val TAG = "IntervisionLeaderActivi"
    }
}