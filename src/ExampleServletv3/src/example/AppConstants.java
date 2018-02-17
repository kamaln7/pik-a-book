package example;

/**
 * A simple place to hold global application constants
 */
public interface AppConstants {
	public final String EBOOKS_FILE = "books.json";
	public final String USERS_FILE = "users.json";
	// derby constants
	public final String DB_NAME = "ExampleDB";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:";
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";

	// create tables
	public final String DB_CREATE_TABLE_USERS = "CREATE TABLE users ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "username VARCHAR(10) NOT NULL," + "email VARCHAR(500) NOT NULL," + "password VARCHAR(8) NOT NULL,"
			+ "fullname VARCHAR(255) NOT NULL," + "street VARCHAR(255) NOT NULL," + "street_number INTEGER NOT NULL,"
			+ "city VARCHAR(255) NOT NULL," + "zip VARCHAR(7) NOT NULL," + "telephone VARCHAR(255) NOT NULL,"
			+ "nickname VARCHAR(20) NOT NULL," + "bio VARCHAR(50)," + "photo VARCHAR(1000),"
			+ "is_admin INTEGER DEFAULT 0 NOT NULL," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
			+ "unique(username)" + ")";

	public final String DB_CREATE_TABLE_EBOOKS = "CREATE TABLE ebooks("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
			+ "name VARCHAR(500) NOT NULL," + "path VARCHAR(2000) NOT NULL," + "price VARCHAR(5) NOT NULL,"
			+ "description VARCHAR(20000) NOT NULL," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL" + ")";

	public final String DB_CREATE_TABLE_LIKES = "CREATE TABLE likes (" + "user_id  INTEGER NOT NULL,"
			+ "ebook_id INTEGER NOT NULL," + "CONSTRAINT primary_key PRIMARY KEY (user_id,ebook_id),"
			+ "CONSTRAINT user_ref FOREIGN KEY (user_id) REFERENCES users(id),"
			+ "CONSTRAINT ebook_ref FOREIGN KEY (ebook_id) REFERENCES ebooks(id)" + ")";

	public final String DB_CREATE_TABLE_REVIEWS = "CREATE TABLE reviews ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
			+ "user_id  INTEGER NOT NULL," + "ebook_id INTEGER NOT NULL," + "content VARCHAR(100) NOT NULL,"
			+ "is_published INTEGER DEFAULT 0 NOT NULL," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
			+ "CONSTRAINT user_ref FOREIGN KEY (user_id) REFERENCES users(id),"
			+ "CONSTRAINT ebook_ref FOREIGN KEY (ebook_id) REFERENCES ebooks(id)" + ")";
	public final String DB_CREATE_TABLE_PURCHASES = "CREATE TABLE purchases ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
			+ "user_id  INTEGER NOT NULL," + "ebook_id INTEGER NOT NULL,"
			+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
			+ "CONSTRAINT user_ref FOREIGN KEY (user_id) REFERENCES users(id),"
			+ "CONSTRAINT ebook_ref FOREIGN KEY (ebook_id) REFERENCES ebooks(id)" + ")";

	// queries
	public final String DB_USER_CREATE = "INSERT INTO users (username, email, password, city, street, street_number, zip, telephone, nickname, bio, photo, is_admin, fullname) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public final String DB_USER_LOGIN = "SELECT id, nickname FROM users WHERE username = ? AND password = ?";
	public final String DB_USER_BYID = "SELECT * FROM users WHERE id = ?";

	public final String DB_EBOOK_CREATE = "INSERT INTO ebooks (name, path, description, price) VALUES (?, ?, ?, ?)";
	public final String DB_EBOOK_BYID = "SELECT * FROM ebooks WHERE id = ?";
	public final String DB_EBOOK_LATEST = "SELECT * FROM ebooks ORDER BY timestamp DESC";

	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER " + "WHERE Name=?";

	// settings
	public final Integer LATEST_EBOOKS_LIMIT = 5;
}
