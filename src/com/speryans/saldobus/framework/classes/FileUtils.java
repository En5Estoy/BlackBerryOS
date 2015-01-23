package com.speryans.saldobus.framework.classes;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.file.ExtendedFileConnection;

import com.speryans.saldobus.framework.tools.Log;

public class FileUtils {

	public static void write( String filename, byte[] data, boolean override ) {
		try {
			FileConnection fc = (FileConnection)Connector.open( filename );
			DataOutputStream writer = null;
			if( override ) {
				if( fc.exists() ) {
					fc.delete();
				}
			} else {
				if( fc.exists() ) {
					throw new IOException("File already exists");
				}
			}
			
			try {
				fc.create();
				
				writer = fc.openDataOutputStream();
					
				writer.write(data);
					
				writer.flush();
			} catch( Exception e ) {					
				Log.error( "Error writing: " + e.toString() );
			} finally {
				try {
					writer.close();
				}catch( Exception e ) {
					Log.error("Error closing OutputStream");
				}
				try {
					fc.close();
				}catch( Exception e ) {
					Log.error("Error closing FileConnection");
				}
			}
		} catch( Exception ex ) {
			Log.error("Error reading cache");
		}
	}
	
	public static void writeWithDrm( String filename, byte[] data, boolean override ) {
		try {
			ExtendedFileConnection fc = (ExtendedFileConnection)Connector.open( filename );
			fc.enableDRMForwardLock();
			
			DataOutputStream writer = null;
			if( override ) {
				if( fc.exists() ) {
					fc.delete();
				}
			} else {
				if( fc.exists() ) {
					throw new IOException("File already exists");
				}
			}
			
			try {
				fc.create();
				
				writer = fc.openDataOutputStream();
					
				writer.write(data);
					
				writer.flush();
			} catch( Exception e ) {					
				Log.error( "Error writing: " + e.toString() );
			} finally {
				try {
					writer.close();
				}catch( Exception e ) {
					Log.error("Error closing OutputStream");
				}
				try {
					fc.close();
				}catch( Exception e ) {
					Log.error("Error closing FileConnection");
				}
			}
		} catch( Exception ex ) {
			Log.error("Error reading cache");
		}
	}
}
