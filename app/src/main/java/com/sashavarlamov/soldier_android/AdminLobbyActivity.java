package com.sashavarlamov.soldier_android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminLobbyActivity extends ActionBarActivity implements OnMapReadyCallback {
    private Button startGameButton = null;
    private Intent intent = null;
    private MapFragment map = null;
    private Marker teamOnePin = null;
    private Marker teamTwoPin = null;
    private boolean firstUpdate = true;
    private int playerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_admin_lobby);
        startGameButton = (Button)findViewById(R.id.start_game_button);
        // TODO: Get actual data from the intent
        //SocketUtil.onPlayerJoined(playerAddedListener);
        setPlayerCount();
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        System.out.println(map);
        initTeamNames(intent.getStringExtra("teamOneName"), intent.getStringExtra("teamTwoName"));
    }

    public void startGame(View view){
        // Init the flags
        SocketUtil.createFlag(0, new LocationObject(teamOnePin.getPosition().latitude, teamOnePin.getPosition().longitude, 1.0));
        SocketUtil.createFlag(1, new LocationObject(teamTwoPin.getPosition().latitude, teamTwoPin.getPosition().longitude, 1.0));

        // Start a new game
        SocketUtil.startGame();
        /*Intent in = new Intent(this, AdminGameActivity.class);
        // TODO: Put in the extra
        in.putExtra("teamOneName", getIntent().getStringExtra("teamOneName"));
        in.putExtra("teamTwoName", getIntent().getStringExtra("teamTwoName"));
        startActivity(in);*/
        SocketUtil.onGameStart(onGameStart);
        SocketUtil.onGameStartingIn(secondPassed);
        System.out.println("Starting game...");
    }

    public void setPlayerCount(){
        Resources res = getResources();
        startGameButton.setText("Start Game!");
        startGameButton.setClickable(true);
        //startGameButton.setText(res.getQuantityString(R.plurals.start_with_player_count, playerCount, playerCount));
        /*if(playerCount == 0){
            startGameButton.setClickable(false);
        } else{
            startGameButton.setClickable(true);
        }*/
    }

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
        Intent intent = new Intent(this,AdminGameActivity.class);
        // TODO: pass the right variables
        intent.putExtra("teamOneName", getIntent().getStringExtra("teamOneName"));
        intent.putExtra("teamTwoName", getIntent().getStringExtra("teamTwoName"));
        startActivity(intent);
        SocketUtil.switchTeams(0);
        SocketUtil.offGameStart(onGameStart);
        SocketUtil.offGameStartingIn(secondPassed);
    }

    private void flashSecondsLeft(int sec){
        /*
        Context context = getApplicationContext();
        CharSequence text = sec + " Seconds Until Game Start!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        */
        System.out.println(sec);
    }

    public void onMapReady(GoogleMap m) {
        System.out.println("Map is ready");
        map.getMap().setMyLocationEnabled(true);
        map.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getMap().setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                if (firstUpdate) {
                    LatLng cur_Latlng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    map.getMap().moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
                    map.getMap().animateCamera(CameraUpdateFactory.zoomTo(16));
                    initPins(cur_Latlng, "Team One", "Team Two");
                    firstUpdate = false;
                }

            }
        });

        //map.getMap().moveCamera(new CameraUpdate());
    }

    private void initPins(LatLng ll, String t1, String t2){
        LatLng llOne = new LatLng(ll.latitude + 0.001, ll.longitude);
        teamOnePin = this.map.getMap().addMarker(new MarkerOptions().position(llOne).draggable(true).title(t1));
        LatLng llTwo = new LatLng(ll.latitude - 0.001, ll.longitude);
        teamTwoPin = this.map.getMap().addMarker(new MarkerOptions().position(llTwo).draggable(true).title(t2));

    }

    private void initTeamNames(String t1, String t2){
        EditText teamOneB = (EditText)findViewById(R.id.team_one_text);
        teamOneB.setText(t1);
        EditText teamTwoB = (EditText)findViewById(R.id.team_two_text);
        teamTwoB.setText(t2);
    }
    /*
    private Emitter.Listener playerAddedListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            playerCount++;
            setPlayerCount();
        }
    };
    */
    @Override
    public void onBackPressed() {
        System.out.println("Going Back");
        cancelGame(null);
        Intent intent = new Intent(this, DecisionActivity.class);
        intent.putExtra("username", this.intent.getStringExtra("username"));
        startActivity(intent);
    }

    public void cancelGame(View view){
        // Cancel the game
        SocketUtil.endGame();
        Intent intent = new Intent(this, DecisionActivity.class);
        intent.putExtra("username", this.intent.getStringExtra("username"));
        startActivity(intent);
        System.out.println("Ended the Game");
    }
}
