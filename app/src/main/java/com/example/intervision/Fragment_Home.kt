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
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Home : Fragment() {

    private val mParam1: String? = null
    private val mParam2: String? = null
    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private var Statements: ArrayList<String>? = null
    private var Questions: ArrayList<String>? = null
    private var viewPager2: ViewPager2? = null
    private var greeting: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun SetName(data: Map<String?, Any>) {
        greeting!!.text = """Goedemiddag,
 ${data["Voornaam"]}"""
    }

    private val username: Unit
        private get() {
            db!!.collection("User Data")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, " => UserID " + document.data["User UID"])
                            Log.d(TAG, user!!.uid)
                            val u = user!!.uid
                            val d = document.data["User UID"] as String?
                            if (u == d) {
                                Log.d(TAG, "Inside if")
                                //SetName(document.getData());
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }
    private val thesis: Unit
        private get() {
            db!!.collection("Theses")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Statements!!.add(document.data["Statement"].toString())
                            Questions!!.add(document.data["Question"].toString())
                            Log.d(TAG, "Statements : $Statements")
                        }
                        Log.d(TAG, "MakeThesisViewPager")
                        MakeThesisViewPager()
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    private fun MakeThesisViewPager() {
        Log.d(TAG, "Start MakeThesisViewPager")
        Log.d(TAG, "viewPager2: $viewPager2")
        val viewPagerItemArrayList: ArrayList<Item_View_Pager_Small>
        viewPagerItemArrayList = ArrayList()
        Log.d(TAG, "viewPagerItemArrayList: $viewPagerItemArrayList")
        for (i in Statements!!.indices) {
            val viewPagerItem = Item_View_Pager_Small(Questions!![i], Statements!![i])
            viewPagerItemArrayList.add(viewPagerItem)
        }
        val vpAdapter = Adapter_View_Pager_Small(viewPagerItemArrayList)
        viewPager2!!.adapter = vpAdapter
        viewPager2!!.clipToPadding = false
        viewPager2!!.clipChildren = false
        viewPager2!!.offscreenPageLimit = 2
        viewPager2!!.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
    }

    private fun ToTutorial() {
        val i = Intent(activity, Activity_Tutorial::class.java)
        startActivity(i)
    }

    private fun reload() {
        val i = Intent(activity, Activity_Login::class.java)
        startActivity(i)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.acitivity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
        Statements = ArrayList()
        Questions = ArrayList()
        username
        thesis
        val Layout = getView()
        val toTutorialButton = Layout!!.findViewById<View>(R.id.tutorial_button_home) as Button
        toTutorialButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped $toTutorialButton")
            ToTutorial()
        }
        viewPager2 = Layout.findViewById(R.id.thesis_home)
        greeting = Layout.findViewById(R.id.greeting_home)
        // Inflate the layout for this fragment
    }

    companion object {

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val TAG = "Home"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment algorithm_fragment.
         */

        fun newInstance(param1: String?, param2: String?): Fragment_Home {
            val fragment = Fragment_Home()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}