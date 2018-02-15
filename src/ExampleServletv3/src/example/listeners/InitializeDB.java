package example.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import example.AppConstants;
import example.model.User;

/**
 * An example listener that reads the customer json file and populates the data
 * into a Derby database
 */
@WebListener
public class InitializeDB implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public InitializeDB() {
		// TODO Auto-generated constructor stub
	}

	private boolean tableAlreadyExists(SQLException e) {
		boolean exists;
		if (e.getSQLState().equals("X0Y32")) {
			exists = true;
		} else {
			exists = false;
		}
		return exists;
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext cntx = event.getServletContext();

		try {
			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			// create tables
			String[] tables = { AppConstants.DB_CREATE_TABLE_USERS, AppConstants.DB_CREATE_TABLE_EBOOKS,
					AppConstants.DB_CREATE_TABLE_LIKES, AppConstants.DB_CREATE_TABLE_REVIEWS,
					AppConstants.DB_CREATE_TABLE_PURCHASES };
			Boolean insert_admin = true;

			for (String s : tables) {
				try {
					Statement stmt = conn.createStatement();
					stmt.executeUpdate(s);
					// commit update
					conn.commit();
					stmt.close();

				} catch (SQLException e) {
					if (!tableAlreadyExists(e)) {
						throw e;
					}

					if (tableAlreadyExists(e) && s == AppConstants.DB_CREATE_TABLE_USERS) {
						insert_admin = false;
					}
				}
			}

			// add admin user
			if (insert_admin) {
				User admin = new User("admin", "admin@booksforall.com", "Passw0rd", "Administrator", "Aba Khoushy Ave",
						"199", "Haifa", "3498838", "048240111", "admin",
						"Computer Science student", "", 1);
				admin.insert(conn);
			}

			// close connection
			conn.close();

		} catch (SQLException | NamingException e) {
			// log error
			cntx.log("Error during database initialization", e);
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext cntx = event.getServletContext();

		// shut down database
		try {
			// obtain CustomerDB data source from Tomcat's context and shutdown
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.SHUTDOWN);
			ds.getConnection();
			ds = null;
		} catch (SQLException | NamingException e) {
			cntx.log("Error shutting down database", e);
		}

	}

}