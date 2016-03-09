/**
 * 
 */
package fr.nikk.services;

/**
 * @author aguyon
 *
 */
public class MissingDependencies extends Exception {

	private static final long serialVersionUID = -1516357137768622117L;
	
	private String service;
	
	public MissingDependencies() {
		// do nothing
	}
	
	public MissingDependencies(String service) {
		super();
		this.service = service;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
}
