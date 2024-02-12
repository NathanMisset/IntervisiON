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


class FragmentProfile : Fragment() {

    private var storage: FirebaseStorage? = null
    private var user: FirebaseUser? = null

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
        val layout = getView()
        storage = FirebaseStorage.getInstance()
        data()
        val settingsButton = layout!!.findViewById(R.id.button_settings_profile) as ImageView

        settingsButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped $settingsButton")
            toSettings()

        }
    }

    fun data() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User Data")
            .whereEqualTo("User UID", user!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                        setName(document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
    private fun toSettings(){
        //Settings
        val i = Intent(activity, ActivityJetpackCompose::class.java)
        startActivity(i)
    }

    private fun setName(data: Map<String?, Any>) {
        val layout = view
        val viewUsername = layout!!.findViewById<TextView>(R.id.username_settings)
        viewUsername.text = data["Voornaam"].toString()
        val viewProfilePicture = layout.findViewById<ImageView>(R.id.profile_picture_settings)
        val storageRef = storage!!.reference

        // Create a reference with an initial file path and name
        val pathReference = storageRef.child("ProfilePictures/" + user!!.uid)
        val oNEMEGABYTE = (1024 * 1024).toLong()
        pathReference.getBytes(oNEMEGABYTE)
            .addOnSuccessListener { bytes -> // Data for "images/island.jpg" is returns, use this as needed
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                viewProfilePicture.setImageBitmap(bitmap)
            }.addOnFailureListener {
            // Handle any errors
        }
    }

    private fun reload() {
        val i = Intent(activity, ActivityLogin::class.java)
        startActivity(i)
    }

//    private fun toUserData() {
////        Intent i = new Intent(getActivity(), ACtivity.class);
////        startActivity(i);
//    }

    companion object {

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val TAG = "Settings"


//        fun newInstance(param1: String?, param2: String?): FragmentProfile {
//            val fragment = FragmentProfile()
//            val args = Bundle()
//            fragment.arguments = args
//            return fragment
//        }
    }
}