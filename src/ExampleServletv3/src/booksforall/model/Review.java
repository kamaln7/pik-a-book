package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchReview;

public class Review {
	public String content, user_photo, user_nickname, ebook_name, user_username;
	public Integer user_id, ebook_id, is_published = 0;
	public Timestamp timestamp;

	public static Review find(Integer userId, Integer ebookId, Connection conn) throws SQLException, NoSuchReview {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_REVIEW_FIND);

		pstmt.setInt(1, userId);
		pstmt.setInt(2, ebookId);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchReview();
		}
		System.out.println("line 31 -find ");

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

	public static Collection<Review> RSToCollectionUser(ResultSet rs) throws SQLException {
		ArrayList<Review> reviews = new ArrayList<Review>();

		while (rs.next()) {
			Review review = new Review();
			review.user_id = rs.getInt("user_id");
			review.ebook_id = rs.getInt("ebook_id");
			review.content = rs.getString("content");
			review.is_published = rs.getInt("is_published");
			review.user_nickname = rs.getString("user_nickname");
			review.user_username = rs.getString("user_username");
			review.user_photo = rs.getString("user_photo");
			review.ebook_name = rs.getString("ebook_name");
			review.timestamp = rs.getTimestamp("timestamp");

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

	public static Collection<Review> getUnApprovedReviews(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_GET_UNAPROVED_REVIEWS);
		Collection<Review> reviews = Review.RSToCollectionUser(rs);
		stmt.close();

		return reviews;
	}

	public static void delete(Integer user_id, Integer ebook_id, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_REVIEW_DELETE);
		pstmt.setInt(1, user_id);
		pstmt.setInt(2, ebook_id);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

}
