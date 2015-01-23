package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class BackgroundHorizontalFieldManager extends HorizontalFieldManager {
	private int color;
	
	public BackgroundHorizontalFieldManager( int color ) {
		super();
		
		this.color = color;
	}
	
	public BackgroundHorizontalFieldManager( int color, long style ) {
		super( style );
		
		this.color = color;
	}
	
	public void paintBackground(Graphics g) {
		g.setBackgroundColor(color);
		g.clear();
	}
}
