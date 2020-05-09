/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ro.cjarges.formupload.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import ro.cjarges.formupload.dao.FormUploadImpl;
import ro.cjarges.formupload.dao.IFormUpload;
import ro.cjarges.formupload.model.FileModel;
import ro.cjarges.formupload.model.FormUploadModel;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * @author Brian Wing Shun Chan
 */
public class ConnectionPool {
	
	private static Log logger = LogFactoryUtil.getLog(ConnectionPool.class);

	private static ConnectionPool instance = new ConnectionPool();

	private ComboPooledDataSource cpds;
	private Properties props;
	private ConnectionSource connPooled;
	private IFormUpload formUploadDao;

	public static void cleanUp(Connection con) {
		instance._cleanUp(con);
	}

	public static void cleanUp(Connection con, Statement s) {
		instance._cleanUp(con, s);
	}

	public static void cleanUp(Connection con, Statement s, ResultSet rs) {
		instance._cleanUp(con, s, rs);
	}

	public static void destroy() throws SQLException {
		instance._destroy();
	}

	public static Connection getConnection() throws SQLException {
		return instance._getConnection();
	}
	
	public static ConnectionSource getConnectionPooled() throws SQLException {
		return instance._getConnectionPooled();
	}
	
	public static IFormUpload getFormUploadDao() {
		return instance._getFormUploadDao();
	}

	private IFormUpload _getFormUploadDao() {
		// TODO Auto-generated method stub
		return instance.formUploadDao;
	}

	public static Properties getProperties() {
		return instance.props;
	}

	private ConnectionPool() {
		try {

			// Properties

			ClassLoader classLoader = getClass().getClassLoader();
			
			props = new Properties();
			props.load(classLoader.getResourceAsStream("connection-pool.properties"));


			// Pooled data source

			String driverClass = props.getProperty("driver.class");
			String jdbcUrl = props.getProperty("jdbc.url");
			String user = props.getProperty("user");
			String password = props.getProperty("password");

			/*
			int minPoolSize = 5;

			try {
				minPoolSize = Integer.parseInt(props.getProperty("min.pool.size"));
			} catch (Exception e) {
			}

			int maxPoolSize = 5;

			try {
				maxPoolSize = Integer.parseInt(props.getProperty("max.pool.size"));
			} catch (Exception e) {
			}

			int acquireIncrement = 5;

			try {
				acquireIncrement = Integer.parseInt(props.getProperty("acquire.increment"));
			} catch (Exception e) {
			}

			
			cpds = new ComboPooledDataSource();

			cpds.setDriverClass(driverClass);
			cpds.setJdbcUrl(jdbcUrl);
			cpds.setUser(user);
			cpds.setPassword(password);

			cpds.setMinPoolSize(minPoolSize);
			cpds.setMaxPoolSize(maxPoolSize);
			cpds.setAcquireIncrement(acquireIncrement);
			*/
			
			
			/**
			 * Ormlite settings
			 */
			connPooled = new JdbcPooledConnectionSource(jdbcUrl, user, password);
			formUploadDao = new FormUploadImpl(connPooled);
			//setupFormUploadDatabase(connPooled);
			logger.info("Creating pooled connection to: " + jdbcUrl);
			
		}
		catch (Exception e) {
			logger.error(e);
		}
	}

	private void _cleanUp(Connection con) {
		_cleanUp(con, null, null);
	}

	private void _cleanUp(Connection con, Statement s) {
		_cleanUp(con, s, null);
	}

	private void _cleanUp(Connection con, Statement s, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (connPooled != null) {
				try {
					connPooled.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		catch (SQLException sqle) {
			logger.error(sqle);
		}

		try {
			if (s != null) {
				s.close();
			}
		}
		catch (SQLException sqle) {
			logger.error(sqle);
		}

		try {
			if (con != null) {
				con.close();
			}
		}
		catch (SQLException sqle) {
			logger.error(sqle);
		}
	}

	private void _destroy() throws SQLException {
		DataSources.destroy(cpds);
	}

	private Connection _getConnection() throws SQLException {
		return cpds.getConnection();
	}
	
	private ConnectionSource _getConnectionPooled() throws SQLException {
		return connPooled;
	}
	
	private void setupFormUploadDatabase(ConnectionSource connectionSource) throws Exception {

		// create table if not exists
		TableUtils.createTableIfNotExists(connectionSource, FormUploadModel.class);
		TableUtils.createTableIfNotExists(connectionSource, FileModel.class);
	}



}