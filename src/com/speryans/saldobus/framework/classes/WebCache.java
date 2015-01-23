package com.speryans.saldobus.framework.classes;

import java.io.DataOutputStream;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.speryans.saldobus.framework.tools.Base64Encoder;
import com.speryans.saldobus.framework.tools.Log;

public class WebCache {
	public static String read(String url) {
		try {
			Tools.CheckAppDirectory();
		} catch( Exception e ) {
			if( e.getMessage() == Tools.NO_SD ) {
				return "";
			}
		}
		
		String fileName = "";
		try {
			fileName = Commons.directory + "cache/" + WebCache.buildName(url);
		} catch( Exception e ) {
			return null;
		}
		
        InputStream input = null;
        FileConnection fconn = null;
        byte [] readBuffer = null;
        try {
            fconn = (FileConnection)Connector.open(fileName, Connector.READ);
            if (fconn.exists()) {
                long fileSize = fconn.fileSize();
                readBuffer = new byte [(int) fileSize];
                input = fconn.openInputStream();
                int readLen = input.read(readBuffer);
            } else {
            	return null;
            }
        } catch (Exception e) {
            System.out.println("Exception in File Processing" +
                               ".\nError Exception: " + e.toString());
            return null;
        } finally {
        	try {
                if ( input != null ) {
                    input.close();
                }
            } catch (Exception e) {
                System.out.println("Exception in file tidying : " + e.toString()); 
            }
            try {
                if ( fconn != null ) {
                   fconn.close();
                }
            } catch (Exception e) {
                System.out.println("Exception in file tidying : " + e.toString()); 
            }
        }
        return new String(readBuffer);
    }
	
	private static String buildName( String url ) {
		return Integer.toHexString( Math.abs( url.hashCode() ) );
	}
	
	public static void write( String url, String data ) {
		try {
			Tools.CheckAppDirectory();
		} catch( Exception e ) {
			if( e.getMessage() == Tools.NO_SD ) {
				return;
			}
		}
		
		//Base64Encoder b64 = new Base64Encoder( url ); 
		String filename = Commons.directory + "cache/" + WebCache.buildName(url);
		
		try {
			FileConnection fc = (FileConnection)Connector.open( filename );
			DataOutputStream writer = null;
			if( fc.exists() ) { fc.delete(); }
			
			try {
				fc.create();
				
				writer = fc.openDataOutputStream();
				
				writer.write(data.getBytes());
					
				writer.flush();
			} catch( Exception e ) {					
				Log.error( "Error reading cache: " + e.toString() );
			} finally {
				try {
					writer.close();
				} catch( Exception e ) {
					
				}
				try {
					fc.close();
				} catch( Exception e ) {
					
				}
			}
		} catch( Exception ex ) {
			Log.error("Error reading cache");
		}
	}
}
