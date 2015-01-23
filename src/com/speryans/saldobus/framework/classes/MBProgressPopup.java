package com.speryans.saldobus.framework.classes;


import com.speryans.saldobus.framework.classes.ui.FontColorField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public final class MBProgressPopup extends Screen implements KeyListener {
	private int _CUSTOM_WIDTH;
	private int _CUSTOM_HEIGHT;
	private Bitmap backgroundBitmap;
	private int _X;
	private int _Y;
	private static MBProgressPopup instance;
	private popupListener listener;
	private boolean cancelable;
	private FontColorField label;
	private Manager manager;
	private AnimatedField loader;
    
    public MBProgressPopup( Manager manager, String text ) //int width, int height,int bgColor,int textColor 
    {  
    	super( manager );
    	
    	this.manager = manager;
    	
    	//super.setBackground( BackgroundFactory.createSolidTransparentBackground(0x000000, 0) );
    	//this.setBackground( BackgroundFactory.createSolidTransparentBackground(0x000000, 0) );
    	
    	loader = new AnimatedField( Commons.blackloader, FIELD_HCENTER | FIELD_VCENTER );
    	loader.setMargin(0, 0, 6, 0);
        label = new FontColorField( text, DrawStyle.HCENTER | DrawStyle.VCENTER, Color.WHITE );
        
        manager.add(loader);
        manager.add(label);
        
        super.setFont(Font.getDefault().derive( Font.BOLD, 13));
        
        makeMaths();
    }
    
    private void makeMaths() {
    	_CUSTOM_WIDTH   = manager.getPreferredWidth() + 20;
		_CUSTOM_HEIGHT = manager.getPreferredHeight() + 20;
		
		//backgroundBitmap = new Bitmap(_CUSTOM_WIDTH,_CUSTOM_HEIGHT);
		_X = ( Display.getWidth() - _CUSTOM_WIDTH ) >> 1;
		_Y = ( Display.getHeight() - _CUSTOM_HEIGHT ) >> 1;

		//Display.screenshot(backgroundBitmap, _X, _Y, _CUSTOM_WIDTH, _CUSTOM_HEIGHT);
		this.invalidate();
    }
    
    public static MBProgressPopup create( String text ) {
    	if( instance == null ) {
    		instance = new MBProgressPopup( new VerticalFieldManager( ), text );
    	}
    	return instance;
    }
    
    public void show() {
    	UiApplication.getApplication().invokeLater(new Runnable() {
			public void run() {
				UiEngine ui = Ui.getUiEngine();
				
				ui.pushGlobalScreen(instance, 1, UiEngine.GLOBAL_QUEUE);
			}
		});
    }
        
    public void setText( final String text ) {
    	if( instance != null ) {
    		UiApplication.getApplication().invokeLater(new Runnable() {
    			public void run() {
    				label.setText(text);
    		
    				makeMaths();
    			}
    		});
    	}
    }
    
    public void hide() {
    	if( instance != null ) {
    		UiApplication.getApplication().invokeLater(new Runnable() {
    			public void run() {
    				Ui.getUiEngine().popScreen(instance);
    				instance = null;
    			}
    		});
    	}
    }
    
    protected void paintBackground(Graphics g){	
		// Instead of trying to make the background transparent (which only work on OS4.5 devices and not OS4.6 devices for some reason),
		// we'll just draw the screenshot bitmap to the background of our popup and make it appear that it's transparent
		//g.drawBitmap(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), backgroundBitmap, 0, 0);
	}
  
    /*Paint Method*/
    protected void paint(Graphics g) {
    	g.clear();
    	int arc = 3;
    	
    	XYRect myExtent = getExtent();
		int alpha = g.getGlobalAlpha();
		
		g.setColor(Color.BLACK);
		// Fill transparent rounded rectangle
		g.fillRoundRect(0, 0, myExtent.width, myExtent.height, arc, arc);
		g.setColor(Color.WHITESMOKE);
		// Draw black rounded border
		g.drawRoundRect(0, 0, myExtent.width, myExtent.height, arc, arc);
		g.setGlobalAlpha(alpha);
        
        super.paint(g);
    }
    
    public boolean keyChar(char key, int status, int time) 
    {
        boolean retval = false;
        
        if(key == Characters.ESCAPE) {
            retval = true;
            if( cancelable ) {
            	if( listener != null ) listener.onCancel( instance );
            	hide();
            }
        } else {         
            retval = super.keyChar(key, status, time);
        } 
        
        return retval;
    }
    
    protected void sublayout( int width, int height ) {
        setExtent( _CUSTOM_WIDTH, _CUSTOM_HEIGHT );
        // If you want to make it look like the popupscreen is glass that "distorts" the light, you can 
        // add a little offset of a pixel or two to the setPosition method below, so that the background of
        // the popupscreen is painted a bit off from the actual screen below. Just a thought....
        setPosition( _X, _Y );
        layoutDelegate( _CUSTOM_WIDTH + 20, _CUSTOM_HEIGHT + 20 );
        setPositionDelegate(10,10);        
    }
    
    /* Implementation of KeyListener.keyDown(). */
    public boolean keyDown(int keycode, int time) 
    {
        return false;
    }

    /* Implementation of KeyListener.keyRepeat(). */
    public boolean keyRepeat(int keycode, int time) 
    {
        return false;
    }
    
    /* Implementation of KeyListener.keyStatus(). */
    public boolean keyStatus(int keycode, int time) 
    {
        return false;
    }
    
    /* Implementation of KeyListener.keyUp(). */
     public boolean keyUp(int keycode, int time) 
    {
        return false;
    }

	public popupListener getListener() {
		return listener;
	}

	public void setListener(popupListener listener) {
		this.listener = listener;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	public void onComplete() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onClose() {
		if( cancelable ) {
			if( listener != null ) listener.onCancel( instance );
			
			super.close();
			hide();
			
			return true;
		}
		return false;
	}

	public void paintOn(Graphics graphics) {
		this.paint(graphics);
	}

	public void paintBackgroundOn(Graphics graphics) {
		this.paintBackground(graphics);
	}
} 
