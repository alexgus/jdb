/**
 * 
 */
package fr.nikk.model.note;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import fr.nikk.services.couchdb.StorableEntity;

/**
 * @author Alexandre Guyon
 *
 */
public class Note extends StorableEntity{
	
	private Instant date;
	
	private List<Instant> dateModif;

	private String tag;

	private String note;
	
	private List<String> res;

	/**
	 * Default constructor
	 */
	public Note(){
		super(Note.class.getName());
		this.date = Instant.now();
		this.dateModif = new ArrayList<>();
	}
	
	/**
	 * @param note The note to create
	 */
	public Note(String note) {
		super(Note.class.getName());
		this.note = note;
		this.date = Instant.now();
		this.dateModif = new ArrayList<>();
	}

	/**
	 * @param d The date to create the note
	 * @param s The string in the note
	 */
	public Note(Instant d, String s){
		super(Note.class.getName());
		this.date = d;
		this.note = s;
		this.dateModif = new ArrayList<>();
	}

	/**
	 * @return the date
	 */
	public Instant getDate() {
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Instant date) {
		this.date = date;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the dateModif
	 */
	public List<Instant> getDateModif() {
		return this.dateModif;
	}

	/**
	 * @param dateModif the dateModif to set
	 */
	public void setDateModif(List<Instant> dateModif) {
		this.dateModif = dateModif;
	}

	/**
	 * @return the res
	 */
	public List<String> getRes() {
		return this.res;
	}

	/**
	 * @param res the res to set
	 */
	public void setRes(List<String> res) {
		this.res = res;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.date == null) ? 0 : this.date.hashCode());
		result = prime * result + ((this.note == null) ? 0 : this.note.hashCode());
		result = prime * result + ((this.tag == null) ? 0 : this.tag.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (this.date == null) {
			if (other.date != null)
				return false;
		} else if (!this.date.equals(other.date))
			return false;
		if (this.note == null) {
			if (other.note != null)
				return false;
		} else if (!this.note.equals(other.note))
			return false;
		if (this.tag == null) {
			if (other.tag != null)
				return false;
		} else if (!this.tag.equals(other.tag))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Note [date=" + this.date + ", dateModif=" + this.dateModif + ", tag=" + this.tag + ", note=" + this.note + ", res=" + this.res
				+ ", _id=" + this._id + ", _rev=" + this._rev + ", _revs_info=" + this._revs_info + ", $table=" + this.$table + "]";
	}

}
