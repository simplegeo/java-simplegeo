package com.simplegeo.client.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class SimpleGeoHttpRequestInterceptor implements HttpRequestInterceptor {
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		if (context.getAttribute("AuthHeader") != null) {
			request.removeHeaders("Authorization");
			request.addHeader((Header) context.getAttribute("AuthHeader"));
		}
	}
	
}
