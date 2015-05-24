package com.sashavarlamov.soldier_android;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketUtil {
    private static Socket mSocket;
    private static boolean connected = false;

    public static void connectSocket() {
        try {
            mSocket = IO.socket("http://hollinsky.com:6969");
            mSocket.connect();
            connected = true;
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

	public static void onGameCreated(Emitter.Listener el){
		if(el == null)
			return;
		mSocket.on("gameCreated", el);
	}

	public static void offGameCreated(Emitter.Listener el){
		mSocket.off("gameCreated", el);
	}

	public static void createGame(String gameName, String gamePassword){
		mSocket.emit("createGame", gameName, gamePassword);
	}

	public static void onGameJoined(Emitter.Listener el){
		mSocket.on("gameJoined", el);
	}

	public static void offGameJoined(Emitter.Listener el){
		mSocket.off("gameJoined", el);
	}

	public static void onPlayerJoined(Emitter.Listener el){
		mSocket.on("playerJoined", el);
	}

	public static void offPlayerJoined(Emitter.Listener el){
		mSocket.off("playerJoined", el);
	}

	public static void onJoinGameError(Emitter.Listener el){
		mSocket.on("joinGameError", el);
	}

	public static void offJoinGameError(Emitter.Listener el){
		mSocket.off("joinGameError", el);
	}

	public static void joinGame(String gameName, String gamePassword){
		mSocket.emit("joinGame", gameName, gamePassword);
	}

	public static void onTeamNameSet(Emitter.Listener el){
		mSocket.on("teamNameSet", el);
	}

	public static void offTeamNameSet(Emitter.Listener el){
		mSocket.off("teamNameSet", el);
	}

	public static void setTeamName(int team, String newName){
		mSocket.emit("setTeamName", team, newName);
	}

	public static void onTeamSwitched(Emitter.Listener el){
		mSocket.on("teamSwitched", el);
	}

	public static void offTeamSwitched(Emitter.Listener el){
		mSocket.off("teamSwitched", el);
	}

	public static void switchTeams(int newTeam){
		mSocket.emit("switchTeams", newTeam);
	}

	public static void onFlagCreated(Emitter.Listener el){
		mSocket.on("flagCreated", el);
	}

	public static void offFlagCreated(Emitter.Listener el){
		mSocket.off("flagCreated", el);
	}

	public static void createFlag(int team, LocationObject location){
		mSocket.emit("createFlag", team, location);
	}

	public static void onFlagDeleted(Emitter.Listener el){
		mSocket.on("flagDeleted", el);
	}

	public static void offFlagDeleted(Emitter.Listener el){
		mSocket.off("flagDeleted", el);
	}

	public static void deleteFlag(int team, String flagID){
		mSocket.emit("deleteFlag", team, flagID);
	}

	public static void onLocationUpdated(Emitter.Listener el){
		mSocket.on("locationUpdated", el);
	}

	public static void offLocationUpdated(Emitter.Listener el){
		mSocket.off("locationUpdated", el);
	}

	public static void updateLocation(LocationObject location){
		mSocket.emit("updateLocation", location);
	}

	public static void onGameStartingIn(Emitter.Listener el){
		mSocket.on("gameStartingIn", el);
	}

	public static void offGameStartingIn(Emitter.Listener el){
		mSocket.off("gameStartingIn", el);
	}

	public static void onGameStart(Emitter.Listener el){
		mSocket.on("gameStart", el);
	}

	public static void offGameStart(Emitter.Listener el){
		mSocket.off("gameStart", el);
	}

	public static void startGame(){
		mSocket.emit("startGame");
	}

	public static void onGameWin(Emitter.Listener el){
		mSocket.on("gameWin", el);
	}

	public static void offGameWin(Emitter.Listener el){
		mSocket.off("gameWin", el);
	}

	public static void onGameEnd(Emitter.Listener el){
		mSocket.on("gameEnd", el);
	}

	public static void offGameEnd(Emitter.Listener el){
		mSocket.off("gameEnd", el);
	}

	public static void endGame(){
		mSocket.emit("endGame");
	}

	public static void onTeamOneUpdate(Emitter.Listener el){
		mSocket.on("teamOnePercentage", el);
	}

	public static void offTeamOneUpdate(Emitter.Listener el){
		mSocket.off("teamOnePercentage", el);
	}

	public static void onTeamTwoUpdate(Emitter.Listener el){
		mSocket.on("teamTwoPercentage", el);
	}

	public static void offTeamTwoUpdate(Emitter.Listener el){
		mSocket.off("teamTwoPercentage", el);
	}
}
