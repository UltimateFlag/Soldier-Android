package com.sashavarlamov.soldier_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DecisionActivity extends Activity {
    private String username = "NULL USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Decision maker has been opened");
        setContentView(R.layout.activity_decision);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    public void joinGame(View view){
        // TODO: join the game, and setup all of the intent extras
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra("teamOneName", "FAKE TEAM ONE");
        intent.putExtra("teamTwoName", "FAKE TEAM TWO");
        startActivity(intent);
        System.out.println(username + " is joining a game");
    }

    public void startNewGame(View view){
        // TODO: start a new game
        System.out.println(username + " is starting a new game");
        Intent intent = new Intent(this, AdminLobbyActivity.class);
        intent.putExtra("teamOneName", "FAKE TEAM ONE");
        intent.putExtra("teamTwoName", "FAKE TEAM TWO");
        startActivity(intent);
    }
}
