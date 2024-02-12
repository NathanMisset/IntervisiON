package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Activity_Test_Connection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_connection)
        val joinLeaderButton = findViewById<View>(R.id.leader_button_test_connection) as Button
        joinLeaderButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the RegisterButton")
            launchIntervisionLeader()
        }
        val joinParticipantButton =
            findViewById<View>(R.id.participant_button_test_connection) as Button
        joinParticipantButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the RegisterButton")
            launchIntervision()
        }
    }

    private fun launchIntervisionLeader() {
        //Launch Image activity
        val i = Intent(this, ActivityIntervisionLeader::class.java)
        startActivity(i)
    }

    private fun launchIntervision() {
        //Launch Image activity
        val i = Intent(this, ActivityIntervision::class.java)
        startActivity(i)
    }
}