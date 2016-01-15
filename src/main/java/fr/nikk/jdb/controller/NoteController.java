package fr.nikk.jdb.controller;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONObject;
import org.lightcouch.Response;

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
			Response r = this.dao.save(n);
			n.set_id(r.getId());
			n.set_rev(r.getRev());
			
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
	 * Modify note
	 * @param id Id of the note to delete
	 * @param rev Rev of the object to delete
	 * @param tag Tag, comma separated
	 * @param note Note
	 * @return status json
	 */
	@POST
	@Path("/{id}/{rev}/{tag}/{note}")
	public String modNote(@PathParam("id")String id, @PathParam("rev") String rev, @PathParam("tag") String tag, @PathParam("note") String note){
		Note n = new Note();
		n.set_id(id);
		n.set_rev(rev);
		n.setDate(new Date());
		n.setTag(tag);
		n.setNote(note);

		try {
			Response r = this.dao.update(n);
			n.set_rev(r.getRev());
			return this.mapper.writeValueAsString(n);
		} catch (UnimplementedOperationException | JsonProcessingException e) {
			e.printStackTrace();
			return "{\"status\" : \"error\"}";
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
	 * Delete note
	 * @param id Id of the note to delete
	 * @param rev Rev of the object to delete
	 * @return status json
	 */
	@DELETE
	@Path("/{id}/{rev}")
	public String delNote(@PathParam("id")String id, @PathParam("rev") String rev){
		Note n = new Note();
		n.set_id(id);
		n.set_rev(rev);

		try {
			this.dao.delete(n);
			return "{\"status\" : \"deleted\"}";
		} catch (UnimplementedOperationException e) {
			e.printStackTrace();
			return "{\"status\" : \"error\"}";
		}
	}
	
	/**
	 * Get all tags
	 * @return status json
	 */
	@GET
	@Path("/gTag")
	public String getTag(){
		String res = this.dao.getTag();
		return res;
	}
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(NoteDAO dao) {
		this.dao = dao;
	}

}
