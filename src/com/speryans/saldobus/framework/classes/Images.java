package com.speryans.saldobus.framework.classes;

import com.speryans.saldobus.framework.tools.Log;

import net.rim.device.api.ui.component.BitmapField;

public class Images {
	public String url;
	public String file;
	public BitmapField bitmap;
	public int width, height;
	public int scale;
	
	public Images( String u, BitmapField b, int w, int h, int sc ) {
		url = u;
		bitmap = b;
		width = w;
		height = h;
		scale = sc;
		
		String filename = Integer.toHexString( Math.abs( url.hashCode() ) );
		
		file =  Commons.directory + "cache/" + filename + ".png";
		Log.info(file);
	}
}
