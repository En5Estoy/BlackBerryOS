package com.speryans.saldobus.framework.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.ui.UiApplication;

import com.speryans.saldobus.framework.rpc.json.me.JSONException;
import com.speryans.saldobus.framework.rpc.json.me.JSONObject;
import com.speryans.saldobus.framework.tools.Log;

public class Tools {
	public static final String NO_SD = "NO_SD_CARD";

	public static void getWebData(final String url,
			final WebDataCallback callback) throws IOException {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpConnection connection = null;
				InputStream inputStream = null;

				try {

					// String mdsurl = url;
					// if( !Commons.isDebug ) mdsurl +=
					// ";deviceside=false;ConnectionType=mds-public";

					ConnectionFactory connFact = new ConnectionFactory();

					connFact.setAttemptsLimit(5);
					connFact.setTimeLimit(25);
					
					ConnectionDescriptor connDesc;
					connDesc = connFact.getConnection(url);
					if (connDesc != null) {
						connection = (HttpConnection) connDesc.getConnection();

						// connection = (HttpConnection) Connector.open(mdsurl,
						// Connector.READ, true);

						inputStream = connection.openInputStream();
						byte[] responseData = new byte[10000];
						int length = 0;
						StringBuffer rawResponse = new StringBuffer();
						while (-1 != (length = inputStream.read(responseData))) {
							rawResponse.append(new String(responseData, 0,
									length));
						}
						int responseCode = connection.getResponseCode();
						if (responseCode != HttpConnection.HTTP_OK) {
							throw new IOException("HTTP response code: "
									+ responseCode);
						}

						final String result = rawResponse.toString();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {
									public void run() {
										callback.callback(result);
									}
								});
					}
				} catch (final Exception ex) {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									callback.callback("Exception ("
											+ ex.getClass() + "): "
											+ ex.getMessage());
								}
							});
				} finally {
					try {
						inputStream.close();
					} catch (Exception e) {
					}
					try {
						connection.close();
					} catch (Exception e) {
					}
				}
			}
		});
		t.start();
	}

	public static boolean checkSDCard() {
		String root = null;
		Enumeration e = FileSystemRegistry.listRoots();
		while (e.hasMoreElements()) {
			root = (String) e.nextElement();

			if (root.equalsIgnoreCase("sdcard/")) {
				return true;
			}
		}
		return false;
	}

	public static void CheckAppDirectory() throws IOException {
		if (!Tools.checkSDCard()) {
			throw new IOException(NO_SD);
		}
		// Base Directory
		FileConnection directory = (FileConnection) Connector.open(
				Commons.directory, Connector.READ_WRITE);
		if (!directory.exists()) {
			directory.mkdir();
		}
		// Cache directory
		FileConnection cachedirectory = (FileConnection) Connector.open(
				Commons.directory + "cache/", Connector.READ_WRITE);
		if (!cachedirectory.exists()) {
			cachedirectory.mkdir();
		}
	}

	public static void WriteFile(String fileName, byte[] data) {
		FileConnection fconn = null;

		try {
			CheckAppDirectory();
		} catch (IOException e) {
			System.out.print("Error creating directory");
		}

		try {
			fconn = (FileConnection) Connector.open(fileName,
					Connector.READ_WRITE);

			// if (fconn.exists()) {
			try {
				fconn.delete();

				try {
					fconn.create();

					OutputStream out = null;
					try {
						out = fconn.openOutputStream();

						try {
							out.write(data);

							try {
								fconn.close();
							} catch (IOException e) {
								System.out.print("Error closing file");
							}
						} catch (IOException e) {
							System.out.print("Error writing to output stream");
						}
					} catch (IOException e) {
						System.out.print("Error opening output stream");
					}
				} catch (IOException e) {
					System.out.print("Error creating file");
				}
			} catch (IOException e) {
				System.out.print("Error deleting file");
			}
			// }
		} catch (IOException e) {
			System.out.print("Error opening file");
		}
	}

	private static void checkKeyStoreDirectory(String directory)
			throws IOException {
		FileConnection dir = (FileConnection) Connector.open(directory,
				Connector.READ_WRITE);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public static void writeKeyStore(String json) {
		FileConnection fconn = null;

		byte[] data = json.getBytes();

		String directory = Commons.directory;
		String filename = Integer.toHexString(Math.abs(Commons.appName.hashCode())) + ".json";
		try {
			checkKeyStoreDirectory(directory);
		} catch (IOException e) {
			System.out.print("Error creating directory");
		}

		try {
			FileConnection fc = (FileConnection) Connector.open( directory + filename );
			// If no exception is thrown, then the URI is valid, but the file
			// may or may not exist.
			if (!fc.exists()) {
				fc.create(); // create the file if it doesn't exist
			}
			OutputStream outStream = fc.openOutputStream();
			outStream.write( data );
			outStream.close();
			fc.close();
		} catch (IOException ioe) {
			Log.error( ioe.getMessage() );
		}
	}

	public static JSONObject readKeyStore() throws JSONException {

		String directory = Commons.directory;
		String filename = Integer.toHexString(Math.abs(Commons.appName
				.hashCode())) + ".json";

		try {
			checkKeyStoreDirectory(directory);
		} catch (IOException e) {
			System.out.print("Error creating directory");
		}

		String fileName = directory + filename;

		InputStream input = null;
		FileConnection fconn = null;
		byte[] readBuffer = null;
		try {
			fconn = (FileConnection) Connector.open(fileName, Connector.READ);
			if (fconn.exists()) {
				long fileSize = fconn.fileSize();
				readBuffer = new byte[(int) fileSize];
				input = fconn.openInputStream();
				int readLen = input.read(readBuffer);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.error("Exception in File Processing.\nError Exception: "
					+ e.toString());
			return null;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				Log.error("Exception in file tidying : " + e.toString());
			}
			try {
				if (fconn != null) {
					fconn.close();
				}
			} catch (Exception e) {
				Log.error("Exception in file tidying : " + e.toString());
			}
		}
		return new JSONObject(new String(readBuffer));
	}
}
