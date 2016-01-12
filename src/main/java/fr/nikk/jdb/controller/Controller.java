/**
 * 
 */
package fr.nikk.jdb.controller;

import fr.nikk.services.AbstractService;
import fr.nikk.services.couchdb.CouchdbService;

/**
 * @author Alexandre Guyon
 *
 */
public class Controller extends AbstractService{

	private CouchdbService couch;
	
	/**
	 * 
	 */
	public Controller() {
		// nothing
	}

	@Override
	public void start() {
		this.couch = new CouchdbService();
		this.couch.start();
	}

	@Override
	public void stop() {
		this.couch.stop();
	}

}
