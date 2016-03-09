/**
 * 
 */
package fr.nikk.services;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Guyon
 *
 */
public abstract class AbstractService implements Service {

	/**
	 * Configuration of the service
	 */
	protected Conf<?> config;
	
	/**
	 * Service depending on other services
	 */
	protected List<Service> dependencies = new ArrayList<>();

	/* (non-Javadoc)
	 * @see fr.utbm.to52.smarthome.services.Service#start()
	 */
	@Override
	public abstract void start();

	/* (non-Javadoc)
	 * @see fr.utbm.to52.smarthome.services.Service#stop()
	 */
	@Override
	public abstract void stop();
	
}
