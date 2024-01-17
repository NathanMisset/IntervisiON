package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Activity_Reset_Password : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    var emailText: EditText? = null
    var sendButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        auth = FirebaseAuth.getInstance()

//      Button
        sendButton = findViewById(R.id.send_button_reset_password)
        sendButton!!.setOnClickListener(View.OnClickListener {
            Log.d("BUTTONS", "User tapped the SendButton")
            ResetPassword("nl")
        })
        val backButton = findViewById<Button>(R.id.back_button_reset_password)
        backButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the backButton")
            ToLogin()
        }
        emailText = findViewById(R.id.email_reset_password)
    }

    private fun ToLogin() {
        val i = Intent(this, Activity_Login::class.java)
        startActivity(i)
    }

    protected fun ResetPassword(languageCode: String?) {
        auth!!.setLanguageCode(languageCode!!)
        auth!!.sendPasswordResetEmail(emailText!!.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent to: " + emailText!!.text.toString())
                    MailSend()
                }
            }
    }

    protected fun MailSend() {
        val button = findViewById<Button>(R.id.back_button_reset_password)
        button.visibility = View.VISIBLE
        sendButton!!.visibility = View.GONE
    }

    companion object {
        private const val TAG = "Reset Password"
    }
}