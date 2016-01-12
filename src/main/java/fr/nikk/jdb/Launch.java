/**
 * 
 */
package fr.nikk.jdb;

import fr.nikk.jdb.controller.Controller;

/**
 * @author Alexandre Guyon
 *
 */
public class Launch {

	/**
	 * Start the application
	 * @param args not used
	 */
	public static void main(String[] args) {
		Controller c = new Controller();
		c.start();
	}

}
