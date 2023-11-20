package com.example.intervision;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IntervisionActivity extends IntervisionLeaderActivity {
    private static final String TAG = "IntervisionActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void init(){
        InitConnection();
        InitLayout();
        currentRound = 0;
        FillContent();
        ((ViewGroup) special).addView(intervisionRounds[0].roundSpecific);
        InitProgressButton();
    }
    @Override
    protected void InitLayout(){
        setContentView(R.layout.activity_intervision);
        roundText = findViewById(R.id.round_text_intevision);
        headerText = findViewById(R.id.header_intervision);
        statusText = findViewById(R.id.status_intervision);
        progresbar = findViewById(R.id.progressBar_intervision);
        special = findViewById(R.id.special_layout_intervision);
    }
    @Override
    protected void InitConnection(){
        // Write a message to the database
        database = database = FirebaseDatabase.getInstance ("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app");

        myRef = database.getReference(sessionID);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "before get number");
                Long value = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Value = " + value);

                currentRound = value.intValue();
                ChangeRound(intervisionRounds[currentRound].roundTitle,
                        intervisionRounds[currentRound].round,
                        "statusText",
                        intervisionRounds[currentRound].roundSpecific);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void FillContent(){
        String[] headers = {getString(R.string.header_1_intervision),
                getString(R.string.header_2_intervision),
                getString(R.string.header_3_intervision),
                getString(R.string.header_4_intervision),
                getString(R.string.header_5_intervision)};

        View[] Specials = {
                LayoutInflater.from(special.getContext()).inflate(R.layout.rounds_explained_item,
                        (ViewGroup) special,
                        false),
                LayoutInflater.from(special.getContext()).inflate(R.layout.thesis_item,
                        (ViewGroup) special,
                        false),
                LayoutInflater.from(special.getContext()).inflate(R.layout.single_image_item,
                        (ViewGroup) special,
                        false),
                LayoutInflater.from(special.getContext()).inflate(R.layout.single_image_item,
                        (ViewGroup) special,
                        false),
                LayoutInflater.from(special.getContext()).inflate(R.layout.single_image_item,
                        (ViewGroup) special,
                        false),
        };
        SetSingleItemView[] images = {
                new SetSingleItemView(R.drawable.discussing_image_128x128, Specials[2]),
                new SetSingleItemView(R.drawable.chat_image_128x128, Specials[3]),
                new SetSingleItemView(R.drawable.handshake_image_128x128, Specials[4]),
        };


         intervisionRounds = new IntervisionRound[ROUNDNUMBERS];
        for (int i = 0; i < ROUNDNUMBERS; i++) {
            intervisionRounds[i] =  new IntervisionRound(headers[i],
                    i + 1,
                    Specials[i]
            );
        }
    }
}
