package com.sashavarlamov.soldier_android;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AdminGameActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_game);
    }

    public void endGame(View view){
        SocketUtil.endGame();
        System.out.println("The game has been ended...");
    }

}
