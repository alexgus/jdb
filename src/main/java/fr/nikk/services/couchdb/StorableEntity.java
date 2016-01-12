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
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StorableEntity [$table=" + this.$table + "]";
	}

}
