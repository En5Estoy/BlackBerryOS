package com.speryans.saldobus.classes;

import com.speryans.saldobus.framework.classes.ui.FontColorField;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * 
 */
public class HomeButton extends VerticalFieldManager implements
		FocusChangeListener {

	private FieldChangeListener listener;
	private FontColorField titleField;

	public HomeButton( String label )
	{
    	super(HorizontalFieldManager.FOCUSABLE );
		
    	try {
    		this.setBackground(BackgroundFactory.createSolidBackground(0x333333) );
    		
			HorizontalFieldManager vfm = new HorizontalFieldManager( Field.FIELD_HCENTER );
				
			int fontSize = Font.getDefault().getHeight() - 3;
			Font font = Font.getDefault().derive(Font.BOLD, fontSize);
			FontColorField titleField = new FontColorField( label,Field.NON_FOCUSABLE, Color.WHITESMOKE, font);
				
			vfm.add(titleField);
				
			vfm.setMargin(9, 9, 9, 9);
			add( vfm );
			add(new NullField());
			setFocusListener(this);
		} catch (Throwable e) {}
	}
	
	public HomeButton( String label, long style )
	{
    	super(HorizontalFieldManager.FOCUSABLE | style );
		
    	try {
    		this.setBackground(BackgroundFactory.createSolidBackground(0xB81B2B) );
    		
			HorizontalFieldManager vfm = new HorizontalFieldManager( Field.FIELD_HCENTER );
				
			int fontSize = Font.getDefault().getHeight() - 3;
			Font font = Font.getDefault().derive(Font.BOLD, fontSize);
			titleField = new FontColorField( label,Field.NON_FOCUSABLE, Color.WHITESMOKE, font);
				
			vfm.add(titleField);
				
			vfm.setMargin(9, 9, 9, 9);
			add( vfm );
			add(new NullField());
			setFocusListener(this);
		} catch (Throwable e) {}
	}
	
	public void setText( String text ) {
		titleField.setText(text);
		invalidate();
	}
	
	public String getText() {
		return titleField.getText();
	}
	
	public void setListener( FieldChangeListener listener ) {
		this.listener = listener;
	}

	public boolean invokeAction(int action) {
		switch (action) {
		case ACTION_INVOKE: // Trackball click.
			loadProduct();
			return true; // We've consumed the event.
		}
		return super.invokeAction(action);
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
	protected boolean touchEvent(TouchEvent message) {
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

	public void focusChanged(Field field, int eventType) {
		invalidate();
	}

	public void onFocus(int direction) {
		super.onFocus(direction);

		this.setBackground( BackgroundFactory.createSolidBackground(0xEBAE15) );
	}

	public void onUnfocus() {
		super.onUnfocus();
		this.setBackground(BackgroundFactory.createSolidBackground(0x333333) );
	}

	private void loadProduct() {
		listener.fieldChanged(this, 0);
		invalidate();
	}
}
