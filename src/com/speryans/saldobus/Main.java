package com.speryans.saldobus;

import java.util.Hashtable;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.TextFilter;

import com.speryans.saldobus.classes.ButtonsListener;
import com.speryans.saldobus.classes.HomeButton;
import com.speryans.saldobus.framework.classes.AnimatedField;
import com.speryans.saldobus.framework.classes.Commons;
import com.speryans.saldobus.framework.classes.Connection;
import com.speryans.saldobus.framework.classes.ConnectionListener;
import com.speryans.saldobus.framework.classes.ImagesLoader;
import com.speryans.saldobus.framework.classes.KeySaver;
import com.speryans.saldobus.framework.classes.MBProgressPopup;
import com.speryans.saldobus.framework.classes.StatusActivity;
import com.speryans.saldobus.framework.classes.ui.EditText;
import com.speryans.saldobus.framework.classes.ui.FontColorField;
import com.speryans.saldobus.framework.rpc.json.me.JSONArray;
import com.speryans.saldobus.framework.rpc.json.me.JSONObject;
import com.speryans.saldobus.framework.tools.Log;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class Main extends StatusActivity implements ConnectionListener
{
	private static final int DATA = 10;

	private static final int SESSION = 50;
	
	private VerticalFieldManager logoHeader;
	private VerticalFieldManager appContainer;

	private HomeButton consultButton;

	private HomeButton historyButton;

	private EditText dni;

	private EditText card;

	private EditText control;

	private VerticalFieldManager imageManager;

	private AnimatedField loader;
	
	private String session = null;

	private BitmapField captchaField;

	private ImagesLoader imageLoader;

	private MBProgressPopup ploader;
	
    /**
     * Creates a new Main object
     */
    public Main()
    {        
        setTitle("Saldo Bus     ");
        
        this.createLogo();
        appContainer = new VerticalFieldManager( USE_ALL_WIDTH );
        
        container.add(appContainer);
        
        imageLoader = new ImagesLoader( false );
        
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
    	FontColorField infoLbl = new FontColorField("Accedemos al sitio web de Red Bus para obtener los datos por lo cual es probable que debido a ca’das del mismo no se muestren siempre. El c—digo de verificaci—n de tal sitio es obligatorio, el servicio que ofrecemos no pretende pasar por sobre las caracter’sticas de seguridad impuestas por Red Bus. La aplicaci—n guardar‡ los œltimos datos ingresados y podr‡ consultar un hist—rico de sus consultas de manera offline.", USE_ALL_WIDTH | FOCUSABLE, 0x000000);
    	infoLbl.setBackground(Field.VISUAL_STATE_FOCUS, BackgroundFactory.createSolidTransparentBackground(0xFFFFFF, 0) );
    	infoLbl.setBackground( BackgroundFactory.createSolidBackground(0xEAEAEA) );
    	infoLbl.setPadding(7, 7, 7, 7);
    	
    	appContainer.add(infoLbl);
    	
    	dni = new EditText("", "D.N.I.", 255, USE_ALL_WIDTH);
    	dni.setFilter( TextFilter.get( TextFilter.NUMERIC ) );
    	dni.setMargin(8, 8, 8, 8);
    	
    	card = new EditText("", "Nœmero de Tarjeta", 255, USE_ALL_WIDTH);
    	card.setFilter( TextFilter.get( TextFilter.NUMERIC ) );
    	card.setMargin(8, 8, 8, 8);
    	
    	control = new EditText("", "C—digo de Control", 255, USE_ALL_WIDTH);
    	control.setMargin(8, 8, 8, 8);
    	
    	appContainer.add(dni);
    	appContainer.add(card);
    	
    	// Check if data exists and set
    	String lastDNI = (String)KeySaver.getKey("lastDNI");
    	String lastCARD = (String)KeySaver.getKey("lastCARD");
    	
    	if( lastDNI != null ) {
    		dni.setText(lastDNI);
    	}
    	
    	if( lastCARD != null ) {
    		card.setText(lastCARD);
    	}
    	
    	imageManager = new VerticalFieldManager( USE_ALL_WIDTH );
    	
    	captchaField = new BitmapField(null, FIELD_HCENTER | FIELD_VCENTER);
    	
    	loader = new AnimatedField( Commons.imageloader, FIELD_HCENTER | FIELD_VCENTER );
    	
    	imageManager.add(loader);
    	
    	appContainer.add(imageManager);
    	
    	appContainer.add(control);
    	
    	consultButton = new HomeButton("Consultar", USE_ALL_WIDTH);
    	consultButton.setListener( new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				KeySaver.setKey("lastDNI", dni.getText().trim());
				KeySaver.setKey("lastCARD", card.getText().trim());
				
				if( dni.getText().trim().equalsIgnoreCase("") || 
						card.getText().trim().equalsIgnoreCase("") ) {
					Commons.showGlobalMessage("Debe completar los campos D.N.I, Nœmero de Tarjeta y Control para continuar");
				} else {
					sendData();
				}
			}
    	});
    	historyButton = new HomeButton("Historial", USE_ALL_WIDTH);
    	historyButton.setListener( new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				showHistory();
			}
    	});
    	
    	appContainer.add(consultButton);
    	appContainer.add(historyButton);
    	
    	//Connection.getInstance("http://sergiovillella.com/api/news.php", this, DATA).start();
    	loadCaptcha();
    }
    
    private void sendData() {
    	ploader = MBProgressPopup.create("Cargando...");
    	ploader.show();
    	
    	Hashtable values = new Hashtable();
		values.put("dni", dni.getText() );
		values.put("card", card.getText() );
		values.put("captcha", control.getText() );
		values.put("cookie", session );
		
		Connection.postInstance("http://54.243.218.97:3000/wrappost", this, DATA, values).start();
    }

	private void loadCaptcha() {
		if( session == null || session.equalsIgnoreCase("") ) {
			Connection.getInstance("http://54.243.218.97:3000/wrapget", this, SESSION).start();
		} else {
			imageLoader.add("http://54.243.218.97:3000/wrapimg/" + session, captchaField, 150, 80 , Bitmap.SCALE_TO_FIT);
			imageLoader.start();
			
			synchronized (Application.getEventLock()) {
				imageManager.deleteAll();
				imageManager.add(captchaField);
			}
		}
	}

	public void ready(int msg, String message) {
		if( msg == DATA ) {
			processMessage( message );
		}
		if( msg == SESSION ) {
			this.processSession(message);
		}
	}
	
	private void processSession(String message) {
		try {
			JSONObject data = new JSONObject(message);
			
			if( data.getBoolean("result") ) {
				session = data.getString("cookie");
				
				loadCaptcha();
			} else {
				synchronized (Application.getEventLock()) {
					Commons.showToast("Se produjo un error accediendo al sitio web de Red Bus. Intente nuevamente m‡s tarde.");
				}
			}
		} catch( Exception e ) {
			Log.info(e.toString());
		}
	}

	private void processMessage(String message) {
		try {
			JSONObject data = new JSONObject(message);
			
			if( data.getBoolean("result") ) {
				if( !data.getJSONObject("data").getString("date").equalsIgnoreCase("") ) {
					String user_data = (String)KeySaver.getKey( dni.getText().trim() + "-" + card.getText().trim() );
				
					JSONArray u_data = null;
					try {
						if( user_data.equalsIgnoreCase("") || user_data == null ) {
							u_data = new JSONArray();
						} else {
							u_data = new JSONArray( user_data );
						}
					} catch( Exception ae ) {
						u_data = new JSONArray();
					}
				
					JSONObject service_data = data.getJSONObject("data");
				
					JSONObject obj = new JSONObject();
					obj.put("date", service_data.getString("date"));
					obj.put("line", service_data.getString("line"));
					obj.put("total", service_data.getString("balance"));
				
					u_data.put(obj);
				
					KeySaver.setKey(dni.getText().trim() + "-" + card.getText().trim(), u_data.toString());
				
					showHistory();
				
					session = null;
					loadCaptcha();
				} else {
					Commons.showToast("El c—digo de control ingresado no es v‡lido o ha expirado.");
				}
			} else {
				Commons.showToast("Se produjo un error accediendo al sitio web de Red Bus. Intente nuevamente m‡s tarde.");
			}
		} catch( Exception e ) {
			Log.error(this.getClass().getName() + " - " + e.toString());
		}
		
		ploader.hide();
	}
	
	private void showHistory() {
		if( dni.getText().trim().equalsIgnoreCase("") || 
				card.getText().trim().equalsIgnoreCase("") ) {
			Commons.showGlobalMessage("Debe completar los campos D.N.I y Nœmero de Tarjeta para continuar");
		} else {
			synchronized (Application.getEventLock()) {
				UiApplication.getUiApplication().pushScreen( new History(dni.getText().trim(), card.getText().trim()) );
			}
		}
	}

	public void cacheReady(int msg, String message) {
		// TODO Auto-generated method stub
		
	}
}
