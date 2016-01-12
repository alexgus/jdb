/**
 * 
 */
package fr.nikk.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Basic static function for IO
 * @author Alexandre Guyon
 *
 */
public class BasicIO {

	/**
	 * Read a file passed on argument
	 * @param file The file to read
	 * @return String of the file
	 */
	@SuppressWarnings("resource")
	public static String readFile(String file){
		File f = new File(file);
		String content = "";

		if(f.exists() && f.canRead()){
			try {
				content = readBufferedReader(new BufferedReader(new FileReader(f)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content;
	}

	/**
	 * Read input stream
	 * @param is The input stream to read
	 * @return String corresponding to the stream
	 */
	public static String readInputStream(InputStream is){
		return readInputStreamReader(new InputStreamReader(is));
	}
	
	/**
	 * Read input stream
	 * @param is The input stream reader to read
	 * @return String corresponding to the stream
	 */
	public static String readInputStreamReader(InputStreamReader is){
		return readBufferedReader(new BufferedReader(is));
	}

	/**
	 * Read buffered reader
	 * @param reader The buffered reader reader to read
	 * @return String corresponding to the stream
	 */
	public static String readBufferedReader(BufferedReader reader){
		String content = "";
		try{
			String line = null;
			do{
				line = reader.readLine();
				if(line != null)
					content += line;
			}while(line != null);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * Get string from inputstream
	 * @param is InputStream to read
	 * @return String from inputstream
	 */
	public static String getStringFromInput(InputStream is) {
		String content = readInputStream(is);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	


	/**
	 * Write content to a file
	 * @param path The path to the file
	 * @param content The content to write
	 */
	@SuppressWarnings("resource")
	public static void write(String path, String content){
		try {

			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
