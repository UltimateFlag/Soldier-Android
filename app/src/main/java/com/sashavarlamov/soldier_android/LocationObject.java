package com.sashavarlamov.soldier_android;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationObject extends JSONObject {

	public void LocationObject(float latitude, float longitude, float accuracy) {
		try {
			this.put("latitude", latitude);
			this.put("longitude", longitude);
			this.put("accuracy", accuracy);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
