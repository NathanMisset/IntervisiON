package com.example.intervision

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.spacing
import com.google.firebase.firestore.FirebaseFirestore

class ItemVote(private var firestore: FirebaseFirestore, private var stellingID: String?) {
    // Firebase

    //Compose Var
    private lateinit var againtVoteCount: MutableState<String>
    private lateinit var inFavourVoteCount: MutableState<String>
    private lateinit var ThesisString: MutableState<String>

    //Var
    private var Against: ArrayList<String>? = null
    private var InFavour: ArrayList<String>? = null


    fun init() {
        Against = ArrayList()
        InFavour = ArrayList()

        againtVoteCount = mutableStateOf("")
        inFavourVoteCount = mutableStateOf("")
        ThesisString = mutableStateOf("[Stelling]")
        Log.d(TAG, "stellingID " + stellingID)

        firestore.collection("Votes")
            .whereEqualTo("Id", stellingID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                        Against!!.add(document.data["Against"].toString())
                        InFavour!!.add(document.data["In Favour"].toString())
                    }
                    Log.d(TAG, "Against $Against")
                    againtVoteCount = mutableStateOf(Against!!.size.toString())
                    Log.d(TAG, "InFavour $InFavour")
                    inFavourVoteCount = mutableStateOf(InFavour!!.size.toString())
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
                    ThesisString = mutableStateOf(task.result.data!!["Question"].toString())
                    ThesisString =
                        mutableStateOf(ThesisString.value + "\n" + task.result.data!!["Statement"].toString())
                } else {
                    Log.d(TAG, "Cached get failed: ", task.exception)
                }
            }
    }

    @Composable
    fun Component() {
        MyApplicationTheme {
            Column {
                Text(
                    text = "Ronde 2 van 5",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = "Stelling",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
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
                Text(text = ThesisString.value)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = againtVoteCount.value,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 30.sp
                    )
                    Text(
                        text = inFavourVoteCount.value,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "ItemVote"
    }
}