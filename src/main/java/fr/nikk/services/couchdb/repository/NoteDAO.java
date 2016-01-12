/**
 * 
 */
package fr.nikk.services.couchdb.repository;

import fr.nikk.model.note.Note;

/**
 * @author Alexandre Guyon
 *
 */
public class NoteDAO extends AbstractDAO<Note> {
	
	/**
	 * Define tag criteria
	 */
	public static final String CRITERIA_TAG = "Tag";
	
	/**
	 * Define date criteria
	 */
	public static final String CRITERIA_DATE = "Date";
	
	public void setUp(org.lightcouch.CouchDbClient s) {
		super.setUp(s, Note.class);
		this.available_actions.add(AbstractDAO.ACTION_SAVE);
		this.available_actions.add(AbstractDAO.ACTION_LIST);
		this.available_actions.add(AbstractDAO.ACTION_LISTBY);
		
		this.available_criteria.add(NoteDAO.CRITERIA_TAG);
		//this.available_criteria.add(NoteDAO.CRITERIA_DATE);
	}

}
