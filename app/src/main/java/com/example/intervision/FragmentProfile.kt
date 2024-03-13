package com.example.intervision

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FragmentProfile : ComponentActivity() {

    private var storage: FirebaseStorage? = null
    private var user: FirebaseUser? = null
    private var db : FirebaseFirestore? = null
    private var parent: ComponentActivity? = null
    private var username: MutableState<String> = mutableStateOf("")

    fun init(user: FirebaseUser, storage:FirebaseStorage, db: FirebaseFirestore, parent: ComponentActivity) {
        this.user = user
        this.db = db
        this.storage = storage
        this.parent = parent
        data()
    }

   

    fun data() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User Data")
            .whereEqualTo("User UID", user!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                        username.value = document.data["Voornaam"].toString()
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
    private fun toSettings(){
        val i = Intent(parent, ActivitySettings::class.java)
        parent!!.startActivity(i)
    }
    @Preview(device = "spec:width=1080px,height=2280px,dpi=400", showBackground = true)
    @Composable
    fun Component() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Profile")
                IconButton(
                    onClick = { toSettings() }
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = username.value)
            }
        }
    }
    
    
    
    companion object {
        private const val TAG = "Settings"
    }
}