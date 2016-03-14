package fr.nikk.services.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Servlet implementation class Proxy
 */
public class Proxy extends HttpServlet {
	private static final long serialVersionUID = 6729550776774642774L;

	private final String forwardTo; 
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Proxy(String uri) {
        super();
        forwardTo = uri;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// get request args
		String uri = request.getRequestURI();
		String r = "";
		if(uri.indexOf("/", 2) >= 0)
			r = uri.substring(uri.indexOf("/", 2), uri.length());
		
        // Second request: GET
        HttpGet httpGet = new HttpGet(this.forwardTo + "/" + r);
        CloseableHttpResponse response_get = httpclient.execute(httpGet);  
        HttpEntity entity_get = response_get.getEntity();            
        
        // write the response
        String resp = EntityUtils.toString(entity_get);
        
        PrintWriter writer = response.getWriter();
        writer.println(resp);
        
        response_get.close();           
	}

}