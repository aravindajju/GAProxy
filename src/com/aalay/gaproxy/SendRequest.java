package com.aalay.gaproxy;

import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * Creates the HTTP Request based on the URL passed.  Copies all the original http headers except Host.
 * Uses URLFetchService to send the request to GA 
 * 
 * @author aravind
 * 
 */
public class SendRequest {
	
	private static final Logger log = Logger.getLogger(SendRequest.class.getName());

	public static void sendRequestToGA(String urlString,
			HttpServletRequest origRequest) {

		try {
			
			//Use url fetch service
			URL url = new URL(urlString);
			URLFetchService ufls = URLFetchServiceFactory.getURLFetchService();
			HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET);
			
			//Copy the http headers from the client request
			Enumeration headerNames = origRequest.getHeaderNames();

			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				//Don't copy host -- request won't go to the intended destination
				if (headerName.equals("Host"))
					continue;
				HTTPHeader header = new HTTPHeader(headerName,
						origRequest.getHeader(headerName));
				log.info("header " + headerName + " - " + origRequest.getHeader(headerName));
				request.addHeader(header);
			}
			
			//add the client ip address, so that client address might show up in the Visitor stats
			//Apparently this doesn't work as per several GA proxy implementations in other languages
	        String clientIp = origRequest.getRemoteAddr();
	        if(clientIp!=null){
	        	HTTPHeader header = new HTTPHeader("X-Forwarded-For",
						clientIp);
	        	request.addHeader(header);
				log.info("Client IP Address " + clientIp);
	        }

	        //Fetch response	
			log.info("Final URL String = " + request.getURL().toString());
			HTTPResponse response = ufls.fetch(request);
			//Future ft = ufls.fetchAsync(request);
			//ft.isDone()
			log.info("Response Code = " + response.getResponseCode());

		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}

	}

}
