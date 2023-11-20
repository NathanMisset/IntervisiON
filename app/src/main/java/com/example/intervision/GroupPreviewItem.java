package com.example.intervision;

import static androidx.core.content.ContentResolverCompat.query;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupPreviewItem{
    private static final String TAG = "GroupPreviewItem";
    public String SessionId;
    private String name;
    private TextView header;
    private Button button;
    private ArrayList<String> participantsSiD;
    private ArrayList<TextView> participantsTextView;
    private String leaderId;
    private Map<String, Object> dataSession;
    private Map<String, Object> dataUsers;
    private FirebaseFirestore dataBase;
    private FirebaseStorage storage;
    protected FirebaseAuth user;
    private Activity activity;

    public GroupPreviewItem(String name, ArrayList<String> participantsSiD,
                            View parent, Activity activity, FirebaseFirestore database ,
                            FirebaseAuth user, String SessionID, String leaderId) {
        this.name = name;
        storage = FirebaseStorage.getInstance();
        this.user = user;
        this.participantsSiD = participantsSiD;
        this.SessionId = SessionID;
        this.dataBase = database;
        this.leaderId = leaderId;
        this.activity = activity;
        Log.d(TAG, this.participantsSiD.toString());
        participantsTextView = new ArrayList<TextView>();
        View Layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_preview_item,
                (ViewGroup) parent,
                false);
        ((ViewGroup) parent).addView(Layout);
        getViews(Layout);
        getUserData();
        SetName();
        SetButton();

    }
    private void getViews(View layout){
        header = layout.findViewById(R.id.header_group_card_group_preview);

        button = layout.findViewById(R.id.join_group_group_preview);

        participantsTextView.add(layout.findViewById(R.id.user_and_icon_1_group_preview));
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_2_group_preview));
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_3_group_preview));
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_4_group_preview));
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_5_group_preview));
        participantsTextView.add(layout.findViewById(R.id.user_and_icon_6_group_preview));
        int participantN;
        if(participantsSiD == null){
            participantN = 0;
        } else{
            participantN = participantsSiD.size();
        }

        for (int i = 0; i < participantsTextView.size() ; i++) {
            participantsTextView.get(i).setText(null);
            participantsTextView.get(i).setCompoundDrawables(null,null,null,null);
        }
    }



    private void getUserData(){

        dataBase.collection("user_data")
                .whereIn("User UID", participantsSiD)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                SetParticipant(i ,document.getData());
                                i+=1;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    private void SetButton(){
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped " + button);
                CheckIfLeader();
            }
        });
    }
    private void SetName(){
        this.header.setText(name);
    }

    private void SetParticipant(int i, Map<String, Object> data){
        Log.d(TAG, data + " => ");
        participantsTextView.get(i).setText((String)data.get("Voornaam"));
        getImage(data.get("User UID").toString(),participantsTextView.get(i));
    }
    private void getImage(String Uid, TextView view) {
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("ProfilePictures/" + Uid);
        //Log.d(TAG, "pathReference " + pathReference);

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, false);
                view.setCompoundDrawablesWithIntrinsicBounds(null,new BitmapDrawable(view.getResources(),bitmap),null,null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }
    private void CheckIfLeader(){
        Log.d(TAG, "leaderId " + leaderId + " userid " + user.getUid().toString());
        if (user.getUid().toString().equals(leaderId)){
            ToIntervisionLeader();
        }else{
            ToIntervision();
        }
    }
    private void ToIntervision(){
        Intent i = new Intent(activity, IntervisionActivity.class);
        Log.d(TAG, "SessionId ");
        Log.d(TAG, "SessionId " + SessionId);
        i.putExtra("SessionID", SessionId);
        activity.startActivity(i);
    }
    private  void ToIntervisionLeader(){
        Intent i = new Intent(activity, IntervisionLeaderActivity.class);
        Log.d(TAG, "SessionId ");
        Log.d(TAG, "SessionId " + SessionId);
        i.putExtra("SessionID", SessionId);
        activity.startActivity(i);
    }

}
