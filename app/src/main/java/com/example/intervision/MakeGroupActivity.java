package com.example.intervision;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeGroupActivity extends AppCompatActivity {
    private TextView leader, groupName;
    private FirebaseAuth user;
    private FirebaseDatabase database;
    private FirebaseFirestore fireStoreDatabase;
    private static final String TAG = "MakeGroupActivity";
    private SpinnerItem mainSpinner;
    public ArrayList<SpinnerItem> spinners;
    private ArrayList<String> users;
    private ArrayList<String> UIDs;
    public Activity activity;
    public int postionMainSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);

        user = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance ("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app");
        fireStoreDatabase = FirebaseFirestore.getInstance();

        groupName = findViewById(R.id.group_name_make_group);

        users = new ArrayList<>();
        UIDs = new ArrayList<>();
        users.add("Gebruiker");
        UIDs.add(null);
        postionMainSpinner = 0;
        getData();
        Button makeGroupButton = (Button) findViewById(R.id.make_group_button);
        makeGroupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                MakeGroup();
            }
        });

    }
    void setSpinner(){
        ArrayList listOfNUmbers = new ArrayList<String>();
        for (int i = 0; i < 6; i++) {
            listOfNUmbers.add(i);
        }
        mainSpinner = new SpinnerItem(listOfNUmbers,    findViewById(R.id.layoutOut_make_group),this);
        mainSpinner.init();
        spinners = new ArrayList<SpinnerItem>();
        for (int i = 0; i < 5; i++) {
            spinners.add(new SpinnerItem(users, findViewById(R.id.layoutOut_make_group), this));
            spinners.get(i).init();
            spinners.get(i).Disable();
        }
        mainSpinner.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postionMainSpinner = position;
                for (SpinnerItem spinner: spinners
                     ) {
                    spinner.Disable();
                }
                for (int i = 0; i < position; i++) {
                    spinners.get(i).Enable();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void SetName(Map<String, Object> data){
        leader = findViewById(R.id.leader_name_make_group);
        leader.setText("Groepsleider: " + data.get("Voornaam").toString());
    }
    private void getData(){
        fireStoreDatabase.collection("user_data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG,  " => UserID " + document.getData().get("User UID"));
                                Log.d(TAG,  user.getUid());
                                String u = user.getUid();
                                String d =  (String)document.getData().get("User UID");
                                if(document.getData().get("Voornaam").toString().length() > 0){
                                    users.add(document.getData().get("Voornaam").toString());
                                    UIDs.add(d);
                                }
                                if(u.equals(d)){
                                    Log.d(TAG, "Inside if");
                                    SetName(document.getData());
                                }
                            }
                            setSpinner();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void MakeGroup(){
        ArrayList<String> participants = new ArrayList<String>();
        participants.add(user.getUid());
        for (int i = 0; i < (int)mainSpinner.dropdown.getSelectedItem(); i++) {
            if (spinners.get(i).dropdown.getSelectedItem() != "Gebruiker"){
                participants.add(UIDs.get(spinners.get(i).dropdown.getSelectedItemPosition()));
            }
        }

        Map<String, Object> session = new HashMap<>();
        session.put("Leader Sid", user.getUid());
        session.put("Participant Sid", participants);
        session.put("round Number", 0);
        session.put("Group Name", groupName.getText().toString());

        fireStoreDatabase.collection("Sessions")
                .add(session)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance ("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app");
                        DatabaseReference myRef = database.getReference(documentReference.getId());

                        myRef.setValue(0);
                        ToHome();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    private void ToHome(){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}
