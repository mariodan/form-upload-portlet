package ro.cjarges.formupload.dao;

import java.util.List;


import ro.cjarges.formupload.model.FormUploadModel;

public interface IFormUpload {
	FormUploadModel add(FormUploadModel reg);
	FormUploadModel getById(int id);
	void deleteById(int id);
	void updateRegistration(FormUploadModel reg);
	long getCount();
	List<FormUploadModel> subList(long offset, long limit);
	List<FormUploadModel> getAll();
}
