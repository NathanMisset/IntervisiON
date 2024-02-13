package com.example.intervision

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class Item_Vote(parent: ViewGroup?, activity: FragmentActivity?, statement: String, question: String, statementId: String, user: FirebaseUser, fireStoreDatabase: FirebaseFirestore) {

        var parent: ViewGroup?
        var layout: View
        var activity: FragmentActivity?
        var statement: String
        var statementId: String
        var question: String
        var agreedButton: Button
        var disAgreedButton: Button
        var statementView: TextView
        var questionView: TextView

        var user: FirebaseUser
        var fireStoreDatabase: FirebaseFirestore
        init {
            this.parent = parent
            this.activity = activity
            this.statement = statement
            this.statementId = statementId
            this.question = question

            this.user = user
            this.fireStoreDatabase = fireStoreDatabase

            layout = LayoutInflater.from(parent!!.context).inflate(
                R.layout.item_vote,
                parent,
                true
            )
            this.agreedButton = layout.findViewById(R.id.agreedButton_item_vote)
            this.disAgreedButton = layout.findViewById(R.id.disAgreedButton_item_vote)
            this.statementView = layout.findViewById(R.id.statement_item_vote)
            this.statementView.text = statement
            this.questionView = layout.findViewById(R.id.question_item_vote)
            this.questionView.text = question


            agreedButton.setOnClickListener {
                Log.d("BUTTONS", "User tapped the agree vote button")
                saveAgreed()
                agreedButton.isEnabled = false
            }
            disAgreedButton.setOnClickListener {
                Log.d("BUTTONS", "User tapped the disAgree vote button")
                saveDisAgreed()
                disAgreedButton.isEnabled = false
            }
        }
    fun saveAgreed(){
        var voteData: ArrayList<ArrayList<String?>>
        voteData = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    Log.d(TAG, "document.data =" + document.data.get("In Favour"))
                    voteData.add(document.data.get("In Favour") as java.util.ArrayList<String?>)
                    voteData.get(0).add(user.uid)
                    saveAgreed1(voteData.get(0))

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    fun saveAgreed1(data: java.util.ArrayList<String?>){
        fireStoreDatabase.collection("Votes")
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, result.documents[0].id)
                saveAgreed2(data, result.documents[0].id)
            }
            .addOnFailureListener{exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    fun saveAgreed2(data: java.util.ArrayList<String?>, id: String){
        fireStoreDatabase.collection("Votes")
            .document(id)
            .update("In Favour", data)
            .addOnSuccessListener { reload() }
    }


    fun saveDisAgreed(){
        var voteData: ArrayList<ArrayList<String?>>
        voteData = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    Log.d(TAG, "document.data =" + document.data.get("Against"))
                    voteData.add(document.data.get("Against") as java.util.ArrayList<String?>)
                    voteData.get(0).add(user.uid)
                    saveDisAgreed1(voteData.get(0))

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    fun saveDisAgreed1(data: java.util.ArrayList<String?>){

        fireStoreDatabase.collection("Votes")
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, result.documents[0].id)
                saveDisAgreed2(data, result.documents[0].id)
            }
            .addOnFailureListener{exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    fun saveDisAgreed2(data: java.util.ArrayList<String?>, id: String){
        fireStoreDatabase.collection("Votes")
            .document(id)
            .update("Against", data)
            .addOnSuccessListener {
                reload()
            }


    }
    fun reload(){
        val i = Intent(activity!!, ActivityNavigation::class.java)
        activity!!.startActivity(i)
    }
    companion object {
        private const val TAG = "Item Vote"
    }

}

