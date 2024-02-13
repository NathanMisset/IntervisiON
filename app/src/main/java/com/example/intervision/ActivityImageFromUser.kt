package com.example.intervision

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class ActivityImageFromUser : AppCompatActivity() {


    // One Preview Image
    private var iVPreviewImage: Uri? = null

    // constant to compare
    // the activity result code
    private val selectPicture = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // register the UI widgets with their appropriate IDs
        val bSelectImage: ImageButton = findViewById(R.id.add_photo_button_register)

        // handle the Choose Image button to trigger
        // the image chooser function
        bSelectImage.setOnClickListener { imageChooser() }
    }

    private fun imageChooser() {

        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), selectPicture)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == selectPicture) {
                // Get the url of the image from data
                val selectedImageUri = data!!.data
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    iVPreviewImage = selectedImageUri
                }
            }
        }
    }
}