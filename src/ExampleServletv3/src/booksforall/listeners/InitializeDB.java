package booksforall.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

import booksforall.AppConstants;
import booksforall.Helpers;
import booksforall.model.Ebook;
import booksforall.model.Like;
import booksforall.model.Review;
import booksforall.model.User;

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

	private Set<String> getDBTables(Connection targetDBConn) throws SQLException {
		Set<String> set = new HashSet<String>();
		DatabaseMetaData dbmeta = targetDBConn.getMetaData();
		readDBTable(set, dbmeta, "TABLE", null);
		readDBTable(set, dbmeta, "VIEW", null);
		return set;
	}

	private void readDBTable(Set<String> set, DatabaseMetaData dbmeta, String searchCriteria, String schema)
			throws SQLException {
		ResultSet rs = dbmeta.getTables(null, schema, null, new String[] { searchCriteria });
		while (rs.next()) {
			set.add(rs.getString("TABLE_NAME").toLowerCase());
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext cntx = event.getServletContext();

		try {
			Connection conn = Helpers.getConnection(cntx);

			// create tables
			LinkedHashMap<String, String> tables = new LinkedHashMap<String, String>();
			tables.put(AppConstants.DB_CREATE_TABLE_USERS_NAME, AppConstants.DB_CREATE_TABLE_USERS);
			tables.put(AppConstants.DB_CREATE_TABLE_EBOOKS_NAME, AppConstants.DB_CREATE_TABLE_EBOOKS);
			tables.put(AppConstants.DB_CREATE_TABLE_LIKES_NAME, AppConstants.DB_CREATE_TABLE_LIKES);
			tables.put(AppConstants.DB_CREATE_TABLE_REVIEWS_NAME, AppConstants.DB_CREATE_TABLE_REVIEWS);
			tables.put(AppConstants.DB_CREATE_TABLE_PURCHASES_NAME, AppConstants.DB_CREATE_TABLE_PURCHASES);
			// import data?
			Boolean insert = true;

			System.out.println("Looking up existing tables");
			Set<String> existingTables = getDBTables(conn);

			System.out.println("Creating tables");
			for (Map.Entry<String, String> table : tables.entrySet()) {
				System.out.println("Attempting to create table ".concat(table.getKey()));
				if (existingTables.contains(table.getKey())) {
					System.out.println("- Table already exists, skipping & not importing data");
					insert = false;
					continue;
				}

				Statement stmt = conn.createStatement();
				stmt.executeUpdate(table.getValue());
				// commit update
				conn.commit();
				stmt.close();
			}

			// import users
			if (insert) {
				System.out.println("Importing data");
				ArrayList<Integer> ebookIds = new ArrayList<Integer>();
				ArrayList<Integer> userIds = new ArrayList<Integer>();
				Random randomGenerator = new Random();

				// populate users table with user data from json file
				Collection<User> users = loadUsers(cntx.getResourceAsStream(File.separator + AppConstants.USERS_FILE));

				System.out.println("Importing users");
				for (User user : users) {
					userIds.add(user.insert(conn));
				}

				// populate ebooks
				Collection<Ebook> ebooks = loadEbooks(
						cntx.getResourceAsStream(File.separator + AppConstants.EBOOKS_FILE));

				System.out.println("Importing ebooks");
				for (Ebook ebook : ebooks) {
					ebookIds.add(ebook.insert(conn));
				}

				// populate reviews
				ArrayList<String> reviews = loadReviews(
						cntx.getResourceAsStream(File.separator + AppConstants.REVIEWS_FILE));

				System.out.println("Importing reviews");
				for (Integer ebook : ebookIds) {
					System.out.println("Importing reviews for ebook ".concat(ebook.toString()));

					// new list for each ebook, same user can't review a book twice
					ArrayList<Integer> ebookUserIds = new ArrayList<Integer>();
					ebookUserIds.addAll(0, userIds);

					for (Integer i = 0; i < 10; i++) {
						Integer userId = ebookUserIds.get(randomGenerator.nextInt(ebookUserIds.size()));

						Review review = new Review();
						review.ebook_id = ebook;
						review.user_id = userId;
						review.content = reviews.get(randomGenerator.nextInt(reviews.size()));
						review.is_published = 1;
						review.insert(conn);

						// ensure same user doesn't review twice
						ebookUserIds.remove(userId);
					}
				}

				// populate likes
				System.out.println("Populating likes");
				for (Integer user : userIds) {
					System.out.println("Populating likes for user ".concat(user.toString()));
					for (Integer ebook : ebookIds) {
						if (randomGenerator.nextBoolean()) {
							Like like = new Like();
							like.user_id = user;
							like.ebook_id = ebook;
							like.insert(conn);
						}
					}
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

	private ArrayList<String> loadReviews(InputStream is) throws IOException {
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
		Type type = new TypeToken<ArrayList<String>>() {
		}.getType();
		ArrayList<String> reviews = gson.fromJson(jsonFileContent.toString(), type);
		// close
		br.close();
		return reviews;
	}

}
