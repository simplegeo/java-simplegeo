package com.simplegeo.client.urlconn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.simplegeo.client.AbsSimpleGeoClient;
import com.simplegeo.client.SimpleGeoClientIfc;
import com.simplegeo.client.handler.SimpleGeoJSONHandlerIfc;
import com.simplegeo.client.http.OAuthClientIfc;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.NoSuchRecordException;
import com.simplegeo.client.http.exceptions.NotAuthorizedException;

public class SimpleGeoURLConnClient extends AbsSimpleGeoClient {
	
	/* Status codes */
	public static final int GET_SUCCESS = 200;
	public static final int PUT_SUCCESS = 202;
	public static final int BAD_REQUEST = 400;
	public static final int NO_SUCH = 404;
	public static final int NOT_AUTHORIZED = 401;
	
	protected OAuthURLConnection urlConn = null;
	
	/**
	 * @return the shared instance of this class
	 */
	static public SimpleGeoClientIfc getInstance() {
		
		if(sharedLocationService == null)
			sharedLocationService = new SimpleGeoURLConnClient();
		
		return sharedLocationService;
	}
	
	private SimpleGeoURLConnClient() {
		
		super();

		this.urlConn = new OAuthURLConnection();
	}

	/**
	 * @return the Http client used to execute all requests
	 */
	public OAuthClientIfc getHttpClient() {
		return this.urlConn;
	}
	
	protected Object execute(String uri, String request, String payload, SimpleGeoJSONHandlerIfc handler)
									throws IOException {
		
		logger.info(String.format("sending %s", request.toString()));
		
		URL url = new URL (uri);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(request);
		conn.setDoInput(true);
		try {
			urlConn.sign(conn);
		} catch (OAuthMessageSignerException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthExpectationFailedException e) {
			dealWithAuthorizationException(e);	
		} catch (OAuthCommunicationException e) {
			dealWithAuthorizationException(e);
		};
		
		if (payload != null)
		{
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", 
		       "application/x-www-form-urlencoded");
		      DataOutputStream wr = new DataOutputStream (
		              conn.getOutputStream ());
		      wr.writeBytes (payload);
		      wr.flush ();
		      wr.close ();
		}
		
	
		
			switch(conn.getResponseCode()) {
			
			case GET_SUCCESS:
			case PUT_SUCCESS:
				//
				// Fall through and continue
				//
				break;
			case BAD_REQUEST:
				throw new APIException (conn.getResponseCode(), null);
			case NO_SUCH:
				throw new NoSuchRecordException(conn.getResponseCode(), null);
			case NOT_AUTHORIZED:
				throw new NotAuthorizedException (conn.getResponseCode(), null);
			default:
				throw new APIException (conn.getResponseCode(), null);
		
		}		
		//Get Response	
		InputStream is = conn.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
		  response.append(line);
		  response.append('\r');
		}
		rd.close();
  
	    return handler.parseResponse(response.toString());
		
	}
	
	@Override
	protected Object executeGet(String uri, SimpleGeoJSONHandlerIfc handler)
			throws IOException {
		return execute (uri, "GET", null, handler);
	}

	@Override
	protected Object executePost(String uri, String jsonPayload,
			SimpleGeoJSONHandlerIfc handler) throws IOException {
		
		return execute(uri, "POST", jsonPayload, handler);
	}

	@Override
	protected Object executeDelete(String uri, SimpleGeoJSONHandlerIfc handler)
			throws IOException {

		return execute(uri, "DELETE", null, handler);
	}
}
