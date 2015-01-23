package com.speryans.saldobus.inlines;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.speryans.saldobus.classes.ButtonsListener;
import com.speryans.saldobus.framework.classes.ui.FontColorField;
import com.speryans.saldobus.framework.rpc.json.me.JSONObject;

/**
 * 
 */
public class TwoLinesManager extends HorizontalFieldManager implements FocusChangeListener {
    
    private int id = -1;
    private String title_str = null;
    private String subtitle_str = null;
    
    private ButtonsListener listener = null;
    private int index;
	private FontColorField title;
	private FontColorField subtitle;
    
    public TwoLinesManager( int id_field, String tstr, String ststr, ButtonsListener listener )
	{
    	super(HorizontalFieldManager.FOCUSABLE);
		
    	id = id_field;
    	title_str = tstr;
    	subtitle_str = ststr;
    	
    	this.listener = listener;
    	
		if (title_str != null) {
			try {
				this.setPadding(8, 0, 0, 0);
				
				VerticalFieldManager manager = new VerticalFieldManager( Field.USE_ALL_WIDTH );
				manager.setPadding(0, 4, 0, 4);
				
				title = new FontColorField( title_str, Field.USE_ALL_WIDTH | Field.NON_FOCUSABLE , 0x000000 );
				manager.add(title);
				
				if( subtitle_str != null ) {
					subtitle = new FontColorField( subtitle_str, Field.USE_ALL_WIDTH | Field.NON_FOCUSABLE , 0x333333 );
					manager.add(subtitle);
				}
				
				BitmapField line = new BitmapField( Bitmap.getBitmapResource("linea_separadora.png"), USE_ALL_WIDTH);
				line.setMargin(8, 0, 0, 0);
				manager.add( line );
				add(manager);
				
				add(new NullField());
				setFocusListener(this);
			} catch (Throwable e) {}
		}
	}
    
    public boolean invokeAction(int action)
    {        
        switch(action)
        {
            case ACTION_INVOKE: // Trackball click.
            	loadProduct();
                return true; // We've consumed the event.
        }    
        return  super.invokeAction(action);
    }

	public boolean keyChar(char key, int status, int time) {
		boolean retval = false;
		switch (key) {
		case Characters.ENTER:			
			loadProduct();
			
			retval = true;
			break;
		default:
			retval = super.keyChar(key, status, time);
		}
		return retval;
	}
	
	public void setTitle( String text ) {
		title.setText(text);
	}
	
	public void setSubtitle( String text ) {
		subtitle.setText(text);
	}
    
	/**
     * overrides net.rim.device.api.ui.Field.touchEvent
     */
    protected boolean touchEvent(TouchEvent message)
    {
		int event = message.getEvent();
		switch (event) {
		case TouchEvent.CLICK:
			loadProduct();
			
			break;
		default:
			super.touchEvent(message);
			break;
		}
		return false;
	}
	
    public void focusChanged( Field field, int eventType )
    {
    	invalidate();
    }
    
    public void onFocus(int direction)
    {
        super.onFocus(direction);
        this.setBackground( BackgroundFactory.createLinearGradientBackground(0x3E85C5, 0x3E85C5, 0x3A76BC, 0x3A76BC) );
    }
    
    public void onUnfocus()
    {
		super.onUnfocus();
		this.setBackground( BackgroundFactory.createSolidBackground(0xFFFFFF));
	}

	private void loadProduct() {
		if( listener != null ) {
			listener.onClicked(id, null);
			listener.onClicked(this);
		}
		invalidate();
	}
} 
