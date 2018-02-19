package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchReading;

public class Reading {
	public Integer user_id, ebook_id;
	public String user_nickname;
	public String position;

	public Reading() {
	}

	public static Reading find(Integer user_id, Integer ebook_id, Connection conn) throws SQLException, NoSuchReading {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_READING_FIND);

		pstmt.setInt(1, ebook_id);
		pstmt.setInt(2, user_id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchReading();
		}

		Reading Reading = new Reading();
		Reading.user_id = rs.getInt("user_id");
		Reading.ebook_id = rs.getInt("ebook_id");

		return Reading;
	}

	public void insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_READING_CREATE);

		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

	public void delete(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_READING_DELETE);

		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}
}
