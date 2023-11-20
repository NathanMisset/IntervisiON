package com.example.intervision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "Reset Password";
    private FirebaseAuth auth;
    EditText emailText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        auth = FirebaseAuth.getInstance();

//      Button

        sendButton = findViewById(R.id.send_button_reset_password);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the SendButton");
                ResetPassword("nl");
            }
        });
        Button backButton = findViewById(R.id.back_button_reset_password);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the backButton");
                ToLogin();
            }
        });
        emailText = findViewById(R.id.email_reset_password);
//        SendButton.setEnabled(false);
//        SendButton.setEnabled(false);
//        emailText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                SendButton.setEnabled(true);
//                Log.d(TAG, "onEditorAction: SendButton enabled");
//                return true;
//            }
//        });
    }

    private void ToLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    protected void ResetPassword(String languageCode){
        auth.setLanguageCode(languageCode);
        auth.sendPasswordResetEmail(emailText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent to: " + emailText.getText().toString());
                            MailSend();
                        }
                    }
                });
    }
    protected void MailSend(){
        Button button = findViewById(R.id.back_button_reset_password);
        button.setBackgroundColor(getResources().getColor(R.color.background_black));
        button.setTextColor(getResources().getColor(R.color.yellow));
        sendButton.setTextColor(getResources().getColor(R.color.gray));
        sendButton.setBackgroundColor(getResources().getColor(R.color.gray));
    }
}
