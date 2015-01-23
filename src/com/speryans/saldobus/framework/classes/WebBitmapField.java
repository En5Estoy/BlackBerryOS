package com.speryans.saldobus.framework.classes;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

public class WebBitmapField extends BitmapField implements WebDataCallback
{
	public static final int DOWNLOAD_READY = 499;
	public static final int DOWNLOAD_ERROR = 498;
	
	private EncodedImage bitmap = null;
	private ConnectionListener listener;

	public WebBitmapField(String url) {
		try {
			Tools.getWebData(url, this);
		} catch (Exception e) {}
	}
	
	public WebBitmapField(String url, long style) {
		super( null, style);
		try {
			Tools.getWebData(url, this);
		} catch (Exception e) {}
	}

	public Bitmap getBitmap() {
		if (bitmap == null) return null;
		return bitmap.getBitmap();
	}

	public void setBitmapUrl( String url ) {
		try
		{
			Tools.getWebData(url, this);
		}
		catch (Exception e) {}
	}
	
	public void setReadyListener( ConnectionListener l ) {
		listener = l;
	}
	
	public void callback(final String data) {
		if (data.startsWith("Exception")) return;

		try {
			byte[] dataArray = data.getBytes();
			bitmap = EncodedImage.createEncodedImage(dataArray, 0,
					dataArray.length);
			
			/*try {
				Tools.WriteFile( Commons.qrimage , data.getBytes());
			} catch( Exception e ) {
				Log.error("No se guardo la imagen. Excepcion: " + e.toString());
			}*/
			setImage(bitmap);
			
			if( listener != null ) listener.ready(DOWNLOAD_READY, "");
		} catch (final Exception e){
			if( listener != null ) listener.ready(DOWNLOAD_ERROR, "");
		}
	}
}