package com.speryans.saldobus.framework.classes;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;

import com.speryans.saldobus.framework.tools.Log;


public class Connection extends Thread {
	public static final int ERROR = 500;
	
	public static final String POST = "POST";
	public static final String GET = "GET";

	private String ConnectionType;
	private String page;
	private ConnectionListener invoker;

	private int msg;

	private Hashtable values;

	public Connection(String ConnectionType, String page, ConnectionListener invoker, int msg,
			Hashtable values) {
		this.ConnectionType = ConnectionType;
		
		this.page = page;
		this.invoker = invoker;

		this.msg = msg;

		this.values = values;
	}

	public static Connection getInstance(String page, ConnectionListener invoker, int msg) {
		return new Connection(Connection.GET, page, invoker, msg, null);
	}
	
	public static Connection postInstance(String page, ConnectionListener invoker, int msg, Hashtable values) {
		return new Connection(Connection.POST, page, invoker, msg, values);
	}

	public void run() { // , String[] keys, String[] values
		HttpConnection conn = null;
		InputStreamReader isr = null;
		OutputStream out = null;
		try {
			String url = this.page;
			
			String cache = WebCache.read( this.page );
			if( cache != null ) {
				invoker.cacheReady(msg, cache);
			}

			ConnectionFactory connFact = new ConnectionFactory();
			
			connFact.setPreferredTransportTypes( new int[]{ TransportInfo.TRANSPORT_TCP_WIFI, TransportInfo.TRANSPORT_BIS_B, TransportInfo.TRANSPORT_MDS } );
			connFact.setAttemptsLimit(3);
			connFact.setTimeLimit(20);
			
	        ConnectionDescriptor connDesc;
	        connDesc = connFact.getConnection(url);
	        if (connDesc != null) {

			conn = (HttpConnection) connDesc.getConnection();
			
			
			if( this.ConnectionType == Connection.GET ) {
				conn.setRequestMethod(HttpConnection.GET);
				conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
			} else {
				byte[] postData = this.convertToUrlencoded(this.values);
				
				conn.setRequestMethod(HttpConnection.POST);
		        conn.setRequestProperty( HttpProtocolConstants.HEADER_CONTENT_LENGTH, String.valueOf(postData.length));
		        
		        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		        //conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

		        out = conn.openOutputStream();
		        out.write(postData);
		        //out.flush();
			}

			int resp = conn.getResponseCode();
			
			Log.debug( "The response was: " + resp );

			if (resp == HttpConnection.HTTP_OK) {
				//int total = 0;
				int size = 1024;
				char[] buffer = new char[size];
				int len;

				isr = new InputStreamReader(conn.openInputStream(), "UTF-8");
				StringBuffer sbuf = new StringBuffer();

				while ((len = isr.read(buffer, 0, size)) > 0) { // &&
																// !m_stopThread
					sbuf.append(buffer, 0, len);
					//total += len;
				}

				String content = sbuf.toString();
				WebCache.write(this.page, content);
				invoker.ready(msg, content);
			} else {
				invoker.ready(ERROR, "Error en la conexion");
			}
	        } else {
	        	invoker.ready(ERROR, "Error creando la conexion");
	        }
		} catch ( Exception e) {
			invoker.ready(ERROR, e.toString());
		} finally {
			try {
				if (isr != null) {
					isr.close();
					isr = null;
				}
				if (out != null) {
					out.close();
					out = null;
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

	public byte[] convertToUrlencoded(Hashtable data) {
		URLEncodedPostData keyvalpairs = new URLEncodedPostData(
				URLEncodedPostData.DEFAULT_CHARSET, false);
		Enumeration enumeration = data.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			keyvalpairs.append(key, (String) data.get(key));
		}

		Log.debug(keyvalpairs.toString());
		
		return keyvalpairs.getBytes();

	}
}
