package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;

public class TitleBar extends Field {

	private Bitmap bitmap = Bitmap.getBitmapResource("top.png");

	public String text;
	public Bitmap title = null;
	public int HEIGHT = 69;

	// Layout values
	public int CURVE_X = 0; // X-axis inset of curve
	public int CURVE_Y = 0; // Y-axis inset of curve
	public int MARGIN = 0; // Space within component boundary

	// Static colors
	private static final int TEXT_COLOR = 0xFFFFFF; // White
	private static final int BACKGROUND_COLOR = 0xC8C8C8;

	public TitleBar() {
		super(FIELD_HCENTER | FIELD_VCENTER);
	}
	
	public TitleBar( String text ) {
		super(FIELD_HCENTER | FIELD_VCENTER);
		
		this.text = text;
	}
	
	public TitleBar( Bitmap title ) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.updateLayout();
	}

	public void setHeight( int height ) {
		HEIGHT = height;
	}

	public int getPreferredHeight() {
		return ( bitmap == null ) ? this.title.getHeight() : HEIGHT;
	}

	public int getPreferredWidth() {
		return Display.getWidth();
	}

	protected void layout(int width, int height) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	// When painting is requested, do it ourselves.
	protected void paint(Graphics g) {
		// Clear this area to white background, fully opaque.
		g.clear();
		g.setGlobalAlpha(255);
		g.setBackgroundColor(BACKGROUND_COLOR);

		// Drawing within our margin.
		int width = getPreferredWidth() - (MARGIN * 2);
		int height = getPreferredHeight() - (MARGIN * 2);

		if( bitmap != null ) {
			g.drawBitmap(0, 0, width, height, bitmap, 0, 0);
		} else {
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, width, height);
		}

		if( title == null ) {
			Font font = Font.getDefault().derive(Font.PLAIN, 9, Ui.UNITS_pt);
			int textWidth = font.getAdvance(text);
			int textHeight = font.getHeight();
			g.setColor(TEXT_COLOR);
			g.setFont(font);
			g.drawText(text, (width / 2) - (textWidth / 2) - MARGIN, (height / 2)
					- (textHeight / 2) - MARGIN);
		} else {
			int imgWidth = title.getWidth();
			int imgHeight = title.getHeight();
			
			g.drawBitmap((width / 2) - (imgWidth / 2) - MARGIN, (height / 2)
					- (imgHeight / 2) - MARGIN, imgWidth, imgHeight, title, 0, 0);
		}
	}
}
