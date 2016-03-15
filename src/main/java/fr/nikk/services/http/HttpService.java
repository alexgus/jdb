/**
 * 
 */
package fr.nikk.services.http;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import fr.nikk.services.Service;
/**
 * This class launch HTTP an server. (Jetty implementation)
 * @author Alexandre Guyon
 *
 */
public class HttpService implements Service {
	
	/**
	 * Default HTTP port to open the server
	 */
	public static final int DEFAULT_PORT = 8080;
	
	private Server server;
	
	private HandlerList hl;
	
	private ResourceHandler staticHandler;
	
	private WebAppContext servletContext;
	
	private Runnable serverThread = new Runnable() {
		
		@Override
		public void run() {
			try {
				HttpService.this.getServer().start();
				HttpService.this.getServer().join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}; 
	
	/**
	 * Default Constructor.
	 * Inititialize web server without launching it.
	 */
	public HttpService() {
		this.server = new Server(DEFAULT_PORT); // TODO Find a running instance of jetty and add handler to it
		
		// Add a web context for supporting servlet
		this.servletContext = new WebAppContext();
		this.servletContext.setContextPath("/");
		this.servletContext.setResourceBase(""); // ugly hack
		
		// Add static http
		this.staticHandler = new ResourceHandler();
		this.staticHandler.setResourceBase("./src/main/webapp"); // TODO param
		
		// merge handers
		this.hl = new HandlerList();
		this.hl.addHandler(this.staticHandler);
		this.hl.addHandler(this.servletContext);
		
		// Add it to server
		this.server.setHandler(this.hl);
	}
	
	/**
	 * Shortcut of addservlet, for proxy
	 * @param p Proxy class
	 * @param path Path to listen on
	 */
	public void addProxy(Proxy p, String path){
		this.addServlet(p, path);
	}
	
	/**
	 * Add a servlet to this service
	 * @param servlet The servlet to add
	 * @param path The path to map this servlet on
	 */
	public void addServlet(Servlet servlet, String path){
		this.servletContext.addServlet(new ServletHolder(servlet), path);
	}

	/* (non-Javadoc)
	 * @see fr.nikk.services.Service#start()
	 */
	@Override
	public void start() {
		Thread t = new Thread(this.serverThread);
		t.start();
	}

	/* (non-Javadoc)
	 * @see fr.nikk.services.Service#stop()
	 */
	@Override
	public void stop() {
		try {
			this.server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return this.server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the context
	 */
	public WebAppContext getContext() {
		return this.servletContext;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(WebAppContext context) {
		this.servletContext = context;
	}

	@Override
	public void run() {
		this.start();
	}

}
