package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.RichTextField;

public class PasswordEditText extends PasswordEditField {
	private String hint;
	private Bitmap BACKGROUND = Bitmap
			.getBitmapResource("edittext_background.png");
	
	private static final int YPADDING = Display.getWidth() > 320 ? 5 : 4;   // TODO: Touchscreen should be different
	
	private Font _buttonFont;

	public PasswordEditText(String text, String hint, int q, long style) {
		super(text, "", q, style);

		this.hint = hint;
		this.setPadding(2, 2, 2, 2);
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public void applyFont() {
        _buttonFont = getFont().derive(Font.BOLD, 6 ,Ui.UNITS_pt);
    }	

	public int getPreferredWidth() {
		return Display.getWidth();
	}

	public int getPreferredHeight() {
		return ( 2 * YPADDING + _buttonFont.getHeight() );
	}

	public void layout(int width, int height) {
        setExtent(width, getPreferredHeight());
        super.layout(width, getPreferredHeight());
    }

	protected void paint(Graphics g) {
		int oldColour = g.getColor();
		Font oldFont = g.getFont();
		try {
			if (getText().equals("")) {
				g.setFont(_buttonFont);
				g.setColor(0xCCCCCC);
				g.drawText(hint, 4, (getHeight() / 2) - 1 , DrawStyle.VCENTER);
			} else {
				super.paint(g);
			}
		} finally {
			g.setFont(oldFont);
			g.setColor(oldColour);
		}
	}

	protected void paintBackground(Graphics g) {
		int oldColour = g.getColor();
		Font oldFont = g.getFont();
		try {
			g.clear();
			g.setBackgroundColor(0xFFFFFF);

			g.setColor(0x706F6F);
			g.drawRect(0, 0, getWidth(), getHeight());
		} finally {
			g.setFont(oldFont);
			g.setColor(oldColour);
		}
	}

	private void paintBackgroundImage(Graphics g, Bitmap background) {
		int[] xPts = { 0, getWidth(), getWidth(), 0 };
		int[] yPts = { 0, 0, getHeight(), getHeight() };
		int dux = Fixed32.div(Fixed32.toFP(background.getWidth()), Fixed32
				.toFP(getWidth()));
		int dvy = Fixed32.div(Fixed32.toFP(background.getHeight()), Fixed32
				.toFP(getHeight()));
		boolean aaPolygons = g.isDrawingStyleSet(Graphics.DRAWSTYLE_AAPOLYGONS);
		g.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);
		g.drawTexturedPath(xPts, yPts, null, null, 0, 0, dux, 0, 0, dvy,
				background);
		g.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, aaPolygons);
	}
}
