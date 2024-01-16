package com.example.intervision

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel



class Item_Result(parent: ViewGroup?, activity: FragmentActivity?, statement: String, user: FirebaseUser, fireStoreDatabase: FirebaseFirestore, context: Context) {
    var parent: ViewGroup?
    var layout: View
    var activity: FragmentActivity?
    var statement: String
    var user: FirebaseUser
    var fireStoreDatabase: FirebaseFirestore
    var context: Context


    // Create the object of TextView
    // and PieChart class
    var tvVoor: TextView? = null
    var tvTegen: TextView? = null
    var pieChart: PieChart? = null




    var nFor: Float? = null
    var nAgaint: Float? = null

    init {
        this.parent = parent
        this.activity = activity
        this.statement = statement
        this.user = user
        this.fireStoreDatabase = fireStoreDatabase
        this.context = context

        layout = LayoutInflater.from(parent!!.context).inflate(
            R.layout.item_results,
            parent,
            true
        )
        this.tvVoor = this.layout.findViewById(R.id.voor_tv_item_result)
        this.tvTegen = this.layout.findViewById(R.id.tegen_tv_item_result)
        this.pieChart = this.layout.findViewById(R.id.piechart_item_result)
        getData()
    }

    private fun setData() {


        // Set the percentage of language used
        tvVoor!!.setText("Voor")
        tvTegen!!.setText("Tegen")

        // Set the data and color to the pie chart
        pieChart!!.addPieSlice(
            PieModel(
                "Voor", nFor!!,
                Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.yellow_100)))
            )
        )
        pieChart!!.addPieSlice(
            PieModel(
                "Tegen", nAgaint!!,
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.blue_100)))
            )
        )

        // To animate the pie chart
        pieChart!!.startAnimation()
    }
    fun getData(){
        var ForArray: ArrayList<ArrayList<String?>>
        ForArray = ArrayList()
        var AgaintArray: ArrayList<ArrayList<String?>>
        AgaintArray = ArrayList()
        fireStoreDatabase.collection("Votes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
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
}