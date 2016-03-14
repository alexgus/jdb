package fr.nikk.services.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
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
     * @param uri The target uri
	 * @see HttpServlet#HttpServlet()
     */
    public Proxy(String uri) {
        super();
        this.forwardTo = uri;
    }
    
    @SuppressWarnings("resource")
    /**
     * method // TODO enum !
     *  0 : get
     *  1 : post
     *  2 : delete
     *  3 : put
     */
    private void forwardRequest(HttpServletRequest request, HttpServletResponse response, int method) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// get request args
		String uri = request.getRequestURI();
		String r = "";
		if(uri.indexOf("/", 2) >= 0)
			r = uri.substring(uri.indexOf("/", 2), uri.length());
		
        // Second request
		String forwURI = this.forwardTo + "/" + r;
        HttpRequestBase httpreq = new HttpGet(forwURI);
        switch (method) {
			case 0:
				httpreq = new HttpGet(forwURI);
				break;
			case 1:
				httpreq = new HttpPost(forwURI);
				break;
			case 2:
				httpreq = new HttpDelete(forwURI);
				break;
			case 3:			
				httpreq = new HttpPut(forwURI);
				break;
			default:
				break;
		}
        CloseableHttpResponse response_get = httpclient.execute(httpreq);  
        HttpEntity entity_get = response_get.getEntity();            
        
        // write the response
        String resp = EntityUtils.toString(entity_get);
        
        PrintWriter writer = response.getWriter();
        writer.println(resp);
        
        httpclient.close();
        writer.close();
        response_get.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forwardRequest(request, response, 0);
	}
    
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forwardRequest(request, response, 2);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.forwardRequest(req, resp, 1);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.forwardRequest(req, resp, 3);
	}

}