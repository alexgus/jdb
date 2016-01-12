/**
 * 
 */
package fr.nikk.services;

// TODO add dependencies to other services (exception too)

/**
 * @author Alexandre Guyon
 *
 */
public abstract class AbstractService implements Service {

	/**
	 * Configuration of the service
	 */
	protected Conf<?> config;

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
