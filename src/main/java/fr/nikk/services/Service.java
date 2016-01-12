/**
 * 
 */
package fr.nikk.services;

// TODO implement Runnable

/**
 * Define how service mean to be used
 * 
 * @author Alexandre Guyon
 *
 */
public interface Service {
	
	/**
	 * Start the service
	 */
	public void start();
	
	/**
	 * Stop the service
	 */
	public void stop();
	
}
