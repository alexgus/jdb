/**
 * 
 */
package fr.nikk.services.couchdb.repository;

import java.util.List;

import org.json.JSONObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

/**
 * 
 * @author Alexandre Guyon
 * @param <D> Data to save
 */
public interface DAO<D>{

	/**
	 * @param data Save data D
	 * @return The response of the server (for id et rev mainly)
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public Response save(D data) throws UnimplementedOperationException;
	
	/**
	 * @param data Update selected data
	 * @return The response of the server (for id et rev mainly)
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public Response update(D data) throws UnimplementedOperationException;
	
	/**
	 * @param data Delete selected data
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public void delete(D data) throws UnimplementedOperationException;
	
	/**
	 * Get all the data from this type
	 * @return Collection of data
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public List<D> getData() throws UnimplementedOperationException;
	
	/**
	 * @return The raw data from the database
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public String getRawData() throws UnimplementedOperationException;
	
	/**
	 * Get all the data from this type with this criteria (aspect)
	 * @param criteria JSON criteria
	 * @return Collection of data
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public List<D> getData(JSONObject criteria) throws UnimplementedOperationException;
	
	/**
	 * @param criteria JSON criteria
	 * @return Raw data from the database 
	 * @throws UnimplementedOperationException If the method does not exist for the DAO
	 */
	public String getRawData(JSONObject criteria) throws UnimplementedOperationException;
	
	/**
	 * Set up access to couchdb session
	 * @param s Opened session to couchdb
	 */
	public void setUp(CouchDbClient s);
	
}
