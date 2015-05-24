package com.sashavarlamov.soldier_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.nkzawa.emitter.Emitter;

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
        SocketUtil.onTeamSwitched(onTeamSwitched);
        SocketUtil.onPlayerJoined(onPlayerJoined);
        spectators = intent.getIntExtra("spectatorCount", 0);
        ((TextView)findViewById(R.id.undecided_players_text)).setText((spectators == 0) ? "" : spectators + " undecided player" + (spectators > 1 || spectators < 1 ? "s" : ""));
        teamOneName = intent.getStringExtra("teamOneName");
        teamOneCount = intent.getIntExtra("teamOneCount", 0);
        teamTwoName = intent.getStringExtra("teamTwoName");
        teamTwoCount = intent.getIntExtra("teamTwoCount", 0);
        this.setTitle(intent.getStringExtra("gameName") + " - Lobby");
        ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName") + " (" + teamOneCount + ")");
        ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName") + " (" + teamOneCount + ")");
    }

    public void teamOneSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamOneName") + " was selected");
        SocketUtil.switchTeams(0);
        if(!((ToggleButton)findViewById(R.id.choose_team_one)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_one)).setChecked(true);
        if(((ToggleButton)findViewById(R.id.choose_team_two)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_two)).setChecked(false);
        ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName") + " (" + teamOneCount + ")");
        ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName") + " (" + teamOneCount + ")");
    }

    public void teamTwoSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamTwoName") + " was selected");
        SocketUtil.switchTeams(1);
        if(((ToggleButton)findViewById(R.id.choose_team_one)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_one)).setChecked(false);
        if(!((ToggleButton)findViewById(R.id.choose_team_two)).isChecked())
            ((ToggleButton)findViewById(R.id.choose_team_two)).setChecked(true);
        ((ToggleButton)findViewById(R.id.choose_team_one)).setText(intent.getStringExtra("teamOneName") + " (" + teamOneCount + ")");
        ((ToggleButton)findViewById(R.id.choose_team_two)).setText(intent.getStringExtra("teamTwoName") + " (" + teamOneCount + ")");
    }

    private Emitter.Listener onTeamSwitched = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            switch((int) args[1])
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
            switch((int) args[1])
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
        }
    };

    private Emitter.Listener onPlayerJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            spectators++;
        }
    };

    public void goToGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        // TODO: pass the right variables
        intent.putExtra("teamOneName", teamOneName);
        intent.putExtra("teamTwoName", teamTwoName);
        startActivity(intent);
    }
}
