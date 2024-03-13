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
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ActivityRegister : ComponentActivity() {
    private var mAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var voornaam: MutableState<String> = mutableStateOf("")
    private var werkfunctie: MutableState<String> = mutableStateOf("")
    private var email: MutableState<String> = mutableStateOf("")
    private var password: MutableState<String> = mutableStateOf("")
    private var checked: MutableState<Boolean> = mutableStateOf(false)
    private var checkedFinish: MutableState<Boolean> = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        setContent {
            DefaultPreview()
        }
    }

    private fun checkAllFields(): Boolean {
        var check = true
        if (voornaam.value.isEmpty()) {
            val error = "Voornaam is verplich"
            toast(error)
            //voornaam.value.error = error
            check = false
        }
        Log.d(TAG, "Voornaam is ingevuld")
        if (werkfunctie.value.isEmpty()) {
            val error = "Werkfunctie is verplicht"
            toast(error)
            //werkfunctie!!.error = "Werkfunctie is verplicht"
            check = false
        }
        Log.d(TAG, "werkfunctie is ingevuld")
        if (email.value.isEmpty()) {
            val error = "Email is verplicht"
            toast(error)
            //email!!.error = "Email is verplicht"
            check = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            val error = "email format niet juist"
            toast(error)
            //email!!.error =
            //    "Vul uw email adres volgens het volgende format in:\n uwnaam@voorbeeld.nl"
            check = false
        }
        Log.d(TAG, "email is ingevuld")
        if (password.value.isEmpty()) {
            val error = "Wachtwoord is verplicht"
            toast(error)
            //password!!.error = "Wachtwoord is verplicht"
            check = false
        } else if (password.value.length < 6) {
            val error = "Wachtwoord moet minimaal 8 karakters bevatten"
            toast(error)
            //password!!.error = "Wachtwoord moet minimaal 8 karakters bevatten"
            check = false
        }
        Log.d(TAG, "Wachtwoord is ingevuld")
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
        // [START create_user_with_email]
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth!!.currentUser
                    saveUserData(user)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@ActivityRegister, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun saveUserData(firebaseUser: FirebaseUser?) {
        val db = FirebaseFirestore.getInstance()
        Log.d(TAG, "IF")





        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        user["Voornaam"] = voornaam.value
        user["Werk functie"] = werkfunctie.value
        user["User UID"] = firebaseUser!!.uid
        db.collection("User Data")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
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
    fun DefaultPreview() {
        val focusManager = LocalFocusManager.current
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
                    Text(text = "Registeren")
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
                            label = { Text("email") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                        )
                        TextField(
                            value = voornaam.value,
                            onValueChange = { voornaam.value = it },
                            label = { Text("voornaam") },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                        )
                        TextField(
                            value = werkfunctie.value,
                            onValueChange = { werkfunctie.value = it },
                            label = { Text("werkfunctie") },
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
                            label = { Text("wachtwoord") },
                            singleLine = true,
                            placeholder = { Text("Wachtwoord") },
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

                                // Localized description for accessibility services
                                val description = if (passwordVisible) "Hide password" else "Show password"

                                // Toggle button to hide or display password
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
                            text = "Ik verleen toestemming voor het anoniem bewaren van mijn stemmen op de stellingen ten behoeve" +
                                    "van onderzoeksdoeleinden van het lectoraat Legal Management"
                        )
                    }
                    Button(onClick = { startRegister() }) {
                        Text(text = "Aan de slag")
                    }

                }
            }
        }
    }


    companion object {
        private const val TAG = "EmailPassword"
//        private const val EMAIL_PATTERN =
//            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}