package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchLike;

public class Like {
	public Integer user_id, ebook_id;
	public String user_nickname, user_username;

	public Like() {
	}

	public static Like find(Integer user_id, Integer ebook_id, Connection conn) throws SQLException, NoSuchLike {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_LIKE_FIND);

		pstmt.setInt(1, ebook_id);
		pstmt.setInt(2, user_id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchLike();
		}

		Like like = new Like();
		like.user_id = rs.getInt("user_id");
		like.ebook_id = rs.getInt("ebook_id");

		return like;
	}

	public void insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_LIKE_CREATE);

		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

	public void delete(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_LIKE_DELETE);
		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}
}
