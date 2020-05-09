package ro.cjarges.formupload.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ro.cjarges.formupload.model.FormUploadModel;
import ro.cjarges.formupload.util.ConnectionPool;


public class FormUploadDAO {

	
	private static Logger logger = Logger.getLogger(FormUploadDAO.class);


	public static void addFormUpload(FormUploadModel model) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			FormUploadImpl dao = ConnectionPool.getFormUploadDao();
			dao.add(model);

			logger.info("Adding new FormUpload: " + model.toString());
		} finally {
			ConnectionPool.cleanUp(con, ps);
		}
	}
}
