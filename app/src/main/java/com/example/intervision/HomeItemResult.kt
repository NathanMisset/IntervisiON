/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.util.Log

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.customColor1ContainerLightMediumContrast
import com.example.intervision.ui.primaryLightMediumContrast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 *
 * This item can be initiated as an object in an activity
 * This item show the result of the mothly votes as a piechart
 *
 */

class HomeItemResult {

    /** Firebase */
    lateinit var user: FirebaseUser
    private lateinit var fireStoreDatabase: FirebaseFirestore

    /** Class Variables */
    private lateinit var entries: MutableList<PieChartEntry>
    private lateinit var statement: MutableState<String>
    private lateinit var question: MutableState<String>

    private var nFor: Float? = null
    private var nAgaint: Float? = null

    /** After instanstasiating this needs to becaled to make the item ready */
    fun init(
        statement: String,
        question: String,
        user: FirebaseUser,
        fireStoreDatabase: FirebaseFirestore
    ) {
        this.question = mutableStateOf(question)
        this.statement = mutableStateOf(statement)
        this.user = user
        this.fireStoreDatabase = fireStoreDatabase
        getData()
    }

    private fun setData() {
        val tot = nFor!! + nAgaint!!
        entries = mutableListOf(
            PieChartEntry(primaryLightMediumContrast, (nFor!! / tot)),
            PieChartEntry(customColor1ContainerLightMediumContrast, (nAgaint!! / tot))
        )
    }

    private fun getData() {
        val forArray: ArrayList<ArrayList<String?>> = ArrayList()
        val againtArray: ArrayList<ArrayList<String?>> = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    forArray.add(document.data["In Favour"] as java.util.ArrayList<String?>)
                    againtArray.add(document.data["Against"] as java.util.ArrayList<String?>)
                }
                nFor = forArray[0].size.toFloat()
                nAgaint = againtArray[0].size.toFloat()
                setData()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /** Composables */
    @Composable
    fun Component() {
        /** cardSize wil changed based on the fontscale of the users phone to keep the UI clear */
        val cardSize = when (LocalDensity.current.fontScale) {
            0.85F -> 350.dp
            1.00F  -> 400.dp
            1.15F  -> 400.dp
            1.30F  -> 400.dp
            1.50F  -> 400.dp
            1.80F  -> 450.dp
            2.00F -> 480.dp
            else -> 400.dp
        }
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .size(cardSize)
                        .padding(16.dp),
                    ) {
                    Column(
                        modifier = Modifier
                        .padding(16.dp)) {
                        Text(text = question.value,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 30.sp)
                        Text(text = statement.value,
                                fontSize = 13.sp ,
                                lineHeight = 15.sp,
                            minLines = 1,
                            maxLines = 2)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PieChart(entries)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                        .defaultMinSize(50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text(text = stringResource(R.string.againstHomeItemResult),
                            fontSize = 13.sp)
                        Icon(imageVector = Icons.Default.Circle,
                            contentDescription = stringResource(R.string.againstIconItemVote),
                            tint = MaterialTheme.colorScheme.secondary)
                        Text(text = stringResource(R.string.infavourHomeItemResult),
                            fontSize = 13.sp)
                        Icon(imageVector = Icons.Default.Circle,
                            contentDescription = stringResource(R.string.InfavourIconItemVote),
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
    companion object {
        private const val TAG = "Item Result"
    }
}

/**
 *
 * These methode are outside the class so the preview can easier reach them
 *
 */
fun calculateStartAngles(entries: MutableList<PieChartEntry>): List<Float> {
    var totalPercentage = 0f
    val startAngleList = ArrayList<Float>()
    for (item in entries) {
        val startAngle = totalPercentage * 360
        totalPercentage += item.percentage
        startAngleList.add(startAngle)
    }
    return startAngleList
}

data class PieChartEntry(val color: Color, val percentage: Float)

@Composable
fun PieChart(entries: MutableList<PieChartEntry>) {
    Canvas(modifier = Modifier.size(150.dp)) {
        val startAngles = calculateStartAngles(entries)
        entries.forEachIndexed { index, entry ->
            drawArc(
                color = entry.color,
                startAngle = startAngles[index],
                sweepAngle = entry.percentage * 360f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size
            )
        }
    }
}

/** Seperate Composable to view how the design looks with standard variables */
@PreviewFontScale @Composable
fun Component() {
    val cardSize = when (LocalDensity.current.fontScale) {
        0.85F -> 350.dp
        1.00F  -> 400.dp
        1.15F  -> 400.dp
        1.30F  -> 400.dp
        1.50F  -> 400.dp
        1.80F  -> 450.dp
        2.00F -> 480.dp
        else -> 400.dp
    }

    IntervisionBaseTheme {
        val entries: MutableList<PieChartEntry> = mutableListOf(
            PieChartEntry(MaterialTheme.colorScheme.secondary, (1f / 2f)),
            PieChartEntry(MaterialTheme.colorScheme.primary, (1f / 2f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(cardSize)
                    .padding(16.dp),
                ) {
                Column(Modifier
                    .padding(16.dp)) {
                    Text(text = stringResource(R.string.thesis1HomeItemResult),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 30.sp)
                    Text(text = stringResource(R.string.thesis2HomeItemResult),
                        fontSize = 13.sp ,
                        lineHeight = 15.sp,
                        minLines = 1,
                        maxLines = 2)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PieChart(entries)
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = stringResource(R.string.againstHomeItemResult),
                        fontSize = 13.sp)
                    Icon(imageVector = Icons.Default.Circle,
                        contentDescription = stringResource(R.string.circelAgainstHomeItemResult),
                        tint = MaterialTheme.colorScheme.secondary)
                    Text(text = stringResource(R.string.infavourHomeItemResult),
                        fontSize = 13.sp)
                    Icon(imageVector = Icons.Default.Circle,
                        contentDescription = stringResource(R.string.circelInfavourHomeItemResult),
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

