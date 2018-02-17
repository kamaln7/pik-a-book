package example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import example.AppConstants;

public class Like {
	public Integer user_id, ebook_id;

	public Like() {
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
}
