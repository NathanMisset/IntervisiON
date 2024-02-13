package com.example.intervision

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("UNCHECKED_CAST")
class ActivityGroupAssembly : AppCompatActivity() {
    private var scrollLayout: ViewGroup? = null
    private var availableGroups: ArrayList<Item_Group_Preview>? = null
    private var user: FirebaseAuth? = null
    private var dataBase: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_assembly)
        user = FirebaseAuth.getInstance()
        scrollLayout = findViewById(R.id.scroll_layout_child_group_assembly)
        availableGroups = ArrayList()
        data
    }

    private val data: Unit
        get() {
            dataBase = FirebaseFirestore.getInstance()
            dataBase!!.collection("Sessions")
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
        availableGroups!!.add(
            Item_Group_Preview(
                data["Group Name"].toString(),
                data["Participant Sid"] as ArrayList<String?>?,
                scrollLayout,
                this, dataBase, user, docname, data["Leader Sid"].toString()
            )
        )
    }

//    private fun toHome() {
//        val i = Intent(this, ActivityLogin::class.java)
//        startActivity(i)
//    }

    companion object {
        private const val TAG = "GroupAssemblyActivity"
    }
}