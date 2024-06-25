/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
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
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.UiString
import com.google.firebase.auth.FirebaseAuth

/**
 *
 * This activity controls the reset password proces
 *
 */

open class ActivityResetPassword : ComponentActivity() {

    /** Class Variables */
    private var email: MutableState<String> = mutableStateOf("")
    private var sendButtonState: MutableState<Boolean> = mutableStateOf(true)

    /** Firebase */
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            Screen()
        }
    }

    private fun toLogin() {
        val i = Intent(this, ActivityLogin::class.java)
        startActivity(i)
    }

    private fun resetPassword() {
        auth!!.setLanguageCode(UiString.taalCodeApp)
        auth!!.sendPasswordResetEmail(email.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mailSend()
                }
            }
    }

    private fun mailSend() {
        sendButtonState.value = false
    }

    /** Composables */
    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true)
    @Composable
    fun Screen() {
        IntervisionBaseTheme {
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
                    Text(text = ComposableUiString.forgotPasswordButtonLogin,
                        fontSize = 20.sp)
                    Text(
                        text = ComposableUiString.contentResetPassword,
                        softWrap = true
                    )
                    TextField(value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(ComposableUiString.emailTextFieldLogin) }
                    )
                    if (sendButtonState.value) {
                        Button(onClick = { resetPassword() }) {
                            Text(text = ComposableUiString.verzendenLabelResetPassword)
                        }
                    } else {
                        Button(onClick = { toLogin() }) {
                            Text(text = ComposableUiString.backButtonApp)
                        }
                    }
                }
            }
        }
    }
    companion object {
        private const val TAG = "Reset Password"
    }
}