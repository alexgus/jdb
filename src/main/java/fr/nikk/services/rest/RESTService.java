/**
 * 
 */
package fr.nikk.services.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import fr.nikk.jdb.controller.Controller;
import fr.nikk.services.AbstractService;

/**
 * @author Alexandre Guyon
 *
 */
public class RESTService extends AbstractService {

	private Server server;
	
	private List<Controller> lContr = new ArrayList<>();

	/* (non-Javadoc)
	 * @see fr.nikk.services.AbstractService#start()
	 */
	@Override
	public void start() { // FIXME Conf with all annotated class
		JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
		for (Controller controller : this.lContr) {
			Class<?> controllerClass = controller.getClass();
			serverFactory.setResourceClasses(controllerClass);
			serverFactory.setResourceProvider(controllerClass, 
					new SingletonResourceProvider(controller));	
		}
		
		serverFactory.setAddress("http://localhost:9000/"); // TODO Conf
		this.server = serverFactory.create();
	}

	/* (non-Javadoc)
	 * @see fr.nikk.services.AbstractService#stop()
	 */
	@Override
	public void stop() {
		this.server.stop();
	}
	
	/**
	 * Add controller to REST service. This method MUST be called before start !
	 * @param c The controller to set
	 */
	public void addController(Controller c){
		this.lContr.add(c);
	}
	
	/**
	 * Return the list of REST controller
	 * @return Annotated class which are REST controller
	 */
	public List<Controller> getController() {
		return this.lContr;
	}

	@Override
	public void run() {
		this.start();
	}

}
