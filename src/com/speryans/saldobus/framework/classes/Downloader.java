package com.speryans.saldobus.framework.classes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.system.Application;

import com.speryans.saldobus.framework.tools.Log;

public class Downloader extends Thread {
	public static final int ERROR = 500;
	
	private DownloaderListener invoker;
	private int msg;
	private String page;
	
	private HttpConnection conn = null;
	private InputStream inputStream = null;
	
	public Downloader( DownloaderListener listener, String url, int msg ) {
		this.invoker = listener;
		this.page = url;
		this.msg = msg;
	}
	
	public static Downloader createDownload( DownloaderListener listener, String url, int msg ) {
		return new Downloader( listener, url, msg );
	}
	
	public void stopDownload() {
		if( this.isAlive() ) {
			this.interrupt();
		
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception e) {}
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {}
		}
	}
	
	public void run() { // , String[] keys, String[] values
		conn = null;
		inputStream = null;
		try {
			String url = this.page;

			ConnectionFactory connFact = new ConnectionFactory();
			
	        ConnectionDescriptor connDesc;
	        connDesc = connFact.getConnection(url);
	        if (connDesc != null) {

			conn = (HttpConnection) connDesc.getConnection();

			int resp = conn.getResponseCode();
			
			Log.debug( "The response was: " + resp );

			if (resp == HttpConnection.HTTP_OK) {
				int total = 0;
				
				long contentLength = conn.getLength();
				
				inputStream = conn.openInputStream();
	        	byte[] responseData = new byte[10000];
	        	int length = 0;
	        	StringBuffer rawResponse = new StringBuffer();
	        	while (-1 != (length = inputStream.read(responseData))) {
	        		rawResponse.append(new String(responseData, 0, length));
	        		total += length;
					
					if( ( ( total * 100 ) / contentLength ) % 10 == 0 ) {
	                	synchronized (Application.getEventLock()) {
	                		synchronized( this ) {
	                			invoker.progressChanged(msg, contentLength, total);
	                		}
	                	};
	                }
	        	}

				String content = rawResponse.toString();
				invoker.downloadReady(msg, content);
				rawResponse = null;
			} else {
				invoker.downloadReady(ERROR, "Error en la conexion");
			}
	        } else {
	        	invoker.downloadReady(ERROR, "Error creando la conexion");
	        }
		} catch ( Exception e) {
			invoker.downloadReady(ERROR, e.toString());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				// swallow the cleanup failures
			}
		}
	}
}
