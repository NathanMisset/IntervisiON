package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth


open class ActivityResetPassword : ComponentActivity() {
    private var auth: FirebaseAuth? = null
    private var email: MutableState<String> = mutableStateOf("")
    private var sendButtonState: MutableState<Boolean> = mutableStateOf(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            DefaultPreview()
        }
    }

    private fun toLogin() {
        val i = Intent(this, ActivityLogin::class.java)
        startActivity(i)
    }

    private fun resetPassword() {
        auth!!.setLanguageCode("nl")
        auth!!.sendPasswordResetEmail(email.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent to: " + email.value)
                    mailSend()
                }
            }
    }

    private fun mailSend() {
        sendButtonState.value = false
    }

    companion object {
        private const val TAG = "Reset Password"
    }

    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = "Wachtwoord vergeten?",
                        fontSize = 20.sp)
                    Text(
                        text = "Vul je email in.\n\nAls de email gelinked is " +
                                "aan een account\nkrijgt u een mail met een link om \n" +
                                "wachtwoord te reseten.",
                        softWrap = true
                    )
                    TextField(value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("email") })
                    if (sendButtonState.value) {
                        Button(onClick = { resetPassword() }) {
                            Text(text = "Verzenden")
                        }
                    } else {
                        Button(onClick = { toLogin() }) {
                            Text(text = "Terug")
                        }
                    }
                }
            }
        }
    }
}