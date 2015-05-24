package com.sashavarlamov.soldier_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DecisionActivity extends Activity {
    private String username = null;

    private DecisionActivity me = this;

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
                //intent.putExtra("spectatorCount", ((JSONObject) game.get("spectators")).length());
                intent.putExtra("teamOneName", (String) ((JSONObject) teams.get(0)).get("name"));
                //intent.putExtra("teamOneCount", (((JSONObject) ((JSONObject) teams.get(0)).get("players")).length()));
                intent.putExtra("teamTwoName", (String) ((JSONObject) teams.get(1)).get("name"));
                //intent.putExtra("teamTwoCount", (((JSONObject) ((JSONObject) teams.get(1)).get("players")).length()));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SocketUtil.offGameJoined(onGameJoinedAdmin);
            SocketUtil.offJoinGameError(onJoinGameError);
        }
    };

    private Emitter.Listener onJoinGameError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println((String) args[0]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Decision maker has been opened");
        setContentView(R.layout.activity_decision);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
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
        SocketUtil.joinGame(((EditText) findViewById(R.id.game_name_input)).getText().toString(), ((EditText) findViewById(R.id.game_password_input)).getText().toString());
    }
}
