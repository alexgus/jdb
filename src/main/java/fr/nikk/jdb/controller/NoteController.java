package fr.nikk.jdb.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lightcouch.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fr.nikk.model.note.Note;
import fr.nikk.services.couchdb.repository.NoteDAO;
import fr.nikk.services.couchdb.repository.UnimplementedOperationException;

/**
 * Controller using REST API.
 * @author Alexandre Guyon
 *
 */
@Produces("text/json")
@Path("/note")
public class NoteController implements Controller{

	private NoteDAO dao;

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Using other controller for other data, too large to be passed by REST (URL limitation)
	 */
	private ContentNoteController bigDataController = new ContentNoteController();

	/**
	 * Default constructor
	 */
	public NoteController() {
		this.mapper.registerModule(new JavaTimeModule());
		this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * Add a note
	 * @param tag Tag, comma separated
	 * @param note Note
	 * @return The new string
	 */
	@POST
	@Path("/{tag}/{note}")
	public String addNote(@PathParam("tag") String tag, @PathParam("note") String note){
		return this.addNote(tag, note);
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
	public String addDateNote(@PathParam("tag") String tag, @PathParam("note") String note, @PathParam("date") String d){
		return this.bigDataController.addDateNote(tag, note, d);
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
		return this.bigDataController.modNote(id, rev, tag, note);
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
				ret = this.mapper.writeValueAsString(ln);
			} catch (JsonProcessingException e) {
				ret = "jdb error :\n";
				ret += e.getMessage();
			}
		} catch (UnimplementedOperationException e) {
			e.printStackTrace();
		}
		return ret;
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

	/**
	 * @return the dao
	 */
	public NoteDAO getDao() {
		return this.dao;
	}

	/**
	 * @return the bigDataController
	 */
	public ContentNoteController getBigDataController() {
		return this.bigDataController;
	}

	/**
	 * @return the mapper
	 */
	public ObjectMapper getMapper() {
		return this.mapper;
	}

	/**
	 * The {@link ContentNoteController} deals with much bigger data than {@link NoteController} in upload for the client,
	 * data that can not be sent by REST.
	 * @author Alexandre Guyon
	 *
	 */
	private class ContentNoteController extends HttpServlet implements Controller {

		private static final long serialVersionUID = -3272120673430017122L;

		/**
		 * Default constructor
		 */
		public ContentNoteController() {
			NoteController.this.getMapper().registerModule(new JavaTimeModule());
			NoteController.this.getMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		}

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.setContentType("text/html;charset=utf-8");
			resp.setStatus(HttpServletResponse.SC_OK);
		}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			if(req.getParameterMap().containsKey("note") && req.getParameterMap().containsKey("tag")){
				String note = req.getParameter("note");
				String tag = req.getParameter("tag");
				if(req.getParameterMap().containsKey("rev")){ // modification of existing note
					String id = req.getParameter("id");
					String rev = req.getParameter("rev");

					resp.getWriter().println(this.modNote(id, rev, tag, note));
				}else // Add new note
					resp.getWriter().println(this.addNote(tag, note));
			}

			resp.setContentType("text/json;charset=utf-8");
			resp.setStatus(HttpServletResponse.SC_OK);
		}

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
				Response r = NoteController.this.getDao().save(n);
				n.set_id(r.getId());
				n.set_rev(r.getRev());

				String ret = "";
				try {
					ret = NoteController.this.getMapper().writeValueAsString(n);
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
		public String addDateNote(@PathParam("tag") String tag, @PathParam("note") String note, @PathParam("date") String d){
			Note n = new Note();
			n.setNote(note);
			n.setTag(tag);
			n.setDate(Instant.parse(d));
			try {
				NoteController.this.getDao().save(n);

				String ret = "";
				try {
					ret = NoteController.this.getMapper().writeValueAsString(n);
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
			n.setTag(tag);
			n.setNote(note);
			List<Instant> d = new ArrayList<>();

			NoteController.this.getDao().getRevisions(n);

			JSONObject js = new JSONObject(NoteController.this.getDao().getByIdAndRev(id, rev));
			if(js.has("dateModif")){
				JSONArray dates = js.getJSONArray("dateModif");			

				for (int i= 0 ; i < dates.length() ; ++i){
					try {
						d.add(Instant.parse(dates.getString(i)));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			d.add(Instant.now());
			n.setDateModif(d);

			if(js.has("date")){
				try {
					n.setDate(Instant.parse(js.getString("date")));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}else
				return "{\"status\" : \"no creation date in database\"}";

			try {
				Response r = NoteController.this.getDao().update(n);
				n.set_rev(r.getRev());
				return NoteController.this.getMapper().writeValueAsString(n);
			} catch (UnimplementedOperationException | JsonProcessingException e) {
				e.printStackTrace();
				return "{\"status\" : \"error\"}";
			}
		}

	}

}
