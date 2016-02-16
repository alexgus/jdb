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
public class MainController extends AbstractService{

	private CouchdbService couch;
	
	private RESTService rest;
	
	private HttpService http;
	
	private NoteController note;
	
	/**
	 * 
	 */
	public MainController() {
		// nothing
	}

	@Override
	public void start() {
		this.couch = new CouchdbService();
		this.couch.start();
		
		this.note = new NoteController();
		NoteDAO ndao = new NoteDAO();
		ndao.setUp(this.couch.getSession());
		this.note.setDao(ndao);
		
		this.rest =  new RESTService();
		this.rest.addController(this.note);
		this.rest.start();
		
		this.http = new HttpService();
		this.http.addServlet(this.note.getBigDataController(), "/*");
		this.http.start();
	}

	@Override
	public void stop() {
		this.couch.stop();
		this.rest.stop();
	}

}
