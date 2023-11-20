package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Activity_Group_Assembly : AppCompatActivity() {
    protected var scrollLayout: ViewGroup? = null
    protected var availableGroups: ArrayList<Item_Group_Preview>? = null
    protected var user: FirebaseAuth? = null
    protected var dataBase: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_assembly)
        user = FirebaseAuth.getInstance()
        scrollLayout = findViewById(R.id.scroll_layout_child_group_assembly)
        availableGroups = ArrayList()
        data
    }

    protected val data: Unit
        protected get() {
            dataBase = FirebaseFirestore.getInstance()
            dataBase!!.collection("Sessions")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                            AddGroup(document.data, document.id)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    protected fun AddGroup(data: Map<String?, Any>, docname: String) {
        availableGroups!!.add(
            Item_Group_Preview(
                data["Group Name"].toString(),
                data["Participant Sid"] as ArrayList<String?>?,
                scrollLayout,
                this, dataBase, user, docname, data["Leader Sid"].toString()
            )
        )
    }

    private fun toHome() {
        val i = Intent(this, Activity_Login::class.java)
        startActivity(i)
    }

    companion object {
        private const val TAG = "GroupAssemblyActivity"
    }
}