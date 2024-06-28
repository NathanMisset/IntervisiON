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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.spacing
import com.google.firebase.firestore.FirebaseFirestore

/**
 *
 * This item can be initiated as an object in an activity
 * This shows the these that is gonne be discused and the votes casted
 *
 */

class ItemVote(private var firestore: FirebaseFirestore, private var stellingID: String?) {

    /** Class Variables */
    private var against: ArrayList<String> = ArrayList()
    private var inFavour: ArrayList<String> = ArrayList()

    /** Mutables */
    private lateinit var againtVoteCount: MutableState<String>
    private lateinit var inFavourVoteCount: MutableState<String>
    private lateinit var thesisString: MutableState<String>

    fun init() {
        againtVoteCount = mutableStateOf("")
        inFavourVoteCount = mutableStateOf("")
        thesisString = mutableStateOf("[Stelling]")

        firestore.collection("Votes")
            .whereEqualTo("Id", stellingID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val listA = document.data["Against"].toString().substring( 1, document.data["Against"].toString().length - 1 ).split(',')
                        for (any in listA) {
                            against.add(document.data["Against"].toString())
                        }
                        val listIF = document.data["In Favour"].toString().substring( 1, document.data["In Favour"].toString().length - 1 ).split(',')
                        for (any in listIF) {
                            inFavour.add(document.data["In Favour"].toString())
                        }
                    }
                    againtVoteCount = mutableStateOf(against.size.toString())
                    inFavourVoteCount = mutableStateOf(inFavour.size.toString())
                    getThesis()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun getThesis() {
        firestore.collection("Theses").document(stellingID!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    thesisString = mutableStateOf(task.result.data!!["Question"].toString())
                    thesisString =
                        mutableStateOf(thesisString.value + "\n" + task.result.data!!["Statement"].toString())
                } else {
                    Log.d(TAG, "Cached get failed: ", task.exception)
                }
            }
    }

    /** Composables */
    @Composable
    fun Screen() {
        IntervisionBaseTheme {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = ComposableUiString.roundTextItemVote,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = ComposableUiString.titleTextItemVote,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
                Text(
                    text = ComposableUiString.asignmentItemVote,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    painter = painterResource(id = R.drawable.image_quotations_72x72),
                    contentDescription = ComposableUiString.buttonTextItemFinalRound
                )
                Text(text = thesisString.value)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = ComposableUiString.voorHomeItemResult + inFavourVoteCount.value)
                    Text(text = ComposableUiString.tegenHomeItemResult + againtVoteCount.value)
                }
            }
        }
    }

    companion object {
        private const val TAG = "ItemVote"
    }
}
/**
 *
 * This preview is used to look how the screen would look with some values
 * Preview does work if there is logic outside it like the component above
 *
 */
@PreviewFontScale @Composable
fun ItemVotePreview() {
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
            ){
                Text(
                    text = ComposableUiString.roundTextItemVote,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = ComposableUiString.titleTextItemVote,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
                Text(
                    text = ComposableUiString.asignmentItemVote,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    painter = painterResource(id = R.drawable.image_quotations_72x72),
                    contentDescription = ComposableUiString.buttonTextItemFinalRound
                )
                Text(text = ComposableUiString.exampleThesisItemVote)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = ComposableUiString.exampleVoteresultItemVote)
                    Text(text = ComposableUiString.exampleVoteresultItemVote)
                }
            }
        }
    }
}
