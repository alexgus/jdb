/**
 * 
 */
package fr.nikk.services.http;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
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
	
	private WebAppContext context;
	
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
		this.context = new WebAppContext();
		this.context.setContextPath("/");
		this.context.setResourceBase(""); // ugly hack
		this.server.setHandler(this.context);
	}
	
	/**
	 * Add a servlet to this service
	 * @param servlet The servlet to add
	 * @param path The path to map this servlet on
	 */
	public void addServlet(Servlet servlet, String path){
		this.context.addServlet(new ServletHolder(servlet), path);
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
		return this.context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(WebAppContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		this.start();
	}

}
