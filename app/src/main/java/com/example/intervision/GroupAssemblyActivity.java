package com.example.intervision;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class GroupAssemblyActivity  extends AppCompatActivity {
    private static final String TAG = "GroupAssemblyActivity";
    protected ViewGroup scrollLayout;
    protected ArrayList<GroupPreviewItem> availableGroups;
    protected FirebaseAuth user;
    protected FirebaseFirestore dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_assembly);

        user =  FirebaseAuth.getInstance();
        scrollLayout = findViewById(R.id.scroll_layout_child_group_assembly);
        availableGroups = new ArrayList<GroupPreviewItem>();
        getData();
    }
    protected void getData(){
        dataBase = FirebaseFirestore.getInstance();
        dataBase.collection("Sessions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                AddGroup(document.getData(), document.getId());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    protected void AddGroup(Map<String, Object> data, String docname){

        availableGroups.add(new GroupPreviewItem(data.get("Group Name").toString(),
                                                 (ArrayList<String>) data.get("Participant Sid"),
                                                 scrollLayout,
                                          this, dataBase, user, docname.toString(), data.get("Leader Sid").toString()));
    }
}
