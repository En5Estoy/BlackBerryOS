package com.speryans.saldobus.classes;

import com.speryans.saldobus.framework.rpc.json.me.JSONObject;

import net.rim.device.api.ui.Manager;

public interface ButtonsListener {
	public void onClicked( Manager view );
	public void onClicked( int index, JSONObject data );
	public void onClicked( JSONObject data );
}
