/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * This activity is a splash screen at the moment just shows at the start of the app
 * Should be used in the future as a loading screen and inform user of the backend activity
 *
 */

class ActivitySplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
            startActivity(intent)
            finish()
        }, 100)
    }
}