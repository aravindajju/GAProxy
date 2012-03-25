package com.aalay.gaproxy;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Builds the URL GA requests.   No instance level state, hence modeled as static
 * 
 * @author aravind
 *
 */
public class GAURLBuilder {

	private static final Logger log = Logger.getLogger(GAURLBuilder.class.getName());
	
	/**
	 * GA url for the request
	 */
	private static String 	gaImg = "http://www.google-analytics.com/__utm.gif";


	
	public static String makeURL(HttpServletRequest request){
		
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(gaImg + "?");
		
		String utmac=request.getParameter("account");
		sb.append("utmac=" + utmac);

		// cookie data
        String utmcsr = "(direct)";
        String utmccn = "(direct)";
        String utmctr = null;
        String utmcmd = "(none)";
        String utmcct = null;
        int hostNameHash = 999;
        int visitorId = new SecureRandom().nextInt() & 0x7FFFFFFF; //number under 2147483647
        long currTime=System.currentTimeMillis();
        long timeStampFirst = currTime;
        long timeStampPrevious=currTime;
        long timeStampCurrent=currTime;
        int visits=1;
        
        sb.append("&utmcc=__utma%3D"+hostNameHash+"."+visitorId+"."+timeStampFirst+"."+timeStampPrevious+"."+timeStampCurrent+"."+ visits + "%3B%2B__utmz%3D"+hostNameHash+"."+currTime+".1.1.utmcsr%3D"+utmcsr+"%7Cutmccn%3D"+utmccn+"%7Cutmcmd%3D"+utmcmd+(utmctr != null?"%7Cutmctr%3D"+utmctr:"")+(utmcct != null?"%7Cutmcct%3D"+utmcct:"")+"%3B&gaq=1");

        //Other variables.  Copied from various proxy implementations
		sb.append("&utmcr=1");
		
		sb.append("&?utmwv=4.7.2");
		
        sb.append("&utmn=" + visitorId); // random int so no caching
        
        String hostName = request.getParameter("host");
        sb.append("&utmhn=" + hostName); //name of the host
        
        sb.append("&utmcs=-"); // encoding

        sb.append("&utmsr=-"); // resolution

        sb.append("&utmsc=-"); // deapth
        
        sb.append("&utmul=-"); // deapth
        
        sb.append("&utmje=-"); //java 
        
        sb.append("&utmfl=-"); //java
        
        sb.append("&utmdt=" + request.getParameter("pageurl")); //page title
        
        sb.append("&utmhid=" + visitorId); //adsense related
        
        
        
        String ref = (String)request.getAttribute("HTTP_REFERER");
        sb.append("&utmr=" + ref); //referer
        
        String pageURL = request.getRequestURI();
        sb.append("&utmp=" + request.getParameter("pageurl")); //page url

        String clientIp = request.getRemoteAddr();
        if (clientIp !=null) sb.append("&utmip=" + clientIp);  //remote ip address

        //return the URL	
        return sb.toString();
		
	}

	/*
utmac 	Account String. Appears on all requests. 	utmac=UA-2202604-2
utmcc
	Cookie values. This request parameter sends all the cookies requested from the page.
	utmcc=__utma%3D117243.1695285.22%3B%2B __utmz%3D117945243.1202416366.21.10. utmcsr%3Db%7C utmccn%3D(referral)%7C utmcmd%3Dreferral%7C utmcct%3D%252Fissue%3B%2B
utmcn 	Starts a new campaign session. Either utmcn or utmcr is present on any given request. Changes the campaign tracking data; but does not start a new session
	utmcn=1
utmcr
	Indicates a repeat campaign visit. This is set when any subsequent clicks occur on the same link. Either utmcn or utmcr is present on any given request.
	utmcr=1
utmcs
	Language encoding for the browser. Some browsers don't set this, in which case it is set to "-"
	utmcs=ISO-8859-1
utmdt
	Page title, which is a URL-encoded string. 	utmdt=analytics%20page%20test
utme 	Extensible Parameter 	Value is encoded. Used for events and custom variables.
utmfl
	Flash Version 	utmfl=9.0%20r48&
utmhn

	Host Name, which is a URL-encoded string. 	utmhn=x343.gmodules.com
utmhid

	A random number used to link Analytics GIF requests with AdSense. 	utmhid=2059107202
utmipc
	Product Code. This is the sku code for a given product.

	utmipc=989898ajssi
utmipn
	Product Name, which is a URL-encoded string. 	utmipn=tee%20shirt
utmipr
	Unit Price. Set at the item level. Value is set to numbers only in U.S. currency format.
	utmipr=17100.32
utmiqt
	Quantity. 	utmiqt=4
utmiva
	Variations on an item. For example: large, medium, small, pink, white, black, green. String is URL-encoded.
	utmiva=red;
utmje
	Indicates if browser is Java-enabled. 1 is true. 	utmje=1
utmn
	Unique ID generated for each GIF request to prevent caching of the GIF image. 	utmn=1142651215
utmp
	Page request of the current page. 	utmp=/testDirectory/myPage.html
utmr
	Referral, complete URL. 	utmr=http://www.example.com/aboutUs/index.php?var=selected
utmsc
	Screen color depth 	utmsc=24-bit
utmsr
	Screen resolution 	utmsr=2400x1920&
utmt
	Indicates the type of request, which is one of: event, transaction, item, or custom variable. If this value is not present in the GIF request, the request is typed as page. 	utmt=event
utmtci
	Billing City 	utmtci=San%20Diego
utmtco
	Billing Country 	utmtco=United%20Kingdom
utmtid
	Order ID, URL-encoded string. 	utmtid=a2343898
utmtrg
	Billing region, URL-encoded string. 	utmtrg=New%20Brunswick
utmtsp
	Shipping cost. Values as for unit and price. 	utmtsp=23.95
utmtst
	Affiliation. Typically used for brick and mortar applications in ecommerce. 	utmtst=google%20mtv%20store
utmtto
	Total. Values as for unit and price. 	utmtto=334.56
utmttx
	Tax. Values as for unit and price. 	utmttx=29.16
utmul
	Browser language. 	utmul=pt-br
utmwv
	Tracking code version 	utmwv=1
	 */
	
}
