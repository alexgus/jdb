/**
 * 
 */
package fr.nikk.services.rest;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import fr.nikk.jdb.controller.NoteController;
import fr.nikk.services.AbstractService;

/**
 * @author Alexandre Guyon
 *
 */
public class RESTService extends AbstractService {

	private JAXRSServerFactoryBean server;
	
	private NoteController nc;

	/**
	 * 
	 */
	public RESTService() {
		this.nc = new NoteController();
	}

	/* (non-Javadoc)
	 * @see fr.nikk.services.AbstractService#start()
	 */
	@Override
	public void start() { // FIXME Conf with all annotated class
		this.server = new JAXRSServerFactoryBean();
		this.server.setResourceClasses(NoteController.class);
		this.server.setResourceProvider(NoteController.class, 
				new SingletonResourceProvider(this.nc));
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
	 * @return the nc
	 */
	public NoteController getNc() {
		return this.nc;
	}

}
