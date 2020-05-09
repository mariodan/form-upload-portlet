package ro.cjarges.formupload.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author marian
 *
 */
@DatabaseTable(tableName = "file_uploads")
public class FileModel {

	public static final String ID_FIELD_ID = "id",
			COLUMN_NUME_FISIER = "nume_fisier",
			COLUMN_PATH = "path",
			COLUMN_SIZE = "size_in_kb";
	
	@DatabaseField(generatedId = true, indexName = "cd_id_idx", columnName = ID_FIELD_ID)
	private int id;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_NUME_FISIER, canBeNull = false)
	private String numeFisier;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_PATH, canBeNull = false)
	private String path;
	
	@DatabaseField(useGetSet = true, columnName = COLUMN_SIZE, canBeNull = false)
	private String size;
	
	@DatabaseField(foreign = true, columnName = "id_form_upload", canBeNull = false, foreignAutoCreate=true, foreignAutoRefresh=true)
    private FormUploadModel formUpload;

	
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
	 * @return the numeFisier
	 */
	public String getNumeFisier() {
		return numeFisier;
	}

	/**
	 * @param numeFisier the numeFisier to set
	 */
	public void setNumeFisier(String numeFisier) {
		this.numeFisier = numeFisier;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the formUpload
	 */
	public FormUploadModel getFormUpload() {
		return formUpload;
	}

	/**
	 * @param formUpload the formUpload to set
	 */
	public void setFormUpload(FormUploadModel formUpload) {
		this.formUpload = formUpload;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FileModel [id=" + id + ", numeFisier=" + numeFisier + ", path="
				+ path + ", size=" + size + "]";
	}

}