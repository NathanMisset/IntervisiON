/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ActivityLogin.Companion.TAG
import com.example.intervision.ui.IntervisionBaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 *
 * This activity controls the login activity
 *
 */

class ActivityLogin : ComponentActivity() {

    /** Class variables */
    private var username: MutableState<String> = mutableStateOf("")
    private var password: MutableState<String> = mutableStateOf("")

    /** Firebase */
    private var mAuth: FirebaseAuth? = null

    /** Initialisation */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        setContent {
            Screen()
        }
    }

    /**
     *
     * toast is a small message at the bottom of the screen
     * This toast is mostly shown when a login error occurs
     *
     */
    private fun toast(message: String) {
        Toast.makeText(
            this@ActivityLogin, message,
            Toast.LENGTH_SHORT
        ).show()
    }


    /**
     *
     * Checks if username and password fields are filled
     *
     */
    private fun checkAllFields(): Boolean {
        var check = true
        if (username.value.isEmpty()) {
            val error = "Gebruikersnaam is verplich"
            toast(error)
            check = false
        }
        if (password.value.isEmpty()) {
            val error = "Wachtwoord is verplicht"
            toast(error)
            check = false
        }
        return check
    }

    /**
     *
     * Basic login for email and password signIn copied from android
     *
     */
    private fun signIn(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@ActivityLogin, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) return
        toNavigation()
    }
    private fun toRestPassword() {
        val i = Intent(this, ActivityResetPassword::class.java)
        startActivity(i)
    }
    private fun toCredits() {
        val i = Intent(this, ActivityCredits::class.java)
        startActivity(i)
    }
    private fun toRegister() {
        val i = Intent(this, ActivityRegister::class.java)
        startActivity(i)
    }
    private fun toNavigation() {
        val i = Intent(this, ActivityNavigation::class.java)
        startActivity(i)
    }


    /** Composables */
    @Composable
    fun Screen() {
        val focusManager = LocalFocusManager.current
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly

            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    Image(
                        painter = painterResource(id = R.drawable.main_icon),
                        contentDescription = getString(R.string.descriptionIconApp),
                        Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        getString(R.string.loginButtonLogin),
                        fontSize = 30.sp,
                        fontWeight = Bold,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.3f),
                )
                {
                    TextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text(getString(R.string.emailTextFieldLogin)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 50.dp)
                            .fillMaxWidth()
                    )

                    var rememberPasswordVisibility by remember { mutableStateOf(false) }
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text(getString(R.string.passwordTextFieldLogin)) },
                        singleLine = true,
                        visualTransformation = if (rememberPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { if (checkAllFields()) {
                                signIn(username.value, password.value)
                            } }),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 50.dp)
                            .fillMaxWidth(),
                        trailingIcon = {
                            val image = if (rememberPasswordVisibility)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description =
                                if (rememberPasswordVisibility) "Wachtwoord verbergen" else "Wachtwoord laten zien"

                            IconButton(onClick = { rememberPasswordVisibility = !rememberPasswordVisibility }) {
                                Icon(imageVector = image, description)
                            }
                        }
                    )
                }
                Button(
                    onClick = {
                        Log.d(TAG, "User tapped the LoginButton")
                        if (checkAllFields()) {
                            signIn(username.value, password.value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .defaultMinSize(minHeight = 50.dp)
                ) {
                    Text(
                        text = getString(R.string.loginButtonLogin),
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.50f),
                ) {
                    OutlinedButton(
                        onClick = {
                            toRegister()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 50.dp)
                    ) {
                        Text(text = getString(R.string.registerButtonLogin))
                    }
                    OutlinedButton(
                        onClick = {
                            toRestPassword()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 50.dp)
                    ) {
                        Text(text = getString(R.string.forgotPasswordButtonLogin))
                    }
                    TextButton(
                        onClick = {
                            toCredits()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 50.dp)
                    ) {
                        Text(text = getString(R.string.creditsButtonLogin))
                    }
                }
            }
        }
    }
    companion object {
        private const val TAG = "LoginActivity"
    }
}


@Preview
@Composable
fun Preview() {
    val focusManager = LocalFocusManager.current
    IntervisionBaseTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                Image(
                    painter = painterResource(id = R.drawable.main_icon),
                    contentDescription = "dsadasda",
                    Modifier
                        .fillMaxWidth()
                )
                Text(
                    "Login",
                    fontSize = 30.sp,
                    fontWeight = Bold,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.3f),
            )
            {
                var text by remember { mutableStateOf("Hello") }
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("dasda") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .defaultMinSize(minHeight = 50.dp)
                        .fillMaxWidth()
                )

                var rememberPasswordVisibility by remember { mutableStateOf(false) }
                var text1 by remember { mutableStateOf("Hello") }
                TextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    label = { Text("dasda") },
                    singleLine = true,
                    visualTransformation = if (rememberPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { }),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .defaultMinSize(minHeight = 50.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        val image = if (rememberPasswordVisibility)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description =
                            if (rememberPasswordVisibility) "Wachtwoord verbergen" else "Wachtwoord laten zien"

                        IconButton(onClick = { rememberPasswordVisibility = !rememberPasswordVisibility }) {
                            Icon(imageVector = image, description)
                        }
                    }
                )
            }
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .defaultMinSize(minHeight = 50.dp)
            ) {
                Text(
                    text = "dsada",
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.50f),
            ) {
                OutlinedButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp)
                ) {
                    Text(text = "sadad")
                }
                OutlinedButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp)
                ) {
                    Text(text = "asdada")
                }
                TextButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp)
                ) {
                    Text(text = "dsadad")
                }
            }
        }
    }
}

