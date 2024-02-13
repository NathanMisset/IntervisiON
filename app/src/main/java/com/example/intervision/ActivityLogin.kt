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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ActivityLogin : ComponentActivity() {
    private var mAuth: FirebaseAuth? = null
    private var username: MutableState<String> = mutableStateOf("")
    private var password: MutableState<String> = mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        setContent {
            DefaultPreview()
        }
    }

    private fun toast(message: String) {
        Toast.makeText(
            this@ActivityLogin, message,
            Toast.LENGTH_SHORT
        ).show()
    }

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

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("dasda", "signInWithEmail:success")
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("tads", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@ActivityLogin, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun toRestPassword() {
        val i = Intent(this, ActivityResetPassword::class.java)
        startActivity(i)
    }

    private fun toRegister() {
        val i = Intent(this, ActivityRegister::class.java)
        startActivity(i)
    }

    private fun reload() {
        val i = Intent(this, ActivityNavigation::class.java)
        startActivity(i)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) return
        reload()
    }

    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true)
    //@Preview(device = "spec:width=1080px,height=540px,dpi=400")
    @Composable
    fun DefaultPreview() {
        MyApplicationTheme {
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
                        painter = painterResource(id = R.drawable.image_main_icon),
                        contentDescription = stringResource(id = R.string.content_1),
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
                    TextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text("Email") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 50.dp)
                            .fillMaxWidth()
                    )
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Wachtwoord") },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 50.dp)
                            .fillMaxWidth()
                    )
                }

                Button(
                    onClick = {
                        Log.d("BUTTONS", "User tapped the LoginButton")
                        if (checkAllFields()) {
                            signIn(username.value, password.value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .defaultMinSize(minHeight = 50.dp)

                )
                {
                    Text(
                        text = "Login",
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.35f),
                ) {
                    OutlinedButton(
                        onClick = {
                            toRegister()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 50.dp)
                    )
                    {
                        Text(text = "Registeren")
                    }
                    OutlinedButton(
                        onClick = {
                            toRestPassword()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 50.dp)
                    )
                    {
                        Text(text = "Wachtwoord vergeten?")
                    }
                }
            }
        }
    }
}



