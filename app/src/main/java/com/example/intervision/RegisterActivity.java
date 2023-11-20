package com.example.intervision;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.NavType;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ImageView IVPreviewImage;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Patterns patterns;
    int SELECT_PICTURE = 200;
    private View BSelectImage;
    private Uri ProfileURI;

    EditText voornaam, regio, werkfunctie, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        setContentView(R.layout.activity_register);


        voornaam = findViewById(R.id.voornaam_register);
        regio = findViewById(R.id.regio_register);
        werkfunctie = findViewById(R.id.werkfunctie_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);

        //Buttons
        // register the UI widgets with their appropriate IDs
        BSelectImage = findViewById(R.id.add_photo_button_register);
        IVPreviewImage = findViewById(R.id.image_preview_register);
        Button finishRegisteruButton = findViewById(R.id.finish_button_register);
        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        SwitchCompat AcceptToggle = findViewById(R.id.switch_register);
        AcceptToggle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the AcceptToggle");
                if (AcceptToggle.isChecked() && CheckAllFields()){
                    finishRegisteruButton.setEnabled(true);
                }else{
                    AcceptToggle.toggle();
                    finishRegisteruButton.setEnabled(false);
                }
            }
        });

        finishRegisteruButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the finishRegisteruButton");
                if(AcceptToggle.isChecked() && CheckAllFields()){
                    StartRegister();
                }
            }
        });
    }
    private boolean CheckAllFields() {
        boolean check = true;
        if (voornaam.length() == 0) {
            String error = "Voornaam is verplich";
            Toast(error);
            voornaam.setError(error);
            check = false;
        }
        Log.d(TAG,"Voornaam is ingevuld");
        if (regio.length() == 0) {
            String error = "Regio is verplicht";
            Toast(error);
            regio.setError("Regio is verplicht");
            check = false;
        }
        Log.d(TAG,"regio is ingevuld");
        if (werkfunctie.length() == 0) {
            String error = "Werkfunctie is verplicht";
            Toast(error);
            werkfunctie.setError("Werkfunctie is verplicht");
            check = false;
        }
        Log.d(TAG,"werkfunctie is ingevuld");
        if (email.length() == 0) {
            String error = "Email is verplicht";
            Toast(error);
            email.setError("Email is verplicht");
            check = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            String error = "email format niet juist";
            Toast(error);
            email.setError("Vul uw email adres volgens het volgende format in:\n uwnaam@voorbeeld.nl");
            check = false;
        }
        Log.d(TAG,"email is ingevuld");
        if (password.length() == 0) {
            String error = "Wachtwoord is verplicht";
            Toast(error);
            password.setError("Wachtwoord is verplicht");
            check = false;
        } else if (password.length() < 6) {
            String error = "Wachtwoord moet minimaal 8 karakters bevatten";
            Toast(error);
            password.setError("Wachtwoord moet minimaal 8 karakters bevatten");
            check = false;
        }
        Log.d(TAG,"Wachtwoord is ingevuld");
        Log.d(TAG, IVPreviewImage.toString());


        // after all validation return true.
        return check;
    }

    private void Toast(String Message){
        Toast.makeText(RegisterActivity.this, Message,
                Toast.LENGTH_SHORT).show();
    }
    private void StartRegister(){
        EditText emailEditText = findViewById(R.id.email_register);
        String email = emailEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.password_register);
        String password = passwordEditText.getText().toString();
        createAccount(email, password);
    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserData(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }
    private void saveUserData(FirebaseUser firebaseUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference storageRef = storage.getReference();

        if(IVPreviewImage != null)
        {
            StorageReference ref = storageRef.child("ProfilePictures/"+ firebaseUser.getUid());
            ref.putFile(ProfileURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "image uploaded successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "image uploaded failed");
                        }
                    });
        }

        EditText regioEditText = findViewById(R.id.regio_register);
        String regio = regioEditText.getText().toString();
        EditText voornaamEditText = findViewById(R.id.voornaam_register);
        String voornaam = voornaamEditText.getText().toString();
        EditText werkFunctieEditText = findViewById(R.id.werkfunctie_register);
        String werkFunctie = werkFunctieEditText.getText().toString();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Regio", regio);
        user.put("Voornaam", voornaam);
        user.put("Werk functie", werkFunctie);
        user.put("User UID", firebaseUser.getUid());


        db.collection("user_data")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
    }

    private void reload() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
    private void updateUI(FirebaseUser user) {
        if (user == null) return;
        reload();
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                ProfileURI = data.getData();
                if (null != ProfileURI) {
                    // update the preview image in the layout

                    IVPreviewImage.setImageURI(ProfileURI);
                }
            }
        }
    }
}
