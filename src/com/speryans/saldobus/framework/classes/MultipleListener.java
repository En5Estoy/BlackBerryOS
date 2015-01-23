package com.speryans.saldobus.framework.classes;

public interface MultipleListener {
	public void partDownloadFinished( int msg );
	public void downloadPartReady( String url, String message );
	public void partProgressChanged( long length, long total, int cant );
	public void downloadsCanceled();
}
