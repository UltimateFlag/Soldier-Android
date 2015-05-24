package com.sashavarlamov.soldier_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

public class LobbyActivity extends ActionBarActivity {
    private String username = null;
    private String teamOneName = null;
    private String teamTwoName = null;
    private int spectators = 0;
    private int teamOneCount = 0;
    private int teamTwoCount = 0;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        username = intent.getStringExtra(username);
        setContentView(R.layout.activity_lobby);
        SocketUtil.onGameStart(onGameStart);
        SocketUtil.onGameStartingIn(secondPassed);
        //SocketUtil.onTeamSwitched(onTeamSwitched);
        //SocketUtil.onPlayerJoined(onPlayerJoined);
        spectators = intent.getIntExtra("spectatorCount", 0);
        ((TextView)findViewById(R.id.undecided_players_text)).setText((spectators == 0) ? "" : spectators + " undecided player" + (spectators > 1 || spectators < 1 ? "s" : ""));
        teamOneName = intent.getStringExtra("teamOneName");
        teamOneCount = intent.getIntExtra("teamOneCount", 0);
        teamTwoName = intent.getStringExtra("teamTwoName");
        teamTwoCount = intent.getIntExtra("teamTwoCount", 0);
        this.setTitle(intent.getStringExtra("gameName") + " - Lobby");
        ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName"));
        ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName"));
    }

    public void teamOneSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamOneName") + " was selected");
        /*if (((ToggleButton) findViewById(R.id.choose_team_one)).isChecked()){
            ((ToggleButton) findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName") + " (" + teamOneCount + ")");
            ((ToggleButton) findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName") + " (" + teamTwoCount + ")");
            return;
        }*/
        SocketUtil.switchTeams(0);
        if(!((ToggleButton)findViewById(R.id.choose_team_one)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_one)).setChecked(true);
        if(((ToggleButton)findViewById(R.id.choose_team_two)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_two)).setChecked(false);
        ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName"));
        ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName"));

    }

    public void teamTwoSelected(View view) {
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamTwoName") + " was selected");
        /*if (((ToggleButton) findViewById(R.id.choose_team_two)).isChecked()){
            ((ToggleButton) findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName") + " (" + teamOneCount + ")");
            ((ToggleButton) findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName") + " (" + teamTwoCount + ")");
            return;
        }*/
        SocketUtil.switchTeams(1);
        if(((ToggleButton)findViewById(R.id.choose_team_one)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_one)).setChecked(false);
        if(!((ToggleButton)findViewById(R.id.choose_team_two)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_two)).setChecked(true);
        ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName"));
        ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName"));
    }
    /*
    private Emitter.Listener onTeamSwitched = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                switch(((JSONObject) args[1]).getInt("side"))
				{
					case -1:
						spectators++;
						break;
					case 0:
						teamOneCount++;
						break;
					case 1:
						teamTwoCount++;
						break;
				}
                switch((int) args[2])
                {
                    case -1:
                        spectators--;
                        break;
                    case 0:
                        teamOneCount--;
                        break;
                    case 1:
                        teamTwoCount--;
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                ((TextView)findViewById(R.id.undecided_players_text)).setText((spectators == 0) ? "" : spectators + " undecided player" + (spectators > 1 || spectators < 1 ? "s" : ""));
                ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName") + " (" + teamOneCount + ")");
                ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName") + " (" + teamOneCount + ")");
            }
        }
    };

    private Emitter.Listener onPlayerJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            spectators++;
            ((TextView)findViewById(R.id.undecided_players_text)).setText((spectators == 0) ? "" : spectators + " undecided player" + (spectators > 1 || spectators < 1 ? "s" : ""));
        }
    };
    */

    private Emitter.Listener onGameStart = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            goToGame();
        }
    };

    private Emitter.Listener secondPassed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            flashSecondsLeft((Integer)args[0]);
        }
    };

    public void goToGame(){
        Intent intent = new Intent(this,GameActivity.class);
        // TODO: pass the right variables
        intent.putExtra("teamOneName", teamOneName);
        intent.putExtra("teamTwoName", teamTwoName);
        startActivity(intent);
    }

    private void flashSecondsLeft(int sec){
        Context context = getApplicationContext();
        CharSequence text = sec + " Seconds Until Game Start!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
