/**
 * 
 */
package fr.nikk.services.couchdb;

import org.lightcouch.CouchDbClient;

import fr.nikk.services.AbstractService;

/**
 * @author Alexandre Guyon
 *
 */
public class CouchdbService extends AbstractService {

	private CouchDbClient session;

	/**
	 * Create a couchdb service
	 */
	public CouchdbService() {

	}
	
	/* (non-Javadoc)
	 * @see fr.utbm.to52.smarthome.services.AbstractService#start()
	 */
	@SuppressWarnings("unused")
	@Override
	public void start() {
		try{
			this.session = new CouchDbClient();

		}catch(org.lightcouch.CouchDbException e){
			System.err.println("No couchdb server found. Service not started");
		}
	}

	/* (non-Javadoc)
	 * @see fr.utbm.to52.smarthome.services.AbstractService#stop()
	 */
	@Override
	public void stop() {
		this.session.shutdown();
	}

	/**
	 * @return the client
	 */
	public CouchDbClient getSession() {
		return this.session;
	}

}
