/**
 * 
 */
package fr.nikk.services.couchdb;

/**
 * @author Alexandre Guyon
 *
 */
public abstract class StorableEntity {

	/**
	 * Unique id od the object
	 */
	protected String _id;
	
	/**
	 * Revision of this object
	 */
	protected String _rev;
	
	/**
	 * List of all available revisions of an object
	 */
	protected String _revs_info;
	
	/**
	 * Table name
	 */
    protected String $table;
	
	/**
	 * Constructor defining table property
	 * @param table Table to store
	 */
	public StorableEntity(String table) {
		this.$table = table;
	}

	/**
	 * @return the _id
	 */
	public String get_id() {
		return this._id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}

	/**
	 * @return the _rev
	 */
	public String get_rev() {
		return this._rev;
	}

	/**
	 * @param _rev the _rev to set
	 */
	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	/**
	 * @return the _revs_info
	 */
	public String get_revs_info() {
		return this._revs_info;
	}

	/**
	 * @param _revs_info the _revs_info to set
	 */
	public void set_revs_info(String _revs_info) {
		this._revs_info = _revs_info;
	}

	/**
	 * @return the $table
	 */
	public String get$table() {
		return this.$table;
	}

	/**
	 * @param $table the $table to set
	 */
	public void set$table(String $table) {
		this.$table = $table;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.$table == null) ? 0 : this.$table.hashCode());
		result = prime * result + ((this._id == null) ? 0 : this._id.hashCode());
		result = prime * result + ((this._rev == null) ? 0 : this._rev.hashCode());
		result = prime * result + ((this._revs_info == null) ? 0 : this._revs_info.hashCode());
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
		StorableEntity other = (StorableEntity) obj;
		if (this.$table == null) {
			if (other.$table != null)
				return false;
		} else if (!this.$table.equals(other.$table))
			return false;
		if (this._id == null) {
			if (other._id != null)
				return false;
		} else if (!this._id.equals(other._id))
			return false;
		if (this._rev == null) {
			if (other._rev != null)
				return false;
		} else if (!this._rev.equals(other._rev))
			return false;
		if (this._revs_info == null) {
			if (other._revs_info != null)
				return false;
		} else if (!this._revs_info.equals(other._revs_info))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StorableEntity [_id=" + this._id + ", _rev=" + this._rev + ", _revs_info=" + this._revs_info + ", $table=" + this.$table
				+ "]";
	}

}
