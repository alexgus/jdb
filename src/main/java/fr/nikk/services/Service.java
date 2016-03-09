/**
 * 
 */
package fr.nikk.services;

/**
 * Define how service mean to be used
 * 
 * @author Alexandre Guyon
 *
 */
public interface Service  extends Runnable {
	
	/**
	 * Start the service
	 */
	public void start();
	
	/**
	 * Stop the service
	 */
	public void stop();
	
}
