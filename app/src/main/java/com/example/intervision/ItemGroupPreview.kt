/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.IntervisionBaseTheme
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *
 * This item can be initiated as an object in an activity
 * This item show all the users that are participating in a group
 * This item is called mutiple times in FragmentGroups for every joinable group
 *
 */

class ItemGroupPreview : ComponentActivity() {

    /** Class Variables */
    private var participantsTextView: ArrayList<TextView>? = null
    private var participantsSiD: ArrayList<String>? = null
    private var leaderId: String? = null
    private var sessionId: String? = null
    private var parent: ComponentActivity? = null

    /** Firebase */
    private var user: FirebaseUser? = null
    private var storage: FirebaseStorage? = null
    private var db: FirebaseFirestore? = null

    /** Mutables */
    private var groupTitle: MutableState<String> = mutableStateOf("Eigne Collega's")
    private lateinit var userNames: MutableList<List<String>>

    fun init(storage: FirebaseStorage, db: FirebaseFirestore, user: FirebaseUser,data: Map<String?, Any>, parent: ComponentActivity, docname: String) {
        this.storage = storage
        this.db = db
        this.user = user
        this.parent = parent

        sessionId = docname
        participantsTextView = ArrayList()

        leaderId = data["Leader Sid"] as String
        participantsSiD = data["Participant Sid"] as ArrayList<String>

        groupTitle = mutableStateOf(data["Group Name"]as String)
        userNames = mutableStateListOf(
            listOf(
                "Gebruiker  1",
                "Gebruiker 2",
                "Gebruiker 3",
                "Gebruiker 4",
                "Gebruiker 5",
                "Gebruiker 6"
            ))
        userData()
    }

    private fun userData() {
        db!!.collection("User Data").whereIn("User UID", participantsSiD!!).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = ArrayList<String>()
                    for (document in task.result) {
                        list.add(document.data["Voornaam"].toString())
                    }
                    for (i in list.size..5) {
                        list.add("Leeg")
                    }
                    userNames = mutableListOf(list)
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun checkIfLeader() {
        if (user!!.uid == leaderId) {
            toIntervisionLeader()
        } else {
            toIntervision()
        }
    }

    private fun toIntervision() {
        val i = Intent(parent, ActivityWaitingRoom::class.java)
        i.putExtra("SessionID", sessionId)
        i.putExtra("Leader", false)
        parent!!.startActivity(i)
    }

    private fun toIntervisionLeader() {
        val i = Intent(parent, ActivityWaitingRoom::class.java)
        i.putExtra("SessionID", sessionId)
        i.putExtra("Leader", true)
        parent!!.startActivity(i)
    }

    companion object {
        private const val TAG = "GroupPreviewItem"
    }

    /** Composables */
    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true) @Composable
    fun Screen() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = groupTitle.value,
                            textAlign = TextAlign.Justify)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                                contentScale = ContentScale.Inside
                            )
                            Text(text = userNames[0][0])
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                            )
                            Text(text = userNames[0][1])
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                            )
                            Text(text = userNames[0][2])
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                                contentScale = ContentScale.Inside
                            )
                            Text(text = userNames[0][3])
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                            )
                            Text(text = userNames[0][4])
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                            )
                            Text(text = userNames[0][5])
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = { checkIfLeader() }) {
                            Text(text = stringResource(R.string.joinGroupFragmentGroup))
                        }
                    }
                }
            }
        }
    }
}




