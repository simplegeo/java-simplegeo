package com.simplegeo.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.SimpleGeoClient.HttpRequestMethod;

/**
 * A subclass of {@link org.apache.http.impl.client.DefaultHttpClient}
 * that signs requests using a given OAuth key and secret.
 * 
 * @author Derek Smith
 */
public class OAuthHttpClient implements OAuthClient {
	
	private static Logger logger = Logger.getLogger(OAuthHttpClient.class.getName());
	private OAuthConsumer token;
	
	/**
	 * Returns the consumer key that is used to sign Http requests.
	 * 
	 * @return the consumer key
	 */
	public String getKey() {
		return token.getConsumerKey();
	}

	/**
	 * Returns the consumer secret that is used to sign Http requests.
	 * 
	 * @return the consumer secret
	 */
	public String getSecret() {
		return token.getConsumerSecret();
	}
	
	/**
	 * Set the key/secret pair that will be used to sign Http
	 * requests.
	 * 
	 * @param key the consumer key
	 * @param secret the secret key
	 */
	public void setToken(String key, String secret) {
		if(key != null && secret != null) {
			token  = new DefaultOAuthConsumer(key, secret);
			if(token == null)
				logger.info(String.format(Locale.US, "Failed to created OAuth token."));
			else
				logger.info(String.format(Locale.US, "Successfully created OAuth token."));
		}
	}
	
	/**
	 * Signs the Http request with the registered token before execution.
	 * 
	 * @param urlString The url that the request should be sent to.
	 * @param jsonPayload The json string that should be sent along with POSTs and PUTs.
	 * @param responseHandler the handler that will be used on a successful
	 * response
	 * @return String
	 * @throws OAuthMessageSignerException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String executeOAuthRequest(String urlString, HttpRequestMethod method, String jsonPayload, SimpleGeoHandler responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, ClientProtocolException, IOException {
		HttpsURLConnection connection = null;
		InputStream response = null;
		while (true) {
			connection = buildRequest(urlString, method);
			response = makeRequest(connection, method, jsonPayload);
			if (connection.getResponseCode() != 301 && connection.getResponseCode() != 302) {
				break;
			}
			// It's a redirect, rebuild the connection from the response
			urlString = connection.getHeaderField("Location");
			
			connection = null;
			response = null;
		}
		return responseHandler.handleResponse(response, connection.getResponseCode());
	}
	
	private HttpsURLConnection buildRequest(String urlString, HttpRequestMethod method) 
			throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException, ClientProtocolException, IOException {
		HttpsURLConnection connection = (HttpsURLConnection) new URL(urlString).openConnection();
		connection.setRequestProperty("User-Agent", "SimpleGeo Java Client");
		connection.setInstanceFollowRedirects(false);
		switch (method) {
			case GET:
				connection.setRequestMethod("GET");
				break;
			case POST:
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestMethod("POST");
				break;
			case PUT:
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestMethod("PUT");
				break;
			case DELETE:
				connection.setRequestMethod("DELETE");
				break;
			default:
				return null;
		}
		this.token.sign(connection);
		return connection;
	}
	
	private InputStream makeRequest(HttpURLConnection connection, HttpRequestMethod method, String jsonPayload) throws IOException {
		if (method == HttpRequestMethod.PUT || method == HttpRequestMethod.POST) {
			OutputStream os = connection.getOutputStream();
			os.write(jsonPayload.getBytes("UTF-8"));
			os.close();
		}
		InputStream response = null;
		try {
			response = connection.getInputStream();
		} catch (IOException e) {
			response = connection.getErrorStream();
		}
		return response;
	}
	
}
