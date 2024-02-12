package com.example.intervision

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityMain : AppCompatActivity() {
    var loginType = 2
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
        val i = Intent(this, Activity_Image_From_User::class.java)
        startActivity(i)
    }

    fun launchRegister() {
        //Launch register activity
        val i = Intent(this, Activity_Register::class.java)
        startActivity(i)
    }
    fun launchCompose() {
        //Launch register activity
        val i = Intent(this, ActivityJetpackCompose::class.java)
        startActivity(i)
    }

    fun launchTutorial() {
        //Launch Tutorial activity
        val i = Intent(this, Activity_Tutorial::class.java)
        startActivity(i)
    }

    fun launchLogin() {
        val i = Intent(this, ActivityLogin::class.java)
        startActivity(i)
    }

    fun launchIntervision() {
        val i = Intent(this, Activity_Test_Connection::class.java)
        startActivity(i)
    }

    fun launchMakeGroup() {
        val i = Intent(this, Activity_Make_Group::class.java)
        startActivity(i)
    }

    fun launchHome() {
        val i = Intent(this, Activity_Navigation::class.java)
        startActivity(i)
    }

    fun launchFragmentTest() {
        val i = Intent(this, Activity_Navigation::class.java)
        startActivity(i)
    }

    fun launchJetpackCompose() {
        val i = Intent(this, ActivityJetpackCompose::class.java)
        startActivity(i)
    }


}