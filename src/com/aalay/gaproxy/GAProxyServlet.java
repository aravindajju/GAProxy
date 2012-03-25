package com.aalay.gaproxy;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.logging.Logger;


/**
 * This servlet acts as a proxy for the GA __uta.gif file.
 * The servlet calls the GAURLBuild to create the URL to be sent to
 * GA.
 * 
 * @author aravind
 *
 */
@SuppressWarnings("serial")
public class GAProxyServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(GAProxyServlet.class.getName());
    
    //Primary implementation with redirect
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//Create the URL
		String url = GAURLBuilder.makeURL(req);
		log.info("URL returned from the URL bulider - " + url);
		//Send the redirect
		resp.sendRedirect(url);
			
		
	}
	
	//Alternative implementation without redirect.  Uses URLFetch service
	//to make the call.  Doesn't record the client IP
	public void doGet2(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//Create the URL
		String url = GAURLBuilder.makeURL(req);
		
		log.info("URL returned from the URL bulider - " + url);
		//Send the request to GA
		SendRequest.sendRequestToGA(url, req);
		
		
		resp.setContentType("image/gif");
		RequestDispatcher dispatcher = req.getRequestDispatcher("/__utm.gif");
		try {
			dispatcher.forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			log.severe(e.getMessage());
			e.printStackTrace();
		}
			
		
	}
}
