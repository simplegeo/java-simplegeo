package com.simplegeo.client.urlconn;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oauth.signpost.basic.DefaultOAuthConsumer;

public class OAuthURLConnection {
	
	private static Logger logger = Logger.getLogger(OAuthURLConnection.class.getName());
	
	private URL uri = null;
	
	private DefaultOAuthConsumer token = null;
	
	public OAuthURLConnection(URL uri) {
		this.uri = uri;	
	}

	public void performGet()
	{
		HttpURLConnection hpConn;
		try {
			hpConn = (HttpURLConnection) uri.openConnection();
			hpConn.setRequestMethod("GET");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
