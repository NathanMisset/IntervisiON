/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * This activity is where the applications start
 * useful to launch a specifike activity for debugging
 *
 */

class ActivityMain : AppCompatActivity() {
    private var loginType = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        when (loginType) {
            0 -> launchRegister()
            1 -> launchTutorial()
            2 -> launchLogin()
            5 -> launchMakeGroup()
            6 -> launchHome()
            7 -> launchFragmentTest()
        }
    }

    private fun launchRegister() {
        val i = Intent(this, ActivityRegister::class.java)
        startActivity(i)
    }

    private fun launchTutorial() {
        val i = Intent(this, ActivityTutorial::class.java)
        startActivity(i)
    }

    private fun launchLogin() {
        val i = Intent(this, ActivityLogin::class.java)
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
}