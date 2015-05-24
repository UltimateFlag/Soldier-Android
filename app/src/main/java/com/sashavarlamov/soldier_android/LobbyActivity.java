package com.sashavarlamov.soldier_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class LobbyActivity extends ActionBarActivity {
    private ExpandableListView teamOneList = null;
    private ExpandableListView teamTwoList = null;
    private String username = "NULL USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra(username);
        setContentView(R.layout.activity_lobby);
        this.setTitle(intent.getStringExtra("gameName") + " - Lobby");
        ((TextView)findViewById(R.id.team_one_text)).setText(intent.getStringExtra("teamOneName"));
        ((TextView)findViewById(R.id.team_two_text)).setText(intent.getStringExtra("teamTwoName"));
    }

    public void teamOneSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamOneName") + " was selected");
    }

    public void teamTwoSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamTwoName") + " was selected");
    }

    public void goToGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        // TODO: pass the right variables
        intent.putExtra("teamOneName", "TEAM ONE");
        intent.putExtra("teamTwoName", "TEAM TWO");
        startActivity(intent);
    }
}
