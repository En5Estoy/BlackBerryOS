package com.speryans.saldobus.framework.classes;

import com.speryans.saldobus.framework.rpc.json.me.JSONException;
import com.speryans.saldobus.framework.rpc.json.me.JSONObject;
import com.speryans.saldobus.framework.tools.Log;

public class KeySaver {
	
	KeySaver() {}
	
	public static void setKey( String key, String value ) {
		try {
			JSONObject obj = Tools.readKeyStore();
			if( obj == null ) {
				obj = new JSONObject();
			}
			obj.put(key, value);
			
			Tools.writeKeyStore( obj.toString() );
		} catch (JSONException e) {
			Log.error("Error on KeyStore: " + e.toString() );
		}
	}
	
	public static Object getKey( String key ) {
		try {
			JSONObject obj = Tools.readKeyStore();
			if( obj == null ) {
				KeySaver.setKey(key, "");
				
				return "";
			}
			
			return obj.opt( key );
		} catch (Exception e) {
			Log.error("Error on KeyStore: " + e.toString() );
			KeySaver.setKey(key, "");
			
			return "";
		}
	}
}
