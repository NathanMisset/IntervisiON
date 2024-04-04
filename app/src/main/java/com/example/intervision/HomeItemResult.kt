package com.example.intervision

import android.util.Log

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.customColor1ContainerLightMediumContrast
import com.example.intervision.ui.primaryLightMediumContrast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class HomeItemResult() {

    lateinit var user: FirebaseUser
    lateinit var fireStoreDatabase: FirebaseFirestore
    lateinit var entries: MutableList<PieChartEntry>
    lateinit var statement: MutableState<String>
    lateinit var question: MutableState<String>

    var nFor: Float? = null
    var nAgaint: Float? = null

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

    fun getData() {
        val ForArray: ArrayList<ArrayList<String?>> = ArrayList()
        val AgaintArray: ArrayList<ArrayList<String?>> = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "document.data =" + document.data.get("In Favour"))
                    Log.d(TAG, "document.data =" + document.data.get("Against"))
                    ForArray.add(document.data.get("In Favour") as java.util.ArrayList<String?>)
                    AgaintArray.add(document.data.get("Against") as java.util.ArrayList<String?>)
                }
                nFor = ForArray[0].size.toFloat()
                nAgaint = AgaintArray[0].size.toFloat()
                setData()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    companion object {
        private const val TAG = "Item Result"
    }

    @Composable
    fun Component() {
        var cardSize = 400.dp
        when (LocalDensity.current.fontScale) {
            0.85F -> cardSize = 350.dp
            1.00F  -> cardSize = 400.dp
            1.15F  -> cardSize = 400.dp
            1.30F  -> cardSize = 400.dp
            1.50F  -> cardSize = 400.dp
            1.80F  -> cardSize = 450.dp
            2.00F ->  cardSize = 480.dp
            else -> cardSize = 400.dp
        }
        MyApplicationTheme {
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
                    Row(modifier = Modifier.fillMaxWidth()
                        .defaultMinSize(50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text(text = "Tegen:",
                            fontSize = 13.sp)
                        Icon(imageVector = Icons.Default.Circle,
                            contentDescription ="Circle",
                            tint = MaterialTheme.colorScheme.secondary)
                        Text(text = "Voor:",
                            fontSize = 13.sp)
                        Icon(imageVector = Icons.Default.Circle,
                            contentDescription ="Circle",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

fun calculateStartAngles(entries: MutableList<PieChartEntry>): List<Float> {
    var totalPercentage = 0f
    var startAngleList = ArrayList<Float>()
    for (item in entries) {
        val startAngle = totalPercentage * 360
        totalPercentage += item.percentage
        startAngleList.add(startAngle)
    }
    return startAngleList
}

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

data class PieChartEntry(val color: Color, val percentage: Float)

@PreviewFontScale
@Composable
fun Component() {
    var cardSize = 400.dp
    when (LocalDensity.current.fontScale) {
        0.85F -> cardSize = 350.dp
        1.00F  -> cardSize = 400.dp
        1.15F  -> cardSize = 400.dp
        1.30F  -> cardSize = 400.dp
        1.50F  -> cardSize = 400.dp
        1.80F  -> cardSize = 450.dp
        2.00F ->  cardSize = 480.dp
        else -> cardSize = 400.dp
    }
    var entries: MutableList<PieChartEntry>
    entries = mutableListOf(
        PieChartEntry(Color.Yellow, (1f / 2f)),
        PieChartEntry(Color.Cyan, (1f / 2f))
    )
    MyApplicationTheme {
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
                    Text(text = "Ga ik de bezwaarmaker bellen?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 30.sp)

                    Text(text = "Ik bel NIET als de bzwaarmaker een advocaat heeft ingeschakkelt",
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
                    Text(text = "Tegen:",
                        fontSize = 13.sp)
                    Icon(imageVector = Icons.Default.Circle,
                        contentDescription ="Circle",
                        tint = MaterialTheme.colorScheme.secondary)
                    Text(text = "Voor:",
                        fontSize = 13.sp)
                    Icon(imageVector = Icons.Default.Circle,
                        contentDescription ="Circle",
                        tint = MaterialTheme.colorScheme.primary)
                }
            }

        }

    }
}

