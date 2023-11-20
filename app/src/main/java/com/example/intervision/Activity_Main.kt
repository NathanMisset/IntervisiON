package com.example.intervision

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class Activity_Main : AppCompatActivity() {
    var loginType = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
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

    fun launchTutorial() {
        //Launch Tutorial activity
        val i = Intent(this, Activity_Tutorial::class.java)
        startActivity(i)
    }

    fun launchLogin() {
        val i = Intent(this, Activity_Login::class.java)
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
        val i = Intent(this, Activity_Jetpack_Compose::class.java)
        startActivity(i)
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}