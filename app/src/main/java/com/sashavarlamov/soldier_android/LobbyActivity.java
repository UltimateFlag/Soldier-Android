package com.sashavarlamov.soldier_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;

public class LobbyActivity extends ActionBarActivity {
    private String username = null;
    private String teamOneName = null;
    private String teamTwoName = null;
    private int spectators = 0;
    private int teamOneCount = 0;
    private int teamTwoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
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
        ((Button)findViewById(R.id.choose_team_one)).setText(teamOneName + " (" + teamOneCount + ")");
        ((Button)findViewById(R.id.choose_team_two)).setText(teamTwoName + " (" + teamTwoCount + ")");
    }

    public void teamOneSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamOneName") + " was selected");
        SocketUtil.switchTeams(0);
    }

    public void teamTwoSelected(View view){
        // TODO: Select a team
        System.out.println(getIntent().getStringExtra("teamTwoName") + " was selected");
        SocketUtil.switchTeams(1);
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
