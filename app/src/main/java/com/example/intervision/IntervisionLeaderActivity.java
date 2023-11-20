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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class IntervisionLeaderActivity extends AppCompatActivity {
    protected String sessionID = "1";
    protected ProgressBar progresbar;
    protected TextView roundText,headerText,statusText;
    protected View special;
    protected String partisipantsIdS[];
    protected final static int ROUNDNUMBERS = 5;
    protected IntervisionManager intervisionManager;
    protected IntervisionRound intervisionRounds[];
    protected int currentRound;
    protected FirebaseDatabase database;
    protected DatabaseReference myRef;
    private static final String TAG = "IntervisionLeaderActivi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionID = getIntent().getExtras().getString("SessionID");
        init();
    }
    protected void init(){
        InitConnection();
        InitLayout();
        currentRound = 0;
        FillContent();
        intervisionManager = new IntervisionManager(intervisionRounds, partisipantsIdS, ROUNDNUMBERS);
        ((ViewGroup) special).addView(intervisionRounds[0].roundSpecific);
        InitButtons();
        InitProgressButton();
        ChangeRound(intervisionRounds[currentRound].roundTitle,
                intervisionRounds[currentRound].round,
                "statusText",
                intervisionRounds[currentRound].roundSpecific);
    }
    protected void InitConnection(){
        database = FirebaseDatabase.getInstance ("https://intervision-1be7c-default-rtdb.europe-west1.firebasedatabase.app");
        myRef = database.getReference(sessionID);

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

    protected class IntervisionManager{
        IntervisionRound[] intervisionRounds;
        int rounds;
        private String[] participentIdS;

        public IntervisionManager(IntervisionRound[] intervisionRounds, String[] participentIdS, int rounds) {
            this.intervisionRounds = intervisionRounds;
            this.rounds = rounds;
            this.participentIdS = participentIdS;
        }
    }

    protected void InitLayout(){
        setContentView(R.layout.activity_intervision_leaderview);
        roundText = findViewById(R.id.round_textView_intervision_leaderView);
        headerText = findViewById(R.id.header_intervision_leaderView);
        statusText = findViewById(R.id.status_text_intervision_leaderView);
        progresbar = findViewById(R.id.progressBar_intervision_leaderview);
        special = findViewById(R.id.special_layout_intervision_leaderView);
    }


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
                LayoutInflater.from(special.getContext()).inflate(R.layout.eleborate_chose_item,
                        (ViewGroup) special,
                        false),
                LayoutInflater.from(special.getContext()).inflate(R.layout.single_image_item,
                        (ViewGroup) special,
                        false),
        };
        intervisionRounds = new IntervisionRound[ROUNDNUMBERS];
        for (int i = 0; i < ROUNDNUMBERS; i++) {
            intervisionRounds[i] =  new IntervisionRound(headers[i],
                    i + 1,
                    Specials[i]
            );
        }
    }

    protected void InitProgressButton(){
        Handler hdlr = new Handler();
        new Thread(new Runnable() {
            int i = 0;
            public void run() {
                while (i < 100) {
                    i += 1;
                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            progresbar.setProgress(i);
                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    protected void InitButtons(){
        Button nextRoundButton = (Button) findViewById(R.id.next_button_intervision_leaderView);
        nextRoundButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                currentRound++;
                myRef.setValue(currentRound);
                ChangeRound(intervisionRounds[currentRound].roundTitle,
                        intervisionRounds[currentRound].round,
                        "statusText",
                        intervisionRounds[currentRound].roundSpecific);
            }
        });
        Button previousRoundButton = (Button) findViewById(R.id.back_intervision_leaderView);
        previousRoundButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                currentRound--;
                myRef.setValue(currentRound);
                ChangeRound(intervisionRounds[currentRound].roundTitle,
                        intervisionRounds[currentRound].round,
                        "statusText",
                        intervisionRounds[currentRound].roundSpecific);
            }
        });
    }

    protected void ChangeRound(String roundTitle, int roundNumber, String statusText, View special){
        roundText.setText("Ronder " + roundNumber +" van 5");
        headerText.setText(roundTitle);
        ((ViewGroup) this.special).removeAllViews();
        ((ViewGroup) this.special).addView(special);
    }


    protected class IntervisionRound{
        protected String roundTitle;
        protected int round;
        protected View roundSpecific;
        protected String participantTurn;
        protected String getParticipantTurn() {
            return participantTurn;
        }
        protected void setParticipantTurn(String participantTurn) {
            this.participantTurn = participantTurn;
        }
        protected IntervisionRound(String roundTitle, int round, View roundSpecific) {
            this.roundTitle = roundTitle;
            this.round = round;
            this.roundSpecific = roundSpecific;
        }

    }
}
