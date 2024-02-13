package com.example.intervision

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityMain : AppCompatActivity() {
    private var loginType = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_login)
        when (loginType) {
            0 -> launchRegister()
            1 -> launchTutorial()
            2 -> launchLogin()
            3 -> launchImage()
            4 -> launchIntervision()
            5 -> launchMakeGroup()
            6 -> launchHome()
            7 -> launchFragmentTest()
            8 -> launchJetpackCompose()
            9 -> launchCompose()
        }
    }

    private fun launchImage() {
        //Launch Image activity
        val i = Intent(this, ActivityImageFromUser::class.java)
        startActivity(i)
    }

    private fun launchRegister() {
        //Launch register activity
        val i = Intent(this, ActivityRegister::class.java)
        startActivity(i)
    }
    private fun launchCompose() {
        //Launch register activity
        val i = Intent(this, ActivityJetpackCompose::class.java)
        startActivity(i)
    }

    private fun launchTutorial() {
        //Launch Tutorial activity
        val i = Intent(this, ActivityTutorial::class.java)
        startActivity(i)
    }

    private fun launchLogin() {
        val i = Intent(this, ActivityLogin::class.java)
        startActivity(i)
    }

    private fun launchIntervision() {
        val i = Intent(this, ActivityTestConnection::class.java)
        startActivity(i)
    }

    private fun launchMakeGroup() {
        val i = Intent(this, ActivityMakeGroup::class.java)
        startActivity(i)
    }

    private fun launchHome() {
        val i = Intent(this, ActivityNavigation::class.java)
        startActivity(i)
    }

    private fun launchFragmentTest() {
        val i = Intent(this, ActivityNavigation::class.java)
        startActivity(i)
    }

    private fun launchJetpackCompose() {
        val i = Intent(this, ActivityJetpackCompose::class.java)
        startActivity(i)
    }


}