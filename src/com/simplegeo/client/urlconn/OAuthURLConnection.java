package com.simplegeo.client.urlconn;

import java.util.logging.Logger;

import com.simplegeo.client.http.OAuthClientIfc;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class OAuthURLConnection implements OAuthClientIfc {
	
	
	private static Logger logger = Logger.getLogger(OAuthURLConnection.class.getName());
	
	private OAuthConsumer token = null;
	
			
	/**
	 * @param connManager
	 * @param params
	 */
	public OAuthURLConnection() {
		super();
	}

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
		
		return token.getTokenSecret();
		
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
				logger.info(String.format("failure to create OAuth token %s,%s", key, secret));
			else
				logger.info(String.format("token was created with %s,%s", key, secret));
		}
		
	}
	
	public void sign (Object obj) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException
	{
		token.sign(obj);
	}
	
}
