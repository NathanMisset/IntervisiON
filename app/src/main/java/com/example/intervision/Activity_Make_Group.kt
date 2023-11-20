package com.example.intervision

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Activity_Make_Group : AppCompatActivity() {
    private var leader: TextView? = null
    private var groupName: TextView? = null
    private var user: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var fireStoreDatabase: FirebaseFirestore? = null
    private var mainSpinner: Item_Spinner? = null
    var spinners: ArrayList<Item_Spinner>? = null
    private lateinit var users: ArrayList<String>
    private var UIDs: ArrayList<String?>? = null
    var activity: Activity? = null
    var postionMainSpinner = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_group)
        user = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        fireStoreDatabase = FirebaseFirestore.getInstance()
        groupName = findViewById(R.id.group_name_make_group)
        users = ArrayList()
        UIDs = ArrayList()
        users!!.add("Gebruiker")
        UIDs!!.add(null)
        postionMainSpinner = 0
        val makeGroupButton = findViewById<View>(R.id.make_group_button) as Button
        makeGroupButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the makeGroupButton")
            MakeGroup()
        }
        data
    }

    fun setSpinner() {
        val listOfNUmbers: ArrayList<String> = arrayListOf("0","1","2","3","4","5" )

        mainSpinner = Item_Spinner(listOfNUmbers, findViewById(R.id.layout_make_group), this)
        mainSpinner!!.init()
        spinners = ArrayList()
        Log.d(TAG, "users: $users")
        for (i in 0..listOfNUmbers.size) {
            spinners!!.add(Item_Spinner(users, findViewById(R.id.layout_make_group), this))
            spinners!![i].init()
            spinners!![i].Disable()
        }
        mainSpinner!!.dropdown!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    postionMainSpinner = position
                    for (i in position until spinners!!.size) {
                        spinners!![i].Disable()
                        Log.d(TAG, "Disable spinner: $i")
                    }
                    Log.d(TAG, "spinner: $spinners")
                    for (i in 0 until position) {
                        spinners!![i].Enable()
                        Log.d(TAG, "Enable spinner: $i")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    fun SetName(data: Map<String?, Any>) {
        leader = findViewById(R.id.leader_name_make_group)
        leader!!.setText("Groepsleider: " + data["Voornaam"].toString())
    }

    private val data: Unit
        private get() {
            fireStoreDatabase!!.collection("User Data")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, " => UserID " + document.data["User UID"])
                            Log.d(TAG, user!!.uid!!)
                            val u = user!!.uid
                            val d = document.data["User UID"] as String?
                            if (document.data["Voornaam"].toString().length > 0) {
                                users!!.add(document.data["Voornaam"].toString())
                                UIDs!!.add(d)
                            }
                            if (u == d) {
                                Log.d(TAG, "Inside if")
                                SetName(document.data)
                            }
                        }
                        setSpinner()
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    private fun MakeGroup() {
        val participants = ArrayList<String?>()
        participants.add(user!!.uid)
        for (i in 0 until mainSpinner!!.dropdown!!.selectedItem as Int) {
            if (spinners!![i].dropdown!!.selectedItem !== "Gebruiker") {
                participants.add(UIDs!![spinners!![i].dropdown!!.selectedItemPosition])
            }
        }
        val session: MutableMap<String, Any?> = HashMap()
        session["Leader Sid"] = user!!.uid
        session["Participant Sid"] = participants
        session["Group Name"] = groupName!!.text.toString()
        fireStoreDatabase!!.collection("Sessions")
            .add(session)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                // Write a message to the database
                val database =
                    FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
                val myRef = database.getReference(documentReference.id)
                myRef.setValue(0)
                ToHome()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun ToHome() {
        val i = Intent(this, Activity_Navigation::class.java)
        startActivity(i)
    }

    companion object {
        private const val TAG = "MakeGroupActivity"
    }
}