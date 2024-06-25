/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.UiString
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *
 * This activity controls the registration proces
 *
 */

class ActivityRegister : ComponentActivity() {

    /** Class variable */
    private var voornaam: MutableState<String> = mutableStateOf("")
    private var werkfunctie: MutableState<String> = mutableStateOf("")
    private var email: MutableState<String> = mutableStateOf("")
    private var password: MutableState<String> = mutableStateOf("")
    private var checked: MutableState<Boolean> = mutableStateOf(false)
    private var checkedFinish: MutableState<Boolean> = mutableStateOf(false)

    /** Firebase */
    private var mAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        setContent {
            Screen()
        }
    }

    private fun checkAllFields(): Boolean {
        var check = true
        if (voornaam.value.isEmpty()) {
            val error = UiString.voornaamErrorRegister
            toast(error)
            check = false
        }
        if (werkfunctie.value.isEmpty()) {
            val error = UiString.werkfuntieErrorRegister
            toast(error)
            check = false
        }
        if (email.value.isEmpty()) {
            val error = UiString.emailErrorRegister
            toast(error)
            check = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            val error = UiString.emailFormatErrorRegister
            toast(error)
            check = false
        }
        if (password.value.isEmpty()) {
            val error = UiString.wachtwoordErrorRegister
            toast(error)
            check = false
        } else if (password.value.length < 6) {
            val error = UiString.wachtwoordLengtenErrorRegister
            toast(error)
            check = false
        }
        // after all validation return true.
        return check
    }

    private fun toast(message: String) {
        Toast.makeText(
            this@ActivityRegister, message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun startRegister() {
        createAccount(email.value, password.value)
    }

    private fun createAccount(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth!!.currentUser
                    saveUserData(user)
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@ActivityRegister, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun saveUserData(firebaseUser: FirebaseUser?) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()

        user["Voornaam"] = voornaam.value
        user["Werk functie"] = werkfunctie.value
        user["User UID"] = firebaseUser!!.uid

        db.collection("User Data")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun reload() {
        val i = Intent(this, ActivityTutorial::class.java)
        startActivity(i)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) return
        reload()
    }

    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true)
    @Composable
    fun Screen() {
        val focusManager = LocalFocusManager.current
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
                    Text(text = ComposableUiString.registerRegister)
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        TextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            label = { Text(ComposableUiString.emailLabelRegister) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                        )
                        TextField(
                            value = voornaam.value,
                            onValueChange = { voornaam.value = it },
                            label = { Text(ComposableUiString.voornaamLabelRegister) },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                        )
                        TextField(
                            value = werkfunctie.value,
                            onValueChange = { werkfunctie.value = it },
                            label = { Text(ComposableUiString.werkfuntieLabelRegister) },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                        )
                        var passwordVisible by remember { mutableStateOf(false) }
                        TextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = { Text(ComposableUiString.wachtwoordLabelRegister) },
                            singleLine = true,
                            placeholder = { Text(ComposableUiString.wachtwoordLabelRegister) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Password),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.clearFocus() }),

                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) ComposableUiString.wachtwoordOntzichbaarRegister else ComposableUiString.wachtwoordZichbaarLabelRegister

                                IconButton(onClick = {passwordVisible = !passwordVisible}){
                                    Icon(imageVector  = image, description)
                                }
                            }
                        )

                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = checked.value,
                            onCheckedChange = {
                                checked.value = it
                                if (checkAllFields()) {
                                    checkedFinish.value = true
                                } else {
                                    checked.value = false
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            )
                        )
                        Text(
                            text = ComposableUiString.toestemmingRegister
                        )
                    }
                    Button(onClick = { startRegister() }) {
                        Text(text = ComposableUiString.finishRegister)
                    }

                }
            }
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
//        private const val EMAIL_PATTERN =
//            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}