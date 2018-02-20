package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchUser;

/**
 * A simple bean to hold data
 */
public class User {

	public String username, email, password, fullname, street, city, zip, telephone, nickname, bio, photo, timestamp;
	public Integer id, street_number;
	public Boolean is_admin;

	public User() {
	}

	public User(String username, String email, String password, String fullname, String street, String city, String zip,
			String telephone, String nickname, String bio, String photo, Boolean is_admin, Integer street_number) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.street = street;
		this.street_number = street_number;
		this.city = city;
		this.zip = zip;
		this.telephone = telephone;
		this.nickname = nickname;
		this.bio = bio;
		this.photo = photo;
		this.is_admin = is_admin;
	}

	public static User find(Integer id, Connection conn) throws SQLException, NoSuchUser {
		User user = new User();

		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_USER_BYID);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchUser();
		}

		user.id = rs.getInt("id");
		user.username = rs.getString("username");
		user.email = rs.getString("email");
		user.password = rs.getString("password");
		user.fullname = rs.getString("fullname");
		user.street = rs.getString("street");
		user.street_number = rs.getInt("street_number");
		user.city = rs.getString("city");
		user.zip = rs.getString("zip");
		user.telephone = rs.getString("telephone");
		user.nickname = rs.getString("nickname");
		user.bio = rs.getString("bio");
		user.photo = rs.getString("photo");
		user.is_admin = (rs.getInt("is_admin") == 1) ? true : false;

		return user;
	}

	public Integer insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_USER_CREATE, Statement.RETURN_GENERATED_KEYS);

		pstmt.setString(1, this.username);
		pstmt.setString(2, this.email);
		pstmt.setString(3, this.password);
		pstmt.setString(4, this.city);
		pstmt.setString(5, this.street);
		pstmt.setInt(6, this.street_number);
		pstmt.setString(7, this.zip);
		pstmt.setString(8, this.telephone);
		pstmt.setString(9, this.nickname);
		pstmt.setString(10, this.bio);
		pstmt.setString(11, this.photo);
		pstmt.setInt(12, this.is_admin ? 1 : 0);
		pstmt.setString(13, this.fullname);

		pstmt.executeUpdate();

		ResultSet rs = pstmt.getGeneratedKeys();

		if (rs.next()) {
			this.id = rs.getInt(1);
		}

		// commit update
		conn.commit();
		// close statements
		pstmt.close();

		return this.id;
	}

	public static User login(String username, String password, Connection conn) throws SQLException, NoSuchUser {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_USER_LOGIN);
		pstmt.setString(1, username);
		pstmt.setString(2, password);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchUser();
		}

		User user = User.find(rs.getInt(1), conn);
		pstmt.close();

		return user;

	}

	public static Collection<User> RSToCollection(ResultSet rs) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();

		while (rs.next()) {
			User user = new User();
			user.id = rs.getInt("id");
			user.username = rs.getString("username");
			user.email = rs.getString("email");
			user.password = rs.getString("password");
			user.fullname = rs.getString("fullname");
			user.street = rs.getString("street");
			user.street_number = rs.getInt("street_number");
			user.city = rs.getString("city");
			user.zip = rs.getString("zip");
			user.telephone = rs.getString("telephone");
			user.nickname = rs.getString("nickname");
			user.bio = rs.getString("bio");
			user.photo = rs.getString("photo");
			user.is_admin = (rs.getInt("is_admin") == 1) ? true : false;

			users.add(user);
		}

		return users;
	}

	public static Collection<User> getAllUsers(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_GET_USERS);

		Collection<User> users = User.RSToCollection(rs);
		stmt.close();

		return users;
	}
}
