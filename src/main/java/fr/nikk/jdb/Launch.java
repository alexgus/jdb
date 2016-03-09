/**
 * 
 */
package fr.nikk.jdb;

import fr.nikk.jdb.controller.MainController;

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
		MainController c = new MainController();
		Thread t = new Thread(c);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
