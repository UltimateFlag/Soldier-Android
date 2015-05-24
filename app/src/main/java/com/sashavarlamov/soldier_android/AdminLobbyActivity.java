package com.sashavarlamov.soldier_android;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class AdminLobbyActivity extends LobbyActivity implements OnMapReadyCallback {
    private Button startGameButton = null;
    private Intent intent = null;
    private MapFragment map = null;
    private Marker teamOnePin = null;
    private Marker teamTwoPin = null;
    private boolean firstUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_admin_lobby);
        startGameButton = (Button)findViewById(R.id.start_game_button);
        // TODO: Get actual data from the intent
        setPlayerCount(0);
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        System.out.println(map);
        initTeamNames("Team One", "Team Two");
    }

    public static void startGame(View view){
        // TODO: Start a new game
        System.out.println("Starting game...");
    }

    public void setPlayerCount(int i){
        Resources res = getResources();
        startGameButton.setText(res.getQuantityString(R.plurals.start_with_player_count, i, i));
        if(i == 0){
            startGameButton.setClickable(false);
        } else{
            startGameButton.setClickable(true);
        }
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
        TextView teamOneB = (TextView)findViewById(R.id.team_one_text);
        teamOneB.setText(t1);
        TextView teamTwoB = (TextView)findViewById(R.id.team_two_text);
        teamTwoB.setText(t2);
    }

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
        System.out.println("Ended the Game");
    }
}
