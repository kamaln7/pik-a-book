package example;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import example.model.User;

/**
 * A simple place to hold global application constants
 */
public interface AppConstants {

	public final String CUSTOMERS = "customers";
	public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
	public final String NAME = "name";
	public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<User>>() {
	}.getType();
	// derby constants
	public final String DB_NAME = "ExampleDB";
	public final String DB_DATASOURCE = "ExampleDatasource";
	public final String PROTOCOL = "jdbc:derby:";
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";

	// create tables
	public final String DB_CREATE_TABLE_USERS = "CREATE TABLE users ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "username VARCHAR(10) NOT NULL," + "email VARCHAR(500) NOT NULL," + "password VARCHAR(8) NOT NULL,"
			+ "fullname VARCHAR(255) NOT NULL," + "street VARCHAR(255) NOT NULL," + "street_number INTEGER NOT NULL,"
			+ "city VARCHAR(255) NOT NULL," + "zip VARCHAR(7) NOT NULL," + "telephone VARCHAR(255) NOT NULL,"
			+ "nickname VARCHAR(20)," + "bio VARCHAR(50)," + "photo VARCHAR(1000),"
			+ "is_admin INTEGER DEFAULT 0 NOT NULL," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL" + ")";

	public final String DB_CREATE_TABLE_EBOOKS = "CREATE TABLE ebooks("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
			+ "name VARCHAR(500) NOT NULL," + "cover VARCHAR(500) NOT NULL," + "price INTEGER NOT NULL,"
			+ "content VARCHAR(20000) NOT NULL," + "description VARCHAR(50) NOT NULL,"
			+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL" + ")";

	public final String DB_CREATE_TABLE_LIKES = "CREATE TABLE likes (" + "user_id  INTEGER NOT NULL,"
			+ "ebook_id INTEGER NOT NULL," + "CONSTRAINT primary_key PRIMARY KEY (user_id,ebook_id)" + ")";

	public final String DB_CREATE_TABLE_REVIEWS = "CREATE TABLE reviews ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
			+ "user_id  INTEGER NOT NULL," + "ebook_id INTEGER NOT NULL," + "content VARCHAR(100) NOT NULL,"
			+ "is_published INTEGER DEFAULT 0 NOT NULL," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL"
			+ ")";
	public final String DB_CREATE_TABLE_PURCHASES = "CREATE TABLE purchases ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
			+ "user_id  INTEGER NOT NULL," + "ebook_id INTEGER NOT NULL,"
			+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL" + ")";

	// queries
	public final String DB_USER_CREATE = "INSERT INTO users (username, email, password, city, street, street_number, zip, telephone, nickname, bio, photo, is_admin, fullname) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public final String DB_USER_LOGIN = "SELECT id FROM users WHERE username = ? AND password = ?";
	public final String DB_USER_BYID = "SELECT * FROM users WHERE id = ?";

	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER " + "WHERE Name=?";
}
