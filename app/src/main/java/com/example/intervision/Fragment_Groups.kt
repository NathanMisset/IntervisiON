package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Groups.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Groups : Fragment() {

    private val mParam1: String? = null
    private val mParam2: String? = null
    protected var scrollLayout: ViewGroup? = null
    protected var availableGroups: ArrayList<Item_Group_Preview>? = null
    protected var user: FirebaseAuth? = null
    protected var dataBase: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_group_assembly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Layout = getView()
        user = FirebaseAuth.getInstance()
        scrollLayout = Layout!!.findViewById(R.id.scroll_layout_child_group_assembly)
        availableGroups = ArrayList()

        val toMakeGroup = Layout.findViewById<View>(R.id.make_group_button_group_assembly) as Button
        toMakeGroup.setOnClickListener {
            Log.d("BUTTONS", "User tapped $toMakeGroup")
            ToMakeGroup()
        }
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
                activity, dataBase, user, docname, data["Leader Sid"].toString()
            )
        )
    }

    private fun ToMakeGroup() {
        val i = Intent(activity, Activity_Make_Group::class.java)
        startActivity(i)
    }

    private fun toHome() {
        val i = Intent(activity, Activity_Login::class.java)
        startActivity(i)
    }

    companion object {

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val TAG = "GroupAssemblyActivity"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profile.
         */

        fun newInstance(param1: String?, param2: String?): Fragment_Groups {
            val fragment = Fragment_Groups()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}