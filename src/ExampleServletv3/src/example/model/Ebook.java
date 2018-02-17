package example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import example.AppConstants;
import example.exceptions.NoSuchEbook;

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

	public static Ebook find(Integer id, Connection conn) throws SQLException, NoSuchEbook {
		Ebook ebook = new Ebook();

		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_BYID);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchEbook();
		}

		ebook.id = rs.getInt("id");
		ebook.name = rs.getString("name");
		ebook.path = rs.getString("path");
		ebook.price = rs.getString("path");

		return ebook;
	}

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

	public static Collection<Ebook> RSToCollection(ResultSet rs) throws SQLException {
		ArrayList<Ebook> ebooks = new ArrayList<Ebook>();

		while (rs.next()) {
			Ebook ebook = new Ebook();
			ebook.id = rs.getInt("id");
			ebook.name = rs.getString("name");
			ebook.path = rs.getString("path");
			ebook.description = rs.getString("description");
			ebook.price = rs.getString("price");

			ebooks.add(ebook);
		}

		return ebooks;
	}

	public static Collection<Ebook> alphabetic(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_EBOOK_ALPHABETICAL);

		Collection<Ebook> ebooks = Ebook.RSToCollection(rs);
		stmt.close();

		return ebooks;
	}

	public static Collection<Ebook> latest(Integer limit, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.setMaxRows(limit);
		ResultSet rs = stmt.executeQuery(AppConstants.DB_EBOOK_LATEST);

		Collection<Ebook> ebooks = Ebook.RSToCollection(rs);
		stmt.close();

		return ebooks;
	}
}
