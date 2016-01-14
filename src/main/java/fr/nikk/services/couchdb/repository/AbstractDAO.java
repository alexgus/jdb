/**
 * 
 */
package fr.nikk.services.couchdb.repository;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

import fr.nikk.util.BasicIO;

/**
 * @author Alexandre Guyon
 * @param <D> Data to handle in DAO
 *
 */
@SuppressWarnings("boxing")
public abstract class AbstractDAO<D> implements DAO<D> {

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

		this.designDoc = classname.substring(0, classname.indexOf("dao"));

		this.couch = s;
		System.out.println(this.designDoc);
		s.design().synchronizeWithDb(s.design().getFromDesk(this.designDoc));

		this.typeToHandle = d;
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
							.endKey(crit));
				}
			}
			else
				return null;
		}
		throw new UnimplementedOperationException(ACTION_LISTBY + " : not for now");
	}

	@Override
	public void save(D data) throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_SAVE))
			this.couch.save(data);
		else
			throw new UnimplementedOperationException(ACTION_SAVE + " : not for now");
	}

	@Override
	public List<D> getData() throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_LIST))
			return this.couch.view(this.designDoc + "/" + ACTION_LIST)
					.includeDocs(true)
					.descending(true)
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
	public void update(D data) throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_UPDATE))
			this.couch.update(data);
		else
			throw new UnimplementedOperationException(ACTION_UPDATE + " : not for now");
	}

	@Override
	public void delete(D data) throws UnimplementedOperationException{
		if(this.available_actions.contains(ACTION_DELETE))
			this.couch.remove(data);
		else
			throw new UnimplementedOperationException(ACTION_DELETE + " : not for now");
	}

	/**
	 * Get string from view
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
