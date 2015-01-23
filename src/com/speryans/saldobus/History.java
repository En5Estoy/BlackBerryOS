package com.speryans.saldobus;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.speryans.saldobus.framework.classes.KeySaver;
import com.speryans.saldobus.framework.classes.MBProgressPopup;
import com.speryans.saldobus.framework.classes.StatusActivity;
import com.speryans.saldobus.framework.classes.ui.FontColorField;
import com.speryans.saldobus.framework.rpc.json.me.JSONArray;
import com.speryans.saldobus.framework.rpc.json.me.JSONException;
import com.speryans.saldobus.framework.rpc.json.me.JSONObject;
import com.speryans.saldobus.framework.tools.Log;
import com.speryans.saldobus.inlines.TwoLinesManager;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class History extends StatusActivity
{
	
	private VerticalFieldManager logoHeader;
	private VerticalFieldManager appContainer;
	private JSONArray u_data;
	private MBProgressPopup dialog;
	
    /**
     * Creates a new Main object
     */
    public History(String dni, String card)
    {        
    	setTitle("Saldo Bus     ");
        
        this.createLogo();
        appContainer = new VerticalFieldManager( USE_ALL_WIDTH );
        
        container.add(appContainer);
        
        String user_data = (String)KeySaver.getKey( dni + "-" + card );
		
        dialog = MBProgressPopup.create("Cargando...");
        dialog.show();
        
		u_data = null;
		try {
			if( user_data.equalsIgnoreCase("") ) {
				u_data = new JSONArray();
			} else {
				u_data = new JSONArray( user_data );
			}
		} catch (JSONException e) {
			u_data = new JSONArray();
		}
        
        this.loadApp();
    }
    
    private void createLogo() {
    	logoHeader = new VerticalFieldManager(USE_ALL_WIDTH);
    	logoHeader.setBackground(BackgroundFactory.createSolidBackground(0xEBAE15) );
    	
    	HorizontalFieldManager head = new HorizontalFieldManager(Field.FIELD_HCENTER);
    	BitmapField headLogo = new BitmapField( Bitmap.getBitmapResource("logo.png"), Field.FIELD_HCENTER );
    	FontColorField headerTxt = new FontColorField("Saldo Bus", Field.FIELD_VCENTER, 0x000000, Font.getDefault().derive(Font.BOLD));
    	headerTxt.setMargin(0, 4, 0, 4);
    	
    	head.add(headLogo);
    	head.add(headerTxt);
    	
    	logoHeader.add( head );
    	
    	logoHeader.setPadding(10, 10, 10, 10);
    	
    	container.add(logoHeader);
    }
    
    private void loadApp() {
    	FontColorField infoLbl = new FontColorField("Aqui se mostrar‡n sus 5 œltimas consultas. Es probable que Red Bus demore hasta 2 d’as en actualizar el saldo.", USE_ALL_WIDTH | FOCUSABLE, 0x000000);
    	infoLbl.setBackground(Field.VISUAL_STATE_FOCUS, BackgroundFactory.createSolidTransparentBackground(0xFFFFFF, 0) );
    	infoLbl.setBackground( BackgroundFactory.createSolidBackground(0xEAEAEA) );
    	infoLbl.setPadding(7, 7, 7, 7);
    	
    	appContainer.add(infoLbl);
    	
    	try {
    		TwoLinesManager firstMng = null;
    		
			for( int i = (u_data.length() - 1) ; i >= 0 ; i-- ) {
				JSONObject temp = u_data.getJSONObject(i);
			
				synchronized (Application.getEventLock()) {
					TwoLinesManager mng = new TwoLinesManager( i, "Linea " + temp.getString("line"), temp.getString("date") + "\n" + "$ " + temp.getString("total"), null );
					if( firstMng == null ) {
						firstMng = mng;
					}
					appContainer.add( mng );
				}
			
				if( i < (u_data.length() - 6) ) break;
			}
			
			synchronized (Application.getEventLock()) {
				firstMng.setFocus();
			}
		} catch( Exception e ) {
			Log.error(this.getClass().getName() + " - " + e.toString());
		}
    	
    	dialog.hide();
    }
}
