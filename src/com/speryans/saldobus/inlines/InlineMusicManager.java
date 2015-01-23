package com.speryans.saldobus.inlines;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.TouchEvent;
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
public class InlineMusicManager extends HorizontalFieldManager implements FocusChangeListener {
    
    private JSONObject object = null;
    
    private ButtonsListener listener = null;
    private int index;
    
    public InlineMusicManager( JSONObject obj, ButtonsListener listener, int index )
	{
    	super(HorizontalFieldManager.FOCUSABLE);
		
    	object = obj;
    	this.listener = listener;
    	this.index = index;
		if (object != null) {
			try {
				this.setPadding(3, 3, 3, 3);
				
				VerticalFieldManager manager = new VerticalFieldManager( Field.USE_ALL_WIDTH );
				
				FontColorField title = new FontColorField( obj.optString("name"), Field.USE_ALL_WIDTH | Field.NON_FOCUSABLE , 0x000000 );
				manager.add(title);
				
				FontColorField artist = new FontColorField( obj.optString("artist"), Field.USE_ALL_WIDTH | Field.NON_FOCUSABLE , 0x666666 );
				manager.add(artist);
				
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
			listener.onClicked(index, object);
		}
		invalidate();
	}
} 
