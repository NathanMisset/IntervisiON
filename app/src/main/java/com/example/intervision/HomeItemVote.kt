/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 *
 * This item can be initiated as an object in an activity
 * This item show the thesis of the month and alow user to vote
 * After they voted they get the change to reload the homepage and look at homeItemResult
 * or continue and make a group imidiatly
 *
 */

class HomeItemVote {
    /** Firebase */
    lateinit var user: FirebaseUser
    private lateinit var fireStoreDatabase: FirebaseFirestore

    /** Class Variables */
    private lateinit var statement: MutableState<String>
    private lateinit var statementId: String
    private lateinit var question: MutableState<String>
    private var parent: ComponentActivity? = null
    private var openAlertDialog = mutableStateOf(false)

    fun init(
        statement: String,
        statementId: String,
        question: String,
        user: FirebaseUser,
        fireStoreDatabase: FirebaseFirestore,
        parent: ComponentActivity
    ) {
        this.statement = mutableStateOf(statement)
        this.statementId = statementId
        this.question = mutableStateOf(question)
        this.user = user
        this.fireStoreDatabase = fireStoreDatabase
        this.parent = parent
    }

    /** get list of in favour votes, puts it in a list and adds the id of the user */
    private fun saveAgreed() {
        val voteData: ArrayList<ArrayList<String?>> = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "document.data =" + document.data["In Favour"])
                    voteData.add(document.data["In Favour"] as java.util.ArrayList<String?>)
                    voteData[0].add(user.uid)
                    saveAgreed1(voteData[0])
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /** gets the id of the document that needs to be updated  */
    private fun saveAgreed1(data: java.util.ArrayList<String?>) {
        fireStoreDatabase.collection("Votes")
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, result.documents[0].id)
                saveAgreed2(data, result.documents[0].id)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /** updates the document inside the database */
    private fun saveAgreed2(data: java.util.ArrayList<String?>, id: String) {
        fireStoreDatabase.collection("Votes")
            .document(id)
            .update("In Favour", data)
            .addOnSuccessListener {
                openAlertDialog.value = true
            }
    }

    /** gets list of against votes, puts it in a list and adds the id of the user */
    private fun saveDisAgreed() {
        val voteData: ArrayList<ArrayList<String?>> = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "document.data =" + document.data["Against"])
                    voteData.add(document.data["Against"] as java.util.ArrayList<String?>)
                    voteData[0].add(user.uid)
                    saveDisAgreed1(voteData[0])

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /** gets the id of the document that needs to be updated  */
    private fun saveDisAgreed1(data: java.util.ArrayList<String?>) {
        fireStoreDatabase.collection("Votes")
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, result.documents[0].id)
                saveDisAgreed2(data, result.documents[0].id)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /** updates the document inside the database */
    private fun saveDisAgreed2(data: java.util.ArrayList<String?>, id: String) {
        fireStoreDatabase.collection("Votes")
            .document(id)
            .update("Against", data)
            .addOnSuccessListener {
                openAlertDialog.value = true
            }
    }

    private fun reload() {
        val i = Intent(parent!!, ActivityNavigation::class.java)
        parent!!.startActivity(i)
    }

    private fun toMakeGroup() {
        val i = Intent(parent!!, ActivityMakeGroup::class.java)
        parent!!.startActivity(i)
    }

    /** Composables */
    @Composable
    fun Screen() {
        IntervisionBaseTheme {
            when {
                openAlertDialog.value -> {
                    AlertDialog(
                        onDismissRequest = { reload() },
                        onConfirmation = {
                            toMakeGroup()
                            openAlertDialog.value = false
                        },
                        dialogTitle = ComposableUiString.votedTextHomeItemVote,
                        dialogText = ComposableUiString.votedTextQuestionHomeItemVote,
                        icon = Icons.Default.HowToVote
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = ComposableUiString.text1HomeItemVote)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = question.value,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = statement.value,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        Log.d(TAG, "User tapped the disAgree vote button")
                        saveDisAgreed()
                    }) {
                        Text(text = ComposableUiString.onEensHomeItemVote)
                    }
                    Button(onClick = {
                        saveAgreed()
                    }) {
                        Text(text = ComposableUiString.eensHomeItemVote)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "Item Vote"
    }
}

/**
 *
 * These methode are outside the class so the preview can easier reach them
 *
 */
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = ComposableUiString.exampleIconHomeItemVote)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(ComposableUiString.jaHomeItemVote)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(ComposableUiString.neeHomeItemVote)
            }
        }
    )
}

@PreviewFontScale @Composable
fun HomeItemVotePreview() {
    IntervisionBaseTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = ComposableUiString.text1HomeItemVote)

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "question.value",
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "statement.value",
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxHeight()
                        .defaultMinSize(minHeight = 50.dp)
                        .fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        Log.d("BUTTONS", "User tapped the disAgree vote button")

                    }) {
                        Text(text = ComposableUiString.onEensHomeItemVote)
                    }
                    Button(onClick = {

                    }) {
                        Text(text = ComposableUiString.eensHomeItemVote)
                    }
                }
            }
        }
    }
}



