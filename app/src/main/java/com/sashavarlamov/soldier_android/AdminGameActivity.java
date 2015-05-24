package com.sashavarlamov.soldier_android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;


public class AdminGameActivity extends ActionBarActivity implements OnMapReadyCallback {
    private ProgressBar teamOneBar = null;
    private ProgressBar teamTwoBar = null;
    private boolean teamOneIsOpposing = false;
    private Intent intent = null;
    private MapFragment map = null;
    private boolean firstUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_admin_game);
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
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.game_map);
        map.getMapAsync(this);
        SocketUtil.onTeamOneUpdate(teamOneUpd);
        SocketUtil.onTeamTwoUpdate(teamTwoUpd);
        System.out.println("Added Listeners");
    }

    private Emitter.Listener teamOneUpd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("One Method Call");
            updateTeamOnePercentage((Float) args[0]);
        }
    };

    private Emitter.Listener teamTwoUpd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("Two Method Call");
            updateTeamTwoPercentage((Float) args[0]);
        }
    };

    public void endGame(View view){
        SocketUtil.endGame();
        Intent in = new Intent(this, DecisionActivity.class);
        in.putExtra("username", intent.getStringExtra("username"));
        startActivity(in);
        System.out.println("The game has been ended...");
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
        System.out.println("Percentage for Team One is " + per);
        teamOneBar.setProgress(Math.round(per));
    }

    private void updateTeamTwoPercentage(float per){
        // TODO: Use this as a callback to update the percentage from socket
        System.out.println("Percentage for Team One is " + per);
        teamOneBar.setProgress(Math.round(per));
    }

    private void sendLocUpdate(Location l){
        System.out.println("Sending location Update");
        SocketUtil.updateLocation(new LocationObject(l.getLatitude(), l.getLongitude(), l.getAccuracy()));
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
                    firstUpdate = false;
                }
                sendLocUpdate(arg0);
            }
        });
    }

}
