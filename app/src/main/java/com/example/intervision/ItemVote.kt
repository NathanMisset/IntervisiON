package com.example.intervision

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.spacing
import com.google.firebase.firestore.FirebaseFirestore

class ItemVote(private var firestore: FirebaseFirestore, private var stellingID: String?) {
    // Firebase

    //Compose Var
    private lateinit var againtVoteCount: MutableState<String>
    private lateinit var inFavourVoteCount: MutableState<String>
    private lateinit var thesisString: MutableState<String>

    //Var
    private var against: ArrayList<String> = ArrayList()
    private var inFavour: ArrayList<String> = ArrayList()



    fun init() {
        againtVoteCount = mutableStateOf("")
        inFavourVoteCount = mutableStateOf("")
        thesisString = mutableStateOf("[Stelling]")
        Log.d(TAG, "stellingID $stellingID")

        firestore.collection("Votes")
            .whereEqualTo("Id", stellingID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)

                        val listA = document.data["Against"].toString().substring( 1, document.data["Against"].toString().length - 1 ).split(',')
                        Log.d(TAG, "listA $listA")
                        for (any in listA) {
                            against.add(document.data["Against"].toString())
                        }
                        val listIF = document.data["In Favour"].toString().substring( 1, document.data["In Favour"].toString().length - 1 ).split(',')
                        Log.d(TAG, "listIF $listIF")
                        for (any in listIF) {
                            inFavour.add(document.data["In Favour"].toString())
                        }

                    }
                    Log.d(TAG, "Against $against")
                    againtVoteCount = mutableStateOf(against.size.toString())
                    Log.d(TAG, "InFavour $inFavour")
                    inFavourVoteCount = mutableStateOf(inFavour.size.toString())
                    getThesis()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun getThesis() {
        Log.d(TAG, "stellingID: $stellingID")
        firestore.collection("Theses").document(stellingID!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    Log.d(TAG, "Cached document data: ${document?.data}")
                    thesisString = mutableStateOf(task.result.data!!["Question"].toString())
                    thesisString =
                        mutableStateOf(thesisString.value + "\n" + task.result.data!!["Statement"].toString())
                } else {
                    Log.d(TAG, "Cached get failed: ", task.exception)
                }
            }
    }

    @Composable
    fun Component() {
        MyApplicationTheme {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Ronde 1 van 4",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = "Stelling en resultaat",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
                Text(
                    text = "Voer een gesprek over de stelling",
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
                    contentDescription = stringResource(id = R.string.content_1)
                )
                Text(text = thesisString.value)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Voor: " + inFavourVoteCount.value)
                    Text(text = "Tegen: " + againtVoteCount.value)
                }
            }
        }
    }

    companion object {
        private const val TAG = "ItemVote"
    }
}


@PreviewFontScale
@Composable
fun VoteTestComponent() {
    MyApplicationTheme {
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
                    text = "Ronde 1 van 4",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = "Stelling en resultaat",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
                Text(
                    text = "Voer een gesprek over de stelling",
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
                    contentDescription = stringResource(id = R.string.content_1)
                )
                Text(text = "Ga ik de bezwaarmaker bellen?\nIk bel NIET als de bezwaarmaker een advocaat heeft ingeschakeld")
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "1")
                    Text(text = "1")
                }
            }
        }
    }
}