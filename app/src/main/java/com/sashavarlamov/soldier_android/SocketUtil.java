package com.sashavarlamov.soldier_android;

import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sashaadmin on 5/23/15.
 */
public class SocketUtil {
    private static Socket mSocket;
    private static boolean connected = false;

    public static void connectSocket() {
        try {
            //mSocket = IO.socket(((Integer)R.string.socket_host).toString());
            mSocket = IO.socket("http://hollinsky.com:6969");
            mSocket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onSetName(Emitter.Listener el){
        mSocket.on("nameSet", el);
    }

    public static void offSetName(Emitter.Listener el){
        mSocket.off("nameSet", el);
    }

    public static void setName(String username){
        mSocket.emit("setName", username);
    }

    public static boolean isConnected(){
        return connected;
    }
}
