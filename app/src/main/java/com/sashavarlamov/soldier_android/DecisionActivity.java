package com.sashavarlamov.soldier_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DecisionActivity extends Activity {
    private String username = null;

    private DecisionActivity me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Decision maker has been opened");
        setContentView(R.layout.activity_decision);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        SocketUtil.onGameCreated(onGameCreated);
        SocketUtil.onJoinGameError(onJoinGameError);
    }

    public void joinGame(View view){
        System.out.println("joining game");
        SocketUtil.onGameJoined(onGameJoined);
        SocketUtil.joinGame(((EditText) findViewById(R.id.game_name_input)).getText().toString(), ((EditText) findViewById(R.id.game_password_input)).getText().toString());
        System.out.println("sent request to join");
    }

    public void startNewGame(View view){
        SocketUtil.onGameJoined(onGameJoinedAdmin);
        SocketUtil.createGame(((EditText) findViewById(R.id.game_name_input)).getText().toString(), ((EditText) findViewById(R.id.game_password_input)).getText().toString());
    }

    private Emitter.Listener onGameCreated = new Emitter.Listener() {
        @Override
        public void call(Object... args){
            JSONObject game = (JSONObject) args[0];
            try {
                String gameid = game.getString("name");
                String gamepass = game.getString("password");
                SocketUtil.joinGame(gameid, gamepass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onGameJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("joined game");
            JSONObject game = (JSONObject) args[0];
            try {
                JSONArray teams = game.getJSONArray("teams");
                System.out.println(username + " is starting a new game");
                Intent intent = new Intent(me, LobbyActivity.class);
                intent.putExtra("gameName", game.getString("name"));
                intent.putExtra("teamOneName", (String) ((JSONObject) teams.get(0)).get("name"));
                intent.putExtra("teamTwoName", (String) ((JSONObject) teams.get(1)).get("name"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SocketUtil.offGameJoined(onGameJoined);
            SocketUtil.offJoinGameError(onJoinGameError);
        }
    };

    private Emitter.Listener onGameJoinedAdmin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject game = (JSONObject) args[0];
            try {
                JSONArray teams = game.getJSONArray("teams");
                System.out.println(username + " is starting a new game");
                Intent intent = new Intent(me, AdminLobbyActivity.class);
                intent.putExtra("gameName", game.getString("name"));
                intent.putExtra("teamOneName", (String) ((JSONObject) teams.get(0)).get("name"));
                intent.putExtra("teamTwoName", (String) ((JSONObject) teams.get(1)).get("name"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SocketUtil.onGameJoined(onGameJoinedAdmin);
            SocketUtil.onJoinGameError(onJoinGameError);
        }
    };

    private Emitter.Listener onJoinGameError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println((String) args[0]);
        }
    };
}
