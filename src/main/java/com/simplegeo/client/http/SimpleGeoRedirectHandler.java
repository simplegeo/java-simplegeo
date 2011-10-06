package com.simplegeo.client.http;

import java.net.URI;
import java.net.URISyntaxException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.simplegeo.client.SimpleGeoClient.HttpRequestMethod;

public class SimpleGeoRedirectHandler implements RedirectHandler {

	@Override
	public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
		String urlString = response.getFirstHeader("Location").getValue();
		URI uri;
		try {
			uri = new URI(urlString);
		} catch (URISyntaxException e) {
			throw new ProtocolException("Could not parse redirect uri.");
		}
		
		HttpUriRequest request = OAuthHttpClient.buildRequest(urlString, HttpRequestMethod.GET, response.getEntity().toString());
		
		OAuthConsumer token = new CommonsHttpOAuthConsumer((String) context.getAttribute("consumerKey"),
				(String) context.getAttribute("consumerSecret"));

		synchronized(this) {
			try {
				token.sign(request);
			} catch (OAuthMessageSignerException e) {
				throw new ProtocolException("Could not sign request!");
			} catch (OAuthExpectationFailedException e) {
				throw new ProtocolException("Could not sign request!");
			} catch (OAuthCommunicationException e) {
				throw new ProtocolException("Could not sign request!");
			}
		}

		// Apache puts original headers back on the request, so we need to pass this along to the RequestInterceptor to be re-added.
		context.setAttribute("AuthHeader", request.getFirstHeader("Authorization"));
		
		return uri;
	}

	@Override
	public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 301 || statusCode == 302) return true;
		return false;
	}

}
