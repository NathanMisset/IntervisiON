/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.Spacing
import com.example.intervision.ui.spacing
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.selects.select
import java.util.Locale

/**
 *
 * This activity controls making a group
 *
 */

class ActivityMakeGroup : ComponentActivity() {

    /** Class variables */
    /** To get from other sources */
    private var leaderName: MutableState<String> = mutableStateOf("GroepLeider")
    private var thesisID = ArrayList<String>()
    private var thesis = ArrayList<String>()
    private var users: ArrayList<String>? = null
    private var uIDs: ArrayList<String?>? = null

    /** To get user */
    private lateinit var selectedDate: MutableState<Long>
    private lateinit var selectedDateText: MutableState<String>
    private lateinit var selectedUser: MutableList<String>
    private lateinit var selectedUserID: MutableList<String>
    private var selectedThesis: MutableState<String> = mutableStateOf("")
    private var selectedThesisID: MutableState<String> = mutableStateOf("")
    private var groupName: MutableState<String> = mutableStateOf("")
    private var participants = ArrayList<String?>()


    /** Firebase */
    private var user: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var fireStoreDatabase: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Start")
        super.onCreate(savedInstanceState)
        setContent {
            GroupNameScreen()
        }

        /** Firebase */
        fireStoreDatabase = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
        selectedUserID = mutableStateListOf()
        selectedUserID.add(user!!.uid.toString())
        participants = ArrayList()
        participants.add(user!!.uid)


        users = ArrayList()
        uIDs = ArrayList()
        thesis = ArrayList()

        selectedUser = mutableStateListOf()

        selectedDate = mutableStateOf(0)
        selectedDateText = mutableStateOf("Datum")

        getData()
    }

    private fun getData() {
        fireStoreDatabase!!.collection("User Data")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val u = user!!.uid
                        val d = document.data["User UID"] as String?
                        if (document.data["Voornaam"].toString().isNotEmpty()) {
                            users!!.add(document.data["Voornaam"].toString())
                            uIDs!!.add(d)
                        }
                        if (u == d) {
                            leaderName.value = document.data["Voornaam"].toString()
                        }
                    }
                    getThesis()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun getThesis() {
        fireStoreDatabase!!.collection("Theses")
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        thesis.add(document.data["Statement"].toString())
                        thesisID.add(document.id)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    /**
     *
     * Gather all information put in and save it to the database
     * If succesfull user will be sent bake to navigation in the homescreen
     *
     */
    private fun makeGroup() {
        Log.d(TAG, "Make Group")
        val session: MutableMap<String, Any?> = HashMap()
        session["Leader Sid"] = user!!.uid
        session["Participant Sid"] = selectedUserID
        session["Group Name"] = groupName.value
        session["ThesisID"] = selectedThesisID
        session["DataInMili"] = selectedDate.value.toString()
        Log.d(TAG, session.toString())
        fireStoreDatabase!!.collection("Sessions")
            .add(session)
            .addOnSuccessListener { documentReference ->
                val database =
                    FirebaseDatabase.getInstance("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app")
                val myRef = database.getReference(documentReference.id)
                myRef.setValue("W")
                toHome()
                Log.d(TAG, "Succes adding document")
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


    private fun removeThesis(id: Int) {
    }

    private fun toHome() {
        startActivity(Intent(this, ActivityNavigation::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ActivityNavigation::class.java))
        finish()
    }

    private fun toThesisSelection() {
        setContent {
            TheisScreen()
        }
    }

    private fun toGroupNameSelection() {
        setContent {
            GroupNameScreen()
        }
    }

    private fun toParticipantSelection() {
        setContent {

        }
    }

    private fun checkGroupName(): Boolean {
        var check = true
        if (groupName.value.isEmpty()) {
            val error = getString(R.string.groupNameErrorMakeGroup)
            toast(error)
            check = false
        }
        // after all validation return true.
        return check
    }
    private fun checkThesisID(): Boolean {
        var check = true
        if (selectedThesisID.value.isEmpty()) {
            val error = getString(R.string.thesisErrorMakeGroup)
            toast(error)
            check = false
        }
        // after all validation return true.
        return check
    }

    private fun toast(message: String) {
        Toast.makeText(
            this@ActivityMakeGroup, message,
            Toast.LENGTH_SHORT
        ).show()
    }

    /** Composables */
    @Composable
    //@Preview(showSystemUi = true, showBackground = true)
    fun GroupNameScreen() {
        val focusManager = LocalFocusManager.current
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = spacing.medium),
                        text = getString(R.string.groupNameExplanationMakeGroup)
                    )
                    TextField(
                        value = groupName.value,
                        onValueChange = { groupName.value = it },
                        label = { Text(text = getString(R.string.groupNameMakeGroup)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 50.dp)
                            .fillMaxWidth()
                            .padding(bottom = spacing.medium)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f),

                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            if (checkGroupName()) {
                                toThesisSelection()
                            }
                        }) {
                            Text(text = getString(R.string.nextButtonApp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    //@Preview(showSystemUi = true, showBackground = true)
    fun TheisScreen() {
        val navController = rememberNavController()
        val focusManager = LocalFocusManager.current
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                var selectedThesis = remember { mutableStateOf("") }
                Text(
                    text = "Groep stelling:",
                    modifier = Modifier
                        .padding(bottom = spacing.medium, top = spacing.medium)
                        .fillMaxWidth(0.8f),
                )
                Text(
                    text = selectedThesis.value,
                    modifier = Modifier
                        .padding(bottom = spacing.medium)
                        .fillMaxWidth(0.8f),
                    )
                HorizontalDivider(thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth(0.8f)
                        .padding(bottom = spacing.large))
                Text(
                    modifier = Modifier.padding(bottom = spacing.medium),
                    text = getString(R.string.thesisExplanationMakeGroup)
                )
                val textState = remember { mutableStateOf(TextFieldValue("")) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    SearchView(textState)
                    ThesisList(navController = navController, state = textState, selectedString = selectedThesis)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        toGroupNameSelection()
                    }) {
                        Text(text = getString(R.string.backButtonApp))
                    }
                    Button(onClick = {
                        if(checkThesisID()){
                            toParticipantSelection()
                        }
                    }) {
                        Text(text = getString(R.string.nextButtonApp))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchView(state: MutableState<TextFieldValue>) {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),

            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(20.dp)
                        .size(24.dp)
                )
            },

            trailingIcon = {
                if (state.value != TextFieldValue("")) {
                    IconButton(
                        onClick = {
                            state.value =
                                TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp), // The TextFiled has rounded corners top left and right by default
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    @Composable
    fun ThesisListItem(thesisText: String, onItemClick: (String) -> Unit) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onItemClick(thesisText) })
                .background(MaterialTheme.colorScheme.primaryContainer)
                .height(57.dp)
                .fillMaxWidth()
                .padding(PaddingValues(8.dp, 16.dp)),
        ) {
            Text(text = thesisText, fontSize = 18.sp, color = Color.Black)
        }
    }

    @Composable
    fun ThesisList(navController: NavController, state: MutableState<TextFieldValue>, selectedString: MutableState<String>) {
        val thesisies = thesis
        val empty = ArrayList<String>()
        var filteredThesisies: ArrayList<String>
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            val searchedText = state.value.text
            filteredThesisies = if (searchedText.isEmpty()) {
                empty
            } else {
                var nInList = 0
                val resultList = ArrayList<String>()
                for (thesis in thesisies) {
                    if (thesis.lowercase(Locale.getDefault())
                            .contains(searchedText.lowercase(Locale.getDefault()))
                    ) {
                        resultList.add(thesis + "-" + thesisID[nInList])
                    }
                    nInList++
                }
                resultList
            }
            items(filteredThesisies) { filteredThesis ->
                ThesisListItem(
                    thesisText = filteredThesis.split("-")[0],
                    onItemClick = { selected ->
                        selectedString.value = selected
                        state.value = TextFieldValue()
                        selectedThesis.value = filteredThesis.split("-")[0]
                        selectedThesisID.value = filteredThesis.split("-")[1]
                    }
                )
            }
        }
    }

    companion object {
        private const val TAG = "MakeGroupActivity"
    }
}



//Previews

@Composable
@Preview()
fun PreviewGroupNameScreen() {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(bottom = spacing.medium),
                    text = "dasdasd"
                )
                var text by remember { mutableStateOf("Hello") }

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Label") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .defaultMinSize(minHeight = 50.dp)
                        .fillMaxWidth()
                        .padding(bottom = spacing.medium)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {  }) {
                        Text(text = "Next")
                    }
                }
            }
        }
}


@Composable
@Preview()
fun PreviewTheisScreen() {
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current
    IntervisionBaseTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            var selectedThesis = remember { mutableStateOf("") }
            Text(
                text = "Groep stelling:",
                modifier = Modifier
                    .padding(bottom = spacing.medium, top = spacing.medium)
                    .fillMaxWidth(0.8f),
            )
            Text(
                text = selectedThesis.value,
                modifier = Modifier
                    .padding(bottom = spacing.medium)
                    .fillMaxWidth(0.8f),
            )
            HorizontalDivider(thickness = 2.dp,
                modifier = Modifier.fillMaxWidth(0.8f)
                    .padding(bottom = spacing.large))
            Text(
                modifier = Modifier.padding(bottom = spacing.medium),
                text = "tesda"
            )
            val textState = remember { mutableStateOf(TextFieldValue("")) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SearchView(textState)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {

                }) {
                    Text(text = "Terug")
                }
                Button(onClick = {

                }) {
                    Text(text = "Volgende")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),

        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(20.dp)
                    .size(24.dp)
            )
        },

        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp), // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ThesisListItem(thesisText: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClick(thesisText) })
            .background(MaterialTheme.colorScheme.primaryContainer)
            .height(57.dp)
            .fillMaxWidth()
            .padding(PaddingValues(8.dp, 16.dp)),
    ) {
        Text(text = thesisText, fontSize = 18.sp, color = Color.Black)
    }
}


