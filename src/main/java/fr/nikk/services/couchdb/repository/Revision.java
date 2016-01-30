/**
 * 
 */
package fr.nikk.services.couchdb.repository;

/**
 * @author Alexandre Guyon
 *
 */
public class Revision {

	private String rev;
	
	private String status;

	/**
	 * @return the rev
	 */
	public String getRev() {
		return this.rev;
	}

	/**
	 * @param rev the rev to set
	 */
	public void setRev(String rev) {
		this.rev = rev;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.rev == null) ? 0 : this.rev.hashCode());
		result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
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
		Revision other = (Revision) obj;
		if (this.rev == null) {
			if (other.rev != null)
				return false;
		} else if (!this.rev.equals(other.rev))
			return false;
		if (this.status == null) {
			if (other.status != null)
				return false;
		} else if (!this.status.equals(other.status))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Revision [rev=" + this.rev + ", status=" + this.status + "]";
	}

}
