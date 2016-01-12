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
	
	public void setUp(org.lightcouch.CouchDbClient s) {
		super.setUp(s, Note.class);
		this.available_actions.add(AbstractDAO.ACTION_SAVE);
		this.available_actions.add(AbstractDAO.ACTION_LIST);
		this.available_actions.add(AbstractDAO.ACTION_LISTBY);
		
		this.available_criteria.add("Tag"); // TODO CONST
		this.available_criteria.add("Date");
	}

}
