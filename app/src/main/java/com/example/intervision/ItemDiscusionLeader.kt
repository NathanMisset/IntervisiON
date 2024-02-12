package com.example.intervision

import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.spacing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ItemDiscusionLeader(
    var parent: ActivityIntervisionLeader,
    val user: FirebaseAuth,
    private val db: FirebaseFirestore,
    private var uIdList: ArrayList<String>?
) {

    //Lists
    private lateinit var userNames: MutableList<List<String>>

    //View
    private val chars = charArrayOf('A', 'B', 'C', 'D', 'E', 'F')

    fun init() {
        Log.d(TAG, "uIdList$uIdList")
        userNames = mutableStateListOf(
            listOf(
                "Gebruiker 1",
                "Gebruiker 2",
                "Gebruiker 3",
                "Gebruiker 4",
                "Gebruiker 5",
                "Gebruiker 6"
            )
        )
        Log.d(TAG, "itemElborateChose")
        getNames()
    }


    private fun getNames() {

        Log.d(TAG, "Start Names")
        db.collection("User Data")
            .whereIn("User UID", uIdList!!)
            .get()
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
                    Log.d(TAG, "list $list")
                    Log.d(TAG, "userNames $userNames")
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }


    @Composable
    fun Component() {
        Column {
            Text(
                text = "Ronde 4 van 5",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp
            )
            Text(
                text = "Discussie",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Card {
                Text(
                    text = "Tik de persoon die aan het woord mag.",
                    modifier = Modifier
                        .padding(spacing.large)
                )
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
                        .clickable { parent.changeValue(chars[0]) }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                        contentDescription = "profile",
                        contentScale = ContentScale.Inside

                    )
                    Text(
                        text = userNames[0][0],
                        fontSize = 10.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { parent.changeValue(chars[1]) }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                        contentDescription = "profile",
                    )
                    Text(
                        text = userNames[0][1],
                        fontSize = 10.sp
                    )
                }




                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { parent.changeValue(chars[2]) }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                        contentDescription = "profile",
                    )
                    Text(
                        text = userNames[0][2],
                        fontSize = 10.sp
                    )
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
                        .clickable { parent.changeValue(chars[3]) }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                        contentDescription = "profile",
                        contentScale = ContentScale.Inside

                    )
                    Text(
                        text = userNames[0][3],
                        fontSize = 10.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { parent.changeValue(chars[4]) }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                        contentDescription = "profile",
                    )
                    Text(
                        text = userNames[0][4],
                        fontSize = 10.sp
                    )
                }




                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { parent.changeValue(chars[5]) }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
                        contentDescription = "profile",
                    )
                    Text(
                        text = userNames[0][5],
                        fontSize = 10.sp
                    )
                }
            }
        }
    }


    companion object {
        private const val TAG = "Eleboratechoice"
    }

}


//@Composable
//fun A() {
//    Column(
//        modifier = Modifier
//            .fillMaxHeight()
//            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.background),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceAround
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.8f)
//                .padding(horizontal = spacing.medium),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Column {
//                Text(
//                    text = "Ronde 4 van 5",
//                    fontWeight = FontWeight.Bold,
//                    fontStyle = FontStyle.Italic,
//                    fontSize = 20.sp
//                )
//                Text(
//                    text = "Discussie",
//                    fontWeight = FontWeight.Bold,
//                    fontStyle = FontStyle.Italic,
//                    fontSize = 30.sp
//                )
//            }
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Card() {
//                    Text(
//                        text = "Tik de persoon die aan het woord mag.",
//                        modifier = Modifier
//                            .padding(spacing.large)
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 10.dp),
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .clickable { }
//                            .background(
//                                MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(15.dp)
//                            )
//                            .padding(10.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
//                            contentDescription = "profile",
//                            contentScale = ContentScale.Inside
//
//                        )
//                        Text(
//                            text = "userNames[0][0]",
//                            fontSize = 10.sp
//                        )
//                    }
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .clickable { }
//                            .background(
//                                MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(15.dp)
//                            )
//                            .padding(10.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
//                            contentDescription = "profile",
//                        )
//                        Text(
//                            text = "userNames[0][1]",
//                            fontSize = 10.sp
//                        )
//                    }
//
//
//
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .clickable { }
//                            .background(
//                                MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(15.dp)
//                            )
//                            .padding(10.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
//                            contentDescription = "profile",
//                        )
//                        Text(
//                            text = "userNames[0][2]",
//                            fontSize = 10.sp
//                        )
//                    }
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 10.dp),
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .clickable { }
//                            .background(
//                                MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(15.dp)
//                            )
//                            .padding(10.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
//                            contentDescription = "profile",
//                            contentScale = ContentScale.Inside
//
//                        )
//                        Text(
//                            text = "userNames[0][0]",
//                            fontSize = 10.sp
//                        )
//                    }
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .clickable { }
//                            .background(
//                                MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(15.dp)
//                            )
//                            .padding(10.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
//                            contentDescription = "profile",
//                        )
//                        Text(
//                            text = "userNames[0][1]",
//                            fontSize = 10.sp
//                        )
//                    }
//
//
//
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .clickable { }
//                            .background(
//                                MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(15.dp)
//                            )
//                            .padding(10.dp),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture_blue_192x192),
//                            contentDescription = "profile",
//                        )
//                        Text(
//                            text = "userNames[0][2]",
//                            fontSize = 10.sp
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
