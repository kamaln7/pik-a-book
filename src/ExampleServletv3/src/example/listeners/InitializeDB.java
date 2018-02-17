package example.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Connection;
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
import example.Helpers;
import example.model.Ebook;
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
			Connection conn = Helpers.getConnection(cntx);

			// create tables
			String[] tables = { AppConstants.DB_CREATE_TABLE_USERS, AppConstants.DB_CREATE_TABLE_EBOOKS,
					AppConstants.DB_CREATE_TABLE_LIKES, AppConstants.DB_CREATE_TABLE_REVIEWS,
					AppConstants.DB_CREATE_TABLE_PURCHASES };
			Boolean insert_users = true, insert_ebooks = true;

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
						insert_users = false;
					}

					if (tableAlreadyExists(e) && s == AppConstants.DB_CREATE_TABLE_EBOOKS) {
						insert_ebooks = false;
					}
				}
			}

			// import users
			if (insert_users) {
				// populate users table with user data from json file
				Collection<User> users = loadUsers(cntx.getResourceAsStream(File.separator + AppConstants.USERS_FILE));

				for (User user : users) {
					user.insert(conn);
				}
			}

			// import ebooks
			if (insert_ebooks) {
				// populate ebooks table with ebook data from json file
				Collection<Ebook> ebooks = loadEbooks(
						cntx.getResourceAsStream(File.separator + AppConstants.EBOOKS_FILE));

				for (Ebook ebook : ebooks) {
					ebook.insert(conn);
				}
			}

			// close connection
			conn.close();

		} catch (SQLException | NamingException | IOException e) {
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

	private Collection<Ebook> loadEbooks(InputStream is) throws IOException {
		// wrap input stream with a buffered reader to allow reading the file line by
		// line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		// read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null) {
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		Type type = new TypeToken<Collection<Ebook>>() {
		}.getType();
		Collection<Ebook> ebooks = gson.fromJson(jsonFileContent.toString(), type);
		// close
		br.close();
		return ebooks;
	}

	private Collection<User> loadUsers(InputStream is) throws IOException {
		// wrap input stream with a buffered reader to allow reading the file line by
		// line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		// read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null) {
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		Type type = new TypeToken<Collection<User>>() {
		}.getType();
		Collection<User> users = gson.fromJson(jsonFileContent.toString(), type);
		// close
		br.close();
		return users;
	}

}
