package com.example.intervision

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Profile : Fragment() {

    private val mParam1: String? = null
    private val mParam2: String? = null
    private var storage: FirebaseStorage? = null
    private val mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user = FirebaseAuth.getInstance().currentUser
        if (user == null) reload()
        val Layout = getView()
        storage = FirebaseStorage.getInstance()
        data
    }

    private val data: Unit
        private get() {
            val db = FirebaseFirestore.getInstance()
            db.collection("User Data")
                .whereEqualTo("User UID", user!!.uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                            SetName(document.data)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    fun SetName(data: Map<String?, Any>) {
        val Layout = view
        val ViewUsername = Layout!!.findViewById<TextView>(R.id.username_settings)
        ViewUsername.text = data["Voornaam"].toString()
        val viewProfilePicture = Layout.findViewById<ImageView>(R.id.profile_picture_settings)
        val storageRef = storage!!.reference

        // Create a reference with an initial file path and name
        val pathReference = storageRef.child("ProfilePictures/" + user!!.uid)
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        pathReference.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes -> // Data for "images/island.jpg" is returns, use this as needed
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                viewProfilePicture.setImageBitmap(bitmap)
            }.addOnFailureListener {
            // Handle any errors
        }
    }

    private fun reload() {
        val i = Intent(activity, Activity_Login::class.java)
        startActivity(i)
    }

    private fun toUserData() {
//        Intent i = new Intent(getActivity(), ACtivity.class);
//        startActivity(i);
    }

    companion object {

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val TAG = "Settings"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment course.
         */

        fun newInstance(param1: String?, param2: String?): Fragment_Profile {
            val fragment = Fragment_Profile()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}