package com.speryans.saldobus.framework.classes;

public interface DownloaderListener {
	public void downloadReady( int msg, String message );
	public void progressChanged( int msg, long length, long total );
}
