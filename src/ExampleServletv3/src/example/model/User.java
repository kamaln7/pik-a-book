package example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import example.AppConstants;

/**
 * A simple bean to hold data
 */
public class User {

	public String username, email, password, fullname, street, street_number, city, zip, telephone, nickname, bio,
			photo, timestamp;
	public Integer id, is_admin = 0;

	public User() {}
	
	public User(String username, String email, String password, String fullname, String street, String street_number,
			String city, String zip, String telephone, String nickname, String bio, String photo, Integer is_admin) {
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

	public static User find(Integer id) {
		User user = new User();

		// SQL id

		return user;
	}

	public Integer insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_USER_CREATE, Statement.RETURN_GENERATED_KEYS);

		pstmt.setString(1, this.username);
		pstmt.setString(2, this.email);
		pstmt.setString(3, this.password);
		pstmt.setString(4, this.city);
		pstmt.setString(5, this.street);
		pstmt.setString(6, this.street_number);
		pstmt.setString(7, this.zip);
		pstmt.setString(8, this.telephone);
		pstmt.setString(9, this.nickname);
		pstmt.setString(10, this.bio);
		pstmt.setString(11, this.photo);
		pstmt.setInt(12, this.is_admin);
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
		// close connection
		conn.close();

		return this.id;
	}
}