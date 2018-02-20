package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchReview;

public class Review {
	public String content, user_photo, user_nickname;
	public Integer user_id, ebook_id, is_published = 0;

	public static Review find(Integer user_id, Integer ebook_id, Connection conn) throws SQLException, NoSuchReview {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_REVIEW_FIND);

		pstmt.setInt(1, ebook_id);
		pstmt.setInt(2, user_id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchReview();
		}

		Review review = new Review();
		review.user_id = rs.getInt("user_id");
		review.ebook_id = rs.getInt("ebook_id");
		review.content = rs.getString("content");
		review.is_published = rs.getInt("is_published");

		return review;
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

	public static Collection<Review> RSToCollection(ResultSet rs) throws SQLException {
		ArrayList<Review> reviews = new ArrayList<Review>();

		while (rs.next()) {
			Review review = new Review();
			review.user_id = rs.getInt("user_id");
			review.ebook_id = rs.getInt("ebook_id");
			review.content = rs.getString("content");
			review.is_published = rs.getInt("is_published");

			reviews.add(review);
		}

		return reviews;
	}

	public static Collection<Review> ownedByUser(Integer user_id, Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_REVIEW_OWNEDBYUSER);
		pstmt.setInt(1, user_id);

		ResultSet rs = pstmt.executeQuery();
		Collection<Review> reviews = Review.RSToCollection(rs);
		pstmt.close();

		return reviews;
	}

}
