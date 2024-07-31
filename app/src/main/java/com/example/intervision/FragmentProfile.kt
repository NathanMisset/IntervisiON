/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *
 * This activity controlls the ProfileSection.
 * The users name display as in the group.
 * Futhermore it contains a button to settings.
 *
 */

class FragmentProfile : ComponentActivity() {

    /** Firebase */
    private var storage: FirebaseStorage? = null
    private var user: FirebaseUser? = null
    private var db : FirebaseFirestore? = null

    /** Class Variables */
    private var parent: ComponentActivity? = null
    private var username: MutableState<String> = mutableStateOf("")

    /**
     *
     * This is an fragment init. This is initiated by the navigationActivity and needs some input.
     *
     */
    fun init(user: FirebaseUser, storage: FirebaseStorage, db: FirebaseFirestore, parent: ComponentActivity) {
        this.user = user
        this.db = db
        this.storage = storage
        this.parent = parent
        getData()
    }

    private fun getData() {
        db!!.collection("User Data")
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

    /** Composables */
    @Preview(device = "spec:width=1080px,height=2280px,dpi=400", showBackground = true)
    @Composable
    fun Component() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "")
                Text(text = stringResource(R.string.profileLabelNavigation))
                FilledTonalIconButton(
                    onClick = { toSettings() }
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.settingsDescriptionProfileGroup))
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
        private const val TAG = "ProfileFragment"
    }
}