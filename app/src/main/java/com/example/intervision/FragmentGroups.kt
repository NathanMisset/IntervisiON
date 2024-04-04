package com.example.intervision

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentGroups : ComponentActivity() {
    private var availableGroups: ArrayList<ItemGroupPreview>? = null
    private var user: FirebaseAuth? = null
    private var dataBase: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null
    private var parent: ComponentActivity? = null

    fun init(user: FirebaseAuth, db: FirebaseFirestore, storage:FirebaseStorage, parent: ComponentActivity) {
        this.user = user
        dataBase = db
        this.storage = storage
        this.parent = parent
        data()
    }
    private fun data(){
            availableGroups = ArrayList()
            dataBase!!.collection("Sessions")
                .whereArrayContains("Participant Sid", user!!.uid.toString())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                            addGroup(document.data, document.id)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    private fun addGroup(data: Map<String?, Any>, docname: String) {
        val a = ItemGroupPreview()
        a.init(storage!!, dataBase!!, user!!.currentUser!!, data, parent!!, docname)
        availableGroups!!.add(a)
    }

    private fun toMakeGroup() {
        val i = Intent(parent, ActivityMakeGroup::class.java)
        parent!!.startActivity(i)
    }
    @Composable
    fun Component() {

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Group")
            }
            Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .verticalScroll(rememberScrollState())
            ) {
            for (group in availableGroups!!){
                group.Component()
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(.5f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { toMakeGroup() },
                    modifier = Modifier

//                        .fillMaxHeight()
                        .defaultMinSize(minHeight = 10.dp),
                ) {
                    Text(text = "Maak groep")
                }
            }
        }
    }


    companion object {
        private const val TAG = "GroupAssemblyActivity"
    }
}

@PreviewFontScale()
@Composable
fun FragmentGroupPreview() {
    MyApplicationTheme {
        Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Group")
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .verticalScroll(rememberScrollState())
            ) {

            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(.5f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier

//                        .fillMaxHeight()
                        .defaultMinSize(minHeight = 10.dp),
                ) {
                    Text(text = "Maak groep")
                }
            }
        }
    }
}
