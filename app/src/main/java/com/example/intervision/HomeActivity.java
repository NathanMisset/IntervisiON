package com.example.intervision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Home";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) reload();
        setContentView(R.layout.acitivity_home);
        getData();


        ImageButton toSettingsButton = (ImageButton) findViewById(R.id.to_settings_home);
        toSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                ToSettings();
            }
        });

        ImageButton toGroupAssemblyButton = (ImageButton) findViewById(R.id.Intervision_button_home);
        toGroupAssemblyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped " + toGroupAssemblyButton);
                ToGroupAssembly();
            }
        });

        Button toMakeGroup = (Button) findViewById(R.id.make_group_button_home);
        toMakeGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped " + toMakeGroup);
                ToMakeGroup();
            }
        });
    }
    void SetName(Map<String, Object> data){
        TextView greeting = findViewById(R.id.greeting_home);
        greeting.setText("Goedemiddag,\n " + data.get("Voornaam"));
    }
    private void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_data")
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

                                if(u.equals(d)){
                                    Log.d(TAG, "Inside if");
                                    SetName(document.getData());
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void ToSettings(){
        Intent i = new Intent(this, SettingActivity.class);
        startActivity(i);
    }
    private void ToGroupAssembly(){
        Intent i = new Intent(this, GroupAssemblyActivity.class);
        startActivity(i);
    }
    private void ToMakeGroup(){
        Intent i = new Intent(this, MakeGroupActivity.class);
        startActivity(i);
    }
    private void reload() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

}