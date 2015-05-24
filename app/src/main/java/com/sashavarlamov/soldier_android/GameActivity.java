package com.sashavarlamov.soldier_android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity {
    private ProgressBar teamOneBar = null;
    private ProgressBar teamTwoBar = null;
    private boolean teamOneIsOpposing = false;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_game);
        ((TextView)findViewById(R.id.team_one_text)).setText(intent.getStringExtra("teamOneName"));
        ((TextView)findViewById(R.id.team_two_text)).setText(intent.getStringExtra("teamTwoName"));
        teamOneBar = (ProgressBar) findViewById(R.id.team_one_bar);
        teamTwoBar = (ProgressBar)findViewById(R.id.team_two_bar);
        // TODO: Setup which team is mine, and which is opposing
        if(teamOneIsOpposing) {
            teamOneBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            teamTwoBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        } else {
            teamOneBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            teamTwoBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTeamOnePercentage(float per){
        // TODO: Use this as a callback to update the percentage from socket
        teamOneBar.setProgress(Math.round(per));
    }

    private void updateTeamTwoPercentage(float per){
        // TODO: Use this as a callback to update the percentage from socket
        teamOneBar.setProgress(Math.round(per));
    }
}
