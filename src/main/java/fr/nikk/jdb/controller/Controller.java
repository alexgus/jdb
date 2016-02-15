/**
 * 
 */
package fr.nikk.jdb.controller;

import fr.nikk.services.AbstractService;
import fr.nikk.services.couchdb.CouchdbService;
import fr.nikk.services.couchdb.repository.NoteDAO;
import fr.nikk.services.http.HttpService;
import fr.nikk.services.rest.RESTService;

/**
 * @author Alexandre Guyon
 *
 */
public class Controller extends AbstractService{

	private CouchdbService couch;
	
	private RESTService rest;
	
	private HttpService http;
	
	private NoteDAO note;
	
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
		
		this.note = new NoteDAO();
		this.note.setUp(this.couch.getSession());
		
		this.rest =  new RESTService();
		this.rest.getNc().setDao(this.note);
		this.rest.start();
		
		this.http = new HttpService();
		this.http.addServlet(NoteController.class, "/note");
		this.http.start();
	}

	@Override
	public void stop() {
		this.couch.stop();
		this.rest.stop();
	}

}
