package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Activity_Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var username: EditText? = null
    private var password: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
//        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login)
        val loginButton = findViewById<View>(R.id.button_login) as Button
        loginButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the LoginButton")
            username = findViewById(R.id.username_login)
            password = findViewById(R.id.password_login)
            if (CheckAllFields()) {
                signIn(username!!.getText().toString(), password!!.getText().toString())
            }
        }
        val toRegisteryButton = findViewById<View>(R.id.register_button_login) as Button
        toRegisteryButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the RegisterButton")
            ToRegister()
        }
        val toResetPassword = findViewById<View>(R.id.forgot_password_login) as Button
        toResetPassword.setOnClickListener {
            Log.d("BUTTONS", "User tapped the RegisterButton")
            ToRestPassword()
        }
    }

    private fun Toast(Message: String) {
        Toast.makeText(
            this@Activity_Login, Message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun CheckAllFields(): Boolean {
        var check = true
        if (username!!.length() == 0) {
            val error = "Gebruikersnaam is verplich"
            Toast(error)
            username!!.error = error
            check = false
        }
        if (password!!.length() == 0) {
            val error = "Wachtwoord is verplicht"
            Toast(error)
            password!!.error = "Wachtwoord is verplicht"
            check = false
        }
        return check
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@Activity_Login, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun ToRestPassword() {
        val i = Intent(this, Activity_Reset_Password::class.java)
        startActivity(i)
    }

    private fun ToRegister() {
        val i = Intent(this, Activity_Register::class.java)
        startActivity(i)
    }

    private fun reload() {
        val i = Intent(this, Activity_Navigation::class.java)
        startActivity(i)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) return
        reload()
    }

    companion object {
        private const val TAG = "Login"
    }
}