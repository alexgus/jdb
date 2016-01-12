package fr.nikk.jdb.controller;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nikk.model.note.Note;
import fr.nikk.services.couchdb.repository.NoteDAO;
import fr.nikk.services.couchdb.repository.UnimplementedOperationException;

/**
 * 
 * @author Alexandre Guyon
 *
 */
@Produces("text/json")
@Path("/note")
public class NoteController {
	
	private NoteDAO dao;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Add a note
	 * @param tag Tag, comma separated
	 * @param note Note
	 * @return The new string
	 */
	@POST
	@Path("/{tag}/{note}")
	public String addNote(@PathParam("tag") String tag, @PathParam("note") String note){
		Note n = new Note();
		n.setNote(note);
		n.setTag(tag);
		try {
			this.dao.save(n);
			
			String ret = "";
			try {
				ret = this.mapper.writeValueAsString(n);
			} catch (JsonProcessingException e) {
				ret = "jdb error :\n";
				ret += e.getMessage();
			}
			return ret;
		} catch (UnimplementedOperationException e1) {
			e1.printStackTrace();	
			return "";
		}
	}
	
	/**
	 * Add a note
	 * @param tag Tag, comma separated
	 * @param note Note
	 * @param d Note's date
	 * @return The new string
	 */
	@POST
	@Path("/{tag}/{note}/{date}")
	public String addDateNote(@PathParam("tag") String tag, @PathParam("note") String note, Date d){
		Note n = new Note();
		n.setNote(note);
		n.setTag(tag);
		n.setDate(d);
		try {
			this.dao.save(n);
			
			String ret = "";
			try {
				ret = this.mapper.writeValueAsString(n);
			} catch (JsonProcessingException e) {
				ret = "jdb error :\n";
				ret += e.getMessage();
			}
			return ret;
		} catch (UnimplementedOperationException e1) {
			e1.printStackTrace();	
			return "";
		}
	}
	
	/**
	 * Search note by tag
	 * @param tag Tag to search
	 * @return full object having those tag
	 */
	@GET
	@Path("/gTag/{tag}")
	public String getNoteByTag(@PathParam("tag") String tag){
		JSONObject crit = new JSONObject();
		crit.put(NoteDAO.CRITERIA_TAG, tag);
		String ret = "";
		try {
			List<Note> ln = this.dao.getData(crit);
			try {
				return this.mapper.writeValueAsString(ln);
			} catch (JsonProcessingException e) {
				ret = "jdb error :\n";
				ret += e.getMessage();
				return ret;
			}
		} catch (UnimplementedOperationException e) {
			e.printStackTrace();
			return ret;
		}
	}
	
	/**
	 * Search note by date
	 * @param date Date to search
	 * @return full object having this date
	 */
	@GET
	@Path("/gDate/{date}")
	public String getNoteByDate(@PathParam("date") String date){
		JSONObject crit = new JSONObject();
		crit.put(NoteDAO.CRITERIA_DATE, date);
		String ret = "";
		try {
			List<Note> ln = this.dao.getData(crit);
			try {
				return this.mapper.writeValueAsString(ln);
			} catch (JsonProcessingException e) {
				ret = "jdb error :\n";
				ret += e.getMessage();
				return ret;
			}
		} catch (UnimplementedOperationException e) {
			e.printStackTrace();
			return ret;
		}
	}

	/**
	 * Search note by tag
	 * @return full object having those tag
	 */
	@GET
	@Path("/")
	public String getNotes(){
		String ret = "";
		try {
			List<Note> ln = this.dao.getData();
			try {
				return this.mapper.writeValueAsString(ln);
			} catch (JsonProcessingException e) {
				ret = "jdb error :\n";
				ret += e.getMessage();
				return ret;
			}
		} catch (UnimplementedOperationException e) {
			e.printStackTrace();
			return ret;
		}
	}
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(NoteDAO dao) {
		this.dao = dao;
	}

}
