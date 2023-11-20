package com.example.intervision;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int loginType = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        switch (loginType){
            case 0:
                launchRegister();
                break;
            case 1:
                launchTutorial();
                break;
            case 2:
                launchLogin();
                break;
            case 3:
                launchImage();
                break;
            case 4:
                launchIntervision();
                break;
            case 5:
                launchMakeGroup();
                break;
            case 6:
                launchHome();
                break;
        }
    }

    private void launchImage() {
        //Launch Image activity
        Intent i = new Intent(this, ImageFromUserActivity.class);
        startActivity(i);
    }

    public void launchRegister(){
        //Launch register activity
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
    public void launchTutorial(){
        //Launch Tutorial activity
        Intent i = new Intent(this, TutorialActivity.class);
        startActivity(i);
    }
    public void launchLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void launchIntervision(){
        Intent i = new Intent(this, TestConnectionActivity.class);
        startActivity(i);
    }
    public void launchMakeGroup(){
        Intent i = new Intent(this, MakeGroupActivity.class);
        startActivity(i);
    }
    public void launchHome(){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}



