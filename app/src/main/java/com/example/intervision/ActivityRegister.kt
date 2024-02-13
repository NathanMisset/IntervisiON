package com.example.intervision

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ActivityRegister : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    var SELECT_PICTURE = 200
    private var BSelectImage: ImageButton? = null
    private var ProfileURI: Uri? = null
    private val settings = "background off, background tint off, compact src"
    var voornaam: EditText? = null
    var werkfunctie: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        setContentView(R.layout.activity_register)
        voornaam = findViewById(R.id.voornaam_register)
        werkfunctie = findViewById(R.id.werkfunctie_register)
        email = findViewById(R.id.email_register)
        password = findViewById(R.id.password_register)
        BSelectImage = findViewById(R.id.add_photo_button_register)
        val finishRegisteruButton = findViewById<Button>(R.id.finish_button_register)
        BSelectImage!!.setOnClickListener(View.OnClickListener { imageChooser() })
        val AcceptToggle = findViewById<SwitchCompat>(R.id.switch_register)
        AcceptToggle.setOnClickListener {
            Log.d("BUTTONS", "User tapped the AcceptToggle")
            if (AcceptToggle.isChecked && CheckAllFields()) {
                finishRegisteruButton.isEnabled = true
            } else {
                AcceptToggle.toggle()
                finishRegisteruButton.isEnabled = false
            }
        }
        finishRegisteruButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the finishRegisteruButton")
            if (AcceptToggle.isChecked && CheckAllFields()) {
                StartRegister()
            }
        }
    }

    private fun CheckAllFields(): Boolean {
        var check = true
        if (voornaam!!.length() == 0) {
            val error = "Voornaam is verplich"
            Toast(error)
            voornaam!!.error = error
            check = false
        }
        Log.d(TAG, "Voornaam is ingevuld")
        if (werkfunctie!!.length() == 0) {
            val error = "Werkfunctie is verplicht"
            Toast(error)
            werkfunctie!!.error = "Werkfunctie is verplicht"
            check = false
        }
        Log.d(TAG, "werkfunctie is ingevuld")
        if (email!!.length() == 0) {
            val error = "Email is verplicht"
            Toast(error)
            email!!.error = "Email is verplicht"
            check = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email!!.text.toString()).matches()) {
            val error = "email format niet juist"
            Toast(error)
            email!!.error =
                "Vul uw email adres volgens het volgende format in:\n uwnaam@voorbeeld.nl"
            check = false
        }
        Log.d(TAG, "email is ingevuld")
        if (password!!.length() == 0) {
            val error = "Wachtwoord is verplicht"
            Toast(error)
            password!!.error = "Wachtwoord is verplicht"
            check = false
        } else if (password!!.length() < 6) {
            val error = "Wachtwoord moet minimaal 8 karakters bevatten"
            Toast(error)
            password!!.error = "Wachtwoord moet minimaal 8 karakters bevatten"
            check = false
        }
        Log.d(TAG, "Wachtwoord is ingevuld")


        // after all validation return true.
        return check
    }

    private fun Toast(Message: String) {
        Toast.makeText(
            this@ActivityRegister, Message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun StartRegister() {
        val emailEditText = findViewById<EditText>(R.id.email_register)
        val email = emailEditText.text.toString()
        val passwordEditText = findViewById<EditText>(R.id.password_register)
        val password = passwordEditText.text.toString()
        createAccount(email, password)
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth!!.currentUser
                    saveUserData(user)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@ActivityRegister, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun saveUserData(firebaseUser: FirebaseUser?) {
        val db = FirebaseFirestore.getInstance()
        val storageRef = storage!!.reference
        Log.d(TAG, "IF")
        if (BSelectImage!!.width.toFloat() == TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                resources.displayMetrics
            )
        ) {
            val ref = storageRef.child("ProfilePictures/" + firebaseUser!!.uid)
            ref.putFile(ProfileURI!!)
                .addOnSuccessListener { Log.d(TAG, "image uploaded successfully") }
                .addOnFailureListener { Log.d(TAG, "image uploaded failed") }
        } else {
        }
        val voornaamEditText = findViewById<EditText>(R.id.voornaam_register)
        val voornaam = voornaamEditText.text.toString()
        val werkFunctieEditText = findViewById<EditText>(R.id.werkfunctie_register)
        val werkFunctie = werkFunctieEditText.text.toString()

        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        user["Voornaam"] = voornaam
        user["Werk functie"] = werkFunctie
        user["User UID"] = firebaseUser!!.uid
        db.collection("User Data")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun reload() {
        val i = Intent(this, ActivityNavigation::class.java)
        startActivity(i)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) return
        reload()
    }

    fun imageChooser() {

        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                Log.d(TAG, "data.getData() " + data!!.data)
                Log.d(TAG, "type " + data.data!!.javaClass)

                // Get the url of the image from data
                ProfileURI = data.data
                if (null != ProfileURI) {
                    // update the preview image in the layout
                    BSelectImage!!.setImageURI(ProfileURI)
                    BSelectImage!!.background = null
                    BSelectImage!!.setBackgroundColor(Color.TRANSPARENT)
                    val params = BSelectImage!!.layoutParams
                    val height = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        200f,
                        resources.displayMetrics
                    ).toInt()
                    val width = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        200f,
                        resources.displayMetrics
                    ).toInt()
                    params.height = height
                    params.width = width
                    BSelectImage!!.layoutParams = params
                }
            }
        }
    }

    private fun toHome() {}

    companion object {
        private const val TAG = "EmailPassword"
        private const val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}