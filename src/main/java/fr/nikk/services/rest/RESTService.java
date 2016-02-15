/**
 * 
 */
package fr.nikk.services.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import fr.nikk.jdb.controller.Controller;
import fr.nikk.services.AbstractService;

/**
 * @author Alexandre Guyon
 *
 */
public class RESTService extends AbstractService {

	private JAXRSServerFactoryBean server;
	
	private List<Controller> lContr = new ArrayList<>();

	/* (non-Javadoc)
	 * @see fr.nikk.services.AbstractService#start()
	 */
	@Override
	public void start() { // FIXME Conf with all annotated class
		this.server = new JAXRSServerFactoryBean();
		for (Controller controller : this.lContr) {
			Class<?> controllerClass = controller.getClass();
			this.server.setResourceClasses(controllerClass);
			this.server.setResourceProvider(controllerClass, 
					new SingletonResourceProvider(controller));	
		}
		
		this.server.setAddress("http://localhost:9000/");
		this.server.create();
	}

	/* (non-Javadoc)
	 * @see fr.nikk.services.AbstractService#stop()
	 */
	@Override
	public void stop() {
		// FIXME Stop server
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

}
