
package ro.cjarges.formupload.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author marian
 * formular_upload_portlet
 */
@DatabaseTable(tableName = "formular_upload_portlet")
public class FormUploadModel implements Serializable, Comparable<FormUploadModel> {

	private static final long serialVersionUID = -6631976313120114440L;

	public static final String ID_FIELD_ID = "id",
			COLUMN_NUME = "nume",
			COLUMN_PRENUME = "prenume",
			COLUMN_TELEFON = "telefon",
			COLUMN_EMAIL = "email",
			COLUMN_RASPUNS = "raspuns",
			COLUMN_CREATED_AT = "created_at";
	
	@DatabaseField(generatedId = true, indexName = "id_form_upload_idx", columnName = ID_FIELD_ID)
	private int id;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_NUME, canBeNull = false)
	private String nume;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_PRENUME, canBeNull = false)
	private String prenume;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_TELEFON, canBeNull = false)
	private String telefon;

	@DatabaseField(useGetSet = true, columnName = COLUMN_EMAIL, canBeNull = false)
	private String email;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_RASPUNS, canBeNull = false)
	private String raspuns;

	@DatabaseField(useGetSet = true, columnName = COLUMN_CREATED_AT, dataType = DataType.DATE, format = "dd/MM/yyyy HH:mm")
	private Date createdAt = Calendar.getInstance().getTime();

	
	@ForeignCollectionField(eager = true)
    private Collection<FileModel> files;
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the nume
	 */
	public String getNume() {
		return nume;
	}

	/**
	 * @param nume the nume to set
	 */
	public void setNume(String nume) {
		this.nume = nume;
	}

	/**
	 * @return the prenume
	 */
	public String getPrenume() {
		return prenume;
	}

	/**
	 * @param prenume the prenume to set
	 */
	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	/**
	 * @return the telefon
	 */
	public String getTelefon() {
		return telefon;
	}

	/**
	 * @param telefon the telefon to set
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
	/**
	 * @return the files
	 */
	public Collection<FileModel> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<FileModel> files) {
		this.files = files;
	}
	
	
	/**
	 * Get column name by column id
	 */
	public static String getColumnNameById(int id) {
		String columnName = FormUploadModel.ID_FIELD_ID;
		switch (id) {
		case 0:
			columnName = FormUploadModel.ID_FIELD_ID;
			break;
		case 1:
			columnName = FormUploadModel.COLUMN_NUME;
			break;
		case 2:
			columnName = FormUploadModel.COLUMN_PRENUME;
			break;
		case 3:
			columnName = FormUploadModel.COLUMN_TELEFON;
			break;
		case 4:
			columnName = FormUploadModel.COLUMN_EMAIL;
			break;
		case 5:
			columnName = FormUploadModel.COLUMN_CREATED_AT;
			break;
		default:
			columnName = FormUploadModel.ID_FIELD_ID;
		}
		return columnName;
	}

	/**
	 * @return the raspuns
	 */
	public String getRaspuns() {
		return raspuns;
	}

	/**
	 * @param raspuns the raspuns to set
	 */
	public void setRaspuns(String raspuns) {
		this.raspuns = raspuns;
	}

	@Override
	public int compareTo(FormUploadModel o) {
		return this.getCreatedAt().compareTo(o.getCreatedAt());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		result = prime * result + id;
		result = prime * result + ((nume == null) ? 0 : nume.hashCode());
		result = prime * result + ((prenume == null) ? 0 : prenume.hashCode());
		result = prime * result + ((telefon == null) ? 0 : telefon.hashCode());
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
		FormUploadModel other = (FormUploadModel) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (id != other.id)
			return false;
		if (nume == null) {
			if (other.nume != null)
				return false;
		} else if (!nume.equals(other.nume))
			return false;
		if (prenume == null) {
			if (other.prenume != null)
				return false;
		} else if (!prenume.equals(other.prenume))
			return false;
		if (telefon == null) {
			if (other.telefon != null)
				return false;
		} else if (!telefon.equals(other.telefon))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FormUploadModel [id=" + id + ", nume=" + nume + ", prenume="
				+ prenume + ", telefon=" + telefon + ", email=" + email
				+ ", raspuns=" + raspuns + ", createdAt=" + createdAt + "]";
	}

}
