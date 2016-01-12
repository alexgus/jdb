package fr.nikk.services;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to parse the configuration file.
 * This is also used to store all this values.
 * @author Alexandre Guyon
 * @param <D> Configuration's model data
 *
 */
public class Conf<D> {

	/**
	 * Singleton instance for this configuration 
	 */
	private static Conf<?> singleton = null;

	/**
	 * Get an instance from the configuration. The first call initialize it with the default configuration file.
	 * @return Singleton instance of this Configuration
	 */
	public static Conf<?> getInstance(){
		if(singleton == null)
			singleton = new Conf<>();
		return singleton;
	}
	
	/**
	 * Private constructor --> singleton
	 */
	private Conf() {
		// nothing
	}
	
	private D data;
	
	/**
	 * Import configuration file into this object
	 * @param file The configuration file to import
	 * @param c Class of D elements
	 */
	public void importConfFromFile(String file, Class<D> c) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
			this.data = mapper.readValue(file, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the data
	 */
	public D getData() {
		return this.data;
	}
}
