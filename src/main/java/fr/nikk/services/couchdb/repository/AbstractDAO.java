/**
 * 
 */
package fr.nikk.services.couchdb.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.lightcouch.View;

import fr.nikk.services.couchdb.StorableEntity;
import fr.nikk.util.BasicIO;

// TODO add response

/**
 * @author Alexandre Guyon
 * @param <D> Data to handle in DAO
 * Daughter class MUST be name "something"DAO
 */
@SuppressWarnings("boxing")
public abstract class AbstractDAO<D extends StorableEntity> implements DAO<D> {

	/**
	 * Save Action
	 */
	protected final static String ACTION_SAVE   = "save";

	/**
	 * List action (Read)
	 */
	protected final static String ACTION_LIST   = "list";

	/**
	 * List action (Read with criteria)
	 */
	protected final static String ACTION_LISTBY = "listBy";

	/**
	 * Update action
	 */
	protected final static String ACTION_UPDATE = "update";

	/**
	 * Delete action
	 */
	protected final static String ACTION_DELETE = "delete";

	/**
	 * designDoc directory
	 */
	protected String designDoc;

	/**
	 * Array of available criteria for the research
	 */
	protected List<String> available_criteria = new ArrayList<>();

	/**
	 * Array of available actions for user
	 */
	protected List<String> available_actions = new ArrayList<>();

	/**
	 * Class to handle in database
	 */
	private Class<D> typeToHandle; 

	/**
	 * Configured couchdb session
	 */
	protected CouchDbClient couch;

	/**
	 * Setup with classname to handle
	 * @param s {@link DAO#setUp(CouchDbClient)}
	 * @param d Classname to handle
	 */
	protected void setUp(CouchDbClient s, Class<D> d){
		String classname = this.getClass()
				.getSimpleName()
				.toLowerCase();
		
		this.typeToHandle = d;
		
		// Get "something"DAO
		this.designDoc = classname.substring(0, classname.indexOf("dao"));

		
		// Create used files for CRUD operations
		try {
			this.createFiles();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		
		this.couch = s;
		s.design().synchronizeWithDb(s.design().getFromDesk(this.designDoc));

		
	}
	
	/*public List<String> getRevisions(D d){
		this.couch.find(designDoc.getClass(), d.get_id()
	}*/
	
	private void createFiles() throws FileSystemException{
		URL designDocs = AbstractDAO.class.getClassLoader().getResource("design-docs");
		
		URL listFolder = AbstractDAO.class.getClassLoader().getResource("design-docs/" + this.designDoc + "1" + "/views/list");
		// Create folders if not exists
		if(listFolder == null){
			try {
				listFolder = new URL(designDocs.toString() + "/" + this.designDoc + "/views/list");
				File f = new File(listFolder.toURI());
				if(!f.exists()){
					if(!f.mkdirs())
						throw new FileSystemException(f.getAbsolutePath(), "", "Cannot create directory");
				}
			} catch (MalformedURLException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		URL listFile = AbstractDAO.class.getClassLoader().getResource("design-docs/" + this.designDoc + "/views/list/map.js");
		// Create files if not exists
		if(listFile == null){
			try {
				listFile = new URL(designDocs.toString() + "/" + this.designDoc + "/views/list");
				
				@SuppressWarnings("null")
				Path p = FileSystems.getDefault().getPath(listFolder.getPath(), "map.js");
				Files.createFile(p);
				
				BasicIO.write(listFile.getPath() + "/map.js", "function(doc) { if(doc.$table == \"" + this.typeToHandle.getName() +"\") { emit(doc.date, doc); } }");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * Check if criteria given are supported.
	 * @param criteria Criteria to check
	 * @return Criteria supported
	 */
	protected List<String> supportedCriteria(JSONObject criteria){
		List<String> lc = new ArrayList<>();

		String stringCriteria = criteria.toString();

		for (String crit : this.available_criteria) {
			if(stringCriteria.contains(crit) || stringCriteria.contains(crit.toLowerCase()))
				lc.add(crit);
		}

		return lc;
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

	@Override
	public List<D> getData(JSONObject criteria) throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_LISTBY)){

			List<String> lTag = this.supportedCriteria(criteria);

			if(lTag.size() > 0){
				if(lTag.contains(this.available_criteria.get(0))){ // TODO many criteria
					String crit = criteria.getString(this.available_criteria.get(0));

					return this.couch.view(this.designDoc+ "/" + ACTION_LISTBY + this.available_criteria.get(0))
							.includeDocs(true)
							.descending(true)
							.key(crit)
							.query(this.typeToHandle);
				}
			}
			else
				return null;
		}
		throw new UnimplementedOperationException(ACTION_LISTBY + " : not for now");
	}

	@Override
	public String getRawData(JSONObject criteria) throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_LISTBY)){

			List<String> lTag = this.supportedCriteria(criteria);

			if(lTag.size() > 0){
				if(lTag.contains(this.available_criteria.get(0))){ // TODO many criteria
					String crit = this.available_criteria.get(0);

					return AbstractDAO.getStringFromView(this.couch.view(this.designDoc+ "/" + ACTION_LISTBY + crit)
							.includeDocs(true)
							.descending(true)
							.key(crit));
				}
			}
			else
				return null;
		}
		throw new UnimplementedOperationException(ACTION_LISTBY + " : not for now");
	}

	@Override
	public Response save(D data) throws UnimplementedOperationException{
		Response r;
		if(this.available_actions.contains(ACTION_SAVE))
			 r = this.couch.save(data);
		else
			throw new UnimplementedOperationException(ACTION_SAVE + " : not for now");
		return r;
	}

	@Override
	public List<D> getData() throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_LIST))
			return this.couch.view(this.designDoc + "/" + ACTION_LIST)
					.includeDocs(true)
					.query(this.typeToHandle);
		throw new UnimplementedOperationException(ACTION_LIST + " : not for now");
	}

	@Override
	public String getRawData() throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_LIST))
			return AbstractDAO.getStringFromView(this.couch.view(this.designDoc+ "/" + ACTION_LIST)
					.includeDocs(true));
		throw new UnimplementedOperationException(ACTION_LIST + " : not for now");
	}

	@Override
	public Response update(D data) throws UnimplementedOperationException{
		Response r;
		if(this.available_actions.contains(ACTION_UPDATE))
			r = this.couch.update(data);
		else
			throw new UnimplementedOperationException(ACTION_UPDATE + " : not for now");
		return r;
	}

	@Override
	public void delete(D data) throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_DELETE))
			this.couch.remove(data);
		else
			throw new UnimplementedOperationException(ACTION_DELETE + " : not for now");
	}

	/**
	 * Get string from view. The view must be created in your own and give to this DAO.
	 * @param v The view to query
	 * @return the string result from database
	 */
	protected static String getStringFromView(View v){
		return BasicIO.getStringFromInput(v.queryForStream());
	}

	/**
	 * @return the available_criteria
	 */
	public List<String> getAvailable_criteria() {
		return this.available_criteria;
	}

	/**
	 * @return the available_actions
	 */
	public List<String> getAvailable_actions() {
		return this.available_actions;
	}

}
