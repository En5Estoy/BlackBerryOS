package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;

public class FocusableBitmapField extends BitmapField {

	public FocusableBitmapField( Bitmap image, long style ) {
		super( image, style | BitmapField.FOCUSABLE );
	}
	protected void drawFocus(Graphics _g, boolean _on) {
	    paint(_g);
	}
}
