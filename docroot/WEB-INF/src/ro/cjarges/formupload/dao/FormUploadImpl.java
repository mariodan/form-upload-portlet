package ro.cjarges.formupload.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ro.cjarges.formupload.model.FileModel;
import ro.cjarges.formupload.model.FormUploadModel;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class FormUploadImpl implements IFormUpload {

	private Dao<FormUploadModel, Integer> dao;
	private Dao<FileModel, Integer> daoFiles;
	private Logger logger = Logger.getLogger(FormUploadImpl.class);

	
	public FormUploadImpl(ConnectionSource connectionSource) {
		try {
			this.dao = DaoManager.createDao(connectionSource, FormUploadModel.class);
			this.daoFiles = DaoManager.createDao(connectionSource, FileModel.class);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	@Override
	public FormUploadModel add(FormUploadModel model) {
		try {
			this.dao.create(model);
			/**
			 * Add collection of foreign FileModel if any
			 */
			for (FileModel fileModel : model.getFiles()) {
				fileModel.setFormUpload(model);
				this.daoFiles.create(fileModel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return model;
	}

	@Override
	public FormUploadModel getById(int id) {
		try {
			return this.dao.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteById(int id) {
		try {
			FormUploadModel reg = this.dao.queryForId(id);
			this.dao.delete(reg);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateRegistration(FormUploadModel model) {
		try {
			this.dao.update(model);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getCount() {
		long total = 0L;
		try {
			total = this.dao.countOf();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public List<FormUploadModel> subList(long offset, long limit) {
		QueryBuilder<FormUploadModel, Integer> queryBuilder = this.dao.queryBuilder();

		try {
			queryBuilder
			.offset(offset)
			.limit(limit);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedQuery<FormUploadModel> preparedQuery = null;
		try {
			preparedQuery = queryBuilder.prepare();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<FormUploadModel> listaInregistrari = null;
		try {
			listaInregistrari = this.dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaInregistrari;
	}


	@Override
	public List<FormUploadModel> getAll() {
		List<FormUploadModel> items = new ArrayList<FormUploadModel>();

		CloseableIterator<FormUploadModel> iterator = this.dao.closeableIterator();
		try {
			while (iterator.hasNext()) {
				FormUploadModel form = iterator.next();
				items.add(form);
			}
		} finally {
			try {
				iterator.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return items;
	}

}
