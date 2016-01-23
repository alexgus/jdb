/**
 * 
 */
package fr.nikk.services.couchdb.repository;

import java.io.InputStream;

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
	
	public void setUp(org.lightcouch.CouchDbClient s) {
		super.setUp(s, Note.class);
		this.available_actions.add(AbstractDAO.ACTION_SAVE);
		this.available_actions.add(AbstractDAO.ACTION_LIST);
		this.available_actions.add(AbstractDAO.ACTION_LISTBY);
		this.available_actions.add(AbstractDAO.ACTION_DELETE);
		this.available_actions.add(AbstractDAO.ACTION_UPDATE);
		
		this.available_criteria.add(NoteDAO.CRITERIA_TAG);
		//this.available_criteria.add(NoteDAO.CRITERIA_DATE);
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
	
	@SuppressWarnings("resource")
	@Override
	public String getByIdAndRev(String id, String rev){
		InputStream is;
		if(rev != null)
			is = this.couch.find(id, rev);
		else
			is = this.couch.find(id);
		
		return BasicIO.readInputStream(is);
	}

}
