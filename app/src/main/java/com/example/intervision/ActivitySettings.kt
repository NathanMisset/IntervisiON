package com.example.intervision

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
//import androidx.compose.ui.tooling.preview.Preview
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
//import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.spacing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class ActivitySettings : ComponentActivity() {
    //Firebase
    private var firestore : FirebaseFirestore? = null
    private var auth : FirebaseAuth? = null
    private var user : FirebaseUser? = null

    //Variables
    private lateinit var userDataId : String
    private var userData : Map<String?, Any>? = null

    // Onscreen values


    //
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
    private fun setOnscreen(){
        name = mutableStateOf(userData!!["Voornaam"].toString())
        email = mutableStateOf(user!!.email.toString())
        job = mutableStateOf(userData!!["Werk functie"].toString())
        setContent {
            DefaultPreview()
        }
    }
    private fun updateUserData(){
        val data = hashMapOf(
            "User UID" to user!!.uid,
            "Voornaam" to name.value,
            "Werk functie" to job.value
        )
        firestore!!.collection("User Data")
            .document(userDataId)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
            }
    }
//    fun updataEmail(){
//        auth!!.currentUser!!.updateEmail(email.value)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "User email address updated.")
//                    finish()
//                }else{
//                    Log.w(TAG, "Error getting documents.", task.exception)
//                }
//            }
//    }

    private fun getUserData(){
        firestore!!.collection("User Data")
            .limit(1)
            .whereEqualTo("User UID", auth!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, " => document id: " + document.id)
                        userDataId = document!!.id
                        userData = document.data
                        Log.d(TAG, "Data $userData")
                        setOnscreen()
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }




    private fun backButton(){
        finish()
    }

    //@OptIn(ExperimentalMaterial3Api::class)
    //@Preview(device = "spec:width=1080px,height=2280px,dpi=400")
    @Composable
    fun DefaultPreview(){
        MyApplicationTheme{
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
                        backButton()
                    }) {
                        Icon(Icons.Filled.Close, "Close Cross")
                    }
                    Text(text = "Account")
                    TextButton(onClick = {
                        updateUserData()
                    }) {
                        Text(text = "Opslaan")
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
                        label = { Text("Email") },
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
                        label = { Text("Naam") },
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
                        label = { Text("Werk functie") },
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
        private const val TAG = "Settings"
    }

}