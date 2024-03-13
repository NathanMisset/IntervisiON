package com.example.intervision


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class ActivityMakeGroup : ComponentActivity() {
    private lateinit var selectedDate: MutableState<Long>
    private lateinit var selectedDateText: MutableState<String>
    private var user: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var fireStoreDatabase: FirebaseFirestore? = null
    private var thesis = ArrayList<String>()
    private var thesisID = ArrayList<String>()
    private var leaderName: MutableState<String> = mutableStateOf("GroepLeider")
    private var users: ArrayList<String>? = null
    private var uIDs: ArrayList<String?>? = null
    private lateinit var selectedUser: MutableList<String>
    private lateinit var selectedUserID: MutableList<String>
    private lateinit var selectedThesis: MutableList<String>
    private lateinit var selectedThesisID: MutableList<String>

    private var groupName: MutableState<String> = mutableStateOf("")
    private var participants = ArrayList<String?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CenterAlignedTopAppBarExample()
        }


        user = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        fireStoreDatabase = FirebaseFirestore.getInstance()
        users = ArrayList()
        uIDs = ArrayList()
        thesis = ArrayList()
        participants = ArrayList()
        selectedUser = mutableStateListOf()
        selectedUserID = mutableStateListOf()
        selectedUserID.add(user!!.uid.toString())
        selectedThesis = mutableStateListOf()
        selectedThesisID = mutableStateListOf()
        selectedDate = mutableStateOf(0)
        selectedDateText = mutableStateOf("Datum")
        participants.add(user!!.uid)
        getData()
    }



    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ActivityNavigation::class.java))
        finish()
    }


    private fun getThesis() {
        val firestore = fireStoreDatabase

        firestore!!.collection("Theses")
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                        thesis.add(document.data["Statement"].toString())
                        thesisID.add(document.id)
                    }
                    Log.d(TAG, "thesis $thesis")
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }


    }

    private fun getData() {
        fireStoreDatabase!!.collection("User Data")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, " => UserID " + document.data["User UID"])
                        Log.d(TAG, user!!.uid!!)
                        val u = user!!.uid
                        val d = document.data["User UID"] as String?
                        if (document.data["Voornaam"].toString().isNotEmpty()) {
                            users!!.add(document.data["Voornaam"].toString())
                            uIDs!!.add(d)
                        }
                        if (u == d) {
                            Log.d(TAG, "Inside if")
                            leaderName.value = document.data["Voornaam"].toString()
                        }
                    }
                    getThesis()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun makeGroup() {
        val session: MutableMap<String, Any?> = HashMap()
        session["Leader Sid"] = user!!.uid
        session["Participant Sid"] = selectedUserID
        session["Group Name"] = groupName.value
        session["ThesisID"] = selectedThesisID
        session["DataInMili"] = selectedDate.value.toString()
        fireStoreDatabase!!.collection("Sessions")
            .add(session)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                // Write a message to the database
                val database =
                    FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
                val myRef = database.getReference(documentReference.id)
                myRef.setValue("W")
                toHome()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun addUserToList(id: String, name: String) {
        selectedUser.add(name)
        selectedUserID.add(id)
    }
    private fun removeUserFromList(id: Int) {
        selectedUser.removeAt(id)
        selectedUserID.removeAt(id)
    }
    private fun addThesis(id: String, name: String) {
        if(selectedThesis.size > 0){
            selectedThesis[0] = name
            selectedThesisID[0] = id
        } else{
            selectedThesis.add(name)
            selectedThesisID.add(id)
        }
    }
    private fun removeThesis(id: Int) {
        selectedThesis.removeAt(id)
        selectedThesisID.removeAt(id)
    }


    private fun toHome() {
        startActivity(Intent(this, ActivityNavigation::class.java))
        finish()
    }

    data class TabRowItem(
        val title: String,
        val icon: ImageVector,
        val screen: @Composable () -> Unit,

        )

    @Composable
    fun ContactScreen() {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            items(users!!.size) { i ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .defaultMinSize(minHeight = 80.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupervisedUserCircle,
                            contentDescription = "User",
                            Modifier.padding(10.dp)
                        )
                        Text(
                            text = users!![i],
                            Modifier.padding(10.dp)
                        )
                        IconButton(onClick = { addUserToList(uIDs!![i]!!, users!![i]) })
                        {

                            Icon(
                                imageVector = Icons.Default.Add,
                                tint = MaterialTheme.colorScheme.tertiaryContainer,
                                contentDescription = "User",
                            )
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun ThesesScreen() {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            items(thesis.size) { i ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .defaultMinSize(minHeight = 140.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(
                            imageVector = Icons.Default.Topic,
                            contentDescription = "User",
                            Modifier.padding(10.dp)
                        )
                        Text(
                            text = thesis[i],
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(0.6f),
                            textAlign = TextAlign.Justify
                        )
                        IconButton(onClick = { addThesis(thesisID[i],thesis[i]) })
                        {
                            Icon(
                                imageVector = Icons.Default.Add,
                                tint = MaterialTheme.colorScheme.tertiaryContainer,
                                contentDescription = "User",
                            )
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun GroupScreen() {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Leider: " + leaderName.value,
                modifier = Modifier.padding(top = 20.dp),
            )
            TextField(
                value = groupName.value,
                onValueChange = { groupName.value = it },
                label = { Text("Groeps Naam") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .defaultMinSize(minHeight = 50.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            )
//            Text(
//                modifier = Modifier.padding(top = 20.dp),
//                text = "Datum"
//            )
//            Divider()
//            val selectedDateText = selectedDate.value?.let{
//                convertMillisToDate(it)
//            }
//            Text(
//                text = selectedDateText.toString(),
//                color = Color.Red
//            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "Gekozen stelling"
            )
            Divider()
            LazyColumn(modifier = Modifier
                .fillMaxHeight(0.20f)
                .fillMaxWidth()
                .padding(10.dp),
                verticalArrangement = Arrangement.Top){
                items(selectedThesis.size){ i ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .defaultMinSize(minHeight = 50.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(
                                imageVector = Icons.Default.Contacts,
                                contentDescription = "User",
                                Modifier.padding(10.dp)
                            )
                            Text(
                                fontSize = 10.sp,
                                text = selectedThesis[i],
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(0.6f),
                                textAlign = TextAlign.Justify
                            )
                            IconButton(onClick = { removeThesis(i) })
                            {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    tint = MaterialTheme.colorScheme.errorContainer,
                                    contentDescription = "User",
                                )
                            }
                        }

                    }
                }
            }
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "Participanten"
            )
            Divider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.60f)
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,

                ) {
                items(selectedUser.size) { i ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .defaultMinSize(minHeight = 60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(
                                imageVector = Icons.Default.Contacts,
                                contentDescription = "User",
                                Modifier.padding(10.dp)
                            )
                            Text(
                                text = selectedUser[i],
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(0.6f),
                                textAlign = TextAlign.Justify
                            )
                            IconButton(onClick = { removeUserFromList(i) })
                            {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    tint = MaterialTheme.colorScheme.errorContainer,
                                    contentDescription = "User",
                                )
                            }
                        }

                    }
                }
            }
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Button(onClick = {
                    makeGroup()
                },
                    modifier = Modifier.defaultMinSize(minHeight = 50.dp)) {
                    Text(text = "Maak groep")
                }
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DataPickerScreen() {
        val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis()
            }
        })


        selectedDateText.value = datePickerState.selectedDateMillis?.let {
            convertMillisToDate(it)
            selectedDate.value = it
        }.toString()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DatePicker(
                state = datePickerState
            )
            Spacer(
                modifier = Modifier.height(
                    32.dp
                )
            )
            Text(
                text = selectedDateText.value,
                color = Color.Red
            )
        }
    }

    private val tabRowItems = listOf(
        TabRowItem(
            title = "Groep",
            screen = { GroupScreen() },
            icon = Icons.Default.Group,
        ),
        TabRowItem(
            title = "Contacten",
            screen = { ContactScreen() },
            icon = Icons.Default.Contacts,
        ),
        TabRowItem(
            title = "Stellingen",
            screen = { ThesesScreen() },
            icon = Icons.Default.Topic,
        ),
//        TabRowItem(
//            title = "Datum prikken",
//            screen = { DataPickerScreen() },
//            icon = Icons.Default.CalendarMonth,
//        ),
    )

    @OptIn(ExperimentalPagerApi::class)
    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true)
    @Composable
    fun CenterAlignedTopAppBarExample() {
        val pagerState = rememberPagerState(3)
        val coroutineScope = rememberCoroutineScope()

        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                TabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                ) {
                    tabRowItems.forEachIndexed { index, item ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = "")
                            },
                            text = {
                                Text(
                                    text = item.title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),

                    state = pagerState,
                ) {
                    tabRowItems[pagerState.currentPage].screen()
                }
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(Date(millis))
    }

    companion object {
        private const val TAG = "MakeGroupActivity"
    }
}









