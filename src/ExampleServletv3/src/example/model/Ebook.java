package example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import example.AppConstants;

public class Ebook {
	public String name, path, description, price;
	public Integer id;

	public Ebook() {
	}

	public Ebook(String name, String path, String description, String price) {
		this.name = name;
		this.path = path;
		this.description = description;
		this.price = price;
	}

	// public static Ebook find(Integer id, Connection conn) throws SQLException,
	// NoSuchUser {
	// Ebook ebook = new Ebook();
	//
	// PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_BYID);
	// pstmt.setInt(1, id);
	//
	// ResultSet rs = pstmt.executeQuery();
	//
	// if (!rs.next()) {
	// throw new NoSuchEbook();
	// }
	//
	// ebook.id = rs.getInt("id");
	// user.username = rs.getString("username");
	// user.email = rs.getString("email");
	// user.password = rs.getString("password");
	// user.fullname = rs.getString("fullname");
	// user.street = rs.getString("street");
	// user.street_number = rs.getInt("street_number");
	// user.city = rs.getString("city");
	// user.zip = rs.getString("zip");
	// user.telephone = rs.getString("telephone");
	// user.nickname = rs.getString("nickname");
	// user.bio = rs.getString("bio");
	// user.photo = rs.getString("photo");
	// user.is_admin = (rs.getInt("is_admin") == 1) ? true : false;
	//
	// return user;
	// }

	public Integer insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_CREATE, Statement.RETURN_GENERATED_KEYS);

		pstmt.setString(1, this.name);
		pstmt.setString(2, this.path);
		pstmt.setString(3, this.description);
		pstmt.setString(4, this.price);

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
}
