package com.example.intervision

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class FragmentHome {

    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private var statements: ArrayList<String>? = null
    private var questions: ArrayList<String>? = null
    private var statementId: ArrayList<String>? = null

    private var homeItemVote: HomeItemVote? = null
    private var homeItemResult: HomeItemResult? = null

    private var resultMade: MutableState<Boolean> = mutableStateOf(false)
    private var voteResult: Boolean? = null

    private var parent: ComponentActivity? = null



    fun init(user: FirebaseUser, db: FirebaseFirestore, parent: ComponentActivity) {
        Log.d("FragmentHome", "Init")
        this.user = user
        this.db = db
        this.parent = parent
        prepVars()
    }

    private fun prepVars() {
        Log.d("FragmentHome", "prepVars")
        statementId = ArrayList()
        statements = ArrayList()
        questions = ArrayList()

        thesis()
    }


    private fun thesis() {
        Log.d("FragmentHome", "thesis")
        db!!.collection("Theses")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        //Log.d(TAG, "Statements : $Statements")
                        statementId!!.add(document.id)
                        statements!!.add(document.data["Statement"].toString())
                        questions!!.add(document.data["Question"].toString())
                    }
                    getIfVoted()
                } else {
                    //Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun initVoteItem(result: Boolean) {

        Log.d("FragmentHome", "InitVoteItem")
        Log.d("FragmentHome", "boolean = $result")
        if (!result) {
            homeItemVote = HomeItemVote()
            homeItemVote!!.init(statements!![0], statementId!![0], questions!![0], user!!, db!!, parent!!)
            Log.d("itemHomeVote","itemHomeVote" + homeItemVote!!)
        } else {
            homeItemResult = HomeItemResult()
            homeItemResult!!.init(statements!![0], questions!![0], user!!, db!!)
            Log.d("homeItemResult"," homeItemResult" + homeItemResult!!)
        }
        resultMade = mutableStateOf(true)
        voteResult = result
    }

    private fun getIfVoted() {
        Log.d("FragmentHome", "getIfVoted")
        var againstArray: ArrayList<String>
        againstArray = arrayListOf()
        var forArray: ArrayList<String>
        forArray = arrayListOf()
        var voted: Boolean


        db!!.collection("Votes")
            .orderBy("uploaded", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        //Log.d(TAG, "Statements : $Statements")
                        @Suppress("UNCHECKED_CAST")
                        againstArray = document.data["Against"] as ArrayList<String>
                        @Suppress("UNCHECKED_CAST")
                        forArray = document.data["In Favour"] as ArrayList<String>
                    }

                    voted = againstArray.contains(user!!.uid)
                    if (!voted) {
                        voted = forArray.contains(user!!.uid)
                    }
                    Log.d(TAG, "voted : $voted")
                    initVoteItem(voted)
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
    private fun toTutorial(){
        val i = Intent(parent, ActivityTutorial::class.java)
        parent!!.startActivity(i)
    }
    @Composable
    fun Component() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Home")
                IconButton(
                    onClick = { toTutorial() }
                ) {
                    Icon(imageVector = Icons.Default.QuestionMark, contentDescription = "Tutorial")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                if (!voteResult!!){
                    homeItemVote!!.Component()
                } else{
                    homeItemResult!!.Component()
                }
            }
        }
    }


    companion object {

        private const val TAG = "Home"
    }
}

@PreviewFontScale()
@Composable
fun FragmentHomePreview() {
    MyApplicationTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Home")
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = "Tutorial"
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                }
            }
        }
    }
}

