/*
 * EmbossedButtonField.java
 *
 * © Research In Motion Limited, 2006
 * Confidential and proprietary.
 */

package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.util.*;

/**
 * A custom button field
 */
public class RoundedButtonField extends BaseButtonField {
	public static final long COLOUR_BORDER = 0xc5fd60b0047307a1L; // net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_BORDER
	public static final long COLOUR_TEXT = 0x16a6e940230dba6bL; // net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_TEXT
	public static final long COLOUR_TEXT_FOCUS = 0xe208bcf8cb684c98L; // net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_TEXT_FOCUS
	public static final long COLOUR_BACKGROUND = 0x8d733213d6ac8b3bL; // net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_BACKGROUND
	public static final long COLOUR_BACKGROUND_FOCUS = 0x3e2cc79e4fd151d3L; // net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_BACKGROUND_FOCUS

	private static final int XPADDING = Display.getWidth() > 320 ? 7 : 5;
	private static final int YPADDING = Display.getWidth() > 320 ? 8 : 4;
	private static final int BEVEL = 2;

	private LongIntHashtable _colourTable;
	private Font _buttonFont;
	private String _text;
	private boolean _pressed;

	private int _width;
	private int _height;

	// 0000000000000000000
	int[] upperX_PTS;
	int[] upperY_PTS;
	int[] upperDrawColors;
	int[] lowerX_PTS;
	int[] lowerY_PTS;
	int[] lowerDrawColors;

	int[] upperDrawColorsFocus;
	int[] lowerDrawColorsFocus;

	public RoundedButtonField(String text) {
		this(text, 0);
	}

	public RoundedButtonField(String text, long style) {
		this(text, style, null);
	}

	public RoundedButtonField(String text, long style,
			LongIntHashtable colourTable) {
		super(Field.FOCUSABLE | style);
		_text = text;
		setColourTable(colourTable);
	}

	public void setColourTable(LongIntHashtable colourTable) {
		_colourTable = colourTable;
		invalidate();
	}

	public int getColour(long colourKey) {
		if (_colourTable != null) {
			int colourValue = _colourTable.get(colourKey);
			if (colourValue >= 0) {
				return colourValue;
			}
		}

		// Otherwise, just use some reasonable default colours
		if (colourKey == COLOUR_BORDER) {
			return 0x212121;
		} else if (colourKey == COLOUR_TEXT) {
			return Color.CADETBLUE;
		} else if (colourKey == COLOUR_TEXT_FOCUS) {
			return Color.BLACK;
		} else if (colourKey == COLOUR_BACKGROUND) {
			return isStyle(Field.READONLY) ? 0x777777 : 0x424242;
		} else if (colourKey == COLOUR_BACKGROUND_FOCUS) {
			return isStyle(Field.READONLY) ? 0x666688 : 0x185AB5;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void setText(String text) {
		_text = text;
	}

	public void applyFont() {
		_buttonFont = getFont().derive(Font.BOLD);
	}

	public int getPreferredWidth() {
		return 2 * XPADDING + _buttonFont.getAdvance(_text);
	}

	protected void onUnfocus() {
		super.onUnfocus();
		if (_pressed) {
			_pressed = false;
			invalidate();
		}
	}

	protected boolean navigationClick(int status, int time) {
		_pressed = true;
		invalidate();
		return super.navigationClick(status, time);
	}

	protected boolean navigationUnclick(int status, int time) {
		_pressed = false;
		invalidate();
		return true;
	}

	public int getPreferredHeight() {
		return 2 * YPADDING + _buttonFont.getHeight();
	}

	protected void layout(int width, int height) {
		setExtent(isStyle(USE_ALL_WIDTH) ? width : getPreferredWidth(),
				getPreferredHeight());
		_width = getWidth();
		_height = getHeight();
	}

	protected void paint(Graphics g) {
		int oldColour = g.getColor();
		Font oldFont = g.getFont();
		try {
			g.setFont(_buttonFont);
			g.setColor(g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? 
							getColour(COLOUR_TEXT_FOCUS)
							: getColour(COLOUR_TEXT));
			g.drawText(_text, 0, YPADDING, DrawStyle.HCENTER, _width);
		} finally {
			g.setFont(oldFont);
			g.setColor(oldColour);
		}
	}

	// Layout values
	public int CURVE_X = 8; // X-axis inset of curve
	public int CURVE_Y = 8; // Y-axis inset of curve
	public int MARGIN = 1; // Space within component boundary

	private static final byte[] PATH_POINT_TYPES = {
	    Graphics.CURVEDPATH_END_POINT, 
	    Graphics.CURVEDPATH_QUADRATIC_BEZIER_CONTROL_POINT,
	    Graphics.CURVEDPATH_END_POINT, 
	    Graphics.CURVEDPATH_END_POINT, 
	    Graphics.CURVEDPATH_QUADRATIC_BEZIER_CONTROL_POINT,
	    Graphics.CURVEDPATH_END_POINT, 
			
	    Graphics.CURVEDPATH_END_POINT, 
	    Graphics.CURVEDPATH_QUADRATIC_BEZIER_CONTROL_POINT,
	    Graphics.CURVEDPATH_END_POINT, 
	    Graphics.CURVEDPATH_END_POINT, 
	    Graphics.CURVEDPATH_QUADRATIC_BEZIER_CONTROL_POINT,
	    Graphics.CURVEDPATH_END_POINT, 
	  };

	private static final int[] PATH_GRADIENT = { 
		0x8CC8C4, 0x8CC8C4, 0x8CC8C4, 0x5B77A6, 0x3230CF, 0x3230CF, 
		0x8CC8C4, 0x8CC8C4, 0x8CC8C4, 0x5B77A6, 0x3230CF, 0x3230CF 
		};

	protected void paintBackground(Graphics g) {
		int oldColour = g.getBackgroundColor();
		int oldAlpha = g.getGlobalAlpha();
		try {
			int width = getWidth() - (MARGIN * 2);
			int height = getHeight() - (MARGIN * 2);

			g.setColor( Color.WHITE );
			g.fillRoundRect(0, 0, width, height, CURVE_X * 2, CURVE_Y * 2);
			g.setColor( Color.GRAY );
			g.drawRoundRect(0, 0, width, height, CURVE_X * 2, CURVE_Y * 2);

			/*
			 * if( g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ) {
			 * g.drawShadedFilledPath(upperX_PTS, upperY_PTS, null,
			 * upperDrawColorsFocus, null); g.drawShadedFilledPath(lowerX_PTS,
			 * lowerY_PTS, null, lowerDrawColorsFocus, null); } else {
			 * g.drawShadedFilledPath(upperX_PTS, upperY_PTS, null,
			 * upperDrawColors, null); g.drawShadedFilledPath(lowerX_PTS,
			 * lowerY_PTS, null, lowerDrawColors, null); }
			 */

		} finally {
			g.setBackgroundColor(oldColour);
			g.setGlobalAlpha(oldAlpha);
		}
	}
}
