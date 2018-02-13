package example;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import example.model.Customer;

/**
 * A simple place to hold global application constants
 */
public interface AppConstants {

	public final String CUSTOMERS = "customers";
	public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
	public final String NAME = "name";
	public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>() {}.getType();
	//derby constants
	public final String DB_NAME = "ExampleDB";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";
	
	//sql statements
	public final String DB_CREATE_USERS_TABLE = "CREATE TABLE users("
			+ "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "username VARCHAR(10) NOT NULL,"
			+ "email VARCHAR(500) NOT NULL,"
			+ "password VARCHAR(8) NOT NULL,"
			+ "address_fullname VARCHAR(255) NOT NULL,"
			+ "address_street_name VARCHAR(255) NOT NULL,"
			+ "address_street_number VARCHAR(10) NOT NULL,"
			+ "address_city VARCHAR(255) NOT NULL,"
			+ "address_zip VARCHAR(7) NOT NULL,"
			+ "telephone VARCHAR(255) NOT NULL,"
			+ "nickname VARCHAR(20) NOT NULL,"
			+ "bio VARCHAR(50) NOT NULL,"
			+ "photo_url VARCHAR(1000) NOT NULL,"
			+ "is_admin INTEGER DEFAULT 0 NOT NULL,"
			+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
			+ ""
			+ "CONSTRAINT primary_key PRIMARY KEY (id)"
			+ ")";
	
	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Name=?";
}
