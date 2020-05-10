package ro.cjarges.formupload.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ro.cjarges.formupload.model.FormUploadModel;
import ro.cjarges.formupload.model.WrapperFormUploadModel;
import ro.cjarges.formupload.util.ConnectionPool;


public class FormUploadDAO {

	
	private static Logger logger = Logger.getLogger(FormUploadDAO.class);


	public static void addFormUpload(FormUploadModel model) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			IFormUpload dao = ConnectionPool.getFormUploadDao();
			dao.add(model);

			logger.info("Adding new FormUpload: " + model.toString());
		} finally {
			ConnectionPool.cleanUp(con, ps);
		}
	}
	
	public static FormUploadModel getById(int id) {
		Connection con = null;
		PreparedStatement ps = null;

		FormUploadModel model = null;
		try {
			IFormUpload dao = ConnectionPool.getFormUploadDao();
			model = dao.getById(id);

		} finally {
			ConnectionPool.cleanUp(con, ps);
		}
		return model;
	}
	
	public static List<FormUploadModel> getFormUploads() {
		Connection con = null;
		PreparedStatement ps = null;

		List<FormUploadModel> results = new ArrayList<FormUploadModel>();
		
		try {
			IFormUpload dao = ConnectionPool.getFormUploadDao();
			results = dao.getAll();
			logger.info("Getting list of FormUploadModel size: " + results.size());
		} finally {
			ConnectionPool.cleanUp(con, ps);
		}
		return results;
	}
	
	public static List<WrapperFormUploadModel> getLiferayContainerFormUploads() {
		Connection con = null;
		PreparedStatement ps = null;

		List<FormUploadModel> results = new ArrayList<FormUploadModel>();
		List<WrapperFormUploadModel> wrappedList = new ArrayList<WrapperFormUploadModel>();
		
		try {
			IFormUpload dao = ConnectionPool.getFormUploadDao();
			results = dao.getAll();
			logger.info("Getting list of FormUploadModel size: " + results.size());
		} finally {
			ConnectionPool.cleanUp(con, ps);
		}
		
		for (FormUploadModel model: results) {
			wrappedList.add(new WrapperFormUploadModel(model));
		}
		
		return wrappedList;
	}
}
