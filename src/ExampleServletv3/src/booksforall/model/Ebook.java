package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchEbook;
import booksforall.exceptions.NoSuchPurchase;

public class Ebook {
	public String name, path, description, price;
	public Integer id;
	public Collection<Like> likes;
	public Collection<Review> reviews;
	public Boolean has_purchased = false;

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
		ebook.price = rs.getString("price");
		ebook.description = rs.getString("description");

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

	public void getLikes(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_LIKE_BYEBOOKID);
		pstmt.setInt(1, this.id);

		ResultSet rs = pstmt.executeQuery();
		ArrayList<Like> likes = new ArrayList<Like>();
		while (rs.next()) {
			Like like = new Like();
			like.ebook_id = this.id;
			like.user_id = rs.getInt("user_id");
			like.user_nickname = rs.getString("user_nickname");
			like.user_username = rs.getString("user_username");

			likes.add(like);
		}

		pstmt.close();
		this.likes = likes;
	}

	public void getReviews(Connection conn, Boolean publishedOnly) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(
				publishedOnly ? AppConstants.DB_REVIEW_BYEBOOKID_PUBLISHEDONLY : AppConstants.DB_REVIEW_BYEBOOKID);
		pstmt.setInt(1, this.id);

		ResultSet rs = pstmt.executeQuery();
		ArrayList<Review> reviews = new ArrayList<Review>();
		while (rs.next()) {
			Review review = new Review();
			review.ebook_id = this.id;
			review.user_id = rs.getInt("user_id");
			review.content = rs.getString("content");
			review.is_published = rs.getInt("is_published");
			review.user_nickname = rs.getString("user_nickname");
			review.user_photo = rs.getString("user_photo");

			reviews.add(review);
		}

		pstmt.close();
		this.reviews = reviews;
	}

	public void checkPurchased(Integer user_id, Connection conn) throws SQLException {
		try {
			Purchase.find(user_id, this.id, conn);

			this.has_purchased = true;
		} catch (NoSuchPurchase e) {
			this.has_purchased = false;
		}
	}

	public static Collection<Ebook> ownedByUser(Integer user_id, Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_OWNEDBYUSER);
		pstmt.setInt(1, user_id);

		ResultSet rs = pstmt.executeQuery();
		Collection<Ebook> ebooks = Ebook.RSToCollection(rs);
		pstmt.close();

		for (Ebook ebook : ebooks) {
			ebook.has_purchased = true;
		}

		return ebooks;
	}
}
