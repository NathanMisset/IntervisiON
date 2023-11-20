package com.example.intervision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TestConnectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);

        Button joinLeaderButton = (Button) findViewById(R.id.leader_button_test_connection);
        joinLeaderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                launchIntervisionLeader();
            }
        });

        Button joinParticipantButton = (Button) findViewById(R.id.participant_button_test_connection);
        joinParticipantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                launchIntervision();
            }
        });
    }

    private void launchIntervisionLeader() {
        //Launch Image activity
        Intent i = new Intent(this, IntervisionLeaderActivity.class);
        startActivity(i);
    }
    private void launchIntervision() {
        //Launch Image activity
        Intent i = new Intent(this, IntervisionActivity.class);
        startActivity(i);
    }
}
