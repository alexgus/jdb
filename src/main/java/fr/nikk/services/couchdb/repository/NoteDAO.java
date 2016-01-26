/**
 * 
 */
package fr.nikk.services.couchdb.repository;

import java.io.InputStream;

import org.lightcouch.CouchDbClient;

import fr.nikk.model.note.Note;
import fr.nikk.util.BasicIO;

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

	public void setUp(CouchDbClient s) {
		super.setUp(s, Note.class);
		
		// Set available basics actions on this DAO
		this.available_actions.add(AbstractDAO.ACTION_SAVE);
		this.available_actions.add(AbstractDAO.ACTION_LIST);
		this.available_actions.add(AbstractDAO.ACTION_LISTBY);
		this.available_actions.add(AbstractDAO.ACTION_DELETE);
		this.available_actions.add(AbstractDAO.ACTION_UPDATE);

		// Set the search criteria (only one supported for now)
		this.available_criteria.add(NoteDAO.CRITERIA_TAG);
	}
	
	/**
	 * Get list of all tags
	 * @return List of note which have unique tag
	 */
	@SuppressWarnings({ "boxing", "resource" })
	public String getTag(){
		InputStream is = this.couch.view(this.designDoc+ "/" + "listTag")
			.group(true)
			.queryForStream();
		return BasicIO.readInputStream(is);
	}

}
