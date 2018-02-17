package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import booksforall.AppConstants;

public class Review {
	public String content, user_photo, user_nickname;
	public Integer user_id, ebook_id, is_published = 0;

	public Review() {
	}

	public void insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_REVIEW_CREATE);

		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);
		pstmt.setString(3, this.content);
		pstmt.setInt(4, this.is_published);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}
}
