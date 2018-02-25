package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchReading;

public class Reading {
	public Integer user_id, ebook_id;
	public String user_nickname, position;

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

		Reading reading = new Reading();
		reading.user_id = rs.getInt("user_id");
		reading.ebook_id = rs.getInt("ebook_id");
		reading.position = rs.getString("position");

		return reading;
	}

	public void insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_READING_CREATE);

		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);
		pstmt.setString(3, this.position);

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

	public static Reading get(Integer user_id, Integer ebook_id, Connection conn) throws SQLException {
		Reading reading = null;
		try {
			reading = Reading.find(user_id, ebook_id, conn);
		} catch (NoSuchReading e) {
			reading = new Reading();
			reading.ebook_id = ebook_id;
			reading.user_id = user_id;
			reading.position = "0";
			reading.insert(conn);
		}

		return reading;
	}

	public void update(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_READING_UPDATE);

		pstmt.setString(1, this.position);
		pstmt.setInt(2, this.ebook_id);
		pstmt.setInt(3, this.user_id);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}
}
