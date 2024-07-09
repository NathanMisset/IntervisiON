/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.spacing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 *
 * This activity is used to change user settings
 *
 */

class ActivitySettings : ComponentActivity() {
    /** Firebase */
    private var firestore : FirebaseFirestore? = null
    private var auth : FirebaseAuth? = null
    private var user : FirebaseUser? = null

    /** Class Variables */
    private lateinit var userDataId : String
    private var userData : Map<String?, Any>? = null

    /** Mutables */
    private lateinit var name : MutableState<String>
    private lateinit var email : MutableState<String>
    private lateinit var job : MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepVariables()
    }

    private fun prepVariables(){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser
        userDataId = ""
        getUserData()
    }

    private fun getUserData(){
        firestore!!.collection("User Data")
            .limit(1)
            .whereEqualTo("User UID", auth!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        userDataId = document!!.id
                        userData = document.data
                        setOnscreen()
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun setOnscreen(){
        name = mutableStateOf(userData!!["Voornaam"].toString())
        email = mutableStateOf(user!!.email.toString())
        job = mutableStateOf(userData!!["Werk functie"].toString())
        setContent {
            Screen()
        }
    }
    private fun updateUserSettings(){
        val data = hashMapOf(
            "User UID" to user!!.uid,
            "Voornaam" to name.value,
            "Werk functie" to job.value
        )
        firestore!!.collection("User Data")
            .document(userDataId)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                backToProfile()
            }
    }

    private fun backToProfile(){
        finish()
    }

    @Composable
    fun Screen(){
        IntervisionBaseTheme {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,

                ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                        .weight(0.1f)
                        .background(color = MaterialTheme.colorScheme.background),

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        backToProfile()
                    }) {
                        Icon(Icons.Filled.Close, stringResource(R.string.quitIconDescriptonSettings))
                    }
                    Text(text = stringResource(R.string.accountLabelSettings))
                    TextButton(onClick = {
                        updateUserSettings()
                    }) {
                        Text(text = stringResource(R.string.saveLabelSettings))
                    }
                }
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .weight(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    TextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(stringResource(R.string.emailLabelSettings)) },
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(20.dp)
                            .fillMaxWidth()

                    )
                    TextField(
                        value = name.value,
                        onValueChange = {name.value = it},
                        label = { Text(stringResource(R.string.firstnameLabelSettings)) },
                        maxLines = 2,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    )

                    TextField(
                        value = job.value,
                        onValueChange = { job.value = it },
                        label = { Text(stringResource(R.string.workfunctionLabelSettings)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(20.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "SettingsActivity"
    }
}