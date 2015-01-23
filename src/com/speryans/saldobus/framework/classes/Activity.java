package com.speryans.saldobus.framework.classes;

import java.io.InputStream;
import java.util.Vector;

import com.speryans.saldobus.framework.classes.ui.UX;
import com.speryans.saldobus.framework.classes.ui.UXException;
import com.speryans.saldobus.framework.rpc.json.me.JSONException;
import com.speryans.saldobus.framework.rpc.json.me.JSONObject;
import com.speryans.saldobus.framework.tools.Log;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

public class Activity extends MainScreen implements Translator {
	private static final int RESULT_OK = 0;
	private Activity activity;
	protected int result = RESULT_OK;
	protected int requestcode = -1;
	
	public static JSONObject strings = null;
	
	public Activity() {
		super();
		
		initTextTranslation();
	}
	
	public Activity( long style ) {
		super( style );
		initTextTranslation();
	}
	
	public void startActivityForResult( Activity activity, int requestCode ) {
		this.activity = activity;
		this.requestcode = requestCode;
		
		(UiApplication.getUiApplication()).pushScreen(activity);
	}
	
	public void setResult( int status ) {
		this.result = status;
	}
	
	public int getResult( ) {
		return this.result;
	}
	
	protected void onExposed() {
		if( activity != null ) {
			notifyClose( this.requestcode, activity.getResult() );
			activity = null;
			result = -1;
			requestcode = -1;
		}
	}
	
	public Bitmap getResource( String name ) throws UXException {
		return UX.getResource(name);
	}
	
	/**
	 * Redeclarar obligatoriamente
	 * 
	 * @param msg
	 * @param response
	 */
	public void notifyClose(int msg, int response) {}
	
	public Bitmap getBitmap( String file ) {
		//Log.error("This is the locale: " + locale );
			
		Bitmap temp = Bitmap.getBitmapResource( file + "-" + locale + ".png" );
		if( temp == null ) {
			//Log.error("Te locale resource not exists, returning: " + file + ".png" );
			
			return Bitmap.getBitmapResource( file + ".png" );
		} else {
			//Log.error("File to get: " + file + "-" + locale + ".png" );
				
			return temp;
		}
	}
	
	public String getString( String tag ) {
		try {
			String ret = "";
			try {
				ret = strings.getString( tag );
			} catch( JSONException je ) {
				ret = "";
			}
			return ret;
		} catch( Exception e ) {
			Log.error( "Text translation error: " + e.toString() );
			return null;
		}
	}

	public void initTextTranslation() {
		if( strings == null ) {
			try {
				InputStream stream = getClass().getResourceAsStream("/values-" + locale + ".json");
				
				strings = new JSONObject( new String( IOUtilities.streamToBytes( stream ) ) );
				
				stream.close();
			} catch( Exception e ) {
				try {
					InputStream stream = getClass().getResourceAsStream("/values.json");
					
					strings = new JSONObject( new String( IOUtilities.streamToBytes( stream ) ) );
				
					stream.close();
				} catch( Exception ex ) {
					Log.error("Error loading language file.");
				}
			}
		}
	}
	
	Vector _menuItems = new Vector();
	public void addMenuItem ( MenuItem item ) {
		if( !_menuItems.contains( item ) ) {
			_menuItems.addElement ( item );
		}
	}
	
	public void removeMenuItem ( MenuItem item ) {
		_menuItems.removeElement(item);
	}
	
	protected void makeMenu( Menu menu, int instance ) {
	    for ( int i = 0; i < _menuItems.size(); i++ ) {
	        menu.add( (MenuItem) _menuItems.elementAt( i ));
	    }
	}
	
	public void removeAllMenuItems() {
		_menuItems.removeAllElements();
	}

	public String getLocale() {
		if( locale == null ) {
			return defaultLocale;
		}
		return locale;
	}
}