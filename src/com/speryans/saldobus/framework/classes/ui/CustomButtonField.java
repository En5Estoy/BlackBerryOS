/*
 * EmbossedButtonField.java
 *
 * © Research In Motion Limited, 2006
 * Confidential and proprietary.
 */

package com.speryans.saldobus.framework.classes.ui;


import com.speryans.saldobus.framework.classes.Commons;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.util.*;

/**
 * A custom button field
 */
public class CustomButtonField extends BaseButtonField
{
	protected Bitmap bitmap = Bitmap.getBitmapResource("bottom.png");
	
    public static final long COLOUR_BORDER              = 0xc5fd60b0047307a1L; //net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_BORDER
    public static final long COLOUR_TEXT                = 0x16a6e940230dba6bL; //net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_TEXT
    public static final long COLOUR_TEXT_FOCUS          = 0xe208bcf8cb684c98L; //net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_TEXT_FOCUS
    public static final long COLOUR_BACKGROUND          = 0x8d733213d6ac8b3bL; //net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_BACKGROUND
    public static final long COLOUR_BACKGROUND_FOCUS    = 0x3e2cc79e4fd151d3L; //net.rim.device.api.ui.component.EmbossedButtonField.COLOUR_BACKGROUND_FOCUS

    private static final int XPADDING = Display.getWidth() > 320 ? 7 : 5;   // TODO: Touchscreen should be different
    private static final int YPADDING = Display.getWidth() > 320 ? 16 : 16;   // TODO: Touchscreen should be different
    
    private final int MARGIN_TOP = 0;
    
    private LongIntHashtable _colourTable;
    private Font _buttonFont;
    private String _text;
    private boolean _pressed;
    
    protected int _width;
    protected int _height;
    
    private String button_image;
    private Bitmap IMAGE_BUTTON;
    private Bitmap IMAGE_FOCUS;
    
    // 0000000000000000000
    int[] upperX_PTS;
    int[] upperY_PTS;
    int[] upperDrawColors;
    int[] lowerX_PTS;
    int[] lowerY_PTS;
    int[] lowerDrawColors;
    
    int[] upperDrawColorsFocus;
    int[] lowerDrawColorsFocus;
    
    public CustomButtonField( String text )
    {
        this(text, 0, null, null);
    }
    
    public CustomButtonField( String text, String image )
    {
        this(text, 0, image);
    }
    
    public CustomButtonField( String text, long style )
    {
        this( text, style, null, null );
    }
    
    public CustomButtonField( String text, long style, String image )
    {
        this( text, style, null, image );
    }
    
    public CustomButtonField( String text, long style, LongIntHashtable colourTable, String image )
    {
        super( ButtonField.CONSUME_CLICK | ButtonField.FOCUSABLE | style);
        _text = text;
        
        loadImages( image );
        
        setColourTable( colourTable );
    }
    
    private void loadImages(String image) {
    	button_image = image;
    	try {
        	IMAGE_BUTTON = Bitmap.getBitmapResource( image + ".png");
        } catch( Exception e ) {
        	IMAGE_BUTTON = null;
        }
        try {
        	IMAGE_FOCUS = Bitmap.getBitmapResource( image + "_focus.png");
        } catch( Exception e ) {
        	IMAGE_FOCUS = null;
        }
    }
    
    public void setImage( String image ) {
    	loadImages( image );
    }

    public String getImage() {
    	return button_image;
    }
    
    public void setColourTable( LongIntHashtable colourTable )
    {
        _colourTable = colourTable;
        invalidate();
    }
    
    public int getColour( long colourKey ) 
    {
        if( _colourTable != null ) {
            int colourValue = _colourTable.get( colourKey );
            if( colourValue >= 0 ) {
                return colourValue;
            }
        }
            
        // Otherwise, just use some reasonable default colours
        if( colourKey == COLOUR_BORDER ) {
            return 0x212121;
        } else if( colourKey == COLOUR_TEXT ) {
            return 0x000000;
        } else if( colourKey == COLOUR_TEXT_FOCUS ) {
            return 0xFFFFFF;
        } else if( colourKey == COLOUR_BACKGROUND ) {
            return isStyle( Field.READONLY ) ? 0x000000 : 0x000000;
        } else if( colourKey == COLOUR_BACKGROUND_FOCUS ) {
            return isStyle( Field.READONLY ) ? 0x000000 : 0x000000;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public void setText(String text)
    {
        _text = text;
    }
    
    public void applyFont()
    {
        _buttonFont = getFont().derive(Font.PLAIN, (IMAGE_BUTTON == null) ? 7 : 5 ,Ui.UNITS_pt);
    }
    
    public int getPreferredWidth()
    {
        return ( _text.equalsIgnoreCase("") ) ? IMAGE_BUTTON.getWidth() :  2 * XPADDING + _buttonFont.getAdvance( _text );
    }

    protected void onUnfocus()
    {
        super.onUnfocus();
        if( _pressed ) {
            _pressed = false;
            invalidate();
        }
    }
    
    protected boolean navigationClick(int status, int time) {
        _pressed = true;
        invalidate();
        return super.navigationClick( status, time );
    }
    
    protected boolean navigationUnclick(int status, int time) {
        _pressed = false;
        invalidate();
        return true;
    }
    
    public int getPreferredHeight()
    {
        return ( _text.equalsIgnoreCase("") ) ? IMAGE_BUTTON.getHeight() : ( 2 * YPADDING + _buttonFont.getHeight() ) - MARGIN_TOP;
    }
        
    protected void layout( int width, int height )
    {
        setExtent( isStyle( USE_ALL_WIDTH ) ? width : getPreferredWidth(), getPreferredHeight() );
        _width = getWidth();
        _height = getHeight() - MARGIN_TOP;
    }
    
    protected void paint( Graphics g )
    {
        int oldColour = g.getColor();
        Font oldFont = g.getFont();
        try {        	
        	Bitmap image = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? ( ( IMAGE_FOCUS == null ) ? IMAGE_BUTTON : IMAGE_FOCUS ) : IMAGE_BUTTON;
        	
        	int image_x = 0;
        	int image_y = 0;
        	
        	if( image != null ) {
        		image_x = ( _width / 2 ) - (image.getWidth() / 2);
        		image_y = ( ( _height / 2 ) - ( image.getHeight() / 2 ) - ( _text.equals("") ? 0 : ( _buttonFont.getHeight() / 3 ) ) ) + MARGIN_TOP;
        		g.drawBitmap( image_x , image_y, image.getWidth() , image.getHeight() , image, 0, 0);
        	}
        	
        	g.setFont(_buttonFont);
        	g.setColor( g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? getColour( COLOUR_TEXT_FOCUS ) : getColour( COLOUR_TEXT ) );
        	
        	g.drawText( _text, 0, (image == null) ? YPADDING : ( image_y + image.getHeight() ) - ( _buttonFont.getHeight() / 4 ) , DrawStyle.HCENTER, _width );
        } finally {
            g.setFont( oldFont );
            g.setColor( oldColour );
        }
    }
    
    protected void paintBackground( Graphics g ) {
    	g.clear();
		int color = g.getColor();
		g.setColor( g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? getColour( COLOUR_BACKGROUND_FOCUS ) : getColour( COLOUR_BACKGROUND ) );
		g.fillRect(0, 0, _width, _height);
		if( bitmap != null ) {
			g.drawBitmap(0, 0, _width, _height, bitmap, 0, 0);
		}
		g.setColor(color);
    }
}
