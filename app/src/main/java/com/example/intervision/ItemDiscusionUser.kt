/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.spacing
import com.google.firebase.firestore.FirebaseFirestore

/**
 *
 * This item can be initiated as an object in an activity
 * This item represents the 2de from the preceptive of the User.
 * When the leader pick a user every users see the choice picked
 *
 */

class ItemDiscusionUser(
        val firestore: FirebaseFirestore,
        private val partisipantsIdS: ArrayList<String>?
) {

    /** Class Variables */
    private var userNames: ArrayList<String>? = null
    private lateinit var userTurnText: MutableState<String>

    fun init() {
        userTurnText = mutableStateOf("Test")
        userNames = arrayListOf("Test", "Test", "Test", "Test", "Test", "Test")
        getText()
    }

    private fun getText() {
        firestore.collection("User Data").whereIn("User UID", partisipantsIdS!!).get()
                .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val tempList = ArrayList<String>()
                for (document in task.result) {
                    Log.d(TAG, document.id + " => " + document.data)
                    tempList.add(document.data["Voornaam"].toString())
                }
                for (i in partisipantsIdS.size..5) {
                    tempList.add("Test")
                }
                userNames = tempList
                defaultTurn()
                Log.d(TAG, "UserNames$userNames")
            } else {
                Log.w(TAG, "Error getting documents.", task.exception)
            }
        }
    }

    /**
     *
     * The letter is added to the number that repersents the round
     * The leter repersents the different users.
     * A is user number 1
     * F is user number 6
     *
     */
    fun giveTurn(i: Char) {
        when (i) {
            'A' -> switchTurn(0)
            'B' -> switchTurn(1)
            'C' -> switchTurn(2)
            'D' -> switchTurn(3)
            'E' -> switchTurn(4)
            'F' -> switchTurn(5)
            else -> defaultTurn()
        }
    }

    private fun switchTurn(i: Int) {
        userTurnText = mutableStateOf(userNames!![i] + "is aan de beurt")
    }

    private fun defaultTurn() {
        userTurnText = mutableStateOf("Username")
    }

    /** Composables */
    @Composable
    fun Screen() {
        Column (
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                    text = stringResource(R.string.roundTextItemDiscusionLeader),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
            )
            Text(
                    text = stringResource(R.string.text2ItemDiscusionUser),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
            )
        }
        Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(horizontal = spacing.large)) {
            Row(
                    modifier = Modifier
                            .padding(spacing.medium)
                            .background(
                                    MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp)
                            )
            ) {
                Image(
                        modifier = Modifier.padding(
                                start = spacing.medium,
                                end = spacing.default,
                    top = spacing.medium,
                            bottom = spacing.medium
                    ),
                    painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                            contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                            contentScale = ContentScale.Inside
                )
                    Text(
                            text = userTurnText.value,
                            modifier = Modifier.padding(spacing.medium)
                    )
            }
        }
    }

    companion object {
        private const val TAG = "DiscussionUserItem"
    }
}

    /**
     *
     * These methode are outside the class so the preview can easier reach them
     *
     */
    @Preview(device = "spec:width=1080px,height=2280px,dpi=400") @Composable
    fun ItemDiscusionUserPreview() {
        IntervisionBaseTheme {
            Column(
                    modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
            ) {
                Column (
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = spacing.large),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                            text = stringResource(R.string.roundTextItemDiscusionLeader),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 20.sp
                    )
                    Text(
                            text = stringResource(R.string.text2ItemDiscusionUser),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 30.sp
                    )
                }
                Column (modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(horizontal = spacing.large)){
                    Row(
                            modifier = Modifier
                                    .padding(spacing.medium)
                                    .background(
                                            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp)
                                    )
                    ) {
                        Image(
                                modifier = Modifier.padding(
                                        start = spacing.medium,
                                        end = spacing.default,
                            top = spacing.medium,
                                    bottom = spacing.medium
                        ),
                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                                    contentDescription = stringResource(R.string.imageDesciptionItemDiscusionLeader),
                                    contentScale = ContentScale.Inside
                    )
                            Text(
                                    text = stringResource(R.string.previewtextItemDiscusionUser),
                                    modifier = Modifier.padding(spacing.medium)
                            )
                    }
                }
            }
        }
    }

