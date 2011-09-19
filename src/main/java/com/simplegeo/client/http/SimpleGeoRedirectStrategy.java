package com.simplegeo.client.http;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.simplegeo.client.SimpleGeoClient.HttpRequestMethod;

public class SimpleGeoRedirectStrategy implements RedirectStrategy {
	
	@Override
	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
		HttpUriRequest newRequest = createNewRequest(request, response);
		if (newRequest == null) throw new ProtocolException("Unable to create a redirect request.");
		
		OAuthConsumer token = new CommonsHttpOAuthConsumer((String) context.getAttribute("consumerKey"),
				(String) context.getAttribute("consumerSecret"));
		
		synchronized(this) {
			try {
				token.sign(newRequest);
			} catch (OAuthMessageSignerException e) {
				throw new ProtocolException("Could not sign request!");
			} catch (OAuthExpectationFailedException e) {
				throw new ProtocolException("Could not sign request!");
			} catch (OAuthCommunicationException e) {
				throw new ProtocolException("Could not sign request!");
			}
		}
		
		// Apache puts original headers back on the request, so we need to pass this along to the RequestInterceptor to be re-added.
		context.setAttribute("AuthHeader", newRequest.getFirstHeader("Authorization"));
		
		return newRequest;
	}

	@Override
	public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 301 || statusCode == 302) return true;
		return false;
	}
	
	private HttpUriRequest createNewRequest(HttpRequest request, HttpResponse response) {
		HttpRequestMethod method = HttpRequestMethod.valueOf(request.getRequestLine().getMethod());
		String urlString = response.getFirstHeader("Location").getValue();
		return OAuthHttpClient.buildRequest(urlString, method, response.getEntity().toString());
	}

}
