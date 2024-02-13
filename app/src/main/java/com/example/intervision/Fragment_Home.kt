package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class FragmentHome : Fragment() {

    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private var statements: ArrayList<String>? = null
    private var questions: ArrayList<String>? = null
    private var statementId: ArrayList<String>? = null
    private var voteItemLocation: ViewGroup? = null
    private var greeting: TextView? = null


//    fun setName(data: Map<String?, Any>) {
//        greeting!!.text = """Goedemiddag,
//        ${data["Voornaam"]}"""
//    }

//    private val username: Unit
//        get() {
//            db!!.collection("User Data")
//                .get()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        for (document in task.result) {
//                                Log.d(TAG, " => UserID " + document.data["User UID"])
//                                Log.d(TAG, user!!.uid)
//                            val u = user!!.uid
//                            val d = document.data["User UID"] as String?
//                            if (u == d) {
//                                Log.d(TAG, "Inside if")
//                                SetName(document.getData());
//                            }
//                        }
//                    } else {
//                        Log.w(TAG, "Error getting documents.", task.exception)
//                    }
//                }
//        }
    private val thesis: Unit
        get() {
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
                        //Log.d(TAG, "MakeThesisViewPager")
                        getIfVoted()
                    } else {
                        //Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }
    private fun InitVoteItem(result: Boolean){
        if(!result){
            Item_Vote(voteItemLocation, activity, statements!![0], questions!![0], statementId!![0], user!!, db!!)
        } else{
            Item_Result(voteItemLocation, activity, statements!![0], user!!, db!!, requireContext())
        }
    }

    private fun getIfVoted() {
        var againstArray: ArrayList<String>
        againstArray = arrayListOf()
        var forArray: ArrayList<String>
        forArray = arrayListOf()
        var voted: Boolean


        db!!.collection("Votes")
            .orderBy("uploaded",Query.Direction.DESCENDING)
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
                    if(!voted){
                        voted = forArray.contains(user!!.uid)
                    }
                    Log.d(TAG, "voted : $voted")
                    InitVoteItem(voted)
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
//    private fun MakeThesisViewPager() {
//        Log.d(TAG, "Start MakeThesisViewPager")
//        Log.d(TAG, "viewPager2: $viewPager2")
//        val viewPagerItemArrayList: ArrayList<Item_View_Pager_Small>
//        viewPagerItemArrayList = ArrayList()
//
//        Log.d(TAG, "viewPagerItemArrayList: $viewPagerItemArrayList")
//
//        for (i in Statements!!.indices) {
//            val viewPagerItem = Item_View_Pager_Small(Questions!![i], Statements!![i])
//            viewPagerItemArrayList.add(viewPagerItem)
//        }
//
//        val vpAdapter = Adapter_View_Pager_Small(viewPagerItemArrayList)
//        viewPager2!!.adapter = vpAdapter
//        viewPager2!!.clipToPadding = false
//        viewPager2!!.clipChildren = false
//        viewPager2!!.offscreenPageLimit = 2
//        viewPager2!!.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
//    }

    private fun toTutorial() {
        val i = Intent(activity, ActivityTutorial::class.java)
        startActivity(i)
    }

//    private fun reload() {
//        val i = Intent(activity, ActivityLogin::class.java)
//        startActivity(i)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.acitivity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
        statements = ArrayList()
        questions = ArrayList()
        statementId = ArrayList()



        val layout = getView()
        val toTutorialButton = layout!!.findViewById<View>(R.id.tutorial_button_home) as Button
        toTutorialButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped $toTutorialButton")
            toTutorial()

        }
        voteItemLocation = layout.findViewById(R.id.vote_item_location_home)

        greeting = layout.findViewById(R.id.greeting_home)

        //username
        thesis
        // Inflate the layout for this fragment
    }

    companion object {

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val TAG = "Home"



//        fun newInstance(): FragmentHome {
//            val fragment = FragmentHome()
//            val args = Bundle()
//            fragment.arguments = args
//            return fragment
//        }
    }
}