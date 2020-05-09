
package ro.cjarges.formupload.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
public class FormUploadModel {

	public static final String ID_FIELD_ID = "id",
			COLUMN_NUME = "nume",
			COLUMN_PRENUME = "prenume",
			COLUMN_TELEFON = "telefon",
			COLUMN_EMAIL = "email",
			COLUMN_CREATED_AT = "created_at";
	
	@DatabaseField(generatedId = true, indexName = "cd_id_idx", columnName = ID_FIELD_ID)
	private int id;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_NUME, canBeNull = false)
	private String nume;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_PRENUME, canBeNull = false)
	private String prenume;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_TELEFON, canBeNull = false)
	private String telefon;

	@DatabaseField(useGetSet = true, columnName = COLUMN_EMAIL, canBeNull = false)
	private String email;

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
	public void setFiles(Collection<FileModel> files) {
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

	@Override
	public String toString() {
		return "FormUploadModel [id=" + id + ", nume=" + nume + ", prenume="
				+ prenume + ", telefon=" + telefon + ", email=" + email
				+ ", createdAt=" + createdAt + "]";
	}
	
}
