package com.sashavarlamov.soldier_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.api.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


/**
 * A login screen that offers login via username.
 */
public class LoginActivity extends Activity {

    private EditText mUsernameView;

    private String mUsername;

    private LoginActivity me = this;

    {
        SocketUtil.connectSocket();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mSocket.emit("setName", "Sasha");
        //System.out.println("Just sent a message");
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username_input);
        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        SocketUtil.onSetName(onLogin);
    }
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketUtil.offSetName(onLogin);
    }
    */

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        }

        mUsername = username;

        // perform the user login attempt.
        SocketUtil.setName(username);
        System.out.println("Attempted Login");
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String name = (String) args[0];
            /*JSONObject data = (JSONObject) args[0];

            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }
            */
            Intent intent = new Intent(me, DecisionActivity.class);
            intent.putExtra("username", name);
            SocketUtil.offSetName(onLogin);
            startActivity(intent);
            System.out.println("I have logged in with name, " + name);
        }
    };
}
